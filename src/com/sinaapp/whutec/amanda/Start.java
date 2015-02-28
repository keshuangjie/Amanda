package com.sinaapp.whutec.amanda;

import com.sinaapp.whutec.amanda.page.Login;
import com.sinaapp.whutec.amanda.page.MainPage;
import com.sinaapp.whutec.amanda.page.Setting;
import com.sinaapp.whutec.amanda.ui.util.UIHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class Start extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.start);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (Configuration.getConfig().isPassword()) {
					UIHelper.startActivity(Start.this, Login.class);
				} else {
					UIHelper.startActivity(Start.this, MainPage.class);
				}
				finish();
			}
		}, 1000);
	}
	
}
