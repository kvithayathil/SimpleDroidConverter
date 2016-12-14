package com.jedikv.simpleconverter.data;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.jedikv.simpleconverter.R;

/**
 * Created by Kurian on 13/12/2016.
 */

public class LocalKeyValueCache {

    private final SharedPreferences sharedPreferences;
    private final Resources resources;

    private static final String SAVED_SELECTED_CURRENCY = "selected_currency_iso";
    private static final String SAVED_ENDTERED_VALUE = "saved_entered_value";

    public LocalKeyValueCache(Resources resources,
                              SharedPreferences sharedPreferences) {

        this.sharedPreferences = sharedPreferences;
        this.resources = resources;
    }

    /**
     * Cache the currently entered values for re-use later
     * @param currencyIso the ISO code of the selected source currency (e.g. USD)
     * @param value the numerical value that was last entered (ideally as an integer).
     */
    public void saveSelectedSourceCurrencyIsoAndValue(String currencyIso, int value) {
        sharedPreferences
                .edit()
                .putString(SAVED_SELECTED_CURRENCY, currencyIso)
                .putInt(SAVED_ENDTERED_VALUE, value)
                .apply();
    }

    /**
     * Fetch the last entered value to be converted
     * @return cached value (or default value)
     */
    public int fetchSavedValue() {
        return sharedPreferences.getInt(SAVED_ENDTERED_VALUE, 0);
    }

    /**
     * Fetch the ISO code of the last selected currency or return the default
     * currency that has been defined in the string resources
     * @return ISO code of the last selected source currency (or the local default)
     */
    public String fetchSelectedSourceCurrency() {
        return sharedPreferences.getString(SAVED_SELECTED_CURRENCY,
                resources.getString(R.string.default_source_currency));
    }
}
