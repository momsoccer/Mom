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

    @POST("/momboard/getBoardHeaderList")
    public List<MomBoard> getBoardHeaderList(@Body MomBoard momBoard);

    @POST("/momboard/saveBoardheader")
    public Call<ServerResult> saveBoardheader(@Body MomBoard momBoard);

    @POST("/momboard/deleteBoardheader")
    public void deleteBoardheader(@Body MomBoard momBoard);

    @POST("/momboard/getBoardLineList")
    public List<MomBoard> getBoardLineList(@Body MomBoard momBoard);

    @POST("/momboard/getBoardLineCount")
    public int getBoardLineCount(@Body MomBoard momBoard);

    @POST("/momboard/saveBoardLine")
    public void saveBoardLine(@Body MomBoard momBoard);

    @POST("/momboard/deleteBoardLine")
    public void deleteBoardLine(@Body MomBoard momBoard);

    @POST("/momboard/getBoardLikeCount")
    public int getBoardLikeCount(@Body MomBoard momBoard);

    @POST("/momboard/saveBoardLike")
    public void saveBoardLike(@Body MomBoard momBoard);

    @POST("/momboard/deleteBoardLike")
    public void deleteBoardLike(@Body MomBoard momBoard);

    @POST("/momboard/saveBoardFile")
    public void saveBoardFile(@Body MomBoard momBoard);

    @POST("/momboard/deleteBoardFile")
    public void deleteBoardFile(@Body MomBoard momBoard);

}
