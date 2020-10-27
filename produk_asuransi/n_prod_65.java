// n_prod_65 Sehat Sejahtera
package produk_asuransi;
/*
 * Created on Jul 28, 2005
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
public class n_prod_65 extends n_prod{
	Query query = new Query();

	public n_prod_65()
	{
//		SK No. /EL-SK/VI/01
		ii_bisnis_id = 65;
		ii_contract_period = 79;
		ii_age_from = 1;
		ii_age_to = 50;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 1;   //triwulanan
		ii_pmode_list[2] = 2;   //semesteran
		ii_pmode_list[3] = 3;   //tahunan
		ii_pmode_list[4] = 0;   //sekaligus

		indeks_ii_lama_bayar=3;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 3;
		ii_lama_bayar[1] = 5;
		ii_lama_bayar[2] = 1;
		
		ii_end_from = 79;	
		flag_uppremi=0;	
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		
	}
	
	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk)
	{
		hsl="";

		if (ii_age < ii_age_from)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}

		if (ii_age < 17 && ii_pmode != 0)
		{
			hsl="Usia 1 - 16 tahun hanya boleh ambil plan sekaligus !!";
		}
		
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}

		return hsl;		
	}	
	
	public double of_get_tunai(int ai_th_ke)
	{
		hsl="";
		err="";
		li_lbayar = 0;
		li_cp = 0;
		ldec_rate = 0;
		double ldec_tunai = 0;

		if (ib_single_premium)
		{
			li_lbayar = 1 ;
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

		try {
			
			Double result = query.selectNilai(2, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ai_th_ke, ii_age);
			
		  if(result != null) {
			  ldec_rate = result.doubleValue();
			ldec_tunai = ldec_rate * idec_up / ii_permil;
		  }else{
			hsl="Tidak ada data rate";
		  }
		}
		  catch (Exception e) {
			e.toString();
		  } 

		return ldec_tunai;		
	}

	public void of_hit_premi()
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp = 0;
		ldec_rate=0;
		li_ltanggung =0;

		if (ib_single_premium )
		{
			li_lbayar = 1;
		}else{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}
		li_ltanggung = ii_end_from;
		if (ii_bisnis_id >= 800)
		{
			li_lbayar = ii_lbayar;		
			li_ltanggung = li_lbayar;	 
		}
		li_cp = ii_pmode;
//		   Kalau triwulan, semester, bulanan, jadiin tahunan
		if (ii_pmode == 1 || ii_pmode == 2 || ii_pmode == 6 )
		{
			li_cp = 3;	
		}

		try {
			
			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
			
			 if(result != null) {
				ldec_rate = result.doubleValue();
				if (ii_bisnis_id == 802 || ii_bisnis_id == 804)
				{
				//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
					idec_premi = idec_up * ldec_rate / ii_permil;
				}else{
					idec_premi = idec_up * ldec_rate / ii_permil;
					idec_rate = ldec_rate;
				
					Double result2 = query.selectNilai(9, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
					
					 if(result2 != null) {
						ldec_rate = result2.doubleValue();
						idec_premi =idec_premi + ldec_rate;
					 }	else{
						hsl="Tidak ada data rate";
					 }
					idec_premi = idec_premi * idec_add_pct;		
				}
				if (ii_bisnis_id < 800)
				{
					idec_premi_main = idec_premi;
				}
			 }else{
				hsl="Tidak ada data rate";
			 }
		}
	  catch (Exception e) {
			e.toString();
	  } 
	}

	public double of_hit_premi_netto()
	{
		hsl="";
		err="";
		li_lbayar = 0;
		li_cp =0;
		li_jenis=0;
		li_ltanggung=0;
		ldec_rate=0;

		if (ib_single_premium)
		{
			li_lbayar = 1 ;
		}else{
			li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		}

		li_ltanggung = ii_contract_period;
		if (ii_bisnis_id >= 800)
		{
			li_lbayar = ii_lbayar;
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
			  e.toString();
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
		
		ii_end_age = ii_age;

		li_month = ( ii_end_from - ii_end_age ) * 12;

		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH,-1);

		ii_contract_period = ii_end_from - ii_end_age;
	}
	
	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;
		indeks_li_pmode_list=4;
		li_pmode_list = new int[indeks_li_pmode_list];

		if (ai_no == 3)
		{
			indeks_li_pmode_list=2;
			li_pmode_list[1] = 0 ;			 
		}else{
			indeks_li_pmode_list=4;
			li_pmode_list[1] = 3;
			li_pmode_list[2] = 1;
			li_pmode_list[3] = 2;
		}
		ii_pmode_list = li_pmode_list;
		indeks_ii_pmode_list=indeks_li_pmode_list;

		if (ii_bisnis_id < 800){
			ii_lbayar = ii_lama_bayar[ii_bisnis_no-1];
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

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =ii_contract_period-ii_age;
		return li_cp;		
	}
			
	public static void main(String[] args) {

	}
}
