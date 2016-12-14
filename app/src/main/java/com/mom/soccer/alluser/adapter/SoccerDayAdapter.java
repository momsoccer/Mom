package com.mom.soccer.alluser.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.dto.EventMain;

import java.util.List;

/**
 * Created by sungbo on 2016-12-14.
 */

public class SoccerDayAdapter extends RecyclerView.Adapter<SoccerDayAdapter.Hoder>{

    private static final String TAG = "SoccerDayAdapter";

    Activity activity;
    List<EventMain> eventMains;
    ImageBigAdapter imageBigAdapter;

    public SoccerDayAdapter(Activity activity, List<EventMain> eventMains) {
        this.activity = activity;
        this.eventMains = eventMains;
    }

    @Override
    public Hoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.soccer_item_layout, parent, false);
        return new SoccerDayAdapter.Hoder(v);
    }

    @Override
    public void onBindViewHolder(Hoder holder, int position) {

        final EventMain vo = eventMains.get(position);

        Glide.with(activity)
                .load(vo.getImg())
                .fitCenter()
                .thumbnail(0.1f)
                .override(300,300)
                .into(holder.imageView);

        if(vo.getEndflag().equals("N")){
            holder.status.setText("접수중");
        }else{
            holder.status.setText("종료");
        }

        holder.subject.setText(vo.getAppsubject());
        holder.time.setText(vo.getApptime());
        holder.reqcount.setText(String.valueOf(vo.getReqcount())+"명");
        holder.appdisp.setText(vo.getAppdisp());

        holder.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.momsoft.co.kr/event/eventdetail?mainid="+vo.getMainid());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                activity.startActivity(intent);
            }
        });

        holder.shreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "몸싸커 오프라인 레슨 특강 : " + vo.getAppsubject());
                intent.putExtra(Intent.EXTRA_TEXT,"http://www.momsoft.co.kr/event/eventdetail?mainid="+vo.getMainid());
                intent.putExtra(Intent.EXTRA_TITLE, vo.getAppdisp());
                activity.startActivity(Intent.createChooser(intent, "Mom Soccer"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return eventMains.size();
    }

    public class Hoder  extends RecyclerView.ViewHolder{

        TextView subject,status,time,reqcount,appdisp;
        ImageView imageView;
        Button applyBtn,shreBtn;



        public Hoder(View itemView) {
            super(itemView);

            subject = (TextView) itemView.findViewById(R.id.subject);
            time = (TextView) itemView.findViewById(R.id.time);
            status = (TextView) itemView.findViewById(R.id.status);
            appdisp = (TextView) itemView.findViewById(R.id.appdisp);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            reqcount = (TextView) itemView.findViewById(R.id.reqcount);
            applyBtn = (Button) itemView.findViewById(R.id.applyBtn);
            shreBtn = (Button) itemView.findViewById(R.id.shreBtn);

        }
    }
}
