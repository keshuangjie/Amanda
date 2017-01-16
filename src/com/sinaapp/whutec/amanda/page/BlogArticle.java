package com.sinaapp.whutec.amanda.page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.Constant;
import com.sinaapp.whutec.amanda.bean.Blog;
import com.sinaapp.whutec.amanda.R;

public class BlogArticle extends BaseActivity{

	private TextView blogTitle;
	private TextView blogDate;
	private TextView user;
	private TextView blogContent;
	private Intent intent;
	private Blog blog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.blog_article);
		
		getActionBar().setTitle("日志");
		
		intent = getIntent();
		if(intent == null){
			finish();
		}
		blog = (Blog) intent.getSerializableExtra(Constant.KEY);
		if(blog == null){
			finish();
		}
		initMainView();
	}
	
	private void initMainView(){
		blogTitle = (TextView) findViewById(R.id.article_blog_title);
		blogDate = (TextView) findViewById(R.id.article_blog_publish_date);
		user = (TextView) findViewById(R.id.article_blog_user_name);
		blogContent = (TextView) findViewById(R.id.article_blog_content);
		blogTitle.setText(blog.getTitle());
		blogDate.setText(blog.getDate());
		blogContent.setText("    "+blog.getContent());
		user.setText(Configuration.getConfig().getUserName());
	}
}
