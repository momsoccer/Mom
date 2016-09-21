package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.MissionMainActivity;
import com.mom.soccer.pubretropit.PubReport;
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
    private static final String TAG = "PassListAdapter";
    Activity activity;
    List<MissionPass> passes;
    User user;
    String missionPassFlag;
    int i = 0;
    Mission mission;

    View positiveAction;
    EditText report_content;

    private String getViewFlag = "N";

    public PassListAdapter(Activity activity, List<MissionPass> passes, User user, String missionPassFlag, Mission mission) {
        this.activity = activity;
        this.passes = passes;
        this.user = user;
        this.missionPassFlag = missionPassFlag;
        this.mission = mission;
    }

    public PassListAdapter(Activity activity, List<MissionPass> passes, User user, String missionPassFlag,String getViewFlag) {
        this.activity = activity;
        this.passes = passes;
        this.user = user;
        this.missionPassFlag = missionPassFlag;
        this.getViewFlag = getViewFlag;
    }

    @Override
    public PassItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_card_item, parent, false);

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

        holder.incomment.setText(pass.getInscomment());
        holder.failuredisp.setText(pass.getFailuredisp());

        if(pass.getStatus().equals("REQUEST")){
            holder.status.setText(activity.getString(R.string.user_mission_p));

            holder.title_failuredisp.setVisibility(View.GONE);
            holder.failuredisp.setVisibility(View.GONE);
            holder.title_comment.setVisibility(View.GONE);
            holder.incomment.setVisibility(View.GONE);

            holder.grade.setText(pass.getPassgrade()+activity.getString(R.string.missionpass_get_grade));

        }else if(pass.getStatus().equals("REJECT")){
            holder.status.setText(activity.getString(R.string.user_mission_fail));

            holder.title_failuredisp.setVisibility(View.VISIBLE);
            holder.failuredisp.setVisibility(View.VISIBLE);


            holder.grade.setText(pass.getPassgrade()+activity.getString(R.string.missionpass_get_grade3));

        }else if(pass.getStatus().equals("SUCCESS")){
            holder.status.setText(activity.getString(R.string.user_mission_y));

            holder.title_failuredisp.setVisibility(View.GONE);
            holder.failuredisp.setVisibility(View.GONE);

            holder.grade.setText(pass.getPassgrade()+activity.getString(R.string.missionpass_get_grade2));
        }

        if(pass.getMissiontype().equals("DRIBLE")){
            holder.li_back_color.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_drible)));
        }else if(pass.getMissiontype().equals("LIFTING")){
            holder.li_back_color.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_lifting)));
        }else if(pass.getMissiontype().equals("TRAPING")){
            holder.li_back_color.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_traping)));
        }else if(pass.getMissiontype().equals("AROUND")){
            holder.li_back_color.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_around)));
        }else if(pass.getMissiontype().equals("FLICK")){
            holder.li_back_color.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_flick)));
        }else if(pass.getMissiontype().equals("COMPLEX")){
            holder.li_back_color.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_complex)));
        }

        holder.btnHam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getViewFlag.equals("Y")){
                    new MaterialDialog.Builder(activity)
                            .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                            .title(R.string.mom_diaalog_alert)
                            .titleColor(activity.getResources().getColor(R.color.color6))
                            .content(R.string.user_mission_d_title5)
                            .contentColor(activity.getResources().getColor(R.color.color6))
                            .positiveText(R.string.mom_diaalog_confirm)
                            .show();
                    return;
                }


                PopupMenu p = new PopupMenu(activity,view);
                activity.getMenuInflater().inflate(R.menu.mission_pass_item, p.getMenu());
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        switch (item.getOrder()){
                            case 101:

                                if(pass.getStatus().equals("REQUEST")){
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
                                                    deletePass(pass);
                                                }
                                            })
                                            .negativeText(R.string.mom_diaalog_cancel)
                                            .show();
                                }else if(pass.getStatus().equals("RESULT")){ //심사 통과 된 상태
                                    new MaterialDialog.Builder(activity)
                                            .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                            .title(R.string.mom_diaalog_alert)
                                            .titleColor(activity.getResources().getColor(R.color.color6))
                                            .content(R.string.mom_diaalog_pass_eval)
                                            .contentColor(activity.getResources().getColor(R.color.color6))
                                            .positiveText(R.string.mom_diaalog_confirm)
                                            .show();
                                }else if(pass.getStatus().equals("REJECT")){

                                    //VeteranToast.makeToast(activity,"3:" + pass.toString(),Toast.LENGTH_SHORT).show();
                                    //Log.i(TAG,"4 : " + pass.toString());
                                    new MaterialDialog.Builder(activity)
                                            .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                            .title(R.string.mom_diaalog_alert)
                                            .titleColor(activity.getResources().getColor(R.color.color6))
                                            .content(R.string.mom_diaalog_pass_eval_re)
                                            .contentColor(activity.getResources().getColor(R.color.color6))
                                            .positiveText(R.string.mom_diaalog_confirm)
/*                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    deletePass();
                                                }
                                            })
                                            .negativeText(R.string.mom_diaalog_cancel)*/
                                            .show();
                                }

                                break;
                            case 102:
                                MaterialDialog dialog = new MaterialDialog.Builder(activity)
                                        .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                        .title(R.string.mom_diaalog_board_report)
                                        .titleColor(activity.getResources().getColor(R.color.color6))
                                        .customView(R.layout.dialog_report_view, true)
                                        .positiveText(R.string.momboard_edit_send)
                                        .negativeText(R.string.cancel)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Report report = new Report();
                                                report.setType(PubReport.REPORTTYPE_MISSION_PASS);
                                                report.setUid(user.getUid());
                                                report.setReason(report_content.getText().toString());
                                                report.setContent("Mission Pass complain : " + pass.getPassid());
                                                report.setPublisherid(pass.getInstructorid());
                                                PubReport.doReport(activity,report,user);
                                            }
                                        })
                                        .build();

                                report_content = (EditText) dialog.findViewById(R.id.report_content);
                                positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

                                report_content.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                                        positiveAction.setEnabled(s.toString().trim().length() > 0);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });

                                dialog.show();
                                positiveAction.setEnabled(false);

                                break;
                        }

                        return false;
                    }
                });
                p.show(); // 메뉴를 띄우기
            }

        });
    }

    public void deletePass(MissionPass pass){

        WaitingDialog.showWaitingDialog(activity,false);
        MissionPassService service = ServiceGenerator.createService(MissionPassService.class,activity,user);

        pass.setMissionPassFlag(missionPassFlag);

        Call<ServerResult> c = service.deletePass(pass);
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
                    intent.putExtra(MissionCommon.MISSIONTYPE,mission.getTypename());
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
        LinearLayout li_back_color;

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
            li_back_color = (LinearLayout) itemView.findViewById(R.id.li_back_color);


        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.mission_pass_item, menu);
        return true;
    }

}
