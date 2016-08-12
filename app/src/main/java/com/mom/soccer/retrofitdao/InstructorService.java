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
            @Part MultipartBody.Part file   //꼭 명은 file로 해야만 간다 %%%%%%%%%%%%%%%%%%
    );

    @GET("/all/team/getInstructorList")
    Call<List<Instructor>> getInstructorList();

    @Multipart
    @POST("/ins/insApplyfile")
    Call<ServerResult> insApplyfile(
            @Part("uid") RequestBody uid,
            @Part("updateflag") RequestBody updateflag,
            @Part("fileaddr") RequestBody fileaddr,
            @Part("filename") RequestBody filename,
            @Part MultipartBody.Part file //꼭 명은 file로 해야만 간다 %%%%%%%%%%%%%%%%%% teamfile,userfile 이렇게 하면 에러난다...
    );

    @POST("ins/insApply")
    Call<ServerResult> insApply(@Body InsApplyVo insApplyVo);

    @POST("ins/getIns")
    Call<InsApplyVo> getIns(@Body InsApplyVo insApplyVo);

    @POST("ins/delete")
    Call<ServerResult> delete(@Body InsApplyVo insApplyVo);

    @POST("ins/updateIns")
    Call<ServerResult> updateIns(@Body InsApplyVo insApplyVo);
}
