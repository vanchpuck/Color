package com.jonnygold.colors;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SaveStore {
	
	private SQLiteDatabase db;
	private StoreSQLiteHelper dbHelper;
	private SimpleDateFormat dateFormat;
	private Context context;
	
	public SaveStore(Context context){
		dbHelper = new StoreSQLiteHelper(context);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.context = context;
	}
	
	public void openToRead() throws SQLException {
		dbHelper = new StoreSQLiteHelper(context);
		db = dbHelper.getReadableDatabase();
	}
	
	public void openToWrite() throws SQLException {
		dbHelper = new StoreSQLiteHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		db.close();
		dbHelper.close();
	}
	
	/*
	*	������������ ���������� ����
	*/
	public void save(String name, ColorCombinationView colors){
		
		/* �������� ���������� */
	
		db.beginTransaction();
		try{
//			Log.w("INSERT", "BEGIN");
			ContentValues values = new ContentValues();
			values.put(StoreSQLiteHelper.TabTitle.COL_NAME, name);
			values.put(StoreSQLiteHelper.TabTitle.COL_CREATE_DATE, dateFormat.format(new Date()));
			long insertId = db.insertOrThrow(StoreSQLiteHelper.TabTitle.NAME, null, values);
//			Log.w("INSERT", "ID: :"+insertId+"");
			
			float height = 0;
			for(int i=0; i<colors.getColorsCount(); i++){
				values.clear();
				values.put(StoreSQLiteHelper.TabContent.COL_TITLE_ID, insertId);
				values.put(StoreSQLiteHelper.TabContent.COL_COLOR, colors.getColorBlock(i).getColor());
				
				height = (float)colors.getColorBlock(i).getHeight()/(float)colors.getHeight();
				values.put(StoreSQLiteHelper.TabContent.COL_SIZE, height);
				long id = db.insertOrThrow(StoreSQLiteHelper.TabContent.NAME, null, values);
//				Log.w("INSERT", id+"");
			}
			db.setTransactionSuccessful();
//			Log.w("SAVE", "SUCCEES");
		}
		catch(android.database.sqlite.SQLiteException exc){
//			Log.w("SAVE", "Exception: "+exc.getMessage());
		}
		finally{
			db.endTransaction();
//			Log.w("SAVE", "END TRANSACTION");
		}
	}
	
	public void save(long id, String name, ColorCombinationView colors){
		
		/* �������� ���������� */
		db.beginTransaction();
		try{
			ContentValues values = new ContentValues();
			values.put(StoreSQLiteHelper.TabTitle.COL_NAME, name);
			values.put(StoreSQLiteHelper.TabTitle.COL_CREATE_DATE, dateFormat.format(new Date()));

//			db.update(StoreSQLiteHelper.TabTitle.NAME, values, StoreSQLiteHelper.TabTitle.COL_ID+"="+id, null);

			db.delete(StoreSQLiteHelper.TabContent.NAME, StoreSQLiteHelper.TabContent.COL_TITLE_ID+"="+id, null);

			float height = 0;
			for(int i=0; i<colors.getColorsCount(); i++){
				values.clear();
				values.put(StoreSQLiteHelper.TabContent.COL_TITLE_ID, id);
				values.put(StoreSQLiteHelper.TabContent.COL_COLOR, colors.getColorBlock(i).getColor());
				height = (float)colors.getColorBlock(i).getHeight()/(float)colors.getHeight();
				values.put(StoreSQLiteHelper.TabContent.COL_SIZE, height);
				db.insert(StoreSQLiteHelper.TabContent.NAME, null, values);

			}
			db.setTransactionSuccessful();
		}
		catch(android.database.sqlite.SQLiteException exc){
			exc.printStackTrace();
//			Log.w("RESAVE", "Exception: "+exc.getMessage());
		}
		finally{
			db.endTransaction();
//			Log.w("RESAVE", "END TRANSACTION");
		}
	}
	
	public void saveNew(long id, String name, BlockBasic[] colors) throws SQLException {
		db.beginTransaction();
		try{
			ContentValues values = new ContentValues();
			values.put(StoreSQLiteHelper.TabTitle.COL_NAME, name);
			values.put(StoreSQLiteHelper.TabTitle.COL_CREATE_DATE, dateFormat.format(new Date()));

			db.delete(StoreSQLiteHelper.TabContent.NAME, StoreSQLiteHelper.TabContent.COL_TITLE_ID+"="+id, null);
			
			for(BlockBasic block : colors){
				values.clear();
				values.put(StoreSQLiteHelper.TabContent.COL_TITLE_ID, id);
				values.put(StoreSQLiteHelper.TabContent.COL_COLOR, block.getColor());
				values.put(StoreSQLiteHelper.TabContent.COL_SIZE, block.getHeight());
				db.insertOrThrow(StoreSQLiteHelper.TabContent.NAME, null, values);
			}
			db.setTransactionSuccessful();
		}
		catch(android.database.sqlite.SQLiteException exc){
			exc.printStackTrace();
		}
		finally{
			db.endTransaction();
		}
	}
	
	public void saveNew(String name, BlockBasic[] colors) throws SQLException {		
		db.beginTransaction();
		try{
			ContentValues values = new ContentValues();
			values.put(StoreSQLiteHelper.TabTitle.COL_NAME, name);
			values.put(StoreSQLiteHelper.TabTitle.COL_CREATE_DATE, dateFormat.format(new Date()));
			long insertId = db.insertOrThrow(StoreSQLiteHelper.TabTitle.NAME, null, values);

			for(BlockBasic block : colors){
				values.clear();
				values.put(StoreSQLiteHelper.TabContent.COL_TITLE_ID, insertId);
				values.put(StoreSQLiteHelper.TabContent.COL_COLOR, block.getColor());
				
				values.put(StoreSQLiteHelper.TabContent.COL_SIZE, block.getHeight());
				db.insertOrThrow(StoreSQLiteHelper.TabContent.NAME, null, values);
			}
			db.setTransactionSuccessful();
		}
		finally{
			db.endTransaction();
		}
	}
	
	public void load(long id, ColorCombinationView container){
		
		Log.w("LOAD", "BEGIN!!!");
		container.removeAllColors();
		
		Cursor cursor = db.query(
				StoreSQLiteHelper.TabContent.NAME, 
				new String[]{StoreSQLiteHelper.TabContent.COL_COLOR, StoreSQLiteHelper.TabContent.COL_SIZE},
				StoreSQLiteHelper.TabContent.COL_TITLE_ID+"="+id,
				null,
				null,
				null,
				StoreSQLiteHelper.TabContent.COL_ID,
				null
		);

		// ��������� �������� ������
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Log.w("COLOR", cursor.getInt(0)+"");
			container.addColor(cursor.getInt(0/*��������� ����� �������*/));
			cursor.moveToNext();
		}

		// ������������� ������ ������ �������� �������
		cursor.moveToFirst();
		for(int i=0; !cursor.isAfterLast(); i++){
			Log.w("HEIGHT", cursor.getFloat(1)+"");
			container.getColorBlock(i).setHeight((int)(container.getHeight()*cursor.getFloat(1)));
			cursor.moveToNext();
		}
		cursor.close();

	}
	
	public void delSave(long id){
		db.delete(StoreSQLiteHelper.TabContent.NAME, StoreSQLiteHelper.TabContent.COL_TITLE_ID+"="+id, null);
		db.delete(StoreSQLiteHelper.TabTitle.NAME, StoreSQLiteHelper.TabTitle.COL_ID+"="+id, null);
	}
	
	public Cursor getSaveList(){
		Cursor cursor = db.query(
			StoreSQLiteHelper.TabTitle.NAME, 
			new String[]{StoreSQLiteHelper.TabTitle.COL_ID, StoreSQLiteHelper.TabTitle.COL_NAME, StoreSQLiteHelper.TabTitle.COL_CREATE_DATE},
			null,
			null,
			null,
			null,
			StoreSQLiteHelper.TabTitle.COL_CREATE_DATE+" DESC",
			null
		);
		return cursor;
	}
	
}
