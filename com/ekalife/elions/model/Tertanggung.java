package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.utils.Common;

public class Tertanggung implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer lti_id;
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
//	private String lspr_id;   //propinsi
//	private String lska_id;   //kabupaten
//	private String lskc_id;   //kecamatan
//	private String lskl_id;   //kelurahan                 
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
	private String mcl_first;                      
	private Integer mste_standard;                 
	private Integer mste_age;                      
	private String lsag_name;                      
	private Integer lsag_id;                       
	private String lsed_name;                      
	private Integer lsed_id;                       
	private String ljb_note;                       
	private String ljb_id;                         
	private String lsre_relation;                  
	private Integer lsre_id;                       
	private String msag_id;                        
	private String lca_id;                         
	private String lwk_id;                         
	private String lsrg_id;                        
	private Integer lscb_id;                       
	private String mspo_policy_no;
	private Integer usiattg;
	private String tgl_ttg;
	private String area_code_rumah2;               
	private String telpon_rumah2;                  
	private String area_code_kantor2;              
	private String telpon_kantor2;                 
	private String no_fax;
	private String area_code_fax;  
	private Integer lspd_id;
	private Integer lus_id;
	private Integer mcl_jenis;
	private Integer mcl_blacklist;
	private String mste_insured;
	private String mspe_email;
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
	private ArrayList<Keluarga>listKeluarga;
	private Integer mcl_data_salah;
	private Date mcl_tgl_input;
	private String mkl_smbr_penghasilan;
	private Date mste_kyc_date;
	private String mcl_agama;
	private Integer mste_flag_ref_telemarket;
	private String mste_no_vacc;
	private String mspo_plan_provider;
	private Integer mkl_dana_from;
	private Integer mkl_hasil_from;
	private Integer mkl_smbr_hasil_from;
	private ArrayList daftarKyc;
	private ArrayList daftarKyc2;
	private Integer jmlDaftarKyc;
	private Integer jmlDaftarKyc2;
	private Integer mkl_sumber_premi;
	private Integer lsre_id_premi;
	private Integer mste_flag_cc;
	private Integer mste_dth;
	private Integer jml_dth;
	private ArrayList daftarDth;
	private Date mspe_no_identity_expired;
	private Integer edit_tertanggung;
	private String kriteria_kesalahan;
	private String blacklist;
	
	//data Spaj Tambahan
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
	private Date mpt_tgl_anggaran;
	private Integer height;
	private Integer weight;
	private Integer type_data;
	
	public Integer getType_data() {
		return type_data;
	}
	public void setType_data(Integer type_data) {
		this.type_data = type_data;
	}
	private String kerjaNote;
	
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
			public String getKerjaNote() {
				return kerjaNote;
			}
			public void setKerjaNote(String kerjaNote) {
				this.kerjaNote = kerjaNote;
			}
	
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
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
	public Date getMpt_tgl_anggaran() {
		return mpt_tgl_anggaran;
	}
	public void setMpt_tgl_anggaran(Date mpt_tgl_anggaran) {
		this.mpt_tgl_anggaran = mpt_tgl_anggaran;
	}
	public ArrayList getDaftarDth() {
		return daftarDth;
	}
	public void setDaftarDth(List daftarDth) {
		this.daftarDth = Common.serializableList(daftarDth);
	}
	public Integer getJml_dth() {
		return jml_dth;
	}
	public void setJml_dth(Integer jml_dth) {
		this.jml_dth = jml_dth;
	}
	public Integer getMste_flag_cc() {
		return mste_flag_cc;
	}
	public void setMste_flag_cc(Integer mste_flag_cc) {
		this.mste_flag_cc = mste_flag_cc;
	}
	public Integer getMste_dth() {
		return mste_dth;
	}
	public void setMste_dth(Integer mste_dth) {
		this.mste_dth = mste_dth;
	}
	public Integer getMste_flag_special_offer() {
		return mste_flag_special_offer;
	}
	public void setMste_flag_special_offer(Integer mste_flag_special_offer) {
		this.mste_flag_special_offer = mste_flag_special_offer;
	}
	private Integer mkl_red_flag;
	private Integer mste_flag_special_offer;
	private Integer mste_flag_hadiah;
	public Integer getMste_flag_hadiah() {
		return mste_flag_hadiah;
	}
	public void setMste_flag_hadiah(Integer mste_flag_hadiah) {
		this.mste_flag_hadiah = mste_flag_hadiah;
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
	public String getMste_no_vacc() {
		return mste_no_vacc;
	}
	public void setMste_no_vacc(String mste_no_vacc) {
		this.mste_no_vacc = mste_no_vacc;
	}
	public Date getMste_kyc_date() {
		return mste_kyc_date;
	}
	public void setMste_kyc_date(Date mste_kyc_date) {
		this.mste_kyc_date = mste_kyc_date;
	}
	public String getMkl_smbr_penghasilan() {
		return mkl_smbr_penghasilan;
	}
	public void setMkl_smbr_penghasilan(String mkl_smbr_penghasilan) {
		this.mkl_smbr_penghasilan = mkl_smbr_penghasilan;
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
	
	/*//chandra a - 20180307
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
	//chandra a - 20180307*/
		
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
	public String getDanaa() {
		return danaa;
	}
	public void setDanaa(String danaa) {
		this.danaa = danaa;
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
	public String getIndustria() {
		return industria;
	}
	public void setIndustria(String industria) {
		this.industria = industria;
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
	public ArrayList<Keluarga> getListKeluarga() {
		return listKeluarga;
	}
	public void setListKeluarga(List<Keluarga> listKeluarga) {
		this.listKeluarga = Common.serializableList(listKeluarga);
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
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
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
	public Integer getMcl_data_salah() {
		return mcl_data_salah;
	}
	public void setMcl_data_salah(Integer mcl_data_salah) {
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
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspo_policy_no) {
		this.mspo_policy_no = mspo_policy_no;
	}
	public Integer getMspo_umur_beasiswa() {
		return mspo_umur_beasiswa;
	}
	public void setMspo_umur_beasiswa(Integer mspo_umur_beasiswa) {
		this.mspo_umur_beasiswa = mspo_umur_beasiswa;
	}
	public Integer getMste_age() {
		return mste_age;
	}
	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
	}
	public String getMste_insured() {
		return mste_insured;
	}
	public void setMste_insured(String mste_insured) {
		this.mste_insured = mste_insured;
	}
	public Integer getMste_insured_no() {
		return mste_insured_no;
	}
	public void setMste_insured_no(Integer mste_insured_no) {
		this.mste_insured_no = mste_insured_no;
	}
	public Integer getMste_standard() {
		return mste_standard;
	}
	public void setMste_standard(Integer mste_standard) {
		this.mste_standard = mste_standard;
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
	public String getNama_si() {
		return nama_si;
	}
	public void setNama_si(String nama_si) {
		this.nama_si = nama_si;
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
	public String getTgl_ttg() {
		return tgl_ttg;
	}
	public void setTgl_ttg(String tgl_ttg) {
		this.tgl_ttg = tgl_ttg;
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
	public Integer getUsiattg() {
		return usiattg;
	}
	public void setUsiattg(Integer usiattg) {
		this.usiattg = usiattg;
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
	public Integer getLti_id() {
		return lti_id;
	}
	public void setLti_id(Integer lti_id) {
		this.lti_id = lti_id;
	}
	public String getMcl_agama() {
		return mcl_agama;
	}
	public void setMcl_agama(String mcl_agama) {
		this.mcl_agama = mcl_agama;
	}
	public Integer getMste_flag_ref_telemarket() {
		return mste_flag_ref_telemarket;
	}
	public void setMste_flag_ref_telemarket(Integer mste_flag_ref_telemarket) {
		this.mste_flag_ref_telemarket = mste_flag_ref_telemarket;
	}
	public String getMspo_plan_provider() {
		return mspo_plan_provider;
	}
	public void setMspo_plan_provider(String mspoPlanProvider) {
		mspo_plan_provider = mspoPlanProvider;
	}
	
	public Integer getMkl_red_flag() {
		return mkl_red_flag;
	}
	public void setMkl_red_flag(Integer mkl_red_flag) {
		this.mkl_red_flag = mkl_red_flag;
	}
	public Date getMspe_no_identity_expired() {
		return mspe_no_identity_expired;
	}
	public void setMspe_no_identity_expired(Date mspe_no_identity_expired) {
		this.mspe_no_identity_expired = mspe_no_identity_expired;
	}
	public Integer getEdit_tertanggung() {
		return edit_tertanggung;
	}
	public void setEdit_tertanggung(Integer edit_tertanggung) {
		this.edit_tertanggung = edit_tertanggung;
	}
	public String getKriteria_kesalahan() {
		return kriteria_kesalahan;
	}
	public void setKriteria_kesalahan(String kriteria_kesalahan) {
		this.kriteria_kesalahan = kriteria_kesalahan;
	}
	public String getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(String blacklist) {
		this.blacklist = blacklist;
	}
	
}
