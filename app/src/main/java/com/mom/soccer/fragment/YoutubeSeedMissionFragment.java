package com.mom.soccer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;

/**
 * Created by sungbo on 2016-08-10.
 */
public class YoutubeSeedMissionFragment extends Fragment {

    private static Context mContext;
    private static  String youtubeVideoAddr;

    public YoutubeSeedMissionFragment(){}

    @SuppressLint("ValidFragment")
    public YoutubeSeedMissionFragment(Context context,String youtubeVideoAddr) {
        mContext = context;
        this.youtubeVideoAddr = youtubeVideoAddr;
    }

    public static YoutubeSeedMissionFragment newInstance(int page) {
        YoutubeSeedMissionFragment fragment = new YoutubeSeedMissionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.youtube_fragment_main_item, container, false);


        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_player_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(Auth.KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    //youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.cueVideo(youtubeVideoAddr);
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL); //landscape do not change
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
            }



        });



        return rootview;
    }


}
