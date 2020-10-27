package produk_asuransi;
// n_prod_36  15 paylife
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
public class n_prod_36 extends n_prod {

	public n_prod_36()
	{
//		SK No. 024/EL-SK/VII/96 (15 Pay Life)
		ii_bisnis_id = 36;
		ii_contract_period = 99;
		ii_age_from = 1;
		ii_age_to = 55;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";

		
//		ii_pmode_list[1] = 0   //sekaligus  (ii_pmode)
		indeks_ii_pmode_list=4;
		ii_pmode_list = new int[indeks_ii_pmode_list];		
		ii_pmode_list[1] = 1;   //triwulanan
		ii_pmode_list[2] = 2;   //semesteran
		ii_pmode_list[3] = 3;   //tahunan

//		di lst_det_bisnis, lsdbs_number cuma 1, 15pay life
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 15;
		flag_uppremi=0;
	}

	public static void main(String[] args) {
	}
}
