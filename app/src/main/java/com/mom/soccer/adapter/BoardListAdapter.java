package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.CropCircleTransformation;
import com.mom.soccer.dto.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungbo on 2016-08-01.
 */
public class BoardListAdapter extends BaseAdapter {

    private static final String TAG = "BoardListAdapter";
    private Activity activity = null;
    private LayoutInflater inflater = null;
    private List<Board> boardList = new ArrayList<Board>();
    private int currentUid = 0;

    static class BoardHolder{
        YouTubeThumbnailView thumb;
        ImageView image_userimg;
        TextView  text_username;
        TextView  text_comment;
        TextView  text_date;
        ImageView    btnHam;
    }

    public BoardListAdapter(Activity activity, List<Board> boardList,int uid) {
        this.activity = activity;
        this.boardList = boardList;
        this.currentUid = uid;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                activity.getMenuInflater().inflate(R.menu.feedback_pop_menu, p.getMenu());
                // 이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(activity, "팝업메뉴 이벤트 처리 - "+item.getTitle(), Toast.LENGTH_SHORT).show();
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
        //위에서 만들었던 xml 파일의 리소스아이디값을 넘겨줍니다.
        inflater.inflate(R.menu.feedback_pop_menu, menu);
        return true;
    }
}
