package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Kuesioner implements Serializable {

	private static final long serialVersionUID = 1L;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	private String mku_regional;
	
	private String mku_str_lv1;
	private String mku_str_lv2;
	private String mku_str_lv3;
	private String mku_str_lv4;
	            
	private String mku_lbn_nama;
	
	private String status;
	private String keterangan;
	
	private String mku_flag_keagenan;
	private String mku_bank_cabang;
	private String mku_lisensi;
	private String mku_jabatan_agen;
	
	private String mkt_nama;
	private String mkt_jenis;
	private Date mkt_tgl_lahir;
	private String mkt_tempat_lahir;
	private String mkt_pendidikan;
	private Integer mkt_umur;
	private String mkt_hubungan;
	
	private List daftarTanggungan;
	private Integer juml_daftarTanggungan;
	private Integer juml;
	private Integer juml_x;
	private Integer jumlTanggungan;
	
	
	private Integer jenisHalaman;
	
	
	//tambah kolom baru di table 10/11/2012
	
	
	private String mku_acc_cust;                                                    
	private String fire_id;                                                         
	private String mku_str_lv_um;                                                   
	private String mku_str_lv_bm;                                                   
	private String mku_str_lv_sbm;                                                  
	private String mku_str_lv_rm;                                                   
	private Integer mku_jenis_cabang;                                                   
	private String mku_jenis_cabang_str;                                               
	private String mku_provinsi;                                                    
	private String mku_agama;                                                      
	private String mku_email;
	private String mku_email_personal; //IGA UPDATE EMAIL KE KOLOM MKU_EMAIL_PERSONAL
	private Integer mku_sert;                                                       
	private Integer mku_bpap;                                                       
	private Integer mku_6bln;                                                       
	private Integer mku_flag_bp;                                                    
	private String mku_flag_bp_ket;                                                 
	private String mku_nama_perusahaan;                                             
	private String mku_jenis_perusahaan;                                            
	private String mku_alamat_perusahaan;                                           
	private String mku_jabatan_perusahaan;                                          
	private String mku_telepon_perusahaan;                                          
	private String mku_kodepos_perusahaan;                                          

	                                                  
	
	private Date beg_date;                                                          
	private Date end_date;                                                          
	private Date tgl_aktif;                                                         
	private Integer flag_aktif;                                                     
	private String mcl_id;                                                          
	private String no_kontrak;                                                      
	private String msag_id;                                                         
	private Date mku_tgl_transfer_admin;                                               
	private String mku_tgl_transfer_admin_str;                                         
	private Integer mku_flag_ktp;                                                   
	private Integer mku_flag_foto;                                                  
	private Integer mku_flag_buku_rek;                                              
	private Integer mku_flag_bsb_ujian;                                             
	private String mku_no_reg;                                                
	private String mku_noreg;                                                 
	private String mku_region;                                        
	private String mku_region_nama;                                                              
	private String mku_jenis_rekrut;                                                
	private String mku_diundang;                                                    
	private String mku_first;                                                       
	private String mku_middle;                                                      
	private String mku_last;                                                        
	private String mku_sex;                                                         
	private String mku_identity_id;                                                 

	                                                  
	
	private String mku_no_identity;                                                 
	private String mku_place_birth;                                                 
	private Date mku_date_birth;                                                  
	private String mku_date_birth_str;                                                 
	private String mku_jns_alamat;                                                  
	private String mku_alamat;                                                      
	private String mku_area_rumah;                                                  
	private String mku_tlprumah;                                                    
	private String mku_area_kantor;                                                 
	private String mku_tlpkantor;                                                   
	private String mku_area_hp;                                                     
	private String mku_hp;                                                          
	private String mku_kota;                                                        
	private String mku_kd_pos;                                                      
	private String mku_usia;                                                        
	private String mku_status;                                                      
	private String mku_tanggungan;                                                  
	private String mku_pendidikan;                                                  
	private String mku_pekerjaan;                                                   
	private String mku_ket_pekerjaan;                                               
	private String mku_penghasilan;                                                 
	private String mku_pengalaman;                                                  

	                                                  
	
	private String mku_kendaraan;                                                   
	private String mku_organisasi;                                                  
	private String mku_ket_organisasi;                                              
	private String mku_tinggal;                                                     
	private String mku_ket_tinggal;                                                 
	private String mku_kenalan;                                                     
	private String mku_pernah_jual;                                                 
	private String mku_gabung_pernah;                                               
	private String mku_tenaga_ekalife;                                              
	private String mku_ingin_gabung;                                                
	private String mku_nama_1;                                                      
	private String mku_nama_2;                                                      
	private String mku_tlp_1;                                                       
	private String mku_tlp_2;                                                       
	private String mku_level_rekomendasi;                                           
	private String mku_rekomendasi_nama;                                            
	private String mku_nilai;                                                       
	private Date mku_tglkues;                                                       
	private String mku_tglkues_str;                                                       
	private Date mku_tglinput;                                                            
	private String mku_tglinput_str;                                                      
	private Date mku_tglkirim;                                                      
	private String mku_tglkirim_str;                                                      
	private String msrk_policy_no;                                                  

	                                                  
	
	private String msrk_id;                                                         
	private Integer mku_transfer;                                                   
	private String mku_rekruiter;                                                   
	private Integer mku_bank_id;                                                    
	private String mku_acc_rekruiter;                                               
	private String mku_agent;                                                       
	private Date mku_tgl_berkas;                                                     
	private String mku_tgl_berkas_str;                                                   
	private Integer lus_id;                                                         
	private Integer lbn_id;                                                         
	private String mku_agent_acc;                                                   
	private Integer mku_bank_id2; 
	
	private User currentUser;
	
	//upload
	private boolean chbox;
	private MultipartFile file1;
	private String mku_no_reg_upload;
	
	
	
	//MANTA (24/10/2014)
	
	private Integer id_dist;
	private Integer lsle_id;
	private Integer msag_flag_bm;
	private Integer msag_sbm;
	private Integer msag_gws;
	private Integer msag_mws;
	private Integer msag_tsr;
	private Integer lvl_fcd;
	private Integer msag_flag_hp;
	
	private String mku_tempatkues;
	private String mku_posisi_agen;
	private String mku_no_npwp;
	private String mku_cabang_bank;
	private String mku_pendidikan_lain;
	private String mku_nama_pasangan;
	private String mku_pekerjaan_pasangan;
	private String mku_nama_atasan;
	private String mku_posisi_atasan;
	private String mst_leader;
	private String mku_posisi_rekruter;

	private String mku_nama_experusahaan;
	private Date mku_tglmsk_experusahaan;
	private Date mku_tglklr_experusahaan;
	private Date mku_tglexp_lisensi;
	private String mku_tglmsk_experusahaan_str;
	private String mku_tglklr_experusahaan_str;
	private String mku_tglexp_lisensi_str;
	private Integer mku_bangkrut;
	private String mku_ket_bangkrut;
	private Integer mku_kriminal;
	private String mku_ket_kriminal;
	
	private Object submit1; // button submit
	private Object submit2; // button search
	private Object tools; // button tools
	private String posisi;
	

	public String getMku_region_nama() {
		return mku_region_nama;
	}

	public void setMku_region_nama(String mku_region_nama) {
		this.mku_region_nama = mku_region_nama;
	}

	public String getMku_jenis_cabang_str() {
		return mku_jenis_cabang_str;
	}

	public void setMku_jenis_cabang_str(String mku_jenis_cabang_str) {
		this.mku_jenis_cabang_str = mku_jenis_cabang_str;
	}

	public String getPosisi() {
		return posisi;
	}

	public void setPosisi(String posisi) {
		this.posisi = posisi;
	}

	public Object getTools() {
		return tools;
	}

	public void setTools(Object tools) {
		this.tools = tools;
	}

	public Object getSubmit1() {
		return submit1;
	}

	public void setSubmit1(Object submit1) {
		this.submit1 = submit1;
	}

	public Object getSubmit2() {
		return submit2;
	}

	public void setSubmit2(Object submit2) {
		this.submit2 = submit2;
	}

	public String getMku_noreg() {
		return mku_noreg;
	}

	public void setMku_noreg(String mku_noreg) {
		this.mku_noreg = mku_noreg;
	}

	public String getMku_no_reg_upload() {
		return mku_no_reg_upload;
	}

	public void setMku_no_reg_upload(String mku_no_reg_upload) {
		this.mku_no_reg_upload = mku_no_reg_upload;
	}

	public MultipartFile getFile1() {
		return file1;
	}

	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}

	public boolean isChbox() {
		return chbox;
	}

	public void setChbox(boolean chbox) {
		this.chbox = chbox;
	}

	public String getMku_acc_cust() {
		return mku_acc_cust;
	}

	public void setMku_acc_cust(String mkuAccCust) {
		mku_acc_cust = mkuAccCust;
	}

	public Integer getMku_bank_id2() {
		return mku_bank_id2;
	}

	public void setMku_bank_id2(Integer mkuBankId2) {
		mku_bank_id2 = mkuBankId2;
	}

	public Integer getLus_id() {
		return lus_id;
	}

	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}

	public Date getMku_tgl_berkas() {
		return mku_tgl_berkas;
	}
	public void setMku_tgl_berkas(Date mku_tgl_berkas) {
		this.mku_tgl_berkas = mku_tgl_berkas;
	}
	public String getMku_tgl_berkas_str() {
		return mku_tgl_berkas_str;
	}
	public void setMku_tgl_berkas_str(String mku_tgl_berkas_str) throws ParseException {
		this.mku_tgl_berkas_str = mku_tgl_berkas_str;
		if(mku_tgl_berkas_str!=null)
			setMku_tgl_berkas(df.parse(mku_tgl_berkas_str));
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMku_acc_rekruiter() {
		return mku_acc_rekruiter;
	}
	public void setMku_acc_rekruiter(String mku_acc_rekruiter) {
		this.mku_acc_rekruiter = mku_acc_rekruiter;
	}
	public String getMku_agent() {
		return mku_agent;
	}
	public void setMku_agent(String mku_agent) {
		this.mku_agent = mku_agent;
	}
	public String getMku_alamat() {
		return mku_alamat;
	}
	public void setMku_alamat(String mku_alamat) {
		this.mku_alamat = mku_alamat;
	}
	public String getMku_area_hp() {
		return mku_area_hp;
	}
	public void setMku_area_hp(String mku_area_hp) {
		this.mku_area_hp = mku_area_hp;
	}
	public String getMku_area_kantor() {
		return mku_area_kantor;
	}
	public void setMku_area_kantor(String mku_area_kantor) {
		this.mku_area_kantor = mku_area_kantor;
	}
	public String getMku_area_rumah() {
		return mku_area_rumah;
	}
	public void setMku_area_rumah(String mku_area_rumah) {
		this.mku_area_rumah = mku_area_rumah;
	}
	public Integer getMku_bank_id() {
		return mku_bank_id;
	}
	public void setMku_bank_id(Integer mku_bank_id) {
		this.mku_bank_id = mku_bank_id;
	}
	public Date getMku_date_birth() {
		return mku_date_birth;
	}
	public void setMku_date_birth(Date mku_date_birth) {
		this.mku_date_birth = mku_date_birth;
	}
	public String getMku_date_birth_str() {
		return mku_date_birth_str;
	}
	public void setMku_date_birth_str(String mku_date_birth_str) throws ParseException {
		this.mku_date_birth_str = mku_date_birth_str;
		if(mku_date_birth_str!=null)
			setMku_date_birth(df.parse(mku_date_birth_str));
	}
	public String getMku_diundang() {
		return mku_diundang;
	}
	public void setMku_diundang(String mku_diundang) {
		this.mku_diundang = mku_diundang;
	}
	public String getMku_first() {
		return mku_first;
	}
	public void setMku_first(String mku_first) {
		this.mku_first = mku_first;
	}
	public String getMku_gabung_pernah() {
		return mku_gabung_pernah;
	}
	public void setMku_gabung_pernah(String mku_gabung_pernah) {
		this.mku_gabung_pernah = mku_gabung_pernah;
	}
	public String getMku_hp() {
		return mku_hp;
	}
	public void setMku_hp(String mku_hp) {
		this.mku_hp = mku_hp;
	}
	public String getMku_identity_id() {
		return mku_identity_id;
	}
	public void setMku_identity_id(String mku_identity_id) {
		this.mku_identity_id = mku_identity_id;
	}
	public String getMku_ingin_gabung() {
		return mku_ingin_gabung;
	}
	public void setMku_ingin_gabung(String mku_ingin_gabung) {
		this.mku_ingin_gabung = mku_ingin_gabung;
	}
	public String getMku_jenis_rekrut() {
		return mku_jenis_rekrut;
	}
	public void setMku_jenis_rekrut(String mku_jenis_rekrut) {
		this.mku_jenis_rekrut = mku_jenis_rekrut;
	}
	public String getMku_jns_alamat() {
		return mku_jns_alamat;
	}
	public void setMku_jns_alamat(String mku_jns_alamat) {
		this.mku_jns_alamat = mku_jns_alamat;
	}
	public String getMku_kd_pos() {
		return mku_kd_pos;
	}
	public void setMku_kd_pos(String mku_kd_pos) {
		this.mku_kd_pos = mku_kd_pos;
	}
	public String getMku_kenalan() {
		return mku_kenalan;
	}
	public void setMku_kenalan(String mku_kenalan) {
		this.mku_kenalan = mku_kenalan;
	}
	public String getMku_kendaraan() {
		return mku_kendaraan;
	}
	public void setMku_kendaraan(String mku_kendaraan) {
		this.mku_kendaraan = mku_kendaraan;
	}
	public String getMku_ket_organisasi() {
		return mku_ket_organisasi;
	}
	public void setMku_ket_organisasi(String mku_ket_organisasi) {
		this.mku_ket_organisasi = mku_ket_organisasi;
	}
	public String getMku_ket_pekerjaan() {
		return mku_ket_pekerjaan;
	}
	public void setMku_ket_pekerjaan(String mku_ket_pekerjaan) {
		this.mku_ket_pekerjaan = mku_ket_pekerjaan;
	}
	public String getMku_ket_tinggal() {
		return mku_ket_tinggal;
	}
	public void setMku_ket_tinggal(String mku_ket_tinggal) {
		this.mku_ket_tinggal = mku_ket_tinggal;
	}
	public String getMku_kota() {
		return mku_kota;
	}
	public void setMku_kota(String mku_kota) {
		this.mku_kota = mku_kota;
	}
	public String getMku_last() {
		return mku_last;
	}
	public void setMku_last(String mku_last) {
		this.mku_last = mku_last;
	}
	public String getMku_level_rekomendasi() {
		return mku_level_rekomendasi;
	}
	public void setMku_level_rekomendasi(String mku_level_rekomendasi) {
		this.mku_level_rekomendasi = mku_level_rekomendasi;
	}
	public String getMku_middle() {
		return mku_middle;
	}
	public void setMku_middle(String mku_middle) {
		this.mku_middle = mku_middle;
	}
	public String getMku_nama_1() {
		return mku_nama_1;
	}
	public void setMku_nama_1(String mku_nama_1) {
		this.mku_nama_1 = mku_nama_1;
	}
	public String getMku_nama_2() {
		return mku_nama_2;
	}
	public void setMku_nama_2(String mku_nama_2) {
		this.mku_nama_2 = mku_nama_2;
	}
	public String getMku_nilai() {
		return mku_nilai;
	}
	public void setMku_nilai(String mku_nilai) {
		this.mku_nilai = mku_nilai;
	}
	public String getMku_no_identity() {
		return mku_no_identity;
	}
	public void setMku_no_identity(String mku_no_identity) {
		this.mku_no_identity = mku_no_identity;
	}
	public String getMku_no_reg() {
		return mku_no_reg;
	}
	public void setMku_no_reg(String mku_no_reg) {
		this.mku_no_reg = mku_no_reg;
	}
	public String getMku_organisasi() {
		return mku_organisasi;
	}
	public void setMku_organisasi(String mku_organisasi) {
		this.mku_organisasi = mku_organisasi;
	}
	public String getMku_pekerjaan() {
		return mku_pekerjaan;
	}
	public void setMku_pekerjaan(String mku_pekerjaan) {
		this.mku_pekerjaan = mku_pekerjaan;
	}
	public String getMku_pendidikan() {
		return mku_pendidikan;
	}
	public void setMku_pendidikan(String mku_pendidikan) {
		this.mku_pendidikan = mku_pendidikan;
	}
	public String getMku_pengalaman() {
		return mku_pengalaman;
	}
	public void setMku_pengalaman(String mku_pengalaman) {
		this.mku_pengalaman = mku_pengalaman;
	}
	public String getMku_penghasilan() {
		return mku_penghasilan;
	}
	public void setMku_penghasilan(String mku_penghasilan) {
		this.mku_penghasilan = mku_penghasilan;
	}
	public String getMku_pernah_jual() {
		return mku_pernah_jual;
	}
	public void setMku_pernah_jual(String mku_pernah_jual) {
		this.mku_pernah_jual = mku_pernah_jual;
	}
	public String getMku_place_birth() {
		return mku_place_birth;
	}
	public void setMku_place_birth(String mku_place_birth) {
		this.mku_place_birth = mku_place_birth;
	}
	public String getMku_region() {
		return mku_region;
	}
	public void setMku_region(String mku_region) {
		this.mku_region = mku_region;
	}
	public String getMku_rekomendasi_nama() {
		return mku_rekomendasi_nama;
	}
	public void setMku_rekomendasi_nama(String mku_rekomendasi_nama) {
		this.mku_rekomendasi_nama = mku_rekomendasi_nama;
	}
	public String getMku_rekruiter() {
		return mku_rekruiter;
	}
	public void setMku_rekruiter(String mku_rekruiter) {
		this.mku_rekruiter = mku_rekruiter;
	}
	public String getMku_str_lv1() {
		return mku_str_lv1;
	}
	public void setMku_str_lv1(String mku_str_lv1) {
		this.mku_str_lv1 = mku_str_lv1;
	}
	public String getMku_str_lv2() {
		return mku_str_lv2;
	}
	public void setMku_str_lv2(String mku_str_lv2) {
		this.mku_str_lv2 = mku_str_lv2;
	}
	public String getMku_str_lv3() {
		return mku_str_lv3;
	}
	public void setMku_str_lv3(String mku_str_lv3) {
		this.mku_str_lv3 = mku_str_lv3;
	}
	public String getMku_str_lv4() {
		return mku_str_lv4;
	}
	public void setMku_str_lv4(String mku_str_lv4) {
		this.mku_str_lv4 = mku_str_lv4;
	}
	public String getMku_sex() {
		return mku_sex;
	}
	public void setMku_sex(String mku_sex) {
		this.mku_sex = mku_sex;
	}
	public String getMku_status() {
		return mku_status;
	}
	public void setMku_status(String mku_status) {
		this.mku_status = mku_status;
	}
	public String getMku_tanggungan() {
		return mku_tanggungan;
	}
	public void setMku_tanggungan(String mku_tanggungan) {
		this.mku_tanggungan = mku_tanggungan;
	}
	public String getMku_tenaga_ekalife() {
		return mku_tenaga_ekalife;
	}
	public void setMku_tenaga_ekalife(String mku_tenaga_ekalife) {
		this.mku_tenaga_ekalife = mku_tenaga_ekalife;
	}
	public Date getMku_tglinput() {
		return mku_tglinput;
	}
	public void setMku_tglinput(Date mku_tglinput) {
		this.mku_tglinput = mku_tglinput;
	}
	public String getMku_tglinput_str() {
		return mku_tglinput_str;
	}
	public void setMku_tglinput_str(String mku_tglinput_str) throws ParseException {
		this.mku_tglinput_str = mku_tglinput_str;
		if(mku_tglinput_str!=null)
			setMku_tglinput(df.parse(mku_tglinput_str));
	}
	public Date getMku_tglkirim() {
		return mku_tglkirim;
	}
	public void setMku_tglkirim(Date mku_tglkirim) {
		this.mku_tglkirim = mku_tglkirim;
	}
	public String getMku_tglkirim_str() {
		return mku_tglkirim_str;
	}
	public void setMku_tglkirim_str(String mku_tglkirim_str) throws ParseException {
		this.mku_tglkirim_str = mku_tglkirim_str;
		if(mku_tglkirim_str!=null)
			setMku_tglkirim(df.parse(mku_tglkirim_str));
	}
	public Date getMku_tglkues() {
		return mku_tglkues;
	}
	public void setMku_tglkues(Date mku_tglkues) throws ParseException {
		/*if(mku_tglkues!=null){
			String tgl = df.format(mku_tglkues);
			mku_tglkues = df.parse(tgl);
		}*/
		this.mku_tglkues = mku_tglkues;
	}
	
	public String getMku_tglkues_str() {
		return mku_tglkues_str;
	}

	public void setMku_tglkues_str(String mku_tglkues_str) throws ParseException {
		this.mku_tglkues_str = mku_tglkues_str;
		if(mku_tglkues_str!=null)
			setMku_tglkues(df.parse(mku_tglkues_str));
	}
	public String getMku_tinggal() {
		return mku_tinggal;
	}
	public void setMku_tinggal(String mku_tinggal) {
		this.mku_tinggal = mku_tinggal;
	}
	public String getMku_tlp_1() {
		return mku_tlp_1;
	}
	public void setMku_tlp_1(String mku_tlp_1) {
		this.mku_tlp_1 = mku_tlp_1;
	}
	public String getMku_tlp_2() {
		return mku_tlp_2;
	}
	public void setMku_tlp_2(String mku_tlp_2) {
		this.mku_tlp_2 = mku_tlp_2;
	}
	public String getMku_tlpkantor() {
		return mku_tlpkantor;
	}
	public void setMku_tlpkantor(String mku_tlpkantor) {
		this.mku_tlpkantor = mku_tlpkantor;
	}
	public String getMku_tlprumah() {
		return mku_tlprumah;
	}
	public void setMku_tlprumah(String mku_tlprumah) {
		this.mku_tlprumah = mku_tlprumah;
	}
	public Integer getMku_transfer() {
		return mku_transfer;
	}
	public void setMku_transfer(Integer mku_transfer) {
		this.mku_transfer = mku_transfer;
	}
	public String getMku_usia() {
		return mku_usia;
	}
	public void setMku_usia(String mku_usia) {
		this.mku_usia = mku_usia;
	}
	public String getMsrk_id() {
		return msrk_id;
	}
	public void setMsrk_id(String msrk_id) {
		this.msrk_id = msrk_id.trim();
	}
	public String getMsrk_policy_no() {
		return msrk_policy_no;
	}
	public void setMsrk_policy_no(String msrk_policy_no) {
		this.msrk_policy_no = msrk_policy_no;
	}
	public String getMku_regional() {
		return mku_regional;
	}
	public void setMku_regional(String mku_regional) {
		this.mku_regional = mku_regional;
	}
	public String getMku_lbn_nama() {
		return mku_lbn_nama;
	}
	public void setMku_lbn_nama(String mku_lbn_nama) {
		this.mku_lbn_nama = mku_lbn_nama;
	}
	public String getMku_flag_keagenan() {
		return mku_flag_keagenan;
	}
	public void setMku_flag_keagenan(String mku_flag_keagenan) {
		this.mku_flag_keagenan = mku_flag_keagenan;
	}
	public String getMku_bank_cabang() {
		return mku_bank_cabang;
	}
	public void setMku_bank_cabang(String mku_bank_cabang) {
		this.mku_bank_cabang = mku_bank_cabang;
	}
	public String getMku_lisensi() {
		return mku_lisensi;
	}
	public void setMku_lisensi(String mku_lisensi) {
		this.mku_lisensi = mku_lisensi;
	}
	public String getMku_jabatan_agen() {
		return mku_jabatan_agen;
	}
	public void setMku_jabatan_agen(String mku_jabatan_agen) {
		this.mku_jabatan_agen = mku_jabatan_agen;
	}
	
	public String getMkt_nama() {
		return mkt_nama;
	}
	public void setMkt_nama(String mkt_nama) {
		this.mkt_nama = mkt_nama;
	}
	
	public String getMkt_jenis() {
		return mkt_jenis;
	}
	public void setMkt_jenis(String mkt_jenis) {
		this.mkt_jenis = mkt_jenis;
	}
	
	public Date getMkt_tgl_lahir() {
		return mkt_tgl_lahir;
	}
	public void setMkt_tgl_lahir(Date mkt_tgl_lahir) throws ParseException {
		if(mkt_tgl_lahir!=null){
			String tgl = df.format(mkt_tgl_lahir);
			mku_tglkues = df.parse(tgl);
		}
		this.mkt_tgl_lahir = mkt_tgl_lahir;
	}
	
	public String getMkt_tempat_lahir() {
		return mkt_tempat_lahir;
	}
	public void setMkt_tempat_lahir(String mkt_tempat_lahir) {
		this.mkt_tempat_lahir = mkt_tempat_lahir;
	}
	
	public String getMkt_pendidikan() {
		return mkt_pendidikan;
	}
	public void setMkt_pendidikan(String mkt_pendidikan) {
		this.mkt_pendidikan = mkt_pendidikan;
	}
	
	public Integer getMkt_umur() {
		return mkt_umur;
	}
	public void setMkt_umur(Integer mkt_umur) {
		this.mkt_umur = mkt_umur;
	}
	
	public List getDaftarTanggungan() {
		return daftarTanggungan;
	}
	public void setDaftarTanggungan(List daftarTanggungan) {
		this.daftarTanggungan = daftarTanggungan;
	}
	
	public Integer getJuml_daftarTanggungan() {
		return juml_daftarTanggungan;
	}
	public void setJuml_daftarTanggungan(Integer juml_daftarTanggungan) {
		this.juml_daftarTanggungan = juml_daftarTanggungan;
	}
	
	public Integer getJuml() {
		return juml;
	}
	public void setJuml(Integer juml) {
		this.juml = juml;
	}
	
	public Integer getJuml_x() {
		return juml_x;
	}
	public void setJuml_x(Integer juml_x) {
		this.juml_x = juml_x;
	}
	
	public Integer getJumlTanggungan() {
		return jumlTanggungan;
	}
	public void setJumlTanggungan(Integer jumlTanggungan) {
		this.jumlTanggungan = jumlTanggungan;
	}
	public String getMkt_hubungan() {
		return mkt_hubungan;
	}
	public void setMkt_hubungan(String mkt_hubungan) {
		this.mkt_hubungan = mkt_hubungan;
	}
	
	public String getMku_str_lv_um() {
		return mku_str_lv_um;
	}
	public void setMku_str_lv_um(String mku_str_lv_um) {
		this.mku_str_lv_um = mku_str_lv_um;
	}
	public String getMku_str_lv_bm() {
		return mku_str_lv_bm;
	}
	public void setMku_str_lv_bm(String mku_str_lv_bm) {
		this.mku_str_lv_bm = mku_str_lv_bm;
	}
	public String getMku_str_lv_sbm() {
		return mku_str_lv_sbm;
	}
	public void setMku_str_lv_sbm(String mku_str_lv_sbm) {
		this.mku_str_lv_sbm = mku_str_lv_sbm;
	}
	public String getMku_str_lv_rm() {
		return mku_str_lv_rm;
	}
	public void setMku_str_lv_rm(String mku_str_lv_rm) {
		this.mku_str_lv_rm = mku_str_lv_rm;
	}
	public Integer getMku_bpap() {
		return mku_bpap;
	}
	public void setMku_bpap(Integer mku_bpap) {
		this.mku_bpap = mku_bpap;
	}
	
	public String getMku_flag_bp_ket() {
		return mku_flag_bp_ket;
	}
	public void setMku_flag_bp_ket(String mku_flag_bp_ket) {
		this.mku_flag_bp_ket = mku_flag_bp_ket;
	}
	
	public Integer getMku_6bln() {
		return mku_6bln;
	}
	public void setMku_6bln(Integer mku_6bln) {
		this.mku_6bln = mku_6bln;
	}
	public Integer getMku_flag_bp() {
		return mku_flag_bp;
	}
	public void setMku_flag_bp(Integer mku_flag_bp) {
		this.mku_flag_bp = mku_flag_bp;
	}
	
	public String getMku_nama_perusahaan() {
		return mku_nama_perusahaan;
	}
	public void setMku_nama_perusahaan(String mku_nama_perusahaan) {
		this.mku_nama_perusahaan = mku_nama_perusahaan;
	}
	public String getMku_jenis_perusahaan() {
		return mku_jenis_perusahaan;
	}
	public void setMku_jenis_perusahaan(String mku_jenis_perusahaan) {
		this.mku_jenis_perusahaan = mku_jenis_perusahaan;
	}
	public String getMku_alamat_perusahaan() {
		return mku_alamat_perusahaan;
	}
	public void setMku_alamat_perusahaan(String mku_alamat_perusahaan) {
		this.mku_alamat_perusahaan = mku_alamat_perusahaan;
	}
	public String getMku_jabatan_perusahaan() {
		return mku_jabatan_perusahaan;
	}
	public void setMku_jabatan_perusahaan(String mku_jabatan_perusahaan) {
		this.mku_jabatan_perusahaan = mku_jabatan_perusahaan;
	}
	public String getMku_telepon_perusahaan() {
		return mku_telepon_perusahaan;
	}
	public void setMku_telepon_perusahaan(String mku_telepon_perusahaan) {
		this.mku_telepon_perusahaan = mku_telepon_perusahaan;
	}
	public String getMku_kodepos_perusahaan() {
		return mku_kodepos_perusahaan;
	}
	public void setMku_kodepos_perusahaan(String mku_kodepos_perusahaan) {
		this.mku_kodepos_perusahaan = mku_kodepos_perusahaan;
	}
	
	public String getMku_agama() {
		return mku_agama;
	}
	public void setMku_agama(String mku_agama) {
		this.mku_agama = mku_agama;
	}
	public String getMku_email() {
		return mku_email;
	}
	public void setMku_email(String mku_email) {
		this.mku_email = mku_email;
	}
	//IGA UPDATE EMAIL KE KOLOM MKU_EMAIL_PERSONAL
	public String getMku_email_personal() {
		return mku_email_personal;
	}
	public void setMku_email_personal(String mku_email_personal) {
		this.mku_email_personal = mku_email_personal;
	}//DONE
	
	public String getMku_provinsi() {
		return mku_provinsi;
	}
	public void setMku_provinsi(String mku_provinsi) {
		this.mku_provinsi = mku_provinsi;
	}
	
	public Integer getMku_sert() {
		return mku_sert;
	}
	public void setMku_sert(Integer mku_sert) {
		this.mku_sert = mku_sert;
	}
	public Integer getJenisHalaman() {
		return jenisHalaman;
	}
	public void setJenisHalaman(Integer jenisHalaman) {
		this.jenisHalaman = jenisHalaman;
	}
	
	public Integer getMku_jenis_cabang() {
		return mku_jenis_cabang;
	}
	public void setMku_jenis_cabang(Integer mku_jenis_cabang) {
		this.mku_jenis_cabang = mku_jenis_cabang;
	}
	
	public Integer getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(Integer flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public String getNo_kontrak() {
		return no_kontrak;
	}
	public void setNo_kontrak(String no_kontrak) {
		this.no_kontrak = no_kontrak;
	}
	
	public Date getTgl_aktif() {
		return tgl_aktif;
	}
	public void setTgl_aktif(Date tgl_aktif) {
		this.tgl_aktif = tgl_aktif;
	}
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the msag_id
	 */
	public String getMsag_id() {
		return msag_id;
	}

	/**
	 * @param msag_id the msag_id to set
	 */
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}

	/**
	 * @return the mku_tgl_transfer_admin
	 */
	public Date getMku_tgl_transfer_admin() {
		return mku_tgl_transfer_admin;
	}

	/**
	 * @param mku_tgl_transfer_admin the mku_tgl_transfer_admin to set
	 */
	public void setMku_tgl_transfer_admin(Date mku_tgl_transfer_admin) {
		this.mku_tgl_transfer_admin = mku_tgl_transfer_admin;
	}
	
	public String getMku_tgl_transfer_admin_str() {
		return mku_tgl_transfer_admin_str;
	}

	public void setMku_tgl_transfer_admin_str(String mku_tgl_transfer_admin_str) throws ParseException {
		this.mku_tgl_transfer_admin_str = mku_tgl_transfer_admin_str;
		if(mku_tgl_transfer_admin_str!=null)
			setMku_tgl_transfer_admin(df.parse(mku_tgl_transfer_admin_str));
	}

	/**
	 * @return the mku_flag_ktp
	 */
	public Integer getMku_flag_ktp() {
		return mku_flag_ktp;
	}

	/**
	 * @param mku_flag_ktp the mku_flag_ktp to set
	 */
	public void setMku_flag_ktp(Integer mku_flag_ktp) {
		this.mku_flag_ktp = mku_flag_ktp;
	}

	/**
	 * @return the mku_flag_foto
	 */
	public Integer getMku_flag_foto() {
		return mku_flag_foto;
	}

	/**
	 * @param mku_flag_foto the mku_flag_foto to set
	 */
	public void setMku_flag_foto(Integer mku_flag_foto) {
		this.mku_flag_foto = mku_flag_foto;
	}

	/**
	 * @return the mku_flag_buku_rek
	 */
	public Integer getMku_flag_buku_rek() {
		return mku_flag_buku_rek;
	}

	/**
	 * @param mku_flag_buku_rek the mku_flag_buku_rek to set
	 */
	public void setMku_flag_buku_rek(Integer mku_flag_buku_rek) {
		this.mku_flag_buku_rek = mku_flag_buku_rek;
	}

	/**
	 * @return the mku_flag_bsb_ujian
	 */
	public Integer getMku_flag_bsb_ujian() {
		return mku_flag_bsb_ujian;
	}

	/**
	 * @param mku_flag_bsb_ujian the mku_flag_bsb_ujian to set
	 */
	public void setMku_flag_bsb_ujian(Integer mku_flag_bsb_ujian) {
		this.mku_flag_bsb_ujian = mku_flag_bsb_ujian;
	}

	/**
	 * @return the currentUser
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the fire_id
	 */
	public String getFire_id() {
		return fire_id;
	}

	/**
	 * @param fire_id the fire_id to set
	 */
	public void setFire_id(String fire_id) {
		this.fire_id = fire_id;
	}

	/**
	 * @return the lbn_id
	 */
	public Integer getLbn_id() {
		return lbn_id;
	}

	/**
	 * @param lbn_id the lbn_id to set
	 */
	public void setLbn_id(Integer lbn_id) {
		this.lbn_id = lbn_id;
	}

	/**
	 * @return the mku_agent_acc
	 */
	public String getMku_agent_acc() {
		return mku_agent_acc;
	}

	/**
	 * @param mku_agent_acc the mku_agent_acc to set
	 */
	public void setMku_agent_acc(String mku_agent_acc) {
		this.mku_agent_acc = mku_agent_acc;
	}
	
	//MANTA

	public Integer getId_dist() {
		return id_dist;
	}

	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}

	public Integer getLsle_id() {
		return lsle_id;
	}

	public void setLsle_id(Integer lsle_id) {
		this.lsle_id = lsle_id;
	}

	public Integer getMsag_flag_bm() {
		return msag_flag_bm;
	}

	public void setMsag_flag_bm(Integer msag_flag_bm) {
		this.msag_flag_bm = msag_flag_bm;
	}

	public Integer getMsag_sbm() {
		return msag_sbm;
	}

	public void setMsag_sbm(Integer msag_sbm) {
		this.msag_sbm = msag_sbm;
	}

	public Integer getMsag_gws() {
		return msag_gws;
	}

	public void setMsag_gws(Integer msag_gws) {
		this.msag_gws = msag_gws;
	}

	public Integer getMsag_mws() {
		return msag_mws;
	}

	public void setMsag_mws(Integer msag_mws) {
		this.msag_mws = msag_mws;
	}

	public Integer getMsag_tsr() {
		return msag_tsr;
	}

	public void setMsag_tsr(Integer msag_tsr) {
		this.msag_tsr = msag_tsr;
	}

	public Integer getLvl_fcd() {
		return lvl_fcd;
	}

	public void setLvl_fcd(Integer lvl_fcd) {
		this.lvl_fcd = lvl_fcd;
	}

	public Integer getMsag_flag_hp() {
		return msag_flag_hp;
	}

	public void setMsag_flag_hp(Integer msag_flag_hp) {
		this.msag_flag_hp = msag_flag_hp;
	}

	public String getMku_tempatkues() {
		return mku_tempatkues;
	}

	public void setMku_tempatkues(String mku_tempatkues) {
		this.mku_tempatkues = mku_tempatkues;
	}

	public String getMku_posisi_agen() {
		return mku_posisi_agen;
	}

	public void setMku_posisi_agen(String mku_posisi_agen) {
		this.mku_posisi_agen = mku_posisi_agen;
	}
	
	public String getMku_no_npwp() {
		return mku_no_npwp;
	}
	
	public void setMku_no_npwp(String mku_no_npwp) {
		this.mku_no_npwp = mku_no_npwp;
	}
	
	public String getMku_cabang_bank() {
		return mku_cabang_bank;
	}
	
	public void setMku_cabang_bank(String mku_cabang_bank) {
		this.mku_cabang_bank = mku_cabang_bank;
	}

	public String getMku_pendidikan_lain() {
		return mku_pendidikan_lain;
	}

	public void setMku_pendidikan_lain(String mku_pendidikan_lain) {
		this.mku_pendidikan_lain = mku_pendidikan_lain;
	}
	
	public String getMku_nama_pasangan() {
		return mku_nama_pasangan;
	}

	public void setMku_nama_pasangan(String mku_nama_pasangan) {
		this.mku_nama_pasangan = mku_nama_pasangan;
	}

	public String getMku_pekerjaan_pasangan() {
		return mku_pekerjaan_pasangan;
	}

	public void setMku_pekerjaan_pasangan(String mku_pekerjaan_pasangan) {
		this.mku_pekerjaan_pasangan = mku_pekerjaan_pasangan;
	}
	
	public String getMku_posisi_rekruter() {
		return mku_posisi_rekruter;
	}

	public void setMku_posisi_rekruter(String mku_posisi_rekruter) {
		this.mku_posisi_rekruter = mku_posisi_rekruter;
	}

	public String getMku_nama_atasan() {
		return mku_nama_atasan;
	}

	public void setMku_nama_atasan(String mku_nama_atasan) {
		this.mku_nama_atasan = mku_nama_atasan;
	}

	public String getMst_leader() {
		return mst_leader;
	}

	public void setMst_leader(String mst_leader) {
		this.mst_leader = mst_leader.trim();
	}

	public String getMku_posisi_atasan() {
		return mku_posisi_atasan;
	}

	public void setMku_posisi_atasan(String mku_posisi_atasan) {
		this.mku_posisi_atasan = mku_posisi_atasan;
	}
	
	public String getMku_nama_experusahaan() {
		return mku_nama_experusahaan;
	}

	public void setMku_nama_experusahaan(String mku_nama_experusahaan) {
		this.mku_nama_experusahaan = mku_nama_experusahaan;
	}

	public Date getMku_tglmsk_experusahaan() {
		return mku_tglmsk_experusahaan;
	}

	public void setMku_tglmsk_experusahaan(Date mku_tglmsk_experusahaan)  {
		/*if(mku_tglmsk_experusahaan!=null){
			String tgl = df.format(mku_tglmsk_experusahaan);
			mku_tglmsk_experusahaan = df.parse(tgl);
		}*/
		this.mku_tglmsk_experusahaan = mku_tglmsk_experusahaan;
	}
	
	public String getMku_tglmsk_experusahaan_str() {
		return mku_tglmsk_experusahaan_str;
	}

	public void setMku_tglmsk_experusahaan_str(String mku_tglmsk_experusahaan_str) throws ParseException{
		this.mku_tglmsk_experusahaan_str = mku_tglmsk_experusahaan_str;
		if(mku_tglmsk_experusahaan_str!=null)
			setMku_tglmsk_experusahaan(df.parse(mku_tglmsk_experusahaan_str));
	}

	public Date getMku_tglklr_experusahaan() {
		return mku_tglklr_experusahaan;
	}

	public void setMku_tglklr_experusahaan(Date mku_tglklr_experusahaan) {
		this.mku_tglklr_experusahaan = mku_tglklr_experusahaan;
	}
	public String getMku_tglklr_experusahaan_str() {
		return mku_tglklr_experusahaan_str;
	}

	public void setMku_tglklr_experusahaan_str(String mku_tglklr_experusahaan_str) throws ParseException {
		this.mku_tglklr_experusahaan_str = mku_tglklr_experusahaan_str;
		if(mku_tglklr_experusahaan_str!=null)
			setMku_tglklr_experusahaan(df.parse(mku_tglklr_experusahaan_str));
	}

	public Date getMku_tglexp_lisensi() {
		return mku_tglexp_lisensi;
	}

	public void setMku_tglexp_lisensi(Date mku_tglexp_lisensi) {
		this.mku_tglexp_lisensi = mku_tglexp_lisensi;
	}
	public String getMku_tglexp_lisensi_str() {
		return mku_tglexp_lisensi_str;
	}

	public void setMku_tglexp_lisensi_str(String mku_tglexp_lisensi_str) throws ParseException {
		this.mku_tglexp_lisensi_str = mku_tglexp_lisensi_str;
		if(mku_tglexp_lisensi_str!=null)
			setMku_tglexp_lisensi(df.parse(mku_tglexp_lisensi_str));
	}

	public Integer getMku_bangkrut() {
		return mku_bangkrut;
	}

	public void setMku_bangkrut(Integer mku_bangkrut) {
		this.mku_bangkrut = mku_bangkrut;
	}

	public String getMku_ket_bangkrut() {
		return mku_ket_bangkrut;
	}

	public void setMku_ket_bangkrut(String mku_ket_bangkrut) {
		this.mku_ket_bangkrut = mku_ket_bangkrut;
	}

	public Integer getMku_kriminal() {
		return mku_kriminal;
	}

	public void setMku_kriminal(Integer mku_kriminal) {
		this.mku_kriminal = mku_kriminal;
	}

	public String getMku_ket_kriminal() {
		return mku_ket_kriminal;
	}

	public void setMku_ket_kriminal(String mku_ket_kriminal) {
		this.mku_ket_kriminal = mku_ket_kriminal;
	}
}
