package com.sinaapp.whutec.amanda.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DataProvider extends ContentProvider{
	
	public static final String DB_NAME = "db_private_blog";
	public static final int DB_VERSION = 1;
	public static final String AUTHORITY = "com.sinaapp.whutec.amanda.myProvider";
	
	public static final String TABLE_MOOD = "table_mood";
	public static final String MOOD_ID = "_id";
	public static final String MOOD_CONTENT = "content";
	public static final String MOOD_DATE = "date";
	public static final String MOOD_LOCATION = "location";
	public static final String MOOD_IMAGEPATH = "imagePath";
	
	public static final String TABLE_BLOG = "table_blog";
	public static final String BLOG_ID = "_id";
	public static final String BLOG_TITLE = "title";
	public static final String BLOG_CONTENT = "content";
	public static final String BLOG_DATE = "date";
	public static final String BLOG_LOCATION = "location";
	
	public static final Uri CONTENT_URI_MOOD = Uri.parse("content://" + 
			AUTHORITY + "/" + TABLE_MOOD);
	public static final Uri CONTENT_URI_BLOG = Uri.parse("content://" + 
	        AUTHORITY + "/" + TABLE_BLOG);
	
	private static final int MOOD = 1;
	private static final int BLOG = 2;
	
    private static UriMatcher sUriMatcher;
    
    static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, TABLE_MOOD, MOOD);
		sUriMatcher.addURI(AUTHORITY, TABLE_BLOG, BLOG);
	}
    
    @Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)){
		case MOOD:
			return "vnd.android.cursor.dir/" + TABLE_MOOD;
		case BLOG:
			return "vnd.android.cursor.dir/" + TABLE_BLOG;
		default:
			throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
		}
	}
    
    public class DBHelper extends SQLiteOpenHelper{

		public DBHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		private static final String SQL_CREATE_TABLE_MOOD = "create table if not exists " 
		        + TABLE_MOOD + "(" 
				+ MOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ MOOD_CONTENT + " text,"
				+ MOOD_DATE + " text,"
				+ MOOD_LOCATION + " text,"
				+ MOOD_IMAGEPATH + " text"
				+ ");";
		
		private static final String SQL_CREATE_TABLE_BLOG = "create table if not exists " 
		        + TABLE_BLOG + "(" 
				+ BLOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ BLOG_TITLE + " text,"
				+ BLOG_CONTENT + " text,"
				+ BLOG_DATE + " text,"
				+ BLOG_LOCATION + " text"
				+ ");";

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql_create_mood = SQL_CREATE_TABLE_MOOD;
			db.execSQL(sql_create_mood);
			
			String sql_create_blog = SQL_CREATE_TABLE_BLOG;
			db.execSQL(sql_create_blog);
			
			System.out.println(sql_create_mood);
			System.out.println(sql_create_blog);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//			db.execSQL(SQL_CREATE_TABLE_BLOG);
//			db.execSQL(SQL_CREATE_TABLE_MOOD);
//			db.execSQL("drop table if exists "+ TABLE_BLOG);
//			db.execSQL("ALERT TABLE IF EXISTS " + TABLE_BLOG + " add " 
//                + BLOG_TITLE + " text;");
		}
    }
	
    private DBHelper dbHelper;
	private SQLiteDatabase db;
    
	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(this.getContext());
		db = dbHelper.getReadableDatabase();
		return true;
	}
	
	private String getTableName(Uri uri){
		switch (sUriMatcher.match(uri)) {
		case MOOD:
			return TABLE_MOOD;
		case BLOG:
			return TABLE_BLOG;
        default:
			return null;
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		String tableName = getTableName(uri);
		c = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = -1;
		String tableName = getTableName(uri);
		id = db.insert(tableName, "content is empty", values);
		if(id == -1){
			return null;
		}
		return Uri.parse("" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int num = 0;
		String tableName = getTableName(uri);
		num = db.delete(tableName, selection, selectionArgs);
		return num;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int num = 0;
		String tableName = getTableName(uri);
		num = db.update(tableName, values, selection, selectionArgs);
		return num;
	}

}
