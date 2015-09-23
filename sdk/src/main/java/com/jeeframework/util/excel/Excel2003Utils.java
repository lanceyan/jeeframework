package com.jeeframework.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class Excel2003Utils implements ExcelUtils {
	// excel 文档
	private HSSFWorkbook workBook = null;

	// 文档里的第几个页面
	private HSSFSheet hfSheet = null;

	// 列
	private HSSFRow hfRow = null;

	// 第sheetnum个工作表
	private int sheetNum = 0;

	private int rowNum = 0;

	private FileInputStream fis = null;

	private File file = null;

	public Excel2003Utils() {
	}

	public Excel2003Utils(File file) {
		this.file = file;
	}

	/**
	 * 读取指定sheetNum的sheetName
	 * 
	 * @param sheetNum
	 * @return int
	 */
	public String getSheetName(int sheetNum) {
		HSSFSheet sheet = workBook.getSheetAt(sheetNum);

		return sheet.getSheetName();
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 读取excel文件获得HSSFWorkbook对象
	 */
	public void open() throws IOException {
		fis = new FileInputStream(file);
		workBook = new HSSFWorkbook(new POIFSFileSystem(fis));
		fis.close();
	}

	/**
	 * 返回sheet表数目
	 * 
	 * @return int
	 */
	public int getSheetCount() {
		int sheetCount = -1;
		sheetCount = workBook.getNumberOfSheets();
		return sheetCount;
	}

	/**
	 * sheetNum下的记录行数
	 * 
	 * @return int
	 */
	public int getRowCount() {
		if (workBook == null)
			System.out.println("抱歉，WorkBook为空");
		HSSFSheet sheet = workBook.getSheetAt(this.sheetNum);
		int rowCount = -1;
		rowCount = sheet.getLastRowNum();
		return rowCount;
	}

	/**
	 * 读取指定sheetNum的rowCount
	 * 
	 * @param sheetNum
	 * @return int
	 */
	public int getRowCount(int sheetNum) {
		HSSFSheet sheet = workBook.getSheetAt(sheetNum);
		int rowCount = -1;
		rowCount = sheet.getLastRowNum();
		return rowCount;
	}

	/**
	 * 得到指定行的内容
	 * 
	 * @param lineNum
	 * @return String[]
	 */
	public String[] readExcelLine(int lineNum) {
		return readExcelLine(this.sheetNum, lineNum);
	}

	/**
	 * 指定工作表和行数的内容
	 * 
	 * @param sheetNum
	 * @param lineNum
	 * @return String[]
	 */
	public String[] readExcelLine(int sheetNum, int lineNum) {
		if (sheetNum < 0 || lineNum < 0)
			return null;
		String[] strExcelLine = null;
		try {
			hfSheet = workBook.getSheetAt(sheetNum);
			hfRow = hfSheet.getRow(lineNum);

			if (hfRow == null) {
				return strExcelLine;
			}
			int cellCount = hfRow.getLastCellNum();
			strExcelLine = new String[cellCount + 1];
			for (int i = 0; i <= cellCount; i++) {
				strExcelLine[i] = readStringExcelCell(lineNum, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strExcelLine;
	}

	/**
	 * 读取指定列的内容
	 * 
	 * @param cellNum
	 * @return String
	 */
	public String readStringExcelCell(int cellNum) {
		return readStringExcelCell(this.rowNum, cellNum);
	}

	/**
	 * 指定行和列编号的内容
	 * 
	 * @param rowNum
	 * @param cellNum
	 * @return String
	 */
	public String readStringExcelCell(int rowNum, int cellNum) {
		return readStringExcelCell(this.sheetNum, rowNum, cellNum);
	}

	/**
	 * 指定工作表、行、列下的内容
	 * 
	 * @param sheetNum
	 * @param rowNum
	 * @param cellNum
	 * @return String
	 */
	public String readStringExcelCell(int sheetNum, int rowNum, int cellNum) {
		if (sheetNum < 0 || rowNum < 0)
			return "";
		String strExcelCell = "";
		try {
			hfSheet = workBook.getSheetAt(sheetNum);
			hfRow = hfSheet.getRow(rowNum);

			FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();

			Cell cell = hfRow.getCell(cellNum);

			if (cell != null) { // add this
				// condition
				// judge
				CellValue cellValue = evaluator.evaluate(cell);

				if (cellValue == null) {
					strExcelCell = "";
					return strExcelCell;
				}
				switch (cellValue.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					strExcelCell = cellValue.getBooleanValue() + "";
					break;
				case Cell.CELL_TYPE_ERROR:
					strExcelCell = "";
					break;
				case Cell.CELL_TYPE_NUMERIC: {
					java.text.DecimalFormat formatter = new java.text.DecimalFormat("#############.###");
					strExcelCell = formatter.format(cellValue.getNumberValue());
					// strExcelCell = String.valueOf(hfRow.getCell((short)
					// cellNum).getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(cell)) { // 读取日期
						double d = cell.getNumericCellValue();
						Date date = HSSFDateUtil.getJavaDate(d);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						strExcelCell = sdf.format(date);
					}
				}
					break;
				case Cell.CELL_TYPE_STRING:
					strExcelCell = cellValue.getStringValue();
					break;
				case Cell.CELL_TYPE_BLANK:
					strExcelCell = "";
					break;
				case Cell.CELL_TYPE_FORMULA:
					strExcelCell = "FORMULA ";
					break;
				default:
					strExcelCell = "";
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strExcelCell;
	}

	public HSSFWorkbook getWorkBook() {
		return workBook;
	}

	public void setWorkBook(HSSFWorkbook workBook) {
		this.workBook = workBook;
	}
}
