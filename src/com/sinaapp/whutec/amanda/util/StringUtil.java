package com.sinaapp.whutec.amanda.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StringUtil {
	
	/**
	 * �Ƿ�Ϊ��
	 * @param s
	 * @return
	 */
	public static boolean isNotEmpty(String s)
	{
		return s != null && !"".equals(s.trim());
	}

	/**
	 * �Ƿ�Ϊ��
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s)
	{
		return s == null || "".equals(s.trim());
	}
	
	/**
	 * ����ת����ָ����ʽ���ַ���
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 * �ַ���ת����
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	/**
	 * ����ת����
	 * @param obj
	 * @return ת���쳣���� 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	
	public static void add2ListFirst(List<Object> list, Object o){
		
	}
	
}
