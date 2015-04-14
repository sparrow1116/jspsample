package com.sec.webs.classification.parser.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.sec.webs.classification.match.impl.SimpleTreeMatching;
import com.sec.webs.classification.parser.Parser;
import com.sec.webs.gui.ReportDBManager;

public class ParserImpl implements Parser {
	private static final Log LOG = LogFactory.getLog(ParserImpl.class);

	private HtmlCleaner cleaner;
	private ReportDBManager manager = new ReportDBManager();

	public ParserImpl() {
		cleaner = new HtmlCleaner();
	}

	public List<Document> parseXpathBatch(List<File> fileList, String xPath,
			int id) {
		List<Document> docList = new ArrayList<Document>();
		int fSize = fileList.size();
		int increment = fSize / 4;
		int count = 0;
		int i = 1;

		for (File file : fileList) {
			if (count >= (increment * i)) {
				manager.updateCProcess(id, 10 + 10 * i, "");
				i++;
			}
			Document d = parseXpath(file, xPath);
			if (d != null) {
				docList.add(d);
			}
			count++;
		}
		return docList;
	}

	public Document parseXpath(File file, String xPath) {
		Document doc = null;
		if (xPath.startsWith("html/")) {
			xPath = xPath.substring("html/".length());
		}

		TagNode tagNode = null;
		Object[] objs = null;
		try {
			tagNode = clean(file);
			if (null == tagNode) {
				return doc;
			} else {
				objs = tagNode.evaluateXPath(xPath);
			}
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
		}
		if ((objs == null) || (objs.length == 0)) {
			return doc;
		} else {
			tagNode = (TagNode) objs[0];
			doc = toXml(null, tagNode);

			doc.setName(file.getName());
		}

		return doc;
	}

	private Document toXml(Element parent, TagNode tagNode) {
		Document document = null;
		Element e;
		if (parent == null) {
			document = DocumentHelper.createDocument();

			e = document.addElement(tagNode.getName());
		} else {
			e = parent.addElement(tagNode.getName());
		}
		if (tagNode.getChildTagList().size() != 0) {
			List<TagNode> tagNodeList = tagNode.getChildTagList();
			for (TagNode tNode : tagNodeList) {
				toXml(e, tNode);
			}
		}
		return document;
	}

	private TagNode clean(File file) {
		TagNode tagNode = null;
		try {
			tagNode = cleaner.clean(file, "UTF8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return tagNode;
	}

	public static void main(String[] args) {
		File file1 = new File(
				"E:/SamplePages/www.imdb.com/html1/file_00bbefd44535449c043226495dd3ec9b.html");
		String xPath = "body/div[1]/div/div[2]/div[1]/div[1]/div/div/table/tbody/tr[1]/td[2]/h1/span[1]";

		Document doc1 = new ParserImpl().parseXpath(file1, xPath);

		File file = new File("E:/SamplePages/www.imdb.com/html_gui/html1/");

		File[] files = file.listFiles();
		int i = 0;
		for (File f : files) {

			Document doc2 = new ParserImpl().parseXpath(f, xPath);
			double result = new SimpleTreeMatching().matching(doc1, doc2);

			if (result != 0.0)
				System.out.println(result);

		}

	}
}
