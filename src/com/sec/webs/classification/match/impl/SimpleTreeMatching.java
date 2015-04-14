package com.sec.webs.classification.match.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.sec.webs.classification.match.Matching;
import com.sec.webs.classification.parser.impl.ParserImpl;
import com.sec.webs.classification.util.DomTool;

public class SimpleTreeMatching implements Matching {
	private static final Log LOG = LogFactory.getLog(SimpleTreeMatching.class);
	private DomTool domTool = DomTool.getInstance();

	public double matching(Document doc1, Document doc2) {
		try {
			if ((null != doc1) && (null != doc2)) {
				Element root1 = doc1.getRootElement();
				Element root2 = doc2.getRootElement();

				int result = recursion(root1, root2);
				LOG.debug(result);

				int s1c = domTool.nodeCount(doc1);
				int s2c = domTool.nodeCount(doc2);

				double similarity = (result + 0.0d) / (s1c + s2c + 0.0d) * 2;

				return similarity;
			} else {
				LOG.debug("[Classification] Input document is null!");
				return 0.0;
			}

		} catch (Exception e) {
			return 0.0;
		}
	}

	@SuppressWarnings("unchecked")
	private int recursion(Element root1, Element root2) {
		if (!root1.getName().equals(root2.getName()))
			return 0;

		int m = root1.elements().size();
		int n = root2.elements().size();
		int M[][] = new int[m + 1][n + 1];
		int W[][] = new int[m + 1][n + 1];
		for (int i = 0; i <= m; i++)
			M[i][0] = 0;
		for (int j = 0; j <= n; j++)
			M[0][j] = 0;
		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++) {
				List<Element> list1 = root1.elements();
				List<Element> list2 = root2.elements();

				W[i][j] = recursion(list1.get(i - 1), list2.get(j - 1));
				M[i][j] = M[i][j - 1] > M[i - 1][j] ? (M[i][j - 1] > M[i - 1][j - 1]
						+ W[i][j] ? M[i][j - 1] : M[i - 1][j - 1] + W[i][j])
						: (M[i - 1][j] > M[i - 1][j - 1] + W[i][j] ? M[i - 1][j]
								: M[i - 1][j - 1] + W[i][j]);

			}
		return M[m][n] + 1;
	}

	public static void main(String[] args) {
		File file1 = new File(
				"E:/SamplePages/www.imdb.com/html/file_00ab83c295719fed44f08f91d0b854e2.html");
		File file2 = new File(
				"E:/SamplePages/www.imdb.com/html/file_00b092866384779b9190d9f347a90f80.html");


		/*Document doc1 = new ParserImpl().parseXpath(file1,
				"/body/div[1]/div/div[2]/div[1]/div[1]/div");
		Document doc2 = new ParserImpl().parseXpath(file2,
				"/body/div[1]/div/div[2]/div[1]/div[1]/div");*/
		
		Document doc1 = new ParserImpl().parseXpath(file1,
				"/body");
		Document doc2 = new ParserImpl().parseXpath(file2,
				"/body");

		double similarity = new SimpleTreeMatching().matching(doc1, doc2);
		System.out.println(similarity);

	}
}
