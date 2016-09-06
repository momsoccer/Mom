package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.ball.PlayerMainActivity;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.CropCircleTransformation;
import com.mom.soccer.dto.FriendApply;
import com.mom.soccer.dto.FriendReqVo;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.retrofitdao.FriendService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-24.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendItemViewHoder> {

    Activity activity;
    List<FriendReqVo> manageList;
    User user;

    private int uid=0;

    public FriendListAdapter(Activity activity, List<FriendReqVo> manageList, User user) {
        this.activity = activity;
        this.manageList = manageList;
        this.user = user;
    }

    @Override
    public FriendItemViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_card_item, null);
        return new FriendItemViewHoder(v);
    }

    @Override
    public void onBindViewHolder(FriendItemViewHoder holder, final int position) {
        final FriendReqVo data = manageList.get(position);


        if (!Compare.isEmpty(data.getProfileimgurl())) {
            Glide.with(activity)
                    .load(data.getProfileimgurl())
                    .asBitmap().transform(new CropCircleTransformation(activity))
                    .into(holder.imageView);
        }
        holder.username.setText(data.getUsername());
        holder.level.setText(String.valueOf(data.getLevel()));
        holder.score.setText(String.valueOf(data.getScore()));
        holder.creation_date.setText(data.getChange_creationdate());

        if(!Compare.isEmpty(data.getTeamname())){
            holder.teamname.setText(data.getTeamname());
        }else{
            holder.teamname.setText(activity.getResources().getString(R.string.user_team_yet_join));
        }

        if(data.getFlag().equals("ACCEPT")) {
            holder.acceptBtn.setText(activity.getString(R.string.friedn_friend_cancel));
            holder.rejectbtn.setVisibility(View.GONE);
            holder.frflag.setText("친구");
        }else{
            holder.frflag.setText("친구요청");
            holder.acceptBtn.setText(activity.getString(R.string.friend_re_btn));
            holder.rejectbtn.setVisibility(View.VISIBLE);
        }

        holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitingDialog.showWaitingDialog(activity,false);
                FriendService service = ServiceGenerator.createService(FriendService.class,activity,user);
                FriendApply friendApply = new FriendApply();
                friendApply.setRequid(data.getUid());
                friendApply.setResuid(user.getUid());
                Call<ServerResult> c = service.deleteFriend(friendApply);
                c.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            ServerResult result = response.body();
                            Intent intent = new Intent(activity,PlayerMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResult> call, Throwable t) {
                        WaitingDialog.cancelWaitingDialog();
                        t.printStackTrace();
                    }
                });
            }
        });


        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(data.getFlag().equals("ACCEPT")){
                    //친구삭제
                    WaitingDialog.showWaitingDialog(activity,false);
                    FriendService service = ServiceGenerator.createService(FriendService.class,activity,user);

                    FriendApply friendApply = new FriendApply();
                    friendApply.setRequid(data.getUid());
                    friendApply.setResuid(user.getUid());
                    Call<ServerResult> c = service.deleteFriend(friendApply);
                    c.enqueue(new Callback<ServerResult>() {
                        @Override
                        public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                            WaitingDialog.cancelWaitingDialog();
                            if(response.isSuccessful()){
                                ServerResult result = response.body();
                                Intent intent = new Intent(activity,PlayerMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Param.FRAGMENT_COUNT,3);
                                activity.startActivity(intent);
                            }else{

                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResult> call, Throwable t) {
                            WaitingDialog.cancelWaitingDialog();
                            t.printStackTrace();
                        }
                    });
                }else{
                    //친구추가
                    WaitingDialog.showWaitingDialog(activity,false);
                    FriendService service = ServiceGenerator.createService(FriendService.class,activity,user);

                    FriendApply friendApply = new FriendApply();
                    friendApply.setRequid(data.getUid());
                    friendApply.setResuid(user.getUid());
                    friendApply.setFlag("ACCEPT");
                    Call<ServerResult> c = service.updateFriendFlag(friendApply);
                    c.enqueue(new Callback<ServerResult>() {
                        @Override
                        public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                            WaitingDialog.cancelWaitingDialog();
                            if(response.isSuccessful()){
                                ServerResult result = response.body();
                                Intent intent = new Intent(activity,PlayerMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Param.FRAGMENT_COUNT,3);
                                activity.startActivity(intent);
                            }else{

                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResult> call, Throwable t) {
                            WaitingDialog.cancelWaitingDialog();
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        //요청된 사람 상세 정보
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MyPageActivity.class);
                intent.putExtra("pageflag","friend");
                intent.putExtra("frienduid",data.getUid());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return manageList.size();
    }


    public class FriendItemViewHoder extends RecyclerView.ViewHolder {

        public Button acceptBtn,rejectbtn;
        public CardView cardview;
        public ImageView imageView;
        public TextView username;
        public TextView level,teamname;
        public TextView score,creation_date,frflag;


        /*
        public TextView teamname;
        public TextView missioncount;
        public TextView followercount;
        public TextView followingcount;
        public TextView mecommentcount;
        public TextView usercommentcount;
        */



        public FriendItemViewHoder(View v) {
            super(v);
            cardview = (CardView) v.findViewById(R.id.friend_cardview);
            username = (TextView) v.findViewById(R.id.username);
            imageView = (ImageView) v.findViewById(R.id.userimg);
            level = (TextView) v.findViewById(R.id.level);
            score = (TextView) v.findViewById(R.id.score);
            acceptBtn = (Button) v.findViewById(R.id.acceptBtn);
            creation_date = (TextView) v.findViewById(R.id.creation_date);
            rejectbtn = (Button) v.findViewById(R.id.rejectbtn);
            frflag = (TextView) v.findViewById(R.id.frflag);
            teamname = (TextView) v.findViewById(R.id.teamname);


            /*
            teamname = (TextView) v.findViewById(R.id.teamname);
            missioncount = (TextView) v.findViewById(R.id.missioncount);
            followercount = (TextView) v.findViewById(R.id.followercount);
            followingcount = (TextView) v.findViewById(R.id.followingcount);
            mecommentcount = (TextView) v.findViewById(R.id.mecomment);
            usercommentcount = (TextView) v.findViewById(R.id.usercomment);
            */
        }
    }

}

