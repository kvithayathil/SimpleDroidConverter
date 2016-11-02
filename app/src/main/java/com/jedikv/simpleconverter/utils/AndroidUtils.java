package com.jedikv.simpleconverter.utils;

import android.content.Context;
import android.os.Build;

import timber.log.Timber;

/**
 * Created by Kurian on 16/05/2015.
 */
public class AndroidUtils {

    private AndroidUtils() {
        Timber.tag(AndroidUtils.class.getCanonicalName());
    }

    public static int getDrawableResId(Context context, String filenameNoExtension) {
        Timber.d("Flag name extension: " + filenameNoExtension);
        return context.getResources().getIdentifier(filenameNoExtension, "drawable", context.getPackageName());
    }

    public static int getDrawableResIdByCountryCode(Context context, String countryCode) {
        return getDrawableResId(context, countryCode.toLowerCase() + "_");
    }

    public static int getDrawableResIdByCurrencyCode(Context context, String currencyCode) {
        return getDrawableResIdByCountryCode(context, currencyCode.substring(0, 2));
    }

}
