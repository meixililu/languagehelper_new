package com.messi.languagehelper.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.messi.languagehelper.bean.BDORCItem;
import com.messi.languagehelper.bean.BaiduOcrRoot;

public class CameraUtil {

	public static final int MAX_POST_SIZE = 300 * 1024;
	public static final int REQUEST_CODE_CAPTURE_CAMEIA = 1000;
	public static final int REQUEST_CODE_PICK_IMAGE = 1001;
	public static final int PHOTO_PICKED_WITH_DATA = 1002;

	public static File createImageFile() throws IOException {
		String dir = SDCardUtil.getDownloadPath(SDCardUtil.ImgPath);
		File image = new File(dir + "tran_orc.jpg");
		return image;
	}

	public static String createTempFile() throws IOException {
		// Create an image file name
		return SDCardUtil.getDownloadPath(SDCardUtil.ImgPath) + "tran_temp.jpg";
	}

	public static String getOcrResult(BaiduOcrRoot mBaiduOcrRoot) {
		StringBuilder sb = new StringBuilder();
		if (mBaiduOcrRoot.getWords_result() != null) {
			for (BDORCItem item : mBaiduOcrRoot.getWords_result()) {
				sb.append(item.getWords());
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public static File getFile(String srcPath) {
		File mFile = new File(srcPath);
		if (mFile.length() > MAX_POST_SIZE) {
			compressImage(srcPath);
		}
		return new File(srcPath);
	}

	public static void compressImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f
        float ww = 800f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 300) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 5;// 每次都减少5
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap newBitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		saveBitmap(newBitmap,srcPath);
	}

	public static String encodeBase64File(String path) throws Exception {
		LogUtil.DefalutLog("srcPath:"+path);
		File  file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int)file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer,Base64.DEFAULT);
	}

	public static String getImageBase64(String srcPath) {
//		BitmapFactory.Options newOpts = new BitmapFactory.Options();
//		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
//		newOpts.inJustDecodeBounds = true;
//		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
//
//		newOpts.inJustDecodeBounds = false;
//		int w = newOpts.outWidth;
//		int h = newOpts.outHeight;
//		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//		float hh = 2048f;//这里设置高度为800f
//		float ww = 2048f;//这里设置宽度为480f
//		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//		int be = 1;//be=1表示不缩放
//		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//			be = (int) (newOpts.outWidth / ww);
//		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//			be = (int) (newOpts.outHeight / hh);
//		}
//		if (be <= 0)
//			be = 1;
//		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		LogUtil.DefalutLog("srcPath:"+srcPath);
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//		int options = 100;
//		LogUtil.DefalutLog("orc_img_width:"+w+"---orc_img_height:"+h+"---be:"+be+"--size:"+(baos.toByteArray().length / 1024));
//		while (baos.toByteArray().length / 1024 > 4000) {
//			baos.reset();// 重置baos即清空baos
//			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//			options -= 5;// 每次都减少5
//		}
		return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
	}

	public static void saveBitmap(Bitmap bitmap, String srcPath){
		File file = new File(srcPath);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmapFromBase64(String base64){
		Bitmap bitmap = null;
		try {
			byte[]bitmapArray;
			bitmapArray = Base64.decode(base64, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
