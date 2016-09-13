package com.mom.soccer.retrofitdao;


import com.mom.soccer.dto.FcmToken;
import com.mom.soccer.dto.ServerResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-06-30.
 */
public interface FcmTokenService {

    @POST("/fcm/token/setupToken")
    public Call<ServerResult> setupToken(@Body FcmToken fcmToken);

    @POST("/all/saveToken")
    public Call<ServerResult> saveToken(@Body FcmToken fcmToken);

    @POST("/all/updateToken")
    public Call<ServerResult> updateToken(@Body FcmToken fcmToken);

}
