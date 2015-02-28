package com.sinaapp.whutec.amanda.page;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.provider.SyncStateContract.Constants;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.Constant;
import com.sinaapp.whutec.amanda.bean.Blog;
import com.sinaapp.whutec.amanda.page.adapter.FaceAdapter;
import com.sinaapp.whutec.amanda.util.StringUtil;
import com.sinaapp.whutec.amanda.R;

public class PostBlog extends BaseActivity {

	private static final int MESSAGE_POST_BLOG = 0;
	private static final int MESSAGE_UPDATE_BLOG = 1;

	private EditText blogTitle;
	private EditText blogContent;
	private GridView gridView;
	private ImageButton faceButton;

	private FaceAdapter faceAdapter;

	private Context context;

	private boolean clearTempMood = false;
	private String tempTitle = "";
	private String tempContent = "";

	private boolean isUpdateEvent = false;
	private Blog mBlog = null;

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_POST_BLOG:
				final Blog item = (Blog) msg.obj;
				if (item != null) {
					clearTempMood = true;
					Intent data = new Intent();
					data.putExtra(Constant.KEY, item);
					((Activity) context).setResult(
							MainPage.INTENT_REQUEST_CODE_POSTBLOG, data);
					((Activity) context).finish();
				}
				break;
			case MESSAGE_UPDATE_BLOG:
				final Blog item1 = (Blog) msg.obj;
				if (item1 != null) {
					clearTempMood = true;
					Intent data = new Intent();
					data.putExtra(Constant.KEY, item1);
					((Activity) context).setResult(
							MainPage.INTENT_REQUEST_CODE_UPDATEBLOG, data);
					((Activity) context).finish();
				}
				break;
			default:
				break;
			}
			return true;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		mBlog = (Blog) getIntent().getSerializableExtra(Constant.KEY);
		if (mBlog != null) {
			isUpdateEvent = true;
			clearTempMood = true;
		}

		setContentView(R.layout.blog_post);

		initMainView();
		initGridView();
		initFooterView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		tempTitle = config.getProperty(Configuration.TEMP_BLOG_TITLE);
		tempContent = config.getProperty(Configuration.TEMP_BLOG_CONTENT);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isUpdateEvent && mBlog != null) {
			blogTitle.setText(mBlog.getTitle());
			blogContent.setText(mBlog.getContent());
		} else {
			blogTitle.setText(tempTitle);
			blogContent.setText(tempContent);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mood_post, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.publish:
			publish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initMainView() {
		blogTitle = (EditText) findViewById(R.id.et_blog_title);
		blogContent = (EditText) findViewById(R.id.et_blog_content);
		blogTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tempTitle = blogTitle.getText().toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		blogContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tempContent = blogContent.getText().toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		// blogContent.setText(getResources().getString(R.string.blog_content));
		// blogTitle.setText(blogTitle.getText().toString());
	}

	private void initGridView() {
		gridView = (GridView) findViewById(R.id.gridview);
		faceAdapter = new FaceAdapter(context);
		gridView.setAdapter(faceAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SpannableString ss = new SpannableString(view.getTag()
						.toString());
				Drawable d = getResources().getDrawable(
						(int) faceAdapter.getResourceId(position));
				d.setBounds(0, 0, 50, 50);
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				ss.setSpan(span, 0, view.getTag().toString().length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				// mContent.getText().insert(mContent.getSelectionStart(), ss);
			}

		});
	}

	private void initFooterView() {
		faceButton = (ImageButton) findViewById(R.id.footer_face);
		faceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showOrHideImm();
			}
		});
	}

	private void showOrHideImm() {
		if (gridView.getVisibility() == View.VISIBLE) {
			// imm.showSoftInput(mContent, 0);
			gridView.setVisibility(View.GONE);
			faceButton.setImageResource(R.drawable.widget_footer_button_face);
		} else {
			gridView.setVisibility(View.VISIBLE);
			// imm.hideSoftInputFromWindow(mContent.getWindowToken(), 0);
			faceButton
					.setImageResource(R.drawable.widget_footer_button_keyboard);
		}

	}

	private boolean isPublishing = false;

	private void publish() {
		if (isPublishing) {
			Toast.makeText(context, getString(R.string.posting),
					Toast.LENGTH_SHORT).show();
			return;
		}
		final String title = blogTitle.getText().toString();
		final String content = blogContent.getText().toString();
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			Toast.makeText(context,
					getString(R.string.title_content_cannot_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		isPublishing = true;

		if (isUpdateEvent && mBlog != null) {
			update(title, content);
		} else {
			save(title, content);
		}

	}

	private void save(String title, String content) {
		AVObject blogObject = new AVObject(Blog.CLOUD_TABLE_NAME);
		blogObject.put(Blog.COLUMN_TITLE, title);
		blogObject.put(Blog.COLUMN_CONTENT, content);
		blogObject.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException arg0) {
				if (arg0 != null) {
					Toast.makeText(context, getString(R.string.post_fail),
							Toast.LENGTH_SHORT).show();
				} else {
					Blog b = new Blog();
					b.setTitle(blogTitle.getText().toString());
					b.setContent(blogContent.getText().toString());
					b.setDate(StringUtil.dateToString(new Date()));
					Blog.addBlog(context, b);

					Message msg = handler.obtainMessage();
					msg.what = MESSAGE_POST_BLOG;
					msg.obj = b;
					handler.sendMessage(msg);

					Toast.makeText(context, getString(R.string.post_success),
							Toast.LENGTH_SHORT).show();
				}
				isPublishing = false;
			}
		});
	}

	private void update(final String title, final String content) {

		AVQuery<AVObject> query = new AVQuery<AVObject>(Blog.CLOUD_TABLE_NAME);
		query.getInBackground(mBlog.objectId, new GetCallback<AVObject>() {

			@Override
			public void done(AVObject arg0, AVException arg1) {
				if (arg0 != null && arg1 == null) {
					AVObject blogObject = new AVObject(Blog.CLOUD_TABLE_NAME);
					blogObject = arg0;
					blogObject.put(Blog.COLUMN_TITLE, title);
					blogObject.put(Blog.COLUMN_CONTENT, content);
					blogObject.saveInBackground(new SaveCallback() {

						@Override
						public void done(AVException arg0) {
							if (arg0 != null) {
								Toast.makeText(context,
										getString(R.string.post_fail),
										Toast.LENGTH_SHORT).show();
							} else {
								Blog b = new Blog();
								b.objectId = mBlog.objectId;
								b.setTitle(blogTitle.getText().toString());
								b.setContent(blogContent.getText().toString());
								b.setDate(StringUtil.dateToString(new Date()));
								Blog.addBlog(context, b);

								Message msg = handler.obtainMessage();
								msg.what = MESSAGE_UPDATE_BLOG;
								msg.obj = b;
								handler.sendMessage(msg);

								Toast.makeText(context,
										getString(R.string.update_success),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
				} else {
					Toast.makeText(context, getString(R.string.post_fail),
							Toast.LENGTH_SHORT).show();
				}
				isPublishing = false;
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (clearTempMood) {
			Configuration.getConfig().removeProperty(
					Configuration.TEMP_BLOG_TITLE);
			Configuration.getConfig().removeProperty(
					Configuration.TEMP_BLOG_CONTENT);
			tempTitle = "";
			tempContent = "";
		}
		if (!TextUtils.isEmpty(tempTitle)) {
			config.setProperty(Configuration.TEMP_BLOG_TITLE, tempTitle);
		}
		if (!TextUtils.isEmpty(tempContent)) {
			config.setProperty(Configuration.TEMP_BLOG_CONTENT, tempContent);
		}
	}

}
