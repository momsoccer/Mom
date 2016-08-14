package com.mom.soccer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mom.soccer.R;

/**
 * Created by sungbo on 2016-08-14.
 */
public class BoardItemViewHoder extends RecyclerView.ViewHolder {

    public ImageView userImg;
    public TextView username;
    public TextView comment;
    public TextView change_updatedate;


    public BoardItemViewHoder(View itemView) {
        super(itemView);
        this.userImg = (ImageView) itemView.findViewById(R.id.board_user_img);
        this.userImg = (ImageView) itemView.findViewById(R.id.board_user_img);
        this.username = (TextView) itemView.findViewById(R.id.txt_board_user_name);
        this.comment = (TextView) itemView.findViewById(R.id.txt_board_user_comment);
        this.change_updatedate = (TextView) itemView.findViewById(R.id.txt_board_user_time);


    }
}
