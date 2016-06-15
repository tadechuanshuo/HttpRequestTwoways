package com.yunmall.ymsdk.net.http.model;

import com.yunmall.ymsdk.net.model.BaseObject;

/**
 * 网络请求返回基础数据类
 * sdk中使用，判断返回是否成功
 * Created by Zhp on 2014/6/6.
 */
public class BaseRes extends BaseObject {

    /**
     * 错误码
     */
	public int code;

    /**
     * 服务器返回的信息
     */
    public String msg;


    public final boolean isSucceeded() {
        return (code == 0);
    }

}
