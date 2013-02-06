package com.reader.rss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reader.rss.config.NamingSpace;
import com.reader.rss.entry.RSSChannal;
import com.reader.rss.entry.RSSChannal.RSSChannalColumns;
import com.reader.rss.entry.RSSChannals;
import com.reader.rss.lib.MyRSSChannalsDbHelper;

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
 * 描述：我的频道列表
 * 
 * @author sangxiaokai
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133 Copyright © 2013-2113 Sang Puyuan. All rights
 *        reserved. You can not use it for any commercial purposes
 * 
 */
public class MyChannalsActivity extends Activity implements OnClickListener {

	private static final String TAG = "onAcChList";
	/**
	 * 底部按钮IDs
	 */
	int btmBtnIds[] = { R.id.imageButton1, R.id.imageButton2, R.id.imageButton3 };
	private SimpleAdapter adapter;
	private RSSChannals mChannals = null;
	private ListView listView;
	private List<? extends Map<String, ?>> arrayList;
	private MyRSSChannalsDbHelper helper;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.list_mych);
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
	 * 
	 * @param posi[]
	 */
	protected void GoToChannal(int position) {
		Log.i("tag", "GotoChannal:"+ position+arrayList.get(position).get(RSSChannalColumns.KEY_TITLE));
		Intent intent = new Intent(this, MyChannalActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(NamingSpace.BUNDLE_KEY_INTENT_TITLE, (String)arrayList.get(position).get(RSSChannalColumns.KEY_TITLE));	
		bundle.putString(NamingSpace.BUNDLE_KEY_INTENT_URL, (String)arrayList.get(position).get(RSSChannalColumns.KEY_LINK));	
		bundle.putLong(NamingSpace.BUNDLE_KEY_INTENT_ID, (Long) arrayList.get(position).get(RSSChannalColumns._ID));
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 初始化列表
	 */
	private void initListView() {
		listView = (ListView) findViewById(R.id.listView1);
		configListViewAdapter();
		listView.setAdapter(adapter);
		// 设置单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				GoToChannal(position);
//				// TODO Auto-generated method stub
//				Map<String, String> itemMap = new HashMap<String, String>();
//				itemMap = (HashMap<String, String>) parent
//						.getItemAtPosition(position);
//				String channalLink = itemMap.get(RSSChannalColumns.KEY_LINK);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//
		refreshListView();
	}

	/**
	 * 配置listview数据的adapter,显示方式
	 */
	private void configListViewAdapter() {
		// TODO Auto-generated method stub
		String from[] = { RSSChannalColumns.KEY_TITLE,
				RSSChannalColumns.KEY_LINK };
		int to[] = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleAdapter(this, arrayList,
				android.R.layout.simple_list_item_2, from, to);
	}

	/**
	 * 渲染频道列表
	 */
	private void refreshListView() {
		arrayList.clear();
		mChannals=helper.getRSSChannals(null);
		arrayList.addAll(mChannals.getAllItemsForListView());
		Log.i("tag", "refresh count:"+mChannals.getItemCount());
		adapter.notifyDataSetChanged();
	}

	/**
	 * 写入准备数据
	 */
	private void prepareData() {
		helper = new MyRSSChannalsDbHelper(this);
		mChannals = helper.getRSSChannals(null);
		arrayList = new ArrayList<Map<String, ?>>();
		arrayList = mChannals.getAllItemsForListView();
		Log.i("tag", "prepare count:"+mChannals.getItemCount());
	}

	/**
	 * 初始化标题点击事件
	 */
	private void initTitleView() {
		LinearLayout iconLayout = (LinearLayout) findViewById(R.id.titlelinearLayout1);
		TextView title = (TextView) findViewById(R.id.titleTextView1);
		title.setText("频道列表");
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
		refreshListView();
	}

	private void doAddCh() {
		Toast.makeText(this, "Do 添加频道", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, AddChannalActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putString(NameSpacesOfMy.BUNDLE_KEY_INTENT_URL, url);
		// intent.putExtras(bundle);
		startActivityForResult(intent, 0);
	}

}
