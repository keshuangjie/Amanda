package com.sinaapp.whutec.amanda.page;

import java.io.FileNotFoundException;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.ui.util.UIHelper;
import com.sinaapp.whutec.amanda.util.BitmapUtil;
import com.sinaapp.whutec.amanda.util.DpiUtil;
import com.sinaapp.whutec.amanda.widget.AccountPreference;
import com.sinaapp.whutec.amanda.R;

public class Setting extends PreferenceActivity{
	
	public static final String SETTING_ISSCROLL = "isScroll";//主界面是否可滑动开关
	public static final String SETTING_BACKUP = "setting_backup";
	public static final String SETTING_VOICE = "setting_voice";
	public static final String SETTING_HAVE_PASSWORD = "setting_have_password";
	public static final String SETTING_USER_NAME = "setting_user_name";
	public static final String SETTING_USER_FACE = "setting_user_face";
	public static final String SETTING_PASSWORD = "setting_password";
	public static final String SETTING_ISPASSWORD = "setting_isPassword";
	
	private static final int REQUEST_RESULT_OPEN_IMAGE = 1;
	private static final int REQUEST_RESULT_OPEN_CAMERA = 2;
	private static final int WIDTH_USER_FACE = 80;
	private static final int HEIGHT_USER_FACE = 80;
	
	private Preference password;
	private Preference feedback;
	private Preference update;
	private Preference about;
	private AccountPreference account;
	private CheckBoxPreference backup;
	private CheckBoxPreference scroll;
	private CheckBoxPreference voice;
	private CheckBoxPreference isPassword;
	private Context context;
	private Configuration config;
	private boolean allowNextResume;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		config = Configuration.getConfig();
		//设置显示Preferences
		addPreferencesFromResource(R.xml.preference);
		//获得SharedPreferences
		ListView localListView = getListView();
		localListView.setBackgroundColor(0);
		localListView.setCacheColorHint(0);
		((ViewGroup)localListView.getParent()).removeView(localListView);
		ViewGroup localViewGroup = (ViewGroup)getLayoutInflater().inflate(R.layout.setting, null);
		((ViewGroup)localViewGroup.findViewById(R.id.setting_content)).addView(localListView, -1, -1);
		setContentView(localViewGroup);
		
		account = (AccountPreference) findPreference("account");
		account.setUserFaceListener(userFaceListener);
		account.setUserNameListener(userNameListener);
		
		//主页面是否可滑动
		scroll = (CheckBoxPreference) findPreference("scroll");
		scroll.setChecked(config.getSharedPre().getBoolean(SETTING_ISSCROLL,true));
		scroll.setOnPreferenceClickListener(scollListener);
		
		//是否可备份
		backup = (CheckBoxPreference) findPreference("backup");
		backup.setChecked(config.getSharedPre().getBoolean(SETTING_BACKUP,true));
		backup.setOnPreferenceClickListener(backupListener);
		
		//是否有提供声音
		voice = (CheckBoxPreference) findPreference("voice");
		voice.setChecked(config.getSharedPre().getBoolean(SETTING_VOICE,true));
		voice.setOnPreferenceClickListener(voiceListener);
		
		isPassword = (CheckBoxPreference) findPreference("isPasswrod");
		isPassword.setChecked(config.getSharedPre().getBoolean(SETTING_ISPASSWORD, false));
		isPassword.setOnPreferenceClickListener(isPasswordListener);
		
		password = findPreference("password");
		if(!config.isPasswordNull()){
			password.setTitle("已设置密码");
			password.setSummary("点击重设密码");
		}else{
			password.setTitle("未设置密码");
			password.setSummary("点击设置密码");
		}
		changePasswordState(isPassword.isChecked());
		password.setOnPreferenceClickListener(setPasswordListener);
		
		allowNextResume = true;
	}
	
	//是否启动密码
	private OnPreferenceClickListener isPasswordListener = new OnPreferenceClickListener() {
		
		@Override
		public boolean onPreferenceClick(Preference preference) {
			config.setSharedPre(SETTING_ISPASSWORD, isPassword.isChecked());
			changePasswordState(isPassword.isChecked());
//			if(isPassword.isChecked()){
//				password.setEnabled(true);
//			}else{
//				password.setEnabled(false);
//			}
			return true;
		}
	};
	
	private void changePasswordState(boolean flag){
		if(flag){
			password.setEnabled(true);
		}else{
			password.setEnabled(false);
		}
	}
	
	//设置密码
	private OnPreferenceClickListener setPasswordListener = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {
//			UIHelper.startActivity(context, PasswordReset.class);
			Intent intent = new Intent(context,PasswordReset.class);
			context.startActivity(intent);
		    allowNextResume = true;
			return true;
		}
		
	};
	
	//修改用户图像监听器
	private OnClickListener userFaceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("请选择照片");
			builder.setItems(R.array.photo_select, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent;
					switch (which) {
					case 0:
						intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(intent, REQUEST_RESULT_OPEN_IMAGE);
//						drawable = context.getResources().getDrawable(R.drawable._test);
//						reFreshUserFace(drawable);
						break;
					case 1:
//						drawable = context.getResources().getDrawable(R.drawable.widget_dface);
//						reFreshUserFace(drawable);
						intent = new Intent("android.media.action.IMAGE_CAPTURE"); 
						startActivityForResult(intent, REQUEST_RESULT_OPEN_CAMERA);
						break;
					}
					allowNextResume = true;
				}
			});
			builder.create().show();
		}
	};
	
	private Bitmap reFreshUserFace(Drawable drawable){
		Bitmap bmp = BitmapUtil.drawableToBitmap(drawable);
		bmp = BitmapUtil.zoomBitmap(bmp, WIDTH_USER_FACE, HEIGHT_USER_FACE);
		account.changeUserFace(bmp);
		config.saveUserFaceImage(bmp);
		config.refreshUserFaceImage();
		return bmp;
	}
	
	private void reFreshUserFace(Bitmap bitmap){
		Bitmap bmp = BitmapUtil.zoomBitmap(bitmap, DpiUtil.dip2px(context, WIDTH_USER_FACE), 
				DpiUtil.dip2px(context, HEIGHT_USER_FACE));
		account.changeUserFace(bmp);
		config.saveUserFaceImage(bmp);
		config.refreshUserFaceImage();
	}
	
	//修改用户名监听器
	private OnClickListener userNameListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final EditText name = new EditText(context);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("请输入姓名");
			builder.setView(name);
			builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					account.changeUserName(name.getText().toString());
					config.setSharedPre(SETTING_USER_NAME, name.getText().toString());
					config.refreshUserName();
				}
				
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	};
	
	//设置是否可滑动监听器
	private OnPreferenceClickListener scollListener = new OnPreferenceClickListener() {
		
		@Override
		public boolean onPreferenceClick(Preference preference) {
			System.out.println("backup.isChecked()" + scroll.isChecked());
			config.setSharedPre(SETTING_ISSCROLL, scroll.isChecked());
			return true;
		}
	};
	
	//设置是否定期备份监听器
	private OnPreferenceClickListener backupListener = new OnPreferenceClickListener() {
		
		@Override
		public boolean onPreferenceClick(Preference preference) {
			System.out.println("backup.isChecked()" + backup.isChecked());
			config.setSharedPre(SETTING_BACKUP, backup.isChecked());
			return true;
		}
	};
	
	//设置是否按键有声音监听器
	private OnPreferenceClickListener voiceListener = new OnPreferenceClickListener() {
		
		@Override
		public boolean onPreferenceClick(Preference preference) {
			System.out.println("backup.isChecked()" + voice.isChecked());
			config.setSharedPre(SETTING_VOICE, voice.isChecked());
			return true;
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this,MainPage.class);
			startActivity(intent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null){
			return;
		}
		Bitmap bitmap = null;
		if(requestCode == REQUEST_RESULT_OPEN_IMAGE){
			Uri uri = data.getData();
			ContentResolver cr = context.getContentResolver();
			try {
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				if(bitmap!=null){
					reFreshUserFace(bitmap);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally{
				if(bitmap!=null){
					bitmap.recycle();
				}
			}
		}else if(requestCode == REQUEST_RESULT_OPEN_CAMERA){
		    Bundle extras = data.getExtras(); 
	        bitmap = (Bitmap) extras.get("data"); 
	        if(bitmap!=null){
	        	reFreshUserFace(bitmap);
	        	bitmap.recycle();
	        }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UIHelper.allowNextResume(allowNextResume, this, this.getClass(),true);
		
		allowNextResume = false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}	
}
