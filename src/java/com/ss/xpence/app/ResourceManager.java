package com.ss.xpence.app;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.ss.xpence.dataaccess.AccountsDAO;
import com.ss.xpence.dataaccess.MongoConnector;
import com.ss.xpence.dataaccess.PreferencesDAO;
import com.ss.xpence.dataaccess.SendersDAO;
import com.ss.xpence.dataaccess.SettingsDAO;
import com.ss.xpence.dataaccess.TransactionsDAO;
import com.ss.xpence.exception.ResourceException;

public class ResourceManager {

	private static Map<Class<?>, Object> resources = new HashMap<Class<?>, Object>();

	static {
		resources.put(AccountsDAO.class, new AccountsDAO());
		resources.put(PreferencesDAO.class, new PreferencesDAO());
		resources.put(SendersDAO.class, new SendersDAO());
		resources.put(SettingsDAO.class, new SettingsDAO());
		resources.put(TransactionsDAO.class, new TransactionsDAO());
		resources.put(MongoConnector.class, new MongoConnector());
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) throws ResourceException {
		if (resources.containsKey(clazz)) {
			return (T) resources.get(clazz);
		}

		T instance;

		try {
			instance = clazz.getConstructor(new Class[] {}).newInstance(new Object[] {});
			resources.put(clazz, instance);
		} catch (IllegalArgumentException e) {
			throw new ResourceException("Unable to create instance of type " + clazz.getName(), e);
		} catch (InstantiationException e) {
			throw new ResourceException("Unable to create instance of type " + clazz.getName(), e);
		} catch (IllegalAccessException e) {
			throw new ResourceException("Unable to create instance of type " + clazz.getName(), e);
		} catch (InvocationTargetException e) {
			throw new ResourceException("Unable to create instance of type " + clazz.getName(), e);
		} catch (NoSuchMethodException e) {
			throw new ResourceException("Unable to create instance of type " + clazz.getName(), e);
		}

		return instance;
	}

}
