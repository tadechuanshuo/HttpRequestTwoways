package com.example.qiuqing;

import com.example.qiuqing.net.IJSONResponseCallback;
import com.example.qiuqing.net.JSONClient;
import com.example.qiuqing.net.JSONRequest;
import com.example.qiuqing.request.BaseRequestParams;
import com.example.qiuqing.request.HttpApiBase;
import com.yunmall.ymsdk.net.callback.ResponseCallbackImpl;

import android.content.Context;


public class CaseManager extends HttpApiBase {

	public static void verifyCode(Context context,
			ResponseCallbackImpl<PhoneICEntity> callback) {
		BaseRequestParams params = new BaseRequestParams(EDIT_SCHEDULING);
//		params.put("api_key", "wvP4Egy79SqChhZm");
		get(context, getBaseUrl(), params, callback);
	}
	
	public static void sendMsg(String phone,String code,final IJSONResponseCallback callback,Context con,boolean isCache)
	{
		JSONRequest request = new JSONRequest("http://192.168.199.111:8921/v2.1/rest/util/verifyCode/"+phone+"/"+code,isCache,null);
//		request.addParameter("Content","您好，您本次的验证码为："+code+"，有效期10分钟，请及时操作！");
//		request.addParameter("Mobile",phone);
//		request.addParameter("Pwd","l@18601042721");
		request.addParameter("api_key", "wvP4Egy79SqChhZm");
//		request.addParameter("Cell", "");
//		request.addParameter("SendTime","");
		request.setIsSecert(false);
		request.setIsPost(false);
		invoke(request, callback, con);
	}
	
	/**
	 * 发网络请求
	 * @param request  请求
	 * @param callback 回调
	 */
	private static void invoke(JSONRequest request,IJSONResponseCallback callback,Context con)
	{
		try
		{
			JSONClient.getInstance().sendRequest(request,callback,con);
		}
		catch( InterruptedException e )
		{
			e.printStackTrace();
		}
	}

}
