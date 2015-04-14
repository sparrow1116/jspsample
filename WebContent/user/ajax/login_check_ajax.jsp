<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="org.json.simple.JSONValue"%>
<%@ page language="java" import="org.json.simple.JSONObject"%>
<%@ page language="java" import="org.json.simple.JSONArray"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.sec.webs.common.*" %>
<%@ page language="java" import="com.sec.webs.gui.*" %>
<%@ page language="java" import="com.sec.webs.gui.SessionCounter"%>

<jsp:useBean id="memMgr" class="com.sec.webs.gui.UserDBManager" />
<%
	JSONObject jsonObject = new JSONObject();
	
   	String userID = request.getParameter("userID");
   	String password = request.getParameter("password");

   	boolean isLoginCorrect = memMgr.loginCheck(userID, password);

    if (!isLoginCorrect) {
        jsonObject.put("Result", "Error");
        jsonObject.put("Message", "The username or password is wrong");
        out.println(jsonObject.toJSONString());
        return;
    }

    
    session.setAttribute("UserID", userID);
    session.setAttribute("Role", memMgr.getRole(userID));
    session.setAttribute("UserSrl", memMgr.getUserSrl(userID));
    //Login Session Add
    Date d = new Date();
   	SessionCounter.addLoginSession(session.getId(), userID, request.getRemoteHost(), d.toString());
    
   	String userSrl = memMgr.getUserSrl(userID);
   	
    jsonObject.put("Result", "OK");
    jsonObject.put("userSrl",userSrl);
    out.println(jsonObject.toJSONString());
%>