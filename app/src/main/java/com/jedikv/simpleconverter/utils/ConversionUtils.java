package com.jedikv.simpleconverter.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by KV_87 on 09/05/15.
 */
public class ConversionUtils {

    private static final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));


    private ConversionUtils() {
        Timber.tag(ConversionUtils.class.getSimpleName());
    }


    private static BigDecimal convertValues(final BigDecimal target, final BigDecimal rate) {

        return target.multiply(rate);
    }

    public static BigDecimal convertValues(final String inputValue, final int rate) {

        BigDecimal rateDecimal = new BigDecimal(rate).movePointLeft(4);
        Timber.d("Rate decimal: " + rateDecimal);
        BigDecimal inputDecimal = null;
        try {
             inputDecimal = (BigDecimal)mDecimalFormat.parse(inputValue);
        } catch (ParseException e) {
            e.printStackTrace();
            Timber.e(e, e.getMessage());
            inputDecimal = new BigDecimal(0);
        }

        return convertValues(inputDecimal, rateDecimal);
    }

}
