package com.example.colorcombination;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LoadActivity extends Activity{

	private SaveStore store;
	private StoreSQLiteHelper dbHelper;
	
	private SimpleCursorAdapter adapter;
	private ListView saveList;
	private Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_activity);
		
		store = new SaveStore(this);
		store.openToRead();
		
		cursor = store.getSaveList();
		
		startManagingCursor(cursor);
		
		// формируем столбцы сопоставления
		String[] from = new String[] { StoreSQLiteHelper.TabTitle.COL_NAME};
		int[] to = new int[] { R.id.item_name};
		
		adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);
		
		saveList = (ListView) findViewById(R.id.load_list);
		saveList.setAdapter(adapter);
		
		registerForContextMenu(saveList);
		
		saveList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				load(id);
			}
		});
	}
	
	@Override
	protected void onStop() {
		
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_load, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle presses on the action bar items
//		switch (item.getItemId()) {
//			case R.id.load_btn:
//				load();
//				return true;
//			default:
//				return super.onOptionsItemSelected(item);
//		}
//	}
		
	protected void load(long id){
		Intent intent = new Intent();
		intent.putExtra("id", id);
		setResult(RESULT_OK, intent);
		Log.w("NEW LOAD", RESULT_OK+"");
		finish();
	}
	
}
