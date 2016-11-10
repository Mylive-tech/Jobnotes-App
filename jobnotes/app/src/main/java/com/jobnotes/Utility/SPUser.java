package com.jobnotes.Utility;

import android.content.Context;

public class SPUser {
    public static final String USER_ID = "user_id";
    public static final String PROPERTIES = "properties";
    public static final String REPORTS = "reports";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_password";
    private static final String SP = "user";
    public static final String REPORT_INFO = "report_info";
    public static final String PREVIOUS_TIME = "previous_time";
    public static final String SCROLL_POSITION = "scroll_position";

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
