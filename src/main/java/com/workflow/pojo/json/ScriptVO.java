package com.workflow.pojo.json;

import com.workflow.pojo.Script;
import lombok.Data;

import java.util.List;

@Data
public class ScriptVO {

    private Long   total;
    private Long recent;
    private List<Script> list;

}