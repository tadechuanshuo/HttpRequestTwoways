package com.yunmall.ymsdk.net.callback;

import com.google.gson.Gson;
import com.yunmall.ymsdk.net.GsonManager;
import com.yunmall.ymsdk.net.model.BaseObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 基础网络请求回调抽象类
 * 将具体的数据对象设置为泛型
 * 简化了class的传递过程
 * Created by Zhp on 2014/5/27.
 */
public abstract class ResponseCallbackImpl<GSON_TYPE> implements ResponseCallback<GSON_TYPE> {
    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {

    }

    @Override
    public abstract void onSuccess(GSON_TYPE baseData);

    @Override
    public abstract void onFailed(Throwable error, int statusCode);

    @Override
    public void onCacheLoaded(GSON_TYPE responseJsonType, long time) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public String getCacheFileName() {
        return null;
    }

    /**
     * 获取默认的Gson
     * 子类可以重写该方法返回自定义的Gson
     *
     * @return GsonManager.getGson()
     */
    @Override
    public Gson getGson() {
        return GsonManager.getGson();
    }

    /**
     * 通过反射获取包含具体泛型对象的Class<br>
     * 可以省去再次传递class的过程
     * 除非需要Gson解析的Class与GSON_TYPE不同，否则请不要重写此类
     * @return Type
     */
    @Override
    public Type getClazz() {
        try {
            ResponseCallbackImpl responseCallback = this;
            Type genericSuperclass = responseCallback.getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            if (type != null) {
                return type;
            } else {
                return BaseObject.class;
            }
        } catch (Exception e) {
            return BaseObject.class;
        }
    }

}
