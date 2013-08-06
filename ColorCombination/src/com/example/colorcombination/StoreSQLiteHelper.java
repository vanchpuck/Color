package com.example.colorcombination;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreSQLiteHelper extends SQLiteOpenHelper{

	public final static String TAB_TITLE = "title";
	public final static String COL_TITLE_ID = "_id";
	public final static String COL_TITLE_NAME = "name";
	public final static String COL_TITLE_CREATE_DATE = "create_date";	
	
	
	public final static String TAB_CONTENT = "content";
	public final static String COL_CONTENT_ID = "_id";
	public final static String COL_CONTENT_TITLE_ID = "title_id";
	public final static String COL_CONTENT_COLOR = "color";
	public final static String COL_CONTENT_SIZE = "part";
	
	private final static String STMT_CREATE_TITLE = 
			"create table "+TAB_TITLE+" ("+
				COL_TITLE_ID+" integer primary key autoincrement, "+
				COL_TITLE_NAME+" text not null, "+
				COL_TITLE_CREATE_DATE+"date not null "+
			");"
	;
	private final static String STMT_CREATE_CONTENT = 
			"create table "+TAB_CONTENT+" ("+
				COL_CONTENT_ID+" integer primary key autoincrement, "+
				COL_CONTENT_TITLE_ID+" integer not null, "+
				COL_TITLE_CREATE_DATE+"date not null "+
			");"
	;
	
	public StoreSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub				
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
