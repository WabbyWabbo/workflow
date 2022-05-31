package com.workflow.util;

import com.alibaba.excel.EasyExcel;
import com.workflow.resp.data.Result;
import org.apache.poi.ss.SpreadsheetVersion;
import org.joda.time.DateTime;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class EasyExcelUtil {

    public static void writeExcel(List<Result> results, String excelFilePath) throws IOException{
        DateTime dateTime = new DateTime();
        String fileName = "测试结果-" + dateTime.toString("yyyy年MM月dd日HH时mm分ss秒") + ".xlsx";
        File file = new File(excelFilePath + "\\" + fileName);
        if (file.exists()) {
            //如果文件存在就删除
            file.delete();
        }

        // fix: IllegalArgumentException: The maximum length of cell contents (text) is 32,767 characters
        SpreadsheetVersion excel2007 = SpreadsheetVersion.EXCEL2007;
        if (Integer.MAX_VALUE != excel2007.getMaxTextLength()) {
            Field field;
            try {
                // SpreadsheetVersion.EXCEL2007的_maxTextLength变量
                field = excel2007.getClass().getDeclaredField("_maxTextLength");
                // 关闭反射机制的安全检查，可以提高性能
                field.setAccessible(true);
                // 重新设置这个变量属性值
                field.set(excel2007,Integer.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        EasyExcel.write(excelFilePath + "\\" + fileName,Result.class)
                .registerWriteHandler(new CustomCellWriteHandler())
                .sheet("测试结果")
                .doWrite(results);

    }
}
