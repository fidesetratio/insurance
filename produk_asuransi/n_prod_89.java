// n_prod_89 Ultra Sejahtera
package produk_asuransi;
import java.util.HashMap;
import java.util.Map;

import com.ekalife.utils.f_check_end_aktif;

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class n_prod_89 extends n_prod{

	Query query = new Query();
	
	public n_prod_89()
	{
//		SK No. 007/EL-SK/I/97
		ii_bisnis_id = 89;
		ii_contract_period = 79;
		ii_age_from = 1;
		ii_age_to = 45;
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=4;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 1;   //triwulanan
		ii_pmode_list[2] = 2;   //semesteran
		ii_pmode_list[3] = 3;   //tahunan

		indeks_ii_lama_bayar=5;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[0] = 5;
		ii_lama_bayar[1] = 10;
		ii_lama_bayar[2] = 15;
		ii_lama_bayar[3] = 20;
		ii_lama_bayar[4] = 1;

		ii_end_from = 79;
		idec_min_up01 = 3000000;
		flag_uppremi=0;

	}

	public int of_get_conperiod(int number_bisnis)
	{
		li_cp=0;
		li_cp =	ii_end_from -ii_age;
		return li_cp;		
	}
	
	public void of_hit_premi()
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
			Double result = query.selectNilai(ii_jenis, 60, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
			
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
	}
	
	public void of_set_premi(double adec_premi)
	{
		
		hsl="";
		err="";
		li_lbayar = 0;
		li_cp = 0;
		li_ltanggung = 0;
		ldec_rate = 0;
		idec_premi = adec_premi;
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
			Double result = query.selectNilai(ii_jenis, 60, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			if (ii_bisnis_id ==802 || ii_bisnis_id == 804)
			{
				//kalau PC atau WPD, jangan dikali idec_add_pct, karna premi udah dikali idec_add_pct
				idec_up = idec_premi * ii_permil/ ldec_rate;
			}else{
				idec_up = ((idec_premi *ii_permil)  /ldec_rate)/idec_add_pct;

			}
			of_set_up(idec_up);		   
			idec_rate = ldec_rate;

		}else{
			hsl="Tidak ada data rate";
			idec_rate = 1	;

		}
		}
		catch (Exception e) {
		  err=e.toString();
		} 	

	}
	
	

	public double of_hit_premi_netto()
	{
		li_lbayar=0;
		li_cp=0 ;
		li_jenis=0;
		ldec_rate = 0;
		li_ltanggung =0;
		int li_bisnis = 0;

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
	li_jenis = 8 ;
	try {
		
		Map params = new HashMap();
		params.put("jenis", new Integer(li_jenis));
		params.put("lsbs", new Integer(li_bisnis));
		params.put("lku", is_kurs_id);
		params.put("lscb", new Integer(li_cp));
		params.put("lamaBayar", new Integer(li_lbayar));
		params.put("lamaTanggung", new Integer(li_ltanggung));
		params.put("tahunKe", new Integer(ii_tahun_ke));
		params.put("umur", new Integer(ii_age));
		Double result = (Double) this.sqlMap.queryForObject("elions.n_prod.selectNilai", params);			

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
		int li_month=0;

		if (ib_flag_end_age)
		{
			ii_end_age = ii_age;
		}
		li_month = ( ii_end_from - ii_end_age ) * 12;

		adt_bdate.set(thn,bln-1,tgl);
		
		idt_beg_date.set(thn,bln-1,tgl);		
		
		idt_end_date.set(thn,bln-1,tgl);			
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		idt_end_date.add(idt_end_date.DAY_OF_MONTH , -1);
		
		f_check_end_aktif a = new f_check_end_aktif();
		a.end_aktif(idt_end_date.YEAR, idt_end_date.MONTH, idt_end_date.DAY_OF_MONTH, idt_beg_date.YEAR , idt_beg_date.MONTH, idt_beg_date.DAY_OF_MONTH);
		ii_contract_period = ii_end_from - ii_end_age;
	}

	public void of_set_bisnis_no(int ai_no)
	{
		ii_bisnis_no = ai_no;

		if (ai_no == 5)    // lsdbs_number = 5 Sekaligus
		{
			indeks_li_pmode_list =2;
			li_pmode_list = new int[indeks_li_pmode_list ];
			li_pmode_list[1] = 0;
			ii_pmode_list = li_pmode_list;
			indeks_ii_pmode_list=indeks_li_pmode_list;
		}

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
