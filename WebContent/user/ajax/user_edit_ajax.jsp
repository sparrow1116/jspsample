<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>

<jsp:useBean id="memMgr" class="com.sec.webs.gui.UserDBManager" />
<%
    JSONObject jsonObject = new JSONObject();
	
	String userID = request.getParameter("userID");
    String password = request.getParameter("password");
    String admin = request.getParameter("admin");

    boolean isSuccess = memMgr.userUpdate(userID, password, admin);

    if (!isSuccess) {
        jsonObject.put("Result", "Error");
        jsonObject.put("Message", "Fail to update user");
        out.println(jsonObject.toJSONString());
        return;
    }

    jsonObject.put("Result", "OK");
    out.println(jsonObject.toJSONString());
%>