package com.ekalife.utils.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ekalife.elions.model.NasabahSinarmasSekuriti;

public class XLSViewerDataNasabah extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String jenisReport 	= (String) model.get("jenisReport");
		HSSFSheet sheet 	= workBook.createSheet("Data Nasabah");
		sheet.setDefaultColumnWidth((short)12);
		List dataNasabah 	= (List) model.get("dataNasabah");
		
		//Yusuf - kolom2 yg belum tentu ada
		HSSFCell cell104 = null;
		HSSFCell cell105 = null;
		
		//Data Nasabah Sekuritas berdasarkan tanggal input / Data Nasabah Sekuritas berdasarkan begdate
		if(jenisReport.equals("tanggal_input") || jenisReport.equals("tanggal_efektif") || jenisReport.equals("tanggal_prod")){

			HSSFCell cell0=getCell(sheet,0,0);
			HSSFCell cell1=getCell(sheet,0,1);
			HSSFCell cell2=getCell(sheet,0,2);
			HSSFCell cell3=getCell(sheet,0,3);
			HSSFCell cell4=getCell(sheet,0,4);
			HSSFCell cell5=getCell(sheet,0,5);
			HSSFCell cell6=getCell(sheet,0,6);
			HSSFCell cell7=getCell(sheet,0,7);
			HSSFCell cell8=getCell(sheet,0,8);
			HSSFCell cell9=getCell(sheet,0,9);
			HSSFCell cell10=getCell(sheet,0,10);
			HSSFCell cell11=getCell(sheet,0,11);
			HSSFCell cell12=getCell(sheet,0,12);
			HSSFCell cell13=getCell(sheet,0,13);
			HSSFCell cell14=getCell(sheet,0,14);
			HSSFCell cell15=getCell(sheet,0,15);
			HSSFCell cell16=getCell(sheet,0,16);
			HSSFCell cell17=getCell(sheet,0,17);
			HSSFCell cell18=getCell(sheet,0,18);
			HSSFCell cell19=getCell(sheet,0,19);
			HSSFCell cell20=getCell(sheet,0,20);
			HSSFCell cell21=getCell(sheet,0,21);
			HSSFCell cell22=getCell(sheet,0,22);
			HSSFCell cell23=getCell(sheet,0,23);
			HSSFCell cell24=getCell(sheet,0,24);
			HSSFCell cell25=getCell(sheet,0,25);
			HSSFCell cell26=getCell(sheet,0,26);
			HSSFCell cell27=getCell(sheet,0,27);
			HSSFCell cell28=getCell(sheet,0,28);
			HSSFCell cell29=getCell(sheet,0,29);
			HSSFCell cell30=getCell(sheet,0,30);
			HSSFCell cell31=getCell(sheet,0,31);
			HSSFCell cell32=getCell(sheet,0,32);
			HSSFCell cell33=getCell(sheet,0,33);
			HSSFCell cell34=getCell(sheet,0,34);
			HSSFCell cell35=getCell(sheet,0,35);
			HSSFCell cell36=getCell(sheet,0,36);
			HSSFCell cell37=getCell(sheet,0,37);
			HSSFCell cell38=getCell(sheet,0,38);
			HSSFCell cell39=getCell(sheet,0,39);
			HSSFCell cell40=getCell(sheet,0,40);
			HSSFCell cell41=getCell(sheet,0,41);
			HSSFCell cell42=getCell(sheet,0,42);
			HSSFCell cell43=getCell(sheet,0,43);
			HSSFCell cell44=getCell(sheet,0,44);
			HSSFCell cell45=getCell(sheet,0,45);
			HSSFCell cell46=getCell(sheet,0,46);
			HSSFCell cell47=getCell(sheet,0,47);
			HSSFCell cell48=getCell(sheet,0,48);
			HSSFCell cell49=getCell(sheet,0,49);
			HSSFCell cell50=getCell(sheet,0,50);
			HSSFCell cell51=getCell(sheet,0,51);
			HSSFCell cell52=getCell(sheet,0,52);
			HSSFCell cell53=getCell(sheet,0,53);
			HSSFCell cell54=getCell(sheet,0,54);
			HSSFCell cell55=getCell(sheet,0,55);
			HSSFCell cell56=getCell(sheet,0,56);
			HSSFCell cell57=getCell(sheet,0,57);
			HSSFCell cell58=getCell(sheet,0,58);
			HSSFCell cell59=getCell(sheet,0,59);
			HSSFCell cell60=getCell(sheet,0,60);
			HSSFCell cell61=getCell(sheet,0,61);
			HSSFCell cell62=getCell(sheet,0,62);
			HSSFCell cell63=getCell(sheet,0,63);
			HSSFCell cell64=getCell(sheet,0,64);
			HSSFCell cell65=getCell(sheet,0,65);
			HSSFCell cell66=getCell(sheet,0,66);
			HSSFCell cell67=getCell(sheet,0,67);
			HSSFCell cell68=getCell(sheet,0,68);
			HSSFCell cell69=getCell(sheet,0,69);
			HSSFCell cell70=getCell(sheet,0,70);
			HSSFCell cell71=getCell(sheet,0,71);
			HSSFCell cell72=getCell(sheet,0,72);
			HSSFCell cell73=getCell(sheet,0,73);
			HSSFCell cell74=getCell(sheet,0,74);
			HSSFCell cell75=getCell(sheet,0,75);
			HSSFCell cell76=getCell(sheet,0,76);
			HSSFCell cell77=getCell(sheet,0,77);
			HSSFCell cell78=getCell(sheet,0,78);
			HSSFCell cell79=getCell(sheet,0,79);
			HSSFCell cell80=getCell(sheet,0,80);
			HSSFCell cell81=getCell(sheet,0,81);
			HSSFCell cell82=getCell(sheet,0,82);
			HSSFCell cell83=getCell(sheet,0,83);
			HSSFCell cell84=getCell(sheet,0,84);
			HSSFCell cell85=getCell(sheet,0,85);
			HSSFCell cell86=getCell(sheet,0,86);
			HSSFCell cell87=getCell(sheet,0,87);
			HSSFCell cell88=getCell(sheet,0,88);
			HSSFCell cell89=getCell(sheet,0,89);
			HSSFCell cell90=getCell(sheet,0,90);
			HSSFCell cell91=getCell(sheet,0,91);
			HSSFCell cell92=getCell(sheet,0,92);

			//alamat tagihan
			HSSFCell cell93=getCell(sheet,0,93);
			HSSFCell cell94=getCell(sheet,0,94);
			HSSFCell cell95=getCell(sheet,0,95);
			HSSFCell cell96=getCell(sheet,0,96);
			HSSFCell cell97=getCell(sheet,0,97);
			HSSFCell cell98=getCell(sheet,0,98);
			HSSFCell cell99=getCell(sheet,0,99);
			HSSFCell cell100=getCell(sheet,0,100);
			HSSFCell cell101=getCell(sheet,0,101);
			
			//Yusuf (23/07/2010) - LKU_ID DAN JENIS_TRANS
			HSSFCell cell102=getCell(sheet,0,102);
			HSSFCell cell103=getCell(sheet,0,103);
			
			//Yusuf (21/09/2010) - TGL_PRODUKSI dan PREMI_PRODUKSI
			if(jenisReport.equals("tanggal_prod")){
				cell104=getCell(sheet,0,104);
				cell105=getCell(sheet,0,105);
			}	
			
			//
			setText(cell0, "NO_SERI");
			setText(cell1, "NO_CIF");
			setText(cell2, "NO_POLIS");
			setText(cell3, "NO_SPAJ");
			setText(cell4, "KODE_PRODUK");
			setText(cell5, "NAMA_PRODUK");
			setText(cell6, "PP_NAMA");
			setText(cell7, "PP_GELAR");
			setText(cell8, "PP_MOTHER");
			setText(cell9, "PP_JENIS_ID");
			setText(cell10, "PP_ID");
		    setText(cell11, "PP_BIRTHPLACE");
		    setText(cell12, "PP_BIRTHDATE");
		    setText(cell13, "PP_WN");
		    setText(cell14, "PP_SEX");
		    setText(cell15, "PP_AGAMA");
		    setText(cell16, "PP_PENDIDIKAN");
			   
			   setText(cell17, "PP_JOB_DESC");
			   setText(cell18, "PP_TUJUAN");
			   setText(cell19, "PP_PENGHASILAN");
			   setText(cell20, "PP_PENDANAAN");
			   setText(cell21, "PP_KERJA");
			   setText(cell22, "PP_SMBR_PENGHASILAN");
			   setText(cell23, "PP_INDUSTRI");
			          
			   setText(cell24, "PP_ALAMAT_RUMAH");
			   setText(cell25, "PP_KD_POS_RUMAH");
		    setText(cell26, "PP_KOTA_RUMAH");
		    setText(cell27, "PP_AREA_CODE_RUMAH");
		    setText(cell28, "PP_TELPON_RUMAH");
		    setText(cell29, "PP_AREA_CODE_RUMAH2");
		    setText(cell30, "PP_TELPON_RUMAH2");
		    setText(cell31, "PP_ALAMAT_KANTOR");
		    setText(cell32, "PP_KD_POS_RUMAH2");
		    setText(cell33, "PP_KOTA_KANTOR");
		    setText(cell34, "PP_AREA_CODE_KANTOR");
		    setText(cell35, "PP_TELPON_KANTOR");
		    setText(cell36, "PP_AREA_CODE_KANTOR2");
		    setText(cell37, "PP_TELPON_KANTOR2");
		    setText(cell38, "PP_EMAIL");
		    setText(cell39, "PP_HP");
		    setText(cell40, "PP_AREA_CODE_FAX");
		    setText(cell41, "PP_NO_FAX ");

		    setText(cell42, "TT_NAMA");
		    setText(cell43, "TT_GELAR");
		    setText(cell44, "TT_MOTHER");
		    setText(cell45, "PP_JENIS_ID2");
		    setText(cell46, "TT_ID");
		    setText(cell47, "TT_BIRTHPLACE");
		    setText(cell48, "TT_BIRTHDATE");
		    setText(cell49, "TT_WN");
		    setText(cell50, "TT_SEX");
		    setText(cell51, "TT_AGAMA");
		    setText(cell52, "TT_PENDIDIKAN");
			   
			   setText(cell53, "PP_JOB_DESC2");
			   setText(cell54, "PP_TUJUAN2");
			   setText(cell55, "PP_PENGHASILAN2");
			   setText(cell56, "PP_PENDANAAN2");
			   setText(cell57, "PP_KERJA2");
			   setText(cell58, "PP_SMBR_PENGHASILAN2");
			   setText(cell59, "PP_INDUSTRI2");
			          	   
		    setText(cell60, "TT_ALAMAT_RUMAH");
		    setText(cell61, "TT_KD_POS_RUMAH");
		    setText(cell62, "TT_KOTA_RUMAH");
		    setText(cell63, "TT_AREA_CODE_RUMAH");
		    setText(cell64, "TT_TELPON_RUMAH");
		    setText(cell65, "TT_AREA_CODE_RUMAH2");
		    setText(cell66, "TT_TELPON_RUMAH2");
		    setText(cell67, "TT_ALAMAT_KANTOR");
		    setText(cell68, "TT_KD_POS_RUMAH2");
		    setText(cell69, "TT_KOTA_KANTOR");
		    setText(cell70, "TT_AREA_CODE_KANTOR");
		    setText(cell71, "TT_TELPON_KANTOR");
		    setText(cell72, "TT_AREA_CODE_KANTOR2");
		    setText(cell73, "TT_TELPON_KANTOR2");
		    setText(cell74, "TT_EMAIL");
		    setText(cell75, "TT_HP");
		    setText(cell76, "TT_AREA_CODE_FAX");
		    setText(cell77, "TT_NO_FAX");

			   setText(cell78, "INV_EFFECTIVEDATE");
			   setText(cell79, "INV_EXPIRYDATE");
		    setText(cell80, "INV_RETURN");
		    setText(cell81, "INV_PERIOD");
		    setText(cell82, "INV_TOT_NOMINAL");
		    setText(cell83, "INV_TOT_BUNGA");
		    setText(cell84, "INV_JENIS_RO");
			  
		    setText(cell85, "INV_BANK");
		    setText(cell86, "INV_BANK_BRANCH");
		    setText(cell87, "INV_BANK_CITY");
		    setText(cell88, "INV_BANK_ONBEHALF");
		    setText(cell89, "INV_BANK_NOREK");
			   
			   setText(cell90, "POSISI_DOKUMEN");
			   setText(cell91, "STATUS_POLIS");
			   setText(cell92, "STATUS_AKSEP");

			   //alamat tagihan
			   setText(cell93, "TAGIHAN_ALAMAT");
			   setText(cell94, "TAGIHAN_KD_POS");
			   setText(cell95, "TAGIHAN_TELP");
			   setText(cell96, "TAGIHAN_AREA_CODE");
			   setText(cell97, "TAGIHAN_TELP2");
			   setText(cell98, "TAGIHAN_AREA_CODE2");
			   setText(cell99, "TAGIHAN_EMAIL");
			   setText(cell100, "TAGIHAN_HP");
			   setText(cell101, "TAGIHAN_KOTA");
			   
			   //Yusuf (23/07/2010) LKU_ID & JENIS_TRANS
			   setText(cell102, "KURS");
			   setText(cell103, "JENIS_TRANS");
			   
				//Yusuf (21/09/2010) - TGL_PRODUKSI dan PREMI_PRODUKSI
				if(jenisReport.equals("tanggal_prod")){
					setText(cell104, "TGL_PRODUKSI");
					setText(cell105, "PREMI_PRODUKSI");
				}	
			   
			// untuk isi row
			for(int i=0; i<dataNasabah.size(); i++) {
				 cell0=getCell(sheet,(i+1),0);
				 cell1=getCell(sheet,(i+1),1);
				 cell2=getCell(sheet,(i+1),2);
				 cell3=getCell(sheet,(i+1),3);
				 cell4=getCell(sheet,(i+1),4);
				 cell5=getCell(sheet,(i+1),5);
				 cell6=getCell(sheet,(i+1),6);
				 cell7=getCell(sheet,(i+1),7);
				 cell8=getCell(sheet,(i+1),8);
				 cell9=getCell(sheet,(i+1),9);
				 cell10=getCell(sheet,(i+1),10);
				 cell11=getCell(sheet,(i+1),11);
				 cell12=getCell(sheet,(i+1),12);
				 cell13=getCell(sheet,(i+1),13);
				 cell14=getCell(sheet,(i+1),14);
				 cell15=getCell(sheet,(i+1),15);
				 cell16=getCell(sheet,(i+1),16);
				 cell17=getCell(sheet,(i+1),17);
				 cell18=getCell(sheet,(i+1),18);
				 cell19=getCell(sheet,(i+1),19);
				 cell20=getCell(sheet,(i+1),20);
				 cell21=getCell(sheet,(i+1),21);
				 cell22=getCell(sheet,(i+1),22);
				 cell23=getCell(sheet,(i+1),23);
				 cell24=getCell(sheet,(i+1),24);
				 cell25=getCell(sheet,(i+1),25);
				 cell26=getCell(sheet,(i+1),26);
				 cell27=getCell(sheet,(i+1),27);
				 cell28=getCell(sheet,(i+1),28);
				 cell29=getCell(sheet,(i+1),29);
				 cell30=getCell(sheet,(i+1),30);
				 cell31=getCell(sheet,(i+1),31);
				 cell32=getCell(sheet,(i+1),32);
				 cell33=getCell(sheet,(i+1),33);
				 cell34=getCell(sheet,(i+1),34);
				 cell35=getCell(sheet,(i+1),35);
				 cell36=getCell(sheet,(i+1),36);
				 cell37=getCell(sheet,(i+1),37);
				 cell38=getCell(sheet,(i+1),38);
				 cell39=getCell(sheet,(i+1),39);
				 cell40=getCell(sheet,(i+1),40);
				 cell41=getCell(sheet,(i+1),41);
				 cell42=getCell(sheet,(i+1),42);
				 cell43=getCell(sheet,(i+1),43);
				 cell44=getCell(sheet,(i+1),44);
				 cell45=getCell(sheet,(i+1),45);
				 cell46=getCell(sheet,(i+1),46);
				 cell47=getCell(sheet,(i+1),47);
				 cell48=getCell(sheet,(i+1),48);
				 cell49=getCell(sheet,(i+1),49);
				 cell50=getCell(sheet,(i+1),50);
				 cell51=getCell(sheet,(i+1),51);
				 cell52=getCell(sheet,(i+1),52);
				 cell53=getCell(sheet,(i+1),53);
				 cell54=getCell(sheet,(i+1),54);
				 cell55=getCell(sheet,(i+1),55);
				 cell56=getCell(sheet,(i+1),56);
				 cell57=getCell(sheet,(i+1),57);
				 cell58=getCell(sheet,(i+1),58);
				 cell59=getCell(sheet,(i+1),59);
				 cell60=getCell(sheet,(i+1),60);
				 cell61=getCell(sheet,(i+1),61);
				 cell62=getCell(sheet,(i+1),62);
				 cell63=getCell(sheet,(i+1),63);
				 cell64=getCell(sheet,(i+1),64);
				 cell65=getCell(sheet,(i+1),65);
				 cell66=getCell(sheet,(i+1),66);
				 cell67=getCell(sheet,(i+1),67);
				 cell68=getCell(sheet,(i+1),68);
				 cell69=getCell(sheet,(i+1),69);
				 cell70=getCell(sheet,(i+1),70);
				 cell71=getCell(sheet,(i+1),71);
				 cell72=getCell(sheet,(i+1),72);
				 cell73=getCell(sheet,(i+1),73);
				 cell74=getCell(sheet,(i+1),74);
				 cell75=getCell(sheet,(i+1),75);
				 
				 cell76=getCell(sheet,(i+1),76);
				 cell77=getCell(sheet,(i+1),77);
				 cell78=getCell(sheet,(i+1),78);
				 cell79=getCell(sheet,(i+1),79);
				 cell80=getCell(sheet,(i+1),80);
				 cell81=getCell(sheet,(i+1),81);
				 cell82=getCell(sheet,(i+1),82);
				 cell83=getCell(sheet,(i+1),83);
				 cell84=getCell(sheet,(i+1),84);
				 cell85=getCell(sheet,(i+1),85);
				 cell86=getCell(sheet,(i+1),86);
				 cell87=getCell(sheet,(i+1),87);
				 cell88=getCell(sheet,(i+1),88);
				 cell89=getCell(sheet,(i+1),89);
				 cell90=getCell(sheet,(i+1),90);
				 cell91=getCell(sheet,(i+1),91);
				 cell92=getCell(sheet,(i+1),92);
				 
				 cell93=getCell(sheet,(i+1),93);
				 cell94=getCell(sheet,(i+1),94);
				 cell95=getCell(sheet,(i+1),95);
				 cell96=getCell(sheet,(i+1),96);
				 cell97=getCell(sheet,(i+1),97);
				 cell98=getCell(sheet,(i+1),98);
				 cell99=getCell(sheet,(i+1),99);
				 cell100=getCell(sheet,(i+1),100);
				 cell101=getCell(sheet,(i+1),101);
				 cell102=getCell(sheet,(i+1),102);
				 cell103=getCell(sheet,(i+1),103);
				
				//Yusuf (21/09/2010) - TGL_PRODUKSI dan PREMI_PRODUKSI
				if(jenisReport.equals("tanggal_prod")){
					cell104=getCell(sheet,(i+1),104);
					cell105=getCell(sheet,(i+1),105);
				}	
				 
				NasabahSinarmasSekuriti nasabahSinarmasSekuriti = (NasabahSinarmasSekuriti) dataNasabah.get(i);
				
				setText(cell0, nasabahSinarmasSekuriti.getNO_SERI());
				setText(cell1, nasabahSinarmasSekuriti.getNO_CIF());
				setText(cell2, nasabahSinarmasSekuriti.getNO_POLIS());
				setText(cell3, nasabahSinarmasSekuriti.getNO_SPAJ());
				setText(cell4, nasabahSinarmasSekuriti.getKODE_PRODUK());
				setText(cell5, nasabahSinarmasSekuriti.getNAMA_PRODUK());
				setText(cell6, nasabahSinarmasSekuriti.getPP_NAMA());
				setText(cell7, nasabahSinarmasSekuriti.getPP_GELAR());
				setText(cell8, nasabahSinarmasSekuriti.getPP_MOTHER());
				setText(cell9, nasabahSinarmasSekuriti.getPP_JENIS_ID());
				setText(cell10, nasabahSinarmasSekuriti.getPP_ID());
			    setText(cell11, nasabahSinarmasSekuriti.getPP_BIRTHPLACE());
			    setText(cell12, nasabahSinarmasSekuriti.getPP_BIRTHDATE());
			    setText(cell13, nasabahSinarmasSekuriti.getPP_WN());
			    setText(cell14, nasabahSinarmasSekuriti.getPP_SEX());
			    setText(cell15, nasabahSinarmasSekuriti.getPP_AGAMA());
			    setText(cell16, nasabahSinarmasSekuriti.getPP_PENDIDIKAN());
				   
				   setText(cell17, nasabahSinarmasSekuriti.getPP_JOB_DESC());
				   setText(cell18, nasabahSinarmasSekuriti.getPP_TUJUAN());
				   setText(cell19, nasabahSinarmasSekuriti.getPP_PENGHASILAN());
				   setText(cell20, nasabahSinarmasSekuriti.getPP_PENDANAAN());
				   setText(cell21, nasabahSinarmasSekuriti.getPP_KERJA());
				   setText(cell22, nasabahSinarmasSekuriti.getPP_SMBR_PENGHASILAN());
				   setText(cell23, nasabahSinarmasSekuriti.getPP_INDUSTRI());
				          
				   setText(cell24, nasabahSinarmasSekuriti.getPP_ALAMAT_RUMAH());
				   setText(cell25, nasabahSinarmasSekuriti.getPP_KD_POS_RUMAH());
			    setText(cell26, nasabahSinarmasSekuriti.getPP_KOTA_RUMAH());
			    setText(cell27, nasabahSinarmasSekuriti.getPP_AREA_CODE_RUMAH());
			    setText(cell28, nasabahSinarmasSekuriti.getPP_TELPON_RUMAH());
			    setText(cell29, nasabahSinarmasSekuriti.getPP_AREA_CODE_RUMAH2());
			    setText(cell30, nasabahSinarmasSekuriti.getPP_TELPON_RUMAH2());
			    setText(cell31, nasabahSinarmasSekuriti.getPP_ALAMAT_KANTOR());
			    setText(cell32, nasabahSinarmasSekuriti.getPP_KD_POS_RUMAH2());
			    setText(cell33, nasabahSinarmasSekuriti.getPP_KOTA_KANTOR());
			    setText(cell34, nasabahSinarmasSekuriti.getPP_AREA_CODE_KANTOR());
			    setText(cell35, nasabahSinarmasSekuriti.getPP_TELPON_KANTOR());
			    setText(cell36, nasabahSinarmasSekuriti.getPP_AREA_CODE_KANTOR2());
			    setText(cell37, nasabahSinarmasSekuriti.getPP_TELPON_KANTOR2());
			    setText(cell38, nasabahSinarmasSekuriti.getPP_EMAIL());
			    setText(cell39, nasabahSinarmasSekuriti.getPP_HP());
			    setText(cell40, nasabahSinarmasSekuriti.getPP_AREA_CODE_FAX());
			    setText(cell41, nasabahSinarmasSekuriti.getPP_NO_FAX ());

			    setText(cell42, nasabahSinarmasSekuriti.getTT_NAMA());
			    setText(cell43, nasabahSinarmasSekuriti.getTT_GELAR());
			    setText(cell44, nasabahSinarmasSekuriti.getTT_MOTHER());
			    setText(cell45, nasabahSinarmasSekuriti.getPP_JENIS_ID2());
			    setText(cell46, nasabahSinarmasSekuriti.getTT_ID());
			    setText(cell47, nasabahSinarmasSekuriti.getTT_BIRTHPLACE());
			    setText(cell48, nasabahSinarmasSekuriti.getTT_BIRTHDATE());
			    setText(cell49, nasabahSinarmasSekuriti.getTT_WN());
			    setText(cell50, nasabahSinarmasSekuriti.getTT_SEX());
			    setText(cell51, nasabahSinarmasSekuriti.getTT_AGAMA());
			    setText(cell52, nasabahSinarmasSekuriti.getTT_PENDIDIKAN());
				   
				   setText(cell53, nasabahSinarmasSekuriti.getPP_JOB_DESC2());
				   setText(cell54, nasabahSinarmasSekuriti.getPP_TUJUAN2());
				   setText(cell55, nasabahSinarmasSekuriti.getPP_PENGHASILAN2());
				   setText(cell56, nasabahSinarmasSekuriti.getPP_PENDANAAN2());
				   setText(cell57, nasabahSinarmasSekuriti.getPP_KERJA2());
				   setText(cell58, nasabahSinarmasSekuriti.getPP_SMBR_PENGHASILAN2());
				   setText(cell59, nasabahSinarmasSekuriti.getPP_INDUSTRI2());
				          	   
			    setText(cell60, nasabahSinarmasSekuriti.getTT_ALAMAT_RUMAH());
			    setText(cell61, nasabahSinarmasSekuriti.getTT_KD_POS_RUMAH());
			    setText(cell62, nasabahSinarmasSekuriti.getTT_KOTA_RUMAH());
			    setText(cell63, nasabahSinarmasSekuriti.getTT_AREA_CODE_RUMAH());
			    setText(cell64, nasabahSinarmasSekuriti.getTT_TELPON_RUMAH());
			    setText(cell65, nasabahSinarmasSekuriti.getTT_AREA_CODE_RUMAH2());
			    setText(cell66, nasabahSinarmasSekuriti.getTT_TELPON_RUMAH2());
			    setText(cell67, nasabahSinarmasSekuriti.getTT_ALAMAT_KANTOR());
			    setText(cell68, nasabahSinarmasSekuriti.getTT_KD_POS_RUMAH2());
			    setText(cell69, nasabahSinarmasSekuriti.getTT_KOTA_KANTOR());
			    setText(cell70, nasabahSinarmasSekuriti.getTT_AREA_CODE_KANTOR());
			    setText(cell71, nasabahSinarmasSekuriti.getTT_TELPON_KANTOR());
			    setText(cell72, nasabahSinarmasSekuriti.getTT_AREA_CODE_KANTOR2());
			    setText(cell73, nasabahSinarmasSekuriti.getTT_TELPON_KANTOR2());
			    setText(cell74, nasabahSinarmasSekuriti.getTT_EMAIL());
			    setText(cell75, nasabahSinarmasSekuriti.getTT_HP());
			    setText(cell76, nasabahSinarmasSekuriti.getTT_AREA_CODE_FAX());
			    setText(cell77, nasabahSinarmasSekuriti.getTT_NO_FAX());

				   setText(cell78, nasabahSinarmasSekuriti.getINV_EFFECTIVEDATE());
				   setText(cell79, nasabahSinarmasSekuriti.getINV_EXPIRYDATE());
			    setText(cell80, nasabahSinarmasSekuriti.getINV_RETURN());
			    setText(cell81, nasabahSinarmasSekuriti.getINV_PERIOD());
			    setText(cell82, nasabahSinarmasSekuriti.getINV_TOT_NOMINAL());
			    setText(cell83, nasabahSinarmasSekuriti.getINV_TOT_BUNGA());
			    setText(cell84, nasabahSinarmasSekuriti.getINV_JENIS_RO());
				  
			    setText(cell85, nasabahSinarmasSekuriti.getINV_BANK());
			    setText(cell86, nasabahSinarmasSekuriti.getINV_BANK_BRANCH());
			    setText(cell87, nasabahSinarmasSekuriti.getINV_BANK_CITY());
			    setText(cell88, nasabahSinarmasSekuriti.getINV_BANK_ONBEHALF());
			    setText(cell89, nasabahSinarmasSekuriti.getINV_BANK_NOREK());
				   
				   setText(cell90, nasabahSinarmasSekuriti.getPOSISI_DOKUMEN());
				   setText(cell91, nasabahSinarmasSekuriti.getSTATUS_POLIS());
				   setText(cell92, nasabahSinarmasSekuriti.getSTATUS_AKSEP());
				   
				   setText(cell93, nasabahSinarmasSekuriti.getTAGIHAN_ALAMAT());
				   setText(cell94, nasabahSinarmasSekuriti.getTAGIHAN_KD_POS());
				   setText(cell95, nasabahSinarmasSekuriti.getTAGIHAN_TELP());
				   setText(cell96, nasabahSinarmasSekuriti.getTAGIHAN_AREA_CODE());
				   setText(cell97, nasabahSinarmasSekuriti.getTAGIHAN_TELP2());
				   setText(cell98, nasabahSinarmasSekuriti.getTAGIHAN_AREA_CODE2());
				   setText(cell99, nasabahSinarmasSekuriti.getTAGIHAN_EMAIL());
				   setText(cell100, nasabahSinarmasSekuriti.getTAGIHAN_HP());
				   setText(cell101, nasabahSinarmasSekuriti.getTAGIHAN_KOTA());

				   String lku = nasabahSinarmasSekuriti.getLKU_ID();
				   setText(cell102, (lku.equals("01") ? "IDR" : "USD"));
				   setText(cell103, nasabahSinarmasSekuriti.getJENIS_TRANS());

					//Yusuf (21/09/2010) - TGL_PRODUKSI dan PREMI_PRODUKSI
					if(jenisReport.equals("tanggal_prod")){
						setText(cell104, nasabahSinarmasSekuriti.getTGL_PRODUKSI());
						setText(cell105, nasabahSinarmasSekuriti.getPREMI_PRODUKSI());
					}	

			}
			
		//Data Jatuh Tempo Sekuritas / Data Rollover Sekuritas berdasarkan tgl produksi
		}else if(jenisReport.equals("jatuh_tempo") || jenisReport.equals("roll_over")){

			int x = 0;
			
			setText(getCell(sheet, 0, x++), "NAMA_CABANG");
			setText(getCell(sheet, 0, x++), "NO_POLIS");
			setText(getCell(sheet, 0, x++), "REG_SPAJ");
			setText(getCell(sheet, 0, x++), "PEMEGANG");
			setText(getCell(sheet, 0, x++), "PRODUK");
			setText(getCell(sheet, 0, x++), "MGI");
			setText(getCell(sheet, 0, x++), "KURS");
			setText(getCell(sheet, 0, x++), "PREMI");
			setText(getCell(sheet, 0, x++), "JENIS_ROLLOVER");
			setText(getCell(sheet, 0, x++), "BEG_DATE");
			setText(getCell(sheet, 0, x++), "END_DATE");
			setText(getCell(sheet, 0, x++), "JATUH_TEMPO");
			setText(getCell(sheet, 0, x++), "NAMA_REFF");
			setText(getCell(sheet, 0, x++), "USER_INPUT");
			setText(getCell(sheet, 0, x++), "CIF");
			setText(getCell(sheet, 0, x++), "MPR_DEPOSIT");
			setText(getCell(sheet, 0, x++), "MPR_RATE");
			setText(getCell(sheet, 0, x++), "MPR_INTEREST");
			setText(getCell(sheet, 0, x++), "JENIS_TRANS");
			setText(getCell(sheet, 0, x++), "TGL_PRODUKSI");
			setText(getCell(sheet, 0, x++), "PREMI_PRODUKSI");
			
			// untuk isi row
			for(int i=0; i<dataNasabah.size(); i++) {
				x = 0;
				Map nasabah = (Map) dataNasabah.get(i);
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("NAMA_CABANG"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("MSPO_POLICY_NO_FORMAT"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("REG_SPAJ"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("PEMEGANG"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("LSDBS_NAME"));
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("MGI")).toString());
				String lku_id = (String) nasabah.get("LKU_ID");
				setText(getCell(sheet,(i+1), x++), (lku_id.equals("01") ? "IDR" : "USD"));
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("MSPR_PREMIUM")).toString());
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("JENIS_RO"));
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("MSPRO_BEG_DATE")).toString());
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("MSPRO_END_DATE")).toString());
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("MPR_MATURE_DATE")==null?null:((Date) nasabah.get("MPR_MATURE_DATE")).toString()));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("NAMA_REFF"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("LUS_LOGIN_NAME"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("CIF"));
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("PREMI")).toString());
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("RATE")).toString());
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("BUNGA")).toString());
				setText(getCell(sheet,(i+1), x++), ((String) nasabah.get("JENIS_TRANS")).toString());
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("TGL_PRODUKSI")).toString());
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("PREMI_PRODUKSI")).toString());
			}
			
		//Data Yang Sudah Pencairan
		}else if(jenisReport.equals("cair")){

			int x = 0;
			
			setText(getCell(sheet, 0, x++), "NAMA_CABANG");
			setText(getCell(sheet, 0, x++), "NO_POLIS");
			setText(getCell(sheet, 0, x++), "REG_SPAJ");
			setText(getCell(sheet, 0, x++), "PEMEGANG");
			setText(getCell(sheet, 0, x++), "PRODUK");
			setText(getCell(sheet, 0, x++), "MGI");
			setText(getCell(sheet, 0, x++), "KURS");
			setText(getCell(sheet, 0, x++), "PREMI");
			setText(getCell(sheet, 0, x++), "JENIS_ROLLOVER");
			setText(getCell(sheet, 0, x++), "BEG_DATE");
			setText(getCell(sheet, 0, x++), "END_DATE");
			setText(getCell(sheet, 0, x++), "JATUH_TEMPO");
			setText(getCell(sheet, 0, x++), "NAMA_REFF");
			setText(getCell(sheet, 0, x++), "USER_INPUT");
			setText(getCell(sheet, 0, x++), "CIF");
			setText(getCell(sheet, 0, x++), "MPR_DEPOSIT");
			setText(getCell(sheet, 0, x++), "MPR_RATE");
			setText(getCell(sheet, 0, x++), "MPR_INTEREST");
			setText(getCell(sheet, 0, x++), "JENIS_TRANS");
			setText(getCell(sheet, 0, x++), "TGL_PRODUKSI");
			setText(getCell(sheet, 0, x++), "PREMI_PRODUKSI");
			setText(getCell(sheet, 0, x++), "TGL_CAIR");
			setText(getCell(sheet, 0, x++), "JML_CAIR");
			
			// untuk isi row
			for(int i=0; i<dataNasabah.size(); i++) {
				x = 0;
				Map nasabah = (Map) dataNasabah.get(i);
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("NAMA_CABANG"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("MSPO_POLICY_NO_FORMAT"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("REG_SPAJ"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("PEMEGANG"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("LSDBS_NAME"));
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("MGI")).toString());
				String lku_id = (String) nasabah.get("LKU_ID");
				setText(getCell(sheet,(i+1), x++), (lku_id.equals("01") ? "IDR" : "USD"));
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("MSPR_PREMIUM")).toString());
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("JENIS_RO"));
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("MSPRO_BEG_DATE")).toString());
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("MSPRO_END_DATE")).toString());
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("MPR_MATURE_DATE")==null?null:((Date) nasabah.get("MPR_MATURE_DATE")).toString()));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("NAMA_REFF"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("LUS_LOGIN_NAME"));
				setText(getCell(sheet,(i+1), x++), (String) nasabah.get("CIF"));
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("PREMI")).toString());
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("RATE")).toString());
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("BUNGA")).toString());
				setText(getCell(sheet,(i+1), x++), ((String) nasabah.get("JENIS_TRANS")).toString());
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("TGL_PRODUKSI")).toString());
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("PREMI_PRODUKSI")).toString());
				setText(getCell(sheet,(i+1), x++), ((Date) nasabah.get("TGL_BAYAR")).toString());
				setText(getCell(sheet,(i+1), x++), ((BigDecimal) nasabah.get("JUM_BAYAR")).toString());
			}
			
		}
		
	}

}
