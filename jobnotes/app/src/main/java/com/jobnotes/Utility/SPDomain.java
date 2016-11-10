package com.jobnotes.Utility;

import android.content.Context;

public class SPDomain {
    public static final String DOMAIN_NAME = "domain_name";
    public static final String SHORTCUT = "shortcut_created";

    private static final String SP = "domain";

    public static String getString(Context context, String key) {

        return context.getSharedPreferences(SP, Context.MODE_PRIVATE)
                .getString(key, "");
    }

    public static void setString(Context context, String key, String value) {

        context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
                .putString(key, value).commit();
    }

    public static boolean getBooleanValue(Context context, String key) {

        return context.getSharedPreferences(SP, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }

    public static void setBooleanValue(Context context, String key,
                                       boolean value) {

        context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
                .putBoolean(key, value).commit();
    }

    public static long getLong(Context context, String key) {

        return context.getSharedPreferences(SP, Context.MODE_PRIVATE)
                .getLong(key, 0);
    }

    public static void setLong(Context context, String key,
                               long value) {

        context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
                .putLong(key, value).commit();
    }

    public static int getInt(Context context, String key) {

        return context.getSharedPreferences(SP, Context.MODE_PRIVATE)
                .getInt(key, 0);
    }

    public static void setInt(Context context, String key,
                              int value) {

        context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
                .putInt(key, value).commit();
    }

    public static void clear(Context context) {
        context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit().clear().commit();
    }
}
