package com.mom.soccer.alluser.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.mom.soccer.R;
import com.mom.soccer.alluser.AdBoardActivity;
import com.mom.soccer.dto.AdBoardVo;
import com.mom.soccer.momactivity.MomMainActivity;

import java.util.List;


public class AdBoardAdapter extends RecyclerView.Adapter<AdBoardAdapter.AdBoardViewHoder>{

    private static final String TAG = "AdBoardAdapter";

    Activity activity;
    List<AdBoardVo> adBoardVos;
    ImageAdapter imageAdapter;

    public AdBoardAdapter(Activity activity, List<AdBoardVo> adBoardVos) {
        this.activity = activity;
        this.adBoardVos = adBoardVos;
    }

    @Override
    public AdBoardViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adboard_item_layout, parent, false);
        return new AdBoardViewHoder(v);
    }

    @Override
    public void onBindViewHolder(AdBoardViewHoder holder, int position) {
        final AdBoardVo vo = adBoardVos.get(position);

        if(vo.getFilecount() > 0){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            linearLayoutManager.scrollToPosition(position);
            holder.imageRcView.setLayoutManager(linearLayoutManager);

            holder.imageRcView.setHasFixedSize(true);
            holder.imageRcView.setItemAnimator(new DefaultItemAnimator());
            imageAdapter = new ImageAdapter(activity,vo.getAdBoardFiles());
            holder.imageRcView.setAdapter(imageAdapter);

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

                Intent intent = new Intent(activity, AdBoardActivity.class);
                intent.putExtra("adboardid",vo.getAdvid());
                activity.startActivity(intent);

            }
        });

        holder.liShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    final KakaoLink kakaoLink = KakaoLink.getKakaoLink(activity.getApplicationContext());
                    final KakaoTalkLinkMessageBuilder builder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                    builder.addAppLink("자세히 보기",
                            new AppActionBuilder()
                                    .addActionInfo(AppActionInfoBuilder
                                            .createAndroidActionInfoBuilder()
                                            .setExecuteParam(activity.getResources().getString(R.string.kakao_app_key))
                                            .setMarketParam("details?id=com.mom.soccer")
                                            .build())
                                    //.addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder()
                                    //        .setExecuteParam("execparamkey1=1111")
                                    //        .build())
                                    //.setUrl("https://developers.kakao.com/docs/android") // PC 카카오톡 에서 사용하게 될 웹사이트 주소
                                    .build());


                    builder.addText(vo.getSubcontent1()+":"+vo.getAddr()+" : "+vo.getPhone());

                    if(vo.getFilecount()!=0){
                        String url = vo.getAdBoardFiles().get(0).getFileaddr();
                        builder.addImage(url,800,500);
                    }

                    builder.addAppButton("앱실행");
                    kakaoLink.sendMessage(builder, MomMainActivity.activity);

                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return adBoardVos.size();
    }

    public class AdBoardViewHoder extends RecyclerView.ViewHolder{

        //ImageView attachImage1,attachImage2,attachImage3,attachImage4,attachImage5;
        TextView introduce,content1,phone,addr,txbtnview;
        CardView cardview;
        RecyclerView imageRcView;
        LinearLayout liShareBtn;

        public AdBoardViewHoder(View view) {
            super(view);

/*            attachImage1 = (ImageView) view.findViewById(R.id.attachImage1);
            attachImage2 = (ImageView) view.findViewById(R.id.attachImage2);
            attachImage3 = (ImageView) view.findViewById(R.id.attachImage3);
            attachImage4 = (ImageView) view.findViewById(R.id.attachImage4);
            attachImage5 = (ImageView) view.findViewById(R.id.attachImage5);*/

            introduce = (TextView) view.findViewById(R.id.introduce);
            content1 = (TextView) view.findViewById(R.id.content1);
            phone = (TextView) view.findViewById(R.id.phone);
            addr = (TextView) view.findViewById(R.id.addr);
            cardview = (CardView) view.findViewById(R.id.cardview);
            txbtnview = (TextView) view.findViewById(R.id.txbtnview);
            imageRcView = (RecyclerView) view.findViewById(R.id.imageRcView);
            liShareBtn = (LinearLayout) view.findViewById(R.id.liShareBtn);


        }
    }

}
