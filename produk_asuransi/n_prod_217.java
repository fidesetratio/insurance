//n_prod_217 SMiLe LINK 100
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

/*
 * Created on Sep 13, 2016
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Ridhaal
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_217 extends n_prod{

	public n_prod_217()
	{
		ii_bisnis_id = 217;
		ii_contract_period = 100;
		ii_age_from = 1;
		ii_age_to = 70;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0] = "01";
		is_forex[1] = "02";
		
//		  1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=6;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		
		ii_pmode_list[2] = 3  ; //tahunan
		ii_pmode_list[3] = 2 ;  //semester
		ii_pmode_list[4] = 1 ;  //Tri
		ii_pmode_list[5] = 6 ;  //bulanan

		
		
		indeks_li_pmode=6;
		li_pmode = new int[indeks_li_pmode];

//		  untuk hitung end date ( 79 - issue_date )
		ii_end_from = 100;
//		ib_flag_end_age = False
	  	idec_min_up01 = 15000000;

		indeks_ii_lama_bayar=2;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 5;

		indeks_idec_list_premi=20;
		idec_list_premi= new double[indeks_idec_list_premi];
		for (int i = 0 ; i < 13 ;i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.26; // pmode 1
		idec_pct_list[2] = 0.51; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.09; // pmode 6
		
		flag_uppremi=1;
		flag_uppremiopen=1;
		
		indeks_rider_list=15;
		ii_rider = new int[indeks_rider_list];
		int x = 0;
		ii_rider[x++]=810;		//1.	SMiLe Personal Accident 
//		ii_rider[x++]=826;		//2.	SMiLe Hospital Protection (+) 
		ii_rider[x++]=812;		//3.	SMiLe Total Permanent Disability (TPD) 
		ii_rider[x++]=813;		//4.	SMiLe Critical Illness (CI) 
		ii_rider[x++]=818;		//5.	SMiLe Term Rider Unit Link 
		ii_rider[x++]=823;		//6.	SMiLe Medical 
		ii_rider[x++]=832;		//7.	SMiLe Ladies Medical 
//		ii_rider[x++]=831;		//8.	SMiLe Ladies Hospital 
		ii_rider[x++]=830;		//9.	SMiLe Ladies Insurance 
		ii_rider[x++]=835;		//10.	SMiLe Scholarship 
		ii_rider[x++]=828;		//11.	SMiLe Payor 5/10 TPD/CI/Death 
		ii_rider[x++]=827;		//12.	SMiLe Waiver 5/10 TPD/CI 827
		ii_rider[x++]=837;		//13.	SMiLe ESCI 99 
//		ii_rider[x++]=811;		//14.	SMiLe Hospital Protection  
//		ii_rider[x++]=819;		//15.	SMiLe Hospital Protection PROTECTION FAMILY 
		ii_rider[x++]=825;		//16.	SMiLe Medical IL
		ii_rider[x++]=815;		//17.	Payor Spouse 60 TPD/Death
		ii_rider[x++]=833;		//18.	SMiLe LADIES MEDICAL IL
		ii_rider[x++]=848; //project Smile Medical Extra (848-1~70) helpdesk [132786] //Chandra
		
		flag_account =2;
		flag_rider=1;												
		flag_excell80plus = 1;
		//Yusuf - 20050203
		isInvestasi = true;
		usia_nol = 1;
		flag_as = 0;
		flag_cuti_premi = 1;
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";		
		if (bln < 1)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}		
		if (ii_usia_pp > 85)
		{
			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
		}		
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		if (lm_byr < 5)
		{
			hsl="Untuk produk SMiLe Link Pro 100, minimal masa Pembayaran yg diperbolehkan adalah 5 Tahun !!!";
		}
				
		return hsl;		
	}	
	
//	cek usia berdasarkan paket	
	public String of_check_usia_case_paket(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk,int paket) 
	{
		hsl="";
		ii_age_from = 17;
		
		if(paket == 39 ){
			ii_age_to = 50;
		}else if(paket == 40){
			ii_age_to = 55;
		}
		if(ii_bisnis_no != 2){ // untuk erbe hilangkan Tertanggung polis minimum 
			if (ii_age < ii_age_from)
			{
				hsl="Usia Masuk Plan dengan paket ini minimum : " + ii_age_from;
			}
		}
		if(ii_bisnis_no != 2){ // untuk erbe hilangkan pemegang polis minimum 
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan dengan paket ini maximum : " + ii_age_to;
			}
		};
		
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
		}
		
		if (lm_byr < 5)
		{
			hsl="Untuk produk SMiLe Link Pro 100, minimal masa Pembayaran yg diperbolehkan adalah 5 Tahun !!!";
		}
		
		return hsl;		
	}
	
	public double of_get_fltpersen(Integer pmode_id)
	{
		double fltpersen =1;
		if (pmode_id.intValue()==1){
			fltpersen = 4; 
		}else{
			if (pmode_id.intValue()==2){
				fltpersen = 2; 
			}else{
				if (pmode_id.intValue()==3){
					fltpersen = 1; 
				}else{
					if (pmode_id.intValue()==6){
						fltpersen = 12; 
					}
				}
			}
		}
			return fltpersen ;
	}
	
	public double of_get_min_up()
	{
		double ldec_1=0;
		
		if(ii_bisnis_no == 2){
			
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				if (ii_pmode==3)
					{
						ldec_1 =1800000; 
					}else{
						if (ii_pmode==1)
						{
							ldec_1 = 450000 ;
						}else{
							if (ii_pmode==2)
							{
								ldec_1 = 900000 ;
							}else{
								if (ii_pmode==6)
								{
									ldec_1 = 150000 ;
								}							
							}
						}
					}
				
			}
			
		}else{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				if (ii_pmode==3)
					{
						ldec_1 =3000000; 
					}else{
						if (ii_pmode==1)
						{
							ldec_1 = 750000 ;
						}else{
							if (ii_pmode==2)
							{
								ldec_1 = 1500000 ;
							}else{
								if (ii_pmode==6)
								{
									ldec_1 = 300000 ;
								}							
							}
						}
					}
				
			}else{
				
					if (ii_pmode==3)
					{
						ldec_1 =300; 
					}else{
						if (ii_pmode==1)
						{
							ldec_1 = 75;
						}else{
							if (ii_pmode==2)
							{
								ldec_1 = 150 ;
							}else{
								if (ii_pmode==6)
								{
									ldec_1 = 30 ;
								}
							}
						}
					}
				
			}
		}
		
		
		return ldec_1;
	}
	
	public double of_get_max_up()
	{
		double ldec_1=0;
		
		if(ii_bisnis_no == 2){

			if (ii_pmode==3)
			{
				ldec_1 =6000000; 
			}else{
				if (ii_pmode==1)
				{
					ldec_1 =1500000;
				}else{
					if (ii_pmode==2)
					{
						ldec_1 = 3000000;
					}else{
						if (ii_pmode==6)
						{
							ldec_1 = 500000;
						}
					}
				}
			}
		
		}else{
		
				if (is_kurs_id.equalsIgnoreCase("01") )
				{
					if (ii_age <=5)
					{
						if (ii_pmode==0)
						{
							ldec_1 =400000000; 
						}else{
							if (ii_pmode==3)
							{
								ldec_1 =200000000; 
							}else{
								if (ii_pmode==1)
								{
									ldec_1 = 200000000/0.27;
								}else{
									if (ii_pmode==2)
									{
										ldec_1 = 200000000/0.525 ;
									}else{
										if (ii_pmode==6)
										{
											ldec_1 = 200000000/12 ;
										}								
									}
								}
							}
						}
					}else{
						if (ii_pmode==0)
						{
							ldec_1 =1000000000; 
						}else{
							if (ii_pmode==3)
							{
								ldec_1 =1000000000; 
							}else{
								if (ii_pmode==1)
								{
									ldec_1 =1000000000/0.27;
								}else{
									if (ii_pmode==2)
									{
										ldec_1 = 1000000000/0.525;
									}else{
										if (ii_pmode==6)
										{
											ldec_1 = 1000000000/12;
										}
									}
								}
							}
						}
					}
				}else{
					if (ii_age <=5)
					{
						if (ii_pmode==0)
						{
							ldec_1 =40000; 
						}else{
							if (ii_pmode==3)
							{
								ldec_1 =20000; 
							}else{
								if (ii_pmode==1)
								{
									ldec_1 = 20000/0.27;
								}else{
									if (ii_pmode==2)
									{
										ldec_1 = 20000/0.525;
									}else{
										if (ii_pmode==6)
										{
											ldec_1 = 20000/12;
										}
									}
								}
							}
						}
					}else{
						if (ii_pmode==0)
						{
							ldec_1 =100000; 
						}else{
							if (ii_pmode==3)
							{
								ldec_1 =100000; 
							}else{
								if (ii_pmode==1)
								{
									ldec_1 = 100000/0.27;
								}else{
									if (ii_pmode==2)
									{
										ldec_1 = 100000/0.525;
									}else{
										if (ii_pmode==6)
										{
											ldec_1 = 100000/12;
										}
									}
								}
							}
						}				
					}
				}
		}
		return ldec_1;
	}	
	
	public boolean of_check_premi(double ad_premi)
	{
//		if (ii_bisnis_no == 1 || ii_bisnis_no == 3)
//		{
//			if (ad_premi < 5000000)
//			{
//				return false;
//			}
//		}else{
//			if (ii_bisnis_no == 2 || ii_bisnis_no == 4 )
//			{
//				switch (ii_pmode)
//				{
//					case 3:
//						if (ad_premi < 1800000)
//						{
//							return false;
//						}
//						break;
//					case 2:
//						if (ad_premi < 900000)
//						{
//							return false;
//						}
//						break;
//					case 1:
//						if (ad_premi < 600000)
//						{
//							return false;
//						}
//						break;
//					case 6:
//						if (ad_premi < 150000 )
//						{
//							return false;
//						}
//						break;
//				}
//			}
//		}
		return true;
	}		
	
	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =ii_contract_period-ii_age;
		
		return li_cp;		
	}	
	
//	get pay period
	  public int of_get_payperiod()
	  {
		 
		ii_lama_bayar[0] = (ii_end_from - ii_usia_tt);		
		//ii_pay_period = ii_bisnis_no;
		return ii_lama_bayar[0];
		  
	  }	


//	get rate
	 public double of_get_rate()
	 {
		 return idec_rate;	
	 }

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

		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);
		
		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);
		ii_contract_period = ii_end_from - ii_end_age;
	}

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;

		if (ii_bisnis_id < 800)
		{
			
			indeks_li_pmode=5;
			li_pmode[1] = 3;
			li_pmode[2] = 2;
			li_pmode[3] = 1;
			li_pmode[4] = 6;
			
			ii_pmode_list = li_pmode;
			indeks_ii_pmode_list=indeks_li_pmode;
			ii_bisnis_no_utama = ii_bisnis_no;
	//		ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);
		}
	}

//	of_set_kurs
	 public void of_set_kurs(String as_kurs)
	 {
		is_kurs_id = as_kurs;
		if (as_kurs.equalsIgnoreCase("02"))
		{
			kode_flag=5;
			if (ii_bisnis_no == 1 || ii_bisnis_no == 3)
			{
				indeks_idec_list_premi=17;
				for (int i = 0; i< 16 ; i++)
				{
					idec_list_premi[i] = 900 + ( 100 * i );
				}
			}else{
				indeks_idec_list_premi=19;
				for (int i = 0 ; i< 18; i++)
				{
					idec_list_premi[i] = 200 + ( 100 * i );
				}
			}
		}else{
			kode_flag=4;
			if (ii_bisnis_no == 1 || ii_bisnis_no == 3)
			{
				indeks_idec_list_premi=18;				
				for (int i = 0; i < 16 ; i++)
				{
					idec_list_premi[i] = 9000000 + ( 1000000 * i );
				}
			}else{
				indeks_idec_list_premi=19;				
				for (int i = 0 ; i< 18 ; i++)
				{
					idec_list_premi[i] = 2000000 + ( 1000000 * i );
				}
			}
		}
	 }

	public void of_set_premi(double adec_premi)
	{
		idec_premi_main = adec_premi;
		idec_premi = adec_premi;
		
		idec_rate = 5;
		if (ii_bisnis_no == 1){ //helpdesk [142338] Perubahan minimum UP produk reguler premium UL Agency
			idec_rate = 10;
		}
	
		if (ii_pmode == 1 )
		{
			//triwulan
			adec_premi = adec_premi * 4;
		}else{
			if (ii_pmode == 2)
			{
				//semesteran
				adec_premi = adec_premi * 2;
			}else{
				if (ii_pmode == 6)
				{
					//bulanan
					adec_premi = adec_premi * 12;
				}
			}
		}

		idec_up = adec_premi * idec_rate;
		
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if (idec_up < 7500000){
				idec_up = 7500000;
			}
		}else if (is_kurs_id.equalsIgnoreCase("02") )
		{
			if (idec_up < 750){
				idec_up = 750;
			}
		}
		
		of_set_up(idec_up);
	}	 
	 	
	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia=0;
		if (ar_lb==1)
		{
			if (ar_ke==1)
			{
				ld_bia=0.05;
			}
		}else{
			if (ar_lb>1)
			{
				if (ar_ke==1)
				{
					ld_bia=0.7;	
				}else{
					if (ar_ke==2)
					{
						ld_bia=0.2;
					}else{
						if (ar_ke >=3 && ar_ke <=5)
						{
							ld_bia=0.05;
						}
					}
				}
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
		if (cara_bayar != 0)
		{
			period = 80;	
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
	
	public double cek_awal()
	{
		Double hasil = null;
		Integer nominal = null;
		if (is_kurs_id.equalsIgnoreCase("01") ) {
			nominal = 27500;
			hasil = nominal.doubleValue();
		}else if (is_kurs_id.equalsIgnoreCase("02") ) {
			hasil = 2.75;
		}		
		return hasil; 
	}

	public double cek_rate(int li_bisnis,int li_pmode,int pperiod,int li_insper,int umur_tt , String kurs,int insperiod)
	{
		String err="";
		double hasil=0;
		try {
			Double result = query.selectNilai(1, 190, "01", 3, 80, 80, 1, umur_tt);
			
			if(result != null) {
				hasil=result.doubleValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 		
		return hasil; 
	}
	
	//BIAYA ASURANSI
	public void biaya2(double ld_bia_ass, double premi, int ldec_pct, double up)
	{
		mbu_jumlah2=((up/1000) * ld_bia_ass) * 0.1;
		int decimalPlace = 3;
		BigDecimal bd = new BigDecimal(mbu_jumlah2);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah2 = bd.doubleValue();		
		mbu_persen2=ld_bia_ass/10;
		BigDecimal jm = new BigDecimal(mbu_persen2);
		jm= jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);		
		mbu_persen2=jm.doubleValue();			
	}

	//BIAYA ADMINISTRASI
	public void biaya3(double ldec_awal)
	{
		mbu_jumlah3=ldec_awal;
		int decimalPlace = 3;
		BigDecimal bd = new BigDecimal(mbu_jumlah3);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah3=bd.doubleValue();		
	}

	//pa
	public void count_rate_810(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp1=0;
		ldec_rate1=0;

			try {
				Double result = query.selectRateRider("01", 0, klas, 810, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi PA Tidak Ada !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
//			 ldec_temp1 = ((unit * up / 1000) * hasil) * 0.1;
			 ldec_temp1 = (( up / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp1);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp1=bd.doubleValue();	
			 ldec_rate1 = (hasil);
			 BigDecimal jm = new BigDecimal(ldec_rate1);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate1=jm.doubleValue();			

	}
	
	public void count_rate_811(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
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
					err1="Rate Asuransi HCP Tidak Ada  !!!!";
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
	
	public void count_rate_812(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp3=0;
		ldec_rate3=0;

		try {
			Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
			
			if(result != null) {
				hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi HCP Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp3 = (( up / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp3);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp3=bd.doubleValue();	
			 ldec_rate3 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate3);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate3=jm.doubleValue();			

	}	

	public void count_rate_813(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
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
					err1="Rate Asuransi CI Tidak Ada  !!!!";
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
			 
			 //max up untuk rider ini
			 if (kurs.equalsIgnoreCase("01"))
				{
					if (up > 2000000000)
					{
						up = new Double(2000000000);
					}
				}else{
					if (up > 200000)
					{
						up = new Double(200000);
					}
				}
			 
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
	

	public void count_rate_814(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		ldec_temp5=0;
		ldec_rate5=0;
		String err="";
		String err1="";
		double hasil=0;

			int li_kali = 1;
			switch (pmode)
			{
				case 1 :
					li_kali = 4;
					break;	
				case 2 :
					li_kali = 2;
					break;
				case 6 :
					li_kali = 12 ;
					break;
			}	
			int li_umur_pp = 0;
			try {
				Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp5 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp5);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp5=bd.doubleValue();	
			 ldec_rate5 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate5);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate5=jm.doubleValue();		

	}	
	
	public void count_rate_815(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp6=0;
		ldec_rate6=0;

		double ldec_pct1 = 1;
		int li_kali = 1;
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
		
			int li_umur_pp = umurpp;
			try {
				Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 if(!err1.equals("")) throw new RuntimeException(err1);
			 else if(!err.equals("")) throw new RuntimeException(err);
				 ldec_temp6 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp6);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp6=bd.doubleValue();	
			 ldec_rate6 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate6);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate6=jm.doubleValue();		

	}	
		
	public void count_rate_816(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp7=0;
		ldec_rate7=0;

			int li_kali = 1;
			switch (pmode)
			{
				case 1 :
					li_kali = 4;
					break;	
				case 2 :
					li_kali = 2;
					break;
				case 6 :
					li_kali = 12 ;
					break;
			}	
			int li_umur_pp = 0;
	
			try {
				Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp7 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp7);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp7=bd.doubleValue();	
			 ldec_rate7 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate7);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate7=jm.doubleValue();		

	}	

	public void count_rate_817(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp8=0;
		ldec_rate8=0;

			int li_kali = 1;
			switch (pmode)
			{
				case 1 :
					li_kali = 4;
					break;	
				case 2 :
					li_kali = 2;
					break;
				case 6 :
					li_kali = 12 ;
					break;
			}	
			int li_umur_pp = umurpp;
	
			try {
				Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp8 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp8);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp8=bd.doubleValue();	
			 ldec_rate8 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate8);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate8=jm.doubleValue();			

	}	
	
	public void count_rate_818(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp9=0;
		ldec_rate9=0;

		try {
			Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, 2);
			
			if(result != null) {
				hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Term Tidak Ada !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp9 = ((up / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp9);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp9=bd.doubleValue();	
			 ldec_rate9 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate9);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate9=jm.doubleValue();			

	}	
	
	public void count_rate_827(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp7=0;
		ldec_rate7=0;
		int usia_pp =0;
		if (nomor_produk==4 || nomor_produk==5 )
		{
			usia_pp = 1;
		}

			int li_kali = 1;
			switch (pmode)
			{
				case 1 :
					li_kali = 4;
					break;	
				case 2 :
					li_kali = 2;
					break;
				case 6 :
					li_kali = 12 ;
					break;
			}	
			int li_umur_pp = 0;
			
			try {
				Double result = query.selectRateRider(kurs, umurttg, usia_pp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Waiver CI/TPD Tidak Ada!!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp7 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp7);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp7=bd.doubleValue();	
			 ldec_rate7 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate7);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate7=jm.doubleValue();			

	}
	
	public void count_rate_828(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp6=0;
		ldec_rate6=0;
		double ldec_pct1 = 1;
		int li_kali = 1;
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
			int li_umur_pp = umurpp;
			if (nomor_produk==3 || nomor_produk==2){
				umurttg=1;
			}
			try {
				Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi PAYOR 25 TPD/DEATH/CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 	
			 if(!err1.equals("")) throw new RuntimeException(err1);
			 else if(!err.equals("")) throw new RuntimeException(err);
				 ldec_temp6 = ((premi * li_kali / 1000) * hasil) * 0.1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp6);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp6=bd.doubleValue();	
			 ldec_rate6 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate6);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate6=jm.doubleValue();		

	}	

	public void count_rate_830(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
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
					err1="Rate Asuransi Ladies Tidak Ada !!!!";
				}
//			if(resultPremi != null) {
//				hasilPremi = resultPremi.doubleValue();
//				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 double factor_x=0;
			 
		/*	 if (persenUp==1){
				 factor_x = 0.5;
			 }else if(persenUp==2){
				 factor_x = 0.6;
			 }else if(persenUp==3){
				 factor_x = 0.7;
			 }else if(persenUp==4){
				 factor_x = 0.8;
			 }else if(persenUp==5){
				 factor_x = 0.9;
			 }else if(persenUp==6){
				 factor_x = 1.0;
			 }*/
			 
//			 if(persenUp != 0){
				 ldec_temp4 = (( up / 1000) * hasil ) * 0.1; // FIXIN last , 
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
	
/*	public String cek_min_up(Double premi, Double up, String kurs, Integer pmode_id)
	{
		String hasil_up="";
		double min_up1=0;
		of_set_premi(premi.doubleValue());
		min_up1=idec_up;
				
		if (up.doubleValue() < min_up1)
		{
			hasil_up="Up Minimum untuk plan ini "+ f.format(min_up1);
		}
		return hasil_up;
	}*/
	
	public String cek_min_up(Double premi, Double up, String kurs , Integer pmode_id)
	{
		String hasil_up="";
		double min_up1=0;
		double min_up2=0;
		of_set_premi(premi.doubleValue());
		min_up1=idec_up;
		
		double rate1 = 5;
		if (ii_bisnis_no == 1){ //helpdesk [142338] Perubahan minimum UP produk reguler premium UL Agency
			rate1 = 10;
		}
	
			if (kurs.equalsIgnoreCase("01"))
			{
				min_up1 = 7500000;
			}else{
				min_up1 = 750;
			}
			int fkt=1;
			if (pmode_id.intValue() == 1)
			{
				fkt = 4;
			}else if (pmode_id.intValue()==2)
				{
					fkt = 2;
				}else if (pmode_id.intValue() == 6)
					{
					fkt= 12;
					}
			//min_up1 = fkt*min_up1;
		      min_up2 = rate1 * premi.doubleValue() *fkt;
		
		
		double min = 0;
		if (min_up1 > min_up2)
		{
			min = min_up1;
		}else{
			min = min_up2;
		}
				
		if (up.doubleValue() < min && ii_bisnis_no != 2 )
		{
			hasil_up="Up Minimum untuk plan ini "+ f.format(min);
						
		}
		
		return hasil_up;
	}
	
	public String cek_max_up(Integer li_umur_ttg, Integer kode_produk, Double premi, Double up, Double fltpersen, Integer pmode_id,String kurs)
	{
		
		String hasil_up="";
		int faktor = 0; 
//		if (li_umur_ttg.intValue() < 6)
//		{
//			if ((up.doubleValue()) > 500000000)
//			{
//				hasil_up="Up Maksimum untuk usia dibawah 6 tahun adalah  Rp 500.000.000,-";
//			}
//		}else{	
			try {
					Integer result = query.faktorexcell80(li_umur_ttg.intValue(), 153);
				
					if (result!=null)
					{		
						faktor=result.intValue();
						String ff=Integer.toString(faktor);
						double nil =((premi.doubleValue()) * Double.parseDouble(ff)) * fltpersen.doubleValue();
			
						if ((up.doubleValue()) > nil)
						{
							hasil_up="Up Maksimum untuk plan ini "+ f.format(nil);
						}
					}else{
						
						hasil_up="Tidak ada data faktor";
					 }
			 } catch (Exception e) {
				err=(e.toString());
				}
//		}
		  return hasil_up;
	}
	
	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
		if(kurs.equalsIgnoreCase("01"))
		{
			switch (pmode_id.intValue())
			{
				case 1:
					if (total_premi < 750000)
					{
						hsl=" Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 750.000,-";
					}
					break;
				case 2:
					if (total_premi < 1500000)
					{
						hsl=" Minimum Total Premi Semesteran (6 bulanan ) : Rp. 1.500.000,-";
					}
					break;
				case 3:
					if (total_premi < 3000000)
					{
						hsl=" Minimum Total Premi Tahunan : Rp. 3.000.000,-";
					}
					break;
				case 6:
					if (total_premi < 300000)
					{
						hsl=" Minimum Total Premi Bulanan (1 bulanan) : Rp. 300.000,-";
					}
					break;
			}
		}else{
			switch (pmode_id.intValue())
			{
				case 1:
					if (total_premi < 75)
					{
						hsl=" Minimum Total Premi Triwulanan (3 bulanan ) : US$   75-";
					}
					break;
				case 2:
					if (total_premi < 150)
					{
						hsl=" Minimum Total Premi Semesteran (6 bulanan ) :US$   1,-";
					}
					break;
				case 3:
					if (total_premi < 300)
					{
						hsl=" Minimum Total Premi Tahunan : US$   300";
					}
					break;
				case 6:
					if (total_premi < 30)
					{
						hsl=" Minimum Total Premi Bulanan (1 bulanan) : US$ 30";
					}
					break;
			}
		}
	return hsl;
	}
	
	public String min_topup(Integer pmode_id, double premi , double premi_topup, String kurs, int jenis_topup)
	{
		String hsl="";
		if (jenis_topup==2)
		{
			if (kurs.equalsIgnoreCase("01"))
			{
				if (premi_topup < 1000000)
				{
					hsl=" Minimum Top-Up Tunggal : Rp. 1.000.000.";
				}
				if (premi_topup > 5e10)
				{
					hsl=" Maksimum Top-Up Tunggal dan Berkala sebesar Rp. 50.000.000.000,- untuk satu tahun Polis";
				}
			}else{
				if (premi_topup < 100)
				{
					hsl=" Minimum Top-Up Tunggal : US$ 100.";
				}
				if (premi_topup > 5000000)
				{
					hsl=" Maksimum Top-Up Tunggal dan Berkala sebesar US$ 5.000.000 untuk satu tahun Polis";
				}
			}
		}else if (jenis_topup==1){
			if (kurs.equalsIgnoreCase("01"))
			{
				double total_premi = premi + premi_topup ;
				
				switch (pmode_id.intValue())
				{
					case 1:
						if (total_premi < 750000)
						{
							hsl=" Minimum Total Premi (Premi + Top Up Berkala) Triwulanan (3 bulanan ) : Rp. 750.000,-";
						}
						break;
					case 2:
						if (total_premi < 1500000)
						{
							hsl=" Minimum Total Premi (Premi + Top Up Berkala) Semesteran (6 bulanan ) : Rp. 1.500.000,-";
						}
						break;
					case 3:
						if (total_premi < 3000000)
						{
							hsl=" Minimum Total Premi (Premi + Top Up Berkala) Tahunan : Rp. 3.000.000,-";
						}
						break;
					case 6:
						if (total_premi < 300000)
						{
							hsl=" Minimum Total Premi (Premi + Top Up Berkala) Bulanan (1 bulanan) : Rp. 300.000,-";
						}
						break;
				}				
				
			}else{
				
				double total_premi = premi + premi_topup ;
				switch (pmode_id.intValue())
				{
					case 1:
						if (total_premi < 75)
						{
							hsl=" Minimum Total Premi (Premi + Top Up Berkala) Triwulanan (3 bulanan ) : US$   75-";
						}
						break;
					case 2:
						if (total_premi < 150)
						{
							hsl=" Minimum Total Premi (Premi + Top Up Berkala) Semesteran (6 bulanan ) :US$   1,-";
						}
						break;
					case 3:
						if (total_premi < 300)
						{
							hsl=" Minimum Total Premi (Premi + Top Up Berkala) Tahunan : US$   300";
						}
						break;
					case 6:
						if (total_premi < 30)
						{
							hsl=" Minimum Total Premi (Premi + Top Up Berkala) Bulanan (1 bulanan) : US$ 30";
						}
						break;
				}
				
			}
		}
				
		
		return hsl;
	}
	
	
	public static void main(String[] args) {

	}
}
