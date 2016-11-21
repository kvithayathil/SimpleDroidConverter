package com.jedikv.simpleconverter.ui.model;

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
    public abstract BigDecimal rate();

    static Builder builder() {
        return new AutoValue_ConversionItemModel.Builder();
    }

    @AutoValue.Builder
    static abstract class Builder {
        abstract Builder conversionId(long conversionId);
        abstract Builder name(String name);
        abstract Builder symbol(String symbol);
        abstract Builder isoCode(String isoCode);
        abstract Builder location(String location);
        abstract Builder rate(BigDecimal decimalRate);
        abstract ConversionItemModel build();
    }
}
