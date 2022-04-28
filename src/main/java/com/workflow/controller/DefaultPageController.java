package com.workflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultPageController {
    @RequestMapping("/")
    public String toHomePage() {
        return "../static/pages/index";
    }

    @RequestMapping("/runPage")
    public String toRunPage() {
        return "../static/pages/run";
    }

    @RequestMapping("/sikuliPage")
    public String toSikuliPage() {
        return "../static/pages/sikuli";
    }

    @RequestMapping("/scriptPage")
    public String toScriptPage() {
        return "../static/pages/scripts";
    }

    @RequestMapping("/downloadPage")
    public String toDownloadPage() {
        return "../static/pages/download";
    }

    @RequestMapping("/failedHandlerPage")
    public String toFailedHandlerPage() {
        return "../static/pages/failedHandler";
    }
}
