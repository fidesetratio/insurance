// n_prod_90 Warisan Sejahtera (new)
package produk_asuransi;
import com.ekalife.utils.f_check_end_aktif;

/*
 * Created on Jul 29, 2005
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
public class n_prod_90 extends n_prod{
	Query query = new Query();
	public n_prod_90()
	{
//		SK No. 004/EL-SK/IV/2004
		ii_bisnis_id = 90;
		ii_contract_period = 30;
		ii_age_from = 1;
		ii_age_to = 55;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0;   //sekaligus

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;

		ii_end_from = 79;
		flag_uppremi=1;
		
		isProductBancass = true; 
	}

	public void of_hit_premi()
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp = 0;
		ldec_rate=0;
		li_ltanggung=0;

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

//		ii_contract_period diganti ii_end_from
//		karena lama_tanggung di table premi sama, 79 th
		try {

			Double result = query.selectNilai(ii_jenis, 88, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
			
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
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0 ;
		li_jenis = 0;
		ldec_rate = 0;
		li_ltanggung =0;
		
		li_jenis = 8;

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
		return idec_premi;
	}

	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;

		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
		ii_contract_period = Math.min(ii_end_from - ii_end_age, 30);
		li_month = ( ii_contract_period ) * 12;

		adt_bdate.set(thn,bln-1,tgl);
		
		idt_beg_date.set(thn,bln-1,tgl);		
		
		ldt_end.set(thn,bln-1,tgl);			
		ldt_end.add(idt_beg_date.MONTH,li_month);
		
		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);

		if (adt_bdate.DAY_OF_MONTH == ldt_end.DAY_OF_MONTH)
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);
		}
		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);
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

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;

		li_cp = ii_contract_period;
		if (ii_end_from-ii_age<ii_contract_period)
		{
			li_cp =ii_end_from-ii_age;
		}

		return li_cp;		
	}

	public static void main(String[] args) {
	}
}
