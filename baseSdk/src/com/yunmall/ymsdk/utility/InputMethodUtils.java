package com.yunmall.ymsdk.utility;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtils {
    
    // 打开关闭切换
    public static void showSoftInputMethod(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            
        }
    }
    
    public static void setSoftInputMode(Activity context, int flag) {
        context.getWindow().setSoftInputMode(flag);
    }
    
    public static void hideSoftInputMethod(Activity context,View view) {
            InputMethodManager inputmanger = (InputMethodManager)context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    
    public static void showSoftInputMethod(Activity context,IBinder binder) {
        InputMethodManager iMM = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        iMM.showSoftInputFromInputMethod(binder, 0);
    }
    public static void showSoftInputMethod(Activity context,View view) {
        InputMethodManager iMM = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        iMM.showSoftInput(view, 0);
    }
    
    public static void hideSoftInputMethod(Context context, IBinder binder) {
        InputMethodManager imm = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(binder, 0);
        }
    }
    
    // added by zhaochangqing in 2014/03/21
    public static void hideSoftInputMethod(Context context, IBinder binder, View view) {
        InputMethodManager imm = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            view.scrollBy(0, -80);
            imm.hideSoftInputFromWindow(binder, 0);
        }
    }
    
    public static boolean isSoftKeyboardActive(Activity context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.isActive(view);
    }
    
    public static boolean isSoftKeyboardActive(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.isActive();
    }
    
}
