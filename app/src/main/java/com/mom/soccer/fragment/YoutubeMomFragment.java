package com.mom.soccer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;

/**
 * Created by sungbo on 2016-08-10.
 */
public class YoutubeMomFragment extends Fragment implements YouTubePlayer.OnInitializedListener ,YouTubePlayer.OnFullscreenListener{

    private static final String TAG = "YoutubeSeed";

    private static Context mContext;
    private static  String youtubeVideoAddr;

    public YoutubeMomFragment(){}
    private YouTubePlayer player;
    private boolean fullscreen;
    YouTubePlayerSupportFragment youTubePlayerFragment;

    LinearLayout youtube_player_layout;

    @SuppressLint("ValidFragment")
    public YoutubeMomFragment(Context context, String youtubeVideoAddr) {
        mContext = context;
        this.youtubeVideoAddr = youtubeVideoAddr;
    }

    public static YoutubeMomFragment newInstance(int page) {
        YoutubeMomFragment fragment = new YoutubeMomFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.youtube_mom_fragment_main_item, container, false);

        youtube_player_layout = (LinearLayout) rootview.findViewById(R.id.youtube_player_layout);
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_player_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(Auth.KEY,this);

        return rootview;
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.player = youTubePlayer;
        player.setOnFullscreenListener(this);
        if (!wasRestored) {
            youTubePlayer.cueVideo(youtubeVideoAddr);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    }


    @Override
    public void onFullscreen(boolean isFullscreen) {
        Log.i(TAG,"onFullscreen ************************************************");
        fullscreen = isFullscreen;

            if(fullscreen){
                Common.YOUTUBESCREEN_STATUS ="LANDSCAPE";
            }else{
                Common.YOUTUBESCREEN_STATUS ="PORTRAIT";
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy ************************************************");


    }
}
