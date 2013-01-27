package com.reader.rss.entry;


/**
 * 
 * 功能描述：一条新闻
 * @author sangxiaokai 
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 * Copyright © 2013-2113 Sang Puyuan. All rights reserved. 
 * You can not use it for any commercial purposes
 *
 */
public class RSSItem {

	public static final String KEY_TITLE = "title";
	public static final String KEY_LINK = "link";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PUBDATE = "pubDate";
	public  String _title = null;
	public  String _pubdate = null;
	public  String _link = null;
	public  String _category = null;
	public  String _description = null;

	public RSSItem() {
	}

	public void setTitle(String string) {
		this._title = string;
	}

	public void setPubdate(String string) {
		this._pubdate = string;
	}

	public void setCategory(String string) {
		this._category = string;
	}

	public void setLink(String string) {
		this._link = string;
	}

	public void setDescription(String string) {
		this._description = string;
	}

	public String getTitle() {
		return this._title ;
	}

	public String getPubdate() {
		return this._pubdate ;
	}

	public String getCategory() {
		return this._category ;
	}

	public String getLink() {
		return this._link ;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return this._description;
	}
	
	public String toString() {
		if (this._title.length()>20) {
			return this._title.subSequence(0, 42)+"...";
		}
		return _category;
	}
}
