package com.mom.soccer.retrofitdao;

import com.mom.soccer.dto.InsVideoLikeVo;
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.InsVideoVoLine;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sungbo on 2016-09-21.
 * 강사 비디오 강의 업로드
 */
public interface InsVideoService {

    @POST("/insvideo/saveVideo")
    Call<ServerResult> saveVideo(@Body InsVideoVo insVideoVo);

    @POST("/insvideo/getVideoList")
    Call<List<InsVideoVo>> getVideoList(@Body InsVideoVo insVideoVo);

    @POST("/insvideo/getVideo")
    Call<InsVideoVo> getVideo(@Body InsVideoVo insVideoVo);

    @POST("/insvideo/deleteVideo")
    Call<ServerResult> deleteVideo(@Body InsVideoVo insVideoVo);

    @POST("/insvideo/updateVideo")
    Call<ServerResult> updateVideo(@Body InsVideoVo insVideoVo);

    //like video
    @POST("/insvideo/saveVideoLike")
    Call<ServerResult> saveVideoLike(@Body InsVideoLikeVo insVideoVo);

    @POST("/insvideo/deleteVideoLike")
    Call<ServerResult> deleteVideoLike(@Body InsVideoLikeVo insVideoVo);

    @GET("/insvideo/getVideoLikeList")
    Call<List<InsVideoLikeVo>> getVideoLikeList(@Query("likeid") int likeid);


    //line video
    @POST("/insvideo/saveLine")
    Call<ServerResult> saveLine(@Body InsVideoVoLine insVideoVo);

    @POST("/insvideo/deleteLine")
    Call<ServerResult> deleteLine(@Body InsVideoVoLine insVideoVo);

    @GET("/insvideo/getLineList")
    Call<List<InsVideoVoLine>> getLineList(@Query("videoid") int videoid);

    @GET("/insvideo/getLikeVideoCount")
    Call<Integer> getLikeVideoCount(
            @Query("uid") int uid,
            @Query("videoid") int videoid);

}
