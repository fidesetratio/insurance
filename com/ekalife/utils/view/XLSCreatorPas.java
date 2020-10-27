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

public class XLSCreatorPas {

	//@Override
	public void buildExcelFollowUpDocument(String fileName, String dirFileName, List<Pas> pasList, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		
		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("List data PAS yang sudah diaktivasi klien, tapi belum diinput di Cabang " +pasList.get(0).getCabang()));
		
		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
		
		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
		
		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		
		HSSFRow headerRow = sampleDataSheet.createRow(4);
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
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("No. HP"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama Tertanggung"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("Tanggal Lahir"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("No. KTP TT"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("Nama Paket"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("Premi"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("No. Kartu"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("Nomor PIN"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("Nama Agen"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("Tanggal Aktivasi"));
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<pasList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1+4);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_mobile()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_full_name()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_date_of_birth())));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_identity_no_tt()));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(pasList.get(i).getNama_produk().replace("PAS ", "")));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(pasList.get(i).getPremi()));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(pasList.get(i).getNo_kartu()));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(pasList.get(i).getPin()));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(pasList.get(i).getNama_agent()));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(sdf2.format(pasList.get(i).getMsp_message_date())));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
	public void buildExcelBatalDocument(String fileName, String dirFileName, List<Pas> pasList, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		
		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("List data PAS yang dibatalkan karena sudah lebih dari 3 bulan sejak diaktivasi klien, tapi belum diinput di Cabang " +pasList.get(0).getCabang()));
		
		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
		
		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
		
		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		
		HSSFRow headerRow = sampleDataSheet.createRow(4);
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
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("No. HP"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama Tertanggung"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("Tanggal Lahir"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("No. KTP TT"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("Nama Paket"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("Premi"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("No. Kartu"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("Nomor PIN"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("Nama Agen"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("Tanggal Aktivasi"));
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<pasList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1+4);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_mobile()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_full_name()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_date_of_birth())));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_identity_no_tt()));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(pasList.get(i).getNama_produk().replace("PAS ", "")));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(pasList.get(i).getPremi()));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(pasList.get(i).getNo_kartu()));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(pasList.get(i).getPin()));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(pasList.get(i).getNama_agent()));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(sdf2.format(pasList.get(i).getMsp_message_date())));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
	public void buildExcelEmailStatusPaBsm(String fileName, String dirFileName, List<Map> pasList, String targetDate) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		
		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("List data Email Status PA BSM"));
		
		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
		
		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Tanggal"));
		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(": "+targetDate));
		
		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		
		HSSFRow headerRow = sampleDataSheet.createRow(4);
		HSSFCell firstHeaderCell0 = headerRow.createCell((short) 0);
		HSSFCell firstHeaderCell1 = headerRow.createCell((short) 1);
		HSSFCell firstHeaderCell2 = headerRow.createCell((short) 2);
		HSSFCell firstHeaderCell3 = headerRow.createCell((short) 3);
		HSSFCell firstHeaderCell4 = headerRow.createCell((short) 4);
		HSSFCell firstHeaderCell5 = headerRow.createCell((short) 5);
		HSSFCell firstHeaderCell6 = headerRow.createCell((short) 6);
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("No. Sertifikat"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("Email"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("Cabang"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("Status"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("Tgl. Kirim"));
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for(int i=0; i<pasList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1+4);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(pasList.get(i).get("NO_SERTIFIKAT").toString()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(pasList.get(i).get("MSP_FULL_NAME").toString()));
			int status = 0;
			if(pasList.get(i).get("ME_STATUS") != null){
				status = Integer.parseInt(pasList.get(i).get("ME_STATUS").toString());
			}
			
			if(pasList.get(i).get("ME_STATUS") == null && pasList.get(i).get("MSP_PAS_EMAIL") == null){
				status = 3;
				dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(""));
			}else{
				dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(pasList.get(i).get("MSP_PAS_EMAIL").toString()));
			}
			if(pasList.get(i).get("NAMA_CABANG") == null){
				dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString("-"));
			}else{
				dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(pasList.get(i).get("NAMA_CABANG").toString()));
			}
			if(status == 0){
				dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString("BELUM DIKIRIM"));
			}else if(status == 1){
				dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString("SUDAH DIKIRIM"));
			}else if(status == 2){
				dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString("GAGAL DIKIRIM"));
			}else if(status == 3){
				dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString("TIDAK ADA EMAIL"));
			}else{
				dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString("-"));
			}
			if(pasList.get(i).get("ME_SENT_DATE") == null){
				dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString("-"));
			}else{
				dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(sdf2.format((Date) pasList.get(i).get("ME_SENT_DATE"))));
			}
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
	public void buildExcelAkseptasiDocument(String fileName, String dirFileName, List<Pas> pasList, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		
		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("Report Akseptasi PAS"));
		
		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
		
		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
		
		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		
		HSSFRow headerRow = sampleDataSheet.createRow(4);
		
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
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("No. HP"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama Pemegang"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("Tanggal Lahir"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("Nama Tertanggung"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("Tanggal Lahir"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("Nama Paket"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("Premi"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("Tanggal Aktivasi"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("Tanggal Akseptasi"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("Nama Agen"));
		
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(int i=0; i<pasList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1+4);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_mobile()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_pas_nama_pp()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_pas_dob_pp())));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_full_name()));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_date_of_birth())));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(pasList.get(i).getNama_produk().replace("PAS ", "")));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(pasList.get(i).getPremi()));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_message_date())));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_pas_accept_date())));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(pasList.get(i).getNama_agent()));
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
	public void buildExcelFollowUpAllDocument(String fileName, String dirFileName, List<List<Pas>> pasListx, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		HSSFCellStyle styleHeader, styleRow, styleFont;
		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("List data PAS yang sudah diaktivasi klien, tapi belum diinput di Cabang ALL"));
		
		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
		HSSFFont titleFont = workBook.createFont();
		titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleHeader = workBook.createCellStyle();
		styleHeader.setBorderBottom((short) 1);
		styleHeader.setBorderLeft((short) 1);
		styleHeader.setBorderRight((short) 1);
		styleHeader.setBorderTop((short) 1);
		styleHeader.setFont(titleFont);
		styleHeader.setWrapText(true);
		styleRow = workBook.createCellStyle();
		styleRow.setWrapText(true);
		styleRow.setBorderBottom((short) 1);
		styleRow.setBorderLeft((short) 1);
		styleRow.setBorderRight((short) 1);
		styleRow.setBorderTop((short) 1);
		styleRow.setWrapText(true);
		styleFont = workBook.createCellStyle();
		styleFont.setFont(titleFont);
		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
		
		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		int z = 0;
		for(int x = 0 ; x < pasListx.size(); x++){
			HSSFRow header = sampleDataSheet.createRow(4+z);
			HSSFCell firstHeader0 = header.createCell((short) 0);
			HSSFCell firstHeader1 = header.createCell((short) 1);
			firstHeader0.setCellValue(new HSSFRichTextString("Nama Cabang"));
			firstHeader1.setCellValue(new HSSFRichTextString(pasListx.get(x).get(0).getCabang() +"( "+pasListx.get(x).get(0).getBranch_admin_name()+ " )"));
			firstHeader0.setCellStyle(styleFont);
			firstHeader1.setCellStyle(styleFont);
			z++;
			HSSFRow headerRow = sampleDataSheet.createRow(4+z);
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
			
			firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
			firstHeaderCell1.setCellValue(new HSSFRichTextString("No. HP"));
			firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama Tertanggung"));
			firstHeaderCell3.setCellValue(new HSSFRichTextString("Tanggal Lahir"));
			firstHeaderCell4.setCellValue(new HSSFRichTextString("No. KTP TT"));
			firstHeaderCell5.setCellValue(new HSSFRichTextString("Nama Paket"));
			firstHeaderCell6.setCellValue(new HSSFRichTextString("Premi"));
			firstHeaderCell7.setCellValue(new HSSFRichTextString("No. Kartu"));
			firstHeaderCell8.setCellValue(new HSSFRichTextString("Nomor PIN"));
			firstHeaderCell9.setCellValue(new HSSFRichTextString("Nama Agen"));
			firstHeaderCell10.setCellValue(new HSSFRichTextString("Tanggal Aktivasi"));
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
			z++;
			
			// untuk isi row
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			for(int i = 0; i<pasListx.get(x).size(); i++) {
				HSSFRow dataRow1 = sampleDataSheet.createRow(z+4);
				HSSFCell firstRowCell0 = dataRow1.createCell((short) 0);
				HSSFCell firstRowCell1 = dataRow1.createCell((short) 1);
				HSSFCell firstRowCell2 = dataRow1.createCell((short) 2);
				HSSFCell firstRowCell3 = dataRow1.createCell((short) 3);
				HSSFCell firstRowCell4 = dataRow1.createCell((short) 4);
				HSSFCell firstRowCell5 = dataRow1.createCell((short) 5);
				HSSFCell firstRowCell6 = dataRow1.createCell((short) 6);
				HSSFCell firstRowCell7 = dataRow1.createCell((short) 7);
				HSSFCell firstRowCell8 = dataRow1.createCell((short) 8);
				HSSFCell firstRowCell9 = dataRow1.createCell((short) 9);
				HSSFCell firstRowCell10 = dataRow1.createCell((short) 10);
				firstRowCell0.setCellValue(new HSSFRichTextString((i+1)+""));
				firstRowCell1.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_mobile()));
				firstRowCell2.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_full_name()));
				firstRowCell3.setCellValue(new HSSFRichTextString(sdf.format(pasListx.get(x).get(i).getMsp_date_of_birth())));
				firstRowCell4.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_identity_no_tt()));
				firstRowCell5.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getNama_produk().replace("PAS ", "")));
				firstRowCell6.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getPremi()));
				firstRowCell7.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getNo_kartu()));
				firstRowCell8.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getPin()));
				firstRowCell9.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getNama_agent()));
				firstRowCell10.setCellValue(new HSSFRichTextString(sdf2.format(pasListx.get(x).get(i).getMsp_message_date())));
				firstRowCell0.setCellStyle(styleRow);
				firstRowCell1.setCellStyle(styleRow);
				firstRowCell2.setCellStyle(styleRow);
				firstRowCell3.setCellStyle(styleRow);
				firstRowCell4.setCellStyle(styleRow);
				firstRowCell5.setCellStyle(styleRow);
				firstRowCell6.setCellStyle(styleRow);
				firstRowCell7.setCellStyle(styleRow);
				firstRowCell8.setCellStyle(styleRow);
				firstRowCell9.setCellStyle(styleRow);
				firstRowCell10.setCellStyle(styleRow);
				z++;
			}
			z++;z++;
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
public void buildExcelBatalAllDocument(String fileName, String dirFileName, List<List<Pas>> pasListx, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		HSSFCellStyle styleHeader, styleRow, styleFont;
		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("List data PAS yang dibatalkan karena sudah lebih dari 3 bulan sejak diaktivasi klien, tapi belum diinput di Cabang ALL"));
		
		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
		HSSFFont titleFont = workBook.createFont();
		titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleHeader = workBook.createCellStyle();
		styleHeader.setBorderBottom((short) 1);
		styleHeader.setBorderLeft((short) 1);
		styleHeader.setBorderRight((short) 1);
		styleHeader.setBorderTop((short) 1);
		styleHeader.setFont(titleFont);
		styleHeader.setWrapText(true);
		styleRow = workBook.createCellStyle();
		styleRow.setWrapText(true);
		styleRow.setBorderBottom((short) 1);
		styleRow.setBorderLeft((short) 1);
		styleRow.setBorderRight((short) 1);
		styleRow.setBorderTop((short) 1);
		styleRow.setWrapText(true);
		styleFont = workBook.createCellStyle();
		styleFont.setFont(titleFont);
		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
		
		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		int z = 0;
		for(int x = 0 ; x < pasListx.size(); x++){
			HSSFRow header = sampleDataSheet.createRow(4+z);
			HSSFCell firstHeader0 = header.createCell((short) 0);
			HSSFCell firstHeader1 = header.createCell((short) 1);
			firstHeader0.setCellValue(new HSSFRichTextString("Nama Cabang"));
			firstHeader1.setCellValue(new HSSFRichTextString(pasListx.get(x).get(0).getCabang() +"( "+pasListx.get(x).get(0).getBranch_admin_name()+ " )"));
			firstHeader0.setCellStyle(styleFont);
			firstHeader1.setCellStyle(styleFont);
			z++;
			HSSFRow headerRow = sampleDataSheet.createRow(4+z);
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
			
			firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
			firstHeaderCell1.setCellValue(new HSSFRichTextString("No. HP"));
			firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama Tertanggung"));
			firstHeaderCell3.setCellValue(new HSSFRichTextString("Tanggal Lahir"));
			firstHeaderCell4.setCellValue(new HSSFRichTextString("No. KTP TT"));
			firstHeaderCell5.setCellValue(new HSSFRichTextString("Nama Paket"));
			firstHeaderCell6.setCellValue(new HSSFRichTextString("Premi"));
			firstHeaderCell7.setCellValue(new HSSFRichTextString("No. Kartu"));
			firstHeaderCell8.setCellValue(new HSSFRichTextString("Nomor PIN"));
			firstHeaderCell9.setCellValue(new HSSFRichTextString("Nama Agen"));
			firstHeaderCell10.setCellValue(new HSSFRichTextString("Tanggal Aktivasi"));
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
			z++;
			
			// untuk isi row
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			for(int i = 0; i<pasListx.get(x).size(); i++) {
				HSSFRow dataRow1 = sampleDataSheet.createRow(z+4);
				HSSFCell firstRowCell0 = dataRow1.createCell((short) 0);
				HSSFCell firstRowCell1 = dataRow1.createCell((short) 1);
				HSSFCell firstRowCell2 = dataRow1.createCell((short) 2);
				HSSFCell firstRowCell3 = dataRow1.createCell((short) 3);
				HSSFCell firstRowCell4 = dataRow1.createCell((short) 4);
				HSSFCell firstRowCell5 = dataRow1.createCell((short) 5);
				HSSFCell firstRowCell6 = dataRow1.createCell((short) 6);
				HSSFCell firstRowCell7 = dataRow1.createCell((short) 7);
				HSSFCell firstRowCell8 = dataRow1.createCell((short) 8);
				HSSFCell firstRowCell9 = dataRow1.createCell((short) 9);
				HSSFCell firstRowCell10 = dataRow1.createCell((short) 10);
				firstRowCell0.setCellValue(new HSSFRichTextString((i+1)+""));
				firstRowCell1.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_mobile()));
				firstRowCell2.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_full_name()));
				firstRowCell3.setCellValue(new HSSFRichTextString(sdf.format(pasListx.get(x).get(i).getMsp_date_of_birth())));
				firstRowCell4.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_identity_no_tt()));
				firstRowCell5.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getNama_produk().replace("PAS ", "")));
				firstRowCell6.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getPremi()));
				firstRowCell7.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getNo_kartu()));
				firstRowCell8.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getPin()));
				firstRowCell9.setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getNama_agent()));
				firstRowCell10.setCellValue(new HSSFRichTextString(sdf2.format(pasListx.get(x).get(i).getMsp_message_date())));
				firstRowCell0.setCellStyle(styleRow);
				firstRowCell1.setCellStyle(styleRow);
				firstRowCell2.setCellStyle(styleRow);
				firstRowCell3.setCellStyle(styleRow);
				firstRowCell4.setCellStyle(styleRow);
				firstRowCell5.setCellStyle(styleRow);
				firstRowCell6.setCellStyle(styleRow);
				firstRowCell7.setCellStyle(styleRow);
				firstRowCell8.setCellStyle(styleRow);
				firstRowCell9.setCellStyle(styleRow);
				firstRowCell10.setCellStyle(styleRow);
				z++;
			}
			z++;z++;
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
public void buildExcelDataPesertaPasDocument(String fileName, String dirFileName, List<Pas> pasList, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		HSSFCellStyle styleHeaderMain, styleHeader, styleRow, styleFont;
		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("DATA PESERTA PAS"));
		
		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
		HSSFFont titleFontMain = workBook.createFont();
		titleFontMain.setColor(HSSFColor.BLACK.index);
		titleFontMain.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleFontMain.setFontHeight((short) 250);
		HSSFFont titleFont = workBook.createFont();
		titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        styleHeaderMain = workBook.createCellStyle();
        styleHeaderMain.setFont(titleFontMain);
		styleHeader = workBook.createCellStyle();
		styleHeader.setBorderBottom((short) 1);
		styleHeader.setBorderLeft((short) 1);
		styleHeader.setBorderRight((short) 1);
		styleHeader.setBorderTop((short) 1);
		styleHeader.setFont(titleFont);
		styleHeader.setWrapText(true);
		styleRow = workBook.createCellStyle();
		styleRow.setWrapText(true);
		styleRow.setBorderBottom((short) 1);
		styleRow.setBorderLeft((short) 1);
		styleRow.setBorderRight((short) 1);
		styleRow.setBorderTop((short) 1);
		styleRow.setWrapText(true);
		styleFont = workBook.createCellStyle();
		styleFont.setFont(titleFont);
//		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
//		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
//		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
//		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
//		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
//		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
//		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
//		
//		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		
		firstUpperHeaderCell01.setCellStyle(styleHeaderMain);
		
		int z = 2;
			HSSFRow header = sampleDataSheet.createRow(z);
			HSSFCell firstHeader0 = header.createCell((short) 0);
			HSSFCell firstHeader1 = header.createCell((short) 1);
			firstHeader0.setCellValue(new HSSFRichTextString(""));
			firstHeader1.setCellValue(new HSSFRichTextString(""));
			firstHeader0.setCellStyle(styleFont);
			firstHeader1.setCellStyle(styleFont);
			z++;
			HSSFRow headerRow = sampleDataSheet.createRow(z);
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
			sampleDataSheet.setColumnWidth((short) 0, (short)(1500));
			sampleDataSheet.setColumnWidth((short) 1, (short)(5000));
			sampleDataSheet.setColumnWidth((short) 3, (short)(5000));
			sampleDataSheet.setColumnWidth((short) 4, (short)(8000));
			sampleDataSheet.setColumnWidth((short) 6, (short)(8000));
			sampleDataSheet.setColumnWidth((short) 8, (short)(5000));
			sampleDataSheet.setColumnWidth((short) 9, (short)(15000));
			sampleDataSheet.setColumnWidth((short) 13, (short)(6000));
			sampleDataSheet.setColumnWidth((short) 14, (short)(6000));
			
			firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
			firstHeaderCell1.setCellValue(new HSSFRichTextString("No Kartu PAS"));
			firstHeaderCell2.setCellValue(new HSSFRichTextString("Plan"));
			firstHeaderCell3.setCellValue(new HSSFRichTextString("No Polis Fire"));
			firstHeaderCell4.setCellValue(new HSSFRichTextString("Nama Tertanggung"));
			firstHeaderCell5.setCellValue(new HSSFRichTextString("Tgl Lahir Tertanggung"));
			firstHeaderCell6.setCellValue(new HSSFRichTextString("Nama Pemegang Polis"));
			firstHeaderCell7.setCellValue(new HSSFRichTextString("Tgl Lahir Pemegang Polis"));
			firstHeaderCell8.setCellValue(new HSSFRichTextString("No Identitas Pemegang Polis"));
			firstHeaderCell9.setCellValue(new HSSFRichTextString("Alamat Pemegang Polis"));
			firstHeaderCell10.setCellValue(new HSSFRichTextString("No Telp Pemegang Polis"));
			firstHeaderCell11.setCellValue(new HSSFRichTextString("Periode Aktif Polis"));
			firstHeaderCell12.setCellValue(new HSSFRichTextString("Tgl Terima SMS"));
			firstHeaderCell13.setCellValue(new HSSFRichTextString("Status Polis"));
			firstHeaderCell14.setCellValue(new HSSFRichTextString("Posisi"));
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
			z++;
			
			// untuk isi row
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			for(int i = 0; i<pasList.size(); i++) {
				HSSFRow dataRow1 = sampleDataSheet.createRow(z);
				HSSFCell firstRowCell0 = dataRow1.createCell((short) 0);
				HSSFCell firstRowCell1 = dataRow1.createCell((short) 1);
				HSSFCell firstRowCell2 = dataRow1.createCell((short) 2);
				HSSFCell firstRowCell3 = dataRow1.createCell((short) 3);
				HSSFCell firstRowCell4 = dataRow1.createCell((short) 4);
				HSSFCell firstRowCell5 = dataRow1.createCell((short) 5);
				HSSFCell firstRowCell6 = dataRow1.createCell((short) 6);
				HSSFCell firstRowCell7 = dataRow1.createCell((short) 7);
				HSSFCell firstRowCell8 = dataRow1.createCell((short) 8);
				HSSFCell firstRowCell9 = dataRow1.createCell((short) 9);
				HSSFCell firstRowCell10 = dataRow1.createCell((short) 10);
				HSSFCell firstRowCell11 = dataRow1.createCell((short) 11);
				HSSFCell firstRowCell12 = dataRow1.createCell((short) 12);
				HSSFCell firstRowCell13 = dataRow1.createCell((short) 13);
				HSSFCell firstRowCell14 = dataRow1.createCell((short) 14);
				firstRowCell0.setCellValue(new HSSFRichTextString((i+1)+""));
				firstRowCell1.setCellValue(new HSSFRichTextString(pasList.get(i).getNo_kartu()));
				firstRowCell2.setCellValue(new HSSFRichTextString(pasList.get(i).getNama_produk().replace("PAS ", "")));
				firstRowCell3.setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_policy_no()));
				firstRowCell4.setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_full_name()));
				if(pasList.get(i).getMsp_date_of_birth() == null){
					firstRowCell5.setCellValue(new HSSFRichTextString(""));
				}else{
					firstRowCell5.setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_date_of_birth())));
				}
				firstRowCell6.setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_pas_nama_pp()));
				if(pasList.get(i).getMsp_pas_dob_pp() == null){
					firstRowCell7.setCellValue(new HSSFRichTextString(""));
				}else{
					firstRowCell7.setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_pas_dob_pp())));
				}
				firstRowCell8.setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_identity_no()));
				firstRowCell9.setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_address_1()));
				firstRowCell10.setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_pas_phone_number()));
				firstRowCell11.setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_pas_beg_date())));
				if(pasList.get(i).getMsp_message_date() == null){
					firstRowCell12.setCellValue(new HSSFRichTextString(""));
				}else{
					firstRowCell12.setCellValue(new HSSFRichTextString(sdf2.format(pasList.get(i).getMsp_message_date())));
				}
				firstRowCell13.setCellValue(new HSSFRichTextString(pasList.get(i).getStatus_polis()));
				if("00".equals(pasList.get(i).getMsp_kode_sts_sms()) && pasList.get(i).getLspd_id() == 1){
					firstRowCell14.setCellValue(new HSSFRichTextString("INPUT DETAIL"));
				}else if(pasList.get(i).getLssp_id() == 1 && pasList.get(i).getLspd_id() == 2){
					firstRowCell14.setCellValue(new HSSFRichTextString("TRANSFER KE UW"));
				}else if(pasList.get(i).getLssp_id() == 5 && pasList.get(i).getLspd_id() == 2){
					firstRowCell14.setCellValue(new HSSFRichTextString("AKSEP UW"));
				}else if(pasList.get(i).getLssp_id() == 5 && pasList.get(i).getLspd_id() == 99){
					firstRowCell14.setCellValue(new HSSFRichTextString("TRANSFER PRINT POLIS"));
				}else if(pasList.get(i).getLssp_id() == 1 && pasList.get(i).getLspd_id() == 2){
					firstRowCell14.setCellValue(new HSSFRichTextString("DIBATALKAN"));
				}else{
					firstRowCell14.setCellValue(new HSSFRichTextString("-"));
				}
				firstRowCell0.setCellStyle(styleRow);
				firstRowCell1.setCellStyle(styleRow);
				firstRowCell2.setCellStyle(styleRow);
				firstRowCell3.setCellStyle(styleRow);
				firstRowCell4.setCellStyle(styleRow);
				firstRowCell5.setCellStyle(styleRow);
				firstRowCell6.setCellStyle(styleRow);
				firstRowCell7.setCellStyle(styleRow);
				firstRowCell8.setCellStyle(styleRow);
				firstRowCell9.setCellStyle(styleRow);
				firstRowCell10.setCellStyle(styleRow);
				firstRowCell11.setCellStyle(styleRow);
				firstRowCell12.setCellStyle(styleRow);
				firstRowCell13.setCellStyle(styleRow);
				firstRowCell14.setCellStyle(styleRow);
				z++;
			}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}
	
	public void buildExcelAkseptasiAllDocument(String fileName, String dirFileName, List<List<Pas>> pasListx, String dateFrom, String dateTo) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		HSSFCellStyle styleHeader, styleRow, styleFont;
		HSSFRow upperHeaderRow0 = sampleDataSheet.createRow(0);
		HSSFCell firstUpperHeaderCell01 = upperHeaderRow0.createCell((short) 0);
		firstUpperHeaderCell01.setCellValue(new HSSFRichTextString("Report Akseptasi PAS ALL"));
		
		HSSFRow upperHeaderRow1 = sampleDataSheet.createRow(1);
		HSSFFont titleFont = workBook.createFont();
		titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleHeader = workBook.createCellStyle();
		styleHeader.setBorderBottom((short) 1);
		styleHeader.setBorderLeft((short) 1);
		styleHeader.setBorderRight((short) 1);
		styleHeader.setBorderTop((short) 1);
		styleHeader.setFont(titleFont);
		styleHeader.setWrapText(true);
		styleRow = workBook.createCellStyle();
		styleRow.setWrapText(true);
		styleRow.setBorderBottom((short) 1);
		styleRow.setBorderLeft((short) 1);
		styleRow.setBorderRight((short) 1);
		styleRow.setBorderTop((short) 1);
		styleRow.setWrapText(true);
		styleFont = workBook.createCellStyle();
		styleFont.setFont(titleFont);
		HSSFRow upperHeaderRow2 = sampleDataSheet.createRow(2);
		HSSFCell firstUpperHeaderCell21 = upperHeaderRow2.createCell((short) 0);
		HSSFCell firstUpperHeaderCell22 = upperHeaderRow2.createCell((short) 1);
		HSSFCell firstUpperHeaderCell23 = upperHeaderRow2.createCell((short) 2);
		firstUpperHeaderCell21.setCellValue(new HSSFRichTextString("Periode"));
		firstUpperHeaderCell22.setCellValue(new HSSFRichTextString(":"));
		firstUpperHeaderCell23.setCellValue(new HSSFRichTextString(dateFrom + " sd. " + dateTo));
		
		HSSFRow upperHeaderRow3 = sampleDataSheet.createRow(3);
		int z = 0;
		for(int x = 0 ; x < pasListx.size(); x++){
			HSSFRow header = sampleDataSheet.createRow(4+z);
			HSSFCell firstHeader0 = header.createCell((short) 0);
			HSSFCell firstHeader1 = header.createCell((short) 1);
			firstHeader0.setCellValue(new HSSFRichTextString("Nama Cabang"));
			firstHeader1.setCellValue(new HSSFRichTextString(pasListx.get(x).get(0).getCabang()));
			firstHeader0.setCellStyle(styleFont);
			firstHeader1.setCellStyle(styleFont);
			z++;
			HSSFRow headerRow = sampleDataSheet.createRow(4+z);
			
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
			
			firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
			firstHeaderCell1.setCellValue(new HSSFRichTextString("No. HP"));
			firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama Pemegang"));
			firstHeaderCell3.setCellValue(new HSSFRichTextString("Tanggal Lahir"));
			firstHeaderCell4.setCellValue(new HSSFRichTextString("Nama Tertanggung"));
			firstHeaderCell5.setCellValue(new HSSFRichTextString("Tanggal Lahir"));
			firstHeaderCell6.setCellValue(new HSSFRichTextString("Nama Paket"));
			firstHeaderCell7.setCellValue(new HSSFRichTextString("Premi"));
			firstHeaderCell8.setCellValue(new HSSFRichTextString("Tanggal Aktivasi"));
			firstHeaderCell9.setCellValue(new HSSFRichTextString("Tanggal Akseptasi"));
			firstHeaderCell10.setCellValue(new HSSFRichTextString("Nama Agen"));
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
			z++;
			
			// untuk isi row
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for(int i = 0; i<pasListx.get(x).size(); i++) {
				HSSFRow dataRow1 = sampleDataSheet.createRow(z+4);
				HSSFCell firstRowCell0 = dataRow1.createCell((short) 0);
				HSSFCell firstRowCell1 = dataRow1.createCell((short) 1);
				HSSFCell firstRowCell2 = dataRow1.createCell((short) 2);
				HSSFCell firstRowCell3 = dataRow1.createCell((short) 3);
				HSSFCell firstRowCell4 = dataRow1.createCell((short) 4);
				HSSFCell firstRowCell5 = dataRow1.createCell((short) 5);
				HSSFCell firstRowCell6 = dataRow1.createCell((short) 6);
				HSSFCell firstRowCell7 = dataRow1.createCell((short) 7);
				HSSFCell firstRowCell8 = dataRow1.createCell((short) 8);
				HSSFCell firstRowCell9 = dataRow1.createCell((short) 9);
				HSSFCell firstRowCell10 = dataRow1.createCell((short) 10);
				dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
				dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_mobile()));
				dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_pas_nama_pp()));
				dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(sdf.format(pasListx.get(x).get(i).getMsp_pas_dob_pp())));
				dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getMsp_full_name()));
				dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(sdf.format(pasListx.get(x).get(i).getMsp_date_of_birth())));
				dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getNama_produk().replace("PAS ", "")));
				dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getPremi()));
				dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(sdf.format(pasListx.get(x).get(i).getMsp_message_date())));
				dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(sdf.format(pasListx.get(x).get(i).getMsp_pas_accept_date())));
				dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(pasListx.get(x).get(i).getNama_agent()));
				firstRowCell0.setCellStyle(styleRow);
				firstRowCell1.setCellStyle(styleRow);
				firstRowCell2.setCellStyle(styleRow);
				firstRowCell3.setCellStyle(styleRow);
				firstRowCell4.setCellStyle(styleRow);
				firstRowCell5.setCellStyle(styleRow);
				firstRowCell6.setCellStyle(styleRow);
				firstRowCell7.setCellStyle(styleRow);
				firstRowCell8.setCellStyle(styleRow);
				firstRowCell9.setCellStyle(styleRow);
				firstRowCell10.setCellStyle(styleRow);
				z++;
			}
			z++;z++;
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}

}
