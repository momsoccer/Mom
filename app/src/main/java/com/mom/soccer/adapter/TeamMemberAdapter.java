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
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.UserMainVo;
import com.mom.soccer.dto.User;

import java.util.List;

/**
 * Created by sungbo on 2016-08-14.
 */
public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberItem> {

    private static final String TAG = "TeamMemberAdapter";
    private Activity activity;
    private List<UserMainVo> boardList;
    private User user;

    public TeamMemberAdapter(Activity activity, List<UserMainVo> boardList, User user) {
        this.activity = activity;
        this.boardList = boardList;
        this.user = user;
    }

    @Override
    public TeamMemberItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.teammember_item, parent, false);
        return new TeamMemberItem(layout);
    }

    @Override
    public void onBindViewHolder(TeamMemberItem holder, int i) {

        final UserMainVo vo = boardList.get(i);
        final int posintion = i;

        if(!Compare.isEmpty(vo.getProfileimgurl())){
            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(holder.userimg);
        }

        holder.feedbackcount.setText(String.valueOf(vo.getFeedbackcount()));
        holder.missionpasscount.setText(String.valueOf(vo.getMissionpasscount()));
        holder.joindate.setText(vo.getChange_creationdate());
        holder.username.setText(vo.getUsername());
        holder.level.setText(String.valueOf(vo.getLevel()));
        holder.totalscore.setText(String.valueOf(vo.getTotalscore()));
        holder.cardview.setOnClickListener(new View.OnClickListener() {
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
        return boardList.size();
    }

    public class TeamMemberItem extends RecyclerView.ViewHolder {

        public CardView cardview;
        public ImageView userimg;
        public TextView username,teamname,level,totalscore,joindate,missionpasscount,feedbackcount;

        public TeamMemberItem(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            username = (TextView) itemView.findViewById(R.id.username);
            level = (TextView) itemView.findViewById(R.id.level);
            totalscore = (TextView) itemView.findViewById(R.id.totalscore);
            joindate = (TextView) itemView.findViewById(R.id.joindate);
            missionpasscount = (TextView) itemView.findViewById(R.id.missionpasscount);
            feedbackcount = (TextView) itemView.findViewById(R.id.feedbackcount);
        }
    }


}
