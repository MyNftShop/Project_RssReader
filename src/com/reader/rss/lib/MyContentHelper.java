package com.reader.rss.lib;

import android.content.Context;

import com.reader.rss.entry.RSSChannal;
import com.reader.rss.entry.RSSChannals;

public class MyContentHelper extends BaseContentHelper{

	
	/**
	 * 获得频道列表，根据sort order
	 * @param sortOrder
	 * @return {@link RSSChannals}
	 */
	public RSSChannals getRSSChannals(String sortOrder) {
		
		RSSChannals channals=new RSSChannals();
		
		RSSChannal channal = null;
		//add
		channal=new RSSChannal();
		channal.setTitle("baidu");
		channal.setLink("http://news.baidu.com/n?cmd=1&class=civilnews&tn=rss");
		channals.addItem(channal);
		//add
		channal=new RSSChannal();
		channal.setTitle("163");
		channal.setLink("http://news.163.com/special/00011K6L/rss_newstop.xml");
		channals.addItem(channal);
		//add
		channal=new RSSChannal();
		channal.setTitle("南方航空");
		channal.setLink("http://www.southcn.com/rss/default.xml");
		channals.addItem(channal);
		//add
		channal=new RSSChannal();
		channal.setTitle("QQ");
		channal.setLink("http://news.qq.com/newsgn/rss_newsgn.xml");
		channals.addItem(channal);
		
		channal=null;
		
		return channals;
		
	}
	public MyContentHelper(Context context) {
		mOpenHelper=new DatabaseHelper(context);
	}
	
	

	
}
