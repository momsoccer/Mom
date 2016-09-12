package com.mom.soccer.ins;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.BoardItemAdapter;
import com.mom.soccer.adapter.FeedBackEndAdapter;
import com.mom.soccer.adapter.FeedBackReqAdapter;
import com.mom.soccer.adapter.PassListInsAdapter;
import com.mom.soccer.adapter.PassResultListInsAdapter;
import com.mom.soccer.adapter.TeamApplyAdapter;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.dataDto.FeedBackDataVo;
import com.mom.soccer.dataDto.FeedDataVo;
import com.mom.soccer.dto.FeedbackHeader;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.MissionPass;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.TeamApply;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.FeedBackService;
import com.mom.soccer.retrofitdao.MissionPassService;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retrofitdao.TeamService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
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

    private RecyclerView feedback_req_recyclerview,pass_req_recyclerview
                          ,passRecyclerView,feedbackrecyclerview
                        ,teamApplyRecyclerview,appRecyclerview
            ,boardRecview;

    private FeedBackReqAdapter feedBackReqAdapter;
    private FeedBackEndAdapter feedBackEndAdapter;
    private PassListInsAdapter passListInsAdapter;
    private PassResultListInsAdapter passResultListInsAdapter;

    //팀원관련
    private TeamApplyAdapter teamApplyAdapter, approvteamApplyAdapter;


    private List<FeedbackHeader> feedbackHeaders;
    private List<FeedBackDataVo> feedBackDataVos;

    ImageView slidingimg;
    TextView feedbackCount,feedbackNonCount;

    //2번 페이지 심사관련
    private List<MissionPass> missionPasses;
    private GridLayoutManager gaggeredGridLayoutManager;

    //팀 게시판
    SwipeRefreshLayout swipeRefreshLayout;    //젤리빈 17? 이하는 주석처리로 분기...
    BoardItemAdapter boardItemAdapter;

    private int page= 0;
    private int offset = 0;
    private int limit = 40;
    private boolean lastData = false;
    LinearLayoutManager linearLayoutManager;
    private List<MomBoard> momBoardList = new ArrayList<MomBoard>();

    private LinearLayout li_board_no_data_found;

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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = null;

        if(mPage == 1){
            view = inflater.inflate(R.layout.fr_dash_feedback_main0, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            linearLayoutManager = new LinearLayoutManager(getContext());
            li_board_no_data_found = (LinearLayout) view.findViewById(R.id.li_board_no_data_found);
            boardRecview = (RecyclerView) view.findViewById(R.id.boardRecview);

            getTeamBoarderList("first");

            final View finalView2 = view;
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getTeamBoarderList("first");
                    MomSnakBar.show(finalView2,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });


            //2.스와이프 이벤트 버전별
            if(Build.VERSION.SDK_INT  >= 20) {
                boardRecview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }else{
                boardRecview.setOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }

            //3.스와이프 이벤트 페이징
            final View finalView = view;
            boardRecview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        if (linearLayoutManager.findLastVisibleItemPosition() == momBoardList.size()-1) {

                            if(!lastData){
                                getTeamBoarderList("next");
                            }else{
                                MomSnakBar.show(finalView,getActivity(),getActivity().getResources().getString(R.string.bottom_msg1));
                            }
                        }
                    }
                }
            });


        } else if(mPage == 2 ){  //피드백 심사
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

        }else if(mPage == 3 ){ //심사
            view = inflater.inflate(R.layout.fr_dash_feedback_main2, container, false);
            slidingimg = (ImageView) view.findViewById(R.id.slidingimg);
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

            //피드내용
            pass_req_recyclerview = (RecyclerView) view.findViewById(R.id.pass_req_recyclerview);
            getMissionPassList();

            //심사총 자료
            feedbackCount = (TextView) view.findViewById(R.id.feedbackCount);
            feedbackNonCount = (TextView) view.findViewById(R.id.feedbackNonCount);
            getPassData();

            passRecyclerView = (RecyclerView) view.findViewById(R.id.passRecyclerView);
            getPassResultData();

        }else if(mPage == 4 ) {//기타 강사 메뉴
            view = inflater.inflate(R.layout.fr_dash_feedback_main3, container, false);
            slidingimg = (ImageView) view.findViewById(R.id.slidingimg);
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

            teamApplyRecyclerview = (RecyclerView) view.findViewById(R.id.teamApplyRecyclerview);
            appRecyclerview = (RecyclerView) view.findViewById(R.id.appRecyclerview);
            getTeamApplyList();
            getTeamMemberList();

            feedbackCount = (TextView) view.findViewById(R.id.feedbackCount);
            feedbackNonCount = (TextView) view.findViewById(R.id.feedbackNonCount);

            getTeamCount();
        }
        return view;
    }


    public void getTeamBoarderList(final String pageFlag){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getContext(),user);

        MomBoard query = new MomBoard();
        query.setBoardtypeid(ins.getTeamid());

        if(pageFlag.equals("first")) {
            offset = 0;
        }else{
            page = page + 1;
            offset = limit * page;
        }
        query.setOffset(offset);
        query.setLimit(limit);

        Call<List<MomBoard>> c = momBoardService.getBoardHeaderList(query);
        c.enqueue(new Callback<List<MomBoard>>() {
            @Override
            public void onResponse(Call<List<MomBoard>> call, Response<List<MomBoard>> response) {
                if(response.isSuccessful()){
                    if(pageFlag.equals("first")) {
                        WaitingDialog.cancelWaitingDialog();
                        momBoardList = response.body();

                        Log.i(TAG,"게시판 데이터는 " + momBoardList.size());

                        if (momBoardList.size() == 0) {
                            li_board_no_data_found.setVisibility(View.VISIBLE);
                        } else {
                            li_board_no_data_found.setVisibility(View.GONE);
                        }

                        boardRecview.setHasFixedSize(true);
                        boardRecview.setLayoutManager(linearLayoutManager);
                        boardItemAdapter = new BoardItemAdapter(getActivity(), momBoardList, user,ins,"ins");

                        boardRecview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        boardRecview.getItemAnimator().setAddDuration(300);
                        boardRecview.getItemAnimator().setRemoveDuration(300);
                        boardRecview.getItemAnimator().setMoveDuration(300);
                        boardRecview.getItemAnimator().setChangeDuration(300);
                        boardRecview.setHasFixedSize(true);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(boardItemAdapter);
                        alphaAdapter.setDuration(500);
                        boardRecview.setAdapter(boardItemAdapter);

                        //페이징 초기화
                        lastData = false;
                        offset  = 0;
                        page    = 0;

                    }else{
                        List<MomBoard> addBoardList = response.body();

                        for(int i = 0 ; i < addBoardList.size() ; i++)
                        {
                            momBoardList.add(addBoardList.get(i));
                        }

                        boardItemAdapter.notifyDataSetChanged();

                        if(addBoardList.size() != limit){
                            lastData = true;
                            addBoardList = new ArrayList<MomBoard>();
                        }
                        WaitingDialog.cancelWaitingDialog();
                    }
                }else{
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<List<MomBoard>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    public void getTeamApplyList(){

        WaitingDialog.showWaitingDialog(getActivity(),false);
        TeamService t = ServiceGenerator.createService(TeamService.class,getContext(),user);
        Call<List<TeamApply>> c = t.getReqMember(ins.getInstructorid(),"REQUEST");
        c.enqueue(new Callback<List<TeamApply>>() {
            @Override
            public void onResponse(Call<List<TeamApply>> call, Response<List<TeamApply>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    List<TeamApply> applyList = response.body();

                    teamApplyAdapter = new TeamApplyAdapter(getActivity(),applyList,ins);
                    teamApplyRecyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    teamApplyRecyclerview.getItemAnimator().setAddDuration(300);
                    teamApplyRecyclerview.getItemAnimator().setRemoveDuration(300);
                    teamApplyRecyclerview.getItemAnimator().setMoveDuration(300);
                    teamApplyRecyclerview.getItemAnimator().setChangeDuration(300);

                    teamApplyRecyclerview.setHasFixedSize(true);
                    gaggeredGridLayoutManager = new GridLayoutManager(getContext(),2);
                    teamApplyRecyclerview.setLayoutManager(gaggeredGridLayoutManager);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(teamApplyAdapter);
                    alphaAdapter.setDuration(500);
                    teamApplyRecyclerview.setAdapter(alphaAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<TeamApply>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    public void getTeamMemberList(){

        WaitingDialog.showWaitingDialog(getActivity(),false);
        TeamService t = ServiceGenerator.createService(TeamService.class,getContext(),user);
        Call<List<TeamApply>> c = t.getReqMember(ins.getInstructorid(),"APPROVAL");
        c.enqueue(new Callback<List<TeamApply>>() {
            @Override
            public void onResponse(Call<List<TeamApply>> call, Response<List<TeamApply>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    List<TeamApply> applyList = response.body();


                    approvteamApplyAdapter = new TeamApplyAdapter(getActivity(),applyList,ins);
                    appRecyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    appRecyclerview.getItemAnimator().setAddDuration(300);
                    appRecyclerview.getItemAnimator().setRemoveDuration(300);
                    appRecyclerview.getItemAnimator().setMoveDuration(300);
                    appRecyclerview.getItemAnimator().setChangeDuration(300);

                    appRecyclerview.setHasFixedSize(true);
                    gaggeredGridLayoutManager = new GridLayoutManager(getContext(),2);
                    appRecyclerview.setLayoutManager(gaggeredGridLayoutManager);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(approvteamApplyAdapter);
                    alphaAdapter.setDuration(500);
                    appRecyclerview.setAdapter(alphaAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<TeamApply>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
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
                            gaggeredGridLayoutManager = new GridLayoutManager(getContext(),2);
                            feedback_req_recyclerview.setLayoutManager(gaggeredGridLayoutManager);
                            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(feedBackReqAdapter);
                            alphaAdapter.setDuration(500);
                            feedback_req_recyclerview.setAdapter(alphaAdapter);
                        }else{
                            feedback_req_recyclerview.setVisibility(View.GONE);
                        }
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
            query.setOrderbytype("feedbackid");
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

    //페이지2 강사심사...
    public void getMissionPassList(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        MissionPassService service = ServiceGenerator.createService(MissionPassService.class,getContext(),user);
        MissionPass query  = new MissionPass();
        query.setInstructorid(ins.getInstructorid());
        query.setStatus("REQUEST");
        Call<List<MissionPass>> c = service.getMissionPassList(query);
        c.enqueue(new Callback<List<MissionPass>>() {
            @Override
            public void onResponse(Call<List<MissionPass>> call, Response<List<MissionPass>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    missionPasses = response.body();
                    passListInsAdapter = new PassListInsAdapter(getActivity(),missionPasses,ins);
                    pass_req_recyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    pass_req_recyclerview.getItemAnimator().setAddDuration(300);
                    pass_req_recyclerview.getItemAnimator().setRemoveDuration(300);
                    pass_req_recyclerview.getItemAnimator().setMoveDuration(300);
                    pass_req_recyclerview.getItemAnimator().setChangeDuration(300);

                    pass_req_recyclerview.setHasFixedSize(true);
                    gaggeredGridLayoutManager = new GridLayoutManager(getContext(),2);
                    pass_req_recyclerview.setLayoutManager(gaggeredGridLayoutManager);

                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(passListInsAdapter);
                    alphaAdapter.setDuration(500);
                    pass_req_recyclerview.setAdapter(alphaAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<MissionPass>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    //심사결과
    public void getPassResultData(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        MissionPassService service = ServiceGenerator.createService(MissionPassService.class,getContext(),user);
        MissionPass query  = new MissionPass();
        query.setInstructorid(ins.getInstructorid());
        query.setStatus("SUCCESS");
        Call<List<MissionPass>> c = service.getMissionPassResultList(query);
        c.enqueue(new Callback<List<MissionPass>>() {
            @Override
            public void onResponse(Call<List<MissionPass>> call, Response<List<MissionPass>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    List<MissionPass> pass = response.body();

                    passResultListInsAdapter = new PassResultListInsAdapter(getActivity(),pass,ins);
                    passRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    passRecyclerView.getItemAnimator().setAddDuration(300);
                    passRecyclerView.getItemAnimator().setRemoveDuration(300);
                    passRecyclerView.getItemAnimator().setMoveDuration(300);
                    passRecyclerView.getItemAnimator().setChangeDuration(300);

                    passRecyclerView.setHasFixedSize(true);
                    gaggeredGridLayoutManager = new GridLayoutManager(getContext(),2);
                    passRecyclerView.setLayoutManager(gaggeredGridLayoutManager);

                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(passResultListInsAdapter);
                    alphaAdapter.setDuration(500);
                    passRecyclerView.setAdapter(alphaAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<MissionPass>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void getPassData(){
        WaitingDialog.showWaitingDialog(getActivity());
        DataService dataService = ServiceGenerator.createService(DataService.class,getActivity(),user);
        Call<FeedDataVo> passDataVoCall = dataService.getPassData(ins.getInstructorid());
        passDataVoCall.enqueue(new Callback<FeedDataVo>() {
            @Override
            public void onResponse(Call<FeedDataVo> call, Response<FeedDataVo> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    FeedDataVo vo = response.body();

                    Log.i(TAG,"FeedDataVo : "+ vo.toString());

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

    public void getTeamCount(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        Log.i(TAG,"vo : ####################################");
        TeamService teamService = ServiceGenerator.createService(TeamService.class,getContext(),ins);
        Call<FeedDataVo> c = teamService.getTeamCount(ins.getInstructorid());
        c.enqueue(new Callback<FeedDataVo>() {
            @Override
            public void onResponse(Call<FeedDataVo> call, Response<FeedDataVo> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    FeedDataVo vo = response.body();
                    Log.i(TAG,"vo : " + vo.toString());

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


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
/*
        feedBackReqAdapter.notifyDataSetChanged();
        feedBackEndAdapter.notifyDataSetChanged();
        passListInsAdapter.notifyDataSetChanged();
       passResultListInsAdapter.notifyDataSetChanged();
  */
    }



}
