package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.AdBoardVo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-09-19.
 */
public interface AdBoardService {

    @POST("/adboard/getlist")
    Call<List<AdBoardVo>> getList(@Body AdBoardVo adBoardVo);

    @GET("/adboard/getHeader")
    Call<AdBoardVo> getHeader(@Query("advid") int advid);

}
