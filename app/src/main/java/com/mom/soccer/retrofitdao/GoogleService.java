package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.ServerResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-06-29.
 */
public interface GoogleService {

    @GET("/all/sendfcmtoek")
    Call<ServerResult> sendFcmToken(@Query("token") String token);
}
