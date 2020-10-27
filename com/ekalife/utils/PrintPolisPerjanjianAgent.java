package com.ekalife.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.model.Upp;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import com.mchange.v2.codegen.bean.PropsToStringGeneratorExtension;

public class PrintPolisPerjanjianAgent {
	
	protected static final Log logger = LogFactory.getLog( PrintPolisPerjanjianAgent.class );
	
	private ElionsManager elionsManager;
	private UwManager uwManager;
	protected Properties props;
	//private BacManager bacManager;
	
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setElionsManager(ElionsManager elionsManager) {this.elionsManager = elionsManager;}
	//public void setBacManager(BacManager bacManager) {this.bacManager = bacManager;}
	public Properties getProps() {
		return props;
	}
	public void setProps(Properties props) {
		this.props = props;
	}
	/**
	 * @param ekaSehatBaru 
	 * @param ekaSehatPlus 
	 * @return
	 */
	
/*	public static void main(String args[]) {
		try {
			// adding some metadata
			HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "Kontrak Marketing");
			moreInfo.put("Subject", "Perjanjian - perjanjian untuk Agen");

			// adding content to each page
			PdfContentByte over;
			BaseFont arial_narrow = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			BaseFont arial_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);	
			
			File file = new File("C:\\pdf\\temp.pdf");
			PdfReader reader = new PdfReader("C:\\pdf\\merge.pdf");	
			PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
			
			stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(arial_narrow,11);
			
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "99/99/9999", 303, 598, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama", 200, 484, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tempat / Tgl Lahir", 200, 473, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "No KTP / SIM", 200, 460, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jabatan", 200, 447, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 200, 434, 0);
			over.endText();
			
			
			over = stamp.getOverContent(10);
			over.beginText();
			over.setFontAndSize(arial_narrow,11);
			
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, "PERUSAHAAN ", 110, 420, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, "posisi ", 110, 408, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, "AGEN ", 382, 420, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, "LEVEL ", 382, 408, 0);			
			over.endText();
			stamp.close();
			
		}catch (Exception e) {
			logger.error("ERROR :", e);
    	}
	}*/
	
	public Boolean generateEndorseAdmedikaEkaSehat(String spaj, Integer total_ekasehat, String outputName, Map dataAdmedika, List<Map> dataPeserta, Date sysdate, String ingrid, String lsbs, String lsdbs, Boolean syariah, Integer ekaSehatHCP, Integer ekaSehatBaru, Integer ekaSehatPlus){
		Boolean result = false;
		
		try{
			
			File file = null;
			PdfReader reader=null ;

			String mspo_policy_no_format = (String) dataAdmedika.get("MSPO_POLICY_NO_FORMAT");
			String msen_endors_no = (String) dataAdmedika.get("MSEN_ENDORS_NO");
			String nama_pp = (String) dataAdmedika.get("NAMA_PP");
			String nama_tt = (String) dataAdmedika.get("NAMA_TT");
			Date beg_date = (Date) dataAdmedika.get("MSTE_BEG_DATE");
			//Image img = Image.getInstance(props.getProperty("images.ttd.hamid"));
			
//			 adding some metadata
			HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "Admedika");
			moreInfo.put("Subject", "EKA SEHAT Admedika");
			
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\TIMES.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			file = new File(outputName);
			if(ekaSehatHCP>=1 && ekaSehatBaru<1 && ekaSehatPlus<1){
				reader = new PdfReader("\\\\ebserver\\pdfind\\Template\\Endors_Eka_Sehat\\MergeAdmedikaHCP.pdf");
			}else if(ekaSehatPlus>=1){
				reader = new PdfReader("\\\\ebserver\\pdfind\\Template\\Endors_Eka_Sehat\\MergeAdmedikaMedicalPlus.pdf");
			}else if(syariah){
				reader = new PdfReader("\\\\ebserver\\pdfind\\Template\\Endors_Eka_Sehat\\MergeAdmedikaEkaSehatSyariah.pdf");
			}else reader = new PdfReader("\\\\ebserver\\pdfind\\Template\\Endors_Eka_Sehat\\MergeAdmedikaEkaSehatNew.pdf");
	
			PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
			
			stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,11);
			if(ekaSehatBaru>=1 || ekaSehatPlus>=1){
			if(syariah){		
				if(lsbs.equals("189") && Integer.parseInt(lsdbs.substring(1))>15){
				//EndorsemenPolisSyariahSMP.pdf					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nomor Endors                : "+msen_endors_no, 200, 730, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nomor Polis                   : "+mspo_policy_no_format, 200, 720, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Pemegang Polis   : "+nama_pp.toUpperCase(), 200, 710, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Peserta                 : "+nama_tt, 200, 700, 0);
					over.setFontAndSize(times_new_roman,8);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 270, 682, 0);
				}else{
					over.setFontAndSize(times_new_roman,9);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nomor Endors                : "+msen_endors_no, 200, 689, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nomor Polis                   : "+mspo_policy_no_format, 200, 679, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Pemegang Polis   : "+nama_pp.toUpperCase(), 200, 669, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Peserta                 : "+nama_tt, 200, 659, 0);
//					over.setFontAndSize(times_new_roman,8);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 270, 633, 0);
					
					//MANTA
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, msen_endors_no, 270, 739, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 270, 727, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_pp.toUpperCase(), 270, 715, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_tt, 270, 703, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 264, 680, 0);
				}
			}else{
				if(ekaSehatBaru>=1){
					//MANTA
					over.setFontAndSize(times_new_roman,9);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, msen_endors_no, 335, 738, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 335, 726, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_pp.toUpperCase(), 335, 715, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_tt, 335, 703, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 280, 681, 0);
				}else if(ekaSehatPlus>=1){
					over.setFontAndSize(times_new_roman,9);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nomor Endors                : "+msen_endors_no, 220, 700, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nomor Polis                   : "+mspo_policy_no_format, 220, 690, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Pemegang Polis   : "+nama_pp.toUpperCase(), 220, 680, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Tertanggung        : "+nama_tt, 220, 670, 0);	
					over.setFontAndSize(times_new_roman,9);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 280, 650, 0);
					over.setFontAndSize(times_new_roman,11);
				}
			}
			over.endText();
			if(syariah){				
				
				if(lsbs.equals("189") && Integer.parseInt(lsdbs.substring(1))>15){
				//EndorsemenPolisSyariahSMP.pdf
					over = stamp.getOverContent(2);
					over.beginText();
					over.setFontAndSize(times_new_roman,10);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 353, 205, 0);
					over.setFontAndSize(times_new_roman,11);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 335, 182, 0);
					over.endText();		
					
//					Image img = Image.getInstance(ingrid);					
//					img.setAbsolutePosition(380, 300);		
//					img.scaleAbsolute(120, 34);
//					over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 630);
					//over.stroke();
				}else{
					over = stamp.getOverContent(2);					
					over.beginText();
					over.setFontAndSize(times_new_roman,9);
					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 360, 218, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 326, 195, 0);					
					over.endText();
					
//					Image img = Image.getInstance(ingrid);//					
//					img.setAbsolutePosition(380, 300);		
//					img.scaleAbsolute(120, 34);
//					over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 374);
//					over.stroke();
				}
		/*		
				over = stamp.getOverContent(3);
				over.beginText();
				over.setFontAndSize(times_new_roman,11);				
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 230, 760, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 340, 760, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 402, 738, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 240, 760, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 345, 743, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 405, 704, 0);
				over.endText();				
				Image img = Image.getInstance(ingrid);				
				img.setAbsolutePosition(380, 630);		
				img.scaleAbsolute(120, 34);
				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 630);
				over.stroke();*/
			}else{
				/*over = stamp.getOverContent(2);
				over.beginText();
				over.setFontAndSize(times_new_roman,11);			
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 230, 93, 0);
				over.endText();*/
				if(ekaSehatBaru>=1){
					//MANTA
					over = stamp.getOverContent(2);
					over.beginText();
					over.setFontAndSize(times_new_roman,9);					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 350, 165, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 370, 142, 0);		
					over.endText();
				}else if(ekaSehatPlus>=1){
					over = stamp.getOverContent(2);
					over.beginText();
					over.setFontAndSize(times_new_roman,9);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 350, 204, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 399, 182, 0);
					over.endText();
				}
				
				/*Image img = Image.getInstance(ingrid);
			
				img.setAbsolutePosition(380, 630);		
				img.scaleAbsolute(120, 34);
				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 630);
				over.stroke();*/
			}
			
			
			
			
			
			/**(Deddy)
			 * Comment ini dibuka apabila ada request special case(admedika blum print kartu peserta)
			 */
			/*Integer page_peserta = 5;
			if(total_ekasehat>=1){
				//List<Map> datapeserta = uwManager.selectDataPeserta(spaj);
				for(int i=0; i<dataPeserta.size();i++){
					page_peserta+=1;
					Map perPeserta = dataPeserta.get(i);
					String namaPeserta = (String) perPeserta.get("NAMA");
					Date tgl_lahir = (Date) perPeserta.get("TGL_LAHIR");
					String alamat = (String) perPeserta.get("MSAP_ADDRESS");
					String plan = (String) perPeserta.get("LSDBS_NAME_SUB");
					
					over = stamp.getOverContent(page_peserta);
					over.beginText();
					over.setFontAndSize(times_new_roman,11);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaPeserta, 203, 540, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(tgl_lahir), 203, 528, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat, 203, 515, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "EKA SEHAT", 203, 502, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 203, 489, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, plan, 203, 476, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 203, 463, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 130, 211, 0);
					over.endText();
				}
			}*/
		}
			stamp.close();	
			result=true;
		}catch (Exception e) {
			logger.error("ERROR :", e);
			result=false;
    	}
		
		return result;
	}
	
	public static Boolean generatePolis(Map map){
		Boolean result=false;   	
		
		try {	
			
			String id = "";
			File file = null;
			PdfReader reader=null ;
			String perwakilan = "";
			String posisi = "";
			String perwakilan2 = "";
			String posisi2 = "";
			String joinperwakilan = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("01/MM/yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
			String tanggal = dateFormat.format(map.get("MSAG_BEG_DATE"));
			String tglLahir = dateFormat2.format(map.get("birth"));
			
			// adding some metadata
			HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "Kontrak Marketing");
			moreInfo.put("Subject", "Perjanjian - perjanjian untuk Agen");

			// adding content to each page
			PdfContentByte over;
			BaseFont arial_narrow = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			BaseFont arial_narrow_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			BaseFont arial = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			BaseFont arial_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
			if(map.get("DISTRIBUSI").toString().equals("AGENCY") || map.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY") ||
					map.get("DISTRIBUSI").toString().equals("NEW AGENCY")) {
				
				//yayan
				String filenameJabatanBaru = (String) map.get("JABATAN");					
				if(filenameJabatanBaru.equalsIgnoreCase("BUSINESS PARTNER *** (INKUBATOR)")){
					filenameJabatanBaru = "BUSINESS PARTNER 3 (INKUBATOR)";
				}
				else if(filenameJabatanBaru.equalsIgnoreCase("BUSINESS PARTNER *** (KANTOR MANDIRI)")){
					filenameJabatanBaru = "BUSINESS PARTNER 3 (KANTOR MANDIRI)";
				}
				else if(filenameJabatanBaru.equalsIgnoreCase("BUSINESS PARTNER **")){
					filenameJabatanBaru = "BUSINESS PARTNER 2";
				}	
				else if(filenameJabatanBaru.equalsIgnoreCase("BUSINESS PARTNER *")){
					filenameJabatanBaru = "BUSINESS PARTNER";
				}
				else if(filenameJabatanBaru.equalsIgnoreCase("FINANCIAL CONSULTANT")){
					filenameJabatanBaru = "FINANCIAL CONSULTANT";
				}else{
					filenameJabatanBaru = (String) map.get("JABATAN");
				}
				
				id = "Kontrak Agency System - " + filenameJabatanBaru + " (" + map.get("MCL_FIRST") + ")";
				if(map.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY")){
					id = "Kontrak Bridge System - " + map.get("JABATAN") + " (" + map.get("MCL_FIRST") + ")";
				}
				id = id.replace(" ", "_");	 
				file = new File(map.get("save")+"\\"+id+".pdf");
				
				if(map.get("DISTRIBUSI").toString().equals("AGENCY")){
					//yayan
					//String xx = map.get("JABATAN").toString();
					//String ss = xx;
					
					if(map.get("JABATAN").toString().contains("INKUBATOR")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-INKUBATOR.pdf");
					}
					else if(map.get("JABATAN").toString().contains("KANTOR MANDIRI")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-MANDIRI.pdf");
					}
					else if(map.get("JABATAN").toString().equals("AGENCY MANAGER") || map.get("JABATAN").toString().equals("BUSINESS PARTNER **")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-AM.pdf");
					}
					else if(map.get("JABATAN").toString().equals("SALES MANAGER") || map.get("JABATAN").toString().equals("BUSINESS PARTNER *")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-SM.pdf");
					}
					else if(map.get("JABATAN").toString().equals("SALES EXECUTIVE") || map.get("JABATAN").toString().trim().equals("FINANCIAL CONSULTANT") ) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-SE.pdf");
					}
				}
				
				if(map.get("DISTRIBUSI").toString().equals("NEW AGENCY")){
					if(map.get("JABATAN").toString().contains("INKUBATOR")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-INKUBATOR(NEW AGENCY).pdf");
					}
					else if(map.get("JABATAN").toString().contains("KANTOR MANDIRI")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-MANDIRI(NEW AGENCY).pdf");
					}
					else if(map.get("JABATAN").toString().equals("AGENCY MANAGER") || map.get("JABATAN").toString().equals("BUSINESS PARTNER **")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-AM(NEW AGENCY).pdf");
					}
					else if(map.get("JABATAN").toString().equals("SALES MANAGER") || map.get("JABATAN").toString().equals("BUSINESS PARTNER *")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-SM(NEW AGENCY).pdf");
					}
					else if(map.get("JABATAN").toString().equals("SALES EXECUTIVE") || map.get("JABATAN").toString().equals("FINANCIAL CONSULTANT")) {
						reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-SE(NEW AGENCY).pdf");
					}
				}
				
				if(map.get("JABATAN").toString().contains("AGENCY DIRECTOR")) {
					perwakilan = map.get("LEADER").toString();
					//posisi = "DIREKTUR";
//					posisi = "CHIEF AGENCY OFFICER";
					posisi = map.get("jabatanpusat").toString();
					perwakilan2 = map.get("LEADER2").toString();
					if(!perwakilan2.equals("")){
						//posisi2 = "VP_SALES";
						posisi2 = map.get("jabatanpusat").toString();
					}else posisi2 = "";
//				}else if(map.get("JABATAN").toString().contains("AGENCY MANAGER")){
//					perwakilan = map.get("LEADER2").toString();
//					if(!perwakilan.equals("")){
//						posisi = "VP_SALES";
//					}else posisi = "";
//				else {
//					perwakilan2 = map.get("vp_sales").toString();
//					posisi2 = "AGENCY DIRECTOR";
//					perwakilan = map.get("LEADER2").toString();
//					if(!perwakilan.equals("")){
//						posisi = "VP_SALES";
//					}else posisi = "";
//				}	
				}else if(map.get("JABATAN").toString().equals("SALES EXECUTIVE") || map.get("JABATAN").toString().equals("SALES MANAGER") ){
//					perwakilan = map.get("LEADER2").toString();
//					if(perwakilan.equals("")){
//						perwakilan = map.get("vp_sales").toString();
//						posisi = "SDM";
//					}
					perwakilan2 = map.get("vp_sales").toString();
					if(perwakilan2.equals("")){
						posisi2 = "";
					}else{
						posisi2 = "AGENCY DIRECTOR";
					}
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						//posisi = "SDM";
						posisi = map.get("jabatanpusat").toString();
					}else posisi = "";
				}else if(map.get("JABATAN").toString().equals("FINANCIAL CONSULTANT") || map.get("JABATAN").toString().equals("BUSINESS PARTNER *") ){
					if(!map.get("vp_sales").toString().equals("")){
						perwakilan2 = map.get("vp_sales").toString();
						posisi2 = "BUSINESS PARTNER ***";
					}
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						//posisi = "EVP_SALES";
						posisi = map.get("jabatanpusat").toString();
					}else posisi = "";
				}
				else {
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						//posisi = "EVP_SALES";
						posisi = map.get("jabatanpusat").toString();
					}else posisi = "";
				}
			}
			else if(map.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)")) {
				
				id = "Kontrak Hybrid System(Arthamas) - " + map.get("JABATAN") + " (" + map.get("MCL_FIRST") + ")";
				id = id.replace(" ", "_");	 
				file = new File(map.get("save")+"\\"+id+".pdf");
				
				if(map.get("JABATAN").toString().equals("REGIONAL DIRECTOR")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-RD.pdf");
				}
				else if(map.get("JABATAN").toString().equals("REGIONAL MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-RM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("DISTRICT MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-DM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("BRANCH MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-BM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SALES MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-SM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("FINANCIAL CONSULTANT")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-FC.pdf");
				}
				
				if(map.get("JABATAN").toString().contains("REGIONAL DIRECTOR") || map.get("JABATAN").toString().contains("REGIONAL MANAGER")) {
					perwakilan = map.get("LEADER").toString();
					//posisi = "DIREKTUR";
					posisi = "CHIEF AGENCY OFFICER";
					//perwakilan2 = "JOS CHANDRA IRAWAN";
					perwakilan2 = "INDRIA HERAWATY";
					if(!perwakilan2.equals("")){
						//posisi2 = "EXECUTIVE VP";
						posisi2 = map.get("jabatanpusat").toString();
					}else posisi2 = "";
				}
				else {
					perwakilan = "JOS CHANDRA IRAWAN";
					posisi = "EXECUTIVE VP";
					perwakilan2 = "INDRIA HERAWATY";
					if(!perwakilan2.equals("")){
						posisi2 = "ASSISTANT VP";
					}else posisi2 = "";
				}
//				else {
//					perwakilan = map.get("LEADER2").toString();
//					if(!perwakilan.equals("")){
//						posisi = "VP_SALES";
//					}else posisi = "";
				//}
			}
			else if(map.get("DISTRIBUSI").toString().equals("HYBRID(AJS)")) {
				
				id = "Kontrak Hybrid System(AJS) - " + map.get("JABATAN") + " (" + map.get("MCL_FIRST") + ")";
				id = id.replace(" ", "_");	 
				file = new File(map.get("save")+"\\"+id+".pdf");
				
				if(map.get("JABATAN").toString().equals("REGIONAL DIRECTOR")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-RD(AJS).pdf");
				}
				else if(map.get("JABATAN").toString().equals("REGIONAL MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-RM(AJS).pdf");
				}
				else if(map.get("JABATAN").toString().equals("DISTRICT MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-DM(AJS).pdf");
				}
				else if(map.get("JABATAN").toString().equals("BRANCH MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-BM(AJS).pdf");
				}
				else if(map.get("JABATAN").toString().equals("SALES MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-SM(AJS).pdf");
				}
				else if(map.get("JABATAN").toString().equals("FINANCIAL CONSULTANT")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Hybrid System-FC(AJS).pdf");
				}
				
				if(map.get("JABATAN").toString().contains("REGIONAL DIRECTOR") || map.get("JABATAN").toString().contains("REGIONAL MANAGER")) {
					perwakilan = map.get("LEADER").toString();
					//posisi = "DIREKTUR";
					posisi = "CHIEF AGENCY OFFICER";
					perwakilan2 = map.get("LEADER2").toString();
					if(!perwakilan2.equals("")){
						//posisi2 = "ASSISTANT VP";
						posisi2 = map.get("jabatanpusat").toString();
					}else posisi2 = "";
				}else if(map.get("JABATAN").toString().equals("DISTRICT MANAGER") || map.get("JABATAN").toString().equals("BRANCH MANAGER")){
//					perwakilan = "INDRIA HERAWATY";
//					posisi = "ASSISTANT VP";
					//perwakilan = "I.J SOEGENG WIBOWO";
					//posisi = "DIREKTUR";
					perwakilan = "JOS CHANDRA IRAWAN";
					posisi = "CHIEF AGENCY OFFICER";
				}else {  
					perwakilan2 = map.get("vp_sales").toString();
					if(perwakilan2.equals("")){
						posisi2 = "";
					}else{
						posisi2 = "REGIONAL MANAGER";
					}
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						//posisi = "VP_SALES";
						posisi = map.get("jabatanpusat").toString();
					}else posisi = "";
				}
				
				//REQ HULUK : JOS CHANDRA IRAWAN SUDAH TIDAK DIMUNCULKAN
//				if(map.get("JABATAN").toString().contains("REGIONAL DIRECTOR") || map.get("JABATAN").toString().contains("REGIONAL MANAGER")) {
//					perwakilan = map.get("LEADER").toString();
//					posisi = "DIREKTUR";
//					perwakilan2 = "JOS CHANDRA IRAWAN";
//					if(!perwakilan2.equals("")){
//						posisi2 = "EXECUTIVE VP";
//					}else posisi2 = "";
//				}
//				else {
//					perwakilan = "JOS CHANDRA IRAWAN";
//					posisi = "EXECUTIVE VP";
//					perwakilan2 = "INDRIA HERAWATY";
//					if(!perwakilan2.equals("")){
//						posisi2 = "ASSISTANT VP";
//					}else posisi2 = "";
//				}
				
//				if(map.get("JABATAN").toString().contains("REGIONAL DIRECTOR") || map.get("JABATAN").toString().contains("REGIONAL MANAGER")) {
//					perwakilan = map.get("LEADER").toString();
//					posisi = "DIREKTUR";
//					perwakilan2 = map.get("LEADER2").toString();
//					if(!perwakilan2.equals("")){
//						posisi2 = "VP_SALES";
//					}else posisi2 = "";
//				}
//				else {
//					perwakilan2 = map.get("vp_sales").toString();
//					posisi2 = "REGIONAL DIRECTOR";
//					perwakilan = map.get("LEADER2").toString();
//					if(!perwakilan.equals("")){
//						posisi = "VP_SALES";
//					}else posisi = "";
//					
//				}				
			}			
			else if(map.get("DISTRIBUSI").toString().equals("REGIONAL")) {
				
				id = "Kontrak Regional System - " + map.get("JABATAN") + " (" + map.get("MCL_FIRST") + ")";
				id = id.replace(" ", "_");	 
				file = new File(map.get("save")+"\\"+id+".pdf");
				
				if(map.get("JABATAN").toString().contains("REGIONAL MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Regional System-RM.pdf");
				}
				else if(map.get("JABATAN").toString().contains("SENIOR BRANCH MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Regional System-SBM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("BRANCH MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Regional System-BM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("UNIT MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Regional System-UM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("MARKETING EXECUTIVE")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Regional System-ME.pdf");
				}
				
				if(map.get("JABATAN").toString().contains("REGIONAL MANAGER") || map.get("JABATAN").toString().contains("SENIOR BRANCH MANAGER") || map.get("JABATAN").toString().contains("BRANCH MANAGER")) {
					perwakilan = map.get("LEADER").toString();
					//posisi = "DIREKTUR";
					posisi = "CHIEF AGENCY OFFICER";
					if(map.get("JABATAN").toString().contains("BRANCH MANAGER")){
						perwakilan2 = map.get("vp_sales").toString();
					}else{
						perwakilan2 = map.get("LEADER2").toString();
					}
					if(!perwakilan2.equals("")){
						//posisi2 = "VP_SALES";
						posisi2 = map.get("jabatanpusat").toString();
						if(posisi2.equalsIgnoreCase("ADS")){
							posisi2 = "AGENCY DEVELOPMENT SPECIALIST";
						}
					}else posisi2 = "";
				}
				else {
					perwakilan2 = map.get("vp_sales").toString();
					if(perwakilan2.equals("")){
						posisi2 = "";
					}else{
						posisi2 = "REGIONAL MANAGER";
					}
					
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						//posisi = "VP_SALES";
						posisi = map.get("jabatanpusat").toString();
						if(posisi.equalsIgnoreCase("ADS")){
							posisi = "AGENCY DEVELOPMENT SPECIALIST";
						}
					}else posisi = "";
				}				
			}	
			else if(map.get("DISTRIBUSI").toString().equals("AGENCY ARTHAMAS")) {
				
				id = "Kontrak Agency Arthamas System - " + map.get("JABATAN") + " (" + map.get("MCL_FIRST") + ")";
				id = id.replace(" ", "_");	 
				file = new File(map.get("save")+"\\"+id+".pdf");
				
				if(map.get("JABATAN").toString().contains("INKUBATOR")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency Arthamas System-INKUBATOR.pdf");
				}
				else if(map.get("JABATAN").toString().contains("KANTOR MANDIRI")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency Arthamas System-MANDIRI.pdf");
				}
				else if(map.get("JABATAN").toString().equals("AGENCY MANAGER") || map.get("JABATAN").toString().equals("BUSINESS PARTNER **")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency Arthamas System-AM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SALES MANAGER") || map.get("JABATAN").toString().equals("BUSINESS PARTNER *")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency Arthamas System-SM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SALES EXECUTIVE") || map.get("JABATAN").toString().equals("FINANCIAL CONSULTANT")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency Arthamas System-SE.pdf");
				}
				
				if(map.get("JABATAN").toString().contains("AGENCY DIRECTOR")) {
					perwakilan = map.get("LEADER").toString();
					//posisi = "DIREKTUR";
					posisi = "CHIEF AGENCY OFFICER";
					perwakilan2 = map.get("LEADER2").toString();
					if(!perwakilan2.equals("")){
						posisi2 = "VP_SALES";
					}else posisi2 = "";
//				}else if(map.get("JABATAN").toString().contains("AGENCY MANAGER")){
//					perwakilan = map.get("LEADER2").toString();
//					if(!perwakilan.equals("")){
//						posisi = "VP_SALES";
//					}else posisi = "";
//				}
//				else {
//					perwakilan2 = map.get("vp_sales").toString();
//					posisi2 = "AGENCY DIRECTOR";
//					perwakilan = map.get("LEADER2").toString();
//					if(!perwakilan.equals("")){
//						posisi = "VP_SALES";
//					}else posisi = "";
//				}			
				}else {
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						posisi = "VP_SALES";
					}else posisi = "";
				}
			}
			else if(map.get("DISTRIBUSI").toString().equals("BUSINESS PARTNER")) {
				
				id = "Kontrak Business Partner - " + map.get("JABATAN") + " (" + map.get("MCL_FIRST") + ")";
				id = id.replace(" ", "_");	 
				file = new File(map.get("save")+"\\"+id+".pdf");
				
				if(map.get("JABATAN").toString().contains("ADM")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Business Partner(ADM).pdf");
				}
				else if(map.get("JABATAN").toString().contains("CRO")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Business Partner(CRO).pdf");
				}
				else if(map.get("JABATAN").toString().equals("RO")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Business Partner(RO).pdf");
				}
				
				if(map.get("JABATAN").toString().contains("ADM")) {
					perwakilan = map.get("LEADER").toString();
					//posisi = "DIREKTUR";
					posisi = "CHIEF AGENCY OFFICER";
					perwakilan2 = map.get("LEADER2").toString();
					if(!perwakilan2.equals("")){
						//posisi2 = "VP_SALES";
						posisi2 = map.get("jabatanpusat").toString();
					}else posisi2 = "";
				}
				else {
					perwakilan2 = map.get("vp_sales").toString();
					if(perwakilan2.equals("")){
						posisi2 = "";
					}else{
						posisi2 = "REGIONAL MANAGER";
					}
					
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						//posisi = "VP_SALES";
						posisi = map.get("jabatanpusat").toString();
					}else posisi = "";
				}				
			}else if(map.get("DISTRIBUSI").toString().equals("AKM WORKSITE")) {
				
				id = "Kontrak AKM Worksite - " + map.get("JABATAN") + " (" + map.get("MCL_FIRST") + ")";
				id = id.replace(" ", "_");	 
				file = new File(map.get("save")+"\\"+id+".pdf");
				
			if(map.get("JABATAN").toString().contains("AD")  || map.get("JABATAN").toString().contains("BP ***")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-MANDIRI.pdf");
				}
				else if(map.get("JABATAN").toString().equals("AM-BM") || map.get("JABATAN").toString().equals("BP **")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-AM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SM") || map.get("JABATAN").toString().equals("BP *")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-SM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SE") || map.get("JABATAN").toString().equals("FC")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-SE.pdf");
				}
				
				if(map.get("JABATAN").toString().contains("AGENCY DIRECTOR")) {
					perwakilan = map.get("LEADER").toString();
					//posisi = "DIREKTUR";
					posisi = "CHIEF AGENCY OFFICER";
					perwakilan2 = map.get("LEADER2").toString();
					if(!perwakilan2.equals("")){
						//posisi2 = "VP_SALES";
						posisi2 = map.get("jabatanpusat").toString();
					}else posisi2 = "";
//				}else if(map.get("JABATAN").toString().contains("AGENCY MANAGER")){
//					perwakilan = map.get("LEADER2").toString();
//					if(!perwakilan.equals("")){
//						posisi = "VP_SALES";
//					}else posisi = "";
//				else {
//					perwakilan2 = map.get("vp_sales").toString();
//					posisi2 = "AGENCY DIRECTOR";
//					perwakilan = map.get("LEADER2").toString();
//					if(!perwakilan.equals("")){
//						posisi = "VP_SALES";
//					}else posisi = "";
//				}	
				}else if(map.get("JABATAN").toString().equals("SALES EXECUTIVE") || map.get("JABATAN").toString().equals("SALES MANAGER") ){
//					perwakilan = map.get("LEADER2").toString();
//					if(perwakilan.equals("")){
//						perwakilan = map.get("vp_sales").toString();
//						posisi = "SDM";
//					}
					perwakilan2 = map.get("vp_sales").toString();
					if(perwakilan2.equals("")){
						posisi2 = "";
					}else{
						posisi2 = "AGENCY DIRECTOR";
					}
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						//posisi = "SDM";
						posisi = map.get("jabatanpusat").toString();
					}else posisi = "";
				}else {
					perwakilan = map.get("LEADER2").toString();
					if(!perwakilan.equals("")){
						//posisi = "EVP_SALES";
						posisi = map.get("jabatanpusat").toString();
					}else posisi = "";
				}
			}
			
			
			if(perwakilan2.equals("")){
				joinperwakilan = perwakilan;
			}else if(!perwakilan2.equals("")){
				joinperwakilan = perwakilan+" dan "+perwakilan2;
			}
			
				PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
				stamp.setMoreInfo(moreInfo);
				
				over = stamp.getOverContent(1);
				over.beginText();
				over.setFontAndSize(arial_narrow,11);
				if(map.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)") || map.get("DISTRIBUSI").toString().equals("AGENCY ARTHAMAS") ) {
					Integer pengurangtanggalX=0;
					Integer pengurangtanggalY=0;
					Integer pengurangY=34;
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tanggal, 297-pengurangtanggalX, 599-pengurangtanggalY, 0);
					//over.showTextAligned(PdfContentByte.ALIGN_LEFT, perwakilan, 192, 547, 0);
					//over.showTextAligned(PdfContentByte.ALIGN_LEFT, "JOS CHANDRA IRAWAN", 192, 547, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "ARIE S. HARIYANTO, SE.MM", 192, 548, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("MCL_FIRST").toString(), 192, 473-pengurangY, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("placeBirth").toString() + " / "+ tglLahir, 192, 460-pengurangY, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("ktp").toString(), 192, 447-pengurangY, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("JABATAN").toString(), 192, 434-pengurangY-8, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("LSRG_NAMA").toString(), 192, 421-pengurangY-10, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("alamat").toString(), 192, 408-pengurangY-13, 0);
					over.endText();
				}else {
					Integer pengurangtanggalX=1;
					Integer penambahtanggalY=32;//43
					Integer pengurangX = 10;
					Integer pengurangY=40;
					Integer penambahX=0;
					Integer penambahY=28;//40
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tanggal, 297-pengurangtanggalX, 598+penambahtanggalY, 0);	
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, joinperwakilan, 197-pengurangX, 548+penambahY, 0);	
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("MCL_FIRST").toString(), 197-pengurangX, 463+penambahY, 0);//473
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("placeBirth").toString() + " / "+ tglLahir, 197-pengurangX, 447+penambahY, 0);//460
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("ktp").toString(), 197-pengurangX, 435+penambahY-3, 0);//447
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("JABATAN").toString(), 197-pengurangX, 421+penambahY-6, 0);//434
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("LSRG_NAMA").toString(), 197-pengurangX, 407+penambahY-9, 0);//421
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("alamat").toString(), 197-pengurangX, 394+penambahY-12, 0);//408
					over.endText();
				}
				
				//bagian tanda tangan
				if(map.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)") || map.get("DISTRIBUSI").toString().equals("AGENCY ARTHAMAS") ) {
					over = stamp.getOverContent(12);
					over.beginText();
					over.setFontAndSize(arial_narrow,11);
					Integer pembunuhY=200;
					//kedua fungsi dibawah untuk mendapatkan nama(perwakilan & perwakilan2) dan jabatannya(posisi & posisi2)
//					over.showTextAligned(PdfContentByte.ALIGN_CENTER, "INDRIA HERAWATY", 110, 330, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_CENTER, "PRESIDEN DIREKTUR", 110, 318, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_CENTER, "ARIE S. HARIYANTO, SE.MM", 110, 330, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_CENTER, "DIREKTUR", 110, 318, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, perwakilan2, 246, 330-pembunuhY, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, posisi2, 246, 318-pembunuhY, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, map.get("MCL_FIRST").toString(), 382, 330-pembunuhY, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, map.get("JABATAN").toString(), 382, 318-pembunuhY, 0);					
				}else {
					over = stamp.getOverContent(11);//12
					over.beginText();
					over.setFontAndSize(arial_narrow,11);
					Integer pembunuhY=0;
					Integer penyelamatY=80;
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, perwakilan, 110, 380+penyelamatY, 0);//400
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, posisi, 110, 368+penyelamatY, 0);//388
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, perwakilan2, 261, 380+penyelamatY, 0);//x246=,y=400
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, posisi2, 261, 368+penyelamatY, 0);//x=246,y=388
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, map.get("MCL_FIRST").toString(), 412, 380+penyelamatY, 0);//x=382,y=400
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, map.get("JABATAN").toString(), 412, 368+penyelamatY, 0);//x=382,y=388					
				}
				
				over.endText();
				stamp.close();				
				
			result=true;
    	}catch (Exception e) {
			logger.error("ERROR :", e);
			result=false;
    	}
    	
    	return result;
	}
}
