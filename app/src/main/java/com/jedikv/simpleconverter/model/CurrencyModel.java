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
 * Last modified 25/07/17 18:50
 **************************************************************************************************/

package com.jedikv.simpleconverter.model;

import com.google.auto.value.AutoValue;

/**
 * Created by Kurian on 25/07/2017.
 */
@AutoValue
public abstract class CurrencyModel {

  public abstract int numericCode();
  public abstract String symbol();
  public abstract String name();
  public abstract String countryName();
  public abstract String code();
  public abstract boolean showAtEnd();
  public abstract String decimalMark();
  public abstract String thousandsSeparator();

  public static Builder builder() {
    return new AutoValue_CurrencyModel.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder numericCode(int numericCode);
    public abstract Builder symbol(String symbol);
    public abstract Builder name(String name);
    public abstract Builder countryName(String countryName);
    public abstract Builder code(String code);
    public abstract Builder showAtEnd(boolean showAtEnd);
    public abstract Builder decimalMark(String decimalMark);
    public abstract Builder thousandsSeparator(String thousandsSeparator);
    public abstract CurrencyModel build();
  }
}
