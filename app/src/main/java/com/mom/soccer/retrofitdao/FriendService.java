package com.mom.soccer.retrofitdao;


import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.FriendApply;
import com.mom.soccer.dto.ServerResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-07-12.
 */
public interface FriendService {

    @POST("/api/common/reqFriend")
    public Call<ServerResult> reqFriend(@Body FriendApply friendApply);

    @POST("/api/common/saveFollow")
    public Call<ServerResult> saveFollow(@Body FollowManage followManage);

    @POST("/api/common/deleteFollow")
    public Call<ServerResult> deleteFollow(@Body FollowManage followManage);

    @POST("/api/common/getFollowUserCount")
    public Call<ServerResult> getFollowUserCount(@Body FollowManage followManage);
}
