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
 * validasi untuk halaman penginputan pemegang polis
 */
public class form_pemegang_polis {
	boolean ib_new = false;
	boolean[] ib_ed_client;
	boolean[] ib_ed_alamat;
	boolean[] ib_ed_telp;
	boolean ib_ganti_relasi = false;
	int ii_aktif_tab = 1;
	long il_jamin_ps=0;
	long il_auto_ps=0;
	
	public form_pemegang_polis()
	{
		ib_ed_client = new boolean[2];
		ib_ed_alamat = new boolean[4];
		ib_ed_telp = new boolean[4];
	}

	/**
	 * @author HEMILDA
	 * validasi nama kalau kosong
	 */
	public String nama_pp(String nama_pp)
	{
		String hsl="";
		if (nama_pp==null)
		{
			nama_pp="";
		}
		if (nama_pp.trim().length()==0)
		{
			hsl="Nama pemegang polis masih kosong, silahkan masukkan nama pemegang polis terlebih dahulu";
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi tanggal
	 */
	public String cek_tanggal(int thn, int bln, int tgl)
	{
		boolean b;
		String hsl="";
		f_cek_tanggal a= new f_cek_tanggal();
		b=a.cek(thn,bln,tgl);	
		if (b==false)
		{
			hsl="Silahkan masukkan format tanggal yang benar";
		}
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi alamat rumah kosong
	 */
	public String alamat_rumah_pp(String alamat_rmh_pp)
	{
		String hsl="";
		if (alamat_rmh_pp==null)
		{
			alamat_rmh_pp="";
		}
		if (alamat_rmh_pp.trim().length()==0)
		{
			hsl="Alamat rumah pemegang polis masih kosong, silahkan masukkan alamat rumah pemegang polis terlebih dahulu";
		}
		return hsl;
	}	

	/**
	 * @author HEMILDA
	 * validasi kota rumah kalau kosong
	 */
	public String kota_rumah_pp(String kota_rmh_pp)
	{
		String hsl="";
		if (kota_rmh_pp==null)
		{
			kota_rmh_pp="";
		}
		if (kota_rmh_pp.trim().length()==0)
		{
			hsl="Kota rumah pemegang polis masih kosong, silahkan masukkan kota rumah pemegang polis terlebih dahulu";
		}
		return hsl;
	}

	/**
	 * @author HEMILDA
	 * validasi kota kantor kosong
	 */
	public String kota_kantor_pp(String kota_kantor_pp)
	{
		String hsl="";
		if (kota_kantor_pp==null)
		{
			kota_kantor_pp="";
		}
		if (kota_kantor_pp.trim().length()==0)
		{
			hsl="Kota kantor pemegang polis masih kosong, silahkan masukkan kota kantor pemegang polis terlebih dahulu";
		}
		return hsl;
	}
	
	/**
	 * @author Ridhaal
	 * validasi kota tempat tinggal PP kosong
	 */
	public String kota_tempattgl_pp(String kota_tpttgl_pp)
	{
		String hsl="";
		if (kota_tpttgl_pp==null)
		{
			kota_tpttgl_pp="";
		}
		if (kota_tpttgl_pp.trim().length()==0)
		{
			hsl="Kota tempat tinggal pemegang polis masih kosong, silahkan masukkan kota tempat tinggal pemegang polis terlebih dahulu";
		}
		return hsl;
	}	
	
	/**
	 * @author Ridhaal
	 * validasi kode pos tempat tinggal kosong
	 */
	public String kode_pos_tempattgl_pp(String kd_pos_tpttgl_pp)
	{
		String hsl="";
		boolean cekk;
		if (kd_pos_tpttgl_pp==null)
		{
			kd_pos_tpttgl_pp="";
		}
		if (kd_pos_tpttgl_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(kd_pos_tpttgl_pp);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan kode pos tempat tinggal pemegang polis dalam bentuk numerik";
			}
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi alamat tagih kosong
	 */
	public String alamat_tagih(String alamat_tagih)
	{
		String hsl="";
		if (alamat_tagih==null)
		{
			alamat_tagih="";
		}
		if (alamat_tagih.trim().length()==0)
		{
			hsl="Alamat Tagihan masih kosong, silahkan masukkan alamat tagihan terlebih dahulu";
		}
		return hsl;
	}	

	/**
	 * @author HEMILDA
	 * validasi kota kantor kosong
	 */
	public String kota_tagih(String kota_tagih)
	{
		String hsl="";
		if (kota_tagih==null)
		{
			kota_tagih="";
		}
		if (kota_tagih.trim().length()==0)
		{
			hsl="Kota tagihan masih kosong, silahkan masukkan kota tagihan terlebih dahulu";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi id pemegang kosong
	 */
	public String nomor_id_pp(String id_pp)
	{
		String hsl="";
		if (id_pp==null)
		{
			id_pp="";
		}
		if (id_pp.trim().length()==0)
		{
			hsl="ID pemegang polis masih kosong, silahkan masukkan ID pemegang polis terlebih dahulu";
		}
		return hsl;
	}	
	
	/**
	 * @author HEMILDA
	 * validasi kode pos rumah kosong
	 */
	public String kode_pos_rmh_pp(String kdp_rm_pp)
	{
		boolean cekk;
		String hsl="";
		if (kdp_rm_pp==null)
		{
			kdp_rm_pp="";
		}
		if (kdp_rm_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(kdp_rm_pp);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan kode pos rumah pemegang polis dalam bentuk numerik";
			}
		}
		return hsl;			
	}
	
	/**
	 * @author HEMILDA
	 * validasi kode area telpon rumah  kosong
	 */
	public String kode_area_rmh_pp(String kd_area_pp)
	{
		String hsl="";
		if (kd_area_pp==null)
		{
			kd_area_pp="";
		}
		if (kd_area_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_kode_area_rmh(kd_area_pp);	
		}else{
			hsl="Jika tidak ada kode area telpon rumah 1 harap diisi dengan tanda -";
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi kode area telpon rumah 2  kosong
	 */
	public String kode_area_rmh_pp2(String kd_area_pp)
	{
		String hsl="";
		if (kd_area_pp==null)
		{
			kd_area_pp="";
		}
		if (kd_area_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_kode_area_rmh(kd_area_pp);	
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi telpon rumah kosong
	 */
	public String telpon_rmh_pp(String telp_pp)
	{
		String hsl="";
		if (telp_pp==null)
		{
			telp_pp="";
		}
		if (telp_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_telp_rmh(telp_pp);	
		}else{
			hsl="Jika tidak ada telpon rumah 1 harap diisi dengan tanda -";
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi telpon rumah 2 kosong
	 */
	public String telpon_rmh_pp2(String telp_pp)
	{
		String hsl="";
		if (telp_pp==null)
		{
			telp_pp="";
		}
		if (telp_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_telp_rmh(telp_pp);	
		}
		return hsl;			
	}	
	
	/**
	 * @author HEMILDA
	 * validasi kode area fax kosong
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
	 * validasi fax kosong
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
	 * validasi kode pos kantor kosong
	 */
	public String kode_pos_ktr_pp(String kdp_ktr_pp)
	{
		String hsl="";
		boolean cekk;
		if (kdp_ktr_pp==null)
		{
			kdp_ktr_pp="";
		}
		if (kdp_ktr_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(kdp_ktr_pp);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan kode pos kantor pemegang polis dalam bentuk numerik";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi kode area kantor kosong
	 */
	public String kode_area_ktr_pp(String kd_area_pp)
	{
		String hsl="";
		if (kd_area_pp==null)
		{
			kd_area_pp="";
		}
		if (kd_area_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_kode_area_ktr(kd_area_pp);	
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi telpon kantor kosong
	 */
	public String telpon_ktr_pp(String telp_pp)
	{
		String hsl="";
		if (telp_pp==null)
		{
			telp_pp="";
		}
		if (telp_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			hsl= data.f_validasi_telp_ktr(telp_pp);	
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi tanggal lahir kosong
	 */
	public String tgl_lahir_pp(String detil)
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
				hsl="Silahkan masukkan tanggal lahir pemegang polis dalam bentuk numerik";
			}
		}
		return hsl;	
	}
	
	/**
	 * @author HEMILDA
	 * validasi tempat lahir kosong
	 */
	public String tempat_lahir_pp(String tmp_lhr_pp)
	{
		String hsl="";
		if (tmp_lhr_pp==null)
		{
			tmp_lhr_pp="";
		}
		if (tmp_lhr_pp.trim().length()==0)
		{
			hsl="Tempat lahir pemegang polis masih kosong, silahkan masukkan tempat lahir pemegang polis terlebih dahulu";
		}
		return hsl;
	}	

	/**
	 * @author HEMILDA
	 * validasi hp kosong
	 */
	public String handphone_pp2(String hp_pp)
	{
			boolean cekk;
			String hsl="";
			if (hp_pp==null)
			{
				hp_pp="";
			}
			if (hp_pp.trim().length()!=0)
			{
				f_validasi data = new f_validasi();
				cekk= f_validasi.f_validasi_numerik(hp_pp);	
				if (cekk==false)
				{
					hsl="Silahkan masukkan No Handphone-2 pemegang polis dalam bentuk numerik";
				}
			}
			return hsl;			
	}		

	/**
	 * @author HEMILDA
	 * validasi hp1 kosong
	 */
	public String handphone_pp1(String hp_pp)
	{
		boolean cekk;
		String hsl="";
		if (hp_pp==null)
		{
			hp_pp="";
		}
		if (hp_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= f_validasi.f_validasi_numerik(hp_pp);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan No Handphone-1 pemegang polis dalam bentuk numerik";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi email 1 kosong
	 */
	public String email_pp1(String mail_pp)
	{
		String hsl="";
		boolean cekk;
		if (mail_pp==null)
		{
			mail_pp="";
		}
		if (mail_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= data.f_validasi_email(mail_pp);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Email pemegang polis dalam bentuk numerik,alphabet,@,_";
			}
		}
		return hsl;			
	}		
	
	/**
	 * @author HEMILDA
	 * validasi email 2 kosong
	 */
	public String email_pp(String mail_pp)
	{
		String hsl="";
		boolean cekk;
		if (mail_pp==null)
		{
			mail_pp="";
		}
		if (mail_pp.trim().length()!=0)
		{
			f_validasi data = new f_validasi();
			cekk= data.f_validasi_email(mail_pp);	
			if (cekk==false)
			{
				hsl="Silahkan masukkan Email penagihan dalam bentuk numerik,alphabet,@,_";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi ganti per karakter
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
	 * validasi kode area tagih kosong
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
	 * validasi telpon tagih kosong
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
	 * validasi hp1 tagih kosong
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
				hsl="Silahkan masukkan No Handphone-1 tagihan dalam bentuk numerik";
			}
		}
		return hsl;			
	}
	
	/**
	 * @author HEMILDA
	 * validasi hp2 tagih kosong
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
				hsl="Silahkan masukkan No Handphone-2 tagihan dalam bentuk numerik";
			}
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi kode pos tagih kosong
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
	 * @author Andhika
	 * validasi Negara tagih
	 */
	public String lsne_tgh(String lsne_tgh)
	{
		boolean cekk;
		String hsl="";
		if (lsne_tgh==null)
		{
			lsne_tgh="";
		}
		if (lsne_tgh==null){
			hsl="Negara Penagihan Masih kosong, silahkan pilih negara terlebih dahulu";
		}
		return hsl;			
	}	

	/**
	 * @author HEMILDA
	 * validasi no blanko kosong
	 */
	public String no_seri(String nomor)
	{
		String hsl="";
		if (nomor==null)
		{
			nomor="";
		}
		if (nomor.trim().length()==0)
		{
			hsl="Nomor Seri masih kosong, silahkan masukkan Nomor Seri terlebih dahulu";
		}/*else{
			f_validasi data = new f_validasi();
			hsl=data.f_validasi_nomor_seri(nomor);
		}*/
		return hsl;
	}
	
	/**
	 * @author HEMILDA
	 * validasi tujuan asuransi kosong
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
			hasil_tujuan_asr="Silahkan dijelaskan Tujuan Asuransi Lainnya terlebih dahulu.";
		}
		return hasil_tujuan_asr;
	}
	
	/**
	 * @author HEMILDA
	 * validasi sumber dana kosong
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
			hasil_sumber_dana="Silahkan dijelaskan Sumber Dana Lainnya terlebih dahulu.";
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
	 * validasi pekerjaan kosong
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
			hasil_pekerjaan="Silahkan sebutkan Klasifikasi Pekerjaan Lainnya terlebih dahulu.";
		}
		return hasil_pekerjaan;
		
	}
	
	/**
	 * @author HEMILDA
	 * validasi jabatan kosong
	 */
	public String cek_jabatan(String jabatan)
	{
		String hasil_jabatan="";
		if(jabatan==null)
		{
			jabatan="";
		}
		if (jabatan.trim().length()==0)
		{
			hasil_jabatan="Khusus Klasifikasi Pekerjaan Karyawan Swasta wajib mengisikan jabatan terlebih dahulu.";
		}
		return hasil_jabatan;
	}
	
	/**
	 * @author HEMILDA
	 * validasi bidang industri kosong
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
			hasil_bidang="Silahkan sebutkan Klasifikasi Bidang Industri Lainnya terlebih dahulu.";
		}
		return hasil_bidang;
	}

		public static void main(String[] args) {
	}
}
