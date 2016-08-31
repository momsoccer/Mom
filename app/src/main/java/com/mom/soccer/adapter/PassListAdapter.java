package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.MissionMainActivity;
import com.mom.soccer.retrofitdao.MissionPassService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-29.
 */
public class PassListAdapter extends RecyclerView.Adapter<PassListAdapter.PassItemHolder>{

    Activity activity;
    List<MissionPass> passes;
    User user;
    String missionPassFlag;
    MissionPass missionPass;
    int i = 0;
    Mission mission;

    public PassListAdapter(Activity activity, List<MissionPass> passes, User user, String missionPassFlag, Mission mission) {
        this.activity = activity;
        this.passes = passes;
        this.user = user;
        this.missionPassFlag = missionPassFlag;
        this.mission = mission;
    }

    @Override
    public PassItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_card_item, null);
        return new PassItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PassItemHolder holder, int position) {

        final MissionPass pass = passes.get(position);


        if (!Compare.isEmpty(pass.getInsimge())) {
            Glide.with(activity)
                    .load(pass.getInsimge())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(holder.insimage);
        }

        holder.seq.setText(String.valueOf(pass.getSeq())+activity.getString(R.string.missionpass_get_seq));
        holder.date.setText(pass.getChange_updatedate());
        holder.insname.setText(pass.getInsname());
        holder.grade.setText(pass.getPassgrade()+activity.getString(R.string.missionpass_get_grade));
        holder.incomment.setText(pass.getInscomment());
        holder.failuredisp.setText(pass.getFailuredisp());

        if(pass.getStatus().equals("REQUEST")){
            holder.status.setText(activity.getString(R.string.user_mission_p));

            holder.title_failuredisp.setVisibility(View.GONE);
            holder.failuredisp.setVisibility(View.GONE);
            holder.title_comment.setVisibility(View.GONE);
            holder.incomment.setVisibility(View.GONE);

        }else if(pass.getStatus().equals("REJECT")){
            holder.status.setText(activity.getString(R.string.user_mission_fail));

            holder.title_failuredisp.setVisibility(View.VISIBLE);
            holder.failuredisp.setVisibility(View.VISIBLE);
        }else if(pass.getStatus().equals("SUCCESS")){
            holder.status.setText(activity.getString(R.string.user_mission_y));

            holder.title_failuredisp.setVisibility(View.GONE);
            holder.failuredisp.setVisibility(View.GONE);

        }


        holder.btnHam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu p = new PopupMenu(activity,view); //
                activity.getMenuInflater().inflate(R.menu.mission_pass_item, p.getMenu());
                // 이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getOrder()){
                            case 101:
                                //패스신청 취소하기
                                if(missionPass.getStatus().equals("REQUEST")){
                                    new MaterialDialog.Builder(activity)
                                            .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                            .title(R.string.mom_diaalog_pass_apply_cancel_msg)
                                            .titleColor(activity.getResources().getColor(R.color.color6))
                                            .content(R.string.mom_diaalog_pass_apply_msg4)
                                            .contentColor(activity.getResources().getColor(R.color.color6))
                                            .positiveText(R.string.mom_diaalog_confirm)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    deletePass();
                                                }
                                            })
                                            .negativeText(R.string.mom_diaalog_cancel)
                                            .show();
                                }else if(missionPass.getStatus().equals("RESULT")){ //심사 통과 된 상태
                                    new MaterialDialog.Builder(activity)
                                            .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                            .title(R.string.mom_diaalog_alert)
                                            .titleColor(activity.getResources().getColor(R.color.color6))
                                            .content(R.string.mom_diaalog_pass_eval)
                                            .contentColor(activity.getResources().getColor(R.color.color6))
                                            .positiveText(R.string.mom_diaalog_confirm)
                                            .show();
                                }else if(missionPass.getStatus().equals("REJECT")){
                                new MaterialDialog.Builder(activity)
                                        .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                        .title(R.string.mom_diaalog_alert)
                                        .titleColor(activity.getResources().getColor(R.color.color6))
                                        .content(R.string.mom_diaalog_pass_eval_re)
                                        .contentColor(activity.getResources().getColor(R.color.color6))
                                        .positiveText(R.string.mom_diaalog_confirm)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                deletePass();
                                            }
                                        })
                                        .negativeText(R.string.mom_diaalog_cancel)
                                        .show();

                                }

                            break;
                            case 102:
                                VeteranToast.makeToast(activity,"준비중입니다", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        return false;
                    }
                });
                p.show(); // 메뉴를 띄우기
            }
        });

    }

    public void deletePass(){
        WaitingDialog.showWaitingDialog(activity,false);
        MissionPassService service = ServiceGenerator.createService(MissionPassService.class,activity,user);

        missionPass.setMissionPassFlag(missionPassFlag);

        Call<ServerResult> c = service.deletePass(missionPass);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    //passes.remove(i);
                    //notifyDataSetChanged();
                    VeteranToast.makeToast(activity,activity.getString(R.string.missionpass_delete),Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(activity,MissionMainActivity.class);
                    intent.putExtra(MissionCommon.OBJECT,mission);
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

    @Override
    public int getItemCount() {
        return passes.size();
    }

    public class PassItemHolder extends RecyclerView.ViewHolder{

        TextView status,grade,failuredisp,incomment,date,seq,insname,title_failuredisp,title_comment;
        ImageView insimage;
        ImageButton btnHam;

        public PassItemHolder(View itemView) {
            super(itemView);
            insimage = (ImageView) itemView.findViewById(R.id.insimage);
            seq = (TextView) itemView.findViewById(R.id.seq);
            status = (TextView) itemView.findViewById(R.id.status);
            failuredisp = (TextView) itemView.findViewById(R.id.failuredisp);
            incomment = (TextView) itemView.findViewById(R.id.incomment);
            date = (TextView) itemView.findViewById(R.id.date);
            insname = (TextView) itemView.findViewById(R.id.insname);
            grade = (TextView) itemView.findViewById(R.id.grade);
            btnHam = (ImageButton) itemView.findViewById(R.id.btnHam);

            title_failuredisp = (TextView) itemView.findViewById(R.id.title_failuredisp);
            title_comment = (TextView) itemView.findViewById(R.id.title_comment);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.mission_pass_item, menu);
        return true;
    }

}