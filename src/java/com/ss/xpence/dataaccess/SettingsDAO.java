package com.ss.xpence.dataaccess;

import android.content.ContentValues;

import com.ss.xpence.dataaccess.base.AbstractDAO;
import com.ss.xpence.model.SettingModel;

public class SettingsDAO extends AbstractDAO<SettingModel> {

	private final String TABLE_NAME = "settings";

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected ContentValues modelToContentValues(SettingModel model) {
		return null;
	}

}
