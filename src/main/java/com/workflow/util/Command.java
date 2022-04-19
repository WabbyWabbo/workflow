package com.workflow.util;

import com.workflow.resp.data.Result;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
public class Command {

    /**
     * 将一个脚本的执行结果转化为Result对象
     *
     * @param
     * @return
     */
    public static Result execScriptToResult(String sikuliPath, String scriptName) {
        String commandStr = "java -Dsikuli.FromCommandLine -jar "
                + sikuliPath + " -r "
                + scriptName;
        String resultString = exeCmd(commandStr);


        Result result = new Result();
        result.setScriptName(scriptName);
        result.setPass("成功");
        result.setAllInfo(resultString);

        if (resultString.equals("UNKNOWN ERROR")) {
            result.setPass("未知");
            result.setDetail("UNKNOWN ERROR");
            return result;
        }

        if (resultString.isEmpty()) {
            result.setPass("未知");
            result.setDetail("没有任何信息");
            return result;
        }

        if (resultString.contains("RuntimeError") || resultString.contains("No such file or directory")) {
            result.setPass("错误");
            result.setDetail(resultString);
            return result;
        }

        if (resultString.contains("error")) {
            result.setPass("失败");


            Pattern pattern1 = Pattern.compile("in line (.*?)\n");
            Matcher matcher1 = pattern1.matcher(resultString);
            String errorLine = "";

            if (resultString.contains("Failed")) {
                if (matcher1.find()) {
                    errorLine = matcher1.group(1);
                }
                Pattern pattern2 = Pattern.compile("\n\\[error\\] (.*?)Failed");
                Matcher matcher2 = pattern2.matcher(resultString);
                String errorType = "";
                if (matcher2.find()) {
                    errorType = matcher2.group(1);
                }
                String tempDetail = "错误发生在第" + errorLine + "行，错误信息： " + errorType + "Failed";
                result.setDetail(tempDetail);
                return result;
            } else if (resultString.contains("ImportError")) {
                if (matcher1.find()) {
                    errorLine = matcher1.group(1);
                }
                String tempDetail = "错误发生在第" + errorLine + "行，错误信息： " + "ImportError";
                result.setDetail(tempDetail);
                return result;
            } else {
                result.setDetail("错误信息:\n" + resultString);
                return result;
            }
        }

        // sikuli没报错，但用户自定义的判断未通过，用户打印了[Error].....
        if (resultString.contains("[Error]")) {
            result.setPass("失败");
//            Pattern pattern = Pattern.compile("\\[Error\\](.*?)\\n");
//            Matcher matcher = pattern.matcher(resultString);
//            StringBuilder sb = new StringBuilder();
//            while (matcher.find()) {
//                sb.append("[Error]").append(matcher.group(1)).append("\n");
//            }
//            result.setDetail(sb.toString());
//            return result;
            String[] lines = resultString.split("\n");
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                if (line.startsWith("[Error]")) {
                    sb.append(line).append("\n");
                }
            }
            String temp = sb.toString();
            if (temp.endsWith("\n")) temp = temp.substring(0,temp.length()-1);
            result.setDetail(sb.toString());
            return result;
        }

        // sikuli没报错，且用户自定义打印了成功信息[Success].....
        if (resultString.contains("[Success]")) {
            result.setPass("成功");
            String[] lines = resultString.split("\n");
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                if (line.startsWith("[Success]")) {
                    sb.append(line).append("\n");
                }
            }
            String temp = sb.toString();
            if (temp.endsWith("\n")) temp = temp.substring(0,temp.length()-1);
            result.setDetail(sb.toString());
            return result;
        }

        return result;
    }

    public static String exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {

                log.info(line);
                // [DEBUG]不计入有效信息
                if (!line.startsWith("[DEBUG STARTUP]")) {
                    sb.append(line + "\n");
                }

                //处理.pyc wrong,否则会卡在这
                if (line.contains("RuntimeError")) {
                    p.destroy();
                }
                if (line.contains("No such file or directory")) {
                    p.destroy();
                }

            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "UNKNOWN ERROR";
    }

}