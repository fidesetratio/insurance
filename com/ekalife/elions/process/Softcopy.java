package com.ekalife.elions.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.FileCopyUtils;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.CommandUploadBac;
import com.ekalife.elions.model.Cplan;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.ITextPdf;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.PdfUtils;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentDao;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import id.co.sinarmaslife.std.model.vo.DropDown;




public class Softcopy extends ParentDao{ 	
	
	private boolean dokumenOtomatis(String spaj, User currentUser, String jenis) {
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			String reportPath = null;
			Object dataSource = null;
			String cabang = uwDao.selectCabangFromSpaj(spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
			// Upd-Helpdesk 68901: TTD Polis DBD AGENCY, DBD BP, DBD SYARIAH, AP/BP 
			// String hamid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";			
			String hamid = props.getProperty("images.ttd.direksi");
			
			if(PdfUtils.isExist(exportDirectory + "\\" + jenis + ".pdf")) {
				return true;
			}
			
			if(props.getProperty("pdf.surat_polis").equals(jenis)) {
				dataSource =uwDao.getDataSource();
				
				List tmp = uwDao.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) tmp.get(0)).get("DETBISNIS");
				
//				String va = financeDao.selectVirtualAccountSpaj(spaj);
				
				Integer flag_cc = bacDao.select_flag_cc(spaj);
				
//				if(va != null){
				if(flag_cc ==0){
					params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
					
					List cekSpajPromo = bacDao.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
					
					if(!cekSpajPromo.isEmpty()){
						reportPath = props.get("report.surat_polis.va_promo").toString();
					}else if(lsbs.equals("217") && lsdbs.equals("002")){ //untuk ERbe di set menggunakan surat_polis
						reportPath = props.get("report.surat_polis").toString();
					//set report syariah igaukiarwan 20190814
					}else if(products.syariah(lsbs, lsdbs)){
						reportPath = props.get("report.surat_polis.syariah").toString();
					}else{
						reportPath = props.get("report.surat_polis.va").toString();
					}	
					
					//reportPath = props.get("report.surat_polis.va").toString();
					params.put("reportPath", "/WEB-INF/classes/" + reportPath);
					
				}else {
					params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
					//set report syariah igaukiarwan 20190814
					if (products.syariah(lsbs, lsdbs)){
						reportPath = props.get("report.surat_polis.syariah").toString();
						params.put("reportPath", "/WEB-INF/classes/" + reportPath);
					}
					else{
						reportPath = props.get("report.surat_polis").toString();
						params.put("reportPath", "/WEB-INF/classes/" + reportPath);
						}
					}
				params.put("props", props);
				params.put("spaj", spaj);
				
				Connection conn = null;
				int iErr = 0;				
				try {
					conn = this.getDataSource().getConnection();
					JasperUtils.exportReportToPdf(
							reportPath+".jasper", exportDirectory, jenis+".pdf", 
							params, conn, 
							null, props.getProperty("pdf.ownerPassword"), props.getProperty("pdf.userPassword"));
				} catch (Exception e) {
					logger.error("ERROR :", e);
					iErr = 1;
				}finally{
					closeConnection(conn);
					if (iErr == 1){
						return false;						
					}
				}
				
			}else if(props.getProperty("pdf.tanda_terima_polis").equals(jenis)) {
				dataSource = uwDao.getDataSource();
				String lsbs_id = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
				reportPath = props.getProperty("report.tandaterimapolis.pas");
				params.put("spaj", spaj);
				params.put("reportPath", "/WEB-INF/classes/"+reportPath);
			}else if(props.getProperty("pdf.panduan_account").equals(jenis)){
				String dir = props.getProperty("pdf.template");
//				PdfReader reader = new PdfReader(props.getProperty("pdf.template")+"\\panduan.pdf");
//				String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+props.getProperty("pdf.panduan_account")+".pdf";
				File file = null;
				file = new File(props.getProperty("pdf.template")+"\\panduan.pdf");
			}else if(props.getProperty("pdf.polis_pas").equals(jenis) || props.getProperty("pdf.polis_dbd").equals(jenis)) {
				String dir = props.getProperty("pdf.dir.syaratpolis");
				List detBisnis = uwDao.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
				String jenisPolis = props.getProperty("pdf.polis_pas");
				String pathTemplatePolis = props.getProperty("pdf.template.pas");
				
				
				List rider = uwDao.selectRiderPolisPas(spaj);
				
		        File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
		        if(!userDir.exists()) {
		            userDir.mkdirs();
		        }
	
				List<String> pdfs = new ArrayList<String>();
				boolean suksesMerge = false;
				String PolisSSU = "";
				String SSK = "";
				String merge = "";
				
				PolisSSU = dir + "\\" + lsbs + "-" + lsdbs +".pdf";
				pdfs.add(PolisSSU);
				if(rider.size()>0){
					for(int i=1;i<detBisnis.size();i++){
						String lsbs_rider = (String) ((Map) detBisnis.get(i)).get("BISNIS");
						String lsdbs_rider = (String) ((Map) detBisnis.get(i)).get("DETBISNIS");
						SSK = dir + "\\" +"RIDER"+"\\"+ lsbs_rider + "-" + lsdbs_rider +".pdf";
						pdfs.add(SSK);
					}
				}
				if(lsbs.equals("187")){
					if("005,006".indexOf(lsdbs)>-1){
						merge = "MergeSSUSSKBP.pdf";
					}else if("011,012,013,014".indexOf(lsdbs)>-1){
						merge = "MergeSSUSSKSAC.pdf";
					}else{
						merge = "MergeSSUSSK.pdf";
					}
				}else if(lsbs.equals("203")){
					if(lsdbs.equals("003")){
						merge = "MergeSSUSSKDBDMALL.pdf";
					}
					else if(lsdbs.equals("002")){
						merge = "MergeSSUSSKDBD.pdf";
					}else if(lsdbs.equals("004")){
						merge = "MergeSSUSSKDBDNISSAN.pdf";
					}else{
						merge = "MergeSSUSSKDBDBP.pdf";
					}
					pathTemplatePolis = props.getProperty("pdf.template.dbd");
					jenisPolis = props.getProperty("pdf.polis_dbd");
				}else if(lsbs.equals("209")){
					merge = "MergeSSUSSKDBDSYARIAH.pdf";
					pathTemplatePolis = props.getProperty("pdf.template.dbd");
					jenisPolis = props.getProperty("pdf.polis_dbd");
				}else if(lsbs.equals("073")){
					if(lsdbs.equals("015")){
						merge = "MergeSSUSSK-PANISSAN.pdf";
						pathTemplatePolis = props.getProperty("pdf.template.pa");
						jenisPolis = props.getProperty("pdf.polis_pa");
					}
				}//tika nissan
				OutputStream output = new FileOutputStream(pathTemplatePolis+"\\"+merge);
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				
				PdfReader reader = new PdfReader(pathTemplatePolis+"\\"+merge);
				String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+jenisPolis+".pdf";
				File file = null;
				file = new File(outputName);
				PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
		        PdfContentByte cb = stamp.getOverContent(1);
		        		cb = stamp.getOverContent(1);
	                	cb.beginText();
	                	//BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
	                	//BaseFont arial = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	                	BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	                	//BaseFont arial_narrow = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	        			//BaseFont arial_narrow_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	                	cb.setFontAndSize(bf, 12);
	                	int row_header =0;
	                	if(lsbs.equals("187")){
		                	if("005,006".indexOf(lsdbs)>-1){
		                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS ASURANSI JIWA INDIVIDU", 299, 760, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS PERSONAL ACCIDENT SINARMASLIFE (PAS) - BUSINESS PARTNER", 299, 748, 0);
		                	}else if("011,012,013,014".indexOf(lsdbs)>-1){
		                		row_header=36;
		                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS SMART ACCIDENT CARE", 299, 748-row_header, 0);
		                	}else{
		                		row_header=36;
		                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS PERSONAL ACCIDENT SINARMASLIFE (PAS)", 299, 748-row_header, 0);
		                	}
	                	}else if(lsbs.equals("203")){
	                		if("002,003".indexOf(lsdbs)>-1){
	                			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS ASURANSI JIWA INDIVIDU", 299, 760, 0);
	                			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "ASURANSI DEMAM BERDARAH", 299, 748, 0);
	                		}else if("004".indexOf(lsdbs)>-1){
	                			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS DEMAM BERDARAH", 299, 810-row_header, 0);
	                		}else{
	                			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS ASURANSI JIWA INDIVIDU", 299, 760, 0);
	                			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "ASURANSI DEMAM BERDARAH - BUSINESS PARTNER", 299, 748, 0);
	                		}
	                		
	    				}else if(lsbs.equals("209")){
	                		
	                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS ASURANSI KESEHATAN INDIVIDU SYARIAH", 299, 734, 0);
	                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "\"SMiLe ASURANSI DEMAM BERDARAH SYARIAH\"", 299, 722, 0);
	                		
	    				}else if(lsbs.equals("073") && lsdbs.equals("015")){
	    					row_header=36;
	                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS SMiLe PERSONAL ACCIDENT STAND ALONE A", 299, 830-row_header, 0);
	    				}
	
	                	//bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
	                	bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	                	cb.setFontAndSize(bf, 8);
	
	                	//Kode Bea Meterai
	//                	String meterai = elionsManager.selectIzinMeteraiTerakhir();
	//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, meterai,	 							426, 767, 0);
	                	
	                	//Bagian Label
	                	int row_bp = 0;
	                	int row_tambah= 0;
	                	if(( lsbs.equals("187") && "005,006".indexOf(lsdbs)>-1) || lsbs.equals("203") ){
	                		row_bp += 4;
	                		row_tambah +=12;
	                	}else if(lsbs.equals("209")){
	                		row_bp += -20;
	                	}
	                	if(lsbs.equals("073") && lsdbs.equals("015")){
		                	
		                	//Bagian Isi
		                	Map m = uwDao.selectPolisPAS(spaj);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	120, 743+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_PP"), 				120, 730+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_PP"))+" / "+ (BigDecimal) m.get("USIA_PP") + " Tahun",	120, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("MSPR_BEG_DATE")),	120, 701+row_bp, 0);
		                	
		                	int monyong2 = 0;
		                	String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT"), 32);
		                	for(int i=0; i<alamat.length; i++) {
		                		monyong2 = 12 * i;
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			120, 682-monyong2+row_bp, 0);
		                	}
		
		                	//Bagian Kanan
		                	JasperScriptlet jasper = new JasperScriptlet();
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_TT"), 405, 743+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_TT"))+" / "+ (BigDecimal) m.get("USIA_TT") + " Tahun", 405, 730+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "PA STAND ALONE RISIKO “A”", 405, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Rp. " + jasper.format0Digit((BigDecimal) m.get("MSPR_PREMIUM")) + " " + m.get("LSCB_PRINT").toString().toLowerCase(), 405, 701+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Rp. " + jasper.format0Digit((BigDecimal) m.get("MSPR_TSI")), 405, 682+row_bp, 0);
		                	
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(commonDao.selectSysdate()), 435, 660, 0);
		        			
		                	cb.endText();
	                	}
	                	else if(lsbs.equals("203") && lsdbs.equals("004")){
		                	
		                	//Bagian Isi
		                	Map m = uwDao.selectPolisPAS(spaj);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	120, 749+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_PP"), 				120, 735+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_PP"))+" / "+ (BigDecimal) m.get("USIA_PP") + " Tahun",	120, 722+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("MSPR_BEG_DATE")),	120, 706+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang",	120, 688+row_bp, 0);
		                	
		                	int monyong2 = 0;
		                	String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT"), 32);
		                	for(int i=0; i<alamat.length; i++) {
		                		monyong2 = 12 * i;
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			120, 674-monyong2+row_bp, 0);
		                	}
		
		                	//Bagian Kanan
		                	JasperScriptlet jasper = new JasperScriptlet();
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_TT"), 405, 749+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_TT"))+" / "+ (BigDecimal) m.get("USIA_TT") + " Tahun", 405, 735+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "ASURANSI DEMAM BERDARAH", 405, 722+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Rp. " + jasper.format0Digit((BigDecimal) m.get("MSPR_PREMIUM")) + " " + m.get("LSCB_PRINT").toString().toLowerCase(), 405, 706+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Rp. " + jasper.format0Digit((BigDecimal) m.get("MSPR_TSI")), 405, 688+row_bp, 0);
		                	
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(commonDao.selectSysdate()), 435, 660, 0);
		        			
		                	cb.endText();
	                	}
	                	else if(lsbs.equals("203")){
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Polis", 							20, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Pemegang Polis", 					20, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					20, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 							20, 681+row_bp, 0);
		
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",				 					95, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 681+row_bp, 0);
		
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Tertanggung", 						308, 721, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					308, 709, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 				308, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Premi", 								308, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 						308, 669+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Mulai Asuransi", 					308, 657+row_bp, 0);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",		 							440, 721, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 709, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 669+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 657+row_bp, 0);
		                	
		                	//Bagian Isi
		                	Map m = uwDao.selectPolisPAS(spaj);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	100, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_PP"), 				100, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_PP"))+"/"+ (BigDecimal) m.get("USIA_PP") + " Tahun",	100, 693+row_bp, 0);
		                	
		                	int monyong2 = 0;
		                	String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT"), 32);
		                	for(int i=0; i<alamat.length; i++) {
		                		monyong2 = 12 * i;
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			20, 671-monyong2+row_bp, 0);
		                	}
		
		                	//Bagian Kanan
		                	JasperScriptlet jasper = new JasperScriptlet();
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_TT"), 445, 721, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_TT"))+"/"+ (BigDecimal) m.get("USIA_TT") + " Tahun", 445, 709, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("LKU_SYMBOL"), 445, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("LKU_SYMBOL"), 445, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, jasper.format0Digit((BigDecimal) m.get("MSPR_TSI")), 495, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, jasper.format0Digit((BigDecimal) m.get("MSPR_PREMIUM")), 495, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT,  m.get("LSCB_PRINT").toString().toLowerCase(), 500, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang",	445, 669+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("MSPR_BEG_DATE")),	445, 657+row_bp, 0);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jakarta, " + FormatDate.toIndonesian(commonDao.selectSysdate()), 450, 599, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "PT Asuransi Jiwa Sinarmas MSIG Tbk.", 450, 587, 0);
		        			
		        			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Andrew Bain", 450, 527, 0);
		        			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Chief Operating & IT Officer", 450, 518, 0);
		                	cb.endText();
	                	}else if(lsbs.equals("209")){
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Polis", 							52, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Pemegang Polis", 				52, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					52, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 							52, 681+row_bp, 0);
		
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									127, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",				 					127, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									127, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									127, 681+row_bp, 0);
		
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Peserta", 						308, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					308, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 				308, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Kontribusi/Premi", 					308, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 						308, 669+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Mulai Asuransi", 				308, 657+row_bp, 0);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",		 							430, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									430, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									430, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									430, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									430, 669+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									430, 657+row_bp, 0);
		                	
		                	//Bagian Isi
		                	Map m = uwDao.selectPolisPAS(spaj);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	131, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_PP"), 				131, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_PP"))+"/"+ (BigDecimal) m.get("USIA_PP") + " Tahun",	131, 693+row_bp, 0);
		                	
		                	int monyong2 = 0;
		                	String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT"), 32);
		                	for(int i=0; i<alamat.length; i++) {
		                		monyong2 = 12 * i;
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			52, 671-monyong2+row_bp, 0);
		                	}
		
		                	//Bagian Kanan
		                	JasperScriptlet jasper = new JasperScriptlet();
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_TT"), 435, 717+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_TT"))+"/"+ (BigDecimal) m.get("USIA_TT") + " Tahun", 435, 705+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("LKU_SYMBOL"), 435, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("LKU_SYMBOL"), 435, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, jasper.format0Digit((BigDecimal) m.get("MSPR_TSI")), 485, 693+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, jasper.format0Digit((BigDecimal) m.get("MSPR_PREMIUM")), 485, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT,  m.get("LSCB_PRINT").toString().toLowerCase(), 490, 681+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang",	435, 669+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("MSPR_BEG_DATE")),	435, 657+row_bp, 0);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jakarta, " + FormatDate.toIndonesian(commonDao.selectSysdate()), 440, 599+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "PT Asuransi Jiwa Sinarmas MSIG Tbk.", 440, 587+row_bp, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Unit Syariah", 440, 575+row_bp, 0);
		        			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Andrew Bain", 440, 515+row_bp, 0);
		        			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Chief Operating & IT Officer", 440, 506+row_bp, 0);
		                	cb.endText();
	                	}else if(lsbs.equals("187")){
	                		if("005,006".indexOf(lsdbs)<=-1){
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Kartu", 							20, 729+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 729+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Paket", 						308, 696+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 696+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Terbit Polis", 					308, 669+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 669+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Premi", 								308, 657+row_bp+row_tambah-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 657+row_bp+row_tambah-row_header, 0);
		                	}else{
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cara Bayar", 						308, 669+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 669+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Premi", 								308, 669+row_bp+row_tambah-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 669+row_bp+row_tambah-row_header, 0);
		                	}
		                	
	                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Polis", 							20, 717+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pemegang Polis", 					20, 705+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					20, 693+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 						20, 681+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Mulai Asuransi", 					20, 669+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 							20, 657+row_bp-row_header, 0);
		
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 717+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",				 					95, 705+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 693+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 681+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 669+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 657+row_bp-row_header, 0);
		
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tertanggung", 						308, 721-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					308, 709-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 				308, 681+row_bp+row_tambah-row_header, 0);
		                	
		                	if(rider.size()>0){
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asuransi Tambahan", 					308, 645+row_bp-row_header, 0);
		//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tingkat Investasi Pada MGI Pertama", 308, 645, 0);
		//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nilai Tunai Akhir MGI Pertama", 		308, 633, 0);
		                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 645+row_bp-row_header, 0);
		//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 645, 0);
		//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 633, 0);
		                	}
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",		 							440, 721-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 709-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									440, 681+row_bp+row_tambah-row_header, 0);
		                	
		                	//Bagian Kiri
		                	Map m = uwDao.selectPolisPAS(spaj);
		                	
		                	JasperScriptlet jasper = new JasperScriptlet();
		                	if("005,006".indexOf(lsdbs)<=-1){
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, StringUtil.nomorPAS((String) m.get("NO_KARTU")), 	100, 729+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("PAKET"), 445, 696+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(commonDao.selectSysdate()), 445, 669+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_PREMIUM"))+ m.get("LSCB_PRINT").toString().toLowerCase(), 445, 657+row_bp+row_tambah-row_header, 0);
		                	}else{
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("LSCB_PAY_MODE"), 445, 669+row_bp-row_header, 0);
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_PREMIUM"))+ m.get("LSCB_PRINT").toString().toLowerCase(), 445, 669+row_bp+row_tambah-row_header, 0);
		                	}
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	100, 717+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_PP"), 				100, 705+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_PP"))+"/"+ (BigDecimal) m.get("USIA_PP") + " Tahun",	100, 693+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang",	100, 681+row_bp-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("MSPR_BEG_DATE")),	100, 669+row_bp-row_header, 0);
		                	
		                	//Bagian Alamat Rumah
		                	int monyong = 0;
		//                	String[] alamat = StringUtil.pecahParagraf((String) m.get("ALAMAT_RUMAH"), 32);
		//                	for(int i=0; i<alamat.length; i++) {
		//                		monyong = 12 * i;
		//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							100, 681-monyong, 0);
		//                	}
		                	int monyong2 = 0;
		                	String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT"), 32);
		                	for(int i=0; i<alamat.length; i++) {
		                		monyong2 = 12 * i;
		                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			20, 647-monyong2+row_bp-row_header, 0);
		                	}
		//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			20, 645, 0);
		
		                	//Bagian Kanan
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_TT"), 445, 721-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_TT"))+"/"+ (BigDecimal) m.get("USIA_TT") + " Tahun", 445, 709-row_header, 0);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_TSI")), 445, 681+row_bp+row_tambah-row_header, 0);
		                	
		                	if(rider.size()>0){
			                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "ASURANSI DEMAM BERDARAH", 445, 645+row_bp-row_header, 0);
			                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "- Premi : Gratis", 450, 633+row_bp-row_header, 0);
		                	}
		//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("MPS_RATE") + "% Per Tahun", 463, 645, 0);
		//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("NILAI_TUNAI")), 463, 633, 0);
		                	
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jakarta, " + FormatDate.toIndonesian(commonDao.selectSysdate()), 450, 599-row_header, 0);
		                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "PT Asuransi Jiwa Sinarmas MSIG Tbk.", 450, 587-row_header, 0);
		        			
		        			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Andrew Bain", 450, 527-row_header, 0);
		        			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Chief Operating & IT Officer", 450, 518-row_header, 0);
		                	cb.endText();
	                	}
	                	
	                	//canpri, tes path karena sebelumnya ke tomcat/bin
	                	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	                	String path = classLoader.getResource(hamid).getPath();
	                	Image img = Image.getInstance(path);
	                	//end
	                	
	                	//Image img = Image.getInstance(hamid);
	    				
	    				img.setAbsolutePosition(450, 537-row_header);		
	    				img.scaleAbsolute(120, 34);
	    				if(lsbs.equals("209")){
	    					cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 440, 528-row_header+row_bp);
	    				}else if(lsbs.equals("073") && lsdbs.equals("015") ){
	    					cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 400, 648-row_header+row_bp);
	    				}else if( lsbs.equals("203") && lsdbs.equals("004") ){
	    					cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 400, 608-row_header+row_bp);
	    				}else{
	    					cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 450, 535-row_header);
	    				}
	    				
	    				if(lsbs.equals("209")){
	    					String logo_path = props.getProperty("pdf.dir.syaratpolis")+"\\images\\ajsmsig_syariah.jpg";
	    					Image logo = Image.getInstance(logo_path);
	    					int w = new Double(logo.getScaledWidth()*0.5).intValue();
	    					int h = new Double(logo.getScaledHeight()*0.5).intValue();
		    				cb.addImage(logo, w, 0, 0, h, 54, 757);
	    				}
	    				cb.stroke();
	//                }
	//	        }
	            if(stamp!=null){
	            	stamp.close();
	            }
	    		if(reader!=null){
	    			reader.close();
	    		}
	            if(output!=null){
	            	output.flush();
	            	output.close();
	            }
	            return true;
			}else {
				return false;
			}
			
			//3 buah tujuan, TAMPILKAN_PDF, CETAK_KE_LAYAR atau SIMPAN_KE_PDF, tapi yg tampilkan PDF ditaro diatas
			params.put("props", props);
	
	//		try {
	//			JasperUtils.exportReportToPdf(
	//					reportPath+".jasper", exportDirectory, jenis+".pdf", 
	//					params, getConnection(), 
	//					null, props.getProperty("pdf.ownerPassword"), props.getProperty("pdf.userPassword"));
	//		} catch (Exception e) {
	//			logger.error("ERROR :", e);
	//			return false;
	//		}
	
			
		}catch(Exception e){
			logger.error("ERROR :", e);
			return false;
		}
		return true;
	}
	
	
	public boolean softCopyOtomatis(String reg_spaj, User currentUser, String email_client ) {
		try{
			CommandUploadBac uploader = new CommandUploadBac();
			uploader.setErrorMessages(new ArrayList<String>());
			
//			STRING VALIDASIMETERAI = UWDAO.VALIDASIBEAMETERAI();
//			If(validasiMeterai != null) throw new Exception(validasiMeterai);
			uploader.setSpaj(reg_spaj);
			
			List detBisnis = uwDao.selectDetailBisnis(reg_spaj);
			String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			String Produk = "PERSONAL ACCIDENT SINARMASLIFE";
			String jenisPolis = props.getProperty("pdf.polis_pas");
			String jenisNasabah = "Personal Accident SinarmasMSIGLife (PAS)";
			
			boolean polis=false, surat=false, tandaterima=false, generateVirtual=false;
			
			String cabang = uwDao.selectCabangFromSpaj(reg_spaj);
	        String userDir = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\";
	        
	        File file = new File(userDir + "\\" + props.getProperty("pdf.polis_pas")+".pdf");
	        File file2 = new File(userDir + "\\" + "softcopy"+".pdf");
			if (!file.exists() && !file2.exists()){
			
				if(elesbees.equals("203") || elesbees.equals("209")){
					jenisPolis  = props.getProperty("pdf.polis_dbd");
					Produk		= "DEMAM BERDARAH";
					jenisNasabah= "Individu";
				}
				polis 		= dokumenOtomatis(reg_spaj, currentUser, jenisPolis);
	//			tandaterima = dokumenOtomatis(reg_spaj, currentUser, props.getProperty("pdf.tanda_terima_polis"));
	//			surat 		= dokumenOtomatis(reg_spaj, currentUser, props.getProperty("pdf.surat_polis"));
				generateVirtual = dokumenOtomatis(reg_spaj, currentUser, props.getProperty("pdf.panduan_account"));
				
				if(!polis) 			throw new Exception("Dokumen Polis "+Produk+" tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
	//			if(!tandaterima)	throw new Exception("Dokumen Tanda Terima Polis tidak berhasil dihasilkan. Silahkan hubungi EDP.");
	//			if(!surat) 			throw new Exception("Dokumen Surat Ucapan Terima kasih tidak berhasil dihasilkan. Silahkan hubungi EDP.");
				if(!generateVirtual) 			throw new Exception("Dokumen Panduan tidak berhasil dihasilkan. Silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
				
				
		
				List<DropDown> fileList = new ArrayList<DropDown>();
				//fileList.add(new DropDown(userDir+props.getProperty("pdf.surat_polis")+".pdf", "Surat Ucapan Terima Kasih", "NONE"));
				fileList.add(new DropDown(userDir+jenisPolis+".pdf", "Polis "+Produk, "NONE"));
	//			PdfUtils.addTableOfContents(fileList, props, userDir);
				Map info = uwDao.selectInformasiEmailSoftcopyPAS(uploader.getSpaj());
				
				String mste_no_vacc = financeDao.selectVirtualAccountSpaj(uploader.getSpaj());
				BigDecimal mste_flag_cc = (BigDecimal) info.get("MSTE_FLAG_CC");
				BigDecimal lscb_id = (BigDecimal) info.get("LSCB_ID");
				String lscb_pay_mode= (String) info.get("LSCB_PAY_MODE");
				
	//			if(mste_no_vacc!=null && mste_flag_cc.intValue()==0 && lscb_id.intValue()!=0){
	//				List<DropDown> fileList2 = new ArrayList<DropDown>();
	//				fileList2.add(new DropDown(userDir+props.getProperty("pdf.panduan_account")+".pdf", "Panduan Pembayaran Premi", "NONE"));
	//				PdfUtils.addTableOfContents(fileList2, props, userDir);
	//			}
				
				
				//String userPassword = props.getProperty("pdf.userPassword");
				String userPassword = defaultDateFormatStripes.format(uwDao.selectTanggalLahirPemegang(reg_spaj));
				if(email_client.toUpperCase().equals("HIKMAHASURANSI@YAHOO.CO.ID")){//permintaan Ariani via Helpdesk no 34051
					userPassword=null;
				}
				File softcopy = PdfUtils.combinePdfWithOutline(fileList, userDir, "softcopy.pdf", props, userPassword);
				//set report syariah igaukiarwan 20190814
				if (products.syariah(elesbees, lsdbs)){
					softcopy = PdfUtils.addBarcodeAndLogoSyariah(fileList, softcopy, props, userPassword);
				}else{
					softcopy = PdfUtils.addBarcodeAndLogo(fileList, softcopy, props, userPassword);	
				}
				//File dokumenlain = PdfUtils.combinePdfWithOutline(fileList2, userDir, "dokumen.pdf", props, userPassword);
				File panduanVirtualAccount = new File(props.getProperty("pdf.template")+"\\panduan.pdf");
				
				List<File> attachments = new ArrayList<File>();
				attachments.add(softcopy);
				if(mste_no_vacc!=null && mste_flag_cc.intValue()==0 && lscb_id.intValue()!=0){
					attachments.add(panduanVirtualAccount);
				}
				
				
				
				uploader.setEmailto(email_client);
//				email_client="deddy@sinarmasmsiglife.co.id";
//				uploader.setEmailto("deddy@sinarmasmsiglife.co.id");//testing
				uploader.setEmailsubject("Softcopy Polis Asuransi atas nama " + info.get("GELAR") + " " + info.get("MCL_FIRST"));
				StringBuffer pesan = new StringBuffer();
				String kalimatPertama = "";
				
				BigDecimal premi = (BigDecimal) info.get("MSPR_PREMIUM");
				//BigDecimal premi= (BigDecimal) info.get("MSPR_PREMIUM");
				
				if(uwDao.selectJenisTerbitPolis(uploader.getSpaj())==1) { //softcopy
					
					String link = 
						//"http://www.sinarmaslife.co.id/E-Policy/common/confirm.htm?spaj="+uploader.getSpaj()+"&auth="+ EncryptUtils.encode(("yusufsutarko"+uploader.getSpaj()).getBytes());
						"http://epolicy.sinarmasmsiglife.co.id";
					
					
					kalimatPertama = "Bersama ini kami kirimkan softcopy Polis "+Produk+( ((elesbees.equals("187") && "005,006".indexOf(lsdbs)>-1 ) || elesbees.equals("203") ) ?".":" untuk Nomor Kartu."+ StringUtil.nomorPAS((String) info.get("NO_KARTU")))+"\n\n";
					pesan.append("Kepada Yth.\n");
					pesan.append(info.get("GELAR") + " " + info.get("MCL_FIRST")+"\n");
					pesan.append("di tempat\n\n");
					pesan.append("Dengan Hormat,\n");
					pesan.append(kalimatPertama);
					pesan.append("Untuk menjaga keamanan polis "+Produk+" Bapak/Ibu, Polis Asuransi "+Produk+" Bapak/Ibu sudah kami lindungi dengan password berupa tanggal lahir Bapak/Ibu dalam format dd-mm-yyyy (contoh: <strong> Apabila Tanggal lahir Bapak/Ibu 14 April 1985 maka password Bapak/Ibu 14-04-1985</strong>).\n\n");
					pesan.append("Bapak/Ibu juga dapat mengunduh softcopy Polis "+Produk+" melalui website kami di :"+
							"<a href='"+link+"'>"+link+"</a>" + " dengan cara:\n\n"+
							"	<ol>" +
							" 	<li>Pilihan jenis Nasabah : "+jenisNasabah+",</li>" +
							"   <li>User ID : No Identitas Bapak/Ibu sesuai dengan yang Bapak/Ibu cantumkan saat pendaftaran</li>" +
							"   <li>Password: tanggal lahir Bapak/Ibu dalam format dd-mm-yyyy (Apabila tanggal lahir Bapak/Ibu 14 April 1985 maka password Bapak/Ibu adalah 14-04-1985).</li>" +
							"	</ol>");
					pesan.append("Mohon softcopy Polis "+Produk+" ini disimpan dan dicetak apabila diperlukan.\n\n");
					pesan.append("Sehubungan dengan hal tersebut, kami ucapkan terima kasih  atas pembayaran premi pertama sejumlah " + FormatString.formatCurrency((String) info.get("LKU_SYMBOL"), premi) + " (" +AngkaTerbilang.indonesian( premi.toString(), (String) info.get("LKU_ID") ) + ") \n\n" );
					
					if(mste_no_vacc!=null && mste_flag_cc.intValue()==0 && lscb_id.intValue()!=0){
						pesan.append("Untuk memastikan Polis Bapak/Ibu tetap berlaku, maka sebelum jatuh tempo premi lanjutan setiap "+lscb_pay_mode.toString().toLowerCase()+", mohon premi dibayarkan ke Nomor Rekening Pembayaran Premi Lanjutan di Bank Sinarmas ( Billing Account) : <strong>" + mste_no_vacc + "</strong>.\n 2 (dua) hari setelah pembayaran dilakukan, Bapak/Ibu dapat melihatnya di website kami.\n");
						pesan.append("( Terlampir panduan Cara pembayaran premi melalui Billing Account )\n");
						pesan.append("<strong>Nomor Rekening ini hanya dapat digunakan untuk pembayaran premi polis ini.</strong> \n\n");
					}
					pesan.append("Email ini terkirim secara otomatis oleh sistem, harap jangan mereply email ini. Informasi lebih lanjut dapat disampaikan ke :\n <strong>cs@sinarmasmsiglife.co.id</strong>\n\n");
					pesan.append(props.getProperty("email.uw.footer"));
				}
				
				uploader.setEmailmessage(pesan.toString().replaceAll("\n", "</br>"));
		
				//email agen(close apabila testing)
				Map infoAgen = uwDao.selectEmailAgen(uploader.getSpaj());
				String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
				if(emailAgen!=null) {
					if(!emailAgen.trim().equals("")) {
						if(emailAgen.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
							uploader.setEmailcc(emailAgen);
						}
					}
				}
				
				//kalo agen gak punya email, dikirim ke branch admin nya
				if(uploader.getEmailcc()==null) {
					String emailCabang = uwDao.selectEmailCabangFromSpaj(uploader.getSpaj());
					if(emailCabang!=null) {
						if(!emailCabang.trim().equals("")) {
							if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
								if(uploader.getSpaj().indexOf("58") == 0){//MALLINSURANCE
									emailCabang=emailCabang+";deddy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id;"; 											
								}else{
									emailCabang=emailCabang+";deddy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id";
								}
								uploader.setEmailcc(emailCabang);
							}
						}
					}
				}
				List<DropDown> ListImage = new ArrayList<DropDown>();
				ListImage.add(new DropDown("myLogo", props.getProperty("images.ttd.ekalife.small")));
				
				if(uploader.getEmailcc() == null){
					if(uploader.getSpaj().indexOf("58") == 0){//MALLINSURANCE
						uploader.setEmailcc ("deddy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id;");
					}else{
						uploader.setEmailcc ("deddy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id");
					}
				}
				if(!Common.isEmpty(email_client)) {
					if(!email.equals("")) {
//						email.sendImageEmbeded(
//								true,"policy_service@sinarmasmsiglife.co.id", 
////								uploader.getEmailto().replaceAll(" ", "").split(";") ,
//								new String[] {"deddy@sinarmasmsiglife.co.id"},
//								null,
//								null,
////								new String[] {"ingrid@sinarmasmsiglife.co.id","Rachel@sinarmasmsiglife.co.id","Hayatin@sinarmasmsiglife.co.id","Fouresta@sinarmasmsiglife.co.id","Dinni@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"},
////								(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")),  
////								new String[] {"deddy@sinarmasmsiglife.co.id"},
//								uploader.getEmailsubject(), uploader.getEmailmessage(),attachments, ListImage);
					
					String me_id = sequence.sequenceMeIdEmail();
					Date nowDate = commonDao.selectSysdate();
					Integer months = nowDate.getMonth()+1;
					Integer years = nowDate.getYear()+1900;
					String destTo=props.getProperty("embedded.mailpool.dir")+"\\" +years+"\\"+FormatString.rpad("0", months.toString(), 2)+"\\"+me_id+"\\";
					File dirFile = new File(destTo);
					if(!dirFile.exists()) dirFile.mkdirs();
					
					File embedfrom = new File(Resources.getResourceAsFile(props.getProperty("images.ttd.ekalife.small").trim()).getAbsolutePath());
					File embedto = new File(destTo+"myLogo.gif");
					FileCopyUtils.copy(embedfrom, embedto);
					
					EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, nowDate, null, 
							true, "policy_service@sinarmasmsiglife.co.id", 
							uploader.getEmailto().replaceAll(" ", "").split(";") ,
//							new String[] {"deddy.sucipto@yahoo.com"},
							null,
//							new String[] {"ingrid@sinarmasmsiglife.co.id","Rachel@sinarmasmsiglife.co.id","Hayatin@sinarmasmsiglife.co.id","Fouresta@sinarmasmsiglife.co.id","Dinni@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"},
							(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")),  
//							new String[] {"deddy@sinarmasmsiglife.co.id"},
							uploader.getEmailsubject(), 
							uploader.getEmailmessage(), 
							attachments,14);
						Boolean ok = false;
						do{
							try{
								uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Kirim Softcopy Otomatis Ke : " + email_client, reg_spaj, 0);
								ok=true;
							}catch(Exception e){};
						}while (!ok);
						
					}
				}
				uwDao.updateTanggalKirimPolis(reg_spaj);
				uwDao.saveMstTransHistory(reg_spaj, "tgl_kirim_polis", null, null, null);
				uwDao.updateMst_policyPrintDate(reg_spaj, "mspo_date_print");
				uwDao.saveMstTransHistory(reg_spaj, "tgl_print_polis", null, null, null);
				
	//			email.send(true,
	//					"policy_service@sinarmasmsiglife.co.id", 
	//					uploader.getEmailto().replaceAll(" ", "").split(";"), 
	//					//new String[] {"ingrid@sinarmasmsiglife.co.id", "rachel@sinarmasmsiglife.co.id"},
	//					(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")), 
	//					new String[] {"deddy@sinarmasmsiglife.co.id"},
	//					uploader.getEmailsubject(), uploader.getEmailmessage(), attachments);
			
			}
		}catch(Exception e){
			logger.error("ERROR :", e);
			return false;}
		return true;
		
	}
	
	public boolean softCopyOtomatisPaBsm(String reg_spaj, User currentUser, String email_client, Cplan cplan ) {
		try{
			CommandUploadBac uploader = new CommandUploadBac();
			uploader.setErrorMessages(new ArrayList<String>());
			
//			STRING VALIDASIMETERAI = UWDAO.VALIDASIBEAMETERAI();
//			If(validasiMeterai != null) throw new Exception(validasiMeterai);
			//uploader.setSpaj(reg_spaj);
			
			//List detBisnis = uwDao.selectDetailBisnis(reg_spaj);
			//String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			//String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			
			//boolean polis=false, surat=false, tandaterima=false, generateVirtual=false;
			
			//String cabang = uwDao.selectCabangFromSpaj(reg_spaj);
	        String userDir = props.getProperty("pdf.dir.export") + "\\cplan\\25\\"+cplan.getNo_sertifikat()+"\\";
	        
	        //File file = new File(userDir + "\\" + props.getProperty("pdf.polis_pas")+".pdf");
	        //File file2 = new File(userDir + "\\" + "softcopy"+".pdf");
			//if (!file.exists() && !file2.exists()){
				
			//polis 		= dokumenOtomatis(reg_spaj, currentUser, props.getProperty("pdf.polis_pas"));
//			tandaterima = dokumenOtomatis(reg_spaj, currentUser, props.getProperty("pdf.tanda_terima_polis"));
//			surat 		= dokumenOtomatis(reg_spaj, currentUser, props.getProperty("pdf.surat_polis"));
			//generateVirtual = dokumenOtomatis(reg_spaj, currentUser, props.getProperty("pdf.panduan_account"));
			
			//if(!polis) 			throw new Exception("Dokumen Polis PA tidak berhasil dihasilkan. Silahkan hubungi EDP.");
//			if(!tandaterima)	throw new Exception("Dokumen Tanda Terima Polis tidak berhasil dihasilkan. Silahkan hubungi EDP.");
//			if(!surat) 			throw new Exception("Dokumen Surat Ucapan Terima kasih tidak berhasil dihasilkan. Silahkan hubungi EDP.");
			//if(!generateVirtual) 			throw new Exception("Dokumen Panduan tidak berhasil dihasilkan. Silahkan hubungi EDP.");
			
			
	
			List<DropDown> fileList = new ArrayList<DropDown>();
			//fileList.add(new DropDown(userDir+props.getProperty("pdf.surat_polis")+".pdf", "Surat Ucapan Terima Kasih", "NONE"));
			fileList.add(new DropDown(userDir+cplan.getNo_sertifikat() + ".pdf", "Polis PAS BSM", "NONE"));
//			PdfUtils.addTableOfContents(fileList, props, userDir);
			//Map info = uwDao.selectInformasiEmailSoftcopyPAS(uploader.getSpaj());
			
			//String mste_no_vacc = financeDao.selectVirtualAccountSpaj(uploader.getSpaj());
			//BigDecimal mste_flag_cc = (BigDecimal) info.get("MSTE_FLAG_CC");
			//BigDecimal lscb_id = (BigDecimal) info.get("LSCB_ID");
			//String lscb_pay_mode= (String) info.get("LSCB_PAY_MODE");
			
//			if(mste_no_vacc!=null && mste_flag_cc.intValue()==0 && lscb_id.intValue()!=0){
//				List<DropDown> fileList2 = new ArrayList<DropDown>();
//				fileList2.add(new DropDown(userDir+props.getProperty("pdf.panduan_account")+".pdf", "Panduan Pembayaran Premi", "NONE"));
//				PdfUtils.addTableOfContents(fileList2, props, userDir);
//			}
			
			
			//String userPassword = props.getProperty("pdf.userPassword");
			String userPassword = defaultDateFormatStripes.format(cplan.getMcp_tgl_lahir());
			File softcopy = PdfUtils.combinePdfWithOutline(fileList, userDir, "softcopy.pdf", props, userPassword);
			//softcopy = PdfUtils.addBarcodeAndLogo(fileList, softcopy, props, userPassword);
			//File dokumenlain = PdfUtils.combinePdfWithOutline(fileList2, userDir, "dokumen.pdf", props, userPassword);
			//File softcopy = new File(props.getProperty("pdf.dir.export") + "\\cplan\\25\\" + cplan.getNo_sertifikat() + ".pdf");
			//File panduanVirtualAccount = new File(props.getProperty("pdf.template")+"\\panduan.pdf");
			
			List<File> attachments = new ArrayList<File>();
			attachments.add(softcopy);
			//if(mste_no_vacc!=null && mste_flag_cc.intValue()==0 && lscb_id.intValue()!=0){
				//attachments.add(panduanVirtualAccount);
			//}
			
			
			
			uploader.setEmailto(email_client);
			//uploader.setEmailto("andy@sinarmasmsiglife.co.id");//testing
			uploader.setEmailsubject("Softcopy Polis Asuransi atas nama " + cplan.getFull_name());
			StringBuffer pesan = new StringBuffer();
			String kalimatPertama = "";
			
			//String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			//BigDecimal premi = (BigDecimal) info.get("MSPR_PREMIUM");
			//BigDecimal premi= (BigDecimal) info.get("MSPR_PREMIUM");
			
			//if(uwDao.selectJenisTerbitPolis(uploader.getSpaj())==1) { //softcopy
				
				String link = 
					//"http://www.sinarmaslife.co.id/E-Policy/common/confirm.htm?spaj="+uploader.getSpaj()+"&auth="+ EncryptUtils.encode(("yusufsutarko"+uploader.getSpaj()).getBytes());
					"http://epolicy.sinarmasmsiglife.co.id/";
				
				
				kalimatPertama = "Bersama ini kami kirimkan softcopy Polis Personal Accident untuk Nomor Polis."+ cplan.getNo_sertifikat()+"\n\n";
	
				pesan.append("Kepada Yth.\n");
				pesan.append("Bapak/Ibu " + cplan.getFull_name()+"\n");
				pesan.append("di tempat\n\n");
				pesan.append("Dengan Hormat,\n");
				pesan.append(kalimatPertama);
				pesan.append("Bapak/Ibu dapat mengunduh softcopy Polis Personal Accident melalui website kami di :"+
						"<a href='"+link+"'>"+link+"</a>" + " dengan cara:\n\n"+
						"	<ol>" +
						" 	<li>Pilihan jenis Nasabah : BSIM,</li>" +
						"   <li>User ID : Nama Lengkap Bapak/Ibu sesuai dengan yang Bapak/Ibu cantumkan saat pendaftaran</li>" +
						"   <li>Password: tanggal lahir Bapak/Ibu dalam format dd-mm-yyyy (Apabila tanggal lahir Bapak/Ibu 14 April 1985 maka password Bapak/Ibu adalah 14-04-1985).</li>" +
						"	</ol>");
				pesan.append("Mohon softcopy Polis Personal Accident ini disimpan dan dicetak apabila diperlukan.\n\n");
				
				//if(mste_no_vacc!=null && mste_flag_cc.intValue()==0 && lscb_id.intValue()!=0){
				//	pesan.append("Untuk memastikan Polis Bapak/Ibu tetap berlaku, maka sebelum jatuh tempo premi lanjutan setiap "+lscb_pay_mode.toString().toLowerCase()+", mohon premi dibayarkan ke Nomor Rekening Pembayaran Premi Lanjutan di Bank Sinarmas ( Billing Account) : <strong>" + mste_no_vacc + "</strong>.\n 2 (dua) hari setelah pembayaran dilakukan, Bapak/Ibu dapat melihatnya di website kami.\n");
				//	pesan.append("( Terlampir panduan Cara pembayaran premi melalui Billing Account )\n");
				//	pesan.append("<strong>Nomor Rekening ini hanya dapat digunakan untuk pembayaran premi polis ini.</strong> \n\n");
				//}
				pesan.append("Email ini terkirim secara otomatis oleh sistem, harap jangan mereply email ini. Informasi lebih lanjut dapat disampaikan ke :\n <strong>cs@sinarmasmsiglife.co.id</strong>\n\n");
				pesan.append(props.getProperty("email.uw.footer"));
			//}
			
			uploader.setEmailmessage(pesan.toString().replaceAll("\n", "</br>"));
	
			//email agen(close apabila testing)
	//		Map infoAgen = elionsManager.selectEmailAgen(uploader.getSpaj());
	//		String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
	//		if(emailAgen!=null) {
	//			if(!emailAgen.trim().equals("")) {
	//				if(emailAgen.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
	//					uploader.setEmailcc(emailAgen);
	//				}
	//			}
	//		}
			
			//kalo agen gak punya email, dikirim ke branch admin nya
//			if(uploader.getEmailcc()==null) {
//				String emailCabang = uwDao.selectEmailCabangFromSpaj(uploader.getSpaj());
//				if(emailCabang!=null) {
//					if(!emailCabang.trim().equals("")) {
//						if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
//							uploader.setEmailcc(emailCabang);
//						}
//					}
//				}
//			}
			List<DropDown> ListImage = new ArrayList<DropDown>();
			ListImage.add(new DropDown("myLogo", props.getProperty("pdf.dir.syaratpolis")+"\\images\\ekalife_s.gif"));
			
			if(uploader.getEmailcc() == null) uploader.setEmailcc ("andy@sinarmasmsiglife.co.id");
			if(!Common.isEmpty(email_client)) {
				
				/*if(!email.equals("")) {
					
					email.sendImageEmbeded(
							true,"info@sinarmasmsiglife.co.id", uploader.getEmailto().replaceAll(" ", "").split(";") ,
							null,
//							new String[] {"ingrid@sinarmasmsiglife.co.id","Rachel@sinarmasmsiglife.co.id","Hayatin@sinarmasmsiglife.co.id","Fouresta@sinarmasmsiglife.co.id","Dinni@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"},
							//(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")),  
							new String[] {"andy@sinarmasmsiglife.co.id"},
							uploader.getEmailsubject(), uploader.getEmailmessage(),attachments, ListImage);
					*/
					
				Date nowDate = commonDao.selectSysdate();
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, nowDate, null, 
						true, "info@sinarmasmsiglife.co.id", 
						uploader.getEmailto().replaceAll(" ", "").split(";"), 
						null,
//						new String[] {"ingrid@sinarmasmsiglife.co.id","Rachel@sinarmasmsiglife.co.id","Hayatin@sinarmasmsiglife.co.id","Fouresta@sinarmasmsiglife.co.id","Dinni@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"},
						//(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")),  
						new String[] {"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id","inge@sinarmasmsiglife.co.id"},
						uploader.getEmailsubject(), 
						uploader.getEmailmessage(), 
						attachments, reg_spaj);
						
					Boolean ok = false;
										
				}
			//}
			uwDao.updateTanggalKirimPolis(reg_spaj);
			uwDao.saveMstTransHistory(reg_spaj, "tgl_kirim_polis", null, null, null);
			uwDao.updateMst_policyPrintDate(reg_spaj, "mspo_date_print");
			uwDao.saveMstTransHistory(reg_spaj, "tgl_print_polis", null, null, null);
			
//			email.send(true,
//					"policy_service@sinarmasmsiglife.co.id", 
//					uploader.getEmailto().replaceAll(" ", "").split(";"), 
//					//new String[] {"ingrid@sinarmasmsiglife.co.id", "rachel@sinarmasmsiglife.co.id"},
//					(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")), 
//					new String[] {"deddy@sinarmasmsiglife.co.id"},
//					uploader.getEmailsubject(), uploader.getEmailmessage(), attachments);
			
			
			//}
		}catch(Exception e){
			logger.error("ERROR :", e);
			return false;}
		return true;
		
	}
	
	
	
	//Patar Timotius
	public boolean softCopyOtomatisBSIMSyariah(String reg_spaj,User currentUser,String email,ElionsManager elionsManager,BacManager bacManager){
		boolean ret = false;
		try{
			
		String cabang = uwDao.selectCabangFromSpaj(reg_spaj);
		Pemegang dataPP = bacDao.selectpp(reg_spaj);
		Date sysdate = commonDao.selectSysdate();		
		String fileDir = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\";
		String fileName ="SERTIFIKAT_PA_BSM.pdf";
		String outputName = fileDir+fileName;
	    File l_file = new File(outputName);
	    if(!l_file.exists()){
	    	// generate them please
	    	List detBisnis = uwDao.selectDetailBisnis(reg_spaj);
			String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
	    	String nopolis = "";
	    	String lsbs_number = elesbees;
	    	Integer lsdbs_number = Integer.parseInt(lsdbs);
	    	String up = "";
	    	String premi = "";
	    	int cara_bayar = 0;
	    	String fileGenerate = ITextPdf.generateSertifikatPaBsmSyariah(nopolis,reg_spaj, nopolis, lsbs_number, lsdbs_number,  up, premi, elionsManager,props,bacManager,cara_bayar);
	    }
		CommandUploadBac uploader = new CommandUploadBac();
		uploader.setErrorMessages(new ArrayList<String>());
		List<File> attachments = new ArrayList<File>();
		if(l_file.exists()){
			attachments.add(l_file);
		}

		uploader.setEmailto(email);
		uploader.setSpaj(reg_spaj);
		uploader.setEmailcc(commonDao.selectEmailUser(currentUser.getLus_id()));
		uploader.setEmailcc("PATAR.TAMBUNAN@SINARMASMSIGLIFE.CO.ID");
		uploader.setEmailsubject("Softcopy Surat Penjaminan atas nama A/n "+dataPP.getMcl_first());
		StringBuffer pesan = new StringBuffer();
		pesan.append("Kepada Yth.\n");
		String kalimatPertama = "";		
		kalimatPertama = "Selamat, Anda terlah terdaftar sebagai peserta Personal Accident Risiko ABD dari Sinarmas MSIG Life.\n";
		kalimatPertama+="Terlampir adalah sertifikat polis sebagai penduan Anda dalam memahami ketentuan produk secara ringkas.\n\n";
		
		if(dataPP.getMcl_gelar()==null){
			pesan.append("Bapak/Ibu " +dataPP.getMcl_first()+"\n");
			pesan.append("di tempat\n\n");
			pesan.append("Dengan Hormat,\n");
			pesan.append(kalimatPertama);
			pesan.append("Mohon Surat Penjaminan ini disimpan dan dicetak apabila diperlukan.\n\n");
			pesan.append("Email ini terkirim secara otomatis oleh sistem, harap jangan mereply email ini. Informasi lebih lanjut dapat disampaikan ke :\n <strong>cs@sinarmasmsiglife.co.id</strong>\n\n");
		
		};
		
		uploader.setEmailmessage(pesan.toString().replaceAll("\n", "</br>"));
		
		
		if(!Common.isEmpty(email)) {
			String me_id = sequence.sequenceMeIdEmail();
			Date nowDate = commonDao.selectSysdate();
			EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, "info@sinarmasmsiglife.co.id", 
					uploader.getEmailto().replaceAll(" ", "").split(";") ,
					null,
					(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")),  
					uploader.getEmailsubject(), 
					uploader.getEmailmessage(), 
					attachments,14);
				Boolean ok = true;
				ret = true;
				uwDao.updateTanggalKirimPolis(reg_spaj);
				uwDao.saveMstTransHistory(reg_spaj, "tgl_kirim_polis", null, null, null);
				uwDao.updateMst_policyPrintDate(reg_spaj, "mspo_date_print");
				uwDao.saveMstTransHistory(reg_spaj, "tgl_print_polis", null, null, null);
			}
		
		
		//String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
		}catch(Exception e){
			logger.error("ERROR :", e);
			ret= false;
		}
		
		
		
		
		return ret;
	}
	
	
	
	//Kirim OTOMATIS surat Penjaminan PROVIDER JALUR DIST BJB - Patar Timotius
		public boolean softCopyOtomatisBJB(String reg_spaj, User currentUser, String email_client ) {
			try{
				
				Date sysdate = commonDao.selectSysdate();
				String cabang = uwDao.selectCabangFromSpaj(reg_spaj);
				String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
				
		        
		        Pemegang dataPP = bacDao.selectpp(reg_spaj);
		        Tertanggung dataTT = bacDao.selectttg(reg_spaj);
		        AddressBilling addrBill = bacDao.selectAddressBilling(reg_spaj);
		        Datausulan dataUsulan = bacDao.selectDataUsulanutama(reg_spaj);
		        Integer lsre_id = uwDao.selectPolicyRelation(reg_spaj);
		        Agen agen =this.bacDao.select_detilagen(reg_spaj);
		        dataUsulan.setDaftaRider(bacDao.selectDataUsulan_rider(reg_spaj));
		      //  List peserta=this.bacDao.select_all_mst_peserta(reg_spaj);
		        List peserta= this.bacDao.select_semua_mst_peserta2(reg_spaj);
		        
		        
				Map premiProdukUtama = this.uwDao.selectPremiProdukUtama(reg_spaj);
				String kurs = (String) premiProdukUtama.get("LKU_ID");
				Calendar cal = Calendar.getInstance(); 
				cal.add(Calendar.MONTH, 1);
				//Generate Welcome Letter
				DateFormat yy = new SimpleDateFormat("yyyy");
				DateFormat MM = new SimpleDateFormat("MM");

				// Get the date today using Calendar object.
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDateMM = MM.format(sysdate);
				String reportDateYYYY = yy.format(sysdate);
				File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
				
				String fileDir = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\";
				String fileName ="SURAT_PENJAMINAN_PROVIDER.pdf";
				
		        if(!userDir.exists()) {
		            userDir.mkdirs();
		        }
		        
		        HashMap moreInfo = new HashMap();
				moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
				moreInfo.put("Title", "DMTM - BJB");
				moreInfo.put("Subject", "SURAT KONFIRMASI PENJAMINAN DMTM - BJB");
				
				PdfContentByte over;
				BaseFont calibri = BaseFont.createFont("C:\\WINDOWS\\FONTS\\CALIBRI.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
						
		        PdfReader reader = new PdfReader(props.getProperty("pdf.template.term")+"\\SP_PROV_MERGE.pdf");
		        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"SURAT_PENJAMINAN_PROVIDER.pdf";
		        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
		       
		        String noSPP = sequence.sequenceNoSuratPenjaminanProvider();			
		        
		        over = stamp.getOverContent(1);
				over.beginText();
				over.setFontAndSize(calibri,9);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, noSPP, 138, 687, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, reportDateYYYY, 277, 687, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, AngkaTerbilang.romawiBerjaya(reportDateMM), 251, 687, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(akseptasiDao.selectFAddMonths(FormatDate.toString(sysdate),new Integer(1))), 127, 560, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorPolis(dataPP.getMspo_policy_no()), 203, 513, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 203, 501, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) dataPP.getMspe_date_birth()), 203, 490, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) dataUsulan.getMste_beg_date()), 203, 468, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_name().toUpperCase(), 203, 479, 0);
			 
			    if(peserta.size()>0){
					int vertikal=416;
					for(int z=0;z<peserta.size();z++){
						vertikal = vertikal-16;
					//PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(z);
					//Map m = (HashMap) peserta.get(z);				
					//over.showTextAligned(PdfContentByte.ALIGN_LEFT,pesertaPlus.getNama(),113, vertikal, 0);
					//over.showTextAligned(PdfContentByte.ALIGN_CENTER,FormatDate.toString(pesertaPlus.getTgl_lahir()),447, vertikal, 0);
					Map m = (HashMap) peserta.get(z);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String) m.get("NAMA"),113, vertikal, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,FormatDate.toIndonesian((Date) m.get("TGL_LAHIR")),447, vertikal, 0);
						
					}
				}
			    
//			    String drIndra = props.getProperty("pdf.template.admedika2")+"\\drindra.bmp";
//			    Image img = Image.getInstance(drIndra);					
//				img.setAbsolutePosition(380, 300);		
//				img.scaleAbsolute(120, 34);
//				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 95, 129);
//				over.stroke();
//			    over.endText();
//				stamp.close();	
				
			    String ttd = props.getProperty("pdf.template.admedika2")+"\\suryanto.jpg";
			    Image img = Image.getInstance(ttd);					
				img.setAbsolutePosition(380, 300);		
//				img.scaleAbsolute(90, 34);
				img.scalePercent(20);
				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 95, 130);
				over.stroke();
			    over.endText();
				stamp.close();	
				
	            File l_file = new File(outputName);
	            
				CommandUploadBac uploader = new CommandUploadBac();
				uploader.setErrorMessages(new ArrayList<String>());
				
				uploader.setSpaj(reg_spaj);
				
				List detBisnis = uwDao.selectDetailBisnis(reg_spaj);
				String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
				
				if (l_file.exists()){
					List<DropDown> fileList = new ArrayList<DropDown>();
					List<File> attachments = new ArrayList<File>();
					
					attachments.add(l_file);
					
					uploader.setEmailto(email_client);
//					uploader.setEmailcc(currentUser.getLus_email());
					uploader.setEmailcc(commonDao.selectEmailUser(currentUser.getLus_id()));
					uploader.setEmailsubject("Softcopy Surat Penjaminan atas nama A/n "+dataPP.getMcl_first());
					StringBuffer pesan = new StringBuffer();
					String kalimatPertama = "";
						
						kalimatPertama = "Bersama ini kami kirimkan Surat Penjaminan A/n  "+dataPP.getMcl_first()+"\n\n";
						pesan.append("Kepada Yth.\n");
//						pesan.append((dataPP.getMcl_gelar().equals(null)?"":dataPP.getMcl_gelar())+ " " + dataPP.getMcl_first()+"\n");
//						pesan.append((dataPP.getMcl_gelar().equals(null)?"":dataPP.getMcl_gelar())+ " " + dataPP.getMcl_first()+"\n");
						if(dataPP.getMcl_gelar()==null){
							pesan.append(dataPP.getMcl_first()+"\n");
						}else{
							pesan.append(dataPP.getMcl_gelar()+ " " + dataPP.getMcl_first()+"\n");
						}
						pesan.append("di tempat\n\n");
						pesan.append("Dengan Hormat,\n");
						pesan.append(kalimatPertama);
						pesan.append("Mohon Surat Penjaminan ini disimpan dan dicetak apabila diperlukan.\n\n");
						pesan.append("Email ini terkirim secara otomatis oleh sistem, harap jangan mereply email ini. Informasi lebih lanjut dapat disampaikan ke :\n <strong>cs@sinarmasmsiglife.co.id</strong>\n\n");
					
					uploader.setEmailmessage(pesan.toString().replaceAll("\n", "</br>"));
			
					if(!Common.isEmpty(email_client)) {
						String me_id = sequence.sequenceMeIdEmail();
						Date nowDate = commonDao.selectSysdate();
						EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, nowDate, null, 
								true, "info@sinarmasmsiglife.co.id", 
								uploader.getEmailto().replaceAll(" ", "").split(";") ,
								null,
								(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")),  
								uploader.getEmailsubject(), 
								uploader.getEmailmessage(), 
								attachments,14);
							Boolean ok = false;
							do{
								try{
									uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Kirim Softcopy Otomatis Ke : " + email_client, reg_spaj, 0);
									ok=true;
								}catch(Exception e){};
							}while (!ok);
							
						}
				}
			}catch(Exception e){
				logger.error("ERROR :", e);
				return false;}
			return true;
			
		};
		
	
	//Kirim OTOMATIS surat Penjaminan PROVIDER JALUR DIST BTN - Ryan
	public boolean softCopyOtomatisBTN(String reg_spaj, User currentUser, String email_client ) {
		try{
			
			Date sysdate = commonDao.selectSysdate();
			String cabang = uwDao.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			
	        
	        Pemegang dataPP = bacDao.selectpp(reg_spaj);
	        Tertanggung dataTT = bacDao.selectttg(reg_spaj);
	        AddressBilling addrBill = bacDao.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = bacDao.selectDataUsulanutama(reg_spaj);
	        Integer lsre_id = uwDao.selectPolicyRelation(reg_spaj);
	        Agen agen =this.bacDao.select_detilagen(reg_spaj);
	        dataUsulan.setDaftaRider(bacDao.selectDataUsulan_rider(reg_spaj));
	      //  List peserta=this.bacDao.select_all_mst_peserta(reg_spaj);
	        List peserta= this.bacDao.select_semua_mst_peserta2(reg_spaj);
	        
	        
			Map premiProdukUtama = this.uwDao.selectPremiProdukUtama(reg_spaj);
			String kurs = (String) premiProdukUtama.get("LKU_ID");
			Calendar cal = Calendar.getInstance(); 
			cal.add(Calendar.MONTH, 1);
			//Generate Welcome Letter
			DateFormat yy = new SimpleDateFormat("yyyy");
			DateFormat MM = new SimpleDateFormat("MM");

			// Get the date today using Calendar object.
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String reportDateMM = MM.format(sysdate);
			String reportDateYYYY = yy.format(sysdate);
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
			
			String fileDir = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\";
			String fileName ="SURAT_PENJAMINAN_PROVIDER.pdf";
			
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "DMTM - BTN");
			moreInfo.put("Subject", "SURAT KONFIRMASI PENJAMINAN DMTM - BTN");
			
			PdfContentByte over;
			BaseFont calibri = BaseFont.createFont("C:\\WINDOWS\\FONTS\\CALIBRI.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
					
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.term")+"\\SP_PROV_MERGE.pdf");
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"SURAT_PENJAMINAN_PROVIDER.pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	       
	        String noSPP = sequence.sequenceNoSuratPenjaminanProvider();			
	        
	        over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(calibri,9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, noSPP, 138, 687, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, reportDateYYYY, 277, 687, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, AngkaTerbilang.romawiBerjaya(reportDateMM), 251, 687, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(akseptasiDao.selectFAddMonths(FormatDate.toString(sysdate),new Integer(1))), 127, 560, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorPolis(dataPP.getMspo_policy_no()), 203, 513, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 203, 501, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) dataPP.getMspe_date_birth()), 203, 490, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) dataUsulan.getMste_beg_date()), 203, 468, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_name().toUpperCase(), 203, 479, 0);
		 
		    if(peserta.size()>0){
				int vertikal=416;
				for(int z=0;z<peserta.size();z++){
					vertikal = vertikal-16;
				//PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(z);
				//Map m = (HashMap) peserta.get(z);				
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,pesertaPlus.getNama(),113, vertikal, 0);
				//over.showTextAligned(PdfContentByte.ALIGN_CENTER,FormatDate.toString(pesertaPlus.getTgl_lahir()),447, vertikal, 0);
				Map m = (HashMap) peserta.get(z);				
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String) m.get("NAMA"),113, vertikal, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,FormatDate.toIndonesian((Date) m.get("TGL_LAHIR")),447, vertikal, 0);
					
				}
			}
		    
//		    String drIndra = props.getProperty("pdf.template.admedika2")+"\\drindra.bmp";
//		    Image img = Image.getInstance(drIndra);					
//			img.setAbsolutePosition(380, 300);		
//			img.scaleAbsolute(120, 34);
//			over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 95, 129);
//			over.stroke();
//		    over.endText();
//			stamp.close();	
			
		    String ttd = props.getProperty("pdf.template.admedika2")+"\\suryanto.jpg";
		    Image img = Image.getInstance(ttd);					
			img.setAbsolutePosition(380, 300);		
//			img.scaleAbsolute(90, 34);
			img.scalePercent(20);
			over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 95, 130);
			over.stroke();
		    over.endText();
			stamp.close();	
			
            File l_file = new File(outputName);
            
			CommandUploadBac uploader = new CommandUploadBac();
			uploader.setErrorMessages(new ArrayList<String>());
			
			uploader.setSpaj(reg_spaj);
			
			List detBisnis = uwDao.selectDetailBisnis(reg_spaj);
			String elesbees = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			
			if (l_file.exists()){
				List<DropDown> fileList = new ArrayList<DropDown>();
				List<File> attachments = new ArrayList<File>();
				
				attachments.add(l_file);
				
				uploader.setEmailto(email_client);
//				uploader.setEmailcc(currentUser.getLus_email());
				uploader.setEmailcc(commonDao.selectEmailUser(currentUser.getLus_id()));
				uploader.setEmailsubject("Softcopy Surat Penjaminan atas nama A/n "+dataPP.getMcl_first());
				StringBuffer pesan = new StringBuffer();
				String kalimatPertama = "";
					
					kalimatPertama = "Bersama ini kami kirimkan Surat Penjaminan A/n  "+dataPP.getMcl_first()+"\n\n";
					pesan.append("Kepada Yth.\n");
//					pesan.append((dataPP.getMcl_gelar().equals(null)?"":dataPP.getMcl_gelar())+ " " + dataPP.getMcl_first()+"\n");
//					pesan.append((dataPP.getMcl_gelar().equals(null)?"":dataPP.getMcl_gelar())+ " " + dataPP.getMcl_first()+"\n");
					if(dataPP.getMcl_gelar()==null){
						pesan.append(dataPP.getMcl_first()+"\n");
					}else{
						pesan.append(dataPP.getMcl_gelar()+ " " + dataPP.getMcl_first()+"\n");
					}
					pesan.append("di tempat\n\n");
					pesan.append("Dengan Hormat,\n");
					pesan.append(kalimatPertama);
					pesan.append("Mohon Surat Penjaminan ini disimpan dan dicetak apabila diperlukan.\n\n");
					pesan.append("Email ini terkirim secara otomatis oleh sistem, harap jangan mereply email ini. Informasi lebih lanjut dapat disampaikan ke :\n <strong>cs@sinarmasmsiglife.co.id</strong>\n\n");
				
				uploader.setEmailmessage(pesan.toString().replaceAll("\n", "</br>"));
		
				if(!Common.isEmpty(email_client)) {
					String me_id = sequence.sequenceMeIdEmail();
					Date nowDate = commonDao.selectSysdate();
					EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, nowDate, null, 
							true, "info@sinarmasmsiglife.co.id", 
							uploader.getEmailto().replaceAll(" ", "").split(";") ,
							null,
							(uploader.getEmailcc().trim().equals("")? null : uploader.getEmailcc().replaceAll(" ", "").split(";")),  
							uploader.getEmailsubject(), 
							uploader.getEmailmessage(), 
							attachments,14);
						Boolean ok = false;
						do{
							try{
								uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Kirim Softcopy Otomatis Ke : " + email_client, reg_spaj, 0);
								ok=true;
							}catch(Exception e){};
						}while (!ok);
						
					}
			}
		}catch(Exception e){
			logger.error("ERROR :", e);
			return false;}
		return true;
		
	}
}