package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.MissionHistory;
import com.mom.soccer.dto.ServerResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-08-08.
 */
public interface MissionHistoryService {
    @POST("/api/mission/saveMissionHistory")
    Call<ServerResult> saveMissionHistory(@Body MissionHistory h);

    @POST("/api/mission/getMissionHistoryCount")
    Call<ServerResult> getMissionHistoryCount(@Body MissionHistory h);
}
