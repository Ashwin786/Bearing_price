package com.bearing_price;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user1 on 28/8/18.
 */
public class SqliteOpenHelper extends SQLiteOpenHelper {
    //for logging
    private final String TAG = this.getClass().getSimpleName();

    //DATABASE
    private static final String DATABASE_NAME = "my.db";

    //initial version
    private static final int DATABASE_VERSION = 1;

    //TABLE NAMES
    private static final String TABLE_NAME_A = "exampleOneTable";

    public SqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertTableA(String myName, int myAge) {
        Long lValueToReturn = null;

        //organize the data to store as key/value pairs
        ContentValues kvPairs = new ContentValues();
        kvPairs.put("ColumnOne", myName);
        kvPairs.put("ColumnTwo", myAge);

        try {
            lValueToReturn = getWritableDatabase().insert(TABLE_NAME_A, null, kvPairs);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Db exception",e.toString());
        }

        return lValueToReturn;
    }

}
