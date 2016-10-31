package com.jedikv.simpleconverter.domain;

import junit.framework.Assert;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by Kurian on 30/10/2016.
 */

public class ConversionTest {

    @Test
    public void convertDecimalValueToInteger() {
        Assert.assertEquals(9872, new BigDecimal("0.9872").movePointRight(4).intValue());
        Assert.assertEquals(189500, new BigDecimal("18.9500").movePointRight(4).intValue());
    }

    @Test
    public void multiplyByConversionRateAndCheckResultsAsString() {
        int startValue = 9872 * 10;
        Assert.assertEquals(98720, startValue);
        BigDecimal decimal = new BigDecimal(startValue);
        BigDecimal result = decimal.movePointLeft(4);
        Assert.assertEquals(result.stripTrailingZeros().toString(), "9.872");
    }
}
