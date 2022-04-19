package com.workflow.util;

import com.workflow.resp.data.Result;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    public static void writeExcel(List<Result> results, String excelFilePath) throws IOException, WriteException {
        //开始写入excel,创建模型文件头
        String[] titleA = {"脚本名称", "运行结果", "详细信息"};
        //创建Excel文件，B库CD表文件
        SimpleDateFormat bartDateFormat =
                new SimpleDateFormat("EEEE-MMMM-dd-yyyy-HH-mm-ss");
        String fileName = "测试结果-" + bartDateFormat.format(new Date()) + ".xls";
        File fileA = new File(excelFilePath + "\\" + fileName);
        if (fileA.exists()) {
            //如果文件存在就删除
            fileA.delete();
        }

        fileA.createNewFile();
        //创建工作簿
        WritableWorkbook workbookA = Workbook.createWorkbook(fileA);
        //创建sheet
        WritableSheet sheetA = workbookA.createSheet("测试结果", 0);

        sheetA.setColumnView(2, 40);
        Label labelA;
        //设置列名
        for (int i = 0; i < titleA.length; i++) {
            labelA = new Label(i, 0, titleA[i]);
            sheetA.addCell(labelA);
        }
        //获取数据源
        for (int i = 0; i < results.size(); i++) {
            int row = i + 1;
            labelA = new Label(0, row, results.get(i).getScriptName());
            sheetA.addCell(labelA);
            labelA = new Label(1, row, results.get(i).getPass());
            if (results.get(i).getPass().equals("失败")) {
                WritableCellFormat format = new WritableCellFormat();
                format.setBackground(Colour.PINK2);
                format.setAlignment(Alignment.CENTRE);
                labelA.setCellFormat(format);
                sheetA.addCell(labelA);
            } else if (results.get(i).getPass().equals("成功")) {
                WritableCellFormat format = new WritableCellFormat();
                format.setBackground(Colour.LIGHT_GREEN);
                format.setAlignment(Alignment.CENTRE);
                format.setShrinkToFit(true);
                labelA.setCellFormat(format);
                sheetA.addCell(labelA);
            } else {
                sheetA.addCell(labelA);
            }

            labelA = new Label(2, row, results.get(i).getDetail());
            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.LEFT);
            labelA.setCellFormat(format);
            sheetA.addCell(labelA);
        }



        // 创建并写入sheetB
        String[] titleB = {"脚本名称", "运行日志"};
        //创建sheet
        WritableSheet sheetB = workbookA.createSheet("运行日志", 1);

        sheetB.setColumnView(1, 40);
        Label labelB;
        //设置列名
        for (int i = 0; i < titleB.length; i++) {
            labelB = new Label(i, 0, titleB[i]);
            sheetB.addCell(labelB);
        }
        //获取数据源
        for (int i = 0; i < results.size(); i++) {
            int row = i + 1;
            labelB = new Label(0, row, results.get(i).getScriptName());
            sheetB.addCell(labelB);

            labelB = new Label(1, row, results.get(i).getAllInfo());
            WritableCellFormat format = new WritableCellFormat();
            format.setAlignment(Alignment.LEFT);
            labelB.setCellFormat(format);
            sheetB.addCell(labelB);
        }



        workbookA.write();    //写入数据
        workbookA.close();  //关闭连接

    }
}
