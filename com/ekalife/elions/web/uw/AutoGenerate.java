package com.ekalife.elions.web.uw;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.CekValidPrintPolis;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.CommandUploadBac;
import com.ekalife.elions.model.Endors;
import com.ekalife.elions.model.MstInboxHist;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Position;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Simcard;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.uw.support.WordingPdfViewer;
import com.ekalife.utils.CekPelengkap;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.EncryptUtils;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.PdfUtils;
import com.ekalife.utils.Print;
import com.ekalife.utils.Products;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentMultiController;
import com.ekalife.utils.view.PDFViewer;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import id.co.sinarmaslife.std.model.vo.DropDown;

/**
 * 
 * @author Mark.Panjaitan
 * AutoGenerate is for generate All File Polis and Merge PDF
 */
@SuppressWarnings("unchecked")
public class AutoGenerate extends ParentMultiController{
	protected final Log logger = LogFactory.getLog( getClass() );
	DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	
	//hati2 merubah nilai ini, ada yang di hard code ke printpolis_viewer.jsp dan printpolis.jsp
	public static final int ALL = -1;
	
	public static final int POLIS_DUPLEX = 0; //Polis + Manfaat
	public static final int POLIS_QUADRUPLEX = 1; //Polis + Manfaat + Surat Polis + Alokasi Dana
	public static final int POLIS = 2;
	public static final int MANFAAT = 3;
	public static final int SURAT_POLIS = 4;
	public static final int ALOKASI_DANA = 5;
	public static final int SERTIFIKAT_POWERSAVE = 6;
	public static final int TANDA_TERIMA_POLIS = 7;
	public static final int KARTU_PREMI = 8;
	public static final int SYARAT_UMUM_KHUSUS = 9;
	public static final int TUNGGAKAN_PREMI = 10; //BELUM DIBUAT
	public static final int SERTIFIKAT_GUTHRIE = 11; //KHUSUS PRODUK GUTHRIE
	public static final int SURAT_PERJANJIAN_HUTANG = 12;
	public static final int POLIS_DMTM = 13;
	public static final int SURAT_SIMCARD = 14;
	public static final int SURAT_BREAKABLE = 15;
	public static final int PANDUAN_VIRTUAL_ACCOUNT = 16;
	public static final int SYARAT_UMUM_KHUSUS_EAS = 17; //ini khusus untuk rudy, jadi gak perlu divalidasi, gak perlu javascript print
	public static final int ENDORSEMEN = 18;//KHUSUS RIDER SWINE FLU
	public static final int SYARAT_KHUSUS = 19; //ssk untuk swine flu khusus BSM saja
	public static final int ENDORS_EKASEHAT_ADMEDIKA = 20; // Endorsemen Polis + Petunjuk Penggunaan Kartu + List Provider
	public static final int POLIS_PAS = 21;
	public static final int TANDA_TERIMA_POLIS_DUPLIKAT = 22;
	public static final int KWITANSI = 23;
	public static final int POLIS_TERM = 24;
	public static final int POLIS_QUADRUPLEX_PLUS_HADIAH = 25;
	public static final int ENDORS_GMIT = 28;
	public static final int POLIS_QUADRUPLEX_MEDICAL_PLUS = 29;
	public static final int FINISH = 11;
	public static final int SURAT_PENJAMINAN_PROVIDER = 30;
	public static final int SERTIFIKAT_TERM_ROP = 31;
	public static final int GENERATE_ALL = 32;	

		public ModelAndView generateAll(HttpServletRequest request, HttpServletResponse response, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Properties props, Products products) throws Exception {	
		//public ModelAndView generateAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			int printType = ServletRequestUtils.getRequiredIntParameter(request, "printType");
			String spaj = request.getParameter("spaj");
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			Pemegang pmg =elionsManager.selectpp(spaj);

			boolean isValidated=true;
			boolean isBancass = ServletRequestUtils.getBooleanParameter(request, "isbancass", false);
			boolean isViewer = ServletRequestUtils.getBooleanParameter(request, "isviewer", false);
			String format = ServletRequestUtils.getStringParameter(request, "format", "");
			int isInputanBank= -1;
			isInputanBank = elionsManager.selectIsInputanBank(spaj);
			Integer flag = uwManager.flag_upload_scan(spaj);			
			
			Map detBisnis = (Map) elionsManager.selectDetailBisnis(spaj).get(0);
			String businessId = (String) detBisnis.get("BISNIS");
			String lsbs_name = (String) detBisnis.get("LSBS_NAME");
			String lsdbs_name = (String) detBisnis.get("LSDBS_NAME");
			String lsdbs = (String) detBisnis.get("DETBISNIS");			
			
			format = ServletRequestUtils.getStringParameter(request, "format", "");			
			
			//Map parameters = this.referenceData(spaj, businessId, true, false, false, currentUser);
			
			Boolean isSimasMagnaLink = (businessId.equals("213")) || (businessId.equals("216"));
			
			if(!isSimasMagnaLink) {
					List errors = new ArrayList();
					errors.add("Produk ini tidak bisa menggunakan menu Generate All");
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
			}else if(isSimasMagnaLink){
				//Cek Kelengkapan
				List errors = new ArrayList();
				String cabang = elionsManager.selectCabangFromSpaj(spaj);
				String path ="";
				String pathTemp = "";
				path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				//path = super.getProps().getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				pathTemp = props.getProperty("pdf.dir")+"\\"+cabang+"\\"+spaj;				
				File destDir = new File(path);
				if(!destDir.exists()) {		
					errors.add("File-file Pelengkap belum ada.");				
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}
					
				if(!CekPelengkap.cekKelengkapan(path, request, errors).isEmpty()){
					//errors.add("File-file Pelengkap belum lengkap.");				
					return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
				}					
							
			}			
			
			List validasi = new ArrayList();
			if(isValidated) {
				//validasi = validasi(printType, spaj, businessId, request, products);
				validasi = validasi(printType, spaj, businessId, request, elionsManager, uwManager, bacManager, products);				
			}
			String reportUrl = "";
			if(validasi.size() > 0)return new ModelAndView("uw/printpolis/printpolis_err", "error", validasi);
			if(products.unitLink(businessId) && !products.stableLink(businessId)) {
				elionsManager.updateUlink(3, spaj, df2.format(elionsManager.selectMuTglTrans(spaj)));
			}
		//	Mark Valentino 20180906
			if(isValidated && !isBancass) {
				
				elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS GENERATE_ALL(E-LIONS)", true, currentUser);				

				//request tim Printing : Generate Polis_All & Pelengkap
				response.sendRedirect(request.getContextPath()+"/reports/printall2.pdf?spaj="+spaj+"&flag="+2);
					
			}
			return null;
}
		
		private List validasi(int printProgress, String spaj, String businessId, HttpServletRequest request, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Products products) throws Exception{
			
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			List errors = new ArrayList();
			
			Date tglCetak = elionsManager.selectPrintDatePolis(spaj);
			Calendar tgl_mulai_ssu = new GregorianCalendar(2006, 8, 20);
			String file = ServletRequestUtils.getStringParameter(request, "file", "");
			String format = ServletRequestUtils.getStringParameter(request, "format", "");

			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			
//			if(POLIS_DMTM != printProgress && SURAT_POLIS != printProgress) {
//				//validasi polis DM/TM, baru bisa produk 142-8 saja
//				if(lsbs.equals("142") && lsdbs.equals("008")) {
//					errors.add("Untuk Polis DM/TM mohon gunakan menu POLIS KHUSUS PRODUK DM/TM atau menu SURAT POLIS");
//				}
//			}
			
			//muamalat
			if(products.muamalat(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))) {
				//validasi polis muamalat - mabrur, hanya bisa cetak alokasi dana saja
				if(lsbs.equals("153") && lsdbs.equals("005")) {
					if(ALOKASI_DANA != printProgress && SURAT_SIMCARD != printProgress) {
						errors.add("Khusus Produk Bank Muamalat (Mabrur), hanya dapat melakukan pencetakan LAPORAN ALOKASI DANA (UNIT LINK)");
						return errors;
					}
				}else{
					errors.add("Produk Bank Muamalat, tidak perlu melakukan pencetakan dokumen");
					return errors;
				}
			}
			Simcard simcard = uwManager.selectSimasCardBySpaj(spaj);
			List daftarSebelumnya = uwManager.selectSimasCard(spaj);
			List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj); 
			//daftarSebelumnya.removeAll(daftarSebelumnya);
			if(simcard==null && daftarSebelumnya.isEmpty()){
				if( !lsbs.equals("187") && !products.bethany(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) && !products.muamalat(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) ){
					if(!isAgen.isEmpty() && !FormatString.rpad("0",(spaj.substring(0,2)),2).equalsIgnoreCase("09")){
						if(elionsManager.selectCekPrintUlang(spaj, "%POLIS TUTUPAN AGENT%")<=0){
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "POLIS TUTUPAN AGENT, AGENT SUDAH PERNAH MENDAPATKAN SIMASCARD DARI DISTRIBUTION SUPPORT", spaj, 0);
						}
					}
					else{
						
						if((!((lsbs.equals("143") && (lsdbs.equals("1") || lsdbs.equals("3") || lsdbs.equals("7"))) || (lsbs.equals("144") && lsdbs.equals("1")) || lsbs.equals("183") || lsbs.equals("116"))) && isAgen.isEmpty()){
						// Patar Timoitus 20182008	errors.add("Silakan masukkan No Simas Card terlebih dahulu sebelum melakukan proses print");
						}
					    
					}
				}
			}else{
				if(!daftarSebelumnya.isEmpty()){
					if(elionsManager.selectCekPrintUlang(spaj, "%SUDAH PERNAH DAPAT SIMAS CARD%")<=0){
						Map SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
						if(!Common.isEmpty(SimasCardSebelumnya.get("REG_SPAJ"))){
							String reg_spaj_lama = (String) SimasCardSebelumnya.get("REG_SPAJ");
							String mspo_policy_no_lama = uwManager.selectNoPolisFromSpaj(reg_spaj_lama);
							//MANTA - 14/02/2014
							if(mspo_policy_no_lama == null){
								mspo_policy_no_lama = uwManager.selectGetMspoPolicyNo(reg_spaj_lama);
							}
							String msps_desc = elionsManager.selectMstPositionSpajMspsDesc(spaj, 5);
							if(!reg_spaj_lama.equals(spaj)){
								if(Common.isEmpty(msps_desc) && errors.isEmpty() ){
									elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO POLIS "+ FormatString.nomorPolis(mspo_policy_no_lama), spaj, 0);
								}
								errors.add("Polis ini sudah pernah mendapatkan Simas Card pada no polis "+ FormatString.nomorPolis(mspo_policy_no_lama));
							}
						}else{
							SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
							if(Common.isEmpty((String) SimasCardSebelumnya.get("REG_SPAJ"))){
								//Deddy - ini untuk simas card agent yg diinput manual
								String no_kartu_sebelumnya = (String) SimasCardSebelumnya.get("NO_KARTU");
								elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO : "+ no_kartu_sebelumnya , spaj, 0);
							}else{
								String reg_spaj_lama = (String) SimasCardSebelumnya.get("REG_SPAJ");
								String mspo_policy_no_lama = uwManager.selectNoPolisFromSpaj(reg_spaj_lama);
								String msps_desc = elionsManager.selectMstPositionSpajMspsDesc(spaj, 5);
								if(!reg_spaj_lama.equals(spaj)){
									if(Common.isEmpty(msps_desc) && errors.isEmpty() ){
										elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO POLIS "+ FormatString.nomorPolis(mspo_policy_no_lama), spaj, 0);
									}
									errors.add("Polis ini sudah pernah mendapatkan Simas Card pada no polis "+ FormatString.nomorPolis(mspo_policy_no_lama));
								}
							}
						}
					}
				}
			}
			
			if("58".indexOf(cabang)<=-1){
				if(products.unitLink(lsbs) || (lsbs.equals("164") && lsdbs.equals("11"))){
					List<Date> asdf = uwManager.selectSudahProsesNab(spaj);
					boolean oke = true;
					for(Date d : asdf){
						if(d == null) {
							oke = false;
							break;
						}
					}
					if(asdf.size()==0){
						oke = false;
					}
					if(!oke) errors.add("Polis tidak dapat dicetak karena belum dilakukan proses NAB.");
				}
			}
			
			Integer cekProsesBatal = bacManager.selectCountSpajMstRefund(spaj);
			
			if(cekProsesBatal>0)errors.add("Polis tidak dapat dicetak karena dalam proses Pembatalan.");
			
			//KALAU SSU, MAU DIBUKA DARI MANA SAJA, CUKUP SATU VALIDASI
			//**DIAN
			if( SYARAT_UMUM_KHUSUS == printProgress || SYARAT_KHUSUS == printProgress) { //untuk lihat SSU/SSK, gak perlu validasi
				if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
					return errors;
				}else{
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK SYARAT_UMUM_KHUSUS");
				if(count>0&&((currentUser.getJn_bank()!=2||currentUser.getJn_bank()!=3) && !currentUser.getLca_id().equals("58"))){
					errors.add("Maaf, Pencetakan SSU/SSK hanya dapat dilakukan 1 kali.");
					request.setAttribute("printUlang", spaj);
				}else{
					if(currentUser.getLde_id().equals("11")){ //kalo underwriting, boleh
					
					}else if(tglCetak == null) { 
						errors.add("Maaf, tetapi untuk melihat SSU harus melakukan pencetakan polis/sertifikat terlebih dahulu.");
					} else if(tglCetak.before(tgl_mulai_ssu.getTime())) {
						errors.add("Tidak ada SSU/SSK.");
//					} else if("045, 130, 140".indexOf(businessId) > -1) {
					} else if("045, 130".indexOf(businessId) > -1) {
						errors.add("SSU/SSK untuk produk ini PRE-PRINTED");
					}}
				return errors;
				}
			}
			if(TANDA_TERIMA_POLIS == printProgress) { //tanda terima
				if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
					return errors;
				}else{
					int count= elionsManager.selectCekPrintUlang(spaj, "CETAK TANDA_TERIMA_POLIS");
					if(count>0&&(currentUser.getJn_bank().equals(""))){
						errors.add("Maaf, Pencetakah Tanda Terima Polis hanya dapat dilakukan 1 kali.");
						request.setAttribute("printUlang", spaj);
					}
				}
			}
			
			if( MANFAAT == printProgress ) { //manfaat
				if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
					return errors;
				}else{
					int count= elionsManager.selectCekPrintUlang(spaj, "CETAK MANFAAT");
					if(count>0&&(currentUser.getJn_bank().equals(""))){
						errors.add("Maaf, Pencetakan Manfaat Polis hanya dapat dilakukan sekali.");
						request.setAttribute("printUlang", spaj);
					}
				}
			}
			if( SURAT_POLIS == printProgress ) {
				if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
					return errors;
				}else{
					int count= elionsManager.selectCekPrintUlang(spaj, "CETAK SURAT_POLIS");
					if(count>0&&(currentUser.getJn_bank().equals(""))){ 
						errors.add("Maaf, Pencetakan Surat Polis hanya dapat dilakukan sekali.");
						request.setAttribute("printUlang", spaj);
					}
				}
			}
			//KALAU Surat Perjanjian Pinjaman Polis, MAU DIBUKA DARI MANA SAJA, HANYA BOLEH UNTUK PRODUK SAVE
			if(SURAT_PERJANJIAN_HUTANG == printProgress && !products.powerSave(businessId)) {
				errors.add("Maaf, SPH hanya bisa dicetak untuk produk SAVE");
			}
			if( ALOKASI_DANA == printProgress ) {
				if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
					return errors;
				}else{
					int count= elionsManager.selectCekPrintUlang(spaj, "CETAK ALOKASI_DANA");
					if(count>0&&(currentUser.getJn_bank().equals(""))){ 
						errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan Laporan Alokasi Dana / Surat Excellink hanya 1 kali ");
						request.setAttribute("printUlang", spaj);
					}
				}
				
			}
			
			if( PANDUAN_VIRTUAL_ACCOUNT == printProgress ) {
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK PANDUAN_VIRTUAL_ACCOUNT");
				if(count>0&&(currentUser.getJn_bank().equals(""))){
					errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan PANDUAN_VIRTUAL_ACCOUNT hanya 1 kali ");
					request.setAttribute("printUlang", spaj);
				
				}
			}
			if( POLIS_DMTM == printProgress ){
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK POLIS_DMTM");
				if(count>0&&(currentUser.getJn_bank().equals(""))){
					errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan POLIS_DMTM hanya 1 kali ");
					request.setAttribute("printUlang", spaj);
				
				}
			}
			
			if( POLIS_PAS == printProgress ){
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK POLIS_PAS");
				if(count>0&&(currentUser.getJn_bank().equals(""))){
					errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan POLIS_PAS hanya 1 kali ");
					request.setAttribute("printUlang", spaj);
				
				}
			}
			
			if( POLIS_TERM == printProgress ){
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK POLIS_TERM");
				if(count>0&&(currentUser.getJn_bank().equals(""))){
					errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan POLIS_TERM hanya 1 kali ");
					request.setAttribute("printUlang", spaj);
				
				}
			}
			
			if( KARTU_PREMI == printProgress ){
				int count= elionsManager.selectCekPrintUlang(spaj, "CETAK KARTU_PREMI");
				if(count>0&&(currentUser.getJn_bank().equals(""))){
					errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan KARTU_PREMI hanya 1 kali ");
					request.setAttribute("printUlang", spaj);
				
				}
			}
			if(SERTIFIKAT_POWERSAVE == printProgress) {
				int count= elionsManager.selectCekPrintUlang(spaj, "PRINT POLIS (E-LIONS)");
				Map map =elionsManager.validationPrintPolis(spaj); 
				if(count>0&&map.get("ORI")!=null&&(currentUser.getJn_bank()!=2||currentUser.getJn_bank()!=3)){
					errors.add("Maaf, tetapi anda  bisa melakukan pencetakkan SERTIFIKAT hanya 1 kali ");
					request.setAttribute("printUlang", spaj);
					request.setAttribute("jenis", 13);
				}
			}
			
			//KALAU DIBUKA DARI VIEWER, LEWATIN VALIDASI
			if(ServletRequestUtils.getBooleanParameter(request, "isviewer", false)) {
				return errors;
			}
			
			//VALIDASI UNTUK PRINTING SEMUA JENIS DOKUMEN
//			try {
//				String trimmed = FormatString.rpad("0", Integer.valueOf(businessId).toString(), 2);
//				n_prod produk = (n_prod) Class.forName("produk_asuransi.n_prod_"+trimmed).newInstance();
//				if(produk.isInvestasi && elionsManager.validationNAB(spaj)>0 && !businessId.equals("165")) {
//					errors.add("Untuk produk Excellink, harap lakukan proses NAB terlebih dahulu.");
//				}
//			}catch(ClassNotFoundException e) {
//				errors.add("Maaf, tetapi Produk "+businessId+" belum diimplementasikan. Harap konfirmasi dengan EDP.");
//			}

			//if(elionsManager.validationPositionSPAJ(spaj)!=6) {			
			if(elionsManager.validationPositionSPAJ(spaj)!=6) {
				errors.add("Harap cek posisi SPAJ.");
			}
//			
//			if(elionsManager.validationTglKirim(spaj)== null){
//				errors.add("Harap tanggal kirim di isi");
//			}
//			
			if(uwManager.selectJenisTerbitPolis(spaj)==1) {
				errors.add("Polis ini harus diterbitkan dalam bentuk SOFTCOPY (E-MAIL).");
				return errors;
			}

			//VALIDASI UNTUK PRINT POLIS UTAMA
			if( ( POLIS == printProgress || POLIS_DUPLEX == printProgress || POLIS_QUADRUPLEX == printProgress || POLIS_DMTM == printProgress || POLIS_PAS == printProgress || POLIS_TERM == printProgress )  
					&& request.getParameter("authorized")==null || ALL == printProgress) {
				
				
				String lsbs_id = uwManager.selectBusinessId(spaj);
				
				if(tglCetak != null) {
					if(tglCetak.before(tgl_mulai_ssu.getTime()) && elionsManager.selectSpajCancel(spaj)==0 && !lsbs_id.equals("140")) {
						errors.add("Tidak ada history Cover Polis");
					}
				}
				
				if(currentUser.getJn_bank()==2){
					if(!lsbs_id.equals("120")){
						errors.add("Print polis, manfaat, alokasi dana, saat ini hanya untuk produk Simas Power Link. Selain Produk Simas Power Link, Silakan print Sertifikat Polis.");
					}
				}
				
				if(POLIS_DMTM == printProgress) {
					//validasi polis DM/TM, baru bisa produk 142-8 saja
					if(lsbs.equals("142") && lsdbs.equals("008")) {
						//boleh
					}else {
						errors.add("Menu ini hanya dapat digunakan untuk Polis DM/TM");
					}
				}
				
				if(POLIS_PAS == printProgress){
					if(!lsbs.equals("187") && !lsbs.equals("205")){
						errors.add("Menu ini hanya dapat digunakan untuk Produk PAS");
					}
				}
				
				if(POLIS_TERM == printProgress){
					if(! (products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)))){
						errors.add("Menu ini hanya dapat digunakan untuk Produk TERM & PA Channel 88");
					}
					if(lsbs.equals("196") && lsdbs.equals("002")){					
						errors.add("Untuk Produk Term ini Silahkan pilih Menu POLIS,MANFAAT,ALOKASI,DANA");
						
					}
				}

			
				//validasi print ulang
				if(errors.isEmpty()) {
					Map map =elionsManager.validationPrintPolis(spaj); 
					if(map.get("ORI")!=null) {
						errors.add("Maaf, tetapi polis ini sudah pernah dicetak.");
						request.setAttribute("printUlang", spaj);
						return errors;
					}
				}
				
			}
			
			//VALIDASI UNTUK SURAT ALOKASI DANA / SURAT EXCELLINK
			

			
			//VALIDASI UNTUK SERTIFIKAT GUTHRIE
			else if( SERTIFIKAT_GUTHRIE == printProgress ) {
				if(Integer.valueOf(businessId).intValue() != 89) {
					errors.add("Polis ini bukan produk Ultra Sejahtera (PT. Guthrie Pecconina Indonesia).");
				}
			}
			
			//VALIDASI UNTUK SERTIFIKAT POWERSAVE/STABLE LINK
			else if( SERTIFIKAT_POWERSAVE == printProgress ) {
				int isInputanBank = elionsManager.selectIsInputanBank(spaj);
				String lde_id = currentUser.getLde_id();
				
				int referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
				
				if((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && (currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3)) { //apabila polis inputan bank sinarmas. untuk sinarmas sekuritas, gak perlu otorisasi lagi
					Map map =elionsManager.validationPrintPolis(spaj); 
					//String validasiMeterai = uwManager.validasiBeaMeterai();
					//if(validasiMeterai != null) errors.add(validasiMeterai);
					if(map.get("ORI")!=null) {
						errors.add("Maaf, tetapi pencetakan sertifikat hanya bisa dilakukan satu kali.");
						errors.add("Untuk mencetak ulang, silahkan otorisasi pencetakan ulang menggunakan user SPV / KaOps, kemudian cetak kembali menggunakan user Admin/CS.");
					}
					
					if(errors.isEmpty()) {
						//tambahan otorisasi 1 layer, masukkan password SPV
						if(isInputanBank != 3){
							errors.add("Silahkan masukkan password SPV Anda pada halaman berikut untuk melakukan pencetakan.");
						}
						request.setAttribute("printNeedAuthorization", spaj);
					}
					
				}
			}
			
			return errors;
			//return new ArrayList();
		}
		


}
