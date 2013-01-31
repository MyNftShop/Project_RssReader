package com.reader.rss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.reader.rss.config.NamingSpace;
import com.reader.rss.entry.RSSChannal;
import com.reader.rss.entry.RSSItem;
import com.reader.rss.entry.RSSItems;
import com.reader.rss.entry.RSSItem.RSSItemColumns;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * 描述：频道信息显示列表
 * 
 * @author sangxiaokai
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133 Copyright © 2013-2113 Sang Puyuan. All rights
 *        reserved. You can not use it for any commercial purposes
 * 
 */
public class MyChannalActivity extends Activity implements OnClickListener {

	private static final String TAG = "onACView";
	/**
	 * 底部按钮IDs
	 */
	int btmBtnIds[] = { R.id.imageButton1, R.id.imageButton2, R.id.imageButton3 };
	private SimpleAdapter adapter;
	private RSSItems mRssItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.list_mychview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title);

		prepareData();
		initTitleView();
		initListView();
		initBottomBar();

	}

	/**
	 * 初始化底部工具栏
	 */
	private void initBottomBar() {
		// TODO Auto-generated method stub
		int btmBtnIds[] = { R.id.imageButton1, R.id.imageButton2,
				R.id.imageButton3 };
		ImageButton btmBtns[] = new ImageButton[3];
		for (int i = 0; i < btmBtnIds.length; i++) {
			btmBtns[i] = (ImageButton) findViewById(btmBtnIds[i]);
			btmBtns[i].setOnClickListener((OnClickListener) this);
		}

	}
	/**
	 * 打开频道
	 * @param url
	 */
	protected void GoToDetail(String url) {
		Log.i(TAG, "GoToChannal url=" + url);
		Intent intent = new Intent(this, MyRssDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(NamingSpace.BUNDLE_KEY_INTENT_URL, url);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**
	 * 初始化列表
	 */
	private void initListView() {
		ListView listView = (ListView) findViewById(R.id.listView1);
		configListViewAdapter();
		listView.setAdapter(adapter);
		//设置单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Map<String, String> itemMap = new HashMap<String, String>();
				itemMap = (HashMap<String, String>) parent
						.getItemAtPosition(position);
				String channalLink=itemMap.get(RSSItemColumns.KEY_LINK);
				GoToDetail(channalLink);
			}
		});
		
	}
	

	/**
	 * 配置listview数据的adapter,显示方式
	 */
	private void configListViewAdapter() {
		// TODO Auto-generated method stub
		String from[] = { RSSItemColumns.KEY_TITLE, RSSItemColumns.KEY_LINK };
		int to[] = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleAdapter(this, mRssItems.getAllItemsForListView(),
				android.R.layout.simple_list_item_2, from, to);
	}

	/**
	 * 渲染频道列表
	 */
	private void renderListView() {
		// TODO Auto-generated method stub

	}

	/**
	 * 写入准备数据
	 */
	private void prepareData() {
		mRssItems=getMyRSSItems();
//		data = new ArrayList<Map<String, Object>>();
//		Map<String, Object> item = null;
//		// add baidu
//		item = new HashMap<String, Object>();
//		item.put(RSSChannal.KEY_TITLE, "baidu");
//		item.put(RSSChannal.KEY_LINK,
//				"http://news.baidu.com/n?cmd=1&class=civilnews&tn=rss");
//		data.add(item);
//		// add 163
//		item = new HashMap<String, Object>();
//		item.put(RSSChannal.KEY_TITLE, "163");
//		item.put(RSSChannal.KEY_LINK,
//				"http://news.163.com/special/00011K6L/rss_newstop.xml");
//		data.add(item);
//		// add 163
//		item = new HashMap<String, Object>();
//		item.put(RSSChannal.KEY_TITLE, "QQ");
//		item.put(RSSChannal.KEY_LINK,
//				"http://news.qq.com/newsgn/rss_newsgn.xml");
//		data.add(item);
//		// add 163
//		item = new HashMap<String, Object>();
//		item.put(RSSChannal.KEY_TITLE, "南方");
//		item.put(RSSChannal.KEY_LINK, "http://www.southcn.com/rss/default.xml");
//		data.add(item);
//		// add 163
//		item = new HashMap<String, Object>();
//		item.put(RSSChannal.KEY_TITLE, "南方航空");
//		item.put(RSSChannal.KEY_LINK, "http://www.southcn.com/rss/default.xml");
//		data.add(item);

	}

	/**
	 * 
	 * @return
	 */
	private RSSItems getMyRSSItems() {
		// TODO Auto-generated method stub
		RSSItems items=new RSSItems();
		RSSItem item=null;
		//add
		item=new RSSItem();
		item.setTitle("News_title");
		item.setLink("the linke to news");
		items.addItem(item);
		//add
		item=new RSSItem();
		item.setTitle("News_title");
		item.setLink("the linke to news");
		items.addItem(item);
		//add
		item=new RSSItem();
		item.setTitle("News_title");
		item.setLink("the linke to news");
		items.addItem(item);
		//add
		item=new RSSItem();
		item.setTitle("News_title");
		item.setLink("the linke to news");
		items.addItem(item);
		
		
		return items;
	}

	/**
	 * 初始化标题点击事件
	 */
	private void initTitleView() {
		LinearLayout iconLayout = (LinearLayout) findViewById(R.id.titlelinearLayout1);
		TextView title=(TextView) findViewById(R.id.titleTextView1);
		title.setText("频道文章列表");
		iconLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doExit();
			}
		});
	}

	/**
	 * 退出
	 */
	protected void doExit() {
		Toast.makeText(this, "Do 退出", Toast.LENGTH_SHORT).show();
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 底部按钮
		case R.id.imageButton1:
			doAddCh();
			break;
		case R.id.imageButton2:
			doRefreshCh();
			break;
		case R.id.imageButton3:
			doSettings();
			break;

		default:
			break;
		}
	}

	private void doSettings() {
		Toast.makeText(this, "Do 设置频道", Toast.LENGTH_SHORT).show();
	}

	private void doRefreshCh() {
		Toast.makeText(this, "Do 刷新频道", Toast.LENGTH_SHORT).show();
		renderListView();
	}

	private void doAddCh() {
		Toast.makeText(this, "Do 添加频道", Toast.LENGTH_SHORT).show();
	}

}
