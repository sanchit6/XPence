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
import com.ss.xpence.view.ViewHolder;

public class SenderAdapter extends ArrayAdapter<SenderModel> {

	private final Activity context;
	private boolean showHidden;

	private final List<SenderModel> allObjects;

	public SenderAdapter(Activity context, List<SenderModel> objects) {
		super(context, R.layout.sender_mgr_layout, clone(objects, false));
		this.context = context;

		this.allObjects = objects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder<TextView> h1 = new ViewHolder<TextView>();
		ViewHolder<Spinner> h2 = new ViewHolder<Spinner>();

		if (convertView == null) {
			convertView = context.getLayoutInflater().inflate(R.layout.sender_mgr_layout, null);

			h1.set((TextView) convertView.findViewById(R.id.sm_sender));
			convertView.setTag(R.id.sm_sender, h1);

			h2.set((Spinner) convertView.findViewById(R.id.sm_banks));
			convertView.setTag(R.id.sm_banks, h2);

			h2.get().setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					if (view == null) {
						return;
					}

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
		} else {
			h1 = (ViewHolder<TextView>) convertView.getTag(R.id.sm_sender);
			h2 = (ViewHolder<Spinner>) convertView.getTag(R.id.sm_banks);
		}

		SenderModel model = getItem(position);

		h1.get().setText(model.getSender());
		SimpleListItem1Adapter banksAdapter = new SimpleListItem1Adapter(context, model.getBanks());
		h2.get().setAdapter(banksAdapter);

		h2.get().setSelection(model.getBanks().indexOf(model.getSelectedBank()));

		final ImageView addImage = (ImageView) convertView.findViewById(R.id.sm_add);
		final ImageView remImage = (ImageView) convertView.findViewById(R.id.sm_remove);

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

		return convertView;
	}

	private void clearMargins(View view) {
		if (view != null && view.getLayoutParams() != null
			&& view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
			p.setMargins(7, 0, 0, 0);
			view.requestLayout();
		}
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
