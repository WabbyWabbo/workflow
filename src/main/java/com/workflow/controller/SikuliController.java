package com.workflow.controller;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.Sikuli;
import com.workflow.service.SikuliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;

@RestController
public class SikuliController {
    @Autowired
    SikuliService sikuliService;

    // sikuli
    @PostMapping("/sikuli/sikuliList")
    public Result sikuliList(@RequestBody PageParam pageParam){
        return sikuliService.listSikuli(pageParam);
    }

    @GetMapping("/getSikuliDirs")
    public Result getSikuliDirs(){
        return sikuliService.getSikuliDirs();
    }

    @PostMapping("/sikuli/add")
    public Result add(@RequestBody Sikuli sikuli){
        return sikuliService.add(sikuli);
    }

    @PostMapping("/sikuli/update")
    public Result update(@RequestBody Sikuli sikuli){
        return sikuliService.update(sikuli);
    }

    @GetMapping("/sikuli/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return sikuliService.delete(id);
    }
}
