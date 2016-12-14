package com.jedikv.simpleconverter.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Kurian on 08/05/2015.
 */
public class OttoBus extends Bus {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public OttoBus(ThreadEnforcer enforcer) {
        super(enforcer);
    }

    public OttoBus (String identifier) {
        super(identifier);
    }

    public OttoBus() {
        super();
    }

    @Override
    public void post(final Object event) {
        if(Looper.myLooper() == Looper.getMainLooper().getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                  OttoBus.super.post(event);
                }
            });
        }
    }
}
