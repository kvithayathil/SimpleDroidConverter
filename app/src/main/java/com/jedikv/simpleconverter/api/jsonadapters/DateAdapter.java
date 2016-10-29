package com.jedikv.simpleconverter.api.jsonadapters;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kurian on 29/10/2016.
 */

public class DateAdapter extends JsonAdapter<Date> {

    final SimpleDateFormat sdf;

    public DateAdapter(String format) {
        this.sdf = new SimpleDateFormat(format);
    }

    @Override
    public Date fromJson(JsonReader reader) throws IOException {
        try {
            return sdf.parse(reader.nextString());
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @Override
    public void toJson(JsonWriter writer, Date value) throws IOException {
        String output = sdf.format(value);
        writer.value(output);
    }
}
