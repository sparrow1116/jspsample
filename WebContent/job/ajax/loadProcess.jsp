<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="java.io.*"%>
<%@ page language="java" import="com.sec.webs.common.*"%>
<jsp:useBean id="reportMgr" class="com.sec.webs.gui.ReportDBManager" />
<%
	String jobID = request.getParameter("jobId");

	String sProcess = "";
	String cProcess = "";
	String info="";
	if (jobID != null && jobID.length() > 0) {
		sProcess = reportMgr.getSProcess(Integer.parseInt(jobID));
		cProcess = reportMgr.getCProcess(Integer.parseInt(jobID));
		info=reportMgr.getCinfo(Integer.parseInt(jobID));
	}
	System.out.print("sProcess:" + sProcess);
	System.out.print("cProcess:" + cProcess);
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("sProcess", sProcess);
	jsonObject.put("cProcess", cProcess);
	jsonObject.put("info", info);
	PrintWriter pw = response.getWriter();
	pw.write(jsonObject.toJSONString());
	pw.flush();
	pw.close();
%>