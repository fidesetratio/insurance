package com.ekalife.utils;

import java.io.Serializable;

/*
 * Created on Aug 12, 2005
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
public class f_validasi implements Serializable {

	private static final long serialVersionUID = 1L;

	public static boolean f_validasi_numerik(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789";
		boolean cek = true;
		
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek=false;
			}
		}
		return cek;
	}
	public static boolean f_validasi_numerik1(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789.";
		boolean cek = true;
		
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek=false;
			}
		}
		return cek;
	}
	
	public static boolean f_validasi_karakter(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789";
		
		
		for (int i=0 ; i <ceking.length(); i++)
		{
			dtl=ceking.charAt(i);
			if ( detil.indexOf(dtl)>=0)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean f_validasi_numerik_titikcoma(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789,.";
		boolean cek = true;
		
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);

			if ( ceking.indexOf(dtl)==-1)
			{
				cek=false;
			}
		}
		return cek;
	}	
	
	public boolean f_validasi_numerik_titik(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789.";
		boolean cek = true;
		
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek=false;
			}
		}
		return cek;
	}		
	
	public boolean f_validasi_email(String detil)
	{
		char dtl;
		int jmlh=0;
		int jmlh1=0;
		int jmlh2=0;
		detil=detil.trim();
		String ceking1="@";
		String ceking="0123456789@.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_- ";
		boolean cek = true;
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			
			if (ceking.indexOf(dtl)==-1)
			{
				jmlh=jmlh+1;
			}
		
			if (ceking1.indexOf(dtl)!=-1)
			{
				jmlh1=jmlh1+1;
			}	
		}
		if (ceking1.indexOf(detil.charAt(0))!=-1)
		{
			jmlh2=jmlh2+1;
		}

		if ((jmlh1!=1) || (jmlh>0) || (jmlh2==1))
		{
			cek=false;
		}else{
			cek=f_validasi_emailNew(detil);
		}
		return cek;
	}	
	public boolean f_validasi_emailNew(String email){
		boolean flag;
		int akeong;
		int titik;
		flag=true;
		akeong=email.indexOf("@");
		titik =email.indexOf(".");
		if(akeong==-1 || akeong==0 || akeong==email.length()-1)
			flag=false;
		if(titik==-1 || titik==0 || titik==email.length()-1)
			flag=false;
//		if(akeong>titik)
//			flag=false;
		if(akeong==titik-1 || akeong==titik+1)
			flag=false;
		//
		if(flag==false) {
			return false;
		}else
			return true;
	}
	public boolean f_validasi_kurs(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789.";
		boolean cek = true;
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek=false;
			}
		}
		return cek;
	}	

	public String f_validasi_kode_area_tgh(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789-";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="No kode area penagihan harus dalam bentuk numerik ";
			}
		}
		return cek;
	}
	
	public String f_validasi_kode_area_rmh(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789-";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="No kode area rumah harus dalam bentuk numerik ";
			}
		}
		return cek;
	}
	
	public String f_validasi_area_fax(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="No kode area fax harus dalam bentuk numerik ";
			}
		}
		return cek;
	}	
	
	public String f_validasi_fax(String detil)
	{
		char dtl;
		char dtl1;
		detil=detil.trim();
		String ceking="0123456789";
		String ceking1="0";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="No fax harus dalam bentuk numerik";
			}
			dtl1=detil.charAt(0);
			if (ceking1.indexOf(dtl1)!=-1)
			{
				cek="Kode area fax harus diisi di kotak isian kode area fax, dan isikan no fax di kotak isian no fax.";
			}
		}
		return cek;
	}		

	public String f_validasi_no_rek(String detil)
	{
		char dtl;
		detil = detil.trim();
		String ceking = "0123456789.";
		String cek = "";
		for(int i=0; i<detil.length(); i++){
			dtl = detil.charAt(i);
			if(ceking.indexOf(dtl)==-1){
				cek = "No rekening harus dalam bentuk format angka 0 - 9 dan . saja ";
			}
		}
		return cek;
	}
	
	public String f_validasi_kode_area_ktr(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="No kode area kantor harus dalam bentuk numerik ";
			}
		}
		return cek;
	}
	
	public String f_validasi_telp_ktr(String detil)
	{
		char dtl;
		char dtl1;
		detil=detil.trim();
		String ceking="0123456789#";
		String ceking1="0#";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="No telpon kantor harus dalam bentuk numerik # extention";
			}
			dtl1=detil.charAt(0);
			if (ceking1.indexOf(dtl1)!=-1)
			{
				cek="Kode Area telpon kantor harus diisi di kotak isian kode area telpon kantor, dan isikan no telpon kantor  # ext di kotak isian no telpon kantor.";
			}
		}
		return cek;
	}	

	public String f_validasi_telp_rmh(String detil)
	{
		char dtl;
		char dtl1;
		detil=detil.trim();
		String ceking="0123456789-";
		String ceking1="0";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="No telpon rumah harus dalam bentuk numerik atau - jika tidak ada";
			}
			dtl1=detil.charAt(0);
			if (ceking1.indexOf(dtl1)!=-1)
			{
				cek="Kode area telpon rumah harus diisi di kotak isian kode area telpon rumah, dan isikan no telpon rumah di kotak isian no telpon rumah.";
			}
		}
		return cek;
	}	

	public String f_validasi_telp_tgh(String detil)
	{
		char dtl;
		char dtl1;
		detil=detil.trim();
		String ceking="0123456789-";
		String ceking1="0";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="No telpon penagihan harus dalam bentuk numerik";
			}
			dtl1=detil.charAt(0);
			if (ceking1.indexOf(dtl1)!=-1)
			{
				cek="Kode area telpon penagihan harus diisi di kotak isian kode area telpon penagihan, dan isikan no telpon penagihan di kotak isian no telpon penagihan.";
			}
		}
		return cek;
	}		

	public String f_validasi_nomor_seri(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="Nomor Seri harus dalam bentuk numerik ";
			}
		}
		return cek;
	}	
	
	public static String f_validasi_nomor_blanko(String detil)
	{
		char dtl;
		detil=detil.trim();
		String ceking="0123456789;-";
		String cek = "";
		for (int i=0 ; i <detil.length(); i++)
		{
			dtl=detil.charAt(i);
			if ( ceking.indexOf(dtl)==-1)
			{
				cek="(cth: 000020-000025;000030-000035)";
			}
		}
		return cek;
	}	
	
	public static String convert_karakter(String detil){
		if(detil == null) return "";
		return detil.trim().replaceAll("'", "`").replaceAll("&", " dan ");
	}
	
	public static String cek_nohp(String no_hp , boolean use_default) {
		String hsl="";
		if(use_default && "0000".equals(no_hp)){
			//kalau use_default = true dan no hp 0000 -> dilewatkan
		}else{
			String no_hp1=no_hp.substring(0, 2);
			if(no_hp1.equals("00")){
				hsl=hsl+"karakter ke 2 dari no hp tidak boleh diisi dengan angka 0.";
			}
			//minimal no hp esia bisa masuk yg hanya punya jumlah karakter sedikit
			if(no_hp.trim().length() < 7)hsl=hsl+"jumlah karakter no hp minimum harus 7.";
		}
		return hsl;
	}
	
	public static String gantiKata(String kata){
		
		if(kata==null || kata.equals("") || kata.equals(" ")){
			kata = "-";
		}
		
		return kata;
	}
	
	public static void main(String[] args) {
	}
	
	
}
