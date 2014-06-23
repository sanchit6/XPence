package com.ss.xpence.dataaccess;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

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

	@Override
	public List<SettingModel> queryAll(Context context) {
		return null;
	}

}
