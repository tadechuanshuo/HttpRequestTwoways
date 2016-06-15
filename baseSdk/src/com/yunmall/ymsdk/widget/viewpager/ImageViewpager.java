package com.yunmall.ymsdk.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ImageViewpager extends ViewPager{
	
	private static final String TAG = "HackyViewPager";

	public ImageViewpager(Context context) {
		super(context);
	}
	
	public ImageViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			//不理会
			Log.e(TAG,"hacky viewpager error1");
			return false;
		}catch(ArrayIndexOutOfBoundsException e ){
			//不理会
			Log.e(TAG,"hacky viewpager error2");
			return false;
		}
	}

}
