package com.yd.yourdoctorandroid.utils;

import android.content.Context;
import android.content.res.Resources;

public class Utils {
    public static String getStringResourceByString(Context context, String name) {
        try {
            Resources res = context.getResources();
            return res.getString(res.getIdentifier(name, "string", context.getPackageName()));
        } catch (Exception ex) {
            return "";
        }
    }
}
