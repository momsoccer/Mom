package com.mom.soccer.ins;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.FeedBackEndAdapter;
import com.mom.soccer.adapter.FeedBackReqAdapter;
import com.mom.soccer.dataDto.FeedBackDataVo;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-26.
 */
public class insDashFragment extends Fragment {

    private static final String TAG = "insDashFragment";
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private User user = new User();
    private Instructor ins = new Instructor();
    private SlidingUpPanelLayout mLayout;

    private RecyclerView feedback_req_recyclerview;
    private FeedBackReqAdapter feedBackReqAdapter;

    private RecyclerView feedbackrecyclerview;

    private FeedBackEndAdapter feedBackEndAdapter;

    private List<FeedbackHeader> feedbackHeaders;
    private List<FeedBackDataVo> feedBackDataVos;

    TextView no_feed_text,no_feed_text_list;


    public static insDashFragment newInstance(int page, User user, Instructor ins){

        Log.i(TAG,"insDashFragment : ==========================");

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        args.putSerializable(MissionCommon.INS_OBJECT,ins);
        insDashFragment fragment = new insDashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
        ins = (Instructor) getArguments().getSerializable(MissionCommon.INS_OBJECT);
        Log.i(TAG,"onCreate : ==========================" + mPage);
        Log.i(TAG,"user : " + user.toString());
        Log.i(TAG,"ins : " + ins.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = null;

        /****************************************************
         * 피드백 답변 페이지
         * ***************************************************/
        if(mPage==1){
            view = inflater.inflate(R.layout.fr_dash_feedback_main1, container, false);

            feedback_req_recyclerview = (RecyclerView) view.findViewById(R.id.feedback_req_recyclerview);
            no_feed_text = (TextView) view.findViewById(R.id.no_feed_text);

            FeedReqList(view,"req");
            mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
            mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                    Log.i(TAG, "onPanelStateChanged " + newState);
                }
            });


            mLayout.setFadeOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            });

            feedbackrecyclerview = (RecyclerView) view.findViewById(R.id.feedbackrecyclerview);
            no_feed_text_list = (TextView) view.findViewById(R.id.no_feed_text_list);

            FeedReqList(view,"req_end");

            /****************************************************
             * 강사 영상 페이지
             * ***************************************************/
        }else if(mPage == 2 ){
            view = inflater.inflate(R.layout.fr_dash_feedback_main2, container, false);


        }

        return view;
    }

    public void FeedReqList(View view,String type){

        if(type.equals("req")){  //피드백 요청
            WaitingDialog.showWaitingDialog(getContext(),false);
            FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getContext(),user);
            FeedbackHeader query = new FeedbackHeader();
            query.setInstructorid(ins.getInstructorid());
            query.setInstype("user");

            Call<List<FeedbackHeader>> c = feedBackService.requestFeed(query);
            c.enqueue(new Callback<List<FeedbackHeader>>() {
                @Override
                public void onResponse(Call<List<FeedbackHeader>> call, Response<List<FeedbackHeader>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){

                        feedbackHeaders = response.body();

                        if(feedbackHeaders.size()!=0){

                            feedback_req_recyclerview.setVisibility(View.VISIBLE);
                            no_feed_text.setVisibility(View.GONE);

                            feedBackReqAdapter = new FeedBackReqAdapter(getActivity(),feedbackHeaders,user,ins);
                            feedback_req_recyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                            feedback_req_recyclerview.getItemAnimator().setAddDuration(300);
                            feedback_req_recyclerview.getItemAnimator().setRemoveDuration(300);
                            feedback_req_recyclerview.getItemAnimator().setMoveDuration(300);
                            feedback_req_recyclerview.getItemAnimator().setChangeDuration(300);

                            feedback_req_recyclerview.setHasFixedSize(true);
                            feedback_req_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(feedBackReqAdapter);
                            alphaAdapter.setDuration(500);
                            feedback_req_recyclerview.setAdapter(alphaAdapter);
                        }else{
                            feedback_req_recyclerview.setVisibility(View.GONE);
                            no_feed_text.setVisibility(View.VISIBLE);
                        }

                    }else{

                    }
                }

                @Override
                public void onFailure(Call<List<FeedbackHeader>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();

                }
            });
        }else{  //피드백 답변
            WaitingDialog.showWaitingDialog(getContext(),false);

            FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getContext(),user);
            FeedBackDataVo query = new FeedBackDataVo();
            query.setInstructorid(ins.getInstructorid());
            query.setQueryRow(100);

            Call<List<FeedBackDataVo>> c = feedBackService.getFeedBackTotalList(query);
            c.enqueue(new Callback<List<FeedBackDataVo>>() {
                @Override
                public void onResponse(Call<List<FeedBackDataVo>> call, Response<List<FeedBackDataVo>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){

                        feedBackDataVos = response.body();

                        if(feedBackDataVos.size()!=0){
                            Log.i(TAG,"데이터가 왔습니다...레이아웃을 그려주세요");
                            feedbackrecyclerview.setVisibility(View.VISIBLE);
                            no_feed_text_list.setVisibility(View.GONE);

                            feedBackEndAdapter = new FeedBackEndAdapter(getActivity(),feedBackDataVos);
                            feedbackrecyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                            feedbackrecyclerview.getItemAnimator().setAddDuration(300);
                            feedbackrecyclerview.getItemAnimator().setRemoveDuration(300);
                            feedbackrecyclerview.getItemAnimator().setMoveDuration(300);
                            feedbackrecyclerview.getItemAnimator().setChangeDuration(300);

                            feedbackrecyclerview.setHasFixedSize(true);
                            feedbackrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(feedBackEndAdapter);
                            alphaAdapter.setDuration(500);
                            feedbackrecyclerview.setAdapter(alphaAdapter);
                        }else{
                            feedbackrecyclerview.setVisibility(View.GONE);
                            no_feed_text_list.setVisibility(View.VISIBLE);
                        }

                    }else{

                    }
                }

                @Override
                public void onFailure(Call<List<FeedBackDataVo>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();

                }
            });
        }



    }






}
