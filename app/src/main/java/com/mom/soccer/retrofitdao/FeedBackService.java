package com.mom.soccer.retrofitdao;

import com.mom.soccer.dataDto.FeedBackDataVo;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.FeedbackLine;
import com.mom.soccer.dto.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sungbo on 2016-06-20.
 */
public interface FeedBackService {

 @POST("/api/feed/saveFeedHeader")
 Call<ServerResult> saveFeedHeader(@Body FeedbackHeader feedbackHeader);

 @POST("/api/feed/getFeedBackList")
 Call<List<FeedbackHeader>> getFeedHeaderList(@Body FeedbackHeader feedbackHeader);

 @POST("/api/feed/updateLine")
 Call<ServerResult> updateLine(@Body FeedbackLine line);

 @POST("/api/feed/deleteLine")
 Call<ServerResult> deleteLine(@Body FeedbackLine line);

 @POST("/api/feed/updateContent")
 Call<ServerResult> updateContent(@Body FeedbackLine line);

 @POST("/api/feed/requestFeed")
 Call<List<FeedbackHeader>> requestFeed(@Body FeedbackHeader feedbackHeader);

 @POST("/api/feed/saveLine")
 Call<ServerResult> saveLine(@Body FeedbackLine saveLine);

 @POST("/api/feed/getFeedBackTotalList")
 Call<List<FeedBackDataVo>> getFeedBackTotalList(@Body FeedBackDataVo v);
}
