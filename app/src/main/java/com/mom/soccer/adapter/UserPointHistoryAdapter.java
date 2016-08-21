package com.mom.soccer.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.dto.SpBalanceLine;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by sungbo on 2016-08-21.
 */
public class UserPointHistoryAdapter extends RecyclerView.Adapter<UserPointHistoryAdapter.UserPointItemViewHoder>{

    Context context;
    List<SpBalanceLine> lineList;

    public UserPointHistoryAdapter(Context context, List<SpBalanceLine> lineList) {
        this.context = context;
        this.lineList = lineList;
    }

    @Override
    public UserPointItemViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_card_view, null);

        return new UserPointItemViewHoder(v);
    }

    @Override
    public void onBindViewHolder(UserPointItemViewHoder holder, int position) {
        final SpBalanceLine vo = lineList.get(position);

        NumberFormat numberFormat = NumberFormat.getInstance();

        holder.tx_date.setText(vo.getChange_creationdate());
        holder.tx_type.setText(vo.getType());
        holder.tx_pre_amount.setText(numberFormat.format(vo.getPreviousamount())+"P");
        holder.tx_last_amount.setText(numberFormat.format(vo.getLastamount())+"P");
        holder.tx_disp.setText(vo.getDescription());

        //차감액 로직넣기

        if(vo.getInamount()==0){
            holder.tx_inout_type.setText(context.getString(R.string.point_item_type_pay));
        }else{
            holder.tx_inout_type.setText(context.getString(R.string.point_item_type_reiceve));
        }


    }

    @Override
    public int getItemCount() {
        return lineList.size();
    }

    public class UserPointItemViewHoder extends RecyclerView.ViewHolder{

        public TextView tx_date;
        public TextView tx_inout_type;
        public TextView tx_type;
        public TextView tx_pre_amount;
        public TextView tx_subject;
        public TextView tx_amount;
        public TextView tx_last_amount;
        public TextView tx_disp;
        public CardView cardview;


        public UserPointItemViewHoder(View view) {
            super(view);

            tx_subject = (TextView) view.findViewById(R.id.tx_subject);
            tx_amount = (TextView) view.findViewById(R.id.tx_amount);

            tx_date = (TextView) view.findViewById(R.id.tx_date);
            tx_inout_type = (TextView) view.findViewById(R.id.tx_inout_type);
            tx_type = (TextView) view.findViewById(R.id.tx_type);
            tx_pre_amount = (TextView) view.findViewById(R.id.tx_pre_amount);
            tx_last_amount = (TextView) view.findViewById(R.id.tx_last_amount);
            tx_disp = (TextView) view.findViewById(R.id.tx_disp);
            cardview = (CardView) view.findViewById(R.id.cardview);
        }
    }

}
