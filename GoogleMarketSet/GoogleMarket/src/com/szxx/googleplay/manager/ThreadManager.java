package com.szxx.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理器，管理线程池
 * 
 * @author System_ivy
 * 
 */
public class ThreadManager {
	private static ThreadPool mthreaPool;

	public static ThreadPool getThreadPool() {
		if (mthreaPool == null) {
			synchronized (ThreadManager.class) {
				if (mthreaPool == null) {
					int cpuCount = Runtime.getRuntime().availableProcessors(); //获取cpu数量
					System.out.println("cpu 个数是 :" + cpuCount);
					int threadCount = 10;
					mthreaPool = new ThreadPool(threadCount, threadCount, 1L);
				}
			}
		}
		return mthreaPool;
	}

	// 线程池
	public static class ThreadPool {
		private int corePoolSize; // 核心线程数
		private int maximumPoolSize; // 最大线程数
		private long keepAliveTime; // 休息时间
		private ThreadPoolExecutor executor;

		private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		//添加线程到线程池中执行
		public void executeThread(Runnable r) {
			/**
			 * 参数：1.核心数 2.最大线程数 3.线程休眠时间 4.时间单位 5.线程队列  6.线程工厂  7、线程异常处理
			 */
			if (executor == null) {
				executor = new ThreadPoolExecutor(corePoolSize,
						maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
						new LinkedBlockingDeque<Runnable>(),
						Executors.defaultThreadFactory(), new AbortPolicy());
			}
			//执行一个Runnable对象
			executor.execute(r);
		}
		
		//将线程从线程中移除
		public void removeThread(Runnable r){
			executor.getQueue().remove(r);
		}
	}
}
