
package com.sec.webs.engine;

public class EngineEntry {
    public static int startEngine(int taskID, String cTableName, double threshold) {
        // taskID from GUI, and search detail information from RDB
        // cTableName is the result of classification. Two columns include file
        // name and similarity score
        // threshold of similarity score is the foundation of classification
        // module

        WrapperParser.wrapperParse(taskID, cTableName, threshold);
        return 0;
    }
}
