
package com.sec.webs.classification.hdfs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 *
 * HdfsFileOperation.java
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
public class HdfsFileOperation {
    private static HdfsFileOperation instance = new HdfsFileOperation();
    private String hdfsUrl;
    private FileSystem fs;
    private Configuration conf;
    private Path workDirectory;
    private static final String LINE_END = ";";

    private HdfsFileOperation() {
        conf = new Configuration();
        init();
    }

    private void init() {
        HdfsConfInfoGetter infoGetter = HdfsConfInfoGetter.getInstance();
        String ip = infoGetter.getHdfsUriIP();
        String port = infoGetter.getHdfsUriPort();
        hdfsUrl = "hdfs://" + ip + ":" + port;
        conf.set("fs.defaultFS", hdfsUrl);
        try {
            System.out.println("Initial HdfsFileOperation...");
            fs = FileSystem.get(conf);
            System.out.println("fs init is:" + fs);
            workDirectory = fs.getWorkingDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if file/directory exist in HDFS.
     *
     * @param file
     * @return boolean
     * @throws IOException
     */
    public boolean checkExist(String file) {
        boolean isExists = false;
        try {
            isExists = fs.exists(new Path(file));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isExists;
    }

    /**
     * Upload local file to HDFS
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public void localFile2Hdfs(String src, String dst) {
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        try {
            System.out.println("fs is:" + fs);
            fs.copyFromLocalFile(srcPath, dstPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Download file from HDFS to local
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public void hdfsFile2Local(String src, String dst) {
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        try {
            fs.copyToLocalFile(false, srcPath, dstPath, true);
            // hadoop 2.5.1 not support copyToLocalFile which arg less than 4
            // fs.copyToLocalFile(false, srcPath, dstPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Download file from HDFS to local to get a File
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public File hdfsFile2LocalFile(String src, String dst) {
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        try {
            fs.copyToLocalFile(false, srcPath, dstPath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File dstFile = new File(dst);
        if (dstFile.exists()) {
            return dstFile;
        } else {
            return null;
        }

    }

    /**
     * Save a list of String to a HDFS file.
     *
     * @param data
     * @param fileName
     * @throws IOException
     */
    public void saveHdfsFile(List<String> data, String fileName) {
        FSDataOutputStream out;
        try {
            out = fs.create(new Path(fileName), true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            for (String content : data) {
                if (!content.contains(LINE_END)) {
                    content += LINE_END;
                }
                bw.write(content);
            }
            bw.flush();
            bw.close();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Read HDFS file content and form result list.
     *
     * @param fileName
     * @param result
     * @throws IOException
     */

    public List<String> readFileToGetList(String fileName) {
        FSDataInputStream inStream;
        try {
            inStream = fs.open(new Path(fileName));
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
            List<String> result = new ArrayList<String>();
            String content = null;
            while ((content = br.readLine()) != null) {
                result.add(content);
            }
            br.close();
            return result;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Read file from HDFS
     *
     * @param fileName
     */
    public void ReadFile(String fileName) {
        try {
            FSDataInputStream dis = fs.open(new Path(fileName));
            IOUtils.copyBytes(dis, System.out, 4096, false);
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Traverse HDFS files
     *
     * @return List<String> fileNameList
     * @throws IOException
     * @throws FileNotFoundException
     */
    public List<String> getAllDirectoryNameFromHdfs() {
        List<String> fileNameList = new ArrayList<String>();
        try {
            fs = FileSystem.get(URI.create(hdfsUrl), conf);
            FileStatus fileList[] = fs.listStatus(new Path(hdfsUrl + "/"));
            int size = fileList.length;
            for (int i = 0; i < size; i++) {
                fileNameList.add(fileList[i].getPath().getName());
                System.out.println("name:" + fileList[i].getPath().getName() + "/t/tsize:"
                        + fileList[i].getLen());
            }
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNameList;
    }

    /**
     * Traverse HDFS files of a certain directory
     *
     * @param String
     *            path
     * @return List<String> fileNameList
     * @throws IOException
     * @throws FileNotFoundException
     */
    public List<String> getFileNameOfCertainDirFromHdfs(String path) {
        List<String> fileNameList = new ArrayList<String>();
        try {
            fs = FileSystem.get(URI.create(hdfsUrl), conf);
            FileStatus fileList[] = fs.listStatus(new Path(hdfsUrl + "/" + path));
            int size = fileList.length;
            for (int i = 0; i < size; i++) {
                fileNameList.add(fileList[i].getPath().getName());
                System.out.println("name:" + fileList[i].getPath().getName() + "/t/tsize:"
                        + fileList[i].getLen());
            }
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNameList;
    }

    /**
     *
     * deleteHdfsFile
     *
     * @param file
     * @return
     * @throws IOException
     *             boolean
     * @warning
     * @exception
     * @see
     */
    public boolean deleteHdfsFile(String fileName) {
        boolean isExists = checkExist(fileName);
        boolean isDeleted = true;
        if (isExists) {
            try {
                isDeleted = fs.delete(new Path(fileName), true);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("The file or directory " + fileName + " not exists in hdfs.");
        }
        return isDeleted;
    }

    /**
     *
     * appendContentByAppend
     *
     * @param fileName
     * @param txtLine
     *            void
     * @warning
     * @exception
     * @see Should add the following content in hdfs-site.xml <property>
     *      <name>dfs.support.append</name> <value>true</value> </property>
     */
    public void appendContentByAppend(String fileName, String txtLine) {
        try {
            Path dstPath = new Path(fileName);
            // if file not exist, create it.
            if (!fs.exists(dstPath)) {
                fs.create(dstPath);
            }
            fs.close();
            fs = FileSystem.get(conf);
            FSDataOutputStream out = fs.append(dstPath);
            int readLen = txtLine.length();
            System.out.println("readLen is:" + readLen);
            if (-1 != readLen) {
                out.write(txtLine.getBytes());
                out.flush();
            }
            out.close();
            fs.close();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     *
     * appendContent
     *
     * @param fileName
     * @param txtLine
     *            void
     * @warning
     * @exception
     * @see Check if file exists. If file doesn't exist, then create new file &
     *      write to new file If file exists, create a temporary file. Read line
     *      from original file & write that same line to temporary file (don't
     *      forget the newline) Write the lines you want to append to the
     *      temporary file. Finally, delete the original file & move(rename) the
     *      temporary file to the original file.
     */
    public void appendContent(String fileName, String txtLine) {
        try {
            Path dstPath = new Path(fileName);
            // if file not exist, create it.
            if (!fs.exists(dstPath)) {
                FSDataOutputStream out = fs.create(dstPath);
                int readLen = txtLine.length();
                if (-1 != readLen) {
                    out.write(txtLine.getBytes());
                    out.flush();
                }
                out.close();
                fs.close();
            } else {
                Path cpyPath = new Path(fileName + "_cpy");
                FSDataOutputStream out = fs.create(cpyPath);
                FSDataInputStream in = fs.open(dstPath);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String content = null;
                while ((content = br.readLine()) != null) {
                    content = content + "\n";
                    out.write(content.getBytes());
                }
                out.write(txtLine.getBytes());
                out.close();
                fs.delete(dstPath, true);
                fs.rename(cpyPath, dstPath);
                fs.close();

            }

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void appendHdfsFileByMerge(String fileName, String appendFileName) {
        Path srcPath = new Path(appendFileName);
        Path dstPath = new Path(fileName);
        try {
            FileUtil.copyMerge(fs, srcPath, fs, dstPath, false, conf, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     *
     * appendLocalFile
     *
     * @param fileName
     * @param appendFileName
     *            void
     * @warning
     * @exception
     * @see Should add the following content in hdfs-site.xml <property>
     *      <name>dfs.support.append</name> <value>true</value> </property>
     */
    public void appendHdfsFileByAppend(String fileName, String appendFileName) {
        try {
            FSDataOutputStream outStream = fs.append(new Path(fileName));
            FSDataInputStream inStream = fs.open(new Path(appendFileName));
            IOUtils.copyBytes(inStream, outStream, 4096, true);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * setInstance
     */
    public static void setInstance(HdfsFileOperation instance) {
        HdfsFileOperation.instance = instance;
    }

    /**
     * getInstance
     */
    public static HdfsFileOperation getInstance() {
        return instance;
    }

    /**
     * setHdfsUrl
     */
    public void setHdfsUrl(String hdfsUrl) {
        this.hdfsUrl = hdfsUrl;
    }

    /**
     * getHdfsUrl
     */
    public String getHdfsUrl() {
        return hdfsUrl;
    }

    /**
     * setFs
     */
    public void setFs(FileSystem fs) {
        this.fs = fs;
    }

    /**
     * getFs
     */
    public FileSystem getFs() {
        return fs;
    }

    /**
     * setConf
     */
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    /**
     * getConf
     */
    public Configuration getConf() {
        return conf;
    }

    /**
     * setWorkDirectory
     */
    public void setWorkDirectory(Path workDirectory) {
        this.workDirectory = workDirectory;
    }

    /**
     * getWorkDirectory
     */
    public String getWorkDirectory() {
        return workDirectory.toString();
    }

}
