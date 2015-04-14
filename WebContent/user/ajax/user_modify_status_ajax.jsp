<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.sec.webs.common.*" %>
<%@ page language="java" import="com.sec.webs.gui.*" %>

<jsp:useBean id="memMgr" class="com.sec.webs.gui.UserDBManager" />
<%

	JSONObject jsonObject = new JSONObject();
	String userSrl = request.getParameter("userSrl");
	String status = request.getParameter("status");
	if(status.equals("Disable")){
	    status = "Y";
	}else{
	    status = "N";
	}
		
	boolean isSuccess = memMgr.modifyStatus(userSrl, status);	
    if (!isSuccess) {
        jsonObject.put("Result", "Error");
        jsonObject.put("Message", "Fail to modify Status");
        out.println(jsonObject.toJSONString());
        return;
    }

	jsonObject.put("Result", "OK");
	out.println(jsonObject.toJSONString());
%>