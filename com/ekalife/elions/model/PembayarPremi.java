package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.ekalife.utils.Common;

public class PembayarPremi implements Serializable {
	
	private static final long serialVersionUID = 4950241476328402703L;
	
	private Integer ljp_id;
	private String ljp_name;
	private Integer ljp_flag;
	
	private Integer lcpp_id;
	private String lcpp_name;
	private Integer lcpp_flag;
	
	private Integer lpr_id;
	private String lpr_name;
	private Integer lpr_flag;
	
	private Integer lpnr_id;
	private String lpnr_name;
	private Integer lpnr_flag;
	
	private Integer ljmp_id;
	private String ljmp_name;
	private Integer ljmp_flag;
	
	private Integer lhp_id;
	private String lhp_name;
	private Integer lhp_flag;
	
	private Integer lta_id;
	private String lta_name;
	private Integer lta_flag;
	
	private String reg_spaj;
	private String perusahaan;
	private String alamat_perusahaan; 
	private String kota_perusahaan;
	private String prov_perusahaan;
	private String kdpos_perusahaan;
	private String telp_perusahaan;
	private String faks_perusahaan;
	private String usaha;
	private String usaha_lain;
	private String pihak_ketiga;
	private String alasan;
	private String nama_pihak_ketiga;
	private String alamat_lengkap;
	private String telp_rumah;
	private String telp_kantor;
	private String kewarganegaraan;
	private String tgl_pendirian;
	private String tempat_lahir;
	private String tgl_lahir;
	private String tempat_kedudukan;
	private String pekerjaan;
	private String usaha_berbadan_hukum;
	private String jabatan;
	private String instansi;
	private String no_npwp;
	private String sumber_dana;
	private String tujuan_dana;
	private String informasi;
	private String tempat_ttd;
	private String tgl_ttd;
	private String area_code_rumah; 
	private String no_hp; 
	private String no_fax;
	private String mkl_kerja;   
	private String email;  
	
	private String edit_pembayarpremi;
	
	//tambahan natan
	private String lsre_id_payer;
	private String lsre_relation;
	private String relation_payor;
	private String lsre_id_premi;
	private String bidang_usaha_individu;
	private String bidang_usaha_badan_hukum;
	private String bidang_usaha_pembayar_premi;
	private String area_code_kantor;
	private String area_code_fax;
	private String area_code_fax_perusahaan;
	private String total_rutin;
	private String total_non_rutin;
	
	
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
	
	
	private String bidang_usaha_individu_lain;
	private String sumber_dana_lain;	
	private String tujuan_dana_lain;
	private String bidang_usaha_premi_lainnya;
	private String mcl_id;
	private String mcl_jenis;
	
	private ArrayList daftarKyc;
	
	public ArrayList getDaftarKyc() {
		return daftarKyc;
	}

	public void setDaftarKyc(ArrayList daftarKyc) {
		this.daftarKyc = Common.serializableList(daftarKyc);
	}

	public String getMcl_jenis() {
		return mcl_jenis;
	}

	public void setMcl_jenis(String mcl_jenis) {
		this.mcl_jenis = mcl_jenis;
	}

	public String getMcl_id() {
		return mcl_id;
	}

	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}

	public String getBidang_usaha_premi_lainnya() {
		return bidang_usaha_premi_lainnya;
	}

	public void setBidang_usaha_premi_lainnya(
			String bidang_usaha_premi_lainnya) {
		this.bidang_usaha_premi_lainnya = bidang_usaha_premi_lainnya;
	}

	public String getBidang_usaha_individu_lain() {
		return bidang_usaha_individu_lain;
	}

	public void setBidang_usaha_individu_lain(String bidang_usaha_individu_lain) {
		this.bidang_usaha_individu_lain = bidang_usaha_individu_lain;
	}

	public String getSumber_dana_lain() {
		return sumber_dana_lain;
	}

	public void setSumber_dana_lain(String sumber_dana_lain) {
		this.sumber_dana_lain = sumber_dana_lain;
	}

	public String getTujuan_dana_lain() {
		return tujuan_dana_lain;
	}

	public void setTujuan_dana_lain(String tujuan_dana_lain) {
		this.tujuan_dana_lain = tujuan_dana_lain;
	}

	public String getLsre_id_premi() {
		return lsre_id_premi;
	}

	public void setLsre_id_premi(String lsre_id_premi) {
		this.lsre_id_premi = lsre_id_premi;
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
		
	public String getTahun_investasi_note() {
		return tahun_investasi_note;
	}

	public void setTahun_investasi_note(String tahun_investasi_note) {
		this.tahun_investasi_note = tahun_investasi_note;
	}

	public String getBulan_usaha_note() {
		return bulan_usaha_note;
	}

	public void setBulan_usaha_note(String bulan_usaha_note) {
		this.bulan_usaha_note = bulan_usaha_note;
	}

	public String getBulan_investasi_note() {
		return bulan_investasi_note;
	}

	public void setBulan_investasi_note(String bulan_investasi_note) {
		this.bulan_investasi_note = bulan_investasi_note;
	}

	public String getBulan_lainnya_note() {
		return bulan_lainnya_note;
	}

	public void setBulan_lainnya_note(String bulan_lainnya_note) {
		this.bulan_lainnya_note = bulan_lainnya_note;
	}

	public String getTahun_lainnya_note() {
		return tahun_lainnya_note;
	}

	public void setTahun_lainnya_note(String tahun_lainnya_note) {
		this.tahun_lainnya_note = tahun_lainnya_note;
	}

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

	public String getBulan_investasi() {
		return bulan_investasi;
	}

	public void setBulan_investasi(String bulan_investasi) {
		this.bulan_investasi = bulan_investasi;
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

	public String getTotal_rutin() {
		return total_rutin;
	}

	public void setTotal_rutin(String total_rutin) {
		this.total_rutin = total_rutin;
	}

	public String getTotal_non_rutin() {
		return total_non_rutin;
	}

	public void setTotal_non_rutin(String total_non_rutin) {
		this.total_non_rutin = total_non_rutin;
	}

	public String getArea_code_perusahaan() {
		return area_code_fax_perusahaan;
	}

	public void setArea_code_perusahaan(String area_code_perusahaan) {
		this.area_code_fax_perusahaan = area_code_perusahaan;
	}

	private Date mspe_date_birth_3rd;
	private Date mspe_date_birth_3rd_pendirian; 
	private String tempat_lahir_3rd;
	private String tujuan_dana_3rd;
	
	public String getTujuan_dana_3rd() {
		return tujuan_dana_3rd;
	}

	public void setTujuan_dana_3rd(String tujuan_dana_3rd) {
		this.tujuan_dana_3rd = tujuan_dana_3rd;
	}

	public String getTempat_lahir_3rd() {
		return tempat_lahir_3rd;
	}

	public void setTempat_lahir_3rd(String tempat_lahir_3rd) {
		this.tempat_lahir_3rd = tempat_lahir_3rd;
	}

	public String getBidang_usaha_badan_hukum() {
		return bidang_usaha_badan_hukum;
	}

	public void setBidang_usaha_badan_hukum(String bidang_usaha_badan_hukum) {
		this.bidang_usaha_badan_hukum = bidang_usaha_badan_hukum;
	}

	public Date getMspe_date_birth_3rd_pendirian() {
		return mspe_date_birth_3rd_pendirian;
	}

	public void setMspe_date_birth_3rd_pendirian(Date mspe_date_birth_3rd_pendirian) {
		this.mspe_date_birth_3rd_pendirian = mspe_date_birth_3rd_pendirian;
	}

	private String mkl_kerja_other_radio;
	private String mkl_kerja_other;
	private String ada_pihak_ketiga;
	private String jenis_pihak_ketiga;
	
	
	
	public String getAda_pihak_ketiga() {
		return ada_pihak_ketiga;
	}

	public void setAda_pihak_ketiga(String ada_pihak_ketiga) {
		this.ada_pihak_ketiga = ada_pihak_ketiga;
	}

	public String getJenis_pihak_ketiga() {
		return jenis_pihak_ketiga;
	}

	public void setJenis_pihak_ketiga(String jenis_pihak_ketiga) {
		this.jenis_pihak_ketiga = jenis_pihak_ketiga;
	}

	public String getMkl_kerja_other_radio() {
		return mkl_kerja_other_radio;
	}

	public void setMkl_kerja_other_radio(String mkl_kerja_other_radio) {
		this.mkl_kerja_other_radio = mkl_kerja_other_radio;
	}

	public String getMkl_kerja_other() {
		return mkl_kerja_other;
	}

	public void setMkl_kerja_other(String mkl_kerja_other) {
		this.mkl_kerja_other = mkl_kerja_other;
	}

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

	public String getArea_code_kantor_3rd() {
		return area_code_kantor_3rd;
	}

	public void setArea_code_kantor_3rd(String area_code_kantor_3rd) {
		this.area_code_kantor_3rd = area_code_kantor_3rd;
	}

	public String getArea_code_3rd() {
		return area_code_3rd;
	}

	public void setArea_code_3rd(String area_code_3rd) {
		this.area_code_3rd = area_code_3rd;
	}

	private String area_code_kantor_3rd;
	private String area_code_3rd;

	
	public Date getMspe_date_birth_3rd() {
		return mspe_date_birth_3rd;
	}

	public void setMspe_date_birth_3rd(Date mspe_date_birth_3rd) {
		this.mspe_date_birth_3rd = mspe_date_birth_3rd;
	}

	public String getBidang_usaha_individu() {
		return bidang_usaha_individu;
	}

	public String getBidang_usaha_pembayar_premi() {
		return bidang_usaha_pembayar_premi;
	}

	public void setBidang_usaha_pembayar_premi(String bidang_usaha_pembayar_premi) {
		this.bidang_usaha_pembayar_premi = bidang_usaha_pembayar_premi;
	}

	public String getArea_code_fax_perusahaan() {
		return area_code_fax_perusahaan;
	}

	public void setArea_code_fax_perusahaan(String area_code_fax_perusahaan) {
		this.area_code_fax_perusahaan = area_code_fax_perusahaan;
	}

	public void setBidang_usaha_individu(String bidang_usaha_individu) {
		this.bidang_usaha_individu = bidang_usaha_individu;
	}

	public String getLsre_id_payer() {
		return lsre_id_payer;
	}

	public void setLsre_id_payer(String lsre_id_payer) {
		this.lsre_id_payer = lsre_id_payer;
	}

	public String getLsre_relation() {
		return lsre_relation;
	}

	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	
	public String getRelation_payor() {
		return relation_payor;
	}

	public void setRelation_payor(String relation_payor) {
		this.relation_payor = relation_payor;
	}

	public void setLjp_id(Integer ljp_id) {
		this.ljp_id=ljp_id;
	}
	
	public Integer getLpj_id() {
		return ljp_id;
	}
	
	public void setLjp_name(String ljp_name) {
		this.ljp_name=ljp_name;
	}
	
	public String getLjp_name() {
		return ljp_name;		
	}
	
	public void setLjp_flag(Integer ljp_flag) {
		this.ljp_flag=ljp_flag;
	}
	
	public Integer getLjp_flag() {
		return ljp_flag;
	}
	
	public void setLcpp_id(Integer lcpp_id) {
		this.lcpp_id=lcpp_id;
	}
	
	public Integer getLcpp_id() {
		return lcpp_id;
	}
	
	public void setLcpp_name(String lcpp_name) {
		this.lcpp_name=lcpp_name;
	}
	
	public String getLccp_name() {
		return lcpp_name;
	}
	
	public void setLcpp_flag(Integer lcpp_flag) {
		this.lcpp_flag=lcpp_flag;
	}
	
	public Integer getLcpp_flag() {
		return lcpp_flag;
	}
	
	public void setLpr_id(Integer lpr_id) {
		this.lpr_id=lpr_id;
	}
	
	public Integer getLpr_id() {
		return lpr_id;
	}
	
	public void setLpr_name(String lpr_name) {
		this.lpr_name=lpr_name;
	}
	
	public String getLpr_name() {
		return lpr_name;
	}
	
	public void setLpr_flag(Integer lpr_flag) {
		this.lpr_flag=lpr_flag;
	}
	
	public Integer getLpr_flag() {
		return lpr_flag;
	}
	
	public void setLpnr_id(Integer lpnr_id) {
		this.lpnr_id=lpnr_id;
	}
	
	public Integer getLpnr_id() {
		return lpnr_id;
	}
	
	public void setLpnr_name(String lpnr_name) {
		this.lpnr_name=lpnr_name;
	}
	
	public String getLpnr_name() {
		return lpnr_name;
	}
	
	public void setLpnr_flag(Integer lpnr_flag) {
		this.lpnr_flag=lpnr_flag;
	}
	
	public Integer getLpnr_flag() {
		return lpnr_flag;
	}
	
	public void setLjmp_id(Integer ljmp_id) {
		this.ljmp_id=ljmp_id;
	}
	
	public Integer getLjmp_id() {
		return ljmp_id;
	}
	
	public void setLjmp_name(String ljmp_name) {
		this.ljmp_name=ljmp_name;
	}
	
	public String getLjmp_name() {
		return ljmp_name;
	}
	
	public void setLjmp_flag(Integer ljmp_flag) {
		this.ljmp_flag=ljmp_flag;
	}
	
	public Integer getLjmp_flag() {
		return ljmp_flag;		
	}
	
	public void setLhp_id(Integer lhp_id) {
		this.lhp_id=lhp_id;
	}
	
	public Integer getLhp_id() {
		return lhp_id;
	}
	
	public void setLhp_name(String lhp_name) {
		this.lhp_name=lhp_name;
	}
	
	public String getLhp_name() {
		return lhp_name;
	}
	
	public void setLhp_flag(Integer lhp_flag) {
		this.lhp_flag=lhp_flag;
	}
	
	public Integer getLhp_flag() {
		return lhp_flag;
	}

	public Integer getLta_id() {
		return lta_id;
	}

	public void setLta_id(Integer lta_id) {
		this.lta_id = lta_id;
	}

	public String getLta_name() {
		return lta_name;
	}

	public void setLta_name(String lta_name) {
		this.lta_name = lta_name;
	}

	public Integer getLta_flag() {
		return lta_flag;
	}

	public void setLta_flag(Integer lta_flag) {
		this.lta_flag = lta_flag;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

	public String getPerusahaan() {
		return perusahaan;
	}

	public void setPerusahaan(String perusahaan) {
		this.perusahaan = perusahaan;
	}

	public String getAlamat_perusahaan() {
		return alamat_perusahaan;
	}

	public void setAlamat_perusahaan(String alamat_perusahaan) {
		this.alamat_perusahaan = alamat_perusahaan;
	}

	public String getKota_perusahaan() {
		return kota_perusahaan;
	}

	public void setKota_perusahaan(String kota_perusahaan) {
		this.kota_perusahaan = kota_perusahaan;
	}

	public String getProv_perusahaan() {
		return prov_perusahaan;
	}

	public void setProv_perusahaan(String prov_perusahaan) {
		this.prov_perusahaan = prov_perusahaan;
	}

	public String getKdpos_perusahaan() {
		return kdpos_perusahaan;
	}

	public void setKdpos_perusahaan(String kdpos_perusahaan) {
		this.kdpos_perusahaan = kdpos_perusahaan;
	}

	public String getTelp_perusahaan() {
		return telp_perusahaan;
	}

	public void setTelp_perusahaan(String telp_perusahaan) {
		this.telp_perusahaan = telp_perusahaan;
	}

	public String getFaks_perusahaan() {
		return faks_perusahaan;
	}

	public void setFaks_perusahaan(String faks_perusahaan) {
		this.faks_perusahaan = faks_perusahaan;
	}

	public String getUsaha() {
		return usaha;
	}

	public void setUsaha(String usaha) {
		this.usaha = usaha;
	}

	public String getUsaha_lain() {
		return usaha_lain;
	}

	public void setUsaha_lain(String usaha_lain) {
		this.usaha_lain = usaha_lain;
	}

	public String getPihak_ketiga() {
		return pihak_ketiga;
	}

	public void setPihak_ketiga(String pihak_ketiga) {
		this.pihak_ketiga = pihak_ketiga;
	}

	public String getAlasan() {
		return alasan;
	}

	public void setAlasan(String alasan) {
		this.alasan = alasan;
	}

	public String getNama_pihak_ketiga() {
		return nama_pihak_ketiga;
	}

	public void setNama_pihak_ketiga(String nama_pihak_ketiga) {
		this.nama_pihak_ketiga = nama_pihak_ketiga;
	}

	public String getAlamat_lengkap() {
		return alamat_lengkap;
	}

	public void setAlamat_lengkap(String alamat_lengkap) {
		this.alamat_lengkap = alamat_lengkap;
	}

	public String getTelp_rumah() {
		return telp_rumah;
	}

	public void setTelp_rumah(String telp_rumah) {
		this.telp_rumah = telp_rumah;
	}

	public String getTelp_kantor() {
		return telp_kantor;
	}

	public void setTelp_kantor(String telp_kantor) {
		this.telp_kantor = telp_kantor;
	}

	public String getKewarganegaraan() {
		return kewarganegaraan;
	}

	public void setKewarganegaraan(String kewarganegaraan) {
		this.kewarganegaraan = kewarganegaraan;
	}

	public String getTgl_pendirian() {
		return tgl_pendirian;
	}

	public void setTgl_pendirian(String tgl_pendirian) {
		this.tgl_pendirian = tgl_pendirian;
	}

	public String getTempat_lahir() {
		return tempat_lahir;
	}

	public void setTempat_lahir(String tempat_lahir) {
		this.tempat_lahir = tempat_lahir;
	}

	public String getTgl_lahir() {
		return tgl_lahir;
	}

	public void setTgl_lahir(String tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}

	public String getTempat_kedudukan() {
		return tempat_kedudukan;
	}

	public void setTempat_kedudukan(String tempat_kedudukan) {
		this.tempat_kedudukan = tempat_kedudukan;
	}

	public String getPekerjaan() {
		return pekerjaan;
	}

	public void setPekerjaan(String pekerjaan) {
		this.pekerjaan = pekerjaan;
	}

	public String getJabatan() {
		return jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	public String getInstansi() {
		return instansi;
	}

	public void setInstansi(String instansi) {
		this.instansi = instansi;
	}

	public String getNo_npwp() {
		return no_npwp;
	}

	public void setNo_npwp(String no_npwp) {
		this.no_npwp = no_npwp;
	}

	public String getSumber_dana() {
		return sumber_dana;
	}

	public void setSumber_dana(String sumber_dana) {
		this.sumber_dana = sumber_dana;
	}

	public String getTujuan_dana() {
		return tujuan_dana;
	}

	public void setTujuan_dana(String tujuan_dana) {
		this.tujuan_dana = tujuan_dana;
	}

	public String getInformasi() {
		return informasi;
	}

	public void setInformasi(String informasi) {
		this.informasi = informasi;
	}

	public String getTempat_ttd() {
		return tempat_ttd;
	}

	public void setTempat_ttd(String tempat_ttd) {
		this.tempat_ttd = tempat_ttd;
	}

	public String getTgl_ttd() {
		return tgl_ttd;
	}

	public void setTgl_ttd(String tgl_ttd) {
		this.tgl_ttd = tgl_ttd;
	}

	public String getEdit_pembayarpremi() {
		return edit_pembayarpremi;
	}

	public void setEdit_pembayarpremi(String edit_pembayarpremi) {
		this.edit_pembayarpremi = edit_pembayarpremi;
	}

	public String getUsaha_berbadan_hukum() {
		return usaha_berbadan_hukum;
	}

	public void setUsaha_berbadan_hukum(String usaha_berbadan_hukum) {
		this.usaha_berbadan_hukum = usaha_berbadan_hukum;
	}

	public String getArea_code_rumah() {
		return area_code_rumah;
	}

	public void setArea_code_rumah(String area_code_rumah) {
		this.area_code_rumah = area_code_rumah;
	}

	public String getNo_hp() {
		return no_hp;
	}

	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}

	public String getNo_fax() {
		return no_fax;
	}

	public void setNo_fax(String no_fax) {
		this.no_fax = no_fax;
	}

	public String getMkl_kerja() {
		return mkl_kerja;
	}

	public void setMkl_kerja(String mkl_kerja) {
		this.mkl_kerja = mkl_kerja;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
