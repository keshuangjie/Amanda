package com.sinaapp.whutec.amanda.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.sinaapp.whutec.amanda.AppManager;
import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.Constant;
import com.sinaapp.whutec.amanda.ui.util.UIHelper;
import com.sinaapp.whutec.amanda.widget.LocusPassWordView;
import com.sinaapp.whutec.amanda.widget.LocusPassWordView.OnCompleteListener;
import com.sinaapp.whutec.amanda.R;

public class Login extends Activity {
	
	private Context context;
	private Class reback = null;
	private LocusPassWordView passwordView;
	private boolean isPassword = false;//�Ƿ���Ҫ��������
	private Intent intent = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		System.out.println("Login:onCreate");
		
		context = this;
		if(Configuration.getConfig().isPassword()){
			setContentView(R.layout.login);
			initView();
			isPassword = true;
		}else{
			UIHelper.startActivity(context, MainPage.class);
			isPassword = false;
		}
			AppManager.getAppManager().addActivity(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("Login:onResume");
		intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras != null){
			reback = (Class) extras.getSerializable(Constant.REBACK);
		}
	}
	
	private void initView(){
		passwordView = (LocusPassWordView) findViewById(R.id.locus_password_view);
		passwordView.setOnCompleteListener(new OnCompleteListener() {
			
			@Override
			public void onComplete(String password) {
				if(passwordView.verifyPassword(password)){
					if(reback!=null){
						UIHelper.startActivity(context, reback);
						finish();
					}else{
						UIHelper.startActivity(context, MainPage.class);
					}
					
				}else{
					Toast.makeText(context, "���������������������", Toast.LENGTH_SHORT).show();
				}
				passwordView.clearPassword();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().AppExit(context);
		}
		return super.onKeyDown(keyCode, event);
	}

}
