package com.sinaapp.whutec.amanda.page;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sinaapp.whutec.amanda.ui.util.UIHelper;
import com.sinaapp.whutec.amanda.widget.LocusPassWordView;
import com.sinaapp.whutec.amanda.widget.LocusPassWordView.OnCompleteListener;
import com.sinaapp.whutec.amanda.R;

public class PasswordReset extends Activity{
	
	private static final int PASSWORD_VERIFY = 0;//
	private static final int PASSWORD_RESET = 1;//
	private static final int PASSWORD_RESET_VERIFY = 2;//
	
	private LocusPassWordView passwordView;
	private Button cancle;
	private Button next;
	private Button clear;
	private TextView notice;
	
	private boolean isPassword = true;//
	private int verify = 0;
	private boolean verifySuccess = false;//
	private String resetPassword;
	private boolean allowNextResume;
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.password_reset);
		initView();
		
		allowNextResume = true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UIHelper.allowNextResume(allowNextResume, this, Setting.class, true);
		allowNextResume = false;
	}

	private void initView() {
		notice = (TextView) findViewById(R.id.notice);
		cancle = (Button) findViewById(R.id.cancle);
		next = (Button) findViewById(R.id.next);
		clear = (Button) findViewById(R.id.clear);
		passwordView = (LocusPassWordView) findViewById(R.id.locus_password_view);
		passwordView.setOnCompleteListener(onCompleteListener);
		cancle.setOnClickListener(cancleListener);
		next.setOnClickListener(nextListener);
		clear.setOnClickListener(clearListener);
	}
	
	private OnClickListener clearListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			passwordView.clearPassword();
		}
		
	};
	
	
	private OnClickListener cancleListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	private OnClickListener nextListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(verify == PASSWORD_RESET){
				notice.setText(context.getResources().getString(R.string.set_password_verify_notice));
				passwordView.clearPassword();
				verify = PASSWORD_RESET_VERIFY;
			}else if(verify == PASSWORD_RESET_VERIFY && verifySuccess){
				Toast.makeText(context, "密码设置成功", Toast.LENGTH_SHORT).show();
				passwordView.resetPassWord(resetPassword);
				finish();
			}
		}
		
	};
	
	private OnCompleteListener onCompleteListener = new OnCompleteListener() {
		
		@Override
		public void onComplete(String password) {
			System.out.println("password:" + password);
//			if(isPassword && verify==PASSWORD_VERIFY){
//				if(passwordView.verifyPassword(password)){
//					//���������֤�ɹ�,������������ҳ��
////					verify = PASSWORD_RESET;
//					//�ı�ҳ��
//				}
//			}else{
				if(verify == PASSWORD_VERIFY){
					verify = PASSWORD_RESET;
				}
				if(verify == PASSWORD_RESET){
					resetPassword = password;
					//��������ȷ��ҳ�棬�ı�ҳ��
				}else{
					if(password.equals(resetPassword)){
						//��������ɹ�������������
						verifySuccess = true;
					}else{
						passwordView.clearPassword();
						Toast.makeText(context, "���벻һ�£�����������", Toast.LENGTH_SHORT).show();
					}
				}
//			}
		}
	};

}
