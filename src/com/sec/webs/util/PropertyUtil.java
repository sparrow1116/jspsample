package com.sec.webs.util;

import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertyUtil {
	private static Configuration mConfiguration;

	private static void init() {
		try {
			mConfiguration = new PropertiesConfiguration(
					"./conf/config.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getString(String key) {
		init();
		return mConfiguration.getString(key);
	}

	public static int getInt(String key) {
		init();
		return mConfiguration.getInt(key);
	}

	public static double getDouble(String key) {
		init();
		return mConfiguration.getDouble(key);
	}

	public static long getLong(String key) {
		init();
		return mConfiguration.getLong(key);
	}

	public static boolean getBoolean(String key) {
		init();
		return mConfiguration.getBoolean(key);
	}

	public static List<Object> getList(String key) {
		init();
		return mConfiguration.getList(key);
	}

	public static byte getByte(String key) {
		init();
		return mConfiguration.getByte(key);
	}

	public static Object getProperty(String key) {
		init();
		return mConfiguration.getProperty(key);
	}
}
