
package com.sec.webs.classification.localfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * LocalFile.java
 *
 * Copyright 2014 by Samsung Electronics, Inc.,
 *
 * This software is the confidential and proprietary information of Samsung
 * Electronics, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Samsung.
 *
 * @author Fang Fang
 * @version 1.0, Dec 20, 2014.
 * @see
 * @since 1.0
 *
 */

public class LocalFile {

    /**
     *
     * readFileToGetList
     *
     * @param fileName
     * @return List<String>
     * @warning
     * @exception
     * @see
     */
    public static List<String> readFileToGetList(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            List<String> result = new ArrayList<String>();
            String content = null;
            while ((content = reader.readLine()) != null) {
                result.add(content);
            }
            reader.close();
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    /**
     *
     * appedLine
     *
     * If file not exist, append from the first line, else append from the next
     * new line.
     *
     * @param fileName
     * @param txtLine
     * @return boolean
     * @warning
     * @exception
     * @see
     */
    public static boolean appendLine(String fileName, String txtLine) {
        boolean result = false;
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                boolean success = f.createNewFile();
                if (success) {
                    BufferedWriter writer = null;
                    try {
                        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                                fileName, true)));
                        writer.write(txtLine);
                        result = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (!txtLine.startsWith("\n")) {
                txtLine = "\n" + txtLine;
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName,
                        true)));
                writer.write(txtLine);
                result = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;

    }

    /**
     *
     * appendFile Append next to the end of file, not from a new line.
     *
     * @param fileName
     * @param txtLine
     * @return boolean
     * @warning
     * @exception
     * @see
     */
    public static boolean appendContent(String fileName, String txtLine) {
        boolean result = false;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(fileName, true)));
            writer.write(txtLine);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
