package com.sinaapp.whutec.amanda.ui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.Constant;
import com.sinaapp.whutec.amanda.page.Login;
import com.sinaapp.whutec.amanda.page.adapter.FaceAdapter;
import com.sinaapp.whutec.amanda.util.StringUtil;

public class UIHelper {
	
	/** ����ͼƬƥ�� */
	private static Pattern facePattern = Pattern.compile("\\[{1}([0-9]\\d*)\\]{1}");
	
	/**
	 * ��[12]֮����ַ����滻Ϊ����
	 * @param context
	 * @param content
	 */
	public static SpannableStringBuilder parseFaceByText(Context context, String content) {
		
		return parseFaceByText(context, content, 0);
	}
	
	public static SpannableStringBuilder parseFaceByText(Context context, String str,int length) {
		String content = str;
		if(length>0 && content.length()>length){
			content = content.substring(0, length-1);
		}
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Matcher matcher = facePattern.matcher(content);
		while (matcher.find()) {
			//ʹ��������ʽ�ҳ����е�����
			int position = StringUtil.toInt(matcher.group(1));
			int resId = 0;
			try {
				if(position > 65 && position < 102)
					position = position-1;
				else if(position > 102)
					position = position-2;
				resId = FaceAdapter.getResourceId(position);
				Drawable d = context.getResources().getDrawable(resId);
				d.setBounds(0, 0, 50, 50);
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception e) {
			}
		}
		return builder;
	}
	
	public static <T> void startActivity(Context context,Class<T> o){
		Intent intent = new Intent(context,o);
		context.startActivity(intent);
		((Activity)context).finish();
	}
	
	public static <T> void rebackLogin(Activity activity,Class<T> reback){
		Intent intent = new Intent(activity,Login.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.REBACK, reback);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}
	
	public static void rebackLoginAndFinish(Context context){
		Intent intent = new Intent(context,Login.class);
		context.startActivity(intent);
		Activity activity = (Activity)context;
		activity.finish();
	}
	
	/**
	 * �������������
	 * @param allowNextResume
	 * @param activity
	 * @param reback
	 */
	public static <T> void allowNextResume(boolean allowNextResume,Activity activity,Class<T> reback){
		allowNextResume(allowNextResume, activity, reback, false);
	}
	
	public static <T> void allowNextResume(boolean allowNextResume,Activity activity,Class<T> reback,boolean finish){
		if(Configuration.getConfig().isPassword()){
			if(!allowNextResume){
				rebackLogin(activity, reback);
				if(finish){
					activity.finish();
				}
			}
		}
	}
	
	
}
