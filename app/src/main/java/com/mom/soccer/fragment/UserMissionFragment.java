package com.mom.soccer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.mom.soccer.R;
import com.mom.soccer.adapter.BoardItemAdapter;
import com.mom.soccer.dto.Board;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.retrofitdao.BoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-16.
 */
public class UserMissionFragment extends Fragment {

    private static final String TAG = "UserMissionFragment";
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private User user;
    private UserMission userMission;

    private BoardItemAdapter boardItemAdapter;
    private RecyclerView recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = null;

        if(mPage==1){

            view =  inflater.inflate(R.layout.fr_user_mission_layout1, container, false);



        }else if(mPage==2){
            view = inflater.inflate(R.layout.fr_user_mission_layout2, container, false);

            recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

            WaitingDialog.showWaitingDialog(getActivity(),false);
            BoardService boardService = ServiceGenerator.createService(BoardService.class,getActivity(),user);

            Board board = new Board();
            board.setUsermissionid(userMission.getUsermissionid());
            board.setUid(userMission.getUid());

            Call<List<Board>> call = boardService.getboardlist(board);
            call.enqueue(new Callback<List<Board>>() {
                @Override
                public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        List<Board> boardList;
                        boardList = response.body();
                        Log.i(TAG,"Board : " + boardList.size());

                        boardItemAdapter = new BoardItemAdapter(getActivity(),boardList,user);
                        recyclerview.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                        recyclerview.getItemAnimator().setAddDuration(300);
                        recyclerview.getItemAnimator().setRemoveDuration(300);
                        recyclerview.getItemAnimator().setMoveDuration(300);
                        recyclerview.getItemAnimator().setChangeDuration(300);
                        recyclerview.setHasFixedSize(true);
                        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(boardItemAdapter);
                        alphaAdapter.setDuration(500);
                        recyclerview.setAdapter(alphaAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Board>> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });

        }

        return view;
    }


    public static UserMissionFragment newInstance(int page, User user, UserMission mission){

        Log.i(TAG,"FollowerFragment : ==========================");

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(MissionCommon.USER_OBJECT,user);
        args.putSerializable(MissionCommon.USER_MISSTION_OBJECT,mission);
        UserMissionFragment fragment = new UserMissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        user = (User) getArguments().getSerializable(MissionCommon.USER_OBJECT);
        userMission = (UserMission) getArguments().getSerializable(MissionCommon.USER_MISSTION_OBJECT);
    }

}
