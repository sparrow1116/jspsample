package com.sec.webs.gui;

import java.io.*;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.json.simple.*;

import com.sec.webs.gui.ReportDBManager;

public class mainPageServer {

	public String LoadHTMLByUrl(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String html = "";
		String filePath = "";
		String url = request.getParameter("url");
		if (url != null && url != "") {

			URL domain;
			String doMainStr = "";
			PrintWriter pw = response.getWriter();

			domain = new URL(url);
			ReportDBManager rdbMObj = new ReportDBManager();
			filePath = rdbMObj.getFilePathByUrl(url);
			if (filePath != null && filePath.length() > 0) {

				System.out.print("filePath:" + filePath);
				BufferedReader br = null;
				StringBuffer sb = new StringBuffer();

				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(filePath), "utf-8"));
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sb.append(temp);
				}

				html = sb.toString();
			} else {
				Connection _conn = Jsoup.connect(url);
				_conn.timeout(10000);
				Document doc2 = _conn.get();
				html = doc2.html().toString();
			}
			System.out.print("HTML:" + html);
			String[] htmlList = html.split("<head");

			System.out.print("htmlList:" + htmlList.length);
			String jq = "<script id='guiws_jq' src='/js/jqueryForIframe.js'></script>";
			String xpath = "<script id='guiws_xpath' src='/js/xpath.js'></script>";
			String dom = "<script src='/js/contentForIframe.js'></script>";
			html = htmlList[0] + jq + xpath + dom + "<base href='"
					+ domain.getProtocol() + "://" + doMainStr + "'/>"
					+ "<head" + htmlList[1];
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("HTML", html);
			jsonObject.put("filePath", filePath);
			System.out.print("filePath:" + filePath);

			pw.flush();
			pw.close();
			return jsonObject.toJSONString();
		}
		return "";
	}
}
