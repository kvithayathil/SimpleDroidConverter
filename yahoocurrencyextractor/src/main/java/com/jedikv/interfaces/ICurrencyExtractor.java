package com.jedikv.interfaces;

/**
 * Created by Kurian on 03/06/2015.
 */
public interface ICurrencyExtractor {

    /**
     * Read in an input file and produce an appropriate output
     * @param inputPath location of file to read in
     * @param outputPath location of generated file
     */
    void readAndExtract(String inputPath, String outputPath);
}
