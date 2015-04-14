package com.sec.webs.classification.util;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DomTool {
	private static DomTool domTool;
	private static int count = 0;

	private DomTool() {

	}

	public static DomTool getInstance() {
		if (domTool == null)
			return new DomTool();
		else
			return domTool;
	}

	public int nodeCount(Document doc) {
		count = 0;
		treeWalk(doc.getRootElement());
		count++;
		return count;
	}

	public static void printDoc(Element e) {
		System.out.print(e.getName()+"(");
		for (Iterator i = e.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			System.out.print(element.getName() + "(");
			printDoc(element);
			System.out.print(")");
		}
		System.out.print(")");
	}

	@SuppressWarnings("rawtypes")
	public static void treeWalk(Element e) {

		for (Iterator i = e.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			count++;
			treeWalk(element);
		}
	}

	public static void main(String[] args) throws DocumentException {
		SAXReader reader = new SAXReader();

		Document document = reader.read(new File("F:/workspace_new/GUIWS/src/school.xml"));

		DomTool.getInstance().printDoc(document.getRootElement());
		System.out.println(count);
	}
}
