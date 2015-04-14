<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.sec.webs.common.*" %>
<%@ page language="java" import="com.sec.webs.gui.*" %>
<jsp:useBean id="memMgr" class="com.sec.webs.gui.UserDBManager" />
<%
	HashMap<String, String> searchParam = new HashMap();
	Enumeration parameterNames = request.getParameterNames();

	String jtSorting = request.getParameter("jtSorting");
	String UserID = session.getAttribute("UserID").toString();

	String totalRecordCount = memMgr.getUserListRecordCount(searchParam, UserID);
	searchParam.put("queryMode", "LIST");
	searchParam.put("jtSorting", jtSorting);
	ArrayList<LinkedHashMap<String, String>> records = memMgr.getUserDataList(searchParam, Integer.parseInt(totalRecordCount), UserID);

	JSONObject jsonObject = new JSONObject();
	jsonObject.put("Result", "OK");
	jsonObject.put("Records", records);
	jsonObject.put("TotalRecordCount", totalRecordCount);
	out.println(jsonObject.toJSONString());
%>