package com.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.workflow.mapper.SikuliMapper;
import com.workflow.model.params.PageParam;
import com.workflow.service.SikuliService;
import com.workflow.pojo.Sikuli;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vo.DirResult;
import vo.PageResult;
import vo.Result;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SikuliServiceImpl implements SikuliService {
    @Resource
    SikuliMapper sikuliMapper;

    @Override
    public Result listSikuli(PageParam pageParam) {
        Page<Sikuli> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Sikuli> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(Sikuli::getName,pageParam.getQueryString());
        }
        Page<Sikuli> sikuliPage = sikuliMapper.selectPage(page, queryWrapper);
        PageResult<Sikuli> pageResult = new PageResult<>();
        pageResult.setList(sikuliPage.getRecords());
        pageResult.setTotal(sikuliPage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result add(Sikuli sikuli) {
        sikuliMapper.insert(sikuli);
        return Result.success(null);
    }

    @Override
    public Result update(Sikuli sikuli) {
        sikuliMapper.updateById(sikuli);
        return Result.success(null);
    }

    @Override
    public Result delete(Long id) {
        sikuliMapper.deleteById(id);
        return Result.success(null);
    }


    @Override
    public Result getSikuliDirs() {
        List<DirResult> sikuliDirs = this.sikuliMapper.getSikuliDirs();
        return Result.success(sikuliDirs);
    }
}
