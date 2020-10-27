package com.ekalife.elions.web.bac.support;
import com.ekalife.utils.f_cek_tanggal;
import com.ekalife.utils.f_validasi;

/*
 * Created on Sep 15, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Hemilda
 *
 *validasi form investasi pada penginputan bac
 */
public class form_investasi {

	/**
	 * @author HEMILDA
	 * validasi jumlah top up kalau kosong ketika memilih top up
	 */
	public String jmlh_topup(String topup)
	{
		if (topup==null)
		{
			topup="";
		}
		boolean cekk;
		String hsl="";
		if (topup.trim().length()==0)
		{
		/*	f_validasi data = new f_validasi();

		/	cekk= data.f_validasi_numerik_titikcoma(topup);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan jumlah top up premium dalam bentuk numerik";
			}
		}else{*/
			hsl="Total Top Up Premium masih kosong, silahkan isi total top up premium dahulu.";
		}
		return hsl;		
	}
	
	/**
	 * @author HEMILDA
	 * validasi pilihan top up  kalau jumlah top up diisi
	 */
	public String cek_topup(Double topup, String jns)
	{
		boolean cekk;
		String hsl="";
		if (topup==null)
		{
			topup = new Double(0);
		}
		if (jns==null)
		{
			jns = "0";
		}
		
		if ((topup.doubleValue()==0 ) && !jns.equalsIgnoreCase("0"))
		{
			hsl="Silahkan isi total top up premium > 0 dahulu kecuali jenis top up premium none.";
		}
		return hsl;		
	}	
	
	/**
	 * @author HEMILDA
	 * validasi jenis nasabah
	 */
	public String cek_jenis_nasabah(String jenis_nasabah)
	{
		boolean cekk;
		String hsl="";
		if (jenis_nasabah==null)
		{
			jenis_nasabah="0";
		}
		
		/*if (jenis_nasabah.equalsIgnoreCase("0"))
		{
			hsl="Silahkan pilih jenis nasabah selain None terlebih dahulu untuk produk ini.";
		}*/
		return hsl;		
	}	

	/**
	 * @author HEMILDA
	 * validasi pilihan jenis top up kalau isian jumlah top up diisi
	 */
	public String jenis_topup(Double topup, String jns)
	{
		boolean cekk;
		String hsl="";
		if ( topup==null)
		{
			topup= new Double(0);
		}
		if (jns==null)
		{
			jns="0";
		}
		if (topup.doubleValue()>0)
		{
			if (jns.equalsIgnoreCase("0"))
			{
				hsl="Silahkan pilih Jenis Top Up Premium jika ada, jika tidak ada silahkan kosongkan Total Top Up Premium.";
			}
		}
		if (!jns.equalsIgnoreCase("0"))
		{
			if (topup.doubleValue()==0)
			{
				hsl="Silahkan isi Total Top Up Premium.";
			}
		}
		
		if (hsl.trim().length()==0)
		{
			if (jns.equalsIgnoreCase("3"))
			{
				hsl="Silahkan pilih Jenis Top Up Premium yang lain, Jenis Top Up ini hanya untuk Excellink Platinum.";
			}
		}
		return hsl;		
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian fixed
	 */
	public String fixed(String fixed)
	{
		boolean cekk;
		String hsl="";
		if (fixed==null)
		{
			fixed="0";
		}
		if (fixed.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(fixed);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Excellink Fixed Income Fund dalam bentuk numerik";
			}
		}else{
			hsl="Silahkan masukkan terlebih dahulu persentase Excellink Fixed Income Fund";
		}
		return hsl;		
	}

	/**
	 * @author HEMILDA
	 * validasi isian dyna
	 */
	public String dyna(String dyna)
	{
		boolean cekk;
		String hsl="";
		if (dyna==null)
		{
			dyna="0";
		}
		if (dyna.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(dyna);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Excellink Fixed Dynamic Fund dalam bentuk numerik";
			}
		}else{
			hsl="Silahkan masukkan terlebih dahulu persentase Excellink Fixed Dynamic Fund";
		}
		return hsl;		
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian aggressive
	 */
	public String aggresive(String aggresive)
	{
		boolean cekk;
		String hsl="";
		if (aggresive==null)
		{
			aggresive="0";
		}
		if (aggresive.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(aggresive);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Excellink Fixed Aggressive Fund dalam bentuk numerik";
			}
		}else{
			hsl="Silahkan masukkan terlebih dahulu persentase Excellink Fixed Aggressive Fund";
		}
		return hsl;		
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian secure dollar
	 */
	public String secure_dlr(String secure_dlr)
	{
		boolean cekk;
		String hsl="";
		if (secure_dlr==null)
		{
			secure_dlr="0";
		}
		if (secure_dlr.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(secure_dlr);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Excellink Secure Dollar Income Fund dalam bentuk numerik";
			}
		}else{
			hsl="Silahkan masukkan terlebih dahulu persentase Excellink Fixed Aggressive Fund";
		}
		return hsl;		
	}		

	/**
	 * @author HEMILDA
	 * validasi isian syariah fixed
	 */
	public String syariahfixed(String sfixed)
	{
		boolean cekk;
		String hsl="";
		if (sfixed==null)
		{
			sfixed="0";
		}
		if (sfixed.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(sfixed);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Excellink Fixed Syariah Fund dalam bentuk numerik";
			}
		}else{
			hsl="Silahkan masukkan terlebih dahulu persentase Excellink Fixed Syariah Fund";
		}
		return hsl;		
	}			

	/**
	 * @author HEMILDA
	 * validasi isian syariah dyna
	 */
	public String syariahdyna(String sdyna)
	{
		boolean cekk;
		String hsl="";
		if (sdyna==null)
		{
			sdyna="0";
		}
		if (sdyna.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(sdyna);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Excellink Dynamic Syariah Fund dalam bentuk numerik";
			}
		}else{
			hsl="Silahkan masukkan terlebih dahulu persentase Excellink Dynamic Syariah Fund";
		}
		return hsl;		
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian dyna dollar
	 */
	public String dyna_dlr(String dyna_dlr)
	{
		boolean cekk;
		String hsl="";
		if (dyna_dlr==null)
		{
			dyna_dlr="0";
		}
		if (dyna_dlr.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(dyna_dlr);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Excellink Dynamic Dollar Fund dalam bentuk numerik";
			}
		}else{
			hsl="Silahkan masukkan terlebih dahulu persentase xcellink Dynamic Dollar Fund";
			
		}
		return hsl;		
	}	

	/**
	 * @author HEMILDA
	 * validasi isian kelipatan fixed dynamic
	 */
	public String cek_fixed(String fixed, Integer flag_ekalink)
	{
		String hsl="";
		if (fixed==null)
		{
			fixed="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(fixed)!=0) && (Integer.parseInt(fixed) % 20 !=0))
//			{
//				hsl="Nilai Persentase Excellink Fixed Dynamic Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(fixed)!=0) && (Integer.parseInt(fixed) % 10 !=0))
			{
				hsl="Nilai Persentase Excellink Fixed Dynamic Fund harus kelipatan 10";
			}			
//		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan fixed dynamic
	 */
	public String cek_ad_fixed(String fixed, Integer flag_ekalink)
	{
		String hsl="";
		if (fixed==null)
		{
			fixed="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(fixed)!=0) && (Integer.parseInt(fixed) % 20 !=0))
//			{
//				hsl="Nilai Persentase SECURE DOLLAR INCOME FUND harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(fixed)!=0) && (Integer.parseInt(fixed) % 10 !=0))
			{
				hsl="Nilai Persentase SECURE DOLLAR INCOME FUND harus kelipatan 10";
			}			
//		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan fixed dynamic
	 */
	public String cek_a_fixed(String fixed, Integer flag_ekalink)
	{
		String hsl="";
		if (fixed==null)
		{
			fixed="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(fixed)!=0) && (Integer.parseInt(fixed) % 20 !=0))
//			{
//				hsl="Nilai Persentase FIXED INCOME FUND harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(fixed)!=0) && (Integer.parseInt(fixed) % 10 !=0))
			{
				hsl="Nilai Persentase FIXED INCOME FUND harus kelipatan 10";
			}			
//		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi isian kelipatan fixed dynamic
	 */
	public String cek_dynamic(String dynamic, Integer flag_ekalink)
	{
		String hsl="";
		if (dynamic==null)
		{
			dynamic="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(dynamic)!=0)&&(Integer.parseInt(dynamic) % 20!=0))
//			{
//				hsl="Nilai Persentase Excellink Fixed Dynamic Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(dynamic)!=0)&&(Integer.parseInt(dynamic) % 10!=0))
			{
				hsl="Nilai Persentase Excellink Fixed Dynamic Fund harus kelipatan 10";
			}			
//		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan fixed dynamic
	 */
	public String cek_ad_dynamic(String dynamic, Integer flag_ekalink)
	{
		String hsl="";
		if (dynamic==null)
		{
			dynamic="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(dynamic)!=0)&&(Integer.parseInt(dynamic) % 20!=0))
//			{
//				hsl="Nilai Persentase DYNAMIC DOLLAr INCOME FUND harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(dynamic)!=0)&&(Integer.parseInt(dynamic) % 10!=0))
			{
				hsl="Nilai Persentase DYNAMIC DOLLAr INCOME FUND harus kelipatan 10";
			}			
//		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan fixed dynamic
	 */
	public String cek_a_dynamic(String dynamic, Integer flag_ekalink)
	{
		String hsl="";
		if (dynamic==null)
		{
			dynamic="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(dynamic)!=0)&&(Integer.parseInt(dynamic) % 20!=0))
//			{
//				hsl="Nilai Persentase DYNAMIC INCOME FUND harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(dynamic)!=0)&&(Integer.parseInt(dynamic) % 10!=0))
			{
				hsl="Nilai Persentase DYNAMIC INCOME FUND harus kelipatan 10";
			}			
//		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan fixed aggressive
	 */
	public String cek_aggressive(String aggressive, Integer flag_ekalink)
	{
		String hsl="";
		if (aggressive==null)
		{
			aggressive="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(aggressive)!=0)&&(Integer.parseInt(aggressive) % 20!=0))
//			{
//				hsl="Nilai Persentase Excellink Fixed Aggressive Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(aggressive)!=0)&&(Integer.parseInt(aggressive) % 10!=0))
			{
				hsl="Nilai Persentase Excellink Fixed Aggressive Fund harus kelipatan 10";
			}			
//		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan fixed aggressive
	 */
	public String cek_a_aggressive(String aggressive, Integer flag_ekalink)
	{
		String hsl="";
		if (aggressive==null)
		{
			aggressive="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(aggressive)!=0)&&(Integer.parseInt(aggressive) % 20!=0))
//			{
//				hsl="Nilai Persentase AGGRESSIVE INCOME FUND harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(aggressive)!=0)&&(Integer.parseInt(aggressive) % 10!=0))
			{
				hsl="Nilai Persentase AGGRESSIVE INCOME FUND harus kelipatan 10";
			}			
//		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan aggressive syariah
	 */
	public String cek_aggressive_secure(String aggressive, Integer flag_ekalink)
	{
		String hsl="";
		if (aggressive==null)
		{
			aggressive="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(aggressive)!=0)&&(Integer.parseInt(aggressive) % 20!=0))
//			{
//				hsl="Nilai Persentase Excellink Aggresive Syariah Fund  harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(aggressive)!=0)&&(Integer.parseInt(aggressive) % 10!=0))
			{
				hsl="Nilai Persentase Excellink Aggresive Syariah Fund  harus kelipatan 10";
			}			
//		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan secure
	 */
	public String cek_secure(String secure, Integer flag_ekalink)
	{
		String hsl="";
		if (secure==null)
		{
			secure = "0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(secure)!=0)&&(Integer.parseInt(secure) % 20!=0))
//			{
//				hsl="Nilai Persentase Excellink Secure Dollar Income Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(secure)!=0)&&(Integer.parseInt(secure) % 10!=0))
			{
				hsl="Nilai Persentase Excellink Secure Dollar Income Fund harus kelipatan 10";
			}			
//		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan dynamic dollar
	 */
	public String cek_dyna(String dyna, Integer flag_ekalink)
	{
		String hsl="";
		if (dyna==null)
		{
			dyna ="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(dyna)!=0)&&(Integer.parseInt(dyna) % 20!=0))
//			{
//				hsl="Nilai Persentase Excellink Dynamic Dollar Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(dyna)!=0)&&(Integer.parseInt(dyna) % 10!=0))
			{
				hsl="Nilai Persentase Excellink Dynamic Dollar Fund harus kelipatan 10";
			}			
//		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi isian kelipatan dynamic syariah
	 */
	public String cek_sdyna(String sdyna, Integer flag_ekalink)
	{
		String hsl="";
		if (sdyna==null)
		{
			sdyna="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(sdyna)!=0)&&(Integer.parseInt(sdyna) % 20!=0))
//			{
//				hsl="Nilai Persentase Excellink Dynamic Syariah Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(sdyna)!=0)&&(Integer.parseInt(sdyna) % 10!=0))
			{
				hsl="Nilai Persentase Excellink Dynamic Syariah Fund harus kelipatan 10";
			}			
//		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan syariah dyna dollar
	 */
	public String cek_sdyna_s(String sdyna, Integer flag_ekalink)
	{
		String hsl="";
		if (sdyna==null)
		{
			sdyna="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(sdyna)!=0)&&(Integer.parseInt(sdyna) % 20!=0))
//			{
//				hsl="Nilai Persentase Excellink Dynamic $ Syariah Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(sdyna)!=0)&&(Integer.parseInt(sdyna) % 10!=0))
			{
				hsl="Nilai Persentase Excellink Dynamic $ Syariah Fund harus kelipatan 10";
			}			
//		}
		return hsl;
	}		
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan syariah  fixed
	 */
	public String cek_sfixed(String sfixed, Integer flag_ekalink)
	{
		String hsl="";
		if (sfixed==null)
		{
			sfixed="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(sfixed)!=0) && (Integer.parseInt(sfixed) % 20 !=0))
//			{
//				hsl="Nilai Persentase Excellink Fixed Syariah Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(sfixed)!=0) && (Integer.parseInt(sfixed) % 10 !=0))
			{
				hsl="Nilai Persentase Excellink Fixed Syariah Fund harus kelipatan 10";
			}			
//		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian kelipatan secure syariah dollar
	 */
	public String cek_sfixed_s(String sfixed, Integer flag_ekalink)
	{
		String hsl="";
		if (sfixed==null)
		{
			sfixed="0";
		}
//		if (flag_ekalink.intValue() == 0)
//		{
//			if ((Integer.parseInt(sfixed)!=0) && (Integer.parseInt(sfixed) % 20 !=0))
//			{
//				hsl="Nilai Persentase Excellink Secure $ Syariah Fund harus kelipatan 20";
//			}
//		}else{
			if ((Integer.parseInt(sfixed)!=0) && (Integer.parseInt(sfixed) % 10 !=0))
			{
				hsl="Nilai Persentase Excellink Secure $ Syariah Fund harus kelipatan 10";
			}
//		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi jumlah dyna dan secure dollar
	 */
	public String cek_jml_dlr(String secure,String dyna)
	{
		String hsl="";
		if (secure==null)
		{
			secure="0";
		}
		if (dyna==null)
		{
			dyna="0";
		}
		if (Integer.parseInt(dyna) + Integer.parseInt(secure) !=100)
		{
			hsl="Nilai jumlah persentase Excellink Secure Dollar Income Fund dengan persentase Excellink Dynamic Dollar Fund harus 100%";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi jumlah fixed dynamic aggressive
	 */
	public String cek_jml_rp(String fixed,String dyna, String aggresive)
	{
		String hsl="";
		if (fixed==null)
		{
			fixed="0";
		}
		if (dyna==null)
		{
			dyna="0";
		}
		if (aggresive==null)
		{
			aggresive="0";
		}
		if (Integer.parseInt(fixed) + Integer.parseInt(dyna) + Integer.parseInt(aggresive) !=100)
		{
			hsl="Nilai jumlah persentase Excellink Fixed Dynamic Fund dengan persentase Excellink Fixed Dynamic Fund dengan Excellink Fixed Aggressive Fund harus 100%";
		}
		return hsl;
	}		
	
	/**
	 * @author HEMILDA
	 * validasi jumlah fixed , dynamic
	 */
	public String cek_jml_fd(String fixed,String dyna)
	{
		String hsl="";
		if (fixed==null)
		{
			fixed = "0";
		}
		if(dyna==null)
		{
			dyna="0";
		}
		if (Integer.parseInt(fixed) + Integer.parseInt(dyna)  !=100)
		{
			hsl="Nilai jumlah persentase Excellink Fixed Dynamic Fund dengan persentase Excellink Fixed Dynamic Fund  harus 100%";
		}
		return hsl;
	}		
	
	/**
	 * @author HEMILDA
	 * validasi jumlah ficed syariah dan dynamic syariah
	 */
	public String cek_jml_sfd(String sfixed,String sdyna)
	{
		String hsl="";
		if (sfixed==null)
		{
			sfixed="0";
		}
		if(sdyna==null )
		{
			sdyna="0";
		}
		if (Integer.parseInt(sfixed) + Integer.parseInt(sdyna)  !=100)
		{
			hsl="Nilai jumlah persentase Excellink Fixed Syariah Fund dengan persentase Excellink Dynamic Syariah Fund  harus 100%";
		}
		return hsl;
	}	
	
	
	/**
	 * @author HEMILDA
	 * validasi jumlah ficed syariah dan dynamic syariah
	 */
	public String cek_jml_a_sfd(String sfixed,String sdyna)
	{
		String hsl="";
		if (sfixed==null)
		{
			sfixed="0";
		}
		if(sdyna==null )
		{
			sdyna="0";
		}
		if (Integer.parseInt(sfixed) + Integer.parseInt(sdyna)  !=100)
		{
			hsl="Nilai jumlah persentase SECURE DOLLAR INCOME FUND dengan persentase DYNAMIC DOLLAr INCOME FUND harus 100%";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi jumlah ficed syariah dan dynamic syariah
	 */
	public String cek_jml_a_fda(String sfixed,String sdyna ,String saggr)
	{
		String hsl="";
		if (sfixed==null)
		{
			sfixed="0";
		}
		if(sdyna==null )
		{
			sdyna="0";
		}
		if(saggr==null )
		{
			saggr="0";
		}
		if (Integer.parseInt(sfixed) + Integer.parseInt(sdyna) + Integer.parseInt(saggr)  !=100)
		{
			hsl="Jumlah % FIXED INCOME FUND dengan % DYNAMIC INCOME FUND dengan % AGGRESSIVE INCOME FUND harus 100 %";
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi jumlah secure dollar syariah  dan dynamic dollar syariah
	 */
	public String cek_jml_sfd_s(String sfixed,String sdyna)
	{
		String hsl="";
		if (sfixed==null)
		{
			sfixed="0";
		}
		if(sdyna==null )
		{
			sdyna="0";
		}
		if (Integer.parseInt(sfixed) + Integer.parseInt(sdyna)  !=100)
		{
			hsl="Nilai jumlah persentase Excellink Secure $ Syariah Fund dengan persentase Excellink Dynamic $ Syariah Fund  harus 100%";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi jumlaha fixed syariah dan dynamic syariah dan aggressive syariah
	 */
	public String cek_jml_sfda(String sfixed,String sdyna,String saggr)
	{
		String hsl="";
		if (sfixed==null)
		{
			sfixed="0";
		}
		if(sdyna==null )
		{
			sdyna="0";
		}
		if(saggr==null )
		{
			saggr="0";
		}
		if (Integer.parseInt(sfixed) + Integer.parseInt(sdyna) + Integer.parseInt(saggr) !=100)
		{
			hsl="Nilai jumlah persentase Excellink Fixed Syariah Fund dengan persentase Excellink Dynamic Syariah Fund dengan Excellink Aggresive Syariah Fund harus 100%";
		}
		return hsl;
	}		

	/**
	 * @author HEMILDA
	 * validasi isian persentase penerima manfaat
	 */
	public String persen_manfaat(String persen)
	{
		boolean cekk = true;
		String hsl="";
		if (persen==null)
		{
			persen ="0";
		}
		if(persen.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(persen);

			if (cekk==false)
			{
				hsl="Silahkan masukkan persentase penerima manfaat dalam bentuk numerik";
			}
		}
		return hsl;			
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian cek tanggal
	 */
	public String cek_tanggal(String thn, String bln, String tgl)
	{
		boolean b=true;
		boolean cekk1=true;
		boolean cekk2=true;
		boolean cekk3=true;
		int tgl1=0;
		int bln1=0;
		int thn1=0;
		String hsl="";
		if (tgl==null)
		{
			tgl="";
		}
		if (bln==null)
		{
			bln="";
		}
		if (thn==null)
		{
			thn="";
		}
		if ((tgl.trim().length()==0)|| (bln.trim().length()==0) || (thn.trim().length()==0)){
			hsl="Tanggal Valid masih kosong, Silahkan masukkan tanggal valid kartu kredit";
		}else{
			f_validasi data = new f_validasi();
			cekk1= f_validasi.f_validasi_numerik(tgl);	
			cekk2= f_validasi.f_validasi_numerik(bln);
			cekk3= f_validasi.f_validasi_numerik(thn);		
			if ((cekk1==false) ||(cekk2==false) || (cekk3==false)  )
			{
				hsl="Silahkan masukkan tanggal dalam bentuk numerik";
			}else{
				tgl1=Integer.parseInt(tgl);
				bln1=Integer.parseInt(bln);
				thn1=Integer.parseInt(thn);
				f_cek_tanggal a= new f_cek_tanggal();
				b=a.cek(thn1,bln1,tgl1);	
				if (b==false)
				{
					hsl="Silahkan masukkan tanggal yang benar";
				}
			}

		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi isian no rekening
	 */
	public String no_rek(String no, String lca_id, int flag_account, String autodebet, Integer maxDigit, Integer minDigit)
	{
		String hsl = "";
		if(no==null) no = "";
		if(autodebet==null) autodebet = "0";
		
		if ((Integer.parseInt(autodebet)==1) || (Integer.parseInt(autodebet)==2) || (Integer.parseInt(autodebet)==5) || (flag_account==3))
		{
			//(muamalat) = 5	
			if(no.trim().length()==0){
				hsl="No Rekening masih kosong, silahkan masukkan nomor rekening dahulu.";
			}else{
				f_validasi id = new f_validasi();
				hsl = id.f_validasi_no_rek(no.trim());
				if(hsl.trim().length()==0){
					if ((Integer.parseInt(autodebet)==1) && no.trim().length() != 16){
						hsl = "Format digit kartu kredit sebanyak 16 digit.";
					}else if(Integer.parseInt(autodebet)==2){
						if(no.trim().length() != maxDigit && maxDigit==minDigit){
							hsl = "Format nomor rekening tabungan harus "+maxDigit+" digit.";
						}else if(no.trim().length() > maxDigit){
							hsl = "Format nomor rekening tabungan maksimal "+maxDigit+" digit.";
						}else if(no.trim().length() < minDigit){
							hsl = "Format nomor rekening tabungan minimal "+minDigit+" digit.";
						}
					}
				}
			}
		}
		return hsl;	
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian no rekening 1
	 */
	public String no_rek1(String no, String lca_id, int flag_account, String autodebet, Integer maxDigit, Integer minDigit)
	{
		String hsl = "";
		if(no==null) no= "";
		if(autodebet==null) autodebet = "0";
		
		if((flag_account==2) || (flag_account==3)){
			if (no.trim().length()==0){
				hsl = "No Rekening masih kosong, silahkan masukkan nomor rekening dahulu.";
			}else{
				f_validasi id = new f_validasi();
				hsl = id.f_validasi_no_rek(no.trim());
				if(hsl.trim().length()==0){
					if(Integer.parseInt(autodebet)==2){
						if(no.trim().length() != maxDigit && maxDigit==minDigit){
							hsl = "Format nomor rekening tabungan harus "+maxDigit+" digit.";
						}else if(no.trim().length() > maxDigit){
							hsl = "Format nomor rekening tabungan maksimal "+maxDigit+" digit.";
						}else if(no.trim().length() < minDigit){
							hsl = "Format nomor rekening tabungan minimal "+minDigit+" digit.";
						}
					}
				}
			}
		}
		return hsl;	
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian nama pemilik rekening autodebet
	 */
	public String cek_atas_nama(String atasnama,String lca_id,int flag_account,String autodebet)
	{
		String hsl="";
		if(atasnama==null)
		{
			atasnama="";	
		}
		if (autodebet==null)
		{
			autodebet="0";
		}
		if ((Integer.parseInt(autodebet)==1) || (Integer.parseInt(autodebet)==2) || Integer.parseInt(autodebet) == 5 || (flag_account==3))
		{
			if (atasnama.trim().length()==0)
			{
				hsl="Atas nama rekening bank masih kosong, Silahkan masukkan atas nama rekening bank terlebih dahulu.";
			}
		}
		return hsl;
	}
	
	public String flag_jn_tabungan(Integer flag, String nama_bank)
	{
		String hsl="";
		String nama_bank2 =nama_bank.substring(0,nama_bank.length());
		//String nama_bank3 =nama_bank.substring(0, 12);
		if(flag!=null){
			if (flag == 1)
			{
				if (nama_bank2.contains("BCA")||nama_bank2.contains("MANDIRI")||nama_bank2.contains("BRI")||nama_bank2.contains("BII"))
					hsl="Jenis Rekening TABUNGANKU untuk "+nama_bank+" Bank Tersebut tidak bisa digunakan untuk autodebet";
			}
		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi isian nama pemilik rekening withdrawal
	 */
	public String cek_atas_nama1(String atasnama,String lca_id,int flag_account,String autodebet)
	{
		String hsl="";
		if (atasnama==null)
		{
			atasnama="";
		}
		if (autodebet==null)
		{
			autodebet ="0";
		}
		if ((flag_account==2)|| (flag_account==3))
		{
			if (atasnama.trim().length()==0)
			{
				hsl="Atas nama rekening bank masih kosong, Silahkan masukkan atas nama rekening bank terlebih dahulu.";
			}
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian cabang bank autodebet
	 */	
	public String cek_cabang_bank(String cabang_bank,String lca_id,int flag_account,String autodebet)
	{
		String hsl="";
		if (cabang_bank==null)
		{
			cabang_bank="";
		}
		if (autodebet==null)
		{
			autodebet ="0";
		}
		if ( (flag_account==2)|| (flag_account==3))
		{
			if (cabang_bank.trim().length()==0)
			{
				hsl="Cabang bank masih kosong, Silahkan masukkan cabang bank terlebih dahulu.";
			}
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian kota bank autodebet
	 */		
	public String cek_kota_bank(String kota_bank,String lca_id,int flag_account,String autodebet)
	{
		String hsl="";
		if (kota_bank==null)
		{
			kota_bank="";
		}
		if (autodebet==null)
		{
			autodebet ="0";
		}
		if  ( (flag_account==2)|| (flag_account==3))
		{
			if (kota_bank.trim().length()==0)
			{
				hsl="Kota bank masih kosong, Silahkan masukkan kota bank terlebih dahulu.";
			}
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian nama bank autodebet
	 */		
	public String cek_bank(String bank,String lca_id,int flag_account, String autodebet)
	{
		String hsl="";
		if (bank==null)
		{
			bank="";
		}
		if (autodebet==null)
		{
			autodebet ="0";
		}
		if ((Integer.parseInt(autodebet)==1) || (Integer.parseInt(autodebet)==2) || Integer.parseInt(autodebet) == 5 || (flag_account==3)) 
		{
			//(muamalat) = 5
			if(bank==null)
			{
				bank="";
			}
			if (bank.trim().trim().equalsIgnoreCase(""))
			{
				hsl="Silahkan pilih salah satu nama bank terlebih dahulu.";
			}
		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi isian cabang bank withdrawal
	 */		
	public String cek_bank1(String bank,String lca_id,int flag_account, String autodebet)
	{
		String hsl="";
		if(bank==null)
		{
			bank="";
		}
		if (autodebet==null)
		{
			autodebet ="0";
		}

		if ((flag_account==2) || (flag_account==3)) 
		{
			if (bank.trim().trim().equalsIgnoreCase(""))
			{
				hsl="Silahkan pilih salah satu nama bank terlebih dahulu.";
			}
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian kombinasi premi pokok dan premi top up berkala
	 */	
	public String cek_kombinasi(String kombinasi)
	{
		String hsl="";
		if (kombinasi.trim().equalsIgnoreCase(""))
		{
			hsl="Silahkan pilih salah satu pilihan kombinasi premi pokok dan premi top up berkala terlebih dahulu.";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi pilihan rollover
	 */	
	public String cek_rolloover(String rolloover)
	{
		String hsl="";
		if (rolloover == null)
		{
			rolloover="";
		}
		if (rolloover.trim().equalsIgnoreCase(""))
		{
			hsl="Silahkan pilih salah satu pilihan Jenis Roll Over terlebih dahulu.";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian no memo kalau jenis bunga bukan normal
	 */	
	public String cek_memo(String memo)
	{
		String hsl="";
		if (memo==null)
		{
			memo="";
		}
		if (memo.trim().equalsIgnoreCase(""))
		{
			hsl="Data Investasi Belum Lengkap, Silahkan isi No memo terlebih dahulu.";
		}else{
			if (memo.trim().length()<=10)
			{
				hsl="Data Investasi Belum Lengkap (min 10 karakter), Silahkan isi No memo terlebih dahulu.";
			}
		}
		return hsl;
	}		
	
	/**
	 * @author HEMILDA
	 * validasi pilihan jangka waktu MGI
	 */	
	public String cek_jangkawaktu(String jangkawaktu)
	{
		String hsl="";
		if (jangkawaktu==null)
		{
			jangkawaktu="";
		}
		if (jangkawaktu.trim().equalsIgnoreCase(""))
		{
			hsl="Silahkan pilih salah satu pilihan Jangka Waktu terlebih dahulu.";
		}
		return hsl;
	}	
	
	public String debet(String autodebet, String tahun, String bulan, String tanggal,String lca_id,int flag_account)
	{
		String hsl="";
		/*if  ((flag_account==3) || (Integer.parseInt(autodebet)==1)|| (Integer.parseInt(autodebet)==2)) 
		{
			hsl=cek_tanggal(tahun,bulan,tanggal);
		}*/
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi kartu kredit
	 */	
	public String valid(String autodebet, String tahun, String bulan, String tanggal,String lca_id,int flag_account)
	{
		String hsl="";
		/*if  ((Integer.parseInt(autodebet)==1) || (flag_account==3)) 
		{
			hsl=cek_tanggal(tahun,bulan,tanggal);
		}*/
		if (Integer.parseInt(autodebet)==1 || Integer.parseInt(autodebet)==5) //(muamalat)
		{
			hsl=cek_tanggal(tahun,bulan,tanggal);
		}
		return hsl;
	}		

	/**
	 * @author HEMILDA
	 * validasi cek tanggal 1
	 */	
	public String cek_tanggal1(String thn, String bln, String tgl)
	{
		boolean b=true;
		boolean cekk1=true;
		boolean cekk2=true;
		boolean cekk3=true;
		int tgl1=0;
		int bln1=0;
		int thn1=0;
		String hsl="";
		if (tgl==null)
		{
			tgl="";
		}
		if (bln==null)
		{
			bln="";
		}
		if (thn==null)
		{
			thn="";
		}
		if ((tgl.trim().length()==0)|| (bln.trim().length()==0) || (thn.trim().length()==0)){
			hsl="";
		}else{
			f_validasi data = new f_validasi();
			cekk1= f_validasi.f_validasi_numerik(tgl);	
			cekk2= f_validasi.f_validasi_numerik(bln);
			cekk3= f_validasi.f_validasi_numerik(thn);		
			if ((cekk1==false) ||(cekk2==false) || (cekk3==false)  )
			{
				hsl="Silahkan masukkan tanggal dalam bentuk numerik";
			}else{
				tgl1=Integer.parseInt(tgl);
				bln1=Integer.parseInt(bln);
				thn1=Integer.parseInt(thn);
			}
			if (hsl.trim().length()==0)
			{
				f_cek_tanggal a= new f_cek_tanggal();
				b=a.cek(thn1,bln1,tgl1);	
				if (b==false)
				{
					hsl="Silahkan masukkan tanggal yang benar";
				}
			}
		}

		return hsl;
	}	

	/**
	 * @author HEMILDA
	 * validasi pilihan jenis tabungan
	 */	
	public String jenis_tab(String jenis_tab,String lca_id,int flag_account)
	{
		if (jenis_tab==null)
		{
			jenis_tab="0";
		}
		String hsl="";
		if (  (flag_account==2)||(flag_account==3) )
		{
			if (Integer.parseInt(jenis_tab)==0)
			{
				hsl = "Silahkan pilih salah satu pilihan jenis tabungan terlebih dahulu.";
			}
				
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian bunga simponi
	 */	
	public String cek_bunga_simponi(String bunga, boolean flag)
	{
		boolean cekk;
		String hsl="";
		if (bunga==null)
		{
			bunga="";
		}
		if (flag==true)
		{
			if (bunga.trim().length()!=0)
			{
				f_validasi data = new f_validasi();
				cekk= f_validasi.f_validasi_numerik(bunga);	
				if (cekk==false)
				{
					hsl="Silahkan masukkan Bunga Simponi dalam bentuk numerik";
				}
			}else{
				hsl="Silahkan masukkan terlebih dahulu Bunga Simponi";
			}
		}
		return hsl;		
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian bonus tahapan
	 */	
	public String cek_bonus_tahapan(String bonus,boolean flag)
	{
		boolean cekk;
		String hsl="";
		if (bonus==null)
		{
			bonus = "";
		}
		if (flag==true)
		{
			if (bonus.trim().length()!=0)
			{
				/*f_validasi data = new f_validasi();
				cekk= data.f_validasi_numerik(bonus);	
				if (cekk==false)
				{
					hsl="Silahkan masukkan Bonus Tahapan dalam bentuk numerik";
				}else{*/
					if ((Double.parseDouble(bonus))>12)
					{
						hsl="Bonus Tahapan maximum 12% !!!";
					}
			//	}
			}else{
				hsl="Silahkan masukkan terlebih dahulu Bonus Tahapan";
			}
		}
		return hsl;		
	}	
	
	
	
	public static void main(String[] args) {
	}
}
