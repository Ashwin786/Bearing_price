package com.bearing_price;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.bearing_price.sync.GFG;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class TestActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 0;
//    private TestDb db;

//    private SqliteOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        GFG gf=new GFG();
        gf.start();
//        db = new TestDb(TestActivity.this);
//        helper();
//        SQLdataHelper sdb = SQLdataHelper.getInstance();
//        TestDb db = new TestDb(TestActivity.this);
//        db.delete();
//        withHelper();
    }

    private void withHelper() {
         new Thread(new Runnable() {
            @Override
            public void run() {
                SQLdataHelper db = SQLdataHelper.getInstance();
                for (int i = 0; i < 5; i++) {
                    db.insertTableA("Thread_B", i);
                }
            }
        }).start();
        SQLdataHelper db = SQLdataHelper.getInstance();
        for (int i = 0; i < 5; i++) {
            db.insertTableA("Thread_A", i);
        }
    }

    private void withoutHelper() {

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                TestDb db = new TestDb(TestActivity.this);
                for (int i = 0; i < 5; i++) {
                    db.insertTableA("Thread_B", i);
                }
            }
        }).start();

        TestDb db = new TestDb(this);
        for (int i = 0; i < 5; i++) {
            db.insertTableA("Thread_A", i);
        }*/
    }

    private void helper() {
        SqliteOpenHelper db = new SqliteOpenHelper(this);

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {

                    db.insertTableA("Thread_A", 0);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                SqliteOpenHelper db = new SqliteOpenHelper(TestActivity.this);
                for (int i = 0; i < 50; i++) {

                    db.insertTableA("Thread_B", 1);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            db.insertTableA("Thread_A", 0);

        }
    }


    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception {
        byte[] keyBytes = new byte[16];
        byte[] b = encryptionKey.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }

    String Decrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//        Log.e("Test", "data to be decrypted..."+text);
        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        return new String(Base64.decode(results, Base64.DEFAULT), "UTF-8");
    }


    private void launch_scanner() {
//        Intent i = new Intent(TestActivity.this,QrCodeActivity.class);
//        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "" + requestCode);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                        Log.e("QR CODE result", result);
//                        qrResponse(contents);
                    } catch (Exception e) {
                        Log.e("QRCodeSalesActivity", "Empty", e);
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e(TestActivity.class.getSimpleName(), "Scan cancelled");
                }

                break;

            default:
                break;
        }
    }
}
