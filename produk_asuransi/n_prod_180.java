//n_prod 180 - ProSaver
package produk_asuransi;

import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * ProSaver
 * @author Deddy
 * @since July 21, 2009 (8:40:03 AM)
 */
public class n_prod_180 extends n_prod {
	
	Query query = new Query();
	
	public n_prod_180(){
		
		//SK. Direksi No. 078/AJS-SK/VII/2009
		ii_bisnis_id = 180;
		ii_contract_period = 10;
		ii_age_from = 1;
		ii_age_to = 60;
		
		//flag_jenis_plan = 9;
		//flag_uppremi=1;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
		// 1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=6;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 1; // triwulan
		ii_pmode_list[2] = 2; // semesteran
		ii_pmode_list[3] = 3; // tahunan
		ii_pmode_list[4] = 6; // bulanan
		ii_pmode_list[5] = 0 ;  //sekaligus

		indeks_ii_lama_bayar=6;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 10;
		ii_lama_bayar[1] = 15;
		ii_lama_bayar[2] = 20;
		ii_lama_bayar[3] = 10;
		ii_lama_bayar[4] = 15;
		ii_lama_bayar[5] = 20;
		
		ii_end_from = 60;
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
		
		indeks_rider_list=3;
		ii_rider[0]=823;
		ii_rider[1]=825;
		ii_rider[2]=832;
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) {
		//f_hit_umur umr =new f_hit_umur();
		hsl="";
		
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
		if (bln < 6)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from+" tahun(Min. 6 bulan)";
		}
		
		lm_byr = of_get_payperiod();
		
		if(lm_byr == 10) { //lama bayar 10 tahun
			ii_age_to = 60;
		}else if(lm_byr == 15) { //lama bayar 15 tahun
			ii_age_to = 55;
		}else if(lm_byr == 20) { //lama bayar 20 tahun
			ii_age_to = 50;
		}
		if (ii_age > ii_age_to) {
			hsl = "Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		//
		if (ii_usia_pp > 85) {
			hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!"; 
		} else if (ii_usia_pp < 17) {
			hsl = "Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		return hsl;		
		
	}	
	
	public void of_hit_premi()	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		li_ltanggung=0;
		ldec_rate=0;

		if (ib_single_premium)	{
			li_lbayar = 1; 
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}
		li_ltanggung = ii_contract_period;
		
		if (ii_bisnis_id >= 800){ 
			li_lbayar = ii_lbayar;
			// kalo rider maka ambil dari contract period
			li_ltanggung = li_lbayar;
		}

		li_cp = ii_pmode;
		//Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6){
			 li_cp = 3;
		}

		try {
			Double resultnew_x = query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);			
			
			if (resultnew_x != null) {
				ldec_rate = resultnew_x.doubleValue();
				if (ii_bisnis_id == 814 || ii_bisnis_id == 815) {
					// kalau PC atau WPD, jangan dikali idec_add_pct, karna
					// premi udah dikali idec_add_pct
					idec_premi = idec_up * ldec_rate / ii_permil;
				} else {
					idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
				}
				idec_rate = ldec_rate;

				if (ii_bisnis_id < 800) {
					idec_premi_main = idec_premi;
				}

			} else {
				hsl = "Tidak ada data rate";
			}
		} catch (Exception e) {
			err = e.toString();
		} 
		
	}

	public void of_set_begdate(int thn, int bln, int tgl){
		int li_month = 0;

		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);		

		if (ib_flag_end_age){
			ii_end_age = ii_age;
		}
		li_month = ( ii_lbayar ) * 12;
		
		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);

		ii_contract_period = ii_lbayar;	
		if (ib_single_premium)	{
			ii_lbayar = 1;
		}
	}
	
	public void of_set_bisnis_no(int ai_no)
	  {
		  ii_bisnis_no = ai_no;

		  if (ii_bisnis_id < 800){
			  ii_bisnis_no_utama = ii_bisnis_no;
			  ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			  ii_contract_period= ii_lbayar;
			  of_set_pmode(ii_pmode_list[1]);
		  }else{
			   if (ii_bisnis_id == 802 || ii_bisnis_id == 804)//PC or WPD
				  {
					  ii_age = ii_usia_pp;
					  of_set_up(idec_premi_main);	
				  }else{
					  of_set_age();
					  of_set_up(idec_up);
				  }
		  }
		 

		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}		
	  }
	
	public int of_get_payperiod()
	{
		li_lama=0;
		if (ib_single_premium)
		{
			li_lama = 1;
		}else{
			li_lama = ii_lama_bayar[ii_bisnis_no-1];
		}
		return li_lama;
	}
	
	public int of_get_conperiod(int number_bisnis){	
		li_cp=0;
		li_cp=ii_lbayar;
		return li_cp;	
	}
	
	public double of_get_min_up(){
		double ldec_1 = new Double("99999999999");
		if (is_kurs_id.equalsIgnoreCase("01")) {
				ldec_1 = 100000000;
		} else {
				ldec_1 = 10000;
		}
		return ldec_1;
	}
		
	
	public static void main(String[] args) {}
	
}