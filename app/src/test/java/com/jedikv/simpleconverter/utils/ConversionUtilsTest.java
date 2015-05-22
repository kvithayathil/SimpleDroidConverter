package com.jedikv.simpleconverter.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kurian on 22/05/2015.
 */
public class ConversionUtilsTest {

    private int resId;

    @Before
    public void setUp() throws Exception {
        resId = 1;
    }

    @Test
    public void testGetDrawableResId() throws Exception {

        assertEquals(resId, 1);
    }
}