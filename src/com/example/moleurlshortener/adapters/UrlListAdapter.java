package com.example.moleurlshortener.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.moleurlshortener.MainActivity;
import com.example.moleurlshortener.R;
import com.example.moleurlshortener.DAO.UrlObject;

public class UrlListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<UrlObject> mList = new ArrayList<UrlObject>();

	public UrlListAdapter(Context context, ArrayList<UrlObject> list) {
		this.mList = list;
		this.mContext = context;
	}

	private class ViewHolder {
		private TextView originalUrl;
		private TextView createdUrl;
	}

	public void refreshList(ArrayList<UrlObject> list) {
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public UrlObject getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = ((Activity) mContext).getLayoutInflater().inflate(
					R.layout.url_item_list, null);

			holder = new ViewHolder();
			holder.originalUrl = (TextView) view
					.findViewById(R.id.original_url_textview);
			holder.createdUrl = (TextView) view
					.findViewById(R.id.created_url_textview);

			view.setTag(holder);
		} else
			holder = (ViewHolder) view.getTag();

		UrlObject url = getItem(position);

		holder.originalUrl.setText(url.getOriginalUrl());
		holder.createdUrl.setText(url.getCreatedUrl());

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final ViewHolder holder = (ViewHolder) v.getTag();
				((MainActivity) mContext).createDialog(holder.originalUrl.getText().toString(), holder.createdUrl.getText().toString());
			}
		});

		return view;
	}
}
