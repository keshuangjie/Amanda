package com.sinaapp.whutec.amanda.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinaapp.whutec.amanda.R;

public class LoadMoreListView extends ListView implements OnScrollListener {

	private static final String TAG = "LoadMoreListView";

	/**
	 * Listener that will receive notifications every time the list scrolls.
	 */
	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;

	// footer view
	private RelativeLayout mFooterView;
	// private TextView mLabLoadMore;
	private ProgressBar mProgressBarLoadMore;
	private TextView loadMore;

	// Listener to process load more items when user reaches the end of the list
	private OnLoadMoreListener mOnLoadMoreListener;
	// To know if the list is loading more items
	private boolean mIsLoadingMore = false;
	private int mCurrentScrollState;

	public LoadMoreListView(Context context) {
		super(context);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		// footer
		mFooterView = (RelativeLayout) mInflater.inflate(
				R.layout.load_more_footer, this, false);
		
		loadMore = (TextView) mFooterView.findViewById(R.id.load_more);
		
		/*
		 * mLabLoadMore = (TextView) mFooterView
		 * .findViewById(R.id.load_more_lab_view);
		 */
		mProgressBarLoadMore = (ProgressBar) mFooterView
				.findViewById(R.id.load_more_progressBar);
		loadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				loadMore.setVisibility(View.GONE);
//				mProgressBarLoadMore.setVisibility(View.VISIBLE);
//				mIsLoadingMore = true;
//				onLoadMore();
			}
		});
		
		

		addFooterView(mFooterView);

		super.setOnScrollListener(this);
	}
	
	public void setEmptyView(){
		ViewGroup parentView = (ViewGroup) this.getParent();
		LinearLayout empty = (LinearLayout) mInflater.inflate(R.layout.list_empty, parentView ,false);
		parentView.addView(empty);
		setEmptyView(empty);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}

	/**
	 * Set the listener that will receive notifications every time the list
	 * scrolls.
	 * 
	 * @param l
	 *            The scroll listener.
	 */
	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		mOnScrollListener = l;
	}

	/**
	 * Register a callback to be invoked when this list reaches the end (last
	 * item be visible)
	 * 
	 * @param onLoadMoreListener
	 *            The callback to run.
	 */

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}
	
	public void removeOnLoadMoreListerner(){
		mOnLoadMoreListener = null;
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
//		boolean isEnd = (firstVisibleItem + visibleItemCount == totalItemCount); 
//		System.out.println("firstVisibleItem:" + firstVisibleItem);
//		System.out.println("visibleItemCount:" + visibleItemCount);
//		System.out.println("totalItemCount:" + totalItemCount);
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	public void onLoadMore() {
		Log.d(TAG, "onLoadMore");
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * Notify the loading more operation has finished
	 */
	public void onLoadMoreComplete() {
		mIsLoadingMore = false;
		this.loadMore.setVisibility(View.VISIBLE);
		mProgressBarLoadMore.setVisibility(View.GONE);
	}
	
	/**
	 * Notify the database has loaded completely
	 */
	public void onLoadDataComplete(){
		removeOnLoadMoreListerner();
//		mFooterView.setVisibility(View.GONE);
//		removeFooterView(mFooterView);
		this.loadMore.setVisibility(View.VISIBLE);
		loadMore.setOnClickListener(null);
		this.loadMore.setText(R.string.no_more);
		mProgressBarLoadMore.setVisibility(View.GONE);
	}

	/**
	 * Interface definition for a callback to be invoked when list reaches the
	 * last item (the user load more items in the list)
	 */
	public interface OnLoadMoreListener {
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user)
		 */
		public void onLoadMore();
	}

}