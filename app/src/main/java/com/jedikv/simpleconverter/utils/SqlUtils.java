package com.jedikv.simpleconverter.utils;

/**
 * Created by Kurian on 14/08/2016.
 */
public class SqlUtils {

    private SqlUtils() {
    }

    public static String generateListPlaceholder(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}
