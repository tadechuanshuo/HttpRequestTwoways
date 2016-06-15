package com.example.qiuqing.request;

import android.content.Context;
import android.widget.Toast;

import com.example.qiuqing.Myapplication;
import com.example.qiuqing.R;
import com.yunmall.ymsdk.net.HttpManager;
import com.yunmall.ymsdk.net.callback.ResponseCallback;

public class HttpApiBase implements CommandInterface {
	
//	http://app.eyisheng.net.cn/eyisheng/patientc/reservelist?page=1&aid=100102200&member_id=11538

	private final static int HOST = 0;
	private final static String[] IPS = {"192.168.16.184:8921/v2.1/rest/", "192.168.199.111:8921/v2.1/rest/"};

	private final static String BASE_URL = "http://" + IPS[HOST];

	
	public static String getBaseUrl() {
		return BASE_URL;
	}
	
	public static void post(Context context, final String url, final BaseRequestParams params, final ResponseCallback responseCallback) {
		checkNetwork(responseCallback);
		params.doAuthSigh();
		String reqUrl = formatUrl(url, params);
		HttpManager.getInstance().post(context, reqUrl, params, getResponseCallback(context, responseCallback));
	}

	public static void post(final String url, final BaseRequestParams params, final ResponseCallback responseCallback) {
		checkNetwork(responseCallback);
		params.doAuthSigh();
		String reqUrl = formatUrl(url, params);
		HttpManager.getInstance().post(reqUrl, params, getResponseCallback(responseCallback));
	}

	public static void get(Context context, final String url,final BaseRequestParams params, final ResponseCallback responseCallback) {
		checkNetwork(responseCallback);
		params.doAuthSigh();
		String reqUrl = formatUrl(url, params);
		HttpManager.getInstance().get(context, reqUrl, params,getResponseCallback(responseCallback));
	}

	public static void get(final String url, final BaseRequestParams params,final ResponseCallback responseCallback) {
		checkNetwork(responseCallback);
		params.doAuthSigh();
		String reqUrl = formatUrl(url, params);
		HttpManager.getInstance().get(reqUrl, params,getResponseCallback(responseCallback));
	}

	private static String formatUrl(String url, BaseRequestParams params) {
		String command = params.getCommand();
		return url + command;
	}

	private static ResponseCallbackBR getResponseCallback(ResponseCallback responseCallback) {
		return new ResponseCallbackBR(responseCallback);
	}
	
	private static ResponseCallbackBR getResponseCallback(Context context, ResponseCallback responseCallback) {
		return new ResponseCallbackBR(context, responseCallback);
	}

	private static boolean checkNetwork(ResponseCallback responseCallback) {
		if (!NetworkUtils.isConnected()) {
			Toast.makeText(Myapplication.getInstance(), R.string.network_error, Toast.LENGTH_SHORT).show();
			if (responseCallback != null) {
				responseCallback.onFailed(new Throwable(Myapplication.getInstance().getString(R.string.network_error)), 0);
			}
			return true;
		}
		return false;
	}
}
