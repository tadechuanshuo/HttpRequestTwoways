package com.yunmall.ymsdk.widget.richtext.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;

import com.yunmall.ymsdk.utility.YmToastUtils;
import com.yunmall.ymsdk.widget.richtext.YmTextSpan;

/**
 * 话题  匹配字符串的样式为：#话题名# 
 * @author changxiang
 *
 */
public class TopicParser implements BaseParser{

	private static final Pattern PATTERN = Pattern.compile("#([^\\#|.]+)#");
	
	private static TopicParser instance = null;
	private TopicParser(){};
	
	public static TopicParser getInstance(){
		if(instance == null){
			instance = new TopicParser();
		}
		return instance;
	}
	
	@Override
	public boolean parse(final Context context, SpannableStringBuilder spannable) {
		boolean hasMatches = false;
		Matcher m = PATTERN.matcher(spannable);

		while (m.find()) {
			int start = m.start();
			int end = m.end();

			final String item = spannable.subSequence(start, end).toString();
			View.OnClickListener clicker = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					YmToastUtils.showToast(context, "click:"+item);
				}
			};
			
			YmTextSpan span = new YmTextSpan(0xff00ffff,0xff00ffff, Color.TRANSPARENT, 0xffff0000, clicker);
			spannable.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

			hasMatches = true;
		}

		return hasMatches;
	}

}
