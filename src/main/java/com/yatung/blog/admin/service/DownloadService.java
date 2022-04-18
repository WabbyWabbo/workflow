package com.yatung.blog.admin.service;

import com.yatung.blog.admin.model.params.PageParam;
import com.yatung.blog.admin.pojo.Download;
import vo.Result;

public interface DownloadService {
    Result listDownload(PageParam pageParam);

    Result add(Download download);

    Result update(Download download);

    Result delete(Long id);

    Result getDownloadDirs();
}
