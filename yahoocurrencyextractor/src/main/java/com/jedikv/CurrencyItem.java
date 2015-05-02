package com.jedikv;

import com.google.gson.annotations.SerializedName;

import java.util.Currency;
import java.util.Locale;

/**
 * Created by Kurian on 02/05/2015.
 */
public class CurrencyItem {

    @SerializedName("numericCode")
    private long id;
    private String symbol;
    private String name;
    private String countryName;
    private String code;

    public CurrencyItem(Locale locale, Currency currency, String symbol) {
        this.symbol = symbol;
        this.name = currency.getDisplayName(locale);
        this.code = currency.getCurrencyCode();
        this.id = currency.getNumericCode();
        this.countryName = locale.getDisplayCountry();

        System.out.print("CurrencyItem: " + locale.getDisplayCountry(locale) + " No Locale: " + locale.getDisplayCountry() + "\n");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
