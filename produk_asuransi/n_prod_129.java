package produk_asuransi;
import java.math.BigDecimal;

// n_prod_129 cerdas sejahtera
/*
 * Created on Oct 5, 2005
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
public class n_prod_129 extends n_prod {

	Query query = new Query();
	public n_prod_129()
	{
		
		flag_warning_autodebet = 1;//1 seharusnya autodebet, tapi tidak diblokir bila pilihannya TUNAI, hanya diberikan warning

//		 Cerdas Sejahtera - 129
		ii_bisnis_id = 129;
		ii_contract_period = 100;
		ii_age_from = 1;
		ii_age_to = 65;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 1 ;  //tahunan
		ii_pmode_list[2] = 2 ;  //semester
		ii_pmode_list[3] = 3 ;  //Tri
		ii_pmode_list[4] = 6 ;  //Bulanan		
		
//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 100;
//		ib_flag_end_age = False
		idec_min_up01 = 1000000;

		indeks_ii_lama_bayar=12;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 10;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 10;
		ii_lama_bayar[4] = 5;
		ii_lama_bayar[5] = 10;
		ii_lama_bayar[6] = 5;
		ii_lama_bayar[7] = 10;
		ii_lama_bayar[8] = 5;
		ii_lama_bayar[9] = 10;
		ii_lama_bayar[10] = 5;
		ii_lama_bayar[11] = 10;

		indeks_idec_list_premi=11;
		idec_list_premi = new double[indeks_idec_list_premi];
		for( int i = 0 ; i< 10 ; i++)
		{
			idec_list_premi[i] = 5000000 + ( 1000000 * (i - 1) );
		}
		
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1 ;// pmode 6		
		
		flag_uppremi=1;
		usia_nol = 1;
		kode_flag=4;
		indeks_rider_list=1;		
		flag_rider=1;
		flag_jenis_plan=2;
		flag_account =3;
		flag_reff_bii = 1;
		//Yusuf - 20050203
		isInvestasi = true;
		isProductBancass = true; 
		
		indeks_rider_list=7;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=810;
		ii_rider[1]=812;		
		ii_rider[2]=813;
		ii_rider[3]=814;
		ii_rider[4]=816;
		ii_rider[5]=823;
		ii_rider[6]=835;
		
	}
	
	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;
		if (as_kurs.equalsIgnoreCase("02"))
		{
			kode_flag=5;
		}else{
			kode_flag=4;
		}
	}
	
	public void f_kombinasi(){
		if (ii_bisnis_id==129){
			if(ii_bisnis_no==7 || ii_bisnis_no==8 || ii_bisnis_no==9 || ii_bisnis_no==10){
				   indeks_kombinasi = 6;
				   kombinasi = new String[indeks_kombinasi];
				   kombinasi[0] = "A"; // pp 100% 
				   kombinasi[1] = "C"; // pp 80% - ptb 20%				   
				   kombinasi[2] = "E"; // pp 60% - ptb 40%
				   kombinasi[3] = "F"; // pp 50% - ptb 50%
				   kombinasi[4] = "G"; // pp 40% - ptb 60%				   
				   kombinasi[5] = "I"; // pp 20% - ptb 80%
				   
				}else {
				   indeks_kombinasi = 5;
				   kombinasi = new String[indeks_kombinasi];				  
				   kombinasi[0] = "C"; // pp 80% - ptb 20%
				   kombinasi[1] = "E"; // pp 60% - ptb 40%
				   kombinasi[2] = "F"; // pp 50% - ptb 50%
				   kombinasi[3] = "G"; // pp 40% - ptb 60%
				   kombinasi[4] = "I"; // pp 20% - ptb 80%
				}
		}
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		String hsl="";
		ii_lbayar=lm_byr;
		if (ii_age < 17)
		{
			hsl="Usia Masuk Tertanggung minimum : 18 tahun";
		}		
//		if (ii_usia_pp > 85)
//		{
//			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
//		}
		if (ii_lbayar == 5)
		{
			if (ii_age > 55)
			{
				hsl="Usia Masuk Tertanggung maximum : 55 Tahun";
			}
		}else{
			if (ii_age > 50)
			{
				hsl="Usia Masuk Tertanggung maximum : 50 Tahun";
			}
		}
		
		return hsl;		
	}

	public boolean of_check_premi(double ad_premi)
	{
		boolean hsl=true;		
		if(ii_bisnis_no==7 || ii_bisnis_no==8 || ii_bisnis_no==9 || ii_bisnis_no==10|| ii_bisnis_no==11 || ii_bisnis_no==12){//product excelink dan cerdas untuk mall
		if (is_kurs_id.equalsIgnoreCase("01")){
		if (ii_pmode == 3)
		{
			if (ad_premi < 4000000)
			{
				hsl=false;
			}
		}else{
			if (ii_pmode == 2)
			{
				if (ad_premi < 2625000)
				{
					hsl= false;
				}
			}else{
				if (ii_pmode == 1)
				{
					if (ad_premi < 1350000)
					{ 
						hsl= false;
					}
				}else{
					if (ii_pmode == 6)
					{
						if (ad_premi < 500000)
						{
							hsl= false;
						}
					}
				}
			}
		}
		
	  }else{
		  if (ii_pmode == 3)
			{
				if (ad_premi < 400)
				{
					hsl=false;
				}
			}else{
				if (ii_pmode == 2)
				{
					if (ad_premi < 262.5)
					{
						hsl= false;
					}
				}else{
					if (ii_pmode == 1)
					{
						if (ad_premi < 135)
						{ 
							hsl= false;
						}
					}else{
						if (ii_pmode == 6)
						{
							if (ad_premi < 50)
							{
								hsl= false;
							}
						}
					}
				}
			}
	    }	
		
	   }else{//product cerdas
		   if (ii_pmode == 3)
			{
				if (ad_premi < 1000000)
				{
					hsl=false;
				}
			}else{
				if (ii_pmode == 2)
				{
					if (ad_premi < 525000)
					{
						hsl= false;
					}
				}else{
					if (ii_pmode == 1)
					{
						if (ad_premi < 270000)
						{ 
							hsl= false;
						}
					}else{
						if (ii_pmode == 6)
						{
							if (ad_premi < 100000)
							{
								hsl= false;
							}
						}
					}
				}
			} 
	    }
		return hsl;
 }
	
	public String cek_hubungan(String hub)
	{
		hsl="" ;
		if (Integer.parseInt(hub)!=1)
		{
			hsl="Khusus plan ini , hubungan pemegang polis dengan tertanggung adalah diri sendiri";
		}
		return hsl;
	}

	public int of_get_conperiod(int number_bisnis)
	{
		ii_contract_period=0;
		ii_contract_period =ii_end_from - ii_age;
		return ii_contract_period;		
	}
	
	public double of_get_rate()
	{
		return idec_rate;		
	}	
	
	public void of_set_premi(double adec_premi)
	{
		double min_up=7500000;
		int li_kali=1;
		idec_premi_main = adec_premi;
		idec_rate = 5000;
		//idec_up = (adec_premi * 1 / idec_add_pct) * 5;
		if(ii_bisnis_no==5 || ii_bisnis_no==6 || ii_bisnis_no==9 || ii_bisnis_no==10|| ii_bisnis_no==11 || ii_bisnis_no==12){ // *Product Exelling
			
			double fkt=1;
			if (ii_pmode == 1)
			{
				fkt = 0.25;
				li_kali=4;
			}else if (ii_pmode==2)
				{
					fkt = 0.525;
					li_kali=2;
				}else if (ii_pmode == 6)
					{
					fkt= 0.1;
					li_kali=12;
				}
			//min_up1 = fkt*min_up1;
			//adec_premi = adec_premi * (1/fkt);
			//idec_up = adec_premi * idec_rate / 1000;
		}else{
			// *Product Cerdas
			idec_up = (adec_premi / idec_add_pct) * idec_rate / 1000;
			ldec_sisa =  (idec_up % 1000);
			if (ldec_sisa != 0)
			{
				if (ldec_sisa >= 500)
				{
				   idec_up = idec_up + (1000 - ldec_sisa);
				}else{
				   idec_up = idec_up - ldec_sisa;
				}
			}
		}
		
		if(ii_bisnis_no==5 || ii_bisnis_no==6 || ii_bisnis_no==9 || ii_bisnis_no==10|| ii_bisnis_no==11|| ii_bisnis_no==12){
			
			
				if (is_kurs_id.equalsIgnoreCase("02")){
					
					min_up=750;
					idec_rate = 5000;
				
				}
		idec_up = adec_premi * li_kali * (idec_rate / 1000);
		if(idec_up < min_up){// *Cek klo UP lebih kecil dari minimum UP maka, UP = Minimum UP
			idec_up = min_up;
		} 
			  
		  
		}
		
		of_set_up(idec_up);
	}


	public double of_get_min_up()
	{
		double ldec_1=0;
		if(ii_bisnis_no==7 || ii_bisnis_no==8 || ii_bisnis_no==9 || ii_bisnis_no==10|| ii_bisnis_no==11 || ii_bisnis_no==12){ // *Product Excelling
			if (is_kurs_id.equalsIgnoreCase("01"))	{
				switch (ii_pmode)
				{
				case 3:
					idec_min_up01=5000000;
					break;
				case 1:
					idec_min_up01=1250000;
					break;
				case 2:
					idec_min_up01=2500000;
					break;
				case 6:
					idec_min_up01=500000;
					break;			
				}
			}else{
				
				switch (ii_pmode)
				{
				case 3:
					idec_min_up01=500;
					break;
				case 1:
					idec_min_up01=125;
					break;
				case 2:
					idec_min_up01=250;
					break;
				case 6:
					idec_min_up01=50;
					break;			
				}
			}
		}else if(ii_bisnis_no==5 || ii_bisnis_no==6){ // *Paket Excelling For Pension
			idec_min_up01=300000;
		}else{// *Product Cerdas
			switch (ii_pmode)
			{
				case 3:
					idec_min_up01=1000000;
					break;
				case 1:
					idec_min_up01=270000;
					break;
				case 2:
					idec_min_up01=525000;
					break;
				case 6:
					idec_min_up01=100000;
					break;			
			}
		}

		ldec_1 = idec_min_up01;
		return ldec_1;
	}
	
	public double of_get_max_up()
	{
		double ldec_1=0;
		if(ii_bisnis_no==7 || ii_bisnis_no==8 || ii_bisnis_no==9 || ii_bisnis_no==10){
			if (ii_age >=1 || ii_age <=19 )
			{
				idec_max_up01 = idec_premi * 50;
			}else if(ii_age >=20 || ii_age <=29){
				idec_max_up01 = idec_premi * 40;		
			}else if(ii_age >=30 || ii_age <=39){
				idec_max_up01 = idec_premi * 30;				
			}else if(ii_age >=40 || ii_age <=49){
				idec_max_up01 = idec_premi * 20;				
			}else if(ii_age >=50 || ii_age <=60){
				idec_max_up01 = idec_premi * 10;				
			}
		}
//		switch (ii_pmode)
//		{
//			case 3:
//				idec_max_up01=200000000;
//				break;
//			case 1:
//				idec_max_up01=200000000/0.27;
//				break;
//			case 2:
//				idec_max_up01=200000000/0.525;
//				break;
//			case 6:
//				idec_max_up01=200000000/0.1;
//				break;			
//		}

		ldec_1 = idec_max_up01;
		return ldec_1;
	}			

	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia=0;
		switch (ar_ke)
		{
			 case 1: 
				ld_bia=0.6;
			 break;
			 case 2:
				ld_bia=0.15;
			 break;
		}
		return ld_bia;	
	}
	
	public double cek_awal()
	{
		if (is_kurs_id.equalsIgnoreCase("02") ) 
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
		 } 		
		return hasil; 
	}

	public double cek_rate(int li_bisnis,int li_pmode,int pperiod,int li_insper,int umur_tt,  String kurs ,int insperiod)
	{

		String err="";
		double hasil=0;
		try {
			Double result = query.selectNilai(1, 115, "01", 3, 80, 80, 1, umur_tt);
			
			if(result != null) {
				hasil = result.doubleValue();
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
		int decimalPlace = 2;
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
		int decimalPlace = 2;
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
		ldec_rate=0;
		

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
			 ldec_temp1 = (( up/ 1000) * hasil) * 0.1;
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
				
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, 2);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi TPD Tidak Ada  !!!!";
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
		String err="";
		String err1="";
		double hasil=0;
		ldec_temp5=0;
		ldec_rate5=0;
		

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
				Double result = query.selectRateRider(kurs, umurttg, 1, kode_produk, nomor_produk);
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Waiver TPD Tidak Ada !!!!";
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
				
				Double result = query.selectRateRider(kurs, 1, umurpp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Payor TPD Tidak Ada !!!!";
				}
			   }
			 catch (Exception e) {
				   err=e.toString();
			 } 					 if(!err1.equals("")) throw new RuntimeException(err1);
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
				
				Double result = query.selectRateRider(kurs, umurttg, 1, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Waiver CI Tidak Ada!!!!";
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
				
				Double result = query.selectRateRider(kurs, 1, li_umur_pp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Payor CI Tidak Ada   !!!!";
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
				
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, 1);
				
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
			 if (nomor_produk > 15)
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
				Double result = query.selectRateRider(kurs, umurttg,usia_pp, kode_produk, nomor_produk);
				
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
	
	public void riderInclude(int nomor_produk)
	{
		if (nomor_produk==1 || nomor_produk==2 || nomor_produk==7 || nomor_produk==8)
		{
			indeks_rider_include=2;		
			rider_include = new int [indeks_rider_include];
			rider_include[0]=810;
			rider_include[1]=812;
			
			indeks_rider_list=2;
			ii_rider = new int[indeks_rider_list];
			ii_rider[0]=810;
			ii_rider[1]=812;
			
		}else if(nomor_produk==3 || nomor_produk==4 || nomor_produk==9 || nomor_produk==10){
			indeks_rider_include=4;		
			rider_include = new int [indeks_rider_include];
			rider_include[0]=810;
			rider_include[1]=812;
			rider_include[2]=813;
			rider_include[3]=816;	
			
			indeks_rider_list=4;
			ii_rider = new int[indeks_rider_list];
			ii_rider[0]=810;
			ii_rider[1]=812;	
			ii_rider[2]=813;
			ii_rider[3]=816;			
		}

	}

	public void cek_rider_include(int nomor_produk,int kode_produk,int umurttg, int umurpp, double up, double premi, int pmode)
	{
		units=0;
		klases=0;

			if (kode_produk==810)
			{
				 nomor_rider_include=1;
				 units=2;
				 klases=1;
			}

			if (kode_produk==812)
			{
				 nomor_rider_include=2;
			}


		if (nomor_produk ==3 || nomor_produk==4 || nomor_produk ==9 || nomor_produk==10)
		{
			if (kode_produk==813)
			{
				nomor_rider_include=8;
			}	
			
			if (nomor_produk ==3 || nomor_produk ==9 )
			{
				if (kode_produk==816)
				{
					nomor_rider_include=4;
				}					
			}else{
				if (nomor_produk ==4 || nomor_produk ==10 )
				{
					if (kode_produk==816)
					{
						nomor_rider_include=5;
					}						
				}
			}
		}

	}	
	
	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
		switch (pmode_id.intValue())
		{

			case 1:
				if (total_premi < 1350000)
				{
					hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 1.350.000,-";
				}
				break;
			case 2:
				if (total_premi < 2625000)
				{
					hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp. 2.625.000,-";
				}
				break;
			case 3:
				if (total_premi < 4000000)
				{
					hsl="Minimum Total Premi Tahunan : Rp. 4.000.000.-";
				}
				break;
			case 6:
				if (total_premi < 500000)
				{
					hsl="Minimum Total Premi Bulanan (1 bulanan) : Rp. 500.000,-";
				}
				break;
		}
		
		
	return hsl;
	}
	
	public String min_topup(Integer pmode_id, double premi , double premi_topup, String kurs, int jenis_topup)
	{
		String hsl="";
		if (jenis_topup==2)
		{
			if (premi_topup < 1000000)
			{
				hsl="Minimum Top-Up Tunggal dan Berkala : Rp. 1.000.000,-";
			}
			
			if (premi_topup > 1e10)
			{
				hsl="Maksimum Top-Up Tunggal dan Berkala sebesar Rp. 10.000.000.000,- untuk satu tahun Polis";
			}
		}
		
		double total_premi = premi + premi_topup;
		switch (pmode_id.intValue())
		{

			case 1:
				if (total_premi < 1350000)
				{
					hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 1.350.000,-";
				}
				break;
			case 2:
				if (total_premi < 2625000)
				{
					hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp. 2.625.000,-";
				}
				break;
			case 3:
				if (total_premi < 4000000)
				{
					hsl="Minimum Total Premi Tahunan : Rp. 4.000.000.-";
				}
				break;
			case 6:
				if (total_premi < 500000)
				{
					hsl="Minimum Total Premi Bulanan (1 bulanan) : Rp. 500.000,-";
				}
				break;
		}
		
		return hsl;
	}
		
	
	public static void main(String[] args) {
	}
}
