package com.mom.soccer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mom.soccer.R;
import com.mom.soccer.dto.User;

/**
 * Created by sungbo on 2016-08-16.
 */
public class UserMissionFragment extends Fragment {

    private static final String TAG = "UserMissionFragment";
    public static final String ARG_PAGE = "ARG_PAGE";

    //parameter
    private int mPage;
    private User user = new User();

    public static UserMissionFragment newInstance(int page){

        Log.i(TAG,"FollowerFragment : ==========================");

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UserMissionFragment fragment = new UserMissionFragment();
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
        View view = inflater.inflate(R.layout.fr_user_mission_layout, container, false);

        if(mPage==1){
        }else if(mPage==2){
        }

        return view;
    }

}
