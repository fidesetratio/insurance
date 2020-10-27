package com.ekalife.elions.web.bac.support;
import java.util.Date;

import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_cek_tanggal;
import com.ekalife.utils.f_validasi;

/**
 * @author HEMILDA
 *
 * validasi form detil agen dalam penginputan bac
 */
public class form_agen {

	/**
	 * @author HEMILDA
	 * validasi nik karyawan
	 */
	public String nik_karyawan(String nik)
	{
		boolean cekk;
		String hsl="";
		if (nik==null)
		{
			nik="";
		}
		f_validasi id = new f_validasi();
		cekk = f_validasi.f_validasi_numerik(nik.trim());
		if (cekk==false)
		{
//			hsl="Silahkan masukkan Nik Karyawan dalam bentuk numerik";
		}
		return hsl;	
	}	
	
	/**
	 * @author HEMILDA
	 * validasi kode agen kalau kosong
	 */
	public String kode_agen(String kode)
	{
		boolean cekk;
		String hsl="";
		if (kode==null)
		{
			kode="";
			hsl="Silahkan masukkan terlebih dahulu Kode Agen Penutup";
		}else{
			if (kode.trim().length()!=0)
			{
				f_validasi data = new f_validasi();
				cekk= f_validasi.f_validasi_numerik(kode);	
				if (cekk==false)
				{
					hsl="Silahkan masukkan Kode Agen Penutup dalam bentuk numerik";
				}
			}else{
				hsl="Silahkan masukkan terlebih dahulu Kode Agen Penutup";
			}
		}
		return hsl;		
	}
	
	/**
	 * @author Deddy
	 * validasi kode agen leader kalau kosong
	 */
	public String kode_agen_leader(String kode)
	{
		boolean cekk;
		String hsl="";
		if (kode==null)
		{
			kode="";
			hsl="Silahkan masukkan terlebih dahulu Kode Leader dari Agen Penutup Tersebut";
		}else{
			if (kode.trim().length()!=0)
			{
				f_validasi data = new f_validasi();
				cekk= f_validasi.f_validasi_numerik(kode);	
				if (cekk==false)
				{
					hsl="Silahkan masukkan Kode Leader dari Agen Penutup dalam bentuk numerik";
				}
			}else{
				hsl="Silahkan masukkan terlebih dahulu Kode Leader dari Agen Penutup Tersebut";
			}
		}
		return hsl;		
	}
	
	/**
	 * @author HEMILDA
	 * validasi sertifikasi agen
	 */
	public String sertifikasi_agen(String kode, int ulink , int sertifikat, Date tanggal_sertifikat,Date tanggal_beg_date)
	{
		String hsl="";
		int jml_thn = 0;
		int jml_hari = 0;
		int jml_bln = 0;
		Date tanggal = new Date();	
		int tgl_skrg = tanggal_beg_date.getDate();
		int bln_skrg = tanggal_beg_date.getMonth() + 1;
		int thn_skrg = tanggal_beg_date.getYear()+1900;
		int tgl_sertifikat=0;
		int bln_sertifikat=0;
		int thn_sertifikat=0;
		if (kode==null)
		{
			kode="";
		}
		if (tanggal_sertifikat!=null)
		{
			tgl_sertifikat = tanggal_sertifikat.getDate();
			bln_sertifikat = tanggal_sertifikat.getMonth() + 1;
			thn_sertifikat = tanggal_sertifikat.getYear() + 1900;
		}
		
	if ( (!(kode.equalsIgnoreCase("500116"))) && (!(kode.equalsIgnoreCase("016189"))) && (!(kode.equalsIgnoreCase("015586")))&& (!(kode.equalsIgnoreCase("003725"))) && (!(kode.equalsIgnoreCase("000000")))  && (!(kode.equalsIgnoreCase("016374")))  && (!(kode.equalsIgnoreCase("016457"))) )
	{
		if (sertifikat==0)
		{
			hsl="Kode Agent : " + kode + " Belum Certified !!! ";
		}else{
			//if ((sertifikat != 2))
			//{
				//if ((sertifikat == 3) || (sertifikat == 1)|| (sertifikat == 4))
				//{
					jml_thn=(thn_sertifikat) - (thn_skrg);
					if (jml_thn <0)
					{
						hsl="Agent tersebut sertifikasi telah expired, tidak dapat melakukan penginputan";
					}else{
						jml_bln=(bln_sertifikat) - (bln_skrg);
						if ((jml_thn ==0) && (jml_bln<0))
						{
							hsl="Agent tersebut sertifikasi telah expired, tidak dapat melakukan penginputan";
						}else{
							jml_hari=(tgl_sertifikat) - (tgl_skrg);
							if ((jml_thn ==0) && (jml_bln == 0) && (jml_hari<0))
							{
								hsl="Agent tersebut sertifikasi telah expired, tidak dapat melakukan penginputan";
							}						
						}
					}
				//}
			//}
		}
	}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi agen untuk produk karyawan
	 */
	public String agen_link_karyawan(String kode_agen )
	{
		String hsl="";
		if (kode_agen==null)
		{
			kode_agen="";
		}
		if (!(kode_agen.equalsIgnoreCase("003725")))
		{
			hsl="Plan ini, agen penutupnya harus Kantor Pusat"	;
		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi agen agency system
	 */
	public String agen_as(String kode_regional)
	{
		String hsl="";
		if (kode_regional==null)
		{
			kode_regional="";
		}
		if("37,46,52,60,67".indexOf(kode_regional.substring(0,2)) ==-1){
//		if ((!(kode_regional.substring(0,2).equalsIgnoreCase("37"))) && (!(kode_regional.substring(0,2).equalsIgnoreCase("46"))) && (!(kode_regional.substring(0,2).equalsIgnoreCase("60"))))
//		{
			hsl="Plan ini, agen penutupnya harus agen agency system/arthamas/bridge."	;
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi agen artha
	 */
	public String agen_artha(String kode_regional)
	{
		String hsl="";
		if (kode_regional==null)
		{
			kode_regional="";
		}
		if (!(kode_regional.substring(0,2).equalsIgnoreCase("46")))
		{
			hsl="Plan ini, agen penutupnya harus agen arthamas ."	;
		}
		return hsl;
	}
	
	/**
	 * @author Deddy
	 * validasi agen agency system & worksite
	 */
	public String agen_worksite_as(String kode_regional)
	{
		String hsl = "";
		if(kode_regional==null){
			kode_regional="";
		}
		if("08,37,42,46,52,67".indexOf(kode_regional.substring(0,2))== -1)
//		if( ((!(kode_regional.substring(0,2).equalsIgnoreCase("42"))) && (!(kode_regional.substring(0,2).equalsIgnoreCase("08")))) && ((!(kode_regional.substring(0,2).equalsIgnoreCase("37"))) && (!(kode_regional.substring(0,2).equalsIgnoreCase("46")))) )
			hsl="Plan ini, agen penutupnya harus agen corporate marketing atau Agency System."	;
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi agen worksite
	 */
	public String agen_worksite(String kode_regional)
	{
		String hsl="";
		if (kode_regional==null)
		{
			kode_regional="";
		}
		if ((!(kode_regional.substring(0,2).equalsIgnoreCase("42"))) && (!(kode_regional.substring(0,2).equalsIgnoreCase("08"))) && (!(kode_regional.substring(0,2).equalsIgnoreCase("58"))) && (!(kode_regional.substring(0,2).equalsIgnoreCase("62") )) && (!(kode_regional.substring(0,2).equalsIgnoreCase("67") )))
		{
			hsl="Plan ini, agen penutupnya harus agen corporate marketing."	;
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi agen regional
	 */
	public String agen_rg(String kode_regional)
	{
		String hsl="";
		if (kode_regional==null){
			kode_regional="";
		}
		
		String lca = kode_regional.substring(0,2);
		
		if("08, 09, 37, 42, 46, 52".contains(lca)){
			hsl="Plan ini, agen penutupnya harus agen Regional System."	;
		}
		
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi agen regional agency system
	 */
	public String agen_rg_as(String kode_regional)
	{
		String hsl="";
		if (kode_regional==null)
		{
			kode_regional="";
		}
		if ((FormatString.rpad("0",(kode_regional.substring(0,2)),2).equalsIgnoreCase("09")) || ((kode_regional.substring(0,2).equalsIgnoreCase("42"))))
		{
			hsl="Plan ini, agen penutupnya harus agen Regional System atau Agency System."	;
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi agen bancassurance
	 */
	public String agen_bao(String kode_regional)
	{
		String hsl="";
		if (kode_regional==null)
		{
			kode_regional="";
		}
		if (!(FormatString.rpad("0",(kode_regional.substring(0,2)),2).equalsIgnoreCase("09")))
		{
			// produk bancass, bisa dijual oleh kantor pusat
			//hsl="Plan ini, agen penutupnya harus agen Bancassurance."	;
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi agen permata bank
	 */
	public String agen_permata(String kode_regional)
	{
		String hsl="";
		if (kode_regional==null)
		{
			kode_regional="";
		}
		/*if (!(FormatString.rpad("0",(kode_regional.substring(0,4)),4).equalsIgnoreCase("0926")))
		{
			hsl="Plan ini, agen penutupnya harus agen Permata Bank."	;
		}*/
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi reff agen
	 */
	public String agen_reff_bii(String kode_regional,int flag)
	{
		String hsl="";
		if (kode_regional==null)
		{
			kode_regional="";
		}
		kode_regional = FormatString.rpad("0",(kode_regional.substring(0,4)),4);
	   if (flag==1)
	   {
		   if ( kode_regional.equalsIgnoreCase("0914") || kode_regional.equalsIgnoreCase("0915") || kode_regional.equalsIgnoreCase("0917") || kode_regional.equalsIgnoreCase("0919") || kode_regional.equalsIgnoreCase("0920") || kode_regional.equalsIgnoreCase("0921"))
		   {
			   
		   }else{
				hsl="Plan ini, agen penutupnya harus agen Bancassurance hanya untuk Region FA / CIC / Maxi-Inv"	;
		   }
	   }
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi tgl
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
		
	if ((tgl==null)|| (bln==null) || (thn==null)){
		hsl="Tanggal masih kosong, Silahkan masukkan tanggal";
	}else{
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
	}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi nama agen baru
	 */
	public String nama_penutup(String nama_penutup)
	{
		String hsl="";
		if (nama_penutup==null)
		{
			nama_penutup="";
			hsl="Nama Penutup masih kosong, silahkan masukkan nama penutup terlebih dahulu";
		}else{
		
			if (nama_penutup.trim().length()==0)
			{
				hsl="Nama Penutup masih kosong, silahkan masukkan nama penutup terlebih dahulu";
			}
		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi kode ao
	 */
	public String kode_ao(String kode)
	{
		String hsl="";
		if (kode == null)
		{
			kode="";
			hsl="Kode AO masih kosong, silahkan masukkan kode AO terlebih dahulu";

		}else{
			if (kode.trim().length()==0)
			{
				hsl="Kode AO masih kosong, silahkan masukkan kode AO terlebih dahulu";
			}
		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi autodebet
	 */
	public String autodebet(String autodebet,String lca_id,int flag_account)
	{
		String hsl="";
		if (autodebet==null)
		{
			autodebet="0";
		}
		if ((FormatString.rpad("0",lca_id,2).equalsIgnoreCase("09"))  )
		{
			if (Integer.parseInt(autodebet)==0)
			{
				hsl = "Silahkan pilih salah satu pilihan autodebet terlebih dahulu di halaman data usulan.";
			}
				
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi tgl debet
	 */
	public String debet(String autodebet, String tahun, String bulan, String tanggal,String lca_id,int flag_account)
	{
		String hsl="";
		if (autodebet==null)
		{
			autodebet="0";
		}
		if (tanggal==null)
		{
			tanggal="";
		}
		if (bulan==null)
		{
			bulan="";
		}
		if (tahun==null)
		{
			tahun="";
		}
		
		if ((FormatString.rpad("0",lca_id,2).equalsIgnoreCase("09")) )
		{
			hsl=cek_tanggal(tahun,bulan,tanggal);
		}else{
			hsl=cek_tanggal1(tahun,bulan,tanggal);
		}
		if (hsl.trim().length()!=0)
		{
			hsl=hsl+" untuk tanggal debet di halaman investasi.";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi tgl valid kartu kredit
	 */
	public String valid(String autodebet, String tahun, String bulan, String tanggal,String lca_id,int flag_account)
	{
		String hsl="";
		if (autodebet==null)
		{
			autodebet="0";
		}
		if (tanggal==null)
		{
			tanggal="";
		}
		if (bulan==null)
		{
			bulan="";
		}
		if (tahun==null)
		{
			tahun="";
		}
		
		if ((FormatString.rpad("0",lca_id,2).equalsIgnoreCase("09")) && autodebet.equalsIgnoreCase("1") )
		{
			hsl=cek_tanggal(tahun,bulan,tanggal);
		}else{
			hsl=cek_tanggal1(tahun,bulan,tanggal);
		}
		if (hsl.trim().length()!=0)
		{
			hsl=hsl+" untuk tanggal valid di halaman investasi.";
		}
		return hsl;
	}		

	/**
	 * @author HEMILDA
	 * validasi tgl1
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
	 * validasi bank kalau kosong
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
			autodebet="0";
		}
		if ((FormatString.rpad("0",lca_id,2).equalsIgnoreCase("09"))  )
		{
			if (bank==null)
			{
				hsl="Silahkan pilih salah satu nama bank terlebih dahulu di halaman investasi.";
			
			}else{
				if (bank.trim().trim().equalsIgnoreCase(""))
				{
					hsl="Silahkan pilih salah satu nama bank terlebih dahulu di halaman investasi.";
				}
			}
		}
		return hsl;
	}
	

	/**
	 * @author HEMILDA
	 * validasi kota bank kalau kosong
	 */
	public String cek_kota_bank(String kota_bank,String lca_id,int flag_account,String autodebet)
	{
		String hsl="";
		if (autodebet==null)
		{
			autodebet="0";
		}
		if (kota_bank==null)
		{
			kota_bank="";
		}
		if ((FormatString.rpad("0",lca_id,2).equalsIgnoreCase("09"))  )
		{
			if (kota_bank ==null)
			{
				hsl="Kota bank masih kosong, Silahkan masukkan kota bank terlebih dahulu di halaman investasi.";

			}else{
				if (kota_bank.trim().length()==0)
				{
					hsl="Kota bank masih kosong, Silahkan masukkan kota bank terlebih dahulu di halaman investasi.";
				}
			}
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi cabang bank kalau kosong
	 */
	public String cek_cabang_bank(String cabang_bank,String lca_id,int flag_account,String autodebet)
	{
		String hsl="";
		if (autodebet==null)
		{
			autodebet="0";
		}
		if (cabang_bank==null)
		{
			cabang_bank="";
		}
		
		if ((FormatString.rpad("0",lca_id,2).equalsIgnoreCase("09")) )
		{
			if (cabang_bank == null)
			{
				hsl="Cabang bank masih kosong, Silahkan masukkan cabang bank terlebih dahulu di halaman investasi.";
			}else{
					
				if (cabang_bank.trim().length()==0)
				{
					hsl="Cabang bank masih kosong, Silahkan masukkan cabang bank terlebih dahulu di halaman investasi.";
				}
			}
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi no rek autodebet kalau kosong
	 */
	public String no_rek(String no,String lca_id,int flag_account,String autodebet)
	{
		boolean cekk;
		String hsl="";
		if (autodebet==null)
		{
			autodebet="0";
		}
		if (no==null)
		{
			no="";
		}
		if ((FormatString.rpad("0",lca_id,2).equalsIgnoreCase("09")))
		{
			if (no==null)
			{
				hsl="No Rekening masih kosong , Silahkan masukkan nomor rekening dahulu di halaman investasi.";
			}else{
				if (no.trim().length()==0)
				{
					hsl="No Rekening masih kosong , Silahkan masukkan nomor rekening dahulu di halaman investasi.";
				}else{
					f_validasi id = new f_validasi();
					cekk = id.f_validasi_numerik_titik(no.trim());
					if (cekk==false)
					{
						hsl="Silahkan masukkan No Rekening dalam bentuk numerik";
					}
				}
			}
		}
		return hsl;	
	}
	
	/**
	 * @author HEMILDA
	 * validasi nama pemegang account autodebet kalau kosong
	 */
	public String cek_atas_nama(String atasnama,String lca_id,int flag_account,String autodebet)
	{
		String hsl="";
		if (autodebet==null)
		{
			autodebet="0";
		}
		if (atasnama==null)
		{
			atasnama="";
		}
		if ((FormatString.rpad("0",lca_id,2).equalsIgnoreCase("09"))  )
		{
			if (atasnama ==null)
			{
				hsl="Atas nama rekening bank masih kosong, Silahkan masukkan atas nama rekening bank terlebih dahulu di halaman investasi.";
			}else{
				if (atasnama.trim().length()==0)
				{
					hsl="Atas nama rekening bank masih kosong, Silahkan masukkan atas nama rekening bank terlebih dahulu di halaman investasi.";
				}
			}
		}
		return hsl;
	}


	
	public static void main(String[] args) {
	}
}
