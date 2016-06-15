package com.yunmall.ymsdk.widget.segmentencontrol;


import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.yunmall.ymsdk.R;
import com.yunmall.ymsdk.utility.DeviceInfoUtils;
import com.yunmall.ymsdk.utility.YmLog;

public class SegmentedControl extends RadioGroup{

	private Context mContext = null;
	private int count = 0;

	private ArrayList<SegmentedControlButton> mTabButton = new ArrayList<SegmentedControlButton>();
	private String[] nameArray = null;
	private ArrayList<ViewGroup> viewsInSegmented = null;
	private AttributeSet attrs = null;
	
	public SegmentedControl(Context context) {
		super(context);
		mContext = context;
		mTabButton = new ArrayList<SegmentedControlButton>();
		setWillNotDraw(false);
	}

	public SegmentedControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.attrs = attrs;
		mContext = context;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SegmentedControl);
		int nameArrayId = -1;
		nameArrayId = a.getResourceId(R.styleable.SegmentedControl_segmentedNames, R.array.no_input);
		if (nameArrayId >= 0) {
			nameArray = this.getResources().getStringArray(nameArrayId);
		}
		mTabButton = new ArrayList<SegmentedControlButton>();
		this.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		this.setGravity(Gravity.CENTER);
		a.recycle();
		setWillNotDraw(false);
		setView();
		
	}

	public void addButton(SegmentedControlButton button) {
		mTabButton.add(button);
	}

	public void removeButton(int i) {
		this.removeView(mTabButton.get(i));
		mTabButton.remove(i);
	}

	public ArrayList<SegmentedControlButton> getButtons() {
		return mTabButton;
	}


	@Override
	public void onDraw(Canvas canvas) {	
		super.onDraw(canvas);
		
		
		
		// this.addView(mTabButton);
	}

	// 设置默认选中
	public void setCheck(int n) {
		if (n < mTabButton.size())
			mTabButton.get(n).setChecked(true);
		this.invalidate();	
	}

	public int getCheckedIndex(){
		for(int i=0; i<mTabButton.size();i++){
			if(mTabButton.get(i).getId() == getCheckedRadioButtonId()){
				return i;
			}
		}
		return 0;
	}
	
	public void setTabArray(String[] names) {
		nameArray = new String[names.length];
		nameArray = names;

		setView();
	}

	public void setTabArray(ArrayList<String> names, ArrayList<ViewGroup> views) {
		if (this.mTabButton.size() > 0) {
			reset();
		}
		nameArray = new String[names.size()];
		for (int i = 0; i < names.size(); i++) {
			nameArray[i] = names.get(i);
		}

		viewsInSegmented = views;
		setView();
	}

	private void reset() {
		nameArray = null;
		if (viewsInSegmented != null && viewsInSegmented.size() > 0) {
			for (int i = 0; i < viewsInSegmented.size(); i++) {
				viewsInSegmented.get(i).setVisibility(View.GONE);
			}
		}
		viewsInSegmented = null;
		this.mTabButton.clear();
		this.removeAllViews();
	}

	public String getTabItemName(int position) {
		if (position < nameArray.length)
			return nameArray[position];
		else
			return "";
	}

	public void setTabLabelName(String name, int index) {
		SegmentedControlButton sb = mTabButton.get(index);
		sb.setText(name);
	}

	public void setView() {
		this.removeAllViews();
		if (nameArray != null && !nameArray[0].equals("noInput") && nameArray.length > 1) {
			for (int i = 0; i < nameArray.length; i++) {
				final SegmentedControlButton button = new SegmentedControlButton(mContext,this.attrs,(i+1),nameArray[i]);
				button.setIndex(i);
				/*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				button.setLayoutParams(params);*/
				mTabButton.add(button);
				addView(button);
			}

			
		} else if (nameArray != null && !nameArray[0].equals("noInput") && nameArray.length == 1) {
			//只有一条结果的时候显示
//			mTabButton.get(0).setBackgroundResource(R.drawable.segmented_button_center_pressed);
			viewsInSegmented.get(0).setVisibility(View.VISIBLE);
		}
	}
	
	
	@Override      
	protected void onLayout(boolean changed, int left, int top, int right, int bottom){


        int totalWidth = this.getPaddingLeft();
        int childCount = this.getChildCount();
        if(childCount > 0){
            for(int i = 0;i<childCount;i++){
                View child = this.getChildAt(i);
                int measuredWidth = child.getMeasuredWidth();
                child.layout(left+totalWidth, 0, measuredWidth+totalWidth,getMeasuredHeight());
                totalWidth += measuredWidth;
            }
        }
		
	} 
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int measuredWidth = measureWidth(widthMeasureSpec);
		int measuredHeight = measureHeight(heightMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);
		count = this.getChildCount();
		if(count > 0){
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth/count, MeasureSpec.AT_MOST);
			for(int i = 0;i<count;i++){
				View child = this.getChildAt(i);
				child.measure(childWidthMeasureSpec, heightMeasureSpec);
			}
		}
		
	}
	
	
	
	
	private int measureWidth(int widthMeasureSpec) {
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		int result = 500;
		if (specMode == MeasureSpec.AT_MOST){
		
		result = specSize;
		} 

		else if (specMode == MeasureSpec.EXACTLY){

		result = specSize;
		}

		return result;
	}

	private int measureHeight(int heightMeasureSpec) {
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST){
		result = specSize;
		} 
		else if (specMode == MeasureSpec.EXACTLY){
		result = specSize;
		}

		return result;
	}

		
	
	

	
}
