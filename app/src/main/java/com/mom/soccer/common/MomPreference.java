package com.mom.soccer.common;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mom.soccer.R;


public class MomPreference extends Preference {
    public MomPreference(Context context) {
        super(context);
    }

    public MomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MomPreference(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTextColor(view.getResources().getColor(R.color.color6));
        titleView.setTextSize(12);
    }
    
}
