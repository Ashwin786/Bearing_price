package com.bearing_price.data;

import android.util.Log;

import com.bearing_price.data.retrofit.APIInterface;
import com.bearing_price.data.retrofit.RetroFit_Service;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository implements Webservice {
    private final APIInterface apiInterface;
    static Repository repository = new Repository();

    public Repository() {
        apiInterface = RetroFit_Service.getClient().create(APIInterface.class);
    }


    public static Repository getInstance() {
        return repository;
    }


    @Override
    public void check_version(final VersionDto versionDto, final OnFinishedListener finishedListener) {
        Call<VersionDto> call = apiInterface.getversion(versionDto.getPackage_name());
        call.enqueue(new Callback<VersionDto>() {
            @Override
            public void onResponse(Call<VersionDto> call, final Response<VersionDto> response) {
                Log.e("Code", response.code() + "");
                if (response.code() == 200) {
                    VersionDto status = response.body();
                    if (status.getStatus() == 0) {
                        finishedListener.onFinished(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<VersionDto> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
