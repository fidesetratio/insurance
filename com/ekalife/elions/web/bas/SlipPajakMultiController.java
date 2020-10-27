package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.parent.ParentMultiController;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * Form untuk print Slip Pajak di E-Lions
 * 
 * @author yusuf
 * @since Oct 6, 2009 (10:47:59 AM)
 */
public class SlipPajakMultiController extends ParentMultiController{
	
	/**
	 * Halaman Utama (outer frame nya)
	 * 
	 * @author yusuf
	 * @since Oct 6, 2009 (10:48:49 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * http://localhost/E-Lions/bas/slip_pajak.htm?window=main
	 */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map cmd 			= new HashMap();
		Date sysdate 		= elionsManager.selectSysdate(0);
		DateFormat df1 		= new SimpleDateFormat("yyyy");
		DateFormat df2 		= new SimpleDateFormat("MM");
		int currentYear 	= Integer.parseInt(df1.format(sysdate));
		int currentMonth 	= Integer.parseInt(df2.format(sysdate));
		
		//daftar bulan untuk dropdown
		List<DropDown> daftarBulan = new ArrayList<DropDown>();
		daftarBulan.add(new DropDown("01", "Januari"));
		daftarBulan.add(new DropDown("02", "Februari"));
		daftarBulan.add(new DropDown("03", "Maret"));
		daftarBulan.add(new DropDown("04", "April"));
		daftarBulan.add(new DropDown("05", "Mei"));
		daftarBulan.add(new DropDown("06", "Juni"));
		daftarBulan.add(new DropDown("07", "Juli"));
		daftarBulan.add(new DropDown("08", "Agustus"));
		daftarBulan.add(new DropDown("09", "September"));
		daftarBulan.add(new DropDown("10", "Oktober"));
		daftarBulan.add(new DropDown("11", "November"));
		daftarBulan.add(new DropDown("12", "Desember"));
		cmd.put("daftarBulan", daftarBulan);
		
		//daftar tahun untuk dropdown (15 tahun terakhir saja)
		List<Integer> daftarTahun = new ArrayList<Integer>();
		for(int i=0; i<15; i++){
			daftarTahun.add(currentYear-i);
		}
		cmd.put("daftarTahun", daftarTahun);

		//nilai default untuk diset di dropdown
		cmd.put("currentYear", currentYear);
		cmd.put("currentMonth", currentMonth);
		
		return new ModelAndView("bas/slip_pajak_main", cmd);
	}
	
	/**
	 * Window untuk menampilkan PDF nya
	 * 
	 * @author yusuf
	 * @since Oct 6, 2009 (11:17:24 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView pdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bulan 	= ServletRequestUtils.getRequiredStringParameter(request, "bulan");
		String tahun 	= ServletRequestUtils.getRequiredStringParameter(request, "tahun");
		String msag_id 	= ServletRequestUtils.getStringParameter(request, "msag_id", "");
		String tgl;
		
		
		if((bulan.equals("01"))||(bulan.equals("03"))||(bulan.equals("05"))||(bulan.equals("07"))||(bulan.equals("08"))||(bulan.equals(10))||(bulan.equals("12"))){
			tgl="31";
		}else if((bulan.equals("04"))||(bulan.equals("06"))||(bulan.equals("09"))||(bulan.equals("11"))){
			tgl="30";
		}else{
			tgl="28";
		}
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		ServletOutputStream os = response.getOutputStream();
		//DELETE THIS!!!
		//msag_id = "016431";
		//bulan 	= "07";
		//tahun	= "2009";
		
		//validasi agen
		if(!msag_id.equals("")){
			if(!currentUser.getLde_id().equals("01") &&!currentUser.getLde_id().equals("05") && !currentUser.getLde_id().equals("29") && uwManager.selectAksesAdminTerhadapAgen(currentUser.getLus_id(), msag_id) == 0 
					&& !currentUser.getLus_id().equals("574")&& !currentUser.getLus_id().equals("696") && !currentUser.getLus_id().equals("2664")){
				
				os.print("<script>alert('Maaf, tetapi anda tidak bisa mengakses file ini.');</script>");
				os.close();
				return null;
			}
		}
		
		//tarik datanya
		List<Map> daftarSlipPajak;
		if(!msag_id.equals("")){
			//himmia - jan-oct 2009, gunakan MST_AGENT_TAX_REV
			
			if(tahun.equals("2009") && Integer.parseInt(bulan) < 11){
				daftarSlipPajak = uwManager.selectSlipPajakPerAgenTaxRev(msag_id, tahun + bulan);
//				daftarSlipPajak = uwManager.selectSlipPajakPerAgen(msag_id, tahun + bulan);
			}else{
				//Deddy (2 Feb 2016) Req Wenny - ambil dari table mst_tax_all
				daftarSlipPajak = uwManager.selectSlipPajakPerAgen(msag_id, tahun + bulan);
			}
		}else{
			//himmia - jan-oct 2009, gunakan MST_AGENT_TAX_REV
			if(tahun.equals("2009") && Integer.parseInt(bulan) < 11){
//				daftarSlipPajak = uwManager.selectSlipPajakPerAdminCabangTaxRev(Integer.valueOf(currentUser.getLus_id()), tahun + bulan);
				daftarSlipPajak = uwManager.selectSlipPajakPerAdminCabang(Integer.valueOf(currentUser.getLus_id()), tahun + bulan);
			}else{
				//Deddy (2 Feb 2016) Req Wenny - ambil dari table mst_tax_all
				daftarSlipPajak = uwManager.selectSlipPajakPerAdminCabang(Integer.valueOf(currentUser.getLus_id()), tahun + bulan);
			}
		}

		if(daftarSlipPajak.isEmpty()) {
			
			os.print("<script>alert('Data tidak ada.');</script>");
			os.close();
			return null;
			
		}
		
		//set response header, agar tampil pdf nya inline di browser
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline;filename=file.pdf");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		
		Integer thn = Integer.parseInt(tahun);
		if(thn>=2014){
			//tarik softcopy blankonya
			PdfReader reader = new PdfReader(props.getProperty("pdf.dir.pajak") + "\\PPH21 2014.pdf");
			PdfStamper stamp = new PdfStamper(reader,os);
			
			//start looping untuk setiap datanya, 1 row = 1 lembar
			for(int i=0; i<daftarSlipPajak.size(); i++){
				Map slip = daftarSlipPajak.get(i);
				
				//tambah 1 halaman setiap kali looping (kecuali halaman pertama gak usah)
				if(i>0){
					stamp.insertPage(i+1, reader.getPageSize(1)); // atau A4
	    			PdfContentByte under = stamp.getUnderContent(i+1);
	    			under.addTemplate(stamp.getImportedPage(reader, 1), 1, 0, 0, 1, 0, 0);
				}
				
		        PdfContentByte over;
		        BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED);
		        over = stamp.getOverContent(i+1);
		        
		    	over.beginText();
		    	over.setFontAndSize(bf, 10); //set ukuran font
				
		    	//start inserting content
		    	String region 			= (String) slip.get("LAR_ADMIN") + " (" + (String) slip.get("LAR_NAMA") + ")";
		    	String tglBayar			= "Tgl Bayar: " + (String) slip.get("PAY_DATE");
		    	String nomorKomisi 		= (String) slip.get("MSCO_NO");
		    	String npwpAgen			= (String) slip.get("MSAG_NPWP");
		    	String namaAgen			= (String) slip.get("NAMA_AGEN");
		    	String alamat 			= (String) slip.get("ALAMAT");
		    	String ktp				= (String) slip.get("MSPE_NO_IDENTITY");
	
		    	DecimalFormat duit 		= new DecimalFormat("#,##0;(#,##0)");
		    	DecimalFormat currency 	= new DecimalFormat("#,##0.00;(#,##0.00)");
		    	double nominalKomisi	= FormatNumber.round(((BigDecimal) slip.get("MSCO_COMM")).doubleValue(), 0);
		    	String komisi			= duit.format(nominalKomisi);
		    	double nominalPajak		= FormatNumber.round(((BigDecimal) slip.get("MSCO_TAX")).doubleValue(), 0);
		    	String pajak			= duit.format(nominalPajak);
		    	
		    	//sistem koordinat : dari kiri, lalu dari bawah
		    	if(region != null) 		over.showTextAligned(Element.ALIGN_RIGHT, 	region, 		535, 809, 0); //region dan admin
		    	if(tglBayar != null) 	over.showTextAligned(Element.ALIGN_RIGHT, 	tglBayar, 		535, 799, 0); //tanggal bayar komisi (bulan tahun)
		    	if(nomorKomisi != null) over.showTextAligned(Element.ALIGN_LEFT, 	nomorKomisi, 	255, 825, 0); //nomor slip pajak
		    	
		    	if(npwpAgen != null){ //npwp agen
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 0)), 78, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 1)), 84, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf("-"), 94, 784, 0);//garis -
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 2)), 104, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 3)), 110, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 4)), 116, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf("-"), 126, 784, 0);//garis -
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 5)), 136, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 6)), 142, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 7)), 148, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf("-"), 158, 784, 0);//garis -
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 8)), 168, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf("-"), 178, 784, 0);//garis -
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 9)), 188, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(10)), 194, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(11)), 200, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf("-"), 210, 784, 0);//garis -
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(12)), 220, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(13)), 226, 784, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(14)), 232, 784, 0);
		    	}
	
		    	if(namaAgen != null) 	over.showTextAligned(Element.ALIGN_LEFT, 	namaAgen, 		78, 765, 0); //nama agen
		    	if(alamat	!= null) 	over.showTextAligned(Element.ALIGN_LEFT, 	alamat, 		78, 750, 0); //alamat agen
		    	if(ktp		!= null)	over.showTextAligned(Element.ALIGN_LEFT, 	ktp, 		410, 784, 0); //ktp agen
	//	    	if(komisi	!= null) 	{
	//	    		over.showTextAligned(Element.ALIGN_RIGHT, 	komisi, 385, 312, 0); //jml komisi
	//	    		over.showTextAligned(Element.ALIGN_RIGHT, 	komisi, 385, 238, 0); //jml komisi
	//	    	}
		    	
		    	over.showTextAligned(Element.ALIGN_RIGHT, 	"21-100-05", 110, 613, 0); //kode pajak
		    	
		    	if(komisi	!= null) 	{
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	komisi.replace(",", ".") , 225, 613, 0); //jml komisi
		    		//over.showTextAligned(Element.ALIGN_RIGHT, 	komisi, 385, 613, 0); //jml komisi (total di hide dulu)
		    	}
		    	if(pajak	!= null) 	{
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	pajak.replace(",", "."), 	530, 613, 0); //jml pajak
		    		//over.showTextAligned(Element.ALIGN_RIGHT, 	pajak, 	526, 238, 0); //jml pajak (total di hide dulu)
		    		//over.showTextAligned(Element.ALIGN_LEFT, 	AngkaTerbilang.indonesian(String.valueOf(nominalPajak)) + "Rupiah", 128, 222, 0); //angka terbilang
		    	}
		    	
		    	//perhitungan dasar pengenaan pajak
		    	Integer msag_kry = ((BigDecimal) slip.get("MSAG_KRY")).intValue();
		    	double kom = FormatNumber.round(((BigDecimal) slip.get("MSCO_COMM")).doubleValue(),0);
	    		double ptkp = FormatNumber.round((((BigDecimal) slip.get("LSPT_VALUE")).doubleValue())/12,0);
	    		double dpp = 0;
	    		double dpp_tampil = 0;
	    		Integer tarif = 0;
		    	if(msag_kry==1){
		    		dpp = kom - ptkp;
		    		dpp_tampil = kom;
		    	}else{
		    		dpp = (kom/2) - ptkp;
		    		dpp_tampil = kom/2;
		    	}
		    	
		    	if(dpp < 0){ // Deddy 12 Feb 2016
		    		dpp=0;
		    	}else{
		    		//perhitungan tarif(%)
		    		tarif = (int) ((nominalPajak/dpp)*100);
		    	}
		    	
		    	
		    	over.showTextAligned(Element.ALIGN_RIGHT, 	duit.format(FormatNumber.round(dpp_tampil,0)).replace(",", "."), 	325, 613, 0); //jml dpp
		    	
		    	//cek npwp
		    	if(npwpAgen==null){
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	"X", 	370, 613, 0); //tanda X npwp
		    		
		    		if(tarif>30){
		    			tarif = 36;
		    		}else if(tarif>18){
		    			tarif = 30;
		    		}else if(tarif>6){
		    			tarif = 18;
		    		}else{
		    			tarif = 6;
		    		}
		    		
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	tarif+"%", 	420, 613, 0); //tarif(%)
		    	}else if(npwpAgen.equals("000000000000000")){
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	"X", 	370, 613, 0); //tanda X npwp
		    		
		    		if(tarif>30){
		    			tarif = 36;
		    		}else if(tarif>18){
		    			tarif = 30;
		    		}else if(tarif>6){
		    			tarif = 18;
		    		}else{
		    			tarif = 6;
		    		}
		    		
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	tarif+"%", 	420, 613, 0); //tarif(%)
		    	}else{
		    		
		    		if(tarif>25){
		    			tarif = 30;
		    		}else if(tarif>15){
		    			tarif = 25;
		    		}else if(tarif>5){
		    			tarif = 15;
		    		}else{
		    			tarif = 5;
		    		}
		    		
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	tarif+"%", 	420, 613, 0); //tarif(%)
		    	}
		    	
		    	over.showTextAligned(Element.ALIGN_LEFT, "Jakarta,", 308, 557, 0); //Jakarta,
		    	
		    	Date now = elionsManager.selectSysdate();
		    	String now2 = FormatDate.toIndonesian(now);
		    	String sbulan=null;			
				if(bulan.equals("01")){sbulan="Januari";}
				else if(bulan.equals("02")){sbulan="Februari";}
				else if(bulan.equals("03")){sbulan="Maret";}
				else if(bulan.equals("04")){sbulan="April";}
				else if(bulan.equals("05")){sbulan="Mei";}
				else if(bulan.equals("06")){sbulan="Juni";}
				else if(bulan.equals("07")){sbulan="Juli";}
				else if(bulan.equals("08")){sbulan= "Agustus";}
				else if(bulan.equals("09")){sbulan="September";}
				else if(bulan.equals("10")){sbulan="Oktober";}
				else if(bulan.equals("11")){sbulan= "November";}
				else if(bulan.equals("12")){sbulan="Desember";}
				
		    	DateFormat th = new SimpleDateFormat("yy");
		    	over.showTextAligned(Element.ALIGN_LEFT, tgl, 348, 557, 0); //sysdate
		    	over.showTextAligned(Element.ALIGN_LEFT, bulan, 366, 557, 0); //sysdate
		    	over.showTextAligned(Element.ALIGN_LEFT, tahun, 384, 557, 0); //sysdate
		    	
		    	over.showTextAligned(Element.ALIGN_LEFT, "01 - 391 - 150 - 8 - 073 - 000", 78, 575, 0); //npwp AJS
		    	over.showTextAligned(Element.ALIGN_LEFT, "PT ASURANSI JIWA SINARMAS MSIG Tbk.", 78, 557, 0); //nama AJS
	
		    	//over.showTextAligned(Element.ALIGN_LEFT, "ESTER TANTRI SETYANINGSIH", 430, 545, 0); //nama
		    	over.showTextAligned(Element.ALIGN_LEFT, "FURGON WIRAWAN DAMANIK", 430, 545, 0); //nama
		    	
		    	over.endText();
		    	
		    	Image img = Image.getInstance(Resources.getResourceAsFile(props.getProperty("images.ttd.ester").trim()).getAbsolutePath());
		        img.setAbsolutePosition(475, 555);
		        img.scaleAbsolute(50, 29);
		        over.addImage(img);
		    	
		    	
		    	//end of inserting content
			}
			
			stamp.close();
	    	os.close();
		}else{
		
	    	//tarik softcopy blankonya
			PdfReader reader = new PdfReader(props.getProperty("pdf.dir.pajak") + "\\bukti potong pph 21 (25 mei 2009) aligned_nobox.pdf");
	
			PdfStamper stamp = new PdfStamper(reader,os);
			
			//start looping untuk setiap datanya, 1 row = 1 lembar
			for(int i=0; i<daftarSlipPajak.size(); i++){
				Map slip = daftarSlipPajak.get(i);
				
				//tambah 1 halaman setiap kali looping (kecuali halaman pertama gak usah)
				if(i>0){
					stamp.insertPage(i+1, reader.getPageSize(1)); // atau A4
	    			PdfContentByte under = stamp.getUnderContent(i+1);
	    			under.addTemplate(stamp.getImportedPage(reader, 1), 1, 0, 0, 1, 0, 0);
				}
				
		        PdfContentByte over;
		        BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED);
		        over = stamp.getOverContent(i+1);
		        
		    	over.beginText();
		    	over.setFontAndSize(bf, 10); //set ukuran font
				
		    	//start inserting content
		    	String region 			= (String) slip.get("LAR_ADMIN") + " (" + (String) slip.get("LAR_NAMA") + ")";
		    	String tglBayar			= "Tgl Bayar: " + (String) slip.get("PAY_DATE");
		    	String nomorKomisi 		= (String) slip.get("MSCO_NO");
		    	String npwpAgen			= (String) slip.get("MSAG_NPWP");
		    	String namaAgen			= (String) slip.get("NAMA_AGEN");
		    	String alamat 			= (String) slip.get("ALAMAT");
	
		    	DecimalFormat duit 		= new DecimalFormat("#,##0.00;(#,##0.00)");
		    	String komisi			= duit.format((BigDecimal) slip.get("MSCO_COMM"));
		    	int nominalPajak		= ((BigDecimal) slip.get("MSCO_TAX")).intValue();
		    	String pajak			= duit.format(nominalPajak);
		    	
		    	//sistem koordinat : dari kiri, lalu dari bawah
		    	if(region != null) 		over.showTextAligned(Element.ALIGN_RIGHT, 	region, 		535, 703, 0); //region dan admin
		    	if(tglBayar != null) 	over.showTextAligned(Element.ALIGN_RIGHT, 	tglBayar, 		535, 690, 0); //tanggal bayar komisi (bulan tahun)
		    	if(nomorKomisi != null) over.showTextAligned(Element.ALIGN_LEFT, 	nomorKomisi, 	232, 666, 0); //nomor slip pajak
		    	
		    	if(npwpAgen != null){ //npwp agen
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 0)), 209, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 1)), 222, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 2)), 246, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 3)), 259, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 4)), 272, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 5)), 297, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 6)), 310, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 7)), 322, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 8)), 347, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt( 9)), 372, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(10)), 385, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(11)), 398, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(12)), 422, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(13)), 436, 636, 0);
	    	    	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(npwpAgen.charAt(14)), 448, 636, 0);
		    	}
	
		    	if(namaAgen != null) 	over.showTextAligned(Element.ALIGN_LEFT, 	namaAgen, 		204, 618, 0); //nama agen
		    	if(alamat	!= null) 	over.showTextAligned(Element.ALIGN_LEFT, 	alamat, 		204, 600, 0); //alamat agen
	//	    	if(komisi	!= null) 	{
	//	    		over.showTextAligned(Element.ALIGN_RIGHT, 	komisi, 385, 312, 0); //jml komisi
	//	    		over.showTextAligned(Element.ALIGN_RIGHT, 	komisi, 385, 238, 0); //jml komisi
	//	    	}
		    	if(komisi	!= null) 	{
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	komisi, 385, 471, 0); //jml komisi
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	komisi, 385, 238, 0); //jml komisi
		    	}
		    	if(pajak	!= null) 	{
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	pajak, 	526, 471, 0); //jml pajak
		    		over.showTextAligned(Element.ALIGN_RIGHT, 	pajak, 	526, 238, 0); //jml pajak
		    		over.showTextAligned(Element.ALIGN_LEFT, 	AngkaTerbilang.indonesian(String.valueOf(nominalPajak)) + "Rupiah", 128, 222, 0); //angka terbilang
		    	}
		    	
		    	over.showTextAligned(Element.ALIGN_LEFT, "Jakarta", 310, 195, 0); //Jakarta,
		    	
		    	Date now = elionsManager.selectSysdate();
		    	String now2 = FormatDate.toIndonesian(now);
		    	String sbulan=null;			
				if(bulan.equals("01")){sbulan="Januari";}
				else if(bulan.equals("02")){sbulan="Februari";}
				else if(bulan.equals("03")){sbulan="Maret";}
				else if(bulan.equals("04")){sbulan="April";}
				else if(bulan.equals("05")){sbulan="Mei";}
				else if(bulan.equals("06")){sbulan="Juni";}
				else if(bulan.equals("07")){sbulan="Juli";}
				else if(bulan.equals("08")){sbulan= "Agustus";}
				else if(bulan.equals("09")){sbulan="September";}
				else if(bulan.equals("10")){sbulan="Oktober";}
				else if(bulan.equals("11")){sbulan= "November";}
				else if(bulan.equals("12")){sbulan="Desember";}
				
		    	DateFormat th = new SimpleDateFormat("yy");
		    	over.showTextAligned(Element.ALIGN_LEFT, tgl, 367, 195, 0); //sysdate
		    	over.showTextAligned(Element.ALIGN_LEFT, sbulan, 383, 195, 0); //sysdate
		    	over.showTextAligned(Element.ALIGN_LEFT, tahun.substring(2,4), 444, 195, 0); //sysdate
	
		    	over.showTextAligned(Element.ALIGN_LEFT, "ESTER TANTRI SETYANINGSIH", 320, 33, 0); //nama
		    	over.endText();
		    	
		    	Image img = Image.getInstance(Resources.getResourceAsFile(props.getProperty("images.ttd.ester").trim()).getAbsolutePath());
		        img.setAbsolutePosition(365, 45);
		        img.scaleAbsolute(60, 35);
		        over.addImage(img);
		    	
		    	
		    	//end of inserting content
			}
	    	
			stamp.close();
	    	os.close();
		}
		
    	
		return null;
	}
	
	
}