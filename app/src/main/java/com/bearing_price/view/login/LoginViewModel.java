package com.bearing_price.view.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bearing_price.data.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginViewModel extends AndroidViewModel {
    private final Application application;
    private final SharedPreferences sp;
    private LoginNavigator mNavigator;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        sp = application.getSharedPreferences(login, application.MODE_PRIVATE);
    }


    public void attemptLogin() {

        getNavigator().loginValidate();

    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     *
     * @param id
     * @param password
     */
    protected void validateLogin(String id, String password) {
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            getNavigator().setError(1);
            return;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(id)) {
            getNavigator().setError(0);
            return;
        }

        if (id.equals("admin") && password.equals("admin123")) {
            sp.edit().putBoolean(login, true).commit();
            getNavigator().loginSuccess();
        } else {
            getNavigator().loginFailed();
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    String login = "login";

    protected void copydatabase() {
        if (sp.getBoolean("copydatabase", false)) {
            return;
        }
        String dbpath;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            dbpath = application.getApplicationInfo().dataDir + "/databases/";
        } else {
            dbpath = "/data/data/" + application.getPackageName() + "/databases/";
        }
        boolean dbexist = checkdatabase(dbpath);
        if (!dbexist) {
            try {
                InputStream myinput = application.getAssets().open(Database.DB_NAME);
                application.openOrCreateDatabase(Database.DB_NAME, application.MODE_PRIVATE, null);
                OutputStream myoutput = null;

                myoutput = new FileOutputStream(dbpath + Database.DB_NAME);
                // transfer byte to inputfile to outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myinput.read(buffer)) > 0) {
                    myoutput.write(buffer, 0, length);
                }
                myoutput.flush();
                myoutput.close();
                myinput.close();
                sp.edit().putBoolean("copydatabase", true).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private boolean checkdatabase(String dbpath) {
        //SQLiteDatabase checkdb = null;
        boolean checkdb = false;
        try {
            File dbfile = new File(dbpath + Database.DB_NAME);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    public LoginNavigator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(LoginNavigator navigator) {
        this.mNavigator = navigator;
    }

    public void isAlreadyLoggedIn() {
        if (sp.getBoolean(login, false)) 
            getNavigator().loginSuccess();
        else
            getNavigator().initView();
    }
}
