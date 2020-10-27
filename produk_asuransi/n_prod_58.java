// n_prod_58 EKA SIMPONI US DOLLAR                                  
package produk_asuransi;
import java.math.BigDecimal;
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
public class n_prod_58 extends n_prod{

	public n_prod_58()
	{
//		SK No. 011/EL-SK/VII/99 Eka Simponi
		ii_bisnis_id = 58;
		ii_contract_period = 5;
		ii_age_from = 1;
		ii_age_to = 67; //memo ada 40 spaj palembang dprd 17/04/03
		
		indeks_is_forex=1;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];	
		ii_pmode_list[1] = 0;   //sekaligus
		
//		untuk hitung end date ( 79 - issue_date ) kalo ib_flag_end_age true
		ii_end_from = 5;
		ib_flag_end_age = false;

		idec_min_up01 = 0 ; //Rp
		
		indeks_ii_lama_bayar=1;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];		
		ii_lama_bayar[0] = 1;

		indeks_idec_list_premi=101;
		idec_list_premi = new double[indeks_idec_list_premi];
		for (int i=0 ; i <100; i++)
		{
			idec_list_premi[i] = 1000000 * i;
		}
		flag_uppremi=1;
	}

	public double of_get_rate()
	{
		return idec_rate;
	}
	
//	of_set_premi
	 public void of_set_premi(double adec_premi)
	 {
	 	hsl="";
	 	err="";
		 li_lbayar=0;
		 li_cp=0;
		 ldec_rate=0;
		 li_ltanggung=0;
		 li_umur=0;
		// JOptionPane.showMessageDialog(null, "ii_bisnis_no: " + ii_bisnis_no, "PESAN", JOptionPane.ERROR_MESSAGE);
		// JOptionPane.showMessageDialog(null, "ii_bisnis_no: " + (ii_bisnis_no-1), "PESAN", JOptionPane.ERROR_MESSAGE);
					
		 li_lbayar = ii_lama_bayar[ii_bisnis_no-1];
		 li_ltanggung = ii_contract_period ;//reii_end_from  //ii_contract diganti, karena di table 59


			try {
				Double result = query.selectNilai(ii_jenis , ii_bisnis_id, is_kurs_id, li_cp, li_lbayar, li_ltanggung, ii_tahun_ke, ii_age);
				
				if(result != null) {
				   ldec_rate = result.doubleValue();
				idec_premi = adec_premi;
				if (ldec_rate > 0)
				{
					idec_up = (idec_premi * ii_permil / ( ldec_rate));
					int decimalPlace = 2;
					BigDecimal bd = new BigDecimal(idec_up);
					bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
					idec_up = bd.doubleValue();
				}

				idec_rate = ldec_rate;

				of_set_up(idec_up);
			 }else{	
				hsl="Tidak ada data rate";			 	
			 }
			}
		  catch (Exception e) {
				err=e.toString();
		  } 
	 }
	
	public static void main(String[] args) {
	}
}
