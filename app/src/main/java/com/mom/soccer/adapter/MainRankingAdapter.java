package com.mom.soccer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.dataDto.UserRangkinVo;
import com.mom.soccer.widget.VeteranToast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sungbo on 2016-07-22.
 */
public class MainRankingAdapter extends BaseAdapter {

    private static final String TAG = "MainRankingAdapter";

    private Context mContext = null;
    private int layout = 0;
    private LayoutInflater inflater = null;

    //어댑터의 종류에 따라 Vo를 만든다.
    private List<UserRangkinVo> RankingVos;
    private HashMap<View, UserRangkinVo> mLoaders;

    public MainRankingAdapter(Context mContext, int layout, List<UserRangkinVo> vos) {

        this.mContext = mContext;
        this.layout = layout;
        this.RankingVos = vos;

        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            listHolder.tx_level.setText("99");

            if(RankingVos.get(i).getTeamname()==null){
                listHolder.teamname.setText("팀 비소속");
            }else{
                listHolder.teamname.setText(RankingVos.get(i).getTeamname());
            }

            Glide.with(mContext)
                    .load(RankingVos.get(i).getProfileimgurl())
                    .into(listHolder.imageView);

            currentRow.setTag(listHolder);

        }else{

            listHolder = (MainListHolder) currentRow.getTag();

            if(RankingVos.get(i) != null) {
                listHolder.number.setText(String.valueOf(i+1));
                listHolder.username.setText(RankingVos.get(i).getUsername());
                listHolder.score.setText(RankingVos.get(i).getTotalscore());
                listHolder.tx_level.setText("99");

                if(RankingVos.get(i).getTeamname()==null){
                    listHolder.teamname.setText("팀 비소속");
                }else{
                    listHolder.teamname.setText(RankingVos.get(i).getTeamname());
                }

                Glide.with(mContext)
                        .load(RankingVos.get(i).getProfileimgurl())
                        .into(listHolder.imageView);
            }
        }

        currentRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VeteranToast.makeToast(mContext,mContext.getString(R.string.preparation)+ " : "+ i +" 번째 클릭", Toast.LENGTH_SHORT).show();
            }
        });

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
