package com.reader.rss;

import com.reader.rss.lib.MyRSSChannalsDbHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;


/**
 * 
 * 描述：首页显示
 * 
 * @author sangxiaokai 
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 * Copyright © 2013-2113 Sang Puyuan. All rights reserved. 
 * You can not use it for any commercial purposes
 *
 */
public class FirstPage extends Activity {

	
	protected static final long DELAY_MILLONS =0;//default is 3000
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==0) {
				//执行完之后的动作
				Intent intent=new Intent(FirstPage.this, MyChannalsActivity.class);
				startActivity(intent);
				FirstPage.this.finish();
//				Toast.makeText(FirstPage.this, "Go to Activity Main", Toast.LENGTH_SHORT).show();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_page);
		//add show here
		
		goToMainActivity();
	}

	private void goToMainActivity() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessageDelayed(0, DELAY_MILLONS);
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
