// 144 Power Save AS (new)
package produk_asuransi;

import com.ekalife.utils.f_hit_umur;

public class n_prod_144 extends n_prod{

	/**
	 * @param args
	 */
	public n_prod_144()
	{
//		SK 
//		POWER SAVE (AS)
		ii_bisnis_id = 144;
		ii_contract_period = 4;
		ii_age_from = 1;
		ii_age_to = 85;//68;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0;   //sekaligus
		
		indeks_ii_lama_bayar=4;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;

		idec_min_up01=5000000;
		idec_min_up02=500;
		
		ib_flag_end_age = false;
		ii_end_from = 4;


		indeks_idec_list_premi=17;
		idec_list_premi = new double[indeks_idec_list_premi];
		flag_as=1;		
		flag_uppremi=1;
		kode_flag=1;
		flag_account =2;
		usia_nol = 0;
		
		   indeks_mgi=5;
		   mgi = new int[indeks_mgi];
		   mgi[0]= 1;
		   mgi[1]= 3;
		   mgi[2]= 6;
		   mgi[3]=12;
		   mgi[4]=36;
		   
		   indeks_rider_list=8;
			ii_rider = new int[indeks_rider_list];
			ii_rider[0]=813;
			ii_rider[1]=818;
			ii_rider[2]=819;
			ii_rider[3]=820;
			ii_rider[4]=822;
			ii_rider[5]=823;
			ii_rider[6]=825;
			ii_rider[7]=832;
	}	

	public double of_get_rate()
	{
		return idec_rate;	
	}
	
	public void of_set_kurs(String as_kurs)
	{
	
		is_kurs_id = as_kurs;
		if (as_kurs == "02")
		{
			for (int i = 0 ; i <16 ; i++)
			{
				idec_list_premi[i] = 1000 + ( 100 * (i -1) );
			}
		}else{
			for (int i = 0 ; i <16 ; i++)
			{
				idec_list_premi[i] = 10000000 + ( 1000000 * (i - 1) );
			}
		}
	}		

	public void of_set_premi(double adec_premi)
	{
		idec_premi = adec_premi;
		idec_rate = 1000;
		double jumlah_up = 0;
		if (is_kurs_id.equalsIgnoreCase("01"))
		{
			//diatas Rp 1000.000.000
			if (idec_premi > 1000000000)
			{
				jumlah_up = 1000000000;
			}else{
				jumlah_up = idec_premi;
			}
		}else{
			// di atas U$ 100.000
			if (idec_premi > 100000)
			{
				jumlah_up = 100000;
			}else{
				jumlah_up = idec_premi;
			}
		}
		
		of_set_up(jumlah_up);
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);

		hsl="";
		if (bln < 12)
		{
			hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
		}
		if (ii_age > ii_age_to)
		{
			hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
		}
		if (ii_usia_pp > 85)
		{
			hsl="Usia Pemegang Polis maximum : 85 Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
		}
		if (ii_usia_pp < 17)
		{
			hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
		}
		
		return hsl;		
	}	
	
	public double of_get_max_up(){
		double ldec_1 = new Double("99999999999");
		if (is_kurs_id.equalsIgnoreCase("01")) {
			if(ii_bisnis_no == 1){
				ldec_1 = Double.parseDouble("25000000000");
			}
		}else{
			if(ii_bisnis_no == 1){
				ldec_1 = Double.parseDouble("2500000");
			}
		}
		
		return ldec_1;
	}
	
	public static void main(String[] args) {

	}

}
