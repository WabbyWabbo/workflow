package com.workflow.service.impl;

import com.workflow.service.EditorService;
import com.workflow.util.FileUtil;
import org.springframework.stereotype.Service;
import vo.Result;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Service
public class EditorServiceImpl implements EditorService {

    @Override
    public Result getCodeContent(HashMap<String, String> map) {
        String scriptDir = map.get("scriptsPath") + "\\" + map.get("name") + ".sikuli";

        String content = FileUtil.getScriptCode(scriptDir);

        return Result.success(content);
    }

    @Override
    public Result saveContent(HashMap<String, String> map) {
        String scriptDir = map.get("scriptsPath") + "\\" + map.get("scriptName")+".sikuli\\"+map.get("scriptName")+".py";
        String content = map.get("editorContent");
        boolean b = FileUtil.saveScript(scriptDir, content);
        if (b)
            return Result.success("ok");
        else
            return Result.fail(500,"写入文件失败");
    }
}
