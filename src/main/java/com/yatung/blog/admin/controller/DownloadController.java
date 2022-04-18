package com.yatung.blog.admin.controller;

import com.yatung.blog.admin.model.params.PageParam;
import com.yatung.blog.admin.pojo.Download;
import com.yatung.blog.admin.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;

@RestController
public class DownloadController {
    @Autowired
    DownloadService downloadService;

    // download
    @PostMapping("/download/downloadList")
    public Result downloadList(@RequestBody PageParam pageParam){
        return downloadService.listDownload(pageParam);
    }

    @GetMapping("/getDownloadDirs")
    public Result getDownloadDirs(){
        return downloadService.getDownloadDirs();
    }

    @PostMapping("/download/add")
    public Result add(@RequestBody Download download){
        return downloadService.add(download);
    }

    @PostMapping("/download/update")
    public Result update(@RequestBody Download download){
        return downloadService.update(download);
    }

    @GetMapping("/download/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return downloadService.delete(id);
    }
}
