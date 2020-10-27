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
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ekalife.elions.model.BlackList;
import com.ekalife.elions.model.NasabahSinarmasSekuriti;
import com.ekalife.elions.model.Pas;

public class XLSCreatorBlacklist {

	//@Override
	public void buildExcelDocument(String fileName, String dirFileName, List<BlackList> reportBlackList) throws Exception {
		
		HSSFWorkbook workBook = new HSSFWorkbook();
		//String jenisReport 	= (String) model.get("jenisReport");
		//HSSFSheet sampleDataSheet 	= workBook.createSheet("sheet1");
		HSSFSheet sampleDataSheet 	= workBook.createSheet(fileName);
		sampleDataSheet.setDefaultColumnWidth((short)12);
		//List dataNasabah 	= (List) model.get("dataNasabah");
		
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
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("ID"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("Nama"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("Tempat"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("Tgl. Lahir"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("Alamat"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("Telepon"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("Asuransi"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("Alasan"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("Tgl. Input"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("No. Policy"));
		firstHeaderCell11.setCellValue(new HSSFRichTextString("No. SPAJ"));
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(int i=0; i<reportBlackList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getLbl_id().toString()));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getLbl_nama()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getLbl_tempat()));
			if(reportBlackList.get(i).getLbl_tgl_lahir() == null){
				dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(""));
			}else{
				dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(sdf.format(reportBlackList.get(i).getLbl_tgl_lahir())));
			}
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getLbl_alamat()));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getTelepon()));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getLbl_asuransi()));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getLbl_alasan()));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(sdf.format(reportBlackList.get(i).getLbl_tgl_input())));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getNo_policy()));
			dataRow1.createCell((short) 11).setCellValue(new HSSFRichTextString(reportBlackList.get(i).getReg_spaj()));
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}

}
