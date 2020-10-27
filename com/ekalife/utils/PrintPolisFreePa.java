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
import com.ekalife.elions.model.Tmms;
import com.ekalife.elions.model.TmmsDet;
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

public class PrintPolisFreePa {
	
	protected static final Log logger = LogFactory.getLog( PrintPolisFreePa.class );
	
	private ElionsManager elionsManager;
	private UwManager uwManager;
	protected Properties props;
	
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setElionsManager(ElionsManager elionsManager) {this.elionsManager = elionsManager;}
	public Properties getProps() {
		return props;
	}
	public void setProps(Properties props) {
		this.props = props;
	}
	/**
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
	
	public Boolean generateFreePa(String outputName, Tmms tmms, TmmsDet tmmsDet, Date sysdate, String ingrid){
		Boolean result = false;
		try{
			
			File file = null;
			PdfReader reader=null ;
//			String mspo_policy_no = (String) dataAdmedika.get("MSPO_POLICY_NO");
			String mspo_policy_no_format = tmms.getNo_sertifikat();
//			String msen_endors_no = (String) dataAdmedika.get("MSEN_ENDORS_NO");
			String nama_pp = (String) tmms.getHolder_name();
			String nama_tt = (String) tmms.getHolder_name();
			Date beg_date = (Date) tmms.getBeg_date();
			Date end_date = (Date) tmms.getEnd_date();
			String alamat = (String) tmms.getAddress1();
			String kota = (String) tmms.getCity();
			String kode_pos = (String) tmms.getPostal_code();
			int usia = tmmsDet.getUsia();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			//Image img = Image.getInstance(props.getProperty("images.ttd.hamid"));
			
			
//			 adding some metadata
			HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "Mallinsurance");
			moreInfo.put("Subject", "FREE PA");
			
			PdfContentByte over;
			BaseFont arial_narrow = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			BaseFont arial_narrow_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			file = new File(outputName);
//			if(syariah){
				reader = new PdfReader("\\\\ebserver\\pdfind\\Template\\Pa\\SSUPAMallAssurance.pdf");
//			}else 
			//reader = new PdfReader("c:\\MergeAdmedikaEkaSehat.pdf");
			
			PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
			
			stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(arial_narrow_bold,12);
			
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS ASURANSI KECELAKAAN DIRI PERORANGAN", 299, 765, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, "RISIKO MENINGGAL DUNIA", 299, 751, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, "(PA RISIKO “A” - PERSONAL ACCIDENT)", 299, 737, 0);
			over.endText();
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(arial_narrow,8);
			//kiri
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Polis", 30, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Pemegang Polis / Usia", 30, 702, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 30, 689, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, ":     "+mspo_policy_no_format, 130, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, ":     "+nama_pp.toUpperCase()+" / "+usia+" Tahun", 130, 702, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, ":     ", 130, 689, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat, 30, 676, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, kota +" - "+kode_pos, 30, 667, 0);
			//kanan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Tertanggung / Usia", 299, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 299, 702, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 299, 689, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "      - Premi : Gratis", 399, 676, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, ":     "+nama_tt.toUpperCase()+" / "+usia+" Tahun", 399, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, ":     "+sdf.format(beg_date)+" s/d "+sdf.format(end_date), 399, 702, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, ":     Rp. 15.000.000,-", 399, 689, 0);
			
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "      Jakarta, "+FormatDate.toIndonesian(sysdate), 399, 640, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "      PT Asuransi Jiwa Sinarmas MSIG Tbk.", 399, 631, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "      Andrew Bain", 399, 575, 0);
			over.endText();
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(arial_narrow_bold,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "      _____________", 399, 575, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "       Chief Operating & IT Officer", 399, 566, 0);
			over.endText();
			
			Image img = Image.getInstance(ingrid);
			img.setAbsolutePosition(399, 586);		
			img.scaleAbsolute(120, 34);
			over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 399, 586);
			over.stroke();
			
			
//			if(syariah){
//				over = stamp.getOverContent(2);
//				over.beginText();
//				over.setFontAndSize(times_new_roman,11);
//				
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 230, 427, 0);
//				
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 340, 404, 0);
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 402, 381, 0);
//				over.endText();
//				
//				Image img = Image.getInstance(ingrid);
//				
//				img.setAbsolutePosition(380, 287);		
//				img.scaleAbsolute(120, 34);
//				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 287);
//				over.stroke();
//			}else{
//				over = stamp.getOverContent(2);
//				over.beginText();
//				over.setFontAndSize(times_new_roman,11);
//				
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 230, 93, 0);
//				over.endText();
				
//				over = stamp.getOverContent(3);
//				over.beginText();
//				over.setFontAndSize(times_new_roman,11);
				
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 230, 760, 0);
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 340, 760, 0);
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 402, 738, 0);
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 230, 738, 0);
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 340, 715, 0);
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 402, 692, 0);
//				over.endText();
				
//				Image img = Image.getInstance(ingrid);
//				
//				img.setAbsolutePosition(380, 611);		
//				img.scaleAbsolute(120, 34);
//				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 611);
//				over.stroke();
//			}
			
			
			
			
			
			/**(Deddy)
			 * Comment ini dibuka apabila ada request special case(admedika blum print kartu peserta)
			 */
//			Integer page_peserta = 5;
//			if(total_ekasehat>=1){
//				//List<Map> datapeserta = uwManager.selectDataPeserta(spaj);
//				for(int i=0; i<dataPeserta.size();i++){
//					page_peserta+=1;
//					Map perPeserta = dataPeserta.get(i);
//					String namaPeserta = (String) perPeserta.get("NAMA");
//					Date tgl_lahir = (Date) perPeserta.get("TGL_LAHIR");
//					String alamat = (String) perPeserta.get("MSAP_ADDRESS");
//					String plan = (String) perPeserta.get("LSDBS_NAME_SUB");
//					
//					over = stamp.getOverContent(page_peserta);
//					over.beginText();
//					over.setFontAndSize(times_new_roman,11);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaPeserta, 203, 540, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(tgl_lahir), 203, 528, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat, 203, 515, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "EKA SEHAT", 203, 502, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, mspo_policy_no_format, 203, 489, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, plan, 203, 476, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(beg_date), 203, 463, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 130, 211, 0);
//					over.endText();
//				}
//			}
			
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
			
			if(map.get("DISTRIBUSI").toString().equals("AGENCY")) {
				
				id = "Kontrak Agency System - " + map.get("JABATAN") + " (" + map.get("MCL_FIRST") + ")";
				id = id.replace(" ", "_");	 
				file = new File(map.get("save")+"\\"+id+".pdf");
				
				if(map.get("JABATAN").toString().contains("INKUBATOR")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-INKUBATOR.pdf");
				}
				else if(map.get("JABATAN").toString().contains("KANTOR MANDIRI")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-MANDIRI.pdf");
				}
				else if(map.get("JABATAN").toString().equals("AGENCY MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-AM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SALES MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-SM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SALES EXECUTIVE")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency System-SE.pdf");
				}
				
				if(map.get("JABATAN").toString().contains("AGENCY DIRECTOR")) {
					perwakilan = map.get("LEADER").toString();
					posisi = "DIREKTUR";
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
					posisi = "DIREKTUR";
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
					posisi = "DIREKTUR";
					perwakilan2 = map.get("LEADER2").toString();
					if(!perwakilan2.equals("")){
						//posisi2 = "ASSISTANT VP";
						posisi2 = map.get("jabatanpusat").toString();
					}else posisi2 = "";
				}else if(map.get("JABATAN").toString().equals("DISTRICT MANAGER") || map.get("JABATAN").toString().equals("BRANCH MANAGER")){
//					perwakilan = "INDRIA HERAWATY";
//					posisi = "ASSISTANT VP";
					perwakilan = "I.J SOEGENG WIBOWO";
					posisi = "DIREKTUR";
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
					posisi = "DIREKTUR";
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
				else if(map.get("JABATAN").toString().equals("AGENCY MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency Arthamas System-AM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SALES MANAGER")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency Arthamas System-SM.pdf");
				}
				else if(map.get("JABATAN").toString().equals("SALES EXECUTIVE")) {
					reader = new PdfReader(map.get("template")+"\\Kontrak Agency Arthamas System-SE.pdf");
				}
				
				if(map.get("JABATAN").toString().contains("AGENCY DIRECTOR")) {
					perwakilan = map.get("LEADER").toString();
					posisi = "DIREKTUR";
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
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tanggal, 272, 598, 0);
					//over.showTextAligned(PdfContentByte.ALIGN_LEFT, perwakilan, 192, 547, 0);
					//over.showTextAligned(PdfContentByte.ALIGN_LEFT, "JOS CHANDRA IRAWAN", 192, 547, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, "INDRIA HERAWATY", 192, 547, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("MCL_FIRST").toString(), 192, 472, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("placeBirth").toString() + " / "+ tglLahir, 192, 459, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("ktp").toString(), 192, 446, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("JABATAN").toString(), 192, 433, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("LSRG_NAMA").toString(), 192, 420, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("alamat").toString(), 192, 407, 0);
					over.endText();
				}else {
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tanggal, 272, 598, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, joinperwakilan, 192, 548, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("MCL_FIRST").toString(), 192, 471, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("placeBirth").toString() + " / "+ tglLahir, 192, 458, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("ktp").toString(), 192, 445, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("JABATAN").toString(), 192, 432, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("LSRG_NAMA").toString(), 192, 419, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("alamat").toString(), 192, 406, 0);
					over.endText();
				}
				
				
				
				
				
				over = stamp.getOverContent(10);
				over.beginText();
				over.setFontAndSize(arial_narrow,11);
				
				//bagian tanda tangan
				if(map.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)") || map.get("DISTRIBUSI").toString().equals("AGENCY ARTHAMAS") ) {
					//kedua fungsi dibawah untuk mendapatkan nama(perwakilan & perwakilan2) dan jabatannya(posisi & posisi2)
//					over.showTextAligned(PdfContentByte.ALIGN_CENTER, "INDRIA HERAWATY", 110, 330, 0);
//					over.showTextAligned(PdfContentByte.ALIGN_CENTER, "PRESIDEN DIREKTUR", 110, 318, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, perwakilan, 110, 330, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, posisi, 110, 318, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, perwakilan2, 246, 330, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, posisi2, 246, 318, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, map.get("MCL_FIRST").toString(), 382, 330, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, map.get("JABATAN").toString(), 382, 318, 0);					
				}else {
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, perwakilan, 110, 400, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, posisi, 110, 388, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, perwakilan2, 246, 400, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, posisi2, 246, 388, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, map.get("MCL_FIRST").toString(), 382, 400, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, map.get("JABATAN").toString(), 382, 388, 0);					
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
