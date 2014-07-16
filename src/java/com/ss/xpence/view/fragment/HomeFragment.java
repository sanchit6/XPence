package com.ss.xpence.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ss.xpence.R;
import com.ss.xpence.card.thisweek.ThisWeekCardAdapter;
import com.ss.xpence.card.thisweek.ThisWeekModel;

public class HomeFragment extends Fragment {

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.layout_this_week);
		listView.setAdapter(getThisWeekAdapter());

		return rootView;
	}

	private ListAdapter getThisWeekAdapter() {
		List<ThisWeekModel> objects = new ArrayList<ThisWeekModel>();

		ThisWeekModel m1 = new ThisWeekModel();
		m1.setAccountName("HDFC Somewhere");
		m1.setAmount(12011d);
		objects.add(m1);

		ThisWeekModel m2 = new ThisWeekModel();
		m2.setAccountName("Citi Somewhere");
		m2.setAmount(4225.2d);
		objects.add(m2);
		
		ThisWeekModel m3 = new ThisWeekModel();
		m3.setAccountName("HSBC Anywhere");
		m3.setAmount(400.2d);
		objects.add(m3);

		ThisWeekCardAdapter adapter = new ThisWeekCardAdapter(getActivity(), objects);
		return adapter;
	}
}
