package com.example.colorcombination;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
	
		ContentValues values = new ContentValues();
		values.put(StoreSQLiteHelper.tabTitle.COL_NAME, name);
		values.putNull(StoreSQLiteHelper.tabTitle.COL_CREATE_DATE);
		long insertId = db.insert(StoreSQLiteHelper.tabTitle.NAME, null, values);
		
		for(int i=0; i<colors.getColorsCount(); i++){
			values.clear();
			values.put(StoreSQLiteHelper.tabContent.COL_TITLE_ID, insertId);
			values.put(StoreSQLiteHelper.tabContent.COL_COLOR, colors.getColorBlock(i).getColor());
			values.put(StoreSQLiteHelper.tabContent.COL_SIZE, colors.getColorBlock(i).getHeight()/colors.getHeight());
			db.insert(StoreSQLiteHelper.tabContent.NAME, null, values);
		}
	}
	
	public void save(long id, String name, ColorCombinationView colors){
		
		/* ƒŒ¡¿¬»“‹ “–¿Õ«¿ ÷»ﬁ */
	
		ContentValues values = new ContentValues();
		values.put(StoreSQLiteHelper.tabTitle.COL_NAME, name);
		values.putNull(StoreSQLiteHelper.tabTitle.COL_CREATE_DATE);
		db.update(StoreSQLiteHelper.tabTitle.NAME, values, StoreSQLiteHelper.tabTitle.COL_ID+"="+id, null);
		
		db.delete(StoreSQLiteHelper.tabContent.NAME, StoreSQLiteHelper.tabContent.COL_TITLE_ID+"="+id, null);
		
		for(int i=0; i<colors.getColorsCount(); i++){
			values.clear();
			values.put(StoreSQLiteHelper.tabContent.COL_TITLE_ID, id);
			values.put(StoreSQLiteHelper.tabContent.COL_COLOR, colors.getColorBlock(i).getColor());
			values.put(StoreSQLiteHelper.tabContent.COL_SIZE, colors.getColorBlock(i).getHeight()/colors.getHeight());
			db.insert(StoreSQLiteHelper.tabContent.NAME, null, values);
		}
	}
	
	public void load(long id, ColorCombinationView container){
		
		container.removeAllColors();
		
		Cursor cursor = db.query(
				StoreSQLiteHelper.tabContent.NAME, 
				new String[]{StoreSQLiteHelper.tabContent.COL_COLOR, StoreSQLiteHelper.tabContent.COL_SIZE},
				StoreSQLiteHelper.tabContent.COL_TITLE_ID+"="+id,
				null,
				null,
				null,
				StoreSQLiteHelper.tabContent.COL_ID,
				null
		);
		// ƒÓ·‡‚ÎˇÂÏ ˆ‚ÂÚÓ‚˚Â Ô‡ÌÂÎË
		while(!cursor.isAfterLast()){
			container.addColor(cursor.getInt(0/*œŒƒŒ¡–¿“‹ ÕŒÃ≈– —“ŒÀ¡÷¿*/));
			cursor.moveToNext();
		}
		// ”ÒÚ‡Ì‡‚ÎË‚‡ÂÏ ÌÛÊÌ˚È ‡ÁÏÂ ˆ‚ÂÚÓ‚˚ı Ô‡ÌÂÎÂÈ
		cursor.moveToFirst();
		for(int i=0; !cursor.isAfterLast(); i++){
			container.getColorBlock(i).setHeight(container.getHeight()*cursor.getInt(1));
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	public void delSave(long id){
		db.delete(StoreSQLiteHelper.tabContent.NAME, StoreSQLiteHelper.tabContent.COL_TITLE_ID+"="+id, null);
		db.delete(StoreSQLiteHelper.tabTitle.NAME, StoreSQLiteHelper.tabTitle.COL_ID+"="+id, null);
	}
	
	public Cursor getSaveList(){
		Cursor cursor = db.query(
				StoreSQLiteHelper.tabTitle.NAME, 
				new String[]{StoreSQLiteHelper.tabTitle.COL_NAME, StoreSQLiteHelper.tabTitle.COL_CREATE_DATE},
				null,
				null,
				null,
				null,
				StoreSQLiteHelper.tabTitle.COL_CREATE_DATE+" DESC",
				null
		);
		return cursor;
	}
	
}
