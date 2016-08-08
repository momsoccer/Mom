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

import android.app.ProgressDialog;
import android.content.Context;
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
import com.mom.soccer.dto.FavoriteMission;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.MissionMainActivity;
import com.mom.soccer.retrofitdao.FavoriteMissionService;
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

    private Mission mission = new Mission();

    LinearLayout mMainLayout;

    int favoriteCount = 0;
    User user;

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
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(TAG,"onCreateView() ======================");

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
        final TextView tx_mission_point = (TextView) view.findViewById(R.id.tx_mission_point);

        tx_mission_level.setText("Lv."+mission.getSequence());
        tx_mission_name.setText(mission.getMissionname());
        tx_mission_disp.setText(mission.getDescription());
        tx_mission_precon.setText(mission.getPrecon());

        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.gansim_star);
        favoriteTransaction(user.getUid(),mission.getMissionid(),"getCount",imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VeteranToast.makeToast(getContext(),"즐겨찾기 : " + mission.getMissionid(), Toast.LENGTH_SHORT).show();

                if(favoriteCount==0){
                    favoriteTransaction(user.getUid(),mission.getMissionid(),"create",imageButton);
                    favoriteCount=1;
                }else{
                    favoriteTransaction(user.getUid(),mission.getMissionid(),"delete",imageButton);
                    favoriteCount=0;
                }

            }
        });

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

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(getContext(), "",getString(R.string.network_get_list), true);
        dialog.show();

        if(typeMethod.equals("getCount")){
            Call<ServerResult> callBack = service.getCountFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        dialog.dismiss();
                        ServerResult result = response.body();
                        favoriteCount = result.getCount();

                        if(favoriteCount != 0){
                            imageButton.setImageResource(R.drawable.star);
                        }else{
                            imageButton.setImageResource(R.drawable.star_enabled);
                        }

                    }else{
                        dialog.dismiss();
                        VeteranToast.makeToast(getContext(),"Favorite Info Error(1) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    dialog.dismiss();
                    VeteranToast.makeToast(getContext(),"Favorite Info Error(2) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }else if(typeMethod.equals("create")){

            Call<ServerResult> callBack = service.saveFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        dialog.dismiss();
                        VeteranToast.makeToast(getContext(),"즐겨찾기에 추가했습니다", Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star);
                        favoriteCount = 1;
                    }else{
                        dialog.dismiss();
                        VeteranToast.makeToast(getContext(),"Favorite Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star_enabled);
                        favoriteCount = 0;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    dialog.dismiss();
                    VeteranToast.makeToast(getContext(),"Favorite Info Error(4) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }else if(typeMethod.equals("delete")){

            Call<ServerResult> callBack = service.deleteFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        dialog.dismiss();
                        VeteranToast.makeToast(getContext(),"즐겨찾기에서 제외 했습니다", Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star_enabled);
                        favoriteCount = 0;
                    }else{
                        dialog.dismiss();
                        VeteranToast.makeToast(getContext(),"Favorite Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star);
                        favoriteCount = 1;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    dialog.dismiss();
                    VeteranToast.makeToast(getContext(),"Favorite Info Error(6) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }
    }
}
