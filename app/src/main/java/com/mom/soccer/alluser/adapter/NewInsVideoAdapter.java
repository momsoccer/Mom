package com.mom.soccer.alluser.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.mom.soccer.R;
import com.mom.soccer.common.Auth;
import com.mom.soccer.common.Common;
import com.mom.soccer.dto.InsVideoVo;
import com.mom.soccer.dto.User;
import com.mom.soccer.youtubeplayer.YoutubePlayerActivity;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


public class NewInsVideoAdapter extends RecyclerView.Adapter<NewInsVideoAdapter.InsVideoViewHoder>{

    private static final String TAG = "NewInsVideoAdapter";

    private Activity activity;
    private List<InsVideoVo> insVideoVos;
    private User user;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = new GsonFactory();

    public NewInsVideoAdapter(Activity activity, List<InsVideoVo> insVideoVos, User user) {
        this.activity = activity;
        this.insVideoVos = insVideoVos;
        this.user = user;
    }

    @Override
    public InsVideoViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_ins_video_item_layout, parent, false);
        return new InsVideoViewHoder(v);
    }

    @Override
    public void onBindViewHolder(InsVideoViewHoder holder, int i) {
        final InsVideoVo vo = insVideoVos.get(i);
        final int posintion = i;

        holder.ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(vo.getYoutubeaddr());

            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        holder.subject.setText(vo.getSubject());
        holder.content.setText(vo.getContent());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,vo.getYoutubeaddr());
                activity.startActivity(intent);
            }
        });

        holder.shreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"몸싸커 강의 : "+vo.getSubject());
                intent.putExtra(Intent.EXTRA_TEXT,"https://youtu.be/"+vo.getYoutubeaddr());
                intent.putExtra(Intent.EXTRA_TITLE, vo.getContent());
                activity.startActivity(Intent.createChooser(intent, "Mom Soccer"));
            }
        });

        //Log.i(TAG,"갑은 + " + getViewCount(vo.getYoutubeaddr()));

    }

    @Override
    public int getItemCount() {
        return insVideoVos.size();
    }

    public class InsVideoViewHoder extends RecyclerView.ViewHolder{

        CardView cardview;
        YouTubeThumbnailView ThumbnailView;
        TextView subject,content;
        Button shreBtn;

        public InsVideoViewHoder(View view) {
            super(view);

            ThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.ThumbnailView);
            subject = (TextView) view.findViewById(R.id.subject);
            content = (TextView) view.findViewById(R.id.content);
            cardview = (CardView) view.findViewById(R.id.cardview);
            shreBtn = (Button) view.findViewById(R.id.shreBtn);
        }
    }


    public int getViewCount(final String youtubeVideoid){

        int viewCount = 0;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                YouTube youtube = new YouTube.Builder(transport, jsonFactory,
                        new HttpRequestInitializer() {
                            @Override
                            public void initialize(HttpRequest request) throws IOException {
                            }
                        }).setApplicationName("com.mom.cosser").build();
                try {
                    String apiKey = Auth.KEY;
                    YouTube.Videos.List listVideosRequest = youtube.videos().list("statistics");
                    listVideosRequest.setId(youtubeVideoid);
                    listVideosRequest.setKey("AIzaSyAGPtixAgRHZ9GvglPbYynHxS3XNUmkQTk");

                    VideoListResponse listResponse = listVideosRequest.execute();

                    Video video = listResponse.getItems().get(0);
                    BigInteger count = video.getStatistics().getViewCount();

                    Log.i(TAG," ViewCount: " + count);

                } catch (UserRecoverableAuthIOException e) {
                    Log.e(TAG, e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                return null;
            }

        }.execute((Void) null);

        return viewCount;

    }

}
/*
            YouTube youtube = new YouTube.Builder(transport, jsonFactory,
                    new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) throws IOException {
                        }
                    }).setApplicationName("com.mom.cosser").build();

            String apiKey = Auth.KEY;
            YouTube.Videos.List listVideosRequest = youtube.videos().list("statistics");
            listVideosRequest.setId(youtubeVideoid);
            listVideosRequest.setKey(apiKey);

            VideoListResponse listResponse = listVideosRequest.execute();

            Video video = listResponse.getItems().get(0);
            BigInteger count = video.getStatistics().getViewCount();

            Log.i(TAG," ViewCount: " + count);

 */