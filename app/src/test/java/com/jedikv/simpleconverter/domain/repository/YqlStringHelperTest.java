package com.jedikv.simpleconverter.domain.repository;

import com.jedikv.simpleconverter.api.yahoofinance.YqlStringHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kurian on 31/10/2016.
 */
public class YqlStringHelperTest {

    @Test
    public void compareOutputOfGeneratedYqlStatement() {

        YqlStringHelper yqlStringHelper = new YqlStringHelper();

        String expected = "select * from yahoo.finance.xchange where pair in " +
                "(" +
                "\"MXNUSD\",\"USDMXN\"," +
                "\"CHFUSD\",\"USDCHF\"" +
                ")";

        List<String> targetCurrencies = new ArrayList<>(2);
        targetCurrencies.add("MXN");
        targetCurrencies.add("CHF");
        String sourceCurrency = "USD";

        String output = yqlStringHelper.generateYQLCurrencyQuery(targetCurrencies, sourceCurrency);
        Assert.assertEquals(expected, output);
    }

}