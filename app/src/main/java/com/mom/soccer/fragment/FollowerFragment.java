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

import com.mom.soccer.R;
import com.mom.soccer.adapter.FollowerUserAdapter;
import com.mom.soccer.dto.FollowManage;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.FollowService;
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
 * Created by sungbo on 2016-08-16.
 */
public class FollowerFragment extends Fragment {

    private static final String TAG = "FollowerFragment";
    public static final String ARG_PAGE = "ARG_PAGE";

    RecyclerView recyclerView;
    FollowerUserAdapter adapter;
    List<FollowManage> list = new ArrayList<>();
    private int queryuid;

    //parameter
    private int mPage;
    private User user = new User();

    public static FollowerFragment newInstance(int page, User user,int queryuid){

        Log.i(TAG,"FollowerFragment : ==========================");

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putInt("queryuid", queryuid);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        FollowerFragment fragment = new FollowerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG,"onCreate : ==========================");

        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
        queryuid = getArguments().getInt("queryuid");
        Log.i(TAG,"onCreate : ==========================" + mPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        Log.i(TAG,"onCreateView : ==========================");


        View view = inflater.inflate(R.layout.fr_followe_card_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.follower_recyclerview);


        if(mPage==1){
            WaitingDialog.showWaitingDialog(getContext(),false);
            FollowService service = ServiceGenerator.createService(FollowService.class,getContext(),user);
            FollowManage followManage = new FollowManage();
            followManage.setFollowtype(2);
            followManage.setUid(queryuid);

            Call<List<FollowManage>> c = service.getMeFollowList(followManage);
            c.enqueue(new Callback<List<FollowManage>>() {
                @Override
                public void onResponse(Call<List<FollowManage>> call, Response<List<FollowManage>> response) {
                    if(response.isSuccessful()){
                        list = response.body();
                        WaitingDialog.cancelWaitingDialog();
                        adapter = new FollowerUserAdapter(getContext(),list,user);
                        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        recyclerView.getItemAnimator().setAddDuration(300);
                        recyclerView.getItemAnimator().setRemoveDuration(300);
                        recyclerView.getItemAnimator().setMoveDuration(300);
                        recyclerView.getItemAnimator().setChangeDuration(300);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                        alphaAdapter.setDuration(500);
                        recyclerView.setAdapter(alphaAdapter);
                    }else{
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<List<FollowManage>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });


        }else if(mPage==2){
            WaitingDialog.showWaitingDialog(getContext(),false);
            FollowService service = ServiceGenerator.createService(FollowService.class,getContext(),user);
            FollowManage followManage = new FollowManage();
            followManage.setFollowtype(1);
            followManage.setUid(queryuid);
            Call<List<FollowManage>> c = service.getMeFollowList(followManage);

            c.enqueue(new Callback<List<FollowManage>>() {
                @Override
                public void onResponse(Call<List<FollowManage>> call, Response<List<FollowManage>> response) {
                    if(response.isSuccessful()){
                        list = response.body();
                        WaitingDialog.cancelWaitingDialog();
                        adapter = new FollowerUserAdapter(getContext(),list,user);
                        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        recyclerView.getItemAnimator().setAddDuration(300);
                        recyclerView.getItemAnimator().setRemoveDuration(300);
                        recyclerView.getItemAnimator().setMoveDuration(300);
                        recyclerView.getItemAnimator().setChangeDuration(300);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                        alphaAdapter.setDuration(500);
                        recyclerView.setAdapter(alphaAdapter);
                    }else{
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<List<FollowManage>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
            /*

                        //삭제는 자기 자신것만..
            if(user.getUid()==queryuid){
                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                            }

                            @Override
                            public void onLongItemClick(View view, final int i) {
                                new MaterialDialog.Builder(getContext())
                                        .content(R.string.f_layout_arlam_cancel)
                                        .positiveText(R.string.mom_diaalog_confirm)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                FollowManage f = new FollowManage();
                                                f.setFollowid(list.get(i).getFollowid());

                                                WaitingDialog.showWaitingDialog(getContext(),false);
                                                FollowService service = ServiceGenerator.createService(FollowService.class,getContext(),user);
                                                Call<ServerResult> c = service.deleteFollow(f);
                                                c.enqueue(new Callback<ServerResult>() {
                                                    @Override
                                                    public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                                                        if(response.isSuccessful()){
                                                            ServerResult result = response.body();
                                                            WaitingDialog.cancelWaitingDialog();
                                                            VeteranToast.makeToast(getContext(),list.get(i).getUsername()+getContext().getString(R.string.follow_pickup_cancel), Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(getContext(), FollowActivity.class);
                                                            getActivity().finish();
                                                            startActivity(intent);

                                                        }else{
                                                            WaitingDialog.cancelWaitingDialog();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ServerResult> call, Throwable t) {
                                                        WaitingDialog.cancelWaitingDialog();
                                                    }
                                                });
                                            }
                                        })
                                        .negativeText(R.string.mom_diaalog_cancel)
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            }
                                        })
                                        .backgroundColor(getResources().getColor(R.color.mom_color1))
                                        .show();
                            }
                        })
                );
            }

             */
        }



        return view;
    }

}
