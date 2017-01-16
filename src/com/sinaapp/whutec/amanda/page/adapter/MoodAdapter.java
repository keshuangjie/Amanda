package com.sinaapp.whutec.amanda.page.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinaapp.whutec.amanda.R;
import com.sinaapp.whutec.amanda.bean.Mood;
import com.sinaapp.whutec.amanda.ui.util.UIHelper;

public class MoodAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Mood> list;
	
	public MoodAdapter(Context context, ArrayList<Mood> list){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.mood_item, null);
//			holder.imageFace = (ImageView) convertView.findViewById(R.id.image_face);
//			holder.name = (TextView) convertView.findViewById(R.id.mood_name);
			holder.content = (TextView) convertView.findViewById(R.id.mood_content);
			holder.date = (TextView) convertView.findViewById(R.id.mood_date);
		}else{
			holder = (HolderView) convertView.getTag();
		}
//		holder.imageFace.setImageBitmap(Configuration.getConfig().getUserFaceImage());
//		holder.name.setText(list.get(position).getId() + Configuration.getConfig().getUserName());
		holder.content.setText(UIHelper.parseFaceByText(context,list.get(position).getContent()));
		holder.date.setText(list.get(position).getDate());
		holder.mood = list.get(position);
		
		convertView.setTag(holder);
		
		return convertView;
	}
	
	public class HolderView{
//		ImageView imageFace;
//		TextView name;
		TextView content;
		TextView date;
		public Mood mood;
	}

}
