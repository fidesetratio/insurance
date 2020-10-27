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

public class XLSCreatorFrPasBp {

	//@Override
	public void buildExcelDocument(String fileName, String dirFileName, List<Pas> reportFrPasBp, Date yesterday) throws Exception {
		
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
		firstHeaderCellh0.setCellValue(new HSSFRichTextString("Follow Up Further Requirement"));
		
		HSSFCell firstHeaderCellh1 = headerRow1.createCell((short) 0);
		firstHeaderCellh1.setCellValue(new HSSFRichTextString("Sampai Dengan "+sdf.format(yesterday)));
		
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
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("Cabang"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama Pemegang Polis"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("No. Registrasi"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("Tgl. Aktivasi (efektif Polis)"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("Status Polis"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("Tgl. Further Requirement"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("Kekurangan Data"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("User"));
		sampleDataSheet.setColumnWidth((short) 0, (short)(1500));
		sampleDataSheet.setColumnWidth((short) 1, (short)(6000));
		sampleDataSheet.setColumnWidth((short) 2, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 3, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 4, (short)(4000));
		sampleDataSheet.setColumnWidth((short) 5, (short)(7000));
		sampleDataSheet.setColumnWidth((short) 6, (short)(5000));
		sampleDataSheet.setColumnWidth((short) 7, (short)(8000));
		sampleDataSheet.setColumnWidth((short) 8, (short)(8000));
		
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
		
		// untuk isi row
		int row = 4;
		for(int i=0; i<reportFrPasBp.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(row+i);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(reportFrPasBp.get(i).getCabang()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(reportFrPasBp.get(i).getMsp_pas_nama_pp()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(reportFrPasBp.get(i).getMsp_fire_id()));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(sdf.format(reportFrPasBp.get(i).getMsp_pas_create_date())));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(reportFrPasBp.get(i).getStatus_polis()));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(sdf2.format(reportFrPasBp.get(i).getMsp_tgl_status())));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(reportFrPasBp.get(i).getMsp_ket_status()));	
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(reportFrPasBp.get(i).getMcl_first()));	
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}

}
