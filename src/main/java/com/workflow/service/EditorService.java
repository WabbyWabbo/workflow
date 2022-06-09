package com.workflow.service;

import vo.Result;

import java.util.HashMap;

public interface EditorService {
    Result viewScript(HashMap<String, String> map);

    Result saveContent(HashMap<String, String> map);

    Result capture(HashMap<String, String> map);

    Result previewPicture(HashMap<String, String> map);

    Result createRealScript(HashMap<String, String> map);

    Result createTempScript();

    Result saveScriptToTemp(HashMap<String, String> map);

    Result runScript(HashMap<String, String> map);
}
