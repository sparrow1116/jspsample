package com.sec.webs.gui;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;

public class SessionCounter implements HttpSessionListener {


    private static HashMap<String, HashMap<String,String>> loginSessionMap = new HashMap<String, HashMap<String,String>>();

    
    public void sessionCreated(HttpSessionEvent se) {

    }
    
    /**
     * It is called when session is closed by tomcat.
     * We need to check this session is login and delete it from login session map.
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionID = se.getSession().getId();
        //sessionMap 에서 지금 닫히는 세션ID 값이 있으면 지워즌디.별도 로그아웃없이 세션을 닫는경우가 많음.
        if(loginSessionMap.get(sessionID)!=null){
            loginSessionMap.remove(sessionID);
        }
      
    
    }
    
    /**
     * Add login session to the map.
     * @param sessionId sesion id
     * @param loginID login id
     * @param reqIP request ip address
     * @param timeStamp login time
     */
    public static void addLoginSession(String sessionId, String loginID,String reqIP,String timeStamp){
        HashMap<String,String> loginMap = new HashMap<String,String>();
        loginMap.put("id",loginID);
        loginMap.put("ip",reqIP);
        loginMap.put("time", timeStamp);
        loginSessionMap.put(sessionId,loginMap  );
    }
    
    /**
     * Remove login session from the map.
     * It called when user logout.
     * @param sessionId session id
     */
    public static void removeLoginSession(String sessionId){
        if(loginSessionMap.get(sessionId)!=null ){
            loginSessionMap.remove(sessionId);
        }
    }
    
    
    
    /**
     * Get login session map
     * @return login session map.
     */
    public static HashMap<String,HashMap<String,String>> getLoginSessionMap(){
        return loginSessionMap;
    }
    
    

}

