package com.jedikv.simpleconverter.utils;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import timber.log.Timber;

import static org.junit.Assert.*;

/**
 * Created by Kurian on 22/05/2015.
 */
public class ConversionUtilsTest {

    private int resId;
    private DecimalFormat mDecimalFormat;

    @Before
    public void setUp() throws Exception {
        resId = 1;
        mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));
    }

    @Test
    public void testGetDrawableResId() throws Exception {

        assertEquals(resId, 1);
    }



    @Test
    public void testConvertToString() throws Exception{

        String inputValue = "10.000";

        int rate = 6521;

        BigDecimal resultExpected = new BigDecimal(6.521);
        resultExpected = resultExpected.setScale(3, BigDecimal.ROUND_UP);
        BigDecimal resultActual = convertValues(inputValue, rate);

        System.out.println("ResultActual: " + resultActual.toPlainString() + " ResultExpected: " + resultExpected.toPlainString());

        assertEquals(resultExpected.compareTo(resultActual), 0);
    }

    public BigDecimal convertValues(final String inputValue, final int rate) {

        BigDecimal rateDecimal = new BigDecimal(rate).movePointLeft(4);
        System.out.println("Rate decimal: " + rateDecimal);
        BigDecimal inputDecimal = new BigDecimal(inputValue);

        return convertValues(inputDecimal, rateDecimal);
    }

    private BigDecimal convertValues(final BigDecimal target, final BigDecimal rate) {

        return target.multiply(rate);
    }

}