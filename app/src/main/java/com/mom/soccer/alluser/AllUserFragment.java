package com.mom.soccer.alluser;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mom.soccer.R;
import com.mom.soccer.common.EventBus;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;

/**
 * Created by sungbo on 2016-09-18.
 */
public class AllUserFragment extends Fragment {

    private static final String TAG = "FollowerFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    private SwipeRefreshLayout swipeRefreshLayout;

    private int mPage;

    private User user = new User();
    private Instructor instructor= new Instructor();

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView boardRcView;

    //paing function
    private int page= 0;
    private int offset = 0;
    private int limit = 40;
    private boolean lastData = false;

    //query
    private boolean queryExecute = false;


    //view
    LinearLayout li_no_found;

    public static AllUserFragment newInstance(int page, User user){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        AllUserFragment fragment = new AllUserFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getInstance().register(this);
        View view = null;

        linearLayoutManager = new LinearLayoutManager(getContext());

        if(mPage==1){
            view = inflater.inflate(R.layout.all_user_fragment1, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            boardRcView = (RecyclerView) view.findViewById(R.id.boardRcView);
            li_no_found = (LinearLayout) view.findViewById(R.id.li_no_found);

            final View finalView1 = view;

            //2.스와이프 이벤트 버전별
            if(Build.VERSION.SDK_INT  >= 20) {
                boardRcView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }else{
                boardRcView.setOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //query
                    // getSearchUserMission(query,"new");

                    MomSnakBar.show(finalView1,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });


        }else if(mPage==2){
            view = inflater.inflate(R.layout.all_user_fragment2, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            boardRcView = (RecyclerView) view.findViewById(R.id.boardRcView);
            li_no_found = (LinearLayout) view.findViewById(R.id.li_no_found);

            final View finalView1 = view;

            //2.스와이프 이벤트 버전별
            if(Build.VERSION.SDK_INT  >= 20) {
                boardRcView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }else{
                boardRcView.setOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //query
                    // getSearchUserMission(query,"new");

                    MomSnakBar.show(finalView1,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }else if(mPage==3) {
            view = inflater.inflate(R.layout.all_user_fragment3, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            boardRcView = (RecyclerView) view.findViewById(R.id.boardRcView);
            li_no_found = (LinearLayout) view.findViewById(R.id.li_no_found);

            final View finalView1 = view;

            //2.스와이프 이벤트 버전별
            if(Build.VERSION.SDK_INT  >= 20) {
                boardRcView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }else{
                boardRcView.setOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                });
            }

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //query
                    // getSearchUserMission(query,"new");

                    MomSnakBar.show(finalView1,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getInstance().unregister(this);
    }
}
