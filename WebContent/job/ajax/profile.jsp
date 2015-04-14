 <%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="org.json.simple.JSONValue"%>
<%@ page language="java" import="org.json.simple.JSONObject"%>
<%@ page language="java" import="org.json.simple.JSONArray"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.sec.webs.gui.*" %>

<%

	try{
		
		ReportDBManager reportMgr = new ReportDBManager();
		
		boolean isInsertOK = false;
		int updataMode = Integer.parseInt(request.getParameter("updataMode"));
		
		String projectName = request.getParameter("name");
		String url = request.getParameter("url");
		String fileName = request.getParameter("fileName");
		String keys = request.getParameter("key");
		String values = request.getParameter("value");
		String parentXpath = request.getParameter("parentXpath");
		
		//String userSrl = request.getParameter("userSrl");
		String userSrl = (String)session.getAttribute("UserSrl");
		
		String[] keyList = keys.split("---");
		String[] valueList = values.split("---");
		
		String swrapper = "";
		
		for(int i = 0 ; i<keyList.length;i++){
			swrapper = swrapper + keyList[i] + ":" + valueList[i];
			if(i != keyList.length -1){
				swrapper = swrapper + ";";
			}
		}
		
		if(updataMode >= 0){
			isInsertOK = reportMgr.jobUpdate(String.valueOf(updataMode),projectName, url,fileName, swrapper, parentXpath,userSrl);
		}else{
			isInsertOK = reportMgr.jobInsert(projectName, url,fileName, swrapper, parentXpath,userSrl);
		}

		JSONObject jsonObject = new JSONObject();		
		
		if(isInsertOK){
			jsonObject.put("Result", "OK");
		}else{
			jsonObject.put("Result", "ERROR");
		}
		out.println(jsonObject.toJSONString());
				
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	
%>
