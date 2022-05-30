package com.workflow.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    /**
     * @Author：
     * @Description：获取某个目录下所有.sikuli脚本
     * @Date：
     */
    public static List<File> getScripts(String path) {
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

    /**
     * 找出该目录下的.py文件内容
     *
     * @param path
     * @return
     */
    public static String getScriptCode(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile() && tempList[i].toString().endsWith(".py")) {
                return readFile(tempList[i]);
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了
            }
        }
        return null;
    }

    public static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            char[] buffer = new char[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                sb.append(buffer,0,len);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return sb.toString();
    }

    public static boolean saveScript(String scriptDir, String content) {
        File script = new File(scriptDir);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(script))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
