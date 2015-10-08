package com.goodapps.deu.recommovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {
    DBAdapter myDb;
    FavDB FavriteDb;

    public final String LOG_TAG = "TAAAAAA";
    int num=0;
    public int pageNum=12;
    String pageNumF="1";
    String Param="Pop" ;

    Cursor cursor;
    ArrayList<String> postersStrs = new ArrayList<String>();

    GridView gridView;
    ImageAdapter mADaptor;
    Button nextPage;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        openDB();
        updateMovie();
        postersStrs.clear();
        myDb.deleteAll();
    }
    //-------------------------------db opening and closing-----------------------------------------------
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(getActivity());
        myDb.open();

        FavriteDb=new FavDB(getActivity());
        FavriteDb.open();
    }

    private void closeDB() {
        myDb.close();
        FavriteDb.close();
    }
    //-------------------------------db opening and closing-----------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_P) {

            nextPage.setVisibility(View.VISIBLE);

            myDb.deleteAll();
            SingleMovie movieTask = new SingleMovie();
            pageNumF="1";
         //   Log.e("bhfgdfgdsf", "ghfgggggggggg" + pageNumF);

            movieTask.execute("1","2");
            return true;
        }
        if (id == R.id.action_sort_R) {

            nextPage.setVisibility(View.VISIBLE);

            myDb.deleteAll();
            pageNumF="1";
           // Log.e("bhfgdfgdsf", "ghfgggggggggg" + pageNumF);
            Param="Rate";
            SingleMovie movieTask = new SingleMovie();
           movieTask.execute("1");
            return true;
        }
        if (id == R.id.action_fav) {
            cursor = FavriteDb.getAllRows();
            postersStrs.clear();
            if (cursor.moveToFirst()) {
                do {
                    postersStrs.add(cursor.getString(FavriteDb.COL_POSTER2));
                      //  Log.e(LOG_TAG,"kkkkkkkkkkkkkkkkkkkkkGGGGGGGG"+cursor.getString(FavriteDb.COL_POSTER2));
                } while (cursor.moveToNext());
            }
            num=9999999;
            mADaptor=new ImageAdapter(getActivity());
            mADaptor.notifyDataSetChanged();
            gridView.setAdapter(mADaptor);
            nextPage.setVisibility(View.GONE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        nextPage=(Button) rootview.findViewById(R.id.nextPage_id);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pageNum=Integer.parseInt(pageNumF); pageNum++; pageNumF=Integer.toString(pageNum);
               // Log.e(LOG_TAG, "JSONNNNNNpaaaaggggeeeeIIIIIINNNNNN__New" + pageNumF);
                myDb.deleteAll();
                SingleMovie movieTask = new SingleMovie();
                if(Param.equals("Rate")){
                    movieTask.execute("1");
                }
                     else  movieTask.execute("1", "2");

            }
        });

        gridView = (GridView) rootview.findViewById(R.id.gridViewID);
        mADaptor=new ImageAdapter(getActivity());
        gridView.setAdapter(mADaptor);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int p = position + num;
                String poster = Integer.toString(p);
                Intent i = new Intent(getActivity(), DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, poster);
                startActivity(i);
            }
        });
        mADaptor.notifyDataSetChanged();
        return rootview;
    }

    public void updateMovie() {
        SingleMovie movieTask = new SingleMovie();
        movieTask.execute("1","2");
    }
    public class ImageAdapter extends BaseAdapter {
        LayoutInflater inflater;
        private Context context;
        int size;

        public ImageAdapter(Context c) {
            context = c;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            size=postersStrs.size();
       //     Log.e(LOG_TAG,"LLLLLLLLLLEEEEEEEEEENNNNNNN"+size);
        }
        public int getCount() {
            return size;
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageV;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.single_grid_item, null);
            }
            imageV = (ImageView) convertView.findViewById(R.id.imageview_single_grid_item);

            String poster = postersStrs.get(position);
            String thePoster="";
            thePoster="http://image.tmdb.org/t/p/w185/" + poster;
            Picasso.with(context).load(thePoster).into(imageV);
            return convertView;
        }
    }

    public class SingleMovie extends AsyncTask<String, Void, String[]> {
        public final String LOG_TAG = "AAAAAAAAAG";
        // -----------------------------------------------------------the names of the JSON objects that need to be extracted.
        final String PAGE="page";

        final String RESULTS = "results";

        final String POPULARITY = "popularity";
        final String RATE = "vote_average";
        final String TITLE = "original_title";
        final String POSTER = "poster_path";
        final String PLOT = "overview";
        //    final String USERRATING = "vote_average";
        final String REALSEDATE = "release_date";
        final String MOVIEID = "id";

        String page, popularity, rate, title, poster, plot, realseDate, movieId;
        String[] posterStrs;
        String[] resultStrs;

        @Override
        public String[] doInBackground(String... params) {
            // Log.v(LOG_TAG, "doInBackground 1");

            int numMovies = 100;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=[]");
                if (params.length == 1) {
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&page="+pageNumF+"&api_key=[]");
                } else
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page="+pageNumF+"&api_key=[]");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    //   Log.v(LOG_TAG, "inputStream == null 1" );
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();
                //     Log.v(LOG_TAG, "AAAAAAAAAAAAAAA" + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        //  Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            //---------------------------------------------JSON STUFF--------------------------------------------------------
            try {

                JSONObject movieStr = new JSONObject(movieJsonStr);
                JSONArray resultsArray = movieStr.getJSONArray(RESULTS);

                page=movieStr.getString(PAGE);
            //   Log.e(LOG_TAG, "JSONNNNNNpaaaaggggeeeeIIIIIINNNNNN" + page);

                posterStrs = new String[numMovies];
                resultStrs = new String[numMovies];

                for (int i = 0; i < resultsArray.length(); i++) {

                    JSONObject movieInfo = resultsArray.getJSONObject(i);
                    popularity = movieInfo.getString(POPULARITY);
                    rate = movieInfo.getString(RATE);
                    title = movieInfo.getString(TITLE);
                    poster = movieInfo.getString(POSTER);
                    plot = movieInfo.getString(PLOT);
                    realseDate = movieInfo.getString(REALSEDATE);
                    movieId = movieInfo.getString(MOVIEID);

                    posterStrs[i] = poster;
                    // Log.e(LOG_TAG, "JSONNNNNNpppppppppppppppppppppppppppppppppppppppppp" + posterStrs[i]);

                    resultStrs[i] = popularity + " " + rate + " " + title + " " + poster + " " + plot + " " + realseDate + " " + movieId;
                    long newId = myDb.insertRow(popularity, rate, title, poster, plot, realseDate, movieId);
                    Cursor cursor = myDb.getRow(newId);

//                    for (String s : posterStrs) {
//                    Log.e(LOG_TAG, "posters entry: " + s);
//            }
                    for (String s : resultStrs) {
                        Log.v(LOG_TAG, "movies entry: " + s);
                    }

                }
             //   Log.e(LOG_TAG,"NNEEEEEEEEEWWWWWWWWWWLLLLLLLLLLEEEEEEEEEENNNNNNN"+resultStrs.length);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //----------------------------------------END OF JSON STUFF--------------------------------------------------------

            return posterStrs;
        }
        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                cursor = myDb.getAllRows();
                postersStrs.clear();
                if (cursor.moveToFirst()) {

                    do {
                        postersStrs.add(cursor.getString(DBAdapter.COL_POSTER));
                        //    Log.e(LOG_TAG,"kkkkkkkkkkkkkkkkkkkkkGGGGGGGG"+cursor.getString(DBAdapter.COL_POSTER));
                    } while (cursor.moveToNext());
                }
                mADaptor=new ImageAdapter(getActivity());
                mADaptor.notifyDataSetChanged();
                gridView.setAdapter(mADaptor);
            }
        }
    }
}
