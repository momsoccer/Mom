package com.mom.soccer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.dto.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungbo on 2016-08-01.
 */
public class BoardListAdapter extends BaseAdapter {

    private static final String TAG = "BoardListAdapter";
    private Context context = null;
    private LayoutInflater inflater = null;
    private List<Board> boardList = new ArrayList<Board>();
    private int currentUid = 0;

    static class BoardHolder{
        YouTubeThumbnailView thumb;
        ImageView image_userimg;
        TextView  text_username;
        TextView  text_comment;
        TextView  text_date;
    }

    public BoardListAdapter(Context context, List<Board> boardList,int uid) {
        this.context = context;
        this.boardList = boardList;
        this.currentUid = uid;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentRow = convertView;
        BoardHolder boardHolder;

        if(convertView==null){


            currentRow = inflater.inflate(R.layout.board_list_item_layout, parent, false);

            boardHolder = new BoardHolder();

            //내가 쓴글은 파란색으로


            boardHolder.image_userimg = (ImageView) currentRow.findViewById(R.id.board_user_img);
            boardHolder.text_username = (TextView) currentRow.findViewById(R.id.txt_board_user_name);
            boardHolder.text_comment = (TextView) currentRow.findViewById(R.id.txt_board_user_comment);
            boardHolder.text_date = (TextView) currentRow.findViewById(R.id.txt_board_user_time);

            boardHolder.text_username.setText(boardList.get(position).getUsername());

            boardHolder.text_username.setTextColor(context.getResources().getColor(R.color.color6));

            boardHolder.text_comment.setText(boardList.get(position).getComment());
            boardHolder.text_date.setText(boardList.get(position).getChange_creationdate());

            if(!Compare.isEmpty(boardList.get(position).getProfileimgurl())){
                Glide.with(context)
                        .load(boardList.get(position).getProfileimgurl())
                        .into(boardHolder.image_userimg);
            }

            if(currentUid == boardList.get(position).getWriteuid()){
                boardHolder.text_username.setTextColor(context.getResources().getColor(R.color.bg_screen3));
            }

            currentRow.setTag(boardHolder);

        }else{
            boardHolder = (BoardHolder) currentRow.getTag();

            if(boardList.get(position) != null){

                boardHolder.text_username.setTextColor(context.getResources().getColor(R.color.color6));

                boardHolder.image_userimg = (ImageView) currentRow.findViewById(R.id.board_user_img);
                boardHolder.text_username.setText(boardList.get(position).getUsername());
                boardHolder.text_comment.setText(boardList.get(position).getComment());
                boardHolder.text_date.setText(boardList.get(position).getChange_creationdate());

                if(!Compare.isEmpty(boardList.get(position).getProfileimgurl())){
                    Glide.with(context)
                            .load(boardList.get(position).getProfileimgurl())
                            .into(boardHolder.image_userimg);
                }

                if(currentUid == boardList.get(position).getWriteuid()){
                    boardHolder.text_username.setTextColor(context.getResources().getColor(R.color.bg_screen3));
                }

            }

        }

        return currentRow;
    }
}
