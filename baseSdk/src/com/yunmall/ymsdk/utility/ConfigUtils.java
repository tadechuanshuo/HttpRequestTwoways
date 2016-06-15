package com.yunmall.ymsdk.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置文件读写工具类
 * @author zhp
 *
 */
public final class ConfigUtils {

    private ConfigUtils() {
    }

    private static SharedPreferences sp;

    /**
     * 初始化SharedPreferences对象
     *
     * @param ctx
     * @param packageName
     */
    public static void initPreferences(Context ctx, String packageName) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sp;
    }

    /**
     * 务必在initPreferences方法调用之后执行，否则返回false
     *
     * @return
     */
    public static boolean putInt(String key, int i) {
        boolean rst = false;
        try {
            sp.edit().putInt(key, i).commit();
            rst = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return rst;
    }

    /**
     * 务必在initPreferences方法调用之后执行，否则返回false
     *
     * @return
     */
    public static boolean putFloat(String key, float f) {
        boolean rst = false;
        try {
            sp.edit().putFloat(key, f).commit();
            rst = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return rst;
    }

    /**
     * 务必在initPreferences方法调用之后执行，否则返回false
     *
     * @return
     */
    public static boolean putLong(String key, long l) {
        boolean rst = false;
        try {
            sp.edit().putLong(key, l).commit();
            rst = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return rst;
    }

    /**
     * 务必在initPreferences方法调用之后执行，否则返回false
     *
     * @return
     */
    public static boolean putBoolean(String key, boolean b) {
        boolean rst = false;
        try {
            sp.edit().putBoolean(key, b).commit();
            rst = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }

    /**
     * 务必在initPreferences方法调用之后执行，否则返回false
     *
     * @return
     */
    public static boolean putString(String key, String s) {
        boolean rst = false;
        try {
            sp.edit().putString(key, s).commit();
            rst = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }

    /**
     * 默认值为0
     *
     * @return
     */
    public static int getInt(String key) {
        int r = 0;
        try {
            r = sp.getInt(key, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public static int getInt(String key, int defaultValue) {
        int r = 0;
        try {
            r = sp.getInt(key, defaultValue);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 默认值为0
     *
     * @return
     */
    public static float getFloat(String key) {
        float r = 0f;
        try {
            r = sp.getFloat(key, 0f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
    /**
     * 默认值为0
     *
     * @return
     */
    public static long getLong(String key) {
        long r = 0L;
        try {
            r = sp.getLong(key, 0L);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return r;
    }

    /**
     * 默认值为0
     *
     * @return
     */
    public static long getLong(String key, long defaultValue) {
        long r = 0L;
        try {
            r = sp.getLong(key, defaultValue);
        }
        catch (Exception e) {
//          e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return r;
    }
    
    /**
     *
     * @return
     */
    public static boolean getBoolean(String key, boolean b) {
        boolean r = false;
        try {
            r = sp.getBoolean(key, b);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return r;
    }

    /**
     * 默认值为false
     *
     * @return
     */
    public static boolean getBoolean(String key) {
        boolean r = false;
        try {
            r = sp.getBoolean(key, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return r;
    }

    /**
     * 默认值为false
     *
     * @return
     */
    public static boolean getBool(String key, boolean defaultValue) {
        boolean r = false;
        try {
            r = sp.getBoolean(key, defaultValue);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return r;
    }

    /**
     * 默认值为""
     *
     * @return
     */
    public static String getString(String key) {
        String r = "";
        try {
            r = sp.getString(key, "");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return r;
    }

    /**
     * 自定义默认值
     *
     * @return
     */
    public static String getString(String key, String defaultValue) {
        String r = "";
        try {
            r = sp.getString(key, defaultValue);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Preferences.class.notifyAll();
        return r;
    }
    
    public static void remove(String key) {
        try {
            sp.edit().remove(key).commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear() {
        try {
            sp.edit().clear().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
