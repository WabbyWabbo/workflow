package com.workflow.pojo.json;

import com.workflow.pojo.FailedHandler;
import lombok.Data;

import java.util.List;

@Data
public class FailedHandlerVO {

    private Long   total;
    private List<FailedHandler> list;

}