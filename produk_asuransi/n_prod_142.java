// 142 Power Save (BA)
package produk_asuransi;

import com.ekalife.utils.f_hit_umur;

public class n_prod_142 extends n_prod{

	/**
	 * @param args
	 */
	public n_prod_142()
	{
//		NO. 056/EL-SK/XI/2005
//		power save new
		ii_bisnis_id = 142;
		ii_contract_period = 4;
		ii_age_from = 1;
		ii_age_to = 85;//68; SK 079/AJS-SK/VII/2009 memang lebih baru dari IM-095, akan tetapi IM-095 masih berlaku.Jadi USIA sampai 70 tahun

		indeks_is_forex=2;
		is_forex= new String[indeks_is_forex];
		is_forex[0]="01";
		is_forex[1]="02";
		
//		1..4 cuma id, 0..3 di lst_cara_bayar
		indeks_ii_pmode_list=2;
		ii_pmode_list = new int[indeks_ii_pmode_list];
		ii_pmode_list[1] = 0;   //sekaligus
		
		indeks_ii_lama_bayar=6;
		ii_lama_bayar = new int[indeks_ii_lama_bayar];
		ii_lama_bayar[0] = 1;
		ii_lama_bayar[1] = 1;
		ii_lama_bayar[2] = 1;
		ii_lama_bayar[3] = 1;
		ii_lama_bayar[4] = 1;
		ii_lama_bayar[5] = 1;

		idec_min_up01=5000000;
		idec_min_up02=500;
		
		ib_flag_end_age = false;
		ii_end_from = 4;

		indeks_idec_list_premi=17;
		idec_list_premi = new double[indeks_idec_list_premi];
		usia_nol = 0;	
		flag_uppremi=1;
		kode_flag=1;
		flag_account =2;

		indeks_mgi=6;
		mgi = new int[indeks_mgi];
		mgi[0]= 1;
		mgi[1]= 3;
		mgi[2]= 6;
		mgi[3]=12;
		mgi[4]=24;
		mgi[5]=36;
		
		indeks_rider_list=7;
		ii_rider = new int[indeks_rider_list];
		ii_rider[0]=813;
		ii_rider[1]=818;
		ii_rider[2]=819;
		ii_rider[3]=822;
		ii_rider[4]=823;
		ii_rider[5]=825;
		ii_rider[6]=832;
		
	}
	
	public double of_get_rate()
	{
		return idec_rate;	
	}
	
	
	
//	get bisnis no
	@Override
	public int of_get_bisnis_no(int flag_bulanan){
		int flag_powersave=0;
		flag_powersave = f_flag_rate_powersave(ii_bisnis_id, ii_bisnis_no, flag_bulanan);
		
		if(ii_bisnis_no == 1 || ii_bisnis_no == 8){
			isProductBancass = false;
		}else{
			isProductBancass = true;
		}
		
		return flag_powersave;	
	}
	
	@Override
	public void cek_flag_agen(int kode_produk, int no_produk, int flag_bulanan){
		flag_powersave = f_flag_rate_powersave(kode_produk, no_produk, flag_bulanan);
		
		if(no_produk == 1 || no_produk == 8){
			isProductBancass = false;
		}else{
			isProductBancass = true;
		}
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
			if(ii_bisnis_no != 13){
				//diatas Rp 1000.000.000
				if (idec_premi > 1000000000)
				{
					jumlah_up = 1000000000;
				}else{
					jumlah_up = idec_premi;
				}
			} else { //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				jumlah_up = idec_premi;
			}
		}else{
			if(ii_bisnis_no != 13){
				// di atas U$ 100.000
				if (idec_premi > 100000)
				{
					jumlah_up = 100000;
				}else{
					jumlah_up = idec_premi;
				}
			} else { //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				jumlah_up = idec_premi;
			}
		}
		
		of_set_up(jumlah_up);
	}

	public String of_check_usia(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2,int lm_byr,int nomor_produk) 
	{
		f_hit_umur umr =new f_hit_umur();
		int bln=umr.bulan(tahun1,bulan1,tanggal1,tahun2,bulan2,tanggal2);
		
		if(ii_bisnis_no == 13) //helpdesk [133346] produk baru 142-13 Smart Investment Protection
			ii_age_to = 65;

		hsl="";
		
			if (bln < 12)
			{
				hsl="Usia Masuk Plan ini minimum : " + ii_age_from;
			}
			if (ii_age > ii_age_to)
			{
				hsl="Usia Masuk Plan ini maximum : " + ii_age_to;
			}
			if (ii_usia_pp > 100)//menurut vito tidak ada batasan untuk umur pemegang polis
			{
				hsl="Usia Pemegang Polis maximum : 100 Tahun !!!"; // permintaan Achmad Anwarudin Tuesday, November 06, 2007 11:03 AM
			}
			if (ii_usia_pp < 17)
			{
				hsl="Usia Pemegang Polis minimum : 17 Tahun !!!";
			}
		
		return hsl;		
	}	
	
	public void cek_mgi(int nomor_bisnis)
	{
		switch (nomor_bisnis)
		{		
			case 2:
				//Konversi Simas prima (stand alone) masa asuransi dari 4 tahun menjadi 1 tahun dan ubah mgi jadi 3 6 12 saja //Chandra				
				indeks_mgi=3;
				mgi = new int[indeks_mgi];
				mgi[0]= 3;
				mgi[1]= 6;
				mgi[2]=12;
			  break;
			case 3:
				indeks_mgi = 4;
				mgi = new int[indeks_mgi];
				mgi[0]= 1;
				mgi[1]= 3;
				mgi[2]= 6;
				mgi[3]=12;
				break;
			case 7:
				indeks_mgi = 6;
				mgi = new int[indeks_mgi];
				mgi[0]= 1;
				mgi[1]= 3;
				mgi[2]= 6;
				mgi[3]=12;
				mgi[4]=24;
				mgi[5]=36;
				break;
			case 9:
				indeks_mgi = 6;
				mgi = new int[indeks_mgi];
				mgi[0]= 1;
				mgi[1]= 3;
				mgi[2]= 6;
				mgi[3]=12;
				mgi[4]=24;
				mgi[5]=36;
				break;
			case 11:
				indeks_mgi = 1;
				mgi = new int[indeks_mgi];
				mgi[0]=36;
				break;
			case 13: //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				indeks_mgi = 1;
				mgi = new int[indeks_mgi];
				mgi[0]=6;
				break;
			default :
			   indeks_mgi=6;
			   mgi = new int[indeks_mgi];
			   mgi[0]= 1;
			   mgi[1]= 3;
			   mgi[2]= 6;
			   mgi[3]=12;
			   mgi[4]=24;
			   mgi[5]=36;
			   break;
		}	
	}
	
	public String of_get_min_up_permgi(int nomor_bisnis , int mgi, double premi,String kurs)
	{
		String hasil="";
		/*if (nomor_bisnis == 2)
		{
			if (mgi == 1)
			{
				if (kurs.equalsIgnoreCase("01"))
				{
					if (premi < 3e9)
					{
						hasil = "Minimal Premi plan ini untuk MGI 1 bulan adalah Rp 3.000.000.000,-.";
						//logger.info(3e9);
					}
				}else{
					if (premi < 300000)
					{
						hasil = "Minimal Premi plan ini untuk MGI 1 bulan adalah U$ 300.000,-.";
					}					
				}
			}
		}*/
		return hasil;
	}
	
	public double of_get_min_up()
	{
		double ldec_1=0;
		if (ii_bisnis_no == 3)
		{
			if (is_kurs_id.equalsIgnoreCase("01") )
			{
				ldec_1 = 25000000;
			}else{
				ldec_1 = 2500;
			}
		}else if (ii_bisnis_no == 4)//mayapada 
			{
				if (is_kurs_id.equalsIgnoreCase("01") )
				{
					ldec_1 = 30000000;//SK No. 079/AJS-SK/X/2007
				}else{
					ldec_1 = 3000;
				}
			}else if (ii_bisnis_no == 5 || ii_bisnis_no == 6 || ii_bisnis_no == 7)
				{
					if (is_kurs_id.equalsIgnoreCase("01") )
					{
						//ldec_1 = 100000000;
						ldec_1 = idec_min_up01;//special case
					}else{
						//ldec_1 = 10000;
						ldec_1 = idec_min_up02;//special case
					}
				}else if (ii_bisnis_no == 11)
				{
					if (is_kurs_id.equalsIgnoreCase("01") )
					{
						ldec_1 = 10000000;
					}else{
						ldec_1 = 1000;
					}
				}else if (ii_bisnis_no == 13){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
					if (is_kurs_id.equalsIgnoreCase("01")){
						ldec_1 = 50000000;
					} else {
						ldec_1 = 5000;
					}
				}else{
					if (is_kurs_id.equalsIgnoreCase("01") )
					{
						ldec_1 = idec_min_up01;
					}else{
						ldec_1 = idec_min_up02;
					}
				}
		return ldec_1;
	}
	
	public double of_get_max_up(){
		double ldec_1 = new Double("99999999999");
		if (is_kurs_id.equalsIgnoreCase("01")) {
			if(ii_bisnis_no == 1){
				ldec_1 = Double.parseDouble("25000000000");
			}else if(ii_bisnis_no == 11){
				ldec_1 = Double.parseDouble("100000000000");
			}else if(ii_bisnis_no == 13){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				if(ii_age >= 1 & ii_age <= 55){
					ldec_1 = Double.parseDouble("2000000000");
				} else if(ii_age >= 56 & ii_age <= 65){
					ldec_1 = Double.parseDouble("1600000000");
				}
			}
		}else{
			if(ii_bisnis_no == 1){
				ldec_1 = Double.parseDouble("2500000");
			}else if(ii_bisnis_no == 11){
				ldec_1 = Double.parseDouble("10000000");
			}else if(ii_bisnis_no == 13){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				if(ii_age >= 1 & ii_age <= 55){
					ldec_1 = Double.parseDouble("200000");
				} else if(ii_age >= 56 & ii_age <= 65){
					ldec_1 = Double.parseDouble("160000");
				}
			}
		}
		
		return ldec_1;
	}
	
	public static void main(String[] args) {

	}

}
