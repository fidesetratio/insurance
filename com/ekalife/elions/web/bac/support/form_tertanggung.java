package com.ekalife.elions.web.bac.support;

import com.ekalife.utils.f_cek_tanggal;
import com.ekalife.utils.f_replace;
import com.ekalife.utils.f_validasi;

/*
 * Created on Aug 10, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author HEMILDA
 *
 * validasi untuk form tertanggung dalam penginputan bac
 */
public class form_tertanggung {
	boolean ib_new = false;
	boolean[] ib_ed_client;
	boolean[] ib_ed_alamat;
	boolean[] ib_ed_telp;
	boolean ib_ganti_relasi = false;
	int ii_aktif_tab = 1;
	long il_jamin_ps=0;
	long il_auto_ps=0;
	
	public form_tertanggung()
	{
		ib_ed_client = new boolean[2];
		ib_ed_alamat = new boolean[4];
		ib_ed_telp = new boolean[4];
	}

	/**
	 * @author HEMILDA
	 * validasi nama tertanggung kalau kosong
	 */
	public String nama_ttg(String nama_ttg)
	{
		String hsl="";
		if (nama_ttg==null)
		{
			nama_ttg="";
		}
		if (nama_ttg.trim().length()==0)
		{
			hsl="Nama tertanggung masih kosong, silahkan masukkan nama tertanggung terlebih dahulu";
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi tgl1
	 */
	public String cek_tanggal(int thn, int bln, int tgl)
	{
		boolean b;
		String hsl="";

		f_cek_tanggal a= new f_cek_tanggal();
		b=a.cek(thn,bln,tgl);	
		if (b==false)
		{
			hsl="Silahkan masukkan tanggal yang benar";
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi alamat rumah kalau kosong
	 */
	public String alamat_rumah_ttg(String alamat_rmh_ttg)
	{
		String hsl="";
		if (alamat_rmh_ttg==null)
		{
			alamat_rmh_ttg="";
		}
		if (alamat_rmh_ttg.trim().length()==0)
		{
			hsl="Alamat rumah tertanggung masih kosong, silahkan masukkan alamat rumah tertanggung terlebih dahulu";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi id kalau kosong
	 */
	public String nomor_id_ttg(String id_ttg)
	{
		String hsl="";
		if (id_ttg==null)
		{
			id_ttg="";
		}
		if (id_ttg.trim().length()==0)
		{
			hsl="ID tertanggung masih kosong, silahkan masukkan ID tertanggung terlebih dahulu";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi kode pos rumah kalau kosong
	 */
	public String kode_pos_rmh_ttg(String kdp_rm_ttg)
	{
		boolean cekk;
		String hsl="";
		if (kdp_rm_ttg==null)
		{
			kdp_rm_ttg="";
		}
		if (kdp_rm_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(kdp_rm_ttg);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan kode pos rumah tertanggung dalam bentuk numerik";
			}
		}
		return hsl;			
	}

	/**
	 * @author HEMILDA
	 * validasi kode area fax kalau kosong
	 */
	public String kode_area_fax(String kd_area_fax_pp)
	{
		String hsl="";
		if (kd_area_fax_pp==null)
		{
			kd_area_fax_pp="";
		}
		if (kd_area_fax_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_area_fax(kd_area_fax_pp);	
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi no fax kalau kosong
	 */
	public String fax_pp(String fax_pp)
	{
		String hsl="";
		if (fax_pp==null)
		{
			fax_pp="";
		}
		if (fax_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_fax(fax_pp);	
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi kode area rumah kalau kosong
	 */
	public String kode_area_rmh_ttg(String kd_area_ttg)
	{
		String hsl="";
		if (kd_area_ttg==null)
		{
			kd_area_ttg="";
		}
		if (kd_area_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_kode_area_rmh(kd_area_ttg);	
		}else{
			hsl="Jika tidak ada kode area telpon rumah 1 harap diisi dengan tanda -";
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi kode area rumah2 kalau kosong
	 */
	public String kode_area_rmh_ttg2(String kd_area_ttg)
	{
		String hsl="";
		if (kd_area_ttg==null)
		{
			kd_area_ttg="";
		}
		if (kd_area_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_kode_area_rmh(kd_area_ttg);	
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi telpon rumah kalau kosong
	 */
	public String telpon_rmh_ttg(String telp_ttg)
	{
		String hsl="";
		if (telp_ttg==null)
		{
			telp_ttg="";
		}
		if (telp_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_telp_rmh(telp_ttg);	
		}else{
			hsl="Jika tidak ada telpon rumah 1 harap diisi dengan tanda -";
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi telpon rumah2 kalau kosong
	 */
	public String telpon_rmh_ttg2(String telp_ttg)
	{
		String hsl="";
		if (telp_ttg==null)
		{
			telp_ttg="";
		}
		if (telp_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_telp_rmh(telp_ttg);	
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi kode pos kantor kalau kosong
	 */
	public String kode_pos_ktr_ttg(String kdp_ktr_ttg)
	{
		String hsl="";
		boolean cekk;
		if (kdp_ktr_ttg==null)
		{
			kdp_ktr_ttg="";
		}
		if (kdp_ktr_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(kdp_ktr_ttg);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan kode pos kantor tertanggung dalam bentuk numerik";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi kode area kantor kalau kosong
	 */
	public String kode_area_ktr_ttg(String kd_area_ttg)
	{
		String hsl="";
		if (kd_area_ttg==null)
		{
			kd_area_ttg="";
		}
		if (kd_area_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_kode_area_ktr(kd_area_ttg);	
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi tgl kantor kalau kosong
	 */
	public String telpon_ktr_ttg(String telp_ttg)
	{
		String hsl="";
		if (telp_ttg==null)
		{
			telp_ttg="";
		}
		if (telp_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_telp_ktr(telp_ttg);	
		}
		return hsl;			
	}
	
	/**
	 * @author HEMILDA
	 * validasi tgl valid kartu kredit
	 */
	public String tgl_lahir_ttg(String detil)
	{
		String hsl="";
		boolean cekk;
		if (detil==null)
		{
			detil="";
		}
		if (detil.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(detil);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan tanggal lahir tertanggung dalam bentuk numerik";
			}
		}
		return hsl;	
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian tempat lahir
	 */	
	public String tempat_lahir_ttg(String tmp_lhr_ttg)
	{
		String hsl="";
		if (tmp_lhr_ttg==null)
		{
			tmp_lhr_ttg="";
		}
		if (tmp_lhr_ttg.trim().length()==0)
		{
			hsl="Tempat lahir tertanggung masih kosong, silahkan masukkan tempat lahir tertanggung terlebih dahulu";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi isian hp1
	 */	
	public String handphone_ttg1(String hp_ttg)
	{
		boolean cekk;
		String hsl="";
		if (hp_ttg==null)
		{
			hp_ttg="";
		}
		if (hp_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(hp_ttg);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan No Handphone-1 tertanggung dalam bentuk numerik";
			}
		}
		return hsl;			
	}		

	/**
	 * @author HEMILDA
	 * validasi isian hp2
	 */	
	public String handphone_ttg2(String hp_ttg)
	{
		boolean cekk;
		String hsl="";
		if (hp_ttg==null)
		{
			hp_ttg="";
		}
		if (hp_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(hp_ttg);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan No Handphone-2 tertanggung dalam bentuk numerik";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi isian beasiswa
	 */	
	public String beasiswa_ttg(String umur)
	{
		String hsl="";
		boolean cekk;
		if (umur==null)
		{
			umur="";
		}
		if (umur.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(umur);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan umur beasiswa dalam bentuk numerik";
			}
		}
		return hsl;			
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian email
	 */	
	public String email_ttg(String mail_ttg)
	{
		String hsl="";
		boolean cekk;
		if (mail_ttg==null)
		{
			mail_ttg="";
		}
		if (mail_ttg.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= data.f_validasi_email(mail_ttg);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Email tertanggung dalam bentuk numerik,alphabet,@,_";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi timpa beberapa karakter
	 */	
	public String rubah(String detil)
	{
		if (detil==null)
		{
			detil="";
		}
		if (detil.trim().length()!=0)
		{
			f_replace konteks = new f_replace();
			detil=konteks.f_replace_karakter(detil);
		}
		return detil;	
	}	
		
	/**
	 * @author HEMILDA
	 * validasi isian alamat tagih
	 */	
	public String almt_tgh(String almt_tgh)
	{
		String hsl="";
		if (almt_tgh==null)
		{
			almt_tgh="";
		}
		if (almt_tgh.trim().length()==0)
		{
			hsl="Alamat tagihan masih kosong, Silahkan isi alamat tagihan terlebih dahulu ";
		}
		return hsl;
	} 
	
	/**
	 * @author HEMILDA
	 * validasi isian kode area telpon tagih
	 */	
	public String kd_area_tgh(String kd_area_tgh)
	{
		String hsl="";
		if (kd_area_tgh==null)
		{
			kd_area_tgh="";
		}
		if (kd_area_tgh.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_kode_area_tgh(kd_area_tgh);	
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi isian telpon tagih
	 */	
	public String telp_tgh(String telp_tgh)
	{
		String hsl="";
		if (telp_tgh==null)
		{
			telp_tgh="";
		}
		if (telp_tgh.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_telp_tgh(telp_tgh);	
		}
		return hsl;			
	}		

	/**
	 * @author HEMILDA
	 * validasi isian hp tagih 1
	 */	
	public String hp1_tgh(String hp1_tgh)
	{
		boolean cekk;
		String hsl="";
		if (hp1_tgh==null)
		{
			hp1_tgh="";
		}
		if (hp1_tgh.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(hp1_tgh);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan No Handphone-1 dalam bentuk numerik";
			}
		}
		return hsl;			
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian hp tagih 2
	 */	
	public String hp2_tgh(String hp2_tgh)
	{
		boolean cekk;
		String hsl="";
		if (hp2_tgh==null)
		{
			hp2_tgh="";
		}
		if (hp2_tgh.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(hp2_tgh);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan No Handphone-2 dalam bentuk numerik";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi isian kode pos tagih
	 */	
	public String kd_pos_tgh(String kd_pos_tgh)
	{
		boolean cekk;
		String hsl="";
		if (kd_pos_tgh==null)
		{
			kd_pos_tgh="";
		}
		if (kd_pos_tgh.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(kd_pos_tgh);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan kode pos tagihan dalam bentuk numerik";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi isian tujuan asuransi
	 */	
	public String cek_tujuan_asuransi(String tujuan)
	{
		String hasil_tujuan_asr="";
		if (tujuan==null)
		{
			tujuan="";
		}
		if(tujuan.trim().length()==0)
		{
			hasil_tujuan_asr="Tujuan Asuransi Lainnya, silahkan dijelaskan.";
		}
		return hasil_tujuan_asr;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian sumber dana
	 */	
	public String cek_sumber_dana(String sumber_dana)
	{
		String hasil_sumber_dana="";
		if (sumber_dana==null)
		{
			sumber_dana="";
		}
		if(sumber_dana.trim().length()==0)
		{
			hasil_sumber_dana="Sumber Dana Lainnya, silahkan dijelaskan.";
		}
		return hasil_sumber_dana;
	}
	
	public String cek_sumber_penghasilan(String sumber_penghasilan)
	{
		String hasil_sumber_penghasilan="";
		if (sumber_penghasilan==null)
		{
			sumber_penghasilan="";
		}
		if(sumber_penghasilan.trim().length()==0)
		{
			hasil_sumber_penghasilan="Silahkan dijelaskan Sumber Penghasilan Lainnya terlebih dahulu.";
		}
		return hasil_sumber_penghasilan;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian pekerjaan
	 */	
	public String cek_pekerjaan(String pekerjaan)
	{
		String hasil_pekerjaan="";
		if (pekerjaan==null)
		{
			pekerjaan="";
		}
		if(pekerjaan.trim().length()==0)
		{
			hasil_pekerjaan="Klasifikasi Pekerjaan Lainnya, silahkan sebutkan.";
		}
		return hasil_pekerjaan;
		
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian jabatan
	 */	
	public String cek_jabatan(String jabatan)
	{
		String hasil_jabatan="";
		if (jabatan==null)
		{
			jabatan="";
		}
		if (jabatan.trim().length()==0)
		{
			hasil_jabatan="Khusus Klasifikasi Pekerjaan Karyawan Swasta wajib mengisikan jabatan.";
		}
		return hasil_jabatan;
	}
	
	/**
	 * @author HEMILDA
	 * validasi isian bidang
	 */	
	public String cek_bidang(String bidang)
	{
		String hasil_bidang="";
		if (bidang==null)
		{
			bidang="";
		}
		if(bidang.trim().length()==0)
		{
			hasil_bidang="Klasifikasi Bidang Industri Lainnya, silahkan sebutkan.";
		}
		return hasil_bidang;
	}

	/**
	 * @author HEMILDA
	 * validasi isian kota rumah
	 */	
	public String kota_rumah_ttg(String kota_rmh_ttg)
	{
		String hsl="";
		if (kota_rmh_ttg==null)
		{
			kota_rmh_ttg="";
		}
		if (kota_rmh_ttg.trim().length()==0)
		{
			hsl="Kota rumah tertanggung masih kosong, silahkan masukkan kota rumah tertanggung terlebih dahulu";
		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi isian kota kantor
	 */	
	public String kota_kantor_ttg(String kota_kantor_ttg)
	{
		String hsl="";
		if (kota_kantor_ttg==null)
		{
			kota_kantor_ttg ="";
		}
		if (kota_kantor_ttg.trim().length()==0)
		{
			hsl="Kota kantor tertanggung masih kosong, silahkan masukkan kota kantor tertanggung terlebih dahulu";
		}
		return hsl;
	}	
	
	public static void main(String[] args) {
	}
}
