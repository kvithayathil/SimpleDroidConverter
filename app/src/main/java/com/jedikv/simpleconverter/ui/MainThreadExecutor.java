package com.jedikv.simpleconverter.ui;

import com.jedikv.simpleconverter.domain.executor.PostExecutionThread;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Kurian on 01/11/2016.
 */

public class MainThreadExecutor implements PostExecutionThread {
    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
