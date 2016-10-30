package com.jedikv.simpleconverter.api.yahoofinance;

import com.jedikv.simpleconverter.api.ResponseResourceReaderHelper;
import com.jedikv.simpleconverter.api.jsonadapters.DateAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import testutils.ResourcePaths;

/**
 * Created by Kurian on 30/10/2016.
 */
public class YahooJsonParserTest {

    private Moshi moshi;

    @Before
    public void setUp() {
        moshi = new Moshi
                .Builder()
                .add(new DateAdapter("yyyy-MM-dd'T'HH:mm:ss'Z'"))
                .build();

    }

    @Test
    public void testDecimalToInteger() {
        Assert.assertEquals(9872, new BigDecimal("0.9872").movePointRight(4).intValue());
        Assert.assertEquals(189500, new BigDecimal("18.9500").movePointRight(4).intValue());
    }

    /**
     * Ensuring that Moshi can parse a successful JSON response from the Yahoo API
     */
    @Test
    public void testSuccessMessageParsing() {

        final InputStream in = this.getClass()
                .getClassLoader()
                .getResourceAsStream(ResourcePaths.YAHOO_USD_MXD_200);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            String stringResponse = ResponseResourceReaderHelper.readResourceFile(in);
            JsonAdapter<YahooDataContainerResponse> adapter
                    = moshi.adapter(YahooDataContainerResponse.class);

            Date targetDate = sdf.parse("2016-10-30T16:08:09Z");

            YahooDataContainerResponse response = adapter.fromJson(stringResponse);
            Assert.assertNotNull(response);
            Assert.assertEquals(response.query.count, 2);
            Assert.assertEquals(targetDate.getTime(), response.query.created.getTime());
            Assert.assertEquals(response.query.count, response.query.results.rate.size());

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}
