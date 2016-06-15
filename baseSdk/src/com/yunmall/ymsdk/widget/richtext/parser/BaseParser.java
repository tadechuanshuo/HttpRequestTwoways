package com.yunmall.ymsdk.widget.richtext.parser;

import android.content.Context;
import android.text.SpannableStringBuilder;

public interface BaseParser {
	
	/**
	 * 解析字符串，并构造显示文本元素
	 * @param context
	 * @param spannable
	 * @return
	 */
    public boolean parse(Context context, SpannableStringBuilder spannable);
}
