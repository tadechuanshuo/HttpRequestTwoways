package com.yunmall.ymsdk.widget.segmentencontrol;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.yunmall.ymsdk.R;

public class SegmentedControlButton extends RadioButton{
	private float mX;    
	private int positon = 0;
	private int index = 0;
	private float textSize;
	int textColor;
	public SegmentedControlButton(Context context) {
		super(context);    
	}    
	public SegmentedControlButton(Context context, AttributeSet attrs) {        
		super(context, attrs);
		
	} 
	public SegmentedControlButton(Context context, AttributeSet attrs, String Text, int position) {        
		super(context, attrs);
		this.positon = position;
	} 
	public SegmentedControlButton(Context context, AttributeSet attrs, int index,String text) {
		super(context, attrs);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentedButton);
		textSize = ta.getDimension(R.styleable.SegmentedButton_segmentedButton_textSize, 30);
		textColor = ta.getColor(R.styleable.SegmentedButton_segmentedButton_textColor, R.color.black);
		int backgroungRes = ta.getResourceId(R.styleable.SegmentedButton_segmentedButton_backgroung, 0);
		this.setTextSize(textSize);
		this.setTextColor(textColor);
		this.setBackgroundResource(backgroungRes);
		this.setId(index);
		this.setText(text);
		ta.recycle();
		
	}
	
	@Override    
	public void onDraw(Canvas canvas) {   
		
		String text = this.getText().toString();        
		Paint textPaint = new Paint();
		textPaint.setColor(textColor);
		textPaint.setAntiAlias(true); 
		textPaint.setTextSize(textSize);
		float currentWidth = textPaint.measureText(text);        
		float currentHeight = textPaint.measureText("x");        
		textPaint.setTextAlign(Paint.Align.CENTER);
		float w = (this.getWidth() / 2) - currentWidth;        
		float h = ((this.getHeight()-this.getPaddingTop()-this.getPaddingBottom()) / 2) + currentHeight/2;        
		canvas.drawText(text, this.mX, h, textPaint);         
		     
	}   
	@Override    
	protected void onSizeChanged(int w, int h, int ow, int oh) {        
		super.onSizeChanged(w, h, ow, oh);        
		this.mX = w * 0.5f; // remember the center of the screen    
	}
	
	public void setPositon(int pos){
		this.positon = pos;
	}
	
	public int getPositon(){
		return this.positon;
	}
	
	public void setIndex(int ind){
		this.index = ind;
	}
	
	public int getIndex(){
		return this.index;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}
	
	
	private int measureWidth(int widthMeasureSpec) {
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		int result = 0;
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
		int result = 0;
		if (specMode == MeasureSpec.AT_MOST){
		   result = specSize;
		} 
		else if (specMode == MeasureSpec.EXACTLY){
		   result = specSize;
		}

		return result;
	}
	
	
	
}