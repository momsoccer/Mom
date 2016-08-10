package com.mom.soccer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.adapter.GridSearchAdapter;
import com.mom.soccer.dto.User;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.UserService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.util.List;

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
    private GridSearchAdapter gridSearchAdapter;
    private GridView search_grid_view;

    private Button search_button;
    private String mFlag = "NO";

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

        Log.i(TAG,"user value : " + user.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootview = null;

        rootview = inflater.inflate(R.layout.fr_search_layout, container, false);

        search_grid_view = (GridView) rootview.findViewById(R.id.search_grid_view);
        search_button = (Button) getActivity().findViewById(R.id.btn_search);

        final EditText search_word = (EditText) rootview.findViewById(R.id.search_word);
        Button button = (Button) rootview.findViewById(R.id.btn_search);

        if (mPage == 1) {
            if(mFlag.equals("NO")){
                getUserList("NO",user.getUid());
            }
            search_word.setHint(getString(R.string.search_user_field));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUserList(search_word.getText().toString(),user.getUid());
                    mFlag="Y";
                }
            });
        }else{

            if(mFlag.equals("NO")){
                getUserList("NO",user.getUid());
            }
            search_word.setHint(getString(R.string.search_coarch_field));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUserList(search_word.getText().toString(),user.getUid());
                    mFlag="Y";
                }
            });

        }
        return rootview;
    }


    public void getUserList(String searchWord,int uid){

        //final ProgressDialog dialog;
        //dialog = ProgressDialog.show(getContext(), "",getString(R.string.network_get_user), true);
        //dialog.show();

        UserService userService = ServiceGenerator.createService(UserService.class,getContext(),user);
        User user = new User();

        if(!searchWord.equals("NO")){
            user.setUsername(searchWord);
        }

        user.setUid(uid); //자신은 빼고 조회한다

        Call<List<User>> call = userService.getUserSearList(user);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    List<User> userList = response.body();
                    gridSearchAdapter = new GridSearchAdapter(getActivity(),userList,R.layout.ac_search_grid_item_layout);
                    search_grid_view.setAdapter(gridSearchAdapter);
                    //dialog.dismiss();
                    if(userList.size()==0){
                        VeteranToast.makeToast(getContext(),getString(R.string.network_nodata_found_user), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    //VeteranToast.makeToast(getContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_LONG).show();
                    //dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "환경 구성 확인 필요 서버와 통신 불가" + t.getMessage());
                //VeteranToast.makeToast(getContext(),getString(R.string.network_error_message1),Toast.LENGTH_LONG).show();
                t.printStackTrace();
                //dialog.dismiss();
            }
        });
    }

}
