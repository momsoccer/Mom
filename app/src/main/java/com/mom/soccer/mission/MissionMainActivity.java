package com.mom.soccer.mission;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.ExpandableHeightGridView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.FavoriteMission;
import com.mom.soccer.dto.Mission;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.fragment.YoutubeSeedMissionFragment;
import com.mom.soccer.retrofitdao.FavoriteMissionService;
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

public class MissionMainActivity extends AppCompatActivity {

    private static final String TAG = "MissionMainActivity";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private int seedMissionId = 0;

    private User user = new User();
    private PrefUtil prefUtil;
    private YouTubeThumbnailView thumbnailView;
    int favoriteCount = 0;

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

    @Bind(R.id.img_missiontab)
    ImageView img_missiontab;

    @Bind(R.id.li_mymittion)
    LinearLayout linearLayout;

    @Bind(R.id.tx_main_usermission)
    TextView tx_main_usermission;

    @Bind(R.id.view_l1)
    View view_l1;
    @Bind(R.id.view_l2)
    View view_l2;

    @Bind(R.id.usermission_iv_hart)
    ImageView usermission_iv_hart;

    @Bind(R.id.usermission_tx_hart)
    TextView usermission_tx_hart;

    @Bind(R.id.usermission_iv_comment)
    ImageView usermission_iv_comment;

    @Bind(R.id.usermission_tx_comment)
    TextView usermission_tx_comment;

    @Bind(R.id.btn_mission_upload)
    Button btn_mission_upload;

    @Bind(R.id.im_clear_marck)
    ImageView im_clear_marck;

    ExpandableHeightGridView videoGridView;
    UserMission userMission;
    /**************************************************
     * google uplaod define
     **********************/
    private Uri mFileURI = null;

    private static final int RESULT_PICK_IMAGE_CROP = 4;
    private GridMissionAdapter gridMissionAdapter;

    List<UserMission> userMissionList;
    UserMission qUserMission = new UserMission();
    Intent intent;
    private static int UPLOAD_NOTIFICATION_ID = 1001;
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mission_main_layout);
        ButterKnife.bind(this);

        Log.d(TAG,"onCreate() =====================================");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        intent = getIntent();
        mission = (Mission) intent.getSerializableExtra(MissionCommon.OBJECT);


        thumbnailView = (YouTubeThumbnailView) findViewById(R.id.youtybe_Thumbnail);

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


        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent userMissionListIntent = new Intent(getApplicationContext(),UserMissionActivity.class);
                userMissionListIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMissionList.get(position));
                startActivity(userMissionListIntent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = new Intent(getApplicationContext(),UserMissionActivity.class);
                miIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMission);
                startActivity(miIntent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });


        favoriteTransaction(user.getUid(),mission.getMissionid(),"getCount",btnStart);
        //youTubeView.initialize(Auth.KEY,MissionMainActivity.this);

        //업로드후 노티를 지워준다
        NotificationManager nm =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(UPLOAD_NOTIFICATION_ID);
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
        Log.i(TAG,"onResume() =====================================");
        intent = getIntent();
        String uploadflag = intent.getStringExtra("uploadflag");
        Log.i(TAG,"받은 값은 : " + uploadflag);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() =====================================");

        //나의 미션 영상
        getMyVideo();
        //조회 조건 <나를 제외한 같은 미션 영상을 업로드한 리스트>
        qUserMission.setMissionid(mission.getMissionid());
        qUserMission.setUid(user.getUid());
        userGrid_MissionList(qUserMission);

        //YoutubeFragment 유투브 플래그먼트
        YoutubeSeedMissionFragment youtubeFragment = new YoutubeSeedMissionFragment(this,mission.getYoutubeaddr());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.youtube_seed_frame_layout,youtubeFragment,"");
        tc.commit();

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

    //다른 사람들의 영상 목록
    public void userGrid_MissionList(UserMission userMission){
        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);

        userMission.setQueryRow(20);

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,getApplicationContext(),user);
        Call<List<UserMission>> call = userMissionService.uMissionList(userMission);

        call.enqueue(new Callback<List<UserMission>>() {
            @Override
            public void onResponse(Call<List<UserMission>> call, Response<List<UserMission>> response) {

                if(response.isSuccessful()){
                    userMissionList = response.body();
                    gridMissionAdapter = new GridMissionAdapter(getApplicationContext(),R.layout.adapter_user_mission_grid_item,userMissionList,"YOU");
                    videoGridView.setAdapter(gridMissionAdapter);
                    dialog.dismiss();
                }else{
                    VeteranToast.makeToast(getApplicationContext(),"User Video List : "+getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
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

    //내가 수행한 미션
    public void getMyVideo(){
        UserMission u = new UserMission();
        u.setMissionid(mission.getMissionid());
        u.setUid(user.getUid());
        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,this,user);
        Call<UserMission> c = userMissionService.getUserMission(u);

        c.enqueue(new Callback<UserMission>() {
            @Override
            public void onResponse(Call<UserMission> call, Response<UserMission> response) {
                if(response.isSuccessful()){

                    userMission = response.body();

                    Log.i(TAG," 받아온 값은  : " + userMission.toString() );
                    //좋아요 카운트
                    usermission_tx_hart.setText(String.valueOf(userMission.getBookmarkcount()));
                    usermission_tx_comment.setText(String.valueOf(userMission.getBoardcount()));

                    if(userMission.getUid()==0){
                        img_missiontab.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                    }else{

                        Log.i(TAG," Test View Start ######" + userMission.toString());

                        if(userMission.getPassflag().equals("Y")){
                            Log.i(TAG,"===1" + userMission.getPassflag());
                            img_missiontab.setVisibility(View.VISIBLE);
                            view_l1.setVisibility(View.GONE);
                            view_l2.setVisibility(View.GONE);
                            im_clear_marck.setVisibility(View.VISIBLE);
                        }else if(userMission.getPassflag().equals("P")){

                            Log.i(TAG,"===2" + userMission.getPassflag());
                            img_missiontab.setVisibility(View.GONE);
                            tx_main_usermission.setText(R.string.user_mission_p);
                            btn_mission_upload.setText(R.string.user_mission_p);

                            btn_mission_upload.setBackgroundResource(R.color.color8);
                            im_clear_marck.setVisibility(View.GONE);

                        }else if(userMission.getPassflag().equals("N")){
                            Log.i(TAG,"===3" + userMission.getPassflag());
                            img_missiontab.setVisibility(View.GONE);
                            tx_main_usermission.setText(R.string.user_mission_n);
                            im_clear_marck.setVisibility(View.GONE);
                        }
                        Log.i(TAG," Test View End #####");


                        //내가 좋아요를 했다면
                        if(userMission.getMycheck()==0){
                            usermission_iv_hart.setImageResource(R.drawable.ic_white_hart);
                        }else{
                            usermission_iv_hart.setImageResource(R.drawable.ic_hart_red);
                        }

                        linearLayout.setVisibility(View.VISIBLE);
                        thumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                                youTubeThumbnailLoader.setVideo(userMission.getYoutubeaddr());
                            }

                            @Override
                            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
                    }
                }else{
                    VeteranToast.makeToast(getApplicationContext(),"UserMission(1) "+getString(R.string.network_error_isnotsuccessful), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UserMission> call, Throwable t) {
                VeteranToast.makeToast(getApplicationContext(),"UserMission(2) "+getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
            }
        });

    }


    //버튼 컨트롤
    @OnClick(R.id.btn_mission_upload)
    public void missionVideoUpload(){

        if(userMission.getUid()!=0){

            if(userMission.getPassflag().equals("P")) {
                VeteranToast.makeToast(getApplicationContext(), getString(R.string.user_mission_progress), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, RESULT_PICK_IMAGE_CROP);
    }

    @OnClick(R.id.ic_tree)
    public void ic_Btn(){
        VeteranToast.makeToast(getApplicationContext(),"테스트",Toast.LENGTH_SHORT).show();
    }

    //즐겨찾기 컨트롤
    public void favoriteTransaction(int uId, int missionId, String typeMethod, final ImageButton imageButton){

        FavoriteMissionService service = ServiceGenerator.createService(FavoriteMissionService.class,this,user);
        FavoriteMission favoriteMission = new FavoriteMission();
        favoriteMission.setUid(uId);
        favoriteMission.setMissionid(missionId);

        if(typeMethod.equals("getCount")){
            Call<ServerResult> callBack = service.getCountFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        favoriteCount = result.getCount();

                        if(favoriteCount != 0){
                            imageButton.setImageResource(R.drawable.star);
                        }else{
                            imageButton.setImageResource(R.drawable.star_enabled);
                        }

                    }else{
                        //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(1) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(2) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }else if(typeMethod.equals("create")){

            Call<ServerResult> callBack = service.saveFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        VeteranToast.makeToast(getApplicationContext(),"즐겨찾기에 추가했습니다", Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star);
                        favoriteCount = 1;
                    }else{
                        //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star_enabled);
                        favoriteCount = 0;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(4) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }else if(typeMethod.equals("delete")){

            Call<ServerResult> callBack = service.deleteFavoriteMission(favoriteMission);
            callBack.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {

                    if(response.isSuccessful()){
                        VeteranToast.makeToast(getApplicationContext(),"즐겨찾기에서 제외 했습니다", Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star_enabled);
                        favoriteCount = 0;
                    }else{
                        //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(3) : " + getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                        imageButton.setImageResource(R.drawable.star);
                        favoriteCount = 1;
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    //VeteranToast.makeToast(getApplicationContext(),"Favorite Info Error(6) : " + getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }
    }

    @OnClick(R.id.main_mission_start)
    public void btnStart(){
        if(favoriteCount==0){
            favoriteTransaction(user.getUid(),mission.getMissionid(),"create",btnStart);
            favoriteCount=1;
        }else{
            favoriteTransaction(user.getUid(),mission.getMissionid(),"delete",btnStart);
            favoriteCount=0;
        }
    }

    @OnClick(R.id.btnMainVideo)
    public void userVideoList(){
        Intent intent = new Intent(this,UserMissionListActivity.class);
        startActivity(intent);
    }

/*    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            if(!setNet){
                youTubePlayer.cueVideo(mission.getYoutubeaddr());
            }else{
                //유투브 주소가 없다면 문제. 이럴때는 몸싸커 메인 인트로를 보여준다
                MomComService service = ServiceGenerator.createService(MomComService.class,MissionMainActivity.this,user);
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
        youTubeInitializationResult.isUserRecoverableError();
    }*/
}
