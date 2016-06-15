package com.example.qiuqing.request;


import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yunmall.ymsdk.net.callback.ResponseCallback;
import com.yunmall.ymsdk.net.callback.ResponseCallbackImpl;

/**
 * 统一处理BaseResponse的callback,
 * 会在OnSuccess时处理未读消息等
 * Created by Zhp on 2014/6/18.
 */
@SuppressWarnings("unchecked")
public class ResponseCallbackBR extends ResponseCallbackImpl<BaseResponse>{

    private ResponseCallback responseCallback;
    private Context context;

    public ResponseCallbackBR(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }
    
    public ResponseCallbackBR(Context context, ResponseCallback responseCallback) {
    	this.context = context;
        this.responseCallback = responseCallback;
    }

    @Override
    public void onStart() {
        responseCallback.onStart();
    }

    @Override
    public void onSuccess(BaseResponse baseData) {
        if (baseData != null) {
        }
        responseCallback.onSuccess(baseData);
        responseCallback.onFinish();
    }

    @Override
    public void onFailed(Throwable error, int statusCode) {
        responseCallback.onFailed(error, statusCode);
        responseCallback.onFinish();
    }

    @Override
    public void onCacheLoaded(BaseResponse responseJsonType, long time) {
        responseCallback.onCacheLoaded(responseJsonType, time);
    }

    @Override
    public String getCacheFileName() {
        return responseCallback.getCacheFileName();
    }

    @Override
    public Type getClazz() {
        return responseCallback.getClazz();
    }
}
