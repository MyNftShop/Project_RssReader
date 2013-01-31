package com.reader.rss.fragments;

import com.reader.rss.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述或功能：分类，里边还应该可以再加入1个list
 */
public class ListTypeFragment extends Fragment {

	public ListTypeFragment() {
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.lay1,container,false);
		return view;	}

}
