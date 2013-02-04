package com.reader.rss;

import java.util.ArrayList;

import com.reader.rss.fragments.ListCustomFragment;
import com.reader.rss.fragments.ListNewFragment;
import com.reader.rss.fragments.ListTopFragment;
import com.reader.rss.fragments.ListTypeFragment;
import com.reader.rss.fragments.MyFragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



/**
 * 
 * 功能描述：实现左右滑动的指引效果，用......来标识页数
 * 
 * @author sangxiaokai 
 * @email sangxiaokai@qq.com
 * @phone +86 15237210133
 */
public class AddChannalActivity extends FragmentActivity {
	 // ViewPager是google SDk中自带的一个附加包的一个类，可以用来实现屏幕间的切换。

	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView t4;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private ImageView cursor;
	/**
	 * 图片宽度
	 */
	private int bmpW;
	private int offsetCursor;
	protected int currentIndex=0;//当前页卡编号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_add_channal);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title);
		
		initTitleView();
		//edit below
		InitCursorView();
		InitTextView();
		InitViewPager();
	}
	/**
	 * 初始化标题点击事件
	 */
	private void initTitleView() {
		Log.i("main", "init Title View");
		LinearLayout iconLayout = (LinearLayout) findViewById(R.id.titlelinearLayout1);
		TextView title=(TextView) findViewById(R.id.titleTextView1);
		title.setText("添加频道");
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


	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		Log.i("main", "init view pager");

		fragmentList=new ArrayList<Fragment>();
		mPager=(ViewPager)findViewById(R.id.vPager);
		
		Fragment listNewFragment=new ListNewFragment();//最新
		Fragment listTopFragment=new ListTopFragment();//排行
		Fragment listTypeFragment=new ListTypeFragment();//分类
		ListCustomFragment listCustomFragment=new ListCustomFragment();//自定义
		
		
		fragmentList.add(listNewFragment);
		fragmentList.add(listTopFragment);
		fragmentList.add(listTypeFragment);
		fragmentList.add(listCustomFragment);
		
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList));
		
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			int one=offsetCursor*2+bmpW;//一个单位
			int two=2*one;//两个单位
			int three=3*one;//两个单位
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Log.i("main", "onPageSelected");

				Animation animation=null;
				switch (arg0) {
				case 0://选第一卡
					if (currentIndex==1) {
						animation=new TranslateAnimation(one	, 0, 0, 0);
					}else if (currentIndex==2) {
						animation=new TranslateAnimation(two	, 0, 0, 0);
					} else if(currentIndex==3){
						animation=new TranslateAnimation(three, 0, 0, 0);
					}
					break;
				case 1://选第二卡
					if (currentIndex==0) {//当前是第一卡
						animation=new TranslateAnimation(offsetCursor, one, 0, 0);
					}else if(currentIndex==2) {
						animation=new TranslateAnimation(two, one, 0, 0);
					}else if(currentIndex==3){
						animation=new TranslateAnimation(three, one, 0, 0);
					}
					break;
				case 2://选第三卡
					if (currentIndex==0) {//当前是第一卡
						animation=new TranslateAnimation(offsetCursor, two, 0, 0);
					}else if(currentIndex==1) {
						animation=new TranslateAnimation(one, two, 0, 0);
					}else if(currentIndex==3){
						animation=new TranslateAnimation(three, two, 0, 0);
					}
					break;
				case 3://选第4卡
					if (currentIndex==0) {//当前是第一卡
						animation=new TranslateAnimation(offsetCursor,three , 0, 0);
					}else if(currentIndex==1) {
						animation=new TranslateAnimation(one, three, 0, 0);
					}else if(currentIndex==2){
						animation=new TranslateAnimation(two, three, 0, 0);
					}
					break;
				}
				currentIndex=arg0;
				animation.setFillAfter(true);//True:图片停在动画结束位置
				animation.setDuration(300);
				cursor.startAnimation(animation);
				
				
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mPager.setCurrentItem(3);//设置到自定义卡
		
	}
	/**
     * 初始化 TAB VIEW
     */

	private void InitTextView() {
		Log.i("main", "init text view");
		t1=(TextView)findViewById(R.id.text1);
		t2=(TextView)findViewById(R.id.text2);
		t3=(TextView)findViewById(R.id.text3);
		t4=(TextView)findViewById(R.id.text4);
		
		t1.setOnClickListener(new MOnClickListener(0));
		t2.setOnClickListener(new MOnClickListener(1));
		t3.setOnClickListener(new MOnClickListener(2));
		t4.setOnClickListener(new MOnClickListener(3));
		
	}

	/**
	 * 切换TAB标签
	 * @param i
	 * @return
	 */
	public class MOnClickListener implements View.OnClickListener  {

		private int index=0;
		public MOnClickListener(int i) {
			index=i;
		}
		@Override
		public void onClick(View arg0) {
			Log.i("main", "MOnClickListener"+index);
			mPager.setCurrentItem(index);
		}
	}

	/**
	 * 初始化图片Cursor
	 */
	private void InitCursorView() {
		Log.i("main", "init image view");
		cursor=(ImageView)findViewById(R.id.cursor);
		
		bmpW=BitmapFactory.decodeResource(getResources(),R.drawable.a).getWidth();
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		int screenWidth=dm.widthPixels;//获取分辨率宽度
		offsetCursor=(screenWidth/4-bmpW)/2;//计算偏移量
		Matrix matrix=new Matrix();
		matrix.postTranslate(offsetCursor, 0);
		cursor.setImageMatrix(matrix);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
