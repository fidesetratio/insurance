package com.ekalife.elions.web.bac.support;

import com.ekalife.utils.Products;
import com.ekalife.utils.f_cek_tanggal;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_validasi;

/*
 * Created on Aug 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author HEMILDA
 *
 * validasi form data usulan dalam penginputan bac
 */
public class form_data_usulan {

	private Products products;
	
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	/**
	 * @author HEMILDA
	 * validasi produk belum dipilih
	 */
	public String bisnis(String bisnis)
	{
		String hsl="";
		if (bisnis==null)
		{
			bisnis="0~X0";
		}
		if (bisnis.trim().equalsIgnoreCase("0~X0"))
		{
			hsl="Produk Utama belum dipilih, Silahkan pilih produk utama terlebih dahulu";
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi up kalau kosong
	 */
	public String cek_up(String up)
	{
		boolean cekk;
		String hsl="";
		if (up==null)
		{
			up="";
		}
		if (up.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= data.f_validasi_numerik_titikcoma(up);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan up dalam bentuk numerik";
			}
		}
		return hsl;	
	}
	
	/**
	 * @author HEMILDA
	 * validasi premi kalau kosong
	 */
	public String cek_premi(String premi)
	{
		boolean cekk;
		String hsl="";
		if (premi==null)
		{
			premi="";
		}
		if (premi.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= data.f_validasi_numerik_titikcoma(premi);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan premi dalam bentuk numerik";
			}
		}
		return hsl;	
	}	
	
	/**
	 * @author HEMILDA
	 * validasi up kalau kosong
	 */
	public double hsl_up(String up)
	{
		double uang_pertanggungan=0;
		String hsl="";
		if (up==null)
		{
			up="";
		}
		form_data_usulan data = new form_data_usulan();
		hsl=data.cek_up(up);
		if (hsl.trim().length()==0)
		{
			uang_pertanggungan=Double.parseDouble(up);
		}
		return uang_pertanggungan;
	}
	
	/**
	 * @author HEMILDA
	 * validasi premi kalau kosong
	 */
	public double hsl_premi(String premi)
	{
		double uang_premi=0;
		String hsl="";
		if (premi==null)
		{
			premi="";
		}
		form_data_usulan data = new form_data_usulan();
		hsl=data.cek_up(premi);
		if (hsl.trim().length()==0)
		{
			uang_premi=Double.parseDouble(premi);
		}
		return uang_premi;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi up premi kalau kosong
	 */
	public String up_premi(String up1, String premi1)
	{
		boolean cekk;
		String hsl="";
		if (up1==null)
		{
			up1="";
		}
		if (premi1==null)
		{
			premi1="";
		}
		form_data_usulan data = new form_data_usulan();
		double up2=data.hsl_up(up1);
		double premi2=data.hsl_premi(premi1);
		if (up2==0 && premi2==0)
		{
			hsl="Silahkan masukkan up atau premi";
		}else{
			String hasil1= data.cek_up(up1);
			String hasil2= data.cek_premi(premi1);
			if (hasil1.trim().length()==0 && hasil2.trim().length()==0)
			{
				hsl=hasil1;
			}else{
				if (hasil1.trim().length()!=0){
					hsl=hasil1;
				}else{
					if (hasil2.trim().length()!=0){
						hsl=hasil2;
					}				
				}
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi tanggal
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
			hsl="Tanggal masih kosong, Silahkan masukkan tanggal";
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
	 * validasi tanggal awal berlaku produk endowment kalau kosong
	 */
	public String cek_tanggal_endow(String thn, String bln, String tgl, Integer kode)
	{
			f_hit_umur n_date=  new f_hit_umur();
			boolean b=true;
			boolean cekk1=true;
			boolean cekk2=true;
			boolean cekk3=true;
			int tgl1=0;
			int bln1=0;
			int thn1=0;
			String hsl="";
			if (kode.intValue()==157)
			{
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
				hsl="Tanggal mulai berlaku pertanggungan masih kosong, Silahkan masukkan tanggal mulai berlaku pertanggungan";
			}else{
				f_validasi data = new f_validasi();
				cekk1= f_validasi.f_validasi_numerik(tgl);	
				cekk2= f_validasi.f_validasi_numerik(bln);
				cekk3= f_validasi.f_validasi_numerik(thn);		
				if ((cekk1==false) ||(cekk2==false) || (cekk3==false)  )
				{
					hsl="Silahkan masukkan tanggal mulai berlaku pertanggungan dalam bentuk numerik";
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
						hsl="Silahkan masukkan Tanggal mulai berlaku pertanggungan yang benar";
					}
				}
			}
			/*if (hsl.trim().length()==0)
			{
				Integer jumlah_hari1 =new Integer( n_date.hari1(Integer.parseInt(thn),Integer.parseInt(bln),Integer.parseInt(tgl),2006,7,10));
				Integer jumlah_hari2 =new Integer( n_date.hari1(Integer.parseInt(thn),Integer.parseInt(bln),Integer.parseInt(tgl),2006,9,9));
				if (jumlah_hari1.intValue() > 0 || jumlah_hari2.intValue() < 0)
				{
					hsl = "Untuk Produk ini hanya bisa diinput pada masa pertanggungan di antara tanggal 10 Juli 2006 - 10 September 2006 saja.";
				}
			}*/
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi tanggal1
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
		if(bln==null)
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
	 * validasi unit rider kosong
	 */
	public String unit_rider(String unit)
	{
		String hsl="";
		boolean cekk=true;
		if (unit==null)
		{
			unit="";
		}
		if (unit.trim().length()==0)
		{
			hsl="Unit rider masih kosong, Silahkan masukkan unit rider terlebih dahulu";
		}else{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(unit);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan unit rider dalam bentuk numerik";
			}
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi klas rider kalau kosong
	 */
	public String klas_rider(String klas)
	{
		String hsl="";
		boolean cekk=true;
		if (klas==null)
		{
			klas="";
		}
		if (klas.trim().length()==0)
		{
			hsl="Klas rider masih kosong, Silahkan masukkan klas rider terlebih dahulu";
		}else{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(klas);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan klas rider dalam bentuk numerik";
			}
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi detil plan kalau belum dipilih
	 */
	public String bisnis_rider(String rider,String unit,String klas)
	{
		String hsl="";
		String hsl1="";
		String hsl2="";
		if (rider==null)
		{
			rider="0~X0";
		}
		if(unit==null)
		{
			unit="";
		}
		if (klas==null)
		{
			klas="";
		}
		if (!rider.trim().equalsIgnoreCase("0~X0"))
		{
			form_data_usulan data = new form_data_usulan();
			hsl1=data.unit_rider(unit);
			hsl2=data.klas_rider(klas);
			hsl=hsl1 + " "+hsl2;
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi klas
	 */
	public String cek_klas(String klas)
	{
		boolean cekk;
		String hsl="";
		if (klas==null)
		{
			klas="";
		}
		if (klas.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(klas);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Class dalam bentuk numerik";
			}
		}
		return hsl;	
	}	

	/**
	 * @author HEMILDA
	 * validasi cek unit
	 */
	public String cek_unit(String unit)
	{
		boolean cekk;
		String hsl="";
		if (unit==null)
		{
			unit="";
		}
		if (unit.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(unit);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Unit dalam bentuk numerik";
			}
		}
		return hsl;	
	}

	/**
	 * @author HEMILDA
	 * validasi autodebet
	 * untuk bulanan wajib kartu kredit atau tabungan
	 * untuk sekaligus tidak bisa autodebet
	 */
	public String autodebet(int lsbs_id, int lsdbs_number, String autodebet,String lca_id,int flag_account,int pmode_id, int bao,int flag_excell80plus)
	{
		String hsl="";
		if (autodebet==null)
		{
			autodebet="0";
		}

		if (bao==1)
		{
			if (Integer.parseInt(autodebet)==0 && pmode_id!=0 && pmode_id!=6)
			{
				hsl = "Silahkan pilih salah satu pilihan autodebet terlebih dahulu.";
			}
		}
		
		if (flag_account==2 && pmode_id==0 && flag_excell80plus==1)
		{
			if (Integer.parseInt(autodebet)==1 )
			{
				hsl="Untuk plan ini dengan cara bayar sekaligus tidak bisa autodebet dengan Kartu Kredit.";
			}
		}

		//muamalat - mabrur
		if(products.muamalat(lsbs_id, lsdbs_number) && Integer.parseInt(autodebet) != 5) {
			hsl = "Produk Bank Muamalat Harus Pilih Cara Bayar : DEBET MUAMALAT";
		}
		
		if (!hsl.equals("") && flag_account==2 && pmode_id==6 && flag_excell80plus==1)
		{
			if (Integer.parseInt(autodebet)!=1 && Integer.parseInt(autodebet)!=2 && Integer.parseInt(autodebet)!=0)
			{
				hsl="Untuk plan ini harus autodebet dengan Kartu Kredit atau Tabungan atau Tunai.";
			}
		}
		if(lsbs_id==164 && Integer.parseInt(autodebet)!=0 ){
			hsl="Untuk plan STABLE LINK, Harus Pilih Cara bayar : TUNAI.";
		}
		
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi pilihan dplk untuk horison
	 */
	public String cek_persentase_dplk (double persen)
	{
		String hsl="";
		if (persen == 0)
		{
			hsl="Silahkan memilih terlebih dahulu persentase DPLK.";
		}
		return hsl;
	}
	
	public static void main(String[] args) {
		
	}
}
