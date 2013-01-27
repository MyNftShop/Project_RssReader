package com.reader.rss;


import com.reader.rss.entry.RSSItem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 描述：信息显示列表
 * 
 * @author sangxiaokai 
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 * Copyright © 2013-2113 Sang Puyuan. All rights reserved. 
 * You can not use it for any commercial purposes
 *
 */
public class MyRssDetailActivity extends Activity {

	private RSSItem mRssItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.view_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title);
		
		prepareData();
		initTitleView();
		initDetailView();

		
	}

	/**
	 * 初始化信息显示
	 */
	private void initDetailView() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 初始化标题点击事件
	 */
	private void initTitleView() {
		LinearLayout iconLayout = (LinearLayout) findViewById(R.id.titlelinearLayout1);
		TextView title=(TextView) findViewById(R.id.titleTextView1);
		title.setText("文章显示页面");
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


	private void prepareData() {
		// TODO Auto-generated method stub
		mRssItem=getRssItem();
	}

	private RSSItem getRssItem() {
		RSSItem item=new RSSItem();
		item.setTitle("this is news title");
		
		
		return item;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
