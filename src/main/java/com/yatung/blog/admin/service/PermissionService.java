package com.yatung.blog.admin.service;


import com.yatung.blog.admin.model.params.PageParam;
import com.yatung.blog.admin.pojo.Permission;
import vo.Result;

public interface PermissionService {
    Result listPermission(PageParam pageParam);

    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);
}
