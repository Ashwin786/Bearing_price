package com.bearing_price;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.provider.Settings;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bearing_price.view.price.Price_Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Password_Activity extends AppCompatActivity {

    String dbpath, dbname = "BearingPrice_old_1.sqlite", login = "login";
    private SharedPreferences sp;
    private String copydatabase = "copydatabase";
    public String mi_id = "a654034904db8133"; //mi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(login, MODE_PRIVATE);
//        new AutoUpgradeCheck(this).check_version();
        if (checkIs_mymobile() && sp.getBoolean(copydatabase, false)) {
            finish();
            startActivity(new Intent(this, Price_Activity.class));
            return;
        }
        setContentView(R.layout.activity_password_);

        set_listener();
        if (!sp.getBoolean(copydatabase, false)) {
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                dbpath = getApplicationInfo().dataDir + "/databases/";
            } else {
                dbpath = "/data/data/" + getPackageName() + "/databases/";
            }

            try {
                copydatabase();
                sp.edit().putBoolean(copydatabase, true).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void set_listener() {
        ((EditText) findViewById(R.id.ed_pin)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e("total",s+","+start+","+before+","+count);
                if (s.length() == 4) {
                    if (s.toString().equals("8293")) {
                        finish();
                        startActivity(new Intent(Password_Activity.this, Price_Activity.class));
                    } else {
                        Toast toast = Toast.makeText(Password_Activity.this, "Wrong Pin", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void copydatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if (!dbexist) {
            InputStream myinput = getAssets().open(dbname);
            openOrCreateDatabase(dbname, MODE_PRIVATE, null);
            OutputStream myoutput = new FileOutputStream(dbpath + dbname);
            // transfer byte to inputfile to outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myinput.read(buffer)) > 0) {
                myoutput.write(buffer, 0, length);
            }
            myoutput.flush();
            myoutput.close();
            myinput.close();
        }
    }

    private boolean checkdatabase() {
        //SQLiteDatabase checkdb = null;
        boolean checkdb = false;
        try {
            File dbfile = new File(dbpath + dbname);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    public boolean checkIs_mymobile() {
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("android id", android_id);
        if (android_id.equals(mi_id)) {
            return true;
        }
        return false;
    }


}
