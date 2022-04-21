package com.workflow.pojo.json;

import com.workflow.pojo.Download;
import lombok.Data;

import java.util.List;

@Data
public class DownloadVO {

    private Long   total;
    private List<Download> list;

}