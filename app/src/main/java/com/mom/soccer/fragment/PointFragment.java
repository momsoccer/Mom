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

import com.mom.soccer.R;
import com.mom.soccer.adapter.UserPointHistoryAdapter;
import com.mom.soccer.dto.SpBalanceLine;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.PointService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-17.
 */
public class PointFragment extends Fragment {

    private static final String TAG = "PlayerFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private User user = new User();

    LinearLayout li_layout_page1,li_layout_page2;

    RecyclerView recyclerView;
    UserPointHistoryAdapter pointHistoryAdapter;


    public static PointFragment newInstance(int page, User user){

        Log.i(TAG,"PlayerFragment newInstance : ==========================");

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        PointFragment fragment = new PointFragment();
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

        View view = inflater.inflate(R.layout.fr_point_fragment_layout, container, false);

        li_layout_page1 = (LinearLayout) view.findViewById(R.id.li_layout_page1);
        li_layout_page2 = (LinearLayout) view.findViewById(R.id.li_layout_page2);

        if(mPage==1){
            li_layout_page1.setVisibility(View.VISIBLE);
            li_layout_page2.setVisibility(View.GONE);
        }else if(mPage==2){
            li_layout_page1.setVisibility(View.GONE);
            li_layout_page2.setVisibility(View.VISIBLE);

            recyclerView = (RecyclerView) view.findViewById(R.id.point_recyclerview);
            WaitingDialog.showWaitingDialog(getContext(),false);
            PointService service = ServiceGenerator.createService(PointService.class,getContext(),user);
            Call<List<SpBalanceLine>> call = service.getCashLineList(user.getUid());
            call.enqueue(new Callback<List<SpBalanceLine>>() {
                @Override
                public void onResponse(Call<List<SpBalanceLine>> call, Response<List<SpBalanceLine>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){

                        List<SpBalanceLine> spBalanceLines = response.body();

                        pointHistoryAdapter = new UserPointHistoryAdapter(getContext(),spBalanceLines);
                        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        recyclerView.getItemAnimator().setAddDuration(300);
                        recyclerView.getItemAnimator().setRemoveDuration(300);
                        recyclerView.getItemAnimator().setMoveDuration(300);
                        recyclerView.getItemAnimator().setChangeDuration(300);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(pointHistoryAdapter);
                        alphaAdapter.setDuration(500);
                        recyclerView.setAdapter(alphaAdapter);

                    }else{
                        Log.i(TAG,"getPoint List Error 1");
                    }
                }

                @Override
                public void onFailure(Call<List<SpBalanceLine>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    Log.i(TAG,"getPoint List Error 2");
                    t.printStackTrace();
                }
            });

        }

        return view;
    }

}
