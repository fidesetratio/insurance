//n_prod_164 stable link
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

/**
 * @author Hemilda
 *
 *	REVISI TERAKHIR OLEH YUSUF (28/04/2008)
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_164 extends n_prod{

	public n_prod_164()
	{
		
		ii_bisnis_id = 164;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 65;//Diubah dari 70 menjadi 65 tgl 8 Januari 2009

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0] = "01";
		is_forex[1] = "02";
		
//		  1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0 ;  //sekaligus
		

		indeks_li_pmode=2;
		li_pmode = new int[indeks_li_pmode];

//		  untuk hitung end date ( 79 - issue_date )
		ii_end_from = 99;
		ib_flag_end_age = false;
	  	idec_min_up01 = 20000000;
	  	idec_min_up02 = 2000;
		idec_max_up01 = 200000000;
		idec_max_up02 = 20000;
		idec_min_up01_karyawan = 5000000;
		idec_min_up02_karyawan = 500;

		indeks_ii_lama_bayar=11;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 1;
		ii_lama_bayar[6] = 1;
		ii_lama_bayar[7] = 1;
		ii_lama_bayar[8] = 1;
		ii_lama_bayar[9] = 1;
		ii_lama_bayar[10] = 1;

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
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];	
		
		flag_account =2;
		flag_rider=1;												
		flag_excell80plus = 1;

		isInvestasi = true;
		usia_nol = 0;
		kode_flag=11;
		flag_powersave = 12;
		indeks_mgi=8;
		mgi = new int[indeks_mgi];
		mgi[0]= 1;
		mgi[1]= 3;
		mgi[2]= 6;
		mgi[3]= 9;
		mgi[4]= 12;
		mgi[5]= 24;
		mgi[6]= 36;
		
		   indeks_kombinasi = 19;
		   kombinasi = new String[indeks_kombinasi];
		   kombinasi[0] = "K"; // pp 95% - ptb 5%
		   kombinasi[1] = "B"; // pp 90% - ptb 10%
		   kombinasi[2] = "L"; // pp 85% - ptb 15%
		   kombinasi[3] = "C"; // pp 80% - ptb 20%
		   kombinasi[4] = "M"; // pp 75% - ptb 25%
		   kombinasi[5] = "D"; // pp 70% - ptb 30%
		   kombinasi[6] = "N"; // pp 65% - ptb 35%
		   kombinasi[7] = "E"; // pp 60% - ptb 40%
		   kombinasi[8] = "O"; // pp 55% - ptb 45%
		   kombinasi[9] = "F"; // pp 50% - ptb 50%
		   kombinasi[10] = "P"; // pp 45% - ptb 55%
		   kombinasi[11] = "G"; // pp 40% - ptb 60%
		   kombinasi[12] = "Q"; // pp 35% - ptb 65%
		   kombinasi[13] = "H"; // pp 30% - ptb 70%
		   kombinasi[14] = "R"; // pp 25% - ptb 75%
		   kombinasi[15] = "I"; // pp 20% - ptb 80%
		   kombinasi[16] = "S"; // pp 15% - ptb 85%
		   kombinasi[17] = "J"; // pp 10% - ptb 90%
		   kombinasi[18] = "T"; // pp 5% - ptb 95%
		   
		   flag_jenis_plan=7;
		   flag_cuti_premi = 0;
		   
		   indeks_rider_list=8;
			ii_rider = new int[indeks_rider_list];
			ii_rider[0]=813;
			ii_rider[1]=818;
			ii_rider[2]=819;
			ii_rider[3]=820;
			ii_rider[4]=822;
			ii_rider[5]=823;
			ii_rider[6]=825;
			ii_rider[7]=832;
	}
	
	public String of_check_usia_case_premi(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk,double premi) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if(premi>=1000000000){
				ii_age_to = 75;
			} 
		}else{
			if(premi>=100000){
				ii_age_to = 75;
			}
		}
		
		hsl="";
		if (bln < 6)
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
			
		return hsl;		
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if(idec_premi>=1000000000){
				ii_age_to = 75;
			}
		}else{
			if(idec_premi>=100000){
				ii_age_to = 75;
			}
		}
		
		hsl="";
		if (bln < 6)
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
			
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = 20000000; 
		}else{
			ldec_1 =2000; 
		}
		return ldec_1;
	}
	
	public boolean of_check_premi(double ad_premi)
	{
	
		return true;
	}		
	
	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =ii_contract_period;
		
		return li_cp;		
	}	
	
//	get pay period
	  public int of_get_payperiod()
	  {
		  if (ib_single_premium)
		  {
				ii_lama_bayar[0] = 1;
		  }else{
				ii_lama_bayar[0] = (ii_end_from - ii_usia_tt);
		  }
		ii_pay_period = ii_lama_bayar[0];
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

	public void of_set_bisnis_no(int ai_no) {
		ii_bisnis_no = ai_no;
		int position = ii_bisnis_no-1;
		if(position<0) position=0;

		indeks_li_pmode = 2;
		li_pmode[1] = 0;
		ii_pmode_list = li_pmode;
		indeks_ii_pmode_list = indeks_li_pmode;
		ii_bisnis_no_utama = ii_bisnis_no;
		ii_lbayar = ii_lama_bayar[position];
		of_set_pmode(ii_pmode_list[1]);
		
		flag_as=3;
		
		if(ii_bisnis_no > 1) {
			flag_as=0;
			isProductBancass = true;
			flag_artha = 0;
			if(ii_bisnis_no == 6){//Req Himmia : Stabil Link khusus Worksite (164-6)
				flag_worksite = 1;
				isProductBancass = false;
			}else if(ii_bisnis_no ==7 || ii_bisnis_no == 8){
				isProductBancass = false;
			}
		}else if(ii_bisnis_no == 1){
			flag_worksite = 1;
		}
		
	}

// of_set_kurs
	 public void of_set_kurs(String as_kurs)
	 {
		is_kurs_id = as_kurs;
		if(ii_bisnis_no == 11){
			kode_flag=16;
		}
		if (as_kurs.equalsIgnoreCase("02"))
		{
			if (ii_bisnis_no == 1 || ii_bisnis_no == 3|| ii_bisnis_no == 5 || ii_bisnis_no == 6)
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
			if (ii_bisnis_no == 1 || ii_bisnis_no == 3 || ii_bisnis_no == 5 || ii_bisnis_no == 6)
			{
				indeks_idec_list_premi=18;				
				for (int i = 0; i < 17 ; i++)
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
		
		idec_rate = 1250;
		
		if (ii_pmode == 1)
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

		idec_up = adec_premi * idec_rate / 1000;
		
		of_set_up(idec_up);
	}
	
	public void of_set_premi_karyawan(double adec_premi)
	{
		idec_premi_main = adec_premi;
		idec_premi = adec_premi;
		
		idec_rate = 1250;
		
		if (ii_pmode == 1)
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

		idec_up = adec_premi * idec_rate / 1000;
		if (is_kurs_id.equalsIgnoreCase("02") ) 
		{
			if(idec_up < 1500){
				idec_up=1500;
			}
		}else{
			if(idec_up < 15000000){
				idec_up=15000000;
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
					ld_bia=1;	
				}else if (ar_ke==2)
					{
						ld_bia=0.55;
					}else if (ar_ke==3)
						{
							ld_bia=0.35;
						}else if (ar_ke==4)
						{
							ld_bia=0.15;
						}else if (ar_ke ==5)
							{
								ld_bia=0.05;
							}else if (ar_ke >=6)
							{
								ld_bia=0;
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
			period = 99;	
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
		String err="";
		double hasil=0;
		if (is_kurs_id.equalsIgnoreCase("02") ) 
		{
			hasil = new Double(5);
		//	li_id = 19;
		}else{
			hasil= new Double(30000);
		}

		return hasil; 
	}

	public double cek_rate(int li_bisnis,int li_pmode,int pperiod,int li_insper,int umur_tt , String kurs,int insperiod)
	{
		String err="";
		double hasil=0;
		try {
			Double result = query.selectNilai(1, 115, "01", 3, 80, 80, 1, umur_tt);
			
			if(result != null) {
				hasil=result.doubleValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 		
		return hasil; 
	}
	
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
			 ldec_temp1 = ((unit * up / 1000) * hasil) * 0.1;
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
	
	public String cek_min_up(Double premi, Double up, String kurs , Integer pmode_id)
	{
		String hasil_up="";
		double min_up1=0;
		double min_up2=0;
		of_set_premi(premi.doubleValue());
		min_up1=idec_up;
		if (kurs.equalsIgnoreCase("01"))
		{
			min_up1 = 15000000;
		}else{
			min_up1 = 1500;
		}
		min_up2 = 125 * premi.doubleValue() /100;
		
		double min = 0;
		if (min_up1 > min_up2)
		{
			min = min_up1;
		}else{
			min = min_up2;
		}
		
		if (up.doubleValue() < min)
		{
			hasil_up="Up Minimum untuk plan ini "+ f.format(min);
		}
		
		return hasil_up;
	}
	
	public String cek_max_up(Integer li_umur_ttg, Integer kode_produk, Double premi, Double up, Double fltpersen, Integer pmode_id,String kurs )
	{
		String hasil_up="";
		return hasil_up;
	}
	
	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
		if(kurs.equalsIgnoreCase("01"))
		{
			if (total_premi < 20000000)
			{
				hsl="Minimum Total Premi Sekaligus : Rp. 20.000.000,-";
			}
		}else{
			if (total_premi < 2000)
			{
				hsl="Minimum Total Premi Sekaligus :  US$ 2000 ";
			}
		}
	return hsl;
	}
	
	public String min_total_premi_karyawan(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
		if(kurs.equalsIgnoreCase("01"))
		{
			if (total_premi < 5000000)
			{
				hsl="Minimum Total Premi Sekaligus : Rp. 5.000.000,-";
			}
		}else{
			if (total_premi < 500)
			{
				hsl="Minimum Total Premi Sekaligus :  US$ 2000 ";
			}
		}
	return hsl;
	}
	
	public String min_topup(Integer pmode_id, double premi , double premi_topup, String kurs, int jenis_topup)
	{
		String hsl="";
		double total_premi = premi + premi_topup;
		if(kurs.equalsIgnoreCase("01"))
		{
			if (total_premi < 20000000)
			{
				hsl="Minimum Total Premi Sekaligus : Rp. 20.000.000,-";
			}
		}else{
			if (total_premi < 2000)
			{
				hsl="Minimum Total Premi Sekaligus :  US$ 2000 ";
			}
		}

		return hsl;
	}
	
	public String cek_rider_number(int nomor_produk,int kode_produk,int umurttg, int umurpp, double up, double premi, int pmode,int hub, String kurs, int pay_period)
	{
		String hsl="";
		return hsl;
	}
	
//	get bisnis no
	@Override
	public int of_get_bisnis_no(int flag_bulanan){
		int flag_powersave = f_flag_rate_powersave(ii_bisnis_id, ii_bisnis_no, flag_bulanan);
		return flag_powersave;	
	}
	
	@Override
	public void cek_flag_agen(int kode_produk, int no_produk, int flag_bulanan){
		flag_powersave = f_flag_rate_powersave(ii_bisnis_id, ii_bisnis_no, flag_bulanan);
	}
	
	
	public void cek_mgi(int nomor_bisnis){
		/*
		if(ii_bisnis_no > 1) {
			indeks_mgi=7;
			mgi = new int[indeks_mgi];
			mgi[0]= 3;
			mgi[1]= 6;
			mgi[2]= 9;
			mgi[3]= 12;
			mgi[4]= 24;
			mgi[5]= 36;
		}else {
			indeks_mgi=8;
			mgi = new int[indeks_mgi];
			mgi[0]= 1;
			mgi[1]= 3;
			mgi[2]= 6;
			mgi[3]= 9;
			mgi[4]= 12;
			mgi[5]= 24;
			mgi[6]= 36;
		}
		*/
	}
	
	public String of_get_min_up_permgi(int nomor_bisnis , int mgi, double premi,String kurs)
	{
		String hasil="";
		return hasil;
	}

	public static void main(String[] args) {

	}
}
