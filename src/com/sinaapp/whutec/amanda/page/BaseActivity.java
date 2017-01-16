package com.sinaapp.whutec.amanda.page;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.sinaapp.whutec.amanda.AppManager;
import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.R;
import com.sinaapp.whutec.amanda.ui.util.UIHelper;

public class BaseActivity extends FragmentActivity{
	
	protected boolean allowNextResume;
	protected Configuration config;
	protected TextView headTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allowNextResume = true;
		AppManager.getAppManager().addActivity(this);
		config = Configuration.getConfig();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setHeaderTitle();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UIHelper.allowNextResume(allowNextResume, this, this.getClass(),true);
		allowNextResume = false;
	}
	
	protected void setHeaderTitle(){
//		LayoutInflater inflate = LayoutInflater.from(this);
//		LinearLayout head = (LinearLayout) inflate.inflate(R.layout.main_head, null);
		headTitle = (TextView) findViewById(R.id.head_title);
	}
}
