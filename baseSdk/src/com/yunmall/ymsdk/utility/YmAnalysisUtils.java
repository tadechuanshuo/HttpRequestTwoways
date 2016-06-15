package com.yunmall.ymsdk.utility;

import java.util.HashMap;

import com.baidu.mobstat.StatService;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;


/**
 * 统计工具类
 * @author cuijie
 *
 */
public class YmAnalysisUtils {

    private static final boolean UMENG = false;

    private static final boolean BAIDU = true;

    /**
	 * 在activity中onResume方法调用的
	 * @param pageName(一般可以用activity.getclass.getname)
	 * @param context
	 */
	public static void onActivityResume(String pageName,Context context){

        if (UMENG) {
            MobclickAgent.onPageStart(pageName);
            MobclickAgent.onResume(context);
        }

        if (BAIDU) {
            StatService.onResume(context);
        }

    }
	
	/**
	 * 在activity中onPause方法调用的
	 * @param pageName
	 * @param context
	 */
	public static void onActivityPause(String pageName,Context context){
        if (UMENG) {
            MobclickAgent.onPageEnd(pageName);
            MobclickAgent.onPause(context);
        }

        if (BAIDU) {
            StatService.onPause(context);
        }
    }
	
	/**
	 * 在FragmentActivity的onResume方法调用 
	 * @param pageName
	 * @param context
	 */
	public static void onFragmentActivityResume(String pageName,Context context){
        if (UMENG) {
            MobclickAgent.onResume(context);
        }

        if (BAIDU) {
            StatService.onResume(context);
        }
    }
	
	/**
	 * 在FragmentActivity的onPause方法调用
	 * @param pageName
	 * @param context
	 */
	public static void onFragmentActivityPause(String pageName,Context context){
        if (UMENG) {
            MobclickAgent.onPause(context);
        }

        if (BAIDU) {
            StatService.onPause(context);
        }
    }
	
	/**
	 * Fragment中onResume方法
	 * @param pageName
	 * @param context 这个传入当前fragement的引用
	 */
	public static void onSingleFragmentResume(Context context, String pageName){
        if (UMENG) {
            MobclickAgent.onPageStart(pageName);
        }
        if (BAIDU) {
            StatService.onPageStart(context, pageName);
        }

    }
	
	/**
	 * Fragment中onPause方法
	 * @param pageName
	 * @param context
	 */
	public static void onSingleFragmentPause(Context context, String pageName){
        if (UMENG) {
            MobclickAgent.onPageEnd(pageName);
        }

        if (BAIDU) {
            StatService.onPageEnd(context, pageName);
        }
    }
	
	/**
	 * 百度统计特有 自定义页面或者fragment中嵌套的fragment
	 * @param pageName
	 */
	public static void nestFragmentAndCustomViewResume(String pageName,Context context){
        if (BAIDU) {
            StatService.onPageStart(context, pageName);
        }
    }
	
	/**
	 * 百度统计特有   自定义页面或者fragment中嵌套的fragment
	 * @param pageName
	 */
	public static void nestFragmentAndCustomViewPause(String pageName,Context context){
        if (BAIDU) {
            StatService.onPageEnd(context, pageName);
        }
    }
	
	/**
	 * 事件开始（与事件结束配对出现）
	 * @param context
	 * @param eventId
	 * @deprecated 不推荐使用，因为在百度统计中不支持没有lable的自定义事件，建议都统一用有lable的，在统计后台建立event的时候也建议添加lable
	 */
	public static void customEventStart(Context context, String eventId){
        if (UMENG) {
            MobclickAgent.onEventBegin(context, eventId);
        }
    }

	/**
	 * 事件结束（与事件开始配对出现）
	 * @param context
	 * @param eventId
	 * @deprecated 不推荐使用，因为在百度统计中不支持没有lable的自定义事件，建议都统一用有lable的，在统计后台建立event的时候也建议添加lable
	 */
	public static void customEventEnd(Context context, String eventId){
        if (UMENG) {
            MobclickAgent.onEventEnd(context, eventId);
        }
    }
	
	public static void customEventStartWithLable(Context context, String eventId,String lable){
        if (UMENG) {
            MobclickAgent.onEventBegin(context, eventId,lable);
        }
        if (BAIDU) {
            StatService.onEventStart(context, eventId, lable);
        }
    }
	
	public static void customEventEndWithLable(Context context, String eventId,String lable){
        if (UMENG) {
            MobclickAgent.onEventEnd(context, eventId,lable);
        }
        if (BAIDU) {
            StatService.onEventEnd(context, eventId, lable);
        }
    }
	
	/**
	 * @deprecated 不推荐使用，因为在百度统计中不支持没有lable的自定义事件，建议都统一用有lable的，在统计后台建立event的时候也建议添加lable
	 * @param context
	 * @param eventId
	 */
	public static void customEvent(Context context, String eventId){
        if (UMENG) {
            MobclickAgent.onEvent(context, eventId);
        }

    }
	
	public static void customEventWithLable(Context context,String eventId,String lable){
        if (UMENG) {
            MobclickAgent.onEvent(context, eventId, lable);
        }
        if (BAIDU) {
            StatService.onEvent(context, eventId, lable);
        }
    }
	
	/**
	 * 多属性统计  这个百度不支持
	 * @param context
	 * @param eventId
	 * @param map
	 */
	public static void customEventWtihKVValues(Context context,String eventId,HashMap<String,String> map){
        if (UMENG) {
            MobclickAgent.onEvent(context, eventId, map);
        }

    }
	
	/**
	 * 自定义事件使用时常的统计 （customEventStartWithLable customEventEndWithLable 是一个效果 不同之处是这个是自己传入使用时常）
	 * @param context
	 * @param eventId
	 * @param lable
	 * @param duration
	 */
	@SuppressWarnings("deprecation")
	public static void customEventDuration(Context context,String eventId,String lable ,long duration){
        if (UMENG) {
            MobclickAgent.onEventDuration(context, eventId, lable, duration);
        }
        if (BAIDU) {
            StatService.onEventDuration(context, eventId, lable, duration);
        }
    }
	
	
	
	
	
	
	
	
	
	

}
