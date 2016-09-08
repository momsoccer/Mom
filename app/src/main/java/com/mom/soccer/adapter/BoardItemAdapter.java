package com.mom.soccer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.Board;
import com.mom.soccer.dto.User;

import java.util.List;

/**
 * Created by sungbo on 2016-08-14.
 */
public class BoardItemAdapter extends RecyclerView.Adapter<BoardItemAdapter.BoardItemViewHolder> {

    private static final String TAG = "BoardItemAdapter";
    private Context context;
    private List<Board> boardList;
    private User user;

    public BoardItemAdapter(Context context, List<Board> boardList,User user) {
        this.context = context;
        this.boardList = boardList;
        this.user = user;
    }

    @Override
    public BoardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_list_item_layout, parent, false);

        return new BoardItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(BoardItemViewHolder holder, int i) {

        Log.i(TAG,"보더 리스트 =============================" + i );

        if(!Compare.isEmpty(boardList.get(i).getProfileimgurl())){
            Glide.with(context)
                    .load(boardList.get(i).getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(context, 10, 5))
                    .into(holder.userImg);
        }

        holder.username.setText(boardList.get(i).getUsername());
        holder.comment.setText(boardList.get(i).getComment());
        holder.change_updatedate.setText(boardList.get(i).getFormatDataSign());
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

/*    public void removeItem(int index){
        boardList.remove(index);
        notifyItemRemoved(index);
    }*/

    public class BoardItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView userImg;
        public TextView username;
        public TextView comment;
        public TextView change_updatedate;


        public BoardItemViewHolder(View itemView) {
            super(itemView);
            this.userImg = (ImageView) itemView.findViewById(R.id.board_user_img);
            this.username = (TextView) itemView.findViewById(R.id.txt_board_user_name);
            this.comment = (TextView) itemView.findViewById(R.id.txt_board_user_comment);
            this.change_updatedate = (TextView) itemView.findViewById(R.id.txt_board_user_time);
        }
    }
}
