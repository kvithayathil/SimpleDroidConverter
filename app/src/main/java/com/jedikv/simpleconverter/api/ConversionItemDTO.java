package com.jedikv.simpleconverter.api;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * Created by Kurian on 31/10/2016.
 */
@AutoValue
public abstract class ConversionItemDTO {

    public abstract String currencyId();
    public abstract String pairTo();
    public abstract int conversionRateAsInteger();
    @Nullable
    public abstract String source();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder currencyId(String currencyId);
        public abstract Builder pairTo(String currencyId);
        public abstract Builder conversionRateAsInteger(int conversionRate);
        public abstract Builder source(String source);
        public abstract ConversionItemDTO build();
    }

    public static Builder builder() {
        return new AutoValue_ConversionItemDTO.Builder();
    }
}
