package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_check_end_aktif;
import com.ekalife.utils.f_hit_umur;

/**
 * @author RANDY
 * 9 OKT 2017
 * SMiLe LIFE SYARIAH (BTN)
 */
public class n_prod_223 extends n_prod{
	
	private static final long serialVersionUID = 1L;
	Query query = new Query();
	
	public n_prod_223()
	{
		ii_bisnis_id = 223;
		ii_contract_period = 15;
		ii_age_from = 18;
		ii_age_to = 50;

		indeks_is_forex = 1;
		is_forex = new String[indeks_is_forex];
		is_forex[0] = "01";
		
		indeks_ii_lama_bayar = 2;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 15;
		ii_lama_bayar[1] = 20;

		
		indeks_idec_list_premi = 14;
		idec_list_premi = new double[indeks_idec_list_premi];
		for(int i=0; i<13; i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
		//1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list = 5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 3  ; //tahunan
		ii_pmode_list[2] = 2 ;  //semester
		ii_pmode_list[3] = 1 ;  //tri
		ii_pmode_list[4] = 6 ;  //bulanan
		
		indeks_idec_pct_list = 7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1;    // pmode 4
		idec_pct_list[5] = 1;    // pmode 5
		idec_pct_list[6] = 0.1;// pmode 6			

		flag_account = 2;
		flag_uppremi = 1;
	}
	
	public void of_set_bisnis_no(int ai_no){
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list = 5;
		li_pmode_list = new int[indeks_li_pmode_list];
		
		if(ii_bisnis_no == 2){ //helpdesk [138638] produk baru SLP Syariah (223-2)
			indeks_li_pmode_list=3;
			li_pmode_list[1] = 3;
			li_pmode_list[2] = 6;			
		} else {
			indeks_li_pmode_list=5;
			li_pmode_list[1] = 3;
			li_pmode_list[2] = 2;
			li_pmode_list[3] = 1;
			li_pmode_list[4] = 6;	
		}

		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list = indeks_li_pmode_list;

		indeks_li_pmode = indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i = 1; i < indeks_li_pmode; i++){
			li_pmode[i] = ii_pmode_list[i];			
		}
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr = new f_hit_umur();
		int bln = umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		if(ii_age < ii_age_from)
		{
			hsl = "Usia Masuk Plan ini minimum : " + ii_age_from + " tahun";
		}
		if(ii_age > ii_age_to)
		{
			hsl = "Usia Masuk Plan ini maximum : " + ii_age_to + " tahun";
		}
		if(ii_usia_pp < 17)
		{
			hsl = "Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		if(ii_usia_pp > 85)
		{
			hsl = "Usia Pemegang Polis maximum : 85 Tahun !!!";
		}
		
		return hsl;		
	}
	
	// set beg date
	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;
		
		if(ib_flag_end_age)
		{
			ii_end_age = 60;
		}
		
//		ii_contract_period = ii_end_age - ii_age;
//		while(ii_contract_period > 15){
//			ii_contract_period = ii_contract_period - 1;
//		}
		
		li_month = ( ii_contract_period ) * 12;
		adt_bdate.set(thn,bln-1,tgl);
		
		idt_beg_date.set(thn,bln-1,tgl);		
		
		ldt_end.set(thn,bln-1,tgl);	
		ldt_end.add(idt_beg_date.MONTH,li_month);

		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		
		if (adt_bdate.DAY_OF_MONTH  == ldt_end.DAY_OF_MONTH )
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);
		}

		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);

	}
	
//	public int of_get_payperiod()
//	{
//		li_lama = ii_end_age - ii_age;
//		while(li_lama > 15){
//			li_lama = li_lama - 1;
//		}
//		return li_lama;
//	}
	
//	public int of_get_conperiod(int number_bisnis)
//	{
//		li_cp = ii_end_age - ii_age;
//		while(li_cp > 15){
//			li_cp = li_cp - 1;
//		}
//		return li_cp;
//	}

	public String of_alert_min_premi(double premi)
	{
		String hasil_min_premi = "";
		
		if (ii_pmode == 1){ //Triwulan
			premi = premi * 4;
		}else if (ii_pmode == 2){ //Semesteran
			premi = premi * 2;
		}else if (ii_pmode == 6){ //Bulanan
			premi = premi * 12;
		}else if (ii_pmode == 3){ //Tahunan
			premi = premi * 1;
		}

		switch (ii_pmode)
		{

		case 1://triwulanan
			if(premi < 600000){
				hasil_min_premi = " Premi Minimum triwulanan untuk Plan ini adalah Rp 150.000,00";
			}
			if(premi > 28800000){
				hasil_min_premi = " Premi Maksimum triwulanan untuk Plan ini adalah Rp 7.200.000,00";
			}
			break;
		case 2://semesteran
			if(premi < 600000){
				hasil_min_premi = " Premi Minimum semesteran untuk Plan ini adalah Rp 300.000,00";
			}
			if(premi > 28800000){
				hasil_min_premi = " Premi Maksimum semesteran untuk Plan ini adalah Rp 14.400.000,00";
			}
			break;
		case 3://tahunan
			if(ii_bisnis_no == 2){ //helpdesk [138638] produk baru SLP Syariah (223-2)
				if(premi < 1200000){
					hasil_min_premi = " Premi Minimum tahunan untuk Plan ini adalah Rp 1.200.000,00";
				}
			} else {
				if(premi < 600000){
					hasil_min_premi = " Premi Minimum tahunan untuk Plan ini adalah Rp 600.000,00";
				}
			}
			if(premi > 28800000){
				hasil_min_premi = " Premi Maksimum tahunan untuk Plan ini adalah Rp 28.800.000,00";
			}
			break;
		case 6://bulanan
			if(ii_bisnis_no == 2){ //helpdesk [138638] produk baru SLP Syariah (223-2)
				if(premi < 1200000){
					hasil_min_premi = " Premi Minimum bulanan untuk Plan ini adalah Rp 100.000,00";
				}
			} else {
				if(premi < 600000){
					hasil_min_premi = " Premi Minimum bulanan untuk Plan ini adalah Rp 50.000,00";
				}
			}
			if(premi > 28800000){
				hasil_min_premi = " Premi Maksimum bulanan untuk Plan ini adalah Rp 2.400.000,00";
			}
			break;
		}
		
		return hasil_min_premi;
	}
	
	public void of_set_premi(double adec_premi)
	{
		idec_premi_main = adec_premi;
		idec_premi = adec_premi;
		
		if (ii_pmode == 1){ //Triwulan
			adec_premi = adec_premi / 3;
		}else if (ii_pmode == 2){ //Semesteran
			adec_premi = adec_premi / 6;
		}else if (ii_pmode == 6){ //Bulanan
			adec_premi = adec_premi / 1;
		}else if (ii_pmode == 3){ //Tahunan
			adec_premi = adec_premi / 12;
		}
		
		idec_up = (adec_premi * 10) * 50;
		
	}
	
	public void of_hit_premi()
	{
		
	}
	
	public double cek_awal()
	{
		Double hasil = new Double(0);
		return hasil; 
	}
	
	public static void main(String[] args) {
		
	}
}
