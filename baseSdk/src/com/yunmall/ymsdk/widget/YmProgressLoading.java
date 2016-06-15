package com.yunmall.ymsdk.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunmall.ymsdk.R;

public class YmProgressLoading extends Dialog {
	private CharSequence mMessage;

	private TextView mMessageTextView;

	private Window mWindow;

	private ProgressBar mProgressBar;

	private boolean mShowProgress = true;

	public YmProgressLoading(Context context) {
		super(context);
		init();
	}

	public YmProgressLoading(Context context, int theme) {
		super(context, theme);
		init();
	}

	private void init() {
		this.mWindow = getWindow();
		mWindow.requestFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.ymsdk_widget_progressloading);
		mMessageTextView = (TextView) findViewById(R.id.xg_progressloading_textView);
		mProgressBar = (ProgressBar) findViewById(R.id.xg_progressloading_progressbar);
	}

	public void setMessage(CharSequence message) {
		mMessage = message;
	}

	public static YmProgressLoading show(Context context, CharSequence message) {
		return show(context, message, false);
	}

	public static YmProgressLoading show(Context context, CharSequence message, boolean cancelable) {
		return show(context, message, cancelable, null);
	}

	private static YmProgressLoading dialog;
	/**
	 * 
	 * @param context
	 * @param message
	 * @param cancelable   这个字段true表示点击返回按钮可以关闭，flase表示不可以关闭
	 * @param cancelListener
	 * @return
	 */
	public static YmProgressLoading show(Context context, CharSequence message, boolean cancelable,
			OnCancelListener cancelListener) {
		dialog = new YmProgressLoading(context, R.style.YmToastTheme);
		dialog.setMessage(message);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.show();
		return dialog;
	}
	
	public static YmProgressLoading close(){	
		if(dialog.isShowing()){
			dialog.dismiss();
		}
		return dialog;
	}

	@Override
	public void show() {
		if (TextUtils.isEmpty(mMessage)) {
			mMessageTextView.setVisibility(View.GONE);
		} else {
			mMessageTextView.setVisibility(View.VISIBLE);
			mMessageTextView.setText(mMessage);
		}

		if (mShowProgress) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.GONE);
		}

		super.show();
	}
}
