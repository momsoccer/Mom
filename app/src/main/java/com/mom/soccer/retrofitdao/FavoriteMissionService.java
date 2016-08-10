package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.FavoriteMission;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-08-08.
 * Seed Mission favorite transation
 */
public interface FavoriteMissionService {

    @POST("/api/common/saveFavoriteMission")
    Call<ServerResult> saveFavoriteMission(@Body FavoriteMission f);

    @POST("/api/common/deleteFavoriteMission")
    Call<ServerResult> deleteFavoriteMission(@Body FavoriteMission f);

    @POST("/api/common/getCountFavoriteMission")
    Call<ServerResult> getCountFavoriteMission(@Body FavoriteMission f);

    @POST("/api/common/getFavoriteList")
    Call<List<FavoriteMission>> getFavoriteList(@Body FavoriteMission f);

    @POST("/api/common/getFavorite")
    Call<FavoriteMission> getFavorite(@Body FavoriteMission f);

    @POST("/api/common/getFavoriteMissionList")
    Call<List<Mission>> getFavoriteMissionList(@Body Mission m);
}
