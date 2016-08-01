package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.MainTypeListVo;
import com.mom.soccer.dataDto.UserRangkinVo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-07-21.
 */
public interface DataService {

    @GET("/api/data/typeList")
    Call<List<MainTypeListVo>> getMisstionTypeList();

    @GET("/api/data/getTotalRanking")
    Call<List<UserRangkinVo>> getTotalRanking(@Query("sequence") int sequence);

}
