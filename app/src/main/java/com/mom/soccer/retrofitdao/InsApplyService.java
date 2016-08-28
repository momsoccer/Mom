package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.ins.InsApplyVo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sungbo on 2016-08-11.
 *
 */
public interface InsApplyService {

    @POST("/ins/insApply")
    Call<ServerResult> insLogin(@Body InsApplyVo applyVo);

    @POST("/ins/updateIns")
    Call<ServerResult> updateIns(@Body InsApplyVo applyVo);

    @POST("/ins/getListIns")
    Call<List<InsApplyVo>> getListIns(@Body InsApplyVo applyVo);

    @POST("/ins/gettIns")
    Call<InsApplyVo> gettIns(@Body InsApplyVo applyVo);



    @Multipart
    @POST("/ins/userFileupload")
    Call<ServerResult> fileupload(
            @Part("uid") RequestBody uid,
            @Part("userfileAddr") RequestBody userfileAddr,
            @Part("filename") RequestBody filename,
            @Part MultipartBody.Part file,
            @Part("teamStatus") RequestBody teamStatus,
            @Part("insfileAddr") RequestBody insfileAddr
    );

}
