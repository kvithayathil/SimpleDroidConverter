package com.jedikv.simpleconverter.component;

import com.jedikv.simpleconverter.App;

import javax.inject.Singleton;

import dagger.Component;
import module.AppModule;

/**
 * Created by Kurian on 03/05/2015.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

   public void inject(App app);
}
