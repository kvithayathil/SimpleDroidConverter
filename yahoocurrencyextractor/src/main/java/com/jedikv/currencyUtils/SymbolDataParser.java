package com.jedikv.currencyUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kurian on 05/06/2015.
 */
public class SymbolDataParser {

    private final Map<String, CurrencyEntity> currencyEntityMap;

    //Taken from: https://github.com/RubyMoney/money/blob/03c2034e7674097d536f084f40c57770411550a9/config/currency_iso.json

    private final String inputPath = "../yahoocurrencyextractor/resources/symbol_data.json";

    private List<CurrencyEntity> currencyEntityList;

    public SymbolDataParser() {

        currencyEntityMap = new HashMap<>();
        parseAndCreateMap();
    }


    private void parseAndCreateMap() {

        Gson gson = new GsonBuilder().create();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputPath));
            currencyEntityList = gson.fromJson(bufferedReader, new TypeToken<List<CurrencyEntity>>(){}.getType());

            for(CurrencyEntity entity : currencyEntityList) {
                currencyEntityMap.put(entity.isoCode, entity);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public boolean isInMap(String code) {
        return currencyEntityMap.containsKey(code);
    }

    public String getSymbol(String code) {
        return currencyEntityMap.get(code).symbol;
    }

    public CurrencyEntity getCurreny(String code) {

        return currencyEntityMap.get(code);
    }


    public class CurrencyEntity {

        int priority;
        @SerializedName("iso_code")
        String isoCode;
        String name;
        String symbol;
        String subunits;
        @SerializedName("subunit_to_unit")
        int subunitToUnits;
        @SerializedName("symbol_first")
        boolean symbolFirst;
        @SerializedName("html_entity")
        String htmlEntity;
        @SerializedName("decimal_mark")
        String decimalMark;
        @SerializedName("thousands_separator")
        String thousandsSeparator;
        @SerializedName("iso_numeric")
        int isoNumeric;
        @SerializedName("smallest_denomination")
        float smallestDenomination;


        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getIsoCode() {
            return isoCode;
        }

        public void setIsoCode(String isoCode) {
            this.isoCode = isoCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getSubunits() {
            return subunits;
        }

        public void setSubunits(String subunits) {
            this.subunits = subunits;
        }

        public int getSubunitToUnits() {
            return subunitToUnits;
        }

        public void setSubunitToUnits(int subunitToUnits) {
            this.subunitToUnits = subunitToUnits;
        }

        public boolean isSymbolFirst() {
            return symbolFirst;
        }

        public void setSymbolFirst(boolean symbolFirst) {
            this.symbolFirst = symbolFirst;
        }

        public String getHtmlEntity() {
            return htmlEntity;
        }

        public void setHtmlEntity(String htmlEntity) {
            this.htmlEntity = htmlEntity;
        }

        public String getDecimalMark() {
            return decimalMark;
        }

        public void setDecimalMark(String decimalMark) {
            this.decimalMark = decimalMark;
        }

        public String getThousandsSeparator() {
            return thousandsSeparator;
        }

        public void setThousandsSeparator(String thousandsSeparator) {
            this.thousandsSeparator = thousandsSeparator;
        }

        public int getIsoNumeric() {
            return isoNumeric;
        }

        public void setIsoNumeric(int isoNumeric) {
            this.isoNumeric = isoNumeric;
        }

        public float getSmallestDenomination() {
            return smallestDenomination;
        }

        public void setSmallestDenomination(float smallestDenomination) {
            this.smallestDenomination = smallestDenomination;
        }
    }


}
