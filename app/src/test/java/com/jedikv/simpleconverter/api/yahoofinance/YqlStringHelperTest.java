/***************************************************************************************************
 * The MIT License (MIT)
 * Copyright (c) 2017 Kurian
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Last modified 13/07/17 19:12
 **************************************************************************************************/

package com.jedikv.simpleconverter.api.yahoofinance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Kurian on 13/07/2017.
 */
public class YqlStringHelperTest {

  private YqlStringHelper yqlStringHelper;

  @Before
  public void setUp() {
    this.yqlStringHelper = spy(new YqlStringHelper());
  }

  @After
  public void tearDown() {
    yqlStringHelper = null;
  }

  @Test
  public void createReverseFromPairsReturnsExpectedList() throws Exception {
    List<String> targetCurrencies = Arrays.asList("USD", "GBP");
    String sourceCurrency = "CAD";
    List<String> result = yqlStringHelper.createReverseFromPairs(targetCurrencies, sourceCurrency);
    assertEquals(4, result.size());
  }

  @Test
  public void createReverseFromPairsReturnsExpectedEmptyList() throws Exception {
    List<String> result =
        yqlStringHelper.createReverseFromPairs(Collections.<String>emptyList(), "CAD");
    assertTrue(result.isEmpty());
  }

  @Test
  public void generateYQLCurrencyQueryInvokesCreateReverseFromPairs() throws Exception {
    List<String> pairList = Arrays.asList("USDGBP");
    yqlStringHelper.generateYQLCurrencyQuery(pairList, "GBP");
    verify(yqlStringHelper).createReverseFromPairs(eq(pairList), eq("GBP"));
  }

  @Test
  public void generateYQLCurrencyQueryReturnsExpectedYQLStatement() throws Exception {
    List<String> pairList = Arrays.asList("USDGBP","GBPUSD");
    doReturn(pairList).when(yqlStringHelper)
        .createReverseFromPairs(anyListOf(String.class), anyString());
    String result = yqlStringHelper.generateYQLCurrencyQuery(pairList, "GBP");
    String expected = "select * from yahoo.finance.xchange where pair in (\"USDGBP\",\"GBPUSD\")";
    assertEquals(expected, result);
  }

  @Test
  public void generateYQLCurrencyQueryReturnsExpectedYQLStatementWithEmptyInput() throws Exception {
    List<String> pairList = Collections.emptyList();
    doReturn(pairList).when(yqlStringHelper)
        .createReverseFromPairs(anyListOf(String.class), anyString());
    String result = yqlStringHelper.generateYQLCurrencyQuery(pairList, "GBP");
    String expected = "select * from yahoo.finance.xchange where pair in ()";
    assertEquals(expected, result);
  }
}