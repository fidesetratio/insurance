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

import com.ekalife.elions.model.DanamasPrima;
import com.ekalife.elions.model.NasabahSinarmasSekuriti;
import com.ekalife.elions.model.Pas;
import com.ekalife.utils.FormatString;

public class XLSCreatorReportDanamasPrima {

	//@Override
	public void buildExcelPenerimaanDanamasPrimaDocument(String fileName, String dirFileName, List<DanamasPrima> danamasPrimaList, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		
//		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
//		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
//		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("List data PAS yang sudah diaktivasi klien, tapi belum diinput di Cabang " +pasList.get(0).getCabang()));
//		
//		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
//		
//		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
//		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
//		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
//		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
//		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
//		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
//		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
//		
//		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		
		HSSFCellStyle styleFont;
		styleFont = workBook.createCellStyle();
		styleFont.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		styleFont.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
		styleFont.setFillPattern(HSSFColor.GREY_40_PERCENT.index);
		
		HSSFRow headerRow = sampleDataSheet.createRow(0);
		HSSFCell firstHeaderCell0 = headerRow.createCell((short) 0);
		HSSFCell firstHeaderCell1 = headerRow.createCell((short) 1);
		HSSFCell firstHeaderCell2 = headerRow.createCell((short) 2);
		HSSFCell firstHeaderCell3 = headerRow.createCell((short) 3);
		HSSFCell firstHeaderCell4 = headerRow.createCell((short) 4);
		HSSFCell firstHeaderCell5 = headerRow.createCell((short) 5);
		HSSFCell firstHeaderCell6 = headerRow.createCell((short) 6);
		HSSFCell firstHeaderCell7 = headerRow.createCell((short) 7);
		HSSFCell firstHeaderCell8 = headerRow.createCell((short) 8);
		HSSFCell firstHeaderCell9 = headerRow.createCell((short) 9);
		HSSFCell firstHeaderCell10 = headerRow.createCell((short) 10);
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("REG_SPAJ"));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("POLIS"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("PEMEGANG"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("KURS"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("UANG_MASUK"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("TGL_RK"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("NO_VOUCHER"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("NO_PRE"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("TGL_AKSEP"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("PREMI"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("NO_JM"));
		
		firstHeaderCell0.setCellStyle(styleFont);
		firstHeaderCell1.setCellStyle(styleFont);
		firstHeaderCell2.setCellStyle(styleFont);
		firstHeaderCell3.setCellStyle(styleFont);
		firstHeaderCell4.setCellStyle(styleFont);
		firstHeaderCell5.setCellStyle(styleFont);
		firstHeaderCell6.setCellStyle(styleFont);
		firstHeaderCell7.setCellStyle(styleFont);
		firstHeaderCell8.setCellStyle(styleFont);
		firstHeaderCell9.setCellStyle(styleFont);
		firstHeaderCell10.setCellStyle(styleFont);
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<danamasPrimaList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getReg_spaj()));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getMspo_policy_no()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getPemegang()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getKurs()));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(danamasPrimaList.get(i).getUang_masuk())))));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(sdf2.format(danamasPrimaList.get(i).getTgl_rk())));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getNo_voucher()));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getNo_pre()));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(sdf2.format(danamasPrimaList.get(i).getTgl_aksep())));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(danamasPrimaList.get(i).getPremi())))));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getNo_jm()));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
	public void buildExcelKlaimDanamasPrimaDocument(String fileName, String dirFileName, List<DanamasPrima> danamasPrimaList, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		
//		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
//		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
//		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("List data PAS yang sudah diaktivasi klien, tapi belum diinput di Cabang " +pasList.get(0).getCabang()));
//		
//		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
//		
//		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
//		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
//		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
//		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
//		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
//		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
//		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
//		
//		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		
		HSSFCellStyle styleFont;
		styleFont = workBook.createCellStyle();
		styleFont.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		styleFont.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
		styleFont.setFillPattern(HSSFColor.GREY_40_PERCENT.index);
		
		HSSFRow headerRow = sampleDataSheet.createRow(0);
		HSSFCell firstHeaderCell0 = headerRow.createCell((short) 0);
		HSSFCell firstHeaderCell1 = headerRow.createCell((short) 1);
		HSSFCell firstHeaderCell2 = headerRow.createCell((short) 2);
		HSSFCell firstHeaderCell3 = headerRow.createCell((short) 3);
		HSSFCell firstHeaderCell4 = headerRow.createCell((short) 4);
		HSSFCell firstHeaderCell5 = headerRow.createCell((short) 5);
		HSSFCell firstHeaderCell6 = headerRow.createCell((short) 6);
		HSSFCell firstHeaderCell7 = headerRow.createCell((short) 7);
		HSSFCell firstHeaderCell8 = headerRow.createCell((short) 8);
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("REG_SPAJ"));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("POLIS"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("PEMEGANG"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("KURS"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("POKOK"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("BUNGA"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("TOTAL"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("TGL_BAYAR"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("NO_REG"));
		
		firstHeaderCell0.setCellStyle(styleFont);
		firstHeaderCell1.setCellStyle(styleFont);
		firstHeaderCell2.setCellStyle(styleFont);
		firstHeaderCell3.setCellStyle(styleFont);
		firstHeaderCell4.setCellStyle(styleFont);
		firstHeaderCell5.setCellStyle(styleFont);
		firstHeaderCell6.setCellStyle(styleFont);
		firstHeaderCell7.setCellStyle(styleFont);
		firstHeaderCell8.setCellStyle(styleFont);
		
		// untuk isi row
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<danamasPrimaList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getReg_spaj()));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getMspo_policy_no()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getPemegang()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getKurs()));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(danamasPrimaList.get(i).getPokok())))));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(danamasPrimaList.get(i).getBunga())))));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(danamasPrimaList.get(i).getTotal())))));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(sdf2.format(danamasPrimaList.get(i).getTgl_bayar())));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getNo_reg()));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}

	public void buildExcelManfDanamasPrimaDocument(String fileName, String dirFileName, List<DanamasPrima> danamasPrimaList, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		
		HSSFCellStyle styleFont;
		styleFont = workBook.createCellStyle();
		styleFont.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		styleFont.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
		styleFont.setFillPattern(HSSFColor.GREY_40_PERCENT.index);
		
		HSSFRow headerRow = sampleDataSheet.createRow(0);
		HSSFCell firstHeaderCell0 = headerRow.createCell((short) 0);
		HSSFCell firstHeaderCell1 = headerRow.createCell((short) 1);
		HSSFCell firstHeaderCell2 = headerRow.createCell((short) 2);
		HSSFCell firstHeaderCell3 = headerRow.createCell((short) 3);
		HSSFCell firstHeaderCell4 = headerRow.createCell((short) 4);
		HSSFCell firstHeaderCell5 = headerRow.createCell((short) 5);
		HSSFCell firstHeaderCell6 = headerRow.createCell((short) 6);
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("REG_SPAJ"));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("POLIS"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("PEMEGANG"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("KURS"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("BUNGA"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("TGL_BAYAR"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("NO_REG"));
		
		firstHeaderCell0.setCellStyle(styleFont);
		firstHeaderCell1.setCellStyle(styleFont);
		firstHeaderCell2.setCellStyle(styleFont);
		firstHeaderCell3.setCellStyle(styleFont);
		firstHeaderCell4.setCellStyle(styleFont);
		firstHeaderCell5.setCellStyle(styleFont);
		firstHeaderCell6.setCellStyle(styleFont);
		
		// untuk isi row
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<danamasPrimaList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getReg_spaj()));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getMspo_policy_no()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getPemegang()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getKurs()));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(danamasPrimaList.get(i).getBunga())))));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(sdf2.format(danamasPrimaList.get(i).getTgl_bayar())));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(danamasPrimaList.get(i).getNo_reg()));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
		
}
