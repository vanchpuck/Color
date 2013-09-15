package com.example.colorcombination;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreSQLiteHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "colors.db";
	
	public static class TabTitle{
		public final static String NAME = "title";
		public final static String COL_ID = "_id";
		public final static String COL_NAME = "name";
		public final static String COL_CREATE_DATE = "create_date";
		
		private final static String STMT_CREATE = 
			"create table "+NAME+" ( "+
				COL_ID+" integer primary key autoincrement, "+
				COL_NAME+" text not null, "+
				COL_CREATE_DATE+" date not null "+
			");"
		;		
	}
	
	public static class TabContent{
		public final static String NAME = "content";
		public final static String COL_ID = "_id";
		public final static String COL_TITLE_ID = "title_id";
		public final static String COL_COLOR = "color";
		public final static String COL_SIZE = "part";
		
		private final static String STMT_CREATE = 
			"create table "+NAME+" ( "+
				COL_ID+" integer primary key autoincrement, "+
				COL_TITLE_ID+" integer not null references "+TabTitle.NAME+"("+TabTitle.COL_ID+"), "+
				COL_COLOR+" integer not null, "+
				COL_SIZE+" real not null "+
				//"FOREIGN KEY("+COL_TITLE_ID+") REFERENCES "+tabTitle.NAME+"("+tabTitle.COL_ID+") "+
			");"
		;
	}
	

	public StoreSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TabTitle.STMT_CREATE);
		db.execSQL(TabContent.STMT_CREATE);			
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("PRAGMA foreign_keys=ON;");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
		/*œŒ ¿ Õ»◊≈√Œ Õ≈ ƒ≈À¿≈Ã*/
	}

}