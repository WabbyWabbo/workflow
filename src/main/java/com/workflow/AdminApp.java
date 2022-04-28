package com.workflow;

import com.workflow.resp.RestBean;
import com.workflow.util.FastJsonUtils;
import com.workflow.util.FileUtil;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class AdminApp {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(AdminApp.class,args);
        // 运行前检查用户的data.json是否存在
        File file = new File("data.json");
        if (!file.exists()) {
            FastJsonUtils.writeFile("data.json","{\"download\":{\"list\":[],\"total\":0},\"script\":{\"list\":[],\"total\":0},\"sikuli\":{\"list\":[],\"total\":0},\"failedHandler\":{\"list\":[],\"total\":0}}");
        }
        // 如果cache文件夹不存在则创建
        File cache = new File("cache");
        if (!cache.exists() && !cache.isDirectory()) {
            cache.mkdir();
        }
    }
}
