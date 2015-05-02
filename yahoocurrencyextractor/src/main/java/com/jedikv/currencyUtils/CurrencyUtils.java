package com.jedikv.currencyUtils;

import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Kurian on 02/05/2015.
 */
public class CurrencyUtils {

    public static SortedMap<Currency, Locale> currencyLocaleSortedMap;

    private CurrencyUtils() {

    }

    static {
        currencyLocaleSortedMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
            @Override
            public int compare(Currency c1, Currency c2) {
                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });

        for (Locale locale: Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                currencyLocaleSortedMap.put(currency, locale);
            } catch (Exception e) {

            }
        }
    }

    public static String getCurrencySymbol (String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);

        try {
            return currency.getSymbol(currencyLocaleSortedMap.get(currency));
        } catch (NullPointerException e) {
            return currencyCode;
        }
    }
}
