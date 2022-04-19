package com.workflow.service;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.Sikuli;
import vo.Result;

public interface SikuliService {
    Result listSikuli(PageParam pageParam);

    Result add(Sikuli sikuli);

    Result update(Sikuli sikuli);

    Result delete(Long id);

    Result getSikuliDirs();
}
