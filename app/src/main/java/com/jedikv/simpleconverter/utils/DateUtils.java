package com.jedikv.simpleconverter.utils;

import timber.log.Timber;

/**
 * Created by KV_87 on 27/09/2015.
 */
public class DateUtils {

    public static final int ONE_SECOND = 1000;
    public static final int ONE_MINUTE = ONE_SECOND * 60;
    public static final int ONE_HOUR = ONE_MINUTE * 60;
    public static final int ONE_DAY = ONE_HOUR * 24;

    private DateUtils() {
        Timber.tag(DateUtils.class.getSimpleName());
    }

    public static long getPrevious24HoursTimestamp() {
        return Math.abs(System.currentTimeMillis() - ONE_DAY);
    }
}
