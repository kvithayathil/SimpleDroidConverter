package com.jedikv.simpleconverter.api.jsonadapters;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.ToJson;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kurian on 29/10/2016.
 */

public class DateAdapter {

    @VisibleForTesting
    private final DateFormat df;

    public DateAdapter(@NonNull DateFormat dateFormat) {
        this.df = dateFormat;
    }

    @FromJson
    public Date fromJson(JsonReader reader) throws IOException {
        try {
            return df.parse(reader.nextString());
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @ToJson
    public void toJson(JsonWriter writer, Date value) throws IOException {
        String output = df.format(value);
        writer.value(output);
    }
}
