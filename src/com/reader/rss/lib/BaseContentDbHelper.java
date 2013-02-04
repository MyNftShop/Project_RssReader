package com.reader.rss.lib;

import com.reader.rss.config.NamingSpace;
import com.reader.rss.entry.RSSChannal.RSSChannalColumns;
import com.reader.rss.entry.RSSItem.RSSItemColumns;
import com.reader.rss.entry.RSSItems;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseContentDbHelper {

	/**
		 * 描述或功能：内部类负责操作数据库
		 */
		protected static class DatabaseHelper extends SQLiteOpenHelper {
	
			private static final String TAG = "dbHelp";
	
			DatabaseHelper(Context context) {
				super(context, NamingSpace.DB_DABASENAME, null, NamingSpace.DATABASE_VERSION);
				Log.i(TAG, "DATABASE_VERSION=" + NamingSpace.DATABASE_VERSION);
			}
	
			@Override
			public void onCreate(SQLiteDatabase db) {
				Log.i(TAG, "onCreate");
				String sql = "CREATE TABLE IF NOT EXISTS " + NamingSpace.CHANNALS_TABLENAME+ " ("
						+ RSSChannalColumns._ID + " INTEGER PRIMARY KEY,"
						+ RSSChannalColumns.KEY_TITLE + " TEXT," + RSSChannalColumns.KEY_LINK+" TEXT," + RSSChannalColumns.KEY_ORDERBY+ " INTEGER default 0" + ");";
				String sql2="CREATE TABLE IF NOT EXISTS " + NamingSpace.ITEMS_TABLENAME+ " ("
						+ RSSItemColumns._ID + " INTEGER PRIMARY KEY,"+RSSItemColumns.KEY_CHANNAL_ID+" INTEGER,"
						+ RSSItemColumns.KEY_TITLE + " TEXT," + RSSItemColumns.KEY_LINK+" TEXT,"	
						+ RSSItemColumns.KEY_DESCRIPTION + " TEXT," + RSSItemColumns.KEY_PUBDATE+" TEXT,"
						+	RSSItemColumns.KEY_CREATEDATE+" TEXT,"+RSSItemColumns.KEY_READED+" INTEGER(1) DEFAULT 0"
						+ ");";
	
				Log.i(TAG, "sql=" + sql);
				Log.i(TAG, "sql=" + sql2);
				db.execSQL(sql);
				db.execSQL(sql2);				
			}
	
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				//版本升级在这里，可以防止升级的时候数据丢失
	//			Log.i(TAG,
	//					" onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)="
	//							+ newVersion);
	//			switch (oldVersion) {
	//			case 1:
	//				break;
	////			case 2:
	////				break;
	////			case 3:
	////				break;
	//			}
	////			db.execSQL("DROP TABLE IF EXISTS Note");
	//			onCreate(db);
			}
		}

		/**
		 * 数据库操作对象
		 */
	protected DatabaseHelper mOpenHelper;

}
