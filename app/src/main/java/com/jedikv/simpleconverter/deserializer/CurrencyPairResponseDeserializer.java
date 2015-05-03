package com.jedikv.simpleconverter.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jedikv.simpleconverter.response.ExchangePairResponse;



import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hirondelle.date4j.DateTime;

/**
 * Created by Kurian on 02/05/2015.
 */
public class CurrencyPairResponseDeserializer implements JsonDeserializer<ExchangePairResponse> {

    private final DecimalFormat decimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));
    private final DateFormat dateFormat = new SimpleDateFormat("M/D/YYYY h:mma");

    @Override
    public ExchangePairResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();

        jsonObject.getAsJsonObject("query").get("created").getAsString();

        final String id = jsonObject.get("id").getAsString();
        final String pair = jsonObject.get("Name").getAsString();
        final BigDecimal rate = jsonObject.get("Rate").getAsBigDecimal();
        final String dateString = jsonObject.get("Date").getAsString();
        final String timeString = jsonObject.get("Time").getAsString();

        DateTime dateTime = new DateTime(dateString + " " + timeString);

        final ExchangePairResponse response = new ExchangePairResponse();
        final BigDecimal inRateConverter = rate.multiply(new BigDecimal(10000));

        response.setId(id);
        response.setDate(new Date());
        response.setRate(inRateConverter.intValue());



        return response;
    }
}
