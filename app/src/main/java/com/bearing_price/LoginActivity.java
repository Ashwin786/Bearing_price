package com.bearing_price;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Encoder;

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
        if (1 == 1) {
            _encrypt();
            return;
        }
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

    private void _encrypt() {
//        String strValue = "51|90:09:17:30:F3:F5|192.168.2.98|1045036111751007379807092018113933.039|10450361|117510073798|175|20191230|BB8Ja2VhASlE/OG23PJZ9rO34cEqGYuDiG8Pm9uXRZfx0bdbco90I258RlpxCaoyZiMkljkwiWon+XlBIZgkFPYE4haN/aEvgD2KrLkqFuyREOPlrtltKDXn6wxDjp5NDPcO517zHL6zXNDHeSwrwqbo/5Pqb5fTtSx1Go9w+yHguY++qqbCZNmQ6pvzRnMBgoVqYmwqDvlly1Tyxk3RljtxVLW5vBSZOasxPey6x9n8yHnvxGq4hKD+POPgOZXuCnLW5BPUhQnfMcEFPXciG34ao/oA9ryb8Ii6kRq0g/wN4lR7pMaZa2oR0EP0IizPSgrgnG34bNYo3F1zGBVnfQ==|MjAxOC0wOS0wN1QxMTozOToxMds8r9VZPzenJMVFQpT/N21pepwd3uwCZNPHpIwb8vpnsnbf0zjOz8riIVxzTxSEIVzSDbteUEt8DMlCZujiQn6dqsLK8u8nIYEkVoVtnMgWoaEH0ooCY5ElPo27Un1pf64mB/PL4bypZdwyQOYGp2/pH8+mCRfgD+BVDAgvHeNAgjd2RxitODHht9FnQ6qXyqNpjLbHeNzenxp/2IlnBD8Mn2F/YyZjQTAt3lq8zfMVBWYsz66C351ObUJm8l5WZ66YZGodtmtRtcRCMafLjHkGxVS1wn8+TfuippQpWA86HXTnOm1D9A185WUhf+2p7AmKKNJnz4viZiLKhk51xDdFeGDVPKWI9Y9NnKDq/8t+WqxH0Gx4f7ouhfu2vNegg2a9q8FHo0Ghp+Cx03SPJjqNH+It1IAIRdJ/WwX+0rWWhbiJiuoqaeL8rrEgCjvthRKEtkFonbRIB8OfVJ9Rls5iFPE324HKiP3q2Lx09qPgS9gplL8EsYqFbuWYhCAa22v+Jj8TtzXufCjUqiBZ0iiJmOH4+R5Oi6cUP/33hjOwE1LlvQooZA0i2DXXNllr31pc2P3FDPW42RZf/MRErV3wEklWIIDyJeTP2uDfJCowMUQsZAazG5nSlh48/MMbS7XFTKhyBcM1I/oWoUfjaFYt4dJCv0Dv9HBvDNHfRlETnnZOIRi5RfI818SBJ/nX5W0HQPJchbuP0jF9lOJ0SGxyYjCSU/KvALgLYaJnsG+GBcKkxb5sJPUY9yNAP+LO5BHIhc0BvVE+0z38UcLcxHR3zZCPZeeKULjxZ/Rhf+cfidY5T3OnwZ6QiCwaeIsVkCM2Yj1NmRMmvm4sS0J8WVeMrdFlQCaxPwS8DSQq3CBViQKtAbhdhzq4K+OwMM0fr+m9wUQWCK9vPwCfgXz3wMznFDyZw67U//U65WW394xDIfbkYkVAoJ/9ag==|wlJLIP+oZ/BZmQIwr6+KsL9Jj6Epj3qrBHzaka/+9ryPfYeSfpmrpvnhxglLeiz1|MANTRA.AND.001|9.0.0|MANTRA.MSIPL|a56c6a8b-7ebd-4518-80d3-534cb94b2e82|MFS100|MIIEFDCCAvygAwIBAgIGAWUioGecMA0GCSqGSIb3DQEBCwUAMIHkMSowKAYDVQQDEyFEUyBNYW50cmEgU29mdGVjaCBJbmRpYSBQdnQgTHRkIDIxPTA7BgNVBDMTNEIgMjAzIDJuZCBmbG9vciBTaGFtcGF0aCBIZXhhIE5ldyBHdWphcmF0IEhpZ2ggQ291cnQxEjAQBgNVBAkTCUFobWVkYWJhZDEQMA4GA1UECBMHR3VqYXJhdDEdMBsGA1UECxMUVGVjaG5pY2FsIERlcGFydG1lbnQxJTAjBgNVBAoTHE1hbnRyYSBTb2Z0ZWNoIEluZGlhIFB2dCBMdGQxCzAJBgNVBAYTAklOMB4XDTE4MDkwMzA5MjYyMVoXDTE4MDkwOTA2NTc0MFowgbAxJTAjBgNVBAMTHE1hbnRyYSBTb2Z0ZWNoIEluZGlhIFB2dCBMdGQxHjAcBgNVBAsTFUJpb21ldHJpYyBNYW51ZmFjdHVyZTEOMAwGA1UEChMFTVNJUEwxEjAQBgNVBAcTCUFITUVEQUJBRDEQMA4GA1UECBMHR1VKQVJBVDELMAkGA1UEBhMCSU4xJDAiBgkqhkiG9w0BCQEWFXN1cHBvcnRAbWFudHJhdGVjLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALh+hnpfPkWXbVG+JfynvT9UQFM7zzg5ppF+t1vHjJRMtq2ugcnuXgTBhyt2EM1ATliGErwtPUsv3/3RorbpH1Xocx+NjO8X0B0KGhJ8oZ1L+rgCC/4gChM2K3dGCkP2H4g1uXmf+qwxKBuYVHv/34KFjMf9bhy5ftrG0PR+FnVTw2HRwZykX63Ktdel3AwEPL1h8QQIq8gW91lTKYuQk4mHfb30xBV+p61Y4fsJSZ56jW34vkKwtuKoTe6WcdcZJXextcI0tnG80/kO0VY+XlqR9XyTzXkPmot4B2FjjbmrfKDU/9khoVrq21cwIETuEiTyVoDIfkKjeziv6TbTWiECAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAsa/iz+zlUFQYqHF8dJY6SdesgZEP0M8ncDVvJ6NN1V9v8BqWLuJmxOGO75MooVWySawtmukfW9Hk7Icr8o+DrqbzkVmqoS9gAb06AY2kKl+oUHr8HPVMiSWcHCqXFfF3zLxHDBP8Vq+mO7hahOYJqkPgNEODbHRYdxUi75EkZulOzl+iaaVnfJisFQHHMwCA8FBUJiUxTWuED4uocA4h9dfTMoGmEm+uQy0eahet6MPIV+n++S1iPa7Jy2q/f1LH5PJ/brOAjYRghL/KEqUO/32QyEOCvOFs2g7iIxTX6UgdENXm6vdKRu0xufP1x+CvdF0MxWiHIxzUmxVGuitlgw==|Y|0946653|09/07/201811:39:34|checksum=9840bd1795ba3592512cbe05092f3393#459264917163a4d4bccc09201271460c";
        String sb = "51|90:09:17:30:F3:F5|192.168.2.98|1045036111751007379807092018113933.039|10450361|117510073798|175|20191230|BB8Ja2VhASlE/OG23PJZ9rO34cEqGYuDiG8Pm9uXRZfx0bdbco90I258RlpxCaoyZiMkljkwiWon+XlBIZgkFPYE4haN/aEvgD2KrLkqFuyREOPlrtltKDXn6wxDjp5NDPcO517zHL6zXNDHeSwrwqbo/5Pqb5fTtSx1Go9w+yHguY++qqbCZNmQ6pvzRnMBgoVqYmwqDvlly1Tyxk3RljtxVLW5vBSZOasxPey6x9n8yHnvxGq4hKD+POPgOZXuCnLW5BPUhQnfMcEFPXciG34ao/oA9ryb8Ii6kRq0g/wN4lR7pMaZa2oR0EP0IizPSgrgnG34bNYo3F1zGBVnfQ==|MjAxOC0wOS0wN1QxMTozOToxMds8r9VZPzenJMVFQpT/N21pepwd3uwCZNPHpIwb8vpnsnbf0zjOz8riIVxzTxSEIVzSDbteUEt8DMlCZujiQn6dqsLK8u8nIYEkVoVtnMgWoaEH0ooCY5ElPo27Un1pf64mB/PL4bypZdwyQOYGp2/pH8+mCRfgD+BVDAgvHeNAgjd2RxitODHht9FnQ6qXyqNpjLbHeNzenxp/2IlnBD8Mn2F/YyZjQTAt3lq8zfMVBWYsz66C351ObUJm8l5WZ66YZGodtmtRtcRCMafLjHkGxVS1wn8+TfuippQpWA86HXTnOm1D9A185WUhf+2p7AmKKNJnz4viZiLKhk51xDdFeGDVPKWI9Y9NnKDq/8t+WqxH0Gx4f7ouhfu2vNegg2a9q8FHo0Ghp+Cx03SPJjqNH+It1IAIRdJ/WwX+0rWWhbiJiuoqaeL8rrEgCjvthRKEtkFonbRIB8OfVJ9Rls5iFPE324HKiP3q2Lx09qPgS9gplL8EsYqFbuWYhCAa22v+Jj8TtzXufCjUqiBZ0iiJmOH4+R5Oi6cUP/33hjOwE1LlvQooZA0i2DXXNllr31pc2P3FDPW42RZf/MRErV3wEklWIIDyJeTP2uDfJCowMUQsZAazG5nSlh48/MMbS7XFTKhyBcM1I/oWoUfjaFYt4dJCv0Dv9HBvDNHfRlETnnZOIRi5RfI818SBJ/nX5W0HQPJchbuP0jF9lOJ0SGxyYjCSU/KvALgLYaJnsG+GBcKkxb5sJPUY9yNAP+LO5BHIhc0BvVE+0z38UcLcxHR3zZCPZeeKULjxZ/Rhf+cfidY5T3OnwZ6QiCwaeIsVkCM2Yj1NmRMmvm4sS0J8WVeMrdFlQCaxPwS8DSQq3CBViQKtAbhdhzq4K+OwMM0fr+m9wUQWCK9vPwCfgXz3wMznFDyZw67U//U65WW394xDIfbkYkVAoJ/9ag==|wlJLIP+oZ/BZmQIwr6+KsL9Jj6Epj3qrBHzaka/+9ryPfYeSfpmrpvnhxglLeiz1|MANTRA.AND.001|9.0.0|MANTRA.MSIPL|a56c6a8b-7ebd-4518-80d3-534cb94b2e82|MFS100|MIIEFDCCAvygAwIBAgIGAWUioGecMA0GCSqGSIb3DQEBCwUAMIHkMSowKAYDVQQDEyFEUyBNYW50cmEgU29mdGVjaCBJbmRpYSBQdnQgTHRkIDIxPTA7BgNVBDMTNEIgMjAzIDJuZCBmbG9vciBTaGFtcGF0aCBIZXhhIE5ldyBHdWphcmF0IEhpZ2ggQ291cnQxEjAQBgNVBAkTCUFobWVkYWJhZDEQMA4GA1UECBMHR3VqYXJhdDEdMBsGA1UECxMUVGVjaG5pY2FsIERlcGFydG1lbnQxJTAjBgNVBAoTHE1hbnRyYSBTb2Z0ZWNoIEluZGlhIFB2dCBMdGQxCzAJBgNVBAYTAklOMB4XDTE4MDkwMzA5MjYyMVoXDTE4MDkwOTA2NTc0MFowgbAxJTAjBgNVBAMTHE1hbnRyYSBTb2Z0ZWNoIEluZGlhIFB2dCBMdGQxHjAcBgNVBAsTFUJpb21ldHJpYyBNYW51ZmFjdHVyZTEOMAwGA1UEChMFTVNJUEwxEjAQBgNVBAcTCUFITUVEQUJBRDEQMA4GA1UECBMHR1VKQVJBVDELMAkGA1UEBhMCSU4xJDAiBgkqhkiG9w0BCQEWFXN1cHBvcnRAbWFudHJhdGVjLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALh+hnpfPkWXbVG+JfynvT9UQFM7zzg5ppF+t1vHjJRMtq2ugcnuXgTBhyt2EM1ATliGErwtPUsv3/3RorbpH1Xocx+NjO8X0B0KGhJ8oZ1L+rgCC/4gChM2K3dGCkP2H4g1uXmf+qwxKBuYVHv/34KFjMf9bhy5ftrG0PR+FnVTw2HRwZykX63Ktdel3AwEPL1h8QQIq8gW91lTKYuQk4mHfb30xBV+p61Y4fsJSZ56jW34vkKwtuKoTe6WcdcZJXextcI0tnG80/kO0VY+XlqR9XyTzXkPmot4B2FjjbmrfKDU/9khoVrq21cwIETuEiTyVoDIfkKjeziv6TbTWiECAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAsa/iz+zlUFQYqHF8dJY6SdesgZEP0M8ncDVvJ6NN1V9v8BqWLuJmxOGO75MooVWySawtmukfW9Hk7Icr8o+DrqbzkVmqoS9gAb06AY2kKl+oUHr8HPVMiSWcHCqXFfF3zLxHDBP8Vq+mO7hahOYJqkPgNEODbHRYdxUi75EkZulOzl+iaaVnfJisFQHHMwCA8FBUJiUxTWuED4uocA4h9dfTMoGmEm+uQy0eahet6MPIV+n++S1iPa7Jy2q/f1LH5PJ/brOAjYRghL/KEqUO/32QyEOCvOFs2g7iIxTX6UgdENXm6vdKRu0xufP1x+CvdF0MxWiHIxzUmxVGuitlgw==|Y|0946653|09/07/2018 11:39:34|checksum=9840bd1795ba3592512cbe05092f3393#459264917163a4d4bccc09201271460c";
        try {
            String encryptedStr = Encrypt(sb).trim();
            encryptedStr = encryptedStr.replaceAll("\n","");
            Log.e("encryptedStr", encryptedStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Encrypt(String text) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = getKeyBytes();
//        byte[] b = {(byte) (186 & 0xff), (byte) (234 & 0xff), 123, (byte) (205 & 0xff), 92, 86, 43, 49, 77, 41, (byte) (168 & 0xff), 103, (byte) (164 & 0xff), (byte) (157 & 0xff), (byte) (129 & 0xff), 12};
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;


        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        BASE64Encoder encoder = new BASE64Encoder();
//
        return encoder.encode(results); // it returns the result as a String
//        return Base64.encodeToString(results, Base64.DEFAULT);
    }

    private byte[] getKeyBytes() throws UnsupportedEncodingException {
        InputStream fileStream = null;
        String destDir = Environment.getExternalStorageDirectory().toString() + "/Fps/UP_GOVT.key";

        try {
            fileStream = new FileInputStream(new File(destDir));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[0];
        try {
            int length = fileStream.available();
            buffer = new byte[length];
            int count;
            int sum = 0;
            while ((count = fileStream.read(buffer, sum, length - sum)) > 0)
                sum += count;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

    public String getRandomNumber() {
        String ranNum;
        Random random = new Random();

        // Generate random integers in range 0 to 999
        int rand_int1 = random.nextInt((99999 - 11111) + 1) + 11111;
        int rand_int2 = random.nextInt((89999 - 21111) + 1) + 21111;
        int rand_int3 = random.nextInt((79999 - 31111) + 1) + 31111;

        StringBuffer sb = new StringBuffer();
        sb.append(rand_int1 != 0 ? String.valueOf(rand_int1) : "");
        sb.append(rand_int2 != 0 ? String.valueOf(rand_int2) : "");
        sb.append(rand_int3 != 0 ? String.valueOf(rand_int3) : "");

        ranNum = sb.toString();
        return getCheckSum(ranNum);
    }

    protected String getCheckSum(String str1) {
        return getMD511(str1);
    }

    public String getMD511(String md5Input) {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(md5Input.getBytes(Charset.forName("UTF8")));
            final byte[] resultByte = messageDigest.digest();
            final String result = new String(Hex.encodeHex(resultByte));
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
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

