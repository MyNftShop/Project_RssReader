package com.reader.rss;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * 
 * 描述：我的频道列表
 * 
 * @author sangxiaokai 
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 * Copyright © 2013-2113 Sang Puyuan. All rights reserved. 
 * You can not use it for any commercial purposes
 *
 */
public class MyChListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
