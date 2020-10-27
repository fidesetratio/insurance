// n_prod_149 horison
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
public class n_prod_149 extends n_prod {
	Query query = new Query();
	public n_prod_149()
	{
//		SK No. 008/EL-SK/I/97
		ii_bisnis_id = 149;
		ii_contract_period = 55;
		ii_age_from = 17;
		ii_age_to = 50;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";


//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 6;   //bulanan

//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 55;
		ib_flag_end_age = true;

		ib_single_premium=false;
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 55;

		idec_min_up01 = 100000;  //Rp // cek lagi, ini min premi
		idec_max_up01 = 1000000000;
		flag_uppremi=1;
		flag_worksite = 1;
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		
		indeks_idec_pct_list=7;
		idec_pct_list = new double[indeks_idec_pct_list];
		idec_pct_list[0] = 1;     // pmode 0
		idec_pct_list[1] = 0.270; // pmode 1
		idec_pct_list[2] = 0.525; // pmode 2
		idec_pct_list[3] = 1;     // pmode 3
		idec_pct_list[4] = 1 ;    // pmode 4
		idec_pct_list[5] = 1 ;    // pmode 5
		idec_pct_list[6] = 0.1; // pmode 6
		
		flag_horison = 1;
		
	}

	public int of_get_payperiod()
	{
		ii_lbayar=(ii_end_from - ii_usia_tt);
		return ii_lbayar;
	}	
	
/*	 public void of_set_premi(double adec_premi)
	 {
	 	hsl="";
	 	err="";
		 li_lbayar=0;
		 li_cp=0;
		 ldec_rate=0;
		 li_ltanggung=0;
		 li_umur=0;
		// JOptionPane.showMessageDialog(null, "ii_bisnis_no: " + ii_bisnis_no, "PESAN", JOptionPane.ERROR_MESSAGE);
		// JOptionPane.showMessageDialog(null, "ii_bisnis_no: " + (ii_bisnis_no-1), "PESAN", JOptionPane.ERROR_MESSAGE);
					
		 li_lbayar = ii_lama_bayar[ii_bisnis_no - 1];
		 li_ltanggung = ii_end_from;  //ii_contract diganti, karena di table 59
		 li_cp = ii_pmode;
		 li_umur = ii_age;
		 if (ii_age == 1)
		 {
		 	li_umur = 2;
		 }

			try {
//				Double result = query.selectNilai(1, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, li_umur);
				Double result = query.selectNilai(1, ii_bisnis_id, is_kurs_id, 3, li_lbayar-li_umur, li_ltanggung-li_umur, ii_tahun_ke, li_umur); // ada yg di hardcode
				
				if(result != null) {
				   ldec_rate = result.doubleValue();
				idec_premi = adec_premi;
				if (ldec_rate > 0)
				{
					idec_up = (idec_premi * ii_permil / ( ldec_rate * 0.1)); // FAKTOR = 0.1 UNTUK BULANAN, GANTI!
					int decimalPlace = 2;
					BigDecimal bd = new BigDecimal(idec_up);
					bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
					idec_up = bd.doubleValue();
				}

				idec_rate = ldec_rate;

				of_set_up(idec_up);
			 }else{	
				hsl="Tidak ada data rate";			 	
			 }
			}
		  catch (Exception e) {
				err=e.toString();
		  } 
	 }*/	
	
		public void of_set_premi(double adec_premi)
		{
			hsl="";
			err="";
			li_lbayar = 0;
			li_cp = 0;
			li_ltanggung = 0;
			ldec_rate = 0;

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
			
//			   Kalau triwulan, semester, bulanan, jadiin tahunan
			if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
			{
				li_cp = 3;
			}

//			   ii_contract_period diganti ii_end_from
//			   karena lama_tanggung di table premi sama, 79 th

			try {
				Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, ii_lbayar, ii_lbayar, ii_tahun_ke, ii_age);
				
				if(result != null) {
				   ldec_rate = result.doubleValue();

				idec_premi = adec_premi;
				if (ldec_rate > 0)
				{
					idec_up = (idec_premi * ii_permil / ( ldec_rate * idec_add_pct)); 
					int decimalPlace = 2;
					BigDecimal bd = new BigDecimal(idec_up);
					bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
					idec_up = bd.doubleValue();
				}

				idec_rate = ldec_rate;

				of_set_up(idec_up);

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
	 
/*	public void of_hit_premi()
	{
		hsl="";
		err="";
		li_lbayar = 0;
		li_cp = 0;
		li_ltanggung = 0;
		ldec_rate = 0;

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
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6)
		{
			li_cp = 3;
		}

//		   ii_contract_period diganti ii_end_from
//		   karena lama_tanggung di table premi sama, 79 th

		try {
			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, ii_lbayar, ii_lbayar, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			if (ii_bisnis_id ==802 || ii_bisnis_id == 804)
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
	}*/

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =	ii_end_from -ii_age;
		return li_cp;		
	}
	
	public double of_hit_premi_netto()
	{
		li_lbayar = 0;
		li_cp = 0;
		li_ltanggung = 0;  
		li_jenis = 0;
		ldec_rate = 0;

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
		li_jenis = 8 ;
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
		if (ii_bisnis_id < 800)
		{
			idec_premi_main = idec_premi;
		}
		return idec_premi;	
	}

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month = 0;
		
		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);				

		if (ib_flag_end_age){
			ii_end_age = ii_age;
		}

		li_month = ( ii_end_from - ii_end_age ) * 12;

		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);


		ii_contract_period = ii_end_from - ii_end_age;	
		ii_lbayar = ii_contract_period;
	}

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;

		if (ii_bisnis_id < 800)
		{
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
			of_set_pmode(ii_pmode_list[1]);
		}else{
			if (ii_bisnis_id == 802 || ii_bisnis_id == 804) //PC & WPD
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
	
	
	public static void main(String[] args) {

	}
}
