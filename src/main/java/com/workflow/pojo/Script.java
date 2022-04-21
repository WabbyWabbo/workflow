package com.workflow.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Script {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String path;

    private String description;
}