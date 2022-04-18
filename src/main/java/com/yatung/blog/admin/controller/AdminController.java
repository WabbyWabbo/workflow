package com.yatung.blog.admin.controller;

import com.yatung.blog.admin.model.params.PageParam;
import com.yatung.blog.admin.pojo.Permission;
import com.yatung.blog.admin.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/scripts/scriptsList")
    public Result permissionList(@RequestBody PageParam pageParam){
        return permissionService.listPermission(pageParam);
    }

    @PostMapping("/scripts/add")
    public Result add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

    @PostMapping("/scripts/update")
    public Result update(@RequestBody Permission permission){
        return permissionService.update(permission);
    }

    @GetMapping("/scripts/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return permissionService.delete(id);
    }
}

