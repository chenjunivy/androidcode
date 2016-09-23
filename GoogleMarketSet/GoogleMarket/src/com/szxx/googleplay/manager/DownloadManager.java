package com.szxx.googleplay.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Intent;
import android.net.Uri;

import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.domain.DownloadInfo;
import com.szxx.googleplay.http.HttpHelper;
import com.szxx.googleplay.http.HttpHelper.HttpResult;
import com.szxx.googleplay.uiutils.IOUtils;
import com.szxx.googleplay.uiutils.UIUtils;

/**
 * 下载管理器 未下载 ，等待下载， 暂停下载， 正在下载， 下载成功，下载失败 DownloadManager
 * 为被观察者，负责通知所有观察者状态和进度发生变化
 * 
 * @author SystemIvy
 * 
 */
public class DownloadManager {
	// 下载状态
	public static final int STATE_UNDO = 0;
	public static final int STATE_WAITING = 1;
	public static final int STATE_PAUSE = 2;
	public static final int STATE_DOWNLOADING = 3;
	public static final int STATE_SUCCESS = 4;
	public static final int STATE_FAIL = 5;

	private static DownloadManager mDmanag = new DownloadManager();
	// 2.观察者集合
	private ArrayList<DownloadObserver> observers = new ArrayList<DownloadManager.DownloadObserver>();

	// 下载应用的集合，要求应用不重复，线程安全
	private ConcurrentHashMap<String, DownloadInfo> mDownloadInfos = new ConcurrentHashMap<String, DownloadInfo>();

	// 下载任务的集合，要求任务不重复，线程安全
	private ConcurrentHashMap<String, DownloadTask> mDownloadTasks = new ConcurrentHashMap<String, DownloadManager.DownloadTask>();

	private DownloadManager() {

	}

	public static DownloadManager getDownloadInstance() {
		return mDmanag;
	}

	// 2.注册观察者
	public void registerDownloadObserver(DownloadObserver observer) {
		if (observer != null && !observers.contains(observer)) {
			observers.add(observer);
		}
	}

	// 3.注销观察者
	public void removeDownloadObserver(DownloadObserver observer) {
		if (observer != null && observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	/**
	 * 1.声明观察者的接口
	 * 
	 */
	public interface DownloadObserver {
		// 下载状态变化
		public void onDownloadStateChanged(DownloadInfo downloadInfo);

		// 下载进度变化
		public void onDownloadProgressChanged(DownloadInfo downloadInfo);
	}

	// 5.通知观察者状态发生变化
	public void notifyDownloadStateChanged(DownloadInfo downloadInfo) {
		for (DownloadObserver mObserver : observers) {
			mObserver.onDownloadStateChanged(downloadInfo);
		}
	}

	// 6.通知观察者进度发生变化
	public void notifyDownloadProgressChanged(DownloadInfo downloadInfo) {
		for (DownloadObserver mObserver : observers) {
			mObserver.onDownloadProgressChanged(downloadInfo);
		}
	}

	// 7.应用下载
	public synchronized void onDownloadApk(AppInfos info) {
		// 如果是第一次下载，重新创建一个下载对象
		// 如果从暂停状态恢复下载，使用断点续传
		if (info != null) {
			String appId = info.id;
			DownloadInfo downloadInfo = mDownloadInfos.get(appId);
			
			if (downloadInfo == null) {
				downloadInfo = DownloadInfo.copy(info);
			}
			
			downloadInfo.currentState = STATE_WAITING; // 等待下载
			notifyDownloadStateChanged(downloadInfo);
			
			mDownloadInfos.put(appId, downloadInfo);
			
			// 新启动一个下载任务，放在线程池中执行
			DownloadTask task = new DownloadTask(downloadInfo);
			ThreadManager.getThreadPool().executeThread(task);
			mDownloadTasks.put(appId, task);
			
		}

	}

	// 8.下载暂停
	public synchronized void onPauseDownload(AppInfos info) {
		// 取出要暂停的下载对象
		DownloadInfo pauseDownload = mDownloadInfos.get(info.id);
		if (pauseDownload != null) {
			// 只有在正在下载状态和等待下载状态才有可能暂停下载
			if (pauseDownload.currentState == STATE_DOWNLOADING
					|| pauseDownload.currentState == STATE_WAITING) {
			
				DownloadTask taskPause = mDownloadTasks.get(pauseDownload.id);
				// 如果下载任务在线程队列中尚未开始，如果暂停下载，可以将任务从下载队列中移除
				// 如果下载任务应经开始，需要在下载任务中暂停下载（在读写流的时候使用状态标识判断是否向文件中继续写入数据）
				if (taskPause != null) {
					ThreadManager.getThreadPool().removeThread(taskPause);
				}
				pauseDownload.currentState = STATE_PAUSE;
				notifyDownloadStateChanged(pauseDownload);
			}
		}
	}

	// 9.应用安装
	public synchronized void onInstallApk(AppInfos info) {
		DownloadInfo appInstall = mDownloadInfos.get(info.id);

		// 调到系统安装界面安装
		if (appInstall != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + appInstall.apkpath),
					"application/vnd.android.package-archive");
			UIUtils.getUIContext().startActivity(intent);
		}
	}

	// 下载耗时任务。放在线程池中的线程来执行
	class DownloadTask implements Runnable {
		private DownloadInfo taskDownload;
		private HttpResult httpResult;

		public DownloadTask(DownloadInfo taskDownload) {
			this.taskDownload = taskDownload;
		}

		@Override
		public void run() {
			System.out.println("开始下载任务");
			taskDownload.currentState = STATE_DOWNLOADING;
			notifyDownloadStateChanged(taskDownload);
			File file = new File(taskDownload.apkpath);
			// 如果文件不存在，或者是先前下载的文件长度与当前下载对象的文件长度不一致，(即先前下载的文件出现丢失字节的情况)
			if (!file.exists() || file.length() != taskDownload.currentPos
					|| taskDownload.currentPos == 0) {
				// 正式下载
				file.delete();// 文件清空，如果没有文件，只是没有效果，不会报空指针异常
				taskDownload.currentPos = 0; // 当前位置置零
				
				// 从头开始下载
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + taskDownload.downloadUrl);

			} else {
				// 断点续传
				// range 表示请求服务器从文件的哪个位置开始返回数据
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + taskDownload.downloadUrl
						+ "&range=" + file.length());
			}
			
			InputStream in = null;
			FileOutputStream out = null;
			if (httpResult != null && (in = httpResult.getInputStream()) != null) {

				try {
					out = new FileOutputStream(file, true); // 在原有文件的基础上增减内容，防止覆盖已经下载的部分文件

					int len = 0;
					byte[] buffer = new byte[1024];
					// 判读当前状态是否是正在下载，如果是，向文件中写入数据，如果不是，按照网络异常处理，正在下载时暂停下载按照网络处理
					while ((len = in.read(buffer)) != -1
							&& taskDownload.currentState == STATE_DOWNLOADING) {
						out.write(buffer, 0, len);
						out.flush(); // 为防止最后写入文件数据丢失字节

						taskDownload.currentPos += len;
						notifyDownloadProgressChanged(taskDownload);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtils.close(in);
					IOUtils.close(out);
				}
				// 文件下载结束
				if (file.length() == taskDownload.size) {
					// 文件完整，表示下载结束
					taskDownload.currentState = STATE_SUCCESS;
					notifyDownloadStateChanged(taskDownload);
				} else if (taskDownload.currentState == STATE_PAUSE) {
					// 中途暂停
					notifyDownloadStateChanged(taskDownload);
				} else {
					// 下载失败
					System.out.println("下载文件长度不够");
					file.delete();
					taskDownload.currentState = STATE_FAIL;
					taskDownload.currentPos = 0;
					notifyDownloadStateChanged(taskDownload);
				}
			} else {
				// 网络异常
				System.out.println("网络异常:");
				file.delete(); // 清除无效数据
				taskDownload.currentState = STATE_FAIL;
				taskDownload.currentPos = 0;
				notifyDownloadStateChanged(taskDownload);
			}
				mDownloadTasks.remove(taskDownload.id);  //从下载任务集合中
		}
	}
	//获取下载对象
	public DownloadInfo getDownloadInfo(AppInfos info){
		return mDownloadInfos.get(info.id);
	}

}
