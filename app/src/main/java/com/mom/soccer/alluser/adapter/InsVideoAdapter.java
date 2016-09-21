package com.mom.soccer.alluser.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.alluser.InsVideoActivity;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsMainActivity;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InsVideoAdapter extends RecyclerView.Adapter<InsVideoAdapter.InsVideoViewHoder>{

    private static final String TAG = "InsVideoAdapter";

    private Activity activity;
    private List<InsVideoVo> insVideoVos;
    private User user;
    private Instructor instructor;

    public InsVideoAdapter(Activity activity, List<InsVideoVo> insVideoVos,User user) {
        this.activity = activity;
        this.insVideoVos = insVideoVos;
        this.user = user;
    }

    @Override
    public InsVideoViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ins_video_item_layout, parent, false);
        return new InsVideoViewHoder(v);
    }

    @Override
    public void onBindViewHolder(InsVideoViewHoder holder, int position) {
        final InsVideoVo vo = insVideoVos.get(position);
        if(!Compare.isEmpty(vo.getInsimage())){
            Glide.with(activity)
                    .load(vo.getInsimage())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(holder.insimage);
        }

        holder.insname.setText(vo.getInsname());
        holder.teamname.setText(vo.getTeamname());
        holder.formatingdate.setText(vo.getFormatDataSign());
        holder.likecount.setText(String.valueOf(vo.getLikecount()));
        holder.commentcount.setText(String.valueOf(vo.getCommentcount()));

        holder.ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getYoutubeaddr());
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        holder.insimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,InsMainActivity.class);
                intent.putExtra("inspath","search");
                intent.putExtra(MissionCommon.INS_OBJECT,instructor);
                intent.putExtra("goPage",0);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        holder.li_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, InsVideoActivity.class);
                intent.putExtra(MissionCommon.INS_OBJECT,instructor);
                intent.putExtra(MissionCommon.OBJECT,vo);
                intent.putExtra(MissionCommon.USER_OBJECT,user);
                intent.putExtra("viewtype","view");
                activity.startActivity(intent);

            }
        });


        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getInsinfor(vo.getInstructorid());
    }

    @Override
    public int getItemCount() {
        return insVideoVos.size();
    }

    public class InsVideoViewHoder extends RecyclerView.ViewHolder{

        CardView cardview;
        TextView insname,teamname,likecount,commentcount,formatingdate,subject;
        LinearLayout liWriteBtn,liShareBtn,li_content;
        ImageButton btnMenu;
        ImageView insimage;

        YouTubeThumbnailView ThumbnailView;

        public InsVideoViewHoder(View view) {
            super(view);

            cardview = (CardView) view.findViewById(R.id.cardview);
            insname = (TextView) view.findViewById(R.id.insname);
            teamname = (TextView) view.findViewById(R.id.teamname);
            likecount = (TextView) view.findViewById(R.id.likecount);
            commentcount = (TextView) view.findViewById(R.id.commentCount);
            formatingdate = (TextView) view.findViewById(R.id.formatingdate);
            subject = (TextView) view.findViewById(R.id.subject);

            liWriteBtn = (LinearLayout) view.findViewById(R.id.liWriteBtn);
            liShareBtn = (LinearLayout) view.findViewById(R.id.liShareBtn);
            li_content = (LinearLayout) view.findViewById(R.id.li_content);

            btnMenu = (ImageButton) view.findViewById(R.id.btnMenu);
            insimage = (ImageView) view.findViewById(R.id.insimage);
            ThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.ThumbnailView);
        }
    }

    public void getInsinfor(int insid){
        WaitingDialog.showWaitingDialog(activity,false);
        InstructorService instructorService = ServiceGenerator.createService(InstructorService.class,activity,user);
        Instructor query = new Instructor();
        query.setInstructorid(insid);

        Call<Instructor> c = instructorService.insLogin(query);
        c.enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    instructor = response.body();
                }
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }
}
