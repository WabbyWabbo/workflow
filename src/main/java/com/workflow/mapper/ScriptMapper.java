package com.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.workflow.pojo.Script;
import org.apache.ibatis.annotations.Select;
import vo.DirResult;

import java.util.List;

public interface ScriptMapper extends BaseMapper<Script> {
    @Select("select path value, path label from ms_script")
    List<DirResult> getScriptsDirs();
}