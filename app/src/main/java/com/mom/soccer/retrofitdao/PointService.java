package com.mom.soccer.retrofitdao;


import com.mom.soccer.dataDto.PointTrVo;
import com.mom.soccer.dto.CpBalanceHeader;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.SpBalanceHeader;
import com.mom.soccer.dto.SpBalanceLine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by sungbo on 2016-06-11.
 */
public interface PointService {

    @GET("/api/user/selfpointamount")
    Call<SpBalanceHeader> getSelfAmt(@Query("uid") int uid);

    @GET("/api/user/cashpointamount")
    Call<CpBalanceHeader> getCashAmt(@Query("uid") int uid);

    //출석체크
    @GET("/api/user/userdailycheck")
    Call<ServerResult> daliyCheck(
            @Query("uid") String uid,
            @Query("pointtype") String pointType,
            @Query("lang") String lang
    );


    //미션 오픈
    @POST("/api/user/pointTr")
    Call<ServerResult> pointTr(@Body PointTrVo pointTrVo);

    @GET("/api/user/getCashLineList")
    Call<List<SpBalanceLine>> getCashLineList(@Query("uid") int uid);
}
