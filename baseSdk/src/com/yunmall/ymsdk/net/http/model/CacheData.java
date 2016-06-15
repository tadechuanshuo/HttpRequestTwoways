package com.yunmall.ymsdk.net.http.model;

import com.yunmall.ymsdk.net.model.BaseObject;

/**
 * 基本Cache数据
 * Created by Zhp on 2014/5/16.
 */
public class CacheData extends BaseObject {

    /**
     * cache数据
     */
    public byte[] cacheData;

    /**
     * cache的最后一次修改时间
     * 使用者根据该事件判断是否需要使用该chache
     */
    public long time;

}
