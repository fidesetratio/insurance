//n_prod_32 10 PAY LIFE                   
package produk_asuransi;
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
public class n_prod_32 extends n_prod{
	int ii_pay_period=0;
	public n_prod_32()
	{
//		SK No. 024/EL-SK/VII/96 (10 Pay Life)
		ii_bisnis_id = 32;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 55;
		ib_flag_pp = true;
		
		indeks_is_forex=2;
		is_forex=new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=4;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 1 ;  //triwulanan
		ii_pmode_list[2] = 2 ;  //semesteran
		ii_pmode_list[3] = 3 ;  //tahunan


//		untuk hitung end date ( 99 - issue_date )
		ii_end_from = 99;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 10;
		flag_uppremi=0;
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		usia_nol = 0;
		
	}	

	public static void main(String[] args) {

		
	}
}
