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

public class XLSCreatorFrProduksiCair {

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
		firstHeaderCellh0.setCellValue(new HSSFRichTextString("Report Produksi Cair"));
		
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
		HSSFCell firstHeaderCell12 = headerRow3.createCell((short) 12);
		HSSFCell firstHeaderCell13 = headerRow3.createCell((short) 13);
		HSSFCell firstHeaderCell14 = headerRow3.createCell((short) 14);
		HSSFCell firstHeaderCell15 = headerRow3.createCell((short) 15);
		HSSFCell firstHeaderCell16 = headerRow3.createCell((short) 16);
		HSSFCell firstHeaderCell17 = headerRow3.createCell((short) 17);
		HSSFCell firstHeaderCell18 = headerRow3.createCell((short) 18);
		HSSFCell firstHeaderCell19 = headerRow3.createCell((short) 19);
		HSSFCell firstHeaderCell20 = headerRow3.createCell((short) 20);
		HSSFCell firstHeaderCell21 = headerRow3.createCell((short) 21);
		HSSFCell firstHeaderCell22 = headerRow3.createCell((short) 22);
		HSSFCell firstHeaderCell23 = headerRow3.createCell((short) 23);
		HSSFCell firstHeaderCell24 = headerRow3.createCell((short) 24);
		HSSFCell firstHeaderCell25 = headerRow3.createCell((short) 25);
		HSSFCell firstHeaderCell26 = headerRow3.createCell((short) 26);
		HSSFCell firstHeaderCell27 = headerRow3.createCell((short) 27);
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("Jenis"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("Jalur"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("Kat"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("January"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("February"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("March"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("April"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("May"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("June"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("July"));
		firstHeaderCell11.setCellValue(new HSSFRichTextString("August"));
		firstHeaderCell12.setCellValue(new HSSFRichTextString("September"));
		firstHeaderCell13.setCellValue(new HSSFRichTextString("October"));
		firstHeaderCell14.setCellValue(new HSSFRichTextString("November"));
		firstHeaderCell15.setCellValue(new HSSFRichTextString("December"));
		firstHeaderCell16.setCellValue(new HSSFRichTextString("Polis_January"));
		firstHeaderCell17.setCellValue(new HSSFRichTextString("Polis_February"));
		firstHeaderCell18.setCellValue(new HSSFRichTextString("Polis_March"));
		firstHeaderCell19.setCellValue(new HSSFRichTextString("Polis_April"));
		firstHeaderCell20.setCellValue(new HSSFRichTextString("Polis_May"));
		firstHeaderCell21.setCellValue(new HSSFRichTextString("Polis_June"));
		firstHeaderCell22.setCellValue(new HSSFRichTextString("Polis_July"));
		firstHeaderCell23.setCellValue(new HSSFRichTextString("Polis_August"));
		firstHeaderCell24.setCellValue(new HSSFRichTextString("Polis_September"));
		firstHeaderCell25.setCellValue(new HSSFRichTextString("Polis_October"));
		firstHeaderCell26.setCellValue(new HSSFRichTextString("Polis_November"));
		firstHeaderCell27.setCellValue(new HSSFRichTextString("Polis_December"));
		sampleDataSheet.setColumnWidth((short) 0, (short)(1500));
		sampleDataSheet.setColumnWidth((short) 1, (short)(6000));
		sampleDataSheet.setColumnWidth((short) 2, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 3, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 4, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 5, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 6, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 7, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 8, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 9, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 10, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 11, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 12, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 13, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 14, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 15, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 16, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 17, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 18, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 19, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 20, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 21, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 22, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 23, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 24, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 25, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 26, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 27, (short)(8000));
		
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
		firstHeaderCell12.setCellStyle(styleHeader);
		firstHeaderCell13.setCellStyle(styleHeader);
		firstHeaderCell14.setCellStyle(styleHeader);
		firstHeaderCell15.setCellStyle(styleHeader);
		firstHeaderCell16.setCellStyle(styleHeader);
		firstHeaderCell17.setCellStyle(styleHeader);
		firstHeaderCell18.setCellStyle(styleHeader);
		firstHeaderCell19.setCellStyle(styleHeader);
		firstHeaderCell20.setCellStyle(styleHeader);
		firstHeaderCell21.setCellStyle(styleHeader);
		firstHeaderCell22.setCellStyle(styleHeader);
		firstHeaderCell23.setCellStyle(styleHeader);
		firstHeaderCell24.setCellStyle(styleHeader);
		firstHeaderCell25.setCellStyle(styleHeader);
		firstHeaderCell26.setCellStyle(styleHeader);
		firstHeaderCell27.setCellStyle(styleHeader);
		
		// untuk isi row
		int row = 4;
		for(int i=0; i<reportProduksiCair.size(); i++) {
			Map MapreportProduksiCair = (Map) reportProduksiCair.get(i);
			HSSFRow dataRow1 = sampleDataSheet.createRow(row+i);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString((String) MapreportProduksiCair.get("JENIS")));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString((String) MapreportProduksiCair.get("JALUR")));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString((String) MapreportProduksiCair.get("KAT")));
			String test = ((BigDecimal) MapreportProduksiCair.get("JANUARY")).toString();
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("JANUARY") ).toString() ));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("FEBRUARY") ).toString() ));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("MARCH") ).toString() ));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("APRIL") ).toString() ));	
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("MAY") ).toString() ));	
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("JUNE") ).toString() ));	
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("JULY") ).toString() ));	
			dataRow1.createCell((short) 11).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("AUGUST") ).toString() ));	
			dataRow1.createCell((short) 12).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("SEPTEMBER") ).toString() ));	
			dataRow1.createCell((short) 13).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("OCTOBER") ).toString() ));	
			dataRow1.createCell((short) 14).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("NOVEMBER") ).toString() ));	
			dataRow1.createCell((short) 15).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("DECEMBER") ).toString() ));	
			dataRow1.createCell((short) 16).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_JANUARY") ).toString() ));
			dataRow1.createCell((short) 17).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_FEBRUARY") ).toString() ));
			dataRow1.createCell((short) 18).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_MARCH") ).toString() ));
			dataRow1.createCell((short) 19).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_APRIL") ).toString() ));	
			dataRow1.createCell((short) 20).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_MAY") ).toString() ));	
			dataRow1.createCell((short) 21).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_JUNE") ).toString() ));	
			dataRow1.createCell((short) 22).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_JULY") ).toString() ));	
			dataRow1.createCell((short) 23).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_AUGUST") ).toString() ));	
			dataRow1.createCell((short) 24).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_SEPTEMBER") ).toString() ));	
			dataRow1.createCell((short) 25).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_OCTOBER") ).toString() ));	
			dataRow1.createCell((short) 26).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_NOVEMBER") ).toString() ));	
			dataRow1.createCell((short) 27).setCellValue(new HSSFRichTextString( ( (BigDecimal) MapreportProduksiCair.get("POLIS_DECEMBER") ).toString() ));	
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}

}
