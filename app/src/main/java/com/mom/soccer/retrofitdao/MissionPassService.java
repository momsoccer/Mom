package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-08-29.
 */
public interface MissionPassService {

    @POST("/api/pass/saveUserMissionPass")
    Call<ServerResult> saveUserMissionPass(@Body MissionPass h);

    @POST("/api/pass/getPassList")
    Call<List<MissionPass>> getPassList(@Body MissionPass h);

    @POST("/api/pass/updatePass")
    Call<ServerResult> updatePass(@Body MissionPass h);

    @POST("/api/pass/getMissionPass")
    Call<MissionPass> getMissionPass(@Body MissionPass h);

    @POST("/api/pass/deletePass")
    Call<ServerResult> deletePass(@Body MissionPass h);

    //강사 패스를 위함..
    @POST("/api/pass/getMissionPassList")
    Call<List<MissionPass>> getMissionPassList(@Body MissionPass h);

}
