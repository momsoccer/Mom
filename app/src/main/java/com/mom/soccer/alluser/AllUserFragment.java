package com.mom.soccer.alluser;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.mom.soccer.R;
import com.mom.soccer.alluser.adapter.AdBoardAdapter;
import com.mom.soccer.alluser.adapter.AllUserBoardItemAdapter;
import com.mom.soccer.alluser.adapter.InsVideoAdapter;
import com.mom.soccer.common.ActivityResultEvent;
import com.mom.soccer.common.EventBus;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.common.internal.BusObject;
import com.mom.soccer.dto.AdBoardVo;
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.AdBoardService;
import com.mom.soccer.retrofitdao.InsVideoService;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-09-18.
 */
public class AllUserFragment extends Fragment {

    private static final String TAG = "AllUserFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private SwipeRefreshLayout swipeRefreshLayout;

    private int mPage;

    private User user = new User();
    private Instructor instructor= new Instructor();

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView boardRcView;

    //paing function
    private int page= 0;
    private int offset = 0;
    private int limit = 40;
    private boolean lastData = false;

    //query
    private boolean queryExecute = false;

    //adapter
    private AdBoardAdapter adBoardAdapter;
    private AllUserBoardItemAdapter allUserBoardItemAdapter;
    private InsVideoAdapter insVideoAdapter;

    //view
    LinearLayout li_no_found;

    //data
    private List<AdBoardVo> adBoardVos = new ArrayList<AdBoardVo>();
    private List<MomBoard> momBoardList = new ArrayList<MomBoard>();
    private List<InsVideoVo> insVideoVos = new ArrayList<InsVideoVo>();

    public static AllUserFragment newInstance(int page, User user){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        AllUserFragment fragment = new AllUserFragment();



        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
        instructor = (Instructor) getArguments().getSerializable(MissionCommon.INS_OBJECT);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getInstance().register(this);
        View view = null;

        if(mPage==1){
            view = inflater.inflate(R.layout.all_user_fragment1, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            boardRcView = (RecyclerView) view.findViewById(R.id.boardRcView);
            //li_no_found = (LinearLayout) view.findViewById(R.id.li_no_found);
            linearLayoutManager = new LinearLayoutManager(getContext());

            getTeamBoarderList("first");

            final View finalView1 = view;
            //2.스와이프 이벤트 버전별
            if(Build.VERSION.SDK_INT  >= 22) {  //롤리팝 5.1 이상
                boardRcView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
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
                boardRcView.setOnScrollListener(new RecyclerView.OnScrollListener(){
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

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getTeamBoarderList("first");
                    MomSnakBar.show(finalView1,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            //3.스와이프 이벤트 페이징
            final View finalView = view;
            boardRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        }else if(mPage==2){

            view = inflater.inflate(R.layout.all_user_fragment2, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            linearLayoutManager = new LinearLayoutManager(getContext());
            boardRcView = (RecyclerView) view.findViewById(R.id.boardRcView);
            li_no_found = (LinearLayout) view.findViewById(R.id.li_no_found);

            final InsVideoVo query = new InsVideoVo();
            getInsVideoList("first",query);

            final View finalView1 = view;
            //2.스와이프 이벤트 버전별
            if(Build.VERSION.SDK_INT  >= 22) {
                boardRcView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
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
                boardRcView.setOnScrollListener(new RecyclerView.OnScrollListener(){
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

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getInsVideoList("first",query);
                    MomSnakBar.show(finalView1,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            //3.스와이프 이벤트 페이징
            final View finalView = view;
            boardRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findLastVisibleItemPosition() == adBoardVos.size()-1) {
                            if(!lastData){
                                getInsVideoList("next",query);
                            }else{
                                MomSnakBar.show(finalView,getActivity(),getActivity().getResources().getString(R.string.bottom_msg1));
                            }
                        }
                    }
                }
            });



        }else if(mPage==3) {
            view = inflater.inflate(R.layout.all_user_fragment3, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            boardRcView = (RecyclerView) view.findViewById(R.id.boardRcView);
            linearLayoutManager = new LinearLayoutManager(getContext());

            getAdBoardData("new");
            final View finalView1 = view;
            if(Build.VERSION.SDK_INT  >= 22) {
                boardRcView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
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
                boardRcView.setOnScrollListener(new RecyclerView.OnScrollListener(){
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

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getAdBoardData("next");
                    MomSnakBar.show(finalView1,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            //3.스와이프 이벤트 페이징
            final View finalView = view;
            boardRcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (linearLayoutManager.findLastVisibleItemPosition() == adBoardVos.size()-1) {
                            if(!lastData){
                                getAdBoardData("next");
                            }else{
                                MomSnakBar.show(finalView,getActivity(),getActivity().getResources().getString(R.string.bottom_msg1));
                            }
                        }
                    }
                }
            });

        }
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getInstance().unregister(this);
    }

    public void getInsVideoList(final String pageFlag, InsVideoVo videoVo){

        if(pageFlag.equals("first")) {
            offset = 0;
        }else{
            page = page + 1;
            offset = limit * page;
        }
        videoVo.setOffset(offset);
        videoVo.setLimit(limit);

        WaitingDialog.showWaitingDialog(getActivity(),false);
        InsVideoService insVideoService = ServiceGenerator.createService(InsVideoService.class,getContext(),user);
        Call<List<InsVideoVo>> c = insVideoService.getVideoList(videoVo);
        c.enqueue(new Callback<List<InsVideoVo>>() {
            @Override
            public void onResponse(Call<List<InsVideoVo>> call, Response<List<InsVideoVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    if(pageFlag.equals("first")){

                        insVideoVos = response.body();

                        if(insVideoVos.size()==0){
                            //li_no_found.setVisibility(View.VISIBLE);
                        }else{
                            //li_no_found.setVisibility(View.GONE);
                        }

                        insVideoAdapter = new InsVideoAdapter(getActivity(),insVideoVos,user);
                        boardRcView.setHasFixedSize(true);
                        boardRcView.setLayoutManager(linearLayoutManager);
                        boardRcView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        boardRcView.getItemAnimator().setAddDuration(300);
                        boardRcView.getItemAnimator().setRemoveDuration(300);
                        boardRcView.getItemAnimator().setMoveDuration(300);
                        boardRcView.getItemAnimator().setChangeDuration(300);
                        boardRcView.setHasFixedSize(true);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(insVideoAdapter);
                        alphaAdapter.setDuration(300);
                        boardRcView.setAdapter(insVideoAdapter);

                        //페이징 초기화
                        lastData = false;
                        offset  = 0;
                        page    = 0;

                    }else{
                        List<InsVideoVo> insVideoVos1 = response.body();

                        for(int i = 0 ; i < insVideoVos1.size() ; i++)
                        {
                            insVideoVos.add(insVideoVos1.get(i));
                        }

                        insVideoAdapter.notifyDataSetChanged();

                        if(insVideoVos.size() != limit){
                            lastData = true;
                            insVideoVos = new ArrayList<InsVideoVo>();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<InsVideoVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });

    }

    public void getTeamBoarderList(final String pageFlag){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getContext(),user);

        MomBoard query = new MomBoard();
        query.setBoardtype("nomal");

        if(pageFlag.equals("first")) {
            offset = 0;
        }else{
            page = page + 1;
            offset = limit * page;
        }
        query.setOffset(offset);
        query.setLimit(limit);

        Call<List<MomBoard>> c = momBoardService.getAllBoardHeaderList(query);
        c.enqueue(new Callback<List<MomBoard>>() {
            @Override
            public void onResponse(Call<List<MomBoard>> call, Response<List<MomBoard>> response) {
                if(response.isSuccessful()){
                    if(pageFlag.equals("first")) {

                        WaitingDialog.cancelWaitingDialog();
                        momBoardList = response.body();

                        Log.i(TAG,"momBoardList : ** " + momBoardList.size());

                        if (momBoardList.size() == 0) {
                           // li_no_found.setVisibility(View.VISIBLE);
                        } else {
                           // li_no_found.setVisibility(View.GONE);
                        }

                        boardRcView.setHasFixedSize(true);
                        boardRcView.setLayoutManager(linearLayoutManager);
                        allUserBoardItemAdapter = new AllUserBoardItemAdapter(getActivity(), momBoardList, user);

                        boardRcView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        boardRcView.getItemAnimator().setAddDuration(300);
                        boardRcView.getItemAnimator().setRemoveDuration(300);
                        boardRcView.getItemAnimator().setMoveDuration(300);
                        boardRcView.getItemAnimator().setChangeDuration(300);
                        boardRcView.setHasFixedSize(true);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(allUserBoardItemAdapter);
                        alphaAdapter.setDuration(500);
                        boardRcView.setAdapter(allUserBoardItemAdapter);

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

                        allUserBoardItemAdapter.notifyDataSetChanged();

                        if(addBoardList.size() != limit){
                            lastData = true;
                            addBoardList = new ArrayList<MomBoard>();
                        }
                        WaitingDialog.cancelWaitingDialog();
                    }
                }else{
                    Log.i(TAG,"====================-------------------------");
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

    public void getAdBoardData(final String queryType){

        AdBoardVo query = new AdBoardVo();

        if(queryType.equals("new")){
            offset = 0;
        }else{
            page = page + 1;
            offset = limit * page;
        }
        query.setOffset(offset);
        query.setLimit(limit);

        WaitingDialog.showWaitingDialog(getActivity(),false);
        AdBoardService adBoardService = ServiceGenerator.createService(AdBoardService.class,getActivity(),user);
        Call<List<AdBoardVo>> c = adBoardService.getList(query);
        c.enqueue(new Callback<List<AdBoardVo>>() {
            @Override
            public void onResponse(Call<List<AdBoardVo>> call, Response<List<AdBoardVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    if(queryType.equals("new")){
                        adBoardVos = response.body();

                        Log.i(TAG,"from server data size : " + adBoardVos.toString());

                        adBoardAdapter = new AdBoardAdapter(getActivity(),adBoardVos);
                        boardRcView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        boardRcView.setHasFixedSize(true);
                        boardRcView.setLayoutManager(linearLayoutManager);
                        boardRcView.setAdapter(adBoardAdapter);

                        //페이징 초기화
                        lastData = false;
                        offset  = 0;
                        page    = 0;
                    }else{

                        List<AdBoardVo> adBoardVosadd = response.body();
                        for(int i = 0 ; i < adBoardVosadd.size() ; i++)
                        {
                            adBoardVos.add(adBoardVosadd.get(i));
                        }
                        adBoardAdapter.notifyDataSetChanged();

                        if(adBoardVosadd.size() != limit){
                            lastData = true;
                            adBoardVosadd = new ArrayList<AdBoardVo>();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<List<AdBoardVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    @Subscribe
    public void recevedUpdateBoardFile(final MomBoard momBoard){
        if(mPage==1){
            WaitingDialog.showWaitingDialog(getActivity(),false);
            MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getContext(),user);

            MomBoard query = new MomBoard();
            query.setBoardid(momBoard.getBoardid());
            Call<MomBoard> momBoardCall = momBoardService.getBoardHeader(query);
            momBoardCall.enqueue(new Callback<MomBoard>() {
                @Override
                public void onResponse(Call<MomBoard> call, Response<MomBoard> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        MomBoard board = response.body();
                        allUserBoardItemAdapter.updateHeaderImage(momBoard.getPosition(),board);
                    }
                }

                @Override
                public void onFailure(Call<MomBoard> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent){
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
    }

    //강사배열 변경시 이벤트
    @Subscribe
    public void receveInsObjectCommnet(BusObject busObject){
        int i = busObject.getPosition();
        int count = busObject.getLineCount();
        if(mPage==2) {
            insVideoAdapter.updateLineItemCount(i,count);
        }
    }

    @Subscribe
    public void receveInsObjectLikeCount(BusObject busObject){
        int i = busObject.getPosition();
        int count = busObject.getLineCount();
        if(mPage==2) {
            insVideoAdapter.updateLineItemLikeCount(i,count);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


}
