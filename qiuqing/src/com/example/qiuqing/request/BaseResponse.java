package com.example.qiuqing.request;

import com.yunmall.ymsdk.net.model.BaseObject;

/**
 * 网络请求返回基础数据类
 * Created by Zhp on 2014/6/6.
 */
public class BaseResponse extends BaseObject {

	public int code;

    public String msg;
    
    
    public final boolean isSucceeded() {
        return (code == 0);
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
