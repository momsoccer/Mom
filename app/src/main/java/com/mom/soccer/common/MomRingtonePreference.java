package com.mom.soccer.common;

import android.content.Context;
import android.preference.RingtonePreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mom.soccer.R;

/**
 * Created by sungbo on 2016-08-01.
 */
public class MomRingtonePreference extends RingtonePreference {
    public MomRingtonePreference(Context context) {
        super(context);
    }

    public MomRingtonePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MomRingtonePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTextColor(view.getResources().getColor(R.color.color6));
        titleView.setTextSize(12);
    }
}
