package com.reader.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reader.rss.config.NamingSpace;
import com.reader.rss.entry.RSSItems;
import com.reader.rss.entry.RSSItem.RSSItemColumns;
import com.reader.rss.lib.MyDate;
import com.reader.rss.lib.MyRSSItemsDbHelper;
import com.reader.rss.parser.MyRSSItemsHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
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

	/**
	 * 刷新列表
	 */
	public static final int REFRESH_LIST =1;


	/***
	 * 线程：从数据库中获得列表,执行成功后，如果获得列表失败或是空的，启动线程从网路获取
	 */
	public class GetListFromLocalTask extends AsyncTask<Long, Integer, List<Map<String, Object>>> {
		private Context context;
		
		public GetListFromLocalTask(Context context) {
			this.context=context;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			if (result!=null&&result.size()>0) {
				Log.i(TAG, "get list from local ok with count"+result.size());
				//成功获得有数据的列表
				mHandler.sendEmptyMessage(REFRESH_LIST);
			}else {
				//数据个数是0，从网路获取
				Log.e(TAG, "get list from internet");
				//检测网络连接状态
				//启动从网络获取数据列表线程
				//成功获得有数据的列表
				if(getListFromInternetTask != null) {
		    		AsyncTask.Status diStatus = getListFromInternetTask.getStatus();
		    		Log.v("doClick", "diTask status is " + diStatus);
		    		if(diStatus != AsyncTask.Status.FINISHED) {
		    			Log.v("doClick", "... no need to start a new task");
		                return;
		    		}
		    	}
				getListFromInternetTask=new GetListFromInternetTask(context);
				getListFromInternetTask.execute(channalUrl);
			}
		}

		/**
		 * 从数据库中获得列表
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected List<Map<String, Object>> doInBackground(Long... params) {

			helper = new MyRSSItemsDbHelper(context);
			mRSSItems = helper.getRSSItemsByChanId(channalId, null);
			return mRSSItems.getAllItemsForListView();
		}
		


	}

	protected GetListFromInternetTask getListFromInternetTask;
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_LIST:
				refreshListView();
				break;
			}
		}
		
	};

	/**
	 * 描述或功能：从本地取得数据的线程
	 */

	private static final String TAG = "onACView";
	private SimpleAdapter adapter;
	/**
	 * listview 所用的list数据
	 */
	private List<? extends Map<String, ?>> arrayList=new ArrayList<Map<String, ?>>();
	/**
	 * 数据库支持类
	 */
	private MyRSSItemsDbHelper helper;
	/**
	 * 信息列表 
	 */
	private RSSItems mRSSItems;
	
	private String channalUrl;
	private long channalId;
	private String channalTitle;

	private GetListFromLocalTask getListFromLocalTask;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.list_common);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title);

		getDataFromIntent();
	
		initTitleView();
		initListView();
		prepareInitData();

	}

	private void getDataFromIntent() {
		//get data from intent
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			channalTitle = bundle.getString(NamingSpace.BUNDLE_KEY_INTENT_TITLE);
			channalUrl = bundle.getString(NamingSpace.BUNDLE_KEY_INTENT_URL);
			channalId = bundle.getLong(NamingSpace.BUNDLE_KEY_INTENT_ID);
			Log.i("tag","获得 "+channalTitle+" "+channalUrl );
		}else {
			setResult(RESULT_OK);
			this.finish();
			return;
		}
	}

	/**
	 * 打开频道
	 * 
	 * @param url
	 */
	protected void GoToDetail(String url) {
		Log.i(TAG, "GoToChannal url=" + url);
		Intent intent = new Intent(this, WebViewActivity.class);
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
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Map<String, String> itemMap = new HashMap<String, String>();
				itemMap = (HashMap<String, String>) parent
						.getItemAtPosition(position);
				String link = itemMap.get(RSSItemColumns.KEY_LINK);
				GoToDetail(link);
			}
		});

	}

	/**
	 * 配置listview数据的adapter,显示方式
	 */
	private void configListViewAdapter() {
		// TODO Auto-generated method stub
		String from[] = { 
				RSSItemColumns.KEY_TITLE,
				RSSItemColumns.KEY_PUBDATE,
				RSSItemColumns.KEY_DESCRIPTION,
				null
		};
		int to[] = { 
				R.id.textView_title,
				R.id.textView_time,
				R.id.textView_desc,
				R.id.textView_type
		};

		adapter = new SimpleAdapter(this, arrayList,
				R.layout.rss_item_listview, from, to);
		//设置显示方式
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view.getId()==R.id.textView_type) {
					Log.i("tag", "set text="+channalTitle);
					((TextView) view).setText(channalTitle);
				}else if (view.getId()==R.id.textView_time) {
					((TextView) view).setText(dateFormater(textRepresentation));
				}else if (view.getId()==R.id.textView_desc) {
				
					//显示可视化的网页
					((TextView) view).setText(Html.fromHtml(textRepresentation, imageGetter, null));
				}else {
					return false;//使用原来的binde
				}
				
				return true;
			}

			/**
			 * 转换日期到需要的格式
			 * @param textRepresentation
			 * @return
			 */
			private CharSequence dateFormater(String textRepresentation) {
				// TODO Auto-generated method stub
				return MyDate.formatGMTToLocal(textRepresentation, NamingSpace.DEFAULT_TIME_FORMAT);
			}
			
		});
		
	}
	ImageGetter imageGetter=new Html.ImageGetter() {
		
		@Override
		public Drawable getDrawable(String source) {
			 Drawable drawable = null;
			   Log.d("Image Path", source);
			   URL url;
			   try {
			    url = new URL(source);
			    drawable = Drawable.createFromStream(url.openStream(), "src");
			   } catch (Exception e) {
			    return null;
			   }
			   drawable.setBounds(0, 0, 100,100);
			   return drawable;
		}
	};

	/**
	 * 渲染频道列表
	 */
	private void refreshListView() {
		arrayList.clear();
		arrayList.addAll(mRSSItems.getAllItemsForListView());
		Log.i("tag", "refresh count:" + arrayList.size());
		adapter.notifyDataSetChanged();
	}

	/**
	 * 写入准备数据
	 */
	private void prepareInitData() {
		//数据库线程
		getListFromLocalTask=new GetListFromLocalTask(this);
		getListFromLocalTask.execute(channalId);
	}

	/**
	 * 
	 * 线程：从网路获取信息列表,并插入本地数据库中
	 */
	public class GetListFromInternetTask extends
			AsyncTask<String, Integer, List<? extends Map<String,?>>> {
		private Context context;

		public GetListFromInternetTask(Context context) {
			this.context = context;
		}

		/**
		 * 从网路获取信息列表,并插入本地数据库中
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected List<? extends Map<String, ?>> doInBackground(String... urls) {
			//获得rss.xml并解析，存储至sqlite数据库中
			MyRSSItemsHelper helper = new MyRSSItemsHelper();
			Log.i("tag", "网络获取xml "+channalId+":"+channalUrl);
			RSSItems rssItems = helper.getRSSItemsFromUrl(urls[0]);
			if (rssItems!=null&&rssItems.getCount()>0) {
				//取得数据,insert into sqlite
				Log.i("tag", "get from rss.xml ready and insert into sqlite count="+rssItems.getCount());
				MyRSSItemsDbHelper dbHelper=new MyRSSItemsDbHelper(context);
				dbHelper.insert(rssItems,channalId);
			}else {
				Log.i("tag", "get from rss.xml failed");
			}
			return rssItems.getAllItemsForListView();
		}

		@Override
		protected void onPostExecute(List<? extends Map<String, ?>> result) {
			if (result!=null&&result.size()>0) {
				Log.i(TAG, "get list from internet ok with count"+result.size());
				//成功获得有数据的列表
				if(getListFromLocalTask != null) {
		    		AsyncTask.Status diStatus = getListFromLocalTask.getStatus();
		    		Log.v("doClick", "diTask status is " + diStatus);
		    		if(diStatus != AsyncTask.Status.FINISHED) {
		    			Log.v("doClick", "... no need to start a new task");
		                return;
		    		}
		    	}
				getListFromLocalTask = new GetListFromLocalTask(context);
				getListFromLocalTask.execute(channalId);
			}else {
				//数据个数是0，从网路获取
				Log.e(TAG, "get list from internet failed");
				Toast.makeText(context, "获取数据失败，请检查网络设置", Toast.LENGTH_SHORT).show();
			}
		}


	}


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
