package com.mom.soccer.alluser.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.dto.AdBoardVo;

import java.util.List;


public class AdBoardAdapter extends RecyclerView.Adapter<AdBoardAdapter.AdBoardViewHoder>{

    private static final String TAG = "AdBoardAdapter";

    Activity activity;
    List<AdBoardVo> adBoardVos;

    public AdBoardAdapter(Activity activity, List<AdBoardVo> adBoardVos) {
        this.activity = activity;
        this.adBoardVos = adBoardVos;
    }

    @Override
    public AdBoardViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adboard_item_layout, null);
        return new AdBoardViewHoder(v);
    }

    @Override
    public void onBindViewHolder(AdBoardViewHoder holder, int position) {
        final AdBoardVo vo = adBoardVos.get(position);

        if(vo.getFilecount() > 0){
            for(int i=0;i < vo.getAdBoardFiles().size(); i++){

                if(i==0){
                    Glide.with(activity)
                            .load(vo.getAdBoardFiles().get(i).getFileaddr())
                            .fitCenter()
                            .thumbnail(0.1f)
                            .override(500,500)
                            .into(holder.attachImage1);
                }else if(i==1){
                    Glide.with(activity)
                            .load(vo.getAdBoardFiles().get(i).getFileaddr())
                            .fitCenter()
                            .thumbnail(0.1f)
                            .override(500,500)
                            .into(holder.attachImage2);
                }else if(i==2){
                    Glide.with(activity)
                            .load(vo.getAdBoardFiles().get(i).getFileaddr())
                            .fitCenter()
                            .thumbnail(0.1f)
                            .override(500,500)
                            .into(holder.attachImage3);
                }else if(i==3){
                    Glide.with(activity)
                            .load(vo.getAdBoardFiles().get(i).getFileaddr())
                            .fitCenter()
                            .thumbnail(0.1f)
                            .override(500,500)
                            .into(holder.attachImage4);
                }else if(i==4){
                    Glide.with(activity)
                            .load(vo.getAdBoardFiles().get(i).getFileaddr())
                            .fitCenter()
                            .thumbnail(0.1f)
                            .override(500,500)
                            .into(holder.attachImage5);
                }
            }
        }

        //더보기 표현
        if(vo.getSubcontent1().length() > 70){
            holder.txbtnview.setVisibility(View.VISIBLE);
        }else{
            holder.txbtnview.setVisibility(View.GONE);
        }

        holder.introduce.setText(vo.getIntroduce());
        holder.content1.setText(vo.getSubcontent1());
        holder.addr.setText(vo.getAddr());
        holder.phone.setText(vo.getPhone());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return adBoardVos.size();
    }

    public class AdBoardViewHoder extends RecyclerView.ViewHolder{

        ImageView attachImage1,attachImage2,attachImage3,attachImage4,attachImage5;
        TextView introduce,content1,phone,addr,txbtnview;
        CardView cardview;

        public AdBoardViewHoder(View view) {
            super(view);

            attachImage1 = (ImageView) view.findViewById(R.id.attachImage1);
            attachImage2 = (ImageView) view.findViewById(R.id.attachImage2);
            attachImage3 = (ImageView) view.findViewById(R.id.attachImage3);
            attachImage4 = (ImageView) view.findViewById(R.id.attachImage4);
            attachImage5 = (ImageView) view.findViewById(R.id.attachImage5);

            introduce = (TextView) view.findViewById(R.id.introduce);
            content1 = (TextView) view.findViewById(R.id.content1);
            phone = (TextView) view.findViewById(R.id.phone);
            addr = (TextView) view.findViewById(R.id.addr);
            cardview = (CardView) view.findViewById(R.id.cardview);
            txbtnview = (TextView) view.findViewById(R.id.txbtnview);

        }
    }

}
