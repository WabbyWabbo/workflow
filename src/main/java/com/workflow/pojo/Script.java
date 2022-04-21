package com.workflow.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Script {

    private Long id;

    private String name;

    private String path;

    private String description;
}