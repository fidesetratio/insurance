// n_prod_33 Ekasiswa ideal
package produk_asuransi;
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
public class n_prod_33 extends n_prod{

	public n_prod_33()
	{
//		SK No. 009/EL-SK/I/96 EkaSiswa Ideal
		ii_bisnis_id = 33;
		ii_contract_period = 23;
		ii_age_from = 20;
		ii_age_to = 50;
		ib_flag_pp = true;
		
		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=4;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 1 ;  //triwulanan
		ii_pmode_list[2] = 2 ;  //semesteran
		ii_pmode_list[3] = 3 ;  //tahunan

//		untuk hitung end date ( 99 - issue_date )
		ii_end_from = 23;
		ib_flag_end_age = false;

		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 18;
		idec_min_up02 = 5000;	
		flag_uppremi=0;	
		
		indeks_rider_list=0;
		ii_rider = new int[indeks_rider_list];
		usia_nol = 1;
	}	

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk)
	{
		hsl="";
//		   Chek Pemegang Polis
		if (ii_age < ii_age_from)
		{
			hsl="Usia Pemegang Polis untuk Plan ini minimum : " +  ii_age_from  ;
		}

		if (ii_age > ii_age_to)
		{
			hsl="Usia Pemegang Polis untuk Plan ini maximum : " +  ii_age_to ;
		}

//		   Chek Tertanggung Polis
		if (ii_usia_tt < 1)
		{
			hsl="Usia Anak untuk Plan ini minimum : 1 Tahun" ;
		}

		if (ii_usia_tt > 12)
		{
			hsl="Usia Anak untuk Plan ini maximum : 12 Tahun" ;
		}
		return hsl;		
	}
	
	public int of_get_payperiod()
	{
//		18 - umur anak
		ii_lama_bayar[0] = (18 - ii_usia_tt);
		return ii_lama_bayar[0];	 
	}	

	public void of_set_usia_tt(int ai_tt)
	{
		if (ai_tt < 1)
		{
			ii_usia_tt = 1;
		}else{
			ii_usia_tt = ai_tt;
		}
		of_set_age();
//		  ii_contract_period = 23 - ii_usia_pp
		ii_contract_period = 23 - ii_usia_tt;
		ii_end_from = ii_contract_period;
		ii_lama_bayar[0] = (18 - ii_usia_tt);
	}

	public static void main(String[] args) {

		
	}
}
