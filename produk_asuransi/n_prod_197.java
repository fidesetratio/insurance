package produk_asuransi;

import java.math.BigDecimal;

/**
/* n_prod_197 CI STAND ALONE
 * 
 * @author Andhika
 * @since Mei 01, 2013 (10:08:36 AM)
 */
public class n_prod_197 extends n_prod{

	public n_prod_197(){
		
		ii_bisnis_id = 197;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 60;

		indeks_is_forex = 1;
		is_forex = new String[indeks_is_forex];
		is_forex[0] = "01";

		//1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list = 6;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0 ; //sekaligus
		ii_pmode_list[2] = 3 ; //tahunan
		ii_pmode_list[3] = 2 ; //semester
		ii_pmode_list[4] = 1 ; //Tri
		ii_pmode_list[5] = 6 ; //bulanan

		//untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 1;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar = 4;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		
		idec_min_up01 = 20000000;  //Rp
		idec_max_up01 = 100000000;
		flag_uppremi = 1;
		flag_jenis_plan = 5;
		indeks_rider_list = 0;
		ii_rider = new int[indeks_rider_list];
		flag_as = 3;

	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		
		if(ai_no == 2){
			indeks_li_pmode = 3;
			li_pmode[1] = 3;
			li_pmode[2] = 6;
		}
		ii_pmode_list = li_pmode;
		indeks_ii_pmode_list = indeks_li_pmode;
		of_set_pmode(ii_pmode_list[1]);
	}
	
	public String of_alert_min_up( double up1)
	{
		double min_up = of_get_min_up();
		double up = new Double(20000000);
		String hasil_min_up = "";
		String min = "";
		BigDecimal bd = new BigDecimal(min_up);
		min = bd.setScale(2,0).toString();
		
		if(ii_bisnis_no == 2){
			if(up1 != Double.valueOf(200000000)){
				hasil_min_up = " UP untuk plan ini harus Rp. 200.000.000,-";
			}
		}else{
			if (min_up > up1)
			{
				hasil_min_up = " UP Minimum untuk plan ini : "+ min;	
			}
			if(up1%up!=0){
				hasil_min_up = " UP harus kelipatan Rp.20.000.000,-";	
			}
		}
		
		return hasil_min_up;
	}

	public void of_set_premi(double adec_premi)	{
		if(ii_bisnis_no == 2) adec_premi = 450000.0;
		
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
	
	//cek usia	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk){
		hsl = "";
		if(ii_age < ii_age_from){
			hsl = "Usia Masuk Plan ini minimum : " + ii_age_from;
		}

		if(ii_age > ii_age_to){
			hsl = "Usia Masuk Plan ini maximum : " + ii_age_to;
		}

		if(ii_usia_pp < 17){
			hsl = "Usia Pemegang Polis mininum : 17 Tahun !!!";
		}
		return hsl;		
	}
	
}
