package com.yatung.blog.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Download {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String path;

    private String description;
}
