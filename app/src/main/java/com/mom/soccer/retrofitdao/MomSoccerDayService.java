package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.EventMain;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sungbo on 2016-12-14.
 */

public interface MomSoccerDayService {

    @GET("/api/event/getEventMainList")
    Call<List<EventMain>> getEventMainList();

}
