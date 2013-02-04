package com.reader.rss.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.reader.rss.entry.RSSChannal;

public class ChannelHandler extends DefaultHandler {
	private static final String TAG = "ChannelHandler";

	private int currentstate = 0;
	private static final int TAG_TITLE = 1;
	private static final int TAG_LINK = 2;
	private static final int TAG_DESC = 3;

	private RSSChannal rssChannal;

	private boolean stopFlag = false;

	private String PreTag = "";

	public RSSChannal getChannal() {
		Log.i("tag", rssChannal.toString());
		return rssChannal;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (stopFlag) {
			return;
		}
		String string = new String(ch, start, length);
		if (currentstate == TAG_DESC) {
			Log.i(TAG, "set des:" + string);
			rssChannal.setDescription(string);
			return;
		}
		if (currentstate == TAG_TITLE) {
			Log.i(TAG, "set title:" + string);
			rssChannal.setTitle(string);
			return;
		}
	}

	@Override
	public void startDocument() throws SAXException {
		rssChannal = new RSSChannal();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (stopFlag) {
			return;
		}
		PreTag = "";

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (stopFlag) {
			return;
		}
		Log.i(TAG, attributes.getLength()+":" + qName);
		localName = localName.toLowerCase();
		PreTag = localName;
		if (localName.equals("title")) {
			currentstate = TAG_TITLE;
			return;
		}
		if (localName.equals("description")) {
			currentstate = TAG_DESC;
			return;
		}
		if (localName.equals("item")) {
			stopFlag = true;
			return;
		}
		currentstate = 0;

	}

}
