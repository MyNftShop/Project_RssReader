package com.reader.rss.lib;

import java.nio.channels.Channels;

import android.app.ActionBar.Tab;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.text.TextUtils;
import android.util.Log;

import com.reader.rss.config.NamingSpace;
import com.reader.rss.entry.RSSChannal;
import com.reader.rss.entry.RSSChannals;
import com.reader.rss.entry.RSSItem;
import com.reader.rss.entry.RSSItem.RSSItemColumns;
import com.reader.rss.entry.RSSItems;

public class MyRSSItemsDbHelper extends BaseContentDbHelper {

	/**
	 * 获得信息列表，根据sort order
	 * 
	 * @param sortOrder
	 * @return {@link RSSChannals}
	 */
	public RSSItems getRSSItems(String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(NamingSpace.ITEMS_TABLENAME);
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = RSSItemColumns.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = qb.query(db, null, null, null, null, null, orderBy,
				null);

		RSSItems items = new RSSItems();
		RSSItem item = null;
		while (cursor.moveToNext()) {
			// add
			item = new RSSItem();

			item.setTitle(cursor.getString(cursor
					.getColumnIndex(RSSItemColumns.KEY_TITLE)));
			item.setLink(cursor.getString(cursor
					.getColumnIndex(RSSItemColumns.KEY_LINK)));
			item.setId(cursor.getLong(cursor.getColumnIndex(RSSItemColumns._ID)));

			items.addItem(item);
		}

		return items;

	}

	public MyRSSItemsDbHelper(Context context) {
		mOpenHelper = new DatabaseHelper(context);
	}

	/**
	 * 添加rss item
	 * @param channalId 
	 * 
	 * @param values
	 */
	public long insert(RSSItem item, long channalId) {
		if (item == null) {
			return 0;
		}
		ContentValues values=new ContentValues();
		values.put(RSSItemColumns.KEY_CHANNAL_ID, channalId);
		values.put(RSSItemColumns.KEY_CREATEDATE, MyDate.getNowByFormat(NamingSpace.DATETIME_FORMAT));//time of now
		values.put(RSSItemColumns.KEY_DESCRIPTION, item.getDescription());
		values.put(RSSItemColumns.KEY_LINK, item.getLink());
		values.put(RSSItemColumns.KEY_PUBDATE, item.getPubdate());
		values.put(RSSItemColumns.KEY_TITLE, item.getTitle());
		// insert to database
		SQLiteDatabase database = mOpenHelper.getWritableDatabase();

		return database.insert(NamingSpace.ITEMS_TABLENAME,
				RSSItemColumns.KEY_LINK, values);

	}

	/**
	 * 
	 * @param channalId
	 * @param sortOrder
	 * @return null or items
	 */

	public RSSItems getRSSItemsByChanId(long channalId, String sortOrder) {
		RSSItems items = new RSSItems();
		try {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(NamingSpace.ITEMS_TABLENAME);
			String orderBy;
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = RSSItemColumns.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}

			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			Cursor cursor = qb.query(db, null, RSSItemColumns.KEY_CHANNAL_ID
					+ "= ? ", new String[] { channalId + "" }, null, null,
					orderBy, null);

			RSSItem item = null;
			while (cursor.moveToNext()) {
				item = new RSSItem();
				// 添加字段
				item.setTitle(cursor.getString(cursor
						.getColumnIndex(RSSItemColumns.KEY_TITLE)));
				item.setLink(cursor.getString(cursor
						.getColumnIndex(RSSItemColumns.KEY_LINK)));
				item.setDescription(cursor.getString(cursor
						.getColumnIndex(RSSItemColumns.KEY_DESCRIPTION)));
				item.setPubdate(cursor.getString(cursor
						.getColumnIndex(RSSItemColumns.KEY_PUBDATE)));
				item.setReadFlag(cursor.getInt(cursor
						.getColumnIndex(RSSItemColumns.KEY_READED)));
				item.setCreateDate(cursor.getString(cursor
						.getColumnIndex(RSSItemColumns.KEY_CREATEDATE)));
				item.setId(cursor.getLong(cursor
						.getColumnIndex(RSSItemColumns._ID)));

				items.addItem(item);
			}
		} catch (Exception e) {
			Log.i("tag", "查询出错，没有返回值");
		}

		return items;
	}

	/**
	 * 
	 * @param rssItems
	 * @param channalId 
	 */
	public long[] insert(RSSItems rssItems, long channalId) {
		RSSItem item=null;
		long[] ids=new long[ rssItems.getCount()];
		for (int i = 0; i < rssItems.getCount(); i++) {
			item=rssItems.getItem(i);
			ids[i]=insert(item,channalId);
		}
		return ids;
	}

}
