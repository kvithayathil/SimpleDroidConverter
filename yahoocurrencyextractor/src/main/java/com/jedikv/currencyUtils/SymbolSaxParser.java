package com.jedikv.currencyUtils;

import com.google.common.escape.CharEscaperBuilder;
import com.google.common.escape.UnicodeEscaper;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import sun.nio.cs.UnicodeEncoder;


/**
 * Created by Kurian on 03/06/2015.
 */
public class SymbolSaxParser extends DefaultHandler {

    private final String symbolPath = "../yahoocurrencyextractor/resources/symbol_list.xml";

    private String tempValue;
    private String currentCurrencyCode;
    private String currentHexCode;
    private String currentDecCode;
    private Map<String, String> symbolMap;

    private Charset utf8Charset = Charset.forName("UTF-8");
    private Charset utf16Charset = Charset.forName("UTF-16");

    public SymbolSaxParser() {
        super();

        symbolMap = new HashMap<>();
        parseDocument();

    }

    private void parseDocument() {

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(symbolPath, this);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getCurrencyUnicodeSymbol(String currencyCode) {
        return symbolMap.get(currencyCode);

    }

    public boolean hasUnicodeSymbol(String currencyCode) {

        return symbolMap.containsKey(currencyCode);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(qName.equalsIgnoreCase("entry")) {

            currentCurrencyCode = attributes.getValue("code");
            currentHexCode = attributes.getValue("unicode-hex");
            currentDecCode = attributes.getValue("unicode-decimal");

            System.out.println("Currency code: " + currentCurrencyCode + " - " + currentDecCode);
        }
    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        tempValue = new String(chars, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(qName.equalsIgnoreCase("entry")) {
            //Reached the end of the element so add to the currency symbol map.
            symbolMap.put(currentCurrencyCode, convertStringToUnicode(currentDecCode));

            System.out.println("Symbol map size: " + symbolMap.size());
        }
    }


    private String convertStringToUnicode(String unicodeString) {

        Scanner scanner = new Scanner(unicodeString);
        scanner.useDelimiter(", *");

        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNext()) {

            String code = scanner.next();

            char[] symbolChar = Character.toChars(Integer.parseInt(code));
            stringBuilder.append(symbolChar);
        }

        scanner.close();

        System.out.println(stringBuilder.toString());

        return stringBuilder.toString();
    }
}
