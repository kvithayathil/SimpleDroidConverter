package com.jedikv.simpleconverter.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Kurian on 30/10/2016.
 */

public class ResponseResourceReaderHelper {

    public static String readResourceFile(InputStream in) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String newLine = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        String line;
        boolean flag = false;

        while ((line = reader.readLine()) != null) {
            result.append(flag ? newLine : "").append(line);
            flag = true;
        }

        in.close();
        reader.close();

        return result.toString();
    }
}
