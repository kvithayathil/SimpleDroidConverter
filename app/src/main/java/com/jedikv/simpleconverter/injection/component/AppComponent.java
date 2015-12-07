package com.jedikv.simpleconverter.injection.component;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.dbutils.ConversionItemDbHelper;
import com.jedikv.simpleconverter.ui.activities.BaseActivity;
import com.jedikv.simpleconverter.ui.activities.CurrencyPickerActivity;
import com.jedikv.simpleconverter.ui.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
import com.jedikv.simpleconverter.injection.module.AppModule;
import com.jedikv.simpleconverter.injection.module.CurrencyUpdaterModule;

/**
 * Created by Kurian on 03/05/2015.
 */
@Singleton
@Component(modules = {AppModule.class, CurrencyUpdaterModule.class})
public interface AppComponent {

   public void inject(App app);

   public void inject(BaseActivity baseActivity);

   public void inject (MainActivity mainActivity);

   public void inject (CurrencyPickerActivity currencyPickerActivity);



}
