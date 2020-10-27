// n_prod_78 EKA PROTEKSI (PLB)                                    
package produk_asuransi;

import java.math.BigDecimal;

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
public class n_prod_78 extends n_prod {

	public n_prod_78()
	{
//		Eka Proteksi Palembang special case, up sama semua
		ii_bisnis_id = 78;
		ii_contract_period = 59;
		ii_age_from = 1;
		ii_age_to = 49 ;   // By Gideon 55
		ib_flag_pp = false;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=3;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 3 ;  //tahunan
		ii_pmode_list[2] = 0 ;  //tahunan
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 59;

//		di lst_det_bisnis, lsdbs_number cuma 1, 5pay life
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 10;
		idec_min_up01 = 0;  //Rp

		indeks_idec_list_premi=3;
		idec_list_premi= new double[indeks_idec_list_premi];
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
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];		
	}

//	 get rate
	public double of_get_rate()
	{
		return idec_rate;	
	}
	
//	 set beg date
	public void of_set_begdate(int thn, int bln, int tgl)
	{
		int li_month=0;
		
		if (is_kurs_id.equalsIgnoreCase("02"))
		{
			ii_end_from = 65;
		}else{
			ii_end_from = 59;
		}

//	If ib_flag_end_age Then
		ii_end_age = ii_age;
//	End if

		li_month = ( ii_end_from - ii_end_age ) * 12;
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
		li_cp = ii_pmode;

		try {

			Double result = query.selectNilai(1, ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, ii_contract_period, ii_tahun_ke, li_umur);
			
			if(result != null) {
			   ldec_rate = result.doubleValue();
			   idec_premi = adec_premi;  
			   if (ldec_rate > 0)
			   {
				   idec_up = (idec_premi * ii_permil / ( ldec_rate));	
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
