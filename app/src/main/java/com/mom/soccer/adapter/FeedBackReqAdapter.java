package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.FeedBackWrite;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.youtubeplayer.YoutubePlayerActivity;

import java.util.List;

/**
 * Created by sungbo on 2016-08-26.
 */
public class FeedBackReqAdapter extends RecyclerView.Adapter<FeedBackReqAdapter.FeedbackItemHolder>{

    Activity activity;
    List<FeedbackHeader> feedbackHeaders;
    User user;
    Instructor instructor;

    private String videoAddr;
    private String youtubeMissionVideo;
    private FeedbackHeader paramFeed;

    public FeedBackReqAdapter(Activity activity, List<FeedbackHeader> feedbackHeaders, User user, Instructor instructor) {
        this.activity = activity;
        this.feedbackHeaders = feedbackHeaders;
        this.user = user;
        this.instructor = instructor;
    }

    @Override
    public FeedbackItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, null);

        return new FeedbackItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final FeedbackItemHolder holder, int position) {
        final FeedbackHeader feed = feedbackHeaders.get(position);

        paramFeed = feed;

        videoAddr = feed.getVideoaddr();
        youtubeMissionVideo = feed.getYoutubeaddr();

        if (!Compare.isEmpty(feed.getProfileimgurl())) {
            Glide.with(activity)
                    .load(feed.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(holder.userimg);

        }
        holder.subject.setText(feed.getSubject());
        holder.username.setText(feed.getUsername());

        if(Compare.isEmpty(feed.getTeamname())){
            holder.teamname.setText(activity.getString(R.string.user_team_yet_join));
        }else{
            holder.teamname.setText(feed.getTeamname());
        }

        if(feed.getFeedbacktype().equals("video")){
            holder.reqtype.setText(activity.getString(R.string.feedback_type_video));
        }else{
            holder.reqtype.setText(activity.getString(R.string.feedback_type_word));
        }


        holder.level.setText(String.valueOf(feed.getLevel()));
        holder.point.setText(feed.getCashpoint()+"P");
        holder.content.setText(feed.getContent());
        holder.date.setText(feed.getChange_creationdate());

        holder.user_video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(feed.getVideoaddr());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        holder.mission_video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(feed.getYoutubeaddr());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        holder.btnfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //VeteranToast.makeToast(activity,"이름은 : " + feed.getContent(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity,FeedBackWrite.class);
                intent.putExtra(MissionCommon.FEEDBACKHEADER,feed);
                Log.i("test","FEED : "+ feed.getContent());
                activity.startActivity(intent);
            }
        });

        //유저 영상풀로 보기
        holder.user_video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,feed.getVideoaddr());
                activity.startActivity(intent);
            }
        });

        holder.mission_video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,feed.getYoutubeaddr());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedbackHeaders.size();
    }

    public class FeedbackItemHolder extends RecyclerView.ViewHolder {

        ImageView  userimg;
        TextView   subject,username,teamname,reqtype,point,content,date,level;
        YouTubeThumbnailView  user_video_ThumbnailView,mission_video_ThumbnailView;
        Button btnfeed;
        public CardView cardview;

        public FeedbackItemHolder(View itemView) {
            super(itemView);
            level = (TextView) itemView.findViewById(R.id.level);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            subject = (TextView) itemView.findViewById(R.id.subject);
            username = (TextView) itemView.findViewById(R.id.username);
            teamname = (TextView) itemView.findViewById(R.id.teamname);
            reqtype = (TextView) itemView.findViewById(R.id.reqtype);
            point = (TextView) itemView.findViewById(R.id.point);
            date = (TextView) itemView.findViewById(R.id.date);
            content = (TextView) itemView.findViewById(R.id.content);
            user_video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.user_video_ThumbnailView);
            btnfeed = (Button) itemView.findViewById(R.id.btnfeed);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            //mission_video= (ImageView) itemView.findViewById(R.id.mission_video);

            mission_video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.mission_video_ThumbnailView);

        }
    }
}
