package com.sinaapp.whutec.amanda;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("AppContext:onCreate()");
		Configuration.initConfig(getApplicationContext());
		
		AVOSCloud.initialize(this, "6nmy5gka0kabauzab7gnsxndiyywpeqw4i8t49uev29dojwr", "cdwz7cvo58cn6ey955xp71kmmz1490ofolor51uk01s2d29d");
	}
	
}
