package com.reader.rss.parser;

import java.lang.reflect.Constructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.reader.rss.entry.RSSItem;
import com.reader.rss.entry.RSSItems;

import android.R.fraction;
import android.util.Log;


/**
 * 
 * 功能描述：处理解析后的数据 RSS ContentHandler
 * 
 * @author sangxiaokai
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 */
public class RSSItemsHandler extends DefaultHandler {

//	private String trim(String str) {
//		String dest = "";
//		if (str != null) {
//			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//			Matcher m = p.matcher(str);
//			dest = m.replaceAll("");
//		}
//		return dest;
//	}

	private static final String TAG = "RSSHandler";

	private final int RSS_TITLE = 1;
	private final int RSS_DESC = 2;
	private final int RSS_LINK = 3;
	private final int RSS_CATEGORY = 4;
	private final int RSS_PUBDATE = 5;

	private RSSItems rssItems;
	private RSSItem rssItem;

	private int currentstate;

	private String descString = "";

	private String tmpString=null;

	/**
	 * 获得存储了数据的FEED对象
	 * 
	 * @return
	 */
	public RSSItems getRSSItems() {
		// TODO Auto-generated method stub
		return rssItems;
	}

	/**
	 * {@link Constructor}
	 */
	public RSSItemsHandler() {
	}

	public void startDocument() throws SAXException {
		rssItems = new RSSItems();
		rssItem = new RSSItem();
	}

	public void endDocument() throws SAXException {
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		Log.e(TAG, "Localname S: " + localName );
		localName = localName.toLowerCase();
		if (localName.equals("channel")) {
			currentstate = 0;
			return;
		}
		if (localName.equals("item")) {
			rssItem = new RSSItem();
			return;
		}
		if (localName.equals("title")) {
			currentstate = RSS_TITLE;
			return;
		}
		if (localName.equals("description")) {
			currentstate = RSS_DESC;
			tmpString=new String();
			return;
		}
		if (localName.equals("link")) {
			currentstate = RSS_LINK;
			return;
		}
		if (localName.equals("category")) {
			currentstate = RSS_CATEGORY;
			return;
		}
		if (localName.equals("pubdate")) {
			currentstate = RSS_PUBDATE;
			return;
		}

		currentstate = 0;
	}

	public void endElement(String uri, String localName, String qName) {
		Log.e(TAG, "Localname E: " + localName );
		localName = localName.toLowerCase();
		if (localName.equals("description")) {
			rssItem.setDescription(tmpString);
			tmpString=null;
			currentstate=0;
		}
		if (localName.equals("item")) {
			rssItems.addItem(rssItem);
			return;
		}

	}

	public void characters(char[] ch, int start, int length) {
		String string = new String(ch, start, length);
		Log.i(TAG, "characters=" + string);

		switch (currentstate) {
		case RSS_TITLE:
			rssItem.setTitle(string);
			Log.i(TAG, "set title=" + string);
			currentstate = 0;
			break;
		case RSS_DESC:
//			//内容
//			// 去除空格
//			string=trim(string);
//			if (!string.equals("\n") && !string.equals("")) {
//				Log.i(TAG, "Description characters=" + string);
//				descString += string;
//			}
			tmpString+=string;
//			 currentstate=0;//set 0 on endElement
			break;
		case RSS_LINK:
			rssItem.setLink(string);
			Log.i(TAG, "setLink=" + string);
			currentstate = 0;
			break;
		case RSS_PUBDATE:
			//时间
			rssItem.setPubdate(string);
			Log.i(TAG, "setPubdate=" + string);
			currentstate = 0;
			break;
		default:
			break;
		}
	}
}
