package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.FeedbackLine;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.MissionMainActivity;
import com.mom.soccer.pubretropit.PubReport;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;
import com.mom.soccer.youtubeplayer.YoutubePlayerActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-23.
 */
public class FeedBackAllListAdapter extends RecyclerView.Adapter<FeedBackAllListAdapter.ViewHoder>{

    Activity activity;
    List<FeedbackHeader> feedbackHeaders;
    User user;
    Mission mission;
    private String getViewFlag = "N";

    View positiveAction;
    EditText report_content;

    public FeedBackAllListAdapter(Activity context, List<FeedbackHeader> feedbackHeaders,User user,Mission mission) {
        this.activity = context;
        this.feedbackHeaders = feedbackHeaders;
        this.user = user;
        this.mission = mission;
    }

    public FeedBackAllListAdapter(Activity context, List<FeedbackHeader> feedbackHeaders,User user,String getViewFlag) {
        this.activity = context;
        this.feedbackHeaders = feedbackHeaders;
        this.user = user;
        this.getViewFlag = getViewFlag;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_all_list_item, null);

        return new ViewHoder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHoder holder, int i) {
        final FeedbackHeader vo = feedbackHeaders.get(i);

        if(!Compare.isEmpty(vo.getProfileimgurl())){
            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(holder.userimage);
        }
        if(!Compare.isEmpty(vo.getInsprofileimgurl())){
            Glide.with(activity)
                    .load(vo.getInsprofileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(holder.insimage);
        }

        holder.username.setText(vo.getUsername());
        holder.insname.setText(vo.getInsname());
        holder.content.setText(vo.getContent());
        holder.teamname.setText(vo.getTeamname());


        //조건
        if(vo.getReplaycount()==0){
            holder.status.setText(activity.getString(R.string.ins_text_feed_all_msg2));
            holder.youtubefeedback.setVisibility(View.GONE);

            holder.recontent.setText(activity.getString(R.string.ins_text_feed_all_msg6));
            holder.ratingbar.setIsIndicator(true);

        }else{
            holder.status.setText(activity.getString(R.string.ins_text_feed_all_msg1));
            holder.resubject.setText(vo.getInsname() + activity.getString(R.string.ins_text_feed_all_msg3));
            holder.recontent.setText(vo.getRecontent());

            LayerDrawable star =  (LayerDrawable) holder.ratingbar.getProgressDrawable();
            star.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            if (holder.ratingbar != null){
                holder.ratingbar.setNumStars(5);
                holder.ratingbar.setMax(5);
                holder.ratingbar.setStepSize(0.5f);
                holder.ratingbar.setRating(vo.getEvalscore());
            }

            if(vo.getFeedbacktype().equals("video")){
                holder.reqtype.setText(activity.getString(R.string.ins_text_feed_all_msg4));
                holder.youtubefeedback.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
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
                holder.youtubefeedback.setVisibility(View.GONE);
                holder.reqtype.setText(activity.getString(R.string.ins_text_feed_all_msg5));
            }
        }

        holder.ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

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

                FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,activity,user);
                WaitingDialog.showWaitingDialog(activity,false);

                FeedbackLine line = new FeedbackLine();
                line.setFeedbackid(vo.getFeedbackid());

                line.setFeedbacklineid(vo.getFeedbacklineid());
                line.setEvalscore(holder.ratingbar.getRating());

                Call<ServerResult> c = feedBackService.updateLine(line);
                c.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                        WaitingDialog.cancelWaitingDialog();
                        if(response.isSuccessful()){
                            ServerResult result = response.body();
                            VeteranToast.makeToast(activity,activity.getString(R.string.feedback_feedback_eval_save), Toast.LENGTH_SHORT).show();
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

                PopupMenu popupMenu = new PopupMenu(activity,view); //
                activity.getMenuInflater().inflate(R.menu.feedback_pop_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getOrder()){
                            case 101:
                                if(vo.getReplaycount()==0){
                                    FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,activity,user);
                                    WaitingDialog.showWaitingDialog(activity,false);

                                    FeedbackLine line = new FeedbackLine();
                                    line.setFeedbackid(vo.getFeedbackid());
                                    line.setFeedbacklineid(vo.getFeedbacklineid());

                                    Call<ServerResult> s = feedBackService.deleteLine(line);
                                    s.enqueue(new Callback<ServerResult>() {
                                        @Override
                                        public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                                            WaitingDialog.cancelWaitingDialog();
                                            if(response.isSuccessful()){
                                                ServerResult result = response.body();
                                                if(result.getResult().equals("S")){
                                                    VeteranToast.makeToast(activity,activity.getString(R.string.feedback_feedback_delete), Toast.LENGTH_SHORT).show();

                                                    //리프레쉬
                                                    Intent intent = new Intent(activity, MissionMainActivity.class);
                                                    intent.putExtra(MissionCommon.OBJECT,mission);
                                                    intent.putExtra(MissionCommon.MISSIONTYPE,mission.getTypename());
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    activity.startActivity(intent);

                                                }else if(result.getResult().equals("E")){
                                                    VeteranToast.makeToast(activity,activity.getString(R.string.feedback_feedback_not_delete), Toast.LENGTH_SHORT).show();
                                                }
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
                                    new MaterialDialog.Builder(activity)
                                            .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                            .title(R.string.mom_diaalog_alert)
                                            .titleColor(activity.getResources().getColor(R.color.color6))
                                            .content(R.string.feedback_feedback_not_delete)
                                            .contentColor(activity.getResources().getColor(R.color.color6))
                                            .positiveText(R.string.mom_diaalog_confirm)
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
                                                report.setType(PubReport.REPORTTYPE_FEEDBACK);
                                                report.setUid(user.getUid());
                                                report.setReason(report_content.getText().toString());
                                                report.setContent(vo.getContent());
                                                report.setPublisherid(vo.getInstructorid());
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
                popupMenu.show();
            }
        });


        holder.youtubefeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,vo.getVideoaddr());
                activity.startActivity(intent);
            }
        });

        if(vo.getUid() != user.getUid()){
            if(vo.getPubstatus().equals("N")){
                holder.li_no_feedback.setVisibility(View.VISIBLE);
                holder.li_feedback.setVisibility(View.GONE);
            }else{
                holder.li_no_feedback.setVisibility(View.GONE);
                holder.li_feedback.setVisibility(View.VISIBLE);
            }
        }else{
            holder.li_no_feedback.setVisibility(View.GONE);
            holder.li_feedback.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return feedbackHeaders.size();
    }


    public class ViewHoder extends RecyclerView.ViewHolder{

        TextView recontent,insname,resubject,status,content,teamname,username,subject,reqtype;

        ImageView userimage,insimage,btnHam;
        YouTubeThumbnailView youtubefeedback;
        RatingBar ratingbar;

        LinearLayout li_no_feedback,li_feedback;

        public ViewHoder(View v) {
            super(v);
            btnHam = (ImageView) v.findViewById(R.id.btnHam);
            subject = (TextView) v.findViewById(R.id.subject);
            userimage = (ImageView) v.findViewById(R.id.userimage);
            username = (TextView) v.findViewById(R.id.username);
            reqtype = (TextView) v.findViewById(R.id.reqtype);
            content = (TextView) v.findViewById(R.id.content);
            teamname = (TextView) v.findViewById(R.id.teamname);

            insname = (TextView) v.findViewById(R.id.insname);
            status = (TextView) v.findViewById(R.id.status);
            recontent = (TextView) v.findViewById(R.id.recontent);
            resubject = (TextView) v.findViewById(R.id.resubject);
            insimage = (ImageView) v.findViewById(R.id.insimage);
            youtubefeedback= (YouTubeThumbnailView) v.findViewById(R.id.youtubefeedback);
            ratingbar = (RatingBar) v.findViewById(R.id.ratingbar);


            li_no_feedback = (LinearLayout) itemView.findViewById(R.id.li_no_feedback);
            li_feedback = (LinearLayout) itemView.findViewById(R.id.li_feedback);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.feedback_pop_menu, menu);
        return true;
    }

    /*
    public void ratingEval(String trType, FeedbackLine line){
        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,activity,user);
        if(trType.equals("save")){
            WaitingDialog.showWaitingDialog(activity,false);
            line.setFeedbacklineid(line.getFeedbacklineid());
            line.setEvalscore(mRatingBar.getRating());

            Call<ServerResult> c = feedBackService.updateLine(line);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_eval_save) +mRatingBar.getRating() , Toast.LENGTH_SHORT).show();
                    }else{
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }else if(trType.equals("delete")){
            WaitingDialog.showWaitingDialog(activity,false);
            Call<ServerResult> s = feedBackService.deleteLine(line);
            s.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        if(result.getResult().equals("S")){
                            VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_delete), Toast.LENGTH_SHORT).show();

                            //리프레쉬
                            Intent intent = new Intent(getContext(), MissionMainActivity.class);
                            intent.putExtra(MissionCommon.OBJECT,mission);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);

                        }else if(result.getResult().equals("E")){
                            VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_not_delete), Toast.LENGTH_SHORT).show();
                        }
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
    */

}
