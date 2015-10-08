package com.goodapps.deu.recommovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


public class DetailsActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail, new DetailsActivityFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    public static class DetailsActivityFragment extends Fragment {
        private Context context;

        String Link;

        DBAdapter myDb;
        ArrayList<String> popularityStrs = new ArrayList<String>();
        ArrayList<String> rateStrs = new ArrayList<String>();
        ArrayList<String> titleStrs = new ArrayList<String>();
        ArrayList<String> postersStrs = new ArrayList<String>();
        ArrayList<String> plotStrs = new ArrayList<String>();
        ArrayList<String> releaseDateStrs = new ArrayList<String>();
        ArrayList<String> movieIdStrs = new ArrayList<String>();
        String id, popularity, rate, title, poster, plot, releaseDate, movieId;

        //---------------------------------newDB---------------------------
        FavDB FavriteDb;
        ArrayList<String> popularityStrs2 = new ArrayList<String>();
        ArrayList<String> rateStrs2 = new ArrayList<String>();
        ArrayList<String> titleStrs2 = new ArrayList<String>();
        ArrayList<String> postersStrs2 = new ArrayList<String>();
        ArrayList<String> plotStrs2 = new ArrayList<String>();
        ArrayList<String> releaseDateStrs2 = new ArrayList<String>();
        ArrayList<String> movieIdStrs2 = new ArrayList<String>();
        String id2,popularity2, rate2, title2, poster2, plot2, releaseDate2, movieId2;
        //-------------------------------------------------------------------------

        Button trailerBTN; TextView reviewTV;


        public String l="hhhh";


        public DetailsActivityFragment() {

            Thread timer = new Thread(){
                public void run(){
                    try{
                        sleep(5000);
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    } finally{
                        setHasOptionsMenu(true);
                    }
                }
            };
            timer.start();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            //-----intiating db------
            openDB();
            Cursor cursor = myDb.getAllRows();

            //-----------end ------------
        }


        //----------------------------------------------------------------------------db opening and closing-----------------------------------------------
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
        //-----------------------------------------------------------------end of db opening and closing---------------------------------------------------------


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);
            Intent intent = getActivity().getIntent();
              if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            id = intent.getStringExtra(Intent.EXTRA_TEXT);}
            int ID = Integer.parseInt(id);

            Cursor cursor = myDb.getAllRows();
            Cursor cursor2=FavriteDb.getAllRows();

            if(ID>9999998){
                do {
                    popularityStrs2.add(cursor2.getString(FavriteDb.COL_POPULARITY2));
                    rateStrs2.add(cursor2.getString(FavriteDb.COL_RATE2));
                    titleStrs2.add(cursor2.getString(FavriteDb.COL_TITLE2));
                    postersStrs2.add(cursor2.getString(FavriteDb.COL_POSTER2));
                    plotStrs2.add(cursor2.getString(FavriteDb.COL_PLOT2));
                    releaseDateStrs2.add(cursor2.getString(FavriteDb.COL_REALSEDATE2));
                    movieIdStrs2.add(cursor2.getString(FavriteDb.COL_MOVIEID2));
                } while (cursor2.moveToNext());
                int i=ID-9999999;
                popularity = popularityStrs2.get(i);
                rate = rateStrs2.get(i);
                title = titleStrs2.get(i);
                poster = postersStrs2.get(i);
                plot = plotStrs2.get(i);
                releaseDate = releaseDateStrs2.get(i);
                movieId = movieIdStrs2.get(i);
            }
            else{
                do{
                    popularityStrs.add(cursor.getString(DBAdapter.COL_POPULARITY));
                    rateStrs.add(cursor.getString(DBAdapter.COL_RATE));
                    titleStrs.add(cursor.getString(DBAdapter.COL_TITLE));
                    postersStrs.add(cursor.getString(DBAdapter.COL_POSTER));
                    plotStrs.add(cursor.getString(DBAdapter.COL_PLOT));
                    releaseDateStrs.add(cursor.getString(DBAdapter.COL_REALSEDATE));
                    movieIdStrs.add(cursor.getString(DBAdapter.COL_MOVIEID));
                }while (cursor.moveToNext());
            popularity = popularityStrs.get(ID);
            rate = rateStrs.get(ID);
            title = titleStrs.get(ID);
            poster = postersStrs.get(ID);
            plot = plotStrs.get(ID);
            releaseDate = releaseDateStrs.get(ID);
            movieId = movieIdStrs.get(ID);
            }
       //     Log.v("hjkgjhhgf", "UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU" + movieId);
            Trailers trailers = new Trailers();
            trailers.execute(movieId);
            Reviews reviews= new Reviews();
            reviews.execute(movieId);
            //---------------------------------------------------adding the variables to the UI-----------------------------------------------------------------------------------
            ((TextView) rootView.findViewById(R.id.title_id)).setText(title);
            ImageView poster_I_V = (ImageView) rootView.findViewById(R.id.poster_id);
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + poster).into(poster_I_V);
            ((TextView) rootView.findViewById(R.id.release_date_id)).setText(releaseDate);
            ((TextView) rootView.findViewById(R.id.rate_id)).setText(rate + "/10");
            ((TextView) rootView.findViewById(R.id.plot_id)).setText(plot);
            //----------------------------------------------------end of adding the variables to the UI-----------------------------------------------------------------------------------
            reviewTV=(TextView)rootView.findViewById(R.id.reviews_id);
            trailerBTN= (Button) rootView.findViewById(R.id.trailer_id);
            l=trailerBTN.getText().toString();
            trailerBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l = trailerBTN.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + l ));
                    startActivity(intent);
                //    Log.e("hjkgjhhgfffffffffffffffffffffffffffff", "wwwgggggeeeeeeeetttttttttg" + l);
                }
            });

            Button markFav = (Button) rootView.findViewById(R.id.markAsFav_id);
            markFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long newId = FavriteDb.NEWRowInsert(popularity, rate, title, poster, plot, releaseDate, movieId);
                    Cursor cursor2 = myDb.getRow(newId);

                  String  message =
                          popularity
                                + " " + rate
                                + ", " + title
                                + " " + poster + " " + plot + " " + " " + releaseDate +" " +movieId+"\n";
                 //   Log.v("hjkgjhhgfffffffffffffffffffffffffffff", "wwwgggggeeeeeeeetttttttttg" + message);
                }
            });
           // Log.e("hjkgjhhgfffffffffffffffffffffffffffff", "wwwgggggeeeeeeeetttttttttg" + Link);
            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.share_detail_frag, menu);
           // Log.e("hjkgjhhgfffffffffffffffffffffffffffff", "oooonnnnnncreaaaaatttteeee" + Link);
          final  MenuItem menuItem = menu.findItem(R.id.action_share);
                ShareActionProvider mShareActionProvider =
                        (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareForecastIntent());
                } else {
                    Log.d("Error", "Share Action Provider is null?");
                }
        }
        private Intent createShareForecastIntent() {
          //  Log.e("hjkgjhhgfffffffffffffffffffffffffffff", "22nnnndddddddoooonnnnnncreaaaaatttteeee" + Link);
            Trailers trailers = new Trailers();
            trailers.execute(movieId);
         //   Log.e("hjkgjhhgfffffffffffffffffffffffffffff", "33333rrrrrrdddddddoooonnnnnncreaaaaatttteeee" + Link);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            String finalLinK= "https://www.youtube.com/watch?v="+Link;
            if(finalLinK.equals("https://www.youtube.com/watch?v=null"))
            {
               // Toast.makeText(getActivity(),"no Trailer to share",Toast.LENGTH_LONG);
                return null;
            }
            else
                shareIntent.putExtra(Intent.EXTRA_TEXT, finalLinK );
                return shareIntent;
        }
        public class Trailers extends AsyncTask<String, Void, String[]> {
            public final String LOG_TAG = "AAAAAAAAAG";
            // -----------------------------------------------------------the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String KEY = "key";
            String key;
            String[] resultStrs;
            @Override
            public String[] doInBackground(String... params) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String movieJsonStr = null;
                try {
                    String u = params[0];
                  //  Log.v(LOG_TAG, "UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU" + u);
                    URL url = new URL("http://api.themoviedb.org/3/movie/" + u + "/videos?api_key=[]");
                  //  Log.v(LOG_TAG, "UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU" + url);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
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
                   // Log.v(LOG_TAG, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + movieJsonStr);

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
                    resultStrs = new String[1];
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject movieInfo = resultsArray.getJSONObject(i);
                        key = movieInfo.getString(KEY);
                        resultStrs[i] = key;
//                        for (String s : resultStrs) {
//                            Log.v(LOG_TAG, "movies entry: " + s);
//                        }
//                        Log.v(LOG_TAG, "movies entry:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " + key);
                        return resultStrs;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //----------------------------------------END OF JSON STUFF--------------------------------------------------------
                return null;
            }

            @Override
            protected void onPostExecute(String[] strings) {
                if (strings != null) {
                    trailerBTN.setText(strings[0]);
                    Link= strings[0];
                   // Log.e("hjkgjhhgfffffffffffffffffffffffffffff", "ooooooooooollllllllllllldddddddddd" + Link);
                }
                else{
                    trailerBTN.setText("NO TRAILER FOUND");
                    trailerBTN.setTextColor(getActivity().getResources().getColor(R.color.background_floating_material_light));
                }
            }
        }
            public class Reviews extends AsyncTask<String, Void, String[]> {
                public final String LOG_TAG = "AAAAAAAAAG";
                // -----------------------------------------------------------the names of the JSON objects that need to be extracted.
                final String RESULTStrs = "results";
                final String CONTENT = "content";
                String content;
                String[] contentResultStrs;
                @Override
                public String[] doInBackground(String... params) {
                    // Log.v(LOG_TAG, "doInBackground 1");
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    String movieJsonStr = null;
                    try {
                        String u = params[0];
                        //Log.v(LOG_TAG, "UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU" + u);
                        URL url = new URL("http://api.themoviedb.org/3/movie/" + u + "/reviews?api_key=[]");
                       // Log.v(LOG_TAG, "UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU" + url);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();
                        InputStream inputStream = urlConnection.getInputStream();
                        StringBuffer buffer = new StringBuffer();
                        if (inputStream == null) {
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
                    //    Log.v(LOG_TAG, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + movieJsonStr);

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
                        JSONArray resultsArray = movieStr.getJSONArray(RESULTStrs);
                        contentResultStrs = new String[1];
                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject movieInfo = resultsArray.getJSONObject(i);
                            content = movieInfo.getString(CONTENT);
                            contentResultStrs[i] = content;
//                            for (String s : contentResultStrs) {
//                                Log.v(LOG_TAG, "movies entry: " + s);
//                            }
//                            Log.v(LOG_TAG, "movies entry:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " + content);
                            return contentResultStrs;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //----------------------------------------END OF JSON STUFF--------------------------------------------------------

                    return null;
                }

                @Override
                protected void onPostExecute(String[] strings) {
                    if (strings != null) {

                        reviewTV.setText(strings[0]);
                    }
                    else
                    {
                        reviewTV.setText("NO REVIEWS FOUND");
                    }
                }
            }
    }
}





