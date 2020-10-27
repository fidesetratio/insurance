package produk_asuransi;

import java.math.BigDecimal;

/**
/* n_prod_196 TERM SURYAMAS AGUNG AGENCY
 * 
 * @author Deddy
 * @since Oct 14, 2011 (10:08:36 AM)
 */
public class n_prod_196 extends n_prod{

	public n_prod_196(){
		
		ii_bisnis_id = 196;
		ii_contract_period = 1;
		ii_age_from = 1;
		ii_age_to = 69;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

		//1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 2;   //semester
		ii_pmode_list[3] = 1;   //Tri		
		ii_pmode_list[4] = 6;   //bulanan	

		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.27; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6	
		
		//untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 1;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=4;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		idec_min_up01 = 100000000;  
		idec_max_up01 = 500000000;
		idec_min_up02 = 10000;  
		idec_max_up02 = 50000;
		flag_uppremi=0;
		flag_jenis_plan = 5;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		flag_as = 3;

	}

	public void of_hit_premi()	{
		if(ii_bisnis_no==1){
			hsl="";
			err="";
			li_lbayar=0;
			li_cp=0;
			ldec_rate=0;
			
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			li_cp = ii_pmode;
			Double result=0.;
			try {	
			   result=query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no, is_kurs_id, 3, 1, 1, 1, ii_age);
			}catch (Exception e) {
				err=(e.toString());
		    } 
			if(result!=null){
				ldec_rate = result.doubleValue();				
				idec_premi = (idec_up * ldec_rate / ii_permil)*idec_pct_list[ii_pmode];
				idec_premi_main=idec_premi;
				idec_rate = ldec_rate;
			}else{
			 	hsl="Tidak ada data rate";
			 }
		}else {//term sinarmas sekuritas diset preminya
			idec_premi = 346000;
			idec_premi_main=idec_premi;	
		}
	}
	
	public String of_alert_max_up( double up1)
	{
		double max_up=of_get_max_up();
		String hasil_max_up="";
		String max="";
		BigDecimal bd = new BigDecimal(max_up);
		max=bd.setScale(2,0).toString();
		
		if (max_up < up1)
		{
			hasil_max_up=" UP maximum untuk plan ini : "+ max;	
		}
		return hasil_max_up;
	}	
	
	public String of_alert_min_up( double up1)
	{
		double min_up=of_get_min_up();
		String hasil_min_up="";
		String min="";
		BigDecimal bd = new BigDecimal(min_up);
		double up = new Double(100000000);
		min=bd.setScale(2,0).toString();
		
		if (min_up > up1)
		{
			hasil_min_up=" UP Minimum untuk plan ini : "+ min;	
		}
		return hasil_min_up;
	}

	//cek usia	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk){
		hsl="";
		if (nomor_produk == 1){
	
			if (ii_age < ii_age_from){
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
	
			if (ii_age > ii_age_to){
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
			}
	
			if (ii_usia_pp < 17){
				hsl="Usia Pemegang Polis mininum : 17 Tahun !!!";
			}
		}
		return hsl;		
	}
	
	public double of_get_max_up()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			ldec_1 = idec_max_up01;
		}else{
			ldec_1 = idec_max_up02;
		}
		return ldec_1;
	}	
	
}
