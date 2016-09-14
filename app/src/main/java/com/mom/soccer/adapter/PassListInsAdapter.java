package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.ins.InsDashboardActivity;
import com.mom.soccer.pubactivity.Param;
import com.mom.soccer.retrofitdao.MissionPassService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;
import com.mom.soccer.youtubeplayer.YoutubePlayerActivity;

import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** 강사가 유저 미션 패스하는 어댑터
 * Created by sungbo on 2016-08-30.
 */
public class PassListInsAdapter extends RecyclerView.Adapter<PassListInsAdapter.PassItemHolder>{

    private Activity activity;
    private List<MissionPass> missionPasses;
    private Instructor instructor;

    TextView text_mypoint;
    RadioGroup radioGroup;
    RadioButton pass, nopass;
    EditText et_f_reason,et_ins_comment;
    TextInputLayout layout_ins_comment,layout_f_reason;

    private String passFlag;

    public PassListInsAdapter(Activity activity, List<MissionPass> missionPasses, Instructor instructor) {
        this.activity = activity;
        this.missionPasses = missionPasses;
        this.instructor = instructor;
    }

    @Override
    public int getItemCount() {
        return missionPasses.size();
    }

    @Override
    public PassItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_card_ins_item, null);
        return new PassItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PassItemHolder h, int position) {
        final MissionPass vo = missionPasses.get(position);

        if (!Compare.isEmpty(vo.getUserimge())) {
            Glide.with(activity)
                    .load(vo.getUserimge())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(h.userimg);
        }

        if(!Compare.isEmpty(vo.getTeamname())){
            h.teamname.setText(vo.getTeamname());
        }else {
            h.teamname.setText(activity.getString(R.string.user_team_yet_join));
        }

        h.level.setText(String.valueOf(vo.getLevel()));
        h.date.setText(vo.getChange_creationdate());
        h.seq.setText(String.valueOf(vo.getSeq()));
        h.username.setText(vo.getUsername());
        h.missionname.setText(vo.getMissionname());




        h.user_video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getUservideo());
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

        h.mission_video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getMissionvideo());
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

        h.user_video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,vo.getUservideo());
                activity.startActivity(intent);
            }
        });

        h.mission_video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,vo.getMissionvideo());
                activity.startActivity(intent);
            }
        });

        h.resultbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insPassReason(vo);
            }
        });

    }

    //패스 또는 불합격
    public void insPassReason(final MissionPass missionPass){

        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                .title(R.string.ins_pass_title)
                .titleColor(activity.getResources().getColor(R.color.color6))
                .customView(R.layout.dialog_mission_pass_view, true)
                .positiveText(R.string.misison_pass_result)
                .negativeText(R.string.cancel)
                .widgetColor(activity.getResources().getColor(R.color.enabled_red))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(pass.isChecked()){
                            passFlag = "Y";
                            if(et_f_reason.getText().toString().length() > 0){
                                VeteranToast.makeToast(activity,activity.getString(R.string.mom_diaalog_pass_go_msg), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(et_ins_comment.getText().toString().length() <= 5){
                                VeteranToast.makeToast(activity,activity.getString(R.string.mom_diaalog_pass_go_msg1), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if(nopass.isChecked()){
                            passFlag = "N";
                            if(et_f_reason.getText().toString().length() <= 5){
                                VeteranToast.makeToast(activity,activity.getString(R.string.mom_diaalog_pass_go_msg2), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        WaitingDialog.showWaitingDialog(activity,false);
                        MissionPassService service = ServiceGenerator.createService(MissionPassService.class,activity,instructor);
                        MissionPass pass = new MissionPass();
                        pass.setPassid(missionPass.getPassid());
                        pass.setPassflag(passFlag);
                        pass.setUsermissionid(missionPass.getUsermissionid());
                        pass.setMissionid(missionPass.getMissionid());
                        pass.setUid(missionPass.getUid());
                        pass.setUsername(missionPass.getUsername());
                        pass.setInsname(instructor.getName()+":"+activity.getResources().getString(R.string.networkcheck5));

                        if(passFlag.equals("Y")){
                            pass.setInscomment(et_ins_comment.getText().toString());
                            pass.setStatus("SUCCESS");
                        }else{
                            pass.setStatus("REJECT");
                            pass.setInscomment(et_ins_comment.getText().toString());
                            pass.setFailuredisp(et_f_reason.getText().toString());
                        }

                        Call<ServerResult> c = service.updatePass(pass);
                        WaitingDialog.cancelWaitingDialog();
                        c.enqueue(new Callback<ServerResult>() {
                            @Override
                            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                                if (response.isSuccessful()) {
                                    Intent intent = new Intent(activity,InsDashboardActivity.class);
                                    intent.putExtra(Param.FRAGMENT_COUNT,1);
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
                })
                .build();
        dialog.show();


        radioGroup = (RadioGroup) dialog.getCustomView().findViewById(R.id.RadioGroup);
        pass = (RadioButton) dialog.getCustomView().findViewById(R.id.pass);
        nopass = (RadioButton) dialog.getCustomView().findViewById(R.id.nopass);
        et_f_reason = (EditText) dialog.getCustomView().findViewById(R.id.f_reason);
        et_ins_comment = (EditText) dialog.getCustomView().findViewById(R.id.ins_comment);

        text_mypoint = (TextView) dialog.getCustomView().findViewById(R.id.text_mypoint);
        NumberFormat numberFormat = NumberFormat.getInstance();
        text_mypoint.setText(numberFormat.format(missionPass.getCashpoint())+"P");
        layout_ins_comment = (TextInputLayout) dialog.getCustomView().findViewById(R.id.layout_ins_comment);
        layout_f_reason = (TextInputLayout) dialog.getCustomView().findViewById(R.id.layout_f_reason);
    }


    public class PassItemHolder extends RecyclerView.ViewHolder{

        TextView date,seq,username,teamname,missionname,level;
        ImageView userimg;
        Button resultbtn;
        YouTubeThumbnailView user_video_ThumbnailView,mission_video_ThumbnailView;

        public PassItemHolder(View itemView) {
            super(itemView);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            seq = (TextView) itemView.findViewById(R.id.seq);
            date = (TextView) itemView.findViewById(R.id.date);
            username = (TextView) itemView.findViewById(R.id.username);
            level = (TextView) itemView.findViewById(R.id.level);
            teamname = (TextView) itemView.findViewById(R.id.teamname);
            missionname = (TextView) itemView.findViewById(R.id.missionname);
            user_video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.user_video_ThumbnailView);
            mission_video_ThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.mission_video_ThumbnailView);
            resultbtn = (Button) itemView.findViewById(R.id.resultbtn);

        }
    }


}
