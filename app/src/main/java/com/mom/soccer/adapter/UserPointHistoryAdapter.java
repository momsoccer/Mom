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
 *
 * --포인트 트랜잭션 종류 self_point_balance_line.TYPE
 * FEEDBACK_REQ 피드백요청 ,사용
 * MISSION 미션오픈 ,사용
 * EVENT 이벤트 지급 ,유저가 얻음
 * JOIN 가입포인트 ,사용
 * EVAL 심사포인트 ,사용
 * DAY  출석체크 ,유저가 얻음
 * PUR 포인트를 충전함 ,유저가 얻음
 */
public class UserPointHistoryAdapter extends RecyclerView.Adapter<UserPointHistoryAdapter.UserPointItemViewHoder>{

    private static final String TAG = "UserPointHistoryAdapter";

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

        holder.tx_typedisp.setText(vo.getPointtypedisp());
        holder.tx_date.setText(vo.getChange_creationdate());

        if(vo.getOutamount()==0){
            //구매
            holder.tx_amount.setText(numberFormat.format(vo.getInamount())+"P");
        }else{
            //충전
            holder.tx_amount.setText(numberFormat.format(vo.getOutamount())+"P");
        }

        holder.tx_pre_amount.setText(numberFormat.format(vo.getPreviousamount())+"P");
        holder.tx_last_amount.setText(numberFormat.format(vo.getLastamount())+"P");
        holder.tx_disp.setText(vo.getDescription());


    }

    @Override
    public int getItemCount() {
        return lineList.size();
    }

    public class UserPointItemViewHoder extends RecyclerView.ViewHolder{

        public TextView tx_typedisp;
        public TextView tx_date;

        public TextView tx_pre_amount;
        public TextView tx_amount;
        public TextView tx_last_amount;
        public TextView tx_disp;
        public CardView cardview;


        public UserPointItemViewHoder(View view) {
            super(view);


            tx_typedisp = (TextView) view.findViewById(R.id.tx_typedisp);
            tx_date = (TextView) view.findViewById(R.id.tx_date);

            tx_amount = (TextView) view.findViewById(R.id.tx_amount);
            tx_pre_amount = (TextView) view.findViewById(R.id.tx_pre_amount);
            tx_last_amount = (TextView) view.findViewById(R.id.tx_last_amount);
            tx_disp = (TextView) view.findViewById(R.id.tx_disp);
            cardview = (CardView) view.findViewById(R.id.cardview);
        }
    }

}
