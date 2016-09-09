package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.ball.TeamBoardReply;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

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

    public BoardItemAdapter(Activity activity, List<MomBoard> boardList,User user) {
        this.activity = activity;
        this.boardList = boardList;
        this.user = user;
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

        holder.username.setText(vo.getUsername());
        holder.formatingdate.setText(vo.getFormatDataSign());
        holder.content.setText(vo.getContent());
        holder.commentCount.setText(String.valueOf(vo.getLikecount()));

        holder.liWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TeamBoardReply.class);
                intent.putExtra("boardid",vo.getBoardid());
                activity.startActivity(intent);
            }
        });

        holder.liShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(activity,view);
                activity.getMenuInflater().inflate(R.menu.momboard_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getOrder()){
                            case 101:

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

                                                //VeteranToast.makeToast(activity,"오잉 : " + vo.getBoardid(), Toast.LENGTH_SHORT).show();
                                                deleteBoard(vo.getBoardid(),posintion);
                                            }
                                        })
                                        .show();
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

            }
        });
    }

    @Override
    public int getItemCount() {
        return boardList.size();
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

        public ImageView userimg;
        public TextView username,content,commentCount,formatingdate,teamname,txbtnview;
        private LinearLayout liWriteBtn,liShareBtn,liContent;
        private ImageButton btnMenu;

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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.momboard_menu, menu);
        return true;
    }

}
