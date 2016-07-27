package com.mom.soccer.mission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.MomMessage;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.MomComService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionMainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private static final String TAG = "MissionMainActivity";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private int seedMissionId = 0;


    private User user;
    private PrefUtil prefUtil;
    private YouTubePlayerView youTubeView;

    private Mission mission = new Mission();

    private Boolean setNet = false;

    @Bind(R.id.main_mission_rlayout)
    RelativeLayout relativeLayout;

    @Bind(R.id.main_mission_start)
    ImageButton btnStart;

    @Bind(R.id.tx_main_mission_level)
    TextView tx_level;

    @Bind(R.id.tx_main_mission_name)
    TextView tx_missionName;

    @Bind(R.id.tx_main_mission_disp)
    TextView tx_missionDisp;

    @Bind(R.id.tx_main_mission_precon)
    TextView tx_missionPreCondition;

    @Bind(R.id.tx_main_mission_potin)
    TextView tx_missionPoint;

    @Bind(R.id.main_mission_gridview)
    GridView videoGridView;

    /**************************************************
     * google uplaod define
     **********************/
    private Uri mFileURI = null;

    private static final int RESULT_PICK_IMAGE_CROP = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mission_main_layout);
        ButterKnife.bind(this);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Intent intent = getIntent();
        mission = (Mission) intent.getSerializableExtra(MissionCommon.OBJECT);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Auth.KEY, this);

        Log.d(TAG,"미션 객체의 값은 : " + mission.toString());

        if (mission.getYoutubeaddr()==null){
            setNet = true;
        }

        //미션 타입에 따라 백그라운드를 다르게 해준다
        if(mission.getTypename().equals("DRIBLE")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.drible_back));
        }else if(mission.getTypename().equals("LIFTING")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.lifting_back));
        }else if(mission.getTypename().equals("TRAPING")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.trappring_back));
        }else if(mission.getTypename().equals("AROUND")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.around_back));
        }else if(mission.getTypename().equals("FLICK")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.flick_back));
        }else if(mission.getTypename().equals("COMPLEX")){
            relativeLayout.setBackground(getResources().getDrawable(R.drawable.complex_back));
        }

        tx_level.setText(String.valueOf("Lv." + mission.getSequence()));
        tx_missionName.setText(mission.getMissionname());
        tx_missionDisp.setText(mission.getDescription());
        tx_missionPreCondition.setText(mission.getPrecon());

        String advance = getString(R.string.point_upload)+" "+mission.getGrade()+getString(R.string.point_get)+", "+
                getString(R.string.point_pre_word)+" "+mission.getPassgrade()+getString(R.string.point_get);

        tx_missionPoint.setText(advance);

        LocalBroadcastManager.getInstance(this).registerReceiver(uploadReceiver, new IntentFilter("uploadReceiver"));

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean wasRestored) {

        if (!wasRestored) {
            if(!setNet){
                youTubePlayer.cueVideo(mission.getYoutubeaddr());
            }else{
                //유투브 주소가 없다면 문제. 이럴때는 몸싸커 메인 인트로를 보여준다

                MomComService service = ServiceGenerator.createService(MomComService.class,this,user);
                Call<MomMessage> call = service.getCommonInfo("VIDEOERROR");
                call.enqueue(new Callback<MomMessage>() {
                    @Override
                    public void onResponse(Call<MomMessage> call, Response<MomMessage> response) {
                        if(response.isSuccessful()){
                            MomMessage momMessage = response.body();
                            youTubePlayer.cueVideo(momMessage.getAttribute1());
                        }else{
                            youTubePlayer.cueVideo("3v1SRgbGsV8");
                        }
                    }

                    @Override
                    public void onFailure(Call<MomMessage> call, Throwable t) {
                        VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1), Toast.LENGTH_LONG).show();
                        youTubePlayer.cueVideo("3v1SRgbGsV8");
                    }
                });
            }
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    @OnClick(R.id.btn_mission_upload)
    public void missionVideoUpload(){
        //업로드
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, RESULT_PICK_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_PICK_IMAGE_CROP:
                if (resultCode == RESULT_OK) {
                    mFileURI = data.getData();
                    if (mFileURI != null) {
                        Intent intent = new Intent(this, ReviewActivity.class);
                        intent.setData(mFileURI);
                        intent.putExtra(MissionCommon.OBJECT,mission);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }
                }
                break;

        }
    }


    private String getName(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    BroadcastReceiver uploadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String uploadMessage = intent.getStringExtra("uploadMessage");
            String upflag = intent.getStringExtra("upflag");

            Log.d(TAG,"uploadMessage : "+uploadMessage);
            Log.d(TAG,"upflag : " +upflag);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }


}
