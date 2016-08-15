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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.MissionMainActivity;

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

    private Mission mission = new Mission();

    LinearLayout mMainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPage = getArguments().getInt(ARG_PAGE);
        mission = (Mission) getArguments().getSerializable(MissionCommon.OBJECT);

        Log.d(TAG,"미션 객체 값 : " + mission.toString());
    }

    public static MainMissionFragment newInstance(int page,
                                                  Mission mission) {

        MainMissionFragment fragment = new MainMissionFragment();
        //각 프래그먼트에 넘길 함수

        Bundle bdl = new Bundle();

        bdl.putInt(ARG_PAGE,page);
        //mission 객체에 담기
        bdl.putSerializable(MissionCommon.OBJECT,mission);
        fragment.setArguments(bdl);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;
        view = inflater.inflate(R.layout.fr_mission_layout, container, false);
        mMainLayout = (LinearLayout) view.findViewById(R.id.li_mission_back);

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
        final TextView tx_mission_point = (TextView) view.findViewById(R.id.tx_mission_point);

        tx_mission_level.setText("Lv."+mission.getSequence());
        tx_mission_name.setText(mission.getMissionname());
        tx_mission_disp.setText(mission.getDescription());
        tx_mission_precon.setText(mission.getPrecon());


        final Button missionStartBtn = (Button) view.findViewById(R.id.btn_mission_start);
        missionStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MissionMainActivity.class);

                intent.putExtra(MissionCommon.OBJECT,mission);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        return view;
    }




}
