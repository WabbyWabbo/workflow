package com.workflow.service;


import com.workflow.model.params.PageParam;
import com.workflow.pojo.Script;
import vo.Result;

public interface ScriptService {
    Result listScript(PageParam pageParam);

    Result add(Script script);

    Result update(Script script);

    Result delete(Long id);

    Result getScriptsDirs();
}
