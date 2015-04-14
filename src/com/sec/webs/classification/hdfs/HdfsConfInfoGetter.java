
package com.sec.webs.classification.hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * HdfsConfInfoGetter.java
 *
 * Copyright 2014 by Samsung Electronics, Inc.,
 *
 * This software is the confidential and proprietary information of Samsung
 * Electronics, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Samsung.
 *
 * @author Fang Fang
 * @version 1.0, Dec 5, 2014.
 * @see
 * @since 1.0
 *
 */
public class HdfsConfInfoGetter {
    private static HdfsConfInfoGetter instance = null;
    private Properties properties = new Properties();
    private InputStream inputFile;
    private static final String CONFIG_FILE_PATH = "hdfsConfInfo.properties";

    private HdfsConfInfoGetter() {
        loadProperies();
    }

    /**
     * @Title: loadProperies void
     * @warning
     * @exception
     * @see
     */
    private void loadProperies() {
        try {
            inputFile = HdfsConfInfoGetter.class.getResourceAsStream(CONFIG_FILE_PATH);
            properties.load(inputFile);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (inputFile != null) {
                try {
                    inputFile.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }

    }

    /**
     * @Title: getInstance
     * @return SftpInfoGetter
     * @warning
     * @exception
     * @see
     */
    public static HdfsConfInfoGetter getInstance() {
        if (instance == null) {
            instance = new HdfsConfInfoGetter();
        }
        return instance;
    }

    /**
     * @Title: getDefaultBaseUploadDir
     * @return String
     * @warning
     * @exception
     * @see
     */
    public String getHdfsUriIP() {
        return properties.getProperty("HDFS_URI_IP");
    }

    /**
     * @Title: getDefaultPort
     * @return String
     * @warning
     * @exception
     * @see
     */
    public String getHdfsUriPort() {
        return properties.getProperty("HDFS_URI_PORT");
    }

    /**
     * main
     *
     * @param args
     */
    public static void main(String[] args) {
        HdfsConfInfoGetter infoGetter = HdfsConfInfoGetter.getInstance();
        String ip = infoGetter.getHdfsUriIP();
        String port = infoGetter.getHdfsUriPort();
        System.out.println("ip:" + ip);
        System.out.println("port:" + port);

    }

}
