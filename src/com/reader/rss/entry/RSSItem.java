package com.reader.rss.entry;

import android.R.bool;
import android.provider.BaseColumns;


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

	public  String _title = null;
	public  String _pubdate = null;
	public  String _link = null;
	public  String _description = null;
	private long _id=0;
	private boolean _readed;
	private String _createDate;
	
	public static class RSSItemColumns implements BaseColumns {
		private RSSItemColumns() {
		}
		public static final String KEY_TITLE = "title";
		public static final String KEY_LINK = "link";
		public static final String KEY_DESCRIPTION = "description";
		public static final String KEY_PUBDATE = "pubdate";
		public static final String KEY_CHANNAL_ID = "fid";
		public static final String KEY_CREATEDATE = "createdate";
		public static final String KEY_READED = "readflag";
		public static final String DEFAULT_SORT_ORDER = " createdate desc,pubdate desc";
	}


	public RSSItem() {
	}

	public void setTitle(String string) {
		this._title = string;
	}

	public void setPubdate(String string) {
		this._pubdate = string;
	}

	public void setCreateDate(String string) {
		this._createDate = string;
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
	public long getId() {
		return this._id ;
	}

	public String getCreateDate() {
		return this._createDate ;
	}

	public String getLink() {
		return this._link ;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return this._description;
	}
	
//	public String toString() {
//		if (this._title.length()>20) {
//			return this._title.subSequence(0, 42)+"...";
//		}
//		return this._link;
//	}

	public void setId(long id) {
		// TODO Auto-generated method stub
		this._id=id;
	}

	public void setReadFlag(int i ) {
		this._readed=(i>0?true:false);
	}	
	public boolean getReadFlag( ) {
		// TODO Auto-generated method stub
		return this._readed;
	}

}
