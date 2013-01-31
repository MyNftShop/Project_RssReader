package com.reader.rss.entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.reader.rss.entry.RSSChannal.RSSChannalColumns;

import android.nfc.Tag;
import android.util.Log;


/**
 * 功能描述：RSSChannal 的集合
 * 
 * @author sangxiaokai
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 */
public class RSSChannals {
	private static final String TAG = "RSSChannals";
	private int itemCount = 0;
	private List<RSSChannal> itemList;                                                                                                                                                                                                                                                                                                                                                                   

	public RSSChannals() {
		// TODO Auto-generated constructor stub
		itemList = new Vector<RSSChannal>(0);
	}

	public int addItem(RSSChannal item) {
		itemList.add(item);
		itemCount++;
		return itemCount;
	}

	public RSSChannal getItem(int location) {
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
			item.put(RSSChannalColumns.KEY_TITLE, itemList.get(i).getTitle());
			item.put(RSSChannalColumns.KEY_LINK, itemList.get(i).getLink());
			item.put(RSSChannalColumns.KEY_DESCRIPTION, itemList.get(i).getDescription());
			item.put(RSSChannalColumns.KEY_IMAGE_URL, itemList.get(i).getImageUrl());
			Log.i(TAG, "item: "+item.get(RSSChannalColumns.KEY_TITLE));
			data.add(item);
		}
		return data;
	}

	public int getItemCount() {
		return itemCount;
	}





}
