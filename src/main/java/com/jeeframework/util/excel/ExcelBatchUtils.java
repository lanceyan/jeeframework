package com.jeeframework.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.jeeframework.util.validate.Validate;

/**
 * 批量操作excel的类，用于生成excel格式数据。2007
 * 
 * 18000行一个sheet，一个文件放4个sheet，不然需要更大的内存
 * 
 * 最终将所有excel文件打包，每次需要新new一个excelUtils
 * 
 * @author Administrator
 * 
 */
public class ExcelBatchUtils {

    public final static int MAX_SHEET_COUNT = 4;// 最多4个sheet，防止sheet过大需要内存过多
    public final static int MAX_SHEET_ROW_COUNT = 15000; // 18000;//

    private int defaultMaxSheetCount = MAX_SHEET_COUNT;
    private int defaultMaxSheetRowCount = MAX_SHEET_ROW_COUNT;

    // 一个sheet最多放18000条数据，超过了需要更大的内存

    public final static String EXCEL_SUFFIX = ".xlsx"; // excel后缀

    private int curSheetCount = 0;// sheet个数
    private int curSheetRowCount = 0; // sheet行个数
    private int fileCount = 0; // 文件个数

    private String excelFileDir;
    private String sheetName;
    private String excelName;

    private SXSSFWorkbook curWorkBook = null;
    private Sheet curSheet = null;
    private Row curSheetRow = null;

    public ExcelBatchUtils(final String excelFileDir, final String excelName, final String sheetName) {
        this.excelFileDir = excelFileDir;
        File excelFileDirFile = new File(this.excelFileDir);
        if (!excelFileDirFile.exists()) {
            excelFileDirFile.mkdirs();
        }
        this.sheetName = sheetName;
        this.excelName = excelName;
    }

    public void writeExcelData(Object[] dataTitleArr, Object[] dataArr) throws IOException {

        if (curWorkBook == null) {
            curWorkBook = new SXSSFWorkbook();
        }

        if (Validate.isEmpty(dataTitleArr) || Validate.isEmpty(dataArr)) {
            return;
        }

        if (curSheetRowCount > defaultMaxSheetRowCount) {
            if (curSheetCount >= defaultMaxSheetCount) {

                curSheetCount = 0;
                curSheetRowCount = 0;

                String destFile = excelFileDir + File.separator + excelName + "_" + fileCount + EXCEL_SUFFIX;

                FileOutputStream fileOut = new FileOutputStream(destFile);

                curWorkBook.write(fileOut);
                fileOut.close();

                curWorkBook = new SXSSFWorkbook();
                fileCount++;

            }

            // 分sheet，执行不完报oom
            curSheet = curWorkBook.createSheet(sheetName + curSheetCount);
            curSheetCount = curSheetCount + 1;
            curSheetRowCount = 0;
            curSheetRow = curSheet.createRow(curSheetRowCount);

        }

        if (curSheetCount == 0) {
            curSheet = curWorkBook.createSheet(sheetName + curSheetCount);
            curSheetCount++;
        }

        if (curSheetRowCount == 0) {

            curSheetRow = curSheet.createRow(curSheetRowCount);
            curSheetRowCount++;
            int cellCount = dataTitleArr.length;

            for (int iCellCount = 0; iCellCount < cellCount; iCellCount++) {

                Cell cell = curSheetRow.createCell(iCellCount);
                cell.setCellValue("" + dataTitleArr[iCellCount]);
            }
        }

        curSheetRow = curSheet.createRow(curSheetRowCount);
        curSheetRowCount++;

        int cellCount = dataArr.length;

        for (int iCellCount = 0; iCellCount < cellCount; iCellCount++) {

            Cell cell = curSheetRow.createCell(iCellCount);
            cell.setCellValue("" + dataArr[iCellCount]);
        }

    }

    public void flushExcel() throws IOException {

        if (curWorkBook != null) {
            String destFile = excelFileDir + File.separator + excelName + "_" + fileCount + EXCEL_SUFFIX;

            FileOutputStream fileOut = new FileOutputStream(destFile);

            curWorkBook.write(fileOut);

            fileOut.close();
        }

        curSheetCount = 0;
        curSheetRowCount = 0;
        fileCount = 0;
    }

    public static void main(String[] args) throws IOException {

        ExcelBatchUtils excelBatchUtils = new ExcelBatchUtils("D:\\log\\excel", "excelName", "sheetName");
        excelBatchUtils.setDefaultMaxSheetRowCount(10000);
        excelBatchUtils.setDefaultMaxSheetCount(1);
        String[] iArr = { "t1", "t2", "t3", "t4" };
        for (int i = 0; i < 200000; i++) {
            Integer[] iArr1 = { i, i, i, i };
            excelBatchUtils.writeExcelData(iArr, iArr1);
        }
         excelBatchUtils.flushExcel();

        //        ZipUtil.zip("D:", "1.zip", "D:\\apiretweet");

    }

    public String getExcelFileDir() {
        return excelFileDir;
    }

    public void setExcelFileDir(String excelFileDir) {
        this.excelFileDir = excelFileDir;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public int getDefaultMaxSheetCount() {
        return defaultMaxSheetCount;
    }

    public void setDefaultMaxSheetCount(int defaultMaxSheetCount) {
        this.defaultMaxSheetCount = defaultMaxSheetCount;
    }

    public int getDefaultMaxSheetRowCount() {
        return defaultMaxSheetRowCount;
    }

    public void setDefaultMaxSheetRowCount(int defaultMaxSheetRowCount) {
        this.defaultMaxSheetRowCount = defaultMaxSheetRowCount;
    }

}
