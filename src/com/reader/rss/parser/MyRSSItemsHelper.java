package com.reader.rss.parser;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

import com.reader.rss.entry.RSSItems;
import com.reader.rss.lib.CustomHttpClient;

public class MyRSSItemsHelper {

	private static final String TAG = "MyRSSItemsHelper";

	/**
	 * 获得信息列表，从一个URL指向的xml网络文件中
	 * 
	 * @param string
	 * @return 失败返回null
	 */
	public RSSItems getRSSItemsFromUrl(String url) {
		try {
			// 获得xml文件的输入对象
			InputSource inputSource = getInputSourceFromUrlString(url);
			if (inputSource != null) {
				Log.i(TAG, "获得输入流成功！");
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader xmlReader = parser.getXMLReader();

				 RSSItemsHandler rssHandler = new RSSItemsHandler();
				xmlReader.setContentHandler(rssHandler);
				xmlReader.parse(inputSource);
				return rssHandler.getRSSItems();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	private InputSource getInputSourceFromUrlString(String urlString) throws IOException {
		Log.i(TAG, "getRssFeedInputSource url= " + urlString);
		InputSource inputSource = null;
		// 1：获得XML编码方式
		URL url;
		url = new URL(urlString);
		// 得到探测器代理对象
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		// 向代理对象添加探测器
		detector.add(JChardetFacade.getInstance());
		// 得到编码字符集对象
		Charset charset = detector.detectCodepage(url);
		// 得到编码名称
		String encodingName = charset.name();
		// 2：通过InputStreamReader设定好编码
		// ，然后将然后将InputStreamReader
		// 获得inputStream
		HttpClient httpClient=CustomHttpClient.getHttpClient();
		HttpGet request=new HttpGet(urlString);
		HttpResponse response;
		response=httpClient.execute(request);
		InputStream inputStream =response.getEntity().getContent();
		// 通过InputStreamReader设定编码方式
		InputStreamReader streamReader = new InputStreamReader(inputStream,
				encodingName);
		inputSource = new InputSource(streamReader);
		// 3：返回input source,出错返回null
		return inputSource;
	}

}
