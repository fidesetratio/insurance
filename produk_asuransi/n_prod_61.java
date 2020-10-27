// n_prod_61 PROTEKSINARMAS
package produk_asuransi;

import java.math.BigDecimal;

/*
 * Created on Jul 26, 2005
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
public class n_prod_61 extends n_prod {
	Query query = new Query();
	public n_prod_61()
	{
//		SK No. 16/EL-SK/X/99 Proteksi
		ii_bisnis_id = 61;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 55;
		ib_flag_pp = false;
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";

//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 6;  //bulanan

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 10;

		ii_end_from = 59;
		flag_uppremi=0;

		indeks_idec_list_premi=5;
		idec_list_premi = new double[indeks_idec_list_premi];	  		
		idec_list_premi[0] = 100000;
		idec_list_premi[1] = 200000;
		idec_list_premi[2] = 300000;
		idec_list_premi[3] = 400000;
		idec_list_premi[4] = 500000;
		
	}

	public double of_get_rate()
	{
		return idec_rate;
	}
	
	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month = 0;

		adt_bdate.set(thn,bln-1,tgl);

		idt_beg_date.set(thn,bln-1,tgl);
		
		if (is_kurs_id.equalsIgnoreCase("02"))
		{
			ii_end_from = 65;
		}else{
			ii_end_from = 59;
		}

		ii_end_age = ii_age;

		li_month = ( ii_end_from - ii_end_age ) * 12;
		
		idt_end_date.set(thn,bln-1,tgl);		
		idt_end_date.add(idt_beg_date.MONTH,li_month);
		if (adt_bdate.DAY_OF_MONTH  == ldt_end.DAY_OF_MONTH )
		{
			idt_end_date.add(ldt_end.DAY_OF_MONTH , -1);
		}

		ii_contract_period = ii_end_from - ii_end_age;	
	}
	
//	 of_set_kurs
	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;

		if (as_kurs.equalsIgnoreCase("02"))
		{
			ii_end_from = 65;
		}else{
			ii_end_from = 59;
		}

		ii_contract_period = ii_end_from - ii_end_age;

		of_set_premi(of_get_premi());
	}

//	 of_set_premi
	public void of_set_premi(double adec_premi)
	{
		hsl="";
		err="";
		li_lbayar=0;
		li_cp=0;
		ldec_rate=0;
		
		li_lbayar = ii_lama_bayar[ii_bisnis_no-1];

        li_ltanggung = ii_end_from ; //ii_contract diganti, karena di table 59
        li_cp = 3;

		try {

			Double result = query.selectNilai(ii_jenis, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, ii_age);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			   idec_premi = adec_premi;  
			   if (ldec_rate > 0)
			   {
				   idec_up = (idec_premi * 10000  / ( ldec_rate));	
				   int decimalPlace = 0;
				   BigDecimal bd = new BigDecimal(idec_up);
				   bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
				   idec_up = bd.doubleValue();
			   }
			   idec_rate = ldec_rate;
			   of_set_up(idec_up)	;			   
			}else{	
			   hsl="Tidak ada data rate";
			 	
			}
			
		   }
		 catch (Exception e) {
			   err=(e.toString());
		 } 
	}
	
	
	public static void main(String[] args) {

		
	}
}
