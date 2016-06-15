/**
 * WengLog
 * @author dongjianbo
 * log工具类
 * Copyright (c) 2011年 mafengwo. All rights reserved.
 */

package com.yunmall.ymsdk.utility;

import android.os.Environment;
import android.util.Log;

import com.yunmall.ymsdk.BuildConfig;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Log管理类
 */
public class YmLog {
    public static final boolean LOG_TOGGLE = BuildConfig.DEBUG;
    public static final boolean thread_toggle = true;
	private static final String LOG_FILE = Environment.getExternalStorageDirectory().getPath() + "ym_log.txt";
	private static Date date;
	public static SimpleDateFormat sd = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS] ", Locale.US);
	
    public static void v(String tag, String msg) {
        if (!LOG_TOGGLE) {
            return;
        }

        Log.v(tag, formatMessage(msg));
    }

    public static void v(String TAG, String format, Object... params) {
        if (!LOG_TOGGLE) {
            return;
        }
        if (params == null) {
            return;
        }

        date = new Date();

        String logInfo = String.format(format, params);
        logInfo = sd.format(date) + logInfo;
        Log.v(TAG, formatMessage(logInfo));
    }

	public static void d(String TAG, String msg) {
		if (!LOG_TOGGLE) {
			return;
		}

		Log.d(TAG, formatMessage(msg));
	}

    public static void d(String TAG, String format, Object... params) {
		if (!LOG_TOGGLE) {
			return;
		}
		if (params == null) {
			return;
		}

		date = new Date();

		String logInfo = String.format(format, params);
		logInfo = sd.format(date) + logInfo;
		Log.d(TAG, formatMessage(logInfo));
	}

	public static void i(String tag, String msg) {
        if (!LOG_TOGGLE) {
            return;
        }

        Log.i(tag, formatMessage(msg));
    }

	public static void i(String TAG, String format, Object... params) {
        if (!LOG_TOGGLE) {
            return;
        }
        if (params == null) {
            return;
        }

        date = new Date();

        String logInfo = String.format(format, params);
        logInfo = sd.format(date) + logInfo;
        Log.i(TAG, formatMessage(logInfo));
    }

    public static void w(String TAG, String msg) {
        if (!LOG_TOGGLE) {
            return;
        }
        String logInfo = sd.format(new Date()) + msg;
        Log.w(TAG, formatMessage(logInfo));
    }

	public static void w(String TAG, String format, Object... params) {
		if (!LOG_TOGGLE) {
			return;
		}
		if (params == null) {
			return;
		}

		date = new Date();

		String logInfo = String.format(format, params);
		logInfo = sd.format(date) + logInfo;
		Log.w(TAG, formatMessage(logInfo));
	}

	public static void e(String TAG, String msg) {
        if (!LOG_TOGGLE) {
            return;
        }
		Log.e(TAG, formatMessage(msg));
	}

	public static void e(String TAG, String format, Throwable e) {
        if (!LOG_TOGGLE) {
            return;
        }
		Log.e(TAG, format, e);
	}

	public static void f(String TAG, String msg) {
		if (!LOG_TOGGLE) {
			return;
		}
		try {
			FileWriter fw = new FileWriter(LOG_FILE, true);
			fw.write(sd.format(new Date()) + TAG + "\n\t" + formatMessage(msg) + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String GET_THREAD_INFO(Thread th) {
		StringBuilder sb = new StringBuilder("");
		if (th != null) {
			sb.append("Thread<id:").append(th.getId()).append(">");
		}

		return sb.toString();
	}

    public static void logHttpResponse(String TAG, String content) {
        if (LOG_TOGGLE) {
            int logcatSize = 1024 * 4 - 40;
            if (content != null) {
                for (int i = 0, j = content.length() / logcatSize; i <= j; i++) {
                    Log.v(TAG,
                            content.substring(i == 0 ? 0 : i * logcatSize - 1,
                                    i == j ? content.length() : (i + 1) * logcatSize - 1)
                                    + ""
                    );
                }
            }
        }
    }

    private static String formatMessage(String msg) {
        return thread_toggle ? ("[" + getCurrentThreadName() + "]" + msg) : msg;
    }

    private static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }
}
