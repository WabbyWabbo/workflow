package com.workflow.service;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.Download;
import vo.Result;

import java.util.HashMap;

public interface EditorService {
    Result getCodeContent(HashMap<String, String> map);

    Result saveContent(HashMap<String, String> map);

    Result capture(HashMap<String, String> map);

    Result previewPicture(HashMap<String, String> map);

    Result createScript(HashMap<String, String> map);

    Result newScript();

    Result saveScriptToTemp(HashMap<String, String> map);
}
