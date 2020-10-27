/**
 * @author  : Ferry Harlim
 * @created : Feb 12, 2007 
 */
package com.ekalife.utils.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ekalife.utils.FormatString;

public class XLSCreator2 extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HSSFSheet sheet = workBook.createSheet("Reas");
		sheet.setDefaultColumnWidth((short)12);
		NumberFormat nf=new DecimalFormat("#,##0.00");
		List lsHasil=(List)model.get("lsHasil");
		HSSFCell cell0=getCell(sheet,0,0);
		HSSFCell cell1=getCell(sheet,0,1);
		HSSFCell cell2=getCell(sheet,0,2);
		HSSFCell cell3=getCell(sheet,0,3);
		
		//
		setText(cell0, "NO.");
		setText(cell1, "NO. SPAJ");
		setText(cell2, "LSCB_ID");
		setText(cell3, "LSCB_PAY_MODE ");
		
		
		for(int i=0;i<lsHasil.size();i++){
			Map map=(HashMap)lsHasil.get(i);
			cell0=getCell(sheet, i+1, 0);
			cell1=getCell(sheet, i+1, 1);
			cell2=getCell(sheet, i+1, 2);
			cell3=getCell(sheet, i+1, 3);
			
			//
			setText(cell0, (i+1)+"");
			setText(cell1, FormatString.nomorSPAJ((String)map.get("REG_SPAJ")));
			setText(cell2, ""+(Integer)map.get("LSCB_ID"));
			setText(cell3, (String)map.get("LSCB_PAY_MODE"));
				
		}
	}
}
