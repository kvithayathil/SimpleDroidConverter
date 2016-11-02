package com.jedikv.simpleconverter.injection.component;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.injection.module.AppModule;
import com.jedikv.simpleconverter.injection.module.CurrencyUpdaterModule;
import com.jedikv.simpleconverter.ui.activities.BaseActivity;
import com.jedikv.simpleconverter.ui.activities.CurrencyPickerActivity;
import com.jedikv.simpleconverter.ui.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Kurian on 03/05/2015.
 */
@Singleton
@Component(modules = {AppModule.class, CurrencyUpdaterModule.class})
public interface AppComponent {

   void inject(App app);

   void inject(BaseActivity baseActivity);

   void inject (MainActivity mainActivity);

   void inject (CurrencyPickerActivity currencyPickerActivity);
}
