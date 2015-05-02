package com.jedikv;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jedikv.currencyUtils.CurrencyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class RawCurrencyExtractor {

    private static String outputPath;

    public static void main (String [] args) {

        System.out.println(System.getProperty("java.version"));

        outputPath = args[0];

        readFile(args[1]);
    }

    private static void readFile(String inputPath) {

        File file = new File(inputPath);

        StringBuilder stringBuilder = new StringBuilder();
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

                    String countryCode = code.substring(0,2);
                    Locale locale = new Locale("", countryCode);

                    try {
                        Currency instance = Currency.getInstance(code);

                        System.out.print(locale.getDisplayCountry() + ": ");
                        System.out.print(instance.getDisplayName(locale) + " - ");
                        System.out.print(CurrencyUtils.getCurrencySymbol(code) + "\n");
                        stringBuilder.append(code + ",");

                        String symbol = CurrencyUtils.getCurrencySymbol(code);

                        if(symbol.equals("US$")) {
                            symbol = "$";
                        }
                        currencyItemList.add(new CurrencyItem(locale, instance, symbol));
                    } catch (IllegalArgumentException e) {
                        System.out.print("SKIPPED " + code + " " + locale.getDisplayCountry());
                    }
                }
            }

            if(!currencyItemList.isEmpty()) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
                writer.print(gson.toJson(currencyItemList));
                writer.close();
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }
}
