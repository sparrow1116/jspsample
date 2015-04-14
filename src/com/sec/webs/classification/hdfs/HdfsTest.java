
package com.sec.webs.classification.hdfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 *
 * HdfsTest.java
 *
 * Copyright 2014 by Samsung Electronics, Inc.,
 *
 * This software is the confidential and proprietary information of Samsung
 * Electronics, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Samsung.
 *
 * @author Fang Fang
 * @version 1.0, Dec 11, 2014.
 * @see
 * @since 1.0
 *
 */

public class HdfsTest {

    public static void main(String[] args) throws IOException {
        HdfsFileOperation op = HdfsFileOperation.getInstance();
        // 1.Testing localFile2Hdfs--ok
        // String src="F://ddd.txt";
        // String dst=op.getHdfsUrl()+"/"+"abcde"+"/ddd.txt";
        // System.out.println("dst is:"+dst);
        // op.localFile2Hdfs(src, dst);

        // 2.Testing readFileToGetList--ok
         List<String> resultLineList = op.readFileToGetList(op.getHdfsUrl() +
         "/" +
         "abcde" + "/bbb.txt");
         int size=resultLineList.size();
         for(int i=0;i<size;i++){
         System.out.println("result line: " + resultLineList.get(i));
         }

        // 3.Testing getAllDirectoryFromHdfs--ok
        // List<String> dirNameList=op.getAllDirectoryNameFromHdfs();
        // int length=dirNameList.size();
        // for(int i=0;i<length;i++){
        // String str=dirNameList.get(i);
        // System.out.println(str);
        // }

        // 4.Testing getFileNameOfCertainDirFromHdfs--ok
        // List<String>
        // fileNameList=op.getFileNameOfCertainDirFromHdfs("abcde");
        // int fileLength=fileNameList.size();
        // for(int j=0;j<fileLength;j++){
        // String fileName=fileNameList.get(j);
        // System.out.println(fileName);
        // }

        // 5.Testing ReadFile--ok
        // op.ReadFile(op.getHdfsUrl() + "/" + "abcde" + "/aaa.txt");

        // 6.Testing hdfsFile2Local--ok
        // String srcName = op.getHdfsUrl() + "/abcde/bbb.txt";
        // String dstName = "F://bbb_copy.txt";
        // op.hdfsFile2Local(srcName, dstName);

        // 7.Testing hdfsFile2LocalFile--ok
        // String srcName = op.getHdfsUrl() + "/" + "abcde" + "/aaa.txt";
        // String dstName = "F://aaa_copy.txt";
        // File file = op.hdfsFile2LocalFile(srcName, dstName);
        // if (!file.equals(null)) {
        // String ap = file.getAbsolutePath();
        // System.out.println("file absolute path is: " + ap);
        // }

        // 8.Testing getAllDirectoryNameFromHdfs--ok
        // op.getAllDirectoryNameFromHdfs();

        // 9.Testing deleteHdfsFile--ok
        // boolean result=op.deleteHdfsFile(op.getHdfsUrl() + "/" + "abcde" +
        // "/xxx.txt");
        // System.out.println("delete result is:"+result);

        // 10.Testing appendContent--ok
//         op.appendContent(op.getHdfsUrl() +"/" +"abcde" + "/bbb.txt", "cccfff");

        // 11.Testing appendContentByAppend------only the condition no file
        // exist
        // is succuss
        // op.appendContentByAppend(op.getHdfsUrl() + "/" + "abcde" +
        // "/yyy.txt",
        // "aaabbbccc");

        // 12.Testing appendHdfsFile------not test
        // op.appendHdfsFile(op.getHdfsUrl() + "/" + "abcde" + "/bbb.txt",
        // op.getHdfsUrl() + "/" + "abcde" + "/aaa.txt");

        // 13.Testing appendContentBycopyMerge-----Target
        // hdfs://109.123.120.53:9000/abcde/bbb.txt already exists
        // op.appendHdfsFileByMerge(op.getHdfsUrl() + "/" + "abcde" +
        // "/bbb.txt", op.getHdfsUrl() + "/" + "abcde" + "/aaa.txt");

        // 14.Testing appendHdfsFile
        // op.appendHdfsFileByAppend(op.getHdfsUrl() + "/" + "abcde" +
        // "/bbb.txt", op.getHdfsUrl() + "/" + "abcde" + "/aaa.txt");

    }

}
