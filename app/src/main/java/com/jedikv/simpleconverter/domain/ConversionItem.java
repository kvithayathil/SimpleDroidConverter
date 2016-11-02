package com.jedikv.simpleconverter.domain;

import com.google.auto.value.AutoValue;

/**
 * Created by Kurian on 30/10/2016.
 */
@AutoValue
public abstract class ConversionItem {

    public abstract long id();
    public abstract String conversionComboId();
    public abstract String currencyCode();
    public abstract String pairToCode();
    public abstract int conversionRate();
    public abstract Long lastUpdatedDate();
    public abstract int position();
    public abstract String source();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(long numCode);
        public abstract Builder conversionComboId(String comboId);
        public abstract Builder currencyCode(String currencyId);
        public abstract Builder pairToCode(String currencyId);
        public abstract Builder conversionRate(int conversionRate);
        public abstract Builder lastUpdatedDate(Long date);
        public abstract Builder position(int position);
        public abstract Builder source(String source);
        public abstract ConversionItem build();
    }

    public static Builder builder() {
        return new AutoValue_ConversionItem
                .Builder()
                .lastUpdatedDate(System.currentTimeMillis())
                .position(-1);
    }
}
