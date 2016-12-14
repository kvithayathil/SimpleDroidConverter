package com.jedikv.simpleconverter.ui.conversionscreen;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 12/12/2016.
 */
@Module
public class ConversionActivityModule {

    private final MainActivity activity;

    public ConversionActivityModule(MainActivity activity) {
        this.activity = activity;
    }


    @Provides
    @ConversionActivityScope
    public MainActivity providesConversionActivity() {
        return this.activity;
    }

}
