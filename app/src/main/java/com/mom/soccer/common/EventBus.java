package com.mom.soccer.common;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by sungbo on 2016-09-11.
 */
public class EventBus extends Bus {

    private static EventBus instance;

    public static EventBus getInstance() {

        Log.i("EventBus","이벤트 버스가 발생1");

        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    EventBus.super.post(event);
                }
            });
        }
    }

}
