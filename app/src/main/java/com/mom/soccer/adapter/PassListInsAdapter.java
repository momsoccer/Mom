package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.youtubeplayer.YoutubePlayerActivity;

import java.util.List;

/** 강사가 유저 미션 패스하는 어댑터
 * Created by sungbo on 2016-08-30.
 */
public class PassListInsAdapter extends RecyclerView.Adapter<PassListInsAdapter.PassItemHolder>{

    private Activity activity;
    private List<MissionPass> missionPasses;
    private Instructor instructor;
    private MissionPass vo;

    public PassListInsAdapter(Activity activity, List<MissionPass> missionPasses, Instructor instructor) {
        this.activity = activity;
        this.missionPasses = missionPasses;
        this.instructor = instructor;
    }

    @Override
    public int getItemCount() {
        return missionPasses.size();
    }


    @Override
    public PassItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_card_ins_item, null);
        return new PassItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PassItemHolder h, int position) {
        vo = missionPasses.get(position);

        if (!Compare.isEmpty(vo.getUserimge())) {
            Glide.with(activity)
                    .load(vo.getInsimge())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(h.userimg);
        }

        h.date.setText(vo.getChange_creationdate());
        h.seq.setText(String.valueOf(vo.getSeq()));
        h.username.setText(vo.getUsername());
        h.teamname.setText(vo.getTeamname());
        h.missionname.setText(vo.getMissionname());
        h.user_video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getUservideo());
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        h.mission_video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getMissionvideo());
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        h.user_video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,vo.getUservideo());
                activity.startActivity(intent);
            }
        });

        h.mission_video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,vo.getMissionvideo());
                activity.startActivity(intent);
            }
        });

        //합격 사유 및 불합격 사유 다이얼 로그를 뛰운다..
        h.btnComplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        h.btninComplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public class PassItemHolder extends RecyclerView.ViewHolder{

        TextView date,seq,username,teamname,missionname,level;
        ImageView userimg;
        Button btnComplate,btninComplate;
        YouTubeThumbnailView user_video_ThumbnailView,mission_video_ThumbnailView;

        public PassItemHolder(View itemView) {
            super(itemView);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            seq = (TextView) itemView.findViewById(R.id.seq);
            date = (TextView) itemView.findViewById(R.id.date);
            username = (TextView) itemView.findViewById(R.id.username);
            level = (TextView) itemView.findViewById(R.id.level);
            teamname = (TextView) itemView.findViewById(R.id.teamname);
            missionname = (TextView) itemView.findViewById(R.id.missionname);
            btnComplate = (Button) itemView.findViewById(R.id.btnComplate);
            btninComplate = (Button) itemView.findViewById(R.id.btninComplate);
            user_video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.user_video_ThumbnailView);
            mission_video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.mission_video_ThumbnailView);

        }
    }
}
