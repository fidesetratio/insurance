package produk_asuransi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.f_hit_umur;

/*
 * Created on May 31, 2016
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Ridhaal
 * Provestara Rider RB
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_842 extends n_prod{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_842()
	{
		ii_bisnis_id = 842;
		ii_contract_period = 1;
		ii_age_from = 18; 
		ii_age_to = 40;		
		
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
		li_sd = 75;
		simas = 1;
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) {
			
		if (ii_usia_pp > 85) {
			hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!"; 
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
	 
	 public void of_set_bisnis_no(int ai_no){
			ii_bisnis_no = ai_no;
		
			
	}
	
	public String of_check_usia_kesehatan(int utama, int hub, int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{
		hsl="";
		f_hit_umur umr =new f_hit_umur();
		int hari=umr.hari(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
		if(ii_bisnis_no>=1 && ii_bisnis_no<=10){
			if(hub != 5)hsl="Untuk Manfaat Rawat bersalin Provestara hanya boleh diambil oleh istri tertanggung utama";
			if(ii_usia_tt < ii_age_from){
				hsl="Usia Masuk untuk SMiLe Medical RB minimum : " + ii_age_from+ " tahun";
			}else if(ii_usia_tt > ii_age_to){
				hsl="Usia Masuk untuk SMiLe Medical RB maximun : " + ii_age_to+ " tahun";
			}
		}
		return hsl;		
	}	
	
	
	
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		String err="";
		rate_rider=0;
		try {
			Double result = query.selectRateRider(is_kurs_id, umurttg, 0, 842, nomor_bisnis);
			if(result != null) {
				rate_rider = result.doubleValue();		  	
			}
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 		  	

		return rate_rider;	
	}
	
	public void wf_set_premi(int tahun,int bulan,int tanggal, int cara_bayar,int tahun_1,int bulan_1,int tanggal_1,int insperiod, int flag_jenis_plan, int ii_age, int lama_bayar, int flag_cerdas_siswa, int umurpp,int kode_bisnis,int number_bisnis)
	{
		li_insured = insperiod;
		tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
		ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		ldt_edate.add(ldt_edate.DATE,-1);	
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12)-1 );		
		ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());
		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			li_kali = 1000;
		}
	}
	
	public void of_set_pmode(int ai_pmode)
	{
		ii_pmode = ai_pmode;
		
	}		
	
	public double hit_premi_rider(double rate,double up,double persen, double premi)
	{
		double diskon = new Double(0.95); // Tertanggung tambahan Provestara dapat diskon 5%
		
		if( (ii_bisnis_no >= 18 && ii_bisnis_no <= 45 )){ // Tertanggung tambahan Smile Hospital care dapat diskon 10%
			diskon = new Double(0.90);
		}
		
		if(ii_bisnis_no<=2 || (ii_bisnis_no >= 11 && ii_bisnis_no <= 17 )){ // Tertanggung Utama / I
			premi = rate *persen;
		}else{
			premi = rate * diskon * persen;
		}
		return premi;	
		
	}

	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{

		String nama_produk="";
		try {
			String result = query.selectgetnamaplan(kode_bisnis, nomor_bisnis);
			if(result != null) {
				nama_produk = result.toString();		  	
			}
			if(nama_produk.contains("GOLD")){
				idec_up = 20000000;
			}else if(nama_produk.contains("PLATINUM")){
				idec_up = 23000000;
			}else if(nama_produk.contains("COPPER")){
				idec_up = 10000000;
			}else if(nama_produk.contains("BRONZE")){
				idec_up = 13000000;
			}else if(nama_produk.contains("SILVER")){
				idec_up = 15000000;
			}else if(nama_produk.contains("SAPPHIRE")){
				idec_up = 17000000;
			}else if(nama_produk.contains("DIAMOND")){
				idec_up = 21000000;
			}
		}
		catch (Exception e) {
			err=(e.toString());
		}
		return idec_up;
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
			produk2.count_rate_839(klas,unit,842,nomor_produk,kurs,umurttg,umurpp,up,premi,pmode,flag,ins_period,payperiod);
			mbu_jumlah =  produk2.ldec_temp2;
			mbu_rate = produk2.ldec_rate2;
			mbu_persen = mbu_rate ;
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
	
	public String of_check_usia_rider(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{
		hsl="";
		if (ii_usia_tt < ii_age_from)
		{
			hsl="Usia Masuk Plan RB Provestara minimum : " + ii_age_from;
		}

		if (ii_usia_tt > ii_age_to)
		{
			hsl="Usia Masuk Plan RB Provestara maximum : " + ii_age_to;
		}

		return hsl;		
	}
	
	public static void main(String[] args) {
	}
}
