package com.mom.soccer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.dto.Mission;

import java.util.List;

/**
 * Created by sungbo on 2016-08-25.
 */
public class UserMissionAdapter extends RecyclerView.Adapter<UserMissionAdapter.MissionHorder>{

    Context context;
    List<Mission> missions;

    public UserMissionAdapter(Context context, List<Mission> missions) {
        this.context = context;
        this.missions = missions;
    }

    @Override
    public MissionHorder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_mission_item, null);
        return new MissionHorder(v);
    }

    @Override
    public void onBindViewHolder(MissionHorder holder, int position) {
        final Mission vo = missions.get(position);

        holder.view.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getYoutubeaddr());
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return missions.size();
    }

    //allMissionList


    public class MissionHorder extends RecyclerView.ViewHolder {

        YouTubeThumbnailView view;

        public MissionHorder(View itemView) {
            super(itemView);

            view = (YouTubeThumbnailView) itemView.findViewById(R.id.youtybe_Thumbnail);
        }
    }
}
