package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.QuestionVo;
import com.mom.soccer.dto.ServerResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-09-23.
 */

public interface QuestionService {

    @POST("/api/question/saveQuestion")
    Call<ServerResult> saveQuestion(@Body QuestionVo questionVo);

}
