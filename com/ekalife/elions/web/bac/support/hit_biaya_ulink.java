package com.ekalife.elions.web.bac.support;


/*
 * Created on Sep 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author hemilda
 *
 * untuk perhitungan produk link
 */
public class hit_biaya_ulink {

	/**
	 * @author HEMILDA
	 * hitung alokasi biaya produk link
	 */	
	public double hit_jumlah_invest(String premi,String persen, String total_biaya)
	{
		double ld_premi_invest=0;
		double mdu_jumlah=0;
		ld_premi_invest = Double.parseDouble(premi)-Double.parseDouble(total_biaya);
		mdu_jumlah=Double.parseDouble(persen)/100*ld_premi_invest;
		return mdu_jumlah;
	}
	
	/**
	 * @author HEMILDA
	 * validasi pilihan top up sesuai dengan cara bayar
	 */	
	public String top_up_change(String jenis_topup, String flag_kode, String cara_bayar)
	{
		String hsl="";
		if (Integer.parseInt(jenis_topup)==1)
		{
			if (Integer.parseInt(cara_bayar)==0)
			{
				hsl="Cara Bayar Sekaligus Pilih Top-Up Tunggal !";	
			}
		}else{
			if  (Integer.parseInt(jenis_topup)==2)
			{
				if (Integer.parseInt(flag_kode)==3)
				{
					hsl="Pilih Top-Up Platinum !!!";
				}
			}else{
				if  (Integer.parseInt(jenis_topup)==3)
				{
					if (Integer.parseInt(flag_kode)==3)
					{
						hsl="Top-Up Platinum Hanya Untuk Excellink Platinum !!!";
					}				
				}
			}
		}
		return hsl;
	}
		
	public static void main(String[] args) {
	}
}
