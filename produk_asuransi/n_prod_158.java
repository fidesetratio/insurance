// 158 Power Save Bulanan
package produk_asuransi;

import com.ekalife.utils.f_hit_umur;

public class n_prod_158 extends n_prod {

	public n_prod_158()
	{
//		SK 
//		POWER SAVE Bulanan
		ii_bisnis_id = 158;
		ii_contract_period = 4;
		ii_age_from = 1;
		ii_age_to = 85;//68;

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0] = "01";
		is_forex[1] = "02";
		
//		  1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0;   //sekaligus 

		
//		  untuk hitung end date ( 79 - issue_date )
		ii_end_from = 4;
		ib_flag_end_age = false;
		
		idec_min_up01=100000000;
		idec_min_up02=10000;

		indeks_ii_lama_bayar=9;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 1;
		ii_lama_bayar[6] = 1;
		ii_lama_bayar[7] = 1;
		ii_lama_bayar[8] = 1;
		
		indeks_idec_list_premi=17;
		idec_list_premi = new double[indeks_idec_list_premi];
		usia_nol = 0;
		flag_uppremi=1;
		kode_flag=1;
		flag_account =2;
		flag_powersavebulanan = 1;
		flag_powersave = 3;
		//isInvestasi = true;
		
		   indeks_mgi=4;
		   mgi = new int[indeks_mgi];
		   mgi[0]=3;
		   mgi[1]=6;
		   mgi[2]=12;
		   mgi[3]=36;
	
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
	
	public double of_get_min_up()
	{
		double ldec_1=0;
		if (is_kurs_id.equalsIgnoreCase("01") )
		{
			switch (ii_bisnis_no)
			{
				case 1:
					ldec_1 =100000000; 
					break;
				case 2:
					ldec_1 = 100000000 ;
					break;
				case 3:
					ldec_1 = 100000000;
					break;
				case 4:
					ldec_1 = 100000000;
					break;
				case 5:
					ldec_1 = 30000000; // platinum
					break;
				case 6: //simas prima bulanan
					ldec_1 = 50000000;
					break;
				case 7:
					ldec_1 = 10000000;
					break;
				case 8:
					ldec_1 =27500000; // specta 103/IM-DIR/VIII/2007 Tertanggal 31 Agustus 2007
					break;
				case 9:
					//ldec_1 = 250000000; // smart 104/IM-DIR/VIII/2007 Tertanggal 31 Agustus 2007
					ldec_1 = 150000000; // NO.073 / IM – DIR/ V / 2008
					break;	
				case 10: //platinumsave sblink
					ldec_1 = 30000000;
					break;
				case 11: //platinumsave sblink - specta
					ldec_1 =27500000; 
					break;
				case 12: //platinumsave sblink - smart
					ldec_1 = 150000000;
					break;	
				case 13: //stable save
					ldec_1 = 100000000;
					break;	
				case 14: //danamas prima
					ldec_1 = 100000000;
					break;
				case 15: //stable save bulanan BII
					ldec_1 = 100000000;
					break;	
			}
		}else{
			switch (ii_bisnis_no)
			{
				case 1:
					ldec_1 =10000; 
					break;
				case 2:
					ldec_1 = 10000 ;
					break;
				case 3:
					ldec_1 = 10000;
					break;
				case 4:
					ldec_1 =10000;
					break;
				case 5:
					ldec_1 =3000;
					break;
				case 6: //simas prima bulanan
					ldec_1 = 5000;
					break;
				case 7:
					ldec_1 = 1000;
					break;
				case 8:
					ldec_1 = 2750; // 103/IM-DIR/VIII/2007 Tertanggal 31 Agustus 2007
					break;
				case 9:
					ldec_1 = 15000; // // NO.073 / IM – DIR/ V / 2008
					break;	
				case 10: //platinumsave sblink
					ldec_1 =3000;
					break;
				case 11: //platinumsave sblink - specta
					ldec_1 = 2750;
					break;
				case 12: //platinumsave sblink - smart
					ldec_1 = 15000;
					break;	
				case 13: //stable save
					ldec_1 = 10000;
					break;	
				case 14: //danamas prima bulanan
					ldec_1 = 5000;
					break;
				case 15: //stable save bulanan BII
					ldec_1 = 10000;
					break;
				}
			
			}			
			return ldec_1;
	}

	public double of_get_rate()
	{
		return idec_rate;	
	}	
	
	public void of_set_kurs(String as_kurs)
	{
		is_kurs_id = as_kurs;
		if (as_kurs.equalsIgnoreCase("02"))
		{
			for (int i = 0; i< 16 ; i++)
			{
				idec_list_premi[i] = 1000 + ( 100 * (i -1) );
			}
		}else{
			for (int i = 0 ; i < 16 ;i++)
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

	//get bisnis no
	@Override
	public int of_get_bisnis_no(int flag_bulanan){
		int flag_powersave = f_flag_rate_powersave(ii_bisnis_id, ii_bisnis_no, 1);
		
		flag_as = 0;
		flag_worksite = 0;
		isProductBancass = false;
		
		if(ii_bisnis_no == 2 || ii_bisnis_no == 5 || ii_bisnis_no == 6 || (ii_bisnis_no >= 8 && ii_bisnis_no <= 15)){
			isProductBancass = true;
			if(ii_bisnis_no == 13){
				flag_worksite = 1;
			}
		}else if(ii_bisnis_no == 4){
			flag_worksite = 1;
		}else if(ii_bisnis_no == 3){
			flag_as = 1;
		}else if(ii_bisnis_no == 7){
			flag_as = 2;
		}
		
		return flag_powersave;	
	}
	
	@Override
	public void cek_flag_agen(int kode_produk, int no_produk, int flag_bulanan){
		flag_powersave = f_flag_rate_powersave(ii_bisnis_id, ii_bisnis_no, 1);
		
		flag_as = 0;
		flag_worksite = 0;
		isProductBancass = false;
		ii_bisnis_no= no_produk;
		
		if(ii_bisnis_no == 2 || ii_bisnis_no == 5 || ii_bisnis_no == 6 || (ii_bisnis_no >= 8 && ii_bisnis_no <= 15)){
			isProductBancass = true;
			if(ii_bisnis_no== 13){
				flag_worksite = 1;
			}
		}else if(ii_bisnis_no == 4){
			flag_worksite = 1;
		}else if(ii_bisnis_no == 3){
			flag_as = 1;
		}else if(ii_bisnis_no == 7){
			flag_as = 2;
		}
	}
	
	public void cek_mgi(int nomor_bisnis)
	{
		//save bayar link
		if(nomor_bisnis == 10 || nomor_bisnis == 11 || nomor_bisnis == 12) {
			indeks_mgi=4;
			mgi = new int[indeks_mgi];
			mgi[0]= 12;
			mgi[1]= 24;
			mgi[2]= 36;
			mgi[3]= 48;
		}else {
			indeks_mgi=4;
			mgi = new int[indeks_mgi];
			mgi[0]= 3;
			mgi[1]= 6;
			mgi[2]= 12;
			mgi[3]= 36;
		}
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