package com.mom.soccer.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.User;

import java.util.List;

/**
 * Created by sungbo on 2016-08-29.
 */
public class PassListAdapter extends RecyclerView.Adapter<PassListAdapter.PassItemHolder>{

    Activity activity;
    List<MissionPass> passes;
    User user;

    public PassListAdapter(Activity activity, List<MissionPass> passes, User user) {
        this.activity = activity;
        this.passes = passes;
        this.user = user;
    }

    @Override
    public PassItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_card_item, null);
        return new PassItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PassItemHolder holder, int position) {
         MissionPass pass = passes.get(position);

        if (!Compare.isEmpty(pass.getInsimge())) {
            Glide.with(activity)
                    .load(pass.getInsimge())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(holder.insimage);
        }

        holder.seq.setText(String.valueOf(pass.getSeq())+"회");
        holder.date.setText(pass.getChange_updatedate());
        holder.insname.setText(pass.getInsname());
        holder.grade.setText(pass.getPassgrade());
        holder.incomment.setText(pass.getInscomment());
        holder.failuredisp.setText(pass.getFailuredisp());

        if(pass.getStatus().equals("REQUEST")){
            holder.status.setText(activity.getString(R.string.user_mission_p));

            holder.cancelbtn.setVisibility(View.VISIBLE);
            holder.title_failuredisp.setVisibility(View.GONE);
            holder.failuredisp.setVisibility(View.GONE);
            holder.title_comment.setVisibility(View.GONE);
            holder.incomment.setVisibility(View.GONE);

        }else if(pass.getStatus().equals("REJECT")){
            holder.status.setText(activity.getString(R.string.user_mission_fail));

            holder.cancelbtn.setVisibility(View.GONE);
            holder.title_failuredisp.setVisibility(View.VISIBLE);
            holder.failuredisp.setVisibility(View.VISIBLE);
        }else if(pass.getStatus().equals("SUCCESS")){
            holder.status.setText(activity.getString(R.string.user_mission_y));

            holder.cancelbtn.setVisibility(View.GONE);
            holder.title_failuredisp.setVisibility(View.GONE);
            holder.failuredisp.setVisibility(View.GONE);

        }

        holder.cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return passes.size();
    }

    public class PassItemHolder extends RecyclerView.ViewHolder{

        TextView status,grade,failuredisp,incomment,date,seq,insname,title_failuredisp,title_comment;
        ImageView insimage;
        Button cancelbtn;

        public PassItemHolder(View itemView) {
            super(itemView);
            insimage = (ImageView) itemView.findViewById(R.id.insimage);
            seq = (TextView) itemView.findViewById(R.id.seq);
            status = (TextView) itemView.findViewById(R.id.status);
            failuredisp = (TextView) itemView.findViewById(R.id.failuredisp);
            incomment = (TextView) itemView.findViewById(R.id.incomment);
            date = (TextView) itemView.findViewById(R.id.date);
            insname = (TextView) itemView.findViewById(R.id.insname);
            grade = (TextView) itemView.findViewById(R.id.grade);
            cancelbtn = (Button) itemView.findViewById(R.id.cancelbtn);

            title_failuredisp = (TextView) itemView.findViewById(R.id.title_failuredisp);
            title_comment = (TextView) itemView.findViewById(R.id.title_comment);

        }
    }

}
