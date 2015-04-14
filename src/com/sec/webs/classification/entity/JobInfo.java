package com.sec.webs.classification.entity;

public class JobInfo {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getSwrapper() {
		return swrapper;
	}

	public void setSwrapper(String swrapper) {
		this.swrapper = swrapper;
	}

	public String getCwrapper() {
		return cwrapper;
	}

	public void setCwrapper(String cwrapper) {
		this.cwrapper = cwrapper;
	}

	public String getSubmittime() {
		return submittime;
	}

	public void setSubmittime(String submittime) {
		this.submittime = submittime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}

	private int id;
	private String jobname;
	private String datasource;
	private String swrapper;
	private String cwrapper;
	private String submittime;
	private String status;
	private String finishtime;
	private String usersrl;

	public String getUsersrl() {
		return usersrl;
	}

	public void setUsersrl(String usersrl) {
		this.usersrl = usersrl;
	}

	public String getOutputpath() {
		return outputpath;
	}

	public void setOutputpath(String outputpath) {
		this.outputpath = outputpath;
	}

	public String getInputpath() {
		return inputpath;
	}

	public void setInputpath(String inputpath) {
		this.inputpath = inputpath;
	}

	private String outputpath;
	private String inputpath;

}
