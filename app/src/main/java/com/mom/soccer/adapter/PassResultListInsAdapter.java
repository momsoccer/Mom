package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Context;
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
public class PassResultListInsAdapter extends RecyclerView.Adapter<PassResultListInsAdapter.PassItemHolder>{

    private Activity activity;
    private List<MissionPass> missionPasses;
    private Instructor instructor;

    public PassResultListInsAdapter(Activity activity, List<MissionPass> missionPasses, Instructor instructor) {
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
        LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.pass_card_ins_item2, parent,false);

        return new PassItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PassItemHolder h, int position) {
        final MissionPass vo = missionPasses.get(position);

        //result_status,comment,f_content;
        if(vo.getPassflag().equals("Y")){
            h.result_status.setText(activity.getString(R.string.user_mission_pass_flag_title1));
        }else{
            h.result_status.setText(activity.getString(R.string.user_mission_pass_flag_title2));
            h.result_status.setTextColor(activity.getResources().getColor(R.color.enabled_red));
        }

        h.comment.setText(vo.getInscomment());
        h.f_content.setText(vo.getFailuredisp());

        if (!Compare.isEmpty(vo.getUserimge())) {
            Glide.with(activity)
                    .load(vo.getUserimge())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(h.userimg);
        }

        if(!Compare.isEmpty(vo.getTeamname())){
            h.teamname.setText(vo.getTeamname());
        }else {
            h.teamname.setText(activity.getString(R.string.user_team_yet_join));
        }

        h.level.setText(String.valueOf(vo.getLevel()));
        h.date.setText(vo.getChange_updatedate());
        h.seq.setText(String.valueOf(vo.getSeq()));
        h.username.setText(vo.getUsername());
        h.missionname.setText(vo.getMissionname());

        h.user_video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getUservideo());
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

        h.mission_video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getMissionvideo());
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
    }




    public class PassItemHolder extends RecyclerView.ViewHolder{

        TextView date,seq,username,teamname,missionname,level,result_status,comment,f_content;
        ImageView userimg;
        Button resultbtn;
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
            user_video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.user_video_ThumbnailView);
            mission_video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.mission_video_ThumbnailView);
            resultbtn = (Button) itemView.findViewById(R.id.resultbtn);

            result_status = (TextView) itemView.findViewById(R.id.result_status);
            comment = (TextView) itemView.findViewById(R.id.comment);
            f_content = (TextView) itemView.findViewById(R.id.f_content);

        }
    }


}
