package com.mom.soccer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.dto.Board;
import com.mom.soccer.holder.BoardItemViewHolder;
import com.mom.soccer.widget.VeteranToast;

import java.util.List;

/**
 * Created by sungbo on 2016-08-14.
 */
public class BoardItemAdapter extends RecyclerView.Adapter<BoardItemViewHolder> {

    private static final String TAG = "BoardItemAdapter";
    private Context context;
    private List<Board> boardList;

    public BoardItemAdapter(Context context, List<Board> boardList) {
        this.context = context;
        this.boardList = boardList;
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
                    .into(holder.userImg);
        }

        holder.username.setText(boardList.get(i).getUsername());
        holder.comment.setText(boardList.get(i).getComment());
        holder.change_updatedate.setText(boardList.get(i).getChange_creationdate());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeteranToast.makeToast(context,"짧은 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                VeteranToast.makeToast(context,"롱클릭", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public void removeItem(int index){
        boardList.remove(index);
        notifyItemRemoved(index);
    }
}
