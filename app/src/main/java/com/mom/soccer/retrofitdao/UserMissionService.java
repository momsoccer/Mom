package com.mom.soccer.retrofitdao;


import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.UserMission;

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

    @GET("/api/usermission/update")
    Call<ServerResult> updateUserMission(
            @Query("usermissionid") int usermissionid,
            @Query("uid") int uid,
            @Query("youtubeaddr") String youTubeaddr);
}
