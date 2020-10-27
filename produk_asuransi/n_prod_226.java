package produk_asuransi;

import com.ekalife.utils.f_hit_umur;

/*
 * helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
 */
public class n_prod_226 extends n_prod
{
	Query query = new Query();
	public n_prod_226()
	{
		ii_bisnis_id = 226;
		ii_contract_period = 100;
		ii_age_from = 18;
		ii_age_to = 60;

		indeks_is_forex = 1;
		is_forex= new String[indeks_is_forex];
		is_forex[0] = "01";
		
		indeks_ii_pmode_list = 6;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0; // sekaligus
		ii_pmode_list[2] = 1; // triwulan
		ii_pmode_list[3] = 2; // semesteran
		ii_pmode_list[4] = 3; // tahunan
		ii_pmode_list[5] = 6; // bulanan
		
		indeks_ii_lama_bayar = 5;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 10;
		ii_lama_bayar[2] = 15;
		ii_lama_bayar[3] = 20;
		ii_lama_bayar[4] = 1;

		indeks_idec_pct_list = 7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1;   // pmode 6

		ii_end_from = 100;
		flag_uppremi = 0;
		flag_rider = 0;
		flag_as = 0;
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr = new f_hit_umur();
		int bln = umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		hsl = "";
		
		if(ii_bisnis_no == 4){
			if(ii_age > 55){
				hsl = "Usia Masuk Plan ini maximum : 55 Tahun !!!";
			}
		} else {
			if(ii_age > ii_age_to){
				hsl = "Usia Masuk Plan ini maximum : " + ii_age_to + " Tahun !!!";
			}
		}
		
		if(ii_age < ii_age_from){
			if(ii_age == 17){
				if((bln / 12) - ii_age < 0.6){
					hsl = "Usia Masuk Plan ini minimum : 17 Tahun 6 Bulan !!!";
				}
			} else
				hsl = "Usia Masuk Plan ini minimum : " + ii_age_from + " Tahun !!!";
		}

		if(ii_usia_pp > 85){
			hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!"; 
		}else if(ii_usia_pp < 18){
			hsl = "Usia Pemegang Polis minimum : 18 Tahun !!!";
		}
		return hsl;		
	}	

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month = 0;

		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);		

		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
		li_month = ( ii_end_from - ii_end_age ) * 12;
		
		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);

		ii_contract_period = ii_end_from - ii_end_age;	
	}
	
	public void of_hit_premi()
	{
		hsl = "";
		err = "";
		li_lbayar = 0;
		ldec_rate = 0;
		li_cp = 0;

		li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		
		if (ii_bisnis_no >= 1 && ii_bisnis_no <= 4){
			 li_cp = 3;
		}

		try {
			idec_premi = 0;
			Double resultnew_x = query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, 0, ii_tahun_ke, ii_age);			
			if(resultnew_x != null){				
			    ldec_rate = resultnew_x.doubleValue();
			   
			    if(ii_pmode == 6 || ii_pmode == 2 || ii_pmode == 1){ //bulanan, semesteran, triwulanan
			    	idec_premi = (idec_up / ii_permil) * ldec_rate * idec_add_pct;			    	
			    } else {
			    	idec_premi = (idec_up / ii_permil) * ldec_rate;
			    }
		    	idec_rate = ldec_rate;
			}else{
				hsl = "Tidak ada data rate";
			}
			
			idec_premi_main = idec_premi;	
		}catch(Exception e){
			err = e.toString();
		} 
	}

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		isProductBancass = true;
		
		if(ii_bisnis_no == 5){
			indeks_li_pmode_list = 2;
			li_pmode_list = new int[indeks_li_pmode_list];
			li_pmode_list[1] = 0;			
		}else{
			indeks_li_pmode_list = 6;
			li_pmode_list = new int[indeks_li_pmode_list];
			li_pmode_list[1] = 0; // sekaligus
			li_pmode_list[2] = 1; // triwulan
			li_pmode_list[3] = 2; // semesteran
			li_pmode_list[4] = 3; // tahunan
			li_pmode_list[5] = 6; // bulanan
		}

		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list = indeks_li_pmode_list;
		
		indeks_li_pmode = indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++){
			li_pmode[i] = ii_pmode_list[i];
		}		
	}
	
	public int of_get_conperiod(int number_bisnis)
	{	
		li_cp = 0;
		li_cp = ii_contract_period - ii_age;
		
		return li_cp;	
	}
	
	public double of_get_min_up()
	{
		double ldec_1 = 0;
		
		if (is_kurs_id.equalsIgnoreCase("01")){
			ldec_1 = 100000000; 
		}else{
			ldec_1 = 10000;
		}
			
		return ldec_1;
	}

	public void of_set_up(double adec_up)
	{
		idec_up = adec_up;
	}
	
	public static void main(String[] args) { }	
}
