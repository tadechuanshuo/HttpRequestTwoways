package com.yunmall.ymsdk.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.yunmall.ymsdk.widget.YmSlidingPaneLayout;
import com.yunmall.ymsdk.widget.YmSlidingPaneLayout.PanelSlideListener;

/**
 * 手势滑动退出Activity基类，在manifest中需要把该Activity的Theme设置为背景透明的，如 android:theme="@android:style/Theme.Translucent.NoTitleBar"
 * 
 * @author changxiang
 * 
 */
public class SlidingPanelFragmentActivity extends FragmentActivity implements PanelSlideListener {

	private YmSlidingPaneLayout slidingPaneLayout;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		
		ViewGroup localViewGroup = (ViewGroup) getWindow().getDecorView();
		View curView = localViewGroup.getChildAt(0);
		localViewGroup.removeView(curView);

		slidingPaneLayout = new YmSlidingPaneLayout(this);
		slidingPaneLayout.setPanelSlideListener(this);
		slidingPaneLayout.setSliderFadeColor(0);
//		slidingPaneLayout.setShadowResource(R.drawable.ymsdk_fade_bg);
		localViewGroup.addView(slidingPaneLayout);

		View backView = new View(this);
		ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		slidingPaneLayout.addView(backView, localLayoutParams);
		slidingPaneLayout.addView(curView);
	}

	/**
	 * 设置页面是否开启滑动功能，  默认是开启的
	 * @param enable
	 */
	public void setSlideEnable(boolean enable){
		slidingPaneLayout.setSlideable(enable);
	}
	
	/**
	 * 获取slide panel,便于自定义效果
	 * @return
	 */
	public YmSlidingPaneLayout getSlidingPaneLayout(){
		return slidingPaneLayout;
	}
		
	/**
	 * 按返回键时，页面滑动退出
	 */
	@Override
	public void onBackPressed() {
		if(slidingPaneLayout.isSlideable() && !slidingPaneLayout.isOpen()){  
			slidingPaneLayout.openPane();
        }  
        else{  
            super.onBackPressed();  
        }  
	}

	/**
	 * 页面滑动状态变化
	 * @param panel 页面
	 * @param slideOffset 页面向右滑出的距离 0.0f - 1.0f
	 */
	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		// TODO Auto-generated method stub
	}

	/**
	 * 页面从右侧完全滑出
	 */
	@Override
	public void onPanelOpened(View panel) {
		// TODO Auto-generated method stub
		finish();
	}

	/**
	 * 页面回复完全显示
	 */
	@Override
	public void onPanelClosed(View panel) {
		// TODO Auto-generated method stub
	}
}
