package utility.excel;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utility.Global;
import utility.io.IOUtility;

public class ExcelReader {
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	
	public ExcelReader(String path, int sheetIndex) throws Exception{
		workbook = new XSSFWorkbook(IOUtility.getDis(path));
		sheet = workbook.getSheetAt(sheetIndex);
	}
	
	public ExcelReader(String path) throws Exception{
		this(path, 0);
	}
	
	public XSSFSheet setSheet(int sheetIndex) {
		sheet = workbook.getSheetAt(sheetIndex);
		return sheet;
	}
	
	public String getRawNum(int row, int col) {
		return this.sheet.getRow(row -1).getCell(col-1).getRawValue();
	}
	
	public String get(int row, int col) {
		XSSFCell cell = this.sheet.getRow(row-1).getCell(col-1);
		if(cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf(new BigDecimal(cell.getNumericCellValue()));
		} else {
			return cell.toString();
		}
	}
	
	public void close() throws Exception{
		this.workbook.close();
	}
	
	public static void main(String[] args) throws Exception{
		ExcelReader reader = new ExcelReader(Global.getBaseDatasetPath() + "dfc_score - 副本.xlsx");
		
		for(int i=10; i<69; i++) {
			System.out.print(reader.getRawNum(i, 1) + " ");
			System.out.print(reader.get(i, 2) + " ");
			System.out.print(reader.get(i, 3) + " ");
			System.out.println();
		}
		
		reader.close();
	}
	
}	
