package com.mom.soccer.retrofitdao;


import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.UserMission;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by sungbo on 2016-06-13.
 */
public interface UserMissionService {

    @POST("/api/usermission/insert")
    Call<ServerResult> createUserMission(@Body UserMission userMission);

    @POST("/api/usermission/videoValidate")
    Call<ServerResult> videoValidate(@Body UserMission userMission);

    @POST("/api/usermission/uMissionList")
    Call<List<UserMission>> uMissionList(@Body UserMission userMission);

    //my 영상
    @POST("/api/usermission/getUserMissionList")
    Call<List<UserMission>> getUserMissionList(@Body UserMission userMission);

    @GET("/api/usermission/update")
    Call<ServerResult> updateUserMission(
            @Query("usermissionid") int usermissionid,
            @Query("uid") int uid,
            @Query("youtubeaddr") String youTubeaddr);
}
