package com.mom.soccer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.dto.FeedbackHeader;

import java.util.List;

/**
 * Created by sungbo on 2016-08-23.
 */
public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.FeedBackViewHoder>{

    Context context;
    List<FeedbackHeader> feedbackHeaders;

    public FeedBackAdapter(Context context, List<FeedbackHeader> feedbackHeaders) {
        this.context = context;
        this.feedbackHeaders = feedbackHeaders;
    }

    @Override
    public FeedBackViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_card_item, null);

        return new FeedBackViewHoder(v);
    }

    @Override
    public void onBindViewHolder(FeedBackViewHoder holder, int i) {
        final FeedbackHeader vo = feedbackHeaders.get(i);

        if(vo.getType().equals("user")){
            holder.li_feed_back_send.setVisibility(View.VISIBLE);
            holder.li_feed_back_call.setVisibility(View.GONE);

            holder.user_conten.setText(vo.getContent());
            Glide.with(context)
                    .load(vo.getProfileimgurl())
                    .into(holder.userimage);
        }else{
            holder.li_feed_back_send.setVisibility(View.GONE);
            holder.li_feed_back_call.setVisibility(View.VISIBLE);

            holder.ins_content.setText(vo.getContent());

            holder.mRatingBar.setNumStars(5);
            holder.mRatingBar.setMax(5);
            holder.mRatingBar.setStepSize(0.5f);
            holder.mRatingBar.setRating(4.7f);

            Glide.with(context)
                    .load(vo.getInsprofileimgurl())
                    .into(holder.insimage);


        }

    }

    @Override
    public int getItemCount() {
        return feedbackHeaders.size();
    }


    public class FeedBackViewHoder extends RecyclerView.ViewHolder{

        private LinearLayout li_feed_back_call,li_feed_back_send;
        private TextView ins_content,user_conten;
        private ImageView userimage,insimage;
        private RatingBar mRatingBar;

        public FeedBackViewHoder(View itemView) {
            super(itemView);

            li_feed_back_call = (LinearLayout) itemView.findViewById(R.id.li_feed_back_call);
            li_feed_back_send = (LinearLayout) itemView.findViewById(R.id.li_feed_back_send);
            ins_content = (TextView) itemView.findViewById(R.id.inscontent);
            insimage = (ImageView) itemView.findViewById(R.id.insimage);
            user_conten = (TextView) itemView.findViewById(R.id.usercontent);
            userimage = (ImageView) itemView.findViewById(R.id.userimage);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.eval_ratingbar);

        }
    }

}
