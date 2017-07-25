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
 * Last modified 25/07/17 15:20
 **************************************************************************************************/

package com.jedikv.simpleconverter.api.jsonadapters;

import com.squareup.moshi.JsonReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Kurian on 25/07/2017.
 */
public class DateAdapterTest {


  @Test
  public void formatJsonToCorrectDateValue() throws Exception {
    DateAdapter adapter = createDateAdapter("yyyy-MM-dd'T'HH:mm:ss'Z'");
    JsonReader mockJsonReader = mock(JsonReader.class);
    doReturn("2017-01-01T11:00:00Z").when(mockJsonReader).nextString();
    Date result = adapter.fromJson(mockJsonReader);
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    cal.set(2017, 0, 1, 11, 0, 0);
    assertEquals(df.format(cal.getTime()), df.format(result));
  }


  private DateAdapter createDateAdapter(String format) {
    return new DateAdapter(new SimpleDateFormat(format));
  }

}