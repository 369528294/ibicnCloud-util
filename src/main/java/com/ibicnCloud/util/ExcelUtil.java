package com.ibicnCloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {
    private static Logger logger = Logger.getLogger(ExcelUtil.class);

    public static Sheet readExcel(File file, Sheet rs) throws FileNotFoundException, IOException, BiffException {
        Workbook rwb = null;
        InputStream is = null;
        is = new FileInputStream(file);
        rwb = Workbook.getWorkbook(is);
        String[] sheetnames = rwb.getSheetNames();
        rs = rwb.getSheet(sheetnames[0]);
        is.close();
        is = null;
        return rs;
    }

    public static int createdExcel(String PATH, List list, String title, String[] rowsName) {
        return createdExcel(PATH, list, title, rowsName, "");
    }

    public static int createdExcel(String PATH, List list, String title, String[] rowsName, String merged) {
        try {
            File myFile = new File(PATH);
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            WritableWorkbook wbook = Workbook.createWorkbook(myFile); // 创建一个可写返回工作薄同给定文件名
            WritableSheet wsheet = wbook.createSheet(title, 0); // sheet名称

            // 设置字体
            WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD);
            WritableCellFormat wcfFC = new WritableCellFormat(wfont);
            wcfFC.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); // 单元格边缘线格式设置
            wcfFC.setAlignment(jxl.format.Alignment.CENTRE); // 居中对齐
            wcfFC.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 垂直居中
            // wcfFC.setBackground(jxl.format.Colour.BLUE); // 蓝色底
            // 设置行高和列宽
            // wsheet.setColumnView(列数, 列宽);
            // wsheet.setRowView(行数, 行高);

            // 开始生成主体内容
            for (int i = 0; i < StringUtil.size(rowsName); i++) {
                wsheet.addCell(new Label(i, 0, rowsName[i], wcfFC));
                wsheet.setColumnView(i, 12);
            }
            wfont = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD);
            wcfFC = new WritableCellFormat(wfont);
            wcfFC.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); // 单元格边缘线格式设置

            // 是数字时的格式化
            // jxl.write.NumberFormat numberFormat = new jxl.write.NumberFormat(NumberFormat.CURRENCY_DOLLAR);
            // jxl.write.WritableCellFormat wcfFCNUMBER = new jxl.write.WritableCellFormat(wfont,numberFormat);
            // wcfFCNUMBER.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); //单元格边缘线格式设置
            // wcfFCNUMBER.setAlignment(jxl.format.Alignment.CENTRE); // 居中对齐
            // wcfFCNUMBER.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); //垂直居中
            // 是数字时的格式化

            wcfFC.setWrap(true);
            for (int i = 0; i < CollectionUtil.size(list); i++) {
                String[] args = (String[]) list.get(i);
                for (int j = 0; j < StringUtil.size(args); j++) {
                    if (StringUtil.size(args[j])<15 && StringUtil.isFloat(args[j]) ) {
                        wsheet.addCell(new jxl.write.Number(j, i+1, Float.parseFloat(args[j]), wcfFC));
                    } else {
                        wsheet.addCell(new Label(j, i+1, args[j], wcfFC));
                    }
                }
                // 打印分页符
                if (i % 20 == 0) {
                    // wsheet.addRowPageBreak(i);
                }
            }
            // 合并单元格操作
            if (!StringUtil.format(merged).equals("")) {
                String[] mergeds = merged.split(";");
                for (int i = 0; i < StringUtil.size(mergeds); i++) {
                    String[] mergedsTwo = mergeds[i].split(",");
                    wsheet.mergeCells(StringUtil.parseInt(mergedsTwo[0]), StringUtil.parseInt(mergedsTwo[1]), StringUtil.parseInt(mergedsTwo[2]), StringUtil.parseInt(mergedsTwo[3]));
                }
            }
            // 主体内容生成结束
            wbook.write(); // 写入文件
            wbook.close();
            return 1;
        } catch (Exception ex) {
            logger.error("创建excel文件错误！", ex);
            return 0;
        }
    }
}
