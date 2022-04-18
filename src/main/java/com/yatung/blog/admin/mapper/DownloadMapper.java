package com.yatung.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yatung.blog.admin.pojo.Download;
import com.yatung.blog.admin.pojo.Sikuli;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vo.DirResult;

import java.util.List;

@Mapper
public interface DownloadMapper extends BaseMapper<Download> {
    @Select("select path value, path label from ms_download")
    List<DirResult> getDownloadDirs();
}
