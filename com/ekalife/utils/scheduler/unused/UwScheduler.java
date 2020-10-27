package com.ekalife.utils.scheduler.unused;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.mail.MailException;

import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis dept UW
 * 
 * @author Yusuf
 * @since Sep 23, 2008 (1:38:40 PM)
 */
public class UwScheduler extends ParentScheduler{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public void send(boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments) 
			throws MailException, MessagingException{
		//enable untuk debugging saja
//		to = new String[] {"deddy@sinarmasmsiglife.co.id"};
//		cc = new String[] {"deddy@sinarmasmsiglife.co.id"};
//		bcc = new String[] {"deddy@sinarmasmsiglife.co.id"};
		
		this.email.send(isHtml, from, to, cc, bcc, subject, message, attachments);
	}
	
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i")) {
			//1. Summary untuk status2 aksep yg masih terpending, seperti further dkk
//			summaryAkseptasi();
			
			//2. Request Hendra (BAS) 4/3/09 - terkait pencetakan polis di cabang.
			summaryTerlambatCetakPolis();
			
			//3. Mirip yg no 1, tp ini khusus buat BSM & report nya di kelompokin per cabang
			summaryAkseptasiBSM();
		}
	}

	public void summaryTerlambatCetakPolis() throws Exception{
		Date sysdate = elionsManager.selectSysdate(0);
		
		//cc ke orang2 BAS
		String cc = "Hendra@sinarmasmsiglife.co.id;Yune@sinarmasmsiglife.co.id;Huluk@sinarmasmsiglife.co.id;Martino@sinarmasmsiglife.co.id;Desy@sinarmasmsiglife.co.id";
		
		///Yusuf (13 Aug 09) - Request Hendra via email, untuk cabang2 tertentu ada cabang induknya, misalnya Jatim I
		List<DropDown> cabangInduk = new ArrayList<DropDown>();
		cabangInduk.add(new DropDown("05", "jatim1@sinarmasmsiglife.co.id")); //Regional JATIM I : Malang, Kediri, Jember, Mataram, Banjarmasin
		
		//1. Polis2 yang H+1 dari tgl valid belum dicetak juga oleh cabang
		/*
		 * Kondisi2nya adalah : 
		 * - LSPD_ID < 8
		 * - LCA_ID <> '09'
		 * - MSPO_DATE_PRINT IS NULL
		 * - MSTE_TGL_VALID_PRINT IS NOT NULL
		 * - FLAG_PRINT_CABANG = 1
		 * - Selisih antara TGL VALID dgn SYSDATE > 1 
		 */
		List<Map> summary = uwManager.selectPolisBelumDicetak();
		for(Map m : summary){
			Date mste_tgl_valid_print = (Date) m.get("MSTE_TGL_VALID_PRINT");
			
			String bcc = "Hendra@sinarmasmsiglife.co.id";

			String lca_id = ((String) m.get("LCA_ID")).trim();
			for(DropDown c : cabangInduk){
				if(c.getKey().equals(lca_id)){
					bcc += ";" + c.getValue();
				}
			}
			
			int selisih = (int) FormatDate.dateDifference(mste_tgl_valid_print, sysdate, true);
			if(selisih > 1){
				String mspo_policy_no_format = (String) m.get("MSPO_POLICY_NO_FORMAT");
				//String reg_spaj = (String) m.get("REG_SPAJ");
				String emailCabang = (String) m.get("LAR_EMAIL");
				if(emailCabang != null){
					send(false, 
							props.getProperty("admin.ajsjava"), new String[]{emailCabang}, cc.split(";"), 
							bcc.split(";"), 
							"Harap segera lakukan pencetakan Polis " + mspo_policy_no_format, 
							"Harap segera lakukan pencetakan Polis " + mspo_policy_no_format + "! Pencetakan Polis ini sudah terlambat " + selisih + " hari sejak di-VALID oleh UNDERWRITING.", 
							null);
				}
			}
		}
		
		//2. Polis2 yang H+1 dari tgl cetak polis belum ditransfer juga oleh cabang
		/*
		 * Kondisi2nya adalah : 
		 * - LSPD_ID = 6
		 * - LCA_ID <> '09'
		 * - MSPO_DATE_PRINT IS NOT NULL
		 * - MSTE_TGL_VALID_PRINT IS NOT NULL
		 * - FLAG_PRINT_CABANG = 1
		 * - Selisih antara TGL CETAK dgn SYSDATE > 1 
		 */
		List<Map> summary2 = uwManager.selectPolisBelumDitransfer();
		for(Map m : summary2){
			Date mspo_date_print = (Date) m.get("MSPO_DATE_PRINT");

			String lca_id = (String) m.get("LCA_ID");
			String bcc = "Hendra@sinarmasmsiglife.co.id";
			for(DropDown c : cabangInduk){
				if(c.getKey().equals(lca_id)){
					bcc += ";" + c.getValue();
				}
			}

			int selisih = (int) FormatDate.dateDifference(mspo_date_print, sysdate, true);
			if(selisih > 1){
				String mspo_policy_no_format = (String) m.get("MSPO_POLICY_NO_FORMAT");
				//String reg_spaj = (String) m.get("REG_SPAJ");
				String emailCabang = (String) m.get("LAR_EMAIL");
				if(emailCabang != null){
					send(false, 
							props.getProperty("admin.ajsjava"), new String[]{emailCabang}, cc.split(";"),  
							bcc.split(";"), 
							"Harap segera lakukan transfer Polis " + mspo_policy_no_format, 
							"Harap segera lakukan transfer Polis " + mspo_policy_no_format + "! Transfer Polis ini sudah terlambat " + selisih + " hari sejak dilakukan pencetakan Polis.", 
							null);
				}
			}
		}
		
	}
	
	/**
	 * Kirim report ke BSM pertanggal 15 atau tgl 17 jika tgl 14 hari sabtu, 
	 * & tgl 1 bln berikut nya
	 * 
	 * @throws Exception
	 *
	 * @author Yusup_A
	 * @since Jul 31, 2009 (2:25:02 PM)
	 */
	public void summaryAkseptasiBSM() throws Exception {
		Calendar curr = Calendar.getInstance();
		curr.setTime(new Date());
		//int lastDay = curr.getActualMaximum(Calendar.DAY_OF_MONTH);
		int halfMonth = 15;
		String lastMonthLastDay = "";
		Calendar curr2 = Calendar.getInstance();
		curr2.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), halfMonth);
		
		if(curr2.get(Calendar.DAY_OF_WEEK) == 1) halfMonth = 17;
		//else if (curr2.get(Calendar.DAY_OF_WEEK) == 7) halfMonth = 17;
		if(halfMonth == curr.get(Calendar.DATE)) {
			curr2.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH)-1, 1);
			lastMonthLastDay = (new Integer(curr2.getActualMaximum(Calendar.DAY_OF_MONTH))).toString()
								+"/"+
								(new Integer(curr2.get(Calendar.MONTH)+1)).toString()
								+"/"+
								(new Integer(curr2.get(Calendar.YEAR))).toString();
			//lastMonthLastDay = "30/7/2009";
		}
		logger.info(curr.get(Calendar.DATE));
		//if(halfMonth == curr.get(Calendar.DATE) || 1 == curr.get(Calendar.DATE)) {
			try {
				Date yesterday = elionsManager.selectSysdate(-1);
				Date today = elionsManager.selectSysdate(0);
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				
				//Deddy (13/3/2009) tambahan untuk menentukan tahun -1 dari sekarang 
				DateFormat year = new SimpleDateFormat("yyyy");
				String yearbefore = year.format(FormatDate.add(today, Calendar.YEAR, -1) );
				
				//Deddy(16/3/2009) tambahan untuk menentukan bulan1-12 dari tahun sekarang
				DateFormat monthyear = new SimpleDateFormat("mmyyyy");
				String month1 = "01"+ year.format(today);
				String month2 = "02"+ year.format(today);
				String month3 = "03"+ year.format(today);
				String month4 = "04"+ year.format(today);
				String month5 = "05"+ year.format(today);
				String month6 = "06"+ year.format(today);
				String month7 = "07"+ year.format(today);
				String month8 = "08"+ year.format(today);
				String month9 = "09"+ year.format(today);
				String month10 = "10"+ year.format(today);
				String month11 = "11"+ year.format(today);
				String month12 = "12"+ year.format(today);
				
				logger.info("UW SCHEDULER KHUSUS BSM AT " + new Date());
				long start = System.currentTimeMillis();
				
				String outputDir = props.getProperty("pdf.dir.report") + "\\summary_akseptasi\\" + dateFormat.format(today) + "\\";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				params.put("tanggal", df.format(today));
				params.put("user", "SYSTEM");
				
				//passing parameter ke reportnya
				params.put("yearbefore", yearbefore);
				params.put("month1", month1);
				params.put("month2", month2);
				params.put("month3", month3);
				params.put("month4", month4);
				params.put("month5", month5);
				params.put("month6", month6);
				params.put("month7", month7);
				params.put("month8", month8);
				params.put("month9", month9);
				params.put("month10", month10);
				params.put("month11", month11);
				params.put("month12", month12);			

				Map<String, List<Map>> distribusi 			= new HashMap<String, List<Map>>();
				Map<String, List<Map>> daftarReport 		= new HashMap<String, List<Map>>();
				Map<String, List<File>> daftarAttachment  = new HashMap<String, List<File>>();
				
				//Daftar Semua Report
				daftarReport.put("Akseptasi_Khusus", uwManager.selectDaftarAkseptasiNyangkutTemp(10, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,lastMonthLastDay)); 		//1. akseptasi khusus
				daftarReport.put("Further_Requirements", uwManager.selectDaftarAkseptasiNyangkutTemp(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,lastMonthLastDay)); 	//2. further requirements -> lssa_id in (3,4,8)
				
				//Looping Utama dari daftar semua report
				for(String r : daftarReport.keySet()) {
					
					params.put("banyakMaunya", r);
					
					//Parameter Tambahan
					if(r.equals("Expired")) {
						params.put("judul", "Follow-Up Polis Expired");
					}
					else if(r.equals("Akseptasi_Khusus")) {
						params.put("judul", "Follow-Up Akseptasi Khusus");
					}else if(r.equals("Further_Requirements")) {
						params.put("judul", "Follow-Up Further Requirement");
					}else if(r.equals("Postpone")) {
						params.put("judul", "Follow-Up Surat Konfirmasi Case Postpone");
					}else if(r.equals("Decline")) {
						params.put("judul", "Follow-Up Surat Konfirmasi Case Decline");
					}
					
					if(r.equals("Akseptasi_Khusus")) {
						params.put("note",
							"Data yang masuk disini adalah data:\n" +
							"* Polis diaksep dengan kondisi khusus (polis yang sudah diaksep tetapi masih diperlukan data tambahan)");
//							"Note:\n" +
//							"Data yang masuk disini adalah data :\n" +
//							"* Polis yang sudah dicetak, tetapi masih ada Further requirement. Polis diaksep dengan kondisi khusus.\n" + 
//							"Polis yang di 'Aksep dengan kondisi khusus' adalah polis yang diaksep ( dapat sudah langsung dicetak polis atau masih pending cetak polis )\n" +
//							"namun sebenarnya masih diperlukan data tambahan.");
					}else if(r.equals("Further_Requirements")) {
						params.put("note",
							"Note:\n" +
							"Data yang masuk disini adalah data :\n" +
							"* Polis yang belum diaksep, masih perlu data tambahan ( Further requirement )");
					}else {
						params.put("note", "");
					}
					
					//Daftar pembagian distribusi, direset setiap loop
					distribusi.put("Bancass",   new ArrayList<Map>());
					
					List<Map> report = daftarReport.get(r);
					
					//Looping untuk membagi2 hasil query ke masing2 distribusi
					for(Map m : report) {
						String lca_id = (String) m.get("LCA_ID");
						String team_name = (String) m.get("TEAM_NAME");
						BigDecimal jenis = (BigDecimal) m.get("JN_BANK");
						int jn_bank = (jenis == null ? 0 : jenis.intValue());
						
						if(team_name== null){
							team_name= "";
						}
						else if(lca_id.equals("09")) { //Bancassurance
							((List<Map>) distribusi.get("Bancass")).add(m);
						}
					}
					
					List<Map> temp = new ArrayList<Map>();
					Map<String, List<Map>> cabang = new HashMap<String, List<Map>>();
					String key = "";
					for(int a=0;a< distribusi.get("Bancass").size();a++) {
						if(distribusi.get("Bancass").get(a).get("KODE_BSM") != null) {
							if(!key.equals(distribusi.get("Bancass").get(a).get("KODE_BSM"))) {
								if(key.equals("")) {
									temp.add(distribusi.get("Bancass").get(a));
									key = distribusi.get("Bancass").get(a).get("KODE_BSM").toString();
								}
								else {
									cabang.put(key, temp);
									temp = new ArrayList<Map>();
									temp.add(distribusi.get("Bancass").get(a));
									key = distribusi.get("Bancass").get(a).get("KODE_BSM").toString();
								}
							}
							else {
								temp.add(distribusi.get("Bancass").get(a));
							}						
						}
					}
					
					//Looping untuk menyimpan file PDF berdasarkan masing2 distribusi
					for(String s : cabang.keySet()) {
						List<Map> daftar = cabang.get(s);
						
						String cab_bank = "";
						if(s.equals("Bancass1")) cab_bank = "1";
						else if(s.equals("Bancass2")) cab_bank = "2";
						
						
						params.put("cab_bank", cab_bank);
						if(!daftar.isEmpty()) {

							//Bagian ini untuk menghasilkan file dalam bentuk pdf
//							String outputFilename = r + "_" + s + ".pdf";
//							JasperUtils.exportReportToPdf(
//									props.getProperty("report.uw.summary." + r) + ".jasper", 
//									outputDir, outputFilename, params, daftar, PdfWriter.AllowPrinting, null, null);
							
//							Bagian ini untuk menghasilkan file dalam bentuk Xcel
							String outputFilename = r + "_" + s + ".xls";
							JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
									outputDir, outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");

//							buat testing						
//							JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
//									props.getProperty("upload.dir"), outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");
							
							if(daftarAttachment.get(s) == null) {
								List<File> x = new ArrayList<File>();
								x.add(new File(outputDir + "\\" + outputFilename));
								daftarAttachment.put(s,x);
							}
							else {
								List<File> y   = daftarAttachment.get(s);
								y.add(new File(outputDir + "\\" + outputFilename));
								daftarAttachment.put(s,y);
							}
						}
					}
				}
				
				//email semua report
				for (String s : daftarAttachment.keySet()) {
					List<File> attachments = daftarAttachment.get(s);
					List<Map> daftarEmail = uwManager.selectEmailPincab(s);
					
					String[] to = new String[] {};
					String[] cc = new String[] {};
					
					//String pincab = daftarEmail.get(0).get("EM_PINCAB").toString();
					//if(daftarEmail.get(0).get("KODE").toString().trim().equals("BSD")) pincab = "astrid.tambunan@banksinarmas.com";
					//else if(daftarEmail.get(0).get("KODE").toString().trim().equals("FTM")) pincab = "hans.felix@banksinarmas.com";
					//else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SRG")) pincab = "deddy.t.wiharja@banksinarmas.com";
					//else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKB")) pincab = "richard@banksinarmas.com";
					
					
					if(halfMonth == curr.get(Calendar.DATE) || 1 == curr.get(Calendar.DATE)) {
						to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"nia.julidaria@banksinarmas.com"};
						if(daftarEmail.get(0).get("KODE").toString().trim().equals("BSD")) to = new String[]{"ming.aswaty@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("FTM")) to = new String[]{"hans.felix@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SRG")) to = new String[]{"deddy.t.wiharja@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKB")) to = new String[]{"richard@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("TNA")) to = new String[]{"antonius.gustaman@banksinarmas.com","sigit.s.aji@banksinarmas.com","dk.budiartha@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("LPG")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"Bancass_Lampung@sinarmasmsiglife.co.id","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PLU")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"maya.natalia@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("KUD")) to = new String[]{"koengfoe15@yahoo.com","kanthi.nalarantini@banksinarmas.com",daftarEmail.get(0).get("EM_PINCAB").toString(),"nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PLM")) to = new String[]{"sudrajat@banksinarmas.com","cs041@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MDN")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"cs015@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PWT")) to = new String[]{"andreas.s.andrianto@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("HAS")) to = new String[]{"linawati.sudarmo@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKJ")) to = new String[]{"laurencia.y.gunawan@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("DPK")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"cs032@banksinarmas.com","nia.julidaria@banksinarmas.com"};
						
						cc = new String[]{"joni@banksinarmas.com","asriwulan@sinarmasmsiglife.co.id","novie@sinarmasmsiglife.co.id","tities@sinarmasmsiglife.co.id"};

						StringBuffer pesan = new StringBuffer();
						pesan.append("Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bank Sinarmas "+ daftarEmail.get(0).get("NAMA_CAB").toString().trim() +" s/d "+df.format(yesterday));
						pesan.append("<br/><br/><br/><br/>");
						pesan.append("Salam,");
						pesan.append("<br/>");
						pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
						pesan.append("<br/><br/>");
						pesan.append("Tities Partisiwi");
						pesan.append("<br/>");
						pesan.append("Underwriting Departement");
						pesan.append("<br/>");
						pesan.append("[ (021)6257807 / ext. 8711 ]");
					
						
						send(true, "tities@sinarmasmsiglife.co.id", 
								to,
								cc,
								new String[]{"yusup_a@sinarmasmsiglife.co.id"},
								//new String[] {"yusup_a@sinarmasmsiglife.co.id"},	
								//new String[]{},new String[]{},new String[]{"yusup_a@sinarmasmsiglife.co.id"},
								"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bank Sinarmas "+ daftarEmail.get(0).get("NAMA_CAB") +" s/d " + df.format(yesterday), 
								pesan.toString(), attachments);	
					}
					else if(2 == curr.get(Calendar.DAY_OF_WEEK)){
						to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString()};
						if(daftarEmail.get(0).get("KODE").toString().trim().equals("BSD")) to = new String[]{"ming.aswaty@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("FTM")) to = new String[]{"hans.felix@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SRG")) to = new String[]{"deddy.t.wiharja@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKB")) to = new String[]{"richard@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("TNA")) to = new String[]{"antonius.gustaman@banksinarmas.com","sigit.s.aji@banksinarmas.com","dk.budiartha@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("LPG")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"Bancass_Lampung@sinarmasmsiglife.co.id"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PLU")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"maya.natalia@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("KUD")) to = new String[]{"koengfoe15@yahoo.com","kanthi.nalarantini@banksinarmas.com",daftarEmail.get(0).get("EM_PINCAB").toString()};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PLM")) to = new String[]{"sudrajat@banksinarmas.com","cs041@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MDN")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"cs015@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PWT")) to = new String[]{"andreas.s.andrianto@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("HAS")) to = new String[]{"linawati.sudarmo@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKJ")) to = new String[]{"laurencia.y.gunawan@banksinarmas.com"};
						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("DPK")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"cs032@banksinarmas.com"};
						
						cc = new String[] {"tities@sinarmasmsiglife.co.id"};

						StringBuffer pesan = new StringBuffer();
						pesan.append("Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bank Sinarmas "+ daftarEmail.get(0).get("NAMA_CAB").toString().trim() +" s/d "+df.format(yesterday));
						pesan.append("<br/><br/><br/><br/>");
						pesan.append("Salam,");
						pesan.append("<br/>");
						pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
						pesan.append("<br/><br/>");
						pesan.append("Tities Partisiwi");
						pesan.append("<br/>");
						pesan.append("Underwriting Departement");
						pesan.append("<br/>");
						pesan.append("[ (021)6257807 / ext. 8711 ]");
					
						
						send(true, "tities@sinarmasmsiglife.co.id", 
								to,
								cc,
								new String[]{"yusup_a@sinarmasmsiglife.co.id"},
								//new String[] {"yusup_a@sinarmasmsiglife.co.id"},	
								//new String[]{},new String[]{},new String[]{"yusup_a@sinarmasmsiglife.co.id"},
								"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bank Sinarmas "+ daftarEmail.get(0).get("NAMA_CAB") +" s/d " + df.format(yesterday), 
								pesan.toString(), attachments);	
					}
					
				}
				
				long end = System.currentTimeMillis();
				logger.info("UW SCHEDULER BSM FINISHED AT " + new Date() + " in " + ( (float) (end-start) / 1000) + " SECONDS.");
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}			
		//}
	}
	
}