<%@ page language="java" import="java.io.*"%>
<%@ page language="java" import="java.net.URL"%>
<%@ page language="java" import="java.util.*"%>
<jsp:useBean id="reportMgr" class="com.sec.webs.gui.ReportDBManager" />
<%
	//String filePath = request.getParameter("filePath");
	String id = request.getParameter("id");
	HashMap<String, String> searchParam = new HashMap();
	ArrayList<LinkedHashMap<String, String>> records = new ArrayList();

	//System.out.print("filePath2222:" + filePath);
	if (id != null) {
		records = reportMgr.getReportDataByID(searchParam, id);
	}
	if (records.size() > 0) {
		//
		String filePath = records.get(0).get("outputpath");
		if (filePath != null) {
			boolean isOnLine = false;
			File f = new File(filePath);
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			BufferedInputStream br = new BufferedInputStream(
					new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset();
			if (isOnLine) { // online
				URL u = new URL("file:///" + filePath);
				response.setContentType(u.openConnection()
						.getContentType());
				response.setHeader("Content-Disposition",
						"inline; filename=" + f.getName());
				// 
			} else { // downlaod
				response.setContentType("application/x-msdownload");
				response.setContentType("application/csv");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + f.getName());
			}
			OutputStream outFile = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				outFile.write(buf, 0, len);
			br.close();
			outFile.close();
		} else {
			response.sendError(404, "File not found!");
			return;
		}
	}
%>