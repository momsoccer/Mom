package com.mom.soccer.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mom.soccer.R;
import com.mom.soccer.adapter.GridSearchAdapter;
import com.mom.soccer.common.MomSnakBar;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-02.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    private Button btn_search;
    private EditText search_word;

    private String mFlag = "NO";

    private  List<User> userList = new ArrayList<User>();

    private RecyclerView recview;
    private GridSearchAdapter gridSearchAdapter;

    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    User user;

    private int page= 0;
    private int offset = 0;
    private int limit = 40;
    private boolean lastData = false;

    private SwipeRefreshLayout swipeRefreshLayout;

    public static SearchFragment newInstance(int page,User user) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootview;

        if(mPage==1){
            rootview =  inflater.inflate(R.layout.fr_search_layout, container, false);
            recview = (RecyclerView) rootview.findViewById(R.id.recview);
            btn_search = (Button) rootview.findViewById(R.id.btn_search);
            search_word = (EditText) rootview.findViewById(R.id.search_word);

            if(mFlag.equals("NO")){
                getFindInsList("NO",user.getUid());
            }

        }else{
            rootview = inflater.inflate(R.layout.fr_search_user_layout, container, false);
            swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipeRefreshLayout);
            recview = (RecyclerView) rootview.findViewById(R.id.recview);
            btn_search = (Button) rootview.findViewById(R.id.btn_search);
            search_word = (EditText) rootview.findViewById(R.id.search_word);

            if(mFlag.equals("NO")){
                getUserList("NO",user.getUid(),"first");
            }

            linearLayoutManager = new LinearLayoutManager(getContext());
            //mLayoutManager = new GridLayoutManager(getContext(),2);

            if(Build.VERSION.SDK_INT  >= 20) {
                recview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
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
                recview.setOnScrollListener(new RecyclerView.OnScrollListener(){
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

            final View finalView1 = rootview;

            //3스와프 이벤트 페이징
            recview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        if (linearLayoutManager.findLastVisibleItemPosition() == userList.size()-1) {

                            if(!lastData){


                                if(search_word.getText().toString()==null){
                                    getUserList("NO",user.getUid(),"next");
                                }else{
                                    getUserList(search_word.getText().toString(),user.getUid(),"next");
                                }


                            }else{
                                MomSnakBar.show(finalView1,getActivity(),getActivity().getResources().getString(R.string.bottom_msg1));
                            }
                        }
                    }
                }
            });


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if(search_word.getText().toString()==null){
                        getUserList("NO",user.getUid(),"first");
                    }else{
                        getUserList(search_word.getText().toString(),user.getUid(),"first");
                    }

                    MomSnakBar.show(finalView1,getActivity(),getString(R.string.bottom_msg4));
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }



        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPage == 1) {
                    if(search_word.getText().toString()==null){
                        getFindInsList("NO",user.getUid());
                    }else{
                        getFindInsList(search_word.getText().toString(),user.getUid());
                    }

                }else{
                    if(search_word.getText().toString()==null){
                        getUserList("NO",user.getUid(),"first");
                    }else{
                        getUserList(search_word.getText().toString(),user.getUid(),"first");
                    }
                }
            }
        });



        return rootview;
    }

    public void getFindInsList(final String searchWord, int uid){

        WaitingDialog.showWaitingDialog(getContext(),false);
        InstructorService service = ServiceGenerator.createService(InstructorService.class,getContext(),user);

        final Instructor queryIns = new Instructor();

        if(!searchWord.equals("NO")){
            queryIns.setName(searchWord);
        }

        queryIns.setUid(uid);

        Call<List<Instructor>> c = service.getCoachSearchList(queryIns);
        c.enqueue(new Callback<List<Instructor>>() {
            @Override
            public void onResponse(Call<List<Instructor>> call, Response<List<Instructor>> response) {
                if(response.isSuccessful()){

                    List<Instructor> instructorList = response.body();

                    WaitingDialog.cancelWaitingDialog();

                    if(instructorList.size()==0){

                    }else{
                        gridSearchAdapter = new GridSearchAdapter(getActivity(),instructorList,"ins");
                        recview.setHasFixedSize(true);
                        mLayoutManager = new GridLayoutManager(getContext(),2);
                        recview.setLayoutManager(mLayoutManager);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(gridSearchAdapter);
                        alphaAdapter.setDuration(500);
                        recview.setAdapter(gridSearchAdapter);
                    }

                    search_word.setText(null);
                }
            }

            @Override
            public void onFailure(Call<List<Instructor>> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();
            }
        });
    }


    public void getUserList(String searchWord, int uid, final String pageFlag){

        Log.i(TAG,"유저 쿼리를 날립니다");

        WaitingDialog.showWaitingDialog(getContext(),false);
        UserService userService = ServiceGenerator.createService(UserService.class,getContext(),user);

        User user = new User();

        if(pageFlag.equals("first")) {
            offset = 0;
        }else{
            page = page + 1;
            offset = limit * page;
        }
        user.setOffset(offset);
        user.setLimit(limit);



        if(!searchWord.equals("NO")){
            user.setUsername(searchWord);
        }

        user.setUid(uid);

        Call<List<User>> call = userService.getUserSearList(user);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    if(pageFlag.equals("first")) {
                        userList = response.body();
                        WaitingDialog.cancelWaitingDialog();
                        gridSearchAdapter = new GridSearchAdapter(getActivity(),userList);
                        linearLayoutManager = new GridLayoutManager(getContext(),2);
                        recview.setHasFixedSize(true);
                        recview.setLayoutManager(linearLayoutManager);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(gridSearchAdapter);
                        alphaAdapter.setDuration(200);
                        recview.setAdapter(gridSearchAdapter);

                        //페이징 초기화
                        lastData = false;
                        offset  = 0;
                        page    = 0;

                    }else{

                        List<User> addUsers = response.body();

                        for(int i = 0 ; i < addUsers.size() ; i++)
                        {
                            userList.add(addUsers.get(i));
                        }

                        gridSearchAdapter.notifyDataSetChanged();

                        if(addUsers.size() != limit){
                            lastData = true;
                            addUsers = new ArrayList<User>();
                        }
                        WaitingDialog.cancelWaitingDialog();
                    }

                }

                //search_word.setText(null);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                WaitingDialog.cancelWaitingDialog();
                t.printStackTrace();

            }
        });
    }

}
