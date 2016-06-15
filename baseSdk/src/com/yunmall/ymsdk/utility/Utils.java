package com.yunmall.ymsdk.utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.ByteArrayOutputStream;

/**
 * 普通工具类
 * Created by Zhp on 2014/5/16.
 */
public class Utils {
	
	/**
	 * added by zcq on 2014/06/20
	 * 验证手机号码（客户端只验证手机号码长度）
	 */
	public static Boolean verifyPhone(String phone){
		if(!TextUtils.isEmpty(phone) && phone.trim().length()==11){
			return true;
		}else {
			return false;
		}
	}
	 public static void scrollToTop(PullToRefreshListView listView) {
	        listView.getRefreshableView().smoothScrollBy(0, 0);
	        listView.getRefreshableView().setSelection(0);
	    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取通知栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        if (statusBarHeight == 0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusBarHeight = activity.getResources().getDimensionPixelSize(i5);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (InstantiationException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
            }


        }
        return statusBarHeight;
    }
}
