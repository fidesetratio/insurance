//n_prod_220
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

/*
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Randy - 002 JEMPOL LINK , Ridhaal -- 001 Smile Link Plus
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_220 extends n_prod{

	public n_prod_220()
	{
		ii_bisnis_id = 220;
		ii_contract_period = 100;
		ii_age_from = 1;
		ii_age_to = 70;

		indeks_is_forex = 2;
		is_forex = new String[indeks_is_forex];
		is_forex[0] = "01";
		is_forex[1] = "02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list = 5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 3  ; //tahunan
		ii_pmode_list[2] = 2 ;  //semester
		ii_pmode_list[3] = 1 ;  //Tri
		ii_pmode_list[4] = 6 ;  //bulanan

		
		indeks_li_pmode = 6;
		li_pmode = new int[indeks_li_pmode];

//		untuk hitung end date ( 79 - issue_date )
		ii_end_from = 100;
//		ib_flag_end_age = False
	  	idec_min_up01 = 15000000;

		indeks_ii_lama_bayar = 4;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 80;
		ii_lama_bayar[2] = 5;
		ii_lama_bayar[3] = 5; //Chandra A - 20180514 : Untuk produk 220 - 4

		indeks_idec_list_premi = 20;
		idec_list_premi= new double[indeks_idec_list_premi];
		for (int i = 0 ; i < 13 ;i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		indeks_idec_pct_list = 7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;		// pmode 0
		idec_pct_list[1] = 0.26;	// pmode 1
		idec_pct_list[2] = 0.51;	// pmode 2
		idec_pct_list[3] = 1;		// pmode 3
		idec_pct_list[4] = 1 ;		// pmode 4
		idec_pct_list[5] = 1 ;		// pmode 5
		idec_pct_list[6] = 0.09;	// pmode 6
		
		flag_uppremi = 1;
		flag_uppremiopen = 1;
		
		indeks_rider_list = 18;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0] = 810;		//1.	SMiLe Personal Accident 
		ii_rider[1] = 826;		//2.	SMiLe Hospital Protection (+) 
		ii_rider[2] = 812;		//3.	SMiLe Total Permanent Disability (TPD) 
		ii_rider[3] = 813;		//4.	SMiLe Critical Illness (CI) 
		ii_rider[4] = 818;		//5.	SMiLe Term Rider Unit Link 
		ii_rider[5] = 823;		//6.	SMiLe Medical 
		ii_rider[6] = 832;		//7.	SMiLe Ladies Medical 
		ii_rider[7] = 831;		//8.	SMiLe Ladies Hospital 
		ii_rider[8] = 830;		//9.	SMiLe Ladies Insurance 
		ii_rider[9] = 835;		//10.	SMiLe Scholarship 
		ii_rider[10] = 828;		//11.	SMiLe Payor 5/10 TPD/CI/Death 
		ii_rider[11] = 827;		//12.	SMiLe Waiver 5/10 TPD/CI 827
		ii_rider[12] = 837;		//13.	SMiLe ESCI 99 
		ii_rider[13] = 811;		//14.	SMiLe Hospital Protection  
		ii_rider[14] = 819;		//15.	SMiLe Hospital Protection PROTECTION FAMILY 
		ii_rider[15]=825;		//16.	SMiLe Medical IL
		ii_rider[16]=833;		//17.	SMiLe LADIES MEDICAL IL
		ii_rider[17]=848;
		
		flag_account = 2;
		flag_rider = 1;												
		flag_excell80plus = 1;
		//Yusuf - 20050203
		isInvestasi = true;
		usia_nol = 1;
		flag_as = 0;
		flag_cuti_premi = 1;
		li_pct_biaya = 3; // biaya akuisisi top up 3%
	}
	
	public void f_kombinasi(){
		indeks_kombinasi = 10;
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
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr = new f_hit_umur();
		int bln = umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		int hari = umr.hari1(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl = "";
		if (hari < 15)
		{
			hsl="Usia Masuk Tertanggung untuk Plan ini minimum : 15 hari !!!";
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Tertanggung untuk Plan ini maximum : " + ii_age_to + " tahun !!!";
		}
		if (ii_usia_pp > 85)
		{
			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!";
		}		
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		if (lm_byr < 5)
		{
			hsl="Untuk produk plan ini, minimal masa pembayaran yg diperbolehkan adalah 5 Tahun !!!";
		}
				
		return hsl;		
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
	
	public double of_get_fltpersen(Integer pmode_id)
	{
		double fltpersen = 1;
		if(pmode_id.intValue()==1){
			fltpersen = 4; 
		}else if(pmode_id.intValue()==2){
			fltpersen = 2; 
		}else if(pmode_id.intValue()==3){
			fltpersen = 1; 
		}else if(pmode_id.intValue()==6){
			fltpersen = 12; 
		}
		return fltpersen;
	}

	public double of_get_rate()
	{
		return idec_rate;	
	}

	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;
		if (as_kurs.equalsIgnoreCase("02"))
		{
			if(ii_bisnis_no == 3){
				kode_flag = 18;
			}else{
				kode_flag = 5;
			}
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
//			if(ii_bisnis_no == 3){
//				kode_flag = 17;
//			}else{
				kode_flag = 4;
//			}
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
	
	public int of_get_conperiod(int number_bisnis)
	{
		li_cp = 0;
		li_cp = ii_contract_period-ii_age;
		return li_cp;		
	}	
	
	public int of_get_payperiod()
	{
		ii_lama_bayar[0] = (ii_end_from - ii_usia_tt);		
		return ii_lama_bayar[0];
	}
	
	public double cek_awal()
	{
		double hasil = 0;
		if(is_kurs_id.equalsIgnoreCase("01")){
			hasil = 27500;
		}else{
			hasil = 2.75;
		}
		return hasil;
	}

	public double cek_rate(int li_bisnis, int li_pmode, int pperiod, int li_insper, int umur_tt, String kurs, int insperiod)
	{
		String err = "";
		double hasil = 0;
		try {
			Double result = query.selectNilai(1, 220, "01", 3, 80, 80, 1, umur_tt);
			
			if(result != null) {
				hasil = result.doubleValue();
			}
		}
		catch (Exception e) {
			err = e.toString();
		} 		
		return hasil; 
	}

	public double f_get_bia_akui(int ar_lb, int ar_ke)
	{
		double ld_bia=0;
		if (ar_lb==1){
			if (ar_ke==1){
				ld_bia=0.05;
			}
		}else if (ar_lb>1){
			if (ar_ke==1){
				ld_bia=0.7;	
			}else if (ar_ke==2){
				ld_bia=0.2;
			}else if (ar_ke >=3 && ar_ke <=5){
				ld_bia=0.05;
			}
		}
		return ld_bia;	
	}		 	

	public double cek_bia_akui(int kode_produk, int number_produk, int cara_bayar, int period, int ke)
	{
		if (cara_bayar == 1 || cara_bayar == 2 || cara_bayar == 6){
			cara_bayar = 3;
		}
		if (cara_bayar != 0){
			period = 80;	
		}
		
		double ld_bia = 0;
		try {
			Double result = query.select_biaya_akuisisi(kode_produk, number_produk, cara_bayar, ke, period);
			if(result != null) {
				ld_bia = result.doubleValue();
			}
		}
		catch (Exception e) {
			err = e.toString();
		} 			
		return ld_bia;
	}
	
	//BIAYA ASURANSI
	public void biaya2(double ld_bia_ass, double premi, int ldec_pct, double up)
	{
		mbu_jumlah2 = ((up/1000) * ld_bia_ass) * 0.1;
		int decimalPlace = 3;
		BigDecimal bd = new BigDecimal(mbu_jumlah2);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah2 = bd.doubleValue();		
		mbu_persen2 = ld_bia_ass/10;
		BigDecimal jm = new BigDecimal(mbu_persen2);
		jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);		
		mbu_persen2 = jm.doubleValue();			
	}

	//BIAYA ADMINISTRASI
	public void biaya3(double ldec_awal)
	{
		if (is_kurs_id.equalsIgnoreCase("01") ) {
			mbu_jumlah3 = 20000;
		}else if (is_kurs_id.equalsIgnoreCase("02") ) {
			mbu_jumlah3 = 2;
		}
		int decimalPlace = 3;
		BigDecimal bd = new BigDecimal(mbu_jumlah3);
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		mbu_jumlah3 = bd.doubleValue();		
	}

	public void of_set_premi(double adec_premi)
	{
		idec_premi_main = adec_premi;
		idec_premi = adec_premi;
		
		idec_rate = 5;
	
		if (ii_pmode == 1){ //triwulan
			adec_premi = adec_premi * 4;
		}else if (ii_pmode == 2){ //semesteran
			adec_premi = adec_premi * 2;
		}else if (ii_pmode == 6){ //bulanan
			adec_premi = adec_premi * 12;
		}

		idec_up = adec_premi * idec_rate;
		
		if (is_kurs_id.equalsIgnoreCase("01") ){
			if (idec_up < 7500000){
				idec_up = 7500000;
			}
		}else if (is_kurs_id.equalsIgnoreCase("02") ){
			if (idec_up < 750){
				idec_up = 750;
			}
		}
		of_set_up(idec_up);
	}
	
	public String cek_min_up(Double premi, Double up, String kurs, Integer pmode_id)
	{
		String hasil_up = "";
		double min_up1 = 0;
		double min_up2 = 0;
		of_set_premi(premi.doubleValue());
		//min_up1 = idec_up;
			
		//maksimum antara Rp 7.500.000,00 ATAU 500% dari Premi Pokok Tahunannya.
		if (kurs.equalsIgnoreCase("01"))
		{
			min_up1 = 7500000;
		}else{
			min_up1 = 750;
		}
		
		int fkt = 1;
		if (pmode_id.intValue() == 1){
			fkt = 4;
		}else if (pmode_id.intValue() == 2){
			fkt = 2;
		}else if (pmode_id.intValue() == 6){
			fkt = 12;
		}
		
		min_up2 = (5 * premi.doubleValue()) * fkt;
		
		double min = 0;
		if (min_up1 > min_up2)
		{
			min = min_up1;
		}else{
			min = min_up2;
		}
		
		if (up.doubleValue() < min)
		{
			hasil_up = "Up Minimum untuk plan ini "+ f.format(min);
		}
		
		return hasil_up;
	}
	
	public String cek_max_up(Integer li_umur_ttg, Integer kode_produk, Double premi, Double up, Double fltpersen, Integer pmode_id,String kurs)
	{
//		Maksimum Uang Pertanggungan adalah 20x dari Premi Pokok
		String hasil_up = "";
		
		int fkt = 1;
		if (pmode_id.intValue() == 1){
			fkt = 4;
		}else if (pmode_id.intValue() == 2){
			fkt = 2;
		}else if (pmode_id.intValue() == 6){
			fkt = 12;
		}
		
		double nil = 20 * premi.doubleValue() * fkt;
		
		if ((up.doubleValue()) > nil){
			hasil_up = " Up Maksimum untuk plan ini "+ f.format(nil);
		}
		
		return hasil_up;
	}
	
	public double of_get_min_up()
	{
		double ldec_1 = 0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if(ii_bisnis_no == 3){
				if (ii_pmode==3){
					ldec_1 = 2400000; 
				}else if (ii_pmode==1){
					ldec_1 = 600000;
				}else if (ii_pmode==2){
					ldec_1 = 1200000;
				}else if (ii_pmode==6){
					ldec_1 = 200000;
				}
			}else{
				if (ii_pmode==3){
					ldec_1 = 3000000; 
				}else if (ii_pmode==1){
					ldec_1 = 750000;
				}else if (ii_pmode==2){
					ldec_1 = 1500000;
				}else if (ii_pmode==6){
					ldec_1 = 300000;
				}
			}
		}else{
			if(ii_bisnis_no == 3){
				if (ii_pmode==3){
					ldec_1 = 240; 
				}else if (ii_pmode==1){
					ldec_1 = 60;
				}else if (ii_pmode==2){
					ldec_1 = 120;
				}else if (ii_pmode==6){
					ldec_1 = 20;
				}
			}else{
				if (ii_pmode==3){
					ldec_1 = 300; 
				}else if (ii_pmode==1){
					ldec_1 = 75;
				}else if (ii_pmode==2){
					ldec_1 = 150 ;
				}else if (ii_pmode==6){
					ldec_1 = 30 ;
				}
			}
		}
		return ldec_1;
	}
	
	public double of_get_max_up()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			if (ii_age<=5)
			{
				if (ii_pmode==0){
					ldec_1 = 400000000; 
				}else if (ii_pmode==3){
					ldec_1 = 200000000; 
				}else if (ii_pmode==1){
					ldec_1 = 200000000/0.27;
				}else if (ii_pmode==2){
					ldec_1 = 200000000/0.525 ;
				}else if (ii_pmode==6){
					ldec_1 = 200000000/12 ;
				}
			}else{
				if (ii_pmode==0){
					ldec_1 = 1000000000; 
				}else if (ii_pmode==3){
					ldec_1 =1000000000; 
				}else if (ii_pmode==1){
					ldec_1 =1000000000/0.27;
				}else if (ii_pmode==2) {
					ldec_1 = 1000000000/0.525;
				}else if (ii_pmode==6){
					ldec_1 = 1000000000/12;
				}
			}
		}else{
			if (ii_age <=5)
			{
				if (ii_pmode==0){
					ldec_1 = 40000; 
				}else if (ii_pmode==3){
					ldec_1 = 20000; 
				}else if (ii_pmode==1){
					ldec_1 = 20000/0.27;
				}else if (ii_pmode==2){
					ldec_1 = 20000/0.525;
				}else if (ii_pmode==6){
					ldec_1 = 20000/12;
				}
			}else{
				if (ii_pmode==0){
					ldec_1 = 100000; 
				}else if (ii_pmode==3){
					ldec_1 = 100000; 
				}else if (ii_pmode==1){
					ldec_1 = 100000/0.27;
				}else if (ii_pmode==2){
					ldec_1 = 100000/0.525;
				}else if (ii_pmode==6){
					ldec_1 = 100000/12;
				}			
			}
		}
		return ldec_1;
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
			 if (nomor_produk > 15 && nomor_produk!=211 && nomor_produk!=212)
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
	
//	public void count_rate_815(int klas, int unit ,int kode_produk, int nomor_produk, String kurs, int umurttg, int umurpp, double up, double premi, int pmode, int flag, int ins_period,int payperiod)
//	{
//		String err="";
//		String err1="";
//		double hasil=0;
//		ldec_temp6=0;
//		ldec_rate6=0;
//
//		double ldec_pct1 = 1;
//		int li_kali = 1;
//		switch (pmode)
//		{
//			case 1 ://triwulan
//				li_kali = 4;
//				ldec_pct1 = 0.27;
//				break;	
//			case 2 : //semesteran
//				li_kali = 2;
//				ldec_pct1 = 0.525;
//				break;
//			case 6 ://BULANAN
//				li_kali = 12 ;
//				ldec_pct1 = 0.1;
//				break;
//		}	
//		
//			int li_umur_pp = umurpp;
//			try {
//				Double result = query.selectRateRider(kurs, umurttg, li_umur_pp, kode_produk, nomor_produk);
//				
//				if(result != null) {
//					hasil = result.doubleValue();
//				}else{
//					err1="Rate Asuransi CI Tidak Ada  !!!!";
//				}
//			   }
//			 catch (Exception e) {
//				   err=e.toString();
//			 } 		
//			 if(!err1.equals("")) throw new RuntimeException(err1);
//			 else if(!err.equals("")) throw new RuntimeException(err);
//				 ldec_temp6 = ((premi * li_kali / 1000) * hasil) * 0.1;
//			 int decimalPlace = 2;
//			 BigDecimal bd = new BigDecimal(ldec_temp6);
//			 bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
//			 ldec_temp6=bd.doubleValue();	
//			 ldec_rate6 =hasil;
//			 BigDecimal jm = new BigDecimal(ldec_rate6);
//			 jm = jm.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
//			 ldec_rate6=jm.doubleValue();		
//
//	}	
		
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
	
	public static void main(String[] args) {

	}
}
