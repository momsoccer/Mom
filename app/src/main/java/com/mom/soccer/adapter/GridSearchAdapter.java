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
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsMainActivity;
import com.mom.soccer.mission.MissionCommon;

import java.util.List;

/**
 * Created by sungbo on 2016-08-02.
 */
public class GridSearchAdapter extends RecyclerView.Adapter<GridSearchAdapter.SearchItemViewHolder>{

    private static final String TAG = "GridSearchAdapter";

    private Activity activity;
    private List<User> userList;
    private List<Instructor> instructorList;
    private String type = "user";

    //유저 검색페이지
    public GridSearchAdapter(Activity activity, List<User> userList) {
        this.activity = activity;
        this.userList = userList;
    }

    //강사검색페이지
    public GridSearchAdapter(Activity activity, List<Instructor> instructorList, String type) {
        this.activity = activity;
        this.instructorList = instructorList;
        this.type = type;
    }

    @Override
    public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.ac_search_grid_item_layout, parent, false);
        return new SearchItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(SearchItemViewHolder holder, int position) {

        if(type.equals("user")){

            final User vo = userList.get(position);


            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .centerCrop()
                    .placeholder(activity.getResources().getDrawable(R.drawable.userimg))
                    .into(holder.search_item_userimg);

            holder.search_item_name.setText(vo.getUsername());

            if(vo.getTeamname()==null){
                holder.search_item_teamname.setText(activity.getResources().getString(R.string.user_team_yet_join));
            }else{
                holder.search_item_teamname.setText(vo.getTeamname());
            }

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, MyPageActivity.class);
                    intent.putExtra("pageflag","friend");
                    intent.putExtra("frienduid",vo.getUid());
                    activity.startActivity(intent);
                }
            });

            holder.level.setVisibility(View.GONE);

        }else{

            final Instructor vo = instructorList.get(position);
            holder.level.setVisibility(View.GONE);
            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .centerCrop()
                    .placeholder(activity.getResources().getDrawable(R.drawable.userimg))
                    .into(holder.search_item_userimg);

            holder.search_item_name.setText(vo.getName());

            if(vo.getTeamname()==null){
                holder.search_item_teamname.setText(activity.getResources().getString(R.string.user_team_yet_join));
            }else{
                holder.search_item_teamname.setText(vo.getTeamname());
            }

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,InsMainActivity.class);
                    intent.putExtra("inspath","search");
                    intent.putExtra(MissionCommon.INS_OBJECT,vo);
                    intent.putExtra("goPage",0);
                    activity.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {

        if(type.equals("user")){
            return userList.size();
        }else{
            return instructorList.size();
        }
    }

    public class SearchItemViewHolder extends RecyclerView.ViewHolder {

        ImageView search_item_userimg;
        TextView search_item_name,search_item_teamname,level;
        CardView cardview;

        public SearchItemViewHolder(View itemView) {
            super(itemView);

            level = (TextView) itemView.findViewById(R.id.level);
            search_item_userimg = (ImageView) itemView.findViewById(R.id.search_item_userimg);
            search_item_name = (TextView) itemView.findViewById(R.id.search_item_name);
            search_item_teamname = (TextView) itemView.findViewById(R.id.search_item_teamname);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

}