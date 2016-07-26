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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.retrofitdao.MissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
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
    private int misstion_id;
    private String missionType;

    private Mission queryMission = new Mission();

    RelativeLayout mMainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        misstion_id = getArguments().getInt(MISSION_ID);
        missionType = getArguments().getString(MISSIONTYPE);
    }

    public static MainMissionFragment newInstance(int page,int missionid,String missionType) {

        MainMissionFragment fragment = new MainMissionFragment();
        //각 프래그먼트에 넘길 함수

        Bundle bdl = new Bundle();

        bdl.putInt(ARG_PAGE,page);
        bdl.putInt(MISSION_ID,missionid);
        bdl.putString(MISSIONTYPE,missionType);
        fragment.setArguments(bdl);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_dummy, container, false);

        queryMission.setMissionid(misstion_id);
        mMainLayout = (RelativeLayout) v.findViewById(R.id.mission_layout);

        final TextView tx_mission_level = (TextView) v.findViewById(R.id.tx_mission_level);
        final TextView tx_mission_name = (TextView) v.findViewById(R.id.tx_mission_name);
        final TextView tx_mission_disp = (TextView) v.findViewById(R.id.tx_mission_description);
        final TextView tx_mission_precon = (TextView) v.findViewById(R.id.tx_mission_precon);
        final TextView tx_mission_point = (TextView) v.findViewById(R.id.tx_mission_point);

        MissionService missionService = ServiceGenerator.createService(MissionService.class);
        Call<Mission> call = missionService.getMission(queryMission);
        call.enqueue(new Callback<Mission>() {
            @Override
            public void onResponse(Call<Mission> call, Response<Mission> response) {
                if(response.isSuccessful()){
                    Mission reqMissionVo = response.body();
                    tx_mission_level.setText("Lv."+reqMissionVo.getSequence());
                    tx_mission_name.setText(reqMissionVo.getMissionname());
                    tx_mission_disp.setText(reqMissionVo.getDescription());
                    tx_mission_precon.setText(reqMissionVo.getPrecon());

                    //미션 포인트 또는 획득점수, 도전 소비 포인트

                }else{

                }
            }

            @Override
            public void onFailure(Call<Mission> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Button startBtn = (Button) v.findViewById(R.id.btn_mission_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VeteranToast.makeToast(getContext(),"미션을 시작합니다", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }




}
