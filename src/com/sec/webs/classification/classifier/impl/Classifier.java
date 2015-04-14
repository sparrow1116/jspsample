package com.sec.webs.classification.classifier.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import com.sec.webs.classification.entity.JobInfo;
import com.sec.webs.classification.match.Matching;
import com.sec.webs.classification.match.impl.SimpleTreeMatching;
import com.sec.webs.classification.parser.Parser;
import com.sec.webs.classification.parser.impl.ParserImpl;
import com.sec.webs.classification.util.FileTool;
import com.sec.webs.classification.util.MysqlTool;
import com.sec.webs.engine.EngineEntry;
import com.sec.webs.gui.ReportDBManager;
import com.sec.webs.util.PropertyUtil;

public class Classifier {
	private File[] files;
	private Parser parser;
	private Matching matching;
	private Document doc;
	private MysqlTool mysqlTool;
	private ReportDBManager manager;

	private static final double THRESHOLD = PropertyUtil
			.getDouble("classification.matching.threshold");

	private static Log LOG = LogFactory.getLog(Classifier.class);

	public Classifier() {
		parser = new ParserImpl();
		matching = new SimpleTreeMatching();
		mysqlTool = MysqlTool.getInstance();
		manager = new ReportDBManager();
	}

	// id 要明确
	// 日志规范，通过配置实现
	// 删除不必要的注释
	// 返回状态码
	public boolean classifier(int id) {
		long startTime = System.currentTimeMillis();

		LOG.debug("[Classification] Start classification id: " + id);
		JobInfo jobInfo = mysqlTool.getJobinfo(id);

		String inputPath = jobInfo.getInputpath();
		inputPath = inputPath.trim();
		String[] array = inputPath.split("/");

		String fileName = array[array.length - 1];
		String fileDir = inputPath.substring(0, inputPath.indexOf(fileName));

		String xPath = jobInfo.getCwrapper();

		String tableName = mysqlTool.fnv1Hash32(fileName + xPath) + "";

		boolean isExsit = mysqlTool.createTable(tableName);
		if (isExsit) {
			int total = mysqlTool.querySimilarSize(tableName, 0);
			int similar = mysqlTool.querySimilarSize(tableName, THRESHOLD);
			int dissimilar = total - similar;
			String info = "This classification task result has been exsited|"
					+ "Total pages size: " + total + "|"
					+ "Similar pages size: " + similar + "|"
					+ "Dissimilar pages size: " + dissimilar + "|"
					+ "Time consuming: 0ms";
			LOG.debug(info);
			manager.updateCProcess(id, 100, info);
			EngineEntry.startEngine(id, tableName, THRESHOLD);
			return true;
		} else {
			files = FileTool.readFiles(fileDir);
			if (null == files) {
				return false;
			} else {
				manager.updateCProcess(id, 10, "");
			}

			File page = new File(inputPath);

			if (page.exists()) {
				doc = parser.parseXpath(page, xPath);
			} else {
				return false;
			}
			// 语句块之间空一行
			if (null == doc) {
				return false;
			} else {
				List<File> fileList = arrayToList(files);
				List<Document> docList = parser.parseXpathBatch(fileList,
						xPath, id);

				Map<String, Double> map = new HashMap<String, Double>();
				// null判断

				int docSize = docList.size();
				int increment = docSize / 4;
				int count = 0;
				int i = 1;

				for (Document doc2 : docList) {
					if (count >= (increment * i)) {
						manager.updateCProcess(id, 50 + 10 * i, "");
						i++;
					}
					double result = matching.matching(doc, doc2);
					String fName = doc2.getName();
					map.put(fName, result);

					count++;
				}

				mysqlTool.insertResult(tableName, map);

				int total = docList.size();
				int similar = mysqlTool.querySimilarSize(tableName, THRESHOLD);
				int dissimilar = total - similar;
				long endTime = System.currentTimeMillis();
				long timeCosuming = (endTime - startTime);

				String info = "The classification process was finished|"
						+ "Total pages size: " + total + "|"
						+ "Similar pages size: " + similar + "|"
						+ "Dissimilar pages size: " + dissimilar + "|"
						+ "Time consuming: " + timeCosuming + "ms";
				LOG.debug(info);
				manager.updateCProcess(id, 100, info);

				EngineEntry.startEngine(id, tableName, THRESHOLD);
			}
		}
		return true;
	}

	private List<File> arrayToList(File[] files) {
		if (null == files) {
			return null;
		} else {
			List<File> fileList = new ArrayList<File>();
			for (File file : files) {
				fileList.add(file);
			}

			return fileList;
		}
	}

	public static void main(String[] args) {
		new Classifier().classifier(100);
	}
}
