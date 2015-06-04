package com.jedikv;

import com.jedikv.extractors.YahooCurrencyCurrencyExtractor;
import com.jedikv.interfaces.ICurrencyExtractor;

public class RawCurrencyExtractor {

    private static String outputPath;

    public static void main (String [] args) {

        System.out.println(System.getProperty("java.version"));
        init(args[1], args[0]);
    }

    /**
     * Entry point to start the extractor process
     * @param inputPath path to read currency data from
     * @param outputPath path to output newly created currency file
     */
    private static void init(String inputPath, String outputPath) {
        ICurrencyExtractor currencyExtractor = new YahooCurrencyCurrencyExtractor();

        currencyExtractor.readAndExtract(inputPath, outputPath);

    }

}
