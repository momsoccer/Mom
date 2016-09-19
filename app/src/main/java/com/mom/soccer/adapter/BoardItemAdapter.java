package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.mom.soccer.R;
import com.mom.soccer.ball.TeamBoardActivity;
import com.mom.soccer.ball.TeamBoardReply;
import com.mom.soccer.common.ActivityResultEvent;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.EventBus;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.pubretropit.PubReport;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.trservice.MomBoardTRService;
import com.mom.soccer.widget.WaitingDialog;
import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-14.
 */
public class BoardItemAdapter extends RecyclerView.Adapter<BoardItemAdapter.BoardItemViewHolder> {

    private static final String TAG = "BoardItemAdapter";
    private Activity activity;
    private List<MomBoard> boardList;
    private User user;
    private Instructor instructor;
    private static final int HEADER_BOARD_CODE = 205;
    private static final int COMMENT_LINE_CODE = 201;
    String callpage = "";

    View positiveAction;
    EditText report_content;

    public BoardItemAdapter(Activity activity, List<MomBoard> boardList,User user,Instructor instructor,String callpage) {
        this.activity = activity;
        this.boardList = boardList;
        this.user = user;
        this.instructor = instructor;
        this.callpage = callpage;
        EventBus.getInstance().register(this);
    }


    @Override
    public BoardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.momboarder_item, parent, false);
        return new BoardItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(BoardItemViewHolder holder, int i) {
        final MomBoard vo = boardList.get(i);
        final int posintion = i;

        if(!Compare.isEmpty(vo.getUserimg())){
            Glide.with(activity)
                    .load(vo.getUserimg())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(holder.userimg);
        }

        holder.level.setText(String.valueOf(vo.getLevel()));
        holder.username.setText(vo.getUsername());
        holder.formatingdate.setText(vo.getFormatDataSign());
        holder.content.setText(vo.getContent());
        holder.commentCount.setText(String.valueOf(vo.getCommentcount()));

        holder.liWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TeamBoardReply.class);
                intent.putExtra("boardid",vo.getBoardid());
                intent.putExtra("posintion",posintion);
                activity.startActivityForResult(intent,COMMENT_LINE_CODE);
            }
        });

        holder.liShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    final KakaoLink kakaoLink = KakaoLink.getKakaoLink(activity.getApplicationContext());
                    final KakaoTalkLinkMessageBuilder builder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                    builder.addText("몸 싸커에서 공유를 합니다");

                    String url = "http://192.168.0.3:8080/resources/ad/rds5.png";
                    builder.addImage(url,500,800);
                    builder.addAppButton("앱실행");
                    kakaoLink.sendMessage(builder,activity);

                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popupMenu = new PopupMenu(activity,view);
                activity.getMenuInflater().inflate(R.menu.momboard_menu, popupMenu.getMenu());

                final int i = posintion;

                if(user.getUid()==vo.getUid()){
                    popupMenu.getMenu().getItem(0).setVisible(false);
                    popupMenu.getMenu().getItem(1).setVisible(true);
                    popupMenu.getMenu().getItem(2).setVisible(true);
                    popupMenu.getMenu().getItem(3).setVisible(false);
                    popupMenu.getMenu().getItem(4).setVisible(false);

                    if(instructor.getInstructorid()!=0){
                        if(vo.getCategory().equals("A")){
                            popupMenu.getMenu().getItem(4).setVisible(true);
                            popupMenu.getMenu().getItem(0).setVisible(false);
                        }else{
                            popupMenu.getMenu().getItem(4).setVisible(false);
                            popupMenu.getMenu().getItem(0).setVisible(true);
                        }

                    }else{

                        popupMenu.getMenu().getItem(0).setVisible(false);

                    }

                }else{
                    popupMenu.getMenu().getItem(0).setVisible(false);
                    popupMenu.getMenu().getItem(1).setVisible(false);
                    popupMenu.getMenu().getItem(2).setVisible(false);

                    popupMenu.getMenu().getItem(3).setVisible(true);

                    popupMenu.getMenu().getItem(4).setVisible(false);
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getOrder()){
                            case 100: //글을 공지로 A 타입으로 바꾸어준다 header_update
                                MomBoard momBoard = new MomBoard();
                                momBoard.setCategory("A");
                                momBoard.setBoardid(vo.getBoardid());
                                MomBoardTRService.updateHeader(activity,user,momBoard,view,activity.getResources().getString(R.string.bottom_msg6));
                                updateHeader(i,momBoard);
                                break;
                            case 101:
                                    Intent intent = new Intent(activity,TeamBoardActivity.class);
                                    intent.putExtra("boardFlag","modify");
                                    intent.putExtra("boardid",vo.getBoardid());
                                    intent.putExtra("callpage",callpage);
                                    intent.putExtra("position",posintion);
                                    activity.startActivityForResult(intent,HEADER_BOARD_CODE);
                                break;
                            case 102:
                                new MaterialDialog.Builder(activity)
                                        .content(R.string.board_main_delete)
                                        .contentColor(activity.getResources().getColor(R.color.color6))
                                        .positiveText(R.string.mom_diaalog_confirm_y)
                                        .positiveColor(activity.getResources().getColor(R.color.enabled_red))
                                        .negativeText(R.string.mom_diaalog_cancel_n)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                deleteBoard(vo.getBoardid(),posintion);
                                            }
                                        })
                                        .show();
                                break;
                            case 103:
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
                                                report.setType(PubReport.REPORTTYPE_MOMBOARD_HEADER);
                                                report.setUid(user.getUid());
                                                report.setReason(report_content.getText().toString());
                                                report.setContent(vo.getContent());
                                                report.setPublisherid(vo.getUid());
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
                            case 104: //공지글 내리기
                                MomBoard board = new MomBoard();
                                board.setCategory("B");
                                board.setBoardid(vo.getBoardid());
                                MomBoardTRService.updateHeader(activity,user,board,view,activity.getResources().getString(R.string.bottom_msg7));
                                updateHeader(i,board);
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        //더보기 표현
        if(vo.getContent().length() > 99){
            holder.txbtnview.setVisibility(View.VISIBLE);
        }else{
            holder.txbtnview.setVisibility(View.GONE);
        }

        //Content click
        holder.liContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,TeamBoardReply.class);
                intent.putExtra("boardid",vo.getBoardid());
                intent.putExtra("posintion",posintion);
                activity.startActivityForResult(intent,COMMENT_LINE_CODE);
            }
        });

        if(vo.getCategory().equals("A")){
            holder.viewType.setBackground(activity.getResources().getDrawable(R.drawable.xml_list_divider_red));
        }else{
            holder.viewType.setBackground(activity.getResources().getDrawable(R.drawable.xml_list_divider));
        }

        if(vo.getFilecount()==0){
            holder.li_file_layout.setVisibility(View.GONE);
        }else{
            holder.li_file_layout.setVisibility(View.VISIBLE);

            for(int j=0 ; j < vo.getBoardFiles().size(); j++){

                if(j==0){
                    Glide.with(activity)
                            .load(vo.getBoardFiles().get(j).getFileaddr())
                            .fitCenter()
                            .thumbnail(0.1f)
                            .override(500,500)
                            .into(holder.attachImage1);
                }else if(j==1){
                    Glide.with(activity)
                            .load(vo.getBoardFiles().get(j).getFileaddr())
                            .fitCenter()
                            .thumbnail(0.1f)
                            .override(500,500)
                            .into(holder.attachImage2);
                }else if(j==2){
                    Glide.with(activity)
                            .load(vo.getBoardFiles().get(j).getFileaddr())
                            .fitCenter()
                            .thumbnail(0.1f)
                            .override(500,500)
                            .into(holder.attachImage3);
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public void updateLineItemCount(int position,int lineCount){
        boardList.get(position).setCommentcount(lineCount);
        notifyDataSetChanged();
    }


    public void updateHeader(int position,MomBoard momBoard){
        boardList.get(position).setCategory(momBoard.getCategory());
        notifyDataSetChanged();
    }

    //변경된 파일 보내기?
    public void updateHeaderImage(int position, MomBoard momBoard){
        boardList.remove(position);
        boardList.add(position,momBoard);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        boardList.remove(position);
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

    public class BoardItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView userimg,attachImage1,attachImage2,attachImage3;
        public TextView username,content,commentCount,formatingdate,teamname,txbtnview,level;
        private LinearLayout liWriteBtn,liShareBtn,liContent,li_file_layout;
        private ImageButton btnMenu;
        private View viewType;

        public BoardItemViewHolder(View itemView) {
            super(itemView);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            content = (TextView) itemView.findViewById(R.id.content);
            username = (TextView) itemView.findViewById(R.id.username);
            commentCount = (TextView) itemView.findViewById(R.id.commentCount);
            formatingdate = (TextView) itemView.findViewById(R.id.formatingdate);
            liWriteBtn = (LinearLayout) itemView.findViewById(R.id.liWriteBtn);
            liShareBtn = (LinearLayout) itemView.findViewById(R.id.liShareBtn);
            btnMenu = (ImageButton) itemView.findViewById(R.id.btnMenu);
            liContent = (LinearLayout) itemView.findViewById(R.id.liContent);
            txbtnview = (TextView) itemView.findViewById(R.id.txbtnview);
            viewType = itemView.findViewById(R.id.viewType);
            level = (TextView) itemView.findViewById(R.id.level);
            li_file_layout = (LinearLayout) itemView.findViewById(R.id.li_file_layout);

            attachImage1 = (ImageView) itemView.findViewById(R.id.attachImage1);
            attachImage2 = (ImageView) itemView.findViewById(R.id.attachImage2);
            attachImage3 = (ImageView) itemView.findViewById(R.id.attachImage3);
        }
    }

    public void deleteBoard(final int boardid, final int posintion){
        WaitingDialog.showWaitingDialog(activity,false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,activity,user);
        MomBoard query = new MomBoard();
        query.setBoardid(boardid);
        Call<ServerResult> deleteCall = momBoardService.deleteBoardheader(query);
        deleteCall.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    if(result.getResult().equals("S")){
                        removeItem(posintion);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    //about Menu attach
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.momboard_menu, menu);
        return true;
    }

    /* 메뉴 항목 조절 메소드
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setEnabled(true);
        menu.getItem(1).setEnabled(false);
        return true;
    }*/


    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent){
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
    }

    private void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case COMMENT_LINE_CODE:
                if(resultCode == activity.RESULT_OK){
                    int lineCount = data.getExtras().getInt("lineCount");
                    int posintion = data.getExtras().getInt("posintion");
                    updateLineItemCount(posintion,lineCount);
                }
                break;
        }
    }

}
