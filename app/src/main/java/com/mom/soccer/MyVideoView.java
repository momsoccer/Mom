package com.mom.soccer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Created by sungbo on 2016-07-28.
 */
public class MyVideoView extends VideoView
{

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Display dis =((WindowManager)getContext().

                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


        setMeasuredDimension(dis.getWidth(), dis.getHeight() );

    }
}
