package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.mom.soccer.dataDto.InsInfoVo;
import com.mom.soccer.mission.MissionCommon;

import java.util.List;

/**
 * Created by sungbo on 2016-08-15.
 */
public class InsInfoRecyclerAdapter extends RecyclerView.Adapter<InsInfoRecyclerAdapter.InsInfoItemViewHoder> {

    private static final String TAG = "FeedBackInsListActivity";

    Activity activity;
    List<InsInfoVo> infoVoList;
    String missionType;

    public InsInfoRecyclerAdapter(Activity activity, List<InsInfoVo> infoVoList,String missionType) {
        this.activity = activity;
        this.infoVoList = infoVoList;
        this.missionType = missionType;
    }

    @Override
    public InsInfoItemViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.insinfo_card_view, parent, false);
        return new InsInfoItemViewHoder(v);
    }

    @Override
    public void onBindViewHolder(InsInfoItemViewHoder holder, int position) {
        final InsInfoVo vo = infoVoList.get(position);

        if(!Compare.isEmpty(vo.getProfileimgurl())){
            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(holder.insImage);
        }

        if(!Compare.isEmpty(vo.getEmblem())){
            Glide.with(activity)
                    .load(vo.getEmblem())
                    .asBitmap().transform(new RoundedCornersTransformation(activity,10,5))
                    .into(holder.teamImage);
        }

        holder.tx_insName.setText(vo.getName());
        holder.tx_teamName.setText(vo.getTeamname());

        holder.tx_teammembercount.setText(String.valueOf(vo.getTeammembercount()));
        holder.tx_questioncount.setText(String.valueOf(vo.getQuestioncount()));
        holder.tx_answercount.setText(String.valueOf(vo.getAnswercount()));
        holder.tx_estimation.setText(String.valueOf(vo.getEstimation()));
        holder.tx_insvidecount.setText(String.valueOf(vo.getInsvidecount()));

        holder.tx_teamwordpoint.setText(String.valueOf(vo.getTeamwordpoint()));
        holder.tx_teamvideopoint.setText(String.valueOf(vo.getTeamvideopoint()));
        holder.tx_pubvideopoint.setText(String.valueOf(vo.getPubvideopoint()));
        holder.tx_pubwordpoint.setText(String.valueOf(vo.getPubwordpoint()));
        holder.tx_pubpasspoint.setText(String.valueOf(vo.getPubpasspoint()));
        holder.tx_teampasspoint.setText(String.valueOf(vo.getTeampasspoint()));
        holder.btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG,"미션 타입은 %% : " + missionType);

                Intent intent = new Intent();
                intent.putExtra(MissionCommon.INS_OBJECT,vo);
                intent.putExtra(MissionCommon.MISSIONTYPE,missionType);
                activity.setResult(activity.RESULT_OK,intent);
                activity.finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return infoVoList.size();
    }


    public class InsInfoItemViewHoder extends RecyclerView.ViewHolder {

        public ImageView insImage;
        public ImageView teamImage;

        public TextView tx_insName;
        public TextView  tx_teamName;
        public TextView  tx_teammembercount;
        public TextView  tx_questioncount;
        public TextView  tx_answercount;
        public TextView  tx_estimation;  //별 5개 중 평균숫자 답변 평가
        public TextView  tx_insvidecount; //선생님 개인 강의 업로드 카운트

        public TextView  tx_teamvideopoint;
        public TextView  tx_teamwordpoint;
        public TextView  tx_pubvideopoint;
        public TextView  tx_pubwordpoint;
        public TextView  tx_teampasspoint;
        public TextView  tx_pubpasspoint;
        public CardView cardview;
        public Button btnchoose;

        public InsInfoItemViewHoder(View view) {
            super(view);

            //assgin layout item
            insImage = (ImageView) view.findViewById(R.id.insimage);
            teamImage = (ImageView) view.findViewById(R.id.teamimage);

            tx_insName = (TextView) view.findViewById(R.id.name);
            tx_teamName = (TextView) view.findViewById(R.id.teamname);
            tx_teammembercount = (TextView) view.findViewById(R.id.teammembercount);
            tx_questioncount = (TextView) view.findViewById(R.id.questioncount);
            tx_answercount = (TextView) view.findViewById(R.id.answercount);
            tx_estimation = (TextView) view.findViewById(R.id.estimation);
            tx_insvidecount = (TextView) view.findViewById(R.id.insvidecount);

            tx_teamvideopoint = (TextView) view.findViewById(R.id.teamvideopoint);
            tx_teamwordpoint = (TextView) view.findViewById(R.id.teamwordpoint);
            tx_pubvideopoint = (TextView) view.findViewById(R.id.pubvideopoint);
            tx_pubwordpoint = (TextView) view.findViewById(R.id.pubwordpoint);
            tx_teampasspoint = (TextView) view.findViewById(R.id.teampasspoint);
            tx_pubpasspoint = (TextView) view.findViewById(R.id.pubpasspoint);
            cardview = (CardView) view.findViewById(R.id.cardview);
            btnchoose = (Button) view.findViewById(R.id.btnchoose);

        }
    }

}
