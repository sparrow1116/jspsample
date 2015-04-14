<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.sec.webs.common.*" %>
<%@ page language="java" import="com.sec.webs.gui.*" %>
<jsp:useBean id="memMgr" class="com.sec.webs.gui.UserDBManager" />
<%
	JSONObject jsonObject = new JSONObject();
				
    String userID = request.getParameter("userID");
    String password = request.getParameter("password");
    
	boolean isDuplicated = memMgr.userIDDuplicated(userID);
	if(isDuplicated) {
        jsonObject.put("Result", "Error");
        jsonObject.put("Message", "The user already exists");
        out.println(jsonObject.toJSONString());
        return;
	}
	
    boolean isSuccess = memMgr.userRegister(userID, password);
    if (!isSuccess) {
        jsonObject.put("Result", "Error");
        jsonObject.put("Message", "Error occurs at server");
        out.println(jsonObject.toJSONString());
        return;
    }
    
    session.setAttribute("UserID", userID);
    session.setAttribute("Role", memMgr.getRole(userID));
    session.setAttribute("UserSrl", memMgr.getUserSrl(userID));
    //Login Session Add
    Date d = new Date();
   	SessionCounter.addLoginSession(session.getId(), userID, request.getRemoteHost(), d.toString());

    jsonObject.put("Result", "OK");
    out.println(jsonObject.toJSONString());
%>