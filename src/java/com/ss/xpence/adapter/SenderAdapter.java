package com.ss.xpence.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ss.xpence.R;
import com.ss.xpence.model.SenderModel;

public class SenderAdapter extends ArrayAdapter<SenderModel> {

	private final Activity context;
	private boolean showHidden;

	private final List<SenderModel> allObjects;

	public SenderAdapter(Activity context, List<SenderModel> objects) {
		super(context, R.layout.sender_mgr_layout, clone(objects, false));
		this.context = context;

		this.allObjects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SenderModel model = getItem(position);

		View view = context.getLayoutInflater().inflate(R.layout.sender_mgr_layout, null);

		TextView senderView = (TextView) view.findViewById(R.id.sm_sender);
		Spinner banksView = (Spinner) view.findViewById(R.id.sm_banks);

		senderView.setText(model.getSender());
		SimpleListItem1Adapter banksAdapter = new SimpleListItem1Adapter(context, model.getBanks());
		banksView.setAdapter(banksAdapter);

		banksView.setSelection(getPosition(model.getBanks(), model.getSelectedBank()));

		banksView.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				clearMargins(view);
				clearMargins(view.findViewById(R.id.list_item_1));

				Object item = parent.getItemAtPosition(pos);
				String value = item.toString();

				View layoutView = (View) view.getParent().getParent();
				TextView senderView = (TextView) layoutView.findViewById(R.id.sm_sender);
				String sender = senderView.getText().toString();

				setSelectedBank(sender, value);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		final ImageView addImage = (ImageView) view.findViewById(R.id.sm_add);
		final ImageView remImage = (ImageView) view.findViewById(R.id.sm_remove);

		if (model.hidden()) {
			addImage.setVisibility(View.VISIBLE);
			remImage.setVisibility(View.INVISIBLE);
		} else {
			addImage.setVisibility(View.INVISIBLE);
			remImage.setVisibility(View.VISIBLE);
		}

		addImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LinearLayout layout = (LinearLayout) v.getParent().getParent();

				String sender = ((TextView) layout.findViewById(R.id.sm_sender)).getText().toString();
				setHidden(sender, false);

				resetAdapter();
			}
		});

		remImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LinearLayout layout = (LinearLayout) v.getParent().getParent();

				String sender = ((TextView) layout.findViewById(R.id.sm_sender)).getText().toString();
				setHidden(sender, true);

				resetAdapter();
			}
		});

		return view;
	}

	private void clearMargins(View view) {
		if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
			p.setMargins(7, 0, 0, 0);
			view.requestLayout();
		}
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

	private static List<SenderModel> clone(List<SenderModel> source, boolean showHidden) {
		List<SenderModel> destination = new ArrayList<SenderModel>();
		for (SenderModel senderModel : source) {
			if (!showHidden && senderModel.hidden()) {
				continue;
			}
			destination.add(senderModel);
		}
		return destination;
	}

	private void setHidden(String sender, boolean isHidden) {
		for (SenderModel object : allObjects) {
			if (object.getSender().equals(sender)) {
				object.setHidden(isHidden);
				break;
			}
		}
	}

	private void setSelectedBank(String sender, String value) {
		for (SenderModel object : allObjects) {
			if (object.getSender().equals(sender)) {
				object.setSelectedBank(value);
				break;
			}
		}
	}

	public void resetAdapter() {
		this.clear();
		this.addAll(clone(allObjects, showHidden));

		this.notifyDataSetChanged();
	}

	public void setShowHidden(boolean showHidden) {
		this.showHidden = showHidden;
	}
}
