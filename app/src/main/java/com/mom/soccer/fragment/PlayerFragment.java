package com.mom.soccer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.adapter.FriendListAdapter;
import com.mom.soccer.dto.FriendReqVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.FriendService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

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
public class PlayerFragment extends Fragment {

    private static final String TAG = "PlayerFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private User user = new User();

    LinearLayout li_layout_page1,li_layout_page2,li_layout_page3;

    RecyclerView recyclerView;
    FriendListAdapter recyclerAdapter,irecyclerAdapter;

    RecyclerView recyclerview,friendi_recyclerview;
    List<FriendReqVo> friendReqVos = new ArrayList<>();
    TextView friend_no_data,friend_req_data;



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
        Log.i(TAG,"onCreate : ==========================" + mPage);
        Log.i(TAG,"user : " + user.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fr_player_fragment, container, false);

        li_layout_page1 = (LinearLayout) view.findViewById(R.id.li_layout_page1);
        li_layout_page2 = (LinearLayout) view.findViewById(R.id.li_layout_page2);
        li_layout_page3 = (LinearLayout) view.findViewById(R.id.li_layout_page3);

        friend_no_data = (TextView) view.findViewById(R.id.friend_no_data);
        friend_req_data = (TextView) view.findViewById(R.id.friend_req_data);

        if(mPage==1){
            li_layout_page1.setVisibility(View.VISIBLE);
            li_layout_page2.setVisibility(View.GONE);
            li_layout_page3.setVisibility(View.GONE);
            friend_no_data.setVisibility(View.GONE);
            friend_req_data.setVisibility(View.GONE);


        }else if(mPage==2){
            li_layout_page1.setVisibility(View.GONE);
            li_layout_page2.setVisibility(View.VISIBLE);
            li_layout_page3.setVisibility(View.GONE);
            friend_no_data.setVisibility(View.GONE);
            friend_req_data.setVisibility(View.GONE);


        }else if(mPage==3){
            li_layout_page1.setVisibility(View.GONE);
            li_layout_page2.setVisibility(View.GONE);
            li_layout_page3.setVisibility(View.VISIBLE);

            recyclerView = (RecyclerView) view.findViewById(R.id.friend_recyclerview);
            friendi_recyclerview = (RecyclerView) view.findViewById(R.id.friendi_recyclerview);
            friendReqList();
            friendRList();
        }

        return view;
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
                    friendReqVos = response.body();

                    if(friendReqVos.size()==0){
                        friend_req_data.setVisibility(View.VISIBLE);
                    }else{
                        friend_req_data.setVisibility(View.GONE);
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
                    friendReqVos = response.body();

                    Log.i(TAG, "정보는 : " + friendReqVos.toString());

                    if(friendReqVos.size()==0){
                        friend_no_data.setVisibility(View.VISIBLE);
                    }else{
                        friend_no_data.setVisibility(View.GONE);
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
}
