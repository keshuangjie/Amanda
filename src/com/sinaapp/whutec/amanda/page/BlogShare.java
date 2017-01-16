package com.sinaapp.whutec.amanda.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sinaapp.whutec.amanda.bean.BlogInfo;
import com.sinaapp.whutec.amanda.util.NetUtil;
import com.sinaapp.whutec.amanda.R;

public class BlogShare extends Activity {

	private EditText eBlogTitle;
	private EditText eBlogContent;
	private Spinner sBlogWeb;
	private Button bPost;

	private Intent intent;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			boolean isSuccess = (Boolean) msg.obj;
			if (isSuccess) {
				Toast.makeText(BlogShare.this, "blog post successed",
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(BlogShare.this,
						"blog post failed,please check the net",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		intent = getIntent();
		if (intent == null) {
			finish();
		}
		setContentView(R.layout.blog_share);
		initView();
		initData();
	}

	private void initView() {
		
		eBlogTitle = (EditText) findViewById(R.id.et_blog_title);
		eBlogContent = (EditText) findViewById(R.id.et_blog_content);
		bPost = (Button) findViewById(R.id.post);
		bPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postBlog();
			}
		});
	}

	private void initData() {
		eBlogTitle.setText(intent.getStringExtra(Intent.EXTRA_SUBJECT));
		eBlogContent.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
	}

	private void postBlog() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean isSuccess = NetUtil.post(new BlogInfo(),eBlogTitle
						.getText().toString(), eBlogContent.getText()
						.toString());
				Message message = handler.obtainMessage();
				message.obj = isSuccess;
				handler.sendMessage(message);
			}
		}).start();
	}

}
