package secretar.scripts;

import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ScriptUtils {
	
	public static ArrayList<ArrayList<String>> convertSheetDataToStringTable(HSSFSheet sheet){
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>(numberOfRows);

                for (int j = 0; j < numberOfRows; j++ ) {
                    
                    HSSFRow row = sheet.getRow(j);
                    int  numberOfColumns = row.getLastCellNum();
                    ArrayList<String> rowData = new ArrayList<String>(numberOfColumns);
                    for (int i = 0 ; i < numberOfColumns; i++) {
                        rowData.add(getStringValue(row.getCell(i)));
                    }
                    data.add(rowData);
                }
                return data;
	}

	public static void printTableToSheet(HSSFSheet sheet, ArrayList<ArrayList<String>> data){
            for (int i = 0; i < data.size(); i++) {
               ArrayList<String> rowData = data.get(i);
               Row row = sheet.createRow(i);
               for (int j = 0; j < rowData.size(); j++) {
               Cell cell = row.createCell(j) ;
               cell.setCellValue(rowData.get(j));
               }
            }
		
	}

    private static String getStringValue(HSSFCell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
            return cell.getStringCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
            return Integer.toString((int)cell.getNumericCellValue());
        }
        return "";
    }
}
