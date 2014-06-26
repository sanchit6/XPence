package com.ss.xpence.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

import com.ss.xpence.AccountsView;
import com.ss.xpence.dataaccess.AccountsDAO;

public class DeleteAccountConfirmDialog extends DialogFragment {

	private String message;
	private String finalMessage;
	private CharSequence itemId;
	private AccountsDAO accountsDAO;

	public DeleteAccountConfirmDialog(AccountsDAO accountsDAO, CharSequence itemName, CharSequence itemId) {
		this.message = "Confirm Deletion of " + itemName + "?";
		this.finalMessage = "Deleted Account: " + itemName;
		this.itemId = itemId;
		this.accountsDAO = accountsDAO;
	}

	public DeleteAccountConfirmDialog(AccountsDAO accountsDAO, String itemName, long itemId) {
		this(accountsDAO, itemName, String.valueOf(itemId));
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("MESSAGE").setMessage(message).setPositiveButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				accountsDAO.delete(getActivity().getBaseContext(), String.valueOf(itemId));

				Toast.makeText(getActivity().getBaseContext(), finalMessage, Toast.LENGTH_SHORT).show();

				((AccountsView) getActivity()).refreshList();
			}
		}).setNegativeButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		return builder.create();
	}
}
