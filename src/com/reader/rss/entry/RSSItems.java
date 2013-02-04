package com.reader.rss.entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.reader.rss.entry.RSSItem.RSSItemColumns;

import android.nfc.Tag;
import android.util.Log;


/**
 * 功能描述：RSSItem 的集合
 * 
 * @author sangxiaokai
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 */
public class RSSItems {
	private static final String TAG = "RSSFeed";
	private String _title = null;
	private String _pubdate = null;
	private int itemCount = 0;
	private List<RSSItem> itemList;                                                                                                                                                                                                                                                                                                                                                                   

	public RSSItems() {
		// TODO Auto-generated constructor stub
		itemList = new Vector<RSSItem>(0);
	}

	public int addItem(RSSItem item) {
		itemList.add(item);
		itemCount++;
		return itemCount;
	}

	public RSSItem getItem(int location) {
		return itemList.get(location);
	}

	/**
	 * 得到所有的items,为列表生成所需要的数据
	 * @return
	 */
	public List getAllItemsForListView() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		int size = itemList.size();
		for (int i = 0; i < size; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put(RSSItemColumns.KEY_TITLE, itemList.get(i).getTitle());
			item.put(RSSItemColumns.KEY_LINK, itemList.get(i).getLink());
			item.put(RSSItemColumns.KEY_DESCRIPTION, itemList.get(i).getDescription());
			item.put(RSSItemColumns.KEY_PUBDATE, itemList.get(i).getPubdate());
			Log.i(TAG, "item: "+item.get(RSSItemColumns.KEY_TITLE));
			data.add(item);
		}
		return data;
	}

	public int getCount() {
		return itemCount;
	}

	public void setTitle(String title) {
		this._title = title;
	}

	public void setPubDate(String pubdate) {
		this._pubdate = pubdate;
	}

	public String getTitle() {
		return this._title;
	}

	public String getPubDate() {
		return this._pubdate;
	}

}
