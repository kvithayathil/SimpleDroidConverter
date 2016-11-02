package com.jedikv.simpleconverter.domain.interactor;

import com.jedikv.simpleconverter.domain.executor.PostExecutionThread;
import com.jedikv.simpleconverter.domain.executor.ThreadExecutor;

/**
 * Created by Kurian on 01/11/2016.
 */

public abstract class UseCase {

    private final PostExecutionThread mainThread;
    private final ThreadExecutor executionThread;

    public UseCase(PostExecutionThread mainThread, ThreadExecutor executionThread) {
        this.mainThread = mainThread;
        this.executionThread = executionThread;
    }
}
