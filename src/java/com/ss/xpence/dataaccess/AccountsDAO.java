package com.ss.xpence.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ss.xpence.dataaccess.base.AbstractDAO;
import com.ss.xpence.model.AccountModel;
import com.ss.xpence.model.AccountModel.CardModel;
import com.ss.xpence.util.IOUtils;

public class AccountsDAO extends AbstractDAO<AccountModel> {
	private CardsDAO cardsDAO = new CardsDAO();

	@Override
	public void delete(Context context, String id) {
		super.delete(context, id);

		// Now delete associated cards as well
		cardsDAO.delete(context, CardModel.create(Long.parseLong(id)));
	}

	public List<AccountModel> queryAll(Context context) {
		init(context);

		Cursor c = database.query(getTableName(), null, null, null, null, null, null);

		List<AccountModel> response = new ArrayList<AccountModel>();

		try {
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {
					AccountModel model = new AccountModel();
					model.setAccountName(c.getString(c.getColumnIndexOrThrow("account_name")));
					model.setBankName(c.getString(c.getColumnIndexOrThrow("bank_name")));
					model.setAccountNumber(c.getString(c.getColumnIndexOrThrow("account_number")));
					model.setAccountId(c.getInt(c.getColumnIndexOrThrow("_id")));

					List<CardModel> allCards = cardsDAO.queryAll(context);
					for (CardModel card : allCards) {
						if (card.getAccountId() == model.getAccountId()) {
							model.getCardModels().add(card);
						}
					}

					response.add(model);

					c.moveToNext();
				}
			}
		} finally {
			IOUtils.closeQuietly(c);
		}

		return response;
	}

	@Override
	protected String getTableName() {
		return "accounts";
	}

	@Override
	protected ContentValues modelToContentValues(AccountModel model) {
		ContentValues contentValues = new ContentValues();

		contentValues.put("account_name", model.getAccountName());
		contentValues.put("bank_name", model.getBankName());
		contentValues.put("account_number", model.getAccountNumber());

		return contentValues;
	}

	@Override
	public long insert(Context context, AccountModel model) {
		long id = super.insert(context, model);

		// Make a fresh insert of all cards
		// TODO: PERFORMANCE improvement, can be done by checking which all
		// cards have been deleted/edited/added
		// cardsDAO.delete(context, CardModel.create(model.getAccountId()));

		for (CardModel cmodel : model.getCardModels()) {
			cmodel.setAccountId(id);
			cardsDAO.insert(context, cmodel);
		}

		return id;
	}

	private static class CardsDAO extends AbstractDAO<CardModel> {

		@Override
		protected String getTableName() {
			return "accounts_cards";
		}

		public List<CardModel> queryAll(Context context) {
			init(context);

			Cursor c = database.query(getTableName(), null, null, null, null, null, null);

			List<CardModel> response = new ArrayList<CardModel>();

			try {
				if (c.moveToFirst()) {
					for (int i = 0; i < c.getCount(); i++) {
						CardModel model = new CardModel();
						model.setCardNumber(c.getString(c.getColumnIndexOrThrow("card_number")));
						model.setAccountId(c.getInt(c.getColumnIndexOrThrow("account_id")));
						model.setId(c.getInt(c.getColumnIndexOrThrow("_id")));

						response.add(model);

						c.moveToNext();
					}
				}
			} finally {
				IOUtils.closeQuietly(c);
			}

			return response;
		}

		@Override
		protected ContentValues modelToContentValues(CardModel model) {
			ContentValues contentValues = new ContentValues();

			contentValues.put("card_number", model.getCardNumber());
			contentValues.put("account_id", model.getAccountId());

			return contentValues;
		}

		public void delete(Context context, CardModel model) {
			init(context);

			database.beginTransaction();

			try {
				database.delete(getTableName(), "_id = ?", new String[] { String.valueOf(model.getAccountId()) });

				database.setTransactionSuccessful();
			} finally {
				database.endTransaction();
			}
		}

	}

}
