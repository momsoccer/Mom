package com.mom.soccer.mission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mom.soccer.R;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.ExpandableHeightGridView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dataDto.MomMessage;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.retrofitdao.MomComService;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.util.List;

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


    private User user = new User();
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


    ExpandableHeightGridView videoGridView;

    /**************************************************
     * google uplaod define
     **********************/
    private Uri mFileURI = null;

    private static final int RESULT_PICK_IMAGE_CROP = 4;
    private GridMissionAdapter gridMissionAdapter;

    List<UserMission> userMissionList;
    UserMission qUserMission = new UserMission();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mission_main_layout);
        ButterKnife.bind(this);

        Log.d(TAG,"onCreate() =====================================");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        Intent intent = getIntent();
        mission = (Mission) intent.getSerializableExtra(MissionCommon.OBJECT);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Auth.KEY, this);

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

        //LocalBroadcastManager.getInstance(this).registerReceiver(uploadReceiver, new IntentFilter("uploadReceiver"));

        videoGridView = (ExpandableHeightGridView) findViewById(R.id.main_mission_gridview);
        videoGridView.setExpanded(true);

        //조회 조건 <나를 제외한 같은 미션 영상을 업로드한 리스트>
        //qUserMission.setMissionid(mission.getMissionid());
        qUserMission.setUid(user.getUid());

        userGrid_MissionList(qUserMission);

        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent userMissionListIntent = new Intent(getApplicationContext(),UserMissionActivity.class);
                userMissionListIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMissionList.get(position));
                startActivity(userMissionListIntent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"onResume() =====================================");

    }

    /*
    BroadcastReceiver uploadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String uploadMessage = intent.getStringExtra("uploadMessage");
            String upflag = intent.getStringExtra("upflag");

        }
    };
    */

    public void userGrid_MissionList(UserMission userMission){
        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,getApplicationContext(),user);
        Call<List<UserMission>> call = userMissionService.uMissionList(userMission);

        call.enqueue(new Callback<List<UserMission>>() {
            @Override
            public void onResponse(Call<List<UserMission>> call, Response<List<UserMission>> response) {

                if(response.isSuccessful()){
                    userMissionList = response.body();
                    gridMissionAdapter = new GridMissionAdapter(getApplicationContext(),R.layout.adapter_user_mission_grid_item,userMissionList);
                    videoGridView.setAdapter(gridMissionAdapter);
                    dialog.dismiss();
                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<List<UserMission>> call, Throwable t) {
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }


}
