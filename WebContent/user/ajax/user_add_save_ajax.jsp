<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.sec.webs.common.*" %>
<%@ page language="java" import="com.sec.webs.gui.*" %>
<jsp:useBean id="memMgr" class="com.sec.webs.gui.UserDBManager" />
<%
	JSONObject jsonObject = new JSONObject();
	
    String userID = request.getParameter("userID");
    String password = request.getParameter("password");
    String role = request.getParameter("role");
    
	boolean isDuplicated = memMgr.userIDDuplicated(userID);
	if(isDuplicated) {
        jsonObject.put("Result", "Error");
        jsonObject.put("Message", "The user already exists");
        out.println(jsonObject.toJSONString());
        return;
	}

    boolean isSuccess = memMgr.userInsert(userID, password, role);

    if (!isSuccess) {
        jsonObject.put("Result", "Error");
        jsonObject.put("Message", "Fail to insert user");
        out.println(jsonObject.toJSONString());
        return;
    }

    jsonObject.put("Result", "OK");
    out.println(jsonObject.toJSONString());
%>