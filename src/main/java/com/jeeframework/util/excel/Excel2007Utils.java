package com.jeeframework.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jeeframework.util.validate.Validate;

public class Excel2007Utils implements ExcelUtils {
	// excel 文档
	private XSSFWorkbook workBook = null;

	// 文档里的第几个页面
	private XSSFSheet hfSheet = null;

	// 列
	private XSSFRow hfRow = null;

	// 第sheetnum个工作表
	private int sheetNum = 0;

	private int rowNum = 0;

	private FileInputStream fis = null;

	private File file = null;

	public Excel2007Utils() {
	}

	public Excel2007Utils(File file) {
		this.file = file;
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
		workBook = new XSSFWorkbook(fis);
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
		XSSFSheet sheet = workBook.getSheetAt(this.sheetNum);
		int rowCount = -1;
		rowCount = sheet.getLastRowNum();
		return rowCount;
	}

	/**
	 * 读取指定sheetNum的sheetName
	 * 
	 * @param sheetNum
	 * @return int
	 */
	public String getSheetName(int sheetNum) {
		XSSFSheet sheet = workBook.getSheetAt(sheetNum);

		return sheet.getSheetName();
	}

	/**
	 * 读取指定sheetNum的rowCount
	 * 
	 * @param sheetNum
	 * @return int
	 */
	public int getRowCount(int sheetNum) {
		XSSFSheet sheet = workBook.getSheetAt(sheetNum);
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

	public XSSFWorkbook getWorkBook() {
		return workBook;
	}

	public void setWorkBook(XSSFWorkbook workBook) {
		this.workBook = workBook;
	}

	private static void insertKeyword() {

		File szfile = new File("D:\\2.xlsx");
		// File szfile = new File("F:\\关键词编码.xls");

		// 将excel文件放入ExcelReader
		Excel2007Utils readExcel = new Excel2007Utils(szfile);
		try {
			// 打开excel
			readExcel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int sheetNum = readExcel.getSheetCount();
		int maxSheetNum = sheetNum - 1;
		// 共6个sheet，都是需要导入的keycode规则
		for (int sheetCount = 0; sheetCount < maxSheetNum; sheetCount++) {

			// 设置读取索引为i的工作表
			readExcel.setSheetNum(sheetCount);
			int count = readExcel.getRowCount();
			int preLevel = 1;
			for (int i = 0; i <= count; i++) {
				String[] currentRows = readExcel.readExcelLine(i);

				if (currentRows == null) {
					continue;
				}
				int level = 1;
				try {
					level = Integer.valueOf(currentRows[0].trim());
				} catch (NumberFormatException ne) {
					level = 1;
				}
				if ((level - preLevel) > 2) {
					System.out.println("sheet:" + sheetCount + ",row:" + i + " level不符合规范");
				}
				// String keywordCode = currentRows[1].trim();
				String keywordName = currentRows[1].trim();
				if (Validate.isEmpty(keywordName)) {
					continue;
				}
				/*
				 * String subjectCode = ""; try { subjectCode =
				 * currentRows[3].trim(); } catch
				 * (ArrayIndexOutOfBoundsException e) { subjectCode = ""; }
				 * String subjectClass = ""; try { subjectClass =
				 * currentRows[4].trim(); } catch
				 * (ArrayIndexOutOfBoundsException e) { subjectClass = ""; }
				 */
				String keywordGroup = "";
				try {
					keywordGroup = currentRows[2].trim();
				} catch (ArrayIndexOutOfBoundsException e) {
					keywordGroup = "";
				}
				String tags = "";
				try {
					tags = currentRows[3].trim();
				} catch (ArrayIndexOutOfBoundsException e) {
					tags = "";
				}

				System.out.println("level " + level + "  keywordName " + keywordName + "  keywordGroup " + keywordGroup + "  tags " + tags);
			}
		}
	}

	public static void writeExcelPOI() {
		try {

			String fileName = "E:\\work\\a.xlsx";
			File xlsx = new File(fileName);
			if (!xlsx.exists()) {
				xlsx.createNewFile();
			}
			XSSFWorkbook xwb = new XSSFWorkbook();

			XSSFSheet xSheet = xwb.createSheet("aaa");
			XSSFRow xRow = xSheet.createRow(0);
			XSSFCell xCell = xRow.createCell(0);

			xCell.setCellValue("asdfasd");

			FileOutputStream out = new FileOutputStream(fileName);
			xwb.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// insertKeyword();

//		writeExcelPOI();
		insertKeyword();
	}
}
