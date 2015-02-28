package com.sinaapp.whutec.amanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.db.DataProvider;
import com.sinaapp.whutec.amanda.util.FileUtil;

/**
 * ���Iʵ�����
 * @author keshuangjie
 *
 */
public class Mood implements Serializable{
	
	public static final String CLOUD_TABLE_NAME = "Mood";
	
	private static final Uri uri = DataProvider.CONTENT_URI_MOOD;
	public static final String COLUMN_ID = DataProvider.MOOD_ID;
	public static final String COLUMN_CONTENT = DataProvider.MOOD_CONTENT;
	public static final String COLUMN_DATE = DataProvider.MOOD_DATE;
	public static final String COLUMN_LOCATION = DataProvider.MOOD_LOCATION;
	public static final String COLUMN_IMAGEPATH = DataProvider.MOOD_IMAGEPATH;
	public static final String BACK_UP_PATH = "mood";
	
	public String objectId;
	private int id;
	private String content = "";//��������
	private String location = "";//��ǰ����λ��
	private String date = "";    //����ʱ��
	private String imagePath = "";//ͼƬ·��
	
	public static final int pageSize = 10;
	
//	public static int count = 0;
	
	public Mood(){
		
	}
	
	public static Mood addMood(Context context, Mood mood){
		Mood m = mood;
		if(m == null){
			return null;
		}
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(COLUMN_CONTENT, m.getContent());
		values.put(COLUMN_DATE, m.getDate());
		values.put(COLUMN_LOCATION, m.getLocation());
		values.put(COLUMN_IMAGEPATH, m.getImagePath());
		Uri new_uri = resolver.insert(uri, values);
		m.setId(Integer.parseInt(new_uri.toString()));
		return m;
	}
	
	public static void deleteMoodById(Context context, int id){
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(uri, COLUMN_ID + "=" + id, null);
	}
	
	public static void deleteMood(Context context, Mood m){
		if(m == null){
			return;
		}
		deleteMoodById(context, m.getId());
	}
	
	public static void updateMood(Context context, Mood m){
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, m.getId());
		values.put(COLUMN_CONTENT, m.getContent());
		values.put(COLUMN_DATE, m.getDate());
		values.put(COLUMN_LOCATION, m.getLocation());
		values.put(COLUMN_IMAGEPATH, m.getImagePath());
		resolver.update(uri, values, COLUMN_ID + "=" + m.getId(), null);
	}
	
	public static Mood getMood(Context context, int id){
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(uri, null, COLUMN_ID + "=" + id, null, null);
		if(c == null || c.getCount()<=0){
			return null;
		}
		c.moveToFirst();
		Mood m = new Mood();
		m.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
		m.setContent(c.getString(c.getColumnIndex(COLUMN_CONTENT)));
		m.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
		m.setImagePath(c.getString(c.getColumnIndex(COLUMN_IMAGEPATH)));
		m.setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
		c.close();
		return m;
	}
	
	public static List<Mood> listMood(Context context, int pageIndex){
//		int startIndex = count - (pageIndex + 1) * pageSize;
//		int endIndex = count - pageIndex * pageSize;
//		String where = COLUMN_ID + ">" + startIndex + " AND " + COLUMN_ID + "<=" + endIndex;
//		String where = COLUMN_ID + "<=" + endIndex;
		//sql��䣺id desc limit 0,10;
		String sortOrder = COLUMN_ID + " DESC limit " + 
                  pageIndex * pageSize + "," + pageSize;
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(uri, null, null, null, sortOrder);
		if(c == null || c.getCount() <=0){
			return null;
		}
		ArrayList<Mood> listMood = new ArrayList<Mood>();
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			Mood m = new Mood();
			m.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
			m.setContent(c.getString(c.getColumnIndex(COLUMN_CONTENT)));
			m.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
			m.setImagePath(c.getString(c.getColumnIndex(COLUMN_IMAGEPATH)));
			m.setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
			listMood.add(m);
		}
		c.close();
		return listMood;
	}
	
	public static List<Mood> listMood(Context context){
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(uri, null, null, null, null);
		if(c == null || c.getCount() <=0){
			return null;
		}
		ArrayList<Mood> listMood = new ArrayList<Mood>();
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			Mood m = new Mood();
			m.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
			m.setContent(c.getString(c.getColumnIndex(COLUMN_CONTENT)));
			m.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
			m.setImagePath(c.getString(c.getColumnIndex(COLUMN_IMAGEPATH)));
			m.setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
			listMood.add(m);
		}
		c.close();
		return listMood;
	}
	
	public static String exportMood(Context context){
		String sdCardPath = Configuration.getConfig().getSDCardPath();
		String appDir = sdCardPath + "/" + Configuration.APPLICATION_NAME;
		String backupPath = appDir + "/" + BACK_UP_PATH;
		List<Mood> list = listMood(context);
		FileUtil.export(list, backupPath);
		return backupPath;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
//	public static void setCount(int count1){
//		count = count1;
//	}
	
}
