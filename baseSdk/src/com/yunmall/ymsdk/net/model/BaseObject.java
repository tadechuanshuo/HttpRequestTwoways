package com.yunmall.ymsdk.net.model;

import java.io.Serializable;

/**
 * 基础数据类
 * Created by Zhp on 2014/5/16.
 */
public class BaseObject implements Serializable {

    /**
     * 基础id
     */
    public String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}


}
