package com.ekalife.elions.web.rekruitment.support;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ekalife.elions.model.Kuesioner;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_validasi;

/**
 * @author HEMILDA
 * validator untuk rekruitment regional.
 */

public class RekrutRegionalvalidator implements Validator{

	private ElionsManager elionsManager;
	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public boolean supports(Class data) {
		return Kuesioner.class.isAssignableFrom(data);
	}

	 public void validate(Object cmd, Errors err) {
		Kuesioner data = (Kuesioner) cmd;
		String jenis_rekrut = data.getMku_jenis_rekrut();
		String hasil_jenis_rekrut = "";
		
		if(data.getSubmit1()!=null){//save dan tidak upload dokumen
			
			if(!jenis_rekrut.equalsIgnoreCase("1")){
			 	if(data.getMsrk_id().equalsIgnoreCase("00000")){
			 		hasil_jenis_rekrut = "Kode agent 000000 hanya boleh untuk Recruitment Dept (BOP)";
			 		err.rejectValue("msrk_id","", hasil_jenis_rekrut);
			 	}
			}
			
			if(data.getMku_tgl_berkas()!=null){
				if(data.getMku_tgl_berkas().before(elionsManager.selectSysdate(-30)) || data.getMku_tgl_berkas().after(elionsManager.selectSysdate(0))){
//						err.rejectValue("mku_tgl_berkas", "", "Tgl Terima Berkas Tdk blh lbh kecil dr "+ FormatDate.toIndonesian(elionsManager.selectSysdate(-30)) + " dan tdk blh lbh besar dr "+FormatDate.toIndonesian(elionsManager.selectSysdate(0)));
				}
			}
			if(data.getMku_tgl_berkas()==null){
//					err.rejectValue("mku_tgl_berkas", "", "Tgl Terima Berkas belum diisi, Silahkan isi tanggal terima berkas");
			}
			 
			if(data.getMku_jenis_rekrut().equalsIgnoreCase("0")){
				err.rejectValue("mku_jenis_rekrut", "", "Jenis Rekrut masih none, Silahkan pilih terlebih dahulu jenis rekrut");
			}
			 
			if(data.getMsrk_id()==null){
				data.setMsrk_id("");
			}
			if(data.getMsrk_id().equalsIgnoreCase("")){
				err.rejectValue("msrk_id", "", "Kode rekrut masih kosong, Silahkan isi kode rekrut terlebih dahulu.");
			}
			 
			if(data.getMku_rekruiter()==null){
				data.setMku_rekruiter("");
			}
			if(data.getMku_rekruiter().equalsIgnoreCase("") && data.getMsrk_id().equalsIgnoreCase("000000")){
				err.rejectValue("mku_rekruiter", "", "Nama rekrut masih kosong, Silahkan isi nama rekrut terlebih dahulu.");
			}else{
			 	data.setMku_rekruiter(f_validasi.convert_karakter(data.getMku_rekruiter()));
			 	data.setMku_rekruiter(data.getMku_rekruiter().toUpperCase());
			}
			 
			if(data.getMku_first()==null){
				data.setMku_first("");
			}
			if(data.getMku_first().equalsIgnoreCase("")){
				err.rejectValue("mku_first", "", "Nama masih kosong, Silahkan isi nama terlebih dahulu.");
			}else{
				data.setMku_first(f_validasi.convert_karakter(data.getMku_first()));
				data.setMku_first(data.getMku_first().toUpperCase());
			}
			
			if(data.getMku_agama()==null){
				data.setMku_agama("");
			}
			if(data.getMku_agama().equalsIgnoreCase("")){
				err.rejectValue("mku_agama", "", "agama masih kosong, Silahkan isi Agama terlebih dahulu.");
			}else{
				data.setMku_agama(f_validasi.convert_karakter(data.getMku_agama()));
				data.setMku_agama(data.getMku_agama().toUpperCase());
			}
			
			if(data.getMku_email()==null){
				data.setMku_email("");
			}
			if(data.getMku_email().equalsIgnoreCase("")){
//					err.rejectValue("mku_email", "", "email masih kosong, Silahkan isi Email terlebih dahulu.");
			}else{
				data.setMku_email(f_validasi.convert_karakter(data.getMku_email()));
				data.setMku_email(data.getMku_email().toUpperCase());
			}
			
			if(data.getMku_bpap()==null){
				data.setMku_bpap(0);
			}
			if(data.getMku_flag_bp()==null){
				data.setMku_flag_bp(1);
			}
			if(data.getMku_flag_bp()==0 && data.getMku_flag_bp_ket()==""){
				err.rejectValue("mku_flag_bp_ket", "", "Kolom keterangan pengalaman menjadi BP/AP masih kosong. Silahkan isi keterangan tersebut terlebih dahulu.");
			}
			
			if(data.getMku_6bln()==null){
				data.setMku_6bln(0);
			}
			
			if(data.getMku_nama_perusahaan()==""){
				data.setMku_nama_perusahaan("");
			}
			if(data.getMku_jenis_perusahaan()==""){
				data.setMku_jenis_perusahaan("");
			}
			if(data.getMku_alamat_perusahaan()==""){
				data.setMku_alamat_perusahaan("");
			}
			if(data.getMku_jabatan_perusahaan()==""){
				data.setMku_jabatan_perusahaan("");
			}
			if(data.getMku_telepon_perusahaan()==""){
				data.setMku_telepon_perusahaan("");
			}
			if(data.getMku_kodepos_perusahaan()==""){
				data.setMku_kodepos_perusahaan("");
			}
			
			if(data.getMku_str_lv_um()==""){
				data.setMku_str_lv_um("");
			}
			if(data.getMku_str_lv_bm()==""){
				data.setMku_str_lv_bm("");
			}
			if(data.getMku_str_lv_sbm()==""){
				data.setMku_str_lv_sbm("");
			}
			if(data.getMku_str_lv_rm()==""){
				data.setMku_str_lv_rm("");
			}
			 

			Integer bank_id = data.getMku_bank_id();
			if(bank_id==null){
				bank_id=0;
			}
			if(data.getMku_bank_id()==null){
//					err.rejectValue("mku_bank_id", "", "Bank masih kosong, Silahkan isi Bank terlebih dahulu.");
			}else{
				String nama_bank ="";
				Map data1 = (HashMap) this.elionsManager.select_bank2(Integer.toString(data.getMku_bank_id()) );
				if (data1!=null)
				{		
					nama_bank = (String)data1.get("BANK_NAMA");
					data.setMku_lbn_nama(nama_bank.toUpperCase());
				}
			}
			 
			if(data.getMku_acc_rekruiter()==null){
				data.setMku_acc_rekruiter("");
			}
			if(data.getMku_acc_rekruiter().equalsIgnoreCase("")){
//					err.rejectValue("mku_acc_rekruiter", "", "No Account masih kosong, Silahkan isi no account terlebih dahulu.");
			}
			 
			if(data.getMku_acc_cust()==null){
				data.setMku_acc_cust("");
			}
			if(data.getMku_acc_cust().equalsIgnoreCase("")){
				err.rejectValue("mku_acc_cust", "", "No Account Nasabah masih kosong, Silahkan isi no account nasabah terlebih dahulu.");
			}
			 
			if(data.getMku_bank_id2()==null){
				err.rejectValue("mku_bank_id2", "", "Bank Untuk Nasabah masih kosong, Silahkan isi Pilihan Bank Untuk Nasabah terlebih dahulu.");
			}
			 
			if(data.getMku_regional()==null){
				err.rejectValue("mku_regional", "", "Regional masih belum dipilih, Silahkan pilih regional terlebih dahulu.");
			}
			 
			if(data.getMku_tglkues()==null){
				err.rejectValue("mku_tglkues", "", "Tanggal Kuesioner masih kosong, Silahkan isi tanggal kuesioner terlebih dahulu.");
			}
			 
			if(data.getMku_diundang()==null){
				data.setMku_diundang("");
			}
			if(data.getMku_diundang().equalsIgnoreCase("")){
//					err.rejectValue("mku_diundang", "", "Diundang oleh masih kosong, Silahkan isi diundang oleh terlebih dahulu.");
			}else{
				data.setMku_diundang(f_validasi.convert_karakter(data.getMku_rekruiter()));
				data.setMku_diundang(data.getMku_rekruiter().toUpperCase());
			}
			 
			if(data.getMku_no_identity()==null){
				data.setMku_no_identity("");
			}
			if(data.getMku_no_identity().equalsIgnoreCase("")){
				err.rejectValue("mku_no_identity", "", "No Identitas masih kosong, Silahkan isi no identitas terlebih dahulu.");
			}else{
				data.setMku_no_identity(f_validasi.convert_karakter(data.getMku_no_identity()));
				data.setMku_no_identity(data.getMku_no_identity().toUpperCase());
			}
			 
			if(data.getMku_place_birth()==null){
				data.setMku_place_birth("");
			}
			if(data.getMku_place_birth().equalsIgnoreCase("")){
				err.rejectValue("mku_place_birth", "", "Tempat Lahir masih kosong, Silahkan isi tempat lahir terlebih dahulu.");
			}else{
				data.setMku_place_birth(f_validasi.convert_karakter(data.getMku_place_birth()));
				data.setMku_place_birth(data.getMku_place_birth().toUpperCase());
			}
			if(data.getMku_date_birth()==null){
				err.rejectValue("mku_date_birth", "", "Tanggal Lahir masih kosong, Silahkan isi tanggal lahir terlebih dahulu.");
			}
			 
			if(data.getMku_alamat()==null){
				data.setMku_alamat("");
			}
			if(data.getMku_alamat().equalsIgnoreCase("")){
				err.rejectValue("mku_alamat", "", "Alamat masih kosong, Silahkan isi alamat terlebih dahulu.");
			}else{
				if (data.getMku_alamat().trim().length()>120){
					err.rejectValue("mku_alamat", "", "Alamat terlalu panjang.");
				}else{
					data.setMku_alamat(f_validasi.convert_karakter(data.getMku_alamat()));
					data.setMku_alamat(data.getMku_alamat().toUpperCase());
			 
				}
			}
			
			if(data.getMku_kota()==null){
				data.setMku_kota("");
			}
			if (data.getMku_kota().equalsIgnoreCase("")){
				err.rejectValue("mku_kota", "", "Kota masih kosong, Silahkan isi kota terlebih dahulu.");
			}else{
				data.setMku_kota(data.getMku_kota().toUpperCase());
			}
			 
			if(data.getMku_kd_pos()==null){
				data.setMku_kd_pos("");
			}
			if(data.getMku_kd_pos().equalsIgnoreCase("")){
				err.rejectValue("mku_kd_pos", "", "Kode Pos masih kosong, Silahkan isi kode pos terlebih dahulu.");
			}else{
				boolean cekk;
				String hsl="";
				String kode_pos = data.getMku_kd_pos();
				if(kode_pos.trim().length()!=0){
					f_validasi d = new f_validasi();
					cekk= f_validasi.f_validasi_numerik(kode_pos);	
					if(cekk==false){
						hsl="Silahkan masukkan kode pos dalam bentuk numerik";
						err.rejectValue("mku_kd_pos", "", hsl);
					}
				}
			}
			 
			if(data.getMku_area_rumah()==null){
				data.setMku_area_rumah("");
			}
			if(data.getMku_area_rumah().equalsIgnoreCase("")){
				err.rejectValue("mku_area_rumah", "", "Area Rumah masih kosong, Silahkan isi Area Rumah terlebih dahulu.");
			}else{
				boolean cekk;
				String hsl="";
				String area = data.getMku_area_rumah();
				if(area.trim().length()!=0){
					f_validasi d = new f_validasi();
					cekk = f_validasi.f_validasi_numerik(area);	
					if(cekk==false){
						hsl="Silahkan masukkan kode area rumah dalam bentuk numerik";
						err.rejectValue("mku_area_rumah", "", hsl);
					}
				}
			}
			 
			if(data.getMku_tlprumah()==null){
				data.setMku_tlprumah("");
			}
			if(data.getMku_tlprumah().equalsIgnoreCase("")){
				err.rejectValue("mku_tlprumah", "", "Telpon Rumah masih kosong, Silahkan isi Telpon Rumah terlebih dahulu.");
			}else{
				boolean cekk;
				String hsl="";
				String telp = data.getMku_tlprumah();
				if(telp.trim().length()!=0){
					f_validasi d = new f_validasi();
					cekk = f_validasi.f_validasi_numerik(telp);	
					if(cekk==false){
						hsl="Silahkan masukkan telp rumah dalam bentuk numerik";
						err.rejectValue("mku_tlprumah", "", hsl);
					}else if(telp.trim().length()<6){//IGA - DCR/2020/10/265 Enhancement pengisian No. Telp, No. HP & email di menu Rekrutmen Agent
						hsl="No. Telp harus diiisi dengan minimal 6 digit angka";
						err.rejectValue("mku_tlprumah", "", hsl);
					}
				}
			}
			 
			if(data.getMku_area_kantor()==null){
				data.setMku_area_kantor("");
			}
			if(data.getJenisHalaman()==0){
				if(data.getMku_area_kantor().equalsIgnoreCase("")){
					err.rejectValue("mku_area_kantor", "", "Area Kantor masih kosong, Silahkan isi Area Kantor terlebih dahulu.");
				}else{
					boolean cekk;
					String hsl = "";
					String area = data.getMku_area_kantor();
					if(area.trim().length()!=0){
						f_validasi d = new f_validasi();
						cekk = f_validasi.f_validasi_numerik(area);	
						if(cekk==false){
							hsl="Silahkan masukkan kode area kantor dalam bentuk numerik";
							err.rejectValue("mku_area_kantor", "", hsl);
						}
					}
				}
			}
			 
			if(data.getMku_tlpkantor()==null){
				data.setMku_tlpkantor("");
			}
			if(data.getJenisHalaman()==0){
				if(data.getMku_tlpkantor().equalsIgnoreCase("")){
					err.rejectValue("mku_tlpkantor", "", "Telpon Kantor masih kosong, Silahkan isi Telpon Kantor terlebih dahulu.");
				}else{
					boolean cekk;
					String hsl = "";
					String telp = data.getMku_tlpkantor();
					if(telp.trim().length()!=0){
						f_validasi d = new f_validasi();
						cekk = f_validasi.f_validasi_numerik(telp);	
						if(cekk==false){
							hsl="Silahkan masukkan telpon kantor dalam bentuk numerik";
							err.rejectValue("mku_tlpkantor", "", hsl);
						}
					}
				}
			}
			 
			if(data.getMku_area_hp()==null){
				data.setMku_area_hp("");
			}
// 			Iga 23092020 || Update pengisian No. Telp, No. HP & email di menu Rekrutmen Agent (Wasisti)
//			if (data.getMku_area_hp().equalsIgnoreCase("")){
//				err.rejectValue("mku_area_hp", "", "Area HP masih kosong, Silahkan isi Area HP terlebih dahulu.");
//			}else{
//				boolean cekk;
//				String hsl="";
//				String area = data.getMku_area_hp();
//				if(area.trim().length()!=0){
//					f_validasi d = new f_validasi();
//					cekk = f_validasi.f_validasi_numerik(area);	
//					if(cekk==false){
//						hsl="Silahkan masukkan kode area hp dalam bentuk numerik";
//						err.rejectValue("mku_area_hp", "", hsl);
//					}
//				}
//			}
			 
			if(data.getMku_hp()==null){
				data.setMku_hp("");
			}
			if(data.getMku_hp().equalsIgnoreCase("")){
				err.rejectValue("mku_hp", "", "HP masih kosong, Silahkan isi No HP terlebih dahulu.");//IGA - DCR/2020/10/265 Enhancement pengisian No. Telp, No. HP & email di menu Rekrutmen Agent
			}else{
				boolean cekk;
				String hsl="";
				String nohp = data.getMku_hp();//IGA - DCR/2020/10/265 Enhancement pengisian No. Telp, No. HP & email di menu Rekrutmen Agent
				if(nohp.trim().length()!=0){
					f_validasi d = new f_validasi();
					cekk =f_validasi.f_validasi_numerik(nohp);//IGA - DCR/2020/10/265 Enhancement pengisian No. Telp, No. HP & email di menu Rekrutmen Agent
					if(cekk==false){
						hsl="Silahkan masukkan no hp dalam bentuk numerik";//IGA - DCR/2020/10/265 Enhancement pengisian No. Telp, No. HP & email di menu Rekrutmen Agent
						err.rejectValue("mku_hp", "", hsl);
					}else if(nohp.trim().length()<11){
						hsl="No. HP harus diiisi dengan minimal 11 digit angka";
						err.rejectValue("mku_hp", "", hsl);
					}
				}
//	 			Iga 23092020 || Update pengisian No. Telp, No. HP & email di menu Rekrutmen Agent (Wasisti)
//				String area = data.getMku_area_hp();
//				if(area.trim().length()!=0){
//					f_validasi d = new f_validasi();
//					cekk = f_validasi.f_validasi_numerik(area);	
//					if(cekk==false){
//						hsl="Silahkan masukkan kode area hp dalam bentuk numerik";
//						err.rejectValue("mku_area_hp", "", hsl);
//					}
//				}
			}
			 
			if(data.getMku_ket_pekerjaan()==null){
				data.setMku_ket_pekerjaan("");
			}else{
				data.setMku_ket_pekerjaan(f_validasi.convert_karakter(data.getMku_ket_pekerjaan()));
				data.setMku_ket_pekerjaan(data.getMku_ket_pekerjaan().toUpperCase());
			}
			 
			if(data.getMku_ket_organisasi()==null){
				data.setMku_ket_organisasi("");
			}else{
				data.setMku_ket_organisasi(f_validasi.convert_karakter(data.getMku_ket_organisasi()));
				data.setMku_ket_organisasi(data.getMku_ket_organisasi().toUpperCase());
			}
			 
			if(data.getMku_ket_tinggal()==null){
				data.setMku_ket_tinggal("");
			}else{
				data.setMku_ket_tinggal(f_validasi.convert_karakter(data.getMku_ket_tinggal()));
				data.setMku_ket_tinggal(data.getMku_ket_tinggal().toUpperCase());
			}
			 
			if(data.getMku_nama_1()==null){
				data.setMku_nama_1("");
			}else{
				data.setMku_nama_1(f_validasi.convert_karakter(data.getMku_nama_1()));
				data.setMku_nama_1(data.getMku_nama_1().toUpperCase());
			}
			 
			if(data.getMku_nama_2()==null){
				data.setMku_nama_2("");
			}else{
				data.setMku_nama_2(f_validasi.convert_karakter(data.getMku_nama_2()));
				data.setMku_nama_2(data.getMku_nama_2().toUpperCase());
			}
			 
			if(data.getMku_tlp_1()==null){
				data.setMku_tlp_1("");
			}else{
				data.setMku_tlp_1(f_validasi.convert_karakter(data.getMku_tlp_1()));
			}
	
			if(data.getMku_tlp_2()==null){
				data.setMku_tlp_2("");
			}else{
				data.setMku_tlp_2(f_validasi.convert_karakter(data.getMku_tlp_2()));
			}
			 
			 if (data.getMku_rekomendasi_nama() == null)
			 {
				 data.setMku_rekomendasi_nama("");
			 }
			 if (data.getMku_rekomendasi_nama().equalsIgnoreCase(""))
			 {
//				 err.rejectValue("mku_rekomendasi_nama", "", "Nama Rekomendasi masih kosong, Silahkan isi Nama Rekomendasi terlebih dahulu.");
			 }else{
				 data.setMku_rekomendasi_nama(f_validasi.convert_karakter(data.getMku_rekomendasi_nama()));
				 data.setMku_rekomendasi_nama(data.getMku_rekomendasi_nama().toUpperCase());
			 }
	
			data.setMku_transfer(new Integer(0));
			if((!data.getMku_first().equalsIgnoreCase("")) && (data.getMku_date_birth()!=null)){
				String nama = data.getMku_first().toUpperCase();
				Date tanggal_lahir = data.getMku_date_birth();
				Integer tgl1 = tanggal_lahir.getDate();
				Integer bln1 = tanggal_lahir.getMonth()+1;
				Integer thn1 = tanggal_lahir.getYear()+1900;
				String tgllhr = thn1 +FormatString.rpad("0",Integer.toString(bln1.intValue()),2)+FormatString.rpad("0",Integer.toString(tgl1.intValue()),2);
				Integer jumlah = (Integer) this.elionsManager.cek_kuesioner(nama, tgllhr);
				if(jumlah==null){
					jumlah= new Integer(0);
				}
				if(jumlah.intValue()!=0){
					err.rejectValue("mku_first", "", "Tidak boleh merekrut orang yang sudah pernah direkrut.");
				}
			}
			
			//MANTA
			if(data.getMku_tempatkues()==null){
				data.setMku_tempatkues("");
			}
			if(data.getMku_tempatkues().equalsIgnoreCase("")){
				err.rejectValue("mku_tempatkues", "", "Tempat Pengisian masih kosong, Silahkan isi tempat pengisian terlebih dahulu.");
			}
			
			if(data.getMku_pendidikan().equals("0")){
				if(data.getMku_pendidikan_lain().equals("")){
					err.rejectValue("mku_pendidikan_lain", "", "Kolom Pendidikan Lain masih kosong, Silahkan isi pendidikan lainnya terlebih dahulu.");
				}
			}
			
			if(data.getMku_nama_pasangan()==null){
				data.setMku_nama_pasangan("");
			}
			
			if(data.getMku_pekerjaan_pasangan()==null){
				data.setMku_pekerjaan_pasangan("");
			}
			
			if(data.getMku_tanggungan()==null){
				data.setMku_tanggungan("0");
			}
			
			if(data.getMku_status().equals("2")){
				if(data.getMku_nama_pasangan().equals("")){
					err.rejectValue("mku_nama_pasangan", "", "Nama Suami/Istri masih kosong, Silahkan isi nama Suami/Istri terlebih dahulu.");
				}
				if(data.getMku_pekerjaan_pasangan().equals("")){
					err.rejectValue("mku_pekerjaan_pasangan", "", "Pekerjaan Suami/Istri masih kosong, Silahkan isi pekerjaan Suami/Istri terlebih dahulu.");
				}
			}
			
			if(data.getMku_cabang_bank()==null){
				data.setMku_cabang_bank("");
			}
			if(data.getMku_cabang_bank().equals("")){
				err.rejectValue("mku_cabang_bank", "", "Cabang Bank Rekening masih kosong, Silahkan isi cabang bank rekening terlebih dahulu.");
			}
			
			if(data.getMku_nama_atasan()==null){
				data.setMku_nama_atasan("");
			}
			if(data.getMku_nama_atasan().equals("")){
				err.rejectValue("mku_nama_atasan", "", "Nama Atasan Langsung masih kosong, Silahkan isi nama atasan langsung terlebih dahulu.");
			}
			
			if(data.getMst_leader()==null){
				data.setMst_leader("");
			}
			if(data.getMst_leader().equals("")){
				err.rejectValue("mst_leader", "", "Kode Agen Atasan Langsung masih kosong, Silahkan isi kode agen atasan langsung terlebih dahulu.");
			}
			
			if(data.getMku_bangkrut()==null){
				err.rejectValue("mku_bangkrut", "", "Silahkan pilih salah satu jawaban dari pertanyaan Kuesioner bagian No. 4");
			}else{
				if(data.getMku_bangkrut()==1 && (data.getMku_ket_bangkrut()==null))
					err.rejectValue("mku_ket_bangkrut", "", "Kolom Keterangan Bangkrut masih kosong, Silahkan isi keterangan bangkrut terlebih dahulu.");
			}
			
			if(data.getMku_kriminal()==null){
				err.rejectValue("mku_kriminal", "", "Silahkan pilih salah satu jawaban dari pertanyaan Kuesioner bagian No. 5");
			}else{
				if(data.getMku_kriminal()==1 && (data.getMku_ket_kriminal()==null))
					err.rejectValue("mku_ket_kriminal", "", "Kolom Keterangan Kriminal masih kosong, Silahkan isi keterangan kriminal terlebih dahulu.");
			}
			
		}else if(data.getSubmit2()!=null){
			if(data.getMku_noreg()==null){
				data.setMku_noreg(null);
			}
			if(data.getMku_noreg().equals("")){
				err.rejectValue("mku_noreg", "", "No Register masih kosong, Silahkan isi no register yang dicari terlebih dahulu.");
			}
		}
		
		/*if(data.isChbox()==false){
			
		}else{//upload dokumen dan tidak save data
			if(data.getMku_no_reg_upload().equalsIgnoreCase("")){
				err.rejectValue("mku_no_reg_upload", "", "Kode rekrut untuk upload masih kosong, Silahkan isi kode rekrut terlebih dahulu.");
			}
			 
			if(data.getFile1().isEmpty() || data.getFile1()==null){
				err.rejectValue("file1", "", "File upload masih kosong, Silahkan isi file upload terlebih dahulu.");
			}
		}*/
	}
}

