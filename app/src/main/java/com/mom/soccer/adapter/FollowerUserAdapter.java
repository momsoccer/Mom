package com.mom.soccer.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.dto.FollowManage;

import java.util.List;

/**
 * Created by sungbo on 2016-08-16.
 */
public class FollowerUserAdapter extends RecyclerView.Adapter<FollowerUserAdapter.FollewerItemViewHoder>{

    Context context;
    List<FollowManage> manageList;

    public FollowerUserAdapter(Context context, List<FollowManage> manageList) {
        this.context = context;
        this.manageList = manageList;
    }

    @Override
    public FollewerItemViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item_card, null);
        return new FollewerItemViewHoder(v);
    }

    @Override
    public void onBindViewHolder(FollewerItemViewHoder holder, final int position) {
        final FollowManage data = manageList.get(position);

        if(!Compare.isEmpty(data.getProfileimgurl())){
            Glide.with(context)
                    .load(data.getProfileimgurl())
                    .into(holder.imageView);
        }

        holder.username.setText(data.getUsername());
        holder.missioncount.setText(String.valueOf(data.getUsermissioncount()));

        holder.teamname.setText(data.getTeamname());
        holder.level.setText(String.valueOf(data.getLevel()));
        holder.score.setText(String.valueOf(data.getScore()));

        holder.followercount.setText(String.valueOf(data.getFollowercount()));
        holder.followingcount.setText(String.valueOf(data.getFollowingcount()));

        holder.mecommentcount.setText(String.valueOf(data.getMecommentcount()));
        holder.usercommentcount.setText(String.valueOf(data.getCommentcount()));

/*        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(data.getFollowtype()==1){
                    VeteranToast.makeToast(context,data.getUsername() + context.getString(R.string.follow_pickup_cancel) + position, Toast.LENGTH_SHORT).show();
                    remove(position);
                }

                return false;
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return manageList.size();
    }


    public class FollewerItemViewHoder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView   username;
        public TextView    teamname;
        public TextView    missioncount;
        public TextView    level;
        public TextView    score;
        public TextView    followercount;
        public TextView    followingcount;
        public TextView    mecommentcount;
        public TextView    usercommentcount;
        public CardView  cardview;


        public FollewerItemViewHoder(View v) {
            super(v);
            cardview = (CardView) v.findViewById(R.id.followr_cardview);
            username = (TextView) v.findViewById(R.id.username);
            imageView = (ImageView) v.findViewById(R.id.img);
            teamname = (TextView) v.findViewById(R.id.teamname);
            missioncount = (TextView) v.findViewById(R.id.missioncount);
            level = (TextView) v.findViewById(R.id.level);
            score = (TextView) v.findViewById(R.id.score);
            followercount = (TextView) v.findViewById(R.id.followercount);
            followingcount = (TextView) v.findViewById(R.id.followingcount);
            mecommentcount = (TextView) v.findViewById(R.id.mecomment);
            usercommentcount = (TextView) v.findViewById(R.id.usercomment);
        }
    }

    public void remove(int position) {
        Log.d("TAG","아이템 아이디는 : " +position);
        try {
            manageList.remove(position);
            notifyItemRemoved(position);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
