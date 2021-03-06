package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.Board;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-08-01.
 */
public interface BoardService {

    @POST("/api/mission/board/saveboard")
    public Call<ServerResult> saveBoard(@Body Board board);

    @POST("/api/mission/board/getboardlist")
    public Call<List<Board>> getboardlist(@Body Board board);

    @GET("/api/mission/board/deleteBoard")
    public Call<ServerResult> deleteBoard(@Query("boardid")int boardid);

}
