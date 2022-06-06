package com.workflow.controller;

import com.workflow.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vo.Result;

import java.util.HashMap;

@RestController
public class EditorController {
    @Autowired
    EditorService editorService;

    @PostMapping("/editor/editScript")
    public Result editScript(@RequestBody HashMap<String, String> map){
        return editorService.getCodeContent(map);
    }

    @PostMapping("/editor/saveScript")
    public Result saveScript(@RequestBody HashMap<String, String> map){
        return editorService.saveContent(map);
    }

    @PostMapping("/editor/capture")
    public Result snipPicture(@RequestBody HashMap<String, String> map){
        return editorService.capture(map);
    }

    @PostMapping("/editor/previewPic")
    public Result previewPicture(@RequestBody HashMap<String, String> map){
        return editorService.previewPicture(map);
    }

    @PostMapping("/editor/createScript")
    public Result createScript(@RequestBody HashMap<String, String> map){
        return editorService.createScript(map);
    }

    @PostMapping("/editor/saveScriptToTemp")
    public Result saveScriptToTemp(@RequestBody HashMap<String, String> map){
        return editorService.saveScriptToTemp(map);
    }

    @PostMapping("/editor/newScript")
    public Result newScript(){
        return editorService.newScript();
    }


}
