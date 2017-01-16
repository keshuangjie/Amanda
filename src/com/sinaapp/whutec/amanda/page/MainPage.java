package com.sinaapp.whutec.amanda.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.sinaapp.whutec.amanda.AppManager;
import com.sinaapp.whutec.amanda.Configuration;
import com.sinaapp.whutec.amanda.Constant;
import com.sinaapp.whutec.amanda.R;
import com.sinaapp.whutec.amanda.bean.Blog;
import com.sinaapp.whutec.amanda.bean.Mood;
import com.sinaapp.whutec.amanda.page.adapter.BlogAdapter;
import com.sinaapp.whutec.amanda.page.adapter.MoodAdapter;
import com.sinaapp.whutec.amanda.page.adapter.MoodAdapter.HolderView;
import com.sinaapp.whutec.amanda.util.Log;
import com.sinaapp.whutec.amanda.util.StringUtil;
import com.sinaapp.whutec.amanda.widget.LoadMoreListView;
import com.sinaapp.whutec.amanda.widget.LoadMoreListView.OnLoadMoreListener;
import com.sinaapp.whutec.amanda.widget.ScrollLayout;

public class MainPage extends BaseActivity{
	
	private int curSelect = 0;
	
	private ScrollLayout scrollLayout;
	
//	private ImageView headLeftLogo;
	private ImageView iWriteMood;
	private ImageView iWriteBlog;
	
	private RadioButton rmood;
	private RadioButton rblog;
//	private ImageView more;
	
	private LoadMoreListView moodListView;
	private MoodAdapter moodAdapter;
	private LoadMoreListView blogListView;
	private BlogAdapter blogAdapter;
	
	
	private ArrayList<Mood> moodList = new ArrayList<Mood>();
	private ArrayList<Blog> blogList = new ArrayList<Blog>();
	
	private static final int MESSAGE_INIT_DATA = 1;
	private static final int MESSAGE_LOAD_MORE_MOOD = 2;
	private static final int MESSAGE_LOAD_MORE_BLOG = 3;
	
    public static final int INTENT_REQUEST_CODE_POSTMOOD = 0;
    public static final int INTENT_REQUEST_CODE_POSTBLOG = 1;
    public static final int INTENT_REQUEST_CODE_UPDATEMOOD = 2;
    public static final int INTENT_REQUEST_CODE_UPDATEBLOG = 3;
	
	private Context context;
	private int moodPageIndex = 0;
	private int blogPageIndex = 0;
//	private LoadMoodTask loadMoodTask;
//	private LoadBlogTask loadBlogTask;
	private boolean isScroll = true;
	
	private boolean test = false;

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_INIT_DATA:
				loadMoodData();
				moodAdapter.notifyDataSetChanged();
				break;
			case MESSAGE_LOAD_MORE_MOOD:
				List<Mood> list = (List<Mood>) msg.obj;
				if (list == null) {
					break;
				}
				moodListView.onLoadMoreComplete();
				moodList.addAll(list);
				moodAdapter.notifyDataSetChanged();
				break;
			case MESSAGE_LOAD_MORE_BLOG:
				List<Blog> list1 = (List<Blog>) msg.obj;
				if (list1 == null) {
					break;
				}
				blogListView.onLoadDataComplete();
				blogList.addAll(list1);
				blogAdapter.notifyDataSetChanged();
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
		setContentView(R.layout.main);
	
		context = this;
		
		initHeader();
		initFooter();
		initScrollLayout();
		initBlogLayout();
		initMoodLayout();
		
//		initData();
		
	}
	
	private boolean mIsFirstStart = false;
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(mIsFirstStart){
			return;
		}
		
		mIsFirstStart = true;
		
//		if(test){
//			insertTestData();//���ڲ�����
//			test = false;
//		}
	    Message message = handler.obtainMessage();
	    message.what = MESSAGE_INIT_DATA;
	    handler.sendMessage(message);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isScroll = Configuration.getConfig().getSharedPre().getBoolean(Setting.SETTING_ISSCROLL, true);
		scrollLayout.setIsScroll(isScroll);//�����Ƿ�ɻ���
	}
	
    private void initHeader() {
//    	headLeftLogo = (ImageView) findViewById(R.id.head_left_logo);
//    	iWriteMood = (ImageView) findViewById(R.id.head_right_write);
//    	iWriteBlog = (ImageView) findViewById(R.id.head_right_write_blog);
//    	iWriteMood.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context,PostMood.class);
//				startActivityForResult(intent, INTENT_REQUEST_CODE_POSTMOOD);
//				allowNextResume = true;
//			}
//		});
//    	iWriteBlog.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context,PostBlog.class);
//				startActivityForResult(intent, INTENT_REQUEST_CODE_POSTBLOG);
//				allowNextResume = true;
//			}
//		});
//    	iWriteMood.setVisibility(View.VISIBLE);
	}
    
    private void initFooter(){
    	rmood = (RadioButton) findViewById(R.id.radio_mood);
    	rblog = (RadioButton) findViewById(R.id.radio_blog);
//    	more = (ImageView) findViewById(R.id.more);
//    	more.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context,Setting.class);
//				startActivity(intent);
//				allowNextResume = true;
//			}
//		});
    }

	private void initScrollLayout() {
		scrollLayout = (ScrollLayout) findViewById(R.id.scrollLayout);
		LinearLayout footerLayout = (LinearLayout) findViewById(R.id.footerLayout);
		int childCount = scrollLayout.getChildCount();
		RadioButton[] radioButtons = new RadioButton[childCount];
		for(int i=0;i<childCount;i++){
			radioButtons[i] = (RadioButton) footerLayout.getChildAt(i*2);
			radioButtons[i].setChecked(false);
			radioButtons[i].setTag(i);
			radioButtons[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();
					scrollLayout.snapToScreen(pos);
				}
			});
		}
		curSelect = 0;
		radioButtons[curSelect].setSelected(true);
		scrollLayout.SetOnViewChangeListener(new ScrollLayout.OnViewChangeListener() {
			
			@Override
			public void OnViewChange(int viewIndex) {
				setCurPoint(viewIndex);
				loadInitData(viewIndex);
			}
		});
	}
	
	protected void setHeaderTitle(){
		super.setHeaderTitle();
	}
	
	/**
	 * @param viewIndex
	 */
	private void setCurPoint(int viewIndex){
		if(curSelect == viewIndex){
			return;
		}
		curSelect = viewIndex;
		String title[] = getResources().getStringArray(R.array.header_title);
		switch(viewIndex){
		case 0:
			rmood.setSelected(true);
			rblog.setSelected(false);
			break;
		case 1:
			rmood.setSelected(false);
			rblog.setSelected(true);
			break;
		}
	}
	
	
	private void loadInitData(int viewIndex){
		switch(viewIndex){
		case 0:
			if(moodList==null || moodList.size()==0){
				loadMoodData();
			}
			break;
		case 1:
			if(blogList==null || blogList.size()==0){
				loadBlogData();
			}
			break;
		}
	}
	
    private void initMoodLayout() {
    	moodListView = (LoadMoreListView) findViewById(R.id.mood_list);
    	moodListView.setEmptyView();
    	moodListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
//				loadMoodData(++moodPageIndex);
			}
		});
    	moodAdapter = new MoodAdapter(this, moodList);
    	moodListView.setAdapter(moodAdapter);
    	registerForContextMenu(moodListView);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.writeMood:
    		Intent intent1 = new Intent(context,PostMood.class);
			startActivityForResult(intent1, INTENT_REQUEST_CODE_POSTMOOD);
			allowNextResume = true;
			break;
        case R.id.writeBlog:
        	Intent intent2 = new Intent(context,PostBlog.class);
			startActivityForResult(intent2, INTENT_REQUEST_CODE_POSTBLOG);
			allowNextResume = true;
			break;
        case R.id.setting:
        	Intent intent = new Intent(context,Setting.class);
			startActivity(intent);
			allowNextResume = true;
			break;
		case R.id.importt:
			break;
        case R.id.export:
        	String path = Mood.exportMood(context);
        	Toast.makeText(context, "数据备份成功，路径：" + path, Toast.LENGTH_LONG).show();
			break;
        case R.id.exit:
        	AppManager.getAppManager().AppExit(context);
        	break;
		}
    	return super.onOptionsItemSelected(item);
    }

    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	MenuInflater inflater = getMenuInflater();
    	switch (curSelect) {
		case 0:
			inflater.inflate(R.menu.menu_mood, menu);
			super.onCreateContextMenu(menu, moodListView, menuInfo);
			break;

		case 1:
			inflater.inflate(R.menu.menu_blog, menu);
			super.onCreateContextMenu(menu, moodListView, menuInfo);
			break;
		}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo acm = (AdapterContextMenuInfo) item.getMenuInfo();
    	View view = acm.targetView;
    	System.out.println("curSelect:"+curSelect);
    	switch(curSelect){
    	case 0:
    		Mood mood = (Mood) ((HolderView)view.getTag()).mood;
    		handleMoodItem(item, mood);
    		break;
    	case 1:
    		Blog blog = (Blog) ((BlogAdapter.HolderView)view.getTag()).blog;
    		handleBlogItem(item, blog);
    		break;
    	}
    	return super.onContextItemSelected(item);
    }
    
    private void handleMoodItem(MenuItem item,Mood m){
    	System.out.println("item.getItemId():"+item.getItemId());
    	System.out.println("R.id.menu_share:"+R.id.menu_share);
    	System.out.println("R.id.menu_delete:"+R.id.menu_delete);
    	switch (item.getItemId()) {
    	case R.id.menu_share:
			shared("", m.getContent());
			break;
		case R.id.copy:
			ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(m.getContent());
        	Toast.makeText(context, "���������Ѹ��Ƶ����а�", Toast.LENGTH_LONG).show();
			break;
		case R.id.menu_update:
			Intent intent1 = new Intent(context,PostMood.class);
			intent1.putExtra(Constant.KEY, m);
			startActivityForResult(intent1, INTENT_REQUEST_CODE_UPDATEMOOD);
			allowNextResume = true;
			break;
		case R.id.menu_delete:
			//ɾ��һ������
			Mood.deleteMood(context, m);
//			deleteMood(m);
			notifyMoodList(false, m);
			break;
		}
    }
    
    private void handleBlogItem(MenuItem item, Blog b){
    	switch (item.getItemId()) {
    	case R.id.share:
			shared(b.getTitle(), b.getContent());
			break;
		case R.id.edit:
			Intent intent = new Intent(context,PostBlog.class);
			intent.putExtra(Constant.KEY, b);
			startActivityForResult(intent, INTENT_REQUEST_CODE_UPDATEBLOG);
			allowNextResume = true;
			break;
		case R.id.delete:
			Blog.deleteBlog(context, b);
//			deleteBlog(b);
			notifyBlogList(false, b);
			break;
		}
    }
    
    private void shared(String title, String content){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		startActivity(Intent.createChooser(intent, "选择分享渠道"));
	}
    
	private void initBlogLayout() {
		blogListView = (LoadMoreListView) findViewById(R.id.blog_list);
    	blogListView.setEmptyView();
    	blogListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
//				loadBlogData(++blogPageIndex);
			}
		});
    	blogListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context,BlogArticle.class);
				Blog blog = ((com.sinaapp.whutec.amanda.page.adapter.BlogAdapter.HolderView) view.getTag()).blog;
				intent.putExtra(Constant.KEY, blog);
				startActivity(intent);
			}
		});
    	blogAdapter = new BlogAdapter(this, blogList);
    	blogListView.setAdapter(blogAdapter);
    	registerForContextMenu(blogListView);
	}
	
	private void loadMoodData(){
//		loadMoodData(0);
		loadMoodDataFromNet();
	}
	
//	/**
//	 * @param pageIndex
//	 */
//	private void loadMoodData(final int pageIndex){
//		if(loadMoodTask != null){
//			Status diStatus = loadMoodTask.getStatus();
//			if(diStatus != Status.FINISHED){
//				return;
//			}
//		}
//		loadMoodTask = new LoadMoodTask();
//		loadMoodTask.execute(Integer.valueOf(pageIndex));
//    }
	
	private void loadMoodDataFromNet(){
		AVQuery<AVObject> query = new AVQuery<AVObject>(Mood.CLOUD_TABLE_NAME);
//		query.orderByDescending("createAt");
		query.findInBackground(new FindCallback<AVObject>() {
		    public void done(List<AVObject> avObjects, AVException e) {
		        if (e == null) {
		        	ArrayList<Mood> moods = new ArrayList<Mood>();
		        	for(AVObject o : avObjects){
		        		Mood m = new Mood();
		        		m.objectId = o.getObjectId();
		        		m.setContent(o.getString(Mood.COLUMN_CONTENT));
		        		m.setDate(StringUtil.dateToString(o.getCreatedAt()));
		        		moods.add(m);
		        	}
		        	
		        	Message msg = handler.obtainMessage();
					msg.what = MESSAGE_LOAD_MORE_MOOD;
					msg.obj = moods;
					handler.sendMessage(msg);
		        } else {
		            Log.e("kshj", "MainPage -> loadMoodDataFromNet() -> 加载数据失败");
		        }
		    }
		});
	}
	
	private void loadBlogDataFromNet(){
		AVQuery<AVObject> query = new AVQuery<AVObject>(Blog.CLOUD_TABLE_NAME);
//		query.orderByDescending("createAt");
		query.findInBackground(new FindCallback<AVObject>() {
		    public void done(List<AVObject> avObjects, AVException e) {
		        if (e == null) {
		        	ArrayList<Blog> blogs = new ArrayList<Blog>();
		        	for(AVObject o : avObjects){
		        		Blog b = new Blog();
		        		b.objectId = o.getObjectId();
		        		b.setTitle(o.getString(Blog.COLUMN_TITLE));
		        		b.setContent(o.getString(Blog.COLUMN_CONTENT));
		        		b.setDate(StringUtil.dateToString(o.getCreatedAt()));
		        		blogs.add(b);
		        	}
		        	Message msg = handler.obtainMessage();
					msg.what = MESSAGE_LOAD_MORE_BLOG;
					msg.obj = blogs;
					handler.sendMessage(msg);
		        } else {
		            Log.e("kshj", "MainPage -> loadMoodDataFromNet() -> 加载数据失败");
		        }
		    }
		});
	}
	
	private void loadBlogData(){
//		loadBlogData(0);
		loadBlogDataFromNet();
	}
	
//	/**
//	 * ������һҳ��������
//	 * @param pageIndex
//	 */
//	private void loadBlogData(final int pageIndex){
//		if(loadBlogTask != null){
//			Status diStatus = loadBlogTask.getStatus();
//			if(diStatus != Status.FINISHED){
//				return;
//			}
//		}
//		loadBlogTask = new LoadBlogTask();
//		loadBlogTask.execute(Integer.valueOf(pageIndex));
//    }
	
//    class LoadBlogTask extends AsyncTask<Integer, Void, ArrayList<Blog>>{
//		
//		@Override
//		protected void onPostExecute(ArrayList<Blog> result) {
//			ArrayList<Blog> list = result;
//			if(list == null){
//				//footerView��ʾ���ݼ�����,progressView��ʧ��
//				blogListView.onLoadDataComplete();
//			}else{
//				if(list.size()<Blog.pageSize){
//					//footerView��ʾ���ݼ�����,progressView��ʧ��������list
//					blogListView.onLoadDataComplete();
//				}else{
//					////footerView��ʾ�����������,progressView��ʧ������list
//					blogListView.onLoadMoreComplete();
//				}
//				Message msg = handler.obtainMessage();
//				msg.what = MESSAGE_LOAD_MORE_BLOG;
//				msg.obj = list;
//				handler.sendMessage(msg);
//			}
//			
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected void onCancelled() {
//			super.onCancelled();
//		}
//
//		@Override
//		protected ArrayList<Blog> doInBackground(Integer... pageIndex) {
//			ArrayList<Blog> list = null;
//			list = (ArrayList<Blog>) Blog.listBlog(context, ((java.lang.Integer) pageIndex[0]).intValue());
//			return list;
//		}
//	}
//	
//	class LoadMoodTask extends AsyncTask<Integer, Void, ArrayList<Mood>>{
//		
//		@Override
//		protected void onPostExecute(ArrayList<Mood> result) {
//			ArrayList<Mood> list = result;
//			if(list == null){
//				moodListView.onLoadDataComplete();
//			}else{
//				if(list.size()<Mood.pageSize){
//					moodListView.onLoadDataComplete();
//				}else{
//					moodListView.onLoadMoreComplete();
//				}
//				Message msg = handler.obtainMessage();
//				msg.what = MESSAGE_LOAD_MORE_MOOD;
//				msg.obj = list;
//				handler.sendMessage(msg);
//			}
//			
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected void onCancelled() {
//			super.onCancelled();
//		}
//
//		@Override
//		protected ArrayList<Mood> doInBackground(Integer... pageIndex) {
//			ArrayList<Mood> list = null;
//			list = (ArrayList<Mood>) Mood.listMood(context, ((java.lang.Integer) pageIndex[0]).intValue());
//			return list;
//		}
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case INTENT_REQUEST_CODE_POSTMOOD:
			if(data == null){
				break;
			}
			Mood m = (Mood) data.getSerializableExtra(Constant.KEY);
			if(m != null){
				notifyMoodList(true, m);
			}
			break;
		case INTENT_REQUEST_CODE_UPDATEMOOD:
			if(data == null){
				break;
			}
			Mood m1 = (Mood) data.getSerializableExtra(Constant.KEY);
			if(m1 != null){
				updateMood(m1);
			}
			break;
		case INTENT_REQUEST_CODE_POSTBLOG:
			if(data == null){
				break;
			}
			Blog b = (Blog) data.getSerializableExtra(Constant.KEY);
			if(b != null){
				notifyBlogList(true,b);
			}
			break;
		case INTENT_REQUEST_CODE_UPDATEBLOG:
			if(data == null){
				break;
			}
			Blog b1 = (Blog) data.getSerializableExtra(Constant.KEY);
			if(b1 != null){
				updateBlog(b1);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * moodList��Mood.count�仯��������֤moodlist�����ݿ�ͳһ
	 * @param flag
	 * flagΪtrueʱ������һ������
	 * flagΪfalseʱ��ɾ��һ������
	 */
	private void notifyMoodList(boolean flag, Mood m){
		if(flag){
			moodList.add(0, m);
		}else{
			moodList.remove(m);
		}
		moodAdapter.notifyDataSetChanged();
	}
	
	private void deleteMood(Mood m){
		AVQuery<AVObject> query = new AVQuery<AVObject>(Mood.CLOUD_TABLE_NAME);
		query.getInBackground(m.objectId, new GetCallback<AVObject>() {

			@Override
			public void done(AVObject arg0, AVException arg1) {
				if(arg0 != null && arg1 == null){
					AVObject moodObject = new AVObject(Mood.CLOUD_TABLE_NAME);
					moodObject = arg0;
					moodObject.deleteInBackground(new DeleteCallback() {
						
						@Override
						public void done(AVException arg0) {
							if(arg0 == null){
								Toast.makeText(MainPage.this, "删除成功", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(MainPage.this, "删除失败", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}else{
					Toast.makeText(MainPage.this, "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	
	private void deleteBlog(Blog b){
		AVQuery<AVObject> query = new AVQuery<AVObject>(Blog.CLOUD_TABLE_NAME);
		query.getInBackground(b.objectId, new GetCallback<AVObject>() {

			@Override
			public void done(AVObject arg0, AVException arg1) {
				if(arg0 != null && arg1 == null){
					AVObject blogObject = new AVObject(Blog.CLOUD_TABLE_NAME);
					blogObject = arg0;
					blogObject.deleteInBackground(new DeleteCallback() {
						
						@Override
						public void done(AVException arg0) {
							if(arg0 == null){
								Toast.makeText(MainPage.this, "删除成功", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(MainPage.this, "删除失败", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}else{
					Toast.makeText(MainPage.this, "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	
	private void updateMood(Mood m){
		for(Mood item : moodList){
			if(item.objectId.equals(m.objectId)){
				item.setContent(m.getContent());
				moodAdapter.notifyDataSetChanged();
				return;
			}
		}
	}
	
	private void notifyBlogList(boolean flag, Blog b){
		if(flag){
			blogList.add(0, b);
		}else{
			blogList.remove(b);
		}
		blogAdapter.notifyDataSetChanged();
	}
	
	private void updateBlog(Blog b){
		for(Blog item : blogList){
			if(item.objectId.equals(b.objectId)){
				item.setContent(b.getContent());
				item.setTitle(b.getTitle());
				blogAdapter.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().AppExit(context);
		}
		return super.onKeyDown(keyCode, event);
	}

}
