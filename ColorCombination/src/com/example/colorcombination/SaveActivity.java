package com.example.colorcombination;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SaveActivity extends Activity{
	
	private SaveStore store;
	private SimpleCursorAdapter adapter;
	private ListView saveList;
	private EditText saveName;
	private Cursor cursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_activity);
		
		store = new SaveStore(this);
		store.openToWrite();
		
		cursor = store.getSaveList();
		startManagingCursor(cursor);
		
		// формируем столбцы сопоставления
		String[] from = new String[] { DB.COLUMN_IMG, DB.COLUMN_TXT };
		int[] to = new int[] { R.id.ivImg, R.id.tvText };
		
		adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
		
		saveList = (ListView) findViewById(R.id.save_list);
		saveList.setAdapter(adapter);
		
		registerForContextMenu(saveList);
		
		saveName = (EditText)findViewById(R.id.save_name);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_save, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.save_btn:
				newSave();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == CM_DELETE_ID) {
			// получаем из пункта контекстного меню данные по пункту списка 
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			// извлекаем id записи и удаляем соответствующую запись в БД
			Log.w("SaveActivity.onContextItemSelected", acmi.id);
			store.delSave(acmi.id);
			// обновляем курсор
			cursor.requery();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		store.close();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		store.close();
	}
	
	private void newSave(){
		if(!saveName.getText().equals("")){
			Intent intent = new Intent();
			intent.putExtra("saveName", saveName.getText());
			setResult(RESULT_OK, intent);
			finish();
		}
		else{
			/* ВЫВОДИМ СООБЩЕНИЕ */
		}
	}
}
