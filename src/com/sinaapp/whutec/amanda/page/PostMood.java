package com.sinaapp.whutec.amanda.page;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.Constant;
import com.sinaapp.whutec.amanda.R;
import com.sinaapp.whutec.amanda.bean.Mood;
import com.sinaapp.whutec.amanda.page.adapter.FaceAdapter;
import com.sinaapp.whutec.amanda.ui.util.UIHelper;
import com.sinaapp.whutec.amanda.util.StringUtil;

public class PostMood extends BaseActivity {

	private EditText mContent;
	private LinearLayout clearWord;
	private TextView inputCount;
	private ImageButton faceButton;
	private GridView gridView;

	private FaceAdapter faceAdapter;

	private static final int MESSAGE_POST_MOOD = 0;
	private static final int MESSAGE_UPDATE_MOOD = 1;

	private int moodMaxLength = 200;

	private InputMethodManager imm;
	private Context context;

	private String tempContent = "";

	private boolean clearTempMood = false;// 是否清楚临时内容

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_POST_MOOD:
				final Mood m = (Mood) msg.obj;
				if (m != null) {
					clearTempMood = true;
					Intent data = new Intent();
					data.putExtra(Constant.KEY, m);
					((Activity) context).setResult(
							MainPage.INTENT_REQUEST_CODE_POSTMOOD, data);
					((Activity) context).finish();
				}
				break;
			case MESSAGE_UPDATE_MOOD:
				final Mood m1 = (Mood) msg.obj;
				if (m1 != null) {
					clearTempMood = true;
					Intent data = new Intent();
					data.putExtra(Constant.KEY, m1);
					((Activity) context).setResult(
							MainPage.INTENT_REQUEST_CODE_UPDATEMOOD, data);
					((Activity) context).finish();
				}
				break;
			default:
				break;
			}
			return true;
		}
	});
	
	private boolean isUpdateEvent = false;
	private Mood mMood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mMood = (Mood) getIntent().getSerializableExtra(Constant.KEY);
		if(mMood != null){
			clearTempMood = true;
			isUpdateEvent = true;
		}
		
		setContentView(R.layout.mood_post);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		context = this;
		// initHeadView();
		initMainView();
		initFooterView();
		initGridView();
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

	private boolean isPublishing = false;

	private void publish() {
		if (isPublishing) {
			Toast.makeText(context, getString(R.string.posting),
					Toast.LENGTH_SHORT).show();
			return;
		}
		final String content = mContent.getText().toString();
		if (content.equals("")) {
			Toast.makeText(context, getString(R.string.content_cannot_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		isPublishing = true;
		if(mMood != null && isUpdateEvent){
			update(content);
		}else{
			save(content);
		}
	}
	
	private void save(final String content){
		AVObject moodObject = new AVObject(Mood.CLOUD_TABLE_NAME);
		moodObject.put(Mood.COLUMN_CONTENT, content);
		moodObject.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException arg0) {
				if (arg0 != null) {
					Toast.makeText(context, getString(R.string.post_fail),
							Toast.LENGTH_SHORT).show();
				} else {
					Mood m = new Mood();
					m.setContent(content);
					m.setDate(StringUtil.dateToString(new Date()));
					m = Mood.addMood(context, m);

					Message msg = handler.obtainMessage();
					msg.what = MESSAGE_POST_MOOD;
					msg.obj = m;
					handler.sendMessage(msg);
					Toast.makeText(context, getString(R.string.post_success),
							Toast.LENGTH_SHORT).show();
				}
				isPublishing = false;
			}
		});
	}
	
	private void update(final String content){
		AVQuery<AVObject> query = new AVQuery<AVObject>(Mood.CLOUD_TABLE_NAME);
		query.getInBackground(mMood.objectId, new GetCallback<AVObject>() {
			
			@Override
			public void done(AVObject arg0, AVException arg1) {
				if(arg0 != null && arg1 == null){
					AVObject moodObject = new AVObject(Mood.CLOUD_TABLE_NAME);
					moodObject = arg0;
					moodObject.put(Mood.COLUMN_CONTENT, content);
					moodObject.saveInBackground(new SaveCallback() {

						@Override
						public void done(AVException arg0) {
							if (arg0 != null) {
								Toast.makeText(context, getString(R.string.post_fail),
										Toast.LENGTH_SHORT).show();
							} else {
								Mood m = new Mood();
								m.objectId = mMood.objectId;
								m.setContent(content);
								m.setDate(StringUtil.dateToString(new Date()));
								m = Mood.addMood(context, m);

								Message msg = handler.obtainMessage();
								msg.what = MESSAGE_UPDATE_MOOD;
								msg.obj = m;
								handler.sendMessage(msg);
								Toast.makeText(context, getString(R.string.update_success),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
					isPublishing = false;
				}
			}
		});
		
	}

	private void initMainView() {

		clearWord = (LinearLayout) findViewById(R.id.clear_word);
		clearWord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!tempContent.equals("")) {
					showClearContentDialog();
				}
			}
		});
		inputCount = (TextView) clearWord.findViewById(R.id.input_count);
		inputCount.setText(moodMaxLength + "");
		mContent = (EditText) findViewById(R.id.mood_content);
		mContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tempContent = mContent.getText().toString();
				inputCount.setText((moodMaxLength - tempContent.length()) + "");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gridView.setVisibility(View.GONE);
			}
		});
		
		
		if(isUpdateEvent && mMood != null){
			mContent.setText(UIHelper.parseFaceByText(context, mMood.getContent()));
			mContent.setSelection(mContent.length());
		}else{
			tempContent = Configuration.getConfig().getProperty(
					Configuration.TEMP_MOOD);
			if (!tempContent.equals("")) {
				mContent.setText(UIHelper.parseFaceByText(context, tempContent));
				mContent.setSelection(mContent.length());
			}
		}

		InputFilter filter = new InputFilter.LengthFilter(moodMaxLength);
		mContent.setFilters(new InputFilter[] { filter });
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
				mContent.getText().insert(mContent.getSelectionStart(), ss);
			}

		});
	}

	private void showOrHideImm() {
		if (gridView.getVisibility() == View.VISIBLE) {
			imm.showSoftInput(mContent, 0);
			gridView.setVisibility(View.GONE);
			faceButton.setImageResource(R.drawable.widget_footer_button_face);
		} else {
			gridView.setVisibility(View.VISIBLE);
			imm.hideSoftInputFromWindow(mContent.getWindowToken(), 0);
			faceButton
					.setImageResource(R.drawable.widget_footer_button_keyboard);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, MainPage.class);
			startActivity(intent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (clearTempMood) {
			Configuration.getConfig().removeProperty(Configuration.TEMP_MOOD);
			tempContent = "";
		}
		if (!tempContent.equals("")) {
			Configuration.getConfig().setProperty(Configuration.TEMP_MOOD,
					tempContent);
		}
	}

	private void showClearContentDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.content_clear);
		builder.setPositiveButton(getString(R.string.confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mContent.setText("");
						tempContent = "";
					}
				});
		builder.setNegativeButton(getString(R.string.cancle),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
