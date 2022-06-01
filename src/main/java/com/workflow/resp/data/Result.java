package com.workflow.resp.data;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@HeadRowHeight(20)
public class Result {
    @ExcelProperty("脚本名称")
    @ColumnWidth(25)
    String scriptName;
    @ExcelProperty("运行结果")
    @ColumnWidth(12)
    String pass;
    @ExcelProperty("详细信息")
    @ColumnWidth(65)
    String detail;
    @ExcelProperty("运行日志")
    @ColumnWidth(65)
    String allInfo;
}
