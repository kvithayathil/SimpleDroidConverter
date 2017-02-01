package com.jedikv.simpleconverter;

import com.jedikv.simpleconverter.data.DataModule;
import com.jedikv.simpleconverter.data.LocalKeyValueCache;
import com.jedikv.simpleconverter.domain.DomainModule;
import com.jedikv.simpleconverter.domain.interactor.ConversionOperations;
import com.jedikv.simpleconverter.domain.interactor.GetCurrencyList;

import dagger.Component;

/**
 * Created by Kurian on 03/05/2015.
 */
@ConversionAppScope
@Component(modules = {AppModule.class, DataModule.class, DomainModule.class})
public interface AppComponent {
    LocalKeyValueCache providesLocalKeyValueCache();
    ConversionOperations providesNetworkDataSource();
    GetCurrencyList providesGetCurrencyList();
}
