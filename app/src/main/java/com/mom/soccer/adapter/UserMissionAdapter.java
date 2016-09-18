package com.mom.soccer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Compare;
import com.mom.soccer.common.RoundedCornersTransformation;
import com.mom.soccer.dataDto.UserMainVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.dto.UserMission;
import com.mom.soccer.mission.MissionCommon;
import com.mom.soccer.mission.UserMissionActivity;
import com.mom.soccer.retrofitdao.UserMissionService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.WaitingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungbo on 2016-08-25.
 */
public class UserMissionAdapter extends RecyclerView.Adapter<UserMissionAdapter.MissionHorder>{

    private static final String TAG = "UserMissionAdapter";

    Activity activity;
    User user;
    List<UserMainVo> userMainVo;
    UserMission userMission;


    public UserMissionAdapter(Activity activity, User user, List<UserMainVo> userMainVo) {
        this.activity = activity;
        this.user = user;
        this.userMainVo = userMainVo;
    }

    @Override
    public MissionHorder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_mission_item, null);
        return new MissionHorder(v);
    }

    @Override
    public void onBindViewHolder(MissionHorder h, int position) {
        final UserMainVo vo = userMainVo.get(position);

        if(vo.getPassflag().equals("Y")){

            h.missionclearMark.setVisibility(View.VISIBLE);
            h.feedbackstatus.setText(activity.getString(R.string.user_mission_y));
            //h.batch.setVisibility(View.VISIBLE);

        }else if(vo.getPassflag().equals("P")){

            h.missionclearMark.setVisibility(View.GONE);
            h.feedbackstatus.setText(activity.getString(R.string.user_mission_p));
            //h.batch.setVisibility(View.GONE);

        }else if(vo.getPassflag().equals("N")){

            h.missionclearMark.setVisibility(View.GONE);
            h.feedbackstatus.setText(activity.getString(R.string.user_mission_n));
            //h.batch.setVisibility(View.GONE);

        }

        if (!Compare.isEmpty(vo.getProfileimgurl())) {
            Glide.with(activity)
                    .load(vo.getProfileimgurl())
                    .asBitmap().transform(new RoundedCornersTransformation(activity, 10, 5))
                    .into(h.userimg);
        }


        if(!Compare.isEmpty(vo.getTeamname())){
            h.teamname.setText(vo.getTeamname());
        }else{
            h.teamname.setText(activity.getString(R.string.user_team_yet_join));
        }

        h.misisonname.setText(vo.getMissionname());
        h.description.setText(vo.getDescription());
        h.boardcount.setText(String.valueOf(vo.getBoardcount()));
        h.feedbackcount.setText(String.valueOf(vo.getFeedbackcount()));
        h.favoritecount.setText(String.valueOf(vo.getBookmarkcount()));
        h.usersubject.setText(vo.getUsersubject());
        h.userdescription.setText(vo.getUserdescription());
        h.level.setText(String.valueOf(vo.getLevel()));
        h.grade.setText(String.valueOf(vo.getGrade()));
        h.passgrade.setText(String.valueOf(vo.getPassgrade()));
        h.username.setText(vo.getUsername());
        h.sequence.setText("Level."+vo.getSequence());
        h.date.setText(vo.getChange_creationdate());

        if(vo.getMissiontype().equals("DRIBLE")){
            h.missionline.setBackground(activity.getResources().getDrawable(R.drawable.rec_xml_drible));
            h.smallbox.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_drible));
            h.ri_back_ground.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_drible));
            h.batch.setImageDrawable(activity.getResources().getDrawable(R.drawable.batch_drible_a));
        }else if(vo.getMissiontype().equals("LIFTING")){
            h.missionline.setBackground(activity.getResources().getDrawable(R.drawable.rec_xml_lifting));
            h.smallbox.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_lifting));
            h.ri_back_ground.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_lifting));
            h.batch.setImageDrawable(activity.getResources().getDrawable(R.drawable.batch_lifting_a));
        }else if(vo.getMissiontype().equals("TRAPING")){
            h.missionline.setBackground(activity.getResources().getDrawable(R.drawable.rec_xml_traping));
            h.smallbox.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_traping));
            h.ri_back_ground.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_traping));
            h.batch.setImageDrawable(activity.getResources().getDrawable(R.drawable.batch_traping_a));
        }else if(vo.getMissiontype().equals("AROUND")){
            h.missionline.setBackground(activity.getResources().getDrawable(R.drawable.rec_xml_around));
            h.smallbox.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_around));
            h.ri_back_ground.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_around));
            h.batch.setImageDrawable(activity.getResources().getDrawable(R.drawable.batch_around_a));
        }else if(vo.getMissiontype().equals("FLICK")){
            h.missionline.setBackground(activity.getResources().getDrawable(R.drawable.rec_xml_flick));
            h.smallbox.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_flick));
            h.ri_back_ground.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_flick));
            h.batch.setImageDrawable(activity.getResources().getDrawable(R.drawable.batch_flick_a));
        }else if(vo.getMissiontype().equals("COMPLEX")){
            h.missionline.setBackground(activity.getResources().getDrawable(R.drawable.rec_xml_complex));
            h.smallbox.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_complex));
            h.ri_back_ground.setBackground(activity.getResources().getDrawable(R.drawable.xml_boder_complex));
            h.batch.setImageDrawable(activity.getResources().getDrawable(R.drawable.batch_complex_a));
        }


        h.uservideo.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getUservideo());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        h.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,activity,user);
                UserMission query = new UserMission();
                query.setUsermissionid(vo.getUsermissionid());
                Call<UserMission> call = userMissionService.getUserMission(query);
                call.enqueue(new Callback<UserMission>() {
                    @Override
                    public void onResponse(Call<UserMission> call, Response<UserMission> response) {
                        if(response.isSuccessful()){
                            UserMission userMission = response.body();
                            Intent intent = new Intent(activity,UserMissionActivity.class);
                            intent.putExtra(MissionCommon.USER_MISSTION_OBJECT,userMission);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserMission> call, Throwable t) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return userMainVo.size();
    }



    public class MissionHorder extends RecyclerView.ViewHolder {

        CardView cardview;
        ImageView missionclearMark,batch,userimg;
        YouTubeThumbnailView uservideo;
        TextView username,teamname,misisonname,description,favoritecount,boardcount,usersubject,userdescription,feedbackstatus,level,grade,passgrade,feedbackcount,sequence,date;
        View missionline;
        LinearLayout smallbox;
        RelativeLayout ri_back_ground;

        public MissionHorder(View view) {
            super(view);
            cardview = (CardView) view.findViewById(R.id.cardview);

            missionclearMark = (ImageView) view.findViewById(R.id.missionclearMark);
            batch = (ImageView) view.findViewById(R.id.batch);
            userimg = (ImageView) view.findViewById(R.id.userimg);

            uservideo = (YouTubeThumbnailView) view.findViewById(R.id.uservideo);

            username = (TextView) view.findViewById(R.id.username);
            teamname = (TextView) view.findViewById(R.id.teamname);
            misisonname = (TextView) view.findViewById(R.id.misisonname);
            description = (TextView) view.findViewById(R.id.description);
            favoritecount = (TextView) view.findViewById(R.id.favoritecount);

            boardcount = (TextView) view.findViewById(R.id.boardcount);
            usersubject = (TextView) view.findViewById(R.id.usersubject);
            misisonname = (TextView) view.findViewById(R.id.misisonname);
            userdescription = (TextView) view.findViewById(R.id.userdescription);
            feedbackstatus = (TextView) view.findViewById(R.id.feedbackstatus);
            missionline = view.findViewById(R.id.missionline);
            level = (TextView) view.findViewById(R.id.level);
            grade = (TextView) view.findViewById(R.id.grade);
            passgrade = (TextView) view.findViewById(R.id.passgrade);

            smallbox = (LinearLayout) view.findViewById(R.id.smallbox);
            ri_back_ground = (RelativeLayout) view.findViewById(R.id.ri_back_ground);
            feedbackcount = (TextView) view.findViewById(R.id.feedbackcount);
            sequence = (TextView) view.findViewById(R.id.sequence);
            date = (TextView) view.findViewById(R.id.date);

        }
    }

    //when move UsermissionA move
    public void getUserMission(int missionId){
        WaitingDialog.showWaitingDialog(activity,false);
        UserMission u = new UserMission();
        u.setMissionid(missionId);
        u.setUid(user.getUid());
        UserMissionService userMissionService = ServiceGenerator.createService(UserMissionService.class,activity,user);
        Call<UserMission> c = userMissionService.getUserMission(u);
    }

}
