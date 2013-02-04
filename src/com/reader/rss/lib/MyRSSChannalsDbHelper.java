package com.reader.rss.lib;

import java.nio.channels.Channels;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

import com.reader.rss.config.NamingSpace;
import com.reader.rss.entry.RSSChannal;
import com.reader.rss.entry.RSSChannal.RSSChannalColumns;
import com.reader.rss.entry.RSSChannals;

public class MyRSSChannalsDbHelper extends BaseContentDbHelper {

	/**
	 * 获得频道列表，根据sort order
	 * 
	 * @param sortOrder
	 * @return {@link RSSChannals}
	 */
	public RSSChannals getRSSChannals(String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(NamingSpace.CHANNALS_TABLENAME);
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = RSSChannalColumns.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = qb.query(db, null, null, null,
				null, null, orderBy, null);

		RSSChannals channals = new RSSChannals();
		RSSChannal channal = null;
		while (cursor.moveToNext()) {
			// add
			channal = new RSSChannal();
			channal.setTitle(cursor.getString(cursor.getColumnIndex(RSSChannalColumns.KEY_TITLE)));
			channal.setLink(cursor.getString(cursor.getColumnIndex(RSSChannalColumns.KEY_LINK)));
			channal.setId(cursor.getLong(cursor.getColumnIndex(RSSChannalColumns._ID)));			
			channals.addItem(channal);
		}
		
		return channals;

	}

	public MyRSSChannalsDbHelper(Context context) {
		mOpenHelper = new DatabaseHelper(context);
	}

	/**
	 * 添加rss item
	 * 
	 * @param values
	 */
	public long insert(ContentValues values) {
		if (values == null) {
			return 0;
		}
		if (values.containsKey(RSSChannalColumns.KEY_TITLE) == false) {
			Resources resources = Resources.getSystem();
			values.put(RSSChannalColumns.KEY_TITLE,
					resources.getString(android.R.string.untitled));
		}
		// insert to database
		SQLiteDatabase database = mOpenHelper.getWritableDatabase();

		return database.insert(NamingSpace.CHANNALS_TABLENAME,
				RSSChannalColumns.KEY_LINK, values);

	}

}
