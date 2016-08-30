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
import android.widget.ImageView;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.FeedBackEndAdapter;
import com.mom.soccer.adapter.FeedBackReqAdapter;
import com.mom.soccer.adapter.PassListInsAdapter;
import com.mom.soccer.dataDto.FeedBackDataVo;
import com.mom.soccer.dataDto.FeedDataVo;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retrofitdao.MissionPassService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.ButterKnife;
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

    ImageView slidingimg;
    TextView feedbackCount,feedbackNonCount;

    //2번 페이지 심사관련
    private RecyclerView pass_req_recyclerview;
    private PassListInsAdapter passListInsAdapter;
    private List<MissionPass> missionPasses;


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
        ButterKnife.bind(getActivity());
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
        ins = (Instructor) getArguments().getSerializable(MissionCommon.INS_OBJECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = null;

        if(mPage==1){  //피드백 심사
            view = inflater.inflate(R.layout.fr_dash_feedback_main1, container, false);
            slidingimg = (ImageView) view.findViewById(R.id.slidingimg);
            feedback_req_recyclerview = (RecyclerView) view.findViewById(R.id.feedback_req_recyclerview);

            FeedReqList(view,"req");
            feedbackrecyclerview = (RecyclerView) view.findViewById(R.id.feedbackrecyclerview);

            mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
            mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {

                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                    if(newState.toString().equals("COLLAPSED")){
                        slidingimg.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                    }else if(newState.toString().equals("EXPANDED")){
                        slidingimg.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    }
                }
            });

            FeedReqList(view,"req_end");


            feedbackCount = (TextView) view.findViewById(R.id.feedbackCount);
            feedbackNonCount = (TextView) view.findViewById(R.id.feedbackNonCount);
            //카운터 구하기
            FeedCount();

        }else if(mPage == 2 ){ //심사
            view = inflater.inflate(R.layout.fr_dash_feedback_main2, container, false);
            slidingimg = (ImageView) view.findViewById(R.id.slidingimg);
            mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
            mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                    if(newState.toString().equals("COLLAPSED")){
                        slidingimg.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                    }else if(newState.toString().equals("EXPANDED")){
                        slidingimg.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    }
                }
            });

            //피드내용
            pass_req_recyclerview = (RecyclerView) view.findViewById(R.id.pass_req_recyclerview);
            getMissionPassList();

        }else if(mPage == 3 ) {//기타 강사 메뉴
            view = inflater.inflate(R.layout.fr_dash_feedback_main3, container, false);
            slidingimg = (ImageView) view.findViewById(R.id.slidingimg);
            mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
            mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                    if(newState.toString().equals("COLLAPSED")){
                        slidingimg.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                    }else if(newState.toString().equals("EXPANDED")){
                        slidingimg.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    }
                }
            });


            //내용


        }
        return view;
    }

    //피드백 총 카운트구하기
    public void FeedCount(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        DataService dataService = ServiceGenerator.createService(DataService.class,getContext(),user);
        Call<FeedDataVo> c = dataService.getFeedData(ins.getInstructorid());
        c.enqueue(new Callback<FeedDataVo>() {
            @Override
            public void onResponse(Call<FeedDataVo> call, Response<FeedDataVo> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    FeedDataVo vo = response.body();
                    feedbackCount.setText(String.valueOf(vo.getCompletecount()));
                    feedbackNonCount.setText(String.valueOf(vo.getIncompletecount()));
                }
            }

            @Override
            public void onFailure(Call<FeedDataVo> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    //피드백 답변전,후 자료
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

    /*

        private RecyclerView pass_req_recyclerview;
    private PassListInsAdapter passListInsAdapter;
    private List<MissionPass> missionPasses;

     */

    //페이지2 강사심사...
    public void getMissionPassList(){
        MissionPassService service = ServiceGenerator.createService(MissionPassService.class,getContext(),user);

        MissionPass query  = new MissionPass();
        query.setInstructorid(ins.getInstructorid());
        query.setStatus("REQUEST");

        Call<List<MissionPass>> c = service.getMissionPassList(query);
        c.enqueue(new Callback<List<MissionPass>>() {
            @Override
            public void onResponse(Call<List<MissionPass>> call, Response<List<MissionPass>> response) {
                if(response.isSuccessful()){
                    missionPasses = response.body();

                    Log.i(TAG,"미션 패스 응답 값은 : " + missionPasses.toString());

                    passListInsAdapter = new PassListInsAdapter(getActivity(),missionPasses,ins);
                    pass_req_recyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    pass_req_recyclerview.getItemAnimator().setAddDuration(300);
                    pass_req_recyclerview.getItemAnimator().setRemoveDuration(300);
                    pass_req_recyclerview.getItemAnimator().setMoveDuration(300);
                    pass_req_recyclerview.getItemAnimator().setChangeDuration(300);

                    pass_req_recyclerview.setHasFixedSize(true);
                    pass_req_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(passListInsAdapter);
                    alphaAdapter.setDuration(500);
                    pass_req_recyclerview.setAdapter(alphaAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<MissionPass>> call, Throwable t) {

            }
        });

    }

}
