package com.jedikv.currencyUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import com.jedikv.parser.CurrencyEntity;
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

    //Taken from: https://github.com/RubyMoney/money/blob/master/config/currency_iso.json

    private static final String INPUT_PATH = "../yahoocurrencyextractor/resources/symbol_data.json";

    public SymbolDataParser() {
        currencyEntityMap = new HashMap<>();
        parseAndCreateMap();
    }

    private void parseAndCreateMap() {

        Gson gson = new GsonBuilder().create();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_PATH));
            List<CurrencyEntity> currencyEntityList =
                gson.fromJson(bufferedReader, new TypeToken<List<CurrencyEntity>>() {}.getType());

            for (CurrencyEntity entity : currencyEntityList) {
                currencyEntityMap.put(entity.getIsoCode(), entity);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isInMap(String code) {
        return currencyEntityMap.containsKey(code);
    }

    public String getSymbol(String code) {
        return currencyEntityMap.get(code).getSymbol();
    }

    public CurrencyEntity getCurreny(String code) {
        return currencyEntityMap.get(code);
    }
}
