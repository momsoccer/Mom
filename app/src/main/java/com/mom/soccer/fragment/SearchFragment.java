package com.mom.soccer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mom.soccer.R;
import com.mom.soccer.adapter.GridSearchAdapter;
import com.mom.soccer.dto.Instructor;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.InstructorService;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

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

    private RecyclerView recview;
    private GridSearchAdapter gridSearchAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    User user;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fr_search_layout, container, false);

        recview = (RecyclerView) rootview.findViewById(R.id.recview);

        btn_search = (Button) rootview.findViewById(R.id.btn_search);
        search_word = (EditText) rootview.findViewById(R.id.search_word);

        if (mPage == 1) {
            if(mFlag.equals("NO")){
                getFindInsList("NO",user.getUid());
            }
        }else{

            if(mFlag.equals("NO")){
                getUserList("NO",user.getUid());
            }

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
                        getUserList("NO",user.getUid());
                    }else{
                        getUserList(search_word.getText().toString(),user.getUid());
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


    public void getUserList(String searchWord,int uid){

        WaitingDialog.showWaitingDialog(getContext(),false);
        UserService userService = ServiceGenerator.createService(UserService.class,getContext(),user);
        User user = new User();

        if(!searchWord.equals("NO")){
            user.setUsername(searchWord);
        }

        user.setUid(uid);

        Call<List<User>> call = userService.getUserSearList(user);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    List<User> userList = response.body();
                    WaitingDialog.cancelWaitingDialog();

                    if(userList.size()==0){
                        Log.i(TAG,"유저가 없습니다");
                    }else{

                        gridSearchAdapter = new GridSearchAdapter(getActivity(),userList);

                        recview.setHasFixedSize(true);
                        mLayoutManager = new GridLayoutManager(getContext(),2);
                        recview.setLayoutManager(mLayoutManager);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(gridSearchAdapter);
                        alphaAdapter.setDuration(500);
                        recview.setAdapter(gridSearchAdapter);
                    }
                }
                search_word.setText(null);
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
