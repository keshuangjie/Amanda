package com.sinaapp.whutec.amanda.util;

public class MathUtil
{
	/**
	 * �����ľ���
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double distance(double x1,double y1,double x2,double y2)
	{
		return Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));
	}

	/**
	 * �����a(x,y)�ĽǶ�
	 * @param x
	 * @param y
	 * @return
	 */
	public static double pointTotoDegrees(double x,double y)
	{
		return Math.toDegrees(Math.atan2(x,y));
	}
	
	/**
	 * ����Բ��
	 * @param sx
	 * @param sy
	 * @param r
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean checkInRound(float sx, float sy, float r, float x, float y)
	{
		return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
	}
}
