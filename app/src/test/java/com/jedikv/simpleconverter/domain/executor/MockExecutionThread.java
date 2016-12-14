package com.jedikv.simpleconverter.domain.executor;

import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

/**
 * Created by Kurian on 30/11/2016.
 */
public class MockExecutionThread implements ThreadExecutor {

    private final TestScheduler scheduler;

    public MockExecutionThread() {
        this.scheduler = Schedulers.test();
    }

    @Override
    public TestScheduler getScheduler() {
        return scheduler;
    }
}