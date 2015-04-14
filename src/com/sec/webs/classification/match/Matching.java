package com.sec.webs.classification.match;

import org.dom4j.Document;

public interface Matching {
	double matching(Document doc1, Document doc2);
}
