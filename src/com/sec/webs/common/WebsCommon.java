package com.sec.webs.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.sec.webs.util.PropertyUtil;

public class WebsCommon {

	String databaseId = null;
	boolean isDBCP = false;
	boolean debugMode = false;

	private HashMap<String, DataSource> dataSourceMap = null;

	public WebsCommon() {
		dataSourceMap = new HashMap<String, DataSource>();
	}

	public void setConnectInfo(String databaseId) {
		setConnectInfo(databaseId, false);
	}

	public void setConnectInfo(String databaseId, boolean isDBCP) {
		this.databaseId = databaseId;
		this.isDBCP = isDBCP;
	}

	public Connection getConnection() throws SQLException,
			ClassNotFoundException {
		/*
		 * if (databaseId == null) { databaseId = HtmsConfig.getInstance().get(
		 * "htms.default.database.id"); }
		 */

		return getConnection(databaseId);
	}

	private Connection getConnection(String id) throws SQLException,
			ClassNotFoundException {

		return getDirectConnection(id);

	}

	private Connection getDirectConnection(String id) throws SQLException,
			ClassNotFoundException {

		String ip = PropertyUtil.getString("db.ip");
		String port = PropertyUtil.getString("db.port");
		String user = PropertyUtil.getString("db.user");
		String password = PropertyUtil.getString("db.password");
		String DBName = PropertyUtil.getString("db.name"); // 数据库名

		String url = "jdbc:mysql://" + ip + ":" + port + "/" + DBName
				+ "?characterEncoding=utf-8";
		String driver = "com.mysql.jdbc.Driver";

		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	}

	public LinkedHashMap<String, String> getCodes(String manageCode) {
		return getCodes("Common", manageCode);
	}

	public LinkedHashMap<String, String> getCodes(String serviceName,
			String manageCode) {

		String strQuery = "";
		strQuery = "SELECT CodeKey, CodeLabel CodeValue FROM htms.codes WHERE ServiceName = ? AND ManageCode = ? AND UseYN = 'Y' ORDER BY CodeOrder ASC";

		ArrayList<String> bindNames = new ArrayList<String>();
		bindNames.add(serviceName);
		bindNames.add(manageCode);

		return getDataCodes(strQuery, bindNames);
	}

	public int getExecuteUpdateID(String strQuery) {
		ArrayList<String> bindNames = new ArrayList<String>();
		return executeUpdate(strQuery, bindNames, true);
	}

	public int getExecuteUpdateID(String strQuery, ArrayList<String> bindNames) {
		return executeUpdate(strQuery, bindNames, true);
	}

	public int executeUpdate(String strQuery) {
		ArrayList<String> bindNames = new ArrayList<String>();
		return executeUpdate(strQuery, bindNames, false);
	}

	public int executeUpdate(String strQuery, ArrayList<String> bindNames) {
		return executeUpdate(strQuery, bindNames, false);
	}

	private int executeUpdate(String strQuery, ArrayList<String> bindNames,
			boolean returnLastId) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			debugQuery(strQuery, bindNames);
			pstmt = conn.prepareStatement(strQuery);

			int ii = 1;
			for (int i = 0; i < bindNames.size(); i++) {
				String value = bindNames.get(i);
				pstmt.setString(ii++, value);
			}

			if (returnLastId) {
				if (pstmt.executeUpdate() > 0) {

					strQuery = "SELECT LAST_INSERT_ID() AS ID";
					// debugQuery();s
					pstmt = conn.prepareStatement(strQuery);

					rs = pstmt.executeQuery();
					rs.next();
					return rs.getInt("ID");
				} else {
					return 0;
				}
			} else {
				return pstmt.executeUpdate();
			}

		} catch (Exception ex) {
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException es) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException es) {
				}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException es) {
				}
		}

		return 0;
	}

	public LinkedHashMap<String, String> getData(String strQuery) {
		ArrayList<String> bindNames = new ArrayList<String>();
		return getData(strQuery, bindNames);
	}

	public LinkedHashMap<String, String> getData(String strQuery,
			ArrayList<String> bindNames) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		try {
			conn = getConnection();

			debugQuery(strQuery, bindNames);
			pstmt = conn.prepareStatement(strQuery);

			int ii = 1;
			for (int i = 0; i < bindNames.size(); i++) {
				String value = bindNames.get(i);
				pstmt.setString(ii++, value);
			}

			rs = pstmt.executeQuery();
			if (!rs.next()) {
				return data;
			}

			rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {

				if (rs.getString(i) == null) {
					data.put(rsmd.getColumnLabel(i), "");
				} else {
					data.put(rsmd.getColumnLabel(i), rs.getString(i));
				}
			}

		} catch (Exception ex) {
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException es) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException es) {
				}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException es) {
				}
		}

		return data;
	}

	public String getDataOfOneField(String strQuery) {
		ArrayList<String> bindNames = new ArrayList<String>();
		return getDataOfOneField(strQuery, bindNames);
	}

	public String getDataOfOneField(String strQuery, ArrayList<String> bindNames) {
		String data = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			debugQuery(strQuery, bindNames);
			pstmt = conn.prepareStatement(strQuery);

			int ii = 1;
			for (int i = 0; i < bindNames.size(); i++) {
				String value = bindNames.get(i);
				pstmt.setString(ii++, value);
			}

			rs = pstmt.executeQuery();
			if (!rs.next()) {
				return data;
			}

			data = rs.getString(1);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException es) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException es) {
				}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException es) {
				}
		}

		return data;
	}

	public ArrayList<LinkedHashMap<String, String>> getDataList(String strQuery) {
		ArrayList<String> bindNames = new ArrayList<String>();
		return getDataList(strQuery, bindNames);
	}

	public ArrayList<LinkedHashMap<String, String>> getDataList(
			String strQuery, ArrayList<String> bindNames, int totalCount) {

		ArrayList<LinkedHashMap<String, String>> data = getDataList(strQuery,
				bindNames);

		if (totalCount == 0) {
			totalCount = data.size();
		}

		if (totalCount < 0) {
			return data;
		}

		for (int i = 0; i < data.size(); i++) {
			data.get(i).put("OrderIndex", Integer.toString(totalCount--));
		}

		return data;
	}

	public ArrayList<LinkedHashMap<String, String>> getDataList(
			String strQuery, ArrayList<String> bindNames) {
		ArrayList<LinkedHashMap<String, String>> data = new ArrayList<LinkedHashMap<String, String>>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		try {
			conn = getConnection();

			debugQuery(strQuery, bindNames);

			pstmt = conn.prepareStatement(strQuery);

			int ii = 1;
			for (int i = 0; i < bindNames.size(); i++) {
				String value = bindNames.get(i);
				pstmt.setString(ii++, value);
			}

			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			while (rs.next()) {
				LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {

					if (rs.getString(i) == null) {
						row.put(rsmd.getColumnLabel(i), "");
					} else {
						row.put(rsmd.getColumnLabel(i), rs.getString(i));
					}
				}
				data.add(row);
			}

		} catch (Exception ex) {
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException es) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException es) {
				}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException es) {
				}
		}

		return data;
	}

	public ArrayList<String> getDataListOfOneField(String strQuery) {
		ArrayList<String> bindNames = new ArrayList<String>();
		return getDataListOfOneField(strQuery, bindNames);
	}

	public ArrayList<String> getDataListOfOneField(String strQuery,
			ArrayList<String> bindNames) {
		ArrayList<String> data = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			debugQuery(strQuery, bindNames);

			pstmt = conn.prepareStatement(strQuery);

			int ii = 1;
			for (int i = 0; i < bindNames.size(); i++) {
				String value = bindNames.get(i);
				pstmt.setString(ii++, value);
			}

			rs = pstmt.executeQuery();
			while (rs.next()) {
				data.add(rs.getString(1));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException es) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException es) {
				}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException es) {
				}
		}

		return data;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> getDataHashList(
			String strQuery) {

		ArrayList<String> bindNames = new ArrayList<String>();
		return getDataHashList(strQuery, bindNames);
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> getDataHashList(
			String strQuery, ArrayList<String> bindNames) {

		LinkedHashMap<String, LinkedHashMap<String, String>> data = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		try {
			conn = getConnection();

			debugQuery(strQuery, bindNames);

			pstmt = conn.prepareStatement(strQuery);

			int ii = 1;
			for (int i = 0; i < bindNames.size(); i++) {
				String value = bindNames.get(i);
				pstmt.setString(ii++, value);
			}

			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			while (rs.next()) {
				LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {

					if (rs.getString(i) == null) {
						row.put(rsmd.getColumnLabel(i), "");
					} else {
						row.put(rsmd.getColumnLabel(i), rs.getString(i));
					}
				}
				data.put(rs.getString("CodeKey"), row);
			}

		} catch (Exception ex) {
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException es) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException es) {
				}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException es) {
				}
		}

		return data;
	}

	public LinkedHashMap<String, String> getDataCodes(String strQuery) {
		ArrayList<String> bindNames = new ArrayList<String>();
		return getDataCodes(strQuery, bindNames);
	}

	public LinkedHashMap<String, String> getDataCodes(String strQuery,
			ArrayList<String> bindNames) {

		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		try {
			conn = getConnection();

			debugQuery(strQuery, bindNames);

			pstmt = conn.prepareStatement(strQuery);

			int ii = 1;
			for (int i = 0; i < bindNames.size(); i++) {
				String value = bindNames.get(i);
				pstmt.setString(ii++, value);
			}

			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			while (rs.next()) {
				data.put(rs.getString("CodeKey"), rs.getString("CodeValue"));
			}

		} catch (Exception ex) {
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException es) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException es) {
				}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException es) {
				}
		}

		return data;
	}

	public LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>> getDataListCodes(
			String strQuery) {
		ArrayList<String> bindNames = new ArrayList<String>();
		return getDataListCodes(strQuery, bindNames);
	}

	public LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>> getDataListCodes(
			String strQuery, ArrayList<String> bindNames) {
		LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>> data = new LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		try {
			conn = getConnection();

			debugQuery(strQuery, bindNames);

			pstmt = conn.prepareStatement(strQuery);

			int ii = 1;
			for (int i = 0; i < bindNames.size(); i++) {
				String value = bindNames.get(i);
				pstmt.setString(ii++, value);
			}

			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			while (rs.next()) {
				LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {

					if (rs.getString(i) == null) {
						row.put(rsmd.getColumnLabel(i), "");
					} else {
						row.put(rsmd.getColumnLabel(i), rs.getString(i));
					}
				}

				String key = row.get("CodeKey");
				if (!data.containsKey(key)) {
					data.put(key,
							new ArrayList<LinkedHashMap<String, String>>());
				}
				data.get(key).add(row);
			}

		} catch (Exception ex) {
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException es) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException es) {
				}
			if (conn != null)
				try {
					conn.close();
					conn = null;
				} catch (SQLException es) {
				}
		}

		return data;
	}

	public boolean isExist(Map<String, String> param, String key) {
		if (param.containsKey(key) && param.get(key) != null
				&& param.get(key).toString().length() > 0) {
			return true;
		}

		return false;
	}

	public String getProjectGroupCode(int pid) {
		return getProjectGroupCode(Integer.toString(pid));
	}

	public String getProjectGroupCode(String pid) {
		return "Proj_" + pid;
	}

	public int getWeekCount(int fromYear, int fromMonth, int fromDay,
			int toYear, int toMonth, int toDay) {

		// 閻″灝顨硷拷?锟� CEIL((闂嗩喚濞嬮‖娑樼蹈椤繑绶ら悺鍨閸嬶拷+ 1 + 闂夋牗妫堝锟�/ 7)

		Calendar fromDate = Calendar.getInstance();
		fromDate.set(fromYear, fromMonth - 1, fromDay);

		Calendar toDate = Calendar.getInstance();
		toDate.set(toYear, toMonth - 1, toDay);

		Calendar startDate = Calendar.getInstance();
		startDate.set(fromDate.get(Calendar.YEAR), 0, 1);

		long diffSecond = (toDate.getTimeInMillis() - startDate
				.getTimeInMillis()) / 1000;
		long diffDay = (long) Math.floor(diffSecond / (60 * 60 * 24));
		long week = startDate.get(Calendar.DAY_OF_WEEK) - 1;
		int weekCount = (int) Math.ceil((diffDay + 1 + week) / 7);

		return weekCount;
	}

	private void debugQuery(String strQuery, ArrayList<String> bindNames) {
		String errorMsg = "";
		errorMsg += String.format("\n%s\n", strQuery);
		for (int i = 0; i < bindNames.size(); i++) {
			String value = bindNames.get(i);
			errorMsg += String.format("    param = %s\n", value);
		}
	}
}
