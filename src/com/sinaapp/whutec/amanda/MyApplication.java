package com.sinaapp.whutec.amanda;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Configuration.initConfig(getApplicationContext());
		
		AVOSCloud.initialize(this, AVOSign.APPLICATION_ID, AVOSign.CLIENT_KEY);
	}
	
}
