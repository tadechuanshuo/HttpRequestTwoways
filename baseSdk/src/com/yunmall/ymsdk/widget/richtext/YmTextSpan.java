package com.yunmall.ymsdk.widget.richtext;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.yunmall.ymsdk.utility.DeviceInfoUtils;

public class YmTextSpan extends ClickableSpan {

	private int txtNormalColor;
	private int txtPressedColor;
	private int bgNormalColor;
	private int bgPressedColor;
    private float textSize = 0;
	private View.OnClickListener listener;

	protected boolean isPressed;

    public YmTextSpan() {
    }

	public YmTextSpan(int txtNormalColor, View.OnClickListener listener) {
		this.txtNormalColor = txtNormalColor;
		this.txtPressedColor = txtNormalColor;
		this.bgNormalColor = Color.TRANSPARENT;
		this.bgPressedColor = Color.TRANSPARENT;
		this.listener = listener;
	}

	public YmTextSpan(int txtNormalColor, int txtPressedColor, int bgNormalColor, int bgPressedColor, View.OnClickListener listener) {
		this.txtNormalColor = txtNormalColor;
		this.txtPressedColor = txtPressedColor;
		this.bgNormalColor = bgNormalColor;
		this.bgPressedColor = bgPressedColor;
		this.listener = listener;
	}

    /**
     * 设置字体大小
     * @param sp
     */
    public void setTextSize(Context context, float sp){
            textSize = DeviceInfoUtils.sp2px(context, sp);
    }

    /**
     * 设置字体颜色
     * @param normalColor
     * @param pressedColor
     */
    public void setTextColor(int normalColor, int pressedColor){
        txtNormalColor = normalColor;
        txtPressedColor = pressedColor;
    }

    /**
     * 设置字体背景颜色
     * @param normalColor
     * @param pressedColor
     */
    public void setTextBgColor(int normalColor, int pressedColor){
        bgNormalColor = normalColor;
        bgPressedColor = pressedColor;
    }

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(isPressed ? txtPressedColor : txtNormalColor);
		ds.bgColor = isPressed ? bgPressedColor : bgNormalColor;
        if(textSize > 0) {
            ds.setTextSize(textSize);
        }

	}

	@Override
	public void onClick(View widget) {
		if (listener != null) {
			listener.onClick(widget);
		}
	}
}
