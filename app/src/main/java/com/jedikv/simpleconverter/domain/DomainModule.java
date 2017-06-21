package com.jedikv.simpleconverter.domain;

import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.ConversionAppScope;
import com.jedikv.simpleconverter.data.DataModule;
import com.jedikv.simpleconverter.data.NetworkDataSource;
import com.jedikv.simpleconverter.data.PersistentDataSource;
import com.jedikv.simpleconverter.domain.executor.PostExecutionThread;
import com.jedikv.simpleconverter.domain.executor.ThreadExecutor;
import com.jedikv.simpleconverter.domain.interactor.ConversionOperations;
import com.jedikv.simpleconverter.domain.interactor.ExecutionThread;
import com.jedikv.simpleconverter.domain.interactor.GetCurrencyList;
import com.jedikv.simpleconverter.ui.MainThreadExecutor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 01/11/2016.
 */
@Module(includes = {DataModule.class})
public class DomainModule {

    @Provides
    @NonNull
    @ConversionAppScope
    ThreadExecutor providesBackgroundThread() {
        return new ExecutionThread();
    }

    @Provides
    @NonNull
    @ConversionAppScope
    PostExecutionThread providesMainThread() {
        return new MainThreadExecutor();
    }

    @Provides
    @NonNull
    @ConversionAppScope
    ConversionOperations providesConversionOperation(@NonNull PersistentDataSource localSource,
                                                     @NonNull NetworkDataSource remoteSource,
                                                     @NonNull PostExecutionThread postExecThread,
                                                     @NonNull ThreadExecutor threadExecutor) {

        return new ConversionOperations(localSource, remoteSource, postExecThread, threadExecutor);
    }

    @Provides
    @ConversionAppScope
    @NonNull
    GetCurrencyList providesGetCurrencyList(@NonNull PersistentDataSource localSource,
                                            @NonNull PostExecutionThread postExecThread,
                                            @NonNull ThreadExecutor threadExecutor) {

        return new GetCurrencyList(localSource, postExecThread, threadExecutor);
    }
}
