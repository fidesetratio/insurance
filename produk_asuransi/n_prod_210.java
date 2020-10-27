// n_prod_210 SMiLe Accident Cash
package produk_asuransi;
import java.math.BigDecimal;

import com.ekalife.utils.f_hit_umur;

/**
 * @author Lufi
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_210 extends n_prod{
	Query query = new Query();
	public n_prod_210()
	{
		
		ii_bisnis_id = 210;
		ii_contract_period = 10;
		ii_age_from = 1;
		ii_age_to = 60;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		


		
		ib_flag_end_age = false;
	  	idec_min_up01 = 1000000;

		indeks_ii_lama_bayar=2;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 10;
		
		
		indeks_idec_list_premi=14;
		idec_list_premi = new double[indeks_idec_list_premi];
		for( int i = 0 ; i< 13 ; i++)
		{
			idec_list_premi[i] = 3000000 + ( 1000000 * (i - 1) );
		}
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 2 ;  //Semester
		ii_pmode_list[2] = 1 ;  //Tri
		ii_pmode_list[3] = 6 ;  //Bulanan
		ii_pmode_list[4] = 3;  //Tahunan
		
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 0.16;     // pmode 0
		idec_pct_list[1] = 4; // pmode 1
		idec_pct_list[2] = 2; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 12 ;// pmode 6		

		flag_account = 2;
		
	    indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		
		
	}

	public int of_get_conperiod(int number_bisnis){
		li_cp=0;
		
		if (ii_bisnis_no==1 ) {
			ii_contract_period = 5;
			ii_end_from = 5;
		}else if (ii_bisnis_no==2){
			ii_contract_period = 10;
			ii_end_from = 10;
		}
		
		li_cp = ii_contract_period;
		return li_cp;
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=4;
		li_pmode_list = new int[indeks_li_pmode_list];			

//		if (ai_no >= 5 && ai_no <= 8)
//		{
//			indeks_li_pmode_list=2;
//			li_pmode_list[1] = 0 ;	
//			ib_single_premium = true;
//		}else{
//			indeks_li_pmode_list=5;
//			li_pmode_list[1] = 3;
//			li_pmode_list[2] = 1;
//			li_pmode_list[3] = 2;
//			li_pmode_list[4] = 6;
//		}
//		ii_pmode_list = li_pmode_list;
//		indeks_ii_pmode_list=indeks_li_pmode_list;
//
//		if (ii_bisnis_id < 800){
//			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
//			of_set_pmode(ii_pmode_list[1]);
//		}else{
//			 if (ii_bisnis_id == 802 || ii_bisnis_id == 804)//PC or WPD
//				{
//					ii_age = ii_usia_pp;
//					of_set_up(idec_premi_main);	
//				}else{
//					of_set_age();
//					of_set_up(idec_up);
//				}
//		}
//
//		indeks_li_pmode=indeks_ii_pmode_list;
//		li_pmode = new int[indeks_li_pmode];
//		
//		for (int i =1 ; i<indeks_li_pmode;i++)
//		{
//			li_pmode[i] = ii_pmode_list[i];
//			
//		}
		
		indeks_li_pmode=indeks_ii_pmode_list;
		li_pmode = new int[indeks_li_pmode];
		
		for (int i =1 ; i<indeks_li_pmode;i++)
		{
			li_pmode[i] = ii_pmode_list[i];
			
		}
	}
	
	public void of_hit_premi()
	{
		hsl="";
		err="";		
		
		li_cp = 0;
		ldec_rate=0;
		li_cp = ii_pmode;
		
		if (ib_single_premium )
		{
			li_lbayar = 1;
		}else{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			if (ii_bisnis_id >= 800)
			{ 
				li_lbayar = ii_lbayar;
				ii_contract_period = li_lbayar;
			}
		}
//		Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6 )
		{
			li_cp = 3;	
		}
		
		try {

			Double result=0.;			
			result=query.selectNilaiNew(ii_jenis, ii_bisnis_id, ii_bisnis_no, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);

			 if(result != null) {
				ldec_rate = result.doubleValue();
				idec_premi =  (idec_up * (ldec_rate/ii_permil) )/ idec_add_pct ;
				idec_premi_main = idec_premi;	
				
			 }else{
			 	hsl="Tidak ada data rate";
			 }
		
		}
	  catch (Exception e) {
			err=(e.toString());
	  } 
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
//		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);		
		
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		if(ii_usia_pp > 60)
		{
			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!";
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		
		if (ii_age < ii_age_from)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_to;
		}
		
		return hsl;		
	}

	public double of_get_min_up()
	{
		double ldec_1=0;
		
		if(this.is_kurs_id.equals("01")) { //RUPIAH
			if(ii_bisnis_no==12 || ii_bisnis_no==13){
				idec_min_up01=50000000;
			}else{
				idec_min_up01=100000000;
			}
		}else if(this.is_kurs_id.equals("02")){ //DOLLAR
			if(ii_bisnis_no==12 || ii_bisnis_no==13){
				idec_min_up01=5000;
			}else{
				idec_min_up01=10000;
			}
			
		}
		
		ldec_1 = idec_min_up01;
		return ldec_1;
	}
	
	public double cek_awal()
	{
		if (is_kurs_id.equalsIgnoreCase("02") ) 
			li_id = 19;
//		String err="";
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
	
	
	
	public static void main(String[] args) {
	}
}
