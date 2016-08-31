package com.mom.soccer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.dto.UserMission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sungbo on 2016-07-27.
 */
public class GridMissionAdapter extends BaseAdapter implements YouTubeThumbnailView.OnInitializedListener {

    private static final String TAG = "GridMissionAdapter";

    private Map<View, YouTubeThumbnailLoader> mLoaders;
    private Context mContext = null;
    LayoutInflater inflater = null;
    private int layout = 0;
    private List<UserMission> misstionVos = null;
    private String receiveFlag;

    static class VideoHolder{

        //내 영상은 누구의 영상인지 이름을 뺀다
        TextView tx_missionname;
        YouTubeThumbnailView thumb;
        ImageView imageview_clear_marck;
        ImageView imageview_iv_hart;     //내가 좋아요를 눌렀다면...
        TextView text_hart;
        ImageView imageview_iv_comment;  //내가 댓글을 달았다면...
        TextView text_comment;
        TextView text_ninck_name;
        TextView text_subject;

    }


    public GridMissionAdapter(Context c, int l, List<UserMission> misstionVos,String receiveFlag) {
        this.mContext = c;
        this.layout = l;
        this.misstionVos = misstionVos;
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.receiveFlag = receiveFlag;
        mLoaders = new HashMap<View, YouTubeThumbnailLoader>();
    }

    @Override
    public int getCount() {
        return misstionVos.size();
    }

    @Override
    public Object getItem(int position) {
        return misstionVos.get(position).getUsermissionid();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View currentRow = convertView;
        VideoHolder holder;

        if(convertView==null){
            currentRow = inflater.inflate(layout, parent, false);
            holder = new VideoHolder();

            holder.tx_missionname = (TextView) currentRow.findViewById(R.id.tx_missionname);
            holder.thumb = (YouTubeThumbnailView) currentRow.findViewById(R.id.grid_youtybe_Thumbnail);
            holder.text_ninck_name = (TextView) currentRow.findViewById(R.id.tx_ninck_name);
            holder.text_hart = (TextView) currentRow.findViewById(R.id.tx_hart);
            holder.text_comment = (TextView) currentRow.findViewById(R.id.tx_comment);
            holder.imageview_iv_hart = (ImageView) currentRow.findViewById(R.id.iv_hart);
            holder.imageview_clear_marck = (ImageView) currentRow.findViewById(R.id.image_clear_marck);
            holder.text_subject = (TextView) currentRow.findViewById(R.id.tx_subject);

            holder.tx_missionname.setText(misstionVos.get(position).getMissionname());  //미션명
            holder.text_ninck_name.setText(misstionVos.get(position).getUsername());    //수행유저
            holder.text_subject.setText(misstionVos.get(position).getSubject());    //유저미션 제목
            holder.text_hart.setText(String.valueOf(misstionVos.get(position).getBookmarkcount())); //유저미션 좋아요 숫자
            holder.text_comment.setText(misstionVos.get(position).getBoardcount()); //유저미션 댓글 등록수

            holder.thumb.setTag(misstionVos.get(position).getYoutubeaddr());

            if(misstionVos.get(position).getMycheck()==0){
                holder.imageview_iv_hart.setImageResource(R.drawable.ic_white_hart);
            }else{
                holder.imageview_iv_hart.setImageResource(R.drawable.ic_hart_red);
            }

            if(misstionVos.get(position).getPassflag().equals("Y")){
                holder.imageview_clear_marck.setVisibility(View.VISIBLE);
            }else{
                holder.imageview_clear_marck.setVisibility(View.GONE);
            }

            currentRow.setTag(holder);
            holder.thumb.initialize(Auth.KEY,this);

        }else{

            holder = (VideoHolder) currentRow.getTag();
            final YouTubeThumbnailLoader loader = mLoaders.get(holder.thumb);

            if(misstionVos.get(position) != null){
                if (loader == null) {
                    //Loader is currently initialising
                    holder.thumb.setTag(misstionVos.get(position).getYoutubeaddr());
                } else {
                    //The loader is already initialised
                    loader.setVideo(misstionVos.get(position).getVideoaddr());
                }

                holder.tx_missionname.setText(misstionVos.get(position).getMissionname());  //미션명
                holder.text_ninck_name.setText(misstionVos.get(position).getUsername());
                holder.text_subject.setText(misstionVos.get(position).getSubject());
                holder.text_hart.setText(String.valueOf(misstionVos.get(position).getBookmarkcount()));
                holder.text_comment.setText(misstionVos.get(position).getBoardcount());

                holder.thumb.setTag(misstionVos.get(position).getYoutubeaddr());

                if(misstionVos.get(position).getMycheck()==0){
                    holder.imageview_iv_hart.setImageResource(R.drawable.ic_white_hart);
                }else{
                    holder.imageview_iv_hart.setImageResource(R.drawable.ic_hart_red);
                }

                if(misstionVos.get(position).getPassflag().equals("Y")){
                    holder.imageview_clear_marck.setVisibility(View.VISIBLE);
                }else{
                    holder.imageview_clear_marck.setVisibility(View.GONE);
                }

            }
        }
        return currentRow;
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        youTubeThumbnailLoader.setVideo((String) youTubeThumbnailView.getTag());
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
