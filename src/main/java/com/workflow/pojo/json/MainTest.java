package com.workflow.pojo.json;

import com.alibaba.fastjson.JSON;
import com.workflow.pojo.Download;
import com.workflow.pojo.Script;
import com.workflow.pojo.Sikuli;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        All all = new All();

        SikuliVO sikuliVO = new SikuliVO();
        sikuliVO.setTotal(1L);
        List<Sikuli> list = new ArrayList<>();
        list.add(new Sikuli(1L,"dsaf","dafsdf","dafdsf"));
        sikuliVO.setList(list);

        ScriptVO scriptVO = new ScriptVO();
        scriptVO.setTotal(1L);
        List<Script> list1 = new ArrayList<>();
        list1.add(new Script(1L,"dsaf","dafsdf","dafdsf"));
        scriptVO.setList(list1);

        DownloadVO downloadVO = new DownloadVO();
        downloadVO.setTotal(1L);
        List<Download> list2 = new ArrayList<>();
        list2.add(new Download(1L,"dsaf","dafsdf","dafdsf"));
        downloadVO.setList(list2);


        all.setSikuli(sikuliVO);
        all.setScript(scriptVO);
        all.setDownload(downloadVO);

        // 转换为 JSON
        String jsonString = JSON.toJSONString(all);
        System.out.println("JSON字符串：" + jsonString);

        // 转换为 对象BEAN
        All aall = JSON.parseObject(jsonString, All.class);
        System.out.println("JavaBean对象：" + aall);
    }
}