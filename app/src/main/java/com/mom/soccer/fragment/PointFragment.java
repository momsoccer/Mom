package com.mom.soccer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mom.soccer.R;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;

/**
 * Created by sungbo on 2016-08-17.
 */
public class PointFragment extends Fragment {

    private static final String TAG = "PlayerFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private User user = new User();

    LinearLayout li_layout_page1,li_layout_page2;

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
        }

        return view;
    }
}
