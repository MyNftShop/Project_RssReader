package com.reader.rss.entry;

import android.provider.BaseColumns;
import android.util.EventLogTags.Description;

/**
 * 
 * 对象描述：一个频道
 * 成员：
 * 1. String title				标题
 * 2. String imageUrl		图片链接
 * 3. String_description	描述
 * @author sangxiaokai
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133 Copyright © 2013-2113 Sang Puyuan. All rights
 *        reserved. You can not use it for any commercial purposes
 * 
 */
public class RSSChannal {
	private static final String TAG = "RSSChannel";
	private String _title = null;
	private String _link = null;
	private String _imageUrl = null;
	private String _description = null;
	
	public static class RSSChannalColumns implements BaseColumns {
		private RSSChannalColumns() {
		}
		public static final String KEY_TITLE = "_title";
		public static final String KEY_LINK = "_link";
		public static final String KEY_IMAGE_URL = "_imageUrl";
		public static final String KEY_DESCRIPTION = "_description";
	}

	
	public RSSChannal() {
		// TODO Auto-generated constructor stub
	}
	public void setLink(String string) {
		this._link = string;
	}
	public void setTitle(String string) {
		this._title = string;
	}
	public void setImageUrl(String string) {
		this._imageUrl = string;
	}
	public void setDescription(String string) {
		this._description = string;
	}
	public String getTitle() {
		return this._title;
	}
	public String getLink() {
		return this._link;
	}
	public String getImageUrl() {
		return this._imageUrl;
	}
	public String getDescription() {
		return this._description;
	}


	
	
}
