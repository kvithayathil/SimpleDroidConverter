package com.jedikv.simpleconverter.domain.interactor;

import com.jedikv.simpleconverter.domain.executor.ThreadExecutor;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Kurian on 01/11/2016.
 */

public class ExecutionThread implements ThreadExecutor {

    @Override
    public Scheduler getScheduler() {
        return Schedulers.io();
    }
}
