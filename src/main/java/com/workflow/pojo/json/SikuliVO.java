package com.workflow.pojo.json;

import com.workflow.pojo.Sikuli;
import lombok.Data;

import java.util.List;

@Data
public class SikuliVO {

    private Long   total;
    private List<Sikuli> list;

}