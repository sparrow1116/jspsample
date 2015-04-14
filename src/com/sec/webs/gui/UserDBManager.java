package com.sec.webs.gui;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sec.webs.common.PasswordHelper;
import com.sec.webs.common.WebsCommon;


public class UserDBManager {

    public UserDBManager() {}

    
    /**
     * Register new member record
     * @param regBean Member RegisterBean
     * @return insert result
     */
    public boolean userInsert(String useName, String Password, String role) {
  
        WebsCommon websCommon = new WebsCommon();

        String[] digestAndSalt = null;
        
        try {
            digestAndSalt = PasswordHelper.getDigestAndSalt(Password);
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
         /*   WebsCommon.error("Exception: " + e.getMessage());*/
            return false;
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
           /* WebsCommon.error("Exception: " + e.getMessage());*/
            return false;
        }
        
        
        String strQuery = "";
        strQuery += "   INSERT INTO user ( \n";
        strQuery += "       UserID, UserName, Password, Salt, Role, Regdate\n";
        strQuery += "   ) VALUES ( \n";
        strQuery += "       ?, ?,?, ?, ?, SYSDATE() \n";
        strQuery += "   ) \n";
        ArrayList<String> bindNames = new ArrayList<String>();
        bindNames.add(useName);
        bindNames.add(useName);
        bindNames.add(digestAndSalt[0]);
        bindNames.add(digestAndSalt[1]);
        bindNames.add(role);

        int userId = websCommon.getExecuteUpdateID(strQuery, bindNames);

        return (userId > 0) ? true : false;
    }
    
    /**
     * Register new member record
     * @param regBean Member RegisterBean
     * @return insert result
     */
    public boolean userRegister(String userID, String Password) {
  
        WebsCommon websCommon = new WebsCommon();

        String[] digestAndSalt = null;
        
        try {
            digestAndSalt = PasswordHelper.getDigestAndSalt(Password);
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
         /*   WebsCommon.error("Exception: " + e.getMessage());*/
            return false;
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
           /* WebsCommon.error("Exception: " + e.getMessage());*/
            return false;
        }
        
        
        String strQuery = "";
        strQuery += "   INSERT INTO user ( \n";
        strQuery += "       userID, UserName, Password, Salt, Regdate\n";
        strQuery += "   ) VALUES ( \n";
        strQuery += "       ?, ?,?, ?, SYSDATE() \n";
        strQuery += "   ) \n";
        ArrayList<String> bindNames = new ArrayList<String>();
        bindNames.add(userID);
        bindNames.add(userID);
        bindNames.add(digestAndSalt[0]);
        bindNames.add(digestAndSalt[1]);

        int userId = websCommon.getExecuteUpdateID(strQuery, bindNames);

        return (userId > 0) ? true : false;
    }
    
    /**
     * Update member record
     * @return insert result
     */
    public boolean userUpdate(String userID, String password) {
    	WebsCommon websCommon = new WebsCommon();
    	
        String[] digestAndSalt = null;
        
        try {
            digestAndSalt = PasswordHelper.getDigestAndSalt(password);
        } catch (NoSuchAlgorithmException e) {
            return false;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        ArrayList<String> bindNames = new ArrayList<String>();
        String  strQuery = "";
        strQuery += "   UPDATE user \n";
        strQuery += "   SET \n";
        strQuery += "        Password = ?, Salt = ?, ModDate = SYSDATE() \n";
        strQuery += "   WHERE UserID = ? \n";
        
        
        bindNames.add(digestAndSalt[0]);
        bindNames.add(digestAndSalt[1]);
        bindNames.add(userID);

        int ret = websCommon.executeUpdate(strQuery, bindNames);

        return (ret > 0) ? true : false;
    }
    
    /**
     * Update member record
     * @return insert result
     */
    public boolean userUpdate(String userID, String password, String admin) {
    	WebsCommon websCommon = new WebsCommon();
    	
        String[] digestAndSalt = null;
        
        try {
            digestAndSalt = PasswordHelper.getDigestAndSalt(password);
        } catch (NoSuchAlgorithmException e) {
            return false;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        ArrayList<String> bindNames = new ArrayList<String>();
        String  strQuery = "";
        strQuery += "   UPDATE user \n";
        strQuery += "   SET \n";
        strQuery += "        Password = ?, Salt = ?, Role = ?, ModDate = SYSDATE() \n";
        strQuery += "   WHERE UserID = ? \n";
        
        
        bindNames.add(digestAndSalt[0]);
        bindNames.add(digestAndSalt[1]);
        bindNames.add(admin);
        bindNames.add(userID);

        int ret = websCommon.executeUpdate(strQuery, bindNames);

        return (ret > 0) ? true : false;
    }
    
    /**
     * Update member record
     * @return insert result
     */
    public boolean userDelete(String UserSrl) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "UPDATE user SET UseYN='N' WHERE UserSrl= ?";

        bindNames.add(UserSrl);

        int ret = websCommon.executeUpdate(strQuery, bindNames);

        return (ret > 0) ? true : false;
    }
    
    /**
     * Update member record
     * @return insert result
     */
    public boolean modifyStatus(String UserSrl, String Status) {
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String strQuery = "UPDATE user SET UseYN=? WHERE UserSrl= ?";
       
        bindNames.add(Status);
        bindNames.add(UserSrl);

        int ret = websCommon.executeUpdate(strQuery, bindNames);

        return (ret > 0) ? true : false;
    }
    
    /**
     * Check ID and password is correct.
     * @param id member id
     * @param passwd member password
     * @return a boolean indicating if the id and password is correct
     * @throws Exception
     */
    public boolean loginCheck(String id, String passwd) throws Exception {
        WebsCommon websCommon = new WebsCommon();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            boolean userExist = true;
            con = websCommon.getConnection();
            String strQuery = "select Password, Salt from user where userID = ? and UseYN = 'Y'";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            String digest, salt;
            if (rs.next()) {
                digest = rs.getString("PASSWORD");
                salt = rs.getString("SALT");
                
                if (digest == null || salt == null) {
                    throw new SQLException("Database inconsistant Salt or Digested Password altered");
                }
            }
            else { // TIME RESISTANT ATTACK (Even if the user does not exist the
                   // Computation time is equal to the time needed for a legitimate user
                digest = "000000000000000000000000000=";
                salt = "00000000000=";
                userExist = false;
            }
 
            
            byte[] bDigest = PasswordHelper.base64ToByte(digest);
            byte[] bSalt = PasswordHelper.base64ToByte(salt);
            // Compute the new DIGEST
            byte[] proposedDigest = PasswordHelper.getHash(passwd, bSalt);
            
            boolean isSuccess = Arrays.equals(proposedDigest, bDigest) && userExist;
            if(isSuccess){
                insertLoginTime(id);
            }
            
            return isSuccess;
            

        } catch (Exception ex) {
            System.out.println("Exception" + ex);
            throw ex;
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException es) {}
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException es) {}
            if (con != null)
                try {
                    con.close();
                } catch (SQLException es) {}
        }
    }
    
    /**
     * Search Result Data
     * 
     * @param 
     * @param searchParam
     * @return
     */
    public ArrayList<LinkedHashMap<String, String>> getUserDataList(
            Map<String, String> searchParam, int totalCount, String UserID) {
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
            strQuery += "   FROM user A \n";
            strQuery += "   WHERE A.UserID <> ? \n";
        } else {
            strQuery += "   SELECT \n";
            strQuery += "       A.UserSrl, A.UserID, A.UserName,A.Role, A.LastLoginDate,  (CASE WHEN A.UseYN = 'Y' THEN 'Enable' ELSE 'Disable' END)  Status \n";   
            strQuery += "   FROM user A \n";
            strQuery += "   WHERE A.UserID <> ? \n";
        }

    
        // Sorting
        if ("LIST".equals(queryMode)) {
            if (websCommon.isExist(searchParam, "jtSorting")) {
                strQuery += "   ORDER BY "
                        + searchParam.get("jtSorting").toString() + " \n";
            } else {
                strQuery += "   ORDER BY A.UserSrl DESC \n";
            }
        }

        // Paging
        int startOrderIndex = 0;
        if ("LIST".equals(queryMode)) {
        
         /*  strQuery += "   LIMIT 0, 10";*/
            
        } else {
            startOrderIndex = -1;
        }
        
        bindNames.add(UserID);
        return websCommon.getDataList(strQuery, bindNames, totalCount
                - startOrderIndex);
    }
    
    
    
    /**
     * Search Result Count
     * 
     * @param searchParam
     * @return
     */
    public String getUserListRecordCount(Map<String, String> searchParam, String UserID) {

        searchParam.put("queryMode", "COUNT");
        ArrayList<LinkedHashMap<String, String>> records = getUserDataList(
                searchParam, 0, UserID);

        return records.get(0).get("CNT");
    }
    
    
    public boolean insertLoginTime(String UserID){
        WebsCommon websCommon = new WebsCommon();
        ArrayList<String> bindNames = new ArrayList<String>();
        String  strQuery = "";
        strQuery += "   UPDATE user \n";
        strQuery += "   SET \n";
        strQuery += "   LastLoginDate = SYSDATE() \n";
        strQuery += "   WHERE UserID = ? \n";
        
        bindNames.add(UserID);

        int ret = websCommon.executeUpdate(strQuery, bindNames);

        return (ret > 0) ? true : false;
    }
    
    public boolean userIDDuplicated(String userID) {
    	WebsCommon websCommon = new WebsCommon();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = websCommon.getConnection();
            String strQuery = "select Role from user where UserID = ? ";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, userID);
            rs = pstmt.executeQuery();
            return rs.next();

        } catch (Exception ex) {
            System.out.println("Exception" + ex);
            throw new RuntimeException(ex);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException es) {}
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException es) {}
            if (con != null)
                try {
                    con.close();
                } catch (SQLException es) {}
        }
    }
    
    public String getRole(String userID){
    	WebsCommon websCommon = new WebsCommon();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = websCommon.getConnection();
            String strQuery = "select Role from user where UserID = ? ";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, userID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Role");
            }

        } catch (Exception ex) {
            System.out.println("Exception" + ex);
            throw new RuntimeException(ex);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException es) {}
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException es) {}
            if (con != null)
                try {
                    con.close();
                } catch (SQLException es) {}
        }

        return null;
    }
    
    public String getUserSrl(String userID){
    	WebsCommon websCommon = new WebsCommon();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = websCommon.getConnection();
            String strQuery = "select UserSrl from user where UserID = ? ";
            pstmt = con.prepareStatement(strQuery);
            pstmt.setString(1, userID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("UserSrl");
            }

        } catch (Exception ex) {
            System.out.println("Exception" + ex);
            throw new RuntimeException(ex);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException es) {}
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException es) {}
            if (con != null)
                try {
                    con.close();
                } catch (SQLException es) {}
        }

        return null;
    }
    
    public String getUserIDthoughUserSrl(String UserSrl){
        WebsCommon websCommon = new WebsCommon();
        String strQuery = "";
        ArrayList<String> bindNames = new ArrayList<String>();

       
        strQuery += "   SELECT \n";
        strQuery += "        UserID \n";   
        strQuery += "   FROM user A \n";
        strQuery += "   WHERE A.UseYN = 'Y' and UserSrl = ? \n";    
        bindNames.add(UserSrl);

        return websCommon.getDataOfOneField(strQuery, bindNames);
    }

}