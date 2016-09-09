package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-09-08.
 */
public interface MomBoardService {

    @POST("/momboard/getBoardHeader")
    Call<MomBoard> getBoardHeader(@Body MomBoard momBoard);

    @POST("/momboard/getBoardHeaderList")
    Call<List<MomBoard>> getBoardHeaderList(@Body MomBoard momBoard);

    @POST("/momboard/saveBoardheader")
    Call<ServerResult> saveBoardheader(@Body MomBoard momBoard);

    @POST("/momboard/deleteBoardheader")
    Call<ServerResult> deleteBoardheader(@Body MomBoard momBoard);

    @POST("/momboard/getBoardLineList")
    Call<List<MomBoard>> getBoardLineList(@Body MomBoard momBoard);

    @POST("/momboard/getBoardLineCount")
    Call<ServerResult> getBoardLineCount(@Body MomBoard momBoard);

    @POST("/momboard/saveBoardLine")
    Call<ServerResult> saveBoardLine(@Body MomBoard momBoard);

    @POST("/momboard/deleteBoardLine")
    Call<ServerResult> deleteBoardLine(@Body MomBoard momBoard);

    @POST("/momboard/getBoardLikeCount")
    Call<ServerResult> getBoardLikeCount(@Body MomBoard momBoard);

    @POST("/momboard/saveBoardLike")
    Call<ServerResult> saveBoardLike(@Body MomBoard momBoard);

    @POST("/momboard/deleteBoardLike")
    Call<ServerResult> deleteBoardLike(@Body MomBoard momBoard);

    @POST("/momboard/saveBoardFile")
    Call<ServerResult> saveBoardFile(@Body MomBoard momBoard);

    @POST("/momboard/deleteBoardFile")
    Call<ServerResult> deleteBoardFile(@Body MomBoard momBoard);

}
