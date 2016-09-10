package com.mom.soccer.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
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


public class BoardLineItemAdapter  extends RecyclerView.Adapter<BoardLineItemAdapter.BoardItemViewHolder>{

    private static final String TAG = "BoardLineItemAdapter";
    private Activity activity;
    private List<MomBoard> boardList;
    private User user;

    public MomBoard getBoardLine(int position){
        return boardList.get(position);
    }


    public BoardLineItemAdapter(Activity activity, List<MomBoard> boardList,User user) {
        this.activity = activity;
        this.boardList = boardList;
        this.user = user;
    }


    @Override
    public BoardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.momboarder_line_item, parent, false);
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
        holder.level.setText(String.valueOf(vo.getLevel()));
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
        public TextView username,content,formatingdate,teamname,level;

        public BoardItemViewHolder(View itemView) {
            super(itemView);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            content = (TextView) itemView.findViewById(R.id.content);
            username = (TextView) itemView.findViewById(R.id.username);
            formatingdate = (TextView) itemView.findViewById(R.id.formatingdate);
            level = (TextView) itemView.findViewById(R.id.level);
        }
    }

    public void deleteLine(final int position){
        MomBoard query = new MomBoard();
        query.setLineid(boardList.get(position).getLineid());

        WaitingDialog.showWaitingDialog(activity,false);
        MomBoardService service = ServiceGenerator.createService(MomBoardService.class,activity,user);
        Call<ServerResult> c = service.deleteBoardLine(query);
        c.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    if(result.getResult().equals("S")){
                        removeItem(position);
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



}
