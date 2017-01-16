package com.sinaapp.whutec.amanda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sinaapp.whutec.amanda.db.DataProvider;

public class Blog implements Serializable{
	
	public static final String CLOUD_TABLE_NAME = "DailyRecord";
	
	private static final Uri uri = DataProvider.CONTENT_URI_BLOG;
	public static final String COLUMN_ID = DataProvider.BLOG_ID;
	public static final String COLUMN_TITLE = DataProvider.BLOG_TITLE;
	public static final String COLUMN_CONTENT = DataProvider.BLOG_CONTENT;
	public static final String COLUMN_DATE = DataProvider.BLOG_DATE;
	public static final String COLUMN_LOCATION = DataProvider.BLOG_LOCATION;
	
	public String objectId;
	private int id;
	private String title;
	private String content;
	private String date;
	private String location;
	public static final int pageSize = 10;
	
	public Blog(){
		
	}
	
	public static void addBlog(Context context, Blog b){
		if(b == null){
			return;
		}
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(COLUMN_TITLE,b.getTitle());
		values.put(COLUMN_CONTENT, b.getContent());
		values.put(COLUMN_DATE, b.getDate());
		values.put(COLUMN_LOCATION, b.getLocation());
		resolver.insert(uri, values);
	}
	
	public static void deleteBlogById(Context context, int id){
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(uri, COLUMN_ID + "=" + id, null);
	}
	
	public static void deleteBlog(Context context, Blog b){
		if(b == null){
			return;
		}
		deleteBlogById(context, b.getId());
	}
	
	public static void updateBlog(Context context, Blog b){
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, b.getId());
		values.put(COLUMN_TITLE,b.getTitle());
		values.put(COLUMN_CONTENT, b.getContent());
		values.put(COLUMN_DATE, b.getDate());
		values.put(COLUMN_LOCATION, b.getLocation());
		resolver.update(uri, values, COLUMN_ID + "=" + b.getId(), null);
	}
	
	public static Blog getBlog(Context context, int id){
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(uri, null, COLUMN_ID + "=" + id, null, null);
		if(c == null || c.getCount()<=0){
			return null;
		}
		c.moveToFirst();
		Blog b = new Blog();
		b.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
		b.setTitle(c.getString(c.getColumnIndex(COLUMN_TITLE)));
		b.setContent(c.getString(c.getColumnIndex(COLUMN_CONTENT)));
		b.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
		b.setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
		return b;
	}
	
	public static List<Blog> listBlog(Context context, int pageIndex){
		//sql��䣺id desc limit 0,10;
		String sortOrder = COLUMN_ID + " DESC limit " + 
                  pageIndex * pageSize + "," + pageSize;
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(uri, null, null, null, sortOrder);
		if(c == null || c.getCount() <=0){
			return null;
		}
		ArrayList<Blog> listBlog = new ArrayList<Blog>();
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			Blog b = new Blog();
			b.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
			b.setContent(c.getString(c.getColumnIndex(COLUMN_CONTENT)));
			b.setTitle(c.getString(c.getColumnIndex(COLUMN_TITLE)));
			b.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
//			b.setImagePath(c.getString(c.getColumnIndex(COLUMN_IMAGEPATH)));
			b.setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
			listBlog.add(b);
		}
		c.close();
		return listBlog;
	}
	
	public static List<Blog> listBlog(Context context){
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(uri, null, null, null, null);
		if(c == null || c.getCount() <=0){
			return null;
		}
		ArrayList<Blog> listBlog = new ArrayList<Blog>();
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			Blog b = new Blog();
			b.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
			b.setTitle(c.getString(c.getColumnIndex(COLUMN_TITLE)));
			b.setContent(c.getString(c.getColumnIndex(COLUMN_CONTENT)));
			b.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
			b.setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
			listBlog.add(b);
		}
		return listBlog;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

}
