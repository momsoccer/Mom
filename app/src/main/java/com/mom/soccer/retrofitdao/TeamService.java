package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.FeedDataVo;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.Team;
import com.mom.soccer.dto.TeamApply;
import com.mom.soccer.dto.TeamMember;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-07-07.
 */
public interface TeamService {

    //팀 이미지 및 생성 관련, 업데이트는 서버에서 처리
    @Multipart
    @POST("/api/team/saveteam")
    Call<Team> saveTeam(
            @Part("insid") RequestBody insid,
            @Part("teamname") RequestBody teamName,
            @Part("teamdisp") RequestBody teamDisp,
            @Part("filename") RequestBody filename,
            @Part("profileimgurl") RequestBody profileimgurl,
            @Part("trFlag") RequestBody transactionFlag,
            @Part MultipartBody.Part file);

    @POST("/api/team/getTeamMemeber")
    Call<TeamMember> getTeamMemeber(@Body TeamMember teamMember);

    @POST("/api/team/applyTeam")
    Call<ServerResult> applyTeam(@Body TeamApply teamApply);

    @POST("/api/team/getTeamApply")
    Call<TeamApply> getTeamApply(@Body TeamApply teamApply);

    @POST("/api/team/deleteTeamApply")
    Call<ServerResult> deleteTeamApply(@Body TeamApply teamApply);

    @POST("/api/team/getReqMember")
    Call<List<TeamApply>> getReqMember(@Query("insid")int insid,@Query("status") String status);

    @GET("/api/team/acceptMember")
    Call<ServerResult> acceptMember(@Query("applyid")int applyid);

    @GET("/api/team/rejectMember")
    Call<ServerResult> rejectMember(@Query("applyid")int applyid);

    @GET("/api/team/getTeamCount")
    Call<FeedDataVo> getTeamCount(@Query("insid")int insid);

}
