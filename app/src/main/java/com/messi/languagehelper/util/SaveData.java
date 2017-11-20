package com.messi.languagehelper.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.thirdparty.T;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.List;

public class SaveData {

    public static void saveDataAsJson(Context mContext,String name,String json){
        SharedPreferences sp = Settings.getSharedPreferences(mContext);
        Settings.saveSharedPreferences(sp, name, json);
    }

    public static <T> T getDataFonJson(Context mContext,String name,Class<T> classname){
        SharedPreferences sp = Settings.getSharedPreferences(mContext);
        String json = sp.getString(name,"");
        return new Gson().fromJson(json,classname);
    }

    public static void saveDataListAsJson(Context mContext,String name,List<?> json){
        SharedPreferences sp = Settings.getSharedPreferences(mContext);
        Settings.saveSharedPreferences(sp, name, new Gson().toJson(json));
    }

    public static <T> T getDataListFonJson(Context mContext, String name, Type type){
        SharedPreferences sp = Settings.getSharedPreferences(mContext);
        String json = sp.getString(name,"");
        return new Gson().fromJson(json,type);
    }

	/**
	* @function 将一个对象保存到本地 
    * @author D-light 
    * @time 2014-07-23 
    * @param String name 
    * @return void 
    */  
   public static void saveObject(Context mContext,String name,Object object){  
       FileOutputStream fos = null;  
       ObjectOutputStream oos = null;  
       try {  
           fos = mContext.openFileOutput(name, Activity.MODE_PRIVATE);  
           oos = new ObjectOutputStream(fos);  
           oos.writeObject(object);  
       } catch (Exception e) {  
           e.printStackTrace();  
           //这里是保存文件产生异常  
       } finally {  
           if (fos != null){  
               try {  
                   fos.close();  
               } catch (IOException e) {  
                   //fos流关闭异常  
                   e.printStackTrace();  
               }  
           }  
           if (oos != null){  
               try {  
                   oos.close();  
               } catch (IOException e) {  
                   //oos流关闭异常  
                   e.printStackTrace();  
               }  
           }  
       }  
   }  
	
   /**
   * @function 从本地读取保存的对象 
   * @author D-light 
   * @time 2014-07-23 
   * @param String name 
   * @return Object 
   */  
   public static Object getObject(Context mContext,String name){  
      FileInputStream fis = null;  
      ObjectInputStream ois = null;  
      try {  
          fis = mContext.openFileInput(name);  
          ois = new ObjectInputStream(fis);  
          return ois.readObject();  
      } catch (Exception e) {  
          e.printStackTrace();  
          //这里是读取文件产生异常  
      } finally {  
          if (fis != null){  
              try {  
                  fis.close();  
              } catch (IOException e) {  
                  //fis流关闭异常  
                  e.printStackTrace();  
              }  
          }  
          if (ois != null){  
              try {  
                  ois.close();  
              } catch (IOException e) {  
                  //ois流关闭异常  
                  e.printStackTrace();  
              }  
          }  
      }  
      //读取产生异常，返回null  
      return null;  
   }  
   
   public static boolean isFileExists(Context mContext,String name){
	   File file = new File(mContext.getFilesDir(), name);
	   return file.exists();
   }
   
   public static void deleteObject(Context mContext,String name){
	   File file = new File(mContext.getFilesDir(), name);
	   if(file.exists()){
		  file.delete();  
	   }
   }
	
}
