package com.workflow.controller;

import com.workflow.model.params.PageParam;
import com.workflow.pojo.FailedHandler;
import com.workflow.service.FailedHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;

@RestController
public class FailedHandlerController {
    @Autowired
    FailedHandlerService failedHandlerService;

    // failedHandler
    @PostMapping("/failedHandler/failedHandlerList")
    public Result failedHandlerList(@RequestBody PageParam pageParam){
        return failedHandlerService.listFailedHandler(pageParam);
    }

    @GetMapping("/getFailedHandlerDirs")
    public Result getFailedHandlerDirs(){
        return failedHandlerService.getFailedHandlerDirs();
    }

    @PostMapping("/failedHandler/add")
    public Result add(@RequestBody FailedHandler failedHandler){
        return failedHandlerService.add(failedHandler);
    }

    @PostMapping("/failedHandler/update")
    public Result update(@RequestBody FailedHandler failedHandler){
        return failedHandlerService.update(failedHandler);
    }

    @GetMapping("/failedHandler/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return failedHandlerService.delete(id);
    }
}
