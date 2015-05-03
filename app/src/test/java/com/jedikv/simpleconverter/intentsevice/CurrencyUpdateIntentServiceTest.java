package com.jedikv.simpleconverter.intentsevice;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Kurian on 03/05/2015.
 */
public class CurrencyUpdateIntentServiceTest {


    private String generateYQLCurrencyQuery(List<String> currencyPairs) {

        //select * from yahoo.finance.xchange where pair in ("USDMXN", "USDCHF")

        StringBuilder sb = new StringBuilder("select * from yahoo.finance.xchange where pair in ");
        sb.append("(");

        int count = 1;

        for (String currencyPair : currencyPairs) {

            sb.append("\"").append(currencyPair).append("\"");

            //Check if it's not the last entry in the list
            if(count < currencyPairs.size()) {
                sb.append(",");
            }

            count++;
        }

        sb.append(")");

        return sb.toString();
    }
}