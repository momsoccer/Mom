package com.mom.soccer.youtubeplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;

import butterknife.ButterKnife;

public class YoutubePlayerActivity extends YouTubeFailureRecoveryActivity implements
        YouTubePlayer.OnFullscreenListener {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    private LinearLayout baseLayout;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private Intent intent;
    private String youtubeVideoAddr;

    int controlFlags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_youtubeplayer_layout);
        ButterKnife.bind(this);

        intent = getIntent();
        youtubeVideoAddr = intent.getExtras().getString(Common.YOUTUBEVIDEO);

        playerView = (YouTubePlayerView) findViewById(R.id.player);
        playerView.initialize(Auth.KEY, this);

        //풀스크린
        baseLayout = (LinearLayout) findViewById(R.id.layout);
        LinearLayout.LayoutParams playerParams = (LinearLayout.LayoutParams) playerView.getLayoutParams();
        playerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        playerParams.height = LinearLayout.LayoutParams.MATCH_PARENT;


    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.player = youTubePlayer;

        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        player.setOnFullscreenListener(this);
        if (!wasRestored) {
            player.cueVideo(youtubeVideoAddr);
        }
    }


    @Override
    public void onFullscreen(boolean b) {

    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
