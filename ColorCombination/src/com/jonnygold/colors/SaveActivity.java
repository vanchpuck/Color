package com.jonnygold.colors;

import com.jonnygold.colors.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
//import android.util.Log;
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
	
	public static final int REQUEST_CODE_SAVE = 0;
	
	protected final static int CM_DELETE_ID = 1;
	
	private SaveStore store;
	
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
	                   @Override
					public void onClick(DialogInterface dialog, int id) {
	                	   resave(saveId);
	                   }
	               })
	               .setNegativeButton(R.string.resave_cancel, new DialogInterface.OnClickListener() {
	                   @Override
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
		
		// ��������� ������� �������������
		String[] from = new String[] { StoreSQLiteHelper.TabTitle.COL_NAME, StoreSQLiteHelper.TabTitle.COL_CREATE_DATE};
		int[] to = new int[] { R.id.item_name, R.id.item_date};
		
		adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);
		
		saveList = (ListView) findViewById(R.id.save_list);
		saveList.setAdapter(adapter);
		
		registerForContextMenu(saveList);
		
		saveList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
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
			// �������� �� ������ ������������ ���� ������ �� ������ ������ 
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			// ��������� id ������ � ������� ��������������� ������ � ��
			store.delSave(acmi.id);
			// ��������� ������
			cursor.requery();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		cursor.close();
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
//		Log.w("SAVE_RESUME", "1");
		store.openToWrite();
		cursor = store.getSaveList();
		super.onResume();
		
		
	}
	
	@Override
	protected void onStart() {
//		Log.w("SAVE_START", "1");
		super.onStart();
//		store.openToWrite();
	}
	
	private void resave(long id){
		Intent intent = new Intent();
		intent.putExtra("id", id);
		setResult(RESULT_OK, intent);
//		Log.w("RESAVE", RESULT_OK+"");
		finish();
	}
	
	private void newSave(){
//		Log.w("NEW SAVE", "BEGIN");
		if(!saveName.getText().equals("")){
			Intent intent = new Intent();
			intent.putExtra("name", saveName.getText().toString());
			setResult(RESULT_OK, intent);
//			Log.w("NEW SAVE", RESULT_OK+"");
			finish();
		}
	}
}
