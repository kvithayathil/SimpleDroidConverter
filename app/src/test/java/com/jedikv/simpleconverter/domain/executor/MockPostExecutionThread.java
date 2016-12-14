package com.jedikv.simpleconverter.domain.executor;

import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

/**
 * Created by Kurian on 30/11/2016.
 */
public class MockPostExecutionThread implements PostExecutionThread {

    private final TestScheduler scheduler;

    public MockPostExecutionThread() {
        this.scheduler = Schedulers.test();
    }

    @Override
    public TestScheduler getScheduler() {
        return scheduler;
    }
}