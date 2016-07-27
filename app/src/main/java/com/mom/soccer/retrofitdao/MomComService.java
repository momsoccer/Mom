package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.MomMessage;
import com.mom.soccer.dto.ServerResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-07-19.
 */
public interface MomComService {
    @GET("/api/user/deleteCookies")
    Call<ServerResult> logOut();

    @GET("/api/mission/getCommonInfo")
    Call<MomMessage> getCommonInfo(@Query("name") String name

    );

}

