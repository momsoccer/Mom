package com.mom.soccer.common;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mom.soccer.R;

/**
 * Created by sungbo on 2016-08-01.
 */
public class MomSwitchPreference extends SwitchPreference {
    public MomSwitchPreference(Context context) {
        super(context);
    }

    public MomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView textView = (TextView) view.findViewById(android.R.id.summary);
        textView.setTextColor(view.getResources().getColor(R.color.color8));
        textView.setTextSize(10);

        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTextColor(view.getResources().getColor(R.color.color6));
        titleView.setTextSize(12);
    }
}
