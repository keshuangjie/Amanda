package com.sinaapp.whutec.amanda.page.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinaapp.whutec.amanda.R;
import com.sinaapp.whutec.amanda.bean.Blog;
import com.sinaapp.whutec.amanda.ui.util.UIHelper;

public class BlogAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Blog> list;
	private static final int length = 100;
	
	public BlogAdapter(Context context, ArrayList<Blog> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		HolderView holder;
		if(convertView == null){
			holder = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.blog_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.blog_title);
			holder.content = (TextView) convertView.findViewById(R.id.blog_content);
			holder.date = (TextView) convertView.findViewById(R.id.blog_date);
		}else{
			holder = (HolderView) convertView.getTag();
		}
		Blog item = list.get(position);
		if(item != null){
			
			holder.title.setText(item.getTitle());
			holder.content.setText(UIHelper.parseFaceByText(context,item.getContent(),length));
			holder.date.setText(item.getDate());
			holder.blog = item;
			
			convertView.setTag(holder);
		}
		return convertView;
	}
	
	public static class HolderView{
		ImageView imageFace;
		TextView title;
		TextView content;
		TextView date;
		public Blog blog;
	}

}
