package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.MyBookMark;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.UserMission;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-07-30.
 */
public interface PickService {

    @POST("/api/mission/pickvideo")
    Call<ServerResult> pickVideo(@Body MyBookMark myBookMark);

    @POST("/api/mission/pickcancel")
    Call<ServerResult> pickCancel(@Body MyBookMark myBookMark);

    @GET("/api/mission/getPickCount")
    Call<ServerResult> getPickCount(@Query("uid") int uid, @Query("usermissionid") int usermissionid);

    @POST("/api/mission/getMyBookMark")
    Call<List<UserMission>> getMyBookMark(@Body MyBookMark myBookMark);

    @POST("/api/mission/getMyBookMarkList")
    Call<List<MyBookMark>> getMyBookMarkList(@Body MyBookMark myBookMark);
}
