package com.mom.soccer.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.adapter.BoardItemAdapter;
import com.mom.soccer.adapter.FriendListAdapter;
import com.mom.soccer.adapter.UserMissionAdapter;
import com.mom.soccer.ball.PlayerMainActivity;
import com.mom.soccer.bottommenu.SearchActivity;
import com.mom.soccer.common.ActivityResultEvent;
import com.mom.soccer.common.Common;
import com.mom.soccer.common.EventBus;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.dataDto.FeedDataVo;
import com.mom.soccer.dataDto.UserMainVo;
import com.mom.soccer.dto.FriendReqVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.FriendService;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retrofitdao.TeamService;
import com.mom.soccer.retrofitdao.UserMainService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-17.
 */
public class PlayerFragment extends Fragment{

    private static final String TAG = "PlayerFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    private User user = new User();
    private Instructor instructor= new Instructor();

    private SlidingUpPanelLayout mLayout;
    private List<MomBoard> momBoardList = new ArrayList<MomBoard>();
    private static final int COMMENT_LINE_CODE = 201;
    private boolean mLockrecyclerView;

    LinearLayoutManager linearLayoutManager;

    ImageView slidingimg;

    RecyclerView recyclerView,userMissionRecyclerview,friendi_recyclerview,
            userMissionPassRecyclerview,searchUserMissionRecyclerview
            ,boardRecview; //board

    FriendListAdapter recyclerAdapter,irecyclerAdapter;

    ImageButton searchCondition;

    UserMissionAdapter userMissionAdapter;

    LinearLayout li_no_found,li_req_no_found,li_team_no_data;
    TextView tx_nodata_found,tx_req_nodata_found,friend_count,friend_no_count,usermissionnocount,usermissioncount;

    Spinner spinner;
    ArrayList<String> teamsNameList = new ArrayList<>();

    //팀게시판기능
    SwipeRefreshLayout swipeRefreshLayout;    //젤리빈 17? 이하는 주석처리로 분기...
    BoardItemAdapter boardItemAdapter;
    ImageButton teamSearch;
    LinearLayout li_board_no_data_found;

    private int page= 0;
    private int offset = 0;
    private int limit = 40;
    private boolean lastData = false;

    //query
    private boolean queryExecute = false;

    //2page find usermission condition
    CheckBox drible_check,traping_check,lifting_check,around_check,flick_check,complex_check,
            friedn_check,team_check,passyes_check,passno_check;

    UserMainVo query = new UserMainVo();
    List<String> typename = new ArrayList<>();

    List<UserMainVo> userMainVos = new ArrayList<UserMainVo>();


    public static PlayerFragment newInstance(int page, User user,Instructor instructor){

        Log.i(TAG,"PlayerFragment newInstance : ==========================");

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        args.putSerializable(MissionCommon.INS_OBJECT,instructor);
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"fragment onCreate() ===========================================");
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
        instructor = (Instructor) getArguments().getSerializable(MissionCommon.INS_OBJECT);
    }

    @Override
    public void onDestroyView() {
        EventBus.getInstance().unregister(this);
        super.onDestroyView();

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        Log.i(TAG,"fragment onCreateView() ===========================================");
        EventBus.getInstance().register(this);
        View view = null;

        if(mPage==1){
            mLockrecyclerView = true;

            view = inflater.inflate(R.layout.fr_player_fragment2, container, false);
            li_team_no_data = (LinearLayout) view.findViewById(R.id.li_team_no_data);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            teamSearch = (ImageButton) view.findViewById(R.id.teamSearch);
            boardRecview = (RecyclerView) view.findViewById(R.id.boardRecview);
            li_board_no_data_found = (LinearLayout) view.findViewById(R.id.li_board_no_data_found);

            if(Common.teamid==0){
                li_team_no_data.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }else{
                li_team_no_data.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                linearLayoutManager = new LinearLayoutManager(getContext());
                getTeamBoarderList("first");
            }

            if(instructor.getTeamid()!=0){
                li_team_no_data.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                linearLayoutManager = new LinearLayoutManager(getContext());
                getTeamBoarderList("first");
            }

            //1.스와이프 이벤트 설정
            final View finalView2 = view;
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getTeamBoarderList("first");
                    MomSnakBar.show(finalView2,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            teamSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),SearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("goPage",1);
                    startActivity(intent);
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


        }else if(mPage==2){

            view = inflater.inflate(R.layout.fr_player_fragment3, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            li_no_found = (LinearLayout) view.findViewById(R.id.li_no_found);
            linearLayoutManager = new LinearLayoutManager(getContext());
            searchUserMissionRecyclerview = (RecyclerView) view.findViewById(R.id.searchUserMissionRecyclerview);

            getSearchUserMission(query,"new");

            //1.스와이프 이벤트 설정
            final View finalView1 = view;
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getSearchUserMission(query,"new");
                    MomSnakBar.show(finalView1,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            //2
            if(Build.VERSION.SDK_INT  >= 20) {
                searchUserMissionRecyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
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
                searchUserMissionRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener(){
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


            //3
            final View finalView = view;
            searchUserMissionRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        if (linearLayoutManager.findLastVisibleItemPosition() == userMainVos.size()-1) {

                            if(!lastData){
                                getSearchUserMission(query,"next");
                            }else{
                                MomSnakBar.show(finalView,getActivity(),getActivity().getResources().getString(R.string.bottom_msg2));
                            }
                        }
                    }
                }
            });


            //쿼리조건
            PlayerMainActivity.rightLowerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    query = new UserMainVo();
                    if(queryExecute){
                        typename.remove("DRIBLE");
                        typename.remove("TRAPING");
                        typename.remove("LIFTING");
                        typename.remove("AROUND");
                        typename.remove("COMPLEX");
                        typename.remove("FLICK");
                    }


                    MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                            .icon(getActivity().getResources().getDrawable(R.drawable.ic_search_white_36dp))
                            .title(R.string.usermain_misison_filter)
                            .backgroundColor(getActivity().getResources().getColor(R.color.mom_color1))
                            .customView(R.layout.dialog_search_customview, true)
                            .positiveText(R.string.mom_diaalog_find)
                            .negativeText(R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    query.setTypename(typename);
                                    query.setListCount(typename.size());
                                    getSearchUserMission(query,"new");
                                    queryExecute = true;
                                }
                            })
                            .build();
                    dialog.show();

                    spinner = (Spinner) dialog.getCustomView().findViewById(R.id.spinner);
                    ArrayAdapter dataAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, teamsNameList);

                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            query.setTeamname(spinner.getSelectedItem().toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    drible_check = (CheckBox) dialog.getCustomView().findViewById(R.id.drible_check);
                    traping_check = (CheckBox) dialog.getCustomView().findViewById(R.id.traping_check);
                    lifting_check = (CheckBox) dialog.getCustomView().findViewById(R.id.lifting_check);
                    around_check = (CheckBox) dialog.getCustomView().findViewById(R.id.around_check);
                    flick_check = (CheckBox) dialog.getCustomView().findViewById(R.id.flick_check);
                    complex_check = (CheckBox) dialog.getCustomView().findViewById(R.id.complex_check);

                    friedn_check = (CheckBox) dialog.getCustomView().findViewById(R.id.friedn_check);
                    team_check = (CheckBox) dialog.getCustomView().findViewById(R.id.team_check);
                    passyes_check = (CheckBox) dialog.getCustomView().findViewById(R.id.passyes_check);
                    passno_check = (CheckBox) dialog.getCustomView().findViewById(R.id.passno_check);

                    drible_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                typename.add("DRIBLE");
                            }else{
                                typename.remove("DRIBLE");
                            }
                        }
                    });

                    traping_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                typename.add("TRAPING");
                            }else{
                                typename.remove("TRAPING");
                            }
                        }
                    });

                    lifting_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                typename.add("LIFTING");
                            }else{
                                typename.remove("LIFTING");
                            }
                        }
                    });

                    around_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                typename.add("AROUND");
                            }else{
                                typename.remove("AROUND");
                            }
                        }
                    });

                    flick_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                typename.add("FLICK");
                            }else{
                                typename.remove("FLICK");
                            }
                        }
                    });

                    complex_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                typename.add("COMPLEX");
                            }else{
                                typename.remove("COMPLEX");
                            }
                        }
                    });

                    friedn_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                query.setFriendcheck("Y");
                                query.setUid(user.getUid());
                            }else{
                                query.setFriendcheck("N");
                                query.setUid(0);
                            }
                        }
                    });

                    team_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                query.setTeamcheck("Y");
                                query.setTeamid(Common.teamid);
                            }else{
                                query.setTeamcheck("N");
                                query.setTeamid(0);
                            }
                        }
                    });

                    passyes_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                query.setPassflag("Y");
                                passno_check.setChecked(false);
                            }else{
                                query.setPassflag("N");
                            }
                        }
                    });

                    passno_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked){
                                query.setPassflag("N");
                                passyes_check.setChecked(false);
                            }else{
                                query.setPassflag(null);
                            }
                        }
                    });

                }
            });

            getTeamNameList();

        }else if(mPage==3){

            view = inflater.inflate(R.layout.fr_player_fragment4, container, false);

            li_no_found = (LinearLayout) view.findViewById(R.id.li_no_found);
            tx_nodata_found = (TextView) view.findViewById(R.id.tx_nodata_found);

            li_req_no_found = (LinearLayout) view.findViewById(R.id.li_req_no_found);
            tx_req_nodata_found = (TextView) view.findViewById(R.id.tx_req_nodata_found);

            friend_count = (TextView) view.findViewById(R.id.friend_count);
            friend_no_count = (TextView) view.findViewById(R.id.friend_no_count);

            slidingimg = (ImageView) view.findViewById(R.id.slidingimg);
            mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
            mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {}

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                    if(newState.toString().equals("COLLAPSED")){
                        slidingimg.setImageResource(R.drawable.ic_vertical_align_top_white_24dp);
                    }else if(newState.toString().equals("EXPANDED")){
                        slidingimg.setImageResource(R.drawable.ic_vertical_align_bottom_white_24dp);
                    }
                }
            });
            recyclerView = (RecyclerView) view.findViewById(R.id.friend_recyclerview);
            friendi_recyclerview = (RecyclerView) view.findViewById(R.id.friendi_recyclerview);
            friendReqList();
            friendRList();
            getfriendCount();
        }

        return view;
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void getUserMainData(final String passflag){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        UserMainService userMainService = ServiceGenerator.createService(UserMainService.class,getContext(),user);

        UserMainVo query = new UserMainVo();
        query.setUid(user.getUid());
        query.setPassflag(passflag);

        Call<List<UserMainVo>> c = userMainService.getUserMainList(query);
        c.enqueue(new Callback<List<UserMainVo>>() {
            @Override
            public void onResponse(Call<List<UserMainVo>> call, Response<List<UserMainVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    List<UserMainVo> userMainVos = response.body();

                    if(passflag.equals("Y") ){
                        if(userMainVos.size()==0){
                            li_req_no_found.setVisibility(View.VISIBLE);
                        }else{
                            li_req_no_found.setVisibility(View.GONE);
                        }
                        userMissionAdapter = new UserMissionAdapter(getActivity(),user,userMainVos);
                        userMissionPassRecyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        userMissionPassRecyclerview.setHasFixedSize(true);
                        userMissionPassRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                        userMissionPassRecyclerview.setAdapter(userMissionAdapter);
                    }else{
                        if(userMainVos.size()==0){
                            li_no_found.setVisibility(View.VISIBLE);
                        }else{
                            li_no_found.setVisibility(View.GONE);
                        }
                        userMissionRecyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        userMissionAdapter = new UserMissionAdapter(getActivity(),user,userMainVos);
                        userMissionRecyclerview.setHasFixedSize(true);
                        userMissionRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                        userMissionRecyclerview.setAdapter(userMissionAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserMainVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    //친구들과 팀원들의 미션을 본다.
    public void getSearchUserMission(UserMainVo query, final String queryStatus){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        UserMainService userMainService = ServiceGenerator.createService(UserMainService.class,getContext(),user);
        Call<List<UserMainVo>> c = userMainService.getUserMainList(query);

        if(queryStatus.equals("new")) {
            offset = 0;
        }else{
            page = page + 1;
            offset = limit * page;
        }
        query.setOffset(offset);
        query.setLimit(limit);

        c.enqueue(new Callback<List<UserMainVo>>() {
            @Override
            public void onResponse(Call<List<UserMainVo>> call, Response<List<UserMainVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){

                    if(queryStatus.equals("new")){
                        userMainVos = response.body();

                        Log.i(TAG,"!#!##!#! : " + userMainVos.size());

                        if(userMainVos.size()==0){
                            li_no_found.setVisibility(View.VISIBLE);
                        }else{
                            li_no_found.setVisibility(View.GONE);
                        }

                        searchUserMissionRecyclerview.setHasFixedSize(true);
                        searchUserMissionRecyclerview.setLayoutManager(linearLayoutManager);
                        userMissionAdapter = new UserMissionAdapter(getActivity(),user,userMainVos);

                        searchUserMissionRecyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        searchUserMissionRecyclerview.getItemAnimator().setAddDuration(300);
                        searchUserMissionRecyclerview.getItemAnimator().setRemoveDuration(300);
                        searchUserMissionRecyclerview.getItemAnimator().setMoveDuration(300);
                        searchUserMissionRecyclerview.getItemAnimator().setChangeDuration(300);
                        searchUserMissionRecyclerview.setHasFixedSize(true);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(userMissionAdapter);
                        alphaAdapter.setDuration(500);
                        searchUserMissionRecyclerview.setAdapter(userMissionAdapter);

                        //페이징 초기화
                        lastData = false;
                        offset  = 0;
                        page    = 0;


                    }else{
                        List<UserMainVo> userMainVoList = response.body();

                        for(int i = 0 ; i < userMainVoList.size() ; i++)
                        {
                            userMainVos.add(userMainVoList.get(i));
                        }

                        userMissionAdapter.notifyDataSetChanged();

                        if(userMainVoList.size() != limit){
                            lastData = true;
                            userMainVoList = new ArrayList<UserMainVo>();
                        }
                        WaitingDialog.cancelWaitingDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserMainVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    public void getUserMissionCount(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        DataService dataService = ServiceGenerator.createService(DataService.class,getContext(),user);
        Call<FeedDataVo> c = dataService.getUserMissionCount(user.getUid());
        c.enqueue(new Callback<FeedDataVo>() {
            @Override
            public void onResponse(Call<FeedDataVo> call, Response<FeedDataVo> response) {
                if(response.isSuccessful()){
                    FeedDataVo vo = response.body();
                    usermissioncount.setText(String.valueOf(vo.getCompletecount()));
                    usermissionnocount.setText(String.valueOf(vo.getIncompletecount()));
                }
            }

            @Override
            public void onFailure(Call<FeedDataVo> call, Throwable t) {

            }
        });
    }

    public void getfriendCount(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        FriendService friendService = ServiceGenerator.createService(FriendService.class,getContext(),user);
        FeedDataVo query =  new FeedDataVo();
        query.setUid(user.getUid());
        Call<FeedDataVo> c = friendService.getAllfriend(query);
        c.enqueue(new Callback<FeedDataVo>() {
            @Override
            public void onResponse(Call<FeedDataVo> call, Response<FeedDataVo> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    FeedDataVo vo = response.body();
                    friend_count.setText(String.valueOf(vo.getCompletecount()));
                    friend_no_count.setText(String.valueOf(vo.getIncompletecount()));
                }
            }

            @Override
            public void onFailure(Call<FeedDataVo> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void friendReqList(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        final FriendService friendService = ServiceGenerator.createService(FriendService.class,getContext(),user);

        Call<List<FriendReqVo>> c = friendService.FriendReqList(user.getUid(),"REQUEST");

        c.enqueue(new Callback<List<FriendReqVo>>() {
            @Override
            public void onResponse(Call<List<FriendReqVo>> call, Response<List<FriendReqVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    List<FriendReqVo> friendReqVos = response.body();

                    if(friendReqVos.size()==0){
                        li_req_no_found.setVisibility(View.VISIBLE);
                        tx_req_nodata_found.setText(getString(R.string.friend__req_no_data));
                    }else{
                        li_req_no_found.setVisibility(View.GONE);
                    }

                    recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    recyclerView.getItemAnimator().setAddDuration(300);
                    recyclerView.getItemAnimator().setRemoveDuration(300);
                    recyclerView.getItemAnimator().setMoveDuration(300);
                    recyclerView.getItemAnimator().setChangeDuration(300);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerAdapter = new FriendListAdapter(getActivity(),friendReqVos,user);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(recyclerAdapter);
                    alphaAdapter.setDuration(500);
                    recyclerView.setAdapter(alphaAdapter);
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<FriendReqVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void friendRList() {
        WaitingDialog.showWaitingDialog(getActivity(),false);
        final FriendService friendService = ServiceGenerator.createService(FriendService.class, getContext(), user);
        Call<List<FriendReqVo>> c = friendService.FriendReqList(user.getUid(), "ACCEPT");
        c.enqueue(new Callback<List<FriendReqVo>>() {
            @Override
            public void onResponse(Call<List<FriendReqVo>> call, Response<List<FriendReqVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if (response.isSuccessful()) {
                    List<FriendReqVo> friendReqVos = response.body();

                    if(friendReqVos.size()==0){
                        li_no_found.setVisibility(View.VISIBLE);
                        tx_nodata_found.setText(getString(R.string.friend_no_data));
                    }else{
                        li_no_found.setVisibility(View.GONE);
                    }

                    friendi_recyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    friendi_recyclerview.getItemAnimator().setAddDuration(300);
                    friendi_recyclerview.getItemAnimator().setRemoveDuration(300);
                    friendi_recyclerview.getItemAnimator().setMoveDuration(300);
                    friendi_recyclerview.getItemAnimator().setChangeDuration(300);
                    friendi_recyclerview.setHasFixedSize(true);
                    friendi_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    irecyclerAdapter = new FriendListAdapter(getActivity(), friendReqVos, user);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(irecyclerAdapter);
                    alphaAdapter.setDuration(500);
                    friendi_recyclerview.setAdapter(alphaAdapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<FriendReqVo>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    public void getTeamBoarderList(final String pageFlag){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getContext(),user);

        MomBoard query = new MomBoard();
        query.setBoardtypeid(Common.teamid);

        if(instructor.getInstructorid()!=0){
            query.setBoardtypeid(instructor.getTeamid());
        }

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
                        if (momBoardList.size() == 0) {
                            li_board_no_data_found.setVisibility(View.VISIBLE);
                        } else {
                            li_board_no_data_found.setVisibility(View.GONE);
                        }

                        boardRecview.setHasFixedSize(true);
                        boardRecview.setLayoutManager(linearLayoutManager);
                        boardItemAdapter = new BoardItemAdapter(getActivity(), momBoardList, user,instructor,"user");

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


    @Subscribe
    public void recevedUpdateBoardFile(final MomBoard momBoard){
        if(mPage==1){
            Log.i(TAG,"버스 이벤트 : " +momBoard.toString());
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
                        boardItemAdapter.updateHeaderImage(momBoard.getPosition(),board);
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

    public void getTeamNameList(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        TeamService teamService = ServiceGenerator.createService(TeamService.class,getActivity(),user);
        Call<ArrayList<String>> c = teamService.getTeamNameList();
        c.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    teamsNameList = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
