package com.jedikv.simpleconverter.utils;

import android.content.Context;
import android.os.Build;

import timber.log.Timber;

/**
 * Created by Kurian on 16/05/2015.
 */
public class AndroidUtils {

    private AndroidUtils() {
        Timber.tag(AndroidUtils.class.getSimpleName());
    }

    public static boolean isMinLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isMinKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isMinJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isMinJellyBeanMROne() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isMinJellyBeanMRTwo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isMinICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isMinICSMROne() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    public static boolean isMinGingerBread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean isMinGingerBreadMROne() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
    }

    public static boolean isTargetGingerBread() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1;
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
