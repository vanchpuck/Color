package com.example.colorcombination;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SaveStore {
	
	private SQLiteDatabase db;
	private StoreSQLiteHelper dbHelper;
	
	public SaveStore(Context context){
		dbHelper = new StoreSQLiteHelper(context);
	}
	
	public void openToRead() throws SQLException {
		db = dbHelper.getReadableDatabase();
	}
	
	public void openToWrite() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	/*
	*	Œ–√¿Õ»«Œ¬¿“‹ ƒŒ¡¿¬À≈Õ»≈ ƒ¿“€
	*/
	public void save(String name, ColorCombinationView colors){
		
		/* ƒŒ¡¿¬»“‹ “–¿Õ«¿ ÷»ﬁ */
	
		db.beginTransaction();
		try{
			ContentValues values = new ContentValues();
			values.put(StoreSQLiteHelper.TabTitle.COL_NAME, name);
			values.put(StoreSQLiteHelper.TabTitle.COL_CREATE_DATE, "date('now')");
			long insertId = db.insertOrThrow(StoreSQLiteHelper.TabTitle.NAME, null, values);
			Log.w("INSERT", "ID: :"+insertId+"");
			
			float height = 0;
			for(int i=0; i<colors.getColorsCount(); i++){
				values.clear();
				values.put(StoreSQLiteHelper.TabContent.COL_TITLE_ID, insertId);
				values.put(StoreSQLiteHelper.TabContent.COL_COLOR, colors.getColorBlock(i).getColor());
				
				height = (float)colors.getColorBlock(i).getHeight()/(float)colors.getHeight();
				values.put(StoreSQLiteHelper.TabContent.COL_SIZE, height);
				long id = db.insertOrThrow(StoreSQLiteHelper.TabContent.NAME, null, values);
				Log.w("INSERT", id+"");
			}
			db.setTransactionSuccessful();
			Log.w("TRANSACTION", "SUCCEES");
		}
		catch(android.database.sqlite.SQLiteException exc){
			Log.w("TRANSACTION", "Exception");
		}
		finally{
			db.endTransaction();
			Log.w("TRANSACTION", "END TRANSACTION");
		}
	}
	
	public void save(long id, String name, ColorCombinationView colors){
		
		/* ƒŒ¡¿¬»“‹ “–¿Õ«¿ ÷»ﬁ */
			
		db.beginTransaction();
		try{
			ContentValues values = new ContentValues();
			values.put(StoreSQLiteHelper.TabTitle.COL_NAME, name);
			values.put(StoreSQLiteHelper.TabTitle.COL_CREATE_DATE, "date('now')");
			db.update(StoreSQLiteHelper.TabTitle.NAME, values, StoreSQLiteHelper.TabTitle.COL_ID+"="+id, null);
			
			db.delete(StoreSQLiteHelper.TabContent.NAME, StoreSQLiteHelper.TabContent.COL_TITLE_ID+"="+id, null);
			
			float height = 0;
			for(int i=0; i<colors.getColorsCount(); i++){
				values.clear();
				values.put(StoreSQLiteHelper.TabContent.COL_TITLE_ID, id);
				values.put(StoreSQLiteHelper.TabContent.COL_COLOR, colors.getColorBlock(i).getColor());
				height = (float)colors.getColorBlock(i).getHeight()/(float)colors.getHeight();
				values.put(StoreSQLiteHelper.TabContent.COL_SIZE, colors.getColorBlock(i).getHeight()/colors.getHeight());
				db.insert(StoreSQLiteHelper.TabContent.NAME, null, values);
			}
			db.setTransactionSuccessful();
		}
		catch(android.database.sqlite.SQLiteException exc){
			Log.w("TRANSACTION", "Exception");
		}
		finally{
			db.endTransaction();
			Log.w("TRANSACTION", "END TRANSACTION");
		}
	}
	
	public void load(long id, ColorCombinationView container){
		
		Log.w("LOAD", "BEGIN");
		container.removeAllColors();
		Log.w("LOAD", "1");
		
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
		
		Log.w("LOAD", "2");
		// ƒÓ·‡‚ÎˇÂÏ ˆ‚ÂÚÓ‚˚Â Ô‡ÌÂÎË
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Log.w("COLOR", cursor.getInt(0)+"");
			container.addColor(cursor.getInt(0/*œŒƒŒ¡–¿“‹ ÕŒÃ≈– —“ŒÀ¡÷¿*/));
			cursor.moveToNext();
		}
		Log.w("LOAD", "3");
		// ”ÒÚ‡Ì‡‚ÎË‚‡ÂÏ ÌÛÊÌ˚È ‡ÁÏÂ ˆ‚ÂÚÓ‚˚ı Ô‡ÌÂÎÂÈ
		cursor.moveToFirst();
		for(int i=0; !cursor.isAfterLast(); i++){
			Log.w("HEIGHT", cursor.getFloat(1)+"");
			container.getColorBlock(i).setHeight((int)(container.getHeight()*cursor.getFloat(1)));
			cursor.moveToNext();
		}
		cursor.close();
		
		
		Log.w("LOAD", "4");
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
