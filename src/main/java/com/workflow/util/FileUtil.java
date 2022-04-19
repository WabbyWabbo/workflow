package com.workflow.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    /**
     * @Author：
     * @Description：获取某个目录下所有脚本
     * @Date：
     */
    public static List<File> getFiles(String path) {
        List<File> files = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isDirectory() && tempList[i].toString().endsWith(".sikuli")) {
                files.add(tempList[i]);
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了
            }
        }
        return files;
    }


}
