package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.UserMainVo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-09-06.
 */
public interface UserMainService {

    @POST("/api/usermain/getUserMainList")
    Call<List<UserMainVo>> getUserMainList(@Body UserMainVo userMainVo);

}
