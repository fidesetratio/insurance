package com.ekalife.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andy
 * @since 10/03/2011
 * Fungsi2 read berhubungan dengan file excel
 */
public class ExcelRead {
	protected final Log logger = LogFactory.getLog( getClass() );

	public List<List> readExcelFile(String dir, String fileName) throws Exception {
        //
        // An excel file name. You can create a file name with a full 
        // path information.
        //
        //String filename = "..\\data.xls";

        //
        // Create an ArrayList to store the data read from excel sheet.
        //
        List sheetData = new ArrayList();

        FileInputStream fis = null;
        try {
            //
            // Create a FileInputStream that will be use to read the 
            // excel file.
            //
            //fis = new FileInputStream(filename);
        	fis = new FileInputStream(dir + fileName);

            //
            // Create an excel workbook from the file system.
            //
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            //
            // Get the first sheet on the workbook.
            //
            HSSFSheet sheet = workbook.getSheetAt(0);

            //
            // When we have a sheet object in hand we can iterator on 
            // each sheet's rows and on each row's cells. We store the 
            // data read on an ArrayList so that we can printed the 
            // content of the excel to the console.
            //
            Iterator rows = sheet.rowIterator();
            
            int rowStart = sheet.getFirstRowNum();
            int rowEnd = sheet.getLastRowNum();
            
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
            
                Iterator cells = row.cellIterator();
                
                List data = new ArrayList();
                while (cells.hasNext()) {
//                	if(cells==null)cells.next().toString();
                    HSSFCell cell = (HSSFCell) cells.next();
                    data.add(cell);
                }

                sheetData.add(data);
            }
        } catch (IOException e) {
            logger.error("ERROR :", e);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return sheetData;
        //showExelData(sheetData);
    }

//    private static void showExelData(List sheetData) {
//        //
//        // Iterates the data and print it out to the console.
//        //
//        for (int i = 0; i < sheetData.size(); i++) {
//            List list = (List) sheetData.get(i);
//            for (int j = 0; j < list.size(); j++) {
//                HSSFCell cell = (HSSFCell) list.get(j);
//                logger.info(
//                        cell.getRichStringCellValue().getString());
//                if (j < list.size() - 1) {
//                    logger.info(", ");
//                }
//            }
//            logger.info("");
//        }
//    }

	public List<List> readExcelFileNew(String dir, String fileName) throws Exception {
		
        List sheetData = new ArrayList();

        FileInputStream fis = null;
        try {
          
        	fis = new FileInputStream(dir + fileName);

            HSSFWorkbook workbook = new HSSFWorkbook(fis);
           
            HSSFSheet sheet = workbook.getSheetAt(0);

            Iterator rows = sheet.rowIterator();
            
            int rowStart = sheet.getFirstRowNum();
            int rowEnd = sheet.getLastRowNum();
            
            for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
                HSSFRow r = sheet.getRow(rowNum);
                if (r == null) {               
                   continue;
                }else{
	                short lastColumn = r.getLastCellNum();
	                List data = new ArrayList();	
	                for (short cn = 0; cn < lastColumn; cn++) {
	                	String cell = "";
	                	HSSFCell c =r.getCell(cn);	                
	                	if(c!=null){	                	
		                	if(c.getCellType()==0)cell = Double.toString(c.getNumericCellValue());
		                	else cell = c.getStringCellValue();
	                	}
	                	data.add(cell);
	                }
	                sheetData.add(data);
                }
            
            }
        } catch (IOException e) {
            logger.error("ERROR :", e);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return sheetData;
        //showExelData(sheetData);
	}
	
	//helpdesk [149354] Project SMS Polis Retur
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<List> readAllExcelFile(File local_file) throws IOException, InvalidFormatException {
		List excel_data = new ArrayList();

		Workbook workbook = WorkbookFactory.create(local_file);
		Sheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> IRow = sheet.iterator();			
		while(IRow.hasNext()){
			Row row = IRow.next();
			
			if(row == null){
				continue;
			}

			List data = new ArrayList();
			Iterator<Cell> ICell = row.cellIterator();
			while(ICell.hasNext()){
				Cell cell = ICell.next();
				
				if(!cell.getStringCellValue().equalsIgnoreCase("") && cell.getStringCellValue() != null)
					data.add(cell);
			}
			
			excel_data.add(data);
		}
		
		if(workbook != null) workbook.close();
		
		return excel_data;
	}
}
