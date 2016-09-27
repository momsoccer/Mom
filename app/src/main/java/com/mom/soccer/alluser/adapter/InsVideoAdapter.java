package com.mom.soccer.alluser.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.alluser.InsVideoAcView;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.EventBus;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsMainActivity;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.pubretropit.PubReport;
import com.mom.soccer.retrofitdao.InsVideoService;
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


    private Instructor currentins;

    View positiveAction;
    EditText report_content;

    public InsVideoAdapter(Activity activity, List<InsVideoVo> insVideoVos,User user) {
        this.activity = activity;
        this.insVideoVos = insVideoVos;
        this.user = user;
        EventBus.getInstance().register(this);
    }

    @Override
    public InsVideoViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ins_video_item_layout, parent, false);
        return new InsVideoViewHoder(v);
    }

    @Override
    public void onBindViewHolder(InsVideoViewHoder holder, int i) {
        final InsVideoVo vo = insVideoVos.get(i);
        final int posintion = i;
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
                Intent intent = new Intent(activity, InsVideoAcView.class);
                intent.putExtra(MissionCommon.INS_OBJECT,instructor);
                intent.putExtra(MissionCommon.OBJECT,vo);
                intent.putExtra("position",posintion);
                activity.startActivity(intent);

            }
        });


        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final PopupMenu popupMenu = new PopupMenu(activity,view);
                activity.getMenuInflater().inflate(R.menu.ins_video_menu, popupMenu.getMenu());
                final int i = posintion;

                if(vo.getInstructorid() == currentins.getInstructorid()){
                    popupMenu.getMenu().getItem(0).setVisible(true);
                    popupMenu.getMenu().getItem(1).setVisible(false);
                }else{
                    popupMenu.getMenu().getItem(0).setVisible(false);
                    popupMenu.getMenu().getItem(1).setVisible(true);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getOrder()){

                            case 101:
                                deleteInsVideo(vo.getVideoid(),view,i);
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
                                                report.setType(PubReport.REPORTTYPE_INS_VIDEO);
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

        getInsinfor(vo.getInstructorid());

        //공유하기
        holder.liShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Mom Soccer : 강사 강의 "+user.getUsername() +" : "+vo.getSubject());
                intent.putExtra(Intent.EXTRA_TEXT,"https://youtu.be/"+vo.getYoutubeaddr());
                intent.putExtra(Intent.EXTRA_TITLE, vo.getContent());
                activity.startActivity(Intent.createChooser(intent, "Mom Soccer"));
            }
        });

        //현재내가 강사인지 아닌지
        getCurrent();

        holder.subject.setText(vo.getSubject());
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

    //about Menu attach
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.ins_video_menu, menu);
        return true;
    }

    public void deleteInsVideo(final int videoid, final View view, final int position){
        WaitingDialog.showWaitingDialog(activity,false);
        InsVideoService insVideoService = ServiceGenerator.createService(InsVideoService.class,activity,user);
        InsVideoVo insVideoVo = new InsVideoVo();
        insVideoVo.setVideoid(videoid);
        Call<ServerResult> c = insVideoService.deleteVideo(insVideoVo);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    MomSnakBar.show(view,activity,activity.getResources().getString(R.string.ins_video_delete));
                    removeItem(position);
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    public void removeItem(int position){
        insVideoVos.remove(position);
        notifyItemRemoved(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reCallAdapter();
            }

        },1000);
    }
    public void reCallAdapter(){
        notifyDataSetChanged();
    }


    public void getCurrent(){
        InstructorService instructorService = ServiceGenerator.createService(InstructorService.class,activity,user);
        Call<Instructor> c = instructorService.getFindIns(user.getUid());
        c.enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                if(response.isSuccessful()){
                    currentins = response.body();
                }
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {

            }
        });
    }

    public void updateLineItemCount(int position,int lineCount){
        insVideoVos.get(position).setCommentcount(lineCount);
        notifyDataSetChanged();
    }

    public void updateLineItemLikeCount(int position,int lineCount){
        insVideoVos.get(position).setLikecount(lineCount);
        notifyDataSetChanged();
    }

}
