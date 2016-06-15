/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yunmall.ymsdk.net.http;

import android.os.SystemClock;
import android.util.Log;
import com.yunmall.ymsdk.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Logging helper class.<br>
 *  Enable Log:adb shell setprop log.tag.YmHttpLog VERBOSE<br>
 *  Output like:<br>
 *    [211] MarkerLog.finish: (260  ms) finish<br>
 *    [211] MarkerLog.finish: (+0   ) [209] cache-hit<br>
 *    [211] MarkerLog.finish: (+5   ) [ 1] start post for: https://mobileapi.yunmall.com/api/mobileap...<br>
 *    [211] MarkerLog.finish: (+241 ) [209] save-response in: /data/data/...<br>
 *    [211] MarkerLog.finish: (+0   ) [209] cache-saved<br>
 *    [211] MarkerLog.finish: (+3   ) [209] success statuscode: 200 lenghth: 1493<br>
 *    [211] MarkerLog.finish: (+2   ) [211] parsed http response: {...}<br>
 */
public class YmHttpLog {
    private static String TAG = "YmHttpLog";

    public static boolean DEBUG = BuildConfig.DEBUG;

    /**
     * Customize the log tag for your application, so that other apps
     * using Volley don't mix their logs with yours.
     * <br />
     * Enable the log property for your tag before starting your app:
     * <br />
     * {@code adb shell setprop log.tag.&lt;tag&gt;}
     */
    public static void setTag(String tag) {
        d("Changing log tag to %s", tag);
        TAG = tag;

        // Reinitialize the DEBUG "constant"
        DEBUG = Log.isLoggable(TAG, Log.VERBOSE);
    }

    public static void v(String format, Object... args) {
        if (DEBUG) {
            Log.v(TAG, buildMessage(format, args));
        }
    }

    public static void d(String format, Object... args) {
        String content = buildMessage(format, args);
        int logcatSize = 1024 * 4 - 40;
        if (content != null && content.length() > logcatSize) {
            for (int i = 0, j = content.length() / logcatSize; i <= j; i++) {
                Log.d(TAG,
                        content.substring(i == 0 ? 0 : i * logcatSize - 1,
                                i == j ? content.length() : (i + 1) * logcatSize - 1)
                                + ""
                );
            }
        } else {
            Log.d(TAG, content);
        }
    }

    public static void e(String format, Object... args) {
        Log.e(TAG, buildMessage(format, args));
    }

    public static void e(Throwable tr, String format, Object... args) {
        Log.e(TAG, buildMessage(format, args), tr);
    }

    public static void wtf(String format, Object... args) {
        Log.wtf(TAG, buildMessage(format, args));
    }

    public static void wtf(Throwable tr, String format, Object... args) {
        Log.wtf(TAG, buildMessage(format, args), tr);
    }

    /**
     * Will encode url, if not disabled, and adds params on the end of it
     *
     * @param url             String with URL, should be valid URL without params
     * @param params          RequestParams to be appended on the end of URL
     * @return encoded url if requested with params appended if any available
     */
    public static String getUrlWithQueryStringForLog(String url, RequestParams params) {
        if (params != null) {
            // Construct the query string and trim it, in case it
            // includes any excessive white spaces.
            String paramString = params.toString();

            // Only add the query string if it isn't empty and it
            // isn't equal to '?'.
            if (!paramString.equals("") && !paramString.equals("?")) {
                url += url.contains("?") ? "&" : "?";
                url += paramString;
            }
        }

        return url;
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of VolleyLog.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(YmHttpLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg);
    }

    /**
     * A simple event log with records containing a name, thread ID, and timestamp.
     */
    static class MarkerLog {
        public static final boolean ENABLED = YmHttpLog.DEBUG;

        /** Minimum duration from first marker to last in an marker log to warrant logging. */
        private static final long MIN_DURATION_FOR_LOGGING_MS = 0;

        private static class Marker {
            public final String name;
            public final long thread;
            public final long time;

            public Marker(String name, long thread, long time) {
                this.name = name;
                this.thread = thread;
                this.time = time;
            }
        }

        private final List<Marker> mMarkers = new ArrayList<Marker>();
        private boolean mFinished = false;

        /** Adds a marker to this log with the specified name. */
        public synchronized void add(String name, long threadId) {
            if (mFinished) {
                return;
//                throw new IllegalStateException("Marker added to finished log");
            }

            mMarkers.add(new Marker(name, threadId, SystemClock.elapsedRealtime()));
        }

        /**
         * Closes the log, dumping it to logcat if the time difference between
         * the first and last markers is greater than {@link #MIN_DURATION_FOR_LOGGING_MS}.
         * @param header Header string to print above the marker log.
         */
        public synchronized void finish(String header) {
            mFinished = true;

            long duration = getTotalDuration();
            if (duration <= MIN_DURATION_FOR_LOGGING_MS) {
                return;
            }

            long prevTime = mMarkers.get(0).time;
            d("(%-4d ms) %s", duration, header);
            for (Marker marker : mMarkers) {
                long thisTime = marker.time;
                d("(+%-4d) [%2d] %s", (thisTime - prevTime), marker.thread, marker.name);
                prevTime = thisTime;
            }
        }

        @Override
        protected void finalize() throws Throwable {
            // Catch requests that have been collected (and hence end-of-lifed)
            // but had no debugging output printed for them.
            if (!mFinished) {
                finish("Request on the loose");
                e("Marker log finalized without finish() - uncaught exit point for request");
            }
        }

        /** Returns the time difference between the first and last events in this log. */
        private long getTotalDuration() {
            if (mMarkers.size() == 0) {
                return 0;
            }

            long first = mMarkers.get(0).time;
            long last = mMarkers.get(mMarkers.size() - 1).time;
            return last - first;
        }
    }
}
