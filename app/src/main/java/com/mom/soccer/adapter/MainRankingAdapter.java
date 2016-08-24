package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.UserRangkinVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.pubactivity.PubActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sungbo on 2016-07-22.
 */
public class MainRankingAdapter extends BaseAdapter {

    private static final String TAG = "MainRankingAdapter";

    private Activity activity = null;
    private int layout = 0;
    private LayoutInflater inflater = null;
    private User user;

    //어댑터의 종류에 따라 Vo를 만든다.
    private List<UserRangkinVo> RankingVos;
    private HashMap<View, UserRangkinVo> mLoaders;

    public MainRankingAdapter(Activity activity, int layout, List<UserRangkinVo> vos,User user) {

        this.activity = activity;
        this.layout = layout;
        this.RankingVos = vos;
        this.user = user;

        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLoaders = new HashMap<View, UserRangkinVo>();
    }


    @Override
    public int getCount() {
        return RankingVos.size();
    }

    @Override
    public Object getItem(int i) {
        return RankingVos.get(i).getUid();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        View currentRow = convertView;
        MainListHolder listHolder;

        if(currentRow ==null){
            currentRow = inflater.inflate(layout, parent, false);
            listHolder = new MainListHolder();

            listHolder.number = (TextView) currentRow.findViewById(R.id.main_ranking_number);
            listHolder.imageView = (ImageView) currentRow.findViewById(R.id.main_ranking_img );
            listHolder.username = (TextView) currentRow.findViewById(R.id.main_ranking_username);
            listHolder.teamname = (TextView) currentRow.findViewById(R.id.main_ranking_teamname);
            listHolder.score = (TextView) currentRow.findViewById(R.id.main_ranking_score);
            listHolder.tx_level = (TextView) currentRow.findViewById(R.id.tx_level);

            listHolder.number.setText(String.valueOf(i+1));
            listHolder.username.setText(RankingVos.get(i).getUsername());
            listHolder.score.setText(RankingVos.get(i).getTotalscore());
            listHolder.tx_level.setText(String.valueOf(RankingVos.get(i).getLevel()));

            if(RankingVos.get(i).getTeamname()==null){
                listHolder.teamname.setText(R.string.user_team_yet_join);
            }else{
                listHolder.teamname.setText(RankingVos.get(i).getTeamname());
            }

            Glide.with(activity)
                    .load(RankingVos.get(i).getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(listHolder.imageView);

            currentRow.setTag(listHolder);

        }else{

            listHolder = (MainListHolder) currentRow.getTag();

            if(RankingVos.get(i) != null) {
                listHolder.number.setText(String.valueOf(i+1));
                listHolder.username.setText(RankingVos.get(i).getUsername());
                listHolder.score.setText(RankingVos.get(i).getTotalscore());
                listHolder.tx_level.setText(String.valueOf(RankingVos.get(i).getLevel()));

                if(RankingVos.get(i).getTeamname()==null){
                    listHolder.teamname.setText("팀 비소속");
                }else{
                    listHolder.teamname.setText(RankingVos.get(i).getTeamname());
                }

                Glide.with(activity)
                        .load(RankingVos.get(i).getProfileimgurl())
                        .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                        .into(listHolder.imageView);
            }
        }

        listHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,MyPageActivity.class);

                if(user.getUid() == RankingVos.get(i).getUid()){
                    intent.putExtra("pageflag","me");
                }else{
                    intent.putExtra("pageflag","friend");
                    intent.putExtra("frienduid",RankingVos.get(i).getUid());
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);

            }
        });

        listHolder.tx_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PubActivity pubActivity = new PubActivity(activity,user,RankingVos.get(i).getUid());
                pubActivity.showDialog();
            }
        });

/*
        currentRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,MyPageActivity.class);

                if(user.getUid() == RankingVos.get(i).getUid()){
                    intent.putExtra("pageflag","me");
                }else{
                    intent.putExtra("pageflag","friend");
                    intent.putExtra("frienduid",RankingVos.get(i).getUid());
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                if (mContext instanceof Activity) {
                    ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                }
            }
        });
*/

        return currentRow;
    }

    static class MainListHolder {
        TextView  number;
        ImageView imageView;
        TextView  username;
        TextView  teamname;
        TextView  score;
        TextView  tx_level;
    }



}
