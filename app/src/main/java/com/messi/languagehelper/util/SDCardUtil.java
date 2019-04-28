package com.messi.languagehelper.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SDCardUtil {

	/**sd卡保存文件夹名称**/
	public static final String RootPath = "/zyhy/";
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
	public static final String Delimiter = "/";

	/**sdcard路径
	 * @return
	 */
	public static String getDownloadPath(String sdCardPath) {
		File SDdir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			SDdir = Environment.getExternalStorageDirectory();
		}
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
	public static String getDownloadPath(String sdCardPath,Context context,String Type) {
		File SDdir = context.getExternalFilesDir(Type);
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

	public static String saveFile(Context mContext,String path,String name,String content){
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
	public static boolean deleteFile(String sPath) throws Exception{  
		File file = new File(sPath);  
	    /**路径为文件且不为空则进行删除**/  
	    if (file.isFile() && file.exists()) {  
	        return file.delete();  
	    }  
	    return false;  
	}

}
