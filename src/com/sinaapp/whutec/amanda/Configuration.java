package com.sinaapp.whutec.amanda;

import java.io.File;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.sinaapp.whutec.amanda.page.Setting;
import com.sinaapp.whutec.amanda.util.BitmapUtil;
import com.sinaapp.whutec.amanda.util.FileUtil;

/**
 * Ӧ�ó��������ࣺ���ڱ����û������Ϣ������
 * @author keshuangjie
 */
public class Configuration {
	
	private static final String CONFIG = "config";
	private static final String CONFIG_FILE = "config.properties";
	private static final String IMAGE_FILE = "image";
	private static final String IMAGE_USER_FACE = "user_face.png";
	public static final String TEMP_MOOD = "temp_mood";//δ����������
	public static final String TEMP_BLOG_TITLE = "temp_blog_title";
	public static final String TEMP_BLOG_CONTENT = "temp_blog_content";
	public static final String IMPORT_FILE = "privateBlog";//�����ļ�Ŀ¼
	public static final String APPLICATION_NAME = "privateBlog";
	
//	public static boolean isPassword;
	
    private static Configuration config;
    private static String userName = "";
    private static Bitmap userFace = null;
    public Context context;
    
    private Configuration(Context context){
    	this.context = context; 
    	System.out.println("Configuration:Configuration()");
//    	isPassword = isPassword();
    }
    
    public static void initConfig(Context context){
    	if(config == null){
    		config = new Configuration(context);
    	}
    	System.out.println("Configuration:initConfig()");
    }
    
    public static Configuration getConfig(){
    	return config;
    }
    
    /**
     * �Ƿ���������
     * @return
     */
    public boolean isPasswordNull(){
    	String password = config.getSharedPre().getString(Setting.SETTING_PASSWORD, "");
    	if("".equals(password)){
    		return true;
    	}
    	return false;
    }
    
    /**
     * �Ƿ�������������
     * @return
     */
    public boolean isPassword(){
    	boolean isPassword = config.getSharedPre().getBoolean(Setting.SETTING_ISPASSWORD, false);
    	if(isPassword){
    		if(!isPasswordNull()){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * ��ȡ�û�����û���á�����������
     * @return
     */
    public String getUserName(){
    	if(userName.equals("")){
    		userName = getSharedPre().getString(Setting.SETTING_USER_NAME, "Amanda");
    	}
    	return userName;
    }
    
    public void refreshUserName(){
    	userName = getSharedPre().getString(Setting.SETTING_USER_NAME, "Amanda");
    }
    
    /**
     * ��ȡ�û�ͼ��û����Ĭ��
     * @return
     */
    public Bitmap getUserFaceImage(){
    	if(userFace == null){
    		userFace = FileUtil.getImageFile(getFile(IMAGE_FILE,IMAGE_USER_FACE));
    	}
    	if(userFace == null){
    		Drawable d = context.getResources().getDrawable(R.drawable.widget_dface);
    		userFace = BitmapUtil.drawableToBitmap(d);
    	}
    	return userFace;
    }
    
    public void refreshUserFaceImage(){
    	userFace = FileUtil.getImageFile(getFile(IMAGE_FILE,IMAGE_USER_FACE));
    }
    
    /**
     * �����û�ͷ��
     * @param bitmap
     */
    public void saveUserFaceImage(Bitmap bitmap){
//    	File file = context.getDir(IMAGE_FILE, Context.MODE_PRIVATE);
//    	File imageFile = new File(file,IMAGE_USER_FACE);
    	FileUtil.saveImageFile(getFile(IMAGE_FILE,IMAGE_USER_FACE), bitmap);
    }
    
    
    
    public File getFile(String path, String fileName){
    	File director = context.getDir(path, Context.MODE_PRIVATE);
    	File file = new File(director,fileName);
    	return file;
    	
    }
	
	public Properties getProperty(){
		return FileUtil.getProperties(getFile(CONFIG,CONFIG_FILE));
	}
	
	public String getProperty(String key){
		Properties p = getProperty();
		if(p != null){
			return p.getProperty(key,"");
		}
		return "";
	}
	
	public void setProperty(Properties p){
		
//		File file = context.getDir(CONFIG, Context.MODE_PRIVATE);
//		File configFile = new File(file,CONFIG_FILE);
		FileUtil.setProperties(getFile(CONFIG,CONFIG_FILE),p);
	}
	
	public void setProperty(String key, String value){
		Properties p = getProperty();
		System.out.println("key:" + key + "\n" + "value:" +value);
		p.setProperty(key, value);
		setProperty(p);
	}
	
	public void removeProperty(String... key){
		Properties p = getProperty();
		for(String s : key){
			p.remove(s);
		}
		setProperty(p);
	}
	
	public SharedPreferences getSharedPre(){
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public void setSharedPre(String key, String value){
		SharedPreferences.Editor edit = getSharedPre().edit();
		edit.putString(key, value); 
		edit.commit();
	}
	
	public void setSharedPre(String key, boolean value){
		SharedPreferences.Editor edit = getSharedPre().edit();
		edit.putBoolean(key, value); 
		edit.commit();
	}
	
	public String getSDCardPath(){
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		return path;
	}

}
