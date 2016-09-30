package com.mom.soccer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mom.soccer.R;
import com.mom.soccer.adapter.UserPointHistoryAdapter;
import com.mom.soccer.dataDto.Report;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.SpBalanceLine;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.momactivity.MomMainActivity;
import com.mom.soccer.retrofitdao.MomComService;
import com.mom.soccer.retrofitdao.PointService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mom.soccer.momactivity.MomMainActivity.activity;

/**
 * Created by sungbo on 2016-08-17.
 */
public class PointFragment extends Fragment {

    private static final String TAG = "PlayerFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private User user = new User();

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

        View view = null;


        if(mPage==1){

            view = inflater.inflate(R.layout.fr_point_fragment_layout0, container, false);

            final EditText text_reason = (EditText) view.findViewById(R.id.text_reason);
            final Button btnPointReq = (Button) view.findViewById(R.id.btnPointReq);

            text_reason.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    btnPointReq.setEnabled(s.toString().trim().length() > 0);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            btnPointReq.setEnabled(false);

            btnPointReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WaitingDialog.showWaitingDialog(getContext(),false);
                    MomComService momComService = ServiceGenerator.createService(MomComService.class,getActivity(),user);

                    Report report = new Report();
                    report.setType("REQ_POINT");
                    report.setUsername(user.getUsername());
                    report.setUid(user.getUid());
                    report.setPublisherid(user.getUid());
                    report.setReason(text_reason.getText().toString());
                    report.setContent("point request");
                    Call<ServerResult> c = momComService.saveReport(report);
                    c.enqueue(new Callback<ServerResult>() {
                        @Override
                        public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                            WaitingDialog.cancelWaitingDialog();
                            if(response.isSuccessful()){
                                ServerResult result = response.body();
                                if(result.getCount()==1){
                                    new MaterialDialog.Builder(getActivity())
                                            .content(R.string.point_reqcheck)
                                            .positiveText(R.string.mom_diaalog_confirm)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    Intent intent = new Intent(getContext(), MomMainActivity.class);
                                                    getActivity().startActivity(intent);
                                                }
                                            })
                                            .show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResult> call, Throwable t) {
                            WaitingDialog.cancelWaitingDialog();
                            t.printStackTrace();
                        }
                    });
                }
            });

            new MaterialDialog.Builder(getActivity())
                    .icon(getResources().getDrawable(R.drawable.ic_alert_title_mom))
                    .title(R.string.point_chage_title1)
                    .titleColor(getResources().getColor(R.color.color6))
                    .content(R.string.point_chage_title2)
                    .contentColor(getResources().getColor(R.color.color6))
                    .positiveText(R.string.mom_diaalog_confirm)
                    .positiveColor(activity.getResources().getColor(R.color.enabled_red))
                    .show();

        }else if(mPage==2){
            view = inflater.inflate(R.layout.fr_point_fragment_layout, container, false);
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
                    }
                }

                @Override
                public void onFailure(Call<List<SpBalanceLine>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });

        }

        return view;
    }

}
