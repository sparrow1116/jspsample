package com.sec.webs.classification.parser;

import java.io.File;
import java.util.List;

import org.dom4j.Document;

public interface Parser {
	public List<Document> parseXpathBatch(List<File> fileList, String xPath, int id);
	
	public Document parseXpath(File file, String xPath);
}
