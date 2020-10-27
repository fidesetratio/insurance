// n_prod_46 SUPER PROFUND                 
package produk_asuransi;
/*
 * 
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
public class n_prod_46 extends n_prod {

	public n_prod_46()
	{
//		SK No. 006/EL-SK/VII/98 Simas Super Protection
		ii_bisnis_id = 46;
		ii_contract_period = 75;
		ii_age_from = 1;
		ii_age_to = 56;  

		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0 ;  //sekaligus
		ii_pmode_list[2] = 1 ;  //triwulanan
		ii_pmode_list[3] = 2 ;  //semesteran
		ii_pmode_list[4] = 3 ;  //tahunan
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 75;
		ib_flag_end_age = false;		

//		di lst_det_bisnis, lsdbs_number cuma 1, 5pay life
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 5;
		

		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];		
	}

	public void of_hit_premi()
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		li_ltanggung=0;
		ldec_rate=0;

		if (ib_single_premium)
		{
			li_lbayar = 1; 
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}
		li_ltanggung = ii_end_from;
		
		if (ii_bisnis_id >= 800)
		{ 
			li_lbayar = ii_lbayar;
			// kalo rider maka ambil dari contract period
			li_ltanggung = li_lbayar;
		}

		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
		{
			 li_cp = 3;
		}

		try {
			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			  if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
			  {
				  //kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
				  idec_premi = idec_up * ldec_rate / ii_permil;
			  }else{
				  idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
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

	
	public double of_hit_premi_netto()
	{
		li_lbayar=0;
		li_cp=0;
		li_ltanggung=0;
		li_jenis=0;
		ldec_rate=0;


		if (ib_single_premium)
		{ 
			li_lbayar = 1 ;
		}else{ 
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}

		li_ltanggung = ii_end_from;
		if (ii_bisnis_id >= 800)
		{ 
			li_lbayar = ii_lbayar;
			// kalo rider maka ambil dari contract period
			li_ltanggung = li_lbayar;
		}
		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if  (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
		{
			li_cp = 3;
		}
		li_jenis = 8;
		try {
			Double result = query.selectNilai(li_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
			{
				//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
				idec_premi = idec_up * ldec_rate / ii_permil;
			}else{
				idec_premi = idec_up * ldec_rate * idec_add_pct / ii_permil;
			}

			idec_rate = ldec_rate;

		  }else{
			hsl="Tidak ada data rate";
		  }
		}
		  catch (Exception e) {
			err=e.toString();
		  } 
		return idec_premi;
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
	
	
	public static void main(String[] args) {

	}
}
