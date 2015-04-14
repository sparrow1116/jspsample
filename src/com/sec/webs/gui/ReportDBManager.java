
package com.sec.webs.gui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sec.webs.common.PasswordHelper;
import com.sec.webs.common.WebsCommon;

public class ReportDBManager {

    public ReportDBManager() {
    };

    public ArrayList<LinkedHashMap<String, String>> getReportDataList(
            Map<String, String> searchParam, String userSrl) {
        WebsCommon websCommon = new WebsCommon();
        String strQuery = "";
        ArrayList<String> bindNames = new ArrayList<String>();

        String queryMode = "LIST";
        if (websCommon.isExist(searchParam, "queryMode")
                && "COUNT".equals(searchParam.get("queryMode").toString())) {
            queryMode = "COUNT";
        }

        if ("COUNT".equals(queryMode)) {
            strQuery += "   SELECT COUNT(*) CNT \n";
            strQuery += "   FROM report \n";
        } else {
            strQuery += "   SELECT \n";
            strQuery += "       id, jobname, datasource, swrapper, cwrapper,date_format( submittime, '%Y-%m-%d %H:%i:%s' ) as submittime,date_format( finishtime, '%Y-%m-%d %H:%i:%s' ) as finishtime,cprocess,sprocess, status,outputpath,cinfo \n";
            strQuery += "   FROM jobinfo A \n";
            strQuery += "   WHERE A.UserSrl = ? \n";
            bindNames.add(userSrl);
        }

        // Sorting
        if ("LIST".equals(queryMode)) {
            if (websCommon.isExist(searchParam, "jtSorting")) {
                strQuery += "   ORDER BY " + searchParam.get("jtSorting").toString() + " \n";
            }
        }

        return websCommon.getDataList(strQuery, bindNames, 0);
    }

    public ArrayList<LinkedHashMap<String, String>> getReportDataByID(
            Map<String, String> searchParam, String jobid) {
        WebsCommon websCommon = new WebsCommon();
        String strQuery = "";
        ArrayList<String> bindNames = new ArrayList<String>();

        String queryMode = "LIST";
        if (websCommon.isExist(searchParam, "queryMode")
                && "COUNT".equals(searchParam.get("queryMode").toString())) {
            queryMode = "COUNT";
        }

        if ("COUNT".equals(queryMode)) {
            strQuery += "   SELECT COUNT(*) CNT \n";
            strQuery += "   FROM report \n";
        } else {
            strQuery += "   SELECT \n";
            strQuery += "       id, jobname, datasource, swrapper, cwrapper,date_format( submittime, '%Y-%m-%d %H:%i:%s' ) as submittime ,date_format( finishtime, '%Y-%m-%d %H:%i:%s' ) as finishtime,status,outputpath \n";
            strQuery += "   FROM jobinfo A \n";
            strQuery += "   WHERE A.id = ? \n";
            bindNames.add(jobid);
        }

        // Sorting
        if ("LIST".equals(queryMode)) {
            if (websCommon.isExist(searchParam, "jtSorting")) {
                strQuery += "   ORDER BY " + searchParam.get("jtSorting").toString() + " \n";
            }
        }

        return websCommon.getDataList(strQuery, bindNames, 0);
    }

    public ArrayList<LinkedHashMap<String, String>> getReportDataList(
            Map<String, String> searchParam) {
        WebsCommon websCommon = new WebsCommon();
        String strQuery = "";
        ArrayList<String> bindNames = new ArrayList<String>();

        String queryMode = "LIST";
        if (websCommon.isExist(searchParam, "queryMode")
                && "COUNT".equals(searchParam.get("queryMode").toString())) {
            queryMode = "COUNT";
        }

        if ("COUNT".equals(queryMode)) {
            strQuery += "   SELECT COUNT(*) CNT \n";
            strQuery += "   FROM report \n";
        } else {
            strQuery += "   SELECT \n";
            strQuery += "       id, jobname, datasource, swrapper, cwrapper, date_format( submittime, '%Y-%m-%d %H:%i:%s' ) as submittime, status,outputpath \n";
            strQuery += "   FROM jobinfo A \n";
            /*
             * strQuery += "   WHERE A.UserSrl = ? \n"; bindNames.add(userSrl);
             */
        }

        // Sorting
        if ("LIST".equals(queryMode)) {
            if (websCommon.isExist(searchParam, "jtSorting")) {
                strQuery += "   ORDER BY " + searchParam.get("jtSorting").toString() + " \n";
            }
        }

        return websCommon.getDataList(strQuery, bindNames, 0);
    }

    public String getFilePathByUrl(String url) throws MalformedURLException {
        WebsCommon websCommon = new WebsCommon();

        String strQuery = "";
        strQuery += "   SELECT filename \n";
        strQuery += "   FROM  \n";
        strQuery += "       urlmap \n";
        strQuery += "       WHERE url = ? \n";

        ArrayList<String> bindNames = new ArrayList<String>();
        bindNames.add(url);
        URL domain = new URL(url);
        System.out.println(websCommon.getDataOfOneField(strQuery, bindNames));
        String fileName = websCommon.getDataOfOneField(strQuery, bindNames);
        String filePath = "/var/lib/tomcat6/Offline/SamplePages/" + domain.getHost() + "/html/"
                + fileName;
        return filePath;
    }

    public boolean jobUpdate(String jobID, String jobName, String dataSource, String inputpath,
            String swrapper, String cwrapper, String userSrl) {

        WebsCommon websCommon = new WebsCommon();

        String[] digestAndSalt = null;

        // String strQuery = "UPDATE jobinfo SET status=? WHERE jobid= ?";
        String strQuery = "UPDATE jobinfo SET jobname=?," + "datasource=?," + "inputpath=?,"
                + "swrapper=?," + "cwrapper=?," + "usersrl=?" + " WHERE id=?";
        ArrayList<String> bindNames = new ArrayList<String>();
        bindNames.add(jobName);
        bindNames.add(dataSource);
        bindNames.add(inputpath);
        bindNames.add(swrapper);
        bindNames.add(cwrapper);
        bindNames.add(userSrl);
        bindNames.add(jobID);

        int userId = websCommon.executeUpdate(strQuery, bindNames);

        return (userId > 0) ? true : false;
    }

    public boolean jobInsert(String jobName, String dataSource, String inputpath, String swrapper,
            String cwrapper, String userSrl) {

        WebsCommon websCommon = new WebsCommon();

        String[] digestAndSalt = null;

        String strQuery = "";
        strQuery += "   INSERT INTO jobinfo ( \n";
        strQuery += "       jobname, datasource, inputpath,swrapper, cwrapper,usersrl, submittime,status\n";
        strQuery += "   ) VALUES ( \n";
        strQuery += "       ?, ?, ?, ?, ?, ?, SYSDATE(),? \n";
        strQuery += "   ) \n";
        ArrayList<String> bindNames = new ArrayList<String>();
        bindNames.add(jobName);
        bindNames.add(dataSource);
        bindNames.add(inputpath);
        bindNames.add(swrapper);
        bindNames.add(cwrapper);
        bindNames.add(userSrl);
        bindNames.add("submitted");

        int userId = websCommon.getExecuteUpdateID(strQuery, bindNames);

        return (userId > 0) ? true : false;
    }

    public boolean modifyJobStatus(String jobid, String Status) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "UPDATE jobinfo SET status=? WHERE jobid= ?";

        bindNames.add(Status);
        bindNames.add(jobid);

        int ret = websCommon.executeUpdate(strQuery, bindNames);

        return (ret > 0) ? true : false;
    }

    public boolean modifyJobStatus(String jobid, String outputPath, String Status) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "UPDATE jobinfo SET status=?,outputpath=?,finishtime=SYSDATE() WHERE id= ?";

        bindNames.add(Status);
        bindNames.add(outputPath);     
        bindNames.add(jobid);

        int ret = websCommon.executeUpdate(strQuery, bindNames);

        return (ret > 0) ? true : false;
    }

    public String getjobname(String jobid) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "select jobname from jobinfo WHERE id= ?";

        bindNames.add(jobid);

        String ret = websCommon.getDataOfOneField(strQuery, bindNames);

        return ret;
    }

    public String getjobdatasource(String jobid) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "select datasource from jobinfo WHERE id= ?";

        bindNames.add(jobid);

        String ret = websCommon.getDataOfOneField(strQuery, bindNames);

        return ret;
    }
    
    public String getinputpath(String jobid) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "select inputpath from jobinfo WHERE id= ?";
        
        bindNames.add(jobid);
        
        String ret = websCommon.getDataOfOneField(strQuery, bindNames);
        
        return ret;
    }

    public String getjobswrapper(String jobid) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "select swrapper from jobinfo WHERE id= ?";

        bindNames.add(jobid);

        String ret = websCommon.getDataOfOneField(strQuery, bindNames);

        return ret;
    }

    public String getjobcwrapper(String jobid) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "select cwrapper from jobinfo WHERE id= ?";

        bindNames.add(jobid);

        String ret = websCommon.getDataOfOneField(strQuery, bindNames);

        return ret;
    }

    public String getjobstatus(String jobid) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "select status from jobinfo WHERE id= ?";

        bindNames.add(jobid);

        String ret = websCommon.getDataOfOneField(strQuery, bindNames);

        return ret;
    }

    public ArrayList<String> getClassificationFiles(String cTableName, double threshold) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "SELECT file from `" + cTableName + "` WHERE similarity> " + threshold;

        ArrayList<String> ret = websCommon.getDataListOfOneField(strQuery, bindNames);

        return ret;
    }
    
	public boolean updateCProcess(int jobid, int perc, String cinfo) {
		WebsCommon websCommon = new WebsCommon();
		ArrayList<String> bindNames = new ArrayList<String>();
		String strQuery = "UPDATE jobinfo SET cprocess=?,cinfo=? WHERE id= ?";

		bindNames.add(String.valueOf(perc));
		bindNames.add(cinfo);
		bindNames.add(String.valueOf(jobid));

		int ret = websCommon.executeUpdate(strQuery, bindNames);

		return (ret > 0) ? true : false;
	}

	public boolean updateSProcess(int jobid, int perc) {
		WebsCommon websCommon = new WebsCommon();
		ArrayList<String> bindNames = new ArrayList<String>();
		String strQuery = "UPDATE jobinfo SET sprocess=? WHERE id= ?";

		bindNames.add(String.valueOf(perc));
		bindNames.add(String.valueOf(jobid));

		int ret = websCommon.executeUpdate(strQuery, bindNames);

		return (ret > 0) ? true : false;
	}
	
	public String getSProcess(int jobid) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "select sprocess from jobinfo WHERE id= ?";

        bindNames.add(String.valueOf(jobid));

        String ret = websCommon.getDataOfOneField(strQuery, bindNames);

        return ret;
    }
	
	public String getCProcess(int jobid) {
		 WebsCommon websCommon = new WebsCommon();
	        ArrayList<String> bindNames = new ArrayList<String>();
	        String strQuery = "select cprocess from jobinfo WHERE id= ?";

	        bindNames.add(String.valueOf(jobid));

	        String ret = websCommon.getDataOfOneField(strQuery, bindNames);

	        return ret;
    }
	
	public String getCinfo(int jobid) {
		 WebsCommon websCommon = new WebsCommon();
	        ArrayList<String> bindNames = new ArrayList<String>();
	        String strQuery = "select cinfo from jobinfo WHERE id= ?";

	        bindNames.add(String.valueOf(jobid));

	        String ret = websCommon.getDataOfOneField(strQuery, bindNames);

	        return ret;
   }
}
