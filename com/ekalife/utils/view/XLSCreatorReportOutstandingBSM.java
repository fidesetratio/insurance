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
import com.ekalife.elions.model.OutstandingBSM;
import com.ekalife.utils.FormatString;

public class XLSCreatorReportOutstandingBSM {

	//@Override
	public void buildExcelOutstandingBsmSP(String fileName, String dirFileName, List<OutstandingBSM> listOustandingSP, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)18);
		
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
		HSSFCell firstHeaderCell11 = headerRow.createCell((short) 11);
		HSSFCell firstHeaderCell12 = headerRow.createCell((short) 12);
		HSSFCell firstHeaderCell13 = headerRow.createCell((short) 13);
		HSSFCell firstHeaderCell14 = headerRow.createCell((short) 14);
		HSSFCell firstHeaderCell15 = headerRow.createCell((short) 15);
		HSSFCell firstHeaderCell16 = headerRow.createCell((short) 16);
		
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("REG_SPAJ"));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("POLIS"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("PEMEGANG"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("KURS"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("PREMI"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("UP"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("ROLLOVER"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("MGI"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("BUNGA"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("PERSEN"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("TGL_MULAI"));
		firstHeaderCell11.setCellValue(new HSSFRichTextString("TGL_AKSEP"));
		firstHeaderCell12.setCellValue(new HSSFRichTextString("AGEN_PENUTUP"));
		firstHeaderCell13.setCellValue(new HSSFRichTextString("AGEN_REFERRAL"));
		firstHeaderCell14.setCellValue(new HSSFRichTextString("REK_CABANG"));
		firstHeaderCell15.setCellValue(new HSSFRichTextString("REK_NAMA"));
		firstHeaderCell16.setCellValue(new HSSFRichTextString("REK_NO"));
		
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
		firstHeaderCell11.setCellStyle(styleFont);
		firstHeaderCell12.setCellStyle(styleFont);
		firstHeaderCell13.setCellStyle(styleFont);
		firstHeaderCell14.setCellStyle(styleFont);
		firstHeaderCell15.setCellStyle(styleFont);
		firstHeaderCell16.setCellStyle(styleFont);
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<listOustandingSP.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getReg_spaj()));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getPolis()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getPemegang()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getKurs()));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSP.get(i).getPremi())))));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSP.get(i).getUp())))));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getRollover()));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getMgi().toString()));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSP.get(i).getBunga())))));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getPersen().toString()));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(sdf2.format(listOustandingSP.get(i).getTgl_mulai())));
			dataRow1.createCell((short) 11).setCellValue(new HSSFRichTextString(sdf2.format(listOustandingSP.get(i).getTgl_aksep())));
			dataRow1.createCell((short) 12).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getAgent_penutup()));
			dataRow1.createCell((short) 13).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getAgent_referral()));
			dataRow1.createCell((short) 14).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getRek_cabang()));
			dataRow1.createCell((short) 15).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getRek_nama()));
			dataRow1.createCell((short) 16).setCellValue(new HSSFRichTextString(listOustandingSP.get(i).getRek_no()));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
	public void buildExcelOutstandingBsmSSL(String fileName, String dirFileName, List<OutstandingBSM> listOustandingSSL, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)18);
		
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
		HSSFCell firstHeaderCell11 = headerRow.createCell((short) 11);
		HSSFCell firstHeaderCell12 = headerRow.createCell((short) 12);
		HSSFCell firstHeaderCell13 = headerRow.createCell((short) 13);
		HSSFCell firstHeaderCell14 = headerRow.createCell((short) 14);
		HSSFCell firstHeaderCell15 = headerRow.createCell((short) 15);
		HSSFCell firstHeaderCell16 = headerRow.createCell((short) 16);
		HSSFCell firstHeaderCell17 = headerRow.createCell((short) 17);
		
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("REG_SPAJ"));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("POLIS"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("PEMEGANG"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("KURS"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("TRANSAKSI"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("PREMI"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("UP"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("ROLLOVER"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("MGI"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("BUNGA"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("PERSEN"));
		firstHeaderCell11.setCellValue(new HSSFRichTextString("TGL_MULAI"));
		firstHeaderCell12.setCellValue(new HSSFRichTextString("TGL_AKSEP"));
		firstHeaderCell13.setCellValue(new HSSFRichTextString("AGEN_PENUTUP"));
		firstHeaderCell14.setCellValue(new HSSFRichTextString("AGEN_REFERRAL"));
		firstHeaderCell15.setCellValue(new HSSFRichTextString("REK_CABANG"));
		firstHeaderCell16.setCellValue(new HSSFRichTextString("REK_NAMA"));
		firstHeaderCell17.setCellValue(new HSSFRichTextString("REK_NO"));
		
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
		firstHeaderCell11.setCellStyle(styleFont);
		firstHeaderCell12.setCellStyle(styleFont);
		firstHeaderCell13.setCellStyle(styleFont);
		firstHeaderCell14.setCellStyle(styleFont);
		firstHeaderCell15.setCellStyle(styleFont);
		firstHeaderCell16.setCellStyle(styleFont);
		firstHeaderCell17.setCellStyle(styleFont);
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<listOustandingSSL.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getReg_spaj()));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getPolis()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getPemegang()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getKurs()));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getTransaksi()));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSSL.get(i).getPremi())))));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSSL.get(i).getUp())))));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getRollover()));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getMgi().toString()));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSSL.get(i).getBunga())))));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getPersen().toString()));
			dataRow1.createCell((short) 11).setCellValue(new HSSFRichTextString(sdf2.format(listOustandingSSL.get(i).getTgl_mulai())));
			dataRow1.createCell((short) 12).setCellValue(new HSSFRichTextString(sdf2.format(listOustandingSSL.get(i).getTgl_aksep())));
			dataRow1.createCell((short) 13).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getAgent_penutup()));
			dataRow1.createCell((short) 14).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getAgent_referral()));
			dataRow1.createCell((short) 15).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getRek_cabang()));
			dataRow1.createCell((short) 16).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getRek_nama()));
			dataRow1.createCell((short) 17).setCellValue(new HSSFRichTextString(listOustandingSSL.get(i).getRek_no()));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}

	public void buildExcelOutstandingBsmSPL(String fileName, String dirFileName, List<OutstandingBSM> listOustandingSPL, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)18);
		
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
		HSSFCell firstHeaderCell11 = headerRow.createCell((short) 11);
		HSSFCell firstHeaderCell12 = headerRow.createCell((short) 12);
		HSSFCell firstHeaderCell13 = headerRow.createCell((short) 13);
		HSSFCell firstHeaderCell14 = headerRow.createCell((short) 14);
		HSSFCell firstHeaderCell15 = headerRow.createCell((short) 15);
		HSSFCell firstHeaderCell16 = headerRow.createCell((short) 16);
		HSSFCell firstHeaderCell17 = headerRow.createCell((short) 17);
		
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("REG_SPAJ"));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("POLIS"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("PEMEGANG"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("KURS"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("TGL"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("LJI_INVEST"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("PREMI"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("UP"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("TOTAL_UNIT"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("HARGA_UNIT"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("NILAI_POLIS"));
		firstHeaderCell11.setCellValue(new HSSFRichTextString("TGL_MULAI"));
		firstHeaderCell12.setCellValue(new HSSFRichTextString("TGL_AKSEP"));
		firstHeaderCell13.setCellValue(new HSSFRichTextString("AGEN_PENUTUP"));
		firstHeaderCell14.setCellValue(new HSSFRichTextString("AGEN_REFERRAL"));
		firstHeaderCell15.setCellValue(new HSSFRichTextString("REK_CABANG"));
		firstHeaderCell16.setCellValue(new HSSFRichTextString("REK_NAMA"));
		firstHeaderCell17.setCellValue(new HSSFRichTextString("REK_NO"));
		
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
		firstHeaderCell11.setCellStyle(styleFont);
		firstHeaderCell12.setCellStyle(styleFont);
		firstHeaderCell13.setCellStyle(styleFont);
		firstHeaderCell14.setCellStyle(styleFont);
		firstHeaderCell15.setCellStyle(styleFont);
		firstHeaderCell16.setCellStyle(styleFont);
		firstHeaderCell17.setCellStyle(styleFont);
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<listOustandingSPL.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getReg_spaj()));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getPolis()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getPemegang()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getKurs()));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(sdf2.format(listOustandingSPL.get(i).getTgl())));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getLji_invest()));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSPL.get(i).getPremi())))));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSPL.get(i).getUp())))));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSPL.get(i).getTotal_unit())))));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSPL.get(i).getHarga_unit())))));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(FormatString.formatCurrency(null, new BigDecimal(Math.round(listOustandingSPL.get(i).getNilai_polis())))));
			dataRow1.createCell((short) 11).setCellValue(new HSSFRichTextString(sdf2.format(listOustandingSPL.get(i).getTgl_mulai())));
			dataRow1.createCell((short) 12).setCellValue(new HSSFRichTextString(sdf2.format(listOustandingSPL.get(i).getTgl_aksep())));
			dataRow1.createCell((short) 13).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getAgent_penutup()));
			dataRow1.createCell((short) 14).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getAgent_referral()));
			dataRow1.createCell((short) 15).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getRek_cabang()));
			dataRow1.createCell((short) 16).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getRek_nama()));
			dataRow1.createCell((short) 17).setCellValue(new HSSFRichTextString(listOustandingSPL.get(i).getRek_no()));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
		
}
