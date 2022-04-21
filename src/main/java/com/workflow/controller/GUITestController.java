package com.workflow.controller;

import com.workflow.util.Command;
import com.workflow.util.ExcelUtil;
import com.workflow.util.FileUtil;
import com.workflow.resp.RestBean;
import com.workflow.resp.data.Result;
import jxl.write.WriteException;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Log
public class GUITestController {

    String sikuliPath;
    String scriptsPath;
    String downloadPath;
    Robot robot = null;
    boolean needStop;
    boolean currentScriptFinished;

    List<String> scriptsFullPath = new ArrayList<>();
    List<String> scriptsName = new ArrayList<>();
    List<Result> results = new ArrayList<>();

    @PostMapping("/setSikuliPath")
    public RestBean<Void> setSikuliPath(@RequestBody HashMap<String, String> map) {

        if (map.containsKey("sikuliPath"))
            this.sikuliPath = map.get("sikuliPath");
        log.info("sikuliPath: " + sikuliPath);

        return new RestBean<>(200, "set sikuliPath success");
    }


    @PostMapping("/setScriptsPath")
    public RestBean<List<String>> setScriptsPath(@RequestBody HashMap<String, String> map) {

        this.scriptsPath = map.get("scriptsPath");

        if (scriptsPath.equals("")) {
            return new RestBean<>(500, "脚本目录不能为空!");
        }

        List<File> files = FileUtil.getFiles(scriptsPath);
        scriptsFullPath.clear();
        scriptsName.clear();
        //后端保存全路径名,只返回脚本名称给前端
        files.forEach(file -> {
            scriptsFullPath.add(file.getAbsolutePath());
            scriptsName.add(file.getName().replace(".sikuli", ""));
        });


        return new RestBean<>(200, "set scriptsPath success", scriptsName);
    }


    @PostMapping("/testSelected")
    public RestBean<List<Result>> testAllScripts(@RequestBody HashMap<String, Object> map) {
        needStop = false;
        ArrayList<Integer> selected = (ArrayList<Integer>) map.get("selected");

        scriptsPath = (String) map.get("scriptsPath");
        sikuliPath = (String) map.get("sikuliPath");

        //运行前检查用户设置的sikuli.jar是否存在
        File file = new File(sikuliPath);
        if (!file.exists()) {
            return new RestBean<>(500, "sikuli路径设置错误");
        } else if (!file.isFile()) {
            return new RestBean<>(500, "请指定到具体的sikuli.jar文件, 而不是其目录.");
        } else {
            log.info("sikuliPath checked!!! Ready to run...");
        }

        // 若仅重启服务端，有些数据被清掉，这里恢复一下
        if (scriptsFullPath.size() == 0 || scriptsName.size() == 0) {
            List<File> files = FileUtil.getFiles(scriptsPath);
            files.forEach(f -> {
                scriptsFullPath.add(f.getAbsolutePath());
                scriptsName.add(f.getName().replace(".sikuli", ""));
            });
        }

        if (selected.isEmpty()) {
            return new RestBean<>(500, "无数据,请重新选择脚本");
        }

        // 最小化浏览器界面
        try {
            robot = new Robot();
            robot.setAutoDelay(1000);

            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyPress(KeyEvent.VK_N);

            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyRelease(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_N);
        } catch (AWTException e) {
            e.printStackTrace();
            //TODO 前端要处理299响应
            return new RestBean<>(299, "最小化失败，请手动最小化");
        }


        // 开始逐个运行脚本
        results.clear();
        for (int i = 0; i < selected.size(); i++) {

            // 用户若点击了停止按钮，则不继续执行下一个脚本
            if (needStop) {
                break;
            }

            int target = selected.get(i) - 1;   //前端从1编号
            log.info(scriptsName.get(target) + " test start");
            Result result = Command.execScriptToResult(sikuliPath, scriptsFullPath.get(target));
            if (result.getPass().equals("错误")) {
                return new RestBean<>(500, " can't open file 'imageMatch.pyc'");
            }
            log.info(scriptsName.get(target) + " test end");
            result.setScriptName(scriptsName.get(target));
            results.add(result);

        }

        // 未被中途强行停止，全部执行完毕，切回浏览器并弹出系统提示框
        if (!needStop){
//            robot.keyPress(KeyEvent.VK_ALT);
//            robot.delay(1000);
//            robot.keyPress(KeyEvent.VK_TAB);
//            robot.keyRelease(KeyEvent.VK_ALT);
//            robot.keyRelease(KeyEvent.VK_TAB);

            JOptionPane.showMessageDialog(null, "脚本全部执行结束！", "运行完成", JOptionPane.PLAIN_MESSAGE);
        }

        return new RestBean<List<Result>>(200, "see data for detail", results);
    }

    @RequestMapping("/download")
    public RestBean downloadResults(@RequestBody HashMap<String, String> map) {
        downloadPath = map.get("downloadPath");

        File file = new File(downloadPath);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }

        try {
            ExcelUtil.writeExcel(results, downloadPath);
        } catch (IOException e) {
            return new RestBean(500, "下载路径设置错误");
        } catch (WriteException e) {
            return new RestBean(500, "无法写入文件");
        }


        return new RestBean(200, "下载成功");
    }

    @RequestMapping("/stop")
    public RestBean stop() {
        needStop = true;

        // 按 Alt + Shift + c 立即结束正在运行的Sikuli脚本
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_C);

        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_C);

        return new RestBean(200,"成功停止");
    }
}

