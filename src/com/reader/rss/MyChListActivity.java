package com.reader.rss;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

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
public class MyChListActivity extends Activity implements OnClickListener {

	/**
	 * 底部按钮IDs
	 */
	int btmBtnIds[] = { R.id.imageButton1, R.id.imageButton2,
				R.id.imageButton3 };
	private SimpleCursorAdapter adapter;

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
		ImageButton btmBtns[]=new ImageButton[3] ;
		for (int i = 0; i < btmBtnIds.length; i++) {
			btmBtns[i]=(ImageButton) findViewById(btmBtnIds[i]);
			btmBtns[i].setOnClickListener((OnClickListener) this);
		}

	}
	

	/**
	 * 初始化频道列表
	 */
	private void initListView() {
		ListView listView=(ListView) findViewById(R.id.listView1);
		configListViewAdapter();
		listView.setAdapter(adapter);
	}

	/**
	 * 配置listview数据
	 */
	private void configListViewAdapter() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 渲染频道列表
	 */
	private void renderListView() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 为了列表准备数据
	 */
	private void prepareData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化标题点击事件
	 */
	private void initTitleView() {
		LinearLayout iconLayout = (LinearLayout) findViewById(R.id.titlelinearLayout1);
		setTitle("My channal list");
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
		//底部按钮
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
