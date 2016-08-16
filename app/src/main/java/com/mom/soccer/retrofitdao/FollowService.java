package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.TeamMapVo;
import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-08-16.
 */
public interface FollowService {

    @POST("/api/common/saveFollow")
    Call<ServerResult> saveFollow(@Body FollowManage f);

    @POST("/api/common/deleteFollow")
    Call<ServerResult> deleteFollow(@Body FollowManage f);

    @POST("/api/common/getFollowUserCount")
    Call<ServerResult> getFollowUserCount(@Body FollowManage f);

    @POST("/api/common/getMeFollowList")
    Call<List<FollowManage>> getMeFollowList(@Body FollowManage f);

    @GET("/api/common/getFollowerCount")
    Call<List<TeamMapVo>> getFollowerCount(@Query("uid") int uid);


}
