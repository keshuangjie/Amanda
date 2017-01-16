package com.sinaapp.whutec.amanda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Properties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sinaapp.whutec.amanda.bean.Mood;

public class FileUtil {
	
	/**
	 * 备份心情数据到文件
	 * @param objectList
	 * @param path
	 */
	public static void export(List<Mood> objectList, String path){
		System.out.println(path);
		List<Mood> list = objectList;
		list.add(null);
		File file = new File(path);
		createParentFile(file);
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			for(Object o :list){
				out.writeObject(o);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createParentFile(File file){
		if(file.exists()){
			return;
		}
		file.mkdir();
		File parent = file.getParentFile();
		createParentFile(parent);
	}
	
	/**
	 * 将图片文件转换成bitmap
	 * @param file
	 * @return
	 */
	public static Bitmap getImageFile(File file){
		Bitmap bmp = null;
		if(file.exists()){
			try {
				FileInputStream in = new FileInputStream(file);
				bmp = BitmapFactory.decodeStream(in);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
		return bmp;
	}
	
	/**
	 * 将bitmap保存到文件中
	 * @param file
	 * @param bitmap
	 */
	public static void saveImageFile(File file, Bitmap bitmap){
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存properties文件
	 * @param file
	 * @param p
	 */
	public static void setProperties(File file, Properties p){
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			p.store(fos, "");
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 读取properties文件
	 * @param file
	 * @return
	 */
	public static Properties getProperties(File file){
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileInputStream fis = null;
		Properties p = new Properties();
		try {
			fis = new FileInputStream(file);
			p.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return p;
	}
}
