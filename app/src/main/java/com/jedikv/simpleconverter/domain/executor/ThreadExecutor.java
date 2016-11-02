package com.jedikv.simpleconverter.domain.executor;

import rx.Scheduler;

/**
 * Created by Kurian on 01/11/2016.
 */

public interface ThreadExecutor {
    Scheduler getScheduler();
}
