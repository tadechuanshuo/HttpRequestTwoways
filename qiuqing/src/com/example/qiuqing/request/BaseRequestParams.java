package com.example.qiuqing.request;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.yunmall.ymsdk.net.http.RequestParams;
import com.yunmall.ymsdk.utility.AuthUtils;
/**
 * 基础请求参数类,封装了请求所需的基本参数
 * Created by Zhp on 2014/6/6.
 */
public class BaseRequestParams extends RequestParams {

    private String mCommand;
    private boolean paramURLEcode;

    public BaseRequestParams() {
        super();
        initBaseParams();
    }

    public BaseRequestParams(String command) {
        super();
//        put("command", command);
        setCommand(command);
        initBaseParams();
    }

    public String getCommand() {
        return mCommand;
    }

    public void setCommand(String mCommand) {
        this.mCommand = mCommand;
    }

    public void doAuthSigh() {
        AuthUtils.md5Sign(this);
    }

    private void initBaseParams() {
        this.setUseJsonStreamer(true);
    }
    
    @Override
    protected String getParamString(){
        if (paramURLEcode){
            return  URLEncodedUtils.format(getParamsList(), contentEncoding);
        }else {
            StringBuffer sb = new StringBuffer();
            for (BasicNameValuePair pair : getParamsList()){
                if (0 < sb.length()){
                    sb.append("&");
                }
                sb.append(pair.getName());
                sb.append("=");
                sb.append(pair.getValue());
            }
            return sb.toString();
        }
    }

    public boolean isParamURLEcode() {
        return paramURLEcode;
    }

    public void setParamURLEcode(boolean paramURLEcode) {
        this.paramURLEcode = paramURLEcode;
    }

}
