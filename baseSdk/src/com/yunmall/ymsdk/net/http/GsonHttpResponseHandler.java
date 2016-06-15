package com.yunmall.ymsdk.net.http;

import com.yunmall.ymsdk.net.callback.ResponseCallback;
import com.yunmall.ymsdk.net.model.BaseObject;

import org.apache.http.Header;

/**
 * Gson回调处理类
 * Created by Zhp on 2014/5/16.
 */
public class GsonHttpResponseHandler extends BaseJsonHttpResponseHandler<BaseObject> {

    private ResponseCallback mResponseCallback;

    public GsonHttpResponseHandler(ResponseCallback responseCallback) {
        this.mResponseCallback = responseCallback;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseObject response) {
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, BaseObject errorResponse) {

    }

    @Override
    public void onCacheLoad(BaseObject responseJsonType, long time) {
    }

    @Override
    protected BaseObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        return (BaseObject) mResponseCallback.getGson().fromJson(rawJsonData, mResponseCallback.getClazz());
    }

    @Override
    public String getCacheFileName() {
        return mResponseCallback.getCacheFileName();
    }

}
