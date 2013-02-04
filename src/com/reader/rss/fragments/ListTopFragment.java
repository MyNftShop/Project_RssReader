package com.reader.rss.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.reader.rss.R;
import com.reader.rss.config.NamingSpace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 描述或功能：排行
 */
public class ListTopFragment extends Fragment {
	private ArrayList<Map<String, Object>> data;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		prepareData();
		initListView();
	}

	
	private void initListView() {
		String from[]={NamingSpace.KEY_OPRATION};
		int to[]={R.id.text1};
		SimpleAdapter adapter=new SimpleAdapter(getActivity(), data, R.layout.channal_item_listview, from, to);
		 listView.setAdapter(adapter);
	}
	/**
	 * 写入准备数据
	 */
	private void prepareData() {
		String array_opration[]={
				"手动输入",
				"短信提取",
				"二维码输入"
		};
		
		data = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;
		for (int i = 0; i < array_opration.length; i++) {
			
			item = new HashMap<String, Object>();
			item.put(NamingSpace.KEY_OPRATION,array_opration[i]);
			data.add(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.list_common,container,false);
		listView=(ListView) view.findViewById(android.R.id.list);
		return view;	}
}
