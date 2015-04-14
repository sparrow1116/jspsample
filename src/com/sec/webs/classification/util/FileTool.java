package com.sec.webs.classification.util;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileTool {
	private static Log LOG = LogFactory.getLog(FileTool.class);

	public static void deleteFile(String path) {

	}

	public static List<String> readLine(String fileName, String encode) {
		return null;

	}

	public static File[] readFiles(String fileDir) {
		LOG.debug("[Classification] Start reading files in file directory: " + fileDir);
		File file = new File(fileDir);
		File[] files = null;
		if (file.isDirectory()) {
			System.out.println(file.length());
			files = file.listFiles();
			System.out.println(files.length);
		}
		return files;
	}

	public static void main(String[] args) {
		FileTool.readFiles("E:/SamplePages/www.hotel.com/html");
		
//		File file = new File("E:/SamplePages/www.imdb.com/html1");
//		for (String str : file.list()){
//			System.out.println(str);
//		}
	}
}
