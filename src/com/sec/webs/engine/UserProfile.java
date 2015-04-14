
package com.sec.webs.engine;

import java.util.LinkedHashMap;

//javaBean class to save UserProfile data
class UserProfile {

    private String DataSource = ""; // Engine input source

    private LinkedHashMap<String, String> Wrapper = null; // key,xPath,regular expression

    private String TaskId = ""; // Unique Identification of Task

    public UserProfile() {
        super();
    }

    public UserProfile(String DataSource, LinkedHashMap<String, String> Wrapper, String TaskId) {
        this.DataSource = DataSource;
        this.Wrapper = Wrapper;
        this.TaskId = TaskId;
    }

    public String getDataSource() {
        return DataSource;
    }

    public void setDataSource(String DataSource) {
        this.DataSource = DataSource;
    }

    public LinkedHashMap<String, String> getWrapper() {
        return Wrapper;
    }

    public void setWrapper(LinkedHashMap<String, String> Wrapper) {
        this.Wrapper = Wrapper;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String TaskId) {
        this.TaskId = TaskId;
    }
}
