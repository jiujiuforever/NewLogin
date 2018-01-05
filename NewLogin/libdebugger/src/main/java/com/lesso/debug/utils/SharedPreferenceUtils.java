package com.lesso.debug.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * @Author seewhy
 * @Date 2017/4/20
 */

public class SharedPreferenceUtils {

    public static final String FILE_NAME = "debugger_config";
    public static final String KEY_FLOAT_WINDOW_FLAG = "KEY_FLOAT_WINDOW_FLAG";
    public static final String KEY_SHAKE_FLAG = "KEY_SHAKE_FLAG";

    /** 获取悬浮框是否设置 **/
    public static void setFloatWindowFlag(Context context, boolean value) {
        putBoolean(context, FILE_NAME, KEY_FLOAT_WINDOW_FLAG, value);
    }

    public static boolean getFloatWindowFlag(Context context) {
        return getBoolean(context, FILE_NAME, KEY_FLOAT_WINDOW_FLAG, true);
    }

    /** 获取手势摇动 **/
    public static void setKeyShakeFlag(Context context, boolean value) {
        putBoolean(context, FILE_NAME, KEY_SHAKE_FLAG, value);
    }

    public static boolean getKeyShakeFlag(Context context) {
        return getBoolean(context, FILE_NAME, KEY_SHAKE_FLAG, true);
    }

    // ----------------------------------------------------------------
    // common method
    // ----------------------------------------------------------------
    public static int getInt(Context context, String preferencesName, String key, int defValue) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        return sp.getInt(key, defValue);
    }

    public static void putInt(Context context, String preferencesName, String key, int value) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        sp.edit().putInt(key, value).apply();
    }

    public static long getLong(Context context, String preferencesName, String key, long defValue) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        return sp.getLong(key, defValue);
    }

    public static void putLong(Context context, String preferencesName, String key, long value) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        sp.edit().putLong(key, value).apply();
    }

    public static float getFloat(Context context, String preferencesName, String key, float defValue) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        return sp.getFloat(key, defValue);
    }

    public static void putFloat(Context context, String preferencesName, String key, float value) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        sp.edit().putFloat(key, value).apply();
    }

    public static String getString(Context context, String preferencesName, String key, String defValue) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        return sp.getString(key, defValue);
    }

    public static void putString(Context context, String preferencesName, String key, String value) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        sp.edit().putString(key, value).apply();
    }

    public static boolean getBoolean(Context context, String preferencesName, String key, boolean defValue) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        return sp.getBoolean(key, defValue);
    }

    public static void putBoolean(Context context, String preferencesName, String key, boolean value) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void removeKey(Context context, String preferencesName, String key) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        sp.edit().remove(key).apply();
    }

    public static boolean containsKey(Context context, String preferencesName, String key) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        return sp.contains(key);
    }

    public static void clearFile(Context context, String preferencesName) {
        SharedPreferences sp = getSharedPreferences(context, preferencesName);
        sp.edit().clear().apply();
    }

    private static SharedPreferences getSharedPreferences(Context context, String sharedPreferencesName) {
        return context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
    }
}
