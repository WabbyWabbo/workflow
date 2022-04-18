package com.yatung.blog.admin.service;


import com.yatung.blog.admin.model.params.PageParam;
import com.yatung.blog.admin.pojo.Script;
import vo.Result;

public interface ScriptService {
    Result listScript(PageParam pageParam);

    Result add(Script script);

    Result update(Script script);

    Result delete(Long id);

    Result getScriptsDirs();
}
