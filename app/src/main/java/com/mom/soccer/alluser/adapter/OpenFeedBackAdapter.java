package com.mom.soccer.alluser.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mom.soccer.R;
import com.mom.soccer.dto.AdBoardVo;

import java.util.List;


public class OpenFeedBackAdapter extends RecyclerView.Adapter<OpenFeedBackAdapter.OpenFeedBackViewHoder>{

    private static final String TAG = "OpenFeedBackAdapter";

    Activity activity;
    List<AdBoardVo> adBoardVos;

    public OpenFeedBackAdapter(Activity activity, List<AdBoardVo> adBoardVos) {
        this.activity = activity;
        this.adBoardVos = adBoardVos;
    }

    @Override
    public OpenFeedBackViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_card_view, parent, false);
        return new OpenFeedBackViewHoder(v);
    }

    @Override
    public void onBindViewHolder(OpenFeedBackViewHoder holder, int position) {
        final AdBoardVo vo = adBoardVos.get(position);

    }

    @Override
    public int getItemCount() {
        return adBoardVos.size();
    }

    public class OpenFeedBackViewHoder extends RecyclerView.ViewHolder{


        public OpenFeedBackViewHoder(View view) {
            super(view);
        }
    }

}
