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

import com.ekalife.elions.model.NasabahSinarmasSekuriti;
import com.ekalife.elions.model.Pas;

public class XLSCreatorFreeSimasRumah {

	//@Override
	public void buildExcelDocument(String fileName, String dirFileName, List<Pas> pasList) throws Exception {
		
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
		HSSFCell firstHeaderCell12 = headerRow.createCell((short) 12);
		HSSFCell firstHeaderCell13 = headerRow.createCell((short) 13);
		HSSFCell firstHeaderCell14 = headerRow.createCell((short) 14);
		HSSFCell firstHeaderCell15 = headerRow.createCell((short) 15);
		HSSFCell firstHeaderCell16 = headerRow.createCell((short) 16);
		HSSFCell firstHeaderCell17 = headerRow.createCell((short) 17);
		HSSFCell firstHeaderCell18 = headerRow.createCell((short) 18);
		HSSFCell firstHeaderCell19 = headerRow.createCell((short) 19);
		HSSFCell firstHeaderCell20 = headerRow.createCell((short) 20);
		HSSFCell firstHeaderCell21 = headerRow.createCell((short) 21);
		HSSFCell firstHeaderCell22 = headerRow.createCell((short) 22);
		
		firstHeaderCell0.setCellValue(new HSSFRichTextString("No."));
		firstHeaderCell1.setCellValue(new HSSFRichTextString("No. POLICY FREE"));
		firstHeaderCell2.setCellValue(new HSSFRichTextString("Kode Nama"));
		firstHeaderCell3.setCellValue(new HSSFRichTextString("Nama"));
		firstHeaderCell4.setCellValue(new HSSFRichTextString("No. KTP"));
		firstHeaderCell5.setCellValue(new HSSFRichTextString("Jenis Pekerjaan"));
		firstHeaderCell6.setCellValue(new HSSFRichTextString("Bidang Usaha"));
		firstHeaderCell7.setCellValue(new HSSFRichTextString("Sumber Dana"));
		firstHeaderCell8.setCellValue(new HSSFRichTextString("Alamat Tertanggung"));
		firstHeaderCell9.setCellValue(new HSSFRichTextString("Kode Pos"));
		firstHeaderCell10.setCellValue(new HSSFRichTextString("No. Telp"));
		firstHeaderCell11.setCellValue(new HSSFRichTextString("Hp"));
		firstHeaderCell12.setCellValue(new HSSFRichTextString("Email"));
		firstHeaderCell13.setCellValue(new HSSFRichTextString("Kode Okupasi"));
		firstHeaderCell14.setCellValue(new HSSFRichTextString("Kode Alamat"));
		firstHeaderCell15.setCellValue(new HSSFRichTextString("Alamat Pertanggungan"));
		firstHeaderCell16.setCellValue(new HSSFRichTextString("No. Rumah"));
		firstHeaderCell17.setCellValue(new HSSFRichTextString("Kode Pos"));
		firstHeaderCell18.setCellValue(new HSSFRichTextString("No. Telp"));
		firstHeaderCell19.setCellValue(new HSSFRichTextString("Kode obyek Sekitar"));
		firstHeaderCell20.setCellValue(new HSSFRichTextString("TGL LAHIR"));
		firstHeaderCell21.setCellValue(new HSSFRichTextString("NO REFERENSI"));
		firstHeaderCell22.setCellValue(new HSSFRichTextString("BEGIN DATE"));
		
		// untuk isi row
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(int i=0; i<pasList.size(); i++) {
			HSSFRow dataRow1 = sampleDataSheet.createRow(i+1);
			dataRow1.createCell((short) 0).setCellValue(new HSSFRichTextString((i+1)+""));
			dataRow1.createCell((short) 1).setCellValue(new HSSFRichTextString(""));
			dataRow1.createCell((short) 2).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_code_name()));
			dataRow1.createCell((short) 3).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_name()));
			dataRow1.createCell((short) 4).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_identity()));
			dataRow1.createCell((short) 5).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_occupation()));
			dataRow1.createCell((short) 6).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_type_business()));
			dataRow1.createCell((short) 7).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_source_fund()));
			dataRow1.createCell((short) 8).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_address_1()));
			dataRow1.createCell((short) 9).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_postal_code()));
			dataRow1.createCell((short) 10).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_phone_number()));
			dataRow1.createCell((short) 11).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_mobile()));
			dataRow1.createCell((short) 12).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_email()));
			dataRow1.createCell((short) 13).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_okupasi()));
			dataRow1.createCell((short) 14).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_insured_addr_code()));
			dataRow1.createCell((short) 15).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_insured_addr()));
			dataRow1.createCell((short) 16).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_insured_addr_no()));
			dataRow1.createCell((short) 17).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_insured_postal_code()));
			dataRow1.createCell((short) 18).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_insured_phone_number()));
			if(pasList.get(i).getMsp_fire_insured_addr_envir() == null){
				dataRow1.createCell((short) 19).setCellValue(new HSSFRichTextString(""));
			}else{
				dataRow1.createCell((short) 19).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_insured_addr_envir().toString()));
			}
			dataRow1.createCell((short) 20).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_date_of_birth2()));
			dataRow1.createCell((short) 21).setCellValue(new HSSFRichTextString(pasList.get(i).getMsp_fire_id()));
			if(pasList.get(i).getMsp_fire_beg_date() == null){
				dataRow1.createCell((short) 22).setCellValue(new HSSFRichTextString(""));
			}else{
				dataRow1.createCell((short) 22).setCellValue(new HSSFRichTextString(sdf.format(pasList.get(i).getMsp_fire_beg_date())));
			}
				
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(dirFileName);
		workBook.write(fileOutputStream);
		fileOutputStream.close();
	}

}
