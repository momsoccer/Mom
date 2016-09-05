package com.mom.soccer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.mom.soccer.R;
import com.mom.soccer.adapter.MissionFragmentAdapter;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.User;
import com.mom.soccer.widget.VeteranToast;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungbo on 2016-08-27.
 */
public class MissionChallengeFragment extends Fragment {

    private static final String TAG = "MissionChallenge";

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    private User user;
    private List<Mission> missionList;
    private List<Fragment> mViewPagerFragments;

    private int NUMBER_OF_FRAGMENTS;

    private FlippableStackView mFlippableStack;
    private MissionFragmentAdapter mPageAdapter;


    //private List<Fragment> mViewPagerFragments;


    public static MissionChallengeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MissionChallengeFragment fragment = new MissionChallengeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        Log.i(TAG,"page onCreateView: " + mPage);

        View view = inflater.inflate(R.layout.challengefragment_layout0,container,false);

        if(mPage==1){
            VeteranToast.makeToast(getContext(),"1값은 : " + mPage, Toast.LENGTH_SHORT).show();
        }else if(mPage==2){
            VeteranToast.makeToast(getContext(),"2값은 : " + mPage, Toast.LENGTH_SHORT).show();
        }else if(mPage==3){
            VeteranToast.makeToast(getContext(),"3값은 : " + mPage, Toast.LENGTH_SHORT).show();
        }
        return  view;
    }


    private void createViewPagerFragments() {
        Log.d(TAG,"이곳은 createViewPagerFragments()");
        mViewPagerFragments = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_FRAGMENTS; ++i) {
            mViewPagerFragments.add(MainMissionFragment.newInstance(i, missionList.get(i),user,"NOL"));
        }
    }




}
