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
    public abstract Integer position();
    public abstract String source();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(long numCode);
        public abstract Builder conversionComboId(String comboId);
        public abstract Builder currencyCode(String currencyId);
        public abstract Builder pairToCode(String currencyId);
        public abstract Builder conversionRate(int conversionRate);
        public abstract Builder lastUpdatedDate(Long date);
        public abstract Builder position(Integer position);
        public abstract Builder source(String source);
        public abstract ConversionItem build();
    }

    public abstract Builder toBuilder();


    public ConversionItem updatePosition(int position) {
        return toBuilder()
                .position(position)
                .build();
    }

    public ConversionItem updateRate(String source, int rate) {
        return toBuilder()
                .conversionRate(rate)
                .lastUpdatedDate(System.currentTimeMillis())
                .source(source)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ConversionItem
                .Builder()
                .conversionRate(1);
    }
}
