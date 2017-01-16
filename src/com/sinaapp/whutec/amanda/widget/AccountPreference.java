package com.sinaapp.whutec.amanda.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.R;

public class AccountPreference extends Preference {
	
	private OnClickListener userNameListener = null;
	private OnClickListener userFaceListener = null; 
	private Button bUserName;
	private Button bUserFace;
	private TextView tUserName;
	private ImageView iUserFace;
//	private Context context;
	
//	public AccountPreference(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		this.context = context;
//		setLayoutResource(R.layout.setting_account);
//	}
//
//	public AccountPreference(Context context) {
//		super(context);
//		this.context = context;
//		setLayoutResource(R.layout.setting_account);
//	}

	public AccountPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
//		this.context = context;
		setLayoutResource(R.layout.setting_account);
	}
	
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		System.out.println("AccountPreference:3");
		tUserName = (TextView) view.findViewById(R.id.user_name);
		iUserFace = (ImageView) view.findViewById(R.id.user_face);
		bUserName = (Button) view.findViewById(R.id.user_name_edit);
		bUserFace = (Button) view.findViewById(R.id.user_face_edit);

		bUserName.setOnClickListener(userNameListener);
		bUserFace.setOnClickListener(userFaceListener);
		initView();
	}
	
	
	public void initView(){
		tUserName.setText(Configuration.getConfig().getUserName());
		iUserFace.setImageBitmap(Configuration.getConfig().getUserFaceImage());
	}
	
	public void changeUserName(String userName){
		tUserName.setText(userName);
	}
	
	public void changeUserFace(Bitmap dUserFace){
		iUserFace.setImageBitmap(dUserFace);
	}
	
	public void setUserNameListener(OnClickListener userNameListener) {
		this.userNameListener = userNameListener;
	}

	public void setUserFaceListener(OnClickListener userFaceListener) {
		this.userFaceListener = userFaceListener;
	}
	
}
