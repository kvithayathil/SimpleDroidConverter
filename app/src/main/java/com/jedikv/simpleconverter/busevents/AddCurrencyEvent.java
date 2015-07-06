package com.jedikv.simpleconverter.busevents;

import converter_db.CurrencyEntity;

/**
 * Created by KV_87 on 17/05/15.
 */
public class AddCurrencyEvent {

    private final CurrencyEntity mCurrency;

    public AddCurrencyEvent(CurrencyEntity selectedCurrency) {

        mCurrency = selectedCurrency;
    }

    public CurrencyEntity getCurrency() {
        return mCurrency;
    }
}
