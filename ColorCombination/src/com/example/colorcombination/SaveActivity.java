package com.example.colorcombination;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment.SavedState;
import android.app.FragmentManager.BackStackEntry;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SaveActivity extends Activity{
	
	protected final static int CM_DELETE_ID = 1;
	
	private SaveStore store;
	private StoreSQLiteHelper dbHelper;
	
	private SimpleCursorAdapter adapter;
	private ListView saveList;
	private EditText saveName;
	private Cursor cursor;
	
	private FragmentManager fManager;
	
	private class ResaveFragment extends DialogFragment {
	    
		private long saveId;
		
		protected ResaveFragment(long saveId){
			this.saveId = saveId;
		}
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.resave_dialog)
	               .setPositiveButton(R.string.resave_ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       Log.w("RESAVE", "RESAVE NAH!!!!");
	                	   resave(saveId);
	                   }
	               })
	               .setNegativeButton(R.string.resave_cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_activity);
		
		fManager = this.getFragmentManager();
		
		
		store = new SaveStore(this);
		store.openToWrite();
		
		cursor = store.getSaveList();
		startManagingCursor(cursor);
		
		// формируем столбцы сопоставления
		String[] from = new String[] { StoreSQLiteHelper.TabTitle.COL_NAME, StoreSQLiteHelper.TabTitle.COL_CREATE_DATE};
		int[] to = new int[] { R.id.item_name, R.id.item_date};
		
		adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);
		
		saveList = (ListView) findViewById(R.id.save_list);
		saveList.setAdapter(adapter);
		
		registerForContextMenu(saveList);
		
		saveList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.w("ID_SELECTED", id+"");
//				AlertDialog dialog = builder.create();
//				dialog.show();
//				dialog.get
//				ResaveFragment d = new ResaveFragment(id);
				new ResaveFragment(id).show(fManager, null);
			}
		});
		
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
		menu.add(0, CM_DELETE_ID, 0, R.string.cm_delete_btn);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == CM_DELETE_ID) {
			// получаем из пункта контекстного меню данные по пункту списка 
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			// извлекаем id записи и удаляем соответствующую запись в БД
			Log.w("SaveActivity.onContextItemSelected", acmi.id+"");
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
	
	private void resave(long id){
		Intent intent = new Intent();
		intent.putExtra("id", id);
		setResult(RESULT_OK, intent);
		Log.w("RESAVE", RESULT_OK+"");
		finish();
	}
	
	private void newSave(){
		Log.w("NEW SAVE", "BEGIN");
		if(!saveName.getText().equals("")){
			Intent intent = new Intent();
			intent.putExtra("name", saveName.getText().toString());
			setResult(RESULT_OK, intent);
			Log.w("NEW SAVE", RESULT_OK+"");
			finish();
		}
		else{
			/* ВЫВОДИМ СООБЩЕНИЕ */
			Log.w("NEW SAVE", "ELSE");
		}
	}
}
