package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.ServerResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-09-19.
 */
public interface CommonService {

    @GET("/common/set/deleteUser")
    public Call<ServerResult> deleteUser(@Query("uid")int uid);

}
