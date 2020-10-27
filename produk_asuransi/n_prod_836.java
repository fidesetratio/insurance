package produk_asuransi;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.f_hit_umur;

//
/*
 * Created on Nov 14, 2013
 *
 */

/**
 * @author Andhika
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_836 extends n_prod{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_836()
	{
		ii_bisnis_id = 836;
		ii_contract_period = 1;
		ii_age_from = 18;
		ii_age_to = 40;		
	  	idec_min_up01 = 10000000;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";	
		
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 2;   //semester
		ii_pmode_list[3] = 1;   //Tri		
		ii_pmode_list[4] = 6;   //bulanan	

		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.35; // pmode 1
		idec_pct_list[2] = 0.65; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.12; // pmode 6	
		flag_rider=1;
		li_sd = 5;
		simas = 1;
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) {
			
		if (ii_usia_pp > 90) {
			hsl = "Usia Pemegang Polis maximum : 90 Tahun !!!"; 
		} else if (ii_usia_pp < 17) {
			hsl = "Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		return hsl;		
	}	

//	of_set_usia_pp
	 public void of_set_usia_pp(int ai_pp)
	 {
		 ii_usia_pp = ai_pp;
	 }

//	of_set_usia_tt
	 public void of_set_usia_tt(int ai_tt)
	 {
		 ii_usia_tt = ai_tt;
	 }

	public String of_check_usia_rider(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{
		hsl="";
				
		Calendar tgl_perkiraan_lahir = Calendar.getInstance();	
		tgl_perkiraan_lahir.set(tahun1, bulan1, tanggal1);
		tgl_perkiraan_lahir.add(Calendar.MONTH, -9);
		
		tahun1=tgl_perkiraan_lahir.get(Calendar.YEAR);
		bulan1=tgl_perkiraan_lahir.get(Calendar.MONTH);
		tanggal1=tgl_perkiraan_lahir.get(Calendar.DATE);
		
		if(bulan1==0) {
			tahun1 = tahun1-1;
			bulan1 = 12;
		}
		
//		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		int hari=umr.hari1(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		
//		140-224 hari (5 s/d 8 bulan) 
		if(hari<140 || hari>224){
			hsl=" Untuk Rider SMiLe Baby Umur kandungan harus minimal 5 bulan(20 Minggu) dan maksimal 8 bulan(32 Minggu). Pastikan tanggal lahir di halaman tertanggung sesuai dengan tanggal perkiraan lahir di FORM SPAJ";
		}
		

		
		return hsl;
	}
	
	public String of_check_usia_kesehatan(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{	
		hsl="";
		Calendar tgl_perkiraan_lahir = Calendar.getInstance();	
		tgl_perkiraan_lahir.set(tahun1, bulan1, tanggal1);
		tgl_perkiraan_lahir.add(Calendar.MONTH, -9);
		
		tahun1=tgl_perkiraan_lahir.get(Calendar.YEAR);
		bulan1=tgl_perkiraan_lahir.get(Calendar.MONTH);
		tanggal1=tgl_perkiraan_lahir.get(Calendar.DATE);
		
//		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		int hari=umr.hari1(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		
//		140-224 hari (5 s/d 8 bulan) 
		if(hari<140 || hari>224){
			hsl=" Untuk Rider SMiLe Baby Umur kandungan harus minimal 5 bulan(20 Minggu) dan maksimal 8 bulan(32 Minggu).";
		}
		return hsl;
		
	}
	
	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{
		double sum=0;
		sum=idec_up;
		
		if (nomor_bisnis == 4){
			sum = (double) 50000000;
		}else if (nomor_bisnis == 5){
			sum = (double) 50000000;
		}else if (nomor_bisnis == 6){
			sum = (double) 100000000;
		}
		
		return sum;
	}
	
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp){
		String err="";
		rate_rider=0;
		try {
			Double result = query.selectRateRider(is_kurs_id, umurttg, 0, 836, nomor_bisnis);
			if(result != null) {
				rate_rider = result.doubleValue();		  	
			}
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 		  	

		return rate_rider;	
	}
	
	public double of_get_rate2( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp){
		String err="";
		rate_rider=0;
		Double result = (double) 0;
		try {
			if (nomor_bisnis == 1 || nomor_bisnis == 4){
				 result = (double) 1332000;
			}else if (nomor_bisnis == 2 || nomor_bisnis == 5){
				 result = (double) 2603000;
			}else if (nomor_bisnis == 3 || nomor_bisnis == 6){
				 result = (double) 4217000;
			}
			if(result != null) {
				rate_rider = result.doubleValue();		  	
			}
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 		  	

		return rate_rider;	
	}
	
	public void wf_set_premi(int tahun,int bulan,int tanggal, int cara_bayar,int tahun_1,int bulan_1,int tanggal_1,int insperiod, int flag_jenis_plan, int ii_age, int lama_bayar, int flag_cerdas_siswa, int umurpp,int kode_bisnis,int number_bisnis){
		li_insured = li_sd;
		
		tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
		ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		ldt_edate.add(ldt_edate.DATE,-1);
	
		li_insured = li_sd;
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12)-1 );
		ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());

		if (is_kurs_id.equalsIgnoreCase("01")){
			li_kali = 1000;
		}
		li_insured = li_sd;
	}
	
	public void of_set_pmode(int ai_pmode){
		ii_pmode = ai_pmode;
	}		
	
	public double hit_premi_rider(double rate,double up,double persen, double premi)
	{
		double ldec_pct1 = 1;
		switch (ii_pmode)
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
		
//		try {
			//Double result2=query.selectRateUpScholarship(836,ii_usia_tt,ii_bisnis_no);
//			Double result2 = query.selectRateRider(is_kurs_id, ii_usia_tt, 0, 836, ii_bisnis_no);
			Double result2 = rate * ldec_pct1;
			premi=result2;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			logger.error("ERROR :", e);
//		}
		
		return premi;	
	}
	
	public int set_klas(int klas)
	{
		iiclass=0;
		return iiclass;
	}
	
	public int set_unit(int unit)
	{
		iiunit=0;
		return iiunit;
	}

	public String range_class(int ii_age,int ii_class)
	{
		String hsl="";
		return hsl;
	}
	
	public String range_unit(int unit)
	{
		String hsl="";
		return hsl;
	}

	public void count_rate(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String nama_produk="";
		mbu_jumlah =0;
		mbu_rate=0;
		mbu_persen=0;
		if (Integer.toString(kode_produk).trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+Integer.toString(kode_produk);	
		}else{
			nama_produk="produk_asuransi.n_prod_"+Integer.toString(kode_produk);	
		}

		try{
			Class aClass2 = Class.forName( nama_produk );
			n_prod produk2 = (n_prod)aClass2.newInstance();
			produk2.setSqlMap(sqlMap);
			produk2.ii_bisnis_id_utama=ii_bisnis_id_utama;
			produk2.ii_bisnis_no_utama=ii_bisnis_no_utama;
			produk2.count_rate_836(klas,unit,836,nomor_produk,kurs,umurttg,umurpp,up,premi,pmode,flag,ins_period,payperiod);
			mbu_jumlah =  produk2.ldec_temp2;
			mbu_rate = produk2.ldec_rate2;
			mbu_persen = mbu_rate / 10 ;
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
				
	}	
	
	public static void main(String[] args) {
	}
}
