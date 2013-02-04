package com.reader.rss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reader.rss.config.NamingSpace;
import com.reader.rss.entry.RSSChannal;
import com.reader.rss.entry.RSSItem;
import com.reader.rss.entry.RSSItems;
import com.reader.rss.entry.RSSItem.RSSItemColumns;
import com.reader.rss.lib.MyRSSChannalsDbHelper;
import com.reader.rss.lib.MyRSSItemsDbHelper;
import com.reader.rss.parser.MyRSSItemsHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
	private SimpleAdapter adapter;
	private RSSItems mRssItems;
	private List<? extends Map<String, ?>> arrayList;
	private MyRSSItemsDbHelper helper;
	private RSSItems mItems;
	private String channalUrl;
	private long channalId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.list_common);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title);

		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			setResult(RESULT_OK);
			this.finish();
			return;
		}

		channalUrl = bundle.getString(NamingSpace.BUNDLE_KEY_INTENT_URL);
		channalId = bundle.getLong(NamingSpace.BUNDLE_KEY_INTENT_ID);
		if (channalId == 0 || TextUtils.isEmpty(channalUrl)) {
			setResult(RESULT_OK);
			this.finish();
			return;
		}

		prepareData();
		initTitleView();
		initListView();

	}

	/**
	 * 打开频道
	 * 
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
		ListView listView = (ListView) findViewById(android.R.id.list);
		TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
		configListViewAdapter();
		listView.setAdapter(adapter);
		// 设置单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Map<String, String> itemMap = new HashMap<String, String>();
				itemMap = (HashMap<String, String>) parent
						.getItemAtPosition(position);
				String channalLink = itemMap.get(RSSItemColumns.KEY_LINK);
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

		adapter = new SimpleAdapter(this, arrayList,
				R.layout.rss_item_listview, from, to);
	}

	/**
	 * 渲染频道列表
	 */
	private void refreshListView() {
		arrayList.clear();
		mItems = helper.getRSSItemsByChanId(channalId, null);
		arrayList.addAll(mItems.getAllItemsForListView());
		Log.i("tag", "refresh count:" + mItems.getCount());
		adapter.notifyDataSetChanged();
	}

	/**
	 * 写入准备数据
	 */
	private void prepareData() {
		arrayList = new ArrayList<Map<String, ?>>();
		helper = new MyRSSItemsDbHelper(this);
		mItems = helper.getRSSItemsByChanId(channalId, null);
		arrayList = mItems.getAllItemsForListView();
		if (arrayList.size() == 0) {
			updateFromInternet(channalUrl);
		}

		Log.i("tag", "prepare count:" + mItems.getCount());
	}

	private void updateFromInternet(String urlString) {
		// 从网上下载信息,存入数据库
		getChannalInfoTask=new GetChannalInfoTask(this);
		getChannalInfoTask.execute(urlString);
	}

	/**
	 * 
	 * 描述或功能：获得信息的线程
	 */
	public class GetChannalInfoTask extends
			AsyncTask<String, Integer, RSSItems> {
		private Context context;

		public GetChannalInfoTask(Context context) {
			this.context = context;
		}

		@Override
		protected RSSItems doInBackground(String... urls) {
			//获得rss.xml并解析，存储至sqlite数据库中
			MyRSSItemsHelper helper = new MyRSSItemsHelper();
			Log.i("tag", "get from "+channalId+":"+channalUrl);
			RSSItems rssItems = helper.getRSSItemsFromUrl(urls[0]);
			if (rssItems!=null&&rssItems.getCount()>0) {
				//取得数据,insert into sqlite
				Log.i("tag", "get from rss.xml ready and insert into sqlite count="+rssItems.getCount());
				MyRSSItemsDbHelper dbHelper=new MyRSSItemsDbHelper(context);
				dbHelper.insert(rssItems,channalId);
			}else {
				Log.i("tag", "get from rss.xml failed");
			}
			return rssItems;
		}

		@Override
		protected void onPostExecute(RSSItems result) {
			// TODO: 完成获取
			refreshListView();
		}

	}

	public GetChannalInfoTask getChannalInfoTask;

	/**
	 * 初始化标题点击事件
	 */
	private void initTitleView() {
		LinearLayout iconLayout = (LinearLayout) findViewById(R.id.titlelinearLayout1);
		TextView title = (TextView) findViewById(R.id.titleTextView1);
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
		default:
			break;
		}
	}

}
