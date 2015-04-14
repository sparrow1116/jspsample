<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" errorPage="/error/error.jsp" %>
<%@ page language="java" import="com.sec.webs.gui.SessionCounter" %>
<%@ page language="java" import="java.util.*" %>
<%
    session.removeAttribute("UserID");
	SessionCounter.removeLoginSession(session.getId());
	session.invalidate();   
%>
<script>
   location.replace("/login.jsp");
</script>
   
