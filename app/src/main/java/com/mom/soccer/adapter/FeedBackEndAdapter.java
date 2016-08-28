package com.mom.soccer.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.FeedBackDataVo;

import java.util.List;

/**
 * Created by sungbo on 2016-08-28.
 */
public class FeedBackEndAdapter extends RecyclerView.Adapter<FeedBackEndAdapter.ItemHolder>{

    Activity activity;
    private List<FeedBackDataVo> feedBackDataVos;
    private String videoAddr;

    public FeedBackEndAdapter(Activity activity, List<FeedBackDataVo> feedBackDataVos) {
        this.activity = activity;
        this.feedBackDataVos = feedBackDataVos;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_result_item, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

        FeedBackDataVo vo = feedBackDataVos.get(position);

        if(!Compare.isEmpty(vo.getProfileimgurl())){
            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(holder.userimg);
        }

        holder.date.setText(vo.getChange_creationdate());
        holder.name.setText(vo.getName());
        holder.toname.setText(vo.getToname());
        holder.missionname.setText(vo.getMissionname());

        if(vo.getDatatype().equals("request")){
            holder.type.setText(activity.getString(R.string.feedback_type_re));
        }else{
            holder.type.setText(activity.getString(R.string.feedback_type_an));
        }

        videoAddr = vo.getVideoaddr();

        holder.video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(videoAddr);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return feedBackDataVos.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        LinearLayout li_video;
        ImageView userimg;
        YouTubeThumbnailView video_ThumbnailView;
        TextView date,toname,name,missionname,type,teamname;
        CardView cardview;


        public ItemHolder(View itemView) {
            super(itemView);

            li_video = (LinearLayout) itemView.findViewById(R.id.li_video);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.video_ThumbnailView);
            date = (TextView) itemView.findViewById(R.id.date);
            toname = (TextView) itemView.findViewById(R.id.toname);
            name = (TextView) itemView.findViewById(R.id.name);
            missionname = (TextView) itemView.findViewById(R.id.missionname);
            type = (TextView) itemView.findViewById(R.id.type);
            teamname = (TextView) itemView.findViewById(R.id.teamname);
            cardview = (CardView) itemView.findViewById(R.id.cardview);

        }
    }
}
