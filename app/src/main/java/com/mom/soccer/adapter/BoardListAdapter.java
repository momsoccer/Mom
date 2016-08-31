package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.board.BoardMainActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.CropCircleTransformation;
import com.mom.soccer.dto.Board;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.BoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
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
        TextView  text_comment;
        TextView  text_date;
        ImageView    btnHam;
    }

    public BoardListAdapter(Activity activity, List<Board> boardList,int uid,User user
    ) {
        this.activity = activity;
        this.boardList = boardList;
        this.currentUid = uid;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.user= user;
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
        BoardHolder boardHolder;

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

            boardHolder.text_username.setText(boardList.get(position).getUsername());

            boardHolder.text_username.setTextColor(activity.getResources().getColor(R.color.color6));

            boardHolder.text_comment.setText(boardList.get(position).getComment());
            boardHolder.text_date.setText(boardList.get(position).getChange_creationdate());

            if(!Compare.isEmpty(boardList.get(position).getProfileimgurl())){
                Glide.with(activity)
                        .load(boardList.get(position).getProfileimgurl())
                        .asBitmap().transform(new CropCircleTransformation(activity))
                        .into(boardHolder.image_userimg);
            }

            if(currentUid == boardList.get(position).getWriteuid()){
                boardHolder.text_username.setTextColor(activity.getResources().getColor(R.color.bg_screen3));
            }

            currentRow.setTag(boardHolder);

        }else{
            boardHolder = (BoardHolder) currentRow.getTag();

            if(boardList.get(position) != null){

                boardHolder.text_username.setTextColor(activity.getResources().getColor(R.color.color6));

                boardHolder.image_userimg = (ImageView) currentRow.findViewById(R.id.board_user_img);
                boardHolder.text_username.setText(boardList.get(position).getUsername());
                boardHolder.text_comment.setText(boardList.get(position).getComment());
                boardHolder.text_date.setText(boardList.get(position).getChange_creationdate());

                if(!Compare.isEmpty(boardList.get(position).getProfileimgurl())){
                    Glide.with(activity)
                            .load(boardList.get(position).getProfileimgurl())
                            .asBitmap().transform(new CropCircleTransformation(activity))
                            .into(boardHolder.image_userimg);
                }

                if(currentUid == boardList.get(position).getWriteuid()){
                    boardHolder.text_username.setTextColor(activity.getResources().getColor(R.color.bg_screen3));
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
                                //VeteranToast.makeToast(activity,"클릭한 것은 : " + board.getComment(),Toast.LENGTH_SHORT).show();

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

                                                Intent intent = new Intent(activity,BoardMainActivity.class);
                                                intent.putExtra("usermissionid",board.getUsermissionid());
                                                intent.putExtra("missionuid",board.getUid());
                                                activity.finish();
                                                activity.startActivity(intent);

                                                //boardList.remove(i);
                                                //notifyDataSetChanged();
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
                                VeteranToast.makeToast(activity,"준비중입니다", Toast.LENGTH_SHORT).show();
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
