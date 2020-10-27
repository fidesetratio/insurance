package produk_asuransi;
//n_prod_35 SUPER PROLIFE 20    
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
public class n_prod_35 extends n_prod {

	public n_prod_35()
	{
//		SK No. 017/EL-SK/VI/96 Super Prolife 20
		ii_bisnis_id = 35;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 55;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=5;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0;   //sekaligus
		ii_pmode_list[2] = 1;   //triwulanan
		ii_pmode_list[3] = 2;   //semesteran
		ii_pmode_list[4] = 3;   //tahunan
//
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 20;	
		flag_uppremi=0;	
		
		indeks_rider_list=5;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=801;
		ii_rider[1]=802;
		ii_rider[2]=803;
		ii_rider[3]=804;
		ii_rider[4]=822;
	}	
	


	public static void main(String[] args) {
	}
}
