package com.jedikv.simpleconverter.model;

import com.google.auto.value.AutoValue;

import io.requery.Entity;
import io.requery.Key;

/**
 * Created by KV_87 on 25/04/2016.
 */
@Entity
@AutoValue
public abstract class CurrencyItem {


    @AutoValue.Builder
    static abstract class Builder {

        abstract Builder setNumCode(long numCode);
        abstract Builder setName(String name);
        abstract Builder setSymbol(String symbol);
        abstract Builder setISOCode(String isoCode);
        abstract Builder setLocation(String location);
        abstract CurrencyItem build();

    }

    static Builder builder() {
        return new AutoValue_CurrencyItem.Builder();
    }


    @Key
    public abstract long getNumCode();
    public abstract String getSymbol();
    public abstract String getISOCode();
    public abstract String getName();
    public abstract String getLocation();

}
