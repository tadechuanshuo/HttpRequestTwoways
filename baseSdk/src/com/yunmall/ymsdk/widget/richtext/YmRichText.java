package com.yunmall.ymsdk.widget.richtext;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.yunmall.ymsdk.widget.richtext.parser.BaseParser;

/**
 * 实现图文混编，支持文本的点击响应
 * @author changxiang
 *
 */
public class YmRichText extends TextView {

	private SpannableStringBuilder text = new SpannableStringBuilder("");
	
	private ClickableSpan pressedSpan;
	
    /** 当前的文本，异步回调时判断文本是否一致 */
    private String mOriginalText;
    
    /** 解析文本的异步任务 */
    private ParseTextTask mParseTextTask;
    
	public YmRichText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public YmRichText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public YmRichText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		
		/** 取消异步任务 */
		cancelTask(mParseTextTask);
	}

	public void reset(){
		text.clear();
		setText(text);		
	}
	
	public void commit(){
		setText(text);	
		setMovementMethod(LinkMovementMethod.getInstance());  
	}
	
	/** 解决控件在listview里，listview无法获取事件 {@link OnItemClickListener#onItemClick(android.widget.AdapterView, View, int, long)}} */
    @Override
	public boolean hasFocusable() {
		// TODO Auto-generated method stub
		return false;
	}

    /**
     * 设置原始文本，并定义响应的解析器集
     * @param content
     * @param parsers
     */
	public void setRichText(String content, BaseParser... parsers){
    	text = new SpannableStringBuilder(content);
    	if(parsers != null && parsers.length > 0){
    		for(BaseParser parser : parsers){
    			parser.parse(getContext(), text);
    		}
    	}
    	commit();
    }
	
	/**
     * 设置原始文本，并定义响应的解析器集，异步执行
     * @param content
     * @param parsers
     */
	public void setAsyncRichText(String content, BaseParser... parsers){
    	cancelTask(mParseTextTask);
    	this.mOriginalText = content;
    	mParseTextTask = new ParseTextTask(content);
    	mParseTextTask.execute(parsers);
    }
	
	/**
	 * 添加普通文本
	 * @param str
	 */
	public void addText(CharSequence str){
		text.append(str);
		commit();

	}

    /**
     * 添加普通文本
     * @param str
     */
    public void addText(CharSequence str, int textSize, int textColor){
        YmTextSpan span = new YmTextSpan();
        span.setTextSize(getContext(), textSize);
        span.setTextColor(textColor, textColor);

        int start = text.length();
        text.append(str);
        int end = text.length();
        text.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        commit();
    }
	
	/**
	 * 添加可点击的特殊颜色文本
	 * @param str
	 */
	public void addText(CharSequence str, int color, View.OnClickListener clicker){
		int start = text.length();
		text.append(str);
		int end = text.length();
		text.setSpan(new YmTextSpan(color, clicker), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		commit();
	}
	
	/**
	 * 添加可点击文本
	 * @param str
	 */
	public void addText(CharSequence str, ClickableSpan span){
		int start = text.length();
		text.append(str);
		int end = text.length();
		text.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		commit();
	}
	
	/**
	 * 添加可点击的图片
	 * @param str
	 * @param bm
	 * @param clicker
	 */
    public void addImage(CharSequence str, Bitmap bm, View.OnClickListener clicker){
    	int start = text.length();
    	text.append(str);
		int end = text.length();
		text.setSpan(new ImageSpan(getContext(), bm), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		commit();
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	boolean result = false;
        
        TextView widget = (TextView)this;
        Object text = widget.getText();
        if (text instanceof Spanned) {
            Spannable buffer;
            try {
                buffer = (Spannable)text;
            } catch (ClassCastException cce) {
                cce.printStackTrace();
                return false;
            }
            
            int action = event.getAction();
            
            if (action == MotionEvent.ACTION_DOWN) {
            	pressedSpan = getPressedSpan(widget, buffer, event);
                if (pressedSpan != null) {
                    Selection.setSelection(buffer, buffer.getSpanStart(pressedSpan),
                            buffer.getSpanEnd(pressedSpan));
                    invalidateClickSpan(true, widget);
                    result = true;
                }
            } else if (action == MotionEvent.ACTION_MOVE) {
                ClickableSpan touchedSpan = getPressedSpan(widget, buffer, event);
                if (pressedSpan != null && touchedSpan != pressedSpan) {
                    invalidateClickSpan(false, widget);
                    pressedSpan = null;
                    Selection.removeSelection(buffer);
                }
            } else {
                if (pressedSpan != null) {
                    if (action == MotionEvent.ACTION_UP) {
                    	pressedSpan.onClick(widget);
                    }
                    invalidateClickSpan(false, widget);
                }
                pressedSpan = null;
                Selection.removeSelection(buffer);
            }
        }
        
        return result;
    }
    
    private void invalidateClickSpan(boolean pressed, TextView widget) {
        if (pressedSpan instanceof YmTextSpan) {
            ((YmTextSpan)pressedSpan).isPressed = pressed;
        }
        widget.invalidate();
    }
    
    private ClickableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
        
        int x = (int)event.getX();
        int y = (int)event.getY();
        
        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();
        
        x += textView.getScrollX();
        y += textView.getScrollY();
        
        Layout layout = textView.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);
        
        ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);
        ClickableSpan touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }
    
    @SuppressWarnings("rawtypes")
	private void cancelTask(AsyncTask task){
    	if(task != null && task.getStatus() != AsyncTask.Status.FINISHED){
    		task.cancel(true);
    	}
    	task = null;
    }
    /**
     * 异步解析富文本任务
     */
    private class ParseTextTask extends AsyncTask<BaseParser, Void, Integer>{

    	String content;
    	SpannableStringBuilder textBuilder;
    	
    	private ParseTextTask(String context){
    		super();
    		this.content = context;
    		this.textBuilder = new SpannableStringBuilder(this.content);
    	}
    	
		@Override
		protected Integer doInBackground(BaseParser... params) {

			if(params != null && params.length > 0){
	    		for(BaseParser parser : params){
	    			parser.parse(getContext(), textBuilder);
	    		}
	    	}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(this.content != null && this.content.equals(mOriginalText)){
				text = this.textBuilder;
				commit();
			}
		}
    	
    }
    
}
