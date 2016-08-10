/**
 * Copyright 2015 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mom.soccer.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.dataDto.PointTrVo;
import com.mom.soccer.dto.FavoriteMission;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.MissionHistory;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.MissionMainActivity;
import com.mom.soccer.retrofitdao.FavoriteMissionService;
import com.mom.soccer.retrofitdao.MissionHistoryService;
import com.mom.soccer.retrofitdao.MissionService;
import com.mom.soccer.retrofitdao.PointService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.DialogBuilder;
import com.mom.soccer.widget.VeteranToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bartosz Lipinski
 * 28.01.15
 */
public class MainMissionFragment extends Fragment {

    private static final String TAG = "MainMissionFragment";

    public static final String MISSIONTYPE = "MISSIONTYPE";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String MISSION_ID = "MISSION_ID";

    private static int mPage;
    private static int oPenCount = 0;

    private Mission mission = new Mission();

    private static final int REQUEST_MISSION_OPEN = 768;

    LinearLayout mMainLayout;

    int favoriteCount = 0;
    User user;

    //재갱신...
    Mission queryMission;
    Mission reflashMission = new Mission();

    String checkDuplicateFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG,"onCreate() ======================");

        mPage = getArguments().getInt(ARG_PAGE);
        mission = (Mission) getArguments().getSerializable(MissionCommon.OBJECT);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);

        Log.i(TAG,"미션 객체 값 : " + mission.toString());
        Log.i(TAG,"유저 객체 값 : " + user.toString());
    }

    public static MainMissionFragment newInstance(int page,
                                                  Mission mission,
                                                  User pramUser) {

        MainMissionFragment fragment = new MainMissionFragment();
        //각 프래그먼트에 넘길 함수

        Bundle bdl = new Bundle();

        bdl.putInt(ARG_PAGE,page);
        //mission 객체에 담기
        bdl.putSerializable(MissionCommon.OBJECT,mission);
        bdl.putSerializable(MissionCommon.USER_OBJECT,pramUser);
        fragment.setArguments(bdl);


        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(TAG,"onCreateView() ======================");

        if(reflashMission.getMissionid()!=0){
            mission = reflashMission;
        }

        Log.i(TAG,"Mission info : " + mission.toString());

        View view = null;
        view = inflater.inflate(R.layout.fr_mission_layout, container, false);
        mMainLayout = (LinearLayout) view.findViewById(R.id.li_mission_back);
        Context context = getContext();

        if(mission.getTypename().equals("DRIBLE")){
            mMainLayout.setBackground(getResources().getDrawable(R.drawable.drible_back));
        }else if(mission.getTypename().equals("LIFTING")){
            mMainLayout.setBackground(getResources().getDrawable(R.drawable.lifting_back));
        }else if(mission.getTypename().equals("TRAPING")){
            mMainLayout.setBackground(getResources().getDrawable(R.drawable.trappring_back));
        }else if(mission.getTypename().equals("AROUND")){
            mMainLayout.setBackground(getResources().getDrawable(R.drawable.around_back));
        }else if(mission.getTypename().equals("FLICK")){
            mMainLayout.setBackground(getResources().getDrawable(R.drawable.flick_back));
        }else if(mission.getTypename().equals("COMPLEX")){
            mMainLayout.setBackground(getResources().getDrawable(R.drawable.complex_back));
        }

        final TextView tx_mission_level = (TextView) view.findViewById(R.id.tx_mission_level);
        final TextView tx_mission_name = (TextView) view.findViewById(R.id.tx_mission_name);
        final TextView tx_mission_disp = (TextView) view.findViewById(R.id.tx_mission_description);
        final TextView tx_mission_precon = (TextView) view.findViewById(R.id.tx_mission_precon);

        final TextView tx_mission_open = (TextView) view.findViewById(R.id.tx_mission_open);
        final TextView tx_mission_upload = (TextView) view.findViewById(R.id.tx_mission_upload);
        final TextView tx_mission_pass = (TextView) view.findViewById(R.id.tx_mission_pass);
        final TextView tx_mission_cash_point = (TextView) view.findViewById(R.id.tx_mission_cash_point);

        tx_mission_level.setText("Lv."+mission.getSequence());

        tx_mission_name.setText(mission.getMissionname());
        tx_mission_disp.setText(mission.getDescription());
        tx_mission_precon.setText(mission.getPrecon());

        final LinearLayout li_lock_close = (LinearLayout) view.findViewById(R.id.li_lock_close);
        final LinearLayout li_lock_open = (LinearLayout) view.findViewById(R.id.li_lock_open);

        //포인트관련
        final String mission_open_point = String.valueOf(mission.getEscapepoint());
        String mission_score = String.valueOf(mission.getGrade());
        String mission_pass = String.valueOf(mission.getPassgrade());
        String mission_point = String.valueOf(mission.getGetpoint());

        final Button missionStartBtn = (Button) view.findViewById(R.id.btn_mission_start);

        if(mission.getOpencount()==0){

            //미션을 도전하지 않은 상태 레이아웃 변경
            li_lock_close.setVisibility(View.VISIBLE);
            li_lock_open.setVisibility(View.GONE);
            missionStartBtn.setText(getString(R.string.mission_open));
        }else{
            //미션을 도전한 상태 상태 레이아웃 변경
            li_lock_close.setVisibility(View.GONE);
            li_lock_open.setVisibility(View.VISIBLE);
            missionStartBtn.setText(getString(R.string.mission_start));

        }

        tx_mission_open.setText(getString(R.string.mission_require_point)+" : "+mission_open_point+"P");
        tx_mission_upload.setText(getString(R.string.mission_get_upload_score)+" : "+mission_score+"점");
        tx_mission_pass.setText(getString(R.string.mission_clear_score)+" : "+mission_pass +"점");
        tx_mission_cash_point.setText(getString(R.string.mission_clear_point)+" : "+mission_point+"P");

        /*  //업로드 했을 경우
        if(mission.getUploadcount()!=0){

        }

           //미션을 클리어 했을 경우
        if(mission.getMissionpasscount()!=0){

        }
        */

        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.gansim_star);
        favoriteTransaction(user.getUid(),mission.getMissionid(),"getCount",imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(favoriteCount==0){
                    favoriteTransaction(user.getUid(),mission.getMissionid(),"create",imageButton);
                    favoriteCount=1;
                }else{
                    favoriteTransaction(user.getUid(),mission.getMissionid(),"delete",imageButton);
                    favoriteCount=0;
                }

            }
        });

        missionStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //유저가 이미 지불했을 경우...만약의 경우를 위해 코딩한다.
                final MissionHistoryService service = ServiceGenerator.createService(MissionHistoryService.class,getContext(),user);
                MissionHistory history = new MissionHistory(user.getUid(),mission.getMissionid());
                Call<MissionHistory> call = service.getMissionHistory(history);
                call.enqueue(new Callback<MissionHistory>() {
                    @Override
                    public void onResponse(Call<MissionHistory> call, Response<MissionHistory> response) {
                        if(response.isSuccessful()){
                            MissionHistory missionHistory = response.body();
                            Log.i(TAG,"1.결과값을 받았습니다 : " + missionHistory.toString());
                            if(missionHistory.getUid() == user.getUid() && mission.getOpencount()==0) {
                                //이미 결재를 했다면... 그런데...openCount가 0이라면...중복결재이기 때문에 넘긴다
                                Intent intent = new Intent(getContext(),MissionMainActivity.class);
                                intent.putExtra(MissionCommon.OBJECT,mission);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                                checkDuplicateFlag = "duble" ;
                            }else{
                                checkDuplicateFlag = "initial" ;

                                if(checkDuplicateFlag.equals("initial")){
                                    if(mission.getOpencount()==0){
                                        new DialogBuilder(getContext())
                                                //.setTitle("Title")
                                                .setMessage(" "+mission_open_point + getString(R.string.mission_dialg_mission_open)) //500P 가 차감됩니다...미션을 오픈 하시겠습니까?
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        //2. 포인트를 차감한다
                                                        PointTrVo pointTrVo = new PointTrVo();
                                                        pointTrVo.setUid(user.getUid());
                                                        pointTrVo.setMissionid(mission.getMissionid());
                                                        pointTrVo.setTrType("G"); //R일때는 지급
                                                        pointTrVo.setTypecode("MISSION");

                                                        final PointService pointService = ServiceGenerator.createService(PointService.class,getContext(),user);
                                                        Call<ServerResult> call = pointService.pointTr(pointTrVo);

                                                        call.enqueue(new Callback<ServerResult>() {
                                                            @Override
                                                            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                                                                if(response.isSuccessful()){
                                                                    ServerResult result = response.body();
                                                                    if(result.getResult().equals("F")){
                                                                        VeteranToast.makeToast(getContext(),getString(R.string.lack_point),Toast.LENGTH_SHORT).show();
                                                                    }else{
                                                                        openMission();
                                                                    }
                                                                }else{
                                                                    VeteranToast.makeToast(getContext(),"Mission History Error 1 : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            @Override
                                                            public void onFailure(Call<ServerResult> call, Throwable t) {
                                                                //mdialog.dismiss();
                                                                VeteranToast.makeToast(getContext(),"Mission History Error 2 : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                                                                t.printStackTrace();
                                                            }
                                                        });
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                })
                                                .create().show();
                                    }else{
                                        Intent intent = new Intent(getContext(),MissionMainActivity.class);
                                        intent.putExtra(MissionCommon.OBJECT,mission);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                                    }
                                }

                            }
                            Log.i(TAG,"2.결과값을 받았습니다 : " + checkDuplicateFlag);
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<MissionHistory> call, Throwable t) {

                    }
                });

            }
        });
        return view;
    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_MISSION_OPEN){
            //오픈 후 되돌아 온다면 업데이트된 미션 적용

            queryMission = new Mission();
            queryMission.setUid(user.getUid());
            queryMission.setMissionid(mission.getMissionid());

            MissionService missionService = ServiceGenerator.createService(MissionService.class,getContext(),user);
            Call<Mission> call = missionService.getMission(queryMission);
            call.enqueue(new Callback<Mission>() {
                @Override
                public void onResponse(Call<Mission> call, Response<Mission> response) {
                    if(response.isSuccessful()){
                        reflashMission = response.body();
                    }else{
                        VeteranToast.makeToast(getContext(),"mission refrash error 1 " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Mission> call, Throwable t) {
                    VeteranToast.makeToast(getContext(),"mission refrash error 2 : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart() ======================");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume() ======================");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause() ======================");

    }

    public void favoriteTransaction(int uId, int missionId, String typeMethod, final ImageButton imageButton){

        FavoriteMissionService service = ServiceGenerator.createService(FavoriteMissionService.class,getContext(),user);
        FavoriteMission favoriteMission = new FavoriteMission();
        favoriteMission.setUid(uId);
        favoriteMission.setMissionid(missionId);

        if(typeMethod.equals("getCount")){
            Call<ServerResult> callBack = service.getCountFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    //final ProgressDialog dialog;
                    //dialog = ProgressDialog.show(getContext(), "",getString(R.string.network_get_list), true);
                    //dialog.show();

                    if(response.isSuccessful()){
                        //dialog.dismiss();
                        ServerResult result = response.body();
                        favoriteCount = result.getCount();

                        if(favoriteCount != 0){
                            imageButton.setImageResource(R.drawable.star);
                        }else{
                            imageButton.setImageResource(R.drawable.star_enabled);
                        }

                    }else{
                        //dialog.dismiss();
                        VeteranToast.makeToast(getContext(),"Favorite Info Error(1) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //dialog.dismiss();
                    VeteranToast.makeToast(getContext(),"Favorite Info Error(4) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }else if(typeMethod.equals("create")){
            //final ProgressDialog dialog;
            //dialog = ProgressDialog.show(getContext(), "",getString(R.string.network_get_list), true);
            //dialog.show();
            Call<ServerResult> callBack = service.saveFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        //dialog.dismiss();
                        VeteranToast.makeToast(getContext(),"즐겨찾기에 추가했습니다", Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star);
                        favoriteCount = 1;
                    }else{
                        //dialog.dismiss();
                        VeteranToast.makeToast(getContext(),"Favorite Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star_enabled);
                        favoriteCount = 0;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //dialog.dismiss();
                    VeteranToast.makeToast(getContext(),"Favorite Info Error(4) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }else if(typeMethod.equals("delete")){
            //final ProgressDialog dialog;
            //dialog = ProgressDialog.show(getContext(), "",getString(R.string.network_get_list), true);
            //dialog.show();
            Call<ServerResult> callBack = service.deleteFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        //dialog.dismiss();
                        //VeteranToast.makeToast(getContext(),"즐겨찾기에서 제외 했습니다", Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star_enabled);
                        favoriteCount = 0;
                    }else{
                        //dialog.dismiss();
                        //VeteranToast.makeToast(getContext(),"Favorite Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star);
                        favoriteCount = 1;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //dialog.dismiss();
                    //VeteranToast.makeToast(getContext(),"Favorite Info Error(6) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }
    }

    public void openMission(){
        MissionHistoryService service = ServiceGenerator.createService(MissionHistoryService.class,getContext(),user);
        MissionHistory missionHistory = new MissionHistory(user.getUid(),mission.getMissionid(),mission.getEscapepoint(),"each");
        final Call<ServerResult> resultCall = service.saveMissionHistory(missionHistory);
        resultCall.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult serverResult = response.body();
                    VeteranToast.makeToast(getContext(),getString(R.string.mission_open_message),Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(),MissionMainActivity.class);
                    intent.putExtra(MissionCommon.OBJECT,mission);
                    startActivityForResult(intent,REQUEST_MISSION_OPEN);
                    //startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                }else{
                    //mdialog.dismiss();
                    //VeteranToast.makeToast(getContext(),"Mission History Error : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                //mdialog.dismiss();
                VeteranToast.makeToast(getContext(),"Mission History Error : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

}
