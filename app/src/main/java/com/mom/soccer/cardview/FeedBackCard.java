package com.mom.soccer.cardview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.FeedbackLine;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.MissionMainActivity;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;
import com.mom.soccer.widget.WaitingDialog;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-23.
 */
public class FeedBackCard extends Card{

    private FeedbackHeader feedbackHeader;

    private LinearLayout li_feed_back_call,li_feed_back_send;

    private TextView ins_content,user_conten;
    private ImageView userimage,insimage;
    private RatingBar mRatingBar;

    private String title = null;
    private User user;
    private Mission mission;
    private Activity activity;

    public FeedBackCard(final Activity activity, final FeedbackHeader feedbackHeader, final User user, final Mission mission) {
        super(activity, R.layout.feedback_card_item);
        this.feedbackHeader = feedbackHeader;
        this.user = user;
        this.mission = mission;
        this.activity = activity;

        CardHeader header = new CardHeader(getContext());

        if(feedbackHeader.getType().equals("user")){
            title = getContext().getString(R.string.feedback_request_user_request) + " To."+feedbackHeader.getInsname();
            setBackgroundColorResourceId(R.color.color6);
            header.setPopupMenu(R.menu.feedback_pop_menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                        FeedbackLine line = new FeedbackLine();
                        line.setFeedbackid(feedbackHeader.getFeedbackid());
                        ratingEval("delete",line);
                }
            });

        }else{
            title = getContext().getString(R.string.feedback_request_ins_reply) + " From."+feedbackHeader.getUsername();
            setBackgroundColorResourceId(R.color.color2);
            header.setPopupMenu(R.menu.feedback_pop_ins, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                    //피드백 신고를 한다
                    VeteranToast.makeToast(getContext(),"준비 중입니다",Toast.LENGTH_SHORT).show();
                }
            });
        }

        header.setTitle(title);


        addCardHeader(header);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        li_feed_back_call = (LinearLayout) parent.findViewById(R.id.li_feed_back_call);
        li_feed_back_send = (LinearLayout) parent.findViewById(R.id.li_feed_back_send);


        if(feedbackHeader.getType().equals("user")){
            li_feed_back_send.setVisibility(View.VISIBLE);
            li_feed_back_call.setVisibility(View.GONE);

            user_conten = (TextView) parent.findViewById(R.id.usercontent);
            userimage = (ImageView) parent.findViewById(R.id.userimage);

            user_conten.setText(feedbackHeader.getContent());

            Glide.with(getContext())
                    .load(feedbackHeader.getProfileimgurl())
                    .into(userimage);
        }else{
            li_feed_back_send.setVisibility(View.GONE);
            li_feed_back_call.setVisibility(View.VISIBLE);

            ins_content = (TextView) parent.findViewById(R.id.inscontent);
            insimage = (ImageView) parent.findViewById(R.id.insimage);

            ins_content.setText(feedbackHeader.getContent());

            Glide.with(getContext())
                    .load(feedbackHeader.getInsprofileimgurl())
                    .into(insimage);

            mRatingBar = (RatingBar) parent.findViewById(R.id.eval_ratingbar);
            LayerDrawable star =  (LayerDrawable) mRatingBar.getProgressDrawable();
            star.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            if (mRatingBar != null){
                mRatingBar.setNumStars(5);
                mRatingBar.setMax(5);
                mRatingBar.setStepSize(0.5f);
                mRatingBar.setRating(feedbackHeader.getEvalscore());
            }

            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    FeedbackLine line = new FeedbackLine();
                    line.setFeedbackid(feedbackHeader.getFeedbackid());
                    ratingEval("save",line);
                }
            });

        }

    }


    public void ratingEval(String trType, FeedbackLine line){
        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getContext(),user);
        if(trType.equals("save")){
            WaitingDialog.showWaitingDialog(activity,false);
            line.setFeedbacklineid(feedbackHeader.getFeedbacklineid());
            line.setEvalscore(mRatingBar.getRating());

            Call<ServerResult> c = feedBackService.updateLine(line);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_eval_save) +mRatingBar.getRating() , Toast.LENGTH_SHORT).show();
                    }else{
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }else if(trType.equals("delete")){
            WaitingDialog.showWaitingDialog(activity,false);
            Call<ServerResult> s = feedBackService.deleteLine(line);
            s.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        if(result.getResult().equals("S")){
                            VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_delete), Toast.LENGTH_SHORT).show();

                            //리프레쉬
                            Intent intent = new Intent(getContext(), MissionMainActivity.class);
                            intent.putExtra(MissionCommon.OBJECT,mission);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);

                        }else if(result.getResult().equals("E")){
                            VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_not_delete), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }

    }

}
