package com.jedikv.simpleconverter.domain;

import junit.framework.Assert;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by Kurian on 30/10/2016.
 */

public class ConversionTest {

    @Test
    public void testDecimalToInteger() {
        Assert.assertEquals(9872, new BigDecimal("0.9872").movePointRight(4).intValue());
        Assert.assertEquals(189500, new BigDecimal("18.9500").movePointRight(4).intValue());
    }
}
