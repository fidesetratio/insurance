package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.utils.Common;

public class Pemegang implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer lti_id;
	private String info_special_case;
	private Integer mspo_flat;
	private String keterangan_blanko;
	private String keterangan_blanko_spaj;
	private Date mspo_input_date;
	private Date mcl_tgl_input;
	private String mcl_id;                         
	private String mkl_kerja;                      
	private String kerjaa;                         
	private String kerjab;                         
	private String mkl_tujuan;                     
	private String tujuana;                        
	private String mkl_penghasilan;                
	private String mkl_pendanaan;                  
	private String danaa;    
	private String danaa2;
	private String shasil;
	private String mkl_smbr_penghasilan;
	private String mkl_industri;                   
	private String industria;                      
	private String mspe_no_identity;               
	private Integer lside_id;                      
	private String mspe_place_birth;               
	private Date mspe_date_birth;                  
	private Integer mspe_sex;                      
	private String mspe_sex2;                     
	private String mspe_sts_mrt;                   
	private Integer mspo_umur_beasiswa;            
	private String lgj_note;                       
	private String lgj_id;                         
	private Integer mspe_lama_kerja;               
	private String lside_name;                     
	private String lsne_note;                      
	private Integer lsne_id;                       
	private String mcl_gelar;                      
	private String mpn_job_desc;                   
	private String mspe_mother;                    
	private String alamat_rumah;    
	private String lspr_id;   //propinsi
	private String lska_id;   //kabupaten
	private String lskc_id;   //kecamatan
	private String lskl_id;   //kelurahan
	private String kd_pos_rumah;                   
	private String kota_rumah;                     
	private String kota_rumaha;                    
	private String area_code_rumah;                
	private String telpon_rumah;                   
	private String alamat_kantor;                  
	private String kd_pos_kantor;                  
	private String kota_kantor;                    
	private String kota_kantora;                   
	private String no_hp2;                         
	private String no_hp;                          
	private String email;                          
	private String area_code_kantor;               
	private String telpon_kantor;                  
	private String reg_spaj;                       
	private Integer mste_insured_no;               
	private Integer mste_standard;                 
	private Integer mste_age;                      
	private String lsag_name;                      
	private Integer lsag_id;                       
	private String lsed_name;                      
	private Integer lsed_id;                       
	private String ljb_note;                       
	private String ljb_id;                         
	private Integer lsre_id;                       
	private String lsre_relation;                  
	private String msag_id;                        
	private String lca_id;                         
	private String lwk_id;                         
	private String lsrg_id;                        
	private Integer lscb_id;                       
	private String mspo_policy_no;                 
	private String mspo_policy_holder;             
	private String mspo_no_blanko;                 
	private String mcl_first;
	private Date mste_tgl_recur;
	private Double mspo_under_table;
	private Date tgl_mspo_under_table;
	private Integer usiapp;
	private Double bonus_tahapan;
	private String tgl_pp;
	private Integer mspo_pribadi;
	private Integer mspo_follow_up;
	private String nama_ao;
	private String nama_leader;
	private String mspo_ao;
	private String mspo_leader;
	private Integer mspo_ref_bii;
	private Date mspo_spaj_date;
	private String kode_cbg;
	private Integer mspo_komisi_bii;
	private Integer lus_id;
	private String cbg_lus_id;
	private String status;
	private Integer indeks_halaman;
	private String area_code_rumah2;               
	private String telpon_rumah2;                  
	private String area_code_kantor2;              
	private String telpon_kantor2;                 
	private String no_fax;
	private String area_code_fax;
	private String mspo_customer;
	private String mspo_customer_nama;
	private Integer dewasa;
	private Integer mspo_age;
	private Integer mcl_jenis;
	private Integer mcl_blacklist;
	private Integer mste_dewasa;
	private Integer lssp_id;
	private String nik;
	private Double mste_pct_dplk;
	private String mcl_data_salah;
	private String mspe_email;  
	private Integer mspo_jenis_terbit;
	private Integer jumlah_cancel;
	private Integer mste_flag_guthrie;
	private Integer cek_blanko; 
	private Integer mste_spaj_asli;
	private String nama_si;
	private Integer hubungan_si;
	private Date tgllhr_si;
	private String nama_anak1;
	private Integer hubungan_anak1;
	private Date tgllhr_anak1;
	private String nama_anak2;
	private Integer hubungan_anak2;
	private Date tgllhr_anak2;
	private String nama_anak3;
	private Integer hubungan_anak3;
	private Date tgllhr_anak3;
	private String mspo_nasabah_dcif;
	private String mspo_nasabah_acc;
	private String cab_bank;                       
	private Integer valid_bank_1;                  
	private Integer valid_bank_2;
	private Integer jn_bank;
	private String pass1;
	private String pass2;
	private String pass3;
	private String password1;
	private String password2;
	private String valid_password;
	private Integer rate_1 ; 
	private Integer rate_2;
	private String no_pb;
	private String otorotor;
	private String mcl_agama;
	private String spv;
	//private String confNm;
	private String sumber_id;
	private String nama_sumber;
	private String mspo_plan_provider;
	private Integer jmlkyc;
	private Integer jmlkyc2;
	private Integer mkl_dana_from;
	private Integer mkl_hasil_from;
	private Integer mkl_smbr_hasil_from;
	private ArrayList daftarKyc;
	private ArrayList daftarKyc2;
	private Integer kyc;
	private Integer jmlDaftarKyc;
	private Integer jmlDaftarKyc2;
	private Integer mkl_sumber_premi;
	private Integer lsre_id_premi;
	private Integer mkl_red_flag;
	private Date mspo_call_date;
	private Integer flag_dis;
	private Integer flag_ws;
	private String blacklist;
	
	//inputan spaj tambahan
	private String mcl_first_alias;
	private String mcl_green_card;
	private String mcl_company_name;
	private String mkl_kerja_ket;
	private String alamat_tpt_tinggal;
	private String kota_tpt_tinggal;
	private String kd_pos_tpt_tinggal;
	private String mcl_npwp;
	private String mspo_korespondensi;
	private String mpt_no_anggaran;
	private String mpt_notaris;
	private Date mpt_tggl_anggaran;
	private String kerjaNote;
	private String mspo_id_sponsor;
	private String mspo_id_place;
	private Integer row_number;
	private Integer type_data;
	//generate format baru untuk nomor virtual account
	private String prefix_va = "####";
	private String jenis_va = "VA_LAMA";
	
	public Integer getType_data() {
		return type_data;
	}
	public void setType_data(Integer type_data) {
		this.type_data = type_data;
	}
	public Integer getRow_number() {
		return row_number;
	}
	public void setRow_number(Integer row_number) {
		this.row_number = row_number;
	}

	//inputan upload dmtm
	private Date tgl_verification_date;
	private Integer flag_free_pa;
	private String cf_job_code;
	private String cf_customer_id;
	private String cf_campaign_code;
	private Integer flag_cashback;
	//Pega Case Id
    private String pega_case_id;
	
	public String getPega_case_id() {
		return pega_case_id;
	}
	public void setPega_case_id(String pega_case_id) {
		this.pega_case_id = pega_case_id;
	}
	public String getKerjaNote() {
		return kerjaNote;
	}
	public void setKerjaNote(String kerjaNote) {
		this.kerjaNote = kerjaNote;
	}

	private String pemegang_polis;
	
	public String getPemegang_polis() {
		return pemegang_polis;
	}
	public void setPemegang_polis(String pemegang_polis) {
		this.pemegang_polis = pemegang_polis;
	}

	//data suami
	private String nama_suami;
	private Date tgl_suami;
	private Integer usia_suami;
	private String pekerjaan_suami;
	private String jabatan_suami;
	private String perusahaan_suami;
	private String bidang_suami;
	private String npwp_suami;
	private String penghasilan_suami;
	
	//data ayah
	private String nama_ayah;
	private Date tgl_ayah;
	private Integer usia_ayah;
	private String pekerjaan_ayah;
	private String jabatan_ayah;
	private String perusahaan_ayah;
	private String bidang_ayah;
	private String npwp_ayah;
	private String penghasilan_ayah;
	
	//data ibu
	private String nama_ibu;
	private Date tgl_ibu;
	private Integer usia_ibu;
	private String pekerjaan_ibu;
	private String jabatan_ibu;
	private String perusahaan_ibu;
	private String bidang_ibu;
	private String npwp_ibu;
	private String penghasilan_ibu;
	
	
	
	//untuk hardcode pendapatanBulan -->>delete begitu sudah diubah
		private String bulan_gaji;
		private String bulan_penghasilan;
		private String bulan_orangtua;
		private String bulan_usaha;
		private String bulan_usaha_note;
		private String bulan_investasi;
		private String bulan_investasi_note;
		private String bulan_laba;
		private String bulan_lainnya;
		private String bulan_lainnya_note;
		
		//untuk harcode pendapatanTahun -->> delete klo sudah diubah
		private String tahun_bonus;
		private String tahun_komisi;
		private String tahun_aset;
		private String tahun_investasi;
		private String tahun_investasi_note;
		private String tahun_hadiah;
		private String tahun_lainnya;
		private String tahun_lainnya_note;

		//untuk hardcode tujuanInvestasi -->>delete begitu sudah diubah
		private String tujuan_proteksi;
		private String tujuan_pendidikan;
		private String tujuan_investasi;
		private String tujuan_tabungan;
		private String tujuan_pensiun;
		private String tujuan_lainnya;
		private String tujuan_lainnya_note;
		
		String [] tujuanInvestasi = new String[5];
		String [] pendapatanBulan = new String[6];
		String [] pendapatanTahun = new String[5];
		
		
		
	
	
	public String getBulan_gaji() {
			return bulan_gaji;
		}
		public void setBulan_gaji(String bulan_gaji) {
			this.bulan_gaji = bulan_gaji;
		}
		public String getBulan_penghasilan() {
			return bulan_penghasilan;
		}
		public void setBulan_penghasilan(String bulan_penghasilan) {
			this.bulan_penghasilan = bulan_penghasilan;
		}
		public String getBulan_orangtua() {
			return bulan_orangtua;
		}
		public void setBulan_orangtua(String bulan_orangtua) {
			this.bulan_orangtua = bulan_orangtua;
		}
		public String getBulan_usaha() {
			return bulan_usaha;
		}
		public void setBulan_usaha(String bulan_usaha) {
			this.bulan_usaha = bulan_usaha;
		}
		public String getBulan_usaha_note() {
			return bulan_usaha_note;
		}
		public void setBulan_usaha_note(String bulan_usaha_note) {
			this.bulan_usaha_note = bulan_usaha_note;
		}
		public String getBulan_investasi() {
			return bulan_investasi;
		}
		public void setBulan_investasi(String bulan_investasi) {
			this.bulan_investasi = bulan_investasi;
		}
		public String getBulan_investasi_note() {
			return bulan_investasi_note;
		}
		public void setBulan_investasi_note(String bulan_investasi_note) {
			this.bulan_investasi_note = bulan_investasi_note;
		}
		public String getBulan_laba() {
			return bulan_laba;
		}
		public void setBulan_laba(String bulan_laba) {
			this.bulan_laba = bulan_laba;
		}
		public String getBulan_lainnya() {
			return bulan_lainnya;
		}
		public void setBulan_lainnya(String bulan_lainnya) {
			this.bulan_lainnya = bulan_lainnya;
		}
		public String getBulan_lainnya_note() {
			return bulan_lainnya_note;
		}
		public void setBulan_lainnya_note(String bulan_lainnya_note) {
			this.bulan_lainnya_note = bulan_lainnya_note;
		}
		public String getTahun_bonus() {
			return tahun_bonus;
		}
		public void setTahun_bonus(String tahun_bonus) {
			this.tahun_bonus = tahun_bonus;
		}
		public String getTahun_komisi() {
			return tahun_komisi;
		}
		public void setTahun_komisi(String tahun_komisi) {
			this.tahun_komisi = tahun_komisi;
		}
		public String getTahun_aset() {
			return tahun_aset;
		}
		public void setTahun_aset(String tahun_aset) {
			this.tahun_aset = tahun_aset;
		}
		public String getTahun_investasi() {
			return tahun_investasi;
		}
		public void setTahun_investasi(String tahun_investasi) {
			this.tahun_investasi = tahun_investasi;
		}
		public String getTahun_investasi_note() {
			return tahun_investasi_note;
		}
		public void setTahun_investasi_note(String tahun_investasi_note) {
			this.tahun_investasi_note = tahun_investasi_note;
		}
		public String getTahun_hadiah() {
			return tahun_hadiah;
		}
		public void setTahun_hadiah(String tahun_hadiah) {
			this.tahun_hadiah = tahun_hadiah;
		}
		public String getTahun_lainnya() {
			return tahun_lainnya;
		}
		public void setTahun_lainnya(String tahun_lainnya) {
			this.tahun_lainnya = tahun_lainnya;
		}
		public String getTahun_lainnya_note() {
			return tahun_lainnya_note;
		}
		public void setTahun_lainnya_note(String tahun_lainnya_note) {
			this.tahun_lainnya_note = tahun_lainnya_note;
		}
		public String getTujuan_proteksi() {
			return tujuan_proteksi;
		}
		public void setTujuan_proteksi(String tujuan_proteksi) {
			this.tujuan_proteksi = tujuan_proteksi;
		}
		public String getTujuan_pendidikan() {
			return tujuan_pendidikan;
		}
		public void setTujuan_pendidikan(String tujuan_pendidikan) {
			this.tujuan_pendidikan = tujuan_pendidikan;
		}
		public String getTujuan_investasi() {
			return tujuan_investasi;
		}
		public void setTujuan_investasi(String tujuan_investasi) {
			this.tujuan_investasi = tujuan_investasi;
		}
		public String getTujuan_tabungan() {
			return tujuan_tabungan;
		}
		public void setTujuan_tabungan(String tujuan_tabungan) {
			this.tujuan_tabungan = tujuan_tabungan;
		}
		public String getTujuan_pensiun() {
			return tujuan_pensiun;
		}
		public void setTujuan_pensiun(String tujuan_pensiun) {
			this.tujuan_pensiun = tujuan_pensiun;
		}
		public String getTujuan_lainnya() {
			return tujuan_lainnya;
		}
		public void setTujuan_lainnya(String tujuan_lainnya) {
			this.tujuan_lainnya = tujuan_lainnya;
		}
		public String getTujuan_lainnya_note() {
			return tujuan_lainnya_note;
		}
		public void setTujuan_lainnya_note(String tujuan_lainnya_note) {
			this.tujuan_lainnya_note = tujuan_lainnya_note;
		}
		public String[] getTujuanInvestasi() {
			return tujuanInvestasi;
		}
		public void setTujuanInvestasi(String[] tujuanInvestasi) {
			this.tujuanInvestasi = tujuanInvestasi;
		}
		public String[] getPendapatanBulan() {
			return pendapatanBulan;
		}
		public void setPendapatanBulan(String[] pendapatanBulan) {
			this.pendapatanBulan = pendapatanBulan;
		}
		public String[] getPendapatanTahun() {
			return pendapatanTahun;
		}
		public void setPendapatanTahun(String[] pendapatanTahun) {
			this.pendapatanTahun = pendapatanTahun;
		}
	public Date getTgl_suami() {
		return tgl_suami;
	}
	public void setTgl_suami(Date tgl_suami) {
		this.tgl_suami = tgl_suami;
	}
	public Date getTgl_ayah() {
		return tgl_ayah;
	}
	public void setTgl_ayah(Date tgl_ayah) {
		this.tgl_ayah = tgl_ayah;
	}
	public Date getTgl_ibu() {
		return tgl_ibu;
	}
	public void setTgl_ibu(Date tgl_ibu) {
		this.tgl_ibu = tgl_ibu;
	}
	
	public String getNama_suami() {
		return nama_suami;
	}
	public void setNama_suami(String nama_suami) {
		this.nama_suami = nama_suami;
	}
	public Integer getUsia_suami() {
		return usia_suami;
	}
	public void setUsia_suami(Integer usia_suami) {
		this.usia_suami = usia_suami;
	}
	public String getPekerjaan_suami() {
		return pekerjaan_suami;
	}
	public void setPekerjaan_suami(String pekerjaan_suami) {
		this.pekerjaan_suami = pekerjaan_suami;
	}
	public String getJabatan_suami() {
		return jabatan_suami;
	}
	public void setJabatan_suami(String jabatan_suami) {
		this.jabatan_suami = jabatan_suami;
	}
	public String getPerusahaan_suami() {
		return perusahaan_suami;
	}
	public void setPerusahaan_suami(String perusahaan_suami) {
		this.perusahaan_suami = perusahaan_suami;
	}
	public String getBidang_suami() {
		return bidang_suami;
	}
	public void setBidang_suami(String bidang_suami) {
		this.bidang_suami = bidang_suami;
	}
	public String getNpwp_suami() {
		return npwp_suami;
	}
	public void setNpwp_suami(String npwp_suami) {
		this.npwp_suami = npwp_suami;
	}
	public String getPenghasilan_suami() {
		return penghasilan_suami;
	}
	public void setPenghasilan_suami(String penghasilan_suami) {
		this.penghasilan_suami = penghasilan_suami;
	}
	public String getNama_ayah() {
		return nama_ayah;
	}
	public void setNama_ayah(String nama_ayah) {
		this.nama_ayah = nama_ayah;
	}
	public Integer getUsia_ayah() {
		return usia_ayah;
	}
	public void setUsia_ayah(Integer usia_ayah) {
		this.usia_ayah = usia_ayah;
	}
	public String getPekerjaan_ayah() {
		return pekerjaan_ayah;
	}
	public void setPekerjaan_ayah(String pekerjaan_ayah) {
		this.pekerjaan_ayah = pekerjaan_ayah;
	}
	public String getJabatan_ayah() {
		return jabatan_ayah;
	}
	public void setJabatan_ayah(String jabatan_ayah) {
		this.jabatan_ayah = jabatan_ayah;
	}
	public String getPerusahaan_ayah() {
		return perusahaan_ayah;
	}
	public void setPerusahaan_ayah(String perusahaan_ayah) {
		this.perusahaan_ayah = perusahaan_ayah;
	}
	public String getBidang_ayah() {
		return bidang_ayah;
	}
	public void setBidang_ayah(String bidang_ayah) {
		this.bidang_ayah = bidang_ayah;
	}
	public String getNpwp_ayah() {
		return npwp_ayah;
	}
	public void setNpwp_ayah(String npwp_ayah) {
		this.npwp_ayah = npwp_ayah;
	}
	public String getPenghasilan_ayah() {
		return penghasilan_ayah;
	}
	public void setPenghasilan_ayah(String penghasilan_ayah) {
		this.penghasilan_ayah = penghasilan_ayah;
	}
	public String getNama_ibu() {
		return nama_ibu;
	}
	public void setNama_ibu(String nama_ibu) {
		this.nama_ibu = nama_ibu;
	}
	public Integer getUsia_ibu() {
		return usia_ibu;
	}
	public void setUsia_ibu(Integer usia_ibu) {
		this.usia_ibu = usia_ibu;
	}
	public String getPekerjaan_ibu() {
		return pekerjaan_ibu;
	}
	public void setPekerjaan_ibu(String pekerjaan_ibu) {
		this.pekerjaan_ibu = pekerjaan_ibu;
	}
	public String getJabatan_ibu() {
		return jabatan_ibu;
	}
	public void setJabatan_ibu(String jabatan_ibu) {
		this.jabatan_ibu = jabatan_ibu;
	}
	public String getPerusahaan_ibu() {
		return perusahaan_ibu;
	}
	public void setPerusahaan_ibu(String perusahaan_ibu) {
		this.perusahaan_ibu = perusahaan_ibu;
	}
	public String getBidang_ibu() {
		return bidang_ibu;
	}
	public void setBidang_ibu(String bidang_ibu) {
		this.bidang_ibu = bidang_ibu;
	}
	public String getNpwp_ibu() {
		return npwp_ibu;
	}
	public void setNpwp_ibu(String npwp_ibu) {
		this.npwp_ibu = npwp_ibu;
	}
	public String getPenghasilan_ibu() {
		return penghasilan_ibu;
	}
	public void setPenghasilan_ibu(String penghasilan_ibu) {
		this.penghasilan_ibu = penghasilan_ibu;
	}

	//untuk upload data
	private Integer campaign_id;
	private Integer flag_upload;
	private Integer flag_company;
	private String company_name;
	private String company_siup;
	private String company_npwp;
	private String keyword_company;
	private Date tgl_upload;
	private Integer flag_kpr; //tambah kolom baru. helpdesk[132502] //chandra
	
    //untuk power save berhadiah
	private ArrayList<Hadiah> daftar_hadiah;
	private Integer flag_hadiah;
	private Integer unit;
	private Integer flag_standard;
	private Date mspe_no_identity_expired;
	private Integer msag_asnew;
	
	//LInk+PwrSave Bundle
	private String mspo_spaj_bundle;
	//reference ke data dmtm
	private String application_id; 
	private String reff_tm_id;
	private String mspo_no_kerjasama;
	
	//copy via ID PROPOSAL
	private String id_proposal;
	private String mspo_no_pengiriman;
	private Integer mspo_flag_spaj;
	private String lead_id;
	
	private Integer edit_pemegang;
	private String kriteria_kesalahan;
	
	public Integer getFlag_standard() {
		return flag_standard;
	}
	public void setFlag_standard(Integer flag_standard) {
		this.flag_standard = flag_standard;
	}
	public Integer getUnit() {
		return unit;
	}
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
	public ArrayList<Hadiah> getDaftar_hadiah() {
		return daftar_hadiah;
	}
	public void setDaftar_hadiah(List<Hadiah> daftar_hadiah) {
		this.daftar_hadiah = Common.serializableList(daftar_hadiah);
	}
	public Integer getFlag_hadiah() {
		return flag_hadiah;
	}
	public void setFlag_hadiah(Integer flag_hadiah) {
		this.flag_hadiah = flag_hadiah;
	}
	
	public String getMspo_plan_provider() {
		return mspo_plan_provider;
	}
	public void setMspo_plan_provider(String mspoPlanProvider) {
		mspo_plan_provider = mspoPlanProvider;
	}
	public String getNama_sumber() {
		return nama_sumber;
	}
	public void setNama_sumber(String nama_sumber) {
		this.nama_sumber = nama_sumber;
	}
	public String getSumber_id() {
		return sumber_id;
	}
	public void setSumber_id(String sumber_id) {
		this.sumber_id = sumber_id;
	}
	public String getSpv() {
		return spv;
	}
	public void setSpv(String spv) {
		this.spv = spv;
	}
	public String getMcl_agama() {
		return mcl_agama;
	}
	public void setMcl_agama(String mcl_agama) {
		this.mcl_agama = mcl_agama;
	}
	public String getOtorotor() {
		return otorotor;
	}
	public void setOtorotor(String otorotor) {
		this.otorotor = otorotor;
	}
	public Integer getMspo_flat() {
		return mspo_flat;
	}
	public void setMspo_flat(Integer mspo_flat) {
		this.mspo_flat = mspo_flat;
	}
	public String getNo_pb() {
		return no_pb;
	}
	public void setNo_pb(String no_pb) {
		this.no_pb = no_pb;
	}
	public Integer getJn_bank() {
		return jn_bank;
	}
	public void setJn_bank(Integer jn_bank) {
		this.jn_bank = jn_bank;
	}
	public Integer getRate_1() {
		return rate_1;
	}
	public void setRate_1(Integer rate_1) {
		this.rate_1 = rate_1;
	}
	public Integer getRate_2() {
		return rate_2;
	}
	public void setRate_2(Integer rate_2) {
		this.rate_2 = rate_2;
	}
	public String getValid_password() {
		return valid_password;
	}
	public void setValid_password(String valid_password) {
		this.valid_password = valid_password;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getPass1() {
		return pass1;
	}
	public void setPass1(String pass1) {
		this.pass1 = pass1;
	}
	public String getPass2() {
		return pass2;
	}
	public void setPass2(String pass2) {
		this.pass2 = pass2;
	}
	public String getCab_bank() {
		return cab_bank;
	}
	public void setCab_bank(String cab_bank) {
		this.cab_bank = cab_bank;
	}
	public Integer getValid_bank_1() {
		return valid_bank_1;
	}
	public void setValid_bank_1(Integer valid_bank_1) {
		this.valid_bank_1 = valid_bank_1;
	}
	public Integer getValid_bank_2() {
		return valid_bank_2;
	}
	public void setValid_bank_2(Integer valid_bank_2) {
		this.valid_bank_2 = valid_bank_2;
	}
	public String getMspo_nasabah_dcif() {
		return mspo_nasabah_dcif;
	}
	public void setMspo_nasabah_dcif(String mspo_nasabah_dcif) {
		this.mspo_nasabah_dcif = mspo_nasabah_dcif;
	}
	public String getAlamat_kantor() {
		return alamat_kantor;
	}
	public void setAlamat_kantor(String alamat_kantor) {
		this.alamat_kantor = alamat_kantor;
	}
	public String getAlamat_rumah() {
		return alamat_rumah;
	}
	public void setAlamat_rumah(String alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}
	
	//chandra a - 20180307
	public String getLspr_id() {
		return lspr_id;
	}
	public void setLspr_id(String lspr_id) {
		this.lspr_id = lspr_id;
	}
	public String getLska_id() {
		return lska_id;
	}
	public void setLska_id(String lska_id) {
		this.lska_id = lska_id;
	}
	public String getLskc_id() {
		return lskc_id;
	}
	public void setLskc_id(String lskc_id) {
		this.lskc_id = lskc_id;
	}
	public String getLskl_id() {
		return lskl_id;
	}
	public void setLskl_id(String lskl_id) {
		this.lskl_id = lskl_id;
	}
	//chandra a - 20180307
	
	public String getArea_code_fax() {
		return area_code_fax;
	}
	public void setArea_code_fax(String area_code_fax) {
		this.area_code_fax = area_code_fax;
	}
	public String getArea_code_kantor() {
		return area_code_kantor;
	}
	public void setArea_code_kantor(String area_code_kantor) {
		this.area_code_kantor = area_code_kantor;
	}
	public String getArea_code_kantor2() {
		return area_code_kantor2;
	}
	public void setArea_code_kantor2(String area_code_kantor2) {
		this.area_code_kantor2 = area_code_kantor2;
	}
	public String getArea_code_rumah() {
		return area_code_rumah;
	}
	public void setArea_code_rumah(String area_code_rumah) {
		this.area_code_rumah = area_code_rumah;
	}
	public String getArea_code_rumah2() {
		return area_code_rumah2;
	}
	public void setArea_code_rumah2(String area_code_rumah2) {
		this.area_code_rumah2 = area_code_rumah2;
	}
	public Double getBonus_tahapan() {
		return bonus_tahapan;
	}
	public void setBonus_tahapan(Double bonus_tahapan) {
		this.bonus_tahapan = bonus_tahapan;
	}
	public String getCbg_lus_id() {
		return cbg_lus_id;
	}
	public void setCbg_lus_id(String cbg_lus_id) {
		this.cbg_lus_id = cbg_lus_id;
	}
	public Integer getCek_blanko() {
		return cek_blanko;
	}
	public void setCek_blanko(Integer cek_blanko) {
		this.cek_blanko = cek_blanko;
	}
	public String getDanaa() {
		return danaa;
	}
	public void setDanaa(String danaa) {
		this.danaa = danaa;
	}
	public Integer getDewasa() {
		return dewasa;
	}
	public void setDewasa(Integer dewasa) {
		this.dewasa = dewasa;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getHubungan_anak1() {
		return hubungan_anak1;
	}
	public void setHubungan_anak1(Integer hubungan_anak1) {
		this.hubungan_anak1 = hubungan_anak1;
	}
	public Integer getHubungan_anak2() {
		return hubungan_anak2;
	}
	public void setHubungan_anak2(Integer hubungan_anak2) {
		this.hubungan_anak2 = hubungan_anak2;
	}
	public Integer getHubungan_anak3() {
		return hubungan_anak3;
	}
	public void setHubungan_anak3(Integer hubungan_anak3) {
		this.hubungan_anak3 = hubungan_anak3;
	}
	public Integer getHubungan_si() {
		return hubungan_si;
	}
	public void setHubungan_si(Integer hubungan_si) {
		this.hubungan_si = hubungan_si;
	}
	public Integer getIndeks_halaman() {
		return indeks_halaman;
	}
	public void setIndeks_halaman(Integer indeks_halaman) {
		this.indeks_halaman = indeks_halaman;
	}
	public String getIndustria() {
		return industria;
	}
	public void setIndustria(String industria) {
		this.industria = industria;
	}
	public Integer getJumlah_cancel() {
		return jumlah_cancel;
	}
	public void setJumlah_cancel(Integer jumlah_cancel) {
		this.jumlah_cancel = jumlah_cancel;
	}
	public String getKd_pos_kantor() {
		return kd_pos_kantor;
	}
	public void setKd_pos_kantor(String kd_pos_kantor) {
		this.kd_pos_kantor = kd_pos_kantor;
	}
	public String getKd_pos_rumah() {
		return kd_pos_rumah;
	}
	public void setKd_pos_rumah(String kd_pos_rumah) {
		this.kd_pos_rumah = kd_pos_rumah;
	}
	public String getKerjaa() {
		return kerjaa;
	}
	public void setKerjaa(String kerjaa) {
		this.kerjaa = kerjaa;
	}
	public String getKerjab() {
		return kerjab;
	}
	public void setKerjab(String kerjab) {
		this.kerjab = kerjab;
	}
	public String getKode_cbg() {
		return kode_cbg;
	}
	public void setKode_cbg(String kode_cbg) {
		this.kode_cbg = kode_cbg;
	}
	public String getKota_kantor() {
		return kota_kantor;
	}
	public void setKota_kantor(String kota_kantor) {
		this.kota_kantor = kota_kantor;
	}
	public String getKota_kantora() {
		return kota_kantora;
	}
	public void setKota_kantora(String kota_kantora) {
		this.kota_kantora = kota_kantora;
	}
	public String getKota_rumah() {
		return kota_rumah;
	}
	public void setKota_rumah(String kota_rumah) {
		this.kota_rumah = kota_rumah;
	}
	public String getKota_rumaha() {
		return kota_rumaha;
	}
	public void setKota_rumaha(String kota_rumaha) {
		this.kota_rumaha = kota_rumaha;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getLgj_id() {
		return lgj_id;
	}
	public void setLgj_id(String lgj_id) {
		this.lgj_id = lgj_id;
	}
	public String getLgj_note() {
		return lgj_note;
	}
	public void setLgj_note(String lgj_note) {
		this.lgj_note = lgj_note;
	}
	public String getLjb_id() {
		return ljb_id;
	}
	public void setLjb_id(String ljb_id) {
		this.ljb_id = ljb_id;
	}
	public String getLjb_note() {
		return ljb_note;
	}
	public void setLjb_note(String ljb_note) {
		this.ljb_note = ljb_note;
	}
	public Integer getLsag_id() {
		return lsag_id;
	}
	public void setLsag_id(Integer lsag_id) {
		this.lsag_id = lsag_id;
	}
	public String getLsag_name() {
		return lsag_name;
	}
	public void setLsag_name(String lsag_name) {
		this.lsag_name = lsag_name;
	}
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getLsed_id() {
		return lsed_id;
	}
	public void setLsed_id(Integer lsed_id) {
		this.lsed_id = lsed_id;
	}
	public String getLsed_name() {
		return lsed_name;
	}
	public void setLsed_name(String lsed_name) {
		this.lsed_name = lsed_name;
	}
	public Integer getLside_id() {
		return lside_id;
	}
	public void setLside_id(Integer lside_id) {
		this.lside_id = lside_id;
	}
	public String getLside_name() {
		return lside_name;
	}
	public void setLside_name(String lside_name) {
		this.lside_name = lside_name;
	}
	public Integer getLsne_id() {
		return lsne_id;
	}
	public void setLsne_id(Integer lsne_id) {
		this.lsne_id = lsne_id;
	}
	public String getLsne_note() {
		return lsne_note;
	}
	public void setLsne_note(String lsne_note) {
		this.lsne_note = lsne_note;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public Integer getMcl_blacklist() {
		return mcl_blacklist;
	}
	public void setMcl_blacklist(Integer mcl_blacklist) {
		this.mcl_blacklist = mcl_blacklist;
	}
	public String getMcl_data_salah() {
		return mcl_data_salah;
	}
	public void setMcl_data_salah(String mcl_data_salah) {
		this.mcl_data_salah = mcl_data_salah;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getMcl_gelar() {
		return mcl_gelar;
	}
	public void setMcl_gelar(String mcl_gelar) {
		this.mcl_gelar = mcl_gelar;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public Integer getMcl_jenis() {
		return mcl_jenis;
	}
	public void setMcl_jenis(Integer mcl_jenis) {
		this.mcl_jenis = mcl_jenis;
	}
	public Date getMcl_tgl_input() {
		return mcl_tgl_input;
	}
	public void setMcl_tgl_input(Date mcl_tgl_input) {
		this.mcl_tgl_input = mcl_tgl_input;
	}
	public String getMkl_industri() {
		return mkl_industri;
	}
	public void setMkl_industri(String mkl_industri) {
		this.mkl_industri = mkl_industri;
	}
	public String getMkl_kerja() {
		return mkl_kerja;
	}
	public void setMkl_kerja(String mkl_kerja) {
		this.mkl_kerja = mkl_kerja;
	}
	public String getMkl_pendanaan() {
		return mkl_pendanaan;
	}
	public void setMkl_pendanaan(String mkl_pendanaan) {
		this.mkl_pendanaan = mkl_pendanaan;
	}
	public String getMkl_penghasilan() {
		return mkl_penghasilan;
	}
	public void setMkl_penghasilan(String mkl_penghasilan) {
		this.mkl_penghasilan = mkl_penghasilan;
	}
	public String getMkl_tujuan() {
		return mkl_tujuan;
	}
	public void setMkl_tujuan(String mkl_tujuan) {
		this.mkl_tujuan = mkl_tujuan;
	}
	public String getMpn_job_desc() {
		return mpn_job_desc;
	}
	public void setMpn_job_desc(String mpn_job_desc) {
		this.mpn_job_desc = mpn_job_desc;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public Date getMspe_date_birth() {
		return mspe_date_birth;
	}
	public void setMspe_date_birth(Date mspe_date_birth) {
		this.mspe_date_birth = mspe_date_birth;
	}
	public String getMspe_email() {
		return mspe_email;
	}
	public void setMspe_email(String mspe_email) {
		this.mspe_email = mspe_email;
	}
	public Integer getMspe_lama_kerja() {
		return mspe_lama_kerja;
	}
	public void setMspe_lama_kerja(Integer mspe_lama_kerja) {
		this.mspe_lama_kerja = mspe_lama_kerja;
	}
	public String getMspe_mother() {
		return mspe_mother;
	}
	public void setMspe_mother(String mspe_mother) {
		this.mspe_mother = mspe_mother;
	}
	public String getMspe_no_identity() {
		return mspe_no_identity;
	}
	public void setMspe_no_identity(String mspe_no_identity) {
		this.mspe_no_identity = mspe_no_identity;
	}
	public String getMspe_place_birth() {
		return mspe_place_birth;
	}
	public void setMspe_place_birth(String mspe_place_birth) {
		this.mspe_place_birth = mspe_place_birth;
	}
	public Integer getMspe_sex() {
		return mspe_sex;
	}
	public void setMspe_sex(Integer mspe_sex) {
		this.mspe_sex = mspe_sex;
	}
	public String getMspe_sex2() {
		return mspe_sex2;
	}
	public void setMspe_sex2(String mspe_sex2) {
		this.mspe_sex2 = mspe_sex2;
	}
	public String getMspe_sts_mrt() {
		return mspe_sts_mrt;
	}
	public void setMspe_sts_mrt(String mspe_sts_mrt) {
		this.mspe_sts_mrt = mspe_sts_mrt;
	}
	public Integer getMspo_age() {
		return mspo_age;
	}
	public void setMspo_age(Integer mspo_age) {
		this.mspo_age = mspo_age;
	}
	public String getMspo_ao() {
		return mspo_ao;
	}
	public void setMspo_ao(String mspo_ao) {
		this.mspo_ao = mspo_ao;
	}
	public String getMspo_customer() {
		return mspo_customer;
	}
	public void setMspo_customer(String mspo_customer) {
		this.mspo_customer = mspo_customer;
	}
	public String getMspo_customer_nama() {
		return mspo_customer_nama;
	}
	public void setMspo_customer_nama(String mspo_customer_nama) {
		this.mspo_customer_nama = mspo_customer_nama;
	}
	public Integer getMspo_follow_up() {
		return mspo_follow_up;
	}
	public void setMspo_follow_up(Integer mspo_follow_up) {
		this.mspo_follow_up = mspo_follow_up;
	}
	public Integer getMspo_jenis_terbit() {
		return mspo_jenis_terbit;
	}
	public void setMspo_jenis_terbit(Integer mspo_jenis_terbit) {
		this.mspo_jenis_terbit = mspo_jenis_terbit;
	}
	public Integer getMspo_komisi_bii() {
		return mspo_komisi_bii;
	}
	public void setMspo_komisi_bii(Integer mspo_komisi_bii) {
		this.mspo_komisi_bii = mspo_komisi_bii;
	}
	
	public String getMspo_no_blanko() {
		return mspo_no_blanko;
	}
	public void setMspo_no_blanko(String mspo_no_blanko) {
		this.mspo_no_blanko = mspo_no_blanko;
	}
	public String getMspo_policy_holder() {
		return mspo_policy_holder;
	}
	public void setMspo_policy_holder(String mspo_policy_holder) {
		this.mspo_policy_holder = mspo_policy_holder;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Integer getMspo_pribadi() {
		return mspo_pribadi;
	}
	public void setMspo_pribadi(Integer mspo_pribadi) {
		this.mspo_pribadi = mspo_pribadi;
	}
	public Integer getMspo_ref_bii() {
		return mspo_ref_bii;
	}
	public void setMspo_ref_bii(Integer mspo_ref_bii) {
		this.mspo_ref_bii = mspo_ref_bii;
	}
	public Date getMspo_spaj_date() {
		return mspo_spaj_date;
	}
	public void setMspo_spaj_date(Date mspo_spaj_date) {
		this.mspo_spaj_date = mspo_spaj_date;
	}
	public Integer getMspo_umur_beasiswa() {
		return mspo_umur_beasiswa;
	}
	public void setMspo_umur_beasiswa(Integer mspo_umur_beasiswa) {
		this.mspo_umur_beasiswa = mspo_umur_beasiswa;
	}
	public Double getMspo_under_table() {
		return mspo_under_table;
	}
	public void setMspo_under_table(Double mspo_under_table) {
		this.mspo_under_table = mspo_under_table;
	}
	public Integer getMste_age() {
		return mste_age;
	}
	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
	}
	public Integer getMste_dewasa() {
		return mste_dewasa;
	}
	public void setMste_dewasa(Integer mste_dewasa) {
		this.mste_dewasa = mste_dewasa;
	}
	public Integer getMste_flag_guthrie() {
		return mste_flag_guthrie;
	}
	public void setMste_flag_guthrie(Integer mste_flag_guthrie) {
		this.mste_flag_guthrie = mste_flag_guthrie;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public Double getMste_pct_dplk() {
		return mste_pct_dplk;
	}
	public void setMste_pct_dplk(Double mste_pct_dplk) {
		this.mste_pct_dplk = mste_pct_dplk;
	}
	public Integer getMste_spaj_asli() {
		return mste_spaj_asli;
	}
	public void setMste_spaj_asli(Integer mste_spaj_asli) {
		this.mste_spaj_asli = mste_spaj_asli;
	}
	public Integer getMste_standard() {
		return mste_standard;
	}
	public void setMste_standard(Integer mste_standard) {
		this.mste_standard = mste_standard;
	}
	public Date getMste_tgl_recur() {
		return mste_tgl_recur;
	}
	public void setMste_tgl_recur(Date mste_tgl_recur) {
		this.mste_tgl_recur = mste_tgl_recur;
	}
	public String getNama_anak1() {
		return nama_anak1;
	}
	public void setNama_anak1(String nama_anak1) {
		this.nama_anak1 = nama_anak1;
	}
	public String getNama_anak2() {
		return nama_anak2;
	}
	public void setNama_anak2(String nama_anak2) {
		this.nama_anak2 = nama_anak2;
	}
	public String getNama_anak3() {
		return nama_anak3;
	}
	public void setNama_anak3(String nama_anak3) {
		this.nama_anak3 = nama_anak3;
	}
	public String getNama_ao() {
		return nama_ao;
	}
	public void setNama_ao(String nama_ao) {
		this.nama_ao = nama_ao;
	}
	public String getNama_si() {
		return nama_si;
	}
	public void setNama_si(String nama_si) {
		this.nama_si = nama_si;
	}
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
	public String getNo_fax() {
		return no_fax;
	}
	public void setNo_fax(String no_fax) {
		this.no_fax = no_fax;
	}
	public String getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}
	public String getNo_hp2() {
		return no_hp2;
	}
	public void setNo_hp2(String no_hp2) {
		this.no_hp2 = no_hp2;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTelpon_kantor() {
		return telpon_kantor;
	}
	public void setTelpon_kantor(String telpon_kantor) {
		this.telpon_kantor = telpon_kantor;
	}
	public String getTelpon_kantor2() {
		return telpon_kantor2;
	}
	public void setTelpon_kantor2(String telpon_kantor2) {
		this.telpon_kantor2 = telpon_kantor2;
	}
	public String getTelpon_rumah() {
		return telpon_rumah;
	}
	public void setTelpon_rumah(String telpon_rumah) {
		this.telpon_rumah = telpon_rumah;
	}
	public String getTelpon_rumah2() {
		return telpon_rumah2;
	}
	public void setTelpon_rumah2(String telpon_rumah2) {
		this.telpon_rumah2 = telpon_rumah2;
	}
	public Date getTgl_mspo_under_table() {
		return tgl_mspo_under_table;
	}
	public void setTgl_mspo_under_table(Date tgl_mspo_under_table) {
		this.tgl_mspo_under_table = tgl_mspo_under_table;
	}
	public String getTgl_pp() {
		return tgl_pp;
	}
	public void setTgl_pp(String tgl_pp) {
		this.tgl_pp = tgl_pp;
	}
	public Date getTgllhr_anak1() {
		return tgllhr_anak1;
	}
	public void setTgllhr_anak1(Date tgllhr_anak1) {
		this.tgllhr_anak1 = tgllhr_anak1;
	}
	public Date getTgllhr_anak2() {
		return tgllhr_anak2;
	}
	public void setTgllhr_anak2(Date tgllhr_anak2) {
		this.tgllhr_anak2 = tgllhr_anak2;
	}
	public Date getTgllhr_anak3() {
		return tgllhr_anak3;
	}
	public void setTgllhr_anak3(Date tgllhr_anak3) {
		this.tgllhr_anak3 = tgllhr_anak3;
	}
	public Date getTgllhr_si() {
		return tgllhr_si;
	}
	public void setTgllhr_si(Date tgllhr_si) {
		this.tgllhr_si = tgllhr_si;
	}
	public String getTujuana() {
		return tujuana;
	}
	public void setTujuana(String tujuana) {
		this.tujuana = tujuana;
	}
	public Integer getUsiapp() {
		return usiapp;
	}
	public void setUsiapp(Integer usiapp) {
		this.usiapp = usiapp;
	}
	public String getKeterangan_blanko() {
		return keterangan_blanko;
	}
	public void setKeterangan_blanko(String keterangan_blanko) {
		this.keterangan_blanko = keterangan_blanko;
	}
	public Date getMspo_input_date() {
		return mspo_input_date;
	}
	public void setMspo_input_date(Date mspo_input_date) {
		this.mspo_input_date = mspo_input_date;
	}
	public String getMkl_smbr_penghasilan() {
		return mkl_smbr_penghasilan;
	}
	public void setMkl_smbr_penghasilan(String mkl_smbr_penghasilan) {
		this.mkl_smbr_penghasilan = mkl_smbr_penghasilan;
	}
	public String getDanaa2() {
		return danaa2;
	}
	public void setDanaa2(String danaa2) {
		this.danaa2 = danaa2;
	}
	public String getShasil(){
		return shasil;
	}
	public void setShasil(String shasil){
		this.shasil = shasil;
	}
	
	public String getInfo_special_case() {
		return info_special_case;
	}
	public void setInfo_special_case(String info_special_case) {
		this.info_special_case = info_special_case;
	}
	public String getKeterangan_blanko_spaj() {
		return keterangan_blanko_spaj;
	}
	public void setKeterangan_blanko_spaj(String keterangan_blanko_spaj) {
		this.keterangan_blanko_spaj = keterangan_blanko_spaj;
	}
	public Integer getLti_id() {
		return lti_id;
	}
	public void setLti_id(Integer lti_id) {
		this.lti_id = lti_id;
	}
	public String getMspo_nasabah_acc() {
		return mspo_nasabah_acc;
	}
	public void setMspo_nasabah_acc(String mspo_nasabah_acc) {
		this.mspo_nasabah_acc = mspo_nasabah_acc;
	}
	public String getPass3() {
		return pass3;
	}
	public void setPass3(String pass3) {
		this.pass3 = pass3;
	}
	/*public String getConfNm() {
		return confNm;
	}
	public void setConfNm(String confNm) {
		this.confNm = confNm;
	}*/
	public String getMspo_leader() {
		return mspo_leader;
	}
	public void setMspo_leader(String mspo_leader) {
		this.mspo_leader = mspo_leader;
	}
	public String getNama_leader() {
		return nama_leader;
	}
	public void setNama_leader(String nama_leader) {
		this.nama_leader = nama_leader;
	}
	public Integer getJmlkyc(){
		return jmlkyc;
	}
	public void setJmlkyc(Integer jmlkyc){
		this.jmlkyc = jmlkyc;
	}
	public Integer getJmlkyc2(){
		return jmlkyc2;
	}
	public void setJmlkyc2(Integer jmlkyc2){
		this.jmlkyc2 = jmlkyc2;
	}
	public Integer getMkl_dana_from(){
		return mkl_dana_from;
	}
	public void setMkl_dana_from(Integer mkl_dana_from){
		this.mkl_dana_from = mkl_dana_from;
	}
	public Integer getMkl_hasil_from(){
		return mkl_hasil_from;
	}
	public void setMkl_hasil_from(Integer mkl_hasil_from){
		this.mkl_hasil_from = mkl_hasil_from;
	}
	public Integer getMkl_smbr_hasil_from(){
		return mkl_smbr_hasil_from;
	}
	public void setMkl_smbr_hasil_from(Integer mkl_smbr_hasil_from){
		this.mkl_smbr_hasil_from = mkl_smbr_hasil_from;
	}
	public ArrayList getDaftarKyc(){
		return daftarKyc;
	}
	public void setDaftarKyc(List daftarKyc){
		this.daftarKyc = Common.serializableList(daftarKyc);
	}
	public ArrayList getDaftarKyc2(){
		return daftarKyc2;
	}
	public void setDaftarKyc2(List daftarKyc2){
		this.daftarKyc2 = Common.serializableList(daftarKyc2);
	}
	public Integer getKyc(){
		return kyc;
	}
	public void setKyc(Integer kyc){
		this.kyc = kyc;
	}
	public Integer getJmlDaftarKyc(){
		return jmlDaftarKyc;
	}
	public void setJmlDaftarKyc(Integer jmlDaftarKyc){
		this.jmlDaftarKyc = jmlDaftarKyc;
	}
	public Integer getJmlDaftarKyc2(){
		return jmlDaftarKyc2;
	}
	public void setJmlDaftarKyc2(Integer jmlDaftarKyc2){
		this.jmlDaftarKyc2 = jmlDaftarKyc2;
	}
	public Integer getMkl_sumber_premi(){
		return mkl_sumber_premi;
	}
	public void setMkl_sumber_premi(Integer mkl_sumber_premi){
		this.mkl_sumber_premi = mkl_sumber_premi;
	}
	/**
	 * @return the lsre_id_premi
	 */
	public Integer getLsre_id_premi() {
		return lsre_id_premi;
	}
	/**
	 * @param lsre_id_premi the lsre_id_premi to set
	 */
	public void setLsre_id_premi(Integer lsre_id_premi) {
		this.lsre_id_premi = lsre_id_premi;
	}
	
	public Integer getMkl_red_flag() {
		return mkl_red_flag;
	}
	public void setMkl_red_flag(Integer mkl_red_flag) {
		this.mkl_red_flag = mkl_red_flag;
	}
	
	public Date getMspo_call_date() {
		return mspo_call_date;
	}
	public void setMspo_call_date(Date mspo_call_date) {
		this.mspo_call_date = mspo_call_date;
	}
	public Integer getFlag_ws() {
		return flag_ws;
	}
	public void setFlag_ws(Integer flag_ws) {
		this.flag_ws = flag_ws;
	}
	public Integer getFlag_dis() {
		return flag_dis;
	}
	public void setFlag_dis(Integer flag_dis) {
		this.flag_dis = flag_dis;
	}
	public Integer getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(Integer campaign_id) {
		this.campaign_id = campaign_id;
	}
	public Integer getFlag_company() {
		return flag_company;
	}
	public void setFlag_company(Integer flag_company) {
		this.flag_company = flag_company;
	}
	public Integer getFlag_kpr() {
		return flag_kpr;
	}
	public void setFlag_kpr(Integer flag_kpr) {
		this.flag_kpr = flag_kpr;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_siup() {
		return company_siup;
	}
	public void setCompany_siup(String company_siup) {
		this.company_siup = company_siup;
	}
	public String getCompany_npwp() {
		return company_npwp;
	}
	public void setCompany_npwp(String company_npwp) {
		this.company_npwp = company_npwp;
	}
	public String getKeyword_company() {
		return keyword_company;
	}
	public void setKeyword_company(String keyword_company) {
		this.keyword_company = keyword_company;
	}
	public Date getTgl_upload() {
		return tgl_upload;
	}
	public void setTgl_upload(Date tgl_upload) {
		this.tgl_upload = tgl_upload;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getFlag_upload() {
		return flag_upload;
	}
	public void setFlag_upload(Integer flag_upload) {
		this.flag_upload = flag_upload;
	}
	public Date getMspe_no_identity_expired() {
		return mspe_no_identity_expired;
	}
	public void setMspe_no_identity_expired(Date mspe_no_identity_expired) {
		this.mspe_no_identity_expired = mspe_no_identity_expired;
	}
	
	public String getMspo_spaj_bundle() {
		return mspo_spaj_bundle;
	}
	public void setMspo_spaj_bundle(String mspo_spaj_bundle) {
		this.mspo_spaj_bundle = mspo_spaj_bundle;
	}
	public Integer getMsag_asnew() {
		return msag_asnew;
	}
	public void setMsag_asnew(Integer msag_asnew) {
		this.msag_asnew = msag_asnew;
	}
	public String getApplication_id() {
		return application_id;
	}
	public void setApplication_id(String application_id) {
		this.application_id = application_id;
	}
	public String getReff_tm_id() {
		return reff_tm_id;
	}
	public void setReff_tm_id(String reff_tm_id) {
		this.reff_tm_id = reff_tm_id;
	}
	public String getId_proposal() {
		return id_proposal;
	}
	public void setId_proposal(String id_proposal) {
		this.id_proposal = id_proposal;
	}
	public String getMspo_no_pengiriman() {
		return mspo_no_pengiriman;
	}
	public void setMspo_no_pengiriman(String mspo_no_pengiriman) {
		this.mspo_no_pengiriman = mspo_no_pengiriman;
	}
	public Integer getMspo_flag_spaj() {
		return mspo_flag_spaj;
	}
	public void setMspo_flag_spaj(Integer mspo_flag_spaj) {
		this.mspo_flag_spaj = mspo_flag_spaj;
	}
	public Integer getEdit_pemegang() {
		return edit_pemegang;
	}
	public void setEdit_pemegang(Integer edit_pemegang) {
		this.edit_pemegang = edit_pemegang;
	}
	public String getKriteria_kesalahan() {
		return kriteria_kesalahan;
	}
	public void setKriteria_kesalahan(String kriteria_kesalahan) {
		this.kriteria_kesalahan = kriteria_kesalahan;
	}
	public String getMcl_first_alias() {
		return mcl_first_alias;
	}
	public void setMcl_first_alias(String mcl_first_alias) {
		this.mcl_first_alias = mcl_first_alias;
	}
	public String getMcl_green_card() {
		return mcl_green_card;
	}
	public void setMcl_green_card(String mcl_green_card) {
		this.mcl_green_card = mcl_green_card;
	}
	public String getMcl_company_name() {
		return mcl_company_name;
	}
	public void setMcl_company_name(String mcl_company_name) {
		this.mcl_company_name = mcl_company_name;
	}
	public String getMkl_kerja_ket() {
		return mkl_kerja_ket;
	}
	public void setMkl_kerja_ket(String mkl_kerja_ket) {
		this.mkl_kerja_ket = mkl_kerja_ket;
	}

	public String getAlamat_tpt_tinggal() {
		return alamat_tpt_tinggal;
	}
	public void setAlamat_tpt_tinggal(String alamat_tpt_tinggal) {
		this.alamat_tpt_tinggal = alamat_tpt_tinggal;
	}

	public String getKota_tpt_tinggal() {
		return kota_tpt_tinggal;
	}
	public void setKota_tpt_tinggal(String kota_tpt_tinggal) {
		this.kota_tpt_tinggal = kota_tpt_tinggal;
	}
	public String getKd_pos_tpt_tinggal() {
		return kd_pos_tpt_tinggal;
	}
	public void setKd_pos_tpt_tinggal(String kd_pos_tpt_tinggal) {
		this.kd_pos_tpt_tinggal = kd_pos_tpt_tinggal;
	}
	public String getMcl_npwp() {
		return mcl_npwp;
	}
	public void setMcl_npwp(String mcl_npwp) {
		this.mcl_npwp = mcl_npwp;
	}
	public String getMspo_korespondensi() {
		return mspo_korespondensi;
	}
	public void setMspo_korespondensi(String mspo_korespondensi) {
		this.mspo_korespondensi = mspo_korespondensi;
	}
	public String getMpt_no_anggaran() {
		return mpt_no_anggaran;
	}
	public void setMpt_no_anggaran(String mpt_no_anggaran) {
		this.mpt_no_anggaran = mpt_no_anggaran;
	}
	public String getMpt_notaris() {
		return mpt_notaris;
	}
	public void setMpt_notaris(String mpt_notaris) {
		this.mpt_notaris = mpt_notaris;
	}
	public Date getMpt_tggl_anggaran() {
		return mpt_tggl_anggaran;
	}
	public void setMpt_tggl_anggaran(Date mpt_tggl_anggaran) {
		this.mpt_tggl_anggaran = mpt_tggl_anggaran;
	}
	public Date getTgl_verification_date() {
		return tgl_verification_date;
	}
	public void setTgl_verification_date(Date tgl_verification_date) {
		this.tgl_verification_date = tgl_verification_date;
	}
	public String getLead_id() {
		return lead_id;
	}
	public void setLead_id(String lead_id) {
		this.lead_id = lead_id;
	}
	public String getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(String blacklist) {
		this.blacklist = blacklist;
	}
	public Integer getFlag_free_pa() {
		return flag_free_pa;
	}
	public void setFlag_free_pa(Integer flag_free_pa) {
		this.flag_free_pa = flag_free_pa;
	}
	public String getCf_job_code() {
		return cf_job_code;
	}
	public void setCf_job_code(String cf_job_code) {
		this.cf_job_code = cf_job_code;
	}
	public String getCf_customer_id() {
		return cf_customer_id;
	}
	public void setCf_customer_id(String cf_customer_id) {
		this.cf_customer_id = cf_customer_id;
	}
	public String getCf_campaign_code() {
		return cf_campaign_code;
	}
	public void setCf_campaign_code(String cf_campaign_code) {
		this.cf_campaign_code = cf_campaign_code;
	}
	public String getMspo_no_kerjasama() {
		return mspo_no_kerjasama;
	}
	public void setMspo_no_kerjasama(String mspo_no_kerjasama) {
		this.mspo_no_kerjasama = mspo_no_kerjasama;
	}
	public Integer getFlag_cashback() {
		return flag_cashback;
	}
	public void setFlag_cashback(Integer flag_cashback) {
		this.flag_cashback = flag_cashback;
	}
	public String getMspo_id_sponsor() {
		return mspo_id_sponsor;
	}
	public void setMspo_id_sponsor(String mspo_id_sponsor) {
		this.mspo_id_sponsor = mspo_id_sponsor;
	}
	public String getMspo_id_place() {
		return mspo_id_place;
	}
	public void setMspo_id_place(String mspo_id_place) {
		this.mspo_id_place = mspo_id_place;
	}
	//generate format baru untuk nomor virtual account
	public String getJenis_va() {
		return jenis_va;
	}
	public void setJenis_va(String jenis_va) {
		this.jenis_va = jenis_va;
	}
	public String getPrefix_va() {
		return prefix_va;
	}
	public void setPrefix_va(String prefix_va) {
		this.prefix_va = prefix_va;
	}
}
