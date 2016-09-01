package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.mom.soccer.dataDto.FeedBackDataVo;
import com.mom.soccer.youtubeplayer.YoutubePlayerActivity;

import java.util.List;

/**
 * Created by sungbo on 2016-08-28.
 */
public class FeedBackEndAdapter extends RecyclerView.Adapter<FeedBackEndAdapter.ItemHolder>{

    Activity activity;
    private List<FeedBackDataVo> feedBackDataVos;

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

        final FeedBackDataVo vo = feedBackDataVos.get(position);
        final int i = position;
        if(!Compare.isEmpty(vo.getProfileimgurl())){
            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(holder.userimg);
        }

        if(!Compare.isEmpty(vo.getTeamname())){
            holder.teamname.setText(vo.getTeamname());
        }else{
            holder.teamname.setText(activity.getString(R.string.user_team_yet_join));
        }

        holder.date.setText(vo.getChange_creationdate());
        holder.name.setText(vo.getName());
        holder.toname.setText(vo.getToname());
        holder.missionname.setText(vo.getMissionname());
        holder.content.setText(vo.getContent());


        if(vo.getDatatype().equals("request")){
            holder.type.setText(activity.getString(R.string.feedback_type_re));
            holder.mRatingBar.setVisibility(View.GONE);
            holder.totalscore.setText(String.valueOf(vo.getTotalscore()));
        }else{
            holder.li_score.setVisibility(View.GONE);

            holder.type.setText(activity.getString(R.string.feedback_type_an));
            holder.view_top.setBackground(activity.getResources().getDrawable(R.drawable.card_rectangle));
            holder.image_second.setBackground(activity.getResources().getDrawable(R.drawable.card_rectangle));

            //답변 평가

            holder.mRatingBar.setRating(vo.getEvalscore());
            LayerDrawable star =  (LayerDrawable) holder.mRatingBar.getProgressDrawable();
            star.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }

        if(vo.getFeedbacktype().equals("video")){
            holder.video_ThumbnailView.setVisibility(View.VISIBLE);
            holder.video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(vo.getVideoaddr());
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
        }else{
            holder.video_ThumbnailView.setVisibility(View.GONE);
        }

        holder.video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //VeteranToast.makeToast(activity,i +" 클릭 로우 : "+ vo.getContent(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,vo.getVideoaddr());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedBackDataVos.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        LinearLayout li_video,li_score;
        ImageView userimg;
        YouTubeThumbnailView video_ThumbnailView;
        TextView date,toname,name,missionname,type,teamname,content,totalscore;
        CardView cardview;
        View view_top;
        ImageView image_second;
        RatingBar mRatingBar;

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
            content= (TextView) itemView.findViewById(R.id.content);
            totalscore= (TextView) itemView.findViewById(R.id.totalscore);
            image_second = (ImageView) itemView.findViewById(R.id.image_second);
            view_top = itemView.findViewById(R.id.view_top);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.eval_ratingbar);
            li_score = (LinearLayout) itemView.findViewById(R.id.li_score);

        }
    }
}
