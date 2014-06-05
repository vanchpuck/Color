package com.jonnygold.colors;

import java.util.List;

import com.jonnygold.colors.SaveStore.SavedData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SavedDataAdapter extends ArrayAdapter<SavedData> {

	private final List<SavedData> mSavedData;
	
	private LayoutInflater mInflater;
	
	public SavedDataAdapter(Context context, List<SavedData> objects) {
		super(context, R.layout.list_item, objects);
		mSavedData = objects;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView != null){
			return convertView;
		}
		convertView = mInflater.inflate(R.layout.list_item, null);
		
		SavedData saved = mSavedData.get(position);
		
		TextView name = (TextView)convertView.findViewById(R.id.item_name);
		name.setText(saved.getName());
		
		TextView date = (TextView)convertView.findViewById(R.id.item_date);
		date.setText(saved.getDate());
		
		PaletteView palette = (PaletteView)convertView.findViewById(R.id.view_palette);
		palette.setPalette(saved.getPalette());
		
		return convertView;
	}
	
	@Override
	public long getItemId(int position) {
		return mSavedData.get(position).getId();
	}

}
