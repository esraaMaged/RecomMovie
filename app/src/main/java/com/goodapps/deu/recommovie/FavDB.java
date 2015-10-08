package com.goodapps.deu.recommovie;
// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class FavDB {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:



    public static final String KEY_POPULARITY2="popularity2";
    public static final String KEY_RATE2= "rate2";
    public static final String KEY_TITLE2 = "title2";
    public static final String KEY_POSTER2 = "overview2";
    public static final String KEY_PLOT2 = "plot2";
    public static final String KEY_REALSEDATE2 = "releasedate2";
    public static final String KEY_MOVIEID2 = "movieId2";







    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_POPULARITY2 = 1;
    public static final int COL_RATE2 = 2;
    public static final int COL_TITLE2 = 3;
    public static final int COL_POSTER2 = 4;
    public static final int COL_PLOT2 = 5;
    public static final int COL_REALSEDATE2 = 6;
    public static final int COL_MOVIEID2 = 7;









    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_POPULARITY2, KEY_RATE2, KEY_TITLE2,KEY_POSTER2,KEY_PLOT2,KEY_REALSEDATE2,KEY_MOVIEID2};
//public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_POPULARITY, KEY_TITLE,KEY_POSTER,KEY_PLOT,KEY_REALSEDATE};


    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "FavriteDb";
    public static final String DATABASE_TABLE = "NewTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

			/*
			 * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_POPULARITY2 + " text not null, "
                    + KEY_RATE2 + " string not null, "
                    + KEY_TITLE2 + " string not null, "
                    + KEY_POSTER2 + " string not null, "
                    + KEY_PLOT2 + " string not null, "
                    + KEY_REALSEDATE2 + " string not null, "
                    + KEY_MOVIEID2 + " string not null"



                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public FavDB(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public FavDB open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

//----------------------------------------------NEWWWWWW--------------------------------------------------------------

    // Add a new set of values to the NEWWWWWWWWWWWWWWWWdatabase.
    public long NEWRowInsert(String popularity2, String rate2, String title2,String poster2,String plot2, String realsedate2, String movieId2) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_POPULARITY2, popularity2);
        initialValues.put(KEY_RATE2, rate2);
        initialValues.put(KEY_TITLE2, title2);
        initialValues.put(KEY_POSTER2, poster2);
        initialValues.put(KEY_PLOT2, plot2);
        initialValues.put(KEY_REALSEDATE2, realsedate2);
        initialValues.put(KEY_MOVIEID2, movieId2);
        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

//----------------------------------------------END OF NEWWWWWW--------------------------------------------------------------



    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }






//----------------------------------------------NEWWWWWW--------------------------------------------------------------


    // Change an existing row to be equal to new data.
    public boolean updateNEWRow(long rowId,String popularity2, String rate2, String title2,String poster2,String plot2, String realsedate2,String movieId2) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_POPULARITY2, popularity2);
        newValues.put(KEY_RATE2, rate2);
        newValues.put(KEY_TITLE2, title2);
        newValues.put(KEY_POSTER2, poster2);
        newValues.put(KEY_PLOT2, plot2);
        newValues.put(KEY_REALSEDATE2, realsedate2);
        newValues.put(KEY_MOVIEID2, movieId2);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

//----------------------------------------------END OF NEWWWWWW--------------------------------------------------------------




    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
