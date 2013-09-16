package com.example.colorcombination;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LoadActivity extends Activity{

	protected final static int CM_DELETE_ID = 1;
	
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
		String[] from = new String[] { StoreSQLiteHelper.TabTitle.COL_NAME, StoreSQLiteHelper.TabTitle.COL_CREATE_DATE};
		int[] to = new int[] { R.id.item_name, R.id.item_date};

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
//		cursor.close();
		store.close();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursor.close();
		store.close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		store.openToRead();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_load, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CM_DELETE_ID, 0, R.string.cm_delete_btn);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == CM_DELETE_ID) {
			// получаем из пункта контекстного меню данные по пункту списка 
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			// извлекаем id записи и удаляем соответствующую запись в БД
			store.delSave(acmi.id);
			// обновляем курсор
			cursor.requery();
			return true;
		}
		return super.onContextItemSelected(item);
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
		finish();
	}
	
}
