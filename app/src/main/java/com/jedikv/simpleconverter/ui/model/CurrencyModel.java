package com.jedikv.simpleconverter.ui.model;

import com.google.auto.value.AutoValue;

/**
 * Created by Kurian on 21/11/2016.
 */
@AutoValue
public abstract class CurrencyModel {

    public abstract long numericCode();
    public abstract String isoCode();
    public abstract String symbol();
    public abstract String name();
    public abstract String location();

    public static Builder builder() {
        return new AutoValue_CurrencyModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder numericCode(long conversionId);
        public abstract Builder name(String name);
        public abstract Builder symbol(String symbol);
        public abstract Builder isoCode(String isoCode);
        public abstract Builder location(String location);
        public abstract CurrencyModel build();
    }

}
