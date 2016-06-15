package com.example.qiuqing;

import android.app.Application;

public class Myapplication extends Application {
	
	private static Myapplication app;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public static Myapplication getInstance() {
		return app;
	}

}
