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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.TeamApply;
import com.mom.soccer.ins.InsDashboardActivity;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.retrofitdao.TeamService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** 강사가 유저 미션 패스하는 어댑터
 * Created by sungbo on 2016-08-30.
 */
public class TeamApplyAdapter extends RecyclerView.Adapter<TeamApplyAdapter.ApplyItemHolder>{


    private Activity activity;
    private List<TeamApply> teamApplies;
    private Instructor instructor;
    private int i;

    public TeamApplyAdapter(Activity activity, List<TeamApply> teamApplies, Instructor instructor) {
        this.activity = activity;
        this.instructor = instructor;
        this.teamApplies = teamApplies;
    }


    @Override
    public int getItemCount() {
        return teamApplies.size();
    }


    @Override
    public ApplyItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_apply_ins_item, null);
        return new ApplyItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ApplyItemHolder h, final int position) {
        final TeamApply vo = teamApplies.get(position);
        if (!Compare.isEmpty(vo.getUserimge())) {
            Glide.with(activity)
                    .load(vo.getUserimge())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(h.userimge);
        }

        h.date.setText(vo.getChange_creationdate());
        h.username.setText(vo.getUsername());
        h.level.setText(String.valueOf(vo.getLevel()));
        h.totalscore.setText(String.valueOf(vo.getTotalscore()));

        if(vo.getApproval().equals("APPROVAL")){
            h.li_btn.setVisibility(View.GONE); //승인버튼 화면
        }

        h.teamApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WaitingDialog.showWaitingDialog(activity,false);
                TeamService t = ServiceGenerator.createService(TeamService.class,activity,instructor);
                Call<ServerResult> c = t.acceptMember(vo.getApplyid());
                c.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            Intent intent = new Intent(activity,InsDashboardActivity.class);
                            intent.putExtra(Param.FRAGMENT_COUNT,2);
                            activity.finish();
                            activity.startActivity(intent);
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

        h.teamCanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //팀어플라이 삭제 후 노티 보낸다 거절시..
                WaitingDialog.showWaitingDialog(activity,false);
                TeamService t = ServiceGenerator.createService(TeamService.class,activity,instructor);
                Call<ServerResult> c = t.rejectMember(vo.getApplyid());
                c.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            Intent intent = new Intent(activity,InsDashboardActivity.class);
                            intent.putExtra(Param.FRAGMENT_COUNT,2);
                            activity.finish();
                            activity.startActivity(intent);
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

        h.userimge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,MyPageActivity.class);
                intent.putExtra("pageflag","friend");
                intent.putExtra("frienduid",vo.getUid());
                activity.finish();
                activity.startActivity(intent);
            }
        });

    }

    public class ApplyItemHolder extends RecyclerView.ViewHolder{

        TextView username,level,date,totalscore;
        ImageView userimge;
        Button teamApplyBtn,teamCanBtn;
        CardView cardview;
        LinearLayout li_btn;

        public ApplyItemHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.username);
            level = (TextView) v.findViewById(R.id.level);
            date = (TextView) v.findViewById(R.id.date);
            totalscore = (TextView) v.findViewById(R.id.totalscore);
            userimge = (ImageView) v.findViewById(R.id.userimg);
            teamApplyBtn = (Button) v.findViewById(R.id.teamApplyBtn);
            teamCanBtn = (Button) v.findViewById(R.id.teamCanBtn);
            cardview = (CardView) v.findViewById(R.id.cardview);
            li_btn = (LinearLayout) v.findViewById(R.id.li_btn);
        }
    }

/*
    public void accept(){
        WaitingDialog.showWaitingDialog(activity,false);
        TeamService teamService = ServiceGenerator.createService(TeamService.class,activity,instructor);
        Call<ServerResult> c = teamService.acceptMember(vo.getApplyid());
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult serverResult = response.body();
                    Intent intent = new Intent(activity,InsDashboardActivity.class);
                    intent.putExtra(Param.FRAGMENT_COUNT,2);
                    activity.finish();
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void reject(){
        WaitingDialog.showWaitingDialog(activity,false);
        TeamService teamService = ServiceGenerator.createService(TeamService.class,activity,instructor);
        Call<ServerResult> c = teamService.rejectMember(vo.getApplyid());
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult serverResult = response.body();
                    Intent intent = new Intent(activity,InsDashboardActivity.class);
                    intent.putExtra(Param.FRAGMENT_COUNT,2);
                    activity.finish();
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }
*/

}
