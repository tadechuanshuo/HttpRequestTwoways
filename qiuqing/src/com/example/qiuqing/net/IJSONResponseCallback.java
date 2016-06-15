package com.example.qiuqing.net;

import org.json.JSONObject;

public interface IJSONResponseCallback 
{
	/**
     * The actual callback method
     */
    public void handleResponse(JSONObject response);
    
    /**
     * Called when a network error has occurred
     */
    public void handleError(int code);
}
