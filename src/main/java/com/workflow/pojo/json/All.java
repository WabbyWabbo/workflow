package com.workflow.pojo.json;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 对应于整个data.json文件
 */
@Data
public class All {

    private SikuliVO sikuli;
    private ScriptVO script;
    private DownloadVO download;
    private FailedHandlerVO failedHandler;
}