package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.UserRangkinVo;
import com.mom.soccer.dto.User;

import java.util.List;

/**
 * Created by sungbo on 2016-08-14.
 */
public class RankingItemAdapter extends RecyclerView.Adapter<RankingItemAdapter.RankingItemViewHolder> {

    private static final String TAG = "RankingItemAdapter";
    private Activity activity;
    private List<UserRangkinVo> rankingVos;
    private User user;

    public RankingItemAdapter(Activity activity, List<UserRangkinVo> rankingVos, User user) {
        this.activity = activity;
        this.rankingVos = rankingVos;
        this.user = user;
    }

    @Override
    public RankingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.adabter_mainlist_layout, parent, false);
        return new RankingItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RankingItemViewHolder holder, int i) {

        final UserRangkinVo vo = rankingVos.get(i);

        if(!Compare.isEmpty(vo.getProfileimgurl())){
            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .placeholder(activity.getResources().getDrawable(R.drawable.userimg))
                    .into(holder.userimg);
        }

        holder.username.setText(vo.getUsername());
        holder.teamname.setText(vo.getTeamname());
        holder.totalscore.setText(vo.getTotalscore());
        holder.level.setText(String.valueOf(vo.getLevel()));
        holder.rankingnum.setText(String.valueOf(i+1));


        holder.userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,MyPageActivity.class);

                if(user.getUid() == vo.getUid()){
                    intent.putExtra("pageflag","me");
                }else{
                    intent.putExtra("pageflag","friend");
                    intent.putExtra("frienduid",vo.getUid());
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rankingVos.size();
    }

    public class RankingItemViewHolder extends RecyclerView.ViewHolder {

        TextView level,username,teamname,totalscore,rankingnum;
        ImageView userimg;

        public RankingItemViewHolder(View itemView) {
            super(itemView);

            rankingnum = (TextView) itemView.findViewById(R.id.rankingnum);
            level = (TextView) itemView.findViewById(R.id.level);
            username = (TextView) itemView.findViewById(R.id.username);
            teamname = (TextView) itemView.findViewById(R.id.teamname);
            totalscore = (TextView) itemView.findViewById(R.id.totalscore);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);

        }
    }


}
