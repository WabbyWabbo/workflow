package com.yatung.blog.admin.controller;

import com.yatung.blog.admin.model.params.PageParam;
import com.yatung.blog.admin.pojo.Script;
import com.yatung.blog.admin.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;

@RestController
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @PostMapping("/scripts/scriptsList")
    public Result scriptList(@RequestBody PageParam pageParam){
        return scriptService.listScript(pageParam);
    }

    @GetMapping("/getScriptsDirs")
    public Result getScriptsDirs(){
        return scriptService.getScriptsDirs();
    }

    @PostMapping("/scripts/add")
    public Result add(@RequestBody Script script){
        return scriptService.add(script);
    }

    @PostMapping("/scripts/update")
    public Result update(@RequestBody Script script){
        return scriptService.update(script);
    }

    @GetMapping("/scripts/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return scriptService.delete(id);
    }

}

