package com.reader.rss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.waps.AppConnect;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v4.app.TaskStackBuilderHoneycomb;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ZoomControls;

/**
 * 
 * 功能描述：简单网页浏览器, 注意清除缓存！！！！
 * 
 * @author sangxiaokai
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 */
public class WebViewActivity extends Activity {

	public int downRequest = 3;

	public String lastUrl = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	public class MyWebViewDownLoaderListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url2, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			// TODO Auto-generated method stub
			Uri uri = Uri.parse(url2);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivityForResult(intent, downRequest);
		}

	}

	/**
	 * 
	 * 功能描述：webview 上的鼠标动作
	 * 
	 * @author sangxiaokai
	 * @email sangxiaokai@qq.com
	 * @phone +86 15237210133
	 */
	public class MyWebViewTouchListener implements OnTouchListener {

		private float OldX1, OldY1, OldX2, OldY2;

		private float NewX1, NewY1, NewX2, NewY2;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			// webView.requestFocus();

			switch (event.getAction()) {

			case MotionEvent.ACTION_POINTER_2_DOWN:

				if (event.getPointerCount() == 2) {

					OldX1 = event.getX(0);

					OldY1 = event.getY(0);

					OldX2 = event.getX(1);

					OldY2 = event.getY(1);

				}

				break;

			case MotionEvent.ACTION_MOVE:
				if (event.getPointerCount() == 2) {
					if (OldX1 == -1 && OldX2 == -1)
						break;
					NewX1 = event.getX(0);
					NewY1 = event.getY(0);
					NewX2 = event.getX(1);
					NewY2 = event.getY(1);
					float disOld = (float) Math
							.sqrt((Math.pow(OldX2 - OldX1, 2) + Math.pow(OldY2
									- OldY1, 2)));
					float disNew = (float) Math
							.sqrt((Math.pow(NewX2 - NewX1, 2) + Math.pow(NewY2
									- NewY1, 2)));
					Log.e("onTouch", "disOld=" + disOld + "|disNew=" + disNew);
					if (disOld - disNew >= 25) {
						// 缩小
						// wv.zoomOut();
						webView.zoomOut();
						Log.e("onTouch", "zoomOut");
					} else if (disNew - disOld >= 25) {
						// 放大
						webView.zoomIn();
						Log.e("onTouch", "zoomIn");
					}
					OldX1 = NewX1;
					OldX2 = NewX2;
					OldY1 = NewY1;
					OldY2 = NewY2;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (event.getPointerCount() < 2) {
					OldX1 = -1;
					OldY1 = -1;
					OldX2 = -1;
					OldY2 = -1;
				}
				break;
			}
			return false;
		}

	}

	/**
	 * 
	 * 功能描述：处理webview页面中打开动作
	 * 
	 * @author sangxiaokai
	 * @email sangxiaokai@qq.com
	 * @phone +86 15237210133
	 */
	public class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url2, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url2, favicon);
			// 设置GO button
			goBtn.setImageResource(R.drawable.navigation_cancel);
			goBtn.setOnClickListener(stopBrowserOnClick);
			// url
			lastUrl = url;
			url = url2;
			urlInput.setText(url2);
			// 图片下载阻塞
			view.getSettings().setBlockNetworkImage(true);
			// 阻塞JS脚本
			// view.getSettings().setJavaScriptEnabled(false);
			webView.requestFocus();

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			// 设置GO button
			goBtn.setImageResource(R.drawable.navigation_forward);
			goBtn.setOnClickListener(openBrowserOnClick);
			// 图片下载阻塞解除
			view.getSettings().setBlockNetworkImage(false);
			// 阻塞JS脚本解除
			// view.getSettings().setJavaScriptEnabled(true);
			// webView.requestFocus();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String theUrl) {
			// TODO Auto-generated method stub
			// urlInput.setText(theUrl);
			view.loadUrl(theUrl);
			return false;
		}

	}

	private static final CharSequence URL_TEXT_HEAD_HTTPS = "https://";

	private static final CharSequence URL_TEXT_HEAD_HTTP = "http://";

	/**
	 * the Input box that input url text
	 */
	private AutoCompleteTextView urlInput;

	/**
	 * the button that click to open browser
	 */
	private ImageView goBtn;
	private WebView webView;
	/**
	 * 当前浏览的网址
	 */
	private String url;

	/**
	 * 触摸焦点1 x
	 */
	private float touch1_x;
	/**
	 * 触摸焦点1 y
	 */
	private float touch1_y;
	/**
	 * 触摸焦点2 x
	 */
	private float touch2_x;
	/**
	 * 触摸焦点1 y
	 */
	private float touch2_y;

	private ZoomControls zoomControls;

	/**
	 * 是否开启时间任务
	 */
	private boolean haveTask = false;

	private Timer taskExit = new Timer();

	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			haveTask = false;
			try {
				taskExit.cancel();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	private ProgressBar progressBar;

	private View mainBar;

	private AlertDialog menuDialog;

	/**
	 * 打开浏览
	 */
	private OnClickListener openBrowserOnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			hideIMM();
			openBrowser();
			goBtn.setImageResource(R.drawable.navigation_cancel);
			goBtn.setOnClickListener(stopBrowserOnClick);
		}
	};
	/**
	 * 停止浏览
	 */
	private OnClickListener stopBrowserOnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			webViewStop();
			goBtn.setImageResource(R.drawable.navigation_forward);
			goBtn.setOnClickListener(openBrowserOnClick);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Log.e("test", "on create");
		// 获取配置变量
		preferenceAdapter = new PreferenceAdapter(this);
		if (preferenceAdapter.getInt(ConfigUnit.KEY_RUNTIMES, 0) >= 3) {
			int random = (int) (Math.random() * 10);
			switch (random) {
			case 1:
			case 3:
			case 4:
			case 8:
			case 2:
			case 5:
			case 6:
			case 7:
				AppConnect.getInstance(this).showPopAd(this);
				break;
			default:
				break;
			}
			;
		}
		// 快捷方式
		installShortCut();
		// 手势动作
		increaceTimes();

		setWindowFullScreen();

		initViews();

	}

	/**
	 * install shortCut at first run
	 */
	private void installShortCut() {
		if (preferenceAdapter.getBoolean(ConfigUnit.KEY_FIRSTRUN, true)) {
			// first run
			AlertDialog.Builder builder = new AlertDialog.Builder(
					WebViewActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle(R.string.app_name);
			builder.setMessage(getResources().getString(
					R.string.msg_doInstallShortcut));
			builder.setPositiveButton(
					getResources().getString(R.string.label_yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							ShortcutUtil.createShortCut(WebViewActivity.this,
									R.drawable.ic_launcher, R.string.app_name);
						}
					});
			builder.setNegativeButton(
					getResources().getString(R.string.label_no),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			builder.create().show();
			preferenceAdapter.saveBoolean(ConfigUnit.KEY_FIRSTRUN, false);// 设置第一次失效
		}

	}

	/**
	 * 
	 */
	private void setWindowFullScreen() {
		// TODO Auto-generated method stub
		if (preferenceAdapter.getBoolean(ConfigUnit.KEY_FULLSCREEN, false)) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (menuDialog != null) {
			if (menuDialog.isShowing()) {
				menuDialog.dismiss();
				menuDialog = createMenuDialog();
				menuDialog.show();
			} else {
				menuDialog = createMenuDialog();
			}
		}
	}

	/**
	 * 使用次数
	 */
	private void increaceTimes() {
		// TODO Auto-generated method stub
		int times = preferenceAdapter.getInt(ConfigUnit.KEY_RUNTIMES, 0);
		if (times < 30) {// 最大检测数，最大是30
			times++;
			preferenceAdapter.saveInt(ConfigUnit.KEY_RUNTIMES, times);
		}
	}

	private void initViews() {
		// 顶部输入和GO按钮
		mainBar = findViewById(R.id.linearLayout1);
		urlInput = (AutoCompleteTextView) this
				.findViewById(R.id.autoCompleteTextView1);
		goBtn = (ImageView) this.findViewById(R.id.imageButton1);
		urlInput.setSelectAllOnFocus(true);

		goBtn.setOnClickListener(openBrowserOnClick);

		urlInput.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					hideIMM();
					openBrowser();
					return true;
				}
				return false;
			}
		});

		webView = (WebView) this.findViewById(R.id.webView1);
		configWebView(webView);
		zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);
		zoomControls.setVisibility(View.GONE);
		zoomControls.setOnZoomInClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// zoom in
				webView.zoomIn();
			}
		});
		zoomControls.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// zoom out
				webView.zoomOut();
			}
		});
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
	}

	protected void hideIMM() {
		// TODO Auto-generated method stub
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))// 隐藏软键盘
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webViewGoback();
				// 返回
			} else {
				// 不能返回
				if (!haveTask) {
					haveTask = true;
					Toast.makeText(this, R.string.msg_pressBackAgainToExit,
							Toast.LENGTH_SHORT).show();
					// 设置时间任务
					try {
						taskExit.schedule(task, 1000);// 两次按键间隔，默认为1秒
					} catch (Exception e) {
					}
				} else {
					// 退出
					this.finish();
					return super.onKeyDown(keyCode, event);
				}
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/*
	 * 配置内置浏览控件
	 */
	private void configWebView(WebView theWebView) {
		// TODO Auto-generated method stub
		WebSettings set = webView.getSettings();

		webView.setFocusable(true);
		webView.setFocusableInTouchMode(true);
		webView.playSoundEffect(SoundEffectConstants.CLICK);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setDownloadListener(new MyWebViewDownLoaderListener());// 下载监听

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				showMainBar();
				progressBar.setVisibility(View.VISIBLE);
				progressBar.setProgress(newProgress);
				if (newProgress == progressBar.getMax()) {
					// 加载完毕
					progressBar.setVisibility(View.GONE);
					hideMainBar();
				}
				super.onProgressChanged(view, newProgress);
			}

		});
		webView.setWebViewClient(new MyWebViewClient());

		// load default url
		webView.loadUrl(getDefaultUrl());

	}

	protected void showMainBar() {
		// TODO Auto-generated method stub
		mainBar.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏mainbar
	 */
	protected void hideMainBar() {
		// TODO Auto-generated method stub
		// mainBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		return super.onTouchEvent(event);
	}

	/**
	 * 获得默认主页
	 * 
	 * @return
	 */
	private String getDefaultUrl() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Log.e("test", "get action " + intent.toString());
		if (intent.getAction() != null
				&& intent.getAction().equals("android.intent.action.VIEW")) {
			Log.e("test", "get action");

			Uri uri_text = intent.getData();
			Log.i("test", uri_text.toString());
			Toast.makeText(
					this,
					getResources().getString(R.string.msg_loadingOnFromAction)
							+ "\n" + uri_text.toString(), Toast.LENGTH_SHORT)
					.show();
			return uri_text.toString();
		}
		Log.e("test", "get homePage");
		return getHomePage();
	}

	/**
	 * 打开浏览器 取得浏览地址url
	 */
	protected void openBrowser() {
		// TODO Auto-generated method stub
		String urlText = urlInput.getText().toString();
		if (!urlText.startsWith((String) URL_TEXT_HEAD_HTTP)
				&& !urlText.startsWith((String) URL_TEXT_HEAD_HTTPS)) {
			urlText = URL_TEXT_HEAD_HTTP + urlText;
		}
		updateStatusBar(urlText);
		webView.loadUrl(urlText);
		// webView.requestFocus();
		webView.setOnTouchListener(new MyWebViewTouchListener());

	}

	/**
	 * 更新状态栏
	 * 
	 * @param url2
	 */
	private void updateStatusBar(String theUrl) {
		// TODO Auto-generated method stub
		// setTitle("Load to " + theUrl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		showMenuDlg();

		return false;// 返回为true 则显示系统menu
	}

	/**
	 * 显示menu dlg
	 */
	private void showMenuDlg() {
		if (menuDialog == null) {
			menuDialog = createMenuDialog();
		}
		Log.i("menu", "show");
		menuDialog.show();
	}

	/**
	 * create menu dialog
	 * 
	 * @param mainActivity
	 * @return
	 */
	private AlertDialog createMenuDialog() {
		// TODO Auto-generated method stub
		View menuView = View.inflate(this, R.layout.menu_view, null);
		// 创建alertdialog
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		// 设置位置
		Window w = menuDialog.getWindow();
		w.setGravity(Gravity.BOTTOM);
		// w.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
		// WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.alpha = 0.8f;
		// lp.
		// lp.x=0;
		// lp.y=300;//尽量设定一个合适的位置，小屏手机可能会显示不出来

		menuDialog.onWindowAttributesChanged(lp);
		// 设置位置end
		menuDialog.setCanceledOnTouchOutside(true);// 点击外面的时候关闭
		menuDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_MENU) {
					menuDialog.dismiss();
				}
				return false;
			}
		});
		configMenuView(menuView);

		return menuDialog;
	}

	/** 菜单文字 **/
	String[] menu_name_array = { "刷新", "返回", "前进", "停止", "全屏", "主页", "清除缓存",
			"设置主页", "关于", "退出" };// 0 1 2 3 4 5 6 7 8 9

	/** 菜单文字 **/
	int[] menu_name_array_ids = { R.string.menu_refresh, R.string.menu_back,
			R.string.menu_forward, R.string.menu_block,
			R.string.menu_fullscreen, R.string.menu_home,
			R.string.menu_clearcache, R.string.menu_sethome,
			R.string.menu_about, R.string.menu_exit

	};

	/** 菜单图片 **/
	int[] menu_image_array = { R.drawable.ic_menu_refresh,
			R.drawable.ic_menu_back, R.drawable.ic_menu_forward,
			R.drawable.ic_menu_block, R.drawable.ic_menu_full_screen_0,
			R.drawable.ic_menu_home, R.drawable.ic_menu_delete,

			R.drawable.ic_menu_attachment, R.drawable.ic_menu_info_details,
			R.drawable.ic_menu_close_clear_cancel

	};

	private PreferenceAdapter preferenceAdapter;

	/**
	 * 设置监听
	 * 
	 * @param menuView
	 * 
	 */
	private void configMenuView(View menuView) {
		GridView menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array_ids,
				menu_image_array));
		// 设置监听
		menuGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 选择菜单项
				switch (arg2) {
				case 0:
					// 刷新
					webViewReload();
					break;
				case 1:
					// 返回
					webViewGoback();
					break;
				case 2:
					// 前进
					webViewForward();
					break;
				case 3:
					// 停止
					webViewStop();
					break;
				case 4:
					// 全屏
					menuDialog.dismiss();
					changeWindowFullScreen();
					menuDialog = null;
					menuDialog = createMenuDialog();
					return;
				case 5:
					// 主页
					webViewGoHomePage();
					break;
				case 6:
					// 清除缓存
					webViewClearCache();
					break;
				case 7:
					// 设置当前页为主页
					webViewSetCurrentHomePage();
					break;
				case 8:
					// 关于
					doAbout();
					break;
				case 9:
					// 退出
					doExit();
					break;
				default:
					break;
				}
				menuDialog.dismiss();
			}

			private void doAbout() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						WebViewActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle(R.string.msg_titleAbout);
				builder.setMessage(R.string.msg_appAbout);
				builder.setPositiveButton(R.string.label_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});
				// builder.setNeutralButton(R.string.label_ShareToFriends,
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog,
				// int whichButton) {
				// // show offers
				// doShare();
				// }
				// });
				builder.create().show();
			}
		});

	}

	/**
	 * 全屏切换
	 */
	protected void changeWindowFullScreen() {
		// TODO Auto-generated method stub
		if (!preferenceAdapter.getBoolean(ConfigUnit.KEY_FULLSCREEN, false)) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			preferenceAdapter.saveBoolean(ConfigUnit.KEY_FULLSCREEN, true);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			preferenceAdapter.saveBoolean(ConfigUnit.KEY_FULLSCREEN, false);
		}

	}

	/**
	 * 退出
	 */
	private void doExit() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.app_name);
		builder.setMessage(getResources().getString(R.string.msg_doExit));
		builder.setPositiveButton(getResources().getString(R.string.label_ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				});
		builder.setNegativeButton(
				getResources().getString(R.string.label_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		builder.create().show();
	}

	protected void webViewGoHomePage() {
		// TODO Auto-generated method stub
		webView.loadUrl(getHomePage());
	}

	/**
	 * 获得自定义的主页
	 * 
	 * @return
	 */
	private String getHomePage() {
		return preferenceAdapter.getString(ConfigUnit.KEY_HomePage,
				"http://www.google.com");
	}

	protected void webViewClearCache() {
		// TODO Auto-generated method stub
		webView.clearCache(true);
		webView.clearFormData();
		webView.clearHistory();
		Toast.makeText(this, R.string.msg_clearCacheOk, Toast.LENGTH_SHORT)
				.show();
	}

	protected void webViewSetCurrentHomePage() {
		preferenceAdapter.saveString(ConfigUnit.KEY_HomePage, webView.getUrl());
		Toast.makeText(
				this,
				getResources().getString(R.string.msg_setHomeOk) + "\n"
						+ webView.getUrl(), Toast.LENGTH_SHORT).show();
	}

	private void webViewGoback() {
		// TODO Auto-generated method stub
		if (webView.canGoBack()) {
			webView.goBack();
			// urlInput.setText(webView.getUrl());
		}
	}

	protected void webViewReload() {
		// TODO Auto-generated method stub
		webView.reload();
	}

	protected void webViewForward() {
		// TODO Auto-generated method stub
		if (webView.canGoForward()) {
			webView.goForward();
			// urlInput.setText(webView.getUrl());
		}
	}

	protected void webViewStop() {
		// TODO Auto-generated method stub
		webView.stopLoading();
	}

	/**
	 * 
	 * @param menu_image_array2
	 * @param menu_name_array2
	 * @return
	 */
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] menuImgArray) {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// 设置全屏按钮
			if (i == 4
					&& preferenceAdapter.getBoolean(ConfigUnit.KEY_FULLSCREEN,
							false)) {
				// fullscreen
				map.put("itemImage", R.drawable.ic_menu_full_screen_1);
			} else {
				map.put("itemImage", menuImgArray[i]);
			}
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;

	}

	/**
	 * 
	 * @param menu_image_array2
	 * @param menu_name_array2
	 * @return
	 */
	private SimpleAdapter getMenuAdapter(int[] menuNameArray, int[] menuImgArray) {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// 设置全屏按钮
			if (i == 4
					&& preferenceAdapter.getBoolean(ConfigUnit.KEY_FULLSCREEN,
							false)) {
				// fullscreen
				map.put("itemImage", R.drawable.ic_menu_full_screen_1);
			} else {
				map.put("itemImage", menuImgArray[i]);
			}
			map.put("itemText", getResources().getString(menuNameArray[i]));
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;

	}

}
