package com.ekalife.utils.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class XLSCreatorSpaj extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HSSFSheet sheet = workBook.createSheet("Rider Link");
		sheet.setDefaultColumnWidth((short)12);
		NumberFormat nf=new DecimalFormat("#,##0.00");
		List lsReport=(List)model.get("lsReport");
		HSSFCell cell0=getCell(sheet,0,0);
		//
		setText(cell0, "NO SPAJ");
		for(int i=0;i<lsReport.size();i++){
			cell0=getCell(sheet, i+1, 0);
			setText(cell0, (String)lsReport.get(i));
		}
	}

}
