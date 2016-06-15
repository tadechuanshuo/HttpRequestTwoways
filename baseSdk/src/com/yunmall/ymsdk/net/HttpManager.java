package com.yunmall.ymsdk.net;

import android.content.Context;

import com.yunmall.ymsdk.net.callback.ResponseCallback;
import com.yunmall.ymsdk.net.http.AsyncHttpClient;
import com.yunmall.ymsdk.net.http.GsonHttpResponseHandler;
import com.yunmall.ymsdk.net.http.RequestHandle;
import com.yunmall.ymsdk.net.http.RequestParams;
import com.yunmall.ymsdk.net.http.YmHttpLog;
import com.yunmall.ymsdk.net.model.BaseObject;

import org.apache.http.Header;

/**
 * Http请求管理类
 * Created by Zhp on 2014/5/19.
 * <pre>
 * Simple Demo:
 * HttpManager.getInstance().post(getBaseContext(), mTestUrl, requestParams, new ResponseCallbackImpl&#60;TestGsonModel&#62;() {

            &#064;Override
            public void onStart() {
            super.onStart();
            }

            &#064;Override
            public void onSuccess(TestGsonModel baseData) {

            }

            &#064;Override
            public void onFailed(TestGsonModel failData, Throwable error) {

            }
        });
 * </pre>
 */
@SuppressWarnings("unchecked")
public class HttpManager {

    private static HttpManager mInstance;

    private AsyncHttpClient mAsyncHttpClient;

    public static HttpManager getInstance() {
        if (mInstance == null) {
            synchronized (HttpManager.class) {
                if (mInstance == null) {
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;
    }

    private HttpManager() {
        mAsyncHttpClient = new AsyncHttpClient(true, 0, 0);
    }

    /**
     * 基础post请求，在回调中返回获取的数据。
     * 所有的回调都运行在UI线程中
     * @param context 上下文环境
     * @param url     请求的url
     * @param params  请求的参数
     * @param responseCallback   请求的回调
     * @return RequestHandle
     */
    public RequestHandle post(Context context, final String url, final RequestParams params, final ResponseCallback responseCallback) {
        return mAsyncHttpClient.post(context, url, params, new GsonHttpResponseHandler(responseCallback) {

            @Override
            public void onStart() {
                super.onStart();
                this.addMarker("start post for: %s", YmHttpLog.getUrlWithQueryStringForLog(url, params));
                responseCallback.onStart();
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                responseCallback.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseObject response) {
                responseCallback.onSuccess(response);
                responseCallback.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, BaseObject errorResponse) {
                responseCallback.onFailed(throwable, statusCode);
                responseCallback.onFinish();
            }

            @Override
            public void onCacheLoad(BaseObject responseJsonType, long time) {
                responseCallback.onCacheLoaded(responseJsonType, time);
            }

        });
    }

    /**
     * 基础post请求，在回调中返回获取的数据。
     * 所有的回调都运行在UI线程中
     * @param url     请求的url
     * @param params  请求的参数
     * @param responseCallback   请求的回调
     * @return RequestHandle
     */
    public RequestHandle post(final String url, final RequestParams params, final ResponseCallback responseCallback) {
        return mAsyncHttpClient.post(url, params, new GsonHttpResponseHandler(responseCallback) {

            @Override
            public void onStart() {
                this.addMarker("start post for: %s", YmHttpLog.getUrlWithQueryStringForLog(url, params));
                responseCallback.onStart();
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                responseCallback.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseObject response) {
                responseCallback.onSuccess(response);
                responseCallback.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, BaseObject errorResponse) {
                responseCallback.onFailed(throwable, statusCode);
                responseCallback.onFinish();
            }

            @Override
            public void onCacheLoad(BaseObject responseJsonType, long time) {
                responseCallback.onCacheLoaded(responseJsonType, time);
            }

            @Override
            public void onFinish() {
                responseCallback.onFinish();
            }

        });
    }

    /**
     * 基础get请求，在回调中返回获取的数据。
     * 所有的回调都运行在UI线程中
     * @param context 上下文环境
     * @param url     请求的url
     * @param params  请求的参数
     * @param responseCallback   请求的回调
     * @return RequestHandle
     */
    public RequestHandle get(Context context, final String url, final RequestParams params, final ResponseCallback responseCallback) {
        return mAsyncHttpClient.get(context, url, params, new GsonHttpResponseHandler(responseCallback) {

            @Override
            public void onStart() {
                super.onStart();
                this.addMarker("start get for: %s", YmHttpLog.getUrlWithQueryStringForLog(url, params));
                responseCallback.onStart();
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                responseCallback.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseObject response) {
                responseCallback.onSuccess(response);
                responseCallback.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, BaseObject errorResponse) {
                responseCallback.onFailed(throwable, statusCode);
                responseCallback.onFinish();
            }

            @Override
            public void onCacheLoad(BaseObject responseJsonType, long time) {
                responseCallback.onCacheLoaded(responseJsonType, time);
            }

        });
    }

    /**
     * 基础get请求，在回调中返回获取的数据。
     * 所有的回调都运行在UI线程中
     * @param url     请求的url
     * @param params  请求的参数
     * @param responseCallback   请求的回调
     * @return RequestHandle
     */
    public RequestHandle get(final String url, final RequestParams params, final ResponseCallback responseCallback) {
        return mAsyncHttpClient.get(url, new GsonHttpResponseHandler(responseCallback) {

            @Override
            public void onStart() {
                this.addMarker("start get for: %s", YmHttpLog.getUrlWithQueryStringForLog(url, params));
                responseCallback.onStart();
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                responseCallback.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BaseObject response) {
                responseCallback.onSuccess(response);
                responseCallback.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, BaseObject errorResponse) {
                responseCallback.onFailed(throwable, statusCode);
                responseCallback.onFinish();
            }

            @Override
            public void onCacheLoad(BaseObject responseJsonType, long time) {
                responseCallback.onCacheLoaded(responseJsonType, time);
            }

        });
    }

    /**
     * 取消所有请求
     */
    public void cancelAll() {
        mAsyncHttpClient.cancelAllRequests(true);
    }

}