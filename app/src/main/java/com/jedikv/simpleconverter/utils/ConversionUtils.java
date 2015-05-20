package com.jedikv.simpleconverter.utils;

import android.content.Context;

/**
 * Created by KV_87 on 09/05/15.
 */
public class ConversionUtils {

    private ConversionUtils() {

    }

    public static int getDrawableResId(Context context, String filenameNoExtension) {

        return context.getResources().getIdentifier(filenameNoExtension, "drawable", context.getPackageName());
    }

}
