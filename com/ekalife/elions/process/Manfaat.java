package com.ekalife.elions.process;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import com.ekalife.elions.model.Datarider;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.lowagie.text.pdf.PdfReader;

import produk_asuransi.n_prod;

/**
 * Class yang digunakan untuk generate text dalam print manfaat
 * 
 * <br/>Special Case terjadi pada Produk invest (076,096,099,135,136) dimana manfaat tidak diambil dari tabel melainkan diketik langsung di report bersangkutan 
 * 
 * @author Yusuf
 * @since 28 Dec 2005
 */
@SuppressWarnings("unchecked")
public class Manfaat extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private List prosesAsuransiTambahanPlatinumLink(Map info) {
		int lsbs_id = Integer.valueOf((String) info.get("lsbs_id"));
		int lsdbs_number = (Integer) info.get("lsdbs_number");
		String reg_spaj = (String) info.get("reg_spaj");
		Integer mste_age = (Integer) info.get("mste_age");
		Integer mspo_age = (Integer) info.get("mspo_age");
		Double mspr_premium = (Double) info.get("mspr_premium");
		Double mspr_tsi = (Double) info.get("mspr_tsi");
		Integer lscb_id = (Integer) info.get("lscb_id");
		Integer mspo_ins_period = (Integer) info.get("mspo_ins_period");
		Integer mspo_pay_period = (Integer) info.get("mspo_pay_period");
		Integer mspo_installment = (Integer) info.get("mspo_installment");
		
		List daftarRider = bacDao.selectDaftarRider(reg_spaj);
		if(daftarRider.isEmpty()) return null;
		
		List result = new ArrayList();
		Map baris;
		
		for(int tahun=0; tahun<10; tahun++) {
			baris = new HashMap(); 
			baris.put("TAHUN", new Integer(tahun+1));
			baris.put("USIA", new Integer(mste_age+tahun));
			//800, 807, 801, 806, 814, 816, 815, 817
			for(int ke=0; ke<daftarRider.size(); ke++) {
				Datarider rider = (Datarider) daftarRider.get(ke);

				/*
				800 = PA
				801 = PENYAKIT KRITIS
				806 = HCP
				807 = TPD
				810 = PA
				811 = HCP -> MASUK KE 806
				812 = TPD
				813 = CRITICAL ILLNESS -> MASUK KE 801
				814 = WAIVER 60 TPD
				815 = PAYOR 25 TPD
				816 = WAIVER 60 CI
				817 = PAYOR 25 CI
				 */
				if("800, 801, 806, 807, 810, 811, 812, 813, 814, 815, 816, 817, 818".indexOf(FormatString.rpad("0", rider.getLsbs_id().toString(), 3))>-1) {

					String nama_produk ="produk_asuransi.n_prod_"+ FormatString.rpad("0", rider.getLsbs_id().toString(), 2);	
					n_prod produk = null;
					try {
						Class kelas = Class.forName( nama_produk );
						produk = (n_prod) kelas.newInstance();
						produk.setSqlMap(uwDao.getSqlMapClient());
					} catch (Exception e) {
						logger.error("ERROR :", e);
					}

					int lsbs = rider.getLsbs_id();
					int lsdbs = rider.getLsdbs_number();
					
					int pengali = 1;
					double persen = 1;
					if (lscb_id == 1) {  //triwulan
						pengali = 4;
						persen = 0.27;
					}else if (lscb_id == 2){ //semesteran
						pengali = 2;
						persen = 0.525;
					}else if (lscb_id == 6){ //bulanan
						pengali = 12;
						persen = 0.1;
					}
					
					double rate = 0.;

					/** PROSES PERHITUNGAN RATE PREMI */
					
					//USIA_PP = 0, USIA_TT = diisi usia tertanggung
					if("801, 811, 812, 813, 818".indexOf(FormatString.rpad("0", rider.getLsbs_id().toString(), 3))>-1) {
						if(rider.getLsbs_id().intValue()==812){
							if(mste_age+tahun>=60){
								
							}else{
								rate = ((BigDecimal) uwDao.selectLstRiderRate(
										new BigDecimal(rider.getLsbs_id()), 
										new BigDecimal(rider.getLsdbs_number()), 
										rider.getLku_id(), 0, (mste_age + tahun))).doubleValue();
							}
						}else{
							rate = ((BigDecimal) uwDao.selectLstRiderRate(
									new BigDecimal(rider.getLsbs_id()), 
									new BigDecimal(rider.getLsdbs_number()), 
									rider.getLku_id(), 0, (mste_age + tahun))).doubleValue();
						}
					//USIA_PP diurutkan 1-10, USIA_TT = diisi usia tertanggung
					}else if("814,816".indexOf(FormatString.rpad("0", rider.getLsbs_id().toString(), 3))>-1) {
						rate = ((BigDecimal) uwDao.selectLstRiderRate(
								new BigDecimal(rider.getLsbs_id()), 
								new BigDecimal(rider.getLsdbs_number()), 
								rider.getLku_id(), (tahun+1), (mste_age + tahun))).doubleValue();

					//USIA_PP = diisi usia pemegang, USIA_TT diurutkan dari 1 - 5
					}else if(!("810".indexOf(FormatString.rpad("0", rider.getLsbs_id().toString(), 3))>-1)){
						rate = ((BigDecimal) uwDao.selectLstRiderRate(
								new BigDecimal(rider.getLsbs_id()), 
								new BigDecimal(rider.getLsdbs_number()), 
								rider.getLku_id(), mspo_age + tahun, (tahun+1))).doubleValue();
					}
					
					//perhitungan rate ada 3 macam :
					//1. HCP, ratenya tidak dihitung, langsung berupa nominal dari tabel
					if(lsbs == 811 && (lsdbs >= 1 && lsdbs <= 10)) {
						produk.mbu_jumlah = rate;
					//2. Untuk waiver dan payor, Rate dihitung per 1000 premi
					}else if(lsbs >= 814 && lsbs <= 817) {
						produk.mbu_jumlah = ((mspr_premium * pengali / 1000) * rate) * persen;
					}else if(lsbs ==810){
						//produk.mbu_jumlah = 127500;
						produk.mbu_jumlah = rider.getMspr_premium();
					//3. Untuk rider lainnya, rate dihitung per 1000 up
					}else{
						produk.mbu_jumlah = ((rider.getMspr_tsi() * pengali / 1000) * rate) * persen;
					}
					
					BigDecimal bd = new BigDecimal(produk.mbu_jumlah);
					bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					produk.mbu_jumlah = bd.doubleValue();

					/** END OF PROSES PERHITUNGAN RATE PREMI */
					
					// TPD nya platinum link (baru), cuman 5 tahun, bukan 10 tahun (lsbs_id = 182) - Yusuf - 13/09/07
					// trus diset lsbs nya 807, untuk muncul di reportnya
					if(rider.getLsbs_id().intValue() == 812) {
						if(tahun>4) {
							baris.put("R_807", null);
						}else {
							baris.put("R_807", new BigDecimal(produk.mbu_jumlah));
						}
					}else {
						
						//UNTUK RIDER2 YANG NAMANYA SAMA, DISET KE KODE YG LAMA (AGAR TAMPIL DI REPORT)
						String kolom = rider.getLsbs_id().toString();
						if(lsbs == 811) 
							kolom = "806";
						else if(lsbs == 813) 
							kolom = "801";
						else if(lsbs == 810)
							kolom = "800";
							
						//apabila ada cuti premi, tampilkan preminya sampai cuti premi saja
						if(mspo_installment != null) {
							if(tahun >= mspo_installment.intValue()) {
								baris.put("R_" + kolom, null);
							}else {
								baris.put("R_" + kolom, new BigDecimal(produk.mbu_jumlah));
							}
						}else {
							baris.put("R_" + kolom, new BigDecimal(produk.mbu_jumlah));
						}
					}
					
				}
			}
			result.add(baris);
		}
		
		return result;
	}
	
	public String setNilaiManfaat(double idb_premium, int il_cbid, Integer ii_umur_beasiswa, String arg_spaj, String arg_lbs_id, int arg_lbs_number, 
			double arg_value, String arg_kursid, int arg_line, int arg_nourut, int il_age) {
		
		//logger.info("LSBS_ID = " + arg_lbs_id + ", LSDBS_NUMBER = " + arg_lbs_number + ", line = " + arg_line + ", urut = " + arg_nourut);
		
		arg_lbs_id = FormatString.rpad("0", arg_lbs_id, 3);
		
			DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
	
			//Script untuk replace sebagian string (Menampilkan nilai tertentu)
			if(logger.isDebugEnabled()) logger.debug("Setting Nilai Manfaat");
			
			String ls_manfaat, ls_manfaat2, ls_space[] = new String[] {"","","","",""};
			StringBuffer ls_range_age = new StringBuffer();
			StringBuffer ls_table = new StringBuffer();
			Integer li_unit, li_bisnis, ll_period;
			int li_rng_age[] = null ;
			double ld_patok;
			Date ldt_beg_date;
	
			if(logger.isDebugEnabled()) logger.debug("Get Manfaat Message");
			li_bisnis = Integer.valueOf(arg_lbs_id);
			if(li_bisnis==62) li_bisnis=new Integer(52);
//			if(li_bisnis==801 || li_bisnis==803)
//				ls_manfaat = this.uwDao.selectManfaatRider(li_bisnis, arg_lbs_number);
//			else
				ls_manfaat = this.uwDao.selectManfaatPerLine(li_bisnis, arg_lbs_number, arg_nourut, arg_line);
				if(ls_manfaat==null){
					if(uwDao.selectCountSwineFlu(arg_spaj)>0){
						ls_manfaat = "Ketentuan manfaat Asuransi Tambahan ( rider ) tertera pada Syarat-syarat Khusus masing-masing Asuransi Tambahan";
					}
				}
			//logger.info(ls_manfaat);
			
			if(logger.isDebugEnabled()) logger.debug("Get Period");
			Map period = this.uwDao.selectPeriodManfaat(arg_spaj, Integer.parseInt(arg_lbs_id), arg_lbs_number);
			ldt_beg_date = (Date) period.get("MSPR_BEG_DATE");
			ll_period = (Integer) period.get("MSPR_INS_PERIOD");
			li_unit = (Integer) period.get("MSPR_UNIT");
			
			ls_manfaat = ls_manfaat.replaceAll("XX%XX", arg_kursid.equals("01")?"100%":"50%");

			if(logger.isDebugEnabled()) logger.debug("DWIGUNA BERTAHAP IDEAL");
			if(ls_manfaat.indexOf("TTTBBBCCC")>-1 && "015".indexOf(arg_lbs_id) >- 1) {
				ls_manfaat = ls_manfaat.replaceAll("TTTBBBCCC", "adalah " + (arg_kursid.equals("01") ? "41.3%" : "22.4%"));
			}
			
			if(logger.isDebugEnabled()) logger.debug("EKA SISWA");
			if(logger.isDebugEnabled()) logger.debug("Search Result for TTTBBBAAA: " + ls_manfaat.indexOf("TTTBBBAAA"));
			if(ls_manfaat.indexOf("TTTBBBAAA")>-1) {		
				if(il_age>=0 && il_age<=4) {
					li_rng_age = new int[5];
					li_rng_age[0] = 6 - il_age;
					li_rng_age[1] = 12 - il_age;
					li_rng_age[2] = 15 - il_age;
					li_rng_age[3] = 18 - il_age;
					li_rng_age[4] = 19 - il_age;			
				}else if(il_age>=5 && il_age<=10) {
					li_rng_age = new int[4];
					li_rng_age[0] = 12 - il_age;
					li_rng_age[1] = 15 - il_age;
					li_rng_age[2] = 18 - il_age;
					li_rng_age[3] = 19 - il_age;
				}else if(il_age>=11 && il_age<=12) {
					li_rng_age = new int[3];
					li_rng_age[0] = 15 - il_age;
					li_rng_age[1] = 18 - il_age;
					li_rng_age[2] = 19 - il_age;
				}
				ls_range_age.append("");
				
				for(int i=0; i<li_rng_age.length; i++) {
					if(i<(li_rng_age.length-1)) ls_range_age.append("ke " + li_rng_age[i] + ", ");
					else ls_range_age.append("ke " + li_rng_age[i] + " dan seterusnya");
				}
				
				ls_manfaat = ls_manfaat.replaceAll("TTTBBBAAA", ls_range_age.toString());
			}
			
			if(logger.isDebugEnabled()) logger.debug("Search Result for AAABBBTTT: " + ls_manfaat.indexOf("AAABBBTTT"));
			if(ls_manfaat.indexOf("AAABBBTTT")>-1) {
				ls_table.append("");
				if(products.ekaSiswa(arg_lbs_id)) {
					if(arg_lbs_id.equals("033")){
						ls_table.append("Akhir tahun\n");
						ls_table.append("Polis ke-     Pada usia anak    Besarnya Tahapan\n");
						if(il_age<5) {
							ls_table.append((6-il_age)+"               6 tahun           10% UP\n");
						}
						if(il_age<11) {
							if(String.valueOf(12-il_age).length()==1) ls_space[0]=" ";
							ls_table.append((12-il_age)+ls_space[0]+"             12 tahun           10% UP\n");
						}
						if(String.valueOf(15-il_age).length()==1) ls_space[1]=" ";
						if(String.valueOf(18-il_age).length()==1) ls_space[2]=" ";
						if(String.valueOf(23-il_age).length()==1) ls_space[3]=" ";
						ls_table.append((15-il_age)+ls_space[1]+"             15 tahun           10% UP\n");
						ls_table.append((18-il_age)+ls_space[2]+"             18 tahun           70% UP\n");
						ls_table.append((19-il_age)+"-"+(23-il_age)+ls_space[3]+"          19-23 tahun        20% UP/Tahun");
					}else{
						ls_table.append("Akhir tahun\n");
						ls_table.append("Polis ke-     Pada usia anak    Besarnya Tahapan\n");
						if(il_age<5) {
							ls_table.append((6-il_age)+"               6 tahun           10% UP\n");
						}
						if(il_age<11) {
							if(String.valueOf(12-il_age).length()==1) ls_space[0]=" ";
							ls_table.append((12-il_age)+ls_space[0]+"             12 tahun           15% UP\n");
						}
						if(String.valueOf(15-il_age).length()==1) ls_space[1]=" ";
						if(String.valueOf(18-il_age).length()==1) ls_space[2]=" ";
						if(String.valueOf(23-il_age).length()==1) ls_space[3]=" ";
						ls_table.append((15-il_age)+ls_space[1]+"             15 tahun           20% UP\n");
						ls_table.append((18-il_age)+ls_space[2]+"             18 tahun          100% UP\n");
						ls_table.append((19-il_age)+"-"+(23-il_age)+ls_space[3]+"          19-23 tahun        25% UP/Tahun");
					}
				}else if("063,173".indexOf(arg_lbs_id) >- 1) { //eka sarjana mandiri
					if(logger.isDebugEnabled())logger.debug("Eka Sarjana Mandiri - " + arg_lbs_id);
					int lai_usiatahap[] = {12, 15, 18, 19, 20, 21, 22, 25};
					int lai_persentahap[] = null;
					Date ldt_akhir;
					String ls_kurs = "Rp.";				
					if(arg_kursid.equals("01")) lai_persentahap = new int[]{40, 40, 100, 25, 25, 25, 25, 100};
					else if(arg_kursid.equals("02")) {
						lai_persentahap = new int[]{10, 15, 100, 50, 50, 50, 50, 100};
						ls_kurs = "US\\$";
					}
					ls_table.append("Akhir tahun\n");
					ls_table.append("Polis ke-   Pada Tanggal Besarnya Tahapan\n");
					for(int i=0; i<lai_usiatahap.length; i++) {
						if(ii_umur_beasiswa>9 && i==0) continue;
						ll_period = Integer.valueOf(String.valueOf((lai_usiatahap[i] - ii_umur_beasiswa) * 12));
						ldt_akhir = FormatDate.add(ldt_beg_date, Calendar.MONTH, ll_period);
						ls_manfaat2 = df.format(arg_value * lai_persentahap[i] / 100);
						ls_range_age = new StringBuffer();
						ls_range_age.append(FormatString.rpad("0", String.valueOf(lai_usiatahap[i]-ii_umur_beasiswa), 2));
						ls_table.append(ls_range_age.toString() + FormatString.spasi(10) + FormatDate.toString(ldt_akhir) + FormatString.spasi(3) 
								+ ls_kurs + FormatString.spasi(13-ls_manfaat2.length()) + ls_manfaat2 + "\n");
					}
				}else {
					ls_table.append("Akhir tahun\n" + "Polis ke-     Pada usia anak    Besarnya Tahapan\n");
					if(il_age<5) {
						ls_table.append((6-il_age)+"               6 tahun           10% UP\n");
					}
					if(il_age<11) {
						if(String.valueOf(12-il_age).length()==1) ls_space[0]=" ";
						ls_table.append((12-il_age)+ls_space[0]+"             12 tahun           10% UP\n");
					}
					if(String.valueOf(15-il_age).length()==1) ls_space[1]=" ";
					if(String.valueOf(18-il_age).length()==1) ls_space[2]=" ";
					if(String.valueOf(23-il_age).length()==1) ls_space[3]=" ";
					ls_table.append((15-il_age)+ls_space[1]+"             15 tahun           10% UP\n");
					ls_table.append((18-il_age)+ls_space[2]+"             18 tahun          70% UP\n");
					ls_table.append((19-il_age)+"-"+(23-il_age)+ls_space[3]+"          19-23 tahun        20% UP/Tahun");
				}
				if(logger.isDebugEnabled())logger.debug("LS_MANFAAT : " + ls_manfaat);
				//StringUtils.replace(ls_manfaat, "AAABBBTTT", ls_table.toString());
				ls_manfaat = ls_manfaat.replaceAll("AAABBBTTT", ls_table.toString());
				if(logger.isDebugEnabled())logger.debug("LS_MANFAAT : " + ls_manfaat);
			}
			
			//helpdesk [133346] produk baru 142-13 Smart Investment Protection
			if(li_bisnis == 142 && arg_lbs_number == 13 && arg_line == 1){
				StringBuffer ls_add_line = new StringBuffer();
				if(ls_manfaat.indexOf("INILIST1")>-1) {
					ls_add_line.append("");
					ls_add_line.append(" - Meninggal  dunia  dalam 6 (enam)  bulan pertama,  maka  akan  dibayarkan  100%  premi  yang telah\n");
					ls_add_line.append("   dibayarkan.");

					ls_manfaat = ls_manfaat.replaceAll("INILIST1", ls_add_line.toString());
				}
				if(ls_manfaat.indexOf("INILIST2")>-1) {
					ls_add_line = new StringBuffer();
					ls_add_line.append("");
					if(arg_kursid.equalsIgnoreCase("01")){
						ls_add_line.append(" - Meninggal  dunia  setelah 6 (enam)  bulan  pertama, akan dibayarkan  125%  (seratus dua puluh lima\n");
						ls_add_line.append("   perseratus) Uang  Pertanggungan dikurangi manfaat hasil perkembangan investasi yang sudah diambil\n");
						ls_add_line.append("   oleh  tertanggung, dengan  maksimum  Manfaat  Asuransi sebesar IDR 2,500,000,000 (dua miliar lima\n");
						ls_add_line.append("   ratus juta rupiah) bagi Tertanggung  yang  pada  saat  masuk  asuransi berusia 1 (satu) tahun sampai\n");
						ls_add_line.append("   dengan 55 (lima  puluh  lima) tahun,  sedangkan  bagi  Tertanggung  yang  pada saat  masuk asuransi\n");
						ls_add_line.append("   berusia diatas 55 (lima puluh lima) tahun maksimum Manfaat Asuransi yang diperoleh  adalah sebesar\n");
						ls_add_line.append("   IDR 2,000,000,000 (dua miliar  rupiah) dan selanjutnya  pertanggungan berakhir.");
										  //-------------------------------------------------------------------------------------------------------
					}else if(arg_kursid.equalsIgnoreCase("02")){ //helpdesk [146169] Perbaikan Tampilan Manfaat USD SIP
						ls_add_line.append(" - Meninggal  dunia  setelah 6 (enam)  bulan  pertama, akan dibayarkan 105% (seratus lima perseratus)\n");
						ls_add_line.append("   Uang   Pertanggungan   dikurangi  manfaat   hasil  perkembangan  investasi  yang  sudah diambil oleh\n");
						ls_add_line.append("   tertanggung, dengan maksimum  Manfaat  Asuransi  sebesar USD 140,000 (seratus  empat puluh ribu\n");
						ls_add_line.append("   USD)  bagi Tertanggung yang  pada  saat  masuk  asuransi  berusia 1 (satu)  tahun sampai dengan 55\n");
						ls_add_line.append("   (lima puluh lima) tahun, sedangkan bagi Tertanggung yang  pada saat  masuk asuransi berusia diatas\n");
						ls_add_line.append("   55 (lima puluh lima) tahun maksimum Manfaat Asuransi yang  diperoleh adalah sebesar  USD 110,000\n");
						ls_add_line.append("   (seratus sepuluh ribu USD) dan selanjutnya pertanggungan berakhir.");
										  //-------------------------------------------------------------------------------------------------------
					}					
					ls_manfaat = ls_manfaat.replaceAll("INILIST2", ls_add_line.toString());
				}
			} else if(li_bisnis == 142 && arg_lbs_number == 13 && arg_line == 2){ //helpdesk [146169] Perbaikan Tampilan Manfaat USD SIP
				if(ls_manfaat.indexOf("XCPERSEN")>-1) {
					if(arg_kursid.equalsIgnoreCase("01")){
						ls_manfaat = ls_manfaat.replaceAll("XCPERSEN", "150% (seratus lima puluh perseratus)");
					} else if(arg_kursid.equalsIgnoreCase("02")){
						ls_manfaat = ls_manfaat.replaceAll("XCPERSEN", "110% (seratus sepuluh perseratus)");
					}
				}
				if(ls_manfaat.indexOf("XCNILAI")>-1) {
					if(arg_kursid.equalsIgnoreCase("01")){
						ls_manfaat = ls_manfaat.replaceAll("XCNILAI", "IDR 3,000,000,000 (tiga miliar rupiah)");
					} else if(arg_kursid.equalsIgnoreCase("02")){
						ls_manfaat = ls_manfaat.replaceAll("XCNILAI", "USD 145,000 (seratus empat puluh lima ribu USD)");
					}
				}
			}
			
			if(ls_manfaat.indexOf(">>")>-1) {
				if("800".equals(arg_lbs_id)) { //PA
					ls_manfaat = ls_manfaat.replaceAll(">>", (arg_kursid.equals("01")?"Rp. ":"US\\$ ")+df.format(li_unit * arg_value));
					if(il_cbid==0) {
						ls_manfaat+= "Lama pertanggungan PAR : " + ll_period + " Tahun";
					}
				}else if("803".equals(arg_lbs_id)) { //Term
					ls_manfaat = ls_manfaat.replaceAll(">>", (li_unit*100) + " %");
					ls_manfaat = ls_manfaat.replaceAll("<<", String.valueOf(ll_period));
				}
			}
			
			if("069,082".indexOf(arg_lbs_id)>-1) {
				int li_count=0;
				double ld_tbnf;
				String ls_th[] = null, ls_prd=null, ls_bonus="";
				ld_tbnf = 1000000000;
				if(arg_kursid.equals("02")) ld_tbnf = 100000;
				if(arg_lbs_number==1) {
					ls_th = new String[] {"5 (lima)", "5 (lima)", "5 (lima)"};
					ls_prd = "8 (delapan)";
					if(arg_kursid.equals("01")) {
						ls_bonus = "20";
						li_count = 10;
					}else {
						ls_bonus = "2.5";
						li_count = 1;
					}
				}else if(arg_lbs_number==2) {
					ls_th = new String[] {"5 (lima)", "6 (enam)", "7 (tujuh)"};
					ls_prd = "10 (sepuluh)";
					if(arg_kursid.equals("01")) {
						ls_bonus = "40";
						if(arg_lbs_id.equals("082"))ls_bonus = "33";
						li_count=20;
					}else {
						ls_bonus = "18";
						if(arg_lbs_id.equals("082"))ls_bonus = "12.5";
						li_count=9;
					}
				}else if(arg_lbs_number==3) {
					ls_th = new String[] {"7 (tujuh)", "8 (delapan)", "9 (sembilan)"};
					ls_prd = "12 (dua belas)";
					if(arg_kursid.equals("01")) {
						ls_bonus = "50";
						if(arg_lbs_id.equals("082"))ls_bonus = "43";
						li_count=25;
					}else {
						ls_bonus = "24";
						if(arg_lbs_id.equals("082"))ls_bonus = "18";
						li_count=12;
					}
				}
				ls_manfaat = ls_manfaat.replaceAll("INV2", ls_th[2]);
				ls_manfaat = ls_manfaat.replaceAll("PPB", ls_bonus+" %");
				ls_manfaat = ls_manfaat.replaceAll("INV3", ls_th[2]);
				ls_manfaat = ls_manfaat.replaceAll("PMX", (100+li_count)+" % ");
				ls_manfaat = ls_manfaat.replaceAll("MAX", arg_kursid.equals("01")?("Rp. "+df.format(ld_tbnf)+" (Satu Milyar Rupiah) "):("US\\$. "+df.format(ld_tbnf)+" (Seratus Ribu US Dollar) "));
				ls_manfaat = ls_manfaat.replaceAll("INV4", ls_prd);			
			}
			
			if(arg_lbs_id.equals("801")) {
				ld_patok = 100000;
				if(arg_kursid.equals("01")) ld_patok = 300000000;
				ls_manfaat = ls_manfaat.replaceAll("RRR", (arg_kursid.equals("01")?"Rp. ":"US\\$. ")+df.format(arg_value/2<=ld_patok?arg_value/2:ld_patok));
				ls_manfaat = ls_manfaat.replaceAll("OOO", (arg_kursid.equals("01")?"Rp. ":"US\\$. ")+df.format(arg_value-(arg_value/2<=ld_patok?arg_value/2:ld_patok)));
			}
	
			return ls_manfaat;
		}
	
	public Map<String, Object> prosesCetakManfaat(String spaj, String lus_id) throws Exception{
		
		Map info = uwDao.selectInfoCetakManfaat(spaj);
		int il_cbid = ((Integer) info.get("lscb_id"));
		int il_age = ((Integer) info.get("mste_age"));
		
		int tamb = -1; //ini untuk tambahan klausula manfaat

		String businessId=FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		
		String special = "";
		if("046".equals(businessId) && il_cbid!=0 && il_age==56) { 
			//Special case 7/01/2002
			special = "true";
		}else if("053,054,067,131,132,148".indexOf(businessId)>-1) {
			special = "54";
			//Yusuf (24/05/2007) - untuk manfaat PA sekarang ambil dari LST_MANFAAT
//		}else if("073".equals(businessId)) {
//			special = "73";
		}		
		
		Map params = new HashMap();
		int urut=-1;
		
		String lku_id = (String) info.get("lku_id");
		int il_mspo_age = ((Integer) info.get("mspo_age"));
		int lsdbs = ((Integer) info.get("lsdbs_number"));
		String lsdbs_name = (String) info.get("lsdbs_name");
		Double idb_premium = (Double) info.get("mspr_premium");
		Integer payPeriod = (Integer) info.get("mspo_pay_period");
		int insPeriod = nilaiTunai.getLamaTanggung(((Integer) info.get("mspo_ins_period")), businessId, lku_id);
		int li_medical = ((Integer) info.get("mste_medical"));
		Integer ii_umur_beasiswa = (Integer) info.get("mspo_umur_beasiswa");
		Integer lsre_id = (Integer) info.get("lsre_id");
		Double mspr_tsi = (Double) info.get("mspr_tsi");
		
		if(logger.isDebugEnabled())logger.debug("Initializing Printing Manfaat");

		//Yusuf (24/05/2007) - untuk manfaat PA sekarang ambil dari LST_MANFAAT
//		if("073".equals(businessId)) {
//			if(special.equals("73")) {
//				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case73"));
//				List temp = new ArrayList(); Map map = new HashMap();
//				map.put("1", "1"); temp.add(map);
//				
//				return params;
//			}
//		}else 
		if("074,076,096,099,135,136,182".indexOf(businessId)>-1 ||(businessId.equals("111") && lsdbs==2)) {
			return prosesManfaat7476(lus_id, spaj, il_age, businessId, lsdbs, il_cbid, idb_premium, ii_umur_beasiswa, lku_id);
		}else if("079,091".indexOf(businessId)>-1) {
			return prosesManfaat7991(lus_id, spaj, il_age, businessId, lsdbs, il_cbid, idb_premium, ii_umur_beasiswa, lku_id);
		}else if("080,092".indexOf(businessId)>-1) {
			params.put("jenis", 1);
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case80"));
			return params;
		}else if("081".equals(businessId)) {
			return prosesManfaat81(il_cbid, lku_id);				
		}else if("213,217,218".indexOf(businessId)>-1) {
			return prosesManfaat100(businessId,lsdbs);				
		}else if("215".equals(businessId)) {
			return prosesManfaat100(businessId,lsdbs);				
		}else if("171".equals(businessId) && lsdbs==2){
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_hcp"));
			return params;
		}else if("195".equals(businessId) || "204".equals(businessId)){
			if("204".equals(businessId)){
				if (lsdbs >= 37 && lsdbs <= 48){
					params.put("jenis",5);//5= MARZ, VALDO, GOS , VASCO, SYNERGYS, SSI / DMTM New SIO
				}else{
					params.put("jenis",1);//1= syariah,0= konven
				}
			}
			if("195".equals(businessId)&& (lsdbs>=13  &&  lsdbs<=24)){
				params.put("jenis",2);//2 Untuk Hospital care
			}
			if("195".equals(businessId)&& ((lsdbs>=61  &&  lsdbs<=72)||(lsdbs>=73  &&  lsdbs<=84))){
				params.put("jenis",4);//4 Untuk SMile Hospital Care (DKI)
			}
			if(lsdbs%12==1){
				params.put("tipe", "R-100");
				params.put("biayainap", "100,000");
			}else if(lsdbs%12==2){
				params.put("tipe", "R-200");
				params.put("biayainap", "200,000");
			}else if(lsdbs%12==3){
				params.put("tipe", "R-300");
				params.put("biayainap", "300,000");
			}else if(lsdbs%12==4){
				params.put("tipe", "R-400");
				params.put("biayainap", "400,000");
			}else if(lsdbs%12==5){
				params.put("tipe", "R-500");
				params.put("biayainap", "500,000");
			}else if(lsdbs%12==6){
				params.put("tipe", "R-600");
				params.put("biayainap", "600,000");
			}else if(lsdbs%12==7){
				params.put("tipe", "R-700");
				params.put("biayainap", "700,000");
			}else if(lsdbs%12==8){
				params.put("tipe", "R-800");
				params.put("biayainap", "800,000");
			}else if(lsdbs%12==9){
				params.put("tipe", "R-900");
				params.put("biayainap", "900,000");
			}else if(lsdbs%12==10){
				params.put("tipe", "R-1000");
				params.put("biayainap", "1,000,000");
			}else if(lsdbs%12==11){
				params.put("tipe", "R-1500");
				params.put("biayainap", "1,500,000");
			}else if(lsdbs%12==0){
				params.put("tipe", "R-2000");
				params.put("biayainap", "2,000,000");
			}
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_hcp_prov"));
			return params;
		}else if("178".equals(businessId)){
			return prosesManfaat178(lus_id, spaj, il_age, businessId, lsdbs, il_cbid, idb_premium, ii_umur_beasiswa, lku_id);
		}else if("208".equals(businessId)){
			return prosesManfaat208(lus_id, spaj, il_age, businessId, lsdbs, il_cbid, idb_premium, ii_umur_beasiswa, lku_id, mspr_tsi);
		}else if("183".equals(businessId) || "189".equals(businessId) || "193".equals(businessId) ){
			if(("183".equals(businessId) && lsdbs<=120 ) && (lsdbs>=91 && lsdbs<=105)) lsdbs_name = "VIP MEDICAL PLAN";
			if(lsdbs%15==1){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "A");
				params.put("biayainap", "100,000");
				params.put("biayaicu", "200,000");
				params.put("bataslimit", "12,500,000");
				params.put("batasmax", "50,000,000");
			}else if(lsdbs%15==2){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "B");
				params.put("biayainap", "150,000");
				params.put("biayaicu", "300,000");
				params.put("bataslimit", "18,750,000");
				params.put("batasmax", "75,000,000");
			}else if(lsdbs%15==3){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "C");
				params.put("biayainap", "200,000");
				params.put("biayaicu", "400,000");
				params.put("bataslimit", "25,000,000");
				params.put("batasmax", "100,000,000");
			}else if(lsdbs%15==4){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "D");
				params.put("biayainap", "250,000");
				params.put("biayaicu", "500,000");
				params.put("bataslimit", "31,250,000");
				params.put("batasmax", "125,000,000");
			}else if(lsdbs%15==5){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "E");
				params.put("biayainap", "300,000");
				params.put("biayaicu", "600,000");
				params.put("bataslimit", "37,500,000");
				params.put("batasmax", "150,000,000");
			}else if(lsdbs%15==6){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "F");
				params.put("biayainap", "350,000");
				params.put("biayaicu", "700,000");
				params.put("bataslimit", "43,750,000");
				params.put("batasmax", "175,000,000");
			}else if(lsdbs%15==7){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "G");
				params.put("biayainap", "400,000");
				params.put("biayaicu", "800,000");
				params.put("bataslimit", "50,000,000");
				params.put("batasmax", "200,000,000");
			}else if(lsdbs%15==8){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "H");
				params.put("biayainap", "500,000");
				params.put("biayaicu", "1,000,000");
				params.put("bataslimit", "100,000,000");
				params.put("batasmax", "400,000,000");
			}else if(lsdbs%15==9){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "I");
				params.put("biayainap", "600,000");
				params.put("biayaicu", "1,200,000");
				params.put("bataslimit", "125,000,000");
				params.put("batasmax", "500,000,000");
			}else if(lsdbs%15==10){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "J");
				params.put("biayainap", "700,000");
				params.put("biayaicu", "1,400,000");
				params.put("bataslimit", "150,000,000");
				params.put("batasmax", "600,000,000");
			}else if(lsdbs%15==11){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "K");
				params.put("biayainap", "800,000");
				params.put("biayaicu", "1,600,000");
				params.put("bataslimit", "175,000,000");
				params.put("batasmax", "700,000,000");
			}else if(lsdbs%15==12){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "L");
				params.put("biayainap", "900,000");
				params.put("biayaicu", "1,800,000");
				params.put("bataslimit", "200,000,000");
				params.put("batasmax", "800,000,000");
			}else if(lsdbs%15==13){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "M");
				params.put("biayainap", "1,000,000");
				params.put("biayaicu", "2,000,000");
				params.put("bataslimit", "225,000,000");
				params.put("batasmax", "900,000,000");
			}else if(lsdbs%15==14){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "N");
				params.put("biayainap", "1,500,000");
				params.put("biayaicu", "3,000,000");
				params.put("bataslimit", "350,000,000");
				params.put("batasmax", "1,400,000,000");
			}else if(lsdbs%15==0){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "O");
				params.put("biayainap", "2,000,000");
				params.put("biayaicu", "4,000,000");
				params.put("bataslimit", "475,000,000");
				params.put("batasmax", "1,900,000,000");
			}
			
			if("189".equals(businessId) && lsdbs >= 33 && lsdbs <= 47){ // MARZ VALDO GOS VASCO DENA SYNERGYS, SSI , AUSINDO PRATAMA KARYA(DMTM SIO)
				if(lsdbs%16==1){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "A");
					params.put("biayainap", "100,000");
					params.put("biayaicu", "200,000");
					params.put("bataslimit", "12,500,000");
					params.put("batasmax", "50,000,000");
				}else if(lsdbs%16==2){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "B");
					params.put("biayainap", "150,000");
					params.put("biayaicu", "300,000");
					params.put("bataslimit", "18,750,000");
					params.put("batasmax", "75,000,000");
				}else if(lsdbs%16==3){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "C");
					params.put("biayainap", "200,000");
					params.put("biayaicu", "400,000");
					params.put("bataslimit", "25,000,000");
					params.put("batasmax", "100,000,000");
				}else if(lsdbs%16==4){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "D");
					params.put("biayainap", "250,000");
					params.put("biayaicu", "500,000");
					params.put("bataslimit", "31,250,000");
					params.put("batasmax", "125,000,000");
				}else if(lsdbs%16==5){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "E");
					params.put("biayainap", "300,000");
					params.put("biayaicu", "600,000");
					params.put("bataslimit", "37,500,000");
					params.put("batasmax", "150,000,000");
				}else if(lsdbs%16==6){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "F");
					params.put("biayainap", "350,000");
					params.put("biayaicu", "700,000");
					params.put("bataslimit", "43,750,000");
					params.put("batasmax", "175,000,000");
				}else if(lsdbs%16==7){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "G");
					params.put("biayainap", "400,000");
					params.put("biayaicu", "800,000");
					params.put("bataslimit", "50,000,000");
					params.put("batasmax", "200,000,000");
				}else if(lsdbs%16==8){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "H");
					params.put("biayainap", "500,000");
					params.put("biayaicu", "1,000,000");
					params.put("bataslimit", "100,000,000");
					params.put("batasmax", "400,000,000");
				}else if(lsdbs%16==9){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "I");
					params.put("biayainap", "600,000");
					params.put("biayaicu", "1,200,000");
					params.put("bataslimit", "125,000,000");
					params.put("batasmax", "500,000,000");
				}else if(lsdbs%16==10){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "J");
					params.put("biayainap", "700,000");
					params.put("biayaicu", "1,400,000");
					params.put("bataslimit", "150,000,000");
					params.put("batasmax", "600,000,000");
				}else if(lsdbs%16==11){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "K");
					params.put("biayainap", "800,000");
					params.put("biayaicu", "1,600,000");
					params.put("bataslimit", "175,000,000");
					params.put("batasmax", "700,000,000");
				}else if(lsdbs%16==12){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "L");
					params.put("biayainap", "900,000");
					params.put("biayaicu", "1,800,000");
					params.put("bataslimit", "200,000,000");
					params.put("batasmax", "800,000,000");
				}else if(lsdbs%16==13){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "M");
					params.put("biayainap", "1,000,000");
					params.put("biayaicu", "2,000,000");
					params.put("bataslimit", "225,000,000");
					params.put("batasmax", "900,000,000");
				}else if(lsdbs%16==14){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "N");
					params.put("biayainap", "1,500,000");
					params.put("biayaicu", "3,000,000");
					params.put("bataslimit", "350,000,000");
					params.put("batasmax", "1,400,000,000");
				}else if(lsdbs%16==15){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "O");
					params.put("biayainap", "2,000,000");
					params.put("biayaicu", "4,000,000");
					params.put("bataslimit", "475,000,000");
					params.put("batasmax", "1,900,000,000");
				}
			}
			//helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
			if("189".equals(businessId) && (lsdbs >= 48 && lsdbs <= 62)){ 
				if(lsdbs == 48){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "A");
					params.put("biayainap", "100,000");
					params.put("biayaicu", "200,000");
					params.put("bataslimit", "12,500,000");
					params.put("batasmax", "50,000,000");
				}else if(lsdbs == 49){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "B");
					params.put("biayainap", "150,000");
					params.put("biayaicu", "300,000");
					params.put("bataslimit", "18,750,000");
					params.put("batasmax", "75,000,000");
				}else if(lsdbs == 50){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "C");
					params.put("biayainap", "200,000");
					params.put("biayaicu", "400,000");
					params.put("bataslimit", "25,000,000");
					params.put("batasmax", "100,000,000");
				}else if(lsdbs == 51){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "D");
					params.put("biayainap", "250,000");
					params.put("biayaicu", "500,000");
					params.put("bataslimit", "31,250,000");
					params.put("batasmax", "125,000,000");
				}else if(lsdbs == 52){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "E");
					params.put("biayainap", "300,000");
					params.put("biayaicu", "600,000");
					params.put("bataslimit", "37,500,000");
					params.put("batasmax", "150,000,000");
				}else if(lsdbs == 53){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "F");
					params.put("biayainap", "350,000");
					params.put("biayaicu", "700,000");
					params.put("bataslimit", "43,750,000");
					params.put("batasmax", "175,000,000");
				}else if(lsdbs == 54){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "G");
					params.put("biayainap", "400,000");
					params.put("biayaicu", "800,000");
					params.put("bataslimit", "50,000,000");
					params.put("batasmax", "200,000,000");
				}else if(lsdbs == 55){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "H");
					params.put("biayainap", "500,000");
					params.put("biayaicu", "1,000,000");
					params.put("bataslimit", "100,000,000");
					params.put("batasmax", "400,000,000");
				}else if(lsdbs == 56){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "I");
					params.put("biayainap", "600,000");
					params.put("biayaicu", "1,200,000");
					params.put("bataslimit", "125,000,000");
					params.put("batasmax", "500,000,000");
				}else if(lsdbs == 57){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "J");
					params.put("biayainap", "700,000");
					params.put("biayaicu", "1,400,000");
					params.put("bataslimit", "150,000,000");
					params.put("batasmax", "600,000,000");
				}else if(lsdbs == 58){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "K");
					params.put("biayainap", "800,000");
					params.put("biayaicu", "1,600,000");
					params.put("bataslimit", "175,000,000");
					params.put("batasmax", "700,000,000");
				}else if(lsdbs == 59){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "L");
					params.put("biayainap", "900,000");
					params.put("biayaicu", "1,800,000");
					params.put("bataslimit", "200,000,000");
					params.put("batasmax", "800,000,000");
				}else if(lsdbs == 60){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "M");
					params.put("biayainap", "1,000,000");
					params.put("biayaicu", "2,000,000");
					params.put("bataslimit", "225,000,000");
					params.put("batasmax", "900,000,000");
				}else if(lsdbs == 61){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "N");
					params.put("biayainap", "1,500,000");
					params.put("biayaicu", "3,000,000");
					params.put("bataslimit", "350,000,000");
					params.put("batasmax", "1,400,000,000");
				}else if(lsdbs == 62){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "O");
					params.put("biayainap", "2,000,000");
					params.put("biayaicu", "4,000,000");
					params.put("bataslimit", "475,000,000");
					params.put("batasmax", "1,900,000,000");
				}
			}
			
			if("193".equals(businessId)){
				if(lsdbs==1 ){
					params.put("biayaaneka", "1,750,000");
					params.put("biayabedah", "12,000,000");
					params.put("biayakunjung", "50,000");
					params.put("biayakonsul", "300,000");
					params.put("biayaperawat", "50,000");
					params.put("biayaambulance", "40,000");
					params.put("biayabafter", "350,000");
				}else if(lsdbs==2){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("biayaaneka", "2,250,000");
					params.put("biayabedah", "17,250,000");
					params.put("biayakunjung", "60,000");
					params.put("biayakonsul", "360,000");
					params.put("biayaperawat", "60,000");
					params.put("biayaambulance", "45,000");
					params.put("biayabafter", "500,000");
				}else if(lsdbs==3){
					params.put("biayaaneka", "2,750,000");
					params.put("biayabedah", "22,500,000");
					params.put("biayakunjung", "70,000");
					params.put("biayakonsul", "420,000");
					params.put("biayaperawat", "70,000");
					params.put("biayaambulance", "50,000");
					params.put("biayabafter", "650,000");
				}else if(lsdbs==4){
					params.put("biayaaneka", "3,250,000");
					params.put("biayabedah", "27,500,000");
					params.put("biayakunjung", "80,000");
					params.put("biayakonsul", "480,000");
					params.put("biayaperawat", "80,000");
					params.put("biayaambulance", "55,000");
					params.put("biayabafter", "800,000");
				}else if(lsdbs==5){
					params.put("biayaaneka", "3,750,000");
					params.put("biayabedah", "33,000,000");
					params.put("biayakunjung", "90,000");
					params.put("biayakonsul", "540,000");
					params.put("biayaperawat", "90,000");
					params.put("biayaambulance", "60,000");
					params.put("biayabafter", "950,000");
				}else if(lsdbs==6){
					params.put("biayaaneka", "4,250,000");
					params.put("biayabedah", "38,250,000");
					params.put("biayakunjung", "100,000");
					params.put("biayakonsul", "600,000");
					params.put("biayaperawat", "100,000");
					params.put("biayaambulance", "65,000");
					params.put("biayabafter", "1,100,000");
				}else if(lsdbs==7){
					params.put("biayaaneka", "4,750,000");
					params.put("biayabedah", "43,500,000");
					params.put("biayakunjung", "110,000");
					params.put("biayakonsul", "660,000");
					params.put("biayaperawat", "110,000");
					params.put("biayaambulance", "70,000");
					params.put("biayabafter", "1,250,000");
				}else if(lsdbs==8){
					params.put("biayaaneka", "5,750,000");
					params.put("biayabedah", "54,000,000");
					params.put("biayakunjung", "130,000");
					params.put("biayakonsul", "780,000");
					params.put("biayaperawat", "130,000");
					params.put("biayaambulance", "80,000");
					params.put("biayabafter", "1,550,000");
				}else if(lsdbs==9){
					params.put("biayaaneka", "6,750,000");
					params.put("biayabedah", "64,500,000");
					params.put("biayakunjung", "150,000");
					params.put("biayakonsul", "900,000");
					params.put("biayaperawat", "150,000");
					params.put("biayaambulance", "90,000");
					params.put("biayabafter", "1,850,000");
				}else if(lsdbs==10){
					params.put("biayaaneka", "7,750,000");
					params.put("biayabedah", "75,000,000");
					params.put("biayakunjung", "170,000");
					params.put("biayakonsul", "1,020,000");
					params.put("biayaperawat", "170,000");
					params.put("biayaambulance", "100,000");
					params.put("biayabafter", "2,150,000");
				}else if(lsdbs==11){
					params.put("biayaaneka", "8,750,000");
					params.put("biayabedah", "85,500,000");
					params.put("biayakunjung", "190,000");
					params.put("biayakonsul", "1,140,000");
					params.put("biayaperawat", "190,000");
					params.put("biayaambulance", "110,000");
					params.put("biayabafter", "2,450,000");
				}else if(lsdbs==12){
					params.put("biayaaneka", "9,750,000");
					params.put("biayabedah", "96,000,000");
					params.put("biayakunjung", "210,000");
					params.put("biayakonsul", "1,260,000");
					params.put("biayaperawat", "210,000");
					params.put("biayaambulance", "120,000");
					params.put("biayabafter", "2,750,000");
				}else if(lsdbs==13){
					params.put("biayaaneka", "10,750,000");
					params.put("biayabedah", "106,500,000");
					params.put("biayakunjung", "230,000");
					params.put("biayakonsul", "1,380,000");
					params.put("biayaperawat", "230,000");
					params.put("biayaambulance", "130,000");
					params.put("biayabafter", "3,050,000");
				}else if(lsdbs==14){
					params.put("biayaaneka", "15,750,000");
					params.put("biayabedah", "159,000,000");
					params.put("biayakunjung", "330,000");
					params.put("biayakonsul", "1,980,000");
					params.put("biayaperawat", "330,000");
					params.put("biayaambulance", "180,000");
					params.put("biayabafter", "4,550,000");
				}else if(lsdbs==15){
					params.put("biayaaneka", "20,750,000");
					params.put("biayabedah", "211,500,000");
					params.put("biayakunjung", "430,000");
					params.put("biayakonsul", "2,580,000");
					params.put("biayaperawat", "430,000");
					params.put("biayaambulance", "230,000");
					params.put("biayabafter", "6,050,000");
				}
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_eka_sehat_il"));
			}else{
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_eka_sehat"));
			}
			return params;
		}else if(("183".equals(businessId) && (lsdbs>=121 && lsdbs<=122))){
			if(lsdbs==121){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "P");
				params.put("biayainap", "5,000,000");
				params.put("biayaicu", "4,000,000");
				params.put("bataslimit", "350,000,000");
				params.put("batasmax", "6,000,000,000");
			}else if(lsdbs==122){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "Q");
				params.put("biayainap", "7,500,000");
				params.put("biayaicu", "4,000,000");
				params.put("bataslimit", "475,000,000");
				params.put("batasmax", "6,000,000,000");
			}
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_eka_sehat"));
			return params;
		}else if("214".equals(businessId) || "225".equals(businessId) ){
			if((lsdbs%15==1 || lsdbs%15==7) && "214".equals(businessId) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "GOLD");
				params.put("biayainap", "1,500,000");
				params.put("biayaicu", "3,000,000");
				params.put("kemoterapi", "31,500,000");
				params.put("cucidarah", "31,500,000");
				params.put("santunan", "22,500,000");
				params.put("meninggal", "375,000,000");
				params.put("batasmax", "1,400,000,000");
				
			}else if((lsdbs%15==2 || lsdbs%15==9) && "214".equals(businessId)
					|| ( lsdbs==6 &&  "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "PLATINUM");
				params.put("biayainap", "2,000,000");
				params.put("biayaicu", "4,000,000");
				params.put("kemoterapi", "41,500,000");
				params.put("cucidarah", "41,500,000");
				params.put("santunan", "30,000,000");
				params.put("meninggal", "500,000,000");
				params.put("batasmax", "1,900,000,000");
				
			}else if((lsdbs%15==3 && "214".equals(businessId))){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "COPPER");
				params.put("biayainap", "500,000");
				params.put("biayaicu", "1,000,000");
				params.put("kemoterapi", "11,500,000");
				params.put("cucidarah", "11,500,000");
				params.put("santunan", "7,500,000");
				params.put("meninggal", "125,000,000");
				params.put("batasmax", "400,000,000");
				
			}else if((lsdbs==1 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "COPPER");
				params.put("biayainap", "400,000");
				params.put("biayaicu", "800,000");
				params.put("kemoterapi", "9,400,000");
				params.put("cucidarah", "9,400,000");
				params.put("santunan", "6,000,000");
				params.put("meninggal", "100,000,000");
				params.put("batasmax", "320,000,000");
				
			}else if((lsdbs%15==4 && "214".equals(businessId)) || (lsdbs==2 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "BRONZE");
				params.put("biayainap", "800,000");
				params.put("biayaicu", "1,600,000");
				params.put("kemoterapi", "18,500,000");
				params.put("cucidarah", "18,500,000");
				params.put("santunan", "12,000,000");
				params.put("meninggal", "200,000,000");
				params.put("batasmax", "700,000,000");
				
			}else if((lsdbs%15==5 && "214".equals(businessId)) || (lsdbs==3 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "SILVER");
				params.put("biayainap", "1,000,000");
				params.put("biayaicu", "2,000,000");
				params.put("kemoterapi", "21,500,000");
				params.put("cucidarah", "21,500,000");
				params.put("santunan", "15,000,000");
				params.put("meninggal", "250,000,000");
				params.put("batasmax", "900,000,000");
				
			}else if((lsdbs%15==6 && "214".equals(businessId)) || (lsdbs==4 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "SHAPPIRE");
				params.put("biayainap", "1,200,000");
				params.put("biayaicu", "2,400,000");
				params.put("kemoterapi", "28,000,000");
				params.put("cucidarah", "28,000,000");
				params.put("santunan", "18,000,000");
				params.put("meninggal", "300,000,000");
				params.put("batasmax", "1,000,000,000");
				
			}else if((lsdbs%15==8 && "214".equals(businessId)) || (lsdbs==5 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "DIAMOND");
				params.put("biayainap", "1,600,000");
				params.put("biayaicu", "3,200,000");
				params.put("kemoterapi", "37,000,000");
				params.put("cucidarah", "37,000,000");
				params.put("santunan", "24,000,000");
				params.put("meninggal", "400,000,000");
				params.put("batasmax", "1,500,000,000");
			}
			params.put("spaj",spaj);
			
			String subJnProv[] = new String[] {};
			
			if("214".equals(businessId)){ //start provestara 214 RJ RG RB PK
				
				params.put("catatanri", " ");	
				
				List  manfaatrj = this.uwDao.selectPlanProvestara(spaj, 840);
				List  manfaatrg = this.uwDao.selectPlanProvestara(spaj, 841);
				List  manfaatrb = this.uwDao.selectPlanProvestara(spaj, 842);
				List  manfaatpk = this.uwDao.selectPlanProvestara(spaj, 843);
			
				
				if(manfaatrj.size()!=0) {				
					
					if(lsdbs%15==1 || lsdbs%15==7){
						
						params.put("kondokum", "125,000");
						params.put("kondospe", "375,000");
						params.put("konob", "250,000");
						params.put("obresdok", "8,750,000");
						params.put("biayates", "2,500,000");
						params.put("fisioterapirj", "190,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "12,500,000");	
						
					}else if(lsdbs%15==2 || lsdbs%15==9){
						params.put("kondokum", "150,000");
						params.put("kondospe", "450,000");
						params.put("konob", "300,000");
						params.put("obresdok", "10,500,000");
						params.put("biayates", "3,000,000");
						params.put("fisioterapirj", "225,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "15,000,000");	
						
					}else if(lsdbs%15== 3 ){
						params.put("kondokum", "75,000");
						params.put("kondospe", "225,000");
						params.put("konob", "150,000");
						params.put("obresdok", "5,250,000");
						params.put("biayates", "1,500,000");
						params.put("fisioterapirj", "115,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "7,500,000");	
						
					}else if(lsdbs%15== 4 ){
						params.put("kondokum", "90,000");
						params.put("kondospe", "270,000");
						params.put("konob", "180,000");
						params.put("obresdok", "6,300,000");
						params.put("biayates", "1,800,000");
						params.put("fisioterapirj", "140,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "9,000,000");	
						
					}else if(lsdbs%15== 5 ){
						params.put("kondokum", "100,000");
						params.put("kondospe", "300,000");
						params.put("konob", "200,000");
						params.put("obresdok", "7,000,000");
						params.put("biayates", "2,000,000");
						params.put("fisioterapirj", "150,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "10,000,000");	
						
					}else if(lsdbs%15== 6 ){
						params.put("kondokum", "110,000");
						params.put("kondospe", "320,000");
						params.put("konob", "220,000");
						params.put("obresdok", "7,700,000");
						params.put("biayates", "2,200,000");
						params.put("fisioterapirj", "170,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "11,000,000");	
						
					}else if(lsdbs%15== 8 ){
						params.put("kondokum", "135,000");
						params.put("kondospe", "405,000");
						params.put("konob", "270,000");
						params.put("obresdok", "9,450,000");
						params.put("biayates", "2,700,000");
						params.put("fisioterapirj", "210,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "14,000,000");	
						
					}
					
					params.put("catatanrj", "\nCatatan Rawat Jalan (RJ) :" +
							 "\n- Berlaku Masa tunggu untuk Pre-existing Condition dan Penyakit-Penyakit Khusus"+		
							 "\n- Penggantian 100%"+		
							 "\n- Dokter Spesialis TANPA rujukan dari dokter umum"+
							 "\n* Hanya untuk Tertanggung sampai usia  64 tahun");	
					
					params.put("subplanrj", JasperReportsUtils.convertReportData(manfaatrj));
					//StringUtils.addStringToArray(subJnProv, "subplanrj");
				}
				if(manfaatrg.size()!=0) {
									
					if(lsdbs%15==1 || lsdbs%15==7){
						
						params.put("pdasar", "1,500,000");
						params.put("pgusi", "1,000,000");
						params.put("pcegah", "1,500,000");
						params.put("pkomplek", "1,500,000");
						params.put("pperbaik", "750,000");
						params.put("pgigi", "1,500,000");
						params.put("batasmaxrg", "7,500,000");	
						
					}else if(lsdbs%15==2 || lsdbs%15==9){
						params.put("pdasar", "2,000,000");
						params.put("pgusi", "1,250,000");
						params.put("pcegah", "2,000,000");
						params.put("pkomplek", "2,000,000");
						params.put("pperbaik", "1,050,000");
						params.put("pgigi", "2,000,000");
						params.put("batasmaxrg", "10,000,000");	
					
					}else if(lsdbs%15==3){
						params.put("pdasar", "500,000");
						params.put("pgusi", "350,000");
						params.put("pcegah", "500,000");
						params.put("pkomplek", "500,000");
						params.put("pperbaik", "250,000");
						params.put("pgigi", "500,000");
						params.put("batasmaxrg", "2,500,000");	
					
					}else if(lsdbs%15==4){
						params.put("pdasar", "750,000");
						params.put("pgusi", "490,000");
						params.put("pcegah", "750,000");
						params.put("pkomplek", "750,000");
						params.put("pperbaik", "380,000");
						params.put("pgigi", "750,000");
						params.put("batasmaxrg", "3,750,000");	
					
					}else if(lsdbs%15==5){
						params.put("pdasar", "1,000,000");
						params.put("pgusi", "625,000");
						params.put("pcegah", "1,000,000");
						params.put("pkomplek", "1,000,000");
						params.put("pperbaik", "500,000");
						params.put("pgigi", "1,000,000");
						params.put("batasmaxrg", "5,000,000");	
					
					}else if(lsdbs%15==6){
						params.put("pdasar", "1,200,000");
						params.put("pgusi", "820,000");
						params.put("pcegah", "1,250,000");
						params.put("pkomplek", "1,250,000");
						params.put("pperbaik", "630,000");
						params.put("pgigi", "1,250,000");
						params.put("batasmaxrg", "6,250,000");	
					
					}else if(lsdbs%15==8){
						params.put("pdasar", "1,750,000");
						params.put("pgusi", "1,130,000");
						params.put("pcegah", "1,750,000");
						params.put("pkomplek", "1,750,000");
						params.put("pperbaik", "900,000");
						params.put("pgigi", "1,750,000");
						params.put("batasmaxrg", "8,750,000");	
					
					}
					
					params.put("catatanrg", "\nCatatan Rawat Gigi (RG) :" +
							 "\n- Penggantian 100%");	
					
					params.put("subplanrg", JasperReportsUtils.convertReportData(manfaatrg));
					//StringUtils.addStringToArray(subJnProv, "subplanrg");
				}
				if(manfaatrb.size()!=0) {
					
					if(lsdbs%15==1 || lsdbs%15==7){
						
						params.put("mtanpaop", "20,000,000");
						params.put("mdengop", "35,000,000");
						params.put("keguguran", "15,000,000");
						params.put("pkhamilan", "3,800,000");
						
					}else if(lsdbs%15==2 || lsdbs%15==9){
						params.put("mtanpaop", "23,000,000");
						params.put("mdengop", "40,000,000");
						params.put("keguguran", "17,250,000");
						params.put("pkhamilan", "4,300,000");	
					
					}else if(lsdbs%15== 3 ){
						params.put("mtanpaop", "10,000,000");
						params.put("mdengop", "17,500,000");
						params.put("keguguran", "7,500,000");
						params.put("pkhamilan", "1,900,000");	
					
					}else if(lsdbs%15== 4 ){
						params.put("mtanpaop", "13,000,000");
						params.put("mdengop", "21,900,000");
						params.put("keguguran", "9,400,000");
						params.put("pkhamilan", "2,400,000");	
					
					}else if(lsdbs%15== 5 ){
						params.put("mtanpaop", "15,000,000");
						params.put("mdengop", "26,250,000");
						params.put("keguguran", "11,250,000");
						params.put("pkhamilan", "2,850,000");	
					
					}else if(lsdbs%15== 6 ){
						params.put("mtanpaop", "17,000,000");
						params.put("mdengop", "30,700,000");
						params.put("keguguran", "13,200,000");
						params.put("pkhamilan", "3,400,000");	
					
					}else if(lsdbs%15== 8 ){
						params.put("mtanpaop", "21,000,000");
						params.put("mdengop", "37,500,000");
						params.put("keguguran", "16,200,000");
						params.put("pkhamilan", "4,100,000");	
					
					}
					
					params.put("catatanrb", "\nCatatan Rawat Bersalin (RB) :" +
							 "\n- Premi berlaku untuk wanita yang eligible, yaitu wanita menikah, berusia < 45 tahun dan memiliki anak maksimum 2 orang"+
							 "\n- Berlaku masa tunggu 280 (dua ratus delapan puluh) hari untuk Rawat Bersalin"+
							 "\n- Melahirkan Dengan Operasi ( Sectio Cesaria ) harus berdasarkan Indikasi medis"+
							 "\n* Termasuk perawatan bayi maksimum 2 kali perawatan, berlaku maksimum 30 hari setelah kelahiran");	
					
					params.put("subplanrb", JasperReportsUtils.convertReportData(manfaatrb));
					//StringUtils.addStringToArray(subJnProv, "subplanrb");
				}
				if(manfaatpk.size()!=0) {
					
					if(lsdbs%15==1 || lsdbs%15==7){
						params.put("pkkonsultasi", "300,000");
						params.put("pkpemeriksaan", "1,000,000");
						params.put("pklayanan", "300,000");
						params.put("batasmaxpk", "5,500,000");
					
					}else if(lsdbs%15==2 || lsdbs%15==9){
						params.put("pkkonsultasi", "400,000");
						params.put("pkpemeriksaan", "1,500,000");
						params.put("pklayanan", "400,000");
						params.put("batasmaxpk", "7,500,000");
					
					}else if(lsdbs%15==3){
						params.put("pkkonsultasi", "200,000");
						params.put("pkpemeriksaan", "500,000");
						params.put("pklayanan", "200,000");
						params.put("batasmaxpk", "3,500,000");
					
					}else if(lsdbs%15==4){
						params.put("pkkonsultasi", "225,000");
						params.put("pkpemeriksaan", "625,000");
						params.put("pklayanan", "225,000");
						params.put("batasmaxpk", "4,000,000");
					
					}else if(lsdbs%15==5){
						params.put("pkkonsultasi", "250,000");
						params.put("pkpemeriksaan", "750,000");
						params.put("pklayanan", "250,000");
						params.put("batasmaxpk", "4,500,000");
					
					}else if(lsdbs%15==6){
						params.put("pkkonsultasi", "275,000");
						params.put("pkpemeriksaan", "875,000");
						params.put("pklayanan", "275,000");
						params.put("batasmaxpk", "5,000,000");
					
					}else if(lsdbs%15==8){
						params.put("pkkonsultasi", "350,000");
						params.put("pkpemeriksaan", "1,250,000");
						params.put("pklayanan", "350,000");
						params.put("batasmaxpk", "6,500,000");
					
					}
					
					params.put("catatanpk", "\nCatatan Penunjang Kesehatan (PK) :" +
							 "\n- Berlaku Masa tunggu untuk manfaat asuransi Penunjang Kesehatan selama 30 (tiga puluh) hari pertama sejak : tanggal berlaku periode asuransi atau	tanggal pemulihan Polis terakhir, hal mana yang terjadi terakhir"+
							 "\n- Penggantian 80%"+
							 "\n*  Penggantian Biaya Konsultasi Ahli Nutrisi hanya untuk Tertanggung yang didiagnosa menderita penyakit diabetes oleh Dokter dan harus menjalani Rawat Inap"+
							 "\n** Masa tunggu 1 tahun");	
					
					params.put("subplanpk", JasperReportsUtils.convertReportData(manfaatpk));
					//StringUtils.addStringToArray(subJnProv, "subplanpk");
				}
				//END provestara 214 RJ RG RB PK
				
			}else if("225".equals(businessId)){ //start SMILE MEDICAL PLUS 225 RJ RG RB PK
				
				params.put("catatanri", "\nCatatan Rawat Inap (RI) :" +
						 "\n- Berlaku Provider"+		
						 "\n* Diperoleh dalam 30 hari sesudah selesai perawatan di Rumah Sakit, dengan maksimal 10 kali pertahun");	
				
				List  manfaatrj = this.uwDao.selectPlanProvestara(spaj, 840);
				List  manfaatrjsmp = this.bacDao.selectPlanSmileMedicalPlus(spaj, 847," RJ ");
				List  manfaatrgsmp = this.bacDao.selectPlanSmileMedicalPlus(spaj, 847," RG ");
				List  manfaatrbsmp = this.bacDao.selectPlanSmileMedicalPlus(spaj, 847," RB ");
				List  manfaatpksmp = this.bacDao.selectPlanSmileMedicalPlus(spaj, 847," PK ");
				
				
				
				if(manfaatrjsmp.size()!=0) {
					
					Map map = new HashMap();
					Map rjsmp = (Map) manfaatrjsmp.get(0);
					String jenrjsmp = rjsmp.get("JENIS").toString();
					
					if("COPPER".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "45,000");
						params.put("kondospe", "135,000");
						params.put("konob", "90,000");
						params.put("obresdok", "900,000");
						params.put("biayates", "675,000");
						params.put("fisioterapirj", "70,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "2,700,000");	
						
					}else if("BRONZE".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "70,000");
						params.put("kondospe", "210,000");
						params.put("konob", "140,000");
						params.put("obresdok", "1,400,000");
						params.put("biayates", "1,050,000");
						params.put("fisioterapirj", "105,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "4,200,000");	
						
					}else if("SILVER".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "100,000");
						params.put("kondospe", "300,000");
						params.put("konob", "200,000");
						params.put("obresdok", "2,000,000");
						params.put("biayates", "1,500,000");
						params.put("fisioterapirj", "150,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "6,000,000");	
						
					}else if("SAPPHIRE".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "110,000");
						params.put("kondospe", "330,000");
						params.put("konob", "220,000");
						params.put("obresdok", "2,200,000");
						params.put("biayates", "1,650,000");
						params.put("fisioterapirj", "165,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "6,600,000");	
						
					}else if("DIAMOND".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "135,000");
						params.put("kondospe", "405,000");
						params.put("konob", "270,000");
						params.put("obresdok", "2,700,000");
						params.put("biayates", "2,025,000");
						params.put("fisioterapirj", "205,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "8,100,000");	
						
					}else if("PLATINUM".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "150,000");
						params.put("kondospe", "450,000");
						params.put("konob", "300,000");
						params.put("obresdok", "3,000,000");
						params.put("biayates", "3,250,000");
						params.put("fisioterapirj", "225,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "9,000,000");	
						
					}
					
					params.put("catatanrj", "\nCatatan Rawat Jalan (RJ) :" +
							 "\n- Berlaku Masa tunggu untuk Pre-existing Condition dan Penyakit-Penyakit Khusus"+		
							 "\n- Penggantian 100%"+		
							 "\n- Dokter Spesialis TANPA rujukan dari dokter umum"+
							 "\n- Berlaku Provider"+
							 "\n* Hanya untuk Tertanggung sampai usia  64 tahun");	
					
					params.put("subplanrj", JasperReportsUtils.convertReportData(manfaatrjsmp));
					//StringUtils.addStringToArray(subJnProv, "subplanrj");
				}
				if(manfaatrgsmp.size()!=0) {
					
					Map map = new HashMap();
					Map rgsmp = (Map) manfaatrgsmp.get(0);
					String jenrgsmp = rgsmp.get("JENIS").toString();
									
					if("COPPER".equalsIgnoreCase(jenrgsmp)){						
						params.put("pdasar", "400,000");
						params.put("pgusi", "85,000");
						params.put("pcegah", "85,000");
						params.put("pkomplek", "160,000");
						params.put("pperbaik", "410,000");
						params.put("pgigi", "330,000");
						params.put("batasmaxrg", "1,000,000");	
						
					}else if("BRONZE".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "440,000");
						params.put("pgusi", "95,000");
						params.put("pcegah", "95,000");
						params.put("pkomplek", "180,000");
						params.put("pperbaik", "455,000");
						params.put("pgigi", "365,000");
						params.put("batasmaxrg", "1,100,000");	
					
					}else if("SILVER".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "500,000");
						params.put("pgusi", "105,000");
						params.put("pcegah", "105,000");
						params.put("pkomplek", "200,000");
						params.put("pperbaik", "515,000");
						params.put("pgigi", "415,000");
						params.put("batasmaxrg", "1,250,000");	
					
					}else if("SAPPHIRE".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "600,000");
						params.put("pgusi", "125,000");
						params.put("pcegah", "125,000");
						params.put("pkomplek", "240,000");
						params.put("pperbaik", "620,000");
						params.put("pgigi", "500,000");
						params.put("batasmaxrg", "1,500,000");	
					
					}else if("DIAMOND".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "700,000");
						params.put("pgusi", "145,000");
						params.put("pcegah", "145,000");
						params.put("pkomplek", "280,000");
						params.put("pperbaik", "720,000");
						params.put("pgigi", "580,000");
						params.put("batasmaxrg", "1,750,000");	
					
					}else if("PLATINUM".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "1,000,000");
						params.put("pgusi", "210,000");
						params.put("pcegah", "210,000");
						params.put("pkomplek", "400,000");
						params.put("pperbaik", "1,025,000");
						params.put("pgigi", "825,000");
						params.put("batasmaxrg", "2,500,000");	
					
					}
					
					params.put("catatanrg", "\nCatatan Rawat Gigi (RG) :" +
							 "\n- Penggantian 80%"+		
							 "\n- Berlaku Reimbursement");	
					
					params.put("subplanrg", JasperReportsUtils.convertReportData(manfaatrgsmp));
					//StringUtils.addStringToArray(subJnProv, "subplanrg");
				}
				if(manfaatrbsmp.size()!=0) {
					
					Map map = new HashMap();
					Map rbsmp = (Map) manfaatrbsmp.get(0);
					String jenrbsmp = rbsmp.get("JENIS").toString();
					
					if("COPPER".equalsIgnoreCase(jenrbsmp)){							
						params.put("mtanpaop", "6,000,000");
						params.put("mdengop", "12,000,000");
						params.put("keguguran", "4,500,000");
						params.put("pkhamilan", "1,300,000");
						
					}else if("BRONZE".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "7,000,000");
						params.put("mdengop", "14,000,000");
						params.put("keguguran", "5,250,000");
						params.put("pkhamilan", "1,500,000");	
					
					}else if("SILVER".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "8,000,000");
						params.put("mdengop", "16,000,000");
						params.put("keguguran", "6,000,000");
						params.put("pkhamilan", "1,700,000");	
					
					}else if("SAPPHIRE".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "8,000,000");
						params.put("mdengop", "16,000,000");
						params.put("keguguran", "6,000,000");
						params.put("pkhamilan", "1,700,000");		
					
					}else if("DIAMOND".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "9,000,000");
						params.put("mdengop", "18,000,000");
						params.put("keguguran", "6,750,000");
						params.put("pkhamilan", "1,900,000");	
					
					}else if("PLATINUM".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "10,000,000");
						params.put("mdengop", "20,000,000");
						params.put("keguguran", "7,500,000");
						params.put("pkhamilan", "2,100,000");	
					
					}
					
					params.put("catatanrb", "\nCatatan Rawat Bersalin (RB) :" +
							 "\n- Premi berlaku untuk wanita yang eligible, yaitu wanita menikah, berusia < 45 tahun dan memiliki anak maksimum 2 orang"+
							 "\n- Berlaku masa tunggu 280 (dua ratus delapan puluh) hari untuk Rawat Bersalin"+
							 "\n- Melahirkan Dengan Operasi ( Sectio Cesaria ) harus berdasarkan Indikasi medis"+
							 "\n* Termasuk perawatan bayi maksimum 2 kali perawatan, berlaku maksimum 30 hari setelah kelahiran");	
					
					params.put("subplanrb", JasperReportsUtils.convertReportData(manfaatrbsmp));
					//StringUtils.addStringToArray(subJnProv, "subplanrb");
				}
				if(manfaatpksmp.size()!=0) {
					
					Map map = new HashMap();
					Map pksmp = (Map) manfaatpksmp.get(0);
					String jenpksmp = pksmp.get("JENIS").toString();
					
					if("COPPER".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "200,000");
						params.put("pkpemeriksaan", "500,000");
						params.put("pklayanan", "200,000");
						params.put("batasmaxpk", "3,500,000");
					
					}else if("BRONZE".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "225,000");
						params.put("pkpemeriksaan", "625,000");
						params.put("pklayanan", "225,000");
						params.put("batasmaxpk", "4,000,000");
					
					}else if("SILVER".equalsIgnoreCase(jenpksmp)){
						params.put("pkkonsultasi", "250,000");
						params.put("pkpemeriksaan", "750,000");
						params.put("pklayanan", "250,000");
						params.put("batasmaxpk", "4,500,000");
					
					}else if("SAPPHIRE".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "275,000");
						params.put("pkpemeriksaan", "875,000");
						params.put("pklayanan", "275,000");
						params.put("batasmaxpk", "5,000,000");
					
					}else if("DIAMOND".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "350,000");
						params.put("pkpemeriksaan", "1,250,000");
						params.put("pklayanan", "350,000");
						params.put("batasmaxpk", "6,500,000");
					
					}else if("PLATINUM".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "400,000");
						params.put("pkpemeriksaan", "1,500,000");
						params.put("pklayanan", "400,000");
						params.put("batasmaxpk", "7,500,000");
					
					}
					
					params.put("catatanpk", "\nCatatan Penunjang Kesehatan (PK) :" +
							 "\n- Berlaku Masa tunggu untuk manfaat asuransi Penunjang Kesehatan selama 30 (tiga puluh) hari pertama sejak : tanggal berlaku periode asuransi atau	tanggal pemulihan Polis terakhir, hal mana yang terjadi terakhir"+
							 "\n- Penggantian 80%"+
							 "\n- Berlaku Reimbursement"+
							 "\n*  Penggantian Biaya Konsultasi Ahli Nutrisi hanya untuk Tertanggung yang didiagnosa menderita penyakit diabetes oleh Dokter dan harus menjalani Rawat Inap"+
							 "\n** Masa tunggu 1 tahun");	
					
					params.put("subplanpk", JasperReportsUtils.convertReportData(manfaatpksmp));
					//StringUtils.addStringToArray(subJnProv, "subplanpk");
				}
				//END SMILE MEDICAL PLUS 225 RJ RG RB PK
				
				
			}
			
			params.put("subReportDatakeys", new String[] {"subplanrj", "subplanrg", "subplanrb", "subplanpk"});
			params.put("catatan_manf_utama", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini. 2");
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_provestara"));
						
			return params;
		}else
		{
			String ls_insured = (String) info.get("mste_insured");
			String ls_pol_hold = (String) info.get("mspo_policy_holder");
//			 Cara bayar Triwulan, Semesteran, Bulanan, Cicilan dijadikan Tahunan
			if(il_cbid==1 || il_cbid==2 || il_cbid==6 || il_cbid==7) il_cbid=3;
			
			/************************** EKA SISWA **************************/
			if("031,033,070,071,072,172".indexOf(businessId)>-1) {
				if(li_medical==1) urut=1;
				else urut=2;
			/************************** SUPER PROFUND **************************/
			}else if("046".equals(businessId)) {
				if(il_cbid==0) urut=1;
				else if(il_age<17)  urut=2;
				else {
					urut=3;
				}
			/************************** PROTEKSI **************************/	
			}else if("051, 052, 061, 062, 078, 085, 181".indexOf(businessId)>-1) {
				int i = lku_id.equals("01")?0:4;
				if(il_cbid==0) urut=1+i;
				else if(ls_insured.trim().equals(ls_pol_hold.trim())) urut=2+i;
				else if(il_age<17 && il_mspo_age<50) urut=3+i;
				else urut=4+i;
			/************************** SUPER SEHAT, SUPER SEHAT PLUS **************************/
			}else if("053,054,131,132,148".indexOf(businessId)>-1) {
				if(lku_id.equals("01")) urut=1;
				else urut=2;
			/************************** SIMPONI **************************/	
			}else if("056, 058, 064, 083".indexOf(businessId)>-1) {
				if(il_age<17) urut=1;
				else if(il_age<=55) urut=2;
				else urut=3;
			/************************** SEHAT SEJAHTERA **************************/
			}else if("065".equals(businessId)) {
				if(ls_insured.trim().equals(ls_pol_hold.trim()) && il_cbid!=0) urut=1;
				else urut=2;
			/************************** PRO CARE **************************/	
			}else if("066".equals(businessId)) {
				if(il_age>50 && il_cbid==0) urut=4;
				else {
					if(il_age<17) urut=3;
					else if(ls_insured.trim().equals(ls_pol_hold.trim()) && il_cbid!=0) urut=2;
					else urut=1;
				}
			/************************** SUPER INVEST **************************/
			}else if("068,075".indexOf(businessId)>-1) {
				if(il_age<17) urut=1;
				else if(il_age<=50) urut=2;
				else if(il_age<=65) urut=3;
				else urut=4;					
			/**************************  **************************/
//			}else if("093,104,108,110,114,111,125".indexOf(businessId)>-1) {
			}else if("093,104,108,110,125".indexOf(businessId)>-1 || (businessId.equals("111") && lsdbs==1)) {	
				if(il_age<=55) urut=1;
				else urut=2;
			/************************** SIMPONI 8 **************************/
			}else if("112,126".indexOf(businessId)>-1) {
				if(il_age<=50) urut=1;
				else urut=2;
			/************************** LAINNYA **************************/
			}else {
				urut=1;
			}
		}
		Boolean afternov2012 = false;
		Date nov2012 = defaultDateFormat.parse("31/11/2012");
		if (businessId.equals("137") && lsdbs>=6 || businessId.equals("114") && lsdbs>=5 ){
		Date beg_date = basDao.selectBegDateInsuredFromSpaj(spaj);
			if(beg_date.after(nov2012)){
				afternov2012 = true;
				urut=1;
			}
		
			params.put("afternov2012", afternov2012);
		}
		
		List manfaatUtama = this.uwDao.selectDetailManfaat(spaj, new Integer(urut));
		if(manfaatUtama.size()>0){
			if(uwDao.selectCountSwineFlu(spaj)>0){
				Map map = new HashMap();
				Map hasilmanfaatUtama = (Map) manfaatUtama.get(manfaatUtama.size()-1);
				map.put("LKU_ID", hasilmanfaatUtama.get("LKU_ID"));
				map.put("MANFAAT", hasilmanfaatUtama.get("MANFAAT"));
				map.put("MSPR_TSI", hasilmanfaatUtama.get("MSPR_TSI"));
				map.put("LSDBS_NUMBER", hasilmanfaatUtama.get("LSDBS_NUMBER"));
				map.put("LSMAN_LINE", (Integer)hasilmanfaatUtama.get("LSMAN_LINE")+1);
				map.put("LSMAN_MANFAAT", "Ketentuan manfaat Asuransi Tambahan ( rider ) tertera pada Syarat-syarat Khusus masing-masing Asuransi Tambahan");
				map.put("LSMAN_HEADER", hasilmanfaatUtama.get("LSMAN_HEADER"));
				map.put("JUDUL", hasilmanfaatUtama.get("JUDUL"));
				map.put("LSMAN_NOURUT", hasilmanfaatUtama.get("LSMAN_NOURUT"));
				map.put("LSBS_ID", hasilmanfaatUtama.get("LSBS_ID"));
				manfaatUtama.add(map);
			}
		}
		List manfaatTambahan = this.uwDao.selectDetailManfaatTambahan(spaj);
		for(int i=0;i<manfaatTambahan.size();i++){
			Map cekswineflu = (HashMap) manfaatTambahan.get(i);
			String lsbs_id = ((Integer) cekswineflu.get("LSBS_ID")).toString();
			if(lsbs_id.equals("822")) manfaatTambahan.remove(i);
		}
		
		List manfaatNilaiTunai = null;
		List premiTambahan = null;
		
		if(logger.isDebugEnabled())logger.debug("Proses " +spaj + " ~ " + urut + " = " + manfaatUtama.size() + " ~ " + manfaatTambahan.size());

		String ls_manfaat;
		int lsdbs_number = uwDao.selectBusinessNumber(spaj);

		if(logger.isDebugEnabled())logger.debug("Proses Manfaat Polis");
		
		/** PROSES MANFAAT POLIS **/
		double idb_tsi = ((Double) info.get("mspr_tsi")).doubleValue();
		int il_lsbs_num = ((Integer) info.get("lsdbs_number"));

		if(manfaatUtama.size()>1) {
			int ll_no_urut = ((Integer)((HashMap) manfaatUtama.get(0)).get("LSMAN_NOURUT"));
			if("053,054,067,131,132,148".indexOf(businessId)>-1) {
				for(int i=0; i<manfaatUtama.size(); i++) {
					if(logger.isDebugEnabled())logger.debug("1 "+i);
					ls_manfaat = setNilaiManfaat(idb_premium.doubleValue(), il_cbid, ii_umur_beasiswa, spaj, businessId, il_lsbs_num, idb_tsi, lku_id, i, ll_no_urut, il_age);
					Map m = (HashMap) manfaatUtama.get(i);
					m.remove("MANFAAT");m.put("MANFAAT", ls_manfaat);
					manfaatUtama.remove(i); manfaatUtama.add(i, m);
				}
			}else if(!businessId.equals("032")){
				for(int i=0; i<manfaatUtama.size();i++) {
					if(logger.isDebugEnabled())logger.debug("businessId: "+businessId+" ~ 2 "+i);
					ls_manfaat = setNilaiManfaat(idb_premium.doubleValue(), il_cbid, ii_umur_beasiswa, spaj, businessId, il_lsbs_num, idb_tsi, lku_id, i+1, ll_no_urut, il_age);
					if(logger.isDebugEnabled())logger.debug("LS_MANFAAT = " + ls_manfaat);
					Map m = (HashMap) manfaatUtama.get(i);
					m.remove("LSMAN_MANFAAT");m.put("LSMAN_MANFAAT", ls_manfaat);
					manfaatUtama.remove(i); manfaatUtama.add(i, m);
				}
			}
			
		}else if(manfaatUtama.size()==1){
			int ll_no_urut = ((Integer)((HashMap) manfaatUtama.get(0)).get("LSMAN_NOURUT"));
			ls_manfaat = setNilaiManfaat(idb_premium.doubleValue(), il_cbid, ii_umur_beasiswa, spaj, businessId, il_lsbs_num, idb_tsi, lku_id, 0, ll_no_urut, il_age);
			Map m = (HashMap) manfaatUtama.get(0);
			m.remove("MANFAAT");m.put("MANFAAT", ls_manfaat);
			manfaatUtama.remove(0); manfaatUtama.add(0, m);			
		}
		
		//request dr. ingrid - untuk semua produk powersave, apabila usia tertanggung >= 69, maka UP = 0.5 PREMI, dengan nilai max Rp. 100 jt / $10.000 (Yusuf - 11/03/2008)
		//ada tambahan manfaat ke empat untuk request ini
		//{LKU_ID=01, MANFAAT= , MSPR_TSI=5.0E7, LSMAN_NOURUT=1, LSDBS_NUMBER=4, LSMAN_MANFAAT=, LSBS_ID=142, JUDUL= , LSMAN_LINE=3, LSMAN_HEADER=}
		if(products.powerSave(businessId) && bacDao.selectUsiaTertanggung(spaj) >= 69) {
			Map temp = new HashMap();
			temp.put("LSMAN_LINE", 4);
			temp.put("LSMAN_MANFAAT", "Dengan tidak mengurangi maksud pasal 8 ayat 4, maka apabila Tertanggung meninggal dunia akibat kecelakaan pada usia sama dengan atau lebih dari 69 ( enam puluh sembilan ) tahun, maka jumlah manfaat yang dapat dibayarkan adalah 50% ( lima puluh perseratus ) dari premi dengan maksimum  Rp. 100.000.000,- ( seratus juta rupiah ) atau US$ 10.000 ( sepuluh ribu Dollar Amerika )");
			if(products.syariah(businessId, Integer.toString(il_lsbs_num))) //bila syariah, kata premi -> kontribusi/premi
				temp.put("LSMAN_MANFAAT", "Dengan tidak mengurangi maksud pasal 8 ayat 4, maka apabila Tertanggung meninggal dunia akibat kecelakaan pada usia sama dengan atau lebih dari 69 ( enam puluh sembilan ) tahun, maka jumlah manfaat yang dapat dibayarkan adalah 50% ( lima puluh perseratus ) dari kontribusi/premi dengan maksimum  Rp. 100.000.000,- ( seratus juta rupiah ) atau US$ 10.000 ( sepuluh ribu Dollar Amerika )");
			manfaatUtama.add(temp);			
		}		

		logger.info("--------- " + businessId);
		
		//request arin / achmad - 21 Oct 2009 - bila cerdas + ada rider, ada manfaat tambahan ke 3
		if(products.cerdas(businessId)){
			List rider = bacDao.selectDataUsulan_rider(spaj);
			if(!rider.isEmpty()){
				Map temp = new HashMap();
				temp.put("LSMAN_LINE", 3);
				temp.put("LSMAN_MANFAAT", "Ketentuan manfaat Asuransi Tambahan ( rider ) tertera pada Syarat-syarat Khusus masing-masing Asuransi Tambahan ( rider ) tersebut.");
				manfaatUtama.add(temp);			
			}
		}
		
		if(logger.isDebugEnabled())logger.debug("Proses Rider");
		/** PROSES RIDER **/
		if(manfaatTambahan.size()>0) {
			int ll_no_urut = ((Integer)((HashMap) manfaatTambahan.get(0)).get("LSMAN_NOURUT"));
			for(int i=0; i<manfaatTambahan.size(); i++) {
				if(logger.isDebugEnabled())logger.debug("3 "+i);
				Map m = (HashMap) manfaatTambahan.get(i);
				String lsbs_id = ((Integer) m.get("LSBS_ID")).toString();
				String lsbs_number = ((Integer) m.get("LSDBS_NUMBER")).toString();
				int ll_line = ((Integer) m.get("LSMAN_LINE"));
				Map ipi = this.uwDao.selectInfoProductInsured(spaj, lsbs_id);
				Double ldc_rider_tsi = (Double) ipi.get("MSPR_TSI");
				ls_manfaat = setNilaiManfaat(idb_premium.doubleValue(), il_cbid, ii_umur_beasiswa, spaj, lsbs_id, Integer.parseInt(lsbs_number), ldc_rider_tsi.doubleValue(), lku_id, ll_line, ll_no_urut, il_age);
				ls_manfaat += "\n";
				m.put("TAMB_MANFAAT", ls_manfaat);
			}
			
			List tmp = new ArrayList();
			Map daftar = new HashMap();
			for(int i=0; i<manfaatTambahan.size(); i++) {
				Map m = (HashMap) manfaatTambahan.get(i);
				String lsbs_id = ((Integer) m.get("LSBS_ID")).toString();
				if(daftar.containsKey(FormatString.rpad("0", lsbs_id, 3))) {
					for(int j=0; j<tmp.size(); j++) {
						Map map = (HashMap) tmp.get(j);
						String lsbs = ((Integer) map.get("LSBS_ID")).toString();
						if(FormatString.rpad("0", lsbs, 3).equals(FormatString.rpad("0", lsbs_id, 3))) {
							map.put("TAMB_MANFAAT", m.get("TAMB_MANFAAT") + "\n" + map.get("TAMB_MANFAAT"));
						}
					}
				}else {
					daftar.put(FormatString.rpad("0", lsbs_id, 3), i);
					tmp.add(m);
				}
			}
			
			manfaatTambahan = tmp;
			
//			khusus 2000 polis palembang 28/01/04 (hm)
			if(businessId.equals("089")) {
				Map temp = new HashMap();
				temp.put("LSMAN_HEADER", "Klausula: Tambahan mengenai Asuransi Tambahan Bebas Premi");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "0");
				temp.put("TAMB_MANFAAT", "Dalam hal Pemegang Polis berbeda dengan Tertanggung, maka khusus untuk Polis ini Ketentuan dalam Pasal 1 ayat 1 " +
						"Syarat-syarat Khusus Asuransi Tambahan Bebas premi yang menyatakan bahwa Tertanggung juga sebagai Pemegang polis menjadi tidak berlaku. ");
				manfaatTambahan.add(temp);
			}
		}
		
		//Mediplan - 148
		if(businessId.equals("148")){
			Map temp = new HashMap();
			temp.put("LSMAN_HEADER", "Pembagian Bonus :");
			temp.put("LSBS_ID", "998");
			temp.put("LSDBS_NUMBER", "0");
			temp.put("LSMAN_LINE", "1");
			temp.put("SUBHEADER", "Jika Peserta selama satu tahun pertanggungan tidak mengajukan klaim maka akan mendapatkan bonus dengan ketentuan:");
			temp.put("TAMB_MANFAAT", "Jika Peserta Memperpanjang kontrak Asuransi = 25% dari premi dasar tahun berjalan");
			manfaatTambahan.add(temp);
			
			temp = new HashMap();
			temp.put("LSMAN_HEADER", "Pembagian Bonus :");
			temp.put("LSBS_ID", "998");
			temp.put("LSDBS_NUMBER", "0");
			temp.put("LSMAN_LINE", "2");
			temp.put("SUBHEADER", "Jika Peserta selama satu tahun pertanggungan tidak mengajukan klaim maka akan mendapatkan bonus dengan ketentuan:");
			temp.put("TAMB_MANFAAT", "Jika Peserta Tidak Memperpanjang kontrak Asuransi = 15% dari premi dasar tahun berjalan");
			manfaatTambahan.add(temp);
			
			temp = new HashMap();
			temp.put("LSMAN_HEADER", "Keterangan :");
			temp.put("LSBS_ID", "999");
			temp.put("LSDBS_NUMBER", "0");
			temp.put("LSMAN_LINE", "1");
			temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
			temp.put("TAMB_MANFAAT", "Manfaat Pertanggungan mengikuti ketentuan pada Pasal 3 (tiga) Syarat-Syarat Khusus Polis Asuransi Santunan Harian Rawat Inap dan Pembedahan Mediplan");
			manfaatTambahan.add(temp);

			if(lsdbs>=1 && lsdbs<=5) {
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "2");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Manfaat Asuransi Tambahan \"Santunan Penyakit Kritis\" mengikuti ketentuan pada Pasal 2 (dua) Syarat-Syarat Khusus Asuransi Santunan Harian Rawat Inap dan Pembedahan Mediplan Asuransi Tambahan Penyakit Kritis");
				manfaatTambahan.add(temp);
				
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "3");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Bonus mengikuti ketentuan Pasal 4 (empat) Syarat-Syarat Khusus Polis Asuransi Santunan Harian Rawat Inap dan Pembedahan Mediplan");
				manfaatTambahan.add(temp);
				
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "4");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Produk Asuransi Kesehatan Mediplan tidak memiliki Nilai Tunai");
				manfaatTambahan.add(temp);
			}else {
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "2");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Bonus mengikuti ketentuan Pasal 4 (empat) Syarat-Syarat Khusus Polis Asuransi Santunan Harian Rawat Inap dan Pembedahan Mediplan");
				manfaatTambahan.add(temp);
				
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "3");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Produk Asuransi Kesehatan Mediplan tidak memiliki Nilai Tunai");
				manfaatTambahan.add(temp);
			}
		}
		
		if(logger.isDebugEnabled())logger.debug("Proses Nilai Tunai dan Tahapan");
		/** Proses Nilai Tunai & Tahapan **/
		manfaatNilaiTunai = this.nilaiTunai.proses(spaj, lus_id, "cetak", 0, 0);
		
		if(logger.isDebugEnabled())logger.debug("Proses Klausula");
		/** Proses Klausula **/
		String kurs = lku_id.equals("01")?"Rp. ":lku_id.equals("02")?"US\\$ ":"";
		String klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal #PASAL#. Syarat - syarat Umum Polis";

		//YUSUF (5/10/2006) -> KHUSUS ENDOWMENT MUTIARA
		if(businessId.equals("157")) {
			if(lsdbs_number == 2 || lsdbs_number == 3) {
				klausula = "KLAUSULA : Daftar Nilai Tunai #KURS#";
			}
		}
		
		if("056,058,068,075,083".indexOf(businessId)>-1) klausula = klausula.replaceAll("#PASAL#", String.valueOf(5));
		else if("069,082".indexOf(businessId)>-1) {
			klausula = klausula.replaceAll("#PASAL#", String.valueOf(6));
			klausula += " dan Pasal 2 Syarat-Syarat Khusus Polis";
		}else if("167,168,169,210".indexOf(businessId)>-1) { //HIDUP BAHAGIA & EKAWAKTU
			klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 9 Syarat - syarat Umum Polis";
		}else if("040,146,163,180".indexOf(businessId)>-1 || products.SuperSejahtera(businessId)) { //DANA SEJAHTERA - REQUEST MORINOF
			if(products.SuperSejahtera(businessId) && lsdbs_number>=6){
				klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 9. Syarat-syarat Umum Polis dan Pasal 2. Syarat-syarat Khusus Polis";
			}else{
				klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 9. Syarat-syarat Umum Polis dan Pasal 3. Syarat-syarat Khusus Polis";
			}
		}else if("093,095,098,104,108,110,112,125,126,133,114,137,150,151".indexOf(businessId)>-1) {
			klausula = klausula.replaceAll("#PASAL#", String.valueOf(7));
		}else if("149".indexOf(businessId)>-1) {
			klausula = klausula.replaceAll("#PASAL#", String.valueOf(9));
		}else if(products.ekaSiswa(businessId) || "181".indexOf(businessId)>-1) {
			klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 9. Syarat-syarat Umum Polis";
		}else if("173".indexOf(businessId)>-1) {
			klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 3. Syarat-syarat Khusus Polis";
		}else if("178".indexOf(businessId)>-1){
			klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 12. Syarat-syarat Umum Polis";
		}else if("208".indexOf(businessId)>-1){
			klausula = "KLAUSULA : Daftar Tahapan Dana Belajar";
		}else klausula = klausula.replaceAll("#PASAL#", String.valueOf(6));
		klausula += " :";
		klausula = klausula.replaceAll("#KURS#", kurs);
		
		if(logger.isDebugEnabled())logger.debug("Proses Maturity");
		/** Proses Maturity **/
		Double matur = null;
		Double maturBonus = null;
		int lsbs = Integer.parseInt(businessId);
		Integer thMatur = (Integer) info.get("mspo_ins_period");
		if(lsbs==27) {
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, payPeriod, 4);
			if(insPeriod==99) thMatur = new Integer(100-il_age); 
		}else if(products.ekaSiswa(businessId)) {
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_mspo_age, thMatur, 7);
		}else if(lsbs==56) {
			thMatur = new Integer(4);
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, thMatur, 7); 
		}else if("060,089,147".indexOf(businessId)>-1) {
			thMatur = new Integer(79);
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, thMatur, 7);
		}else if("051,052,061,062,078".indexOf(businessId)>-1) {
			if(il_age==49 && lku_id.equals("01")) matur= new Double(1500);
		}else if("167".indexOf(businessId)>-1) { //Yusuf (26/02/2008) - Penarikan nilai maturity dipatok 70
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, 70, il_age, thMatur, 7);
		}else {
			if(insPeriod==99) thMatur = new Integer(100-il_age);
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, thMatur, 7);
			maturBonus = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, insPeriod, 4);
		}
		
		if(logger.isDebugEnabled())logger.debug("Proses Bonus Akhir Kontrak");
		/** Proses Bonus Akhir Kontrak **/
		String ls_nikon = null;
		String ls_nikon2 = null;
		DecimalFormat df = new DecimalFormat("#,##0;(#,##0)");
		if(matur!=null) {
			if(matur.doubleValue()!=0) {
				if(lsbs==27) {
					if(il_lsbs_num==1) {
						ls_nikon = "Bonus akhir tahun ke-10 : ";
					}else if(il_lsbs_num==2) {
						ls_nikon = "Bonus akhir tahun ke-15 : ";
					}else {
						ls_nikon = "Bonus akhir tahun ke-20 : ";
					}
				}else if(lsbs == 167) {
					ls_nikon = "Uang Pertanggungan Akhir Kontrak (apabila Tertanggung Hidup) : ";
				}else if(products.SuperSejahtera(Integer.toString(lsbs)) || lsbs==145) {
					ls_nikon = "Tahapan usia ke-69 : " + df.format(0.5 * idb_tsi);
				}else {
					ls_nikon = "Nilai akhir kontrak : ";
				}
				if(lsbs==83) {
					ls_nikon = "Bonus akhir kontrak : " + df.format(idb_premium.doubleValue() * 1.4);
				}else if(products.SuperSejahtera(Integer.toString(lsbs)) || lsbs==145){
					ls_nikon = ls_nikon;
				}else{
					ls_nikon += df.format(Math.round(idb_tsi * matur.doubleValue()/1000));
				}
			}
		}

		if(maturBonus!=null) {
			if(maturBonus.doubleValue()!=0) {
				ls_nikon2 = "Bonus akhir kontrak : " + df.format(idb_tsi * maturBonus.doubleValue()/1000);
			}
		}
		
		if(products.SuperSejahtera(Integer.toString(lsbs)) || lsbs==145) {
//			ls_nikon2 = "Tahapan usia ke-69 : " + df.format(0.5 * idb_tsi);
			ls_nikon2 = "Nilai akhir kontrak : ";
			ls_nikon2 += df.format(Math.round(idb_tsi * matur.doubleValue()/1000));
			
		}else if(lsbs==65) {
			ls_nikon2 = " Tahapan usia ke-59 : " + df.format(idb_tsi);
		}else if(lsbs==56) {
			ls_nikon = "Manfaat akhir tahun ke-4 : " + df.format(1.25 * idb_premium.doubleValue());
		}else if(lsbs==64) {
			ls_nikon = "Manfaat akhir tahun ke-4 : " + df.format(1.24 * idb_premium.doubleValue());
		}else if(lsbs==68) {
			ls_nikon = "Minimum Manfaat hidup diakhir tahun ke-4 : " + df.format(1.06 * idb_premium.doubleValue());
		}else if(lsbs==75) {
			ls_nikon = "Minimum Manfaat hidup diakhir tahun ke-4 : " + df.format(1.02 * idb_premium.doubleValue());
		}else if("095,098".indexOf(businessId)>-1) {
			ls_nikon = "Manfaat akhir tahun ke-8 : " + df.format(1.818 * idb_premium.doubleValue());
		}
	
		String catUtama = null;
		if(products.syariah(businessId, Integer.toString(il_lsbs_num))) {
			catUtama = "MANFAAT ASURANSI Syariah diberikan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-"+
			"SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan "+
			"pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan "+
			"dengan POLIS INI.";
		}else if(businessId.equals("167")) {
			catUtama = "MANFAAT ASURANSI diberikan sesuai dengan SYARAT-SYARAT UMUM POLIS "+
			"dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan "+
			"pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan "+
			"dengan POLIS INI.";			
		}else if(businessId.equals("168")) {
			catUtama = "MANFAAT ASURANSI diberikan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan "+
			"KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat "+
			"dipisahkan dengan POLIS INI.";			
		}else if(businessId.equals("169")){
			catUtama = "MANFAAT ASURANSI diberikan berdasarkan persyaratan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan "+
			"KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat "+
			"dipisahkan dengan POLIS ini.";
		}else {
			catUtama = "MANFAAT ASURANSI diberikan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-"+
			"SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan "+
			"pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan "+
			"dengan POLIS INI.";
		}
		
		//tambahan klausula di manfaat, bila cetak ulang polis
		if(tamb == 5) {
			catUtama +=
				"\n<style isBold=\"true\" isItalic=\"true\" isUnderline=\"true\">Catatan Mengenai Penggantian Polis:</style>" +
				"\nPolis ini adalah pengganti polis No. 09.155.2008.02856" +
				" yang diterbitkan tanggal 27 JUNI 2008"+
				" yang hilang. Dengan diterbitkannya polis ini maka polis yang diterbitkan tanggal 27 JUNI 2008"+
				" dinyatakan tidak berlaku lagi.";
		}
		
		String catManfAll = null;
		if(il_cbid==0 && li_medical==0 && "047,049,050,055,056,058,068,075,083,093,095,098,104,108,110,112,125,126,133,114,137,150,151,173,180,181".indexOf(businessId)==-1) {
			catManfAll = "Catatan : Syarat khusus Asuransi tanpa pemeriksaan dokter tidak berlaku bagi pembayaran premi sekaligus.";
		}else if(businessId.equals("180")){
			String kurs2 = lku_id.equals("01")?"Rp. ":lku_id.equals("02")?"US$ ":"";
			if(lsdbs_number==1){
				catManfAll = "Manfaat dan Bonus Habis Kontrak :"+kurs2+ df.format(1.2 * idb_tsi); 
			}else if(lsdbs_number==2){
				catManfAll = "Manfaat dan Bonus Habis Kontrak :"+kurs2+ df.format(1.3 * idb_tsi); 
			}else if(lsdbs_number==3){
				catManfAll = "Manfaat dan Bonus Habis Kontrak :"+kurs2+ df.format(1.4 * idb_tsi); 
			}
			
		}
		if(businessId.equals("210")){
			
			catManfAll = "Catatan : \n"+
					     "1.   Termasuk manfaat pengembalian 50% dan 100% dari total premi yang telah dibayarkan (tanpa bunga)\n"+ 
					     "2.   Apabila Usia Polis x tahun y bulan, maka Nilai Tunainya adalah pada saat Usia Polis x tahun" ;
			
		}
		
		String bonustahapan = null;
		if("112,126".indexOf(businessId)>-1) {
			bonustahapan = "*) Bonus Tahapan adalah bonus yang diberikan jika Tertanggung hidup sampai dengan akhir kontrak Asuransi\n"+
										"   (akhir tahun polis kedelapan) bila ada, dimana bonus ini ditentukan diawal pertanggungan / diawal kontrak\n"+
										"   asuransi yang besarnya ditentukan oleh Penanggung";
			ls_nikon = null;
			ls_nikon2 = null;
			catManfAll = null;
		}else if("150, 151".indexOf(businessId)>-1) {
			bonustahapan = "*)  Besarnya Nilai Tunai pada akhir tahun keempat tidak dijamin, tergantung bunga pasar pada waktu itu.";
			ls_nikon = null;
			ls_nikon2 = null;
			catManfAll = null;
		}else if("157".indexOf(businessId)>-1) { //endowment 20
			if(lsdbs_number == 2 || lsdbs_number == 3) {
				bonustahapan = "Catatan : \n"+
				"*) - Ketentuan mengenai besarnya Nilai Tunai yang dibayarkan oleh Penanggung diatur dalam Syarat-Syarat Umum Polis Pasal 6 Ayat 1. \n"+
				"   - Dengan telah dibayarkannya Nilai Tunai tersebut, maka sejak saat itu Asuransi atas diri Tertanggung berakhir.";
			}else {
				bonustahapan = "Catatan : \n"+
											"*) - Ketentuan mengenai besarnya Nilai Tunai yang dibayarkan oleh Penanggung diatur dalam Syarat-Syarat Umum Polis Pasal 7 Ayat 1. \n"+
											"   - Dengan telah dibayarkannya Nilai Tunai tersebut, maka sejak saat itu Asuransi atas diri Tertanggung berakhir.";
			}
			ls_nikon = null;
			ls_nikon2 = null;
			catManfAll = null;
		}else if("167".indexOf(businessId)>-1) {
			catManfAll = 
				"*)   Nilai Tunai (sudah termasuk Tahapan Pasti)";
	//			"*)   Nilai Tunai (sudah termasuk Tahapan Pasti)\n"+
	//			"**) Nilai Tunai pada usia 70 tahun sudah termasuk Dana Hari Tua (apabila Tertanggung masih hidup)";
//			ls_nikon = null;
//			ls_nikon2 = null;
//			catManfAll = null;
		}else if("168".indexOf(businessId)>-1) {
			catManfAll = 
				//"*)   Nilai Tunai di atas sudah termasuk Dana Kehidupan ( apabila diajukan ) pada saat Tertanggung mencapai usia 55 (lima puluh lima) tahun, Tertanggung masih hidup dan Polis masih berlaku.";
				"";
		}else if("178".indexOf(businessId)>-1) {
			catManfAll = "*)   Termasuk manfaat pengembalian 100% dari total premi yang telah dibayarkan (tanpa bunga)";
		}else if("208".indexOf(businessId)>-1) {
			catManfAll = "*)   Termasuk manfaat pengembalian 100% dari total premi yang telah dibayarkan (tanpa bunga)";
		}
		
		/** Proses Perhitungan Tarif Premi Tambahan (Rider) Khusus Platinum Link (134) dan Amanah Link (166) - Yusuf - 03/01/2008 **/
		if((businessId.equals("134") && (lsdbs!=5 || lsdbs!=6)) || businessId.equals("166")) {
			premiTambahan = prosesAsuransiTambahanPlatinumLink(info);
		}
		
		/** MULAI PROSES JASPER REPORTS **/
		params.put("spaj", spaj);
		params.put("props", props);
		
		params.put("akhirkont", ls_nikon);
		params.put("bonuskont", ls_nikon2);
		params.put("catatan_manf_utama", catUtama);
		params.put("catatan_manf_all", catManfAll);
		params.put("bonustahapan", bonustahapan);
		logger.info(params.get("catatan_manf_utama"));

		//Convert DataSource untuk subreport dari Collection ke JRDataSource
		params.put("utamaDS", JasperReportsUtils.convertReportData(manfaatUtama));
		params.put("tambahanDS", JasperReportsUtils.convertReportData(manfaatTambahan));
		if(manfaatNilaiTunai!=null) {
			params.put("klausa", klausula);
			params.put("allDS", JasperReportsUtils.convertReportData(manfaatNilaiTunai));
		}else if(premiTambahan != null) {
			params.put("allSR", props.getProperty("subreport.manfaat.manf_tambahan_platinum")+".jasper");
			if(businessId.equals("166")) params.put("allSR", props.getProperty("subreport.manfaat.manf_tambahan_amanah")+".jasper");
			params.put("allDS", JasperReportsUtils.convertReportData(premiTambahan));
		}
		
		if(businessId.equals("073")) {
			params.put("allSR", props.getProperty("subreport.manfaat.manf_tambahan_verdana")+".jasper");
		}
		
		//endowment 20, nilai tunainya beda dikit (Yusuf)
		if(businessId.equals("157")) {
			params.put("allSR", props.getProperty("subreport.manfaat.manf_all_157") + ".jasper");
		//hidup bahagia, manfaatnya ada tahapan pasti diatas (Yusuf)
		}else if(businessId.equals("167")) {
			params.put("allSR", props.getProperty("subreport.manfaat.manf_all_167") + ".jasper");
		//end-care, produk gak laku, buat nya setengah mati (Yusuf)
		// *}else if(businessId.equals("168") || businessId.equals("169")){
		}else if(businessId.equals("168") || businessId.equals("169") || businessId.equals("210")){
			params.put("allSR", props.getProperty("subreport.manfaat.manf_all_168") + ".jasper");
		//investa, fontnya dikecilin dikit untuk manfaatnya (Yusuf)
		}else if(businessId.equals("111") && lsdbs_number==1) {
			params.put("utamaSR", props.getProperty("subreport.manfaat.manf_utama_investa")+".jasper");
		//maxi invest, ada persentasenya di bagian bawah
		}else if((businessId.equals("114") && (lsdbs_number == 1 || lsdbs_number == 3 || lsdbs_number == 4)) ||
				 (businessId.equals("137") && (lsdbs_number == 1 || lsdbs_number == 2 || lsdbs_number == 3))){
			params.put("allSR", props.getProperty("subreport.manfaat.manf_all_maxi_invest") + ".jasper");
		}
		
		params.put("subReportDatakeys", new String[] {"utamaDS", "tambahanDS", "allDS"});

		if(!special.equals("")) {
			if(special.equals("true")) {
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.specialcase").toString());
			}else if(special.equals("54")) {
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case54"));
			}
		}else {
			if(businessId.equals("181")){
				params.put("usiaPP", il_mspo_age);
				params.put("usiaTT", il_age);
				params.put("lscbId", il_cbid);
				params.put("lsreId", lsre_id);
				if(il_age<=48){//ganti ketentuan manfaat, batas usia jadi 48 dari sebelumnya 47.
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.proteksi46").toString());
				}else{
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.proteksi47").toString());
				}
			}else if(businessId.equals("191")){
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.rencana_cerdas").toString());
				//params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat").toString());
			}else{
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat").toString());
			}
		}
				
		
		return params;
	}
	
	
	public Map<String, Object> prosesCetakManfaat(String spaj, String lus_id, HttpServletRequest request) throws Exception{
		
		Map info = uwDao.selectInfoCetakManfaat(spaj);
		int il_cbid = ((Integer) info.get("lscb_id"));
		int il_age = ((Integer) info.get("mste_age"));
		
		int tamb;
		if (request == null){
			tamb = -1;
		}else{
			tamb = ServletRequestUtils.getIntParameter(request, "tamb", -1); //ini untuk tambahan klausula manfaat			
		}

		String businessId=FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 3);
		
		String special = "";
		if("046".equals(businessId) && il_cbid!=0 && il_age==56) { 
			//Special case 7/01/2002
			special = "true";
		}else if("053,054,067,131,132,148".indexOf(businessId)>-1) {
			special = "54";
			//Yusuf (24/05/2007) - untuk manfaat PA sekarang ambil dari LST_MANFAAT
//		}else if("073".equals(businessId)) {
//			special = "73";
		}		
		
		Map params = new HashMap();
		int urut=-1;
		
		String lku_id = (String) info.get("lku_id");
		int il_mspo_age = ((Integer) info.get("mspo_age"));
		int lsdbs = ((Integer) info.get("lsdbs_number"));
		String lsdbs_name = (String) info.get("lsdbs_name");
		Double idb_premium = (Double) info.get("mspr_premium");
		Integer payPeriod = (Integer) info.get("mspo_pay_period");
		int insPeriod = nilaiTunai.getLamaTanggung(((Integer) info.get("mspo_ins_period")), businessId, lku_id);
		int li_medical = ((Integer) info.get("mste_medical"));
		Integer ii_umur_beasiswa = (Integer) info.get("mspo_umur_beasiswa");
		Integer lsre_id = (Integer) info.get("lsre_id");
		Double mspr_tsi = (Double) info.get("mspr_tsi");
		
		if(logger.isDebugEnabled())logger.debug("Initializing Printing Manfaat");

		//Yusuf (24/05/2007) - untuk manfaat PA sekarang ambil dari LST_MANFAAT
//		if("073".equals(businessId)) {
//			if(special.equals("73")) {
//				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case73"));
//				List temp = new ArrayList(); Map map = new HashMap();
//				map.put("1", "1"); temp.add(map);
//				
//				return params;
//			}
//		}else 
		if("074,076,096,099,135,136,182".indexOf(businessId)>-1 ||(businessId.equals("111") && lsdbs==2)) {
			return prosesManfaat7476(lus_id, spaj, il_age, businessId, lsdbs, il_cbid, idb_premium, ii_umur_beasiswa, lku_id);
		}else if("079,091".indexOf(businessId)>-1) {
			return prosesManfaat7991(lus_id, spaj, il_age, businessId, lsdbs, il_cbid, idb_premium, ii_umur_beasiswa, lku_id);
		}else if("080,092".indexOf(businessId)>-1) {
			params.put("jenis", 1);
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case80"));
			return params;
		}else if("081".equals(businessId)) {
			return prosesManfaat81(il_cbid, lku_id);				
		}else if("213,217,218,221".indexOf(businessId)>-1){
			return prosesManfaat100(businessId,lsdbs);					
		}else if("215".equals(businessId)) {
			return prosesManfaat100(businessId,lsdbs);			
		}else if("220".equals(businessId) && (lsdbs==2 || lsdbs==1 || lsdbs==4) || ("224".equals(businessId) && (lsdbs==1 || lsdbs==2 || lsdbs==3)) ) {

			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_jempol_link"));
			params.put("lsbsId",businessId);
			params.put("lsdbs",lsdbs);
			return params;	
		}else if(("212".equals(businessId) && (lsdbs==4 || lsdbs==5 || lsdbs==7)) ) {
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_term_rop"));
			params.put("lsbsId",businessId);
			params.put("lsdbs",lsdbs);
			return params;	
		}else if(("223".equals(businessId) && (lsdbs==1 || lsdbs==2)) || ("212".equals(businessId) && (lsdbs==1 || lsdbs==3 || lsdbs==6 || lsdbs==8 || lsdbs==9 || lsdbs==10 || lsdbs==12 || lsdbs == 13 || lsdbs == 14)) ){ // add 212-14 nana
			return prosesManfaatTermRop(businessId, lsdbs, spaj, lus_id, il_cbid);
		}else if("171".equals(businessId) && lsdbs==2){
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_hcp"));
			return params;
		}else if("195".equals(businessId) || "204".equals(businessId)){
			if("204".equals(businessId)){
				if (lsdbs >= 37 && lsdbs <= 48){
					params.put("jenis",5);//5=  MARZ, VALDO, GOS , SYNERGYS , SSI / DMTM New SIO
				}else{
					params.put("jenis",1);//1= syariah,0= konven
				}
				
			}
			if("195".equals(businessId)&& (lsdbs>=13  &&  lsdbs<=24) || "195".equals(businessId)&& (lsdbs>=85 && lsdbs<=96) ){ /* [141424] Nana 85-96 */
				/* Nana Tambah Kondisi untuk dmtm bjb */
				if (lsdbs>=85 && lsdbs<=96){
					params.put("jenis",4);//4 Smile Hospital care DMTM BJB
				} else {
					params.put("jenis",2);//2 Untuk Hospital care
				}
			}else if("195".equals(businessId)&& (lsdbs>=49  &&  lsdbs<=60)){
				params.put("jenis",3);//3 VIP hospital plan
			}else if("195".equals(businessId)&& (lsdbs>=37 &&  lsdbs<=48)){
				params.put("jenis",6);//6 SMILE HOSPITAL PLAN
			}else if("195".equals(businessId)&& (lsdbs>=61  &&  lsdbs<=72)){
				params.put("jenis",4);//4 Untuk SMile Hospital Care (DKI)
			}else if("195".equals(businessId)&& (lsdbs>=73  &&  lsdbs<=84)){
				params.put("jenis",7);//7 Untuk SMiLe Hospital Cash Plan
			}
			if(lsdbs%12==1 || lsdbs == 85){				/* [141424] Nana 85 */						
				params.put("tipe", "R-100");
				params.put("biayainap", "100,000");
			}else if(lsdbs%12==2 || lsdbs == 86){		/* [141424] Nana 86 */
				params.put("tipe", "R-200");
				params.put("biayainap", "200,000");
			}else if(lsdbs%12==3 || lsdbs == 87){       /* [141424] Nana 87 */
				params.put("tipe", "R-300");
				params.put("biayainap", "300,000");
			}else if(lsdbs%12==4 || lsdbs == 88){       /* [141424] Nana 88 */
				params.put("tipe", "R-400");
				params.put("biayainap", "400,000");
			}else if(lsdbs%12==5 || lsdbs == 89){       /* [141424] Nana 89 */
				params.put("tipe", "R-500");
				params.put("biayainap", "500,000");
			}else if(lsdbs%12==6 || lsdbs == 90){       /* [141424] Nana 90 */
				params.put("tipe", "R-600");
				params.put("biayainap", "600,000");
			}else if(lsdbs%12==7 || lsdbs == 91){       /* [141424] Nana 91 */
				params.put("tipe", "R-700");
				params.put("biayainap", "700,000");
			}else if(lsdbs%12==8 || lsdbs == 92){       /* [141424] Nana 92 */
				params.put("tipe", "R-800");
				params.put("biayainap", "800,000");
			}else if(lsdbs%12==9 || lsdbs == 93){       /* [141424] Nana 93 */
				params.put("tipe", "R-900");
				params.put("biayainap", "900,000");
			}else if(lsdbs%12==10 || lsdbs == 94){      /* [141424] Nana 94 */  
				params.put("tipe", "R-1000");
				params.put("biayainap", "1,000,000");
			}else if(lsdbs%12==11 || lsdbs == 95){      /* [141424] Nana 95 */
				params.put("tipe", "R-1500");
				params.put("biayainap", "1,500,000");
			}else if(lsdbs%12==0 || lsdbs == 96){       /* [141424] Nana 96 */
				params.put("tipe", "R-2000");
				params.put("biayainap", "2,000,000");
			}
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_hcp_prov"));
			return params;
		}else if("178".equals(businessId)){
			return prosesManfaat178(lus_id, spaj, il_age, businessId, lsdbs, il_cbid, idb_premium, ii_umur_beasiswa, lku_id);
		}else if("208".equals(businessId) || "219".equals(businessId)){
			return prosesManfaat208(lus_id, spaj, il_age, businessId, lsdbs, il_cbid, idb_premium, ii_umur_beasiswa, lku_id, mspr_tsi);
		}else if( ("183".equals(businessId) && (lsdbs<=120 || (lsdbs>=133 && lsdbs<=134)) ) || "189".equals(businessId) || "193".equals(businessId) || "183".equals(businessId) && (lsdbs>=135 && lsdbs<=149) ){ /* [141424] NANA PRODUK BARU SMILE MEDICAL CARE 183 (135-149) */
			if("183".equals(businessId) && (lsdbs>=91 && lsdbs<=105)) lsdbs_name = "VIP MEDICAL PLAN";
			if("183".equals(businessId) && (lsdbs>=106 && lsdbs<=120)) lsdbs_name = "SMiLe MEDICAL CARE";
			if("183".equals(businessId) && (lsdbs>=133 && lsdbs<=134)) lsdbs_name = "SMiLe MEDICAL CARE";
			/* [141424] - NANA PRODUK BARU SMILE MEDICAL CARE 183 (135-149) */
			if("183".equals(businessId) && (lsdbs>=135 && lsdbs<=149)) lsdbs_name = "SMiLe MEDICAL CARE";
			if(lsdbs%15==1){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "A");
				params.put("biayainap", "100,000");
				params.put("biayaicu", "200,000");
				params.put("bataslimit", "12,500,000");
				params.put("batasmax", "50,000,000");
			}else if(lsdbs%15==2){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "B");
				params.put("biayainap", "150,000");
				params.put("biayaicu", "300,000");
				params.put("bataslimit", "18,750,000");
				params.put("batasmax", "75,000,000");
			}else if(lsdbs%15==3){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "C");
				params.put("biayainap", "200,000");
				params.put("biayaicu", "400,000");
				params.put("bataslimit", "25,000,000");
				params.put("batasmax", "100,000,000");
			}else if(lsdbs%15==4){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "D");
				params.put("biayainap", "250,000");
				params.put("biayaicu", "500,000");
				params.put("bataslimit", "31,250,000");
				params.put("batasmax", "125,000,000");
			}else if(lsdbs%15==5){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "E");
				params.put("biayainap", "300,000");
				params.put("biayaicu", "600,000");
				params.put("bataslimit", "37,500,000");
				params.put("batasmax", "150,000,000");
			}else if(lsdbs%15==6){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "F");
				params.put("biayainap", "350,000");
				params.put("biayaicu", "700,000");
				params.put("bataslimit", "43,750,000");
				params.put("batasmax", "175,000,000");
			}else if(lsdbs%15==7){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "G");
				params.put("biayainap", "400,000");
				params.put("biayaicu", "800,000");
				params.put("bataslimit", "50,000,000");
				params.put("batasmax", "200,000,000");
			}else if(lsdbs%15==8){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "H");
				params.put("biayainap", "500,000");
				params.put("biayaicu", "1,000,000");
				params.put("bataslimit", "100,000,000");
				params.put("batasmax", "400,000,000");
			}else if(lsdbs%15==9){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "I");
				params.put("biayainap", "600,000");
				params.put("biayaicu", "1,200,000");
				params.put("bataslimit", "125,000,000");
				params.put("batasmax", "500,000,000");
			}else if(lsdbs%15==10){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "J");
				params.put("biayainap", "700,000");
				params.put("biayaicu", "1,400,000");
				params.put("bataslimit", "150,000,000");
				params.put("batasmax", "600,000,000");
			}else if(lsdbs%15==11){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "K");
				params.put("biayainap", "800,000");
				params.put("biayaicu", "1,600,000");
				params.put("bataslimit", "175,000,000");
				params.put("batasmax", "700,000,000");
			}else if(lsdbs%15==12){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "L");
				params.put("biayainap", "900,000");
				params.put("biayaicu", "1,800,000");
				params.put("bataslimit", "200,000,000");
				params.put("batasmax", "800,000,000");
			}else if(lsdbs%15==13){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "M");
				params.put("biayainap", "1,000,000");
				params.put("biayaicu", "2,000,000");
				params.put("bataslimit", "225,000,000");
				params.put("batasmax", "900,000,000");
			}else if(lsdbs%15==14){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "N");
				params.put("biayainap", "1,500,000");
				params.put("biayaicu", "3,000,000");
				params.put("bataslimit", "350,000,000");
				params.put("batasmax", "1,400,000,000");
			}else if(lsdbs%15==0){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "O");
				params.put("biayainap", "2,000,000");
				params.put("biayaicu", "4,000,000");
				params.put("bataslimit", "475,000,000");
				params.put("batasmax", "1,900,000,000");
			}
			
			 /* [141424] - NANA START */
			if("183".equals(businessId) && (lsdbs>=135 && lsdbs<=149)){ 
				if(lsdbs == 135){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "A");
					params.put("biayainap", "100,000");
					params.put("biayaicu", "200,000");
					params.put("bataslimit", "12,500,000");
					params.put("batasmax", "50,000,000");
				}else if(lsdbs == 136){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "B");
					params.put("biayainap", "150,000");
					params.put("biayaicu", "300,000");
					params.put("bataslimit", "18,750,000");
					params.put("batasmax", "75,000,000");
				}else if(lsdbs == 137){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "C");
					params.put("biayainap", "200,000");
					params.put("biayaicu", "400,000");
					params.put("bataslimit", "25,000,000");
					params.put("batasmax", "100,000,000");
				}else if(lsdbs == 138){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "D");
					params.put("biayainap", "250,000");
					params.put("biayaicu", "500,000");
					params.put("bataslimit", "31,250,000");
					params.put("batasmax", "125,000,000");
				}else if(lsdbs == 139){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "E");
					params.put("biayainap", "300,000");
					params.put("biayaicu", "600,000");
					params.put("bataslimit", "37,500,000");
					params.put("batasmax", "150,000,000");
				}else if(lsdbs == 140){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "F");
					params.put("biayainap", "350,000");
					params.put("biayaicu", "700,000");
					params.put("bataslimit", "43,750,000");
					params.put("batasmax", "175,000,000");
				}else if(lsdbs == 141){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "G");
					params.put("biayainap", "400,000");
					params.put("biayaicu", "800,000");
					params.put("bataslimit", "50,000,000");
					params.put("batasmax", "200,000,000");
				}else if(lsdbs == 142){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "H");
					params.put("biayainap", "500,000");
					params.put("biayaicu", "1,000,000");
					params.put("bataslimit", "100,000,000");
					params.put("batasmax", "400,000,000");
				}else if(lsdbs == 143){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "I");
					params.put("biayainap", "600,000");
					params.put("biayaicu", "1,200,000");
					params.put("bataslimit", "125,000,000");
					params.put("batasmax", "500,000,000");
				}else if(lsdbs == 144){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "J");
					params.put("biayainap", "700,000");
					params.put("biayaicu", "1,400,000");
					params.put("bataslimit", "150,000,000");
					params.put("batasmax", "600,000,000");
				}else if(lsdbs == 145){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "K");
					params.put("biayainap", "800,000");
					params.put("biayaicu", "1,600,000");
					params.put("bataslimit", "175,000,000");
					params.put("batasmax", "700,000,000");
				}else if(lsdbs == 146){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "L");
					params.put("biayainap", "900,000");
					params.put("biayaicu", "1,800,000");
					params.put("bataslimit", "200,000,000");
					params.put("batasmax", "800,000,000");
				}else if(lsdbs == 147){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "M");
					params.put("biayainap", "1,000,000");
					params.put("biayaicu", "2,000,000");
					params.put("bataslimit", "225,000,000");
					params.put("batasmax", "900,000,000");
				}else if(lsdbs == 148){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "N");
					params.put("biayainap", "1,500,000");
					params.put("biayaicu", "3,000,000");
					params.put("bataslimit", "350,000,000");
					params.put("batasmax", "1,400,000,000");
				}else if(lsdbs == 149){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "O");
					params.put("biayainap", "2,000,000");
					params.put("biayaicu", "4,000,000");
					params.put("bataslimit", "475,000,000");
					params.put("batasmax", "1,900,000,000");
				}
			} /* [141424] - NANA END */
			
			if("189".equals(businessId) && lsdbs >= 33 && lsdbs <= 47){ // MARZ VALDO GOS VASCO DENA SYNERGYS, SSI,  PT AUSINDO PRATAMA KARYA (DMTM SIO)
				if(lsdbs%16==1){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "A");
					params.put("biayainap", "100,000");
					params.put("biayaicu", "200,000");
					params.put("bataslimit", "12,500,000");
					params.put("batasmax", "50,000,000");
				}else if(lsdbs%16==2){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "B");
					params.put("biayainap", "150,000");
					params.put("biayaicu", "300,000");
					params.put("bataslimit", "18,750,000");
					params.put("batasmax", "75,000,000");
				}else if(lsdbs%16==3){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "C");
					params.put("biayainap", "200,000");
					params.put("biayaicu", "400,000");
					params.put("bataslimit", "25,000,000");
					params.put("batasmax", "100,000,000");
				}else if(lsdbs%16==4){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "D");
					params.put("biayainap", "250,000");
					params.put("biayaicu", "500,000");
					params.put("bataslimit", "31,250,000");
					params.put("batasmax", "125,000,000");
				}else if(lsdbs%16==5){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "E");
					params.put("biayainap", "300,000");
					params.put("biayaicu", "600,000");
					params.put("bataslimit", "37,500,000");
					params.put("batasmax", "150,000,000");
				}else if(lsdbs%16==6){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "F");
					params.put("biayainap", "350,000");
					params.put("biayaicu", "700,000");
					params.put("bataslimit", "43,750,000");
					params.put("batasmax", "175,000,000");
				}else if(lsdbs%16==7){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "G");
					params.put("biayainap", "400,000");
					params.put("biayaicu", "800,000");
					params.put("bataslimit", "50,000,000");
					params.put("batasmax", "200,000,000");
				}else if(lsdbs%16==8){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "H");
					params.put("biayainap", "500,000");
					params.put("biayaicu", "1,000,000");
					params.put("bataslimit", "100,000,000");
					params.put("batasmax", "400,000,000");
				}else if(lsdbs%16==9){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "I");
					params.put("biayainap", "600,000");
					params.put("biayaicu", "1,200,000");
					params.put("bataslimit", "125,000,000");
					params.put("batasmax", "500,000,000");
				}else if(lsdbs%16==10){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "J");
					params.put("biayainap", "700,000");
					params.put("biayaicu", "1,400,000");
					params.put("bataslimit", "150,000,000");
					params.put("batasmax", "600,000,000");
				}else if(lsdbs%16==11){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "K");
					params.put("biayainap", "800,000");
					params.put("biayaicu", "1,600,000");
					params.put("bataslimit", "175,000,000");
					params.put("batasmax", "700,000,000");
				}else if(lsdbs%16==12){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "L");
					params.put("biayainap", "900,000");
					params.put("biayaicu", "1,800,000");
					params.put("bataslimit", "200,000,000");
					params.put("batasmax", "800,000,000");
				}else if(lsdbs%16==13){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "M");
					params.put("biayainap", "1,000,000");
					params.put("biayaicu", "2,000,000");
					params.put("bataslimit", "225,000,000");
					params.put("batasmax", "900,000,000");
				}else if(lsdbs%16==14){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "N");
					params.put("biayainap", "1,500,000");
					params.put("biayaicu", "3,000,000");
					params.put("bataslimit", "350,000,000");
					params.put("batasmax", "1,400,000,000");
				}else if(lsdbs%16==15){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "O");
					params.put("biayainap", "2,000,000");
					params.put("biayaicu", "4,000,000");
					params.put("bataslimit", "475,000,000");
					params.put("batasmax", "1,900,000,000");
				}
			}
			//helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
			if("189".equals(businessId) && (lsdbs >= 48 && lsdbs <= 62)){ 
				if(lsdbs == 48){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "A");
					params.put("biayainap", "100,000");
					params.put("biayaicu", "200,000");
					params.put("bataslimit", "12,500,000");
					params.put("batasmax", "50,000,000");
				}else if(lsdbs == 49){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "B");
					params.put("biayainap", "150,000");
					params.put("biayaicu", "300,000");
					params.put("bataslimit", "18,750,000");
					params.put("batasmax", "75,000,000");
				}else if(lsdbs == 50){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "C");
					params.put("biayainap", "200,000");
					params.put("biayaicu", "400,000");
					params.put("bataslimit", "25,000,000");
					params.put("batasmax", "100,000,000");
				}else if(lsdbs == 51){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "D");
					params.put("biayainap", "250,000");
					params.put("biayaicu", "500,000");
					params.put("bataslimit", "31,250,000");
					params.put("batasmax", "125,000,000");
				}else if(lsdbs == 52){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "E");
					params.put("biayainap", "300,000");
					params.put("biayaicu", "600,000");
					params.put("bataslimit", "37,500,000");
					params.put("batasmax", "150,000,000");
				}else if(lsdbs == 53){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "F");
					params.put("biayainap", "350,000");
					params.put("biayaicu", "700,000");
					params.put("bataslimit", "43,750,000");
					params.put("batasmax", "175,000,000");
				}else if(lsdbs == 54){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "G");
					params.put("biayainap", "400,000");
					params.put("biayaicu", "800,000");
					params.put("bataslimit", "50,000,000");
					params.put("batasmax", "200,000,000");
				}else if(lsdbs == 55){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "H");
					params.put("biayainap", "500,000");
					params.put("biayaicu", "1,000,000");
					params.put("bataslimit", "100,000,000");
					params.put("batasmax", "400,000,000");
				}else if(lsdbs == 56){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "I");
					params.put("biayainap", "600,000");
					params.put("biayaicu", "1,200,000");
					params.put("bataslimit", "125,000,000");
					params.put("batasmax", "500,000,000");
				}else if(lsdbs == 57){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "J");
					params.put("biayainap", "700,000");
					params.put("biayaicu", "1,400,000");
					params.put("bataslimit", "150,000,000");
					params.put("batasmax", "600,000,000");
				}else if(lsdbs == 58){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "K");
					params.put("biayainap", "800,000");
					params.put("biayaicu", "1,600,000");
					params.put("bataslimit", "175,000,000");
					params.put("batasmax", "700,000,000");
				}else if(lsdbs == 59){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "L");
					params.put("biayainap", "900,000");
					params.put("biayaicu", "1,800,000");
					params.put("bataslimit", "200,000,000");
					params.put("batasmax", "800,000,000");
				}else if(lsdbs == 60){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "M");
					params.put("biayainap", "1,000,000");
					params.put("biayaicu", "2,000,000");
					params.put("bataslimit", "225,000,000");
					params.put("batasmax", "900,000,000");
				}else if(lsdbs == 61){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "N");
					params.put("biayainap", "1,500,000");
					params.put("biayaicu", "3,000,000");
					params.put("bataslimit", "350,000,000");
					params.put("batasmax", "1,400,000,000");
				}else if(lsdbs == 62){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("tipe", "O");
					params.put("biayainap", "2,000,000");
					params.put("biayaicu", "4,000,000");
					params.put("bataslimit", "475,000,000");
					params.put("batasmax", "1,900,000,000");
				}
			}
		
			if("193".equals(businessId)){
				if(lsdbs==1 ){
					params.put("biayaaneka", "1,750,000");
					params.put("biayabedah", "12,000,000");
					params.put("biayakunjung", "50,000");
					params.put("biayakonsul", "300,000");
					params.put("biayaperawat", "50,000");
					params.put("biayaambulance", "40,000");
					params.put("biayabafter", "350,000");
				}else if(lsdbs==2){
					params.put("judul",lsdbs_name);
					params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					params.put("biayaaneka", "2,250,000");
					params.put("biayabedah", "17,250,000");
					params.put("biayakunjung", "60,000");
					params.put("biayakonsul", "360,000");
					params.put("biayaperawat", "60,000");
					params.put("biayaambulance", "45,000");
					params.put("biayabafter", "500,000");
				}else if(lsdbs==3){
					params.put("biayaaneka", "2,750,000");
					params.put("biayabedah", "22,500,000");
					params.put("biayakunjung", "70,000");
					params.put("biayakonsul", "420,000");
					params.put("biayaperawat", "70,000");
					params.put("biayaambulance", "50,000");
					params.put("biayabafter", "650,000");
				}else if(lsdbs==4){
					params.put("biayaaneka", "3,250,000");
					params.put("biayabedah", "27,500,000");
					params.put("biayakunjung", "80,000");
					params.put("biayakonsul", "480,000");
					params.put("biayaperawat", "80,000");
					params.put("biayaambulance", "55,000");
					params.put("biayabafter", "800,000");
				}else if(lsdbs==5){
					params.put("biayaaneka", "3,750,000");
					params.put("biayabedah", "33,000,000");
					params.put("biayakunjung", "90,000");
					params.put("biayakonsul", "540,000");
					params.put("biayaperawat", "90,000");
					params.put("biayaambulance", "60,000");
					params.put("biayabafter", "950,000");
				}else if(lsdbs==6){
					params.put("biayaaneka", "4,250,000");
					params.put("biayabedah", "38,250,000");
					params.put("biayakunjung", "100,000");
					params.put("biayakonsul", "600,000");
					params.put("biayaperawat", "100,000");
					params.put("biayaambulance", "65,000");
					params.put("biayabafter", "1,100,000");
				}else if(lsdbs==7){
					params.put("biayaaneka", "4,750,000");
					params.put("biayabedah", "43,500,000");
					params.put("biayakunjung", "110,000");
					params.put("biayakonsul", "660,000");
					params.put("biayaperawat", "110,000");
					params.put("biayaambulance", "70,000");
					params.put("biayabafter", "1,250,000");
				}else if(lsdbs==8){
					params.put("biayaaneka", "5,750,000");
					params.put("biayabedah", "54,000,000");
					params.put("biayakunjung", "130,000");
					params.put("biayakonsul", "780,000");
					params.put("biayaperawat", "130,000");
					params.put("biayaambulance", "80,000");
					params.put("biayabafter", "1,550,000");
				}else if(lsdbs==9){
					params.put("biayaaneka", "6,750,000");
					params.put("biayabedah", "64,500,000");
					params.put("biayakunjung", "150,000");
					params.put("biayakonsul", "900,000");
					params.put("biayaperawat", "150,000");
					params.put("biayaambulance", "90,000");
					params.put("biayabafter", "1,850,000");
				}else if(lsdbs==10){
					params.put("biayaaneka", "7,750,000");
					params.put("biayabedah", "75,000,000");
					params.put("biayakunjung", "170,000");
					params.put("biayakonsul", "1,020,000");
					params.put("biayaperawat", "170,000");
					params.put("biayaambulance", "100,000");
					params.put("biayabafter", "2,150,000");
				}else if(lsdbs==11){
					params.put("biayaaneka", "8,750,000");
					params.put("biayabedah", "85,500,000");
					params.put("biayakunjung", "190,000");
					params.put("biayakonsul", "1,140,000");
					params.put("biayaperawat", "190,000");
					params.put("biayaambulance", "110,000");
					params.put("biayabafter", "2,450,000");
				}else if(lsdbs==12){
					params.put("biayaaneka", "9,750,000");
					params.put("biayabedah", "96,000,000");
					params.put("biayakunjung", "210,000");
					params.put("biayakonsul", "1,260,000");
					params.put("biayaperawat", "210,000");
					params.put("biayaambulance", "120,000");
					params.put("biayabafter", "2,750,000");
				}else if(lsdbs==13){
					params.put("biayaaneka", "10,750,000");
					params.put("biayabedah", "106,500,000");
					params.put("biayakunjung", "230,000");
					params.put("biayakonsul", "1,380,000");
					params.put("biayaperawat", "230,000");
					params.put("biayaambulance", "130,000");
					params.put("biayabafter", "3,050,000");
				}else if(lsdbs==14){
					params.put("biayaaneka", "15,750,000");
					params.put("biayabedah", "159,000,000");
					params.put("biayakunjung", "330,000");
					params.put("biayakonsul", "1,980,000");
					params.put("biayaperawat", "330,000");
					params.put("biayaambulance", "180,000");
					params.put("biayabafter", "4,550,000");
				}else if(lsdbs==15){
					params.put("biayaaneka", "20,750,000");
					params.put("biayabedah", "211,500,000");
					params.put("biayakunjung", "430,000");
					params.put("biayakonsul", "2,580,000");
					params.put("biayaperawat", "430,000");
					params.put("biayaambulance", "230,000");
					params.put("biayabafter", "6,050,000");
				}
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_eka_sehat_il"));
			}else{
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_eka_sehat"));
			}
			return params;
		}else if(("183".equals(businessId) && (lsdbs>=121 && lsdbs<=122))){
			if(lsdbs==121){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "P");
				params.put("biayainap", "5,000,000");
				params.put("biayaicu", "4,000,000");
				params.put("bataslimit", "350,000,000");
				params.put("batasmax", "5,000,000,000");
			}else if(lsdbs==122){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "Q");
				params.put("biayainap", "7,500,000");
				params.put("biayaicu", "4,000,000");
				params.put("bataslimit", "475,000,000");
				params.put("batasmax", "6,000,000,000");
			}
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_eka_sehat"));
			return params;
		}else if("214".equals(businessId) || "225".equals(businessId) ){
			if((lsdbs%15==1 || lsdbs%15==7) && "214".equals(businessId) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "GOLD");
				params.put("biayainap", "1,500,000");
				params.put("biayaicu", "3,000,000");
				params.put("kemoterapi", "31,500,000");
				params.put("cucidarah", "31,500,000");
				params.put("santunan", "22,500,000");
				params.put("meninggal", "375,000,000");
				params.put("batasmax", "1,400,000,000");
				
			}else if((lsdbs%15==2 || lsdbs%15==9) && "214".equals(businessId)
					|| ( lsdbs==6 &&  "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "PLATINUM");
				params.put("biayainap", "2,000,000");
				params.put("biayaicu", "4,000,000");
				params.put("kemoterapi", "41,500,000");
				params.put("cucidarah", "41,500,000");
				params.put("santunan", "30,000,000");
				params.put("meninggal", "500,000,000");
				params.put("batasmax", "1,900,000,000");
				
			}else if((lsdbs%15==3 && "214".equals(businessId))){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "COPPER");
				params.put("biayainap", "500,000");
				params.put("biayaicu", "1,000,000");
				params.put("kemoterapi", "11,500,000");
				params.put("cucidarah", "11,500,000");
				params.put("santunan", "7,500,000");
				params.put("meninggal", "125,000,000");
				params.put("batasmax", "400,000,000");
				
			}else if((lsdbs==1 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "COPPER");
				params.put("biayainap", "400,000");
				params.put("biayaicu", "800,000");
				params.put("kemoterapi", "9,400,000");
				params.put("cucidarah", "9,400,000");
				params.put("santunan", "6,000,000");
				params.put("meninggal", "100,000,000");
				params.put("batasmax", "320,000,000");
				
			}else if((lsdbs%15==4 && "214".equals(businessId)) || (lsdbs==2 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "BRONZE");
				params.put("biayainap", "800,000");
				params.put("biayaicu", "1,600,000");
				params.put("kemoterapi", "18,500,000");
				params.put("cucidarah", "18,500,000");
				params.put("santunan", "12,000,000");
				params.put("meninggal", "200,000,000");
				params.put("batasmax", "700,000,000");
				
			}else if((lsdbs%15==5 && "214".equals(businessId)) || (lsdbs==3 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "SILVER");
				params.put("biayainap", "1,000,000");
				params.put("biayaicu", "2,000,000");
				params.put("kemoterapi", "21,500,000");
				params.put("cucidarah", "21,500,000");
				params.put("santunan", "15,000,000");
				params.put("meninggal", "250,000,000");
				params.put("batasmax", "900,000,000");
				
			}else if((lsdbs%15==6 && "214".equals(businessId)) || (lsdbs==4 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "SHAPPIRE");
				params.put("biayainap", "1,200,000");
				params.put("biayaicu", "2,400,000");
				params.put("kemoterapi", "28,000,000");
				params.put("cucidarah", "28,000,000");
				params.put("santunan", "18,000,000");
				params.put("meninggal", "300,000,000");
				params.put("batasmax", "1,000,000,000");
				
			}else if((lsdbs%15==8 && "214".equals(businessId)) || (lsdbs==5 && "225".equals(businessId)) ){
				params.put("judul",lsdbs_name);
				params.put("text", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
				params.put("tipe", "DIAMOND");
				params.put("biayainap", "1,600,000");
				params.put("biayaicu", "3,200,000");
				params.put("kemoterapi", "37,000,000");
				params.put("cucidarah", "37,000,000");
				params.put("santunan", "24,000,000");
				params.put("meninggal", "400,000,000");
				params.put("batasmax", "1,500,000,000");
			}
			params.put("spaj",spaj);
			
			String subJnProv[] = new String[] {};
			
			if("214".equals(businessId)){ //start provestara 214 RJ RG RB PK
				
				params.put("catatanri", " ");	
				
				List  manfaatrj = this.uwDao.selectPlanProvestara(spaj, 840);
				List  manfaatrg = this.uwDao.selectPlanProvestara(spaj, 841);
				List  manfaatrb = this.uwDao.selectPlanProvestara(spaj, 842);
				List  manfaatpk = this.uwDao.selectPlanProvestara(spaj, 843);
			
				
				if(manfaatrj.size()!=0) {				
					
					if(lsdbs%15==1 || lsdbs%15==7){
						
						params.put("kondokum", "125,000");
						params.put("kondospe", "375,000");
						params.put("konob", "250,000");
						params.put("obresdok", "8,750,000");
						params.put("biayates", "2,500,000");
						params.put("fisioterapirj", "190,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "12,500,000");	
						
					}else if(lsdbs%15==2 || lsdbs%15==9){
						params.put("kondokum", "150,000");
						params.put("kondospe", "450,000");
						params.put("konob", "300,000");
						params.put("obresdok", "10,500,000");
						params.put("biayates", "3,000,000");
						params.put("fisioterapirj", "225,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "15,000,000");	
						
					}else if(lsdbs%15== 3 ){
						params.put("kondokum", "75,000");
						params.put("kondospe", "225,000");
						params.put("konob", "150,000");
						params.put("obresdok", "5,250,000");
						params.put("biayates", "1,500,000");
						params.put("fisioterapirj", "115,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "7,500,000");	
						
					}else if(lsdbs%15== 4 ){
						params.put("kondokum", "90,000");
						params.put("kondospe", "270,000");
						params.put("konob", "180,000");
						params.put("obresdok", "6,300,000");
						params.put("biayates", "1,800,000");
						params.put("fisioterapirj", "140,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "9,000,000");	
						
					}else if(lsdbs%15== 5 ){
						params.put("kondokum", "100,000");
						params.put("kondospe", "300,000");
						params.put("konob", "200,000");
						params.put("obresdok", "7,000,000");
						params.put("biayates", "2,000,000");
						params.put("fisioterapirj", "150,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "10,000,000");	
						
					}else if(lsdbs%15== 6 ){
						params.put("kondokum", "110,000");
						params.put("kondospe", "320,000");
						params.put("konob", "220,000");
						params.put("obresdok", "7,700,000");
						params.put("biayates", "2,200,000");
						params.put("fisioterapirj", "170,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "11,000,000");	
						
					}else if(lsdbs%15== 8 ){
						params.put("kondokum", "135,000");
						params.put("kondospe", "405,000");
						params.put("konob", "270,000");
						params.put("obresdok", "9,450,000");
						params.put("biayates", "2,700,000");
						params.put("fisioterapirj", "210,000");
						params.put("administrasi", "50,000");
						params.put("batasmaxrj", "14,000,000");	
						
					}
					
					params.put("catatanrj", "\nCatatan Rawat Jalan (RJ):"+
							 "\n- Berlaku Masa tunggu untuk Pre-existing Condition dan Penyakit-Penyakit Khusus"+		
							 "\n- Penggantian 100%"+		
							 "\n- Dokter Spesialis TANPA rujukan dari dokter umum"+
							 "\n* Hanya untuk Tertanggung sampai usia  64 tahun");	
					
					params.put("subplanrj", JasperReportsUtils.convertReportData(manfaatrj));
					//StringUtils.addStringToArray(subJnProv, "subplanrj");
				}
				if(manfaatrg.size()!=0) {
									
					if(lsdbs%15==1 || lsdbs%15==7){
						
						params.put("pdasar", "1,500,000");
						params.put("pgusi", "1,000,000");
						params.put("pcegah", "1,500,000");
						params.put("pkomplek", "1,500,000");
						params.put("pperbaik", "750,000");
						params.put("pgigi", "1,500,000");
						params.put("batasmaxrg", "7,500,000");	
						
					}else if(lsdbs%15==2 || lsdbs%15==9){
						params.put("pdasar", "2,000,000");
						params.put("pgusi", "1,250,000");
						params.put("pcegah", "2,000,000");
						params.put("pkomplek", "2,000,000");
						params.put("pperbaik", "1,050,000");
						params.put("pgigi", "2,000,000");
						params.put("batasmaxrg", "10,000,000");	
					
					}else if(lsdbs%15==3){
						params.put("pdasar", "500,000");
						params.put("pgusi", "350,000");
						params.put("pcegah", "500,000");
						params.put("pkomplek", "500,000");
						params.put("pperbaik", "250,000");
						params.put("pgigi", "500,000");
						params.put("batasmaxrg", "2,500,000");	
					
					}else if(lsdbs%15==4){
						params.put("pdasar", "750,000");
						params.put("pgusi", "490,000");
						params.put("pcegah", "750,000");
						params.put("pkomplek", "750,000");
						params.put("pperbaik", "380,000");
						params.put("pgigi", "750,000");
						params.put("batasmaxrg", "3,750,000");	
					
					}else if(lsdbs%15==5){
						params.put("pdasar", "1,000,000");
						params.put("pgusi", "625,000");
						params.put("pcegah", "1,000,000");
						params.put("pkomplek", "1,000,000");
						params.put("pperbaik", "500,000");
						params.put("pgigi", "1,000,000");
						params.put("batasmaxrg", "5,000,000");	
					
					}else if(lsdbs%15==6){
						params.put("pdasar", "1,200,000");
						params.put("pgusi", "820,000");
						params.put("pcegah", "1,250,000");
						params.put("pkomplek", "1,250,000");
						params.put("pperbaik", "630,000");
						params.put("pgigi", "1,250,000");
						params.put("batasmaxrg", "6,250,000");	
					
					}else if(lsdbs%15==8){
						params.put("pdasar", "1,750,000");
						params.put("pgusi", "1,130,000");
						params.put("pcegah", "1,750,000");
						params.put("pkomplek", "1,750,000");
						params.put("pperbaik", "900,000");
						params.put("pgigi", "1,750,000");
						params.put("batasmaxrg", "8,750,000");	
					
					}
					
					params.put("catatanrg", "\nCatatan Rawat Gigi (RG) :" +
							 "\n- Penggantian 100%");	
					
					params.put("subplanrg", JasperReportsUtils.convertReportData(manfaatrg));
					//StringUtils.addStringToArray(subJnProv, "subplanrg");
				}
				if(manfaatrb.size()!=0) {
					
					if(lsdbs%15==1 || lsdbs%15==7){
						
						params.put("mtanpaop", "20,000,000");
						params.put("mdengop", "35,000,000");
						params.put("keguguran", "15,000,000");
						params.put("pkhamilan", "3,800,000");
						
					}else if(lsdbs%15==2 || lsdbs%15==9){
						params.put("mtanpaop", "23,000,000");
						params.put("mdengop", "40,000,000");
						params.put("keguguran", "17,250,000");
						params.put("pkhamilan", "4,300,000");	
					
					}else if(lsdbs%15== 3 ){
						params.put("mtanpaop", "10,000,000");
						params.put("mdengop", "17,500,000");
						params.put("keguguran", "7,500,000");
						params.put("pkhamilan", "1,900,000");	
					
					}else if(lsdbs%15== 4 ){
						params.put("mtanpaop", "13,000,000");
						params.put("mdengop", "21,900,000");
						params.put("keguguran", "9,400,000");
						params.put("pkhamilan", "2,400,000");	
					
					}else if(lsdbs%15== 5 ){
						params.put("mtanpaop", "15,000,000");
						params.put("mdengop", "26,250,000");
						params.put("keguguran", "11,250,000");
						params.put("pkhamilan", "2,850,000");	
					
					}else if(lsdbs%15== 6 ){
						params.put("mtanpaop", "17,000,000");
						params.put("mdengop", "30,700,000");
						params.put("keguguran", "13,200,000");
						params.put("pkhamilan", "3,400,000");	
					
					}else if(lsdbs%15== 8 ){
						params.put("mtanpaop", "21,000,000");
						params.put("mdengop", "37,500,000");
						params.put("keguguran", "16,200,000");
						params.put("pkhamilan", "4,100,000");	
					
					}
					
					params.put("catatanrb", "\nCatatan Rawat Bersalin (RB) :" +
							 "\n- Premi berlaku untuk wanita yang eligible, yaitu wanita menikah, berusia < 45 tahun dan memiliki anak maksimum 2 orang"+
							 "\n- Berlaku masa tunggu 280 (dua ratus delapan puluh) hari untuk Rawat Bersalin"+
							 "\n- Melahirkan Dengan Operasi ( Sectio Cesaria ) harus berdasarkan Indikasi medis"+
							 "\n* Termasuk perawatan bayi maksimum 2 kali perawatan, berlaku maksimum 30 hari setelah kelahiran");	
					
					params.put("subplanrb", JasperReportsUtils.convertReportData(manfaatrb));
					//StringUtils.addStringToArray(subJnProv, "subplanrb");
				}
				if(manfaatpk.size()!=0) {
					
					if(lsdbs%15==1 || lsdbs%15==7){
						params.put("pkkonsultasi", "300,000");
						params.put("pkpemeriksaan", "1,000,000");
						params.put("pklayanan", "300,000");
						params.put("batasmaxpk", "5,500,000");
					
					}else if(lsdbs%15==2 || lsdbs%15==9){
						params.put("pkkonsultasi", "400,000");
						params.put("pkpemeriksaan", "1,500,000");
						params.put("pklayanan", "400,000");
						params.put("batasmaxpk", "7,500,000");
					
					}else if(lsdbs%15==3){
						params.put("pkkonsultasi", "200,000");
						params.put("pkpemeriksaan", "500,000");
						params.put("pklayanan", "200,000");
						params.put("batasmaxpk", "3,500,000");
					
					}else if(lsdbs%15==4){
						params.put("pkkonsultasi", "225,000");
						params.put("pkpemeriksaan", "625,000");
						params.put("pklayanan", "225,000");
						params.put("batasmaxpk", "4,000,000");
					
					}else if(lsdbs%15==5){
						params.put("pkkonsultasi", "250,000");
						params.put("pkpemeriksaan", "750,000");
						params.put("pklayanan", "250,000");
						params.put("batasmaxpk", "4,500,000");
					
					}else if(lsdbs%15==6){
						params.put("pkkonsultasi", "275,000");
						params.put("pkpemeriksaan", "875,000");
						params.put("pklayanan", "275,000");
						params.put("batasmaxpk", "5,000,000");
					
					}else if(lsdbs%15==8){
						params.put("pkkonsultasi", "350,000");
						params.put("pkpemeriksaan", "1,250,000");
						params.put("pklayanan", "350,000");
						params.put("batasmaxpk", "6,500,000");
					
					}
					
					params.put("catatanpk", "\nCatatan Penunjang Kesehatan (PK) :" +
							 "\n- Berlaku Masa tunggu untuk manfaat asuransi Penunjang Kesehatan selama 30 (tiga puluh) hari pertama sejak : tanggal berlaku periode asuransi atau	tanggal pemulihan Polis terakhir, hal mana yang terjadi terakhir"+
							 "\n- Penggantian 80%"+
							 "\n*  Penggantian Biaya Konsultasi Ahli Nutrisi hanya untuk Tertanggung yang didiagnosa menderita penyakit diabetes oleh Dokter dan harus menjalani Rawat Inap"+
							 "\n** Masa tunggu 1 tahun");	
					
					params.put("subplanpk", JasperReportsUtils.convertReportData(manfaatpk));
					//StringUtils.addStringToArray(subJnProv, "subplanpk");
				}
				//END provestara 214 RJ RG RB PK
				
			}else if("225".equals(businessId)){ //start SMILE MEDICAL PLUS 225 RJ RG RB PK
				
				params.put("catatanri", "\nCatatan Rawat Inap (RI) :" +
						 "\n- Berlaku Provider"+		
						 "\n* Diperoleh dalam 30 hari sesudah selesai perawatan di Rumah Sakit, dengan maksimal 10 kali pertahun");	
				
				List  manfaatrj = this.uwDao.selectPlanProvestara(spaj, 840);
				List  manfaatrjsmp = this.bacDao.selectPlanSmileMedicalPlus(spaj, 847," RJ ");
				List  manfaatrgsmp = this.bacDao.selectPlanSmileMedicalPlus(spaj, 847," RG ");
				List  manfaatrbsmp = this.bacDao.selectPlanSmileMedicalPlus(spaj, 847," RB ");
				List  manfaatpksmp = this.bacDao.selectPlanSmileMedicalPlus(spaj, 847," PK ");
				
				
				
				if(manfaatrjsmp.size()!=0) {
					
					Map map = new HashMap();
					Map rjsmp = (Map) manfaatrjsmp.get(0);
					String jenrjsmp = rjsmp.get("JENIS").toString();
					
					if("COPPER".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "45,000");
						params.put("kondospe", "135,000");
						params.put("konob", "90,000");
						params.put("obresdok", "900,000");
						params.put("biayates", "675,000");
						params.put("fisioterapirj", "70,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "2,700,000");	
						
					}else if("BRONZE".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "70,000");
						params.put("kondospe", "210,000");
						params.put("konob", "140,000");
						params.put("obresdok", "1,400,000");
						params.put("biayates", "1,050,000");
						params.put("fisioterapirj", "105,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "4,200,000");	
						
					}else if("SILVER".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "100,000");
						params.put("kondospe", "300,000");
						params.put("konob", "200,000");
						params.put("obresdok", "2,000,000");
						params.put("biayates", "1,500,000");
						params.put("fisioterapirj", "150,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "6,000,000");	
						
					}else if("SAPPHIRE".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "110,000");
						params.put("kondospe", "330,000");
						params.put("konob", "220,000");
						params.put("obresdok", "2,200,000");
						params.put("biayates", "1,650,000");
						params.put("fisioterapirj", "165,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "6,600,000");	
						
					}else if("DIAMOND".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "135,000");
						params.put("kondospe", "405,000");
						params.put("konob", "270,000");
						params.put("obresdok", "2,700,000");
						params.put("biayates", "2,025,000");
						params.put("fisioterapirj", "205,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "8,100,000");	
						
					}else if("PLATINUM".equalsIgnoreCase(jenrjsmp)){
						params.put("kondokum", "150,000");
						params.put("kondospe", "450,000");
						params.put("konob", "300,000");
						params.put("obresdok", "3,000,000");
						params.put("biayates", "3,250,000");
						params.put("fisioterapirj", "225,000");
						params.put("administrasi", "25,000");
						params.put("batasmaxrj", "9,000,000");	
						
					}
					
					params.put("catatanrj", "\nCatatan Rawat Jalan (RJ):"+
							 "\n- Berlaku Masa tunggu untuk Pre-existing Condition dan Penyakit-Penyakit Khusus"+		
							 "\n- Penggantian 100%"+		
							 "\n- Dokter Spesialis TANPA rujukan dari dokter umum"+
							 "\n- Berlaku Provider"+
							 "\n* Hanya untuk Tertanggung sampai usia  64 tahun");	
					
					params.put("subplanrj", JasperReportsUtils.convertReportData(manfaatrjsmp));
					//StringUtils.addStringToArray(subJnProv, "subplanrj");
				}
				if(manfaatrgsmp.size()!=0) {
					
					Map map = new HashMap();
					Map rgsmp = (Map) manfaatrgsmp.get(0);
					String jenrgsmp = rgsmp.get("JENIS").toString();
									
					if("COPPER".equalsIgnoreCase(jenrgsmp)){						
						params.put("pdasar", "400,000");
						params.put("pgusi", "85,000");
						params.put("pcegah", "85,000");
						params.put("pkomplek", "160,000");
						params.put("pperbaik", "410,000");
						params.put("pgigi", "330,000");
						params.put("batasmaxrg", "1,000,000");	
						
					}else if("BRONZE".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "440,000");
						params.put("pgusi", "95,000");
						params.put("pcegah", "95,000");
						params.put("pkomplek", "180,000");
						params.put("pperbaik", "455,000");
						params.put("pgigi", "365,000");
						params.put("batasmaxrg", "1,100,000");	
					
					}else if("SILVER".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "500,000");
						params.put("pgusi", "105,000");
						params.put("pcegah", "105,000");
						params.put("pkomplek", "200,000");
						params.put("pperbaik", "515,000");
						params.put("pgigi", "415,000");
						params.put("batasmaxrg", "1,250,000");	
					
					}else if("SAPPHIRE".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "600,000");
						params.put("pgusi", "125,000");
						params.put("pcegah", "125,000");
						params.put("pkomplek", "240,000");
						params.put("pperbaik", "620,000");
						params.put("pgigi", "500,000");
						params.put("batasmaxrg", "1,500,000");	
					
					}else if("DIAMOND".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "700,000");
						params.put("pgusi", "145,000");
						params.put("pcegah", "145,000");
						params.put("pkomplek", "280,000");
						params.put("pperbaik", "720,000");
						params.put("pgigi", "580,000");
						params.put("batasmaxrg", "1,750,000");	
					
					}else if("PLATINUM".equalsIgnoreCase(jenrgsmp)){
						params.put("pdasar", "1,000,000");
						params.put("pgusi", "210,000");
						params.put("pcegah", "210,000");
						params.put("pkomplek", "400,000");
						params.put("pperbaik", "1,025,000");
						params.put("pgigi", "825,000");
						params.put("batasmaxrg", "2,500,000");	
					
					}
					
					params.put("catatanrg", "\nCatatan Rawat Gigi (RG) :" +
							 "\n- Penggantian 80%"+		
							 "\n- Berlaku Reimbursement");	
					
					params.put("subplanrg", JasperReportsUtils.convertReportData(manfaatrgsmp));
					//StringUtils.addStringToArray(subJnProv, "subplanrg");
				}
				if(manfaatrbsmp.size()!=0) {
					
					Map map = new HashMap();
					Map rbsmp = (Map) manfaatrbsmp.get(0);
					String jenrbsmp = rbsmp.get("JENIS").toString();
					
					if("COPPER".equalsIgnoreCase(jenrbsmp)){							
						params.put("mtanpaop", "6,000,000");
						params.put("mdengop", "12,000,000");
						params.put("keguguran", "4,500,000");
						params.put("pkhamilan", "1,300,000");
						
					}else if("BRONZE".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "7,000,000");
						params.put("mdengop", "14,000,000");
						params.put("keguguran", "5,250,000");
						params.put("pkhamilan", "1,500,000");	
					
					}else if("SILVER".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "8,000,000");
						params.put("mdengop", "16,000,000");
						params.put("keguguran", "6,000,000");
						params.put("pkhamilan", "1,700,000");	
					
					}else if("SAPPHIRE".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "8,000,000");
						params.put("mdengop", "16,000,000");
						params.put("keguguran", "6,000,000");
						params.put("pkhamilan", "1,700,000");		
					
					}else if("DIAMOND".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "9,000,000");
						params.put("mdengop", "18,000,000");
						params.put("keguguran", "6,750,000");
						params.put("pkhamilan", "1,900,000");	
					
					}else if("PLATINUM".equalsIgnoreCase(jenrbsmp)){	
						params.put("mtanpaop", "10,000,000");
						params.put("mdengop", "20,000,000");
						params.put("keguguran", "7,500,000");
						params.put("pkhamilan", "2,100,000");	
					
					}
					
					params.put("catatanrb", "\nCatatan Rawat Bersalin (RB) :" +
							 "\n- Premi berlaku untuk wanita yang eligible, yaitu wanita menikah, berusia < 45 tahun dan memiliki anak maksimum 2 orang"+
							 "\n- Berlaku masa tunggu 280 (dua ratus delapan puluh) hari untuk Rawat Bersalin"+
							 "\n- Melahirkan Dengan Operasi ( Sectio Cesaria ) harus berdasarkan Indikasi medis"+
							 "\n* Termasuk perawatan bayi maksimum 2 kali perawatan, berlaku maksimum 30 hari setelah kelahiran");	
					
					params.put("subplanrb", JasperReportsUtils.convertReportData(manfaatrbsmp));
					//StringUtils.addStringToArray(subJnProv, "subplanrb");
				}
				if(manfaatpksmp.size()!=0) {
					
					Map map = new HashMap();
					Map pksmp = (Map) manfaatpksmp.get(0);
					String jenpksmp = pksmp.get("JENIS").toString();
					
					if("COPPER".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "200,000");
						params.put("pkpemeriksaan", "500,000");
						params.put("pklayanan", "200,000");
						params.put("batasmaxpk", "3,500,000");
					
					}else if("BRONZE".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "225,000");
						params.put("pkpemeriksaan", "625,000");
						params.put("pklayanan", "225,000");
						params.put("batasmaxpk", "4,000,000");
					
					}else if("SILVER".equalsIgnoreCase(jenpksmp)){
						params.put("pkkonsultasi", "250,000");
						params.put("pkpemeriksaan", "750,000");
						params.put("pklayanan", "250,000");
						params.put("batasmaxpk", "4,500,000");
					
					}else if("SAPPHIRE".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "275,000");
						params.put("pkpemeriksaan", "875,000");
						params.put("pklayanan", "275,000");
						params.put("batasmaxpk", "5,000,000");
					
					}else if("DIAMOND".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "350,000");
						params.put("pkpemeriksaan", "1,250,000");
						params.put("pklayanan", "350,000");
						params.put("batasmaxpk", "6,500,000");
					
					}else if("PLATINUM".equalsIgnoreCase(jenpksmp)){	
						params.put("pkkonsultasi", "400,000");
						params.put("pkpemeriksaan", "1,500,000");
						params.put("pklayanan", "400,000");
						params.put("batasmaxpk", "7,500,000");
					
					}
					
					params.put("catatanpk", "\nCatatan Penunjang Kesehatan (PK) :" +
							 "\n- Berlaku Masa tunggu untuk manfaat asuransi Penunjang Kesehatan selama 30 (tiga puluh) hari pertama sejak : tanggal berlaku periode asuransi atau	tanggal pemulihan Polis terakhir, hal mana yang terjadi terakhir"+
							 "\n- Penggantian 80%"+
							 "\n- Berlaku Reimbursement"+
							 "\n*  Penggantian Biaya Konsultasi Ahli Nutrisi hanya untuk Tertanggung yang didiagnosa menderita penyakit diabetes oleh Dokter dan harus menjalani Rawat Inap"+
							 "\n** Masa tunggu 1 tahun");	
					
					params.put("subplanpk", JasperReportsUtils.convertReportData(manfaatpksmp));
					//StringUtils.addStringToArray(subJnProv, "subplanpk");
				}
				//END SMILE MEDICAL PLUS 225 RJ RG RB PK
				
				
			}
			
			params.put("subReportDatakeys", new String[] {"subplanrj", "subplanrg", "subplanrb", "subplanpk"});
			params.put("catatan_manf_utama", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini. 2");
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_provestara"));
						
			return params;
		}else
		{
			String ls_insured = (String) info.get("mste_insured");
			String ls_pol_hold = (String) info.get("mspo_policy_holder");
//			 Cara bayar Triwulan, Semesteran, Bulanan, Cicilan dijadikan Tahunan
			if(il_cbid==1 || il_cbid==2 || il_cbid==6 || il_cbid==7) il_cbid=3;
			
			/************************** EKA SISWA **************************/
			if("031,033,070,071,072,172".indexOf(businessId)>-1) {
				if(li_medical==1) urut=1;
				else urut=2;
			/************************** SUPER PROFUND **************************/
			}else if("046".equals(businessId)) {
				if(il_cbid==0) urut=1;
				else if(il_age<17)  urut=2;
				else {
					urut=3;
				}
			/************************** PROTEKSI **************************/	
			}else if("051, 052, 061, 062, 078, 085, 181".indexOf(businessId)>-1) {
				int i = lku_id.equals("01")?0:4;
				if(il_cbid==0) urut=1+i;
				else if(ls_insured.trim().equals(ls_pol_hold.trim())) urut=2+i;
				else if(il_age<17 && il_mspo_age<50) urut=3+i;
				else urut=4+i;
			/************************** SUPER SEHAT, SUPER SEHAT PLUS **************************/
			}else if("053,054,131,132,148".indexOf(businessId)>-1) {
				if(lku_id.equals("01")) urut=1;
				else urut=2;
			/************************** SIMPONI **************************/	
			}else if("056, 058, 064, 083".indexOf(businessId)>-1) {
				if(il_age<17) urut=1;
				else if(il_age<=55) urut=2;
				else urut=3;
			/************************** SEHAT SEJAHTERA **************************/
			}else if("065".equals(businessId)) {
				if(ls_insured.trim().equals(ls_pol_hold.trim()) && il_cbid!=0) urut=1;
				else urut=2;
			/************************** PRO CARE **************************/	
			}else if("066".equals(businessId)) {
				if(il_age>50 && il_cbid==0) urut=4;
				else {
					if(il_age<17) urut=3;
					else if(ls_insured.trim().equals(ls_pol_hold.trim()) && il_cbid!=0) urut=2;
					else urut=1;
				}
			/************************** SUPER INVEST **************************/
			}else if("068,075".indexOf(businessId)>-1) {
				if(il_age<17) urut=1;
				else if(il_age<=50) urut=2;
				else if(il_age<=65) urut=3;
				else urut=4;					
			/**************************  **************************/
//			}else if("093,104,108,110,114,111,125".indexOf(businessId)>-1) {
			}else if("093,104,108,110,125".indexOf(businessId)>-1 || (businessId.equals("111") && lsdbs==1)) {	
				if(il_age<=55) urut=1;
				else urut=2;
			/************************** SIMPONI 8 **************************/
			}else if("112,126".indexOf(businessId)>-1) {
				if(il_age<=50) urut=1;
				else urut=2;
			/************************** LAINNYA **************************/
			}else {
				urut=1;
			}
		}
		Boolean afternov2012 = false;
		Date nov2012 = defaultDateFormat.parse("31/11/2012");
		if (businessId.equals("137") && lsdbs>=6 || businessId.equals("114") && lsdbs>=5 ){
		Date beg_date = basDao.selectBegDateInsuredFromSpaj(spaj);
			if(beg_date.after(nov2012)){
				afternov2012 = true;
				urut=1;
			}
		
			params.put("afternov2012", afternov2012);
		}
		
		List manfaatUtama = this.uwDao.selectDetailManfaat(spaj, new Integer(urut));
		if(manfaatUtama.size()>0){
			if(uwDao.selectCountSwineFlu(spaj)>0){
				Map map = new HashMap();
				Map hasilmanfaatUtama = (Map) manfaatUtama.get(manfaatUtama.size()-1);
				map.put("LKU_ID", hasilmanfaatUtama.get("LKU_ID"));
				map.put("MANFAAT", hasilmanfaatUtama.get("MANFAAT"));
				map.put("MSPR_TSI", hasilmanfaatUtama.get("MSPR_TSI"));
				map.put("LSDBS_NUMBER", hasilmanfaatUtama.get("LSDBS_NUMBER"));
				map.put("LSMAN_LINE", (Integer)hasilmanfaatUtama.get("LSMAN_LINE")+1);
				map.put("LSMAN_MANFAAT", "Ketentuan manfaat Asuransi Tambahan ( rider ) tertera pada Syarat-syarat Khusus masing-masing Asuransi Tambahan");
				map.put("LSMAN_HEADER", hasilmanfaatUtama.get("LSMAN_HEADER"));
				map.put("JUDUL", hasilmanfaatUtama.get("JUDUL"));
				map.put("LSMAN_NOURUT", hasilmanfaatUtama.get("LSMAN_NOURUT"));
				map.put("LSBS_ID", hasilmanfaatUtama.get("LSBS_ID"));
				manfaatUtama.add(map);
			}
		}
		List manfaatTambahan = this.uwDao.selectDetailManfaatTambahan(spaj);
		for(int i=0;i<manfaatTambahan.size();i++){
			Map cekswineflu = (HashMap) manfaatTambahan.get(i);
			String lsbs_id = ((Integer) cekswineflu.get("LSBS_ID")).toString();
			if(lsbs_id.equals("822")) manfaatTambahan.remove(i);
		}
		
		List manfaatNilaiTunai = null;
		List premiTambahan = null;
		
		if(logger.isDebugEnabled())logger.debug("Proses " +spaj + " ~ " + urut + " = " + manfaatUtama.size() + " ~ " + manfaatTambahan.size());

		String ls_manfaat;
		int lsdbs_number = uwDao.selectBusinessNumber(spaj);

		if(logger.isDebugEnabled())logger.debug("Proses Manfaat Polis");
		
		/** PROSES MANFAAT POLIS **/
		double idb_tsi = ((Double) info.get("mspr_tsi")).doubleValue();
		int il_lsbs_num = ((Integer) info.get("lsdbs_number"));

		if(manfaatUtama.size()>1) {
			int ll_no_urut = ((Integer)((HashMap) manfaatUtama.get(0)).get("LSMAN_NOURUT"));
			if("053,054,067,131,132,148".indexOf(businessId)>-1) {
				for(int i=0; i<manfaatUtama.size(); i++) {
					if(logger.isDebugEnabled())logger.debug("1 "+i);
					ls_manfaat = setNilaiManfaat(idb_premium.doubleValue(), il_cbid, ii_umur_beasiswa, spaj, businessId, il_lsbs_num, idb_tsi, lku_id, i, ll_no_urut, il_age);
					Map m = (HashMap) manfaatUtama.get(i);
					m.remove("MANFAAT");m.put("MANFAAT", ls_manfaat);
					manfaatUtama.remove(i); manfaatUtama.add(i, m);
				}
			}else if(!businessId.equals("032")){
				for(int i=0; i<manfaatUtama.size();i++) {
					if(logger.isDebugEnabled())logger.debug("businessId: "+businessId+" ~ 2 "+i);
					ls_manfaat = setNilaiManfaat(idb_premium.doubleValue(), il_cbid, ii_umur_beasiswa, spaj, businessId, il_lsbs_num, idb_tsi, lku_id, i+1, ll_no_urut, il_age);
					if(logger.isDebugEnabled())logger.debug("LS_MANFAAT = " + ls_manfaat);
					Map m = (HashMap) manfaatUtama.get(i);
					m.remove("LSMAN_MANFAAT");m.put("LSMAN_MANFAAT", ls_manfaat);
					manfaatUtama.remove(i); manfaatUtama.add(i, m);
				}
			}
			
		}else if(manfaatUtama.size()==1){
			int ll_no_urut = ((Integer)((HashMap) manfaatUtama.get(0)).get("LSMAN_NOURUT"));
			ls_manfaat = setNilaiManfaat(idb_premium.doubleValue(), il_cbid, ii_umur_beasiswa, spaj, businessId, il_lsbs_num, idb_tsi, lku_id, 0, ll_no_urut, il_age);
			Map m = (HashMap) manfaatUtama.get(0);
			m.remove("MANFAAT");m.put("MANFAAT", ls_manfaat);
			manfaatUtama.remove(0); manfaatUtama.add(0, m);			
		}
		
		//request dr. ingrid - untuk semua produk powersave, apabila usia tertanggung >= 69, maka UP = 0.5 PREMI, dengan nilai max Rp. 100 jt / $10.000 (Yusuf - 11/03/2008)
		//ada tambahan manfaat ke empat untuk request ini
		//{LKU_ID=01, MANFAAT= , MSPR_TSI=5.0E7, LSMAN_NOURUT=1, LSDBS_NUMBER=4, LSMAN_MANFAAT=, LSBS_ID=142, JUDUL= , LSMAN_LINE=3, LSMAN_HEADER=}
		if(products.powerSave(businessId) && bacDao.selectUsiaTertanggung(spaj) >= 69) {
			Map temp = new HashMap();
			temp.put("LSMAN_LINE", 4);
			temp.put("LSMAN_MANFAAT", "Dengan tidak mengurangi maksud pasal 8 ayat 4, maka apabila Tertanggung meninggal dunia akibat kecelakaan pada usia sama dengan atau lebih dari 69 ( enam puluh sembilan ) tahun, maka jumlah manfaat yang dapat dibayarkan adalah 50% ( lima puluh perseratus ) dari premi dengan maksimum  Rp. 100.000.000,- ( seratus juta rupiah ) atau US$ 10.000 ( sepuluh ribu Dollar Amerika )");
			if(products.syariah(businessId, Integer.toString(il_lsbs_num))) //bila syariah, kata premi -> kontribusi/premi
				temp.put("LSMAN_MANFAAT", "Dengan tidak mengurangi maksud pasal 8 ayat 4, maka apabila Tertanggung meninggal dunia akibat kecelakaan pada usia sama dengan atau lebih dari 69 ( enam puluh sembilan ) tahun, maka jumlah manfaat yang dapat dibayarkan adalah 50% ( lima puluh perseratus ) dari kontribusi/premi dengan maksimum  Rp. 100.000.000,- ( seratus juta rupiah ) atau US$ 10.000 ( sepuluh ribu Dollar Amerika )");
			manfaatUtama.add(temp);			
		}		

		logger.info("--------- " + businessId);
		
		//request arin / achmad - 21 Oct 2009 - bila cerdas + ada rider, ada manfaat tambahan ke 3
		if(products.cerdas(businessId)){
			List rider = bacDao.selectDataUsulan_rider(spaj);
			if(!rider.isEmpty()){
				Map temp = new HashMap();
				
				if (businessId.equals("120") && (lsdbs >= 22 && lsdbs <= 24)){ /** untuk produk simpol nomer 3 dihilangkan **/
					
				} else {
					temp.put("LSMAN_LINE", 3);
					temp.put("LSMAN_MANFAAT", "Ketentuan manfaat Asuransi Tambahan ( rider ) tertera pada Syarat-syarat Khusus masing-masing Asuransi Tambahan ( rider ) tersebut.");
					manfaatUtama.add(temp);	
				}
			}
		}
		
		if(logger.isDebugEnabled())logger.debug("Proses Rider");
		/** PROSES RIDER **/
		if(manfaatTambahan.size()>0) {
			int ll_no_urut = ((Integer)((HashMap) manfaatTambahan.get(0)).get("LSMAN_NOURUT"));
			for(int i=0; i<manfaatTambahan.size(); i++) {
				if(logger.isDebugEnabled())logger.debug("3 "+i);
				Map m = (HashMap) manfaatTambahan.get(i);
				String lsbs_id = ((Integer) m.get("LSBS_ID")).toString();
				String lsbs_number = ((Integer) m.get("LSDBS_NUMBER")).toString();
				int ll_line = ((Integer) m.get("LSMAN_LINE"));
				Map ipi = this.uwDao.selectInfoProductInsured(spaj, lsbs_id);
				Double ldc_rider_tsi = (Double) ipi.get("MSPR_TSI");
				ls_manfaat = setNilaiManfaat(idb_premium.doubleValue(), il_cbid, ii_umur_beasiswa, spaj, lsbs_id, Integer.parseInt(lsbs_number), ldc_rider_tsi.doubleValue(), lku_id, ll_line, ll_no_urut, il_age);
				ls_manfaat += "\n";
				m.put("TAMB_MANFAAT", ls_manfaat);
			}
			
			List tmp = new ArrayList();
			Map daftar = new HashMap();
			for(int i=0; i<manfaatTambahan.size(); i++) {
				Map m = (HashMap) manfaatTambahan.get(i);
				String lsbs_id = ((Integer) m.get("LSBS_ID")).toString();
				if(daftar.containsKey(FormatString.rpad("0", lsbs_id, 3))) {
					for(int j=0; j<tmp.size(); j++) {
						Map map = (HashMap) tmp.get(j);
						String lsbs = ((Integer) map.get("LSBS_ID")).toString();
						if(FormatString.rpad("0", lsbs, 3).equals(FormatString.rpad("0", lsbs_id, 3))) {
							map.put("TAMB_MANFAAT", m.get("TAMB_MANFAAT") + "\n" + map.get("TAMB_MANFAAT"));
						}
					}
				}else {
					daftar.put(FormatString.rpad("0", lsbs_id, 3), i);
					tmp.add(m);
				}
			}
			
			manfaatTambahan = tmp;
			
//			khusus 2000 polis palembang 28/01/04 (hm)
			if(businessId.equals("089")) {
				Map temp = new HashMap();
				temp.put("LSMAN_HEADER", "Klausula: Tambahan mengenai Asuransi Tambahan Bebas Premi");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "0");
				temp.put("TAMB_MANFAAT", "Dalam hal Pemegang Polis berbeda dengan Tertanggung, maka khusus untuk Polis ini Ketentuan dalam Pasal 1 ayat 1 " +
						"Syarat-syarat Khusus Asuransi Tambahan Bebas premi yang menyatakan bahwa Tertanggung juga sebagai Pemegang polis menjadi tidak berlaku. ");
				manfaatTambahan.add(temp);
			}
		}
		
		//Mediplan - 148
		if(businessId.equals("148")){
			Map temp = new HashMap();
			temp.put("LSMAN_HEADER", "Pembagian Bonus :");
			temp.put("LSBS_ID", "998");
			temp.put("LSDBS_NUMBER", "0");
			temp.put("LSMAN_LINE", "1");
			temp.put("SUBHEADER", "Jika Peserta selama satu tahun pertanggungan tidak mengajukan klaim maka akan mendapatkan bonus dengan ketentuan:");
			temp.put("TAMB_MANFAAT", "Jika Peserta Memperpanjang kontrak Asuransi = 25% dari premi dasar tahun berjalan");
			manfaatTambahan.add(temp);
			
			temp = new HashMap();
			temp.put("LSMAN_HEADER", "Pembagian Bonus :");
			temp.put("LSBS_ID", "998");
			temp.put("LSDBS_NUMBER", "0");
			temp.put("LSMAN_LINE", "2");
			temp.put("SUBHEADER", "Jika Peserta selama satu tahun pertanggungan tidak mengajukan klaim maka akan mendapatkan bonus dengan ketentuan:");
			temp.put("TAMB_MANFAAT", "Jika Peserta Tidak Memperpanjang kontrak Asuransi = 15% dari premi dasar tahun berjalan");
			manfaatTambahan.add(temp);
			
			temp = new HashMap();
			temp.put("LSMAN_HEADER", "Keterangan :");
			temp.put("LSBS_ID", "999");
			temp.put("LSDBS_NUMBER", "0");
			temp.put("LSMAN_LINE", "1");
			temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
			temp.put("TAMB_MANFAAT", "Manfaat Pertanggungan mengikuti ketentuan pada Pasal 3 (tiga) Syarat-Syarat Khusus Polis Asuransi Santunan Harian Rawat Inap dan Pembedahan Mediplan");
			manfaatTambahan.add(temp);

			if(lsdbs>=1 && lsdbs<=5) {
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "2");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Manfaat Asuransi Tambahan \"Santunan Penyakit Kritis\" mengikuti ketentuan pada Pasal 2 (dua) Syarat-Syarat Khusus Asuransi Santunan Harian Rawat Inap dan Pembedahan Mediplan Asuransi Tambahan Penyakit Kritis");
				manfaatTambahan.add(temp);
				
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "3");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Bonus mengikuti ketentuan Pasal 4 (empat) Syarat-Syarat Khusus Polis Asuransi Santunan Harian Rawat Inap dan Pembedahan Mediplan");
				manfaatTambahan.add(temp);
				
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "4");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Produk Asuransi Kesehatan Mediplan tidak memiliki Nilai Tunai");
				manfaatTambahan.add(temp);
			}else {
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "2");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Bonus mengikuti ketentuan Pasal 4 (empat) Syarat-Syarat Khusus Polis Asuransi Santunan Harian Rawat Inap dan Pembedahan Mediplan");
				manfaatTambahan.add(temp);
				
				temp = new HashMap();
				temp.put("LSMAN_HEADER", "Keterangan :");
				temp.put("LSBS_ID", "999");
				temp.put("LSDBS_NUMBER", "0");
				temp.put("LSMAN_LINE", "3");
				temp.put("SUBHEADER", "Manfaat di atas hanya merupakan ulasan ringkas, sedangkan ketentuan lebih lanjut mengenai Manfaat Pertanggungan mengikuti ketentuan pada Syarat-Syarat Umum dan Syarat-Syarat Khusus dimana bagi :");
				temp.put("TAMB_MANFAAT", "Produk Asuransi Kesehatan Mediplan tidak memiliki Nilai Tunai");
				manfaatTambahan.add(temp);
			}
		}
		
		if(logger.isDebugEnabled())logger.debug("Proses Nilai Tunai dan Tahapan");
		/** Proses Nilai Tunai & Tahapan **/
		if(businessId.equals("226") && (lsdbs >= 1 && lsdbs <= 5)){ //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
			info = uwDao.selectInfoCetakManfaat(spaj);
			if(info != null){
				Integer subcode = (Integer)info.get("lsdbs_number");
				params.put("reg_spaj", spaj);
				params.put("lsbs_id", (String)info.get("lsbs_id"));
				params.put("lsdbs_number", subcode);
				params.put("lku_id", (String)info.get("lku_id"));
				params.put("lscb_id", (subcode >= 1 && subcode <= 4 ? 3 : 0));
				params.put("mspo_pay_period", (Integer)info.get("mspo_pay_period"));
				params.put("mspo_ins_period", new Integer(0));
				params.put("umur", (Integer)info.get("mste_age"));
				params.put("lus_id", lus_id);
				params.put("tahun_terakhir", (Integer)info.get("mspo_ins_period"));
				params.put("usia_terakhir", new Integer(100));
				params.put("nt_usia_100", "-");
				params.put("dana_terakhir", BigDecimal.valueOf((Double)info.get("mspr_tsi")));

				manfaatNilaiTunai = uwDao.selectLstTableNewForSimasLegacyPlan(params);
			}
		} else {
			manfaatNilaiTunai = this.nilaiTunai.proses(spaj, lus_id, "cetak", 0, 0);
		}
		
		if(logger.isDebugEnabled())logger.debug("Proses Klausula");
		/** Proses Klausula **/
		String kurs = lku_id.equals("01")?"Rp. ":lku_id.equals("02")?"US\\$ ":"";
		String klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal #PASAL#. Syarat - syarat Umum Polis";

		//YUSUF (5/10/2006) -> KHUSUS ENDOWMENT MUTIARA
		if(businessId.equals("157")) {
			if(lsdbs_number == 2 || lsdbs_number == 3) {
				klausula = "KLAUSULA : Daftar Nilai Tunai #KURS#";
			}
		}
		
		if("056,058,068,075,083".indexOf(businessId)>-1) klausula = klausula.replaceAll("#PASAL#", String.valueOf(5));
		else if("069,082".indexOf(businessId)>-1) {
			klausula = klausula.replaceAll("#PASAL#", String.valueOf(6));
			klausula += " dan Pasal 2 Syarat-Syarat Khusus Polis";
		}else if("167,168,169,210".indexOf(businessId)>-1) { //HIDUP BAHAGIA & EKAWAKTU
			klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 9 Syarat - syarat Umum Polis";
		}else if("040,146,163,180".indexOf(businessId)>-1 || products.SuperSejahtera(businessId) || //DANA SEJAHTERA - REQUEST MORINOF
				businessId.equalsIgnoreCase("226")) { //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
			if(products.SuperSejahtera(businessId) && lsdbs_number>=6){
				klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 9. Syarat-syarat Umum Polis dan Pasal 2. Syarat-syarat Khusus Polis";
			}else{
				klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 9. Syarat-syarat Umum Polis dan Pasal 3. Syarat-syarat Khusus Polis";
			}
		}else if("093,095,098,104,108,110,112,125,126,133,114,137,150,151".indexOf(businessId)>-1) {
			klausula = klausula.replaceAll("#PASAL#", String.valueOf(7));
		}else if("149".indexOf(businessId)>-1) {
			klausula = klausula.replaceAll("#PASAL#", String.valueOf(9));
		}else if(products.ekaSiswa(businessId) || "181".indexOf(businessId)>-1) {
			klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 9. Syarat-syarat Umum Polis";
		}else if("173".indexOf(businessId)>-1) {
			klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 3. Syarat-syarat Khusus Polis";
		}else if("178".indexOf(businessId)>-1){
			klausula = "KLAUSULA : Daftar Nilai Tunai #KURS# berdasarkan Pasal 12. Syarat-syarat Umum Polis";
		}else if("208".indexOf(businessId)>-1){
			klausula = "KLAUSULA : Daftar Tahapan Dana Belajar";
		}else klausula = klausula.replaceAll("#PASAL#", String.valueOf(6));
		klausula += " :";
		klausula = klausula.replaceAll("#KURS#", kurs);
		
		if(logger.isDebugEnabled())logger.debug("Proses Maturity");
		/** Proses Maturity **/
		Double matur = null;
		Double maturBonus = null;
		int lsbs = Integer.parseInt(businessId);
		Integer thMatur = (Integer) info.get("mspo_ins_period");
		if(lsbs==27) {
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, payPeriod, 4);
			if(insPeriod==99) thMatur = new Integer(100-il_age); 
		}else if(products.ekaSiswa(businessId)) {
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_mspo_age, thMatur, 7);
		}else if(lsbs==56) {
			thMatur = new Integer(4);
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, thMatur, 7); 
		}else if("060,089,147".indexOf(businessId)>-1) {
			thMatur = new Integer(79);
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, thMatur, 7);
		}else if("051,052,061,062,078".indexOf(businessId)>-1) {
			if(il_age==49 && lku_id.equals("01")) matur= new Double(1500);
		}else if("167".indexOf(businessId)>-1) { //Yusuf (26/02/2008) - Penarikan nilai maturity dipatok 70
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, 70, il_age, thMatur, 7);
		}else {
			if(insPeriod==99) thMatur = new Integer(100-il_age);
			matur = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, thMatur, 7);
			maturBonus = this.nilaiTunai.getNilaiMaturity(new Integer(lsbs), lku_id, new Integer(il_cbid), payPeriod, new Integer(insPeriod), il_age, insPeriod, 4);
		}
		
		if(logger.isDebugEnabled())logger.debug("Proses Bonus Akhir Kontrak");
		/** Proses Bonus Akhir Kontrak **/
		String ls_nikon = null;
		String ls_nikon2 = null;
		DecimalFormat df = new DecimalFormat("#,##0;(#,##0)");
		if(matur!=null) {
			if(matur.doubleValue()!=0) {
				if(lsbs==27) {
					if(il_lsbs_num==1) {
						ls_nikon = "Bonus akhir tahun ke-10 : ";
					}else if(il_lsbs_num==2) {
						ls_nikon = "Bonus akhir tahun ke-15 : ";
					}else {
						ls_nikon = "Bonus akhir tahun ke-20 : ";
					}
				}else if(lsbs == 167) {
					ls_nikon = "Uang Pertanggungan Akhir Kontrak (apabila Tertanggung Hidup) : ";
				}else if(products.SuperSejahtera(Integer.toString(lsbs)) || lsbs==145) {
					ls_nikon = "Tahapan usia ke-69 : " + df.format(0.5 * idb_tsi);
				}else {
					ls_nikon = "Nilai akhir kontrak : ";
				}
				if(lsbs==83) {
					ls_nikon = "Bonus akhir kontrak : " + df.format(idb_premium.doubleValue() * 1.4);
				}else if(products.SuperSejahtera(Integer.toString(lsbs)) || lsbs==145){
					ls_nikon = ls_nikon;
				}else{
					ls_nikon += df.format(Math.round(idb_tsi * matur.doubleValue()/1000));
				}
			}
		}

		if(maturBonus!=null) {
			if(maturBonus.doubleValue()!=0) {
				ls_nikon2 = "Bonus akhir kontrak : " + df.format(idb_tsi * maturBonus.doubleValue()/1000);
			}
		}
		
		if(products.SuperSejahtera(Integer.toString(lsbs)) || lsbs==145) {
//			ls_nikon2 = "Tahapan usia ke-69 : " + df.format(0.5 * idb_tsi);
			ls_nikon2 = "Nilai akhir kontrak : ";
			ls_nikon2 += df.format(Math.round(idb_tsi * matur.doubleValue()/1000));
			
		}else if(lsbs==65) {
			ls_nikon2 = " Tahapan usia ke-59 : " + df.format(idb_tsi);
		}else if(lsbs==56) {
			ls_nikon = "Manfaat akhir tahun ke-4 : " + df.format(1.25 * idb_premium.doubleValue());
		}else if(lsbs==64) {
			ls_nikon = "Manfaat akhir tahun ke-4 : " + df.format(1.24 * idb_premium.doubleValue());
		}else if(lsbs==68) {
			ls_nikon = "Minimum Manfaat hidup diakhir tahun ke-4 : " + df.format(1.06 * idb_premium.doubleValue());
		}else if(lsbs==75) {
			ls_nikon = "Minimum Manfaat hidup diakhir tahun ke-4 : " + df.format(1.02 * idb_premium.doubleValue());
		}else if("095,098".indexOf(businessId)>-1) {
			ls_nikon = "Manfaat akhir tahun ke-8 : " + df.format(1.818 * idb_premium.doubleValue());
		}
	
		String catUtama = null;
		if(products.syariah(businessId, Integer.toString(il_lsbs_num))) {
			catUtama = "MANFAAT ASURANSI Syariah diberikan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-"+
			"SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan "+
			"pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan "+
			"dengan POLIS INI.";
		}else if(businessId.equals("167")) {
			catUtama = "MANFAAT ASURANSI diberikan sesuai dengan SYARAT-SYARAT UMUM POLIS "+
			"dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan "+
			"pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan "+
			"dengan POLIS INI.";			
		}else if(businessId.equals("168")) {
			catUtama = "MANFAAT ASURANSI diberikan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan "+
			"KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat "+
			"dipisahkan dengan POLIS INI.";			
		}else if(businessId.equals("169")){
			catUtama = "MANFAAT ASURANSI diberikan berdasarkan persyaratan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan "+
			"KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat "+
			"dipisahkan dengan POLIS ini.";
		}else {
			catUtama = "MANFAAT ASURANSI diberikan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-"+
			"SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan "+
			"pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan "+
			"dengan POLIS INI.";
		}
		
		if(businessId.equals("134") && lsdbs == 13){ //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
			Map param = new HashMap();
			param.put("catatan", catUtama);
			param.put("LSMAN_HEADER", "MANFAAT ASURANSI \"SMART PLATINUM LINK\"");
			manfaatUtama.add(param);
		}
		
		//tambahan klausula di manfaat, bila cetak ulang polis
		if(tamb == 5) {
			catUtama +=
				"\n<style isBold=\"true\" isItalic=\"true\" isUnderline=\"true\">Catatan Mengenai Penggantian Polis:</style>" +
				"\nPolis ini adalah pengganti polis No. 09.155.2008.02856" +
				" yang diterbitkan tanggal 27 JUNI 2008"+
				" yang hilang. Dengan diterbitkannya polis ini maka polis yang diterbitkan tanggal 27 JUNI 2008"+
				" dinyatakan tidak berlaku lagi.";
		}
		
		String catManfAll = null;
		if(il_cbid==0 && li_medical==0 && "047,049,050,055,056,058,068,075,083,093,095,098,104,108,110,112,125,126,133,114,137,150,151,173,180,181".indexOf(businessId)==-1) {
			catManfAll = "Catatan : Syarat khusus Asuransi tanpa pemeriksaan dokter tidak berlaku bagi pembayaran premi sekaligus.";
		}else if(businessId.equals("180")){
			String kurs2 = lku_id.equals("01")?"Rp. ":lku_id.equals("02")?"US$ ":"";
			if(lsdbs_number==1){
				catManfAll = "Manfaat dan Bonus Habis Kontrak :"+kurs2+ df.format(1.2 * idb_tsi); 
			}else if(lsdbs_number==2){
				catManfAll = "Manfaat dan Bonus Habis Kontrak :"+kurs2+ df.format(1.3 * idb_tsi); 
			}else if(lsdbs_number==3){
				catManfAll = "Manfaat dan Bonus Habis Kontrak :"+kurs2+ df.format(1.4 * idb_tsi); 
			}
			
		}
		if(businessId.equals("210")){
			
			catManfAll = "Catatan : \n"+
					     "1.   Termasuk manfaat pengembalian 50% dan 100% dari total premi yang telah dibayarkan (tanpa bunga)\n"+ 
					     "2.   Apabila Usia Polis x tahun y bulan, maka Nilai Tunainya adalah pada saat Usia Polis x tahun" ;
			
		}
		
		String bonustahapan = null;
		if("112,126".indexOf(businessId)>-1) {
			bonustahapan = "*) Bonus Tahapan adalah bonus yang diberikan jika Tertanggung hidup sampai dengan akhir kontrak Asuransi\n"+
										"   (akhir tahun polis kedelapan) bila ada, dimana bonus ini ditentukan diawal pertanggungan / diawal kontrak\n"+
										"   asuransi yang besarnya ditentukan oleh Penanggung";
			ls_nikon = null;
			ls_nikon2 = null;
			catManfAll = null;
		}else if("150, 151".indexOf(businessId)>-1) {
			bonustahapan = "*)  Besarnya Nilai Tunai pada akhir tahun keempat tidak dijamin, tergantung bunga pasar pada waktu itu.";
			ls_nikon = null;
			ls_nikon2 = null;
			catManfAll = null;
		}else if("157".indexOf(businessId)>-1) { //endowment 20
			if(lsdbs_number == 2 || lsdbs_number == 3) {
				bonustahapan = "Catatan : \n"+
				"*) - Ketentuan mengenai besarnya Nilai Tunai yang dibayarkan oleh Penanggung diatur dalam Syarat-Syarat Umum Polis Pasal 6 Ayat 1. \n"+
				"   - Dengan telah dibayarkannya Nilai Tunai tersebut, maka sejak saat itu Asuransi atas diri Tertanggung berakhir.";
			}else {
				bonustahapan = "Catatan : \n"+
											"*) - Ketentuan mengenai besarnya Nilai Tunai yang dibayarkan oleh Penanggung diatur dalam Syarat-Syarat Umum Polis Pasal 7 Ayat 1. \n"+
											"   - Dengan telah dibayarkannya Nilai Tunai tersebut, maka sejak saat itu Asuransi atas diri Tertanggung berakhir.";
			}
			ls_nikon = null;
			ls_nikon2 = null;
			catManfAll = null;
		}else if("167".indexOf(businessId)>-1) {
			catManfAll = 
				"*)   Nilai Tunai (sudah termasuk Tahapan Pasti)";
	//			"*)   Nilai Tunai (sudah termasuk Tahapan Pasti)\n"+
	//			"**) Nilai Tunai pada usia 70 tahun sudah termasuk Dana Hari Tua (apabila Tertanggung masih hidup)";
//			ls_nikon = null;
//			ls_nikon2 = null;
//			catManfAll = null;
		}else if("168".indexOf(businessId)>-1) {
			catManfAll = 
				//"*)   Nilai Tunai di atas sudah termasuk Dana Kehidupan ( apabila diajukan ) pada saat Tertanggung mencapai usia 55 (lima puluh lima) tahun, Tertanggung masih hidup dan Polis masih berlaku.";
				"";
		}else if("178".indexOf(businessId)>-1) {
			catManfAll = "*)   Termasuk manfaat pengembalian 100% dari total premi yang telah dibayarkan (tanpa bunga)";
		}else if("208".indexOf(businessId)>-1) {
			catManfAll = "*)   Termasuk manfaat pengembalian 100% dari total premi yang telah dibayarkan (tanpa bunga)";
		}
		
		/** Proses Perhitungan Tarif Premi Tambahan (Rider) Khusus Platinum Link (134) dan Amanah Link (166) - Yusuf - 03/01/2008 **/
		if((businessId.equals("134") && lsdbs<5) || businessId.equals("166")) {
			premiTambahan = prosesAsuransiTambahanPlatinumLink(info);
		}
		
		//helpdesk [133346] produk baru 142-13 Smart Investment Protection
		if(businessId.equals("142") && lsdbs == 13){
			Map temp = new HashMap();		
			temp.put("LSMAN_HEADER", "Informasi Tambahan :");
			temp.put("LSBS_ID", "999");
			temp.put("LSDBS_NUMBER", "0");
			temp.put("LSMAN_LINE", "0");
			if(lku_id.equalsIgnoreCase("01")){
				temp.put("TAMB_MANFAAT", "Untuk produk endowment Smart Investment Protection menggunakan underlying asset dengan grade minimum AA sebagai berikut :\n" + 
										" 1. Obligasi Berkelanjutan IV Astra Sedaya Finance Tahap II, Tahun 2019, Seri B\n" +
										" 2. Obligasi Berkelanjutan IV Astra Sedaya Finance Tahap II, Tahun 2019, Seri C\n" +
										" 3. Obligasi Berkelanjutan IV Adira Finance Tahap III, Tahun 2018, Seri E\n" +
										" 4. Obligasi Berkelanjutan IV Adira Finance Tahap V, Tahun 2019, Seri C\n" +
										" 5. Obligasi Berkelanjutan III Federal Intl Finance Tahap V, Tahun 2019, Seri B\n" +
										" 6. Obligasi Berkelanjutan II Bank Panin Tahap III, Tahun 2018\n" +
										" 7. Obligasi I Pelindo IV, Tahun 2018, Seri A\n" +
										" 8. Obligasi Berkelanjutan II Bank BRI Tahap IV, Tahun 2018, Seri B\n" +
										" 9. Obligasi Berkelanjutan II Bank BRI Tahap II, Tahun 2017, Seri D\n" +
										" 10. Obligasi Berkelanjutan II Bank BRI Tahap I, Tahun 2016, Seri E\n"+
										"\n"+
									
										"Catatan :\n"+
										" 1. Strategi Pemilihan Underlying bisa berubah mengikuti Strategi Asset Liabilites Management (ALM) dari\n"+
										"     PT Asuransi Jiwa Sinarmas MSIG Tbk. melalui Keputusan Komite Investasi (IFC Committee).\n"+
										" 2. PT Asuransi Jiwa Sinarmas MSIG Tbk. mengelola dana nasabah kedalam kelompok Portofolio yang telah \n"+
										"     ditetapkan.");
			} else if(lku_id.equalsIgnoreCase("02")){ //helpdesk [146169] Perbaikan Tampilan Manfaat USD SIP
				temp.put("TAMB_MANFAAT", "Untuk produk endowment Smart Investment Protection (USD) menggunakan underlying asset dengan grade minimum BBB sebagai berikut :\n" +
										" 1. Obligasi USD Pertamina 2023, ISIN CODE USY7138AAE02\n" +
										" 2. Obligasi USD BBRI 23, ISIN CODE XS1852235586\n" +
										"\n" +
										"Catatan :\n" +
										" 1. Strategi Pemilihan Underlying bisa berubah mengikuti Strategi Asset Liabilites Management (ALM) dari\n" +
										"     PT Asuransi Jiwa Sinarmas MSIG Tbk. melalui Keputusan Komite Investasi (IFC Committee).\n" +
										" 2. PT Asuransi Jiwa Sinarmas MSIG Tbk. mengelola dana nasabah kedalam kelompok Portofolio yang telah \n" +
										"     ditetapkan.");
			} else
				temp.put("TAMB_MANFAAT", "##LKU_ID##NOT##VALID##");
			manfaatTambahan.add(temp);			

			Double up = (Double)info.get("mspr_tsi");
			String lsbs_id = (String)info.get("lsbs_id");
			params.put("lsbs_id", lsbs_id);
			params.put("lsdbs_number", lsdbs);
			params.put("lku_id", lku_id);
			Double rate = (lku_id.equalsIgnoreCase("01") ? 0.07 : 0.013);
			List sipNT = uwDao.selectSIPNT(hitungNilaiTunaiSIP(up, rate, 1), hitungNilaiTunaiSIP(up, rate, 2), hitungNilaiTunaiSIP(up, rate, 3));
			params.put("sdsNT", JasperReportsUtils.convertReportData(sipNT));
		}

		/** MULAI PROSES JASPER REPORTS **/
		params.put("spaj", spaj);
		params.put("props", props);
		
		params.put("akhirkont", ls_nikon);
		params.put("bonuskont", ls_nikon2);
		params.put("catatan_manf_utama", catUtama);
		params.put("catatan_manf_all", catManfAll);
		params.put("bonustahapan", bonustahapan);
		logger.info(params.get("catatan_manf_utama"));

		//Convert DataSource untuk subreport dari Collection ke JRDataSource
		params.put("utamaDS", JasperReportsUtils.convertReportData(manfaatUtama));
		params.put("tambahanDS", JasperReportsUtils.convertReportData(manfaatTambahan));
		if(manfaatNilaiTunai!=null) {
			params.put("klausa", klausula);
			params.put("allDS", JasperReportsUtils.convertReportData(manfaatNilaiTunai));
		}else if(premiTambahan != null) {
			params.put("allSR", props.getProperty("subreport.manfaat.manf_tambahan_platinum")+".jasper");
			if(businessId.equals("166")) params.put("allSR", props.getProperty("subreport.manfaat.manf_tambahan_amanah")+".jasper");
			params.put("allDS", JasperReportsUtils.convertReportData(premiTambahan));
		}
		
		if(businessId.equals("073")) {
			params.put("allSR", props.getProperty("subreport.manfaat.manf_tambahan_verdana")+".jasper");
		}
		
		//endowment 20, nilai tunainya beda dikit (Yusuf)
		if(businessId.equals("157")) {
			params.put("allSR", props.getProperty("subreport.manfaat.manf_all_157") + ".jasper");
		//hidup bahagia, manfaatnya ada tahapan pasti diatas (Yusuf)
		}else if(businessId.equals("167")) {
			params.put("allSR", props.getProperty("subreport.manfaat.manf_all_167") + ".jasper");
		//end-care, produk gak laku, buat nya setengah mati (Yusuf)
		// *}else if(businessId.equals("168") || businessId.equals("169")){
		}else if(businessId.equals("168") || businessId.equals("169") || businessId.equals("210")){
			params.put("allSR", props.getProperty("subreport.manfaat.manf_all_168") + ".jasper");
		//investa, fontnya dikecilin dikit untuk manfaatnya (Yusuf)
		}else if(businessId.equals("111") && lsdbs_number==1) {
			params.put("utamaSR", props.getProperty("subreport.manfaat.manf_utama_investa")+".jasper");
		//maxi invest, ada persentasenya di bagian bawah
		}else if((businessId.equals("114") && (lsdbs_number == 1 || lsdbs_number == 3 || lsdbs_number == 4)) ||
				 (businessId.equals("137") && (lsdbs_number == 1 || lsdbs_number == 2 || lsdbs_number == 3))){
			params.put("allSR", props.getProperty("subreport.manfaat.manf_all_maxi_invest") + ".jasper");
		}
				
		if (businessId.equals("142") && lsdbs == 13){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
			params.put("subReportDatakeys", new String[] {"utamaDS", "tambahanDS", "allDS", "sdsNT"});
		} else if (businessId.equals("226") && (lsdbs >= 1 && lsdbs <= 5)){ //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
			params.put("ntDS", JasperReportsUtils.convertReportData(manfaatNilaiTunai));
			params.put("subReportDatakeys", new String[] {"utamaDS", "tambahanDS", "ntDS"});
		} else {
			params.put("subReportDatakeys", new String[] {"utamaDS", "tambahanDS", "allDS"});
		}

		if(!special.equals("")) {
			if(special.equals("true")) {
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.specialcase").toString());
			}else if(special.equals("54")) {
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case54"));
			}
		}else {
			if(businessId.equals("181")){
				params.put("usiaPP", il_mspo_age);
				params.put("usiaTT", il_age);
				params.put("lscbId", il_cbid);
				params.put("lsreId", lsre_id);
				if(il_age<=48){//ganti ketentuan manfaat, batas usia jadi 48 dari sebelumnya 47.
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.proteksi46").toString());
				}else{
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.proteksi47").toString());
				}
			}else if(businessId.equals("191")){
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.rencana_cerdas").toString());
				//params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat").toString());
			}if(businessId.equals("142") && lsdbs == 13){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat_sip").toString());
			}else if(businessId.equals("226") && (lsdbs >= 1 && lsdbs <= 5)){ //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat_simaslegacy").toString());
			}else if(businessId.equals("134") && lsdbs == 13){ //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat_rpul_bel").toString());
			}else{
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat").toString());
			}
		}
				
		
		return params;
	}
	
	private Double hitungNilaiTunaiSIP(Double up, Double rate, Integer year){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
		
		Double bunga = (up * rate * 0.5);
		Double biayaBatal = 0.06 * (up+bunga);
		Double nt = (double) 0;
		
		if(year>=3){
			nt =  up + bunga;
		}else{
			nt =  up + bunga - biayaBatal;
		}
		//Double nt = (((100 / 100) + (year * rate)) * up);
		
		return nt;
	}
	
	private Map prosesManfaat7476(String lus_id, String spaj, int il_age, String businessId, int lsdbs, int cbid, Double idb_premium, Integer ii_umur_beasiswa, String lku_id) throws Exception{
		Map params = new HashMap();
		params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case74"));

		businessId = FormatString.rpad("0", businessId, 3);
		
		Map s = uwDao.selectDataUsulan(spaj);
		int flag_new = (Integer) s.get("MSPO_FLAG_NEW");
		
		List manfaat = new ArrayList(), manfaat2 = new ArrayList(), manfaat3 = new ArrayList();
		Map m;
		String kontribusi = " ";
		String syariah = " ";
		String syariah2 = " ";
		String peserta = "Tertanggung";
		String pertama = " ";
		Boolean beforeMei2010 = false;
		Boolean beforeJan2013= false;
		Boolean beforeMaret2013= false;
		Boolean beforeJuli2013= false;
		String multi_invest = " ";
		String uang_pertanggungan = "Uang Pertanggungan";
		Date Mei2010 = defaultDateFormat.parse("01/01/2010");
		Date Jan2013= defaultDateFormat.parse("01/01/2013");
		Date Juli2013= defaultDateFormat.parse("01/07/2013");
		Date Maret2013= defaultDateFormat.parse("01/03/2013");
		Date beg_date = basDao.selectBegDateInsuredFromSpaj(spaj);
		if(beg_date.before(Mei2010)){
			beforeMei2010 = true;
		}
        if(beg_date.before(Jan2013)){
			beforeJan2013 = true;
		}
        if(beg_date.before(Maret2013)){
			beforeMaret2013 = true;
		}
        if(beg_date.before(Juli2013)){
			beforeJuli2013 = true;
		}
		params.put("SebelumMei2010", beforeMei2010);
		params.put("SebelumJan2013", beforeJan2013);
		params.put("SebelumMaret2013", beforeMaret2013);
		params.put("SebelumJuli2013", beforeJuli2013);
		
		if(cbid == 3){
			params.put("1", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 1, 5).toString()+"%");
			params.put("2", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 1, 6).toString()+"%");
			params.put("3", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 1, 7).toString()+"%");
			params.put("4", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 1, 8).toString()+"%");
			params.put("5", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 2, 5).toString()+"%");
			params.put("6", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 2, 6).toString()+"%");
			params.put("7", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 2, 7).toString()+"%");
			params.put("8", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 2, 8).toString()+"%");
			params.put("9", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 3, 5).toString()+"%");
			params.put("10", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 3, 6).toString()+"%");
			params.put("11", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 3, 7).toString()+"%");
			params.put("12", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 3, 8).toString()+"%");
			params.put("13", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 5).toString()+"%");
			params.put("14", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 6).toString()+"%");
			params.put("15", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 7).toString()+"%");
			params.put("16", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 8).toString()+"%");
			params.put("17", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 5).toString()+"%");
			params.put("18", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 6).toString()+"%");
			params.put("19", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 7).toString()+"%");
			params.put("20", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 8).toString()+"%");
		}else if(cbid == 2){
			params.put("1", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 2, 5).toString()+"%");
			params.put("2", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 2, 6).toString()+"%");
			params.put("3", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 2, 7).toString()+"%");
			params.put("4", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 2, 8).toString()+"%");
			params.put("5", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 3, 5).toString()+"%");
			params.put("6", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 3, 6).toString()+"%");
			params.put("7", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 3, 7).toString()+"%");
			params.put("8", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 3, 8).toString()+"%");
			params.put("9", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid,  4, 5).toString()+"%");
			params.put("10", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 6).toString()+"%");
			params.put("11", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 7).toString()+"%");
			params.put("12", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 8).toString()+"%");
			params.put("13", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 5).toString()+"%");
			params.put("14", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 6).toString()+"%");
			params.put("15", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 7).toString()+"%");
			params.put("16", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 8).toString()+"%");
			params.put("17", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 6, 5).toString()+"%");
			params.put("18", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 6, 6).toString()+"%");
			params.put("19", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 6, 7).toString()+"%");
			params.put("20", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid,6, 8).toString()+"%");
			params.put("21", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 7, 5).toString()+"%");
			params.put("22", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 7, 6).toString()+"%");
			
			params.put("23", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 7, 7).toString()+"%");
			params.put("24", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 7, 8).toString()+"%");
			params.put("25", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 8, 5).toString()+"%");
			params.put("26", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 8, 6).toString()+"%");
			params.put("27", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 8, 7).toString()+"%");
			params.put("28", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 8, 8).toString()+"%");
			params.put("29", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 9, 5).toString()+"%");
			params.put("30", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 9, 6).toString()+"%");
			params.put("31", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 9, 7).toString()+"%");
			params.put("32", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 9, 8).toString()+"%");
			params.put("33", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 10, 5).toString()+"%");
			params.put("34", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 10, 6).toString()+"%");
			params.put("35", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 10, 7).toString()+"%");
			params.put("36", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 10, 8).toString()+"%");
		}else if(cbid == 1){
			params.put("1", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid,  4, 5).toString()+"%");
			params.put("2", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 6).toString()+"%");
			params.put("3", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 7).toString()+"%");
			params.put("4", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 4, 8).toString()+"%");
			params.put("5", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5,5).toString()+"%");
			params.put("6", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 6).toString()+"%");
			params.put("7", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 7).toString()+"%");
			params.put("8", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 5, 8).toString()+"%");
			params.put("9", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 6, 5).toString()+"%");
			params.put("10", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 6, 6).toString()+"%");
			params.put("11", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 6, 7).toString()+"%");
			params.put("12", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid,6, 8).toString()+"%");
			params.put("13", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 7, 5).toString()+"%");
			params.put("14", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 7, 6).toString()+"%");
			
			params.put("15", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 7, 7).toString()+"%");
			params.put("16", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 7, 8).toString()+"%");
			params.put("17", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 8, 5).toString()+"%");
			params.put("18", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 8, 6).toString()+"%");
			params.put("19", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 8, 7).toString()+"%");
			params.put("20", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 8, 8).toString()+"%");
			params.put("21", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 9, 5).toString()+"%");
			params.put("22", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 9, 6).toString()+"%");
			params.put("23", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 9, 7).toString()+"%");
			params.put("24", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 9, 8).toString()+"%");
			params.put("25", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 10, 5).toString()+"%");
			params.put("26", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 10, 6).toString()+"%");
			params.put("27", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 10, 7).toString()+"%");
			params.put("28", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 10, 8).toString()+"%");
			
			params.put("29", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 11, 5).toString()+"%");
			params.put("30", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 11, 6).toString()+"%");
			params.put("31", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 11, 7).toString()+"%");
			params.put("32", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 11, 8).toString()+"%");
			params.put("33", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 12, 5).toString()+"%");
			params.put("34", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 12, 6).toString()+"%");
			params.put("35", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 12, 7).toString()+"%");
			params.put("36", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 12, 8).toString()+"%");
			params.put("37", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 13, 5).toString()+"%");
			params.put("38", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 13, 6).toString()+"%");
			params.put("39", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 13, 7).toString()+"%");
			params.put("40", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 13, 8).toString()+"%");
			params.put("41", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 14, 5).toString()+"%");
			params.put("42", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 14, 6).toString()+"%");
			params.put("43", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 14, 7).toString()+"%");
			params.put("44", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 14, 8).toString()+"%");
			params.put("45", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 15, 5).toString()+"%");
			params.put("46", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 15, 6).toString()+"%");
			params.put("47", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 15, 7).toString()+"%");
			params.put("48", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 15, 8).toString()+"%");
			params.put("49", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 16, 5).toString()+"%");
			params.put("50", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 16, 6).toString()+"%");
			params.put("51", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 16, 7).toString()+"%");
			params.put("52", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 16, 8).toString()+"%");
			params.put("53", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 17, 5).toString()+"%");
			params.put("54", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 17, 6).toString()+"%");
			params.put("55", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 17, 7).toString()+"%");
			params.put("56", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 17, 8).toString()+"%");
			params.put("57", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 18, 5).toString()+"%");
			params.put("58", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 18, 6).toString()+"%");
			params.put("59", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 18, 7).toString()+"%");
			params.put("60", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 18, 8).toString()+"%");
			params.put("61", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 19, 5).toString()+"%");
			params.put("62", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 19, 6).toString()+"%");
			params.put("63", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 19, 7).toString()+"%");
			params.put("64", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 19, 8).toString()+"%");
			params.put("65", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 19, 9).toString()+"%");
			params.put("66", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 19, 10).toString()+"%");
			params.put("67", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 19, 11).toString()+"%");
			params.put("68", uwDao.selectNTMI(businessId, Integer.toString(lsdbs), cbid, 19, 12).toString()+"%");
		}
		
		params.put("lsdbs", lsdbs);
		
		if("096,099,182".indexOf(businessId)>-1 ||( businessId.equals("111") && lsdbs==2)){
			if("096".indexOf(businessId)>-1 && (lsdbs==1 || lsdbs==2 || lsdbs==3)){
				params.put("pasal1", "6");
				params.put("pasal2", "5");
				businessId = "076";
			}else if("182".indexOf(businessId)>-1){ //&& (lsdbs==1 || lsdbs==2 || lsdbs==3)){
				businessId = "182";
				params.put("pasal1", "9");
				params.put("pasal2", "4");
				kontribusi = "Kontribusi/";
				if(flag_new == 0){
					multi_invest = "\"Multi Invest\"";
				}else{
					multi_invest = "\"SMiLe Multi Invest\"";
				}
				syariah = "Syariah ";
				syariah2 = "Syariah";
				peserta = "Peserta";
			}else if("111".indexOf(businessId)>-1 &&lsdbs==2){
				businessId = "111";
				params.put("pasal1", "9");
				params.put("pasal2", "2");
				multi_invest = " \"Simas MaxiSave\"";
			}else {
				businessId = "096";
				params.put("pasal1", "9");
				params.put("pasal2", "4");
				if(flag_new == 0){
					multi_invest = "\"Multi Invest\"";
				}else{
					multi_invest = "\"SMiLe Multi Invest\"";
				}
			}
			
		}
		else if("135,136".indexOf(businessId)>-1){
			businessId = "074";
			params.put("pasal1", "9");
			params.put("pasal2", "4");
		}
		
		//Bagian I (header manfaat & manfaat baris pertama)
		if(businessId.equals("074")) {
			params.put("header", "MANFAAT ASURANSI MULTI INVEST " + (lsdbs==1?"SILVER":lsdbs==2?"GOLD":lsdbs==3?"PLATINUM":""));
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir tahun polis ke-5 (lima) dan asuransi masih berlaku, maka kepada Pemegang Polis akan dibayarkan Manfaat Asuransi yang dihitung berdasarkan prosentase dari Premi Pokok Tahunan dan besarnya tergantung dari Premi yang dibayar penuh. Apabila atas pilihan Pemegang Polis dan atas persetujuan Penanggung, Pemegang Polis memilih untuk tidak mengambil Manfaat Asuransi pada akhir tahun Polis kelima, maka Pemegang Polis dapat mengambilnya di akhir tahun Polis keenam, atau ketujuh atau kedelapan. Apabila Manfaat Asuransi ini dibayarkan setelah akhir tahun kelima dan bukan di akhir tahun Polis, maka besarnya Manfaat Asuransi dihitung secara proporsional. Manfaat Asuransi ini hanya dibayarkan sekali saja. Besarnya Manfaat Asuransi ini adalah sebagai berikut :");
			manfaat.add(0, m);
		}else if(businessId.equals("076")) {
			if(lsdbs>=7){
				multi_invest="Holyland Invest";
				params.put("header", "MANFAAT ASURANSI HOLYLAND INVEST " );
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(1));
				m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir tahun polis ke-5 (lima) dan asuransi masih berlaku, maka kepada Pemegang Polis akan dibayarkan Manfaat Asuransi yang dihitung berdasarkan prosentase dari Premi Tahunan. Apabila atas pilihan Pemegang Polis dan atas persetujuan Penanggung, Pemegang Polis memilih untuk tidak mengambil Manfaat Asuransi pada akhir tahun Polis kelima, maka Pemegang Polis dapat mengambilnya di akhir tahun Polis keenam, atau ketujuh atau kedelapan. Apabila Manfaat Asuransi ini dibayarkan setelah akhir tahun kelima dan bukan di akhir tahun Polis, maka besarnya Manfaat Asuransi dihitung secara proporsional. Manfaat Asuransi ini hanya dibayarkan sekali saja. Besarnya Manfaat Asuransi ini adalah sebagai berikut: ");
				manfaat.add(0, m);	
			}else{
				params.put("header", "MANFAAT ASURANSI MULTI INVEST " + (lsdbs==1?"III SILVER":lsdbs==2?"III GOLD":lsdbs==3?"III PLATINUM":""));
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(1));
				m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir tahun polis ke-5 (lima) dan asuransi masih berlaku, maka kepada Pemegang Polis akan dibayarkan Manfaat Asuransi yang dihitung berdasarkan prosentase dari Premi Pokok Tahunan dan besarnya tergantung dari Premi yang dibayar penuh. Apabila atas pilihan Pemegang Polis dan atas persetujuan Penanggung, Pemegang Polis memilih untuk tidak mengambil Manfaat Asuransi pada akhir tahun Polis kelima, maka Pemegang Polis dapat mengambilnya di akhir tahun Polis keenam, atau ketujuh atau kedelapan.\nNamun demikian, besarnya Manfaat Asuransi yang tidak diambil setelah akhir tahun Polis ketujuh tidak dijamin, tergantung dari tingkat bunga pasar yang akan ditentukan oleh Penanggung pada waktu itu.\nApabila Manfaat Asuransi ini dibayarkan setelah akhir tahun kelima dan bukan di akhir tahun Polis, maka besarnya Manfaat Asuransi dihitung secara proporsional. Manfaat Asuransi ini hanya dibayarkan sekali saja. Besarnya Manfaat Asuransi ini adalah sebagai berikut :");
				manfaat.add(0, m);
			}
			
			
		}else if(businessId.equals("096")){
				if((lsdbs>=7 && lsdbs<=9) ){
					params.put("header", "MANFAAT ASURANSI HOLYLAND INVEST - " + (lsdbs==7?"SILVER":lsdbs==8?"GOLD":lsdbs==9?"PLATINUM":""));
					multi_invest="\"Holyland Invest\"";
				}else if(lsdbs>=13 && lsdbs<=15 ||(lsdbs>=19 && lsdbs<=21)){
					params.put("header", "MANFAAT ASURANSI HOLYLAND INVEST - " + (lsdbs==7 || lsdbs == 19?"SILVER":lsdbs==8 || lsdbs == 20?"GOLD":lsdbs==9 || lsdbs == 21?"PLATINUM":"") + " BETHANY");
					multi_invest="\"Holyland Invest\"";
				}else{
					if(flag_new == 0){
						params.put("header", "MANFAAT ASURANSI MULTI INVEST - " + (lsdbs==10?"SILVER":lsdbs==11?"GOLD":lsdbs==12?"PLATINUM":""));
						multi_invest = " \"Multi Invest\"";
					}else{
						params.put("header", "MANFAAT ASURANSI SMiLe MULTI INVEST - " + ("10,16,22,28".indexOf(Integer.toString(lsdbs))>-1?"SILVER":"11,17,23,29".indexOf(Integer.toString(lsdbs))>-1 ?"GOLD":"12,18,24,30".indexOf(Integer.toString(lsdbs))>-1?"PLATINUM":""));
						multi_invest = " \"SMiLe Multi Invest\"";
					}
				}
				
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(1));
				m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir tahun polis ke-5 (lima) dan asuransi masih berlaku, maka kepada Pemegang Polis akan dibayarkan Manfaat Asuransi yang dihitung berdasarkan prosentase dari Premi Tahunan. Apabila atas pilihan Pemegang Polis dan atas persetujuan Penanggung, Pemegang Polis memilih untuk tidak mengambil Manfaat Asuransi pada akhir tahun Polis kelima, maka Pemegang Polis dapat mengambilnya di akhir tahun Polis keenam, atau ketujuh atau kedelapan. Apabila Manfaat Asuransi ini dibayarkan setelah akhir tahun kelima dan bukan di akhir tahun Polis, maka besarnya Manfaat Asuransi dihitung secara proporsional. Manfaat Asuransi ini hanya dibayarkan sekali saja. Besarnya Manfaat Asuransi ini adalah sebagai berikut: ");
				manfaat.add(0, m);
					
		}else if(businessId.equals("182")){
			if((lsdbs>=7 && lsdbs <=9) || (lsdbs>=13 && lsdbs <=15)){
				params.put("header", "MANFAAT ASURANSI SYARIAH MULTI INVEST " + ((lsdbs==1 || lsdbs==4 || lsdbs == 7 || lsdbs == 13)?"SYARIAH SILVER":(lsdbs==2 || lsdbs==5 || lsdbs == 8 || lsdbs == 14)?"SYARIAH GOLD":(lsdbs==3 || lsdbs==6 || lsdbs == 9 || lsdbs == 15)?"SYARIAH PLATINUM":""));
			}else{
				if(flag_new == 1){
					params.put("header", "MANFAAT ASURANSI SYARIAH SMiLe MULTI INVEST SYARIAH " + ((lsdbs==1 || lsdbs==4 || lsdbs == 10 || lsdbs == 16)?"SYARIAH SILVER":(lsdbs==2 || lsdbs==5 || lsdbs == 11 || lsdbs == 17 )?"SYARIAH GOLD":(lsdbs==3 || lsdbs==6 || lsdbs == 12 || lsdbs == 18)?"SYARIAH PLATINUM":""));
				}else{
					params.put("header", "MANFAAT ASURANSI SYARIAH  MULTI INVEST " + ((lsdbs==1 || lsdbs==4 || lsdbs == 10)?"SYARIAH SILVER":(lsdbs==2 || lsdbs==5 || lsdbs == 11)?"SYARIAH GOLD":(lsdbs==3 || lsdbs==6 || lsdbs == 12)?"SYARIAH PLATINUM":""));
					
				}
			}
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Peserta hidup pada akhir tahun polis ke-5 (lima) dan Asuransi Syariah masih berlaku, maka kepada Pemegang Polis akan dibayarkan Manfaat Asuransi Syariah yang dihitung berdasarkan prosentase dari Kontribusi/Premi Tahunan. Apabila atas pilihan Pemegang Polis dan atas persetujuan Wakil Para Peserta, Pemegang Polis memilih untuk tidak mengambil Manfaat Asuransi Syariah pada akhir tahun Polis kelima, maka Pemegang Polis dapat mengambilnya di akhir tahun Polis keenam, atau ketujuh atau kedelapan. Apabila Manfaat Asuransi Syariah ini dibayarkan setelah akhir tahun kelima dan bukan di akhir tahun Polis, maka besarnya Manfaat Asuransi Syariah dihitung secara proporsional. Manfaat Asuransi Syariah ini hanya dibayarkan sekali saja. Besarnya Manfaat Asuransi Syariah ini adalah sebagai berikut: ");
			businessId = "182";
			pertama = "pertama"; 
			uang_pertanggungan = "Pengembalian Premi";
			manfaat.add(0, m);	
		}else if(businessId.equals("111") && lsdbs==2){//simas maxi save
			params.put("header", "MANFAAT ASURANSI SIMAS MAXISAVE");
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir tahun polis ke-5 (lima) dan asuransi masih berlaku, maka atas pilihan Pemegang Polis dan atas persetujuan Penanggung, akan dibayarkan Manfaat Asuransi pada akhir tahun Polis kelima yang dihitung berdasarkan prosentase dari Premi Tahunan. Apabila Pemegang Polis tidak menentukan pilihannya pada akhir tahun ke-5 (lima), 6 (enam) atau 7 (tujuh) maka akan diperpanjang hingga akhir tahun berikutnya."
						+"Apabila Manfaat Asuransi ini dibayarkan setelah akhir tahun kelima dan bukan di akhir tahun Polis, maka besarnya Manfaat Asuransi dihitung secara proporsional. Manfaat Asuransi ini hanya dibayarkan sekali saja. Besarnya Manfaat Asuransi ini adalah sebagai berikut: ");
			businessId = "111";
			pertama = "pertama"; 
			cbid=3;
			manfaat.add(0, m);	
		}
		
		//Bagian II (Premi)
		if(businessId.equals("111")&& lsdbs==2){
//			params.put("cbid", Integer.valueOf(businessId).toString());
			params.put("cbid", Integer.valueOf("194").toString());
		}else{
			params.put("cbid", Integer.valueOf(businessId).toString()+"_"+new Integer(cbid).toString());
		}
		
		
		//Bagian III  (manfaat2 dan 3)
		
		if(il_age>55) {
			if("111".indexOf(businessId)==-1){
//				m = new HashMap();
//				m.put("LSMAN_LINE", null);
//				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia setelah akhir tahun Polis ke-5 (lima) dan Pemegang Polis memilih untuk tidak mengambil Manfaat Asuransi pada akhir tahun kelima, maka Manfaat Asuransi yang belum diambil langsung dibayarkan dan sejak saat itu asuransi berakhir.");
//				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", null);
				m.put("MANFAAT", "Ketentuan lebih lanjut tentang Manfaat Asuransi "+syariah+"yang dibayarkan secara proporsional dan Pembayaran Premi tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Individu "+multi_invest+" "+syariah+".");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(2));
				m.put("MANFAAT", "Apabila "+peserta+" meninggal dunia bukan karena Kecelakaan dalam 5 (lima) tahun pertama sejak berlakunya Polis dan asuransi "+syariah+"masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi "+syariah+" yang besarnya sebagai berikut:");
				manfaat2.add(m);
				//bagian manfaat meninggal per tahun :
				params.put("usiamature", "udahtuanihye");
				params.put("matureid", "_96");
				params.put("syariah", syariah);
				params.put("kontribusi", kontribusi);
				params.put("pertama", pertama);
				
				if(lsdbs>=4 && lsdbs<=15){
					if(lsdbs>=4 && lsdbs<=7){
						uang_pertanggungan = "Pengembalian Premi yang telah dibayarkan";
					}else{
						uang_pertanggungan = "Pengembalian Premi";
					}
					params.put("uang_pertanggungan", uang_pertanggungan);
				}else{
					params.put("uang_pertanggungan", uang_pertanggungan);
				}
				
				
//				m = new HashMap();
//				m.put("LSMAN_LINE", null);
//				m.put("MANFAAT", "Setelah dibayarkannya Manfaat Asuransi ini maka sejak saat itu asuransi berakhir. Ketentuan lebih lanjut tentang Nilai Tunai tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Multi Invest.");
//				manfaat3.add(m);
//				m = new HashMap();
//				m.put("LSMAN_LINE", null);
//				m.put("MANFAAT", " selain Manfaat Asuransi "+syariah+"tersebut diatas,  juga  ditambahkan  Nilai Tunai dan sejak saat itu asuransi "+syariah+"berakhir.");
//				manfaat3.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", null);
				m.put("MANFAAT", " Sejak diterimanya Manfaat Asuransi ini maka Asuransi berakhir.");
				manfaat3.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(3));
				m.put("MANFAAT", "Apabila "+peserta+" meninggal dunia karena Kecelakaan dalam 5 (lima) tahun pertama sejak berlakunya Polis dan Asuransi "+syariah+"masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi "+syariah+"sebesar 200% (dua ratus perseratus) Uang Pertanggungan ditambah dengan Nilai Tunai dan sejak saat itu Asuransi "+syariah+"berakhir. Ketentuan lebih lanjut tentang Asuransi Kecelakaan Diri tertera pada Syarat-Syarat Khusus Asuransi Tambahan Kecelakaan Diri. Ketentuan lebih lanjut tentang Nilai Tunai tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Individu "+multi_invest+" "+syariah+".");
				manfaat3.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(4));
				m.put("MANFAAT", "Apabila "+peserta+" meninggal dunia karena Kecelakaan setelah akhir tahun Polis ke-5 (lima) hingga akhir tahun Polis ke-8 (delapan), maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi "+syariah+"sebesar 200% (dua ratus perseratus) Uang Pertanggungan ditambah Manfaat Asuransi "+syariah+"yang belum diambil sebagaimana dimaksud dalam butir 1 (bila ada) dan sejak saat itu Asuransi "+syariah+"berakhir.");
				manfaat3.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(5));
				m.put("MANFAAT", "Apabila "+peserta+" meninggal dunia bukan karena Kecelakaan setelah akhir tahun Polis ke-5 (lima) hingga akhir tahun polis ke-8 (delapan), maka tidak ada pembayaran Manfaat Asuransi "+syariah+"kecuali apabila ada Manfaat Asuransi "+syariah+"yang belum diambil sebagaimana dimaksud dalam butir 1 dan sejak saat itu Asuransi "+syariah+"berakhir.");
				manfaat3.add(m);
			}else{
				m = new HashMap();
				m.put("LSMAN_LINE", null);
				m.put("MANFAAT", "Ketentuan lebih lanjut tentang Manfaat Asuransi yang dibayarkan secara proporsional dan pembayaran premi tertera pada Syarat Syarat umum dan Syarat Syarat Khusus Polis Asuransi Jiwa Individu Simas Maxisave");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(2));
				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia bukan karena Kecelakaan dalam 5 (lima) tahun pertama sejak berlakunya Polis dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi yang besarnya sebagai berikut :");
				manfaat2.add(m);
				//bagian manfaat meninggal per tahun :
				params.put("usiamature", "udahtuanihye");
				params.put("matureid", "_194");
				params.put("syariah", syariah);
				params.put("kontribusi", kontribusi);
				params.put("pertama", pertama);
				
//				m = new HashMap();
//				m.put("LSMAN_LINE", null);
//				m.put("MANFAAT", " selain Manfaat Asuransi tersebut diatas,  juga  ditambahkan  Nilai Tunai dan sejak saat itu asuransi berakhir.");
//				manfaat3.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", null);
				m.put("MANFAAT", " Sejak diterimanya Manfaat Asuransi ini maka Asuransi berakhir.");
				manfaat3.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(3));
				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia karena Kecelakaan dalam 5 (lima) tahun pertama sejak berlakunya Polis dan Asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 200% (dua ratus perseratus) Uang Pertanggungan ditambah dengan Nilai Tunai dan sejak saat itu Asuransi berakhir. Ketentuan lebih lanjut tentang Asuransi Kecelakaan Diri tertera pada Syarat-Syarat Khusus Asuransi Tambahan Kecelakaan Diri. Ketentuan lebih lanjut tentang Nilai Tunai tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Individu Simas Maxisave.");
				manfaat3.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(4));
				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia karena Kecelakaan setelah akhir tahun Polis ke-5 (lima) hingga akhir tahun Polis ke-8 (delapan), maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 200% (dua ratus perseratus) Uang Pertanggungan ditambah Manfaat Asuransi yang belum diambil sebagaimana dimaksud dalam butir 1 (bila ada) dan sejak saat itu asuransi berakhir.");
				manfaat3.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(5));
				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia bukan karena Kecelakaan setelah akhir tahun Polis ke-5 (kelima) hingga akhir tahun Polis ke-8 (kedelapan), maka tidak ada pembayaran Manfaat Asuransi kecuali apabila ada Manfaat Asuransi yang belum diambil sebagaimana dimaksud dalam buitr 1 dan sejak saat itu asuransi berakhir.");
				manfaat3.add(m);
			}
		}else {
			if("111".indexOf(businessId)==-1){
				
				if((lsdbs>=7 && lsdbs<=9) || (lsdbs>=13 && lsdbs<=15)){
					multi_invest="Holyland Invest";
				}
				if(businessId.equals("182") && (lsdbs>=7 && lsdbs<=9 || lsdbs>=13 && lsdbs<=15)){					
					multi_invest="Multi Invest";
				} else{
					if(flag_new == 0){
						multi_invest=" Multi Invest";
					}else{
						multi_invest=" SMiLe Multi Invest";
					}
				}
				m = new HashMap();
				m.put("LSMAN_LINE", null);
				m.put("MANFAAT", "Ketentuan lebih lanjut tentang Manfaat Asuransi "+syariah+"yang dibayarkan secara proporsional dan Pembayaran Premi tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Individu "+syariah+"\""+multi_invest+" "+syariah2+"\".");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(2));
				m.put("MANFAAT", "Apabila "+peserta+" meninggal dunia bukan karena Kecelakaan dalam 5 (lima) tahun pertama sejak berlakunya Polis dan asuransi "+syariah+"masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi "+syariah+"sebesar 100% (seratus perseratus) Uang Pertanggungan  ditambah dengan Nilai Tunai dan sejak saat itu Asuransi "+syariah+"berakhir. Ketentuan lebih lanjut tentang Nilai Tunai tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Individu "+syariah+"\" "+multi_invest+" "+syariah2+"\".");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", null);
				m.put("MANFAAT", " Sejak diterimanya Manfaat Asuransi ini maka Asuransi berakhir.");
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(3));
				m.put("MANFAAT", "Apabila "+peserta+" meninggal dunia karena Kecelakaan dalam 5 (lima) tahun pertama sejak berlakunya Polis dan Asuransi "+syariah+"masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi "+syariah+"sebesar 200% (dua ratus perseratus) Uang Pertanggungan ditambah dengan Nilai Tunai dan sejak saat itu Asuransi "+syariah+"berakhir. Ketentuan lebih lanjut tentang Asuransi Kecelakaan Diri "+syariah+"tertera pada Syarat-Syarat Khusus Asuransi Tambahan Kecelakaan Diri "+syariah2+". Ketentuan lebih lanjut tentang Nilai Tunai tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Individu "+syariah+"\" "+multi_invest+" "+syariah2+"\".");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(4));
				m.put("MANFAAT", "Apabila "+peserta+" meninggal dunia karena Kecelakaan setelah akhir tahun Polis ke-5 (lima) hingga akhir tahun Polis ke-8 (delapan), maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi "+syariah+"sebesar 200% (dua ratus perseratus) Uang Pertanggungan ditambah Manfaat Asuransi "+syariah+"yang belum diambil sebagaimana dimaksud dalam butir 1 (bila ada) dan sejak saat itu Asuransi "+syariah+"berakhir.");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(5));
				m.put("MANFAAT", "Apabila "+peserta+" meninggal dunia bukan karena Kecelakaan setelah akhir tahun Polis ke-5 (lima) hingga akhir tahun polis ke-8 (delapan), maka tidak ada pembayaran Manfaat Asuransi "+syariah+"kecuali apabila ada Manfaat Asuransi "+syariah+"yang belum diambil sebagaimana dimaksud dalam butir 1 dan sejak saat itu Asuransi "+syariah+"berakhir.");
				manfaat2.add(m);				
				params.put("matureid", "");
				params.put("syariah", syariah);
				params.put("kontribusi", kontribusi);
				params.put("pertama", pertama);
			}else{
				multi_invest="Simas Maxi Save";
				m = new HashMap();
				m.put("LSMAN_LINE", null);
				m.put("MANFAAT", "Ketentuan lebih lanjut tentang Manfaat Asuransi yang dibayarkan secara proporsional dan pembayaran premi tertera pada Syarat Syarat umum dan Syarat Syarat Khusus Polis Asuransi Jiwa Individu Simas Maxisave.");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(2));
				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia bukan karena Kecelakaan dalam 5 (lima) tahun pertama sejak berlakunya Polis dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% (seratus perseratus) Uang Pertanggungan  ditambah dengan Nilai Tunai dan sejak saat itu asuransi berakhir. Ketentuan lebih lanjut tentang Nilai Tunai tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Individu Simas Maxisave.");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", null);
				m.put("MANFAAT", " Sejak diterimanya Manfaat Asuransi ini maka Asuransi berakhir.");
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(3));
				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia karena Kecelakaan dalam 5 (lima) tahun pertama sejak berlakunya Polis dan Asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 200% (dua ratus perseratus) Uang Pertanggungan ditambah dengan Nilai Tunai dan sejak saat itu asuransi berakhir. Ketentuan lebih lanjut tentang Asuransi Kecelakaan Diri tertera pada Syarat-Syarat Khusus Asuransi Tambahan Kecelakaan Diri. Ketentuan lebih lanjut tentang Nilai Tunai tertera pada Syarat-Syarat Umum dan Syarat-Syarat Khusus Polis Asuransi Jiwa Individu Simas Maxisave.");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(4));
				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia karena Kecelakaan setelah akhir tahun Polis ke-5 (lima) hingga akhir tahun Polis ke-8 (delapan), maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 200% (dua ratus perseratus) Uang Pertanggungan ditambah Manfaat Asuransi yang belum diambil sebagaimana dimaksud dalam butir 1 (bila ada) dan sejak saat itu Asuransi berakhir.");
				manfaat2.add(m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(5));
				m.put("MANFAAT", "Apabila Tertanggung meninggal dunia bukan karena Kecelakaan setelah akhir tahun Polis ke-5 (kelima) hingga akhir tahun Polis ke-8 (kedelapan), maka tidak ada pembayaran Manfaat Asuransi kecuali apabila ada Manfaat Asuransi yang belum diambil sebagaimana dimaksud dalam buitr 1 dan sejak saat itu Asuransi berakhir.");
				manfaat2.add(m);
				params.put("matureid", "");
				params.put("syariah", syariah);
				params.put("kontribusi", kontribusi);
				params.put("pertama", pertama);
			}
			
		}
		
		//Bagian IV (Proses Rider)
		List rider = this.uwDao.selectDetailManfaatTambahan(spaj);
		String ls_manfaat;
		
		if(rider.size()>0) {
			int ll_no_urut = ((Integer)((HashMap) rider.get(0)).get("LSMAN_NOURUT"));
			for(int i=0; i<rider.size(); i++) {
				if(logger.isDebugEnabled())logger.debug("3 "+i);
				Map mm = (HashMap) rider.get(i);
				String lsbs_id = ((Integer) mm.get("LSBS_ID")).toString();
				String lsbs_number = ((Integer) mm.get("LSDBS_NUMBER")).toString();
				Map ipi = this.uwDao.selectInfoProductInsured(spaj, lsbs_id);
				Double ldc_rider_tsi = (Double) ipi.get("MSPR_TSI");
				ls_manfaat = setNilaiManfaat(
						idb_premium.doubleValue(), cbid, ii_umur_beasiswa, spaj, lsbs_id, 
						Integer.parseInt(lsbs_number), ldc_rider_tsi.doubleValue(), lku_id, 0, ll_no_urut, il_age);
				Map mu = (HashMap) rider.get(0);
				mu.remove("TAMB_MANFAAT");mm.put("TAMB_MANFAAT", ls_manfaat);
				rider.remove(i); rider.add(0, mm);			
			}		
		}
		
		//Bagian V (Proses NT)
		List manfaatNilaiTunai = nilaiTunai.proses(spaj, lus_id, "cetak", 0, 0);
		//
		params.put("sdsManfaat", JasperReportsUtils.convertReportData(manfaat));
		params.put("sdsManfaat2", JasperReportsUtils.convertReportData(manfaat2));
		String subKeys[] = new String[] {"sdsManfaat", "sdsManfaat2"};
		if(manfaat3.size()!=0) {
			params.put("sdsManfaat3", JasperReportsUtils.convertReportData(manfaat3));
			StringUtils.addStringToArray(subKeys, "sdsManfaat3");
		}
		if(rider.size()!=0) {
			params.put("sdsRider", JasperReportsUtils.convertReportData(rider));
			StringUtils.addStringToArray(subKeys, "sdsRider");
		}
		if(manfaatNilaiTunai.size()!=0) {
			params.put("sdsNT", JasperReportsUtils.convertReportData(manfaatNilaiTunai));
			StringUtils.addStringToArray(subKeys, "sdsNT");
		}
		params.put("subReportDatakeys", subKeys);
		
		return params;
	}
	
	private Map prosesManfaat178(String lus_id, String spaj, int il_age, String businessId, int lsdbs, int cbid, Double idb_premium, Integer ii_umur_beasiswa, String lku_id) throws Exception{
		Map params = new HashMap();
		String nama="SM";
		if((lsdbs>16 && lsdbs<25) || lsdbs>48 && lsdbs<65){
			nama="PROLITA";
		}else if(lsdbs>24 && lsdbs<33){
			nama="PROSETA";
		}
		if(lsdbs==1 || lsdbs==17 || lsdbs==33 || lsdbs==49){
			params.put("tipe", "("+nama+"-I)");
			params.put("biayainap", "Rp.300,000/Hari");
			params.put("biayaicu", "Rp.600,000/Hari");
			params.put("biayacilaka", "Rp.600,000/Hari");
		}else if(lsdbs==2 || lsdbs==18 || lsdbs==34 || lsdbs==50 ){
			params.put("tipe", "("+nama+"-II)");
			params.put("biayainap", "Rp.400,000/Hari");
			params.put("biayaicu", "Rp.800,000/Hari");
			params.put("biayacilaka", "Rp.800,000/Hari");
		}else if(lsdbs==3 || lsdbs==19 || lsdbs==35 || lsdbs==51){
			params.put("tipe", "("+nama+"-III)");
			params.put("biayainap", "Rp.500,000/Hari");
			params.put("biayaicu", "Rp.1,000,000/Hari");
			params.put("biayacilaka", "Rp.1,000,000/Hari");
		}else if(lsdbs==4 || lsdbs==20 || lsdbs==36 || lsdbs==52){
			params.put("tipe", "("+nama+"-IV)");
			params.put("biayainap", "Rp.600,000/Hari");
			params.put("biayaicu", "Rp.1,200,000/Hari");
			params.put("biayacilaka", "Rp.1,200,000/Hari");
		}else if(lsdbs==5 || lsdbs==21 || lsdbs==37|| lsdbs==53){
			params.put("tipe", "("+nama+"-V)");
			params.put("biayainap", "Rp.700,000/Hari");
			params.put("biayaicu", "Rp.1,400,000/Hari");
			params.put("biayacilaka", "Rp.1,400,000/Hari");
		}else if(lsdbs==6 || lsdbs==22 || lsdbs==38 || lsdbs==54){
			params.put("tipe", "("+nama+"-VI)");
			params.put("biayainap", "Rp.800,000/Hari");
			params.put("biayaicu", "Rp.1,600,000/Hari");
			params.put("biayacilaka", "Rp.1,600,000/Hari");
		}else if(lsdbs==7 || lsdbs==23 || lsdbs==39 || lsdbs==55){
			params.put("tipe", "("+nama+"-VII)");
			params.put("biayainap", "Rp.900,000/Hari");
			params.put("biayaicu", "Rp.1,800,000/Hari");
			params.put("biayacilaka", "Rp.1,800,000/Hari");
		}else if(lsdbs==8 || lsdbs==24 || lsdbs==40|| lsdbs==56){
			params.put("tipe", "("+nama+"-VIII)");
			params.put("biayainap", "Rp.1,000,000/Hari");
			params.put("biayaicu", "Rp.2,000,000/Hari");
			params.put("biayacilaka", "Rp.2,000,000/Hari");
		}else if(lsdbs==9 || lsdbs==25 || lsdbs==41 || lsdbs==57){
			params.put("tipe", "("+nama+"-I)");
			params.put("biayainap", "Rp.300,000/Hari");
			params.put("biayaicu", "Rp.600,000/Hari");
			params.put("biayacilaka", "Rp.600,000/Hari");
		}else if(lsdbs==10 || lsdbs==26 || lsdbs==42 || lsdbs==58){
			params.put("tipe", "("+nama+"-II)");
			params.put("biayainap", "Rp.400,000/Hari");
			params.put("biayaicu", "Rp.800,000/Hari");
			params.put("biayacilaka", "Rp.800,000/Hari");
		}else if(lsdbs==11 || lsdbs==27 ||lsdbs==43 || lsdbs==59){
			params.put("tipe", "("+nama+"-III)");
			params.put("biayainap", "Rp.500,000/Hari");
			params.put("biayaicu", "Rp.1,000,000/Hari");
			params.put("biayacilaka", "Rp.1,000,000/Hari");
		}else if(lsdbs==12 || lsdbs==28 || lsdbs==44 || lsdbs==60){
			params.put("tipe", "("+nama+"-IV)");
			params.put("biayainap", "Rp.600,000/Hari");
			params.put("biayaicu", "Rp.1,200,000/Hari");
			params.put("biayacilaka", "Rp.1,200,000/Hari");
		}else if(lsdbs==13 || lsdbs==29 ||lsdbs==45 || lsdbs==61){
			params.put("tipe", "("+nama+"-V)");
			params.put("biayainap", "Rp.700,000/Hari");
			params.put("biayaicu", "Rp.1,400,000/Hari");
			params.put("biayacilaka", "Rp.1,400,000/Hari");
		}else if(lsdbs==14 || lsdbs==30 || lsdbs==46 || lsdbs==62){
			params.put("tipe", "("+nama+"-VI)");
			params.put("biayainap", "Rp.800,000/Hari");
			params.put("biayaicu", "Rp.1,600,000/Hari");
			params.put("biayacilaka", "Rp.1,600,000/Hari");
		}else if(lsdbs==15 || lsdbs==31 ||lsdbs==47 || lsdbs==63){
			params.put("tipe", "("+nama+"-VII)");
			params.put("biayainap", "Rp.900,000/Hari");
			params.put("biayaicu", "Rp.1,800,000/Hari");
			params.put("biayacilaka", "Rp.1,800,000/Hari");
		}else if(lsdbs==16 || lsdbs==32 || lsdbs==48 || lsdbs==64){
			params.put("tipe", "("+nama+"-VIII)");
			params.put("biayainap", "Rp.1,000,000/Hari");
			params.put("biayaicu", "Rp.2,000,000/Hari");
			params.put("biayacilaka", "Rp.2,000,000/Hari");
		}
		String catatan;
		catatan = "*)   Termasuk manfaat pengembalian 100% dari total premi yang telah dibayarkan (tanpa bunga)";
		params.put("catatan", catatan);
		params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_smart_medi"));
		

		businessId = FormatString.rpad("0", businessId, 3);
		
		Map m;
		
		params.put("klausa", "KLAUSULA : Daftar Nilai Tunai Rp berdasarkan Pasal 12. Syarat - syarat Umum Polis :");
		
		List manfaatNilaiTunai = this.nilaiTunai.proses(spaj, lus_id, "cetak", 0, 0);
		if(manfaatNilaiTunai.size()!=0) {
			params.put("sdsNT", JasperReportsUtils.convertReportData(manfaatNilaiTunai));
			
			params.put("subReportDatakeys", new String[] {"catatan","sdsNT"});
		}
		
		
		return params;
	}
	
	private Map prosesManfaat208(String lus_id, String spaj, int il_age, String businessId, int lsdbs, int cbid, Double idb_premium, Integer ii_umur_beasiswa, String lku_id, Double mspr_tsi) throws Exception{
		Map params = new HashMap();
		String nama="SMART KID";
		if (businessId.equals("219") && (lsdbs >= 1 && lsdbs <= 4)){
			nama="SIMAS KID Insurance Syariah";
		}else if (businessId.equals("219") && (lsdbs >= 5 && lsdbs <= 8)){//helpdesk [138638] produk baru SPP Syariah (219-5~8)
			nama="SMART PLAN PROTECTION Syariah";
		}else if(lsdbs >= 5 && lsdbs <= 8){
			nama="SIMAS KID Insurance";
		}else if((lsdbs >= 9 && lsdbs <= 12) || (lsdbs >= 37 && lsdbs <= 44)){//helpdesk [137082] Mark Valentino
			nama="SMiLe KID Insurance";
		}else if(lsdbs >= 13 && lsdbs <= 16){
			nama="VIP Edu Plan Insurance";
		}else if(lsdbs >= 17 && lsdbs <= 20){
			nama="SMiLe KIDs Insurance";
		}else if(lsdbs >= 21 && lsdbs <= 24){
			nama="SMART PLAN PROTECTION";
		}else if(lsdbs >= 25 && lsdbs <= 28){
			nama="SIMAS KID Insurance";
		}else if(lsdbs >= 29 && lsdbs <= 32){
			nama="SMART KID Insurance";
		
		}else if(lsdbs >= 45 && lsdbs <= 48){
			nama="SMiLe KID Insurance";
		}
		
		BigDecimal tsi1=new BigDecimal(mspr_tsi);
	    
		String tsi =FormatString.formatCurrency("", tsi1);
			
		params.put("upx", ""+tsi+"");
		params.put("spaj", spaj);
		String catatan;
		catatan = "*)   Termasuk manfaat pengembalian 100% dari total premi yang telah dibayarkan (tanpa bunga)";
		params.put("catatan", catatan);
		List thp = null; List nt = null;
		if(businessId.equals("219") && (lsdbs >= 5 && lsdbs <= 8)){ //helpdesk [138638] produk baru SPP Syariah (219-5~8)
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_smart_protect"));
		}else if((lsdbs >= 5 && lsdbs <= 20) || businessId.equals("219") || (lsdbs >= 25 && lsdbs <=28)){
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_simas_kid"));
		}else if(lsdbs >= 21 && lsdbs <= 24){
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_smart_protect"));
		}else if((lsdbs >= 29 && lsdbs <= 32) &&  businessId.equals("208")){ // smart kid btn
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_simas_kid"));		
		}else if((lsdbs >= 33 && lsdbs <= 44) &&  businessId.equals("208")){ // simas kid bsim
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_simas_kid"));		
		}else if((lsdbs >= 45 && lsdbs <= 48) &&  businessId.equals("208")){ // smile kid bjb
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_simas_kid"));	
		}
		else{
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_smart_kid"));
		}

		businessId = FormatString.rpad("0", businessId, 3);
		
		Map m;
		
		params.put("klausa", "KLAUSULA : Daftar Tahapan Dana Belajar :");
		
		List manfaatNilaiTunai = this.nilaiTunai.proses(spaj, lus_id, "cetak", 0, 0);
		if(manfaatNilaiTunai.size()!=0) {
			if(lsdbs >= 5 && lsdbs <= 28 || businessId.equals("219")){
				thp = this.uwDao.selectTahapan208(spaj);
				nt =  this.uwDao.selectNilaiTunai208(spaj);
				params.put("sdsT", JasperReportsUtils.convertReportData(thp));
				params.put("sdsNT", JasperReportsUtils.convertReportData(nt));
				params.put("subReportDatakeys", new String[] {"sdsT","sdsNT"});
				params.put("lsbsId",businessId);
				params.put("lsdbs",lsdbs);
			}else if((lsdbs >= 29 && lsdbs <= 32) &&  businessId.equals("208")){
				thp = this.uwDao.selectTahapan208(spaj);
				nt =  this.uwDao.selectNilaiTunai208(spaj);
				params.put("sdsT", JasperReportsUtils.convertReportData(thp));
				params.put("sdsNT", JasperReportsUtils.convertReportData(nt));
				params.put("subReportDatakeys", new String[] {"sdsT","sdsNT"});
				params.put("lsbsId",businessId);
			}else if((lsdbs >= 33 && lsdbs <= 36) &&  businessId.equals("208")){
				thp = this.uwDao.selectTahapan208(spaj);
				nt =  this.uwDao.selectNilaiTunai208(spaj);
				params.put("sdsT", JasperReportsUtils.convertReportData(thp));
				params.put("sdsNT", JasperReportsUtils.convertReportData(nt));
				params.put("subReportDatakeys", new String[] {"sdsT","sdsNT"});
				params.put("lsbsId",businessId);
			}else if((lsdbs >= 37 && lsdbs <= 48) &&  businessId.equals("208")){
				thp = this.uwDao.selectTahapan208(spaj);
				nt =  this.uwDao.selectNilaiTunai208(spaj);
				params.put("sdsT", JasperReportsUtils.convertReportData(thp));
				params.put("sdsNT", JasperReportsUtils.convertReportData(nt));
				params.put("subReportDatakeys", new String[] {"sdsT","sdsNT"});
				params.put("lsbsId",businessId);
				params.put("lsdbs",lsdbs);
			}else{
				params.put("sdsNT", JasperReportsUtils.convertReportData(manfaatNilaiTunai));
				params.put("subReportDatakeys", new String[] {"catatan","sdsNT"});
			}	
		}
		
		
		return params;
	}
	
	private Map prosesManfaatTermRop(String businessId, int lsdbs, String spaj, String lus_id, Integer lscb_id) throws Exception{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lscb_id", lscb_id);
		
		if(("212".equals(businessId) && (lsdbs==1 || lsdbs==3 || lsdbs==12 || lsdbs==13)) || ("223".equals(businessId)&& (lsdbs==1 || lsdbs==2))){// helpdesk [135727] produk baru 212-12 Smile Term ROP Insurance //request bu titis prod 223 igaukiarwan 20062019
			List manfaatNilaiTunai = this.nilaiTunai.proses(spaj, lus_id, "cetak", 0, 0);
//			if(manfaatNilaiTunai.size()!=0) {
				List nt =  this.uwDao.selectNilaiTunaiTermRop(spaj);
				params.put("sdsNT", JasperReportsUtils.convertReportData(nt));
				params.put("subReportDatakeys", new String[] {"sdsNT"});
				params.put("lsbsId",businessId);
				params.put("lsdbs", lsdbs);
				if("223".equals(businessId) && lsdbs == 2){ //helpdesk [138638] produk baru SLP Syariah (223-2)
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_term_rop").toString());
					
					String pathFile = props.getProperty("pdf.dir.syaratpolis")+"\\"+businessId+"-"+FormatString.rpad("0", String.valueOf(lsdbs), 3)+"-NT.pdf";
					PdfReader reader = new PdfReader(pathFile);
			        int pages = reader.getNumberOfPages();
					com.itextpdf.text.pdf.PdfReader reader2 = new com.itextpdf.text.pdf.PdfReader(pathFile);
					String page = PdfTextExtractor.getTextFromPage(reader2, pages);
					if (page.contains("Ver-")) {
						int awal = page.indexOf("SSU");
						int akhir = page.lastIndexOf("Ver-");
						String footer1 =  page.substring(awal, akhir+5);
						
						int haldari = page.indexOf("Hal ");
						String footer2 = page.substring(haldari+4, haldari+5);
						
						params.put("footer1", footer1);
						params.put("footer2", "Hal "+footer2+" dari "+footer2);
					}
				}
				else
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_term_rop_2").toString());
//			}
		}else{
			List manfaatNilaiTunai = this.nilaiTunai.proses(spaj, lus_id, "cetak", 0, 0);
//			if(manfaatNilaiTunai.size()!=0) {
				List nt =  this.uwDao.selectNilaiTunaiTermRop(spaj);
				params.put("sdsNT", JasperReportsUtils.convertReportData(nt));
				params.put("subReportDatakeys", new String[] {"sdsNT"});
				params.put("lsbsId",businessId);
				params.put("lsdbs", lsdbs);
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.manfaat_sertifikat"));
				
//				String pathFile = "C:\\EkaWeb\\PDF\\"+businessId+"-"+FormatString.rpad("0", String.valueOf(lsdbs), 3)+"-NT.pdf";
				String pathFile = props.getProperty("pdf.dir.syaratpolis")+"\\"+businessId+"-"+FormatString.rpad("0", String.valueOf(lsdbs), 3)+"-NT.pdf";
	//	        PdfReader reader = new PdfReader(props.getProperty("pdf.dir")+"\\ss_smile\\"+lsbs+"-"+FormatString.rpad("0", detbisnis, 3)+".pdf");
		        PdfReader reader = new PdfReader(pathFile);
		        int pages = reader.getNumberOfPages();
				com.itextpdf.text.pdf.PdfReader reader2 = new com.itextpdf.text.pdf.PdfReader(pathFile);
				String page = PdfTextExtractor.getTextFromPage(reader2, pages);
				if (page.contains("Ver-")) {
					int awal = page.indexOf("SSU");
					int akhir = page.lastIndexOf("Ver-");
					String footer1 =  page.substring(awal, akhir+5);
					
					int haldari = page.indexOf("Hal ");
					String footer2 = page.substring(haldari+4, haldari+5);
					
					params.put("footer1", footer1);
					params.put("footer2", "Hal "+footer2+" dari "+footer2);
				}
//			}	
		}
		return params;
	}
	
	private Map prosesManfaat7991(String lus_id, String spaj, int il_age, String businessId, int lsdbs, int cbid, Double idb_premium, Integer ii_umur_beasiswa, String lku_id) throws Exception{
		
		int li_ri[] = new int[]{1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
		
		Map params = new HashMap();
		params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case79"));
		Integer relation = this.uwDao.selectPolicyRelation(spaj);
		if(relation==null) relation= new Integer(-1);
		List manfaat = new ArrayList();
		Map m;
		
		//Bagian I (header manfaat & manfaat)
		params.put("header", "MANFAAT ASURANSI \"SEHAT\"");
		params.put("klausa", "KLAUSULA : Daftar Nilai Tunai Rp berdasarkan Pasal 6. Syarat - syarat Umum Polis :");
		
		if(relation==1 && cbid != 0) {
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir tahun Polis dimana Tertanggung berusia 55 tahun dan Asuransi masih berlaku, maka akan dibayarkan Manfaat Asuransi sebesar 150% (seratus lima puluh perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir.");
			manfaat.add(0, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(2));
			m.put("MANFAAT", "Apabila Tertanggung menjalani Rawat Inap dalam 10 (sepuluh) tahun pertama masa Asuransi atau sampai dengan akhir tahun Polis dimana Tertanggung berusia 55 tahun, mana yang lebih dulu, dan Asuransi Tambahan Rawat Inap masih berlaku, maka akan dibayarkan Manfaat Santunan Harian Rawat Inap sebesar Rp. " + new DecimalFormat("#,##0.00;(#,##0.00)").format(250000 * li_ri[lsdbs-1]) + "/hari selama Tertanggung dirawat, dengan jangka waktu maksimum 90 (sembilan puluh) hari perawatan untuk satu tahun polis dan 300 (tiga ratus) hari perawatan untuk sepuluh tahun polis pertama. Ketentuan lebih lanjut tentang Rawat Inap tertera pada Syarat-Syarat Khusus Asuransi Tambahan Rawat Inap.");
			manfaat.add(1, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(3));
			m.put("MANFAAT", "Apabila tidak terjadi klaim terhadap Manfaat Rawat Inap sebagaimana dimaksud dalam butir 2, maka pada akhir tahun polis ke-10 (sepuluh) atau pada akhir tahun Polis dimana Tertanggung berusia 55 tahun, mana yang lebih dulu, akan dibayarkan Manfaat pengembalian premi sebesar 50% (lima puluh perseratus) dari total premi yang telah dibayarkan.");
			manfaat.add(2, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(4));
			m.put("MANFAAT", "Apabila Tertanggung menderita Cacat Tetap Total dalam masa pembayaran premi dan Asuransi Tambahan Bebas Premi masih berlaku, maka Tertanggung dibebaskan dari pembayaran Premi lanjutan. Ketentuan lebih lanjut tentang Asuransi Bebas Premi tertera pada Syarat-Syarat Khusus Asuransi Tambahan Bebas Premi");
			manfaat.add(3, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(5));
			m.put("MANFAAT", "Apabila Tertanggung meninggal dunia karena Kecelakaan dalam masa Asuransi dan Asuransi Tambahan Kecelakaan Diri masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 1000% (seribu perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir. Ketentuan lebih lanjut tentang Asuransi Kecelakaan Diri tertera pada Syarat-Syarat Khusus Asuransi Tambahan Kecelakaan Diri.");
			manfaat.add(4, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(6));
			m.put("MANFAAT", "Apabila Tertanggung meninggal dunia bukan karena Kecelakaan dalam masa Asuransi, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 150% (seratus lima puluh perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir.");
			manfaat.add(5, m);				
		}else if(il_age<17 && cbid !=0) {
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir tahun Polis dimana Tertanggung berusia 55 tahun dan Asuransi masih berlaku, maka akan dibayarkan Manfaat Asuransi sebesar 150% (seratus lima puluh perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir.");
			manfaat.add(0, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(2));
			m.put("MANFAAT", "Apabila Tertanggung menjalani Rawat Inap dalam 10 (sepuluh) tahun pertama masa Asuransi atau sampai dengan akhir tahun Polis dimana Tertanggung berusia 55 tahun, mana yang lebih dulu, dan Asuransi Tambahan Rawat Inap masih berlaku, maka akan dibayarkan Manfaat Santunan Harian Rawat Inap sebesar Rp. " + new DecimalFormat("#,##0.00;(#,##0.00)").format(250000 * li_ri[lsdbs-1]) + "/hari selama Tertanggung dirawat, dengan jangka waktu maksimum 90 (sembilan puluh) hari perawatan untuk satu tahun polis dan 300 (tiga ratus) hari perawatan untuk sepuluh tahun polis pertama. Ketentuan lebih lanjut tentang Rawat Inap tertera pada Syarat-Syarat Khusus Asuransi Tambahan Rawat Inap.");
			manfaat.add(1, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(3));
			m.put("MANFAAT", "Apabila tidak terjadi klaim terhadap Manfaat Rawat Inap sebagaimana dimaksud dalam butir 2, maka pada akhir tahun polis ke-10 (sepuluh) atau pada akhir tahun Polis dimana Tertanggung berusia 55 tahun, mana yang lebih dulu, akan dibayarkan Manfaat pengembalian premi sebesar 50% (lima puluh perseratus) dari total premi yang telah dibayarkan.");
			manfaat.add(2, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(4));
			m.put("MANFAAT", "Apabila Pemegang Polis meninggal dunia dalam masa pembayaran premi dan Asuransi masih berlaku, maka Polis menjadi bebas premi dan pertanggungan tetap berjalan serta manfaat lainnya yang telah dijanjikan tetap dibayarkan.");
			manfaat.add(3, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(5));
			m.put("MANFAAT", "Apabila Tertanggung meninggal dunia karena Kecelakaan dalam masa Asuransi dan Asuransi Tambahan Kecelakaan Diri masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 1000% (seribu perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir. Ketentuan lebih lanjut tentang Asuransi Kecelakaan Diri tertera pada Syarat-Syarat Khusus Asuransi Tambahan Kecelakaan Diri.");
			manfaat.add(4, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(6));
			m.put("MANFAAT", "Apabila Tertanggung meninggal dunia bukan karena Kecelakaan dalam masa Asuransi, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 150% (seratus lima puluh perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir.");
			manfaat.add(5, m);				
		}else {
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir tahun Polis dimana Tertanggung berusia 55 tahun dan Asuransi masih berlaku, maka akan dibayarkan Manfaat Asuransi sebesar 150% (seratus lima puluh perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir.");
			manfaat.add(0, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(2));
			m.put("MANFAAT", "Apabila Tertanggung menjalani Rawat Inap dalam 10 (sepuluh) tahun pertama masa asuransi atau sampai dengan akhir tahun Polis dimana Tertanggung berusia 55 tahun, mana yang lebih dulu, dan Asuransi Tambahan Rawat Inap masih berlaku, maka akan dibayarkan Manfaat Santunan Harian Rawat Inap sebesar Rp. " + new DecimalFormat("#,##0.00;(#,##0.00)").format(250000 * li_ri[lsdbs-1]) + "/hari selama Tertanggung dirawat, dengan jangka waktu maksimum 90 (sembilan puluh) hari perawatan untuk satu tahun polis dan 300 (tiga ratus) hari perawatan untuk sepuluh tahun polis pertama. Ketentuan lebih lanjut tentang Rawat Inap tertera pada Syarat-Syarat Khusus Asuransi Tambahan Rawat Inap.");
			manfaat.add(1, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(3));
			m.put("MANFAAT", "Apabila tidak terjadi klaim terhadap Manfaat Rawat Inap sebagaimana dimaksud dalam butir 2, maka pada akhir tahun polis ke-10 (sepuluh) atau pada akhir tahun Polis dimana Tertanggung berusia 55 tahun, mana yang lebih dulu, akan dibayarkan Manfaat pengembalian premi sebesar 50% (lima puluh perseratus) dari total premi yang telah dibayarkan.");
			manfaat.add(2, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(4));
			m.put("MANFAAT", "Apabila Tertanggung meninggal dunia karena Kecelakaan dalam masa Asuransi dan Asuransi Tambahan Kecelakaan Diri masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 1000% (seribu perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir. Ketentuan lebih lanjut tentang Asuransi Kecelakaan Diri tertera pada Syarat-Syarat Khusus Asuransi Tambahan Kecelakaan Diri.");
			manfaat.add(3, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(5));
			m.put("MANFAAT", "Apabila Tertanggung meninggal dunia bukan karena Kecelakaan dalam masa Asuransi, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 150% (seratus lima puluh perseratus) Uang Pertanggungan dan sejak itu Asuransi berakhir.");
			manfaat.add(4, m);				
		}
		
		//Bagian II (Nilai Tunai)
		List manfaatNilaiTunai = this.nilaiTunai.proses(spaj, lus_id, "cetak", 0, 0);
		params.put("sdsManfaat", JasperReportsUtils.convertReportData(manfaat));
		params.put("sdsNT", JasperReportsUtils.convertReportData(manfaatNilaiTunai));
		params.put("subReportDatakeys", new String[] {"sdsManfaat", "sdsNT"});
		
		
		return params;
	}
	
	private Map prosesManfaat81(int cbid, String lku_id) throws Exception{
		
		Map params = new HashMap();
		params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case79"));

		List manfaat = new ArrayList();
		Map m;
		
		//Bagian I (header manfaat & manfaat)
		params.put("header", "MANFAAT ASURANSI \"MAXI CARE\"");
		
		m = new HashMap();
		m.put("LSMAN_LINE", new Integer(1));
		m.put("MANFAAT", "Apabila Tertanggung meninggal dunia karena Kecelakaan dalam masa asuransi dan Asuransi Kecelakaan Diri masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 200% Uang Pertanggungan dan sejak itu asuransi berakhir. Ketentuan lebih lanjut tentang Asuransi Kecelakaan Diri tertera pada Syarat-Syarat Khusus Asuransi Tambahan Kecelakaan Diri.");
		manfaat.add(0, m);				
		m = new HashMap();
		m.put("LSMAN_LINE", new Integer(2));
		m.put("MANFAAT", "Apabila Tertanggung meninggal dunia bukan karena Kecelakaan dalam masa asuransi dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% Uang Pertanggungan dan  sejak itu asuransi berakhir");
		manfaat.add(1, m);				
		m = new HashMap();
		m.put("LSMAN_LINE", new Integer(3));
		m.put("MANFAAT", "Apabila Tertanggung didiagnosa menderita salah satu Penyakit Kritis dalam masa asuransi dan Asuransi Tambahan Penyakit Kritis masih berlaku, maka akan dibayarkan Manfaat Asuransi sebesar 50% Uang Pertanggungan. Ketentuan lebih lanjut tentang Penyakit Kritis tertera pada Syarat-Syarat Khusus Asuransi Tambahan Penyakit Kritis.");
		manfaat.add(2, m);				
		int hitung = 4;
		if(cbid!=0) {
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(hitung++));
			m.put("MANFAAT", "Apabila Tertanggung menderita Cacat Tetap Total dalam masa pembayaran premi dan Asuransi Tambahan Bebas Premi masih berlaku, maka Tertanggung dibebaskan dari pembayaran Premi lanjutan. Ketentuan lebih lanjut tentang Asuransi Bebas Premi tertera pada Syarat-Syarat Khusus Asuransi Tambahan Bebas Premi.");
			manfaat.add(3, m);
		}
		m = new HashMap();
		m.put("LSMAN_LINE", new Integer(hitung++));
		m.put("MANFAAT", "'Apabila Tertanggung menjalani Rawat Inap dalam masa asuransi, dan Asuransi Tambahan Rawat Inap masih berlaku, maka akan dibayarkan Manfaat Santunan Harian Rawat Inap sebesar " + (lku_id.equals("01")?"'Rp. 1.000.000":"US$ 200") + "/hari selama Tertanggung dirawat, dengan jangka waktu maksimum 90 (sembilan puluh) hari perawatan untuk satu tahun polis dan 300 (tiga ratus) hari perawatan selama masa asuransi. Ketentuan lebih lanjut tentang Rawat Inap tertera pada Syarat-Syarat Khusus Asuransi Tambahan Rawat Inap.'");
		manfaat.add(4, m);				
		m = new HashMap();
		m.put("LSMAN_LINE", new Integer(hitung++));
		m.put("MANFAAT", "Apabila tidak terjadi klaim selama masa asuransi, maka pada akhir tahun Polis kesepuluh akan dikembalikan seluruh premi yang telah dibayarkan, dan sejak saat itu asuransi berakhir.");
		manfaat.add(5, m);				
		
		params.put("sdsManfaat", JasperReportsUtils.convertReportData(manfaat));
		params.put("subReportDatakeys", new String[] {"sdsManfaat"});
		
		
		return params;
	}

	public NilaiTunai getNilaiTunai() {
		return nilaiTunai;
	}
	
	private Map prosesManfaat100(String businessId, int lsdbs) throws Exception{
		
		Map params = new HashMap();
		params.put("reportPath", "/WEB-INF/classes/" + props.get("report.manfaat.case100"));

		List manfaat = new ArrayList();
		Map m;
		
		if("217".equals(businessId)){ // SMILE LINK PRO 100
			
			if(lsdbs == 2){ // manfaat Erbe
					//Bagian I (header manfaat & manfaat)
					params.put("header", "MANFAAT ASURANSI \"SMILe LINK PRO 100\"");
					params.put("catatan", "MANFAAT ASURANSI diberikan berdasarkan persyaratan sesuai dengan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
					
					m = new HashMap();
					m.put("LSMAN_LINE", new Integer(1));
					m.put("MANFAAT", "Apabila Tertanggung meninggal dunia dalam Masa Asuransi dan Asuransi masih berlaku maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% Uang Pertanggungan  dan ditambah dengan Nilai Polis (bila ada) dan dikurangi biaya-biaya yang belum dibayar (bila ada) dan selanjutnya Pertanggungan berakhir.");
					manfaat.add(0, m);				
					m = new HashMap();
					m.put("LSMAN_LINE", new Integer(2));
					m.put("MANFAAT", "Apabila Tertanggung hidup pada akhir Masa Asuransi dan Asuransi masih berlaku, maka kepada Pemegang Polis akan dibayarkan Nilai Polis dan dikurangi biaya-biaya yang belum dibayar (bila ada) dan selanjutnya Pertanggungan berakhir.Perhitungan Nilai Polis pada akhir Masa Asuransi sama dengan perhitungan Polis batal, dengan menganggap Polis akhirkontrak sama dengan Polis batal.");
					manfaat.add(1, m);				
					m = new HashMap();
					m.put("LSMAN_LINE", new Integer(3));
					m.put("MANFAAT", "Jika Pemegang Polis dan atau Tertanggung mengikuti Asuransi Tambahan (Rider), maka Manfaat Asuransi Tambahan (Rider) seperti yang tertera pada Syarat-syarat Khusus masing-masing Asuransi Tambahan (Rider) tersebut.");
					manfaat.add(2, m);	
			}else{
				//Bagian I (header manfaat & manfaat)
					params.put("header", "MANFAAT ASURANSI \"SMILe LINK PRO 100\"");
					params.put("catatan", "Penanggung tidak menjamin hasil dari investasi Pemegang Polis. Semua resiko, kerugian dan manfaat yang dihasilkan dari investasi akan sepenuhnya menjadi tanggung jawab Pemegang Polis.");
					
					m = new HashMap();
					m.put("LSMAN_LINE", new Integer(1));
					m.put("MANFAAT", "Apabila Tertanggung meninggal dunia dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% Uang Pertanggungan ditambah Nilai Polis dan selanjutnya pertanggungan berakhir.");
					manfaat.add(0, m);				
					m = new HashMap();
					m.put("LSMAN_LINE", new Integer(2));
					m.put("MANFAAT", "Apabila Tertanggung mencapai usia 100 tahun dan Tertanggung masih hidup, maka kepada Pemegang Polis akan dibayarkan Manfaat asuransi sebesar Nilai Polis dan selanjutnya pertanggungan berakhir.");
					manfaat.add(1, m);	
			}
		}else if("218".equals(businessId)){ // SMILE LINK PRO 100 SYARIAH
			params.put("header", "MANFAAT ASURANSI \"SMILe LINK PRO 100 SYARIAH\"");
			params.put("catatan", "Wakil Para Peserta tidak menjamin hasil dari investasi Pemegang Polis. Semua resiko, kerugian dan manfaat yang dihasilkan dari investasi akan sepenuhnya menjadi tanggung jawab Pemegang Polis.");
			
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Peserta meninggal dunia dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% Uang Pertanggungan ditambah Nilai Polis dan selanjutnya Asuransi Syariah berakhir.");
			manfaat.add(0, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(2));
			m.put("MANFAAT", "Apabila Peserta mencapai usia 100 tahun dan Peserta masih hidup, maka kepada Pemegang Polis akan dibayarkan Manfaat asuransi sebesar Nilai Polis dan selanjutnya Asuransi Syariah berakhir.");
			manfaat.add(1, m);	
			
		}else if("215".equals(businessId)){ // uraian manfaat untuk 215 dengan lsdbs number 2 (SMiLe LINK PROASSET SYARIAH) 
			if(lsdbs== 1){
				params.put("header", "MANFAAT ASURANSI \"SIMAS PRIME LINK SYARIAH\"");
			}else if(lsdbs== 2){
				params.put("header", "MANFAAT ASURANSI \"SMiLe LINK PROASSET SYARIAH\"");
			}else if(lsdbs== 3){
				params.put("header", "MANFAAT ASURANSI \"SMiLe PREMIUM LINK SYARIAH\"");
			}else if(lsdbs== 4){
				params.put("header", "MANFAAT ASURANSI \"B SMiLe INSURANCE SYARIAH\"");
			}else{
				params.put("header", "MANFAAT ASURANSI \"SYARIAH\"");
			}

			params.put("catatan", "Wakil Para Peserta tidak menjamin hasil dari investasi Pemegang Polis. Semua resiko, kerugian dan manfaat yang dihasilkan dari investasi akan sepenuhnya menjadi tanggung jawab Pemegang Polis.");

			if(lsdbs== 3){
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(1));
				m.put("MANFAAT", "Apabila Peserta meninggal dunia dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% Manfaat Asuransi Syariah ditambah Nilai Polis dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(0, m);	
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(2));
				m.put("MANFAAT", "Apabila Peserta meninggal dunia yang diakibatkan oleh kecelakaan dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan diberikan tambahan 100% Manfaat Asuransi Syariah dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(1, m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(3));
				m.put("MANFAAT", "Apabila Peserta mencapai usia 100 tahun dan Peserta masih hidup, maka kepada Pemegang Polis akan dibayarkan Manfaat asuransi sebesar Nilai Polis dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(2, m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(4));
				m.put("MANFAAT", "Jika Peserta mengambil tambahan Rider Asuransi, manfaat tambahan mengikuti ketentuan yang berlaku sesuai rider yang diambil.");
				manfaat.add(3, m);
			}else if(lsdbs== 4){
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(1));
				m.put("MANFAAT", "Apabila Peserta meninggal dunia dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% Manfaat Asuransi Syariah ditambah Nilai Polis dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(0, m);	
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(2));
				m.put("MANFAAT", "Apabila Peserta meninggal dunia yang diakibatkan oleh kecelakaan dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan diberikan tambahan 100% Manfaat Asuransi Syariah dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(1, m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(3));
				m.put("MANFAAT", "Apabila Peserta mencapai usia 100 tahun dan Peserta masih hidup, maka kepada Pemegang Polis akan dibayarkan Manfaat asuransi sebesar Nilai Polis dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(2, m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(4));
				m.put("MANFAAT", "Jika Peserta mengambil tambahan rider, manfaat tambahan mengikuti ketentuan yang berlaku sesuai rider yang diambil.");
				manfaat.add(3, m);
			}else {
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(1));
				m.put("MANFAAT", "Apabila Peserta meninggal dunia dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% Uang Pertanggungan ditambah Nilai Polis dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(0, m);	
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(2));
				m.put("MANFAAT", "Apabila Peserta meninggal dunia yang diakibatkan oleh kecelakaan dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan diberikan tambahan 100% Uang Pertanggungan dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(1, m);
				m = new HashMap();
				m.put("LSMAN_LINE", new Integer(3));
				m.put("MANFAAT", "Apabila Peserta mencapai usia 100 tahun dan Peserta masih hidup, maka kepada Pemegang Polis akan dibayarkan Manfaat asuransi sebesar Nilai Polis dan selanjutnya Asuransi Syariah berakhir.");
				manfaat.add(2, m);
			}


		}else if("213".equals(businessId)){ // SIMAS MAGNA LINK.
		//Bagian I (header manfaat & manfaat)
			params.put("header", "MANFAAT ASURANSI \"SIMAS MAGNA LINK\"");
			params.put("catatan", "Penanggung tidak menjamin hasil dari investasi Pemegang Polis. Semua resiko, kerugian dan manfaat yang dihasilkan dari investasi akan sepenuhnya menjadi tanggung jawab Pemegang Polis.");
			
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Tertanggung meninggal dunia dan asuransi masih berlaku, maka kepada Yang Ditunjuk akan dibayarkan Manfaat Asuransi sebesar 100% Uang Pertanggungan ditambah Nilai Polis dan selanjutnya pertanggungan berakhir.");
			manfaat.add(0, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(2));
			m.put("MANFAAT", "Apabila Tertanggung mencapai usia 100 tahun dan Tertanggung masih hidup, maka kepada Pemegang Polis akan dibayarkan Manfaat asuransi sebesar Nilai Polis dan selanjutnya pertanggungan berakhir.");
			manfaat.add(1, m);	
			
		}else if("221".equals(businessId)){ // HCP+NCB
			params.put("header", "MANFAAT ASURANSI \"SMiLe HOSPITAL PLAN\"");
			params.put("catatan", "MANFAAT ASURANSI diberikan berdasarkan SYARAT-SYARAT UMUM POLIS, SYARAT-SYARAT KHUSUS POLIS  dan KETENTUAN LAINNYA apabila ada, yang semuanya dilampirkan pada POLIS serta menjadi bagian yang saling melengkapi dan tidak dapat dipisahkan dengan POLIS ini.");
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(1));
			m.put("MANFAAT", "Apabila Tertanggung menjalani Rawat Inap di Rumah Sakit baik karena sakit maupun Kecelakaan pada saat asuransi masih berlaku, maka Penanggung akan memberikan Santuan Harian Rawat Inap berdasarkan jenis Santunan Harian Rawat Inap selama dirawat di Rumah Sakit sebesar yang telah ditetapkan dalam Polis dengan janga waktu maksimum 180 (seratus delapan puluh) hari perawatan untuk satu tahun Polis.");
			manfaat.add(0, m);				
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(2));
			m.put("MANFAAT", "Apabila Tertanggung mempunyai lebih dari satu Polis yang diterbatkan oleh Penanggung yang juga memberikan manfaat santunan harian Rawat Inap, maka besarnya santunan harian Rawat Inap yang dapat dibayarkan oleh Penanggung untuk Tertanggung yang sama ditetapkan tidak melebihi Rp.2.000.000,- (dua juta Rupiah) per hari atau mata uang yang setara dengan Rp.2.000.000,- (dua juta Rupiah) per hari per Rawat Inap.");
			manfaat.add(1, m);	
			m = new HashMap();
			m.put("LSMAN_LINE", new Integer(3));
			m.put("MANFAAT", "Jika Tertanggung selama 2 (dua) tahun pertanggungan tidak mengajukan klaim maka akan mendapatkan bonus sebesar 50% dari Premi yang dibayarkan pada tahun berjalan.");
			manfaat.add(2, m);	
		}
		
		params.put("sdsManfaat", JasperReportsUtils.convertReportData(manfaat));
		params.put("subReportDatakeys", new String[] {"sdsManfaat"});
		
		return params;
	}

}
