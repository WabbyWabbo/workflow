package com.workflow.util;

import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.poi.ss.usermodel.*;

public class CustomCellWriteHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        Cell cell = context.getCell();
        int rowIndex = cell.getRowIndex();
        int cellIndex = cell.getColumnIndex();

        // 自定义宽度处理

        // 自定义样式处理
        // 当前事件会在 数据设置到poi的cell里面才会回调
        // 判断不是头的情况 如果是fill 的情况 这里会==null 所以用not true
        if (BooleanUtils.isNotTrue(context.getHead())) {
            if (cell.getColumnIndex() == 1 && cell.getStringCellValue().contains("成功")) {
                // 拿到poi的workbook
                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                // 能复用的地方把他缓存起来 一个表格最多创建6W个样式
                // 不同单元格尽量传同一个 cellStyle
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(cellStyle);
                // 由于这里没有指定dataFormat 最后展示的数据 格式可能会不太正确
                // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                // cell里面去 会导致自己设置的不一样（很关键）
                context.getFirstCellData().setWriteCellStyle(null);
            } else if (cell.getColumnIndex() == 1 && cell.getStringCellValue().contains("失败")) {
                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.	ROSE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(cellStyle);
                context.getFirstCellData().setWriteCellStyle(null);
            }
        }
    }

}