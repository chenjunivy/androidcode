package com.szxx.googleplay.domain;

import java.io.File;

import com.szxx.googleplay.manager.DownloadManager;
import com.szxx.googleplay.uiutils.UIUtils;

/**
 * 注意，添加sd卡的读写权限：
 * 	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
 *  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 *
 */

public class DownloadInfo {
	public String id;
	public String name;
	public String downloadUrl;
	public long size;
	public String packageName;
	
	public int currentState; //当前状态
	public int currentPos; //当先下载位置
	public String apkpath;  //下载apk存放的路径
	
	public static final String GOOGLE_MARKET = "GoogleMarket";  //存放下载后的文件根目录
	public static final String DOWNLOAD = "download";  //存放下载文件的次级目录
	//获取进度
	public float getProgress(){
		if (size == 0) {
			return 0;
		}
		float progress = currentPos/(float)size;
		return progress; 
	}
	
	//获取要下载的软件apk存放路径
	private static String getAPKPath(String name){
/*		StringBuffer path = new StringBuffer();
		String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath(); //获取sd卡目录
		
		path.append(sdcard);
//		path.append("/");  //为了更好兼容，采用File.separator代替系统路径分隔符
		path.append(File.separator);
		path.append(GOOGLE_MARKET);
		path.append(File.separator);  
		path.append(DOWNLOAD);
		
		if (creatFilePath(path.toString())) {
			path.append(File.separator);
			path.append(name + ".apk");
			System.out.println("googlemap的下载路径是：" + path.toString());
			return path.toString();
		}
		
		return null;*/
		
				File sdcardPath = UIUtils.getUIContext().getFilesDir();
		
				
				if (!sdcardPath.exists() || !sdcardPath.isDirectory()) {
					sdcardPath.mkdirs();
				}
				
				String path = sdcardPath.toString() + name + ".apk";
				File file = new File(path);
					System.out.println("创建的googlemarket的自由专区文件是:" + path);
				return path;
	}
	
	//判断创建文件夹是否成功
	private static boolean creatFilePath(String dir){
		File fileDir = new File(dir);
		//如果文件夹不存在或者不是文件夹
		if (!fileDir.exists() || !fileDir.isDirectory()) {
			return fileDir.mkdir();
		}
		return true;
	}
	
	//可以当做工具类使用的方法，类似于构造方法的使用
	public static DownloadInfo copy(AppInfos info){
		DownloadInfo downloadInfo = new DownloadInfo();
		
		downloadInfo.id = info.id;
		downloadInfo.name = info.name;
		downloadInfo.downloadUrl = info.downloadUrl;
		downloadInfo.size = info.size;
		downloadInfo.packageName = info.packageName;
		
		downloadInfo.currentPos = 0;
		downloadInfo.currentState = DownloadManager.STATE_UNDO;
		downloadInfo.apkpath = getAPKPath(info.packageName);
		
		return downloadInfo;
	}
}
