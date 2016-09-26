package com.mom.soccer.adapter;

import android.app.Activity;
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
import com.mom.soccer.dto.MyBookMark;

import java.util.List;


public class LikeUserAdapter extends RecyclerView.Adapter<LikeUserAdapter.LikeViewHoder>{

    private static final String TAG = "OpenFeedBackAdapter";

    Activity activity;
    List<MyBookMark> myBookMarks;

    public LikeUserAdapter(Activity activity, List<MyBookMark> myBookMarks) {
        this.activity = activity;
        this.myBookMarks = myBookMarks;
    }

    @Override
    public LikeViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_user_item, parent, false);
        return new LikeViewHoder(v);
    }

    @Override
    public void onBindViewHolder(LikeViewHoder holder, int position) {
        final MyBookMark vo = myBookMarks.get(position);

        if(!Compare.isEmpty(vo.getUserimge())){
            Glide.with(activity)
                    .load(vo.getUserimge())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(holder.userimg);
        }

        holder.username.setText(vo.getUsername());

        if(!Compare.isEmpty(vo.getTeamname())){
            holder.teamname.setText(vo.getTeamname());
        }else{
            holder.teamname.setText(activity.getResources().getString(R.string.user_team_yet_join));
        }

        holder.totalscore.setText(String.valueOf(vo.getTotalscore()));
        holder.level.setText(String.valueOf(vo.getLevel()));
        holder.dateformating.setText(vo.getFormatDataSign());
    }

    @Override
    public int getItemCount() {
        return myBookMarks.size();
    }

    public class LikeViewHoder extends RecyclerView.ViewHolder{

        ImageView userimg;
        TextView username,teamname,level,totalscore,dateformating;

        public LikeViewHoder(View view) {
            super(view);

            userimg = (ImageView) view.findViewById(R.id.userimg);
            username = (TextView) view.findViewById(R.id.username);
            teamname = (TextView) view.findViewById(R.id.teamname);
            level = (TextView) view.findViewById(R.id.level);
            totalscore = (TextView) view.findViewById(R.id.totalscore);
            dateformating = (TextView) view.findViewById(R.id.dateformating);

        }
    }

}
