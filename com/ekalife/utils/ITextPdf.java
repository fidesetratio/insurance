package com.ekalife.utils;

import id.co.sinarmaslife.std.util.DateUtil;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ibatis.common.resources.Resources;
import com.ibm.icu.util.Calendar;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import jxl.format.BoldStyle;

public class ITextPdf {
	
	protected static final Log logger = LogFactory.getLog( ITextPdf.class );
	
    private static String pdfPolisPath;
    private static Properties props;
    
    static {

        props = new Properties();
        InputStream in = null;
        
        try {
            in = new FileInputStream(Resources.getResourceAsFile("ekalife.properties"));
            props.load(in);
        } catch (Exception e) {
            logger.error("ERROR :", e);
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("ERROR :", e);
                }
            }
        }
        
        pdfPolisPath =props.getProperty("pdf.dir.export");
//      pdfPolisPath = "\\\\ebserver\\pdfind\\Polis_Testing";
        
    }
    
    /**
	 *@author Deddy
	 * @throws IOException 
	 * @throws DocumentException 
	 *@since Oct 17, 2014
	 *@description TODO
	 */
	public static void generateSertifikatFreePaDmtm(String no_sertifikat, String no_polis_induk, String nama_pp, String nama_tt, Date tgl_lahir, Date tgl_sekarang, String product_code, String up_text, String insper_text) throws IOException, DocumentException{
		String pathFile = "\\\\ebserver\\pdfind\\Template\\sertifikat_freepa.pdf";
//		PdfReader reader = new PdfReader(pathFile);
		String outputName = pdfPolisPath + "\\free_pa\\"+no_sertifikat+".pdf";
		
		if("060".equals(product_code)) {
		    pathFile = "\\\\ebserver\\pdfind\\Template\\sertifikat_freepa_bank_dki.pdf";
		    outputName = pdfPolisPath + "\\free_pa\\060\\"+no_sertifikat+".pdf";
		}
		
        PdfReader reader = new PdfReader(pathFile);
		File file = new File(outputName);
		PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
        PdfContentByte cb = stamp.getOverContent(1);
        cb.beginText();
        BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\TIMESBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.setFontAndSize(bf, 18);
        int x = 0;
        int y = 0;
        //ini bagian judul
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "S E R T I F I K A T", 299, 660, 0); 
        
        if("060".equals(product_code)) {
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "ASURANSI KECELAKAAN DIRI", 296, 640, 0);
        }
        
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        
        //Ini bagian detail body
        bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\TIMES.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.setFontAndSize(bf, 10);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Sertifikat", 50, 600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, no_sertifikat, 240, 600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Berdasarkan pada Polis Induk No", 50, 585, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 585, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, no_polis_induk, 240, 585, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Atas Nama Pemegang Polis", 50, 570, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 570, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_pp, 240, 570, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Bersama ini Dinyatakan Bahwa", 50, 555, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 555, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_tt, 240, 555, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tanggal Lahir", 50, 540, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 540, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, df1.format(tgl_lahir), 240, 540, 0);
        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Adalah Peserta Asuransi Kumpulan, dengan :", 50, 510, 0);
        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jenis Asuransi", 50, 480, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 480, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asuransi Kecelakaan Diri Risiko Meninggal Dunia Akibat Kecelakaan (Risiko A)", 240, 480, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 50, 465, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 465, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, up_text, 240, 465, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Premi", 50, 450, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 450, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Gratis", 240, 450, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 50, 435, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 230, 435, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, insper_text, 240, 435, 0);
        
        String[] wording = pecahParagrafLineBreaksInclude("Sertifikat ini diterbitkan untuk Peserta sebagai bukti dari penutupan Asuransi Kecelakaan Diri Kumpulan antara PT Asuransi Jiwa Sinarmas MSIG Tbk. dan pemegang polis. Sertifikat ini tunduk pada Syarat-syarat Umum, Syarat-syarat Khusus dan Tambahan lain (jika ada) yang dilekatkan dan merupakan bagian mutlak yang tidak dapat dipisah-pisahkan dari Polis Induk.", 48);
    	for(int i=0; i<wording.length; i++) {
    		int addY = 15 * i;
    		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, wording[i] , 			50, 395-addY, 0);
    	}
    	
    	cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Jakarta, "+ df2.format(tgl_sekarang), 440, 395, 0);
    	cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "PT Asuransi Jiwa Sinarmas MSIG Tbk.", 440, 380, 0);
    	
//    	Image img = Image.getInstance("\\\\ebserver\\pdfind\\Template\\Endors_Eka_Sehat\\hamid.bmp");
    	
    	String path = Resources.getResourceURL(props.getProperty("images.ttd.direksi")).getPath();
        Image img = Image.getInstance(path);
		
		img.setAbsolutePosition(380, 340);		
		img.scaleAbsolute(120, 34);
		cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 340);
    	
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Hidenori Kui", 440, 325, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "__________", 440, 324, 0);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Wakil Presiden Direktur", 440, 310, 0);
		
        cb.endText();
        
        if(stamp!=null){
        	stamp.close();
        }
		if(reader!=null){
			reader.close();
		}
		
	}
	
	public static void generateSertifikatFreePaDmtm(String no_sertifikat, String no_polis_induk, String nama_pp, String nama_tt, Date tgl_lahir, Date tgl_sekarang) throws IOException, DocumentException {
	    generateSertifikatFreePaDmtm(no_sertifikat, no_polis_induk, nama_pp, nama_tt, tgl_lahir, tgl_sekarang, "056", "Rp. 50.000.000,- (lima puluh juta rupiah)", "20 Desember 2014 s/d 19 Desember 2015");
	}
	
	public static void generateSertifikatPaBsmV2(String no_sertifikat, String no_polis_induk, String kode_plan, String kode_sub_plan, String nama_plan, String up, String premi, ElionsManager elionsManager) throws IOException, DocumentException, ParseException {
		kode_plan = FormatString.rpad("0", kode_plan, 3);
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
		BigDecimal bdUp = new BigDecimal(up);
		up = "Rp. " + nf.format(bdUp) + ",-";
		BigDecimal bdPremi = new BigDecimal(premi);
		premi = "Rp. " + nf.format(bdPremi) + ",-";
		//String templatePath = "\\\\ebserver\\pdfind\\Template\\Sertifikat_PA_ABD_TMP_73_14.pdf";// PA ABD
		String templatePath = "\\\\ebserver\\pdfind\\Template\\Polis_PA_Bank_Sinarmas_20190314_v3.pdf";// PA ABD
	    //DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    DateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");   
								
		if("13".equals(kode_sub_plan))
			templatePath = "\\\\ebserver\\pdfind\\Template\\Sertifikat_PA_AB_TMP_73_13.pdf";// PA AB
		
		PdfReader pdfReader = new PdfReader(templatePath);
		String outputName = pdfPolisPath + "\\bsm\\73\\" + no_polis_induk + "-" + kode_plan + "-" + no_sertifikat + ".pdf";
		File file = new File(outputName);
		
    	HashMap mapPasSms = elionsManager.getUwDao().selectMstPasSmsFromNoKartu(no_sertifikat);	
	    String mspDob = mapPasSms.get("MSP_DATE_OF_BIRTH").toString();	    
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Date dateLahir = sdf.parse(mspDob);
		Date dateNow = new Date();
		
		//Mark Valentino - Kalender Indonesia
	    Calendar calDob = Calendar.getInstance();
	    Calendar calNow = Calendar.getInstance();	    
	    calDob.setTime(dateLahir);
	    calNow.setTime(new Date());
	    String[] strMonths = new String[]{
	    		  "Januari","Februari","Maret","April","Mei","Juni","Juli",
	    		  "Agustus","September","Oktober","November","Desember"
	    		};
	    
	    int tglLahir = calDob.get(Calendar.DATE); 
	    String bulanLahir = strMonths[calDob.get(Calendar.MONTH)];
	    int bulanLahirv2 = dateLahir.getMonth() + 1;
	    int tahunLahir = calDob.get(Calendar.YEAR); 
	    
	    int tglLnow = calNow.get(Calendar.DATE);    
	    String bulanNow = strMonths[calNow.get(Calendar.MONTH)];
	    int bulanNowv2 = dateNow.getMonth() + 1;	    
	    int tahunNow = calNow.get(Calendar.YEAR);
	    
	    //Mark Valentino - Akurasi umur
	    int umur = tahunNow - tahunLahir;
	    // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
	    if (((tahunLahir - tahunNow) > 3) || (bulanLahirv2 > bulanNowv2)){
	        umur--;
	    // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
	    }else if ((bulanLahirv2 == bulanNowv2) && (bulanLahirv2 > bulanNowv2)){
	        umur--;
	    }
	    
	    //Split Nama Lengkap yang panjang
	    String namaLengkap = mapPasSms.get("MSP_FULL_NAME").toString();
	    String namaBaris1 = "", namaBaris2 = "";
	    int panjangNama = namaLengkap.length();
	    int panjangNamaBaris1 = 0;
	    int panjangNamaBaris2 = 0;	    
	    String[] tempName = namaLengkap.split(" ");
	    for(String tmpName : tempName){
    		if((panjangNamaBaris1 + tmpName.length()) < 25){
	    		namaBaris1 = namaBaris1 + tmpName;
	    		if(namaBaris1.length() > 0){
	    			namaBaris1 = namaBaris1 + " ";
	    		}
	    		panjangNamaBaris1 = panjangNamaBaris1 + tmpName.length();	    			
    		}else{
	    		namaBaris2 = namaBaris2 + tmpName;
	    		if(namaBaris2.length() > 0){
	    			namaBaris2 = namaBaris2 + " ";
	    		}	    		
	    		panjangNamaBaris2 = panjangNamaBaris2 + tmpName.length();	    		
	    	}
	    }
	    
	    //Mark Valentino - Split alamat panjang (max 100char)
        String alamatFull = mapPasSms.get("MSP_ADDRESS_1").toString();
        //String[] arrayAlamat = alamatFull.split("\\s+");
        int panjangAlamat = alamatFull.length();
        if (panjangAlamat >= 100){
            alamatFull = alamatFull.substring(0, 100);
            panjangAlamat = alamatFull.length();            
        }
        String alamatBaris1 = "";
        String alamatBaris2 = "";
        String alamatBaris3 = "";  
        String alamatBaris4 = "";          
        if(panjangAlamat <= 25){
            alamatBaris1 = alamatFull.substring(0, panjangAlamat);
        }else if((panjangAlamat > 25) && (panjangAlamat <= 50)){
            alamatBaris1 = alamatFull.substring(0, 25);
            alamatBaris2 = alamatFull.substring(25, panjangAlamat);
            if(alamatBaris2.substring(0, 1).contains(" ")){
                alamatBaris2 = alamatFull.substring(26, panjangAlamat);
            }                    
        }else if((panjangAlamat > 50) && (panjangAlamat < 75)){
            alamatBaris1 = alamatFull.substring(0, 25);
            alamatBaris2 = alamatFull.substring(25, 50);
            alamatBaris3 = alamatFull.substring(50, panjangAlamat);
            if(alamatBaris2.substring(0, 1).contains(" ")){
                alamatBaris2 = alamatFull.substring(26, 50);
            }    
            if(alamatBaris3.substring(0, 1).contains(" ")){
            	alamatBaris3 = alamatFull.substring(51, panjangAlamat);
            }	
        }else if((panjangAlamat > 75)){
            alamatBaris1 = alamatFull.substring(0, 25);
            alamatBaris2 = alamatFull.substring(25, 51);
            alamatBaris3 = alamatFull.substring(51, 75);            
            alamatBaris4 = alamatFull.substring(75, (panjangAlamat));
            if(alamatBaris2.substring(0, 1).contains(" ")){
                alamatBaris2 = alamatFull.substring(26, 51);
            }    
            if(alamatBaris3.substring(0, 1).contains(" ")){
            	alamatBaris3 = alamatFull.substring(52, 75);
            }
            if(alamatBaris4.substring(0, 1).contains(" ")){
            	alamatBaris4 = alamatFull.substring(76, (panjangAlamat));
            }             
        }
		
		PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(file));
		PdfContentByte cb = pdfStamper.getOverContent(1);
//		cb.beginText();
//		BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);		
//      cb.setFontAndSize(bf, 10);
        
        // Logo Sinarmas MSIG        
        //PdfContentByte cb = pdfStamper.getOverContent(1);
        Image image = Image.getInstance(Resources.getResourceURL(props.getProperty("images.logo.sinarmas")));
        //cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Sertifikat", 29, 722, 0); 
        //image.setAbsolutePosition(29, pdfReader.getPageSize(1).getHeight() - 50);
        image.setAbsolutePosition(27, 802);        
        image.scaleToFit(150, 69);
        cb.addImage(image);
        
        // JUDUL SERTIFIKAT
		BaseFont bf2 = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);        
        cb.beginText();
        cb.setFontAndSize(bf2, 12);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "POLIS PERSONAL ACCIDENT", 205, 774, 0); 
        cb.endText();

		cb.beginText();
		BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);		
		cb.setFontAndSize(bf, 10);
        
        // No Sertifikat
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Sertifikat", 29, 742, 0);        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 742, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, no_sertifikat, 110, 742, 0);        
        
        // Nama Peserta
	     cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama ", 29, 728, 0);
	     cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Peserta", 29, 716, 0);	     
	     cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 723, 0);
        if(panjangNama <= 25){
        	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, namaLengkap, 110, 723, 0);
        }else{    
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, namaBaris1, 110, 727, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, namaBaris2, 110, 716, 0);        	
        }
        
        // Tgl Lahir / Umur
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl. Lahir", 29, 702, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "/ Umur", 29, 690, 0);        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 700, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, tglLahir + " " + bulanLahir + " " + tahunLahir + " / " + umur + " Tahun", 110, 700, 0);        
        
        // Alamat    
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 29, 677, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 677, 0);
        if(panjangAlamat <= 25){
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT,  alamatBaris1 + ",", 110, 677, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, mapPasSms.get("MSP_CITY").toString(), 110, 666, 0);
        }else if(alamatFull.length() > 25){
        	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris1 + "-", 110, 677, 0);
            if((alamatBaris2.length() > 0) && (panjangAlamat <= 50)){
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris2 + ",", 110, 666, 0);
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, mapPasSms.get("MSP_CITY").toString(), 110, 655, 0);            	
            }else if((alamatBaris3.length() > 0) && (panjangAlamat <= 75)){
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris2 + "-", 110, 666, 0);
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris3 + ",", 110, 655, 0);            	
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, mapPasSms.get("MSP_CITY").toString(), 110, 644, 0);               	
            }else if((alamatBaris4.length() > 0) && (panjangAlamat > 75)){
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris2 + "-", 110, 666, 0);
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris3 + "-", 110, 655, 0);
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris4 + ",", 110, 644, 0);               	
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, mapPasSms.get("MSP_CITY").toString(), 110, 633, 0);
            }	
        }
        
        // Pilihan Manfaat
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pilihan Manfaat", 282, 742, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 742, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Personal Accident Risiko ABD", 392, 742, 0);
        
        // Pertanggungan
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang", 282, 729, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pertanggungan", 282, 718, 0);        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 723, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, up , 392, 723, 0);
        
        // Premi
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Premi", 282, 704, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 704, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, premi + " per TAHUN", 392, 704, 0);
        
        // Tanggal Mulai Pertanggungan
	    Calendar calBegDate = Calendar.getInstance();
	    String mspBegDate = mapPasSms.get("MSP_PAS_BEG_DATE").toString();
		Date dateBegDate = sdf.parse(mspBegDate);
	    calBegDate.setTime(dateBegDate);
	    
	    int tglBegDate = calBegDate.get(Calendar.DATE);
	    String bulanBegDate = strMonths[calBegDate.get(Calendar.MONTH)];    
	    int tahunBegDate = calBegDate.get(Calendar.YEAR);        
	    
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tanggal Mulai", 282, 690, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pertanggungan", 282, 679, 0);        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 685, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, tglBegDate + " " + bulanBegDate + " " + tahunBegDate, 392, 685, 0);
        //(PdfContentByte.ALIGN_LEFT, tglLnow + " " + bulanNow + " " + tahunNow, 431, 640, 0);
        
        // Masa Asuransi
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 282, 664, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 664, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang", 392, 664, 0);
        
        // Tanggal
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, tglLnow + " " + bulanNow + " " + tahunNow, 431, 640, 0);
        
        cb.endText();
        if(pdfStamper != null)
        	pdfStamper.close();
        if(pdfReader != null)
        	pdfReader.close();
        
        //Jika sudah submit/inforce, PDF Sertifikat ditaruh ke EBSERVER (viewer)
		String spaj = mapPasSms.get("REG_SPAJ").toString();
		if(!spaj.isEmpty()){
			try{
				String cabang = elionsManager.selectCabangFromSpaj(spaj);        
				String path ="";
				path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				File copySertifikat = new File(path + "\\" + "SERTIFIKAT_PA_BSM.pdf");
				
				File dirPath = new File(path);
				if(dirPath.exists()){
					FileUtil.copyfile(file.toString(), copySertifikat.toString());
				}else{
					dirPath.mkdirs();
					FileUtil.copyfile(file.toString(), copySertifikat.toString());
				}				
			}catch(Exception e){
				logger.error("ERROR :", e);
			}
		}		
		
	}
	
	public static String generateSertifikatPaBsmSyariah(String no_sertifikat,String reg_spaj, String no_polis_induk, String kode_plan, Integer kode_sub_plan_data, String up, String premi, ElionsManager elionsManager, Properties props,BacManager manager, int cara_bayar) throws IOException, DocumentException, ParseException {
		String kode_sub_plan = Integer.toString(kode_sub_plan_data);
		kode_plan = FormatString.rpad("0", kode_plan, 3);
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
		BigDecimal bdUp = new BigDecimal("0");
		up = "Rp. " + nf.format(bdUp) + ",-";
		BigDecimal bdPremi = new BigDecimal("0");
		premi = "Rp. " + nf.format(bdPremi) + ",-";
	
		HashMap m = (HashMap) manager.select_det_bsim_syariah(reg_spaj);
		HashMap mp = (HashMap) manager.select_det_prod(kode_plan,kode_sub_plan_data);
		HashMap mpr = (HashMap) elionsManager.selectDataUsulanDetail(reg_spaj);
		
		no_sertifikat = elionsManager.selectPolicyNumberFromSpaj(reg_spaj);
		no_polis_induk = no_sertifikat;
	
		if(mpr != null){
			if(mpr.size()>0){
				bdPremi = new BigDecimal((Double)mpr.get("MSPR_PREMIUM"));
				premi = "Rp. " + nf.format(bdPremi) + ",-";
				bdUp = new BigDecimal((Double)mpr.get("MSPR_TSI"));
				up = "Rp. " + nf.format(bdUp) + ",-";
			}
		};
		
		String templatePath = props.getProperty("pdf.dir.bsim.syariah.template");
		DateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");   
		PdfReader pdfReader = new PdfReader(templatePath);
	
		String outputName = pdfPolisPath + "\\bsm\\205\\" + no_polis_induk + "-" + kode_plan + "-" + no_sertifikat + ".pdf";
		File file = new File(outputName);
		Date date = new Date();
		Timestamp ts = (Timestamp) m.get("DOB");
		date.setTime(ts.getTime());
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
		String mspDob = formattedDate;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dateLahir = sdf.parse(mspDob);
		Date dateNow = new Date();
		
	    Calendar calDob = Calendar.getInstance();
	    Calendar calNow = Calendar.getInstance();	    
	    calDob.setTime(dateLahir);
	    calNow.setTime(new Date());
	    String[] strMonths = new String[]{
	    		  "Januari","Februari","Maret","April","Mei","Juni","Juli",
	    		  "Agustus","September","Oktober","November","Desember"
	    		};
	    
	    int tglLahir = calDob.get(Calendar.DATE); 
	    String bulanLahir = strMonths[calDob.get(Calendar.MONTH)];
	    int bulanLahirv2 = dateLahir.getMonth() + 1;
	    int tahunLahir = calDob.get(Calendar.YEAR); 
	    
	    int tglLnow = calNow.get(Calendar.DATE);    
	    String bulanNow = strMonths[calNow.get(Calendar.MONTH)];
	    int bulanNowv2 = dateNow.getMonth() + 1;	    
	    int tahunNow = calNow.get(Calendar.YEAR);
	    
		 int umur = tahunNow - tahunLahir;
	    if (((tahunLahir - tahunNow) > 3) || (bulanLahirv2 > bulanNowv2)){
	        umur--;
	    }else if ((bulanLahirv2 == bulanNowv2) && (bulanLahirv2 > bulanNowv2)){
	        umur--;
	    }
	    String namaLengkap = (String)m.get("FULL_NAME");
	    String namaBaris1 = "", namaBaris2 = "";
	    int panjangNama = namaLengkap.length();
	    int panjangNamaBaris1 = 0;
	    int panjangNamaBaris2 = 0;	    
	    String[] tempName = namaLengkap.split(" ");
	    for(String tmpName : tempName){
    		if((panjangNamaBaris1 + tmpName.length()) < 25){
	    		namaBaris1 = namaBaris1 + tmpName;
	    		if(namaBaris1.length() > 0){
	    			namaBaris1 = namaBaris1 + " ";
	    		}
	    		panjangNamaBaris1 = panjangNamaBaris1 + tmpName.length();	    			
    		}else{
	    		namaBaris2 = namaBaris2 + tmpName;
	    		if(namaBaris2.length() > 0){
	    			namaBaris2 = namaBaris2 + " ";
	    		}	    		
	    		panjangNamaBaris2 = panjangNamaBaris2 + tmpName.length();	    		
	    	}
	    }
			
	    String alamatFull = (String)m.get("ALAMAT_RUMAH");
		
	    
	    
	    int panjangAlamat = alamatFull.length();
        if (panjangAlamat >= 100){
            alamatFull = alamatFull.substring(0, 100);
            panjangAlamat = alamatFull.length();            
        }
        String alamatBaris1 = "";
        String alamatBaris2 = "";
        String alamatBaris3 = "";  
        String alamatBaris4 = "";          
        if(panjangAlamat <= 25){
            alamatBaris1 = alamatFull.substring(0, panjangAlamat);
        }else if((panjangAlamat > 25) && (panjangAlamat <= 50)){
            alamatBaris1 = alamatFull.substring(0, 25);
            alamatBaris2 = alamatFull.substring(25, panjangAlamat);
            if(alamatBaris2.substring(0, 1).contains(" ")){
                alamatBaris2 = alamatFull.substring(26, panjangAlamat);
            }                    
        }else if((panjangAlamat > 50) && (panjangAlamat < 75)){
            alamatBaris1 = alamatFull.substring(0, 25);
            alamatBaris2 = alamatFull.substring(25, 50);
            alamatBaris3 = alamatFull.substring(50, panjangAlamat);
            if(alamatBaris2.substring(0, 1).contains(" ")){
                alamatBaris2 = alamatFull.substring(26, 50);
            }    
            if(alamatBaris3.substring(0, 1).contains(" ")){
            	alamatBaris3 = alamatFull.substring(51, panjangAlamat);
            }	
        }else if((panjangAlamat > 75)){
            alamatBaris1 = alamatFull.substring(0, 25);
            alamatBaris2 = alamatFull.substring(25, 51);
            alamatBaris3 = alamatFull.substring(51, 76);            
            alamatBaris4 = alamatFull.substring(76, (panjangAlamat));
            if(alamatBaris2.substring(0, 1).contains(" ")){
                alamatBaris2 = alamatFull.substring(26, 51);
            }    
            if(alamatBaris3.substring(0, 1).contains(" ")){
            	alamatBaris3 = alamatFull.substring(52, 76);
            }
            if(alamatBaris4.substring(0, 1).contains(" ")){
            	alamatBaris4 = alamatFull.substring(77, (panjangAlamat));
            }             
        }
		
		PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(file));
		PdfContentByte cb = pdfStamper.getOverContent(1);
		cb.beginText();
		BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.setFontAndSize(bf, 10);
        
        // No Sertifikat
        int tambahy=-20;
        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Sertifikat", 29, 722+tambahy, 0);        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 722+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, no_sertifikat, 110, 722+tambahy, 0);        
        
        // Nama Peserta
	     cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama ", 29, 708+tambahy, 0);
	     cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Peserta", 29, 696+tambahy, 0);	     
	     cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 703+tambahy, 0);
        if(panjangNama <= 25){
        	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, namaLengkap, 110, 703+tambahy, 0);
        }else{    
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, namaBaris1, 110, 707+tambahy, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, namaBaris2, 110, 696+tambahy, 0);        	
        }
        
        // Tgl Lahir / Umur
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl. Lahir", 29, 682+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "/ Umur", 29, 670+tambahy, 0);        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 680+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, tglLahir + " " + bulanLahir + " " + tahunLahir + " / " + umur + " Tahun", 110, 680+tambahy, 0);        
        
        
        // Tgl Mulai Syariah
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tanggal Mulai", 29, 657+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asuransi", 29, 645+tambahy, 0);      
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Syariah *)", 29, 634+tambahy, 0);      
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 657+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, tglLahir + " " + bulanLahir + " " + tahunLahir,110 , 657+tambahy, 0);        
        
        
        
        // Tgl Mulai Syariah
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 29, 622+tambahy, 0);     
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 100, 622+tambahy, 0);
    
        if(panjangAlamat <= 25){
	  		cb.showTextAligned(PdfContentByte.ALIGN_LEFT,  alamatBaris1 + ",", 110, 622+tambahy, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String)m.get("KOTA"), 110, 599+tambahy, 0);
        }else if(alamatFull.length() > 25){
        	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris1 + "-", 110, 657+tambahy, 0);
            if((alamatBaris2.length() > 0) && (panjangAlamat <= 50)){
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris2 + ",", 110, 646+tambahy, 0);
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String)m.get("KOTA"), 110, 635+tambahy, 0);            	
            }else if((alamatBaris3.length() > 0) && (panjangAlamat <= 75)){
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris2 + "-", 110, 646+tambahy, 0);
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris3 + ",", 110, 635+tambahy, 0);            	
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT,  (String)m.get("KOTA"), 110, 624+tambahy, 0);               	
            }else if((alamatBaris4.length() > 0) && (panjangAlamat > 75)){
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris2 + "-", 110, 646+tambahy, 0);
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris3 + "-", 110, 635+tambahy, 0);
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamatBaris4 + ",", 110, 624+tambahy, 0);               	
            	cb.showTextAligned(PdfContentByte.ALIGN_LEFT,  (String)m.get("KOTA"), 110, 613+tambahy, 0);
            }	
        }
        
        // Pilihan Manfaat
        String bsimSyariah = (String)mp.get("LSDBS_NAME");
        if(bsimSyariah.toUpperCase().contains("(BSIM)")){
        	bsimSyariah = bsimSyariah.replace("(BSIM)", "");
        	bsimSyariah = bsimSyariah.trim();
        }
        
        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pilihan Paket", 282, 722+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 722+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,bsimSyariah , 392, 722+tambahy, 0);
        
        // Pertanggungan
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Manfaat", 282, 707+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asuransi Jiwa Syariah", 282, 692+tambahy, 0);        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 699+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, up , 392, 699+tambahy, 0);
        
        // Premi
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Kontribusi", 282, 675+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 675+tambahy, 0);
        
        StringBuffer buffer = new StringBuffer();
   
        buffer.append(premi);
        if(cara_bayar == 3){
        	buffer.append(" per tahun");
        }else if(cara_bayar == 2){
        	buffer.append(" per semester");
        }else if(cara_bayar == 1){
        	buffer.append(" per triwulan");
        }else if(cara_bayar == 6){
        	buffer.append(" per bulan");
        }
        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, buffer.toString() , 392, 675+tambahy, 0);
        
        // Masa Asuransi
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 282, 660+tambahy, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 382, 660, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang", 392, 660+tambahy, 0);
        
     // Tanggal
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, tglLnow + " " + bulanNow + " " + tahunNow, 383, 597+tambahy, 0);
       
        cb.endText();
        if(pdfStamper != null)
        	pdfStamper.close();
        if(pdfReader != null)
        	pdfReader.close();
        
        //Jika sudah submit/inforce, PDF Sertifikat ditaruh ke EBSERVER (viewer)
		String spaj = reg_spaj;
		if(!spaj.isEmpty()){
			try{
				String cabang = (String)m.get("LCA_ID");
				String path ="";
				path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				File copySertifikat = new File(path + "\\" + "SERTIFIKAT_PA_BSM.pdf");
				
				File dirPath = new File(path);
				if(dirPath.exists()){
					FileUtil.copyfile(file.toString(), copySertifikat.toString());
				}else{
					dirPath.mkdirs();
					FileUtil.copyfile(file.toString(), copySertifikat.toString());
				}				
			}catch(Exception e){
				
			}
		}		
        
        
		return outputName;
	}
		
	
	public static void generateSertifikatFreeDbdDmtm(String no_sertifikat, String no_polis_induk, String nama_pp, String nama_tt, Date tgl_lahir, Date tgl_sekarang, String product_code, String up_text, String insper_text) throws IOException, DocumentException{
        String pathFile = "\\\\ebserver\\pdfind\\Template\\sertifikat_freedbd_bank_dki.pdf";
        String outputName = pdfPolisPath + "\\free_dbd\\"+no_sertifikat+".pdf";
        
        if("061".equals(product_code)) {
            outputName = pdfPolisPath + "\\free_dbd\\061\\"+no_sertifikat+".pdf";
        }

        PdfReader reader = new PdfReader(pathFile);
        File file = new File(outputName);
        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
        PdfContentByte cb = stamp.getOverContent(1);
        cb.beginText();
        BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\TIMESBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.setFontAndSize(bf, 18);
        int x = 0;
        int y = 0;
        //ini bagian judul
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "S E R T I F I K A T", 299, 660, 0); 
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "ASURANSI DEMAM BERDARAH", 296, 640, 0);
        
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        
        //Ini bagian detail body
        bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\TIMES.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.setFontAndSize(bf, 10);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No Sertifikat", 50, 600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, no_sertifikat, 240, 600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Berdasarkan pada Polis Induk No", 50, 585, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 585, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, no_polis_induk, 240, 585, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Atas Nama Pemegang Polis", 50, 570, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 570, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_pp, 240, 570, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Bersama ini Dinyatakan Bahwa", 50, 555, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 555, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, nama_tt, 240, 555, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tanggal Lahir", 50, 540, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 540, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, df1.format(tgl_lahir), 240, 540, 0);
        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Adalah Peserta Asuransi Kumpulan, dengan :", 50, 510, 0);
        
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jenis Asuransi", 50, 480, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 480, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asuransi Kesehatan Individu \"Asuransi Demam Berdarah\"", 240, 480, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 50, 465, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 465, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, up_text, 240, 465, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Premi", 50, 450, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ": ", 230, 450, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Gratis", 240, 450, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 50, 435, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 230, 435, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, insper_text, 240, 435, 0);
        
        String[] wording = pecahParagrafLineBreaksInclude("Sertifikat ini diterbitkan untuk Peserta sebagai bukti dari penutupan Asuransi Kesehatan Demam Berdarah Kumpulan antara PT Asuransi Jiwa Sinarmas MSIG Tbk. dan pemegang polis. Sertifikat ini tunduk pada Syarat-syarat Umum, Syarat-syarat Khusus dan Tambahan lain (jika ada) yang dilekatkan dan merupakan bagian mutlak yang tidak dapat dipisah-pisahkan dari Polis Induk.", 48);
        for(int i=0; i<wording.length; i++) {
            int addY = 15 * i;
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, wording[i] ,          50, 395-addY, 0);
        }
        
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Jakarta, "+ df2.format(tgl_sekarang), 440, 395, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "PT Asuransi Jiwa Sinarmas MSIG Tbk.", 440, 380, 0);
        
        String path = Resources.getResourceURL(props.getProperty("images.ttd.direksi")).getPath();
        Image img = Image.getInstance(path);
        
        img.setAbsolutePosition(380, 340);      
        img.scaleAbsolute(120, 34);
        cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 340);
        
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Hidenori Kui", 440, 325, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "__________", 440, 324, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Wakil Presiden Direktur", 440, 310, 0);
        
        cb.endText();
        
        if(stamp!=null){
            stamp.close();
        }
        if(reader!=null){
            reader.close();
        }
        
    }
	
	public static String[] pecahParagrafLineBreaksInclude(String string, int max) {
		StringBuffer tmp = new StringBuffer();
		StringBuffer hasil = new StringBuffer();
		
		String []splitLines=string.split("\n");
		int item=0;
		for (int i = 0; i < splitLines.length; i++) {
			String splitLine=splitLines[i];
			
			String[] katakata = splitLine.split(" ");
			for(String kata : katakata) {			
				if(tmp.length()+kata.length()>max || i!=item) {
					hasil.append(tmp.toString().trim());
					hasil.append("~");
					tmp = new StringBuffer();
					item=i;
				}
				tmp.append(kata + " ");
			}
			
		}
		
		
		if(tmp.toString().length() > 0) hasil.append(tmp);
		
		return hasil.toString().trim().split("~");
	}
}
