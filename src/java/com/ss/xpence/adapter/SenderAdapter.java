package com.ss.xpence.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ss.xpence.R;
import com.ss.xpence.model.SenderModel;

public class SenderAdapter extends ArrayAdapter<SenderModel> {

	private final Activity context;

	private List<SenderModel> objects;

	public SenderAdapter(Activity context, List<SenderModel> objects) {
		super(context, R.layout.sender_mgr_layout, objects);
		this.context = context;

		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = context.getLayoutInflater().inflate(R.layout.sender_mgr_layout, null);

		SenderModel model = getItem(position);

		TextView senderView = (TextView) view.findViewById(R.id.sm_sender);
		Spinner banksView = (Spinner) view.findViewById(R.id.sm_banks);

		senderView.setText(model.getSender());
		ArrayAdapter<String> banksAdapter = new ArrayAdapter<String>(context,
			android.R.layout.simple_expandable_list_item_1, model.getBanks());
		banksView.setAdapter(banksAdapter);

		banksView.setSelection(getPosition(model.getBanks(), model.getSelectedBank()));

		banksView.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Object item = parent.getItemAtPosition(pos);
				String value = item.toString();

				View layoutView = (View) view.getParent().getParent();
				TextView senderView = (TextView) layoutView.findViewById(R.id.sm_sender);
				String sender = senderView.getText().toString();

				updateSelectedItem(sender, value);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		return view;
	}

	private int getPosition(List<String> banks, String selectedBank) {
		int i = 0;
		for (String bank : banks) {
			if (bank.equals(selectedBank)) {
				return i;
			}
			++i;
		}
		return 0;
	}

	private void updateSelectedItem(String sender, String value) {
		for (SenderModel object : objects) {
			if (object.getSender().equals(sender)) {
				object.setSelectedBank(value);
			}

		}
	}
}
