package com.jeeframework.util.excel;

import java.io.File;
import java.io.IOException;

public interface ExcelUtils {

	/**
	 * 读取指定sheetNum的sheetName
	 * 
	 * @param sheetNum
	 * @return int
	 */
	public String getSheetName(int sheetNum);

	public void setRowNum(int rowNum);

	public void setSheetNum(int sheetNum);

	public void setFile(File file);

	/**
	 * 读取excel文件获得HSSFWorkbook对象
	 */
	public void open() throws IOException;

	/**
	 * 返回sheet表数目
	 * 
	 * @return int
	 */
	public int getSheetCount();

	/**
	 * sheetNum下的记录行数
	 * 
	 * @return int
	 */
	public int getRowCount();

	/**
	 * 读取指定sheetNum的rowCount
	 * 
	 * @param sheetNum
	 * @return int
	 */
	public int getRowCount(int sheetNum);

	/**
	 * 得到指定行的内容
	 * 
	 * @param lineNum
	 * @return String[]
	 */
	public String[] readExcelLine(int lineNum);

	/**
	 * 指定工作表和行数的内容
	 * 
	 * @param sheetNum
	 * @param lineNum
	 * @return String[]
	 */
	public String[] readExcelLine(int sheetNum, int lineNum);

	/**
	 * 读取指定列的内容
	 * 
	 * @param cellNum
	 * @return String
	 */
	public String readStringExcelCell(int cellNum);

	/**
	 * 指定行和列编号的内容
	 * 
	 * @param rowNum
	 * @param cellNum
	 * @return String
	 */
	public String readStringExcelCell(int rowNum, int cellNum);

	/**
	 * 指定工作表、行、列下的内容
	 * 
	 * @param sheetNum
	 * @param rowNum
	 * @param cellNum
	 * @return String
	 */
	public String readStringExcelCell(int sheetNum, int rowNum, int cellNum);

}
