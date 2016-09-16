package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.MomBoardFile;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sungbo on 2016-09-08.
 */
public interface MomBoardService {

    @POST("/momboard/getBoardHeader")
    Call<MomBoard> getBoardHeader(@Body MomBoard momBoard);

    @POST("/momboard/updateBoardHeader")
    Call<ServerResult> updateBoardHeader(@Body MomBoard momBoard);

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
    Call<ServerResult> deleteBoardFile(@Body MomBoardFile momBoardFile);

    @Multipart
    @POST("/momboard/fileupload")
    Call<ServerResult> fileupload(
            @Part("boardid") RequestBody boardid,
            @Part("filename") RequestBody filename,
            @Part("profileimgurl") RequestBody profileimgurl,
            @Part MultipartBody.Part file
    );
}
