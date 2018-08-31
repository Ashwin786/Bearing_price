package com.bearing_price;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by user1 on 28/8/18.
 */
public class TestDb {
    SQLiteDatabase sql;
    Cursor cursor;
    Context cont;
    private static final String DATABASE_NAME = "my.db";
    private static final String TABLE_NAME_A = "exampleOneTable";

    public TestDb(Context cont) {
        this.cont = cont;
        /*createoropendb();
        try {
            sql.execSQL("drop TABLE exampleOneTable");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME_A
                + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "ColumnOne TEXT,"
                + "ColumnTwo TEXT"
                + ")");
        closedb();*/
    }

    public void createoropendb() {
        sql = cont.openOrCreateDatabase(DATABASE_NAME, cont.MODE_PRIVATE, null);
    }

    public  void insertTableA(String myName, int myAge) {

//        synchronized("dblock") {
            createoropendb();
            String query = "insert into exampleOneTable(ColumnOne,ColumnTwo) values('" + myName + "','" + myAge + "')";

            for (int i = 0; i < 100; i++) {
                Log.d("Query", i + " " + query);
                sql.execSQL(query);
            }
            closedb();
//        }
    }

    public void delete() {
        createoropendb();
        sql.execSQL("delete from exampleOneTable");
        closedb();
    }

    public void closedb() {
        sql.close();
    }
}
