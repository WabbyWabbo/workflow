package com.workflow;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.workflow.resp.RestBean;
import com.workflow.util.FastJsonUtils;
import com.workflow.util.FileUtil;
import com.workflow.util.GlobalKeyListener;
import com.workflow.util.GlobalMouseListener;
import com.workflow.util.jna.MouseLLHook;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class AdminApp {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(AdminApp.class, args);
        // 运行前检查用户的data.json是否存在
        File file = new File("data.json");
        if (!file.exists()) {
            FastJsonUtils.writeFile("data.json", "{\"download\":{\"list\":[],\"total\":0},\"script\":{\"list\":[],\"total\":0},\"sikuli\":{\"list\":[],\"total\":0},\"failedHandler\":{\"list\":[],\"total\":0}}");
        }
        // 如果cache文件夹不存在则创建
        File cache = new File("cache");
        if (!cache.exists() && !cache.isDirectory()) {
            cache.mkdir();
        }
        // 如果cache/log文件夹不存在则创建
        File log = new File("cache/log");
        if (!log.exists() && !log.isDirectory()) {
            log.mkdir();
        }
        // 如果pic_temp文件夹不存在则创建
        File pic_temp = new File("C:\\pic_temp");
        if (!pic_temp.exists() && !pic_temp.isDirectory()) {
            pic_temp.mkdir();
        }

        // 临时脚本文件夹
        File script_temp = new File("C:\\script_temp");
        if (!script_temp.exists() && !script_temp.isDirectory()) {
            script_temp.mkdir();
        }
        File temp = new File("C:\\script_temp\\temp.sikuli");
        if (!temp.exists() && !temp.isDirectory()) {
            temp.mkdir();
        }
        File script = new File("C:\\script_temp\\temp.sikuli\\temp.py");
        if (!script.exists() && !script.isFile()) {
            script.createNewFile();
        }

//        // 打包成jar会遇到 URI is not hierarchical 的问题，原因未知
//        try {
//            GlobalScreen.registerNativeHook();
//        } catch (NativeHookException ex) {
//            System.err.println("There was a problem registering the native hook.");
//            System.err.println(ex.getMessage());
//            System.exit(1);
//        }
//        GlobalMouseListener globalMouseListener = new GlobalMouseListener();
//        GlobalKeyListener globalKeyListener = new GlobalKeyListener();
//        GlobalScreen.addNativeMouseListener(globalMouseListener);
//        GlobalScreen.addNativeMouseMotionListener(globalMouseListener);
//        GlobalScreen.addNativeKeyListener(globalKeyListener);

        // 替代方案
        MouseLLHook.mainF();
    }
}
