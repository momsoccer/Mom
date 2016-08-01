package com.mom.soccer.bottommenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.adapter.GridMissionAdapter;
import com.mom.soccer.common.BlurTransformation;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.ExpandableHeightGridView;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity {

    private static final String TAG = "MyPageActivity";


    @Bind(R.id.mypage_image_user_image)
    ImageView mypageImage;

    @Bind(R.id.mypage_back_image)
    ImageView mypageBackImage;

    @Bind(R.id.mypage_no_data_found)
    TextView noData_textView;

    private User user = new User();
    private PrefUtil prefUtil;

    GridMissionAdapter gridMissionAdapter;
    ExpandableHeightGridView videoGridView;
    UserMission qUserMission = new UserMission();
    List<UserMission> userMissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bottom_mypage_layout);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getString(R.string.toolbar_page_mypage));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();

        if(!Compare.isEmpty(user.getProfileimgurl())) {
            Glide.with(MyPageActivity.this)
                    .load(user.getProfileimgurl())
                    .into(mypageImage);


            //리니어 레이아웃에 블러드 효과 주기
            Glide.with(MyPageActivity.this)
                    .load(user.getProfileimgurl())
                    .asBitmap().transform(new BlurTransformation(this, 25))
                    .into(mypageBackImage);
        }

        videoGridView = (ExpandableHeightGridView) findViewById(R.id.mypage_gridview);
        videoGridView.setExpanded(true);


        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent userMissionListIntent = new Intent(getApplicationContext(),UserMissionActivity.class);
                userMissionListIntent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMissions.get(position));
                startActivity(userMissionListIntent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //조회 조건 <내영상 리스트>
        qUserMission.setUid(user.getUid());
        userGrid_MissionList(qUserMission);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //홈버튼클릭시
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void userGrid_MissionList(final UserMission userMission){

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);

        /*
        final AlertDialog dialog = new SpotsDialog(this, R.style.Custom);
        dialog.show();
        */

        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,getApplicationContext(),user);
        Call<List<UserMission>> call = userMissionService.getUserMissionList(userMission);
        call.enqueue(new Callback<List<UserMission>>() {
            @Override
            public void onResponse(Call<List<UserMission>> call, Response<List<UserMission>> response) {

                if(response.isSuccessful()){
                    userMissions = response.body();
                    gridMissionAdapter = new GridMissionAdapter(getApplicationContext(),R.layout.adapter_user_mission_grid_item,userMissions,"ME");
                    videoGridView.setAdapter(gridMissionAdapter);
                    dialog.dismiss();

                    if(userMissions.size()==0){
                        noData_textView.setVisibility(View.VISIBLE);
                        noData_textView.setText("등록된 영상이 없습니다");
                    }else{
                        noData_textView.setVisibility(View.INVISIBLE);
                    }

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
