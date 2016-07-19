package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.ServerResult;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sungbo on 2016-07-19.
 */
public interface MomComService {
    @GET("/api/user/deleteCookies")
    Call<ServerResult> logOut();
}
