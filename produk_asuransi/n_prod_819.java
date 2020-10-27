package produk_asuransi;
import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.f_hit_umur;

//n_prod_819 (HCP FAMILY-Hospital Cash Plan Family Rider)
/*
 * Created on Oct 6, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Hemilda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_819 extends n_prod{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_819()
	{
		ii_bisnis_id = 819;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 64;		
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";	
		
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 2;   //semester
		ii_pmode_list[3] = 1;   //Tri		
		ii_pmode_list[4] = 6;   //bulanan	

		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6	
		flag_rider=1;
		usia_nol = 0;
		li_sd = 65;
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
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		int hari=umr.hari(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
		if (hari < 15)
		{
			hsl="Usia Masuk untuk HCP FAMILY minimum : " + ii_age_from+ " tahun";
		}		

		if (ii_usia_tt > ii_age_to)
		{
			hsl="Usia Masuk untuk HCP FAMILY maximum : " + ii_age_to +" tahun";
		}
		return hsl;		
	}	
	// *Cek umur peserta product kesehatan (04/06/2012)
	public String of_check_usia_kesehatan(int utama, int hub, int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk, int nomor_rider) 
	{
		hsl="";
		f_hit_umur umr =new f_hit_umur();
		int hari=umr.hari(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
		if(hub != 4 && hub != 8 && hub != 21 && hub != 22){
			if(utama == 0){
				ii_age_from = 0;
			}else{
				ii_age_from = 17;
			}
			if(ii_usia_tt< ii_age_from){
				hsl="Usia Masuk untuk HCP FAMILY minimum : " + ii_age_from+ " tahun";
			}else if (ii_usia_tt > ii_age_to){
				hsl="Usia Masuk untuk HCP FAMILY maximum : " + ii_age_to +" tahun";
			}
		}else{
			ii_age_from = 0;
			if(utama == 0){
				ii_age_to = 65;
			}else{
				ii_age_to = 23;
			}
			if(ii_usia_tt < ii_age_from){
				hsl="Usia Masuk untuk HCP FAMILY minimum : " + ii_age_from+ " tahun";
			}else if (ii_usia_tt > ii_age_to){
				hsl="Usia Masuk untuk HCP FAMILY maximum : " + ii_age_to +" tahun";
			}
		}
		if (ii_usia_tt > ii_age_to)
		{
			hsl="Usia Masuk untuk HCP FAMILY maximum : " + ii_age_to +" tahun";
		}
		return hsl;		
	}	
	
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		String err="";
		rate_rider=0;
		try {
			Double result = query.selectRateRider(is_kurs_id, umurttg, 0, 819, nomor_bisnis);
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
		li_insured = li_sd - ii_age;
		if (flag_jenis_plan==1)
		{
			li_insured = 6;
		}
		//ld_temp = Relativedate(date(f_add_months(istr_polis.beg_date, li_insured * 12)), -1)
		tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
		ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		ldt_edate.add(ldt_edate.DATE,-1);
	
		li_sd = 65;
		li_insured = li_sd - ii_age;
		if (flag_jenis_plan==1)
		{
			li_insured = 6;
		}
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12)-1 );
		//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
		ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());

		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			li_kali = 1000;
		}
		li_sd = 65;
		li_insured = li_sd - ii_age;
		if((number_bisnis >= 281 && number_bisnis <= 380) || (number_bisnis >= 431 && number_bisnis <=530)){
			li_insured = 4;
			tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
			ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
			ldt_edate.add(ldt_edate.DATE,-1);
			tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12)-1 );
			//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
			ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());
		}else if(number_bisnis >= 381 && number_bisnis <= 430){
			li_insured = 8;
			tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
			ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
			ldt_edate.add(ldt_edate.DATE,-1);
			tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12)-1 );
			//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
			ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());
		}
	}

	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{

		double sum=0;
		String nama_produk="";
		String nama_produk2="";
		try {
			String result = query.selectgetnamaplan(kode_bisnis, nomor_bisnis);
			if(result != null) {
				nama_produk = result.toString();		  	
			}
		}
	  catch (Exception e) {
			err=(e.toString());
	  }
//	  nama_produk=nama_produk.substring(nama_produk.lastIndexOf("-")+1,11);
//	  nama_produk=nama_produk.substring(nama_produk.lastIndexOf("-")+1,nama_produk.length());
//	  idec_up = Double.parseDouble(nama_produk);
	  
	  
	  nama_produk=nama_produk.substring(nama_produk.lastIndexOf("-")+1,nama_produk.length());
	  nama_produk2 = nama_produk.substring(0, nama_produk.indexOf(" "));
	  idec_up = Double.parseDouble(nama_produk2);
	  
	  if (flag==1)
	  {
			if (is_kurs_id.equalsIgnoreCase("01"))
			{
				li_kali = 1000;
			}
	  }
	  sum=idec_up * li_kali;
	
		return sum;
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
			produk2.count_rate_819(klas,unit,819,nomor_produk,kurs,umurttg,umurpp,up,premi,pmode,flag,ins_period,payperiod);
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
		double rate_pmode = 1.0;
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
			 if ((nomor_produk > 20 && nomor_produk<141) || (nomor_produk>160 && nomor_produk<281) || (nomor_produk>300 && nomor_produk<381) || (nomor_produk>390 && nomor_produk<431 || (nomor_produk>450 && nomor_produk<=530)))
			 {
				 disc = new Double(0.9);
			 }
			 switch (pmode)
				{
					case 1 ://triwulan
						rate_pmode=0.25;
						break;	
					case 2 : //semesteran
						rate_pmode=0.5;
						break;
					case 6 ://BULANAN
						rate_pmode=0.083;
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
	
	public static void main(String[] args) {
	}
}
