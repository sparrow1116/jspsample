
<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="org.json.simple.JSONValue"%>
<%@ page language="java" import="org.json.simple.JSONObject"%>
<%@ page language="java" import="org.json.simple.JSONArray"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.sec.webs.classification.classifier.impl.*"%>
<%@ page language="java" import="com.sec.webs.classification.classifier.impl.Classifier"%>

<%-- <jsp:useBean id="classifierMgr" class="com.sec.webs.classification.classifier.impl.Classifier" /> --%>
<%
	JSONObject jsonObject = new JSONObject();
	System.out.println("job_start!!!!!!!");
	try {

		Classifier classifierMgr = new Classifier();
		String jobid = request.getParameter("jobid");
		int i = Integer.parseInt(jobid);

		boolean isInsertOK = classifierMgr.classifier(i);
		if (isInsertOK) {
			jsonObject.put("Result", "OK");
		    out.println(jsonObject.toJSONString());
		} else {
			jsonObject.put("Result", "ERROR");
		    out.println(jsonObject.toJSONString());
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
%>
