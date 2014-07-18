package com.ss.xpence.view.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ss.xpence.R;

public class SimpleCard extends LinearLayout {

	ListView innerListing;

	public SimpleCard(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Set layout parameters
		setOrientation(LinearLayout.HORIZONTAL);

		// inflate the layout
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.compound_view_simple_card, this, true);

		innerListing = (ListView) findViewById(R.id.card_body_list);
		// TODO Auto-generated constructor stub
	}

	public SimpleCard(Context context) {
		this(context, null);
	}

	public void setAdapter(ListAdapter adapter) {
		innerListing.setAdapter(adapter);
	}

}
