package com.example.colorcombination;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreSQLiteHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "colors.db";
	
	public static class tabTitle{
		public final static String NAME = "title";
		public final static String COL_ID = "_id";
		public final static String COL_NAME = "name";
		public final static String COL_CREATE_DATE = "create_date";
		
		private final static String STMT_CREATE = 
			"create table "+NAME+" ("+
				COL_ID+" integer primary key autoincrement, "+
				COL_NAME+" text not null, "+
				COL_CREATE_DATE+"date not null "+
			");"
		;		
	}
	
	public static class tabContent{
		public final static String NAME = "content";
		public final static String COL_ID = "_id";
		public final static String COL_TITLE_ID = "title_id";
		public final static String COL_COLOR = "color";
		public final static String COL_SIZE = "part";
		
		private final static String STMT_CREATE = 
			"create table "+NAME+" ("+
				COL_ID+" integer primary key autoincrement, "+
				COL_TITLE_ID+" integer not null, "+
				COL_COLOR+" integer not null, "+
				COL_SIZE+" integer not null, "+
			");"
		;
	}
	

	public StoreSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(tabTitle.STMT_CREATE);
		db.execSQL(tabContent.STMT_CREATE);			
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
		/*œŒ ¿ Õ»◊≈√Œ Õ≈ ƒ≈À¿≈Ã*/
	}

}