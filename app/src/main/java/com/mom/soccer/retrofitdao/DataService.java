package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.MainTypeListVo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sungbo on 2016-07-21.
 */
public interface DataService {

    @GET("/api/data/typeList")
    Call<List<MainTypeListVo>> getMisstionTypeList();

}
