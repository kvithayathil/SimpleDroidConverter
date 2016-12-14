package com.jedikv.simpleconverter;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.jedikv.simpleconverter.data.DataModule;
import com.jedikv.simpleconverter.domain.interactor.ConversionOperations;
import com.jedikv.simpleconverter.domain.interactor.GetCurrencyList;

import dagger.Component;

/**
 * Created by Kurian on 03/05/2015.
 */
@ConversionAppScope
@Component(modules = {AppComponent.class, DataModule.class})
public interface AppComponent {

    ConversionOperations NetworkDataSource();

    GetCurrencyList providesGetCurrencyList();

    SharedPreferences providesSharedPrefs();

    Resources providesAndroidResources();
}
