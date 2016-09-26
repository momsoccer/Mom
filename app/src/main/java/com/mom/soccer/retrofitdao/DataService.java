package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.FeedDataVo;
import com.mom.soccer.dataDto.InsInfoVo;
import com.mom.soccer.dataDto.MainTypeListVo;
import com.mom.soccer.dataDto.TeamRankingVo;
import com.mom.soccer.dataDto.UserLevelDataVo;
import com.mom.soccer.dataDto.UserMainVo;
import com.mom.soccer.dataDto.UserRangkinVo;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @POST("/api/data/getFriendRanking")
    Call<List<UserRangkinVo>> getFriendRanking(@Body UserRangkinVo userRangkinVo);

    //선생님 피드빽 신청할때 필요한 리스트
    @POST("/api/data/getInsInfoList")
    Call<List<InsInfoVo>> getInsInfoList(@Body InsInfoVo v);

    //선생님 피드빽 신청할때 필요한 리스트
    @POST("/api/data/getInsInfo")
    Call<InsInfoVo> getInsInfo(@Body InsInfoVo v);

    @GET("/api/data/getUserLevelDataList")
    Call<List<UserLevelDataVo>> getUserLevelDataList(@Query("uid") int uid);

    @GET("/api/data/getFeedData")
    Call<FeedDataVo> getFeedData(@Query("insid") int insid);

    @GET("/api/data/getPassData")
    Call<FeedDataVo> getPassData(@Query("insid") int insid);

    @GET("/api/data/getUserMissionCount")
    Call<FeedDataVo> getUserMissionCount(@Query("uid") int uid);

    @GET("/api/data/getInsFileCount")
    Call<ServerResult> getInsFileCount(@Query("uid") int uid,
                                       @Query("insid") int insid,
                                       @Query("filename") String filename);

    @GET("/api/data/getTeamMemberList")
    Call<List<UserMainVo>> getTeamMemberList(@Query("insid") int insid);

    @GET("/api/data/getTeamRankingScore")
    Call<List<TeamRankingVo>> getTeamRankingScore(@Query("count") int count);


}
