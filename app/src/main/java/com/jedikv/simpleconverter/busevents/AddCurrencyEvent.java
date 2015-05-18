package com.jedikv.simpleconverter.busevents;

/**
 * Created by KV_87 on 17/05/15.
 */
public class AddCurrencyEvent {

    private final String mCurrencyCode;

    public AddCurrencyEvent(String currencyCode) {

        mCurrencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }
}
