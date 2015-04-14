

<%@page import="org.jsoup.select.Elements"%>
<%@page import="org.jsoup.nodes.Element"%>
<%@page import="com.sec.webs.gui.ReportDBManager"%>
<%@ page language="java" import="org.jsoup.*"%>
<%@ page language="java" import="org.jsoup.nodes.Document"%>
<%@ page language="java" import="java.io.*"%>
<%@ page language="java" import="java.net.URL"%>
<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>


<%
	String url = request.getParameter("url");
	String type = request.getParameter("type");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	String html = "";
	String filePath = "";
	URL domain;
	String doMainStr = "";
	PrintWriter pw = response.getWriter();
	JSONObject jsonObject = new JSONObject();
	String requestTimeSpan = "";

	if (url != null && url != "") {
		try {

			domain = new URL(url);
			if (type.equals("Left")) {
				long beginTime = System.currentTimeMillis();
				Connection _conn = Jsoup.connect(url);
				_conn.timeout(10000);
				Document doc2 = _conn.get();
				
				
				html = doc2.html().toString();
				long endTime = System.currentTimeMillis();
				requestTimeSpan = String.valueOf((endTime - beginTime));
			} else {
				ReportDBManager rdbMObj = new ReportDBManager();
				filePath = rdbMObj.getFilePathByUrl(url);				
				File file = new File(filePath);
				if (file.exists()) {
					Document doc = Jsoup.parse(file, "UTF-8", ""); 
					Elements __scriptList = doc.getElementsByTag("script");
					int size=__scriptList.size();
					if(size>0){
						while(size-->0){						
							__scriptList.get(size).remove();
						}	
					}
					html = doc.html().toString();
					
				} else {
					

					html = "connect time out";
				}
			}
			System.out.print("HTML:" + html);
			String[] htmlList = html.split("<head");
			System.out.print("htmlList:" + htmlList.length);
			String jq = "<script id='guiws_jq' src='/js/jqueryForIframe.js'></script>";
			String xpath = "<script id='guiws_xpath' src='/js/xpath.js'></script>";
			String dom = "<script data='"
					+ type
					+ "' id='contentForIframe1' src='/js/contentForIframe.js'></script>";

			html = htmlList[0] + jq + xpath + dom + "<base href='"
					+ domain.getProtocol() + "://" + domain.getHost()
					+ "'/>" + "<head" + htmlList[1];

		} catch (Exception e) {
			html = "connect time out";
		}
		jsonObject.put("HTML", html);
		jsonObject.put("filePath", filePath);
		jsonObject.put("requestTimeSpan", requestTimeSpan);

		//System.out.print("HTML:" + html);
		System.out.print("filePath:" + filePath);
		System.out.print("type:" + type);
		pw.write(jsonObject.toJSONString());
		pw.flush();
		pw.close();
	}
%>