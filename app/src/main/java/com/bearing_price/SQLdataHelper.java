package com.bearing_price;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user1 on 28/8/18.
 */
public class SQLdataHelper {
    //for logging
    private final String TAG = this.getClass().getSimpleName();

    //DATABASE
    private static final String DATABASE_NAME = "my.db";
    private static final int DATABASE_VERSION = 1;//initial version

    //TABLE NAMES
    private static final String TABLE_NAME_A = "exampleOneTable";

    //MEMBER VARIABLES
    private DatabaseHelper mDBhelper;
    private SQLiteDatabase mDB;

    //SINGLETON
    private static final SQLdataHelper instance = new SQLdataHelper();


    private SQLdataHelper() {
        final DatabaseHelper dbHelper = new DatabaseHelper(ApplicationContext.getContext());

        //open the DB for read and write
        mDB = dbHelper.getWritableDatabase();
    }


    public static SQLdataHelper getInstance() {
        return instance;
    }

    /**
     * INSERT FUNCTIONS consisting of "synchronized" methods
     */
    public void insertTableA(String myName, int myAge) {
        for (int i = 0; i < 50; i++) {
            Long lValueToReturn;

            //organize the data to store as key/value pairs
            ContentValues kvPairs = new ContentValues();
            kvPairs.put("ColumnOne", myName);
            kvPairs.put("ColumnTwo", myAge);

            lValueToReturn = mDB.insert(TABLE_NAME_A, null, kvPairs);
        }
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        //this is called for first time db is created.
        // put all CREATE TABLE here
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "
                    + TABLE_NAME_A
                    + " ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "ColumnOne TEXT,"
                    + "ColumnTwo TEXT"
                    + ")");
        }

        //this is called when an existing user updates to a newer version of the app
        // add CREATE TABLE and ALTER TABLE here
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

}
