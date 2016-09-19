package com.mom.soccer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.adapter.TeamMemberAdapter;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.InsInfoVo;
import com.mom.soccer.dataDto.UserMainVo;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.TeamApply;
import com.mom.soccer.dto.User;
import com.mom.soccer.ins.InsMainActivity;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.DataService;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retrofitdao.TeamService;
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
public class InsFragmentTeamApply extends Fragment{

    private static final String TAG = "InsFragmentTeamApply";
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private User user = new User();
    private Instructor ins = new Instructor();
    private InsInfoVo insInfoVo = new InsInfoVo();
    private TeamApply teamApply = new TeamApply();

    Button btnTeamRequest;

    //TeamMember List
    private List<UserMainVo> userMainVos = new ArrayList<UserMainVo>();
    private RecyclerView memberRecyclerView;
    private TeamMemberAdapter teamMemberAdapter;

    public static InsFragmentTeamApply newInstance(int page, User user,Instructor ins){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        args.putSerializable(MissionCommon.INS_OBJECT,ins);
        InsFragmentTeamApply fragment = new InsFragmentTeamApply();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
        ins = (Instructor) getArguments().getSerializable(MissionCommon.INS_OBJECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View v=null;


        if(mPage==1){
            v = inflater.inflate(R.layout.fr_ins_apply_fragment_layout, container, false);
            setPageInsInfo(v);

        }else if(mPage==2){
            v = inflater.inflate(R.layout.fr_ins_member_fragment_layout, container, false);
            memberRecyclerView = (RecyclerView) v.findViewById(R.id.memberRecyclerView);
            getTeamMemberList();

        }else if(mPage==3){

        }
        return v;
    }

    //TeamMember 현황
    public void getTeamMemberList(){
        WaitingDialog.showWaitingDialog(getActivity(),false);
        DataService dataService = ServiceGenerator.createService(DataService.class,getContext(),user);
        Call<List<UserMainVo>> c = dataService.getTeamMemberList(ins.getInstructorid());
        c.enqueue(new Callback<List<UserMainVo>>() {
            @Override
            public void onResponse(Call<List<UserMainVo>> call, Response<List<UserMainVo>> response) {
                WaitingDialog.cancelWaitingDialog();
                if(response.isSuccessful()){

                    userMainVos = response.body();

                    if(userMainVos.size()==0){

                    }else{
                        memberRecyclerView.setHasFixedSize(true);
                        memberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        teamMemberAdapter = new TeamMemberAdapter(getActivity(),userMainVos,user);

                        memberRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        memberRecyclerView.getItemAnimator().setAddDuration(300);
                        memberRecyclerView.getItemAnimator().setRemoveDuration(300);
                        memberRecyclerView.getItemAnimator().setMoveDuration(300);
                        memberRecyclerView.getItemAnimator().setChangeDuration(300);
                        memberRecyclerView.setHasFixedSize(true);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(teamMemberAdapter);
                        alphaAdapter.setDuration(500);
                        memberRecyclerView.setAdapter(teamMemberAdapter);
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


    //제자 요청 관련
    public void setPageInsInfo(final View v){
        Log.i(TAG,"팀지원을 위한 강사 선택 화면");
        WaitingDialog.showWaitingDialog(getContext(),false);
        InstructorService instructorService = ServiceGenerator.createService(InstructorService.class,getContext(),user);

        Log.i(TAG,"Error 체크");

        final Call<InsInfoVo> infoVoCall = instructorService.getInsInfoApply(ins.getInstructorid());
        infoVoCall.enqueue(new Callback<InsInfoVo>() {
            @Override
            public void onResponse(Call<InsInfoVo> call, Response<InsInfoVo> response) {
                if(response.isSuccessful()){
                    WaitingDialog.cancelWaitingDialog();
                    insInfoVo = response.body();
                    Log.i(TAG,"insInfoVo : " + insInfoVo.toString());

                    ImageView userimg = (ImageView) v.findViewById(R.id.insimage);
                    ImageView teamimg = (ImageView) v.findViewById(R.id.teamimage);

                    TextView  name = (TextView) v.findViewById(R.id.name);
                    TextView  teamname = (TextView) v.findViewById(R.id.teamname);

                    TextView  teammembercount = (TextView) v.findViewById(R.id.teammembercount);
                    TextView  questioncount = (TextView) v.findViewById(R.id.questioncount);
                    TextView  answercount = (TextView) v.findViewById(R.id.answercount);
                    TextView  estimation = (TextView) v.findViewById(R.id.estimation);
                    TextView  insvidecount = (TextView) v.findViewById(R.id.insvidecount);

                    TextView  pubvideopoint = (TextView) v.findViewById(R.id.pubvideopoint);
                    TextView  pubwordpoint = (TextView) v.findViewById(R.id.pubwordpoint);

                    TextView  teamvideopoint = (TextView) v.findViewById(R.id.teamvideopoint);
                    TextView  teamwordpoint = (TextView) v.findViewById(R.id.teamwordpoint);

                    TextView  pubpasspoint = (TextView) v.findViewById(R.id.pubpasspoint);
                    TextView  teampasspoint = (TextView) v.findViewById(R.id.teampasspoint);
                    btnTeamRequest = (Button) v.findViewById(R.id.btnTeamRequest);

                    /****************
                     * Assgin db data
                     * ****************/

                    if(!Compare.isEmpty(insInfoVo.getProfileimgurl())){
                        Glide.with(getContext())
                                .load(insInfoVo.getProfileimgurl())
                                .asBitmap().transform(new RoundedCornersTransformation(getContext(),10,5))
                                .into(userimg);
                    }

                    if(!Compare.isEmpty(insInfoVo.getEmblem())){
                        Glide.with(getContext())
                                .load(insInfoVo.getEmblem())
                                .asBitmap().transform(new RoundedCornersTransformation(getContext(),10,5))
                                .into(teamimg);
                    }

                    name.setText(insInfoVo.getName());
                    teamname.setText(insInfoVo.getTeamname());

                    teammembercount.setText(String.valueOf(insInfoVo.getTeammembercount()));
                    questioncount.setText(String.valueOf(insInfoVo.getQuestioncount()));
                    answercount.setText(String.valueOf(insInfoVo.getAnswercount()));
                    estimation.setText(String.valueOf(insInfoVo.getEstimation()));
                    insvidecount.setText(String.valueOf(insInfoVo.getInsvidecount()));

                    pubvideopoint.setText(String.valueOf(insInfoVo.getPubvideopoint()));
                    pubwordpoint.setText(String.valueOf(insInfoVo.getPubwordpoint()));

                    teamvideopoint.setText(String.valueOf(insInfoVo.getTeamvideopoint()));
                    teamwordpoint.setText(String.valueOf(insInfoVo.getTeamwordpoint()));

                    pubpasspoint.setText(String.valueOf(insInfoVo.getPubpasspoint()));
                    teampasspoint.setText(String.valueOf(insInfoVo.getTeampasspoint()));

                    //화면이 처음 실행 될때. 내가 강사라면 다른팀 지원 불가

                    Log.i(TAG,"강사 검색 1 " + Auth.insFlag);

                    if(Auth.insFlag.equals("Y")){
                        btnTeamRequest.setVisibility(View.GONE);
                    }else {
                        btnTeamRequest.setVisibility(View.VISIBLE);
                        applyTeam("QUERY", btnTeamRequest);
                        btnTeamRequest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i(TAG,"강사 검색 2 " + teamApply.getTeamid());
                                if (teamApply.getTeamid() == 0) {
                                    applyTeam("REQUEST", null);
                                } else {

                                    if (teamApply.getTeamid() == insInfoVo.getTeamid()) { //제자요청을 했거나 현재 제자이라면
                                        if (teamApply.getApproval().equals("REQUEST") || teamApply.getApproval().equals("APPROVAL")) {
                                            applyTeam("WITHDRAWAL", null);
                                        }
                                    } else {
                                        btnTeamRequest.setVisibility(View.INVISIBLE);
                                    }

                                }

                            }
                        });
                    }
                }else{
                    WaitingDialog.cancelWaitingDialog();
                }
            }

            @Override
            public void onFailure(Call<InsInfoVo> call, Throwable t) {
                Log.i(TAG,"Error 체크 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }

    public void applyTeam(String applyType, final Button button){

        WaitingDialog.showWaitingDialog(getContext(),false);
        TeamService teamService = ServiceGenerator.createService(TeamService.class,getContext(),user);

        final TeamApply apply = new TeamApply();
        apply.setUid(user.getUid());

        if(applyType.equals("REQUEST")){
            apply.setInstructorid(ins.getInstructorid());
            apply.setTeamid(insInfoVo.getTeamid());
            apply.setApproval("REQUEST");
            apply.setEnabled("Y");
            apply.setUsername(user.getUsername());

            Log.i(TAG,"User NAme : " +user.toString());

            Call<ServerResult> call = teamService.applyTeam(apply);
            call.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        WaitingDialog.cancelWaitingDialog();
                        ServerResult result = response.body();
                        Intent intent = new Intent(getContext(), InsMainActivity.class);
                        intent.putExtra("inspath","search");
                        intent.putExtra(MissionCommon.INS_OBJECT,ins);
                        intent.putExtra("gopage",0);
                        getActivity().finish();
                        startActivity(intent);

                    }else {
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }else if(applyType.equals("QUERY")){
            Call<TeamApply> c = teamService.getTeamApply(apply);
            c.enqueue(new Callback<TeamApply>() {
                @Override
                public void onResponse(Call<TeamApply> call, Response<TeamApply> response) {
                    if(response.isSuccessful()){
                        WaitingDialog.cancelWaitingDialog();
                        teamApply = response.body();


                        //제가요청 테이블에 자료가 없다면
                        if(teamApply.getTeamid()==0){
                            button.setText(getString(R.string.ins_team_apply));
                        }else{
                            //현재 누군가의 제자이거나, 제자 요청을 했다면..
                            if(teamApply.getTeamid() == insInfoVo.getTeamid()){ //제자요청을 했거나 현재 제자이라면
                                if(teamApply.getApproval().equals("REQUEST")){
                                    button.setText(getString(R.string.ins_team_pregress));
                                }else if(teamApply.getApproval().equals("APPROVAL")){  //현재 제자일 경우
                                    button.setText(getString(R.string.ins_team_applied));
                                }
                            }else{
                                button.setVisibility(View.INVISIBLE);
                            }

                        }

                    }else {
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<TeamApply> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                }
            });
        }else if(applyType.equals("WITHDRAWAL")){

            apply.setInstructorid(insInfoVo.getInstructorid());
            apply.setTeamid(insInfoVo.getTeamid());

            Log.i(TAG,"제자 삭제 요청 : " + apply.toString());

            Call<ServerResult> c = teamService.deleteTeamApply(apply);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    if(response.isSuccessful()){
                        WaitingDialog.cancelWaitingDialog();
                        Intent intent = new Intent(getContext(), InsMainActivity.class);
                        intent.putExtra("inspath","search");
                        intent.putExtra(MissionCommon.INS_OBJECT,ins);
                        intent.putExtra("gopage",0);
                        getActivity().finish();
                        startActivity(intent);

                    }else {
                        WaitingDialog.cancelWaitingDialog();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }
    }

}
