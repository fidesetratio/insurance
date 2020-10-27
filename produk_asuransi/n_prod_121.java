//n_prod_121 CERDAS FAMILY
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * @author Deddy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_121 extends n_prod{
	Query query = new Query();
	public n_prod_121()
	{
		ii_bisnis_id = 121;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 60;
		
		flag_cuti_premi=1;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0] = "01";
		is_forex[1] = "02";
		
//		  1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 3 ;  //tahunan
		ii_pmode_list[2] = 2 ;  //semester
		ii_pmode_list[3] = 1 ;  //Tri
		ii_pmode_list[4] = 6 ;  //Bulanan	
		
		indeks_li_pmode=2;
		li_pmode = new int[indeks_li_pmode];	
		
		
		li_pct_biaya = 3; // biaya akuisisi top up 3%

//		  untuk hitung end date ( 79 - issue_date )
		ii_end_from = 100;
		ib_flag_end_age = false;
		idec_min_up01 = 5000000;
		idec_min_up02 = 500;
		
		idec_max_up01 = 1e9;
		idec_max_up02 = 100000;

		indeks_ii_lama_bayar=9;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 99;
		ii_lama_bayar[1] = 99;
		ii_lama_bayar[2] = 99;
		ii_lama_bayar[3] = 99;
		ii_lama_bayar[4] = 99;
		ii_lama_bayar[5] = 99;
		ii_lama_bayar[6] = 99;
		ii_lama_bayar[7] = 99;
		ii_lama_bayar[8] = 99;
		

		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.25; // pmode 1
		idec_pct_list[2] = 0.5; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.083; // pmode 6
		
		flag_uppremi=1;
		flag_cerdas_global=1;
		
		li_insured = 6;
		
		indeks_rider_list=18;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=818;
		ii_rider[1]=810;
		ii_rider[2]=811;
		ii_rider[3]=812;
		ii_rider[4]=813;
		//ii_rider[5]=814;
		ii_rider[5]=815;
		ii_rider[6]=816;
		ii_rider[7]=817;
		ii_rider[8]=819;
		ii_rider[9]=821;
		ii_rider[10]=823;
		ii_rider[11]=825;
		ii_rider[12]=827;
		ii_rider[13]=828;
		ii_rider[14]=830;
		ii_rider[15]=831;
		ii_rider[16]=832;
		ii_rider[17]=835;
		
		flag_biaya_tambahan=1;
		flag_account =2;
		flag_jenis_plan=2;
		flag_rider=1;
		//Yusuf - 20050203
		isInvestasi = true;
		isProductBancass = true;
		usia_nol = 1;
	}

	public int of_get_conperiod(int number_bisnis)
	{
		if(number_bisnis==1 || number_bisnis==2 || number_bisnis==9){
			ii_contract_period=99;
		}else if(number_bisnis==3 || number_bisnis==4){
			ii_contract_period=22;
		}else if(number_bisnis==5 || number_bisnis==6){
			ii_contract_period=70;
		}else if(number_bisnis==7 || number_bisnis==8){
			ii_contract_period=65;
		}
		li_cp=0;
		li_cp =ii_contract_period-ii_age;
		ii_end_from=li_cp;
		return li_cp;		
	}	
	
	public String of_alert_min_premi( double premi)
	{
		
		double min_premi=of_get_min_up();
		String hasil_min_premi="";
		String min="";
		BigDecimal bd = new BigDecimal(min_premi);
		min=bd.setScale(2,0).toString();	
		
		
			hasil_min_premi=min_total_premi(this.ii_pmode,premi,this.is_kurs_id);	
		
		return hasil_min_premi;
	}
	
	public String cek_min_up(Double premi, Double up, String kurs , Integer pmode_id)
	{
		String hasil_up=""; 
		double min_up1=0;   
		double min_up2=0;
		of_set_premi(premi.doubleValue());
		min_up1=idec_up;

		if (pmode_id.intValue() == 0)
		{
			if (kurs.equalsIgnoreCase("01"))
			{
				min_up1 = 15000000;
			}else{
				min_up1 = 1500;
			}
			min_up2 = 100 * premi.doubleValue() /100;
		}else{
//			if (kurs.equalsIgnoreCase("01"))
//			{
//				min_up1 = 7500000;
//			}else{
//				min_up1 = 750;
//			}
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
			min_up2 = 5 * premi.doubleValue() *fkt;
		}
		
		double min = 0;
//		if (min_up1 > min_up2)
//		{
//			min = min_up1;
//		}else{
			min = min_up2;
//		}
		
		if (up.doubleValue() < min)
		{
			hasil_up="Up Minimum untuk plan ini "+ f.format(min);
		}
		
		return hasil_up;
	}
	
	public String cek_max_up(Integer li_umur_ttg, Integer kode_produk, Double premi, Double up, Double fltpersen, Integer pmode_id,String kurs)
	{
		String hasil_up="";
		int faktor = 0;
		if (pmode_id.intValue() == 1)
		{
			fltpersen = 4.;
		}else if (pmode_id.intValue()==2)
			{
			fltpersen = 2.;
			}else if (pmode_id.intValue() == 6)
				{
				fltpersen= 12.;
				}
		
		if (li_umur_ttg.intValue() >=1 && li_umur_ttg.intValue() <=19) faktor = 50;
		else if(li_umur_ttg.intValue() >=20 && li_umur_ttg.intValue() <=29) faktor = 40;
		else if(li_umur_ttg.intValue() >=30 && li_umur_ttg.intValue() <=39) faktor = 30;
		else if (li_umur_ttg.intValue() >=40 && li_umur_ttg.intValue() <=49) faktor = 20;
		else if (li_umur_ttg.intValue() >=50 && li_umur_ttg.intValue() <=60) faktor = 10;
			
		String ff=Integer.toString(faktor);
		double nil =((premi.doubleValue()) * Double.parseDouble(ff)) * fltpersen.doubleValue();
		if ((up.doubleValue()) > nil)
			{
				hasil_up="Up Maksimum untuk plan ini "+ f.format(nil);
			}
		return hasil_up;
	}
	
	public String min_total_premi(Integer pmode_id, double premi , String kurs)
	{
		String hsl="";
		double total_premi = premi ;
		switch (pmode_id.intValue())
		{

			case 1:
				if (total_premi < 1250000)
				{
					hsl="Minimum Total Premi Triwulanan (3 bulanan ) : Rp. 1.250.000,-";
				}
				break;
			case 2:
				if (total_premi < 2500000)
				{
					hsl="Minimum Total Premi Semesteran (6 bulanan ) : Rp. 2.500.000,-";
				}
				break;
			case 3:
				if (total_premi < 5000000)
				{
					hsl="Minimum Total Premi Tahunan : Rp. 5.000.000.-";
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
	
	public double of_get_rate()
	{
		return idec_rate;	
	}			

	
	public void of_set_premi(double adec_premi)
	{
		idec_premi_main = adec_premi;
		idec_premi = adec_premi;

		idec_rate = 1000;
		idec_up = adec_premi * idec_rate / 1000;
		of_set_up(idec_up);
	}			
	
	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia=0;
		ld_bia=1;
		return ld_bia;	
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
	public double cek_awal()
	{
		if (is_kurs_id.equalsIgnoreCase("02") ) 
		{
			li_id = 55;
		}else{
			li_id = 54;
		}
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

	public double cek_rate(int li_bisnis,int li_pmode,int pperiod,int li_insper,int umur_tt , String kurs,int insperiod)
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
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		
		if(ii_bisnis_no==1 || ii_bisnis_no==2 || ii_bisnis_no==9 ){
			ii_age_to=60;
		}else if(ii_bisnis_no==3 || ii_bisnis_no==4){
			ii_age_to=20;
		}else if(ii_bisnis_no>=5 && ii_bisnis_no<=8){
			ii_age_from=18;
			ii_age_to=55;
		}
		hsl="";
		if(ii_bisnis_no>=1 && ii_bisnis_no<=4){
			if (bln < 12)
			{
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
		}else {
			if(ii_age<ii_age_from){
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
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
				Double result = query.selectRateRider(kurs, umurttg, umurpp, kode_produk, nomor_produk);
				
				if(result != null) {
					hasil = result.doubleValue();
				}else{
					err1="Rate Asuransi Payor TPD Tidak Ada !!!!";
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
				Double result = query.selectRateRider(kurs, umurttg, 0, kode_produk, nomor_produk);
				
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
				Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
				
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
			 if ((nomor_produk > 20 && nomor_produk<141) || (nomor_produk>160 && nomor_produk<281))
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
			 ldec_temp2 = hasil *  rate_pmode * disc.doubleValue() * 0.1;
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
	
	public static void main(String[] args) {
	}
}
