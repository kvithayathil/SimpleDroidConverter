package com.jedikv.simpleconverter.component;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.ui.activities.BaseActivity;
import com.jedikv.simpleconverter.ui.activities.CurrencyPickerActivity;
import com.jedikv.simpleconverter.ui.activities.MainActivity;

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

   public void inject(BaseActivity baseActivity);

   public void inject (MainActivity mainActivity);

   public void inject (CurrencyPickerActivity currencyPickerActivity);
}
