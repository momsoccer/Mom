package com.mom.soccer.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mom.soccer.R;

/**
 * Created by sungbo on 2016-08-14.
 */
public class BoardItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView userImg;
    public TextView username;
    public TextView comment;
    public TextView change_updatedate;
    public CardView cardview;


    public BoardItemViewHolder(View itemView) {
        super(itemView);
        this.userImg = (ImageView) itemView.findViewById(R.id.board_user_img);
        this.username = (TextView) itemView.findViewById(R.id.txt_board_user_name);
        this.comment = (TextView) itemView.findViewById(R.id.txt_board_user_comment);
        this.change_updatedate = (TextView) itemView.findViewById(R.id.txt_board_user_time);
        this.cardview = (CardView) itemView.findViewById(R.id.cardview);
    }
}
