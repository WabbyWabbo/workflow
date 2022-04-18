package com.yatung.blog.admin.service;

import com.yatung.blog.admin.model.params.PageParam;
import com.yatung.blog.admin.pojo.Sikuli;
import vo.Result;

public interface SikuliService {
    Result listSikuli(PageParam pageParam);

    Result add(Sikuli sikuli);

    Result update(Sikuli sikuli);

    Result delete(Long id);

    Result getSikuliDirs();
}
