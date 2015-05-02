package com.jedikv.simpleconverter.component;

import dagger.Component;
import module.CurrencyPairModule;

/**
 * Created by Kurian on 02/05/2015.
 */

@Component(modules = CurrencyPairModule.class)
public interface CurrencyPairComponent {

    CurrencyPairModule provideCurrencyPair();
}
