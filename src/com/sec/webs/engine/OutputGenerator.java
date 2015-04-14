package com.sec.webs.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;

import com.csvreader.CsvWriter;

public class OutputGenerator {

	// create csv file
	public static void createOutputFile(String outPath) {
		try {
			if (!(new File(outPath).isDirectory())) {
				new File(outPath).mkdir();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}

	}

	// Save as CSV files
	public static boolean writeResultToFile(String filePath, Set<String> set,
			String[] result) {
		boolean alreadyExists = new File(filePath).exists();
		try {
			// CsvWriter cw = new CsvWriter(new FileWriter(filePath, true),',');
			OutputStream out = new FileOutputStream(new File(filePath), true);
			CsvWriter cw = new CsvWriter(out, ',', Charset.forName("UTF-8"));

			if (!alreadyExists) {
				for (String item : set) {
					System.out.println(item);
					cw.write(item);
				}
				cw.endRecord();
			}
			for (int i = 0; i < result.length; i++)
				cw.write(result[i]);

			cw.endRecord();
			cw.flush();
			cw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// save as JSON files
	public static void writeResultToJson(String outPath, Set<String> set,
			String[] result) {
		boolean alreadyExists = new File(outPath).exists();
		try {
			CsvWriter cw = new CsvWriter(new FileWriter(outPath, true), ',');
			if (!alreadyExists) {
				for (String item : set) {
					System.out.println(item);
					cw.write(item);
				}
				cw.endRecord();
			}
			for (int i = 0; i < result.length; i++)
				cw.write(result[i]);

			cw.endRecord();
			cw.flush();
			cw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
