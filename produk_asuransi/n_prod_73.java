package produk_asuransi;

import java.math.BigDecimal;
// n_prod_73 Personal Accident PTI
/*
 * Created on Jul 29, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_73 extends n_prod{

	public n_prod_73()
	{
//		PA PT INTIMARAYA
		ii_bisnis_id = 73;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 65;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan

//
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 1;
		ib_flag_end_age = false;
//
		indeks_ii_lama_bayar=15;
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
		ii_lama_bayar[11] = 1;
		ii_lama_bayar[12] = 1;
		ii_lama_bayar[13] = 1;
		ii_lama_bayar[14] = 1;
		idec_min_up01 = 50000000;  //Rp
		idec_max_up01 = 2000000000;
		flag_uppremi=0;
		flag_jenis_plan = 5;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		flag_as = 3;
	}

	public void of_hit_premi()
	{
		idec_rate = 1;
		if (ii_bisnis_no == 1)
		{
			idec_premi = 60000;
			idec_rate = 1;		
		}
		if(ii_bisnis_no == 8)
		{
			idec_premi = 100000;
			idec_premi_main = idec_premi;
		}else{
			switch(ii_bisnis_no)
			{
				case 2:
					idec_rate = 1.8;
					break;
				case 3:
					idec_rate = 3.9;
					break;
				case 4:
					idec_rate = 6.2;
					break;
				case 9:
					idec_rate = 2.1;
					break;
				case 10:
					idec_rate = 4.6;
					break;
				case 11:
					idec_rate = 7.3;
					break;
			}
			idec_premi = (idec_up * idec_rate) / ii_permil;
			if(idec_premi >= 149998 && idec_premi <= 149999){
				
				idec_premi=150000;
			}
			//idec_premi_main = idec_premi;
		}
			int decimalPlace = 0;
			BigDecimal premi = new BigDecimal(idec_premi);
		    premi = premi.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
		   idec_premi_main = premi.doubleValue();
	}
	

	
	public double of_get_max_up()
	{
		double ldec_1=0;
		if(ii_bisnis_no == 8)
		{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				idec_max_up01 = 100000000;
				ldec_1 = idec_max_up01;
			}
		}
		else if (ii_bisnis_no == 1)
		{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				idec_max_up01=50000000;
				ldec_1 = idec_max_up01;
			}
		}else{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				idec_max_up01=500000000;
				ldec_1 = idec_max_up01;
			}else{
				idec_max_up01=50000;
				ldec_1 = idec_max_up01;
			}
		}
		return ldec_1;
	}		
	
	public double of_get_min_up()
	{
		double ldec_1=0;
		if (ii_bisnis_no == 8)
		{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				ldec_1 = 100000000;
			}
		}
		if (ii_bisnis_no == 1)
		{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				ldec_1 = 50000000;
			}
		}else{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				switch(ii_bisnis_no)
				{
					case 2:
						ldec_1=83000000;//83333333
						break;
					case 3:
						ldec_1=38400000;//38461538
						break;
					case 4 :
						ldec_1=24193548;//24193548
						break;
				}
			}else{
				switch(ii_bisnis_no)
				{
					case 2:
						ldec_1=8333;
						break;
					case 3:
						ldec_1=3846;
						break;
					case 4 :
						ldec_1=2419;
						break;
				}
			}
		}
		return ldec_1;
	}

//	cek usia	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		hsl="";
		if(nomor_produk==8)
		{
			ii_age_from = 18;
			ii_age_to = 69;
			
			if (ii_age < ii_age_from)
			{
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
	
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to + " Tahun ";
			}
	
			if (ii_usia_pp < 18)
			{
				hsl="Usia Pemegang Polis mininum : 18 Tahun !!!";
			}
		}
		else if (nomor_produk == 1)
		{
	
			if (ii_age < ii_age_from)
			{
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
	
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
			}
	
			if (ii_usia_pp < 17)
			{
				hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
			}
		}else if (nomor_produk == 15)
		{
			ii_age_from = 17;
			ii_age_to = 60;
	
			if (ii_age < ii_age_from)
			{
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
	
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
			}
	
			if (ii_usia_pp < 17)
			{
				hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
			}
		
		}else{
			
			if (ii_age < ii_age_from)
			{
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
	
			if (ii_age > 60)
			{
				hsl="Usia Masuk Plan ini maximum : 60" ;
			}
	
			if (ii_usia_pp < 17)
			{
				hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
			}
		}
		return hsl;		
	}
	
	public void of_set_premi(double adec_premi)	{
		if(ii_bisnis_no == 15) adec_premi = 50000.0;
		
		if (ii_pmode==0){
			idec_premi = adec_premi * 1;
		}else if (ii_pmode==3){
			idec_premi = adec_premi * 1;
		}else if (ii_pmode==1){
			idec_premi = adec_premi * 0.27;
		}else if (ii_pmode==2){
			idec_premi = adec_premi * 0.525;
		}else if (ii_pmode==6){
			idec_premi = adec_premi * 0.1;
		}
		
//		idec_premi = 346000; 
		idec_premi_main = idec_premi;
	}
	
	public String of_alert_min_up( double up1)
	{
		String hasil_min_up = "";
		
		if(ii_bisnis_no == 15){
			if(up1 != Double.valueOf(70000000)){
				hasil_min_up = " UP untuk plan ini harus Rp. 70.000.000,-";
			}
		}
		
		return hasil_min_up;
	}
	
	public static void main(String[] args) {
	}
}
