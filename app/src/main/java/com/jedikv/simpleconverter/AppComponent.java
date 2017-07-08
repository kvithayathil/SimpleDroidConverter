package com.jedikv.simpleconverter;

import dagger.Component;

/**
 * Created by Kurian on 03/05/2015.
 */
@ConversionAppScope
@Component(modules = {AppModule.class})
public interface AppComponent {
}
