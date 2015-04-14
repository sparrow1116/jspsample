package com.sec.webs.classification.parser;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sec.webs.classification.util.Parameters;


public class XMLParser {
	public static Document getDocument(String fileName) {
		Document doc = null;

		if (null != fileName && fileName.length() > 0) {
			File file = new File(fileName);
			SAXReader reader = new SAXReader();
			// solve the bug: special characters.
			reader.setEncoding(Parameters.ENCODING);
			try {
				doc = reader.read(file);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return doc;
	}
	
	public static void getAllElements(Element element, List<Element> list) {
		if (null == element || null == list)
			return;

		List elements = element.elements();
		if (0 == elements.size()) {
			list.add(element);
		} else {
			for (Iterator it = elements.iterator(); it.hasNext();) {
				Element ele = (Element) it.next();
				getAllElements(ele, list);
			}
		}
	}
}
