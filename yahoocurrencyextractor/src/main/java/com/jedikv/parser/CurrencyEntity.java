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
 * Last modified 09/07/17 14:56
 **************************************************************************************************/

package com.jedikv.parser;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kurian on 09/07/2017.
 */

public class CurrencyEntity {

  @SerializedName("priority")
  private int priority;
  @SerializedName("iso_code")
  private String isoCode;
  @SerializedName("name")
  private String name;
  @SerializedName("symbol")
  private String symbol;
  @SerializedName("subunit")
  private String subunits;
  @SerializedName("subunit_to_unit")
  private int subunitToUnits;
  @SerializedName("symbol_first")
  private boolean symbolFirst;
  @SerializedName("html_entity")
  private String htmlEntity;
  @SerializedName("decimal_mark")
  private String decimalMark;
  @SerializedName("thousands_separator")
  private String thousandsSeparator;
  @SerializedName("iso_numeric")
  private int isoNumeric;
  @SerializedName("smallest_denomination")
  private float smallestDenomination;
  @SerializedName("disambiguate_symbol")
  private String disambiguateSymbol;

  public CurrencyEntity(int priority, String isoCode, String name, String symbol, String subunits,
      int subunitToUnits, boolean symbolFirst, String htmlEntity, String decimalMark,
      String thousandsSeparator, int isoNumeric, float smallestDenomination,
      String disambiguateSymbol) {
    this.priority = priority;
    this.isoCode = isoCode;
    this.name = name;
    this.symbol = symbol;
    this.subunits = subunits;
    this.subunitToUnits = subunitToUnits;
    this.symbolFirst = symbolFirst;
    this.htmlEntity = htmlEntity;
    this.decimalMark = decimalMark;
    this.thousandsSeparator = thousandsSeparator;
    this.isoNumeric = isoNumeric;
    this.smallestDenomination = smallestDenomination;
    this.disambiguateSymbol = disambiguateSymbol;
  }

  public int getPriority() {
    return priority;
  }

  public String getIsoCode() {
    return isoCode;
  }

  public String getName() {
    return name;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getSubunits() {
    return subunits;
  }

  public int getSubunitToUnits() {
    return subunitToUnits;
  }

  public boolean isSymbolFirst() {
    return symbolFirst;
  }

  public String getHtmlEntity() {
    return htmlEntity;
  }

  public String getDecimalMark() {
    return decimalMark;
  }

  public String getThousandsSeparator() {
    return thousandsSeparator;
  }

  public int getIsoNumeric() {
    return isoNumeric;
  }

  public float getSmallestDenomination() {
    return smallestDenomination;
  }

  public String getDisambiguateSymbol() {
    return disambiguateSymbol;
  }
}
