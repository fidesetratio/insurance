package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class TravelInsurance implements Serializable {

	private static final long serialVersionUID = 1L;

	public Integer flag; //0 default, 1 save, 2 transfer, 3 aksep
	public Integer jml_peserta_terinput;
	
	//MST_TRAVEL_INS
	public Integer msti_id;
	public Integer msti_jenis;
	public String lca_id;
	public String lwk_id;
	public String lsrg_id;
	public Integer lsb_id;
	public String no_pre;
	public String no_voucher;
	public Integer msti_posisi;
	public Date msti_tgl;
	public Date msti_tgl_rk;
	public Integer msti_jml_peserta;
	public Double msti_premi;
	public Double msti_premi_setor;
	public Double msti_up;
	public String msti_msag_id;
	public Double msti_komisi;
	public Double msti_kom_tax;
	public Date msti_tgl_input;
	public Date msti_tgl_prod;
	public Date msti_tgl_aksep;
	public Date msti_tgl_komisi;
	public Integer lus_id_input;
	public Integer lus_id_aksep;
	public Integer lus_id_komisi;

	//MST_TRAVEL_INS_DET
	//public Integer msti_id;                       
	//public Integer msti_jenis;                    
	public Integer msid_no;
	public Double msid_premi;
	public Double msid_premi_setor;
	public Double msid_up;
	public String msid_blanko;
	public String msid_nama;
	public Integer msid_sex;
	public String msid_place_birth;
	public Date msid_date_birth;
	public String msid_alamat;
	public String msid_kota;
	public String msid_kd_pos;
	public String msid_kd_telp_rmh;
	public String msid_telp_rmh;
	public String msid_hp;
	public Date msid_beg_date;
	public Date msid_end_date;
	public Date tgl_input;
	public Integer lus_id;
	
	//MST_POSITION_TRAVEL_INS
	//public Integer msti_id;                       
	//public Integer msti_jenis;                    
	public Date mspt_date;
	public String mspt_desc;
	public Integer mspt_lus_id;
	
	//LST_BANDARA
	//public Integer lsb_id;
	public String lsb_code;
	public String lsb_desc;
	public String lsb_kota;
	
	//additionals
	public String user_input;
	public String user_aksep;
	public String user_komisi;
	public TravelInsurance validasi;
	public Integer lsp_id_lama;
	public Integer lsp_id_baru;
	public Integer kode_premi; //1 = 5000, 2 = 10000
	public String msf_id;
	
	public String getMsf_id() {
		return msf_id;
	}
	public void setMsf_id(String msf_id) {
		this.msf_id = msf_id;
	}
	public Integer getLsp_id_baru() {
		return lsp_id_baru;
	}
	public void setLsp_id_baru(Integer lsp_id_baru) {
		this.lsp_id_baru = lsp_id_baru;
	}
	public Integer getLsp_id_lama() {
		return lsp_id_lama;
	}
	public void setLsp_id_lama(Integer lsp_id_lama) {
		this.lsp_id_lama = lsp_id_lama;
	}
	public Integer getKode_premi() {
		return kode_premi;
	}
	public void setKode_premi(Integer kode_premi) {
		this.kode_premi = kode_premi;
	}
	public TravelInsurance getValidasi() {
		return validasi;
	}
	public void setValidasi(TravelInsurance validasi) {
		this.validasi = validasi;
	}
	public String getUser_aksep() {
		return user_aksep;
	}
	public void setUser_aksep(String user_aksep) {
		this.user_aksep = user_aksep;
	}
	public String getUser_input() {
		return user_input;
	}
	public String getMsid_alamat() {
		return msid_alamat;
	}
	public void setMsid_alamat(String msid_alamat) {
		this.msid_alamat = msid_alamat;
	}
	public String getMsid_kd_pos() {
		return msid_kd_pos;
	}
	public void setMsid_kd_pos(String msid_kd_pos) {
		this.msid_kd_pos = msid_kd_pos;
	}
	public String getMsid_kota() {
		return msid_kota;
	}
	public void setMsid_kota(String msid_kota) {
		this.msid_kota = msid_kota;
	}
	public Double getMsid_premi_setor() {
		return msid_premi_setor;
	}
	public void setMsid_premi_setor(Double msid_premi_setor) {
		this.msid_premi_setor = msid_premi_setor;
	}
	public void setUser_input(String user_input) {
		this.user_input = user_input;
	}
	public String getUser_komisi() {
		return user_komisi;
	}
	public void setUser_komisi(String user_komisi) {
		this.user_komisi = user_komisi;
	}
	public String getLca_id() {
		return lca_id;
	}
	public Date getMsti_tgl_rk() {
		return msti_tgl_rk;
	}
	public void setMsti_tgl_rk(Date msti_tgl_rk) {
		this.msti_tgl_rk = msti_tgl_rk;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public String getLsb_code() {
		return lsb_code;
	}
	public void setLsb_code(String lsb_code) {
		this.lsb_code = lsb_code;
	}
	public String getLsb_desc() {
		return lsb_desc;
	}
	public Date getMsid_beg_date() {
		return msid_beg_date;
	}
	public void setMsid_beg_date(Date msid_beg_date) {
		this.msid_beg_date = msid_beg_date;
	}
	public Date getMsid_end_date() {
		return msid_end_date;
	}
	public void setMsid_end_date(Date msid_end_date) {
		this.msid_end_date = msid_end_date;
	}
	public void setLsb_desc(String lsb_desc) {
		this.lsb_desc = lsb_desc;
	}
	public Integer getLsb_id() {
		return lsb_id;
	}
	public void setLsb_id(Integer lsb_id) {
		this.lsb_id = lsb_id;
	}
	public String getLsb_kota() {
		return lsb_kota;
	}
	public void setLsb_kota(String lsb_kota) {
		this.lsb_kota = lsb_kota;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public Integer getJml_peserta_terinput() {
		return jml_peserta_terinput;
	}
	public void setJml_peserta_terinput(Integer jml_peserta_terinput) {
		this.jml_peserta_terinput = jml_peserta_terinput;
	}
	public Integer getLus_id_aksep() {
		return lus_id_aksep;
	}
	public void setLus_id_aksep(Integer lus_id_aksep) {
		this.lus_id_aksep = lus_id_aksep;
	}
	public Integer getLus_id_input() {
		return lus_id_input;
	}
	public void setLus_id_input(Integer lus_id_input) {
		this.lus_id_input = lus_id_input;
	}
	public Integer getLus_id_komisi() {
		return lus_id_komisi;
	}
	public void setLus_id_komisi(Integer lus_id_komisi) {
		this.lus_id_komisi = lus_id_komisi;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}
	public String getMsid_blanko() {
		return msid_blanko;
	}
	public void setMsid_blanko(String msid_blanko) {
		this.msid_blanko = msid_blanko;
	}
	public Date getMsid_date_birth() {
		return msid_date_birth;
	}
	public void setMsid_date_birth(Date msid_date_birth) {
		this.msid_date_birth = msid_date_birth;
	}
	public String getMsid_hp() {
		return msid_hp;
	}
	public void setMsid_hp(String msid_hp) {
		this.msid_hp = msid_hp;
	}
	public String getMsid_kd_telp_rmh() {
		return msid_kd_telp_rmh;
	}
	public void setMsid_kd_telp_rmh(String msid_kd_telp_rmh) {
		this.msid_kd_telp_rmh = msid_kd_telp_rmh;
	}
	public String getMsid_nama() {
		return msid_nama;
	}
	public void setMsid_nama(String msid_nama) {
		this.msid_nama = msid_nama;
	}
	public Integer getMsid_no() {
		return msid_no;
	}
	public void setMsid_no(Integer msid_no) {
		this.msid_no = msid_no;
	}
	public String getMsid_place_birth() {
		return msid_place_birth;
	}
	public void setMsid_place_birth(String msid_place_birth) {
		this.msid_place_birth = msid_place_birth;
	}
	public Double getMsid_premi() {
		return msid_premi;
	}
	public void setMsid_premi(Double msid_premi) {
		this.msid_premi = msid_premi;
	}
	public Integer getMsid_sex() {
		return msid_sex;
	}
	public void setMsid_sex(Integer msid_sex) {
		this.msid_sex = msid_sex;
	}
	public String getMsid_telp_rmh() {
		return msid_telp_rmh;
	}
	public void setMsid_telp_rmh(String msid_telp_rmh) {
		this.msid_telp_rmh = msid_telp_rmh;
	}
	public Double getMsid_up() {
		return msid_up;
	}
	public void setMsid_up(Double msid_up) {
		this.msid_up = msid_up;
	}
	public Date getMspt_date() {
		return mspt_date;
	}
	public void setMspt_date(Date mspt_date) {
		this.mspt_date = mspt_date;
	}
	public String getMspt_desc() {
		return mspt_desc;
	}
	public void setMspt_desc(String mspt_desc) {
		this.mspt_desc = mspt_desc;
	}
	public Integer getMsti_id() {
		return msti_id;
	}
	public void setMsti_id(Integer msti_id) {
		this.msti_id = msti_id;
	}
	public Integer getMsti_jenis() {
		return msti_jenis;
	}
	public void setMsti_jenis(Integer msti_jenis) {
		this.msti_jenis = msti_jenis;
	}
	public Integer getMsti_jml_peserta() {
		return msti_jml_peserta;
	}
	public void setMsti_jml_peserta(Integer msti_jml_peserta) {
		this.msti_jml_peserta = msti_jml_peserta;
	}
	public Double getMsti_kom_tax() {
		return msti_kom_tax;
	}
	public void setMsti_kom_tax(Double msti_kom_tax) {
		this.msti_kom_tax = msti_kom_tax;
	}
	public Double getMsti_komisi() {
		return msti_komisi;
	}
	public void setMsti_komisi(Double msti_komisi) {
		this.msti_komisi = msti_komisi;
	}
	public String getMsti_msag_id() {
		return msti_msag_id;
	}
	public void setMsti_msag_id(String msti_msag_id) {
		this.msti_msag_id = msti_msag_id;
	}
	public Integer getMsti_posisi() {
		return msti_posisi;
	}
	public void setMsti_posisi(Integer msti_posisi) {
		this.msti_posisi = msti_posisi;
	}
	public Double getMsti_premi() {
		return msti_premi;
	}
	public void setMsti_premi(Double msti_premi) {
		this.msti_premi = msti_premi;
	}
	public Double getMsti_premi_setor() {
		return msti_premi_setor;
	}
	public void setMsti_premi_setor(Double msti_premi_setor) {
		this.msti_premi_setor = msti_premi_setor;
	}
	public Date getMsti_tgl() {
		return msti_tgl;
	}
	public void setMsti_tgl(Date msti_tgl) {
		this.msti_tgl = msti_tgl;
	}
	public Date getMsti_tgl_aksep() {
		return msti_tgl_aksep;
	}
	public void setMsti_tgl_aksep(Date msti_tgl_aksep) {
		this.msti_tgl_aksep = msti_tgl_aksep;
	}
	public Date getMsti_tgl_input() {
		return msti_tgl_input;
	}
	public void setMsti_tgl_input(Date msti_tgl_input) {
		this.msti_tgl_input = msti_tgl_input;
	}
	public Date getMsti_tgl_komisi() {
		return msti_tgl_komisi;
	}
	public void setMsti_tgl_komisi(Date msti_tgl_komisi) {
		this.msti_tgl_komisi = msti_tgl_komisi;
	}
	public Date getMsti_tgl_prod() {
		return msti_tgl_prod;
	}
	public void setMsti_tgl_prod(Date msti_tgl_prod) {
		this.msti_tgl_prod = msti_tgl_prod;
	}
	public Double getMsti_up() {
		return msti_up;
	}
	public void setMsti_up(Double msti_up) {
		this.msti_up = msti_up;
	}
	public String getNo_pre() {
		return no_pre;
	}
	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}
	public String getNo_voucher() {
		return no_voucher;
	}
	public void setNo_voucher(String no_voucher) {
		this.no_voucher = no_voucher;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getMspt_lus_id() {
		return mspt_lus_id;
	}
	public void setMspt_lus_id(Integer mspt_lus_id) {
		this.mspt_lus_id = mspt_lus_id;
	}
}