package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.ins.InsApplyVo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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

}
