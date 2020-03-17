package com.bearing_price.data.retrofit;


import com.bearing_price.data.VersionDto;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by user1 on 8/5/19.
 */

public interface APIInterface {

    @FormUrlEncoded
    @POST("/get_version.php?")
    Call<VersionDto> getversion(@Field("package_name") String package_name);
}
