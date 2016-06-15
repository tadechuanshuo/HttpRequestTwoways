package com.yunmall.ymsdk.net.callback;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * 基础网络请求回调
 * GSON_TYPE为BaseObject的子类泛型
 * @see com.yunmall.ymsdk.net.model.BaseObject
 * Created by Zhp on 2014/5/16.
 */
public interface ResponseCallback<GSON_TYPE> {

    /**
     * 请求开始
     */
    public void onStart();

    /**
     * 请求进度
     * @param bytesWritten 已写入
     * @param totalSize    总数
     */
    public void onProgress(int bytesWritten, int totalSize);

    /**
     * 请求成功
     * @param baseData 已解析的数据
     */
    public void onSuccess(GSON_TYPE baseData);

    /**
     * 请求失败
     * @param error    具体的错误
     * @param statusCode 0为断网，基类已提示，具体逻辑中不用再提示
     */
    public void onFailed(Throwable error, int statusCode);

    /**
     * 本地缓存获取成功
     * @param responseJsonType 本地缓存数据
     * @param time             缓存被修改的时间
     *                         用来判断该缓存是否有效
     */
    public void onCacheLoaded(GSON_TYPE responseJsonType, long time);

    /**
     * 请求完成，无论失败或者成功，都会调用该方法。
     */
    public void onFinish();

    /**
     * 如果需要Cache，则返回有效的文件路径
     * 如果为空，则表明不需要。默认为空
     * @return 缓存文件路径
     */
    public String getCacheFileName();

    /**
     * 需要提供Gson对象
     * 用于返回数据解析
     * @return Gson对象
     */
    public Gson getGson();

    /**
     * 返回需要解析的数据对象Class
     * @return 需要解析的数据对象Class
     */
    public Type getClazz();
}
