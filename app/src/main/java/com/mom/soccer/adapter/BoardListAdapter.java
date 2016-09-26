package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.Board;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.pubretropit.PubReport;
import com.mom.soccer.retrofitdao.BoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-01.
 */
public class BoardListAdapter extends BaseAdapter {

    private static final String TAG = "BoardListAdapter";
    private Activity activity = null;
    private LayoutInflater inflater = null;
    private List<Board> boardList = new ArrayList<Board>();
    private int currentUid = 0;
    UserMission usermission;

    View positiveAction;
    EditText report_content;

    private int writeuid = 0;

    private User user;
    private int missionuid = 0;
    private int usermissionid = 0;
    private int boardid = 0;
    private int i = 0;

    private int missionid;


    static class BoardHolder{
        YouTubeThumbnailView thumb;
        ImageView image_userimg;
        TextView  text_username;
        TextView txt_board_team_name;
        TextView  text_comment;
        TextView  text_date;
        ImageView    btnHam;
        TextView  level;
        View li_view_line;
    }

    public BoardListAdapter(Activity activity, List<Board> boardList,int uid,User user,UserMission usermission
    ) {
        this.activity = activity;
        this.boardList = boardList;
        this.currentUid = uid;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.user= user;
        this.usermission = usermission;
    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return boardList.get(position).getBoardid();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View currentRow = convertView;
        final BoardHolder boardHolder;
        final Board board = boardList.get(position);
        writeuid = board.getWriteuid();
        i  = position;


        if(convertView==null){
            currentRow = inflater.inflate(R.layout.board_list_item_layout, parent, false);
            boardHolder = new BoardHolder();

            //내가 쓴글은 파란색으로

            boardHolder.btnHam = (ImageView) currentRow.findViewById(R.id.btnHam);
            boardHolder.image_userimg = (ImageView) currentRow.findViewById(R.id.board_user_img);
            boardHolder.text_username = (TextView) currentRow.findViewById(R.id.txt_board_user_name);
            boardHolder.text_comment = (TextView) currentRow.findViewById(R.id.txt_board_user_comment);
            boardHolder.text_date = (TextView) currentRow.findViewById(R.id.txt_board_user_time);
            boardHolder.level = (TextView) currentRow.findViewById(R.id.level);
            boardHolder.txt_board_team_name = (TextView) currentRow.findViewById(R.id.txt_board_team_name);
            boardHolder.li_view_line = currentRow.findViewById(R.id.li_view_line);

            boardHolder.text_username.setText(boardList.get(position).getUsername());
            boardHolder.text_username.setTextColor(activity.getResources().getColor(R.color.color6));


            if(!Compare.isEmpty(boardList.get(position).getTeamname())){
                boardHolder.txt_board_team_name.setText(boardList.get(position).getTeamname());
            }else{
                boardHolder.txt_board_team_name.setText(activity.getResources().getString(R.string.user_team_yet_join));
            }

            boardHolder.text_comment.setText(boardList.get(position).getComment());
            boardHolder.text_date.setText(boardList.get(position).getFormatDataSign());
            boardHolder.level.setText(String.valueOf(boardList.get(position).getLevel()));

            if(!Compare.isEmpty(boardList.get(position).getProfileimgurl())){
                Glide.with(activity)
                        .load(boardList.get(position).getProfileimgurl())
                        .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                        .into(boardHolder.image_userimg);
            }

            if(currentUid == boardList.get(position).getWriteuid()){
                boardHolder.text_username.setTextColor(activity.getResources().getColor(R.color.bg_screen3));
            }

            if(boardList.get(position).getMissiontype().equals("DRIBLE")){
                boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_drible)));
            }else if(boardList.get(position).getMissiontype().equals("LIFTING")){
                boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_lifting)));
            }else if(boardList.get(position).getMissiontype().equals("TRAPING")){
                boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_traping)));
            }else if(boardList.get(position).getMissiontype().equals("AROUND")){
                boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_around)));
            }else if(boardList.get(position).getMissiontype().equals("FLICK")){
                boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_flick)));
            }else if(boardList.get(position).getMissiontype().equals("COMPLEX")){
                boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_complex)));
            }

            currentRow.setTag(boardHolder);

        }else{
            boardHolder = (BoardHolder) currentRow.getTag();

            if(boardList.get(position) != null){

                boardHolder.text_username.setTextColor(activity.getResources().getColor(R.color.color6));
                boardHolder.li_view_line = currentRow.findViewById(R.id.li_view_line);
                boardHolder.image_userimg = (ImageView) currentRow.findViewById(R.id.board_user_img);
                boardHolder.text_username.setText(boardList.get(position).getUsername());
                boardHolder.text_comment.setText(boardList.get(position).getComment());
                boardHolder.text_date.setText(boardList.get(position).getChange_creationdate());
                boardHolder.level.setText(String.valueOf(boardList.get(position).getLevel()));

                if(!Compare.isEmpty(boardList.get(position).getProfileimgurl())){
                    Glide.with(activity)
                            .load(boardList.get(position).getProfileimgurl())
                            .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                            .into(boardHolder.image_userimg);
                }

                if(currentUid == boardList.get(position).getWriteuid()){
                    boardHolder.text_username.setTextColor(activity.getResources().getColor(R.color.bg_screen3));
                }

                if(!Compare.isEmpty(boardList.get(position).getTeamname())){
                    boardHolder.txt_board_team_name.setText(boardList.get(position).getTeamname());
                }else{
                    boardHolder.txt_board_team_name.setText(activity.getResources().getString(R.string.user_team_yet_join));
                }

                if(boardList.get(position).getMissiontype().equals("DRIBLE")){
                    boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_drible)));
                }else if(boardList.get(position).getMissiontype().equals("LIFTING")){
                    boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_lifting)));
                }else if(boardList.get(position).getMissiontype().equals("TRAPING")){
                    boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_traping)));
                }else if(boardList.get(position).getMissiontype().equals("AROUND")){
                    boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_around)));
                }else if(boardList.get(position).getMissiontype().equals("FLICK")){
                    boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_flick)));
                }else if(boardList.get(position).getMissiontype().equals("COMPLEX")){
                    boardHolder.li_view_line.setBackground(activity.getResources().getDrawable((R.drawable.xml_back_complex)));
                }

            }
        }



        boardHolder.btnHam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu p = new PopupMenu(activity,view); //
                activity.getMenuInflater().inflate(R.menu.board_menu, p.getMenu());
                // 이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getOrder()){
                            case 101:
                                if(board.getWriteuid() == currentUid){
                                    WaitingDialog.showWaitingDialog(activity,false);
                                    //자기가 쓴글 삭제할 수 있음
                                    BoardService boardService = ServiceGenerator.createService(BoardService.class,activity,user);
                                    Call<ServerResult> c = boardService.deleteBoard(board.getBoardid());
                                    c.enqueue(new Callback<ServerResult>() {
                                        @Override
                                        public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                                            WaitingDialog.cancelWaitingDialog();
                                            if(response.isSuccessful()){

                                                Intent intent = new Intent(activity,UserMissionActivity.class);
                                                intent.putExtra(MissionCommon.USER_MISSTION_OBJECT,usermission);
                                                //intent.putExtra("usermissionid",board.getUsermissionid());
                                                //intent.putExtra("missionuid",board.getUid());
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
                                }else{
                                    new MaterialDialog.Builder(activity)
                                            .icon(activity.getResources().getDrawable(R.drawable.ic_alert_title_mom))
                                            .title(R.string.mom_diaalog_alert)
                                            .titleColor(activity.getResources().getColor(R.color.color6))
                                            .content(R.string.board_caustion)
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
                                                report.setType(PubReport.REPORTTYPE_MISSION_COMMENT);
                                                report.setUid(user.getUid());
                                                report.setReason(report_content.getText().toString());
                                                report.setContent(board.getComment());
                                                report.setPublisherid(board.getWriteuid());
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

        return currentRow;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.board_menu, menu);
        return true;
    }

}
