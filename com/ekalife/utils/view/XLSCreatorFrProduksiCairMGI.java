package com.ekalife.utils.view;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ekalife.elions.model.NasabahSinarmasSekuriti;
import com.ekalife.elions.model.Pas;

public class XLSCreatorFrProduksiCairMGI {

	//@Override
	public void buildExcelDocument(String fileName, String dirFileName, List reportProduksiCair, Date bdate, Date edate) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		HSSFRow headerRow0 = sampleDataSheet.createRow(0);
		HSSFRow headerRow1 = sampleDataSheet.createRow(1);
		HSSFRow headerRow2 = sampleDataSheet.createRow(2);
		HSSFRow headerRow3 = sampleDataSheet.createRow(3);
		
		HSSFCell firstHeaderCellh0 = headerRow0.createCell((short) 0);
		firstHeaderCellh0.setCellValue(new HSSFRichTextString("Report Produksi Cair By MGI"));
		
		HSSFCell firstHeaderCellh1 = headerRow1.createCell((short) 0);
		firstHeaderCellh1.setCellValue(new HSSFRichTextString("Dari Tanggal "+sdf.format(bdate)+" Sampai Dengan "+sdf.format(edate)));
		
		HSSFCell firstHeaderCellh2 = headerRow2.createCell((short) 0);
		firstHeaderCellh2.setCellValue(new HSSFRichTextString(""));
		
		HSSFCell firstHeaderCell0 = headerRow3.createCell((short) 0);
		HSSFCell firstHeaderCell1 = headerRow3.createCell((short) 1);
		HSSFCell firstHeaderCell2 = headerRow3.createCell((short) 2);
		HSSFCell firstHeaderCell3 = headerRow3.createCell((short) 3);
		HSSFCell firstHeaderCell4 = headerRow3.createCell((short) 4);
		HSSFCell firstHeaderCell5 = headerRow3.createCell((short) 5);
		HSSFCell firstHeaderCell6 = headerRow3.createCell((short) 6);
		HSSFCell firstHeaderCell7 = headerRow3.createCell((short) 7);
		HSSFCell firstHeaderCell8 = headerRow3.createCell((short) 8);
		HSSFCell firstHeaderCell9 = headerRow3.createCell((short) 9);
		HSSFCell firstHeaderCell10 = headerRow3.createCell((short) 10);
		HSSFCell firstHeaderCell11 = headerRow3.createCell((short) 11);
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("JENIS"));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("KAT"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("MGI_1"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("MGI_3"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("MGI_6"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("MGI_12"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("MGI_36"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("POLIS_MGI_1"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("POLIS_MGI_3"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("POLIS_MGI_6"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("POLIS_MGI_12"));
		firstHeaderCell11.setCellValue(new HSSFRichTextString("POLIS_MGI_36"));
		sampleDataSheet.setColumnWidth((short) 0, (short)(3000));
		sampleDataSheet.setColumnWidth((short) 1, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 2, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 3, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 4, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 5, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 6, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 7, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 8, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 9, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 10, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 11, (short)(5000));
		
		HSSFCellStyle styleHeader, styleHeader2;
		HSSFFont titleFont = workBook.createFont();
		titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFFont titleFont2 = workBook.createFont();
		titleFont2.setColor(HSSFColor.BLACK.index);
        titleFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont2.setFontHeight((short) 250);
        styleHeader2 = workBook.createCellStyle();
		styleHeader2.setFont(titleFont2);
		styleHeader = workBook.createCellStyle();
		styleHeader.setBorderBottom((short) 1);
		styleHeader.setBorderLeft((short) 1);
		styleHeader.setBorderRight((short) 1);
		styleHeader.setBorderTop((short) 1);
		styleHeader.setFont(titleFont);
		styleHeader.setWrapText(true);
		firstHeaderCellh0.setCellStyle(styleHeader2);
		firstHeaderCellh1.setCellStyle(styleHeader2);
		firstHeaderCell0.setCellStyle(styleHeader);
		firstHeaderCell1.setCellStyle(styleHeader);
		firstHeaderCell2.setCellStyle(styleHeader);
		firstHeaderCell3.setCellStyle(styleHeader);
		firstHeaderCell4.setCellStyle(styleHeader);
		firstHeaderCell5.setCellStyle(styleHeader);
		firstHeaderCell6.setCellStyle(styleHeader);
		firstHeaderCell7.setCellStyle(styleHeader);
		firstHeaderCell8.setCellStyle(styleHeader);
		firstHeaderCell9.setCellStyle(styleHeader);
		firstHeaderCell10.setCellStyle(styleHeader);
		firstHeaderCell11.setCellStyle(styleHeader);
		
		// untuk isi row
		int row = 4;
		for(int i=0; i<reportProduksiCair.size(); i++) {
			Map MapreportProduksiCair = (Map) reportProduksiCair.get(i);
			HSSFRow dataRow1 = sampleDataSheet.createRow(row+i);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((String) MapreportProduksiCair.get("JENIS")));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString((String) MapreportProduksiCair.get("KAT")));
//			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("MGI_1") ).toString() ));
//			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("MGI_3") ).toString() ));
//			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("MGI_6") ).toString() ));
//			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("MGI_12") ).toString() ));
//			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("MGI_36") ).toString() ));
//			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_MGI_1") ).toString() ));	
//			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_MGI_3") ).toString() ));	
//			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_MGI_6") ).toString() ));	
//			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_MGI_12") ).toString() ));	
//			dataRow1.createCell((short) 11).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_MGI_36") ).toString() ));	
			dataRow1.createCell((short) 2).setCellValue(((BigDecimal) MapreportProduksiCair.get("MGI_1") ).doubleValue()  );
			dataRow1.createCell((short) 3).setCellValue(((BigDecimal) MapreportProduksiCair.get("MGI_3") ).doubleValue()  );
			dataRow1.createCell((short) 4).setCellValue(((BigDecimal) MapreportProduksiCair.get("MGI_6") ).doubleValue()  );
			dataRow1.createCell((short) 5).setCellValue(((BigDecimal) MapreportProduksiCair.get("MGI_12") ).doubleValue()  );
			dataRow1.createCell((short) 6).setCellValue(((BigDecimal) MapreportProduksiCair.get("MGI_36") ).doubleValue()  );
			dataRow1.createCell((short) 7).setCellValue(((BigDecimal) MapreportProduksiCair.get("POLIS_MGI_1") ).doubleValue()  );	
			dataRow1.createCell((short) 8).setCellValue(((BigDecimal) MapreportProduksiCair.get("POLIS_MGI_3") ).doubleValue()  );	
			dataRow1.createCell((short) 9).setCellValue(((BigDecimal) MapreportProduksiCair.get("POLIS_MGI_6") ).doubleValue()  );	
			dataRow1.createCell((short) 10).setCellValue(((BigDecimal) MapreportProduksiCair.get("POLIS_MGI_12") ).doubleValue()  );	
			dataRow1.createCell((short) 11).setCellValue(((BigDecimal) MapreportProduksiCair.get("POLIS_MGI_36") ).doubleValue()  );	
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}

}
