package com.jedikv.simpleconverter.domain;

import com.google.auto.value.AutoValue;

/**
 * Created by Kurian on 30/10/2016.
 */
@AutoValue
public abstract class CurrencyItem {

    public abstract long numCode();
    public abstract String symbol();
    public abstract String isoCode();
    public abstract String name();
    public abstract String location();

    @AutoValue.Builder
    public static abstract class Builder {
        abstract Builder numCode(long numCode);
        abstract Builder name(String name);
        abstract Builder symbol(String symbol);
        abstract Builder isoCode(String isoCode);
        abstract Builder location(String location);
        abstract CurrencyItem build();
    }

    public static Builder builder() {
        return new AutoValue_CurrencyItem.Builder();
    }

}
