package com.sec.webs.engine;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sec.webs.gui.ReportDBManager;

public class WrapperParser {
	private static Log LOG = LogFactory.getLog(WrapperParser.class);

	public static void main(String[] args) {
		wrapperParse(11, "-4892662532506196667", 0.5);
	}

	public static void wrapperParse(int taskID, String cTableName,
			double threshold) {

		String jobid = Integer.toString(taskID);
		ReportDBManager db = new ReportDBManager();

		String swrapper = db.getjobswrapper(jobid);
		LOG.debug("[scraper engine] swrapper: " + swrapper);

		String[] wrapperMap = parseString(swrapper, ";");

		LinkedHashMap<String, String> wrapper = new LinkedHashMap<String, String>();

		for (String str : wrapperMap) {
			String[] el = parseString(str, ":");
			if (el.length > 1)
				wrapper.put(el[0], el[1]);
			else
				LOG.debug("[scraper engine]invalid swrapper: " + str);
		}

		// get classification pages path
		String fullPath = new String();
		String inputpath = new String();

		fullPath = db.getinputpath(jobid);
		inputpath = parseInputpath(fullPath);
		LOG.debug("[scraper engine] inputpath: " + inputpath);

		// String[] Path = getFileList(rootPath + dataSource);

		ArrayList<String> Path = db.getClassificationFiles(cTableName,
				threshold);
		LOG.debug("[scraper engine] Get classification " + Path.size()
				+ " pages");

		wrapper.put("fileName", "");
		Set<String> key = wrapper.keySet();
		Object[] set = key.toArray();

		// create output folder
		String outputFolder = inputpath + "output/";
		OutputGenerator.createOutputFile(outputFolder);

		String outputPath = new String();
		outputPath = outputFolder + getCurrentTime(true) + "_result.csv";

		int total = Path.size();
		for (int n = 0; n < Path.size(); n++) {

			String filePath = inputpath + Path.get(n);
			LOG.debug("[scraper engine] Parsed file path " + filePath + " - "
					+ n);

			File input = new File(filePath);
			org.jsoup.nodes.Document doc;
			try {
				doc = (org.jsoup.nodes.Document) Jsoup.parse(input, "UTF-8",
						"http://www.imdb.com/");

				String html = null;
				html = doc.html();

				HtmlCleaner hc = new HtmlCleaner();
				TagNode tn = hc.clean(html);

				Document dom = (Document) new DomSerializer(
						new CleanerProperties()).createDOM(tn);
				XPath xPath = XPathFactory.newInstance().newXPath();

				String[] result = new String[key.size()];

				Object re;

				for (int i = 0; i < key.size(); i++) {
					if (i != (key.size() - 1)) {
						re = xPath.evaluate(wrapper.get(set[i]), dom,
								XPathConstants.NODESET);
						if (re instanceof NodeList) {

							NodeList nodeList = (NodeList) re;
							StringBuffer sb = new StringBuffer();
							int size = nodeList.getLength();

							for (int t = 0; t < size; t++) {
								Node node = nodeList.item(t);
								sb.append(node.getNodeValue() == null ? node
										.getTextContent() : node.getNodeValue());
								if (size > 1 && t != size - 1)
									sb.append(" | ");
							}
							result[i] = sb.toString();
						}
					} else {
						result[i] = Path.get(n);
					}
				}

				boolean writeCsv = false;
				writeCsv = OutputGenerator.writeResultToFile(outputPath, key,
						result);
				if (writeCsv)
					LOG.debug("[scraper engine] write the parsed data from "
							+ filePath + " to csv file" + " - " + n);

				int tmp = 100 * (n + 1) / total;
				int sprocess = getSProcess(tmp);

				if (db.updateSProcess(taskID, getSProcess(tmp)))
					LOG.info("[scraper engine] updateSPRocess: " + sprocess);
			} catch (IOException e) {
				LOG.debug("[scraper engine] Can not find the file specified: "
						+ filePath);
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				LOG.debug("[scraper engine] TcreateDOM occur exception");
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				LOG.debug("[scraper engine] xPath.evaluate: expression cannot be evaluated.");
				e.printStackTrace();
			}

		}
		LOG.debug("[scraper engine] parsed data save in : " + outputPath);

		// update task status
		boolean re = db.modifyJobStatus(jobid, outputPath, "finished");

		if (re)
			LOG.debug("[scraper engine] update jobinfo table: success");
		else
			LOG.debug("[scraper engine] update jobinfo table: fail");

	}

	public static void wrapperParse(UserProfile profile, String outPath) {

		String[] Path = getFileList(profile.getDataSource());
		LinkedHashMap<String, String> wrapper = profile.getWrapper();

		Set<String> key = wrapper.keySet();
		Object[] set = key.toArray();

		for (int n = 0; n < Path.length; n++) {
			File input = new File(Path[n]);
			try {
				org.jsoup.nodes.Document doc = (org.jsoup.nodes.Document) Jsoup
						.parse(input, "UTF-8", "http://www.imdb.com/");
				String html = null;
				html = doc.html();

				HtmlCleaner hc = new HtmlCleaner();
				TagNode tn = hc.clean(html);

				Document dom = (Document) new DomSerializer(
						new CleanerProperties()).createDOM(tn);
				XPath xPath = XPathFactory.newInstance().newXPath();

				String[] result = new String[key.size()];

				for (int i = 0; i < key.size(); i++) {
					if (i != (key.size() - 1))
						result[i] = xPath.evaluate(wrapper.get(set[i]), dom,
								XPathConstants.STRING).toString();
					else
						result[i] = Path[n];
				}
				OutputGenerator.writeResultToFile(outPath, key, result);
				System.out.println("extracted data save in line: " + (n + 1));
			} catch (IOException e) {
				LOG.debug("The system cannot find the file specified");
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				LOG.debug("TcreateDOM occur exception");
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}

	}

	protected static String[] getFileList(String dataSource) {
		File dir = new File(dataSource);
		File[] files = dir.listFiles();
		if (files == null)
			return null;
		String[] Path = new String[files.length];

		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory())
				Path[i] = dataSource + "/" + files[i].getName();
		}
		return Path;
	}

	protected static String[] parseString(String swrapper, String sep) {
		String[] wrapper = swrapper.split(sep);
		return wrapper;
	}

	// parse inputpath
	protected static String parseInputpath(String inputpath) {
		String[] arr = inputpath.split("/");
		String path = new String();
		for (int i = 0; i < arr.length - 1; i++) {
			path = path + arr[i] + "/";
		}
		return path;
	}

	// Timestamp Generator
	public static String getCurrentTime(boolean includeTime) {
		Calendar today = Calendar.getInstance();
		DecimalFormat decimalFormat = new DecimalFormat("00");
		int year = today.get(Calendar.YEAR) % 100;
		int month = today.get(Calendar.MONTH) + 1;
		int date = today.get(Calendar.DATE);
		String ret = decimalFormat.format(year) + decimalFormat.format(month)
				+ decimalFormat.format(date);

		if (includeTime) {
			int hour = today.get(Calendar.HOUR_OF_DAY);
			int min = today.get(Calendar.MINUTE);
			int sec = today.get(Calendar.SECOND);
			ret += "_" + decimalFormat.format(hour) + decimalFormat.format(min)
					+ decimalFormat.format(sec);
		}

		return ret;
	}

	public static int getSProcess(int sprocess) {
		if (sprocess >= 10) {
			if (10 <= sprocess && sprocess < 20) {
				sprocess = 10;
			} else if (20 <= sprocess && sprocess < 30) {
				sprocess = 20;
			} else if (30 <= sprocess && sprocess < 40) {
				sprocess = 30;
			} else if (40 <= sprocess && sprocess < 50) {
				sprocess = 40;
			} else if (50 <= sprocess && sprocess < 60) {
				sprocess = 50;
			} else if (60 <= sprocess && sprocess < 70) {
				sprocess = 60;
			} else if (70 <= sprocess && sprocess < 80) {
				sprocess = 70;
			} else if (80 <= sprocess && sprocess < 90) {
				sprocess = 80;
			} else if (90 <= sprocess && sprocess < 100) {
				sprocess = 90;
			} else
				sprocess = 100;
		}
		return sprocess;
	}
}
