package com.yunmall.ymsdk.net.http.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工作线程池，在该线程中处理请求后的数据处理
 * 容量为2
 * Created by Zhp on 2014/5/22.
 */
public class CacheThreadPool {
	private static final String TAG = CacheThreadPool.class.getSimpleName();
	private static CacheThreadPool instance = null;
    private ExecutorService mExecutorService;

	public static CacheThreadPool getInstance() {
		if (instance == null) {
			synchronized (CacheThreadPool.class) {
				if (instance == null) {
					instance = new CacheThreadPool();
				}
			}
		}
		return instance;
	}


	private CacheThreadPool() {
        mExecutorService = Executors.newFixedThreadPool(2);
    }

    public void submit(Runnable runnable) {
        if (mExecutorService != null) {
            mExecutorService.submit(runnable);
        }
    }

	/**
	 * 终止工作线程
	 */
	public void stopWorking() {
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
    }

}
