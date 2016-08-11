package com.mom.soccer.retrofitdao;


import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.InstructorPointHistory;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.ins.InsApplyVo;

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
 * Created by sungbo on 2016-06-27.
 */
public interface InstructorService {


    @POST("/all/ins/saveInstructor")
    Call<ServerResult> saveInstructor(@Body Instructor ins);

    @POST("/api/ins/insLogin")
    Call<Instructor> insLogin(@Body Instructor instructor);

    /**********
     * 강사의 포인트 정책
     * ************/
    @GET("/api/ins/getPointHis")
    Call<InstructorPointHistory> getPointHis(@Query("insid") int insid);

    @Multipart
    @POST("/api/ins/fileupload")
    Call<ServerResult> fileupload(
            @Part("instructorid") RequestBody instructorid,
            @Part("filename") RequestBody filename,
            @Part("profileimgurl") RequestBody profileimgurl,
            @Part MultipartBody.Part file);

    @GET("/all/team/getInstructorList")
    Call<List<Instructor>> getInstructorList();

    //강사신청
    //@Multipart
    @POST("/ins/insApply")
    Call<ServerResult> insApply(
            @Body InsApplyVo ins/*,
            @Part("teamfilename") RequestBody teamfilename,
            @Part("insfilename") RequestBody insfilename,
            @Part("teamimgAddr") RequestBody teamimgAddr,
            @Part("insimgAddr") RequestBody insimgAddr,
            @Part MultipartBody.Part teamfile,*/
    );

}
