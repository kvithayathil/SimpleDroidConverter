package com.jedikv.simpleconverter.ui.conversionscreen;

import com.jedikv.simpleconverter.AppComponent;

import dagger.Component;

/**
 * Created by Kurian on 12/12/2016.
 */
@Component(modules = {ConversionActivityModule.class},
        dependencies = {AppComponent.class})
public interface ConversionActivityComponent {

    void injectConversionActivity(MainActivity mainActivity);
}
