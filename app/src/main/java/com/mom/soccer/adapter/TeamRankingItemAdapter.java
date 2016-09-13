package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import com.mom.soccer.dataDto.TeamRankingVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsMainActivity;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-14.
 */
public class TeamRankingItemAdapter extends RecyclerView.Adapter<TeamRankingItemAdapter.RankingItemViewHolder> {

    private static final String TAG = "RankingItemAdapter";
    private Activity activity;
    private List<TeamRankingVo> rankingVos;
    private User user;
    private int pageSet=0;

    public TeamRankingItemAdapter(Activity activity, List<TeamRankingVo> rankingVos, User user,int pageSet) {
        this.activity = activity;
        this.rankingVos = rankingVos;
        this.user = user;
        this.pageSet = pageSet;
    }

    @Override
    public RankingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.adabter_team_layout, parent, false);
        return new RankingItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RankingItemViewHolder holder, int i) {

        final TeamRankingVo vo = rankingVos.get(i);

        if(!Compare.isEmpty(vo.getEmblem())){
            Glide.with(activity)
                    .load(vo.getEmblem())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(holder.teamimg);
        }

        holder.teamname.setText(vo.getTeamname());
        holder.rankingnum.setText(String.valueOf(i+1));
        holder.totalscore.setText(String.valueOf(vo.getTotalscore()));


        if(pageSet==0){
        }else {
        }

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doMoveInsPage(vo.getInstructorid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return rankingVos.size();
    }

    public class RankingItemViewHolder extends RecyclerView.ViewHolder {

        ImageView teamimg;
        TextView rankingnum,teamname,totalscore;
        CardView cardview;

        public RankingItemViewHolder(View itemView) {
            super(itemView);

            teamimg = (ImageView) itemView.findViewById(R.id.teamimage);
            rankingnum = (TextView) itemView.findViewById(R.id.rankingnum);
            teamname = (TextView) itemView.findViewById(R.id.teamname);
            totalscore = (TextView) itemView.findViewById(R.id.totalscore);
            cardview = (CardView) itemView.findViewById(R.id.cardview);

        }
    }

    public void doMoveInsPage(int insid){
        WaitingDialog.showWaitingDialog(activity,false);
        InstructorService service = ServiceGenerator.createService(InstructorService.class,activity,user);
        Instructor instructor = new Instructor();
        instructor.setInstructorid(insid);
        Call<Instructor> instructorCall = service.insLogin(instructor);
        instructorCall.enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    Instructor ins = response.body();
                    Intent intent = new Intent(activity, InsMainActivity.class);
                    intent.putExtra("inspath","search");
                    intent.putExtra(MissionCommon.INS_OBJECT,ins);
                    intent.putExtra("gopage",1);
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

}
