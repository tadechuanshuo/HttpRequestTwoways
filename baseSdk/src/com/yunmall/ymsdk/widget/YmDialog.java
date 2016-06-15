package com.yunmall.ymsdk.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yunmall.ymsdk.R;

/**
 * 自定义
 * @author Administrator
 *
 */
public class YmDialog extends Dialog {

	protected YmDialog(Context context) {
		super(context);
	}

	protected YmDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {

		private Context context;

		private String title;
		private String content;
		private String leftBtnStr;
		private String rightBtnStr;
		private DialogInterface.OnClickListener leftBtnClickListener;
		private DialogInterface.OnClickListener rightBtnClickListener;
		private int curIndex;
		
		@SuppressWarnings("rawtypes")
		private YmListDialogInterface impl;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

        public Builder setTitle(int titleId) {
            this.title = context.getString(titleId);
            return this;
        }

		public Builder setContent(String content) {
			this.content = content;
			return this;
		}

        public Builder setContent(int contentId) {
            this.content = context.getString(contentId);
            return this;
        }

		public Builder setLeftBtn(String leftBtnStr, DialogInterface.OnClickListener leftBtnClickListener) {
			this.leftBtnStr = leftBtnStr;
			this.leftBtnClickListener = leftBtnClickListener;
			return this;
		}

        public Builder setLeftBtn(int leftBtnId, DialogInterface.OnClickListener leftBtnClickListener) {
            return setLeftBtn(context.getString(leftBtnId), leftBtnClickListener);
        }

		public Builder setRightBtn(String rightBtnStr, DialogInterface.OnClickListener rightBtnClickListener) {
			this.rightBtnStr = rightBtnStr;
			this.rightBtnClickListener = rightBtnClickListener;
			return this;
		}

        public Builder setRightBtn(int rightBtnId, DialogInterface.OnClickListener rightBtnClickListener) {
            return setRightBtn(context.getString(rightBtnId), rightBtnClickListener);
        }

        public Builder setItems(int strArrayId, int curIndex, final OnItemClickListener listener) {
            String[] items = context.getResources().getStringArray(strArrayId);
           return setItems(items, curIndex, listener);
        }

		@SuppressWarnings("rawtypes")
		public Builder setItems(final String[] items, final int curIndex, final OnItemClickListener listener) {
			YmListDialogInterface impl = new YmListDialogInterface<String>() {

				@Override
				public void onItemClicked(AdapterView<?> parent, View view, int position, long id, String item) {
					if (listener != null) {
						listener.onItemClick(parent, view, position, id);
					}
				}

				@Override
				public View getView(int position, String item, View convertView) {
                    if (convertView == null) {
                        convertView = View.inflate(context, R.layout.ymsdk_widget_ymdialog_item, null);
                    }
                    convertView.setBackgroundResource(position == curIndex ? R.drawable.ymdialog_btn_pressed : R.drawable.ymsdk_widget_ymdialog_btn_bg);

					TextView tv = (TextView) convertView.findViewById(R.id.text);
					tv.setText(item);

					return convertView;
				}

				@Override
				public String[] getData() {
					// TODO Auto-generated method stub
					return items;
				}

			};

			return setItems(curIndex, impl);
		}

		@SuppressWarnings("rawtypes")
		public Builder setItems(int curIndex, YmListDialogInterface impl) {
			this.curIndex = curIndex;
			this.impl = impl;
			return this;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public YmDialog create() {
			final YmDialog dialog = new YmDialog(context, android.R.style.Theme_Dialog);
			dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			View layout = LayoutInflater.from(context).inflate(R.layout.ymsdk_widget_ymdialog, null);
			dialog.setContentView(layout);
			if(this.impl != null && this.impl.getData().length > 5){
			  dialog.adjustWidthAndHeight(true);
			}
			dialog.setCanceledOnTouchOutside(false);

			TextView titleTv = (TextView) layout.findViewById(R.id.ymdialog_title);
			TextView contentTv = (TextView) layout.findViewById(R.id.ymdialog_content);
			ListView listView = (ListView) layout.findViewById(R.id.ymdialog_listview);
			Button leftBtn = (Button) layout.findViewById(R.id.ymdialog_left_button);
			Button rightBtn = (Button) layout.findViewById(R.id.ymdialog_right_button);

			View titleSeperator = layout.findViewById(R.id.ymdialog_title_seperator);
			View contentSeperator = layout.findViewById(R.id.ymdialog_content_seperator);
			View btnSeperator = layout.findViewById(R.id.ymdialog_button_seperator);

			// 设置标题
			if (!TextUtils.isEmpty(this.title)) {
				titleTv.setText(this.title);
			} else {
				titleTv.setVisibility(View.GONE);
				titleSeperator.setVisibility(View.GONE);
			}

			// 设置内容
			if (!TextUtils.isEmpty(this.content)) {
				contentTv.setText(this.content);
			} else {
				contentTv.setVisibility(View.GONE);
				contentSeperator.setVisibility(View.GONE);
			}

			// 设置列表选项
			if (impl != null) {
				final Object[] data = impl.getData();
				if (curIndex >= 0 && curIndex < data.length) {
					listView.setSelection(curIndex);
				}

				listView.setAdapter(new InternalAdapter(data, impl));
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						impl.onItemClicked(parent, view, position, id, data[position]);
						dialog.dismiss();
					}
				});
			} else {
				listView.setVisibility(View.GONE);
			}

			// 设置左按键
			if (!TextUtils.isEmpty(this.leftBtnStr)) {
				leftBtn.setText(leftBtnStr);
				leftBtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (leftBtnClickListener != null) {
							leftBtnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
						dialog.dismiss();
					}
				});
			} else {
				leftBtn.setVisibility(View.GONE);
				btnSeperator.setVisibility(View.GONE);
			}

			// 设置右按键
			if (!TextUtils.isEmpty(this.rightBtnStr)) {
				rightBtn.setText(rightBtnStr);
				rightBtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (rightBtnClickListener != null) {
							rightBtnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						}
						dialog.dismiss();
					}
				});
			} else {
				rightBtn.setVisibility(View.GONE);
				btnSeperator.setVisibility(View.GONE);
			}

			return dialog;
		}
	}
	

	private void adjustWidthAndHeight(boolean ajdustHeight) {
		int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

		LayoutParams params = getWindow().getAttributes();
		if (ajdustHeight) {
			params.height = screenHeight - dip2px(140);
		}
		params.width = screenWidth - dip2px(40f);
		getWindow().setAttributes(params);
	}

	private int dip2px(float dpValue) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5f);
	}

	public static interface YmListDialogInterface<T> {
		public T[] getData();

		public View getView(int position, T item, View convertView);

		public void onItemClicked(AdapterView<?> parent, View view, int position, long id, T item);
	}

	private static class InternalAdapter<T> extends BaseAdapter {

		private T[] data;
		private YmListDialogInterface<T> impl;

		public InternalAdapter(T[] data, YmListDialogInterface<T> impl) {
			this.data = data;
			this.impl = impl;
		}

		@Override
		public int getCount() {
			return data == null ? 0 : data.length;
		}

		@Override
		public T getItem(int position) {
			return data[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			return impl.getView(position, getItem(position), convertView);
		}
	}

}
