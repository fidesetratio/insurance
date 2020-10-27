package com.ekalife.elions.web.uw.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.ekalife.elions.model.DataNasabah;
import com.ekalife.elions.model.Keluarga;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_validasi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * @author Hemilda
 * @since Aug 7, 2007 (6:44:30 PM)
 */
public class Nasabahvalidator implements Validator{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private ElionsManager elionsManager;
	
	
	public boolean supports(Class data) {
		return DataNasabah.class.isAssignableFrom(data);
	}
	
	public void validate(Object cmd, Errors err) {
		DataNasabah nasabah = (DataNasabah) cmd;
		List  alert=new ArrayList();
		nasabah.getDatausulan().setStatus_submit("");

		
		f_validasi data = new f_validasi();
		Integer jumlah_client_old = this.elionsManager.select_mst_client_old(nasabah.getPemegang().getMcl_id());
		if (jumlah_client_old.intValue() > 0)
		{
		//	err.rejectValue("pemegang.lside_id", "", "Updating Data sudah pernah dilakukan dan hanya boleh dilakukan satu kali.");
			
		}
		Integer pemegang_lside_id = nasabah.getPemegang().getLside_id();
		if (pemegang_lside_id == null)
		{
			alert.add("Silahkan pilih terlebih dahulu bukti identitas pemegang polis");
			//err.rejectValue("pemegang.lside_id", "", "Silahkan pilih terlebih dahulu bukti identitas pemegang polis");
		}
		
		String pemegang_mspe_no_identity = nasabah.getPemegang().getMspe_no_identity();
		if (pemegang_mspe_no_identity == null)
		{
			pemegang_mspe_no_identity = "";
			nasabah.getPemegang().setMspe_no_identity("");
		}
		if (pemegang_mspe_no_identity.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu no identitas pemegang polis");
			//err.rejectValue("pemegang.mspe_no_identity", "", "Silahkan isi terlebih dahulu no identitas pemegang polis");
		}
		
		String pemegang_alamat_rumah = nasabah.getPemegang().getAlamat_rumah();
		if (pemegang_alamat_rumah  == null)
		{
			pemegang_alamat_rumah = "";
			nasabah.getPemegang().setAlamat_rumah("");
		}
		if (pemegang_alamat_rumah.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu alamat rumah pemegang polis");
			//err.rejectValue("pemegang.alamat_rumah", "", "Silahkan isi terlebih dahulu alamat rumah pemegang polis");
		}
		
		String pemegang_kota_rumah = nasabah.getPemegang().getKota_rumah();
		if (pemegang_kota_rumah == null)
		{
			pemegang_kota_rumah = "";
			nasabah.getPemegang().setKota_rumah("");
		}
		if (pemegang_kota_rumah.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu kota rumah pemegang polis");
			//err.rejectValue("pemegang.kota_rumah", "", "Silahkan isi terlebih dahulu kota rumah pemegang polis");
		}
		
		String pemegang_kd_pos_rumah = nasabah.getPemegang().getKd_pos_rumah();
		 if (pemegang_kd_pos_rumah == null)
		 {
			 pemegang_kd_pos_rumah ="";
			 nasabah.getPemegang().setKd_pos_rumah("");
		 }
		 if (pemegang_kd_pos_rumah.equalsIgnoreCase(""))
		 {
			 alert.add("Silahkan isi terlebih dahulu kode pos rumah pemegang polis");
			 //err.rejectValue("pemegang.kd_pos_rumah", "", "Silahkan isi terlebih dahulu kode pos rumah pemegang polis");
		 }else{
			 Boolean cekk= f_validasi.f_validasi_numerik(pemegang_kd_pos_rumah);	
			 String hasil_kd_pos_rmh_pp0 ="";
				if (cekk==false)
				{
					hasil_kd_pos_rmh_pp0="Silahkan masukkan kode pos rumah pemegang polis dalam bentuk numerik";
				}
			 if (!hasil_kd_pos_rmh_pp0.equalsIgnoreCase(""))
			 {
				 alert.add("Silahkan masukkan kode pos rumah pemegang polis dalam bentuk numerik");
				 //err.rejectValue("pemegang.kd_pos_rumah", "", hasil_kd_pos_rmh_pp0);
			 }
		 }
		
		String pemegang_area_code_rumah = nasabah.getPemegang().getArea_code_rumah();
		if (pemegang_area_code_rumah == null)
		{
			pemegang_area_code_rumah = "";
			nasabah.getPemegang().setArea_code_rumah("");
		}
		if (pemegang_area_code_rumah.equalsIgnoreCase(""))
		{
			 alert.add("Silahkan isi terlebih dahulu kode area rumah pemegang polis");
			 //err.rejectValue("pemegang.area_code_rumah", "", "Silahkan isi terlebih dahulu kode area rumah pemegang polis");
		}else{
			String errors = data.f_validasi_kode_area_rmh(pemegang_area_code_rumah);
			if (!errors.equalsIgnoreCase(""))
			{
				 alert.add(errors);
				//err.rejectValue("pemegang.area_code_rumah", "",  errors);
			}
		}
		
		String pemegang_telpon_rumah = nasabah.getPemegang().getTelpon_rumah();
		if (pemegang_telpon_rumah ==null)
		{
			pemegang_telpon_rumah ="";
			nasabah.getPemegang().setTelpon_rumah("");
		}
		if (pemegang_telpon_rumah.equalsIgnoreCase(""))
		{
			 alert.add("Jika tidak ada telpon rumah pemegang polis 1 harap diisi dengan tanda -");
			//err.rejectValue("pemegang.telpon_rumah", "", "Jika tidak ada telpon rumah pemegang polis 1 harap diisi dengan tanda -");
		}else{
			String hsl= data.f_validasi_telp_rmh(pemegang_telpon_rumah);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("pemegang.telpon_rumah", "", hsl);
			}
		}
		
		String pemegang_area_code_rumah2 = nasabah.getPemegang().getArea_code_rumah2();
		if (pemegang_area_code_rumah2 == null)
		{
			pemegang_area_code_rumah2 ="";
			nasabah.getPemegang().setArea_code_rumah2("");
		}else{
			String errors = data.f_validasi_kode_area_rmh(pemegang_area_code_rumah2);
			if (!errors.equalsIgnoreCase(""))
			{
				alert.add(errors);
				//err.rejectValue("pemegang.area_code_rumah2", "",  errors);
			}
		}
		/*if (pemegang_area_code_rumah2.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.area_code_rumah2", "", "Silahkan isi terlebih dahulu kode area rumah pemegang polis 2");
		}*/
		
		String pemegang_telpon_rumah2 = nasabah.getPemegang().getTelpon_rumah2();
		if (pemegang_telpon_rumah2 == null)
		{
			pemegang_telpon_rumah2="";
			nasabah.getPemegang().setTelpon_rumah2("");
		}else{
			String hsl= data.f_validasi_telp_rmh(pemegang_telpon_rumah2);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("pemegang.telpon_rumah2", "", hsl);
			}
		}
		/*if (pemegang_telpon_rumah2.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.telpon_rumah2", "", "Silahkan isi terlebih dahulu telpon rumah pemegang polis 2");
		}*/
		
		String pemegang_alamat_kantor = nasabah.getPemegang().getAlamat_kantor();
		if (pemegang_alamat_kantor == null)
		{
			pemegang_alamat_kantor ="";
			nasabah.getPemegang().setAlamat_kantor("");
		}
		/*if (pemegang_alamat_kantor.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.alamat_kantor", "", "Silahkan isi terlebih dahulu alamat kantor pemegang polis");
		}*/
		
		String pemegang_kota_kantor = nasabah.getPemegang().getKota_kantor();
		if (pemegang_kota_kantor == null)
		{
			pemegang_kota_kantor = "";
			nasabah.getPemegang().setKota_kantor("");
		}
	/*	if (pemegang_kota_kantor.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.kota_kantor", "", "Silahkan isi terlebih dahulu kota kantor pemegang polis");
		}*/
		
		String pemegang_kd_pos_kantor = nasabah.getPemegang().getKd_pos_kantor();
		if (pemegang_kd_pos_kantor==null)
		{
			pemegang_kd_pos_kantor = "";
			nasabah.getPemegang().setKd_pos_kantor("");
		}else{
			Boolean cekk= f_validasi.f_validasi_numerik(pemegang_kd_pos_kantor);	
			if (cekk==false)
			{
				String hsl="Silahkan masukkan kode pos kantor pemegang polis dalam bentuk numerik";
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("pemegang.kd_pos_kantor", "",  hsl);

				}
			}
		}
		/*if (pemegang_kd_pos_kantor.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.kd_pos_kantor", "", "Silahkan isi terlebih dahulu kode pos kantor pemegang polis");
		}*/
		
		String pemegang_area_code_kantor = nasabah.getPemegang().getArea_code_kantor();
		if (pemegang_area_code_kantor == null)
		{
			pemegang_area_code_kantor = "";
			nasabah.getPemegang().setArea_code_kantor("");
		}else{
			String hsl= data.f_validasi_kode_area_ktr(pemegang_area_code_kantor);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("pemegang.area_code_kantor", "", hsl);
			}
		}
		/*if (pemegang_area_code_kantor.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.area_code_kantor", "", "Silahkan isi terlebih dahulu kode area kantor pemegang polis");
		}*/
		
		String pemegang_telpon_kantor = nasabah.getPemegang().getTelpon_kantor();
		if (pemegang_telpon_kantor == null)
		{
			pemegang_telpon_kantor = "";
			nasabah.getPemegang().setTelpon_kantor("");
		}else{
			String hsl= data.f_validasi_telp_ktr(pemegang_telpon_kantor);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("pemegang.telpon_kantor", "", hsl);
			}
		}
		/*if (pemegang_telpon_kantor.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.telpon_kantor", "", "Silahkan isi terlebih dahulu telpon kantor pemegang polis");
		}*/
		
		String pemegang_area_code_kantor2 = nasabah.getPemegang().getArea_code_kantor2();
		if (pemegang_area_code_kantor2 == null)
		{
			pemegang_area_code_kantor2 ="";
			nasabah.getPemegang().setArea_code_kantor2("");
		}else{
			String hsl= data.f_validasi_kode_area_ktr(pemegang_area_code_kantor2);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("pemegang.area_code_kantor2", "", hsl);
			}
		}
		/*if (pemegang_area_code_kantor2.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.area_code_kantor2", "", "Silahkan isi terlebih dahulu kode area kantor pemegang polis 2 ");
		}*/
		
		String pemegang_telpon_kantor2 = nasabah.getPemegang().getTelpon_kantor2();
		if (pemegang_telpon_kantor2 == null)
		{
			pemegang_telpon_kantor2 = "";
			nasabah.getPemegang().setTelpon_kantor2("");
		}else{
			String hsl= data.f_validasi_telp_ktr(pemegang_telpon_kantor2);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("pemegang.telpon_kantor2", "", hsl);
			}
		}
	/*	if (pemegang_telpon_kantor2.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.telpon_kantor2", "", "Silahkan isi terlebih dahulu telpon kantor pemegang polis 2 ");
		}*/
		
		Date sysdate = elionsManager.selectSysdate();
		for(Keluarga keluarga_pp : nasabah.getPemegang().getListKeluarga()) {
			//keluarga_pp.setTanggal_lahir(sysdate);
			if (keluarga_pp.getNama() == null)
			{
				keluarga_pp.setNama("");
			}

			if (keluarga_pp.getTanggal_lahir() == null)
			{
				keluarga_pp.setTanggal_lahir(null);
			}
		}
		
		nasabah.getAddressBilling().setMsap_contact(nasabah.getPemegang().getMcl_first());
		
		String addressBilling_msap_address = nasabah.getAddressBilling().getMsap_address();
		if (addressBilling_msap_address == null)
		{
			addressBilling_msap_address = "";
			nasabah.getAddressBilling().setMsap_address("");
		}
		if (addressBilling_msap_address.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu alamat tagihan");
			//err.rejectValue("addressBilling.msap_address", "", "Silahkan isi terlebih dahulu alamat tagihan");
		}
		
		String addressBilling_kota = nasabah.getAddressBilling().getKota();
		if (addressBilling_kota == null)
		{
			addressBilling_kota = "";
			nasabah.getAddressBilling().setKota("");
		}
		if (addressBilling_kota.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu kota tagihan");
			//err.rejectValue("addressBilling.kota", "", "Silahkan isi terlebih dahulu kota tagihan");
		}
		
		String addressBilling_msap_zip_code = nasabah.getAddressBilling().getMsap_zip_code();
		if (addressBilling_msap_zip_code == null)
		{
			addressBilling_msap_zip_code = "";
			nasabah.getAddressBilling().setMsap_zip_code("");
		}
		if (addressBilling_msap_zip_code.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu kode pos tagihan");
			//err.rejectValue("addressBilling.msap_zip_code", "", "Silahkan isi terlebih dahulu kode pos tagihan");
		}else{
			Boolean cekk= f_validasi.f_validasi_numerik(addressBilling_msap_zip_code);	
			if (cekk==false)
			{
				String hsl="Silahkan masukkan kode pos tagihan dalam bentuk numerik";
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("addressBilling.msap_zip_code", "", hsl);
				}
			}
		}
		
		String addressBilling_msap_area_code1 = nasabah.getAddressBilling().getMsap_area_code1();
		if (addressBilling_msap_area_code1 == null)
		{
			addressBilling_msap_area_code1 = "";
			nasabah.getAddressBilling().setMsap_area_code1("");
		}
		if (addressBilling_msap_area_code1.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu kode area tagihan 1");
			//err.rejectValue("addressBilling.msap_area_code1", "", "Silahkan isi terlebih dahulu kode area tagihan 1");
		}else{
			String hsl= data.f_validasi_kode_area_tgh(addressBilling_msap_area_code1);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("addressBilling.msap_area_code1", "", hsl);
			}
		}
		
		String addressBilling_msap_phone1 = nasabah.getAddressBilling().getMsap_phone1();
		if (addressBilling_msap_phone1 == null)
		{
			addressBilling_msap_phone1 = "";
			nasabah.getAddressBilling().setMsap_phone1("");
		}else{
			String hsl= data.f_validasi_telp_tgh(addressBilling_msap_phone1);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("addressBilling.msap_phone1", "", hsl);
			}
		}
		if (addressBilling_msap_phone1.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu telpon tagihan 1");
			//err.rejectValue("addressBilling.msap_phone1", "", "Silahkan isi terlebih dahulu telpon tagihan 1");
		}
		
		String addressBilling_msap_area_code2 = nasabah.getAddressBilling().getMsap_area_code2();
		if (addressBilling_msap_area_code2 == null)
		{
			addressBilling_msap_area_code2 = "";
			nasabah.getAddressBilling().setMsap_area_code2("");
		}else{
			String hsl= data.f_validasi_kode_area_tgh(addressBilling_msap_area_code2);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("addressBilling.msap_area_code2", "", hsl);
			}
		}
		/*if (addressBilling_msap_area_code2.equalsIgnoreCase(""))
		{
			err.rejectValue("addressBilling_msap_area_code2", "", "Silahkan isi terlebih dahulu kode area tagihan 2");
		}*/
		
		String addressBilling_msap_phone2 = nasabah.getAddressBilling().getMsap_phone2();
		if (addressBilling_msap_phone2 == null)
		{
			addressBilling_msap_phone2 = "";
			nasabah.getAddressBilling().setMsap_phone2("");
		}else{
			String hsl= data.f_validasi_telp_tgh(addressBilling_msap_phone2);	
			if (!hsl.equalsIgnoreCase(""))
			{
				alert.add(hsl);
				//err.rejectValue("addressBilling.msap_phone2", "", hsl);
			}
		}
		/*if (addressBilling_msap_phone2.equalsIgnoreCase(""))
		{
			err.rejectValue("addressBilling_msap_phone2", "", "Silahkan isi terlebih dahulu telpon tagihan 2");
		}*/
		
		String pemegang_no_hp = nasabah.getPemegang().getNo_hp();
		if (pemegang_no_hp == null)
		{
			pemegang_no_hp= "";
			nasabah.getPemegang().setNo_hp("");
		}else{
			Boolean cekk= f_validasi.f_validasi_numerik(pemegang_no_hp);	
			if (cekk==false)
			{
				String hsl="Silahkan masukkan No Handphone-1 tagihan dalam bentuk numerik";
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("pemegang.no_hp", "", hsl);
				}
			}
		}
	/*	if (pemegang_no_hp.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.no_hp", "", "Silahkan isi terlebih dahulu no hp  pemegang polis 1");
		}*/
		
		String pemegang_no_hp2 = nasabah.getPemegang().getNo_hp2();
		if (pemegang_no_hp2 == null)
		{
			pemegang_no_hp2 = "";
			nasabah.getPemegang().setNo_hp2("");
		}else{
			Boolean cekk= f_validasi.f_validasi_numerik(pemegang_no_hp2);	
			if (cekk==false)
			{
				String hsl="Silahkan masukkan No Handphone-2 tagihan dalam bentuk numerik";
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("pemegang.no_hp2", "", hsl);
				}
			}
		}
		/*if (pemegang_no_hp2.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.no_hp2", "", "Silahkan isi terlebih dahulu no hp  pemegang polis 2");
		}*/
		
		String pemegang_email = nasabah.getPemegang().getEmail();
		if (pemegang_email == null)
		{
			pemegang_email = "";
			nasabah.getPemegang().setEmail("");
		}else{
			if (!pemegang_email.equalsIgnoreCase(""))
			{
				Boolean cekk= data.f_validasi_email(pemegang_email);	
				if (cekk==false)
				{
					String hsl="Silahkan masukkan Email pemegang polis dalam bentuk numerik,alphabet,@,_";
					alert.add(hsl);
					//err.rejectValue("pemegang.email", "", hsl);
				}
			}
		}
		/*if (pemegang_email.equalsIgnoreCase(""))
		{
			err.rejectValue("pemegang.email", "", "Silahkan isi terlebih dahulu email");
		}*/
		
		String addressBilling_e_mail = nasabah.getAddressBilling().getE_mail();
		if (addressBilling_e_mail == null)
		{
			addressBilling_e_mail = "";
			nasabah.getAddressBilling().setE_mail("");
		}else{
			if (!addressBilling_e_mail.equalsIgnoreCase(""))
			{
				Boolean cekk= data.f_validasi_email(addressBilling_e_mail);	
				if (cekk==false)
				{
					String hsl="Silahkan masukkan Email pemegang polis 2 dalam bentuk numerik,alphabet,@,_";
					alert.add(hsl);
					//err.rejectValue("addressBilling.e_mail", "", hsl);
				}
			}
		}
		/*if (addressBilling_e_mail.equalsIgnoreCase(""))
		{
			err.rejectValue("addressBilling_e_mail", "", "Silahkan isi terlebih dahulu email 2");
		}*/
		
		String pemegang_mkl_kerja = nasabah.getPemegang().getMkl_kerja();
		if (pemegang_mkl_kerja == null)
		{
			pemegang_mkl_kerja = "";
			nasabah.getPemegang().setMkl_kerja("");
		}
		if (pemegang_mkl_kerja.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu pekerjaan  pemegang polis");
			//err.rejectValue("pemegang.mkl_kerja", "", "Silahkan isi terlebih dahulu pekerjaan  pemegang polis" );
		}
		//Klasifikasi Pekerjaan
		String pekerjaan= nasabah.getPemegang().getMkl_kerja();
		String pekerjaan_oth= nasabah.getPemegang().getKerjaa();
		String hasil_pekerjaan="";
		if (pekerjaan==null)
		{
			pekerjaan="Lainnya";
		}
		if (pekerjaan.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			if (pekerjaan_oth==null)
			{
				pekerjaan_oth="";
			}
			if(pekerjaan_oth.trim().length()==0)
			{
				hasil_pekerjaan="Silahkan sebutkan Klasifikasi Pekerjaan pemegang polis Lainnya terlebih dahulu.";
				alert.add(hasil_pekerjaan);
				//err.rejectValue("pemegang.mkl_kerja", "", hasil_pekerjaan );

			}
			if (hasil_pekerjaan.trim().length()!=0)
			{
				nasabah.getPemegang().setKerjaa(f_validasi.convert_karakter(pekerjaan_oth));
			}
			nasabah.getPemegang().setKerjaa(f_validasi.convert_karakter(pekerjaan_oth));
		}else{
			nasabah.getPemegang().setKerjaa("");
		}
		
		String jabatan=nasabah.getPemegang().getKerjab();
		String hasil_jabatan="";
		if (jabatan==null)
		{
			jabatan="";
		}

		if (pekerjaan.trim().equalsIgnoreCase("Karyawan"))
		{
			if(jabatan==null)
			{
				jabatan="";
			}
			if (jabatan.trim().length()==0)
			{
				hasil_jabatan="Khusus Klasifikasi Pekerjaan Karyawan wajib mengisikan jabatan pemegang polis terlebih dahulu.";
				alert.add(hasil_jabatan);
				//err.rejectValue("pemegang.mkl_kerja", "", hasil_jabatan );
			}
			if (hasil_jabatan.trim().length()!=0)
			{
				nasabah.getPemegang().setKerjab(f_validasi.convert_karakter(jabatan));
			}
			nasabah.getPemegang().setKerjab(f_validasi.convert_karakter(jabatan));

		}else{
			nasabah.getPemegang().setKerjab("");
		}

		
		String pemegang_mkl_industri = nasabah.getPemegang().getMkl_industri();
		if (pemegang_mkl_industri == null)
		{
			pemegang_mkl_industri = "";
			nasabah.getPemegang().setMkl_industri("");
		}
		if (pemegang_mkl_industri.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu Bidang Usaha  pemegang polis");
			//err.rejectValue("pemegang.mkl_industri", "", "Silahkan isi terlebih dahulu Bidang Usaha  pemegang polis");
		}
		//klasifikasi bidang industri
		String bidang= nasabah.getPemegang().getMkl_industri();
		String bidang_oth= nasabah.getPemegang().getIndustria();
		String hasil_bidang="";
		if (bidang==null)
		{
			bidang="Lainnya";
		}
		if (bidang.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			if (bidang_oth==null)
			{
				bidang_oth="";
			}
			if(bidang_oth.trim().length()==0)
			{
				hasil_bidang="Silahkan sebutkan Klasifikasi Bidang Industri pemegang polis Lainnya terlebih dahulu.";
				alert.add(hasil_bidang);
				//err.rejectValue("pemegang.mkl_industri", "", hasil_bidang);

			}
			if (hasil_bidang.trim().length()!=0)
			{
				nasabah.getPemegang().setIndustria(f_validasi.convert_karakter(bidang_oth));

			}
			nasabah.getPemegang().setIndustria(f_validasi.convert_karakter(bidang_oth));
		}else{
			nasabah.getPemegang().setIndustria("");
		}
		
		String pemegang_mkl_penghasilan = nasabah.getPemegang().getMkl_penghasilan();
		if (pemegang_mkl_penghasilan == null)
		{
			pemegang_mkl_penghasilan = "";
			nasabah.getPemegang().setMkl_penghasilan("");
		}
		if (pemegang_mkl_penghasilan.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu penghasilan per tahun  pemegang polis");	
			//err.rejectValue("pemegang.mkl_penghasilan", "", "Silahkan isi terlebih dahulu penghasilan per tahun  pemegang polis");
		}
		
		String pemegang_mkl_pendanaan = nasabah.getPemegang().getMkl_pendanaan();
		if (pemegang_mkl_pendanaan == null)
		{
			pemegang_mkl_pendanaan = "";
			nasabah.getPemegang().setMkl_pendanaan("");
		}
		if (pemegang_mkl_pendanaan.equalsIgnoreCase(""))
		{
			alert.add("Silahkan isi terlebih dahulu sumber penghasilan  pemegang polis");	
			//err.rejectValue("pemegang.mkl_pendanaan", "", "Silahkan isi terlebih dahulu sumber penghasilan  pemegang polis");
		}
		//sumber pendanaan pembelian asuransi
		String sumber_dana= nasabah.getPemegang().getMkl_pendanaan();
		String sumber_dana_oth= nasabah.getPemegang().getDanaa();
		String hasil_sumber_dana="";
		if (sumber_dana==null)
		{
			sumber_dana="Lainnya";
		}
		if (sumber_dana.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
		{
			if (sumber_dana_oth==null)
			{
				sumber_dana_oth="";
			}
			if(sumber_dana_oth.trim().length()==0)
			{
				hasil_sumber_dana="Silahkan dijelaskan Sumber Dana pemegang polis Lainnya terlebih dahulu.";
				alert.add(hasil_sumber_dana);	
				//err.rejectValue("pemegang.mkl_pendanaan", "", hasil_sumber_dana);

			}
			if (hasil_sumber_dana.trim().length()!=0)
			{
				nasabah.getPemegang().setDanaa(f_validasi.convert_karakter(sumber_dana_oth));
			}
			nasabah.getPemegang().setDanaa(f_validasi.convert_karakter(sumber_dana_oth));
		}else{
			nasabah.getPemegang().setDanaa("");
		}

		//tertanggung
		//pengecekan realasi antara pp dan tt
		//jika pp dan tt null maka di cekin apakah mcl_id sama. jika sama lsre_id=1
		//kalo beda maka untuk sementara di kasih lsre_id=20 (Sementara lsre_id sebelumnya NULL)
		if(nasabah.getPemegang().getLsre_id()==null){
			if(nasabah.getPemegang().getMcl_id()==nasabah.getTertanggung().getMcl_id()){
				nasabah.getPemegang().setLsre_id(1);//diri sendiri karena id pp dan tt sama
				//update tabel mst_policy lsre_id=1(diri sendiri) lstbId=1(Individu)
				elionsManager.updateMstPolicyLsreId(nasabah.getSpajAwal(),1,1);
			}else{
				nasabah.getPemegang().setLsre_id(20);
				//update tabel mst_policy lsre_id=20(Relasi Sementara) lstbId=1(Individu)
				elionsManager.updateMstPolicyLsreId(nasabah.getSpajAwal(),20,1);
			}
		}

		if (nasabah.getPemegang().getLsre_id().intValue() != 1)
		{
			
			Integer tertanggung_lside_id = nasabah.getTertanggung().getLside_id();
			if (tertanggung_lside_id == null)
			{
				alert.add("Silahkan pilih terlebih dahulu bukti identitas tertanggung");	
				//err.rejectValue("tertanggung.lside_id", "", "Silahkan pilih terlebih dahulu bukti identitas tertanggung");
			}
			
			String tertanggung_mspe_no_identity = nasabah.getTertanggung().getMspe_no_identity();
			if (tertanggung_mspe_no_identity == null)
			{
				tertanggung_mspe_no_identity = "";
				nasabah.getTertanggung().setMspe_no_identity("");
			}
			if (tertanggung_mspe_no_identity.equalsIgnoreCase(""))
			{
				alert.add("Silahkan isi terlebih dahulu no identitas tertanggung");	
				//err.rejectValue("tertanggung.mspe_no_identity", "", "Silahkan isi terlebih dahulu no identitas tertanggung");
			}
			
			String tertanggung_alamat_rumah = nasabah.getTertanggung().getAlamat_rumah();
			if (tertanggung_alamat_rumah  == null)
			{
				tertanggung_alamat_rumah = "";
				nasabah.getTertanggung().setAlamat_rumah("");
			}
			if (tertanggung_alamat_rumah.equalsIgnoreCase(""))
			{
				alert.add("Silahkan isi terlebih dahulu alamat rumah tertanggung");
				//err.rejectValue("tertanggung.alamat_rumah", "", "Silahkan isi terlebih dahulu alamat rumah tertanggung");
			}
			
			String tertanggung_kota_rumah = nasabah.getTertanggung().getKota_rumah();
			if (tertanggung_kota_rumah == null)
			{
				tertanggung_kota_rumah = "";
				nasabah.getTertanggung().setKota_rumah("");
			}
			if (tertanggung_kota_rumah.equalsIgnoreCase(""))
			{
				alert.add("Silahkan isi terlebih dahulu kota rumah tertanggung");
				//err.rejectValue("tertanggung.kota_rumah", "", "Silahkan isi terlebih dahulu kota rumah tertanggung");
			}
			
			String tertanggung_kd_pos_rumah = nasabah.getTertanggung().getKd_pos_rumah();
			 if (tertanggung_kd_pos_rumah == null)
			 {
				 tertanggung_kd_pos_rumah ="";
				 nasabah.getTertanggung().setKd_pos_rumah("");
			 }else{
				Boolean	cekk= f_validasi.f_validasi_numerik(tertanggung_kd_pos_rumah);	
					if (cekk==false)
					{
						String hsl="Silahkan masukkan kode pos rumah tertanggung dalam bentuk numerik";
						if (!hsl.equalsIgnoreCase(""))
						{
							alert.add(hsl);
							//err.rejectValue("tertanggung.kd_pos_rumah", "", hsl);
						}
					}
			 }
			 if (tertanggung_kd_pos_rumah.equalsIgnoreCase(""))
			 {
				alert.add("Silahkan isi terlebih dahulu kode pos rumah tertanggung");
				//err.rejectValue("tertanggung.kd_pos_rumah", "", "Silahkan isi terlebih dahulu kode pos rumah tertanggung");
			 }
			
			String tertanggung_area_code_rumah = nasabah.getTertanggung().getArea_code_rumah();
			if (tertanggung_area_code_rumah == null)
			{
				tertanggung_area_code_rumah = "";
				nasabah.getTertanggung().setArea_code_rumah("");
			}
			if (tertanggung_area_code_rumah.equalsIgnoreCase(""))
			{
				alert.add("Jika tidak ada kode area telpon rumah tertanggung  1 harap diisi dengan tanda -");
				//err.rejectValue("tertanggung.area_code_rumah", "", "Jika tidak ada kode area telpon rumah tertanggung  1 harap diisi dengan tanda -");
			}else{
				String hsl= data.f_validasi_kode_area_rmh(tertanggung_area_code_rumah);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.area_code_rumah", "", hsl);
				}
			}
			
			String tertanggung_telpon_rumah = nasabah.getTertanggung().getTelpon_rumah();
			if (tertanggung_telpon_rumah ==null)
			{
				tertanggung_telpon_rumah ="";
				nasabah.getTertanggung().setTelpon_rumah("");
			}else{
				String hsl= data.f_validasi_telp_rmh(tertanggung_telpon_rumah);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.telpon_rumah", "", hsl);
				}
			}
			if (tertanggung_telpon_rumah.equalsIgnoreCase(""))
			{
				alert.add("Silahkan isi terlebih dahulu telpon rumah tertanggung");
				//err.rejectValue("tertanggung.telpon_rumah", "", "Silahkan isi terlebih dahulu telpon rumah tertanggung");
			}
			
			String tertanggung_area_code_rumah2 = nasabah.getTertanggung().getArea_code_rumah2();
			if (tertanggung_area_code_rumah2 == null)
			{
				tertanggung_area_code_rumah2 ="";
				nasabah.getTertanggung().setArea_code_rumah2("");
			}else{
				String hsl= data.f_validasi_kode_area_rmh(tertanggung_area_code_rumah2);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.area_code_rumah2", "", hsl);
				}
			}
			/*if (tertanggung_area_code_rumah2.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.area_code_rumah2", "", "Silahkan isi terlebih dahulu kode area rumah tertanggung 2");
			}*/
			
			String tertanggung_telpon_rumah2 = nasabah.getTertanggung().getTelpon_rumah2();
			if (tertanggung_telpon_rumah2 == null)
			{
				tertanggung_telpon_rumah2="";
				nasabah.getTertanggung().setTelpon_rumah2("");
			}else{
				String hsl= data.f_validasi_telp_rmh(tertanggung_telpon_rumah2);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.telpon_rumah2", "", hsl);
				}
			}
			/*if (tertanggung_telpon_rumah2.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.telpon_rumah2", "", "Silahkan isi terlebih dahulu telpon rumah tertanggung 2");
			}*/
			
			String tertanggung_alamat_kantor = nasabah.getTertanggung().getAlamat_kantor();
			if (tertanggung_alamat_kantor == null)
			{
				tertanggung_alamat_kantor ="";
				nasabah.getTertanggung().setAlamat_kantor("");
			}
			/*if (tertanggung_alamat_kantor.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.alamat_kantor", "", "Silahkan isi terlebih dahulu alamat kantor tertanggung");
			}*/
			
			String tertanggung_kota_kantor = nasabah.getTertanggung().getKota_kantor();
			if (tertanggung_kota_kantor == null)
			{
				tertanggung_kota_kantor = "";
				nasabah.getTertanggung().setKota_kantor("");
			}
		/*	if (tertanggung_kota_kantor.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.kota_kantor", "", "Silahkan isi terlebih dahulu kota kantor tertanggung");
			}*/
			
			String tertanggung_kd_pos_kantor = nasabah.getTertanggung().getKd_pos_kantor();
			if (tertanggung_kd_pos_kantor==null)
			{
				tertanggung_kd_pos_kantor = "";
				nasabah.getTertanggung().setKd_pos_kantor("");
			}else{
				String hsl= data.f_validasi_kode_area_ktr(tertanggung_kd_pos_kantor);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.kd_pos_kantor", "", hsl);
				}
			}
			/*if (tertanggung_kd_pos_kantor.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.kd_pos_kantor", "", "Silahkan isi terlebih dahulu kode pos kantor tertanggung");
			}*/
			
			String tertanggung_area_code_kantor = nasabah.getTertanggung().getArea_code_kantor();
			if (tertanggung_area_code_kantor == null)
			{
				tertanggung_area_code_kantor = "";
				nasabah.getTertanggung().setArea_code_kantor("");
			}else{
				String hsl= data.f_validasi_kode_area_ktr(tertanggung_area_code_kantor);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.area_code_kantor", "", hsl);
				}
			}
			/*if (tertanggung_area_code_kantor.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.area_code_kantor", "", "Silahkan isi terlebih dahulu kode area kantor tertanggung");
			}*/
			
			String tertanggung_telpon_kantor = nasabah.getTertanggung().getTelpon_kantor();
			if (tertanggung_telpon_kantor == null)
			{
				tertanggung_telpon_kantor = "";
				nasabah.getTertanggung().setTelpon_kantor("");
			}else{
				String hsl= data.f_validasi_telp_ktr(tertanggung_telpon_kantor);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.telpon_kantor", "", hsl);
				}
			}
			/*if (tertanggung_telpon_kantor.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.telpon_kantor", "", "Silahkan isi terlebih dahulu telpon kantor tertanggung");
			}*/
			
			String tertanggung_area_code_kantor2 = nasabah.getTertanggung().getArea_code_kantor2();
			if (tertanggung_area_code_kantor2 == null)
			{
				tertanggung_area_code_kantor2 ="";
				nasabah.getTertanggung().setArea_code_kantor2("");
			}else{
				String hsl= data.f_validasi_kode_area_ktr(tertanggung_area_code_kantor2);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.area_code_kantor2", "", hsl);
				}
			}
			/*if (tertanggung_area_code_kantor2.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.area_code_kantor2", "", "Silahkan isi terlebih dahulu kode area kantor tertanggung 2 ");
			}*/
			
			String tertanggung_telpon_kantor2 = nasabah.getTertanggung().getTelpon_kantor2();
			if (tertanggung_telpon_kantor2 == null)
			{
				tertanggung_telpon_kantor2 = "";
				nasabah.getTertanggung().setTelpon_kantor2("");
			}else{
				String hsl= data.f_validasi_telp_ktr(tertanggung_telpon_kantor2);	
				if (!hsl.equalsIgnoreCase(""))
				{
					alert.add(hsl);
					//err.rejectValue("tertanggung.telpon_kantor2", "", hsl);
				}
			}
		/*	if (tertanggung_telpon_kantor2.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.telpon_kantor2", "", "Silahkan isi terlebih dahulu telpon kantor tertanggung 2 ");
			}*/
			
			//Date sysdate = policyManager.selectSysdate();
			for(Keluarga keluarga_pp : nasabah.getTertanggung().getListKeluarga()) {
				//keluarga_pp.setTanggal_lahir(sysdate);
				if (keluarga_pp.getNama() == null)
				{
					keluarga_pp.setNama("");
				}
	
				if (keluarga_pp.getTanggal_lahir() == null)
				{
					keluarga_pp.setTanggal_lahir(null);
				}
			}
			
			
			String tertanggung_no_hp = nasabah.getTertanggung().getNo_hp();
			if (tertanggung_no_hp == null)
			{
				tertanggung_no_hp= "";
				nasabah.getTertanggung().setNo_hp("");
			}else{
				Boolean cekk= f_validasi.f_validasi_numerik(pemegang_no_hp2);	
				if (cekk==false)
				{
					String hsl="Silahkan masukkan No Handphone-1 tertanggung dalam bentuk numerik";
					if (!hsl.equalsIgnoreCase(""))
					{
						alert.add(hsl);
						//err.rejectValue("tertanggung.no_hp", "", hsl);
					}
				}
			}
			/*if (tertanggung_no_hp.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.no_hp", "", "Silahkan isi terlebih dahulu no hp  tertanggung 1");
			}*/
			
			String tertanggung_no_hp2 = nasabah.getTertanggung().getNo_hp2();
			if (tertanggung_no_hp2 == null)
			{
				tertanggung_no_hp2 = "";
				nasabah.getTertanggung().setNo_hp2("");
			}else{
				Boolean cekk= f_validasi.f_validasi_numerik(pemegang_no_hp2);	
				if (cekk==false)
				{
					String hsl="Silahkan masukkan No Handphone-2 tertanggung dalam bentuk numerik";
					if (!hsl.equalsIgnoreCase(""))
					{
						alert.add(hsl);
						//err.rejectValue("tertanggung.no_hp2", "", hsl);
					}
				}
			}
			/*if (tertanggung_no_hp2.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.no_hp2", "", "Silahkan isi terlebih dahulu no hp  tertanggung 2");
			}*/
			
			String tertanggung_email = nasabah.getTertanggung().getEmail();
			if (tertanggung_email == null)
			{
				tertanggung_email = "";
				nasabah.getTertanggung().setEmail("");
			}else{
				if (!tertanggung_email.equalsIgnoreCase(""))
				{
					Boolean cekk= data.f_validasi_email(tertanggung_email);	
					if (cekk==false)
					{
						String hsl="Silahkan masukkan Email tertanggung dalam bentuk numerik,alphabet,@,_";
						alert.add(hsl);
						//err.rejectValue("tertanggung.email", "", hsl);
					}
				}
			}
			/*if (tertanggung_email.equalsIgnoreCase(""))
			{
				err.rejectValue("tertanggung.email", "", "Silahkan isi terlebih dahulu email");
			}*/
			
			String tertanggung_mkl_kerja = nasabah.getTertanggung().getMkl_kerja();
			if (tertanggung_mkl_kerja == null)
			{
				tertanggung_mkl_kerja = "";
				nasabah.getTertanggung().setMkl_kerja("");
			}
			if (tertanggung_mkl_kerja.equalsIgnoreCase(""))
			{
				alert.add("Silahkan isi terlebih dahulu pekerjaan  tertanggung");
				//err.rejectValue("tertanggung.mkl_kerja", "", "Silahkan isi terlebih dahulu pekerjaan  tertanggung");
			}
			//Klasifikasi Pekerjaan
			String pekerjaan_ttg= nasabah.getTertanggung().getMkl_kerja();
			String pekerjaan_oth_ttg= nasabah.getTertanggung().getKerjaa();
			String hasil_pekerjaan_ttg="";
			if (pekerjaan_oth_ttg==null)
			{
				pekerjaan_oth_ttg="";
			}
			if (pekerjaan_ttg==null)
			{
				pekerjaan_ttg="Lainnya";
				nasabah.getTertanggung().setMkl_kerja("Lainnya");
			}
			if (pekerjaan_ttg.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
			{
				if (pekerjaan_oth_ttg==null)
				{
					pekerjaan_oth_ttg="";
				}
				if(pekerjaan_oth_ttg.trim().length()==0)
				{
					hasil_pekerjaan="Klasifikasi Pekerjaan tertanggung Lainnya, silahkan sebutkan.";
					alert.add(hasil_pekerjaan);
					//err.rejectValue("tertanggung.mkl_kerja", "", hasil_pekerjaan);

				}
				if (hasil_pekerjaan_ttg.trim().length()!=0)
				{
					nasabah.getTertanggung().setKerjaa(f_validasi.convert_karakter(pekerjaan_oth_ttg));
				}
				nasabah.getTertanggung().setKerjaa(f_validasi.convert_karakter(pekerjaan_oth_ttg));
			}else{
				nasabah.getTertanggung().setKerjaa(f_validasi.convert_karakter(""));
			}

			String jabatan_ttg=nasabah.getTertanggung().getKerjab();
			
			String hasil_jabatan_ttg="";
			if (pekerjaan_ttg.trim().equalsIgnoreCase("Karyawan"))
			{
				if (jabatan_ttg==null)
				{
					jabatan_ttg="";
				}
				if (jabatan_ttg.trim().length()==0)
				{
					hasil_jabatan="Khusus Klasifikasi Pekerjaan Karyawan wajib mengisikan jabatan tertanggung.";
					alert.add(hasil_jabatan);
					//err.rejectValue("tertanggung.mkl_kerja", "", hasil_jabatan);
				}
				if (hasil_jabatan_ttg.trim().length()!=0)
				{
					nasabah.getTertanggung().setKerjab(f_validasi.convert_karakter(jabatan_ttg));

				}
				nasabah.getTertanggung().setKerjab(f_validasi.convert_karakter(jabatan_ttg));
			}else{
				nasabah.getTertanggung().setKerjab("");
			}
			
			String tertanggung_mkl_industri = nasabah.getTertanggung().getMkl_industri();
			if (tertanggung_mkl_industri == null)
			{
				tertanggung_mkl_industri = "";
				nasabah.getTertanggung().setMkl_industri("");
			}
			if (tertanggung_mkl_industri.equalsIgnoreCase(""))
			{
				//err.rejectValue("tertanggung.mkl_industri", "", "Silahkan isi terlebih dahulu Bidang Usaha  tertanggung");
				alert.add("Silahkan isi terlebih dahulu Bidang Usaha  tertanggung");
			}
			//klasifikasi bidang industri
			String bidang_ttg= nasabah.getTertanggung().getMkl_industri();
			String bidang_oth_ttg= nasabah.getTertanggung().getIndustria();
			String hasil_bidang_ttg="";
			if (bidang_oth_ttg==null)
			{
				bidang_oth_ttg="";
			}
			if (bidang_ttg==null)
			{
				bidang_ttg = "Lainnya";
				nasabah.getTertanggung().setMkl_industri("Lainnya");
			}
			if (bidang_ttg.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
			{
				if (bidang_oth_ttg==null)
				{
					bidang_oth_ttg="";
				}
				if(bidang_oth_ttg.trim().length()==0)
				{
					hasil_bidang="Klasifikasi Bidang Industri tertanggungLainnya, silahkan sebutkan.";
					alert.add(hasil_bidang);
					//err.rejectValue("tertanggung.mkl_industri", "", hasil_bidang);

				}
				if (hasil_bidang_ttg.trim().length()!=0)
				{
					nasabah.getTertanggung().setIndustria(f_validasi.convert_karakter(bidang_oth_ttg));

				}
				nasabah.getTertanggung().setIndustria(f_validasi.convert_karakter(bidang_oth_ttg));
			}else{
				nasabah.getTertanggung().setIndustria("");
			}
			
			String tertanggung_mkl_penghasilan = nasabah.getTertanggung().getMkl_penghasilan();
			if (tertanggung_mkl_penghasilan == null)
			{
				tertanggung_mkl_penghasilan = "";
				nasabah.getTertanggung().setMkl_penghasilan("");
			}
			if (tertanggung_mkl_penghasilan.equalsIgnoreCase(""))
			{
				alert.add("Silahkan isi terlebih dahulu penghasilan per tahun  tertanggung");
				//err.rejectValue("tertanggung.mkl_penghasilan", "", "Silahkan isi terlebih dahulu penghasilan per tahun  tertanggung");
			}
			
			String tertanggung_mkl_pendanaan = nasabah.getTertanggung().getMkl_pendanaan();
			if (tertanggung_mkl_pendanaan == null)
			{
				tertanggung_mkl_pendanaan = "";
				nasabah.getPemegang().setMkl_pendanaan("");
			}
			if (tertanggung_mkl_pendanaan.equalsIgnoreCase(""))
			{
				alert.add("Silahkan isi terlebih dahulu sumber penghasilan  tertanggung");
				//err.rejectValue("tertanggung.mkl_pendanaan", "", "Silahkan isi terlebih dahulu sumber penghasilan  tertanggung");
			}
			//sumber pendanaan pembelian asuransi
			String sumber_dana_ttg= nasabah.getTertanggung().getMkl_pendanaan();
			String sumber_dana_oth_ttg= nasabah.getTertanggung().getDanaa();
			String hasil_sumber_dana_ttg="";
			if (sumber_dana_oth_ttg==null)
			{
				sumber_dana_oth_ttg="";
			}
			if (sumber_dana_ttg==null)
			{
				sumber_dana_ttg="Lainnya";
				nasabah.getTertanggung().setMkl_pendanaan("Lainnya");
			}
			if (sumber_dana_ttg.trim().toUpperCase().equalsIgnoreCase("LAINNYA"))
			{
				if (sumber_dana_oth_ttg==null)
				{
					sumber_dana_oth_ttg="";
				}
				if(sumber_dana_oth_ttg.trim().length()==0)
				{
					hasil_sumber_dana="Sumber Dana tertanggung Lainnya, silahkan dijelaskan.";
					alert.add(hasil_sumber_dana);
					//err.rejectValue("tertanggung.mkl_pendanaan", "", hasil_sumber_dana);
					
				}
				if (hasil_sumber_dana_ttg.trim().length()!=0)
				{
					nasabah.getTertanggung().setDanaa(f_validasi.convert_karakter(sumber_dana_oth_ttg));

				}
				nasabah.getTertanggung().setDanaa(f_validasi.convert_karakter(sumber_dana_oth_ttg));
			}else{
				nasabah.getTertanggung().setDanaa(f_validasi.convert_karakter(""));
			}
		}
		
		//cek list polis
		
		String mcl_id_ttg = nasabah.getTertanggung().getMcl_id();
		String nama_ttg = nasabah.getTertanggung().getMcl_first();
		Date tgl_lhr_ttg = nasabah.getTertanggung().getMspe_date_birth();
		Integer tanggal_ttg = tgl_lhr_ttg.getDate();
		Integer bulan_ttg = tgl_lhr_ttg.getMonth() + 1;
		Integer tahun_ttg = tgl_lhr_ttg.getYear() + 1900;
		String tanggal_lahir_ttg = Integer.toString(tahun_ttg) + FormatString.rpad("0",Integer.toString(bulan_ttg),2) +  FormatString.rpad("0",Integer.toString(tanggal_ttg),2);
		
		
		String mcl_id_pp = nasabah.getPemegang().getMcl_id();
		String nama_pp = nasabah.getPemegang().getMcl_first();
		Date tgl_lhr_pp = nasabah.getPemegang().getMspe_date_birth();
		Integer tanggal_pp = tgl_lhr_pp.getDate();
		Integer bulan_pp = tgl_lhr_pp.getMonth() + 1;
		Integer tahun_pp = tgl_lhr_pp.getYear() + 1900;
		String tanggal_lahir_pp = Integer.toString(tahun_pp) + FormatString.rpad("0",Integer.toString(bulan_pp),2) +  FormatString.rpad("0",Integer.toString(tanggal_pp),2);
		
		List polis1 = new ArrayList();
		polis1 = this.elionsManager.cari_polis_lain(mcl_id_pp);
		nasabah.setListPolis1(polis1);
		List polis3 = new ArrayList();
		//data pertama , data dari window utama
		Map data_polis = new HashMap();
		data_polis.put("reg_spaj",nasabah.getPemegang().getReg_spaj());
		data_polis.put("mspo_policy_no",nasabah.getDatausulan().getMspo_policy_no());
		polis3.add(data_polis);
		
		List polis2 = new ArrayList();
		
		List a1 = nasabah.getListPolisLain1();
		/*List a2 = nasabah.getListPolisLain2();
		List a3 = nasabah.getListPolisLain3();*/
		List listpolis = new ArrayList();
		
		for (int i = 0; i < a1.size() ; i++)
		{
			listpolis.add(a1.get(i));
		}
		
		/*for (int i = 0; i < a2.size() ; i++)
		{
			listpolis.add(a2.get(i));
		}
		
		for (int i = 0; i < a3.size() ; i++)
		{
			listpolis.add(a3.get(i));
		}*/
		
		
			
		Integer count_a1 = listpolis.size();
		for (int j=0;j<count_a1.intValue() ;j++)
		{
			Policy polis = (Policy) listpolis.get(j);
			String nopolis = "";
			Integer cek = new Integer(0);
			nopolis=polis.getMspo_policy_no();
			cek = polis.getCek();
			if (nopolis == null)
			{
				nopolis ="";
			}
			if (!nopolis.equalsIgnoreCase(""))
			{	
				if(cek != null)
				{
					if (cek.intValue() == 1)
					{
				
						String mcl_id_ttg_listpolis = "";
						String mcl_id_pp_listpolis="";
						String reg_spaj_listpolis="";
						Map data_listpolis= (HashMap) this.elionsManager.cari_mcl_id(nopolis);
						if (data_listpolis != null)
						{
							mcl_id_pp_listpolis = (String)data_listpolis.get("MSPO_POLICY_HOLDER");
							mcl_id_ttg_listpolis = (String)data_listpolis.get("MSTE_INSURED");
							reg_spaj_listpolis = (String) data_listpolis.get("REG_SPAJ");
						}
						if (!mcl_id_pp_listpolis.equalsIgnoreCase(mcl_id_pp))
						{
							err.rejectValue("pemegang.mspo_policy_no", "", "Polis nomor "+nopolis+" mempunyai tertanggung yang berbeda dengan polis utama. silahkan hilangkan dari daftar polis dan polis tersebut di edit dengan memilih polis tersebut pada menu utama, lalu menekan tombol PENGKINIAN/PEMBAHARUAN DATA");
						}else{
							
							if (!mcl_id_ttg_listpolis.equalsIgnoreCase(mcl_id_ttg))
							{
								//cek nama dan tanggal lahir
								/*Integer jumlah = (Integer)this.policyManager.select_count_client(nasabah.getPemegang().getMspo_policy_no().replace(".",""), nama_ttg, tanggal_lahir_ttg);
								if (jumlah == null)
								{
									jumlah= new Integer(0);
								}
								if (jumlah.intValue() == 0 )
								{*/
									err.rejectValue("pemegang.mspo_policy_no", "", "Polis nomor "+nopolis+" mempunyai tertanggung yang berbeda dengan polis utama. silahkan hilangkan dari daftar polis dan polis tersebut di edit dengan memilih polis tersebut pada menu utama, lalu menekan tombol PENGKINIAN/PEMBAHARUAN DATA");
								/*}else{
									//data berikutnya , kalau mcl_id ttg berbeda tapi sebenarnya orang yang sama 
									Map data_polis1 = new HashMap();
									data_polis1.put("reg_spaj", reg_spaj_listpolis);
									data_polis1.put("mspo_policy_no", nopolis);
									polis3.add(data_polis1);
								}*/
								
							}else{
		//						data berikutnya , kalau mcl_id ttg sama 
								Map data_polis1 = new HashMap();
								data_polis1.put("reg_spaj", reg_spaj_listpolis);
								data_polis1.put("mspo_policy_no", nopolis);
								polis3.add(data_polis1);
							}
						}
					}
				}
			}
		}
		
		Integer count_polis1 = polis1.size();
		if (count_polis1 == null)
		{
			count_polis1 = new Integer(0);
		}
		Integer count_polis3 = polis3.size();
		if (count_polis3 == null)
		{
			count_polis3 = new Integer(0);
		}
		if (count_polis1.intValue() > 0 && count_polis1.intValue()> count_polis3.intValue())
		{
			String nopolis1 = "";
			for (int i = 0 ; i < count_polis1 ; i++)
			{
				Boolean flag_cek = false;
				Map data_polis1 = (Map)polis1.get(i);
				nopolis1 = (String)  data_polis1.get("MSPO_POLICY_NO");
				for (int j = 0 ; j <count_polis3; j++)
				{
					Map data_polis3 = (Map)polis3.get(j);
					String nopolis3 = (String) data_polis3.get("mspo_policy_no");
					if (nopolis3.equals(nopolis1))
					{
						flag_cek = true;
					}
				}
				if (flag_cek == false)
				{
					Map data_polis2 = new HashMap();
					data_polis2.put("mspo_policy_no", nopolis1);
					String spaj = this.elionsManager.selectMstPolicyRegSpaj(nopolis1);
					data_polis2.put("reg_spaj", spaj);
					polis2.add(data_polis2);
				}
			}
		}
		nasabah.setAlert(alert);
		nasabah.setListPolis2(polis2);
		nasabah.setJumlah_sisa_polis(polis2.size());
		
		//		untuk Keterangan dari data yang belum lengkap sehingga bisa dilanjutkan
		if(alert.size()>0){
			if(nasabah.getCekUpdate()==0 || (nasabah.getAlasan().equals(""))){
				nasabah.setLssh_id(6);
				err.reject("", "Data Masih ada kekurangan, Silahkan isi kolom di atas untuk melanjutkan..");
			}
		}else
			nasabah.setLssh_id(1);
				
		logger.info("akhir validator");
	}

	public ElionsManager getElionsManager() {
		return elionsManager;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

}
