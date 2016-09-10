package com.mom.soccer.fragment;

import android.annotation.TargetApi;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.adapter.BoardItemAdapter;
import com.mom.soccer.adapter.FriendListAdapter;
import com.mom.soccer.adapter.UserMissionAdapter;
import com.mom.soccer.ball.PlayerMainActivity;
import com.mom.soccer.common.Common;
import com.mom.soccer.dataDto.FeedDataVo;
import com.mom.soccer.dataDto.UserMainVo;
import com.mom.soccer.dto.FriendReqVo;
import com.mom.soccer.dto.MomBoard;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.FriendService;
import com.mom.soccer.retrofitdao.MomBoardService;
import com.mom.soccer.retrofitdao.UserMainService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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
    private SlidingUpPanelLayout mLayout;
    private List<MomBoard> momBoardList = new ArrayList<MomBoard>();

    LinearLayoutManager linearLayoutManager;

    ImageView slidingimg;
    private ScrollView scroll_layout;

    RecyclerView recyclerView,userMissionRecyclerview,friendi_recyclerview,
            userMissionPassRecyclerview,searchUserMissionRecyclerview
            ,boardRecview; //board

    FriendListAdapter recyclerAdapter,irecyclerAdapter;

    ImageButton searchCondition;

    UserMissionAdapter userMissionAdapter;

    LinearLayout li_no_found,li_req_no_found,li_team_no_data;
    TextView tx_nodata_found,tx_req_nodata_found,friend_count,friend_no_count,usermissionnocount,usermissioncount;

    //팀게시판기능
    SwipeRefreshLayout swipeRefreshLayout;    //젤리빈 17? 이하는 주석처리로 분기...
    BoardItemAdapter boardItemAdapter;

    public static PlayerFragment newInstance(int page, User user){

        Log.i(TAG,"PlayerFragment newInstance : ==========================");

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = null;

        if(mPage==1){
            view = inflater.inflate(R.layout.fr_player_fragment2, container, false);

            scroll_layout = (ScrollView) view.findViewById(R.id.scroll_layout);
            li_team_no_data = (LinearLayout) view.findViewById(R.id.li_team_no_data);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

            boardRecview = (RecyclerView) view.findViewById(R.id.boardRecview);
            linearLayoutManager = new LinearLayoutManager(getContext());
            getTeamBoarderList();


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getTeamBoarderList();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });


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

        }else if(mPage==2){

            view = inflater.inflate(R.layout.fr_player_fragment3, container, false);

            li_no_found = (LinearLayout) view.findViewById(R.id.li_no_found);

            searchUserMissionRecyclerview = (RecyclerView) view.findViewById(R.id.searchUserMissionRecyclerview);
            UserMainVo query = new UserMainVo();
            getSearchUserMission(query);

            PlayerMainActivity.rightLowerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

                                }
                            })
                            .build();
                    dialog.show();
                }
            });

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
    public void getSearchUserMission(UserMainVo query){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        UserMainService userMainService = ServiceGenerator.createService(UserMainService.class,getContext(),user);
        Call<List<UserMainVo>> c = userMainService.getUserMainList(query);
        c.enqueue(new Callback<List<UserMainVo>>() {
            @Override
            public void onResponse(Call<List<UserMainVo>> call, Response<List<UserMainVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    List<UserMainVo> userMainVos = response.body();

                    if(userMainVos.size()==0){
                        li_no_found.setVisibility(View.VISIBLE);
                    }else{
                        li_no_found.setVisibility(View.GONE);
                    }
                    userMissionAdapter = new UserMissionAdapter(getActivity(),user,userMainVos);
                    searchUserMissionRecyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    searchUserMissionRecyclerview.setHasFixedSize(true);
                    searchUserMissionRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    searchUserMissionRecyclerview.setAdapter(userMissionAdapter);

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


    public void getTeamBoarderList(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        MomBoardService momBoardService = ServiceGenerator.createService(MomBoardService.class,getContext(),user);

        MomBoard query = new MomBoard();
        query.setBoardtypeid(Common.teamid);
        query.setCategory("B");

        Call<List<MomBoard>> c = momBoardService.getBoardHeaderList(query);
        c.enqueue(new Callback<List<MomBoard>>() {
            @Override
            public void onResponse(Call<List<MomBoard>> call, Response<List<MomBoard>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){
                    momBoardList = response.body();

                    if(momBoardList.size() == 0){
                        Log.i(TAG,"보여야 하는것 아님??");
                        li_team_no_data.setVisibility(View.VISIBLE);
                        scroll_layout.setVisibility(View.GONE);
                    }else{
                        li_team_no_data.setVisibility(View.GONE);
                        scroll_layout.setVisibility(View.VISIBLE);
                    }

                    boardRecview.setHasFixedSize(true);
                    boardRecview.setLayoutManager(linearLayoutManager);
                    boardItemAdapter = new BoardItemAdapter(getActivity(),momBoardList,user);

                    boardRecview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    boardRecview.getItemAnimator().setAddDuration(300);
                    boardRecview.getItemAnimator().setRemoveDuration(300);
                    boardRecview.getItemAnimator().setMoveDuration(300);
                    boardRecview.getItemAnimator().setChangeDuration(300);
                    boardRecview.setHasFixedSize(true);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(boardItemAdapter);
                    alphaAdapter.setDuration(500);
                    boardRecview.setAdapter(boardItemAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<MomBoard>> call, Throwable t) {
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

}
