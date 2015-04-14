package com.sec.webs.classification.entity;

/**
 * 
 * @ClassName: DBEntity
 * @Description: TODO
 * @author Comsys-qianwei
 * @date Dec 11, 2014 11:07:02 AM
 * 
 */
public class DBEntity {
	private String driver = null;
	// the host for RDB
	private String host = null;
	// the user for RDB
	private String user = null;
	// the password for RDB
	private String password = null;
	// the database for RDB
	private String database = null;
	// the dbport for RDB
	private String port = null;
	// the minimum connection number
	private int minConNum = -1;
	// the maximum connection number
	private int maxConNum = -1;
	// the increase connection number
	private int increaseConNum = -1;

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getMinConNum() {
		return minConNum;
	}

	public void setMinConNum(int minConNum) {
		this.minConNum = minConNum;
	}

	public int getMaxConNum() {
		return maxConNum;
	}

	public void setMaxConNum(int maxConNum) {
		this.maxConNum = maxConNum;
	}

	public int getIncreaseConNum() {
		return increaseConNum;
	}

	public void setIncreaseConNum(int increaseConNum) {
		this.increaseConNum = increaseConNum;
	}
}
