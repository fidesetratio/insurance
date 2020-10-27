// n_prod_62 Eka Proteksi UP
package produk_asuransi;
import java.math.BigDecimal;
/*
 * Created on Jul 27, 2005
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
public class n_prod_62 extends n_prod{
	Query query = new Query();
	public n_prod_62()
	{
//		SK No. 23/EL-SK/XI/99 Eka Proteksi
		ii_bisnis_id = 62;
		ii_contract_period = 59;
		ii_age_from = 1;
		ii_age_to = 49 ;
		ib_flag_pp = false;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="02";		
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 3;   //tahunan
		ii_pmode_list[2] = 1;   //tahunan
		ii_pmode_list[3] = 2;   //tahunan
		ii_pmode_list[4] = 0;   //tahunan

//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 59;

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[0] = 10;
		
		idec_min_up01 = 7000000;  //Rp
		idec_min_up02 = 1000;     //US$

		indeks_idec_list_premi=15;
		idec_list_premi = new double[indeks_idec_list_premi];	  		
		idec_list_premi[0] = 1000000;
		idec_list_premi[1] = 2000000;
		idec_list_premi[2] = 3000000;
		idec_list_premi[3] = 4000000;
		idec_list_premi[4] = 5000000;
		idec_list_premi[5] = 6000000;
		idec_list_premi[6] = 7000000;
		idec_list_premi[7] = 8000000;
		idec_list_premi[8] = 9000000;
		idec_list_premi[9] = 10000000;
		idec_list_premi[10] = 11000000;
		idec_list_premi[11] = 12000000;
		idec_list_premi[12] = 13000000;
		idec_list_premi[13] = 14000000;
		idec_list_premi[14] = 15000000;
		
		flag_uppremi=0;
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
	
	}

	public double of_get_tunai(int ai_th_ke)
	{
		hsl="";
		err="";
		li_lbayar = 0 ;
		li_cp = 0;
		int li_tg = 0;
		li_umur = 0;
		int li_bisnis = 0;
		ldec_rate = 0;
		double ldec_tunai = 0;

		if (ib_single_premium)
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

		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
		{
			li_cp = 3;
		}
		li_umur = ii_age;
		li_tg = ii_end_from;
		li_bisnis = 52;

		try {
			Double result = query.selectNilai(2, li_bisnis, is_kurs_id, li_cp, li_lbayar, li_tg, ai_th_ke, li_umur);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
				ldec_tunai = ldec_rate * idec_up / ii_permil;				
			}else{
				hsl="Tidak ada data rate";			
			}
		   }
		 catch (Exception e) {
			   err=e.toString();
		 } 				
		return ldec_tunai;		
	}

	public void of_hit_premi()
	{
		hsl="";
		err="";
		li_lbayar = 0;
		li_cp = 0;
		li_ltanggung = 0;
		li_umur = 0 ;
		int li_round = 0;
		ldec_rate = 0;
		int li_bisnis=0;

		if (ib_single_premium)
		{
			li_lbayar = 1 ;
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}

		li_cp = ii_pmode;
		li_bisnis = 52;
		li_ltanggung = ii_end_from;
		li_umur = ii_age;
		if (ii_age == 1)
		{
			li_umur = 2;
		}
		
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
		{
			li_cp = 3;
		}

		try {
			Double result = query.selectNilai(ii_jenis, li_bisnis, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, li_umur );
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			if (ii_bisnis_id ==802 || ii_bisnis_id == 804)
			{
				//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
				idec_premi = idec_up * ldec_rate / ii_permil;
			}else{
				if (is_kurs_id == "02")
				{
					li_round = 2;
				}
				idec_premi =  (idec_up * ldec_rate * idec_add_pct / ii_permil);
				int decimalPlace = li_round;
				BigDecimal bd = new BigDecimal(idec_premi);
				bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				idec_premi = bd.doubleValue();
			}

			idec_rate = ldec_rate;

			if (ii_bisnis_id < 800)
			{
				idec_premi_main = idec_premi;	
			}
		}else{
			hsl="Tidak ada data rate";	
		}
		}
		catch (Exception e) {
		  err=e.toString();
		} 		
	}

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month = 0;
		
		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);				

		if (is_kurs_id == "02")
		{
			ii_end_from = 65;
		}else{
			ii_end_from = 59;
		}

		ii_end_age = ii_age;

		li_month = ( ii_end_from - ii_end_age ) * 12;

		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		
		ldt_end.set(thn,bln-1,tgl);		
		ldt_end.add(idt_beg_date.MONTH,li_month);		
		
		if (adt_bdate.DAY_OF_MONTH == ldt_end.DAY_OF_MONTH)
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH,-1);
		}	

		ii_contract_period = ii_end_from - ii_end_age;
	}
	
	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;

		if (as_kurs == "02")
		{
			ii_end_from = 65;
		}else{
			ii_end_from = 59;
		}

		ii_contract_period = ii_end_from - ii_end_age;
	}	
	
	public static void main(String[] args) {
	
		
	}
}
