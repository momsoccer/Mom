package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.InsPointBalance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-09-05.
 */
public interface InsPointBalanceService {
    @GET("/ins/getInsPointBalanceList")
    Call<List<InsPointBalance>> getInsPointBalanceList(@Query("insid") int insid);
}
