package com.mom.soccer.alluser.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mom.soccer.R;
import com.mom.soccer.dto.AdBoardFile;

import java.util.List;


public class ImageBigAdapter extends RecyclerView.Adapter<ImageBigAdapter.ImageViewHoder>{

    private static final String TAG = "AdBoardAdapter";

    Activity activity;
    List<AdBoardFile> adBoardFiles;

    public ImageBigAdapter(Activity activity, List<AdBoardFile> adBoardFiles) {
        this.activity = activity;
        this.adBoardFiles = adBoardFiles;
    }

    @Override
    public ImageViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_big, null);
        return new ImageViewHoder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHoder holder, int position) {
        final AdBoardFile vo = adBoardFiles.get(position);

        Glide.with(activity)
                .load(vo.getFileaddr())
                .fitCenter()
                .thumbnail(0.1f)
                .override(300,300)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return adBoardFiles.size();
    }

    public class ImageViewHoder extends RecyclerView.ViewHolder{

        ImageView image;

        public ImageViewHoder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);

        }
    }

}
