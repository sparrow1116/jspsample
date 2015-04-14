<%@ page language="java" import="org.json.simple.*"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="java.io.*"%>
<%@ page language="java" import="com.sec.webs.common.*" %>
<jsp:useBean id="reportMgr" class="com.sec.webs.gui.ReportDBManager" />
<%
	HashMap<String, String> searchParam = new HashMap();
	/* String userSrl = session.getAttribute("UserSrl").toString(); */
	
	String jtSorting = request.getParameter("jtSorting");
	String jobID = request.getParameter("jobID");
	//String userSrl = request.getParameter("userSrl");
	String projectName = request.getParameter("projectName");
	
	searchParam.put("queryMode", "LIST");
	searchParam.put("jtSorting", jtSorting);
//	searchParam.put("userSrl", userSrl);

	//session = request.getSession();
	String userSrl = (String)session.getAttribute("UserSrl");
	

	ArrayList<LinkedHashMap<String, String>> recordList = new ArrayList();
	JSONObject jsonObject = new JSONObject();

	if(projectName != null){
		ArrayList<LinkedHashMap<String, String>> theRecord = new ArrayList();
		
		theRecord = reportMgr.getReportDataByID(searchParam,jobID);
		recordList = reportMgr.getReportDataList(searchParam,userSrl);
		if(theRecord.size() > 0){
			for(int i = 0; i<recordList.size();i++){
								
				if(!theRecord.get(0).get("id").equals(recordList.get(i).get("id"))  && 
						projectName.equals(recordList.get(i).get("jobname"))){
					jsonObject.put("Result", "Error");
					out.println(jsonObject.toJSONString());
					return;
				}
			}
			jsonObject.put("Result", "OK");
			out.println(jsonObject.toJSONString());
		}else{
			for(int i = 0; i<recordList.size();i++){
				System.out.println(recordList.get(i).get("jobname"));
				if(projectName.equals(recordList.get(i).get("jobname"))){
					jsonObject.put("Result", "Error");
					out.println(jsonObject.toJSONString());
					return;
				}
			}
			jsonObject.put("Result", "OK");
			out.println(jsonObject.toJSONString());
		}
	}else{
		if(jobID != null){
			recordList = reportMgr.getReportDataByID(searchParam,jobID);
		}else{
			recordList = reportMgr.getReportDataList(searchParam,userSrl);
		}
		
		jsonObject.put("Result", "OK");
		jsonObject.put("Records", recordList);
		jsonObject.put("TotalRecordCount", recordList.size());
		out.println(jsonObject.toJSONString());
	}
	
	
%>