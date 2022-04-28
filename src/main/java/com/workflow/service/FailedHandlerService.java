package com.workflow.service;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.FailedHandler;
import vo.Result;

public interface FailedHandlerService {
    Result listFailedHandler(PageParam pageParam);

    Result add(FailedHandler failedHandler);

    Result update(FailedHandler failedHandler);

    Result delete(Long id);

    Result getFailedHandlerDirs();
}
