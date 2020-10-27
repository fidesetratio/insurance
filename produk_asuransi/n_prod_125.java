package produk_asuransi;
//n_prod_125 POWER SIMPONI US$ (AS)
/*
 * Created on Aug 8, 2005
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
public class n_prod_125 extends n_prod{

	public n_prod_125()
	{
//		power simponi US$
		ii_bisnis_id = 125;
		ii_contract_period = 5;
		ii_age_from = 1;
		ii_age_to = 60;

		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0;   //sekaligus
		
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 0;

		idec_min_up02 = 1000 ;    //US$
		ib_flag_end_age = false;
		ii_end_from = 5;
		usia_nol = 0;

		indeks_idec_list_premi=26;
		idec_list_premi = new double[indeks_idec_list_premi];	
		for (int i =0 ; i < 25 ; i++)
		{
			idec_list_premi[i] =  1000 * i;
		}
	}

	public double of_get_rate()
	{
		return idec_rate;	
	}
	
	public void of_set_premi(double adec_premi)
	{
		idec_premi = adec_premi;
		idec_up = adec_premi;
		ldec_rate = 1000;
//		If ii_age <= 55 Then idec_up = Min (adec_premi, 75000)
//		If ii_age > 55 Then idec_up = Min (adec_premi, 50000)

		idec_rate = ldec_rate;

		of_set_up(idec_up);

	}


	public static void main(String[] args) {
	}
}
