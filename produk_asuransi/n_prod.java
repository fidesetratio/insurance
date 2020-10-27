// n_prod
package produk_asuransi;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.f_check_end_aktif;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/* Created on Jul 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod implements Serializable{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private static final long serialVersionUID = 1L;
	//Yusuf - 20050313/////////////////////////////////////////////////////////////////////////////////////////
	protected SqlMapClient sqlMap; 

	public SqlMapClient getSqlMap() {return sqlMap;}

	public void setSqlMap(SqlMapClient sqlMap) {this.sqlMap = sqlMap;}
	
	class Query{
		private void initSqlMap() {
			if(sqlMap == null ) {
				try {
					Reader reader = Resources.getResourceAsReader("sql-map-config.xml");		
					sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
				}catch(IOException ioe) {
					logger.error(ioe);
				}
			}
		}

		public Integer selectFlagPowersave(int lsbs, int lsdbs, int flag_bulanan) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("lsbs", new Integer(lsbs));
			params.put("lsdbs", new Integer(lsdbs));
			params.put("flag_bulanan", flag_bulanan);
			Integer result = (Integer) sqlMap.queryForObject("elions.n_prod.selectFlagPowersave", params); 
			if(result == null) {
				return -1; 
			}else {
				return result;
			}
		}
		
		public Integer selectLamaBayar(int lsbs, int lsdbs) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("lsbs", new Integer(lsbs));
			params.put("lsdbs", new Integer(lsdbs));
			return (Integer) sqlMap.queryForObject("elions.n_prod.selectLamaBayar", params);
		}
		public Double selectNilai(int jenis, int lsbs, String lku, int lscb, int lamaBayar, int lamaTanggung, int tahunKe, int umur) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("jenis", new Integer(jenis));
			params.put("lsbs", new Integer(lsbs));
			params.put("lku", lku);
			params.put("lscb", new Integer(lscb));
			params.put("lamaBayar", new Integer(lamaBayar));
			params.put("lamaTanggung", new Integer(lamaTanggung));
			params.put("tahunKe", new Integer(tahunKe));
			params.put("umur", new Integer(umur));
			params.put("sex", ii_sex_tt_coi);
			return (Double) sqlMap.queryForObject("elions.n_prod.selectNilai", params);
		}
		
		public Double selectNilaiNew(int jenis, int lsbs, int lsdbs, String lku, int lscb, int lamaBayar, int lamaTanggung, int tahunKe, int umur) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("jenis", new Integer(jenis));
			params.put("lsbs", new Integer(lsbs));
			params.put("lsdbs", new Integer(lsdbs));
			params.put("lku", lku);
			params.put("lscb", new Integer(lscb));
			params.put("lamaBayar", new Integer(lamaBayar));
			params.put("lamaTanggung", new Integer(lamaTanggung));
			params.put("tahunKe", new Integer(tahunKe));
			params.put("umur", new Integer(umur));
			params.put("sex", ii_sex_tt_coi);
			return (Double) sqlMap.queryForObject("elions.n_prod.selectNilaiNew", params);
		}		
		
		public Double selectNilaiFromLstNilai(int jenis, int lsbs, int lsdbs, String lku, int lscb, int umur) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("jenis", new Integer(jenis));
			params.put("lsbs", new Integer(lsbs));
			params.put("lsdbs", new Integer(lsdbs));
			params.put("lku", lku);
			params.put("lscb", new Integer(lscb));
			params.put("umur", new Integer(umur));
			return (Double) sqlMap.queryForObject("elions.n_prod.selectNilaiFromLstNilai", params);
		}
		
		public Double selectDefault(int def_id) throws SQLException {
			initSqlMap();
			return (Double) sqlMap.queryForObject("elions.n_prod.selectDefault", new Integer(def_id));
		}
		public Integer selectJenisBiaya(int lsbs, int lsdbs) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("lsbs", new Integer(lsbs));
			params.put("lsdbs", new Integer(lsdbs));
			return (Integer) sqlMap.queryForObject("elions.n_prod.selectJenisBiaya", params);
		}
		public Double selectPremiSuperSehat(int lsbs, int lsdbs, int umur, String lku) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("lsbs", new Integer(lsbs));
			params.put("lsdbs", new Integer(lsdbs));
			params.put("umur", new Integer(umur));
			params.put("lku", lku);
			return (Double) sqlMap.queryForObject("elions.n_prod.selectPremiSuperSehat", params);			
		}
		public Double selectRateRider(String lku, int umurTertanggung, int umurPemegang, int lsbs, int jenis) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("lku", lku);
			params.put("umurTertanggung", new Integer(umurTertanggung));
			params.put("umurPemegang", new Integer(umurPemegang));
			params.put("lsbs", new Integer(lsbs));
			params.put("jenis", new Integer(jenis));
			Double result = (Double) sqlMap.queryForObject("elions.n_prod.selectRateRider", params);
			if(result == null) {
				throw new RuntimeException("RATE RIDER TIDAK ADA : " + lsbs);
			}
			return result; 
		}
		
		public Double selectRateUpScholarship(int lsbs_id,int umurTertanggung, int lsbs_number) throws SQLException {
			initSqlMap();
			Map params = new HashMap();	
			params.put("lsbs_id", new Integer(lsbs_id));
			params.put("age", new Integer(umurTertanggung));
			params.put("lsbs_number", new Integer(lsbs_number));
			Double result = (Double) sqlMap.queryForObject("elions.n_prod.selectRateUpScholarship", params);
			if(result == null) {
				throw new RuntimeException("RATE UP RIDER TIDAK ADA : " + lsbs_number);
			}
			return result; 
		}
		
		public Double selectResultPremi(int ljb_id, String reg_spaj) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("ljb_id", new Integer(ljb_id));
			params.put("reg_spaj", reg_spaj);
			Double resultPremi = (Double) sqlMap.queryForObject("elions.n_prod.selectResultPremi", params);
//			if(resultPremi == null) {
//				throw new RuntimeException("Premi CI null : " + reg_spaj);
//			}
			return resultPremi; 
		}
		
		public String selectgetnamaplan(int kode_bisnis, int nomor_bisnis) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("kode_bisnis", new Integer(kode_bisnis));
			params.put("nomor_bisnis", new Integer(nomor_bisnis));
			return (String) sqlMap.queryForObject("elions.n_prod.selectgetnamaplan", params);
		}
		
		public Double select_rate_endow(Integer kode_produk, Integer number_produk, Date tgl) throws SQLException {
			initSqlMap();	
			Map params = new HashMap();
				params.put("kode_produk",kode_produk);
				params.put("number_produk",number_produk);
				params.put("tgl",tgl);
				return (Double) sqlMap.queryForObject("elions.n_prod.select_rate_endow",params);
			}
		
		public Double selectlst_premi_43(int kode_bisnis, int sex, int age) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("kode_bisnis", new Integer(kode_bisnis));
			params.put("sex", new Integer(sex));
			params.put("age", new Integer(age));
			return (Double) sqlMap.queryForObject("elions.n_prod.selectlst_premi_43", params);
		}
		
		public Integer faktorexcell80(int usia,int li_bisnis) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("li_bisnis", Integer.toString(li_bisnis));
			params.put("usia", new Integer(usia));
			return (Integer) sqlMap.queryForObject("elions.n_prod.faktorexcell80", params);
		}
		
		public Double select_biaya_akuisisi(int kode_produk,int number_produk,int cara_bayar,int ke,int period) throws SQLException {
			initSqlMap();
			Map params = new HashMap();
			params.put("kode_produk", new Integer(kode_produk));
			params.put("number_produk", new Integer(number_produk));
			params.put("cara_bayar", new Integer(cara_bayar));
			params.put("ke", new Integer(ke));
			params.put("period", new Integer(period));
			return (Double) sqlMap.queryForObject("elions.n_prod.select_biaya_akuisisi", params);
		}
	}

	Query query = new Query();
	//Yusuf - 20050313/////////////////////////////////////////////////////////////////////////////////////////
	
	public  int ii_usia_tt =0;
	public  int ii_usia_pp=0;
	public  int ii_age_from=0;
	public  int ii_age_to=0;//batas usia masuk
	public  int ii_age=0;
	public  Calendar idt_beg_date  = Calendar.getInstance();
	public  Calendar idt_end_date  = Calendar.getInstance();
	public  Calendar adt_bdate = Calendar.getInstance();
	public  Calendar ldt_end = Calendar.getInstance();
	public  double idec_up=0;
	public  double idec_kurs = 0;
	public  double idec_disc = 0;
	public  int ii_end_from=0; // utk hit end_date 
	public  double idec_min_up01=0;
	public  double idec_min_up02=0;
	public  double idec_max_up01=0;	
	public  double idec_max_up02=0;	
	public  double idec_min_up01_karyawan=0;
	public  double idec_min_up02_karyawan=0;
	public  double idec_kelipatan_up01=1;	
	public  double idec_kelipatan_up02=1;		
	public  String is_kurs_id="";
	public  String is_agen_id="";
	public  double[] idec_pct_list;
	public  int ii_bisnis_id=0;
	public int ii_bisnis_no=0;// bisnis_id = 1..99, bisnis_no 1.1, 40.1
	public  int ii_class=0;
	public int lsr_jenis = 1; //lsr_jenis untuk menarik data rate ke lst_rider, default = 1
	
	public  int[] ii_lama_bayar;
	public  double[] idec_list_premi;  //buat list premi kalo pilih premi
	public  int[] li_pmode;
	public  int[] li_pmode_list;
	public  int ii_contract_period=0;
	public  int li_cp=0;
	public  int ii_bisnis_id_utama=0;
	public  int ii_bisnis_no_utama=0;
	public  int ii_group_id = 0;
	public  boolean ib_single_premium = false;//utk cek; plan ada single premium / tidak
	public  double idec_premi=0;
	public  double idec_rate=0;
	public  int ii_lbayar = 0;
	public  int ii_pmode=0;
	public  int ii_jenis = 1;
	public  int ii_tahun_ke=1;
	public  double ii_permil=1000;
	public  double idec_add_pct=0;
	public  double idec_premi_main=0;
	public  double ldec_rate=0;
	public  double ldec_rate_include=0;
	public  boolean ib_flag_pp = false; //flag utk cek umur ambil dr tt/pp
	public  int[] ii_pmode_list;
	public  int ii_medis = 0;
	public  int ii_sex_tt = 1;
	public  boolean ib_flag_end_age = true; //utk cek; end_date - ii_end_age;
	public  int ii_end_age=0; //79 - issue age
	public  int ii_pay_period =0;
	public  int ii_comm_period=0;
	public  int ii_sex_pp = 1;
	public  String[] is_forex;
	public  String[] lds_1;
	public  int[] li_usia;
	public  int indeks_is_forex=0;
	public  int indeks_idec_pct_list=0;
	public  int indeks_ii_lama_bayar=0;
	public  int indeks_lds_1=0;
	public  int indeks_ii_pmode_list=0;
	public  int indeks_idec_list_premi=0;
	public  int indeks_li_pmode=0;
	public  int indeks_li_pmode_list=0;
	public  int li_jenis=0;
	public  int li_lama=0;
	public  int li_lbayar=0;
	public  int li_umur=0;
	public  long ll_premi=0;
	public  double ldec_up=0;
	public  double ldec_sisa=0;
	public  double ldec_premi_tahunan=0;
	public  int li_ltanggung=0;
	public  String hsl="";
	public  String err="";
	public  int flag_uppremi=0; // isian 0 up, 1 premi, 2 up dan premi
	public  int umur_tahun=0;
	public  int umur_bulan=0;
	public  int umur_hari=0;
	public int samePPTT=0; // 0 PP !=TT, 1 PP == TT
	
	public int simas = 0; // 0 bukan simas dan ttg hanya 1, 1 simas dan ttg lebih dari 1
	
	/*
	 * 0 - tutup (bukan link)
	 * 1 - Power save
	 * 2 - buka fixed & dyna
	 * 3 - buka aggresive
	 * 4 - buka fixed & dyna & aggresive
	 * 5 - buka secured & dyna dollar
	 * 6 - buka syariah fixed & dyanamic syariah 
	 * 7 - buka fixed & dyna & aggresive Syariah
	 * 8 - buka secured & dyna dollar Syariah
	 * 9 - buka artha fixed , dyna , aggressive
	 * 10 buka artha secure dan dynamic
	 * 11 buka stable fund rupiah atau stable fund dollar
	 * 12 buka BPPI Plus Fund-1
	 * 13 buka dyna & aggresive Syariah
	 * 14 muamalat - Mabrur
	 * 15 buka stable fund syariah rupiah dan stable fund syariah dollar
	 * 16 buka stable fund 2 rupiah 
	*/
	public  int kode_flag=0;
	
	public  int flag_class=0;// 0 tidak buka kotak isian klas, 1 buka kotak isian klas
	public  double rate_rider=0;
	
	public  int li_sd = 60;
	public  int li_insured=0;
	public  Date tanggal_sementara=null;
	public  Date tanggal_sementara1=null;
	public  Calendar ldt_edate = Calendar.getInstance();
	public  Calendar ldt_epay  = Calendar.getInstance();
	public  int li_kali = 1;	
	
	//0==> biasa 1==> tidak bisa untuk cara bayar sekaligus
	//public  int flag_cara_bayar=0;
	
	public  double up_pa=0;
	public  double up_pb=0;
	public  double up_pc=0;
	public  double up_pd=0;
	public  double up_pm=0;
	
	//rider include
	public  int indeks_rider_list=0;
	public  int[] ii_rider;
	
	// 0==> rider lama 1==> rider baru
	public  int flag_rider=0;
	
	public  int iiunit=0;//set nilai default unit
	public  int iiclass=0;//set nilai default class
	public  int li_insper = 18;
	public  int li_id = 18; 

	//biaya ulink
	public double mbu_jumlah=0;
	public double mbu_jumlah1=0;
	public double mbu_persen1=0;
	public double mbu_jumlah2=0;
	public double mbu_persen2=0;
	public double mbu_jumlah3=0;
	public double mbu_persen3=0;
	public double mbu_persen=0;
	public double mbu_rate=0;
	public double mbu_jumlah4=0;
	public double mbu_persen4=0;
	
	public double ldec_temp1=0;
	public double ldec_temp2=0;
	public double ldec_temp3=0;
	public double ldec_temp4=0;
	public double ldec_temp5=0;
	public double ldec_temp6=0;
	public double ldec_temp7=0;
	public double ldec_temp8=0;
	public double ldec_temp9=0;	
	
	public double ldec_rate1=0;
	public double ldec_rate2=0;
	public double ldec_rate3=0;
	public double ldec_rate4=0;
	public double ldec_rate5=0;
	public double ldec_rate6=0;
	public double ldec_rate7=0;
	public double ldec_rate8=0;
	public double ldec_rate9=0;	
	
	public int  nomor_rider1=0;
	public int  nomor_rider2=0;
	public int  nomor_rider3=0;
	public int  nomor_rider4=0;
	public int  nomor_rider5=0;
	public int  nomor_rider6=0;
	public int  nomor_rider7=0;
	public int  nomor_rider8=0;
	public int  nomor_rider9=0;
	
	public int unit_rider1=0;
	public int unit_rider2=0;
	public int unit_rider3=0;
	public int unit_rider4=0;
	public int unit_rider5=0;
	public int unit_rider6=0;	
	public int unit_rider7=0;
	public int unit_rider8=0;
	public int unit_rider9=0;
	
	public int class_rider1=0;
	public int class_rider2=0;
	public int class_rider3=0;
	public int class_rider4=0;
	public int class_rider5=0;
	public int class_rider6=0;
	public int class_rider7=0;
	public int class_rider8=0;
	public int class_rider9=0;	

	public  int[] rider_include;
	public  int indeks_rider_include = 0;
	
	//0 ==> umum 1 ==>waiver critical 2 ==> tpd supaya tidak boleh ada ambil yang sama CI dan TPD
	public  int flag_rider_baru=0;
	
	public  int rider_unit=0;
	public  int rider_class=0;
	
	public int flag_jenis_plan=0;
	//0 ==> plan biasa 1==> plan excellink 18 2 ==> plan cerdas 3 ==>plan fast excellink  
	//4 ==>plan  super protection 5 ==>pa stand alone 6==>arthalink 7==>stable link 8 ==> investimax
	//9 ==> plan hidup bahagia
	public int flag_excell80plus =0;
	public int flag_as=0; 
	//0 ==> Plan regional 1  ==> Plan AS 2 ==> Plan Karyawan 3 ==> regional dan as
	public int flag_artha = 0;
	public int flag_cerdas_global = 0; //0 ==>bukan jenis cerdas global, 1 = jenis cerdas global
	
	public int nomor_rider_include=0;
	public Date mspr_beg_date=null;
	public int klases=0;
	public int units=0;	
	public int flag_cerdas_siswa=0; //0 bukan cerdas siswa  1 cerdas siswa (127)
	public int flag_account =2;// 0 untuk umum  1 untuk account recur 2 untuk rek client 3 untuk account recur dan rek client//permintaan bas dan nb semua produk harus input data rek client
	public int flag_uppremiopen=0; // untuk biasa, 1 untuk excellink 80plus
	public int flag_debet = 0;//0 tidak harus autodebet , 1 harus autodebet
	public int flag_default_up =0; //0 tidak default, 1 default UP(ambil dari idec_min_up atau idec_max_up)
	
	public int flag_warning_autodebet = 0;//1 seharusnya autodebet, tapi tidak diblokir bila pilihannya TUNAI, hanya diberikan warning

	public boolean isBungaSimponi=false;// Produk yang input Bunga Simponi (Produk Simponi Rupiah & Produk Simponi Dollar)
	public boolean isBonusTahapan=false;// Produk yang input Bonus Tahapan (Produk Premium Saver & simponi 8 )
	public int flag_reff_bii = 0;
	//Yusuf - 20050203
	public boolean isProductBancass = false; //Produk yang termasuk BancAss, flag digunakan di : (Entry BAC - Reff BII, Transfer - Komisi Reff BII)  
	public boolean isInvestasi = false; //Produk yang termasuk produk investasi / unitlink (beda dengan powersave)
	public int flag_biaya_tambahan = 0; // 0 --> tanpa biaya tambahan untuk menutupi minus investasi, 1 ada tambahan biaya top up untuk menutupi minus
	
	public int li_pct_biaya = 5;
	public int li_trans = 5;
	public int flag_worksite = 0;
	public int usia_nol = 1; // 1--> produk yang umur 0 dianggap 1 
	public int flag_powersave = 0; // lihat fungsi f_flag_rate_powersave
	public int flag_platinumlink = 0; // 0 --> bukan platinum link 1 --> platinum link
	public int flag_cuti_premi = 0; // 0 -- >tidak ada cuti premi 1 --> cuti premi
	public int flag_endowment = 0; // -> bukan endowment 20   1 --> endowment 20
	public int flag_mediplan = 0; // --> 0 bukan mediplan 1 -- mediplan
	public int flag_powersavebulanan = 0; // 0 bukan power save bulanan 1 power save bulanan
	public int flag_medivest = 0;// 0 bukan medivest 1 medivest
	public int indeks_discount = 0; // khusus endowment
	public  double[] discount;
	public int indeks_perusahaan =0;
	public String[] perusahaan;
	public int indeks_mgi=0;
	public int[] mgi;
	public int flag_ekalink = 0; // 0--> bukan ekalink 1 --> ekalink
	public int flag_horison = 0;//0 bukan horison 1 horison
	public int indeks_kombinasi =0;
	public String[] kombinasi;
	
	public  int ii_sex_tt_coi = 9; /** default sex = 9, 0 = wanita, 1 = pria, COI **/
	
	NumberFormat f = new DecimalFormat("#,##0.00;(#,##0.00)");
	public n_prod()
	{
		ii_usia_tt=0;	
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.09; // pmode 6
		is_kurs_id = "01";
		idec_min_up01 = 10000000;
		idec_min_up02 = 6000;
		
		idec_max_up01 = 1e12;
		idec_max_up02 = 1e12;
		
		indeks_ii_lama_bayar=6;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 10;
		ii_lama_bayar[4] = 15;
		ii_lama_bayar[5] = 20;
		
		indeks_ii_pmode_list=14;
		ii_pmode_list = new int[indeks_ii_pmode_list];
	
//		untuk hitung end date, mis :( 99 - umur )
	   ii_end_from = 99;  //whole life
//	   ib_flag_end_from buat perhitungan end date, apakah sampai umur tertentu mis 79
	   idec_up = idec_min_up01;
	   
	   indeks_rider_list=6;
	   ii_rider = new int[indeks_rider_list];
	   ii_rider[0]=800;
	   ii_rider[1]=801;
	   ii_rider[2]=802;
	   ii_rider[3]=803;
	   ii_rider[4]=804;
	   ii_rider[5]=822;
	   
	   indeks_discount = 0;
	   discount = new double[indeks_discount];
	   
	   indeks_perusahaan =0;
	   perusahaan = new String[indeks_perusahaan];
	   
	   indeks_mgi=6;
	   mgi = new int[indeks_mgi];
	   mgi[0]= 1;
	   mgi[1]= 3;
	   mgi[2]= 6;
	   mgi[3]=12;
	   mgi[4]=24;
	   mgi[5]=36;
	   
	   indeks_kombinasi = 20;
	   kombinasi = new String[indeks_kombinasi];
	   kombinasi[0] = "A"; // pp 100% 
	   kombinasi[1] = "B"; // pp 90% - ptb 10%
	   kombinasi[2] = "C"; // pp 80% - ptb 20%
	   kombinasi[3] = "D"; // pp 70% - ptb 30%
	   kombinasi[4] = "E"; // pp 60% - ptb 40%
	   kombinasi[5] = "F"; // pp 50% - ptb 50%
	   kombinasi[6] = "G"; // pp 40% - ptb 60%
	   kombinasi[7] = "H"; // pp 30% - ptb 70%
	   kombinasi[8] = "I"; // pp 20% - ptb 80%
	   kombinasi[9] = "J"; // pp 10% - ptb 90%
	   kombinasi[10] = "T"; // *pp 5%  - ptb 95%
	   kombinasi[11] = "S"; // *pp 15% - ptb 85%		   
	   kombinasi[12] = "R"; // *pp 25% - ptb 75%
	   kombinasi[13] = "Q"; // *pp 35% - ptb 65%
	   kombinasi[14] = "P"; // *pp 45% - ptb 55%
	   kombinasi[15] = "O"; // *pp 55% - ptb 45%
	   kombinasi[16] = "N"; // *pp 65% - ptb 35%
	   kombinasi[17] = "M"; // *pp 75% - ptb 25%
	   kombinasi[18] = "L"; // *pp 85% - ptb 15%
	   kombinasi[19] = "K"; // *pp 95% - ptb 5%
	}
	
	public void f_kombinasi(){
		   indeks_kombinasi = 19;
		   kombinasi = new String[indeks_kombinasi];
		   kombinasi[0] = "A"; // pp 100% 
		   kombinasi[1] = "B"; // pp 90% - ptb 10%
		   kombinasi[2] = "C"; // pp 80% - ptb 20%
		   kombinasi[3] = "D"; // pp 70% - ptb 30%
		   kombinasi[4] = "E"; // pp 60% - ptb 40%
		   kombinasi[5] = "F"; // pp 50% - ptb 50%
		   kombinasi[6] = "G"; // pp 40% - ptb 60%
		   kombinasi[7] = "H"; // pp 30% - ptb 70%
		   kombinasi[8] = "I"; // pp 20% - ptb 80%
		   kombinasi[9] = "J"; // pp 10% - ptb 90%
		   kombinasi[10] = "T"; // *pp 5%  - ptb 95%
		   kombinasi[11] = "S"; // *pp 15% - ptb 85%		   
		   kombinasi[12] = "R"; // *pp 25% - ptb 75%
		   kombinasi[13] = "Q"; // *pp 35% - ptb 65%
		   kombinasi[14] = "P"; // *pp 45% - ptb 55%
		   kombinasi[15] = "O"; // *pp 55% - ptb 45%
		   kombinasi[16] = "N"; // *pp 65% - ptb 35%
		   kombinasi[17] = "L"; // *pp 85% - ptb 15%
		   kombinasi[18] = "K"; // *pp 95% - ptb 5%
	}
	
	
	//Yusuf 3 April 2009
	//Fungsi untuk mendapatkan flag_powersave, semuanya digabung disini
	public int f_flag_rate_powersave(int lsbs, int lsdbs, int flag_bulanan) {
		/*
		Flag Rate Power Save
		0 : Normal; 
		1 : Platinum Save; 
		2 : Simas Prima & Danamas Prima;
		3 : Montly Normal; 
		4 : Monthly Platinum; 
		5 : Monthly Simas Prima & Monthly Danamas Prima; 
		6 : Specta & Prosave; 
		7 : Monthly Specta; 
		8 : Smart; 
		9 : Monthly Smart; 
		10: Bank Mayapada; 
		11: Privilege Save;	
		12: Stable Link Agency;
		13: Stable Link Sinarmas;
		14: Stable Link Mayapada;
		15: Stable Link UOB;
		16: Stable Link BII;
		17: Bung-Maxi (Bumiputera);
		18: DMTM
		19: Stable Save
		20: Monthly Stable Save
		21: Stable Link Syariah
		22: PowerSave Syariah
		23: Monthly PowerSave Syariah
		24: Stable Save BII
		25: Monthly Stable Save BII
		26 : stable save (cicilan)
		27 : stable link worksite
		.. dan seterusnya, dapat dilihat di tabel saja (eka.lst_pwrsave_flag)
		*/
		int result = -1;
		
		try {
			result = query.selectFlagPowersave(lsbs, lsdbs, flag_bulanan);
		} catch (SQLException e) {
			logger.error("ERROR :", e);
		}
		
		return result;
	}
	
//cek usia PA
	public String of_check_pa()
	{
		hsl="";
		if (ii_usia_tt < 16)
		{
			hsl= "Usia Masuk untuk PA minimum : 16 Tahun";
		}

		if (ii_usia_tt > 55)
		{
			hsl="Usia Masuk untuk PA maximum : 55 Tahun";
		}
		return hsl;
	}

//cek usia PC	
	public String of_check_pc()
	{
		hsl="";
		if (ii_usia_pp < 16)
		{
			hsl="Usia Masuk untuk PC minimum : 16 Tahun";
		}

		if (ii_usia_pp > 55)
		{
			hsl="Usia Masuk untuk PC maximum : 55 Tahun";
		}
		return hsl;
	}

//cek usia PK		
	public String of_check_pk()
	{
		hsl="";
		if (ii_usia_tt < 17)
		{
			hsl="Usia Masuk untuk PK minimum : 17 Tahun";
		}

		if (ii_usia_tt > 55)
		{
			hsl="Usia Masuk untuk PK maximum : 55 Tahun";
		}
		return hsl;
	}

//cek usia	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		hsl="";
		if (ii_age < ii_age_from)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}

		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}

		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
		}
		return hsl;		
	}
	
//	cek usia berdasarkan premi	
	public String of_check_usia_case_premi(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, double premi) 
	{
		hsl="";
		if (ii_age < ii_age_from)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}

		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}

		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
		}
		return hsl;		
	}
	
//	cek usia berdasarkan paket	
	public String of_check_usia_case_paket(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int paket) 
	{
		hsl="";
		if (ii_age < ii_age_from)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}

		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}

		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
		}
		return hsl;		
	}

// cek premi
	public boolean of_check_premi(double ad_premi)
	{
		return true;
	}
	
//get usia
	public int of_get_age()
	{
		return ii_age;	
	}

//get beg date
	public Calendar of_get_begdate()
	{
		return idt_beg_date;
	}
	
//get bisnis id
	public int of_get_bisnis_id()
	{
		return ii_bisnis_id;
	}	
	
//get bisnis no
	public int of_get_bisnis_no(int flag_bulanan)
	{
		int flag_powersave=0;	
		return flag_powersave;
	}

//get class
	public int of_get_class()
	{
		return ii_class;	
	}
	
//get period
	public int of_get_conperiod(int number_bisnis)
	{
		hsl="";
		err="";
		li_cp=0;

		switch (ii_bisnis_id)
		{
			case 19 :
				li_cp = ii_lama_bayar[ii_bisnis_no-1];
				break;
			case 20 :
				li_cp = ii_lama_bayar[ii_bisnis_no-1];
				break;
			default :
				if (ii_pmode == 0 && ii_bisnis_id >799)
				{
					ii_contract_period = 5;
					try {
						
						Integer result = query.selectLamaBayar(ii_bisnis_id_utama, ii_bisnis_no_utama);
						
						if(result != null) li_cp = result.intValue();
						else hsl = "Tidak ada data lama bayar";
					
					}catch (Exception e) {
						err=e.toString();
					  } 
				}else{
					li_cp = ii_contract_period	;
				}
		}	
		
		//powersave
		if (kode_flag == 1)
		{
			//20180905-Chandra Konversi Simas prima (stand alone) masa asuransi dari 4 tahun menjadi 1 tahun
			if(ii_bisnis_id == 142 & (number_bisnis == 2 || number_bisnis == 1) ){
				li_cp = 1;
				ii_contract_period = 1;
				ii_end_from = 1;
			}else if(ii_bisnis_id == 142 & number_bisnis == 13){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				li_cp = 3;
				ii_contract_period = 3;
				ii_end_from = 3;
			}else{
				//Yusuf (18 jul 08) - selalu dipatok 4
				li_cp = 4;
				ii_contract_period = 4;
				ii_end_from = 4;
			}		
//			if (ii_age == 67)
//			{
//				li_cp = 3;
//				 ii_contract_period =3 ;
//				 ii_end_from = 3;
//			}else if (ii_age == 68)
//				{
//					li_cp = 2;
//					  = 2;
//					 ii_end_from = 2;
//				}
		}
		return li_cp;	
	}

//	get end date
	  public Calendar of_get_enddate()
	  {
		  return idt_end_date;
	  }


// get kurs
	public void of_get_kurs()
	{
		indeks_is_forex=4;
		is_forex = new String[indeks_is_forex];
		
		is_forex[0]="Rp";
		is_forex[1]="US$";
		is_forex[2]="Sin$";
		is_forex[3]="EUR";

	}

	public double kelipatan()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_kelipatan_up01;
		}else{
			ldec_1 = idec_kelipatan_up02;
		}
		return ldec_1;		
	}
	
	//kelipatan up
	public String cek_kelipatan_up(double up)
	{
		String hsl="";
		double klpt=kelipatan();
		String kelipatan="";
		BigDecimal bd = new BigDecimal(klpt);
		kelipatan=bd.setScale(2,0).toString();
		
		if (up % klpt != 0)
		{
			hsl="Uang Pertanggungan harus kelipatan "+kelipatan;
		}
		return hsl;
	}
	
	//kelipatan premi
	public String cek_kelipatan_premi(double premi)
	{
		String hsl="";
		double klpt=kelipatan();
		String kelipatan="";
		BigDecimal bd = new BigDecimal(klpt);
		kelipatan=bd.setScale(2,0).toString();		
		if (premi % klpt != 0)
		{
			hsl="Premi harus kelipatan "+kelipatan;
		}
		return hsl;
	}	

//get min up
	public double of_get_min_up()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_min_up01;
		}else{
			ldec_1 = idec_min_up02;
		}
		return ldec_1;
	}
	
	public double of_get_min_up_with_topup()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_min_up01;
		}else{
			ldec_1 = idec_min_up02;
		}
		return ldec_1;
	}
	
	public double of_get_min_up_karyawan()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_min_up01_karyawan;
		}else{
			ldec_1 = idec_min_up02_karyawan;
		}
		return ldec_1;
	}
	
	public double of_get_min_up_with_flag_bulanan(Integer flag_bulanan)
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_min_up01;
		}else{
			ldec_1 = idec_min_up02;
		}
		return ldec_1;
	}
	
	public double of_get_max_up()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_max_up01;
		}else{
			ldec_1 = idec_max_up02;
		}
		return ldec_1;
	}	

	public String of_alert_min_up( double up1)
	{
		double min_up=of_get_min_up();
		String hasil_min_up="";
		String min="";
		BigDecimal bd = new BigDecimal(min_up);
		min=bd.setScale(2,0).toString();
		
		if (min_up > up1)
		{
			hasil_min_up=" UP Minimum untuk plan ini : "+ min;	
		}
		return hasil_min_up;
	}
	
	public String of_alert_max_up( double up1)
	{
		double max_up=of_get_max_up();
		String hasil_max_up="";
		String max="";
		BigDecimal bd = new BigDecimal(max_up);
		max=bd.setScale(2,0).toString();
		
		if (max_up < up1)
		{
			hasil_max_up=" UP maximum untuk plan ini : "+ max;	
		}
		return hasil_max_up;
	}	

	public String of_alert_min_premi( double premi)
	{
		
		double min_premi=of_get_min_up();
		String hasil_min_premi="";
		String min="";
		BigDecimal bd = new BigDecimal(min_premi);
		min=bd.setScale(2,0).toString();	
		
		if (min_premi > premi)
		{
			hasil_min_premi=" Premi Minimum untuk plan ini : "+ min;	
		}
		return hasil_min_premi;
	}
	
	public String of_alert_min_premi_with_topup( double premi)
	{
		
		double min_premi=of_get_min_up_with_topup();
		String hasil_min_premi="";
		String min="";
		BigDecimal bd = new BigDecimal(min_premi);
		min=bd.setScale(2,0).toString();	
		
		if (min_premi > premi)
		{
			hasil_min_premi=" Premi Minimum untuk plan ini : "+ min;	
		}
		return hasil_min_premi;
	}
	
	public String of_alert_min_premi_karyawan( double premi)
	{
		
		double min_premi=of_get_min_up_karyawan();
		if(min_premi==0){
			min_premi = of_get_min_up();
		}
		String hasil_min_premi="";
		String min="";
		BigDecimal bd = new BigDecimal(min_premi);
		min=bd.setScale(2,0).toString();	
		
		if (min_premi > premi)
		{
			hasil_min_premi=" Premi Minimum untuk plan ini : "+ min;	
		}
		return hasil_min_premi;
	}
	
	public String of_alert_min_premi_With_flag_bulanan( double premi, Integer flag_bulanan)
	{
		
		double min_premi=of_get_min_up();
		String hasil_min_premi="";
		String min="";
		BigDecimal bd = new BigDecimal(min_premi);
		min=bd.setScale(2,0).toString();	
		
		if (min_premi > premi)
		{
			hasil_min_premi=" Premi Minimum untuk plan ini : "+ min;	
		}
		return hasil_min_premi;
	}
	
	public String of_alert_max_premi( double premi, double up)
	{
		idec_premi = premi;
		double max_premi=of_get_max_up();
		String hasil_max_premi="";
		String max="";
		BigDecimal bd = new BigDecimal(max_premi);
		max=bd.setScale(2,0).toString();		
	
		if (max_premi< premi)
		{
			hasil_max_premi=" Premi maximum untuk plan ini : "+ max;	
		}
		return hasil_max_premi;
	}	

//get pay period
	public int of_get_payperiod()
	{
		li_lama=0;
		if (ib_single_premium)
		{
			li_lama = 1;
		}else{
			li_lama = ii_lama_bayar[ii_bisnis_no-1];
		}
		return li_lama;
	}

// get pay mode
	public void of_get_pmode()
	{
		indeks_lds_1=9;
		lds_1 = new String[indeks_lds_1];
		lds_1[0] = "Sekaligus";
		lds_1[1] = "Triwulan";
		lds_1[2] = "Semesteran";
		lds_1[3] = "Tahunan";
		lds_1[4] = "5 Tahunan";
		lds_1[5] = "6 Tahunan";
		lds_1[6] = "Bulanan";
		lds_1[7] = "Cicilan";
		lds_1[8] = "Kwartalan";
	}
	
// get rate
	public double of_get_rate()
	{
		of_hit_premi();
		return idec_rate;	
	}
	
	public void cek_guthrie(int flag_guthrie)
	{
		if (flag_guthrie == 1)
		{
			flag_uppremi = 1;
		}
	}
	
	public double of_get_rate1(int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		return idec_rate;	
	}	
	
	public String of_check_rider_by_up(double up, int nomor_bisnis){
		return "";
	}
	
	public double of_get_rate2(int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		return idec_rate;	
	}	
	
	public double of_get_rate_vip_hcp(int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp, int prod_utama)
	{
		return idec_rate;	
	}

// get premi
	public double of_get_premi()
	{
		return idec_premi;
	}

// get up
	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{
		return idec_up;
	}

// get usia pp
	public int of_get_usia_pp()
	{
		return ii_usia_pp;
	}

// get usia tt
	public int of_get_usia_tt()
	{
		return ii_usia_tt;
	}
	

	
// hit premi
	public void of_hit_premi()
	{
		hsl="";
		err="";
		String ls_sql;
		li_lbayar=0;
		li_cp = 0;
		ldec_rate=0;

		if (ib_single_premium )
		{
			li_lbayar = 1;
		}else{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			if (ii_bisnis_id >= 800)
			{ 
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;
			}
		}
		switch (ii_bisnis_id)
		{
			case 19 :
				ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
				break;
			case 20 :
				ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
				break;
		}

		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6 )
		{
			li_cp = 3;	
		}

		try {
			Double result = query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no,is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);
			
			if (result==null){
				result =query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no,is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
			}
			
/*
			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
*/
			 if(result != null) {
				ldec_rate = result.doubleValue();
				if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
				{
				//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
					idec_premi = idec_up * ldec_rate / ii_permil;
				}else{
					idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
				}
				idec_rate = ldec_rate;

				if (ii_bisnis_id < 800)
				{
					 idec_premi_main = idec_premi;	
				}					
			 }else{
			 	hsl="Tidak ada data rate";
			 }
		
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 
	}


// hit premi netto
	public double of_hit_premi_netto()
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0 ;
		li_jenis=0;
		ldec_rate=0;

		li_jenis = 8;
		if (ib_single_premium)
		{
			li_lbayar = 1 ;
		}else{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			if (ii_bisnis_id >= 800)
			{
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;
			}
		}

	switch(ii_bisnis_id)
	{
		case 19 :
			ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
			break;	
		case 20 :
			ii_contract_period = ii_lama_bayar[ii_bisnis_no-1];
			break;			
	}
	li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
	if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
	{
		li_cp = 3;
	}
	
	try {

		Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
		
		 if(result != null) {
			ldec_rate = result.doubleValue();
			if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
			{
				//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
				idec_premi = idec_up * ldec_rate / ii_permil;				 
			}else{
				idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
			}
		 }else{	
			hsl="Tidak ada data rate";
			 	
		 }
		}	
		catch (Exception e) {
			  err=(e.toString());
		} 			
		return idec_premi;
	}
	
// set age
	public void of_set_age()
	{
		if (ib_flag_pp)
		{
			ii_age = ii_usia_pp;
		}else{
			ii_age = ii_usia_tt;
		}			
	}
	
// set beg date
	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;
		
		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
	
		
		li_month = ( ii_end_from - ii_end_age ) * 12;
		adt_bdate.set(thn,bln-1,tgl);
		
		idt_beg_date.set(thn,bln-1,tgl);		
		
		ldt_end.set(thn,bln-1,tgl);	
		ldt_end.add(idt_beg_date.MONTH,li_month);

		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		
		if (adt_bdate.DAY_OF_MONTH  == ldt_end.DAY_OF_MONTH )
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);
		}

		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);

	}
	
// set bisnis id
	public void of_set_bisnis_id(int ai_bisnis_id)	
	{
		ii_bisnis_id = ai_bisnis_id;
		if (ii_bisnis_id < 300)
		{
			ii_bisnis_id_utama = ii_bisnis_id;
		}
	}
	
//set bisnis no 
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		int position = ii_bisnis_no-1;
		if(position<0) position=0;
		

		if (ii_bisnis_id < 800){
			ii_bisnis_no_utama = ii_bisnis_no;
			
			if(ii_bisnis_no < 7) ii_lbayar = ii_lama_bayar[position];
			
			of_set_pmode(ii_pmode_list[1]);
		}else{
			 if (ii_bisnis_id == 802 || ii_bisnis_id == 804)//PC or WPD
				{
					ii_age = ii_usia_pp;
					of_set_up(idec_premi_main);	
				}else{
					of_set_age();
					of_set_up(idec_up);
				}
		}

			li_pmode = new int[indeks_li_pmode];
			indeks_li_pmode=indeks_ii_pmode_list;
			li_pmode = new int[indeks_li_pmode];
			
			for (int i =1 ; i<indeks_li_pmode;i++)
			{
				li_pmode[i] = ii_pmode_list[i];
				
			}
	}
	
	
// of_set_class
	public void of_set_class(int ai_class)
	{
		ii_class = ai_class;
	}

// of_set_kurs
	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;
	}
	
// of_set_medis
	public void of_set_medis(int ai_medis)
	{
		ii_medis = ai_medis;
	}
	
// of_set_payperiod
	public void of_set_payperiod(int ai_bis_no)
	{
		ii_bisnis_no = ai_bis_no;
	}
	
// of_set_pmode
	public void of_set_pmode(int ai_pmode)
	{
		ii_pmode = ai_pmode;
		if (ii_pmode == 0){
			ib_single_premium = true ;
		}
		idec_add_pct = idec_pct_list[ii_pmode];
		
	}
	
// of_set_premi
	public void of_set_premi(double adec_premi)
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		ldec_rate=0;
		
		li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		li_cp = ii_pmode;

		try {

			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			   idec_premi = adec_premi;  
			   if (ldec_rate > 0)
			   {
				   idec_up = (idec_premi * ii_permil / ( ldec_rate));	
				   int decimalPlace = 0;
				   BigDecimal bd = new BigDecimal(idec_up);
				   bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				   idec_up = bd.doubleValue();
			   }
			   idec_rate = ldec_rate;
			   of_set_up(idec_up)	;			   
			}else{	
			   hsl="Tidak ada data rate";
			 	
			}
			
		   }
		 catch (Exception e) {
			   err=(e.toString());
		 } 
	}
	//untuk stable save hadiah, umur >69 tahun dan maks 100 juta, up = 0.5 * premi
	public void of_set_premi_50(double adec_premi)
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		ldec_rate=0;
		
		li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		li_cp = ii_pmode;

		try {

			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			   idec_premi = adec_premi;  
			   if (ldec_rate > 0)
			   {
				   idec_up = (idec_premi * ii_permil / ( ldec_rate));	
				   int decimalPlace = 0;
				   BigDecimal bd = new BigDecimal(idec_up);
				   bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				   idec_up = bd.doubleValue();
			   }
			   idec_rate = ldec_rate;
			   of_set_up(idec_up)	;			   
			}else{	
			   hsl="Tidak ada data rate";
			 	
			}
			
		   }
		 catch (Exception e) {
			   err=(e.toString());
		 } 
	}
	
	public void of_set_premi_karyawan(double adec_premi)
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		ldec_rate=0;
		
		li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		li_cp = ii_pmode;

		try {

			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			   idec_premi = adec_premi;  
			   if (ldec_rate > 0)
			   {
				   idec_up = (idec_premi * ii_permil / ( ldec_rate));	
				   int decimalPlace = 0;
				   BigDecimal bd = new BigDecimal(idec_up);
				   bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				   idec_up = bd.doubleValue();
			   }
			   idec_rate = ldec_rate;
			   of_set_up(idec_up)	;			   
			}else{	
			   hsl="Tidak ada data rate";
			 	
			}
			
		   }
		 catch (Exception e) {
			   err=(e.toString());
		 } 
	}
	
// of_set_sex
	public void of_set_sex(int ai_sex)
	{
		ii_sex_tt = ai_sex;
		
	}

// of_set_tahun
	public void of_set_tahun(int ai_tahun)
	{
		ii_tahun_ke = ai_tahun;	
	}

// of_set_up
	public void of_set_up(double adec_up)
	{
		idec_up = adec_up;	
	}
	
// of_set_usia_pp
	public void of_set_usia_pp(int ai_pp)
	{
		ii_usia_pp = ai_pp;
		of_set_age();
	}

// of_set_usia_tt
	public void of_set_usia_tt(int ai_tt)
	{
		if (ai_tt < 1)
		{
			ii_usia_tt = 1;
		}else{
			ii_usia_tt = ai_tt;
		}

		of_set_age();			
	}
	
	public String cek_hubungan(String hub)
	{
		hsl="" ;
		return hsl;
	}

	public void get_class(int i_class)
	{
		ii_class=i_class;
	}
	
	public String cek_class(int ii_class)
	{
		String hsl="";
		if(ii_class>4)
		{
			hsl="Maximum klas 4";
		}
		return hsl;
	}	
		
	public String range_class(int ii_age,int ii_class)
	{
		String hsl="";
		if (ii_age <= 16 &&  ii_class != 2) 
		{
			hsl="Untuk Tertanggung <= 16th, Pakai Kelas II pada Rider ke ";
		}
		if (ii_class > 4)
		{
			hsl="Maximum Class sampai kelas IV pada Rider ke ";
		}
		return hsl;
	}
	
	public String range_unit(int unit)
	{
		String hsl="";
		if (unit == 0)
		{
			hsl="Unit Tidak boleh Nol pada Rider ke ";
		}
		if (unit > 4)
		{
			hsl="Maximum 4 Unit pada Rider ke ";
		}		
		return hsl;
	}
	
	public void of_set_insperiod(int ai_ins)
	{
		
	}
	
	public String cek_umur_beasiswa(int umur,int nomor_produk)
	{
		hsl="";
		return hsl;
	}
	
	public void wf_set_premi(int tahun,int bulan,int tanggal, int cara_bayar,int tahun_1,int bulan_1,int tanggal_1,int insperiod, int flag_jenis_plan, int ii_age, int lama_bayar , int flag_cerdas_siswa, int umurp,int kode_bisnis,int number_bisnis)
	{	

	}	
	
	public void wf_set_premi2(int lsre_id, int peserta, int tahun,int bulan,int tanggal, int cara_bayar,int tahun_1,int bulan_1,int tanggal_1,int insperiod, int flag_jenis_plan, int ii_age, int lama_bayar , int flag_cerdas_siswa, int umurp,int kode_bisnis,int number_bisnis){
		
	}

	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan)
	{
		return idec_premi;
	}
		
	public String cek_rider(int cara_bayar, int kode_rider, int flag_rider, int flag_excellink)
	{
		String hsl="";
		if (cara_bayar==0)
		{
			if (flag_rider==0)
			{
				if (kode_rider>=800 && kode_rider <=804)
				{
					hsl="Cara bayar sekaligus tidak bisa memilih rider ke ";
				}
			}else{
				if (kode_rider >=814 && kode_rider<=817)
				{
					hsl="Cara bayar sekaligus tidak bisa memilih rider ke ";
				}
				if (flag_excellink==1)
				{
					if (kode_rider ==813 )
					{
						hsl="Cara bayar sekaligus tidak bisa memilih rider ke ";
					}
				}
			}
		}
		return hsl;
	}

	public int set_klas(int klas)
	{
		iiclass=klas;
		return iiclass;
	}
	
	public int set_unit(int unit)
	{
		iiunit=unit;
		return iiunit;
	}
	
	public double hit_premi_rider(double rate,double up,double persen, double premi)
	{
		double percentage=0;
		percentage=(rate * up / 1000) * persen; 
		return percentage;	
	}

	//produk ulink
	//persentase biaya akuisisi
	public double f_get_bia_akui(int period, int ar_ke)
	{
		double ld_bia=0;
		if (ar_ke == 1)
		{
			ld_bia = 0.8;
		}else{
			if (ar_ke == 2)
			{
				ld_bia = 0.15;
			}
		}
		return ld_bia;	
	}
	
	public double f_get_bia_akui_ekalife(int period, int ar_ke)
	{
		double ld_bia=0;
		if (ar_ke == 1)
		{
			ld_bia = 0.8;
		}else{
			if (ar_ke == 2)
			{
				ld_bia = 0.15;
			}
		}
		return ld_bia;	
	}
	
	public double cek_bia_akui(int kode_produk,int number_produk,int cara_bayar,int period,int ke)
	{
		if ( cara_bayar == 1 ||  cara_bayar == 2 ||  cara_bayar == 6 )
		{
			 cara_bayar = 3;
		}
		double ld_bia=0;
		try {
			
			Double result = query.select_biaya_akuisisi(kode_produk, number_produk, cara_bayar, ke, period);
			if(result != null) {
				ld_bia= result.doubleValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 			
		return ld_bia;
	}

	//biaya admin
	public double cek_awal()
	{
		String err="";
		double hasil=0;
		try {
			
			Double result = query.selectDefault(16);
			
			if(result != null) {
				hasil= result.doubleValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 		
		return hasil; 
	}

	public double cek_rate(int li_bisnis,int li_pmode,int pperiod,int li_insper,int umur_tt , String kurs, int insperiod)
	{
		String nama_prdk="";
		int li_cp=li_pmode;
		int kode=li_bisnis;
		int insper=li_insper;
		if (Integer.toString(li_bisnis).trim().length()==1)
		{
			nama_prdk="produk_asuransi.n_prod_0"+li_bisnis;	
		}else{
			nama_prdk="produk_asuransi.n_prod_"+li_bisnis;	
		}
		try{
			Class aClasss = Class.forName( nama_prdk );
			n_prod prdk = (n_prod)aClasss.newInstance();
			prdk.setSqlMap(sqlMap);
			if (prdk.flag_cerdas_siswa==0)
			{
				kode=77;
				//insper = 18;
			}
			if (prdk.flag_cerdas_siswa==3)
			{
				kode=97;
				//insper = 10;
			}
			
			if ( (prdk.flag_cerdas_siswa==0) || (prdk.flag_cerdas_siswa==3))
			{
				// Kalau triwulan, semester, bulanan, jadiin tahunan
				if (li_pmode == 1 || li_pmode == 2 || li_pmode == 6)
				{
					li_cp = 3;
				}
			}
			
		}
		catch (ClassNotFoundException e)
		{
			logger.error("ERROR :", e);
			 throw new NoClassDefFoundError (e.getMessage());
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		}

		String err="";
		double hasil=0;

		try {
			Double result = query.selectNilai(1, kode, "01", li_cp, pperiod, insper, 1, umur_tt);			
			if(result != null) {
				hasil = result.doubleValue();

			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 		
		return hasil; 
	}
	
	//biaya akuisisi
	public void biaya1(double ld_bia_akui, double premi)
	{
		mbu_jumlah1=ld_bia_akui * premi;
		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(mbu_jumlah1);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah1 = bd.doubleValue();		
		mbu_persen1=ld_bia_akui*100;
		BigDecimal jm = new BigDecimal(mbu_persen1);
		jm= jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);		
		mbu_persen1=jm.doubleValue();	
	}
			
	//biaya asuransi
	public void biaya2(double ld_bia_ass, double premi, int ldec_pct,double up)
	{
		mbu_jumlah2=ld_bia_ass /100 * premi * ldec_pct;
		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(mbu_jumlah2);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah2 = bd.doubleValue();		
		mbu_persen2=ld_bia_ass;
		BigDecimal jm = new BigDecimal(mbu_persen2);
		jm= jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);		
		mbu_persen2=jm.doubleValue();		
	}

	//biaya administrasi
	public void biaya3(double ldec_awal)
	{
		mbu_jumlah3=ldec_awal;
		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(mbu_jumlah3);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah3=bd.doubleValue();	
	}
	
	public void biaya4(double ldec_awal, int li_pct_biaya)
	{
		mbu_jumlah4=ldec_awal* li_pct_biaya / 100;
		int decimalPlace = 2;
		BigDecimal bd = new BigDecimal(mbu_jumlah4);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah4=bd.doubleValue();	
		mbu_persen4=li_pct_biaya;
	}	

	//pa
	public void count_rate_810(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp1 =0;
		ldec_rate1=0;
		nomor_rider1=1;
		unit_rider1=0;
		class_rider1=0;
	}
	
	//hcp
	public void count_rate_811(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp2 =0;
		ldec_rate2=0;
		nomor_rider2=1;
		unit_rider2=0;
		class_rider2=0;		
	}
	
	//tpd
	public void count_rate_812(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp3 =0;
		ldec_rate3=0;
		nomor_rider3=1;
		unit_rider3=0;
		class_rider3=0;
	}	
	
	//*ci
	public void count_rate_813(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp4 =0;
		ldec_rate4=0;
		nomor_rider4=1;
		unit_rider4=0;
		class_rider4=0;
	}	
	
	//w60-tpd
	public void count_rate_814(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp5 =0;
		ldec_rate5=0;
		nomor_rider5=1;
		unit_rider5=0;
		class_rider5=0;		
	}		

	//p25-tpd
	public void count_rate_815(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp6 =0;
		ldec_rate6=0;
		nomor_rider6=1;
		unit_rider6=0;
		class_rider6=0;		
	}
	
	//w60-ci
	public void count_rate_816(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp7 =0;
		ldec_rate7=0;
		nomor_rider7=1;
		unit_rider7=0;
		class_rider7=0;		
	}	
	
	//p25-ci
	public void count_rate_817(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp8 =0;
		ldec_rate8=0;
		nomor_rider8=1;
		unit_rider8=0;
		class_rider8=0;		
	}	
	
	//term
	public void count_rate_818(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp9 =0;
		ldec_rate9=0;
		nomor_rider9=1;
		unit_rider9=0;
		class_rider9=0;		
	}	
	
	public void count_rate_822(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp9 =0;
		ldec_rate9=0;
		nomor_rider9=1;
		unit_rider9=0;
		class_rider9=0;		
	}	
	// waiver TPD/CI
	public void count_rate_827(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp7 =0;
		ldec_rate7=0;
		nomor_rider7=1;
		unit_rider7=0;
		class_rider7=0;		
	}	
	
	public void count_rate_828(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp7 =0;
		ldec_rate7=0;
		nomor_rider7=1;
		unit_rider7=0;
		class_rider7=0;		
	}	
	public void count_rate_830(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp4 =0;
		ldec_rate4=0;
		nomor_rider4=1;
		unit_rider4=0;
		class_rider4=0;
	}
	public void count_rate_835(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		double factorUp=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int tahun = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}
		

			try {
				Double result = query.selectRateRider(kurs, umurpp, 0, kode_produk, 1);
				Double result2=query.selectRateUpScholarship(kode_produk,umurttg,nomor_produk);
				if(result != null && result2 != null) {
					hasil = result.doubleValue();
					factorUp=result2.doubleValue();
				}else{
					err1="Rate Asuransi SMiLe Scholarship Tidak Ada.";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 if(nomor_produk==1){
				 tahun=22;
			 }else{
				 tahun=25;
			 }
			 ldec_temp2 = (up/1000)*factorUp*hasil*0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();
			 ldec_rate2=hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	

	}	
	
	public void count_rate_836(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi SMiLe Baby Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp2 = hasil *  0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();			

	}
	
	//*ci
	public void count_rate_837(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		double hasilPremi=0;
		ldec_temp4=0;
		ldec_rate4=0;

		try {
			Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
//			Double resultPremi = query.selectResultPremi(42, reg_spaj);
			
			if(result != null) {
				hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi SMiLe Early Stage Critical Illness 99 Tidak Ada  !!!!";
				}
//			if(resultPremi != null) {
//				hasilPremi = resultPremi.doubleValue();
//				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 double factor_x=0;
			 
//			 if (persenUp==1){
//				 factor_x = 0.5;
//			 }else if(persenUp==2){
//				 factor_x = 0.6;
//			 }else if(persenUp==3){
//				 factor_x = 0.7;
//			 }else if(persenUp==4){
//				 factor_x = 0.8;
//			 }else if(persenUp==5){
//				 factor_x = 0.9;
//			 }else if(persenUp==6){
//				 factor_x = 1.0;
//			 }
//			 
//			 if(persenUp != 0){
				 ldec_temp4 = (( up / 1000) * hasil ) * 0.1; // FIXIN last
//			 }else{
//				ldec_temp4 = hasilPremi;
//			 }
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp4);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp4=bd.doubleValue();	
			 ldec_rate4 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate4);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate4=jm.doubleValue();			

	}

	//project Smile Medical Extra (848-1~70) helpdesk [129135] //Chandra
	public void count_rate_848(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi SMiLe MEDICAL EXTRA Tidak Ada  !!!!";
				}
			}
			catch (Exception e) {
				   err=e.toString();
			} 		
			Double disc = new Double(1);
			Double rate = new Double(0.10);
			if ((nomor_produk > 10 && nomor_produk <= 70) ||(nomor_produk > 80 && nomor_produk <= 140) ){ 
				disc = new Double(0.975);
			};
			ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();//xxxcccvvv
			int decimalPlace = 2;
			BigDecimal bd = new BigDecimal(ldec_temp2);
			bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			ldec_temp2=bd.doubleValue();	
			ldec_rate2 = 0;
			BigDecimal jm = new BigDecimal(ldec_rate2);
			jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			ldec_rate2=jm.doubleValue();
	}
	
	public void riderInclude(int nomor_produk)
	{
		indeks_rider_include=1;		
		rider_include = new int [indeks_rider_include];
		rider_include[0]=0;
	}
	
	public int jenis_biaya(int kode_bisnis, int nomor_bisnis)
	{
		String err="";
		int hasil=0;
		try {

			Integer result = query.selectJenisBiaya(kode_bisnis, nomor_bisnis);
			
			if(result != null) {
				hasil = result.intValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 		
		return hasil; 		
	}
	
	public void cek_rider_include(int nomor_produk,int kode_produk,int umurttg, int umurpp, double up, double premi, int pmode)
	{
		
	}
	
	public void count_rate(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		
	}
	
	public String cek_rider_number(int nomor_produk,int kode_produk,int umurttg, int umurpp, double up, double premi, int pmode,int hub, String kurs, int pay_period)
	{
		String hsl="";
		if ( kode_produk == 815 && nomor_produk == 6)
		{
			if (hub != 5)
			{
				hsl =" Untuk Produk PAYOR SPOUSE 60 TPD/DEATH- RIDER  hubungan pemegang dan tertanggung harus suami/istri.";
			}
		}
		
		if (kode_produk == 811)
		{
			if (kurs.equals("01"))
			{
				if (nomor_produk > 10)
				{
					hsl ="Untuk Rupiah hanya bisa HCP-R.";
				}
			}else{
				if (nomor_produk < 11)
				{
					hsl ="Untuk Rupiah hanya bisa HCP-D.";
				}
			}
		}
		
		if (kode_produk == 819)
		{
			if (kurs.equalsIgnoreCase("01"))
			{
				if ((nomor_produk >=11 && nomor_produk <=20) || (nomor_produk >=31 && nomor_produk <=40) || (nomor_produk >=51 && nomor_produk <=60) || (nomor_produk >=71 && nomor_produk <=80) || (nomor_produk >=91 && nomor_produk <=100) || (nomor_produk >=111 && nomor_produk <=120) || (nomor_produk >=131 && nomor_produk <=140))
				{
					hsl ="Untuk mata uang Rupiah tidak bisa mengambil HCPF BASIC D";
				}
				
			}else{
				if ((nomor_produk >=1 && nomor_produk <=10) || (nomor_produk >=21 && nomor_produk <=30) || (nomor_produk >=41 && nomor_produk <=50) || (nomor_produk >=61 && nomor_produk <=70) || (nomor_produk >=81 && nomor_produk <=90) || (nomor_produk >=101 && nomor_produk <=110) || (nomor_produk >=121 && nomor_produk <=130))
				{
					hsl ="Untuk mata uang Rupiah tidak bisa mengambil HCPF BASIC R";
				}
			}
		}
		return hsl;
	}
	
	public double of_get_fltpersen(Integer pmode_id)
	{
		double fltpersen =1;

			return fltpersen ;
	}	
	
	public String cek_min_up(Double premi, Double up, String kurs, Integer pmode_id)
	{
		String hasil_up="";
		return hasil_up;
	}
	
	public String cek_max_up(Integer li_umur_ttg, Integer kode_produk, Double premi, Double up, Double fltpersen, Integer pmode_id,String kurs)
	{
		String hasil_up="";
		  return hasil_up;
	}
	
	public void wf_set_rider(int tahun,int bulan,int tanggal, int cuti_premi, int li_umur_ttg, int li_umur_pp)
	{
		
	}
	
	public void cek_flag_agen(int kode_produk, int no_produk, int flag_bulanan)
	{
		
	}
	
	public static void main(String[] args) 
	{

	}
	
//	cek usia	
	public String of_check_usia_rider(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{
		hsl="";
		if (ii_age < ii_age_from)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}

		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}

		if (ii_usia_pp < 16)
		{
			hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
		}
		return hsl;		
	}
	// *Cek umur peserta product kesehatan (04/06/2012)
	public String of_check_usia_kesehatan(int utama, int hub, int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{
		hsl="";
		if (ii_age < ii_age_from)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}

		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}

		if (ii_usia_pp < 16)
		{
			hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
		}
		return hsl;		
	}
	
	public String of_check_usia_kesehatan2(int utama, int hub, int umur_ttg_tamb, int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{
		return "";
	}
	
	public double cek_maks_up_rider (Double up_rider, String kurs)
	{
		
		return up_rider;
	}
	
	public String cek_min_mgi(int mgi,String kurs, Double premi)
	{
		String hsl="";
		return hsl;
	}

	public String min_topup(Integer pmode_id, double premi , double premi_topup, String kurs, int jenis_topup)
	{
		String hsl="";
		if (kurs.equalsIgnoreCase("01"))
		{
			if (premi_topup < 1000000)
			{
				hsl="Minimum Top-Up Tunggal dan Berkala : Rp. 1.000.000,-";
			}
			if (premi_topup > 1e10)
			{
				hsl="Maksimum Top-Up Tunggal dan Berkala sebesar Rp. 10.000.000.000,- untuk satu tahun Polis";
			}	
		}else{
			if (premi_topup < 100)
			{
				hsl="Minimum Top-Up Tunggal dan Berkala : US$ 100";
			}		
			if (premi_topup > 1000000)
			{
				hsl="Maksimum Top-Up Tunggal dan Berkala sebesar US$ 1.000.000 untuk satu tahun Polis";
			}
		}
		return hsl;
	}
	
	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";

		return hsl;
	}
	
	public String min_total_premi_karyawan(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";

		return hsl;
	}
	
	public String of_get_min_up_permgi(int nomor_bisnis , int mgi, double premi,String kurs)
	{
		String hasil="";
		return hasil;
	}
	
	public Double min_up(Double premi, Double up, String kurs , Integer pmode_id)
	{
	
		return up;
	}
	
	public void cek_mgi(int nomor_bisnis)
	{

	}

	public void count_rate_819(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
	
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
//		double rate_pmode = 1.0;
		double rate_pmode = 0.1;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi HCP Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 if ((nomor_produk > 20 && nomor_produk<141) || (nomor_produk>160 && nomor_produk<281))
			 {
				 disc = new Double(0.9);
			 }
			 switch (pmode)
				{
					case 1 ://triwulan
						rate_pmode=0.27;
						break;	
					case 2 : //semesteran
						rate_pmode=0.525;
						break;
					case 6 ://BULANAN
						rate_pmode=0.1;
						break;
				}
			 ldec_temp2 = hasil *  rate_pmode * disc.doubleValue();
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	

	}	
	
	public void count_rate_820(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
//		String err="";
//		String err1="";
//		double hasil=0;
//		ldec_temp2=0;
//		ldec_rate2=0;
//		double ldec_pct1 = 1;
//		switch (pmode)
//		{
//			case 1 ://triwulan
//				li_kali = 4;
//				ldec_pct1 = 0.35;
//				break;	
//			case 2 : //semesteran
//				li_kali = 2;
//				ldec_pct1 = 0.65;
//				break;
//			case 6 ://BULANAN
//				li_kali = 12 ;
//				ldec_pct1 = 0.12;
//				break;
//		}
//		
//		if (kurs.equalsIgnoreCase("01"))
//		{
//			hasil = 90000;
//		}else{
//			hasil = 9;
//		}
//		if (klas == 4)	
//		{
//			hasil= 2 * hasil;
//		}
//		ldec_temp2 = ((hasil * li_kali) );
//		hasil = hasil/1000;
//		 int decimalPlace = 2;
//		 BigDecimal bd = new BigDecimal(ldec_temp2);
//		 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
//		 ldec_temp2=bd.doubleValue();	
//		 ldec_rate2 = (hasil);
//		 BigDecimal jm = new BigDecimal(ldec_rate2);
//		 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
//		 ldec_rate2=jm.doubleValue();		
		
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Eka Sehat Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.12);
			 if (nomor_produk > 15)
			 {
				 disc = new Double(0.975);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	


	}
	
	public void count_rate_821(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		double ldec_pct1 = 1;
		switch (pmode)
		{
			case 1 ://triwulan
				li_kali = 4;
				ldec_pct1 = 0.27;
				break;	
			case 2 : //semesteran
				li_kali = 2;
				ldec_pct1 = 0.525;
				break;
			case 6 ://BULANAN
				li_kali = 12 ;
				ldec_pct1 = 0.1;
				break;
		}
		
		if (kurs.equalsIgnoreCase("01"))
		{
			hasil = 90000;
		}else{
			hasil = 9;
		}
		if (klas == 4)	
		{
			hasil= 2 * hasil;
		}
		ldec_temp2 = ((hasil * li_kali) );
		hasil = hasil/1000;
		 int decimalPlace = 2;
		 BigDecimal bd = new BigDecimal(ldec_temp2);
		 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		 ldec_temp2=bd.doubleValue();	
		 ldec_rate2 = (hasil);
		 BigDecimal jm = new BigDecimal(ldec_rate2);
		 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		 ldec_rate2=jm.doubleValue();			

	}
	
	public void count_rate_823(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Eka Sehat Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.12);
			 if (nomor_produk > 15 && nomor_produk!=211 && nomor_produk!=212){
				 disc = new Double(0.975);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();//xxxcccvvv
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	


	}
	
	public void count_rate_831(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Ladies Hospital Protection Tidak Ada.";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 	
			 Double rate = new Double(0.1);
//			 Double disc = new Double(1);
//			 if (nomor_produk > 15)
//			 {
//				 disc = new Double(0.975);
//			 }
			 ldec_temp2 = hasil * rate.doubleValue();// *disc.doubleValue() xxxcccvvv
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	

	}
	
	public void count_rate_832(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Ladies Medical Expense Tidak Ada.";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.12);
			 if (nomor_produk > 7)
			 {
				 disc = new Double(0.975);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();//xxxcccvvv
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	

	}
	
	public void count_rate_825(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Eka Sehat Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.12);
			 if (nomor_produk > 15)
			 {
				 disc = new Double(0.975);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	


	}
	
	public void count_rate_833(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Eka Sehat Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.12);
			 if (nomor_produk > 15)
			 {
				 disc = new Double(0.975);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	


	}
	
	public void count_rate_826(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi HCP Provider Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.1);
			 if (nomor_produk > 12)
			 {
				 disc = new Double(0.9);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	


	}
	
	public void count_rate_844(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi  Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.1);
			 if (nomor_produk > 12)
			 {
//				 disc = new Double(0.9);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	


	}
	
	public void count_rate_838(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Eka Sehat Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.12);
			 if (nomor_produk >4)
			 {
				 disc = new Double(0.975);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();//xxxcccvvv
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	


	}
	
	public void count_rate_839(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp2=0;
		ldec_rate2=0;
		int fltrt = 0;
		String kd_pd = Integer.toString(new Integer(nomor_produk));
		String kd = kd_pd;
		if(Integer.parseInt(kd_pd) <10)
		{
			kd = kd_pd.substring(kd_pd.length()-1, kd_pd.length());
		}

			try {
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, (Integer.parseInt(kd)));
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Eka Sehat Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 Double disc = new Double(1);
			 Double rate = new Double(0.12);
			 if ((nomor_produk >=5 && nomor_produk <=20) || (nomor_produk >=25 && nomor_produk <=40) ||
			     (nomor_produk >=45 && nomor_produk <=60) || (nomor_produk >=65 && nomor_produk <=80))
			 {
				 disc = new Double(0.975);
			 }
			 ldec_temp2 = hasil * rate.doubleValue() * disc.doubleValue();//xxxcccvvv
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp2);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp2=bd.doubleValue();	
			 ldec_rate2 = 0;
			 BigDecimal jm = new BigDecimal(ldec_rate2);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate2=jm.doubleValue();	


	}
	
	public Double set_default_up(Double up){
		up = idec_min_up01;
		return up;
	}
	
	public double of_get_up_with_limit(double idec_premi, double idec_up, double up_lowest, double up_highest ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag, int factor_up)
	{
		double up = idec_up;
		
		if(up < up_lowest){
			up = up_lowest;
		}else if(up > up_highest){
			up = up_highest;
		}
		
		return up;
	}
	
	public double of_get_up_with_factor(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag, int factor_up)
	{
		return idec_up;
	}
	
	public double hit_premi_rider_with_factor(double rate,double up,double persen, double premi, int factor_up)
	{
		double percentage=0;
//		percentage=(rate * up / 1000) * persen; 
		return percentage;	
	}
	
	public double hit_premi_rider_new(double rate,double up){
		double result = 0.0;
		return result;
	}
}
