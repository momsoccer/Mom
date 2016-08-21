package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.InsInfoVo;
import com.mom.soccer.dataDto.MainTypeListVo;
import com.mom.soccer.dataDto.UserRangkinVo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-07-21.
 */
public interface DataService {

    @GET("/api/data/typeList")
    Call<List<MainTypeListVo>> getMisstionTypeList();

    @POST("/api/data/getTotalRanking")
    Call<List<UserRangkinVo>> getTotalRanking(@Body UserRangkinVo userRangkinVo);

    @POST("/api/data/getTeamRanking")
    Call<List<UserRangkinVo>> getTeamRanking(@Body UserRangkinVo userRangkinVo);

    //선생님 피드빽 신청할때 필요한 리스트
    @POST("/api/data/getInsInfoList")
    Call<List<InsInfoVo>> getInsInfoList(@Body InsInfoVo v);

    //선생님 피드빽 신청할때 필요한 리스트
    @POST("/api/data/getInsInfo")
    Call<InsInfoVo> getInsInfo(@Body InsInfoVo v);


}
