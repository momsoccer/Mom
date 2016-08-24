package com.mom.soccer.retrofitdao;


import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.FriendApply;
import com.mom.soccer.dto.FriendReqVo;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-07-12.
 */
public interface FriendService {

    /****************************************
     * 친구추가
     ************/
    //친구요청
    @POST("/api/common/reqFriend")
    public Call<ServerResult> reqFriend(@Body FriendApply friendApply);

    //마이페이지 친구상태
    @GET("/api/common/getFriendStatus")
    public Call<ServerResult> getFriendStatus(@Query("uid") int myUid, @Query("friendUid") int friendUid);

    //친구요청 리스트
    @GET("/api/common/FriendReqList")
    public Call<List<FriendReqVo>> FriendReqList(@Query("uid") int uid,@Query("flag") String flag);

    @POST("/api/common/getFriendApply")
    public Call<FriendApply> getFriendApply(@Body FriendApply friendApply);

    @POST("/api/common/updateFriendFlag")
    public Call<ServerResult> updateFriendFlag(@Body FriendApply friendApply);

    @POST("/api/common/deleteFriend")
    public Call<ServerResult> deleteFriend(@Body FriendApply friendApply);

    /****************************************
     * 팔로우...기능
     ************/

    @POST("/api/common/saveFollow")
    public Call<ServerResult> saveFollow(@Body FollowManage followManage);

    @POST("/api/common/deleteFollow")
    public Call<ServerResult> deleteFollow(@Body FollowManage followManage);

    @POST("/api/common/getFollowUserCount")
    public Call<ServerResult> getFollowUserCount(@Body FollowManage followManage);
}
