package com.jedikv.simpleconverter.utils;

import android.os.Build;

/**
 * Created by Kurian on 16/05/2015.
 */
public class AndroidUtils {

    public static boolean isLollipop(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
