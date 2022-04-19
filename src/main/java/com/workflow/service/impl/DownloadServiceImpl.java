package com.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.workflow.mapper.DownloadMapper;
import com.workflow.model.params.PageParam;
import com.workflow.pojo.Download;
import com.workflow.service.DownloadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vo.DirResult;
import vo.PageResult;
import vo.Result;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DownloadServiceImpl implements DownloadService {
    @Resource
    DownloadMapper downloadMapper;

    @Override
    public Result listDownload(PageParam pageParam) {
        Page<Download> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Download> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getQueryString())){
            queryWrapper.eq(Download::getName,pageParam.getQueryString());
        }
        Page<Download> downloadPage = downloadMapper.selectPage(page, queryWrapper);
        PageResult<Download> pageResult = new PageResult<>();
        pageResult.setList(downloadPage.getRecords());
        pageResult.setTotal(downloadPage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result add(Download download) {
        downloadMapper.insert(download);
        return Result.success(null);
    }

    @Override
    public Result update(Download download) {
        downloadMapper.updateById(download);
        return Result.success(null);
    }

    @Override
    public Result delete(Long id) {
        downloadMapper.deleteById(id);
        return Result.success(null);
    }


    @Override
    public Result getDownloadDirs() {
        List<DirResult> downloadDirs = this.downloadMapper.getDownloadDirs();
        return Result.success(downloadDirs);
    }
}
