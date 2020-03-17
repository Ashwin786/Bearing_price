package com.bearing_price.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.bearing_price.data.model.Price_dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 18/5/17.
 */
public class Database {
    public static final String DB_NAME = "BearingPrice.sqlite";
    SQLiteDatabase sql;
    Cursor cursor;
    Context cont;

    public Database(Context context) {
        // TODO Auto-generated constructor stub
        cont = context;
    }

    public void createoropendb() {
        sql = cont.openOrCreateDatabase(DB_NAME, cont.MODE_PRIVATE, null);
    }

    public List<Price_dto> get_search_products(String text) {
        if (!TextUtils.isEmpty(text))
            text = "where products like '%" + text + "%'";
        createoropendb();
        String retreivequery = "SELECT * FROM BearingPrice " + text + " order by Products Asc";
        List<Price_dto> pricelist = null;
        cursor = sql.rawQuery(retreivequery, null);
        Log.e("cursor count", "" + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            pricelist = new ArrayList<>();
            do {
                String products = cursor.getString(cursor.getColumnIndex("Products"));
                String brand = cursor.getString(cursor.getColumnIndex("Brand"));
                int price = cursor.getInt(cursor.getColumnIndex("Price"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
//                Log.e("id", "" + id);
                Price_dto dto = new Price_dto();
                dto.setPrice(price);
                dto.setProduct(products);
                dto.setId(id);
                dto.setBrand(brand);
                pricelist.add(dto);
            } while (cursor.moveToNext());
            cursor.close();
            closedb();
            return pricelist;
        }
        closedb();
        return null;
    }

    public List<Price_dto> get_recent_search_products() {
        createoropendb();
        String retreivequery = "SELECT * FROM BearingPrice where Modified_Date is not null order by Modified_Date Desc";
        List<Price_dto> pricelist = null;
        cursor = sql.rawQuery(retreivequery, null);
        Log.e("cursor count", "" + cursor.getCount());
        pricelist = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String products = cursor.getString(cursor.getColumnIndex("Products"));
                int price = cursor.getInt(cursor.getColumnIndex("Price"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
//                Log.e("id", "" + id);
                Price_dto dto = new Price_dto();
                dto.setPrice(price);
                dto.setProduct(products);
                pricelist.add(dto);
            } while (cursor.moveToNext());
            cursor.close();
        }
        closedb();
        return pricelist;
    }

    public void closedb() {
        sql.close();
    }


    public void update_recent(int id) {
        createoropendb();
        sql.execSQL("update BearingPrice set Modified_Date = datetime()  where id = " + id);
        closedb();
    }
}
