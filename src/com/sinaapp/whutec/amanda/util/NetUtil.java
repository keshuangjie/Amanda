package com.sinaapp.whutec.amanda.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.sinaapp.whutec.amanda.bean.BlogInfo;

import android.text.TextUtils;

public class NetUtil {

	public static boolean post(BlogInfo blogInfo, String title, String description) {
		
		BlogInfo bi = blogInfo;
		if(bi == null){
			return false;
		}
		// Set up XML-RPC connection to server
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		URL targetUrl = null;
		try {
			targetUrl = new URL(bi.url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		config.setServerURL(targetUrl);
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);

		// Set up parameters required by newPost method
		Map<Object, Object> post = new HashMap<Object, Object>();
		post.put("title", title);
		post.put("description", description);
		Object[] categories = new Object[] { "Amanda" };// 分类   
        post.put("categories", categories);  
		Object[] params = new Object[] { "1", bi.userName, bi.password, post, Boolean.TRUE };

		// Call newPost
		String result = "";
		boolean retry = true;
		boolean flag = false;
		int i = 0;
		while (i < 3 && retry) {
			try {
				result = (String) client.execute(bi.api, params);
				if (!TextUtils.isEmpty(result)) {
					Log.i("kshj", "post successed !!!");
					retry = false;
					flag = true;
				}
			} catch (XmlRpcException e) {
				i++;
				Log.e("kshj", e.toString());
			}
		}
		System.out.println(" Created with blogid " + result);
		return flag;
	}
	
}
