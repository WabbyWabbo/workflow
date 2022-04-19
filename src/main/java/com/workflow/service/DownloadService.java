package com.workflow.service;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.Download;
import vo.Result;

public interface DownloadService {
    Result listDownload(PageParam pageParam);

    Result add(Download download);

    Result update(Download download);

    Result delete(Long id);

    Result getDownloadDirs();
}
