//n_prod_166 Amanah Link Syariah 
package produk_asuransi;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

/*
 * Created on Dec 18, 2007
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
public class n_prod_166 extends n_prod{
	f_hit_umur umr = new f_hit_umur();
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	public n_prod_166()
	{
		
		flag_warning_autodebet = 1;//1 seharusnya autodebet, tapi tidak diblokir bila pilihannya TUNAI, hanya diberikan warning
				
		ii_bisnis_id = 166;
		ii_contract_period = 80;
		ii_age_from = 0;
		ii_age_to = 65;

		indeks_is_forex=1; //hanya sampai rupiah
		
		is_forex= new String[indeks_is_forex];
		is_forex[0] = "01";
		
//		  1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 3  ; //tahunan
		ii_pmode_list[2] = 2 ;  //semester
		ii_pmode_list[3] = 1 ;  //Tri
		ii_pmode_list[4] = 6 ;  //bulanan

		indeks_li_pmode=5;
		li_pmode = new int[indeks_li_pmode];

//		  untuk hitung end date ( 79 - issue_date )
		ii_end_from = 80;
//		ib_flag_end_age = False
	  	idec_min_up01 = 10000000;
	  	idec_min_up02 = 1000;
	  	
		indeks_ii_lama_bayar=2;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 10;
		ii_lama_bayar[1] = 10;

		indeks_idec_list_premi=20;
		idec_list_premi= new double[indeks_idec_list_premi];
		for (int i = 0 ; i < 13 ;i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.27; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
		
		flag_uppremi=1;
		flag_uppremiopen=1;
		li_pct_biaya = 0;
		
		indeks_rider_list=14;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=810;
		ii_rider[1]=811;
		ii_rider[2]=812;
		ii_rider[3]=813;
		//ii_rider[4]=814;
		ii_rider[4]=815;	
		ii_rider[5]=816;
		ii_rider[6]=817;	
		ii_rider[7]=819;
		ii_rider[8]=822;
		ii_rider[9]=823;
		ii_rider[10]=825;
		ii_rider[11]=827;
		ii_rider[12]=828;
		ii_rider[13]=832;
		
		flag_account =2;
		flag_rider=1;												
		flag_excell80plus = 1;
		//Yusuf - 20050203
		isInvestasi = true;
		flag_platinumlink = 1;
		isProductBancass = true;
		flag_jenis_plan = 5;
		usia_nol = 1;
		   indeks_kombinasi = 6;
		   kombinasi = new String[indeks_kombinasi];
		   kombinasi[0] = "A"; // pp 100% 
		   kombinasi[1] = "C"; // pp 80% - ptb 20%
		   kombinasi[2] = "E"; // pp 60% - ptb 40%
		   kombinasi[3] = "F"; // pp 50% - ptb 50%
		   kombinasi[4] = "G"; // pp 40% - ptb 60%
		   kombinasi[5] = "I"; // pp 20% - ptb 80%
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
		if(ii_bisnis_no==1){
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				if (ii_pmode==3)
				{
					ldec_1 =1000000; 
				}else{
					if (ii_pmode==1)
					{
						ldec_1 = 250000 ;
					}else{
						if (ii_pmode==2)
						{
							ldec_1 = 500000 ;
						}else{
							if (ii_pmode==6)
							{
								ldec_1 = 83333 ;
							}else{
								if (ii_pmode==0)
								{
									ldec_1 = 1000000 ;
								}
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
					ldec_1 =1000000; 
				}else{
					if (ii_pmode==1)
					{
						ldec_1 = 250000 ;
					}else{
						if (ii_pmode==2)
						{
							ldec_1 = 500000 ;
						}else{
							if (ii_pmode==6)
							{
								ldec_1 = 83333 ;
							}else{
								if (ii_pmode==0)
								{
									ldec_1 = 1000000 ;
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
		  if (ib_single_premium)
		  {
			  ii_pay_period = ii_lama_bayar[1];
		  }else{
			  ii_pay_period = ii_lama_bayar[0];
		  }
		
			//ii_pay_period = ii_lama_bayar[0];
		return ii_pay_period;
	  }	
	  
//	 of_set_pmode
		public void of_set_pmode(int ai_pmode)
		{
			ii_pmode = ai_pmode;
			if (ii_pmode == 0){
				ib_single_premium = true ;
			}
			idec_add_pct = idec_pct_list[ii_pmode];
			
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

		if (ii_bisnis_no == 1 || ii_bisnis_no == 2)
		{
			indeks_li_pmode=5;
			li_pmode[1] = 3;
			li_pmode[2] = 2;
			li_pmode[3] = 1;
			li_pmode[4] = 6;
			ii_pmode_list = li_pmode;
			indeks_ii_pmode_list=indeks_li_pmode;
			ii_bisnis_no_utama = ii_bisnis_no;
			ii_lbayar = ii_lama_bayar[0];
			of_set_pmode(ii_pmode_list[1]);
		}else{
			indeks_li_pmode=2;
			li_pmode[1] = 0;
			ii_pmode_list = li_pmode;
			indeks_ii_pmode_list=indeks_li_pmode;
			ii_bisnis_no_utama = ii_bisnis_no;
			ii_lbayar = ii_lama_bayar[1];
			of_set_pmode(ii_pmode_list[1]);
		}
	}

//	of_set_kurs
	 public void of_set_kurs(String as_kurs)
	 {
		is_kurs_id = as_kurs;
		if (as_kurs.equalsIgnoreCase("01"))
		{
			
			kode_flag=13;
			if (ii_bisnis_no == 1 || ii_bisnis_no == 2)
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
		
		idec_rate = 5000;

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
				}else{
					if (ii_pmode == 0)
					{
						//sekaligus
						idec_rate = 1250;
					}
				}
			}
		}

		idec_up = adec_premi * idec_rate / 1000;
		of_set_up(idec_up);
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
		if(ii_bisnis_no==2){
			ii_age_to=60;
		}else{
			ii_age_to=65;
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		return hsl;		
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


	public double cek_awal()
	{
	/*	if (is_kurs_id.equalsIgnoreCase("02") ) 
			li_id = 19;
		String err="";
		double hasil=0;
		try {
			Double result = query.selectDefault(li_id);
			
			if(result != null) {
				hasil = result.doubleValue();
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 	*/
		
		double hasil=0;
		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			hasil = 27500;
		}else{
			hasil = 275;
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
	
	//biaya akuisisi
	public void biaya1(double ld_bia_akui, double premi)
	{
		mbu_jumlah1=0;	
		mbu_persen1=0;	
		
		
	}
//	biaya asuransi
	public void biaya2(double ld_bia_ass, double premi, int ldec_pct, double up)
	{
			mbu_jumlah2=0;	
			mbu_persen2=0;	
	}

	//biaya administrasi
	public void biaya3(double ldec_awal)
	{
			mbu_jumlah3=0;		
	}

	//pa
	public void count_rate_810(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
	{
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp1=0;
		ldec_rate1=0;
		int li_kali1 = 1;
		double ldec_pct1 = 1;
		
		if (pmode== 1)  //triwulan
		{
			li_kali1 = 4;
			ldec_pct1 = 0.27;
		}else if (pmode == 2 ) //semesteran
			{
				li_kali1 = 2;
				ldec_pct1 = 0.525;
			}else if (pmode ==  6) //bulanan
				{
					li_kali1 = 12;
					ldec_pct1 = 0.1;
				}

			try {
				
				Double result = query.selectRateRider("01", 0, 0, 810, klas);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi PA Tidak Ada !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp1 = ((unit * up / 1000) * hasil) * ldec_pct1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp1);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp1=bd.doubleValue();	
			 ldec_rate1 = hasil;
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
		int li_kali1 = 1;
		double ldec_pct1 = 1;
		
		if (pmode== 1)  //triwulan
		{
			li_kali1 = 4;
			ldec_pct1 = 0.27;
		}else if (pmode == 2 ) //semesteran
			{
				li_kali1 = 2;
				ldec_pct1 = 0.525;
			}else if (pmode ==  6) //bulanan
				{
					li_kali1 = 12;
					ldec_pct1 = 0.1;
				}
	

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
			 ldec_temp2 = hasil *  ldec_pct1;
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
		int li_kali1 = 1;
		double ldec_pct1 = 1;
		int li_usia = umurttg + 1 - 1;
		if (pmode== 1)  //triwulan
		{
			li_kali1 = 4;
			ldec_pct1 = 0.27;
		}else if (pmode == 2 ) //semesteran
			{
				li_kali1 = 2;
				ldec_pct1 = 0.525;
			}else if (pmode ==  6) //bulanan
				{
					li_kali1 = 12;
					ldec_pct1 = 0.1;
				}
		
			try {
				
				Double result = query.selectRateRider(kurs, li_usia, 0, kode_produk, 1);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi HCP Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp3 = (( up / 1000) * hasil) * ldec_pct1;
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
		int li_kali1 = 1;
		double ldec_pct1 = 1;
		int li_usia = umurttg + 1 - 1;
		if (pmode== 1)  //triwulan
		{
			li_kali1 = 4;
			ldec_pct1 = 0.27;
		}else if (pmode == 2 ) //semesteran
			{
				li_kali1 = 2;
				ldec_pct1 = 0.525;
			}else if (pmode ==  6) //bulanan
				{
					li_kali1 = 12;
					ldec_pct1 = 0.1;
				}
		
			try {
				
				Double result = query.selectRateRider(kurs,  li_usia , 0, kode_produk, 1);
//				Double resultPremi = query.selectResultPremi(42, reg_spaj);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
//				if(resultPremi != null) {
//					hasilPremi = resultPremi.doubleValue();
//					}
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
				 ldec_temp4 = (( up / 1000) * hasil ) * ldec_pct1;
//			 }else{
//				 ldec_temp4 = hasilPremi; 
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
		int li_kali1 = 1;
		double ldec_pct1 = 1;
		int li_usia = umurttg + 1 - 1;
		if (pmode== 1)  //triwulan
		{
			li_kali1 = 4;
			ldec_pct1 = 0.27;
		}else if (pmode == 2 ) //semesteran
			{
				li_kali1 = 2;
				ldec_pct1 = 0.525;
			}else if (pmode ==  6) //bulanan
				{
					li_kali1 = 12;
					ldec_pct1 = 0.1;
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
				
				Double result = query.selectRateRider(kurs, li_usia, 1, kode_produk, 5);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp5 = ((premi * li_kali / 1000) * hasil) *ldec_pct1;
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
		int li_kali1 = 1;
		double ldec_pct1 = 1;
		int li_tahun_ke = umurpp + 1 - 1;
		if (pmode== 1)  //triwulan
		{
			li_kali1 = 4;
			ldec_pct1 = 0.27;
		}else if (pmode == 2 ) //semesteran
			{
				li_kali1 = 2;
				ldec_pct1 = 0.525;
			}else if (pmode ==  6) //bulanan
				{
					li_kali1 = 12;
					ldec_pct1 = 0.1;
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
			int li_umur_pp = umurpp;
			try {
				
				Double result = query.selectRateRider(kurs, 1, li_tahun_ke, kode_produk,5);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 				 if(!err1.equals("")) throw new RuntimeException(err1);
			 else if(!err.equals("")) throw new RuntimeException(err);
	
			 ldec_temp6 = ((premi * li_kali / 1000) * hasil) * ldec_pct1;
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
		int li_kali1 = 1;
		double ldec_pct1 = 1;
		int li_usia = umurttg +1 - 1;
		if (pmode== 1)  //triwulan
		{
			li_kali1 = 4;
			ldec_pct1 = 0.27;
		}else if (pmode == 2 ) //semesteran
			{
				li_kali1 = 2;
				ldec_pct1 = 0.525;
			}else if (pmode ==  6) //bulanan
				{
					li_kali1 = 12;
					ldec_pct1 = 0.1;
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
				
				Double result = query.selectRateRider(kurs, li_usia,1, kode_produk, 3);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp7 = ((premi * li_kali / 1000) * hasil) * ldec_pct1;
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
		int li_tahun_ke = umurpp + 1 - 1;
		int li_kali1 = 1;
		double ldec_pct1 = 1;
		
		if (pmode== 1)  //triwulan
		{
			li_kali1 = 4;
			ldec_pct1 = 0.27;
		}else if (pmode == 2 ) //semesteran
			{
				li_kali1 = 2;
				ldec_pct1 = 0.525;
			}else if (pmode ==  6) //bulanan
				{
					li_kali1 = 12;
					ldec_pct1 = 0.1;
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
			int li_umur_pp = umurpp;
	
			try {
				
				Double result= query.selectRateRider(kurs,1, li_tahun_ke, kode_produk, 3);
				
				if(result != null) {
					hasil=result.doubleValue();
				}else{
					err1="Rate Asuransi CI Tidak Ada  !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 		
			 ldec_temp8 = ((premi * li_kali / 1000) * hasil) * ldec_pct1;
			 int decimalPlace = 2;
			 BigDecimal bd = new BigDecimal(ldec_temp8);
			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_temp8=bd.doubleValue();	
			 ldec_rate8 =hasil;
			 BigDecimal jm = new BigDecimal(ldec_rate8);
			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			 ldec_rate8=jm.doubleValue();			

	}	
	
	public void count_rate_827(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
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
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
				
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
	
	public String cek_min_up(Double premi, Double up, String kurs, Integer pmode_id)
	{
		String hasil_up="";
		double min_up1=0;
		of_set_premi(premi.doubleValue());
		min_up1=idec_up;
				
		if (up.doubleValue() < min_up1)
		{
			if(up.doubleValue() < 7500000){
				hasil_up="Up Minimum untuk plan ini Rp. 7.500.000,-";
			}else{
				hasil_up="Up Minimum untuk plan ini "+ f.format(min_up1);
			}
		}
		return hasil_up;
	}
	
	public String cek_max_up(Integer li_umur_ttg, Integer kode_produk, Double premi, Double up, Double fltpersen, Integer pmode_id,String kurs)
	{
		String hasil_up="";
		int faktor = 0;
		try {
			Integer result = query.faktorexcell80(li_umur_ttg.intValue(),153);
			result = 100;
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
		
		double premi_s=idec_premi;
		if (ii_pmode == 1)
		{
			//triwulan
			premi_s = premi_s * 4;
		}else{
			if (ii_pmode == 2)
			{
				//semesteran
				premi_s = premi_s * 2;
			}else{
				if (ii_pmode == 6)
				{
					//bulanan
					premi_s = premi_s * 12;
				}
			}
		}

		double up_s = premi_s * idec_rate / 1000;
		
		if (up.doubleValue() < up_s)
		{
			hasil_up="Uang Pertanggungan minimum untuk plan ini " + up_s;
		}
		
		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			if (li_umur_ttg.intValue()<=5)
			{
				if (up.doubleValue() > 500000000)
				{
					hasil_up="Uang Pertanggungan maksimum untuk plan ini untuk Usia Masuk 1 – 5 tahun ditetapkan sebesar: Rp. 500.000.000,- / US$ 50.000 per Tertanggung";
				}
			}else if (li_umur_ttg.intValue() >=6 && li_umur_ttg.intValue()<=19)
				{
					if (up.doubleValue() > 1000000000)
					{
						hasil_up="Uang Pertanggungan maksimum untuk plan ini untuk Usia Masuk 6 – 19 tahun ditetapkan sebesar: Rp. 1.000.000.000,- / US$ 100.000 per Tertanggung";
					}
				}
		}else{
			if (li_umur_ttg.intValue()<=5)
			{
				if (up.doubleValue() > 50000)
				{
					hasil_up="Uang Pertanggungan maksimum untuk plan ini untuk Usia Masuk 1 – 5 tahun ditetapkan sebesar: Rp. 500.000.000,- / US$ 50.000 per Tertanggung";
				}
			}else if (li_umur_ttg.intValue() >=6 && li_umur_ttg.intValue()<=19)
			{
				if (up.doubleValue() > 100000)
				{
					hasil_up="Uang Pertanggungan maksimum untuk plan ini untuk Usia Masuk 6 – 19 tahun ditetapkan sebesar: Rp. 1.000.000.000,- / US$ 100.000 per Tertanggung";
				}
			}		
		}
		  return hasil_up;
	}
	
	public void wf_set_rider(int tahun,int bulan,int tanggal, int cuti_premi,int li_umur_ttg, int li_umur_pp)
	{
		int li_month=0;
		int li_bulan = 0;
		
		//ld_temp = Relativedate(date(f_add_months(istr_polis.beg_date, li_insured * 12)), -1)
	/*	tanggal_sementara = umr.f_add_months(tahun,bulan,tanggal,cuti_premi * 12);
		//ldt_edate.set(tanggal_sementara.getYear()+1900,tanggal_sementara.getMonth(),tanggal_sementara.getDate());
		int tgl1 = tanggal_sementara.getDate();
		int bln1 = tanggal_sementara.getMonth()+1;
		int thn1 = tanggal_sementara.getYear()+1900;
		ldt_edate.set(thn1,bln1,tgl1);
		ldt_edate.add(ldt_edate.DATE,-1);
		
		logger.info(ldt_edate.getTime().getYear()+1900);
		logger.info(ldt_edate.getTime().getMonth()+1);
		logger.info(ldt_edate.getTime().getDate());


		tanggal_sementara1=umr.f_add_months(ldt_edate.getTime().getYear()+1900,ldt_edate.getTime().getMonth()+1,ldt_edate.getTime().getDate(), - 1);
		//ldt_epay = f_add_months(istr_polis.beg_date, ( li_insured * 12) - 1)
		tgl1=tanggal_sementara1.getDate();
		bln1 = tanggal_sementara1.getMonth()+1;
		thn1 = tanggal_sementara1.getYear()+1900;
		ldt_epay.set(thn1,bln1,tgl1);*/
		
		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			li_kali = 1000;
		}
		
		
		if (ii_pmode == 0)
		{
			li_insured=( 60 - li_umur_ttg - 2);
			li_month = (( 60 - li_umur_ttg - 2) * 12);
			
		}else{
			li_month = ( cuti_premi  * 12);
			li_insured=cuti_premi;
			if (ii_pmode == 3)
			{
				li_bulan = ( cuti_premi  * 11);
			}else{
				li_bulan = ( cuti_premi  * 12);
			}
		}
		
		ldt_edate.set(tahun,bulan-1,tanggal);
		ldt_epay.set(tahun,bulan-1,tanggal);		
		adt_bdate.set(tahun,bulan-1,tanggal);
		ldt_edate.add(adt_bdate.MONTH,li_month);
		if (ii_pmode == 3)
		{
			ldt_epay.add(adt_bdate.YEAR,cuti_premi-1);
			//ldt_epay.add(ldt_edate.MONTH,1);
		}else{
			ldt_epay.add(adt_bdate.MONTH,li_bulan);
		}
		
		if (ii_pmode == 1)
		{
			ldt_epay.add(ldt_epay.MONTH,-3);
		}else{
			if  (ii_pmode == 2)
			{
				ldt_epay.add(ldt_epay.MONTH,-6);
			}else{
				if  (ii_pmode == 6)
				{
					ldt_epay.add(ldt_epay.MONTH,-1);
				}else{
					if  (ii_pmode == 0)
					{
						ldt_edate.set(tahun,bulan-1,tanggal);
						ldt_epay.set(tahun,bulan-1,tanggal);		
						adt_bdate.set(tahun,bulan-1,tanggal);
						
						ldt_edate.add(adt_bdate.YEAR,2);
						ldt_edate.add(ldt_edate.MONTH,li_month);
						
						ldt_epay.add(adt_bdate.YEAR,2);
						ldt_epay.add(ldt_epay.MONTH,li_month);
						
						adt_bdate.add(adt_bdate.YEAR,2);						
						
						if (adt_bdate.DAY_OF_MONTH  == ldt_edate.DAY_OF_MONTH )
						{
							ldt_edate.add(ldt_edate.DAY_OF_MONTH , -1);
							//ldt_epay.add(ldt_epay.DAY_OF_MONTH , -1);
						}
						ldt_epay.add(ldt_edate.MONTH,-1);
					}
				}
			}
		}
		
		/*logger.info(ldt_edate.getTime().getDate());
		logger.info(ldt_edate.getTime().getMonth()+1);
		logger.info(ldt_edate.getTime().getYear()+1900);*/
		if  (ii_pmode != 0)
		{
			if (adt_bdate.DAY_OF_MONTH  == ldt_edate.DAY_OF_MONTH )
			{
				ldt_edate.add(ldt_edate.DAY_OF_MONTH , -1);
				//ldt_epay.add(ldt_epay.DAY_OF_MONTH , -1);
			}
		}

		/*logger.info(ldt_edate.getTime().getDate());
		logger.info(ldt_edate.getTime().getMonth()+1);
		logger.info(ldt_edate.getTime().getYear()+1900);*/
		
	}
	
	public String cek_rider_number(int nomor_produk,int kode_produk,int umurttg, int umurpp, double up, double premi, int pmode,int hub, String kurs, int pay_period)
	{
		
		String hsl="";
		/*if (kode_produk == 810)
		{
			if (nomor_produk!= 1)
			{
				hsl="Rider PA ini tidak bisa dapat ditambahkan untuk produk ini.";
			}
		}*/
		
		
		if ( kode_produk == 815 && nomor_produk == 6)
		{
			if (hub !=5)
			{
				hsl =" Untuk Produk PAYOR SPOUSE 60 TPD/DEATH- RIDER  hubungan pemegang dan tertanggung harus suami/istri.";
			}
		}
		
		if (pmode == 0)
		{
			if (kode_produk >=814 && kode_produk <=818 )
			{
				hsl="Untuk Platinum Link Single hanya bisa menambahkan rider PA, HCP, CI, TPD saja.";
			}
		}else{
			if(ii_bisnis_no!=2){
				if (kode_produk == 814)
				{
					if (nomor_produk!= 5)
					{
						hsl="Rider Waiver TPD ini tidak bisa dapat ditambahkan untuk produk ini.";
					}
				}
				
				if (kode_produk == 815)
				{
					if (nomor_produk!= 5)
					{
						hsl="Rider Payor TPD ini tidak bisa dapat ditambahkan untuk produk ini.";
					}
				}	
				
				if (kode_produk == 816)
				{
					if (nomor_produk!= 3)
					{
						hsl="Rider Waiver CI ini tidak bisa dapat ditambahkan untuk produk ini.";
					}
				}
				
				if (kode_produk == 817)
				{
					if ((nomor_produk >=1 && nomor_produk <= 4))
					{
						hsl="Rider Payor CI ini tidak bisa dapat ditambahkan untuk produk ini.";
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
				
				if (kode_produk == 811)
				{
					if ((nomor_produk != 5 && nomor_produk != 10 && nomor_produk != 15 && nomor_produk != 20))
					{
						hsl="Rider Payor HCP ini tidak bisa dapat ditambahkan untuk produk ini.";
					}
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
	

	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
		if (kurs.equalsIgnoreCase("01"))
		{
			switch (pmode_id.intValue())
			{

				case 1:
					if (total_premi < 1250000)
					{
						hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp.   1.250.000,- ";
					}
					break;
				case 2:
					if (total_premi < 2500000)
					{
						hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp. 2.500.000,- ";
					}
					break;
				case 3:
					if (total_premi < 5000000)
					{
						hsl="Minimum Total Premi Tahunan : Rp. 5.000.000,- ";
					}
					break;
				case 6:
					if (total_premi < 500000)
					{
						hsl="Minimum Total Premi Bulanan (1 bulanan) : Rp.   500.000,- ";
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
					hsl="Minimum Top-Up Tunggal : Rp. 1.000.000.";
				}
			}else{
				if (premi_topup < 100)
				{
					hsl="Minimum Top-Up Tunggal : US$ 100.";
				}	
			}
		}
		
		if (kurs.equalsIgnoreCase("01"))
		{
			if (premi_topup > 1e10)
			{
				hsl="Maksimum Top-Up Tunggal dan Berkala sebesar Rp. 10.000.000.000. untuk satu tahun Polis";
			}
		}else{
			if (premi_topup >1000000)
			{
				hsl="Maksimum Top-Up Tunggal dan Berkala sebesar US$ 1.000.000 untuk satu tahun Polis";
			}	
		}
		
		double total_premi = premi + premi_topup;
		if (kurs.equalsIgnoreCase("01"))
		{
			switch (pmode_id.intValue())
			{

			case 1:
				if (total_premi < 1250000)
				{
					hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp.   1.250.000,- ";
				}
				break;
			case 2:
				if (total_premi < 2500000)
				{
					hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp. 2.500.000,- ";
				}
				break;
			case 3:
				if (total_premi < 5000000)
				{
					hsl="Minimum Total Premi Tahunan : Rp. 5.000.000,- ";
				}
				break;
			case 6:
				if (total_premi < 500000)
				{
					hsl="Minimum Total Premi Bulanan (1 bulanan) : Rp.   500.000,- ";
				}
				break;
			}
		}
		
		return hsl;
	}
	
	public static void main(String[] args) {

	}
}
