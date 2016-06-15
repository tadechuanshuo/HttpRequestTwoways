package com.yunmall.ymsdk.utility.thirdparty.sinawb;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

public interface YmSWBAuthListener {
	/**
	 * web授权会用到
	 */
	public void onComplete();
	
	public void fetchSsoHandler(SsoHandler ssoHandler);
	
	/**
	 * sso授权会用到
	 * @param ssoHandler 用于在onActivityResult方法中用到的ssoHandler
	 */
	public void onComplete(SsoHandler ssoHandler);
	
	public void onCancle();
	
	public void onError(String code);
	
	public void saveAccessToken(Oauth2AccessToken accessToken);

}
