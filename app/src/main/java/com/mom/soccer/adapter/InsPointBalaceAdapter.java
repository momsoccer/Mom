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
import com.mom.soccer.dataDto.InsPointBalance;
import com.mom.soccer.dto.Instructor;

import java.util.List;

/**
 * Created by sungbo on 2016-09-05.
 */
public class InsPointBalaceAdapter extends RecyclerView.Adapter<InsPointBalaceAdapter.insPoinHolder>{

    private Activity activity;
    private Instructor instructor;
    private List<InsPointBalance> insPointBalances;

    public InsPointBalaceAdapter(Activity activity, Instructor instructor, List<InsPointBalance> insPointBalances) {
        this.activity = activity;
        this.instructor = instructor;
        this.insPointBalances = insPointBalances;
    }

    @Override
    public insPoinHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ins_point_balance, parent, false);
        return new insPoinHolder(v);
    }

    @Override
    public void onBindViewHolder(insPoinHolder holder, int position) {
        final InsPointBalance vo = insPointBalances.get(position);

        if(vo.getBalancetype().equals("FEEDBACK")){
            holder.balancetype.setText(activity.getResources().getString(R.string.ins_text_feed_a));
        }else{
            holder.balancetype.setText(activity.getResources().getString(R.string.mission_eval_title));
        }

        if(!Compare.isEmpty(vo.getUserimge())){
            Glide.with(activity)
                    .load(vo.getUserimge())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(holder.userimge);
        }

        if(vo.getCalculateflag().equals("N")){
            holder.cacflag.setText(activity.getResources().getString(R.string.ins_poin_balance1));
        }else{
            holder.cacflag.setText(activity.getResources().getString(R.string.ins_poin_balance2));
        }

        holder.username.setText(vo.getUsername());
        holder.cashpoint.setText(String.valueOf(vo.getCashpoint())+"P");
        holder.missionname.setText(vo.getMissionname());
        holder.date.setText(vo.getChange_creationdate());

    }

    @Override
    public int getItemCount() {
        return insPointBalances.size();
    }

    public class insPoinHolder extends RecyclerView.ViewHolder{

        TextView balancetype,cashpoint,username,missionname,date,cacflag;
        ImageView userimge;



        public insPoinHolder(View v) {
            super(v);

            userimge = (ImageView) v.findViewById(R.id.userimage);
            balancetype = (TextView) v.findViewById(R.id.balancetype);
            cashpoint = (TextView) v.findViewById(R.id.cashpoint);
            username = (TextView) v.findViewById(R.id.username);
            missionname = (TextView) v.findViewById(R.id.missionname);
            date = (TextView) v.findViewById(R.id.date);
            cacflag = (TextView) v.findViewById(R.id.cacflag);

        }
    }

}
