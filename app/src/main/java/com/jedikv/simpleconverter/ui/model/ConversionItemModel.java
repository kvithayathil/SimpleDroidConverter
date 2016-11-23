package com.jedikv.simpleconverter.ui.model;

import android.os.Build;

import com.google.auto.value.AutoValue;

import java.math.BigDecimal;

/**
 * Created by Kurian on 01/11/2016.
 */
@AutoValue
public abstract class ConversionItemModel {

    public abstract long conversionId();
    public abstract String isoCode();
    public abstract String symbol();
    public abstract String name();
    public abstract String location();
    public abstract boolean symbolAtStart();
    public abstract String decimalMarker();
    public abstract String separator();
    public abstract BigDecimal rate();

    public static Builder builder() {
        return new AutoValue_ConversionItemModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder conversionId(long conversionId);
        public abstract Builder name(String name);
        public abstract Builder symbol(String symbol);
        public abstract Builder isoCode(String isoCode);
        public abstract Builder location(String location);
        public abstract Builder rate(BigDecimal decimalRate);
        public abstract Builder separator(String separator);
        public abstract Builder decimalMarker(String decimalMarker);
        public abstract Builder symbolAtStart(boolean symbolAtStart);
        public abstract ConversionItemModel build();
    }
}
