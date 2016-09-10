package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.bottommenu.MyPageActivity;
import com.mom.soccer.common.Compare;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsMainActivity;
import com.mom.soccer.mission.MissionCommon;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sungbo on 2016-08-02.
 */
public class GridSearchAdapter extends BaseAdapter {

    private static final String TAG = "GridSearchAdapter";


    private Context mContext = null;
    private LayoutInflater inflater = null;
    private List<User> userList;
    private List<Instructor> instructorList;
    private HashMap<View, User> mLoaders;
    private HashMap<View, Instructor> insLoaders;
    private int layout = 0;
    private String type="ins";

    public GridSearchAdapter(Context mContext,List<User> userList,int layout,String type) {

        this.mContext = mContext;
        this.userList = userList;
        this.layout = layout;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type = type;
        mLoaders = new HashMap<View, User>();
    }

    public GridSearchAdapter(Context mContext,List<Instructor> instructorList,int layout) {

        this.mContext = mContext;
        this.instructorList = instructorList;
        this.layout = layout;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        insLoaders = new HashMap<View, Instructor>();
    }

    @Override
    public int getCount() {

        if(type.equals("user")){
            return userList.size();
        }else{
            return instructorList.size();
        }
    }

    @Override
    public Object getItem(int position) {

        if(type.equals("user")){
            return userList.get(position).getUid();
        }else{
            return instructorList.get(position).getInstructorid();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        View currentRow = convertView;
        final ListHolder listHolder;

        if(currentRow ==null){

            currentRow = inflater.inflate(layout, parent, false);
            listHolder = new ListHolder();

            listHolder.user_imag = (ImageView) currentRow.findViewById(R.id.search_item_userimg);
            listHolder.user_name = (TextView) currentRow.findViewById(R.id.search_item_username);
            listHolder.team_name = (TextView) currentRow.findViewById(R.id.search_item_teamname);
            listHolder.button = (Button) currentRow.findViewById(R.id.search_item_btn_choose);


            if(type.equals("user")) {
                listHolder.user_name.setText(userList.get(i).getUsername());
                if (userList.get(i).getTeamname() == null) {
                    listHolder.team_name.setText(R.string.user_team_yet_join);
                } else {
                    listHolder.team_name.setText(userList.get(i).getTeamname());
                }

                if(!Compare.isEmpty(userList.get(i).getProfileimgurl())){
                    Glide.with(mContext)
                            .load(userList.get(i).getProfileimgurl())
                            .override(180, 120)
                            .centerCrop()
                            .into(listHolder.user_imag);
                }

            }else{
                listHolder.user_name.setText(instructorList.get(i).getName());
                listHolder.team_name.setText(instructorList.get(i).getTeamname()); //선생님은 항상 팀이 있음

                if(!Compare.isEmpty(instructorList.get(i).getProfileimgurl())){
                    Glide.with(mContext)
                            .load(instructorList.get(i).getProfileimgurl())
                            .override(180, 120)
                            .centerCrop()
                            .into(listHolder.user_imag);
                }

            }
            currentRow.setTag(listHolder);

        }else{

            listHolder = (ListHolder) currentRow.getTag();
            if(type.equals("user")){
                if(userList.get(i) != null) {

                    listHolder.user_name.setText(userList.get(i).getUsername());
                    if(userList.get(i).getTeamname()==null){
                        listHolder.team_name.setText(R.string.user_team_yet_join);
                    }else{
                        listHolder.team_name.setText(userList.get(i).getTeamname());
                    }

                    if(!Compare.isEmpty(userList.get(i).getProfileimgurl())){
                        Glide.with(mContext)
                                .load(userList.get(i).getProfileimgurl())
                                .override(180, 120)
                                .centerCrop()
                                .into(listHolder.user_imag);
                    }

                }
            }else{
                if(instructorList.get(i) != null) {

                    listHolder.user_name.setText(instructorList.get(i).getName());
                    if(instructorList.get(i).getTeamname()==null){
                        listHolder.team_name.setText(R.string.user_team_yet_join);
                    }else{
                        listHolder.team_name.setText(instructorList.get(i).getTeamname());
                    }
                    if(!Compare.isEmpty(instructorList.get(i).getProfileimgurl())){
                        Glide.with(mContext)
                                .load(instructorList.get(i).getProfileimgurl())
                                .override(180, 120)
                                .centerCrop()
                                .into(listHolder.user_imag);
                    }

                }
            }

        }

        listHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("user")){
                    Intent intent = new Intent(mContext, MyPageActivity.class);
                    intent.putExtra("pageflag","friend");
                    intent.putExtra("frienduid",userList.get(i).getUid());
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }else{
                    Intent intent = new Intent(mContext,InsMainActivity.class);
                    intent.putExtra("inspath","search");
                    intent.putExtra(MissionCommon.INS_OBJECT,instructorList.get(i));
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }

            }
        });

        return currentRow;
    }

    static class ListHolder {
        TextView user_name;
        TextView team_name;
        ImageView user_imag;
        Button button;
    }
}
