package com.messi.languagehelper.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.messi.languagehelper.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class SDCardUtil {

	/**sd卡保存文件夹名称**/
	public static String RootPath = "/zyhy/";
	public static final String sdPath = "/zyhy/audio/";
	public static final String ImgPath = "/zyhy/img/";
	public static final String SymbolPath = "/zyhy/audio/symbol/";
	public static final String WordStudyPath = "/zyhy/audio/wordstudy/";
	public static final String CompositionPath = "/zyhy/audio/composition/";
	public static final String UserPracticePath = "/zyhy/audio/practice/user/";
	public static final String PracticePath = "/zyhy/audio/practice/en/";
	public static final String StudyDialogPath = "/zyhy/audio/study/dialog/";
	public static final String EvaluationPath = "/zyhy/audio/study/evaluation/tts";
	public static final String SpokenEnglishPath = "/zyhy/audio/study/evaluation/";
	public static final String EvaluationUserPath = "/zyhy/audio/study/evaluation/user/";
	public static final String DailySentencePath = "/zyhy/audio/study/dailysentence/mp3/";
	public static final String ReadingPath = "/zyhy/audio/study/reading/mp3/";
	public static final String apkPath = "/zyhy/apps/download/";
	public static final String apkUpdatePath = "/zyhy/apps/update/";
	public static final String lrcPath = "/zyhy/lrc/voa/";
	public static final String OfflineDicPath = "/zyhy/offline/";
	public static final String OfflineDicPathRoot = "/Android/data/com.messi.languagehelper/files/zyhy/offline/";
	public static final String Delimiter = "/";

	//sd old
//	public static String getSDPath(String sdCardPath) {
//		File SDdir = null;
//		boolean sdCardExist = Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED);
//		if (sdCardExist) {
//			SDdir = Environment.getExternalStorageDirectory();
//		}
//		if (SDdir != null) {
//			String path = SDdir.getPath() + sdCardPath;
//			isFileExistsOrCreate(path);
//			return path;
//		} else {
//			return "";
//		}
//	}

	/**sdcard路径
	 * @return
	 */
	public static String getDownloadPath(String sdCardPath) {
		File SDdir = BaseApplication.instance.getExternalFilesDir(null);
		LogUtil.DefalutLog("SDdir:"+SDdir.getPath());
		if (SDdir != null) {
			String path = SDdir.getPath() + sdCardPath;
			isFileExistsOrCreate(path);
			return path;
		} else {
			return "";
		}
	}

	/**sdcard路径
	 * @return
	 */
	public static String getDownloadPath(Context context, String sdCardPath) {
		File SDdir = context.getExternalFilesDir(null);
		if (SDdir != null) {
			String path = SDdir.getPath() + sdCardPath;
			isFileExistsOrCreate(path);
			return path;
		} else {
			return "";
		}
	}
	
	public static void isFileExistsOrCreate(String path){
		File sdDir = new File(path);
		if(!sdDir.exists()){
			sdDir.mkdirs();
		}
	}

	public static String saveFile(String path,String name,String content){
		try{
			String filePath = getDownloadPath(path) + name;
			LogUtil.DefalutLog("filePath:"+filePath);
			File txtFile = new File(filePath);
			txtFile.createNewFile();
			FileWriter fw = new FileWriter(txtFile);
			fw.flush();
			fw.write(content);
			fw.close();
			return txtFile.getAbsolutePath();
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	public static String saveBitmap(Context mContext, Bitmap bitmap) throws IOException {
		String sdcardDir = getDownloadPath(ImgPath);
		String filePath = "";
		if(!TextUtils.isEmpty(sdcardDir)){
			filePath = sdcardDir + "image_" + System.currentTimeMillis() + ".png";
			File file = new File(filePath);
			file.createNewFile();
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
					out.flush();
					out.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			ToastUtil.diaplayMesShort(mContext, "请插入SD卡");
		}
		return filePath;
	}
	
	public static String saveBitmap(Context mContext, Bitmap bitmap, String name) throws IOException {
		String sdcardDir = getDownloadPath(ImgPath);
		String filePath = "";
		if(!TextUtils.isEmpty(sdcardDir)){
			filePath = sdcardDir + name;
			File file = new File(filePath);
			file.createNewFile();
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
					out.flush();
					out.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			ToastUtil.diaplayMesShort(mContext, "请插入SD卡");
		}
		return filePath;
	}
	
	/**删除内部存储中之前下载的文件
	 */
	public static void deleteOldFile() {
		try {
			String path = getDownloadPath(SDCardUtil.sdPath);
			File file = new File(path);
			deleteFileInDir(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**删除文件夹里面的所有文件
	 * @param cacheDir
	 */
	public static void deleteFileInDir(File cacheDir) throws Exception{
		if(cacheDir.isDirectory()){
			File[] files = cacheDir.listFiles();  
			for (int i = 0; i < files.length; i++) {  
				if (files[i].isFile()) {  
					boolean flag = deleteFile(files[i].getAbsolutePath());  
					if (!flag) break;  
				} 
			}
		}
	}
	
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	public static long getFileSize(String filePath) {
		File file = new File(filePath);
		if(file.exists()){
			return file.length();
		}else {
			return 0;
		}
	}
	
	/**删除文件夹里面的单个文件
	 * @param sPath
	 * @return
	 */
	public static boolean deleteFile(String sPath){
		try {
			File file = new File(sPath);
			/**路径为文件且不为空则进行删除**/
			if (file.isFile() && file.exists()) {
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void deleteContent(String... contents){
		try {
			new Thread(() -> {
				if (contents != null) {
					for(String item : contents){
						deleteContent(item);
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteContent(String content){
		try {
			String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
			String md5 = MD5.encode(content);
			String mp3Path = path + md5 + ".mp3";
			String pcmPath = path + md5 + ".pcm";
			deleteFile(mp3Path);
			deleteFile(pcmPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static long getFolderSize(String[] paths){
		long size = 0;
		try {
			if (paths != null) {
				for (String item : paths){
					String path = SDCardUtil.getDownloadPath(item);
					File file = new File(path);
					size += getFolderSize(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static long getFolderSize(File file) {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static void deleteFolderFiles(String[] paths){
		if (paths != null) {
			for(String item : paths){
				String path = SDCardUtil.getDownloadPath(item);
				deleteFolderFile(path,true);
			}
		}
	}

	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (file.isDirectory()) {
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) {// 如果是文件，删除
						file.delete();
					} else {// 目录
						if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
							file.delete();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if(fileS == 0){
			return wrongSize;
		}
		if (fileS < 1024){
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576){
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824){
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else{
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}
}
