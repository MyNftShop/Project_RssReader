package com.reader.rss.fragments;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;

import com.reader.rss.R;
import com.reader.rss.config.NamingSpace;
import com.reader.rss.entry.RSSChannal;
import com.reader.rss.entry.RSSChannal.RSSChannalColumns;
import com.reader.rss.lib.CustomHttpClient;
import com.reader.rss.lib.MyRSSChannalsDbHelper;

import android.R.anim;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.Context;
import android.net.LocalSocketAddress.Namespace;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 描述或功能：自定义,现在是添加频道页面
 */
public class ListCustomFragment extends Fragment {

	// /**
	// *
	// * 描述或功能：获得频道信息的线程
	// */
	// public class GetChannalInfoTask extends AsyncTask<String, Integer,
	// RSSChannal>{
	// private Context context;
	// public GetChannalInfoTask(Context context) {
	// this.context=context;
	// }
	//
	// @Override
	// protected RSSChannal doInBackground(String... urls) {
	// MyRSSChannalHelper helper= new MyRSSChannalHelper();
	// return helper.getRSSChannalFromUrl(urls[0]);
	// }
	//
	// @Override
	// protected void onPostExecute(RSSChannal result) {
	// //TODO: 完成获取
	// if (result!=null) {
	// debugView.setText(result.getTitle());
	// }else {
	// debugView.setText("NULL");
	// }
	// }
	//
	//
	// }
	//
	// public GetChannalInfoTask getChannalInfoTask;

	public class MyOnSubmit implements OnClickListener {
		@Override
		public void onClick(View v) {

			ContentValues values = new ContentValues();
			values.put(RSSChannalColumns.KEY_TITLE, titleText.getText()
					.toString());
			values.put(RSSChannalColumns.KEY_LINK, urlText.getText().toString());

			MyRSSChannalsDbHelper helper = new MyRSSChannalsDbHelper(context);

			if (helper.insert(values) > 0) {
				// insert succ
				Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
				getActivity().finish();
				getActivity().setResult(getActivity().RESULT_OK);
			} else {
				Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
				// insert fail
			}

		}

	}

	private Context context;
	private EditText urlText;
	private EditText titleText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		this.context = getActivity();

		initView();

	}

	private void initView() {
		titleText = (EditText) getActivity().findViewById(R.id.editInputTitle);
		urlText = (EditText) getActivity().findViewById(R.id.editInputUrl);
		Button submitButton = (Button) getActivity().findViewById(
				R.id.buttonSubmit);

		submitButton.setOnClickListener(new MyOnSubmit());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_input_channal,
				container, false);

		return view;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
