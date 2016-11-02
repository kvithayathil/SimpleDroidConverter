package com.jedikv.simpleconverter.domain;

import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.domain.executor.PostExecutionThread;
import com.jedikv.simpleconverter.domain.executor.ThreadExecutor;
import com.jedikv.simpleconverter.domain.interactor.ExecutionThread;
import com.jedikv.simpleconverter.ui.MainThreadExecutor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kurian on 01/11/2016.
 */
@Module
public class DomainModule {

    public static final String SCHEDULERS_EXECUTION = "schedulers_execution";
    public static final String SCHEDULERS_MAIN = "schedulers_main";

    @Provides
    @NonNull
    @Singleton
    @Named(SCHEDULERS_EXECUTION)
    ThreadExecutor providesBackgroundThread() {
        return new ExecutionThread();
    }

    @Provides
    @NonNull
    @Singleton
    PostExecutionThread providesMainThread() {
        return new MainThreadExecutor();
    }
}
