package com.sec.webs.classification.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.sec.webs.classification.entity.JobInfo;
import com.sec.webs.util.PropertyUtil;

public class MysqlTool {
	private static MysqlTool mysqlTool;
	private static Connection conn;
	private static Log LOG = LogFactory.getLog(MysqlTool.class);

	private MysqlTool() {
		String driverName = "com.mysql.jdbc.Driver"; // 驱动名称
		String DBUser = PropertyUtil.getString("db.user"); // mysql用户名
		String DBPasswd = PropertyUtil.getString("db.password"); // mysql密码
		String DBName = PropertyUtil.getString("db.name"); // 数据库名
		String DBIp = PropertyUtil.getString("db.ip");

		String connUrl = "jdbc:mysql://" + DBIp + "/" + DBName + "?user="
				+ DBUser + "&password=" + DBPasswd;

		try {
			Class.forName(driverName).newInstance();
		} catch (InstantiationException e) {
			// InstantiationException
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// IllegalAccessException
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// ClassNotFoundException e
			e.printStackTrace();
		}
		try {
			System.out.println(connUrl);
			conn = (Connection) DriverManager.getConnection(connUrl);
		} catch (SQLException e) {
			// SQLException
			e.printStackTrace();
		}
	}

	public static MysqlTool getInstance() {
		if (mysqlTool == null)
			return new MysqlTool();
		else
			return mysqlTool;
	}

	public JobInfo getJobinfo(int id) {
		String sql = "select * from jobinfo where id = \"" + id + "\"";

		JobInfo jobInfo = new JobInfo();
		try {
			Statement stat = (Statement) conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				jobInfo.setId(rs.getInt("id"));
				jobInfo.setOutputpath(rs.getString("outputpath"));
				jobInfo.setInputpath(rs.getString("inputpath"));
				jobInfo.setJobname(rs.getString("jobname"));
				jobInfo.setCwrapper(rs.getString("cwrapper"));
				jobInfo.setDatasource(rs.getString("datasource"));
				jobInfo.setFinishtime(rs.getString("finishtime"));
				jobInfo.setStatus(rs.getString("status"));
				jobInfo.setSubmittime(rs.getString("submittime"));
				jobInfo.setSwrapper(rs.getString("swrapper"));
				jobInfo.setUsersrl(rs.getString("usersrl"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jobInfo;

	}

	public void insertResults() {

	}

	public boolean createTable(String tableName) {
		if (isTableExsit(tableName)) {
			LOG.debug("[Classification] Table: " + tableName + "has exsited!");
			return true;
		} else {
			String sql = "CREATE TABLE`"
					+ tableName
					+ "` (`file` VARCHAR(200) NULL,`similarity` DOUBLE NULL) COLLATE='utf8_general_ci' ENGINE=MyISAM;";

			try {
				Statement st = (Statement) conn.createStatement();
				st.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

	}

	public boolean isTableExsit(String tableName) {
		boolean flag = false;
		String sql = "show tables";
		try {
			Statement st = (Statement) conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString(1);
				if (name.equals(tableName)) {
					flag = true;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	public void insertResult(String tableName, Map<String, Double> map) {
		String sql = "INSERT INTO `web`.`" + tableName
				+ "` (`file`, `similarity`) VALUES (?,?);";

		try {
			PreparedStatement pstt = (PreparedStatement) conn
					.prepareStatement(sql);
			for (Entry<String, Double> entry : map.entrySet()) {
				pstt.setString(1, entry.getKey());
				pstt.setDouble(2, entry.getValue());

				pstt.addBatch();
			}
			pstt.executeBatch();

			System.out.println("[Classification: ] insert over");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int querySimilarSize(String tableName, double threhold) {
		int size = 0;
		String sql = "select count(*) from `" + tableName
				+ "` where similarity > " + threhold;
		System.out.println(sql);
		Statement st;
		try {
			st = (Statement) conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				size = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return size;
	}

	public long fnv1Hash32(String str) {
		long prime = 16777619;
		// 32bit offset basis
		long offsetBasis = 2166136261L;
		long hash = offsetBasis;
		for (byte b : str.getBytes()) {
			hash ^= b;
			hash *= prime;
		}
		return hash;
	}

	public static void main(String[] args) {
		// String str = MysqlTool.getInstance().getJobinfo(id).getFinishtime();

		System.out.println(MysqlTool.getInstance().querySimilarSize(
				"-3601166397021810144", 0));
		// System.out.println(str);
	}
}
