package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.Mission;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-07-26.
 */
public interface MissionService {

    @GET("/api/mission/getMissionList")
    Call<List<Mission>> getMissionList(
            @Query("typename") String typename,
            @Query("uid") int uid
    );

    @POST("/api/mission/getMission")
    Call<Mission> getMission(@Body Mission mission);

}
