package produk_asuransi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.f_hit_umur;

//n_prod_828 PAYOR 25 TPD/CI/DEATH - RIDER 
/*
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class n_prod_828 extends n_prod{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	Query query = new Query();
	f_hit_umur umr = new f_hit_umur();
	public n_prod_828() 
	{
		ii_bisnis_id = 828;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 24;		
		
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
		flag_rider_baru=2;
		usia_nol = 0;
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
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		int hari=umr.hari(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
		
		hsl="";
		
		if (nomor_rider ==1 )
		{
			/*if (bln <  6)
			{
				
					-----Original Message-----
					From: Morinof Hendra 
					Sent: Tuesday, 08 April, 2008 2:47 PM
					To: Ingrid; Himmia
					Cc: Herman
					Subject: FW: input SPAJ
					
					
					Pak Himmia
					Tolong dibuka untuk kasus ini sementara sampai IM dikeluarkan
					Dr. Ingrid : please di aksep dengan usia anak kurang dari 1 tahun tetapi lebih dari 6 bulan
					
					Terima kasih.
				 
				hsl="Usia Masuk Plan Payor 25 minimum : " + ii_age_from+ " tahun (6 Bulan)";
			}*/
			 if (hari < 15)//request yuda
				{
					hsl="Usia masuk untuk PAYOR 25 TPD/CI/DEATH minimum  15 Hari";
				}
			if (ii_usia_tt > ii_age_to)
			{
				hsl="Usia Masuk Plan PAYOR 25 TPD/CI/DEATH maximum : " + ii_age_to +" tahun";
			}
			if (ii_usia_pp < 20)
			{
				hsl="Usia Pemegang Polis untuk PAYOR 25 TPD/CI/DEATH minimum : 20 Tahun !!!";
			}
			
			/*if (ii_usia_pp > (65-(25-)))
			{
					hsl="Usia Pemegang Polis untuk PAYOR 25 TPD/CI/DEATH maximum : "+(65-lm_byr)+ " tahun !!!";
			}	*/	
			if (ii_usia_pp > 64)//SK No. 029/AJS-SK/VI/2007
			{
					hsl="Usia Pemegang Polis untuk PAYOR 25 TPD/CI/DEATH maximum : 64 tahun !!!";
			}				
			
			if(hari>=15  && ii_usia_tt<1) {//jika usia lebih dari sama dengan 15 hari maka set usia ttg jadi 1
				ii_usia_tt=1;
			}
			
			if (ii_usia_pp > (65-(25-ii_usia_tt)))
				{
						hsl="Usia Pemegang Polis untuk PAYOR 25 TPD/CI/DEATH maximum : "+(65-(25-ii_usia_tt))+ " tahun !!!";
				}
			}
			
		return hsl;		
	}	
	
	public double of_get_rate1( int li_class, int flag_jenis_plan, int nomor_bisnis, int umurttg, int umurpp)
	{
		String err="";
		rate_rider=0;
		if (nomor_bisnis==3){
			umurttg=1;
		}else if(nomor_bisnis==2){
			umurttg=1;
		}
		try {
			Double result = query.selectRateRider(is_kurs_id, umurttg, umurpp, ii_bisnis_id_utama, nomor_bisnis);
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
		li_sd = 25;
		if (number_bisnis == 2) // SK. No. 105/EL-SK/XI/2006 23/11/2006
		{
			li_sd = 5;
			li_insured = li_sd;
		}else if (number_bisnis == 3) // SK No. 029/AJS-SK/VI/2007
			{
			li_sd = 10;
			li_insured = li_sd;

				}else {
					int daripp = 65 - umurpp;
					int daritt = 25 - ii_age;
					if (daripp < daritt)
						{
							li_insured = daripp;
						}else{
							li_insured = daritt;
					}
				}
		if (flag_jenis_plan==1  || flag_jenis_plan==2 || flag_jenis_plan==9)
		{
			li_insured = lama_bayar;
		}
		
		
		//ld_temp = Relativedate(date(f_add_months(istr_polis.beg_date, li_insured * 12)), -1)
		tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,li_insured * 12);
		ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		ldt_edate.add(ldt_edate.DATE,-1);
	
		tanggal_sementara1=umr.f_add_months(tahun,bulan,tanggal,( li_insured * 12) - 1);
		//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
		ldt_epay.set(tanggal_sementara1.getYear()+1900,tanggal_sementara1.getMonth(),tanggal_sementara1.getDate());

		if (cara_bayar == 1)  //triwulan
		{
			li_kali = 4;
		}else{
			if (cara_bayar == 2)//semesteran
			{
				li_kali = 2;
			}else{
				if (cara_bayar == 6)//bulanan
				{
					li_kali = 12;
				}
			}
		}
	}

	public double of_get_up(double idec_premi,double idec_up ,int li_unit, int flag_jenis_plan, int kode_bisnis, int nomor_bisnis,int flag)
	{
		return idec_premi;
	}

	public int set_klas(int klas)
	{
		iiclass=0;
		return iiclass;
	}
	
	public double hit_premi_rider(double rate,double up,double persen, double premi)
	{
		double percentage=0;
//		percentage=(rate * premi / 1000); 
		percentage=(rate / 1000 * premi * (1/10));
		return percentage;	
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
		if (nomor_produk==3 || nomor_produk==2){
			umurttg=1;
		}
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
			produk2.count_rate_828(klas,unit,828,nomor_produk,kurs,umurttg,umurpp,up,premi,pmode,flag,ins_period,payperiod);
			mbu_jumlah =  produk2.ldec_temp6;
			mbu_rate = produk2.ldec_rate6;
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
	
	public double cek_maks_up_rider (Double up_rider, String kurs)
	{
		if (kurs.equalsIgnoreCase("01"))
		{
			if (up_rider.doubleValue() > 2000000000)//up 2 M
			{
				up_rider = new Double(2000000000);
			}
		}else{
			if (up_rider.doubleValue() > 200000)
			{
				up_rider = new Double(200000);
			}
		}
		return up_rider;
	}
	public static void main(String[] args) 
	{

		
	}
}
