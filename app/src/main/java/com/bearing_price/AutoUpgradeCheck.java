package com.bearing_price;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bearing_price.data.Repository;
import com.bearing_price.data.VersionDto;
import com.bearing_price.data.Webservice;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;

public class AutoUpgradeCheck implements Webservice.OnFinishedListener {
    AutoUpgradeCheck autoUpgradeCheck;
    PackageInfo pInfo = null;
    Context context;

    public AutoUpgradeCheck(Context context) {
        this.context = context;
    }

    public boolean check_version() {
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            VersionDto dto = new VersionDto();
            dto.setPackage_name(pInfo.packageName);
            Repository.getInstance().check_version(dto, this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void getFutureFile(String path, String downloadApkPath) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Upgrading.. Please wait");
        pd.show();
        Ion.with(context).load(downloadApkPath)
                .progressDialog(pd)
                .progressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
//                        double ratio = downloaded / (double) total;
                        pd.setMax((int) total);
                        pd.setProgress((int) downloaded);

                    }
                })
                .write(new File(path))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (e != null) {
                            Toast.makeText(context, "Error downloading file.Try again", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        context.startActivity(i);
                    }

                });
    }

    @Override
    public void onFinished(Object responseDto) {

        int current_version = ((VersionDto) responseDto).getVersion();

        if (pInfo.versionCode < current_version) {
            final String downloadPath = ((VersionDto) responseDto).getPath();

            final String path = Environment.getExternalStorageDirectory() + "/Bearing_Price/bearing.apk";
            File file = new File(Environment.getExternalStorageDirectory(), "Bearing_Price");
            if (!file.exists()) {
                file.mkdir();
            }
            Log.e("Auto Upgrade", "downloadPath: " + downloadPath);
            Log.e("Auto Upgrade", "current_version: " + current_version);
            getFutureFile(path, downloadPath);
        }


    }

    @Override
    public void onFailure(Throwable t) {

    }
}
