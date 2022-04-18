package com.yatung.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yatung.blog.admin.mapper.ScriptMapper;
import com.yatung.blog.admin.model.params.PageParam;
import com.yatung.blog.admin.pojo.Script;
import com.yatung.blog.admin.service.ScriptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vo.DirResult;
import vo.PageResult;
import vo.Result;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ScriptServiceImpl implements ScriptService {

    @Resource
    private ScriptMapper scriptMapper;

    @Override
    public Result listScript(PageParam pageParam) {
        Page<Script> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Script> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(Script::getName,pageParam.getQueryString());
        }
        Page<Script> scriptPage = scriptMapper.selectPage(page, queryWrapper);
        PageResult<Script> pageResult = new PageResult<>();
        pageResult.setList(scriptPage.getRecords());
        pageResult.setTotal(scriptPage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result add(Script script) {
        this.scriptMapper.insert(script);
        return Result.success(null);
    }

    @Override
    public Result update(Script script) {
        this.scriptMapper.updateById(script);
        return Result.success(null);
    }

    @Override
    public Result delete(Long id) {
        this.scriptMapper.deleteById(id);
        return Result.success(null);
    }

    @Override
    public Result getScriptsDirs() {
        List<DirResult> scriptsDirs = this.scriptMapper.getScriptsDirs();
        return Result.success(scriptsDirs);
    }

}
