package com.bearing_price;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText login_id;
    private EditText mPasswordView;
    String dbpath, dbname = "BearingPrice.sqlite", login = "login";
    private SharedPreferences sp;
    private String copydatabase = "copydatabase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(login, MODE_PRIVATE);
        if (!sp.getBoolean(login, false)) {
            setContentView(R.layout.activity_login);
            login_id = (EditText) findViewById(R.id.login_id);
            mPasswordView = (EditText) findViewById(R.id.password);
            Button loginbtn = (Button) findViewById(R.id.email_sign_in_button);
            loginbtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                dbpath = getApplicationInfo().dataDir + "/databases/";
            } else {
                dbpath = "/data/data/" + getPackageName() + "/databases/";
            }
            if (!sp.getBoolean(copydatabase, false)) {
                try {
                    copydatabase();
                    sp.edit().putBoolean(copydatabase, true).commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            finish();
            startActivity(new Intent(this, Price_Activity.class));
        }

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        login_id.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String id = login_id.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(id)) {
            login_id.setError(getString(R.string.error_field_required));
            focusView = login_id;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else if (id.equals("admin") && password.equals("admin123")) {
            sp.edit().putBoolean(login, true).commit();
            finish();
            startActivity(new Intent(this, Price_Activity.class));
        } else {
            Toast toast = Toast.makeText(LoginActivity.this, "Login Id or Password is Incorrect", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

}

