package com.mom.soccer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.dto.User;
import com.mom.soccer.widget.VeteranToast;

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
    private HashMap<View, User> mLoaders;
    private int layout = 0;

    public GridSearchAdapter(Context mContext,List<User> userList,int layout) {

        this.mContext = mContext;
        this.userList = userList;
        this.layout = layout;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLoaders = new HashMap<View, User>();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position).getUid();
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

            listHolder.user_name.setText(userList.get(i).getUsername());
            if(userList.get(i).getTeamname()==null){
                listHolder.team_name.setText(R.string.user_team_yet_join);
            }else{
                listHolder.team_name.setText(userList.get(i).getTeamname());
            }

            Glide.with(mContext)
                    .load(userList.get(i).getProfileimgurl())
                    .into(listHolder.user_imag);

            currentRow.setTag(listHolder);
        }else{

            listHolder = (ListHolder) currentRow.getTag();

            if(userList.get(i) != null) {

                listHolder.user_name.setText(userList.get(i).getUsername());
                if(userList.get(i).getTeamname()==null){
                    listHolder.team_name.setText(R.string.user_team_yet_join);
                }else{
                    listHolder.team_name.setText(userList.get(i).getTeamname());
                }
                Glide.with(mContext)
                        .load(userList.get(i).getProfileimgurl())
                        .into(listHolder.user_imag);
            }

        }

        listHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeteranToast.makeToast(mContext,i +": 클릭 : " + listHolder.user_name.getText(), Toast.LENGTH_SHORT).show();
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
