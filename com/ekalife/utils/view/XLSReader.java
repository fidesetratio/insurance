/**
 * @author  : Ferry Harlim
 * @created : Feb 12, 2007 
 */
package com.ekalife.utils.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class XLSReader extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//response.setHeader("Content-Disposition", "attachment; filename=form.xls");
		
//		OutputStream out = response.getOutputStream();
		
//		InputStream out=(InputStream)model.get("outputFile");
////		logger.info(outputFile.getPath());
////		workBook= getTemplateSource("C:/EkaWeb/Upload_Data_Vendor/10082007/datavendor1", request);
//		workBook=new HSSFWorkbook(out);
//		HSSFSheet sheet = workBook.getSheetAt(0);
//		HSSFCell jobOrd=getCell(sheet,7,1);
//		HSSFCell noSeri=getCell(sheet,7,2);
//		HSSFCell namaPp=getCell(sheet,7,3);
//		HSSFCell namaTt=getCell(sheet,7,4);
//		HSSFCell noPolis=getCell(sheet,7,5);
//		HSSFCell namaProduk=getCell(sheet,7,6);
//		HSSFCell cabang=getCell(sheet,7,7);
//		
//		logger.info(cell0.getStringCellValue());
//		out.close();
		
	}
	
}
