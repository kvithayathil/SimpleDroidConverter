package com.jedikv.extractors;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jedikv.CurrencyItem;
import com.jedikv.currencyUtils.CurrencyUtils;
import com.jedikv.currencyUtils.SymbolDataParser;
import com.jedikv.currencyUtils.SymbolSaxParser;
import com.jedikv.interfaces.ICurrencyExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Kurian on 03/06/2015.
 */
public class YahooCurrencyCurrencyExtractor implements ICurrencyExtractor {

    private Set<String> eurozoneCodeSet;
    private SymbolSaxParser symbolParser;
    private SymbolDataParser symbolDataParser;

    private Set<String> missingSymbolSet;

    public YahooCurrencyCurrencyExtractor() {

        setUpEuroZoneSet();
        symbolParser = new SymbolSaxParser();
        symbolDataParser = new SymbolDataParser();
        missingSymbolSet = new HashSet<>();

    }

    /**
     * Pre-populate a hashset with the current Eurozone currency members
     */
    private void setUpEuroZoneSet () {

        eurozoneCodeSet = Sets.newHashSet(
                //19 Eurozone territories
                "AT","BE","CY","EE","FI","FR","DE","GR","IE","IT","LV","LT","LU","MT","NL","PT","SK","SI","ES"
        );


    }


    /**
     * Check if the currency is associated with a country in the Eurozone
     * @param currencyCode e.g. FRF, DEM etc..
     * @return true if the country code (FR, DE etc..) is present in the Eurozone set
     */
    private boolean isInEurozone(String currencyCode) {

        if(!Strings.isNullOrEmpty(currencyCode)) {

            String countryCode = currencyCode.substring(0,2);

            System.out.println("Euro Currency code: " + currencyCode + " Euro Country code: " + countryCode);

            if(eurozoneCodeSet.contains(currencyCode.substring(0,2).toUpperCase())) {
                System.out.println(countryCode + " is in the EUROZONE");
                return true;
            }
        }

        return false;
    }


    private CurrencyItem createCurrencyList(String currencyCode) {

        String countryCode = currencyCode.substring(0,2);
        Locale locale = new Locale("", countryCode);



        try {
            Currency instance = Currency.getInstance(currencyCode);

            System.out.print(locale.getDisplayCountry() + ": ");
            System.out.print(instance.getDisplayName(locale) + " - ");
            System.out.print(CurrencyUtils.getCurrencySymbol(currencyCode) + "\n");

            String symbol = CurrencyUtils.getCurrencySymbol(currencyCode);



            //Various currency symbol adjustments
            String currencyDisplayName = instance.getDisplayName(locale);

            if(currencyDisplayName.contains("Franc")) {
                symbol = "Fs";
            } else if(currencyDisplayName.contains("Pound")) {
                symbol = "£";
            } else if (currencyDisplayName.contains("Rupee")) {
                symbol = "Rs.";
            } else if (currencyDisplayName.contains("Dollar") && symbol.equals(currencyCode)) {

                System.out.println("Dollar - " + currencyCode + ": " + symbol);

                symbol = "$";
            }


            if(symbol.equals("US$")) {
                symbol = "$";
            }


            switch (currencyCode) {

                case "AMD" : {
                    symbol = "\u058F";
                    break;
                }

                case "AZN": {
                    symbol = "ман";
                    break;
                }

                case "AFN": {
                    symbol = "\u060b";
                    break;
                }

            }


            //Testing out the unicode conversion


            if(symbolParser.hasUnicodeSymbol(currencyCode)) {
                symbol = symbolParser.getCurrencyUnicodeSymbol(currencyCode);
            } else {
                System.out.println("MISSING SYMBOL: " + currencyCode);
                missingSymbolSet.add(currencyCode);
            }

            return new CurrencyItem(locale, instance, symbol);

        } catch (IllegalArgumentException e) {
            System.out.print("SKIPPED " + currencyCode + " " + locale.getDisplayCountry());
        }



        return null;

    }

    @Override
    public void readAndExtract(String inputPath, String outputPath) {

        File file = new File(inputPath);

        List<CurrencyItem> currencyItemList = new ArrayList<>();

        try {

            Scanner scanner = new Scanner(file);

            int lineNum = 0;

            while (scanner.hasNext()) {

                String currentLine = scanner.nextLine();
                lineNum++;

                if(currentLine.contains("USD/")) {

                    int index = currentLine.indexOf('/');
                    int end = currentLine.length();

                    //E.g. /USD",
                    String code = currentLine.substring(index + 1, end - 2);
                    System.out.print(code + " ");

                    //Exclude any countries in the Eurozone we don't want their old currencies
                    if(!isInEurozone(code)) {

                        CurrencyItem item = createCurrencyList(code);
                        SymbolDataParser.CurrencyEntity entity = symbolDataParser.getCurreny(code);
                        if(entity != null) {
                            item.setShowAtEnd(!entity.isSymbolFirst());
                            item.setDecimalmark(entity.getDecimalMark());
                            item.setThousandsSeparator(entity.getThousandsSeparator());
                        }

                        if(item != null) {
                            currencyItemList.add(item);
                        }
                    }
                }
            }

            if(!currencyItemList.isEmpty()) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
                writer.print(gson.toJson(currencyItemList));
                writer.close();
            }

            System.out.println(Arrays.toString(missingSymbolSet.toArray(new String[missingSymbolSet.size()])));

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }

}
