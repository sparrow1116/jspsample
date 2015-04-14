<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>

<jsp:useBean id="memMgr" class="com.sec.webs.gui.UserDBManager" />
<%
    JSONObject jsonObject = new JSONObject();
	
    String password = request.getParameter("password");

    boolean isSuccess = memMgr.userUpdate(session.getAttribute("UserID").toString(), password);

    if (!isSuccess) {
        jsonObject.put("Result", "Error");
        jsonObject.put("Message", "Fail to update user");
        out.println(jsonObject.toJSONString());
        return;
    }

    jsonObject.put("Result", "OK");
    out.println(jsonObject.toJSONString());
%>