package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.AdBoardVo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-09-19.
 */
public interface AdBoardService {

    @POST("/adboard/getlist")
    Call<List<AdBoardVo>> getList(@Body AdBoardVo adBoardVo);

}
