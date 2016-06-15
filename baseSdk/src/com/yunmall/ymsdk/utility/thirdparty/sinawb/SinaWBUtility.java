package com.yunmall.ymsdk.utility.thirdparty.sinawb;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.Utility;
import com.yunmall.ymsdk.utility.Utils;
import com.yunmall.ymsdk.utility.thirdparty.sinawb.SWBSendParam.SWEType;

public class SinaWBUtility {
	
	/** HTTP 参数 */
    private static final String KEY_ACCESS_TOKEN = "access_token";
    
    /** 注销地址（URL） */
    private static final String REVOKE_OAUTH_URL = "https://api.weibo.com/oauth2/revokeoauth2";
    
    public final static String SINAWEIBO_REDIRECT_URI = "http://www.eyisheng.net.cn";
	
    private static SinaWBUtility instance = null;
    
    private final int THUMB_SIZE = 150;
    
    private final int MAX_SINA_TITLE = 512; // 512字节
    
    private final int MAX_SINA_DESCRIPTION = 1024; // 1KB
    
    private final int MAX_SINA_PHOTO_SIZE = 32 * 1024; // 32KB
    
    private SinaWBUtility(){}
    
    public static SinaWBUtility getInstance(){
    	if(instance == null){
    		instance = new SinaWBUtility();
    	}
    	return instance;
    }
    
    
	/**
	 * 新浪微博的web授权
	 * @param activity
	 * @param appKey
	 * @param redirecturl
	 * @param scope
	 * @param listener
	 */
	private void WebAuth(Activity activity,String appKey,String redirecturl,String scope, YmSWBAuthListener listener){
		WeiboAuth mWeiboAuth = new WeiboAuth(activity, appKey, redirecturl, scope);
		final YmSWBAuthListener finalListener = listener;
		mWeiboAuth.anthorize(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException exception) {
				finalListener.onError(0+"");
			}
			
			@Override
			public void onComplete(Bundle values) {
				Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
				if(accessToken.isSessionValid()){
//					
					// TODO 这个待定 这样做是否是最优的选择 给用户暴露存储token的方式 
//					AccessTokenKeeper.writeAccessToken(finalContext, accessToken);
					finalListener.saveAccessToken(accessToken);
					finalListener.onComplete();
				}else{
					String code = values.getString("code");
					finalListener.onError(code);
				}
			}
			
			@Override
			public void onCancel() {
				finalListener.onCancle();
			}
		});
		
	}
	
	/**
	 * sso认证 特别注意 一定要在onActivityResult方法中调用  {@link com.yunmall.ymsdk.utility.thirdparty.sinawb.SinaWBUtility#ssoAuthOnActivityResult(int, int, Intent, SsoHandler)}
	 * @param activity
	 * @param appKey
	 * @param redirecturl
	 * @param scope
	 * @param listener
	 */
	private void SSOAuth(Activity activity,String appKey,String redirecturl,String scope, YmSWBAuthListener listener){
		
		WeiboAuth mWeiboAuth = new WeiboAuth(activity, appKey, redirecturl, scope);
		final SsoHandler mSsoHandler = new SsoHandler(activity, mWeiboAuth);
		final YmSWBAuthListener finalListener = listener;
		listener.fetchSsoHandler(mSsoHandler);
        mSsoHandler.authorize(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				finalListener.onError(0+"");
				
			}
			
			@Override
			public void onComplete(Bundle values) {
				Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
				if(accessToken.isSessionValid()){
//					AccessTokenKeeper.writeAccessToken(finalContext, accessToken);
					//TODO 这个待定 这样做是否是最优的选择 给用户暴露存储token的方式 
					finalListener.saveAccessToken(accessToken);
					finalListener.onComplete(mSsoHandler);
				}else{
					String code = values.getString("code");
					finalListener.onError(code);
				}
			}
			
			@Override
			public void onCancel() {
				finalListener.onCancle();
			}
		});
		
	}
	
	
	/**
	 * 此方法是sso认证的时候，在OnActivityResult时候必须调用的
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @param ssoHandler
	 */
	public void ssoAuthOnActivityResult(int requestCode, int resultCode, Intent data,SsoHandler ssoHandler){
		ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	}
	
	
	
	public void loginWithSSOAuth(Activity activity,String appKey,String redirecturl,String scope, YmSWBAuthListener listener){
		SSOAuth(activity,appKey,redirecturl,scope, listener);
	}
	
	public void loginWithWebAuth(Activity activity,String appKey,String redirecturl,String scope, YmSWBAuthListener listener){
		WebAuth(activity, appKey, redirecturl, scope, listener);
	}
	
	/**
	 * 新浪微博注销 此方法是异步的
	 */
	public void logout(Context context,YmSWBLogoutListener listener){
		Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(context);
		WeiboParameters params = new WeiboParameters();
        params.put(KEY_ACCESS_TOKEN, mAccessToken.getToken());
        final Context finalContext  = context;
        final YmSWBLogoutListener finalListener = listener;
        AsyncWeiboRunner.requestAsync(REVOKE_OAUTH_URL, params, "POST", new RequestListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				finalListener.onFaile(arg0.getMessage());
			}
			
			@Override
			public void onComplete(String response) {
				if (!TextUtils.isEmpty(response)) {
	                try {
	                    JSONObject obj = new JSONObject(response);
	                    if(obj.isNull("error")){
		                    String value = obj.getString("result");
		                    // 注销成功
		                    if ("true".equalsIgnoreCase(value)) {
		                    	AccessTokenKeeper.clear(finalContext);
		                    }
	                    } else {
	                    	String error_code = obj.getString("error_code");
	                    	if(error_code.equals("21317")){
	                    		AccessTokenKeeper.clear(finalContext);
	                    	}
	                    }
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	            }
	            
	            if (finalListener != null) {
	            	finalListener.onComplete();
				}
			}
		});
	}
	
	
	/**
	 * 发送微博
	 * @param context
	 * @param appKey
	 * @param param
	 */
	public IWeiboShareAPI sendMessage(Context context,String appKey,SWBSendParam param){
		IWeiboShareAPI  mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, appKey);
		mWeiboShareAPI.registerApp();
		if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                @Override
                public void onCancel() {
                	Log.e("sinaweibo", "没有装新浪微博");
                    return;
                }
            });
        }
		
		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			//app支持的是4.0以上的 所以不考虑supportApi
			WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
			if(param.getType().equals(SWEType.TYPE_TEXT_OR_IMAGE)){
				if(!TextUtils.isEmpty(param.getText())){
					 TextObject textObject = new TextObject();
				     textObject.text = param.getText();
				     Bitmap bitmap = param.getBitmap();
				     Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150,true);
				     textObject.setThumbImage(thumbBmp);
				     weiboMessage.textObject = textObject;
				}
//				if(param.getBitmap() != null){
//					ImageObject imageObject = new ImageObject();
//			        imageObject.setImageObject(param.getBitmap());
//				}
				
			}
			if(param.getType().equals(SWEType.TYPE_WEB)){
				WebpageObject mediaObject = new WebpageObject();
		        mediaObject.identify = Utility.generateGUID();
		        TextObject textObject = new TextObject();
		        textObject.text = param.getText();
		        weiboMessage.textObject = textObject;
				if (param.getBitmap() != null) {
					ImageObject imageObject = new ImageObject();
					Bitmap thumbBmp = null;
					Bitmap bitmap = param.getBitmap();
					thumbBmp = Bitmap.createScaledBitmap(bitmap, 150,
							150, true);
					if (thumbBmp != null) {
			            int size = THUMB_SIZE;
			            byte[] data = Utils.bmpToByteArray(thumbBmp, false);
			            int byteCount = data.length;
			            while (byteCount > MAX_SINA_PHOTO_SIZE) {
			                size = (int)size * 4 / 5;
			                thumbBmp = Bitmap.createScaledBitmap(thumbBmp, size, size, true);
			                byteCount = Utils.bmpToByteArray(thumbBmp, false).length;
			            }
			            byte [] thumbData = Utils.bmpToByteArray(thumbBmp, false);
			            imageObject.setImageObject(bitmap);
			            imageObject.setThumbImage(thumbBmp);
			            mediaObject.thumbData = thumbData;
						if (imageObject.checkArgs()) {
							weiboMessage.imageObject = imageObject;
						}
//			            imageObject.thumbData = thumbData;
//			            imageObject.setThumbImage(thumbBmp);
			        }
				}
				mediaObject.title = "";
				mediaObject.description = param.getText();
		        mediaObject.actionUrl = param.getActionUrl();
				weiboMessage.mediaObject = mediaObject;
		       
		        
			}else if(param.getType().equals(SWEType.TYPE_MUSIC)){
		        MusicObject musicObject = new MusicObject();
		        musicObject.identify = Utility.generateGUID();
		        musicObject.title = param.getTitle();
		        musicObject.description = param.getDesc();
		        
		        musicObject.setThumbImage(param.getThumbView());
		        musicObject.actionUrl = param.getActionUrl();
		        weiboMessage.mediaObject = musicObject;
			}else if(param.getType().equals(SWEType.TYPE_VIDEO)){
		        VideoObject videoObject = new VideoObject();
		        videoObject.identify = Utility.generateGUID();
		        videoObject.title = param.getTitle();
		        videoObject.description = param.getDesc();
		        
		        videoObject.setThumbImage(param.getThumbView());
		        videoObject.actionUrl = param.getActionUrl();
		        weiboMessage.mediaObject = videoObject;
			}else if(param.getType().equals(SWEType.TYPE_VOICE)){
		        VoiceObject voiceObject = new VoiceObject();
		        voiceObject.identify = Utility.generateGUID();
		        voiceObject.title = param.getTitle();
		        voiceObject.description = param.getDesc();
		        
		        voiceObject.setThumbImage(param.getThumbView());
		        voiceObject.actionUrl = param.getActionUrl();
		        weiboMessage.mediaObject = voiceObject;
			}
			
			SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
	        request.transaction = String.valueOf(System.currentTimeMillis());
	        request.multiMessage = weiboMessage;
	        
	        mWeiboShareAPI.sendRequest(request);
			
        } else {
            Toast.makeText(context, "不支持", Toast.LENGTH_SHORT).show();
        }
		return mWeiboShareAPI;
	}

	
	/**
	 * 这个方法要在调用 {@link SinaWBUtility#sendMessage(Context, String, SWBSendParam)}的activity中的{@link Activity#onNewIntent}中调用 一定要调用 才可以接收到分享微博后的回调
	 * @param context
	 * @param appKey
	 * @param intent
	 * @param response
	 */
	public void handleWeiboMsgResponse(Context context,String appKey,Intent intent,IWeiboHandler.Response response){
		IWeiboShareAPI  mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, appKey);
		mWeiboShareAPI.registerApp();
		mWeiboShareAPI.handleWeiboResponse(intent, response);
	}
	
	
	


}
