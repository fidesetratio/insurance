package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * MST_HADIAH
 * 
 * @author Yusuf
 * @since Nov 30, 2011 (11:10:37 AM)
 *
 */
public class Hadiah implements Serializable {

	private static final long serialVersionUID = 8238795333728661197L;

	//MST_HADIAH
	public String reg_spaj; //nomor spaj
	public Integer mh_no; //nomor urut hadiah (misalnya 1 spaj bisa > 1 macam hadiah)
	public Integer lhc_id; //kategori hadiah, key ke LST_HADIAH_CAT
	public Integer lh_id; //nama hadiah, key ke LST_HADIAH
	public Integer lspd_id; //posisi dokumen, key ke LST_DOCUMENT_POSITION
	public String supplier_id; //vendor, key ke STOK.MASTER_SUPPLIER
	public Integer mh_flag_kirim; //0 = direct dari vendor ke nsbh, 1 = dari vendor ke AJS dulu baru ke nasabah
	public Double mh_budget; //budget pengiriman, default dari LST_HADIAH.LH_HARGA
	public Double mh_harga; //harga barang, default dari LST_HADIAH.LH_HARGA
	public String mh_alamat; //alamat pengiriman nasabah
	public String mh_kota; //kota pengiriman nasabah
	public String mh_kodepos; //kode pos pengiriman nasabah
	public String mh_telepon; //telepon/hp nasabah yg bisa dihubungi
	public String mh_keterangan; //keterangan tambahan
	public Integer mh_quantity; //jumlah pembelian unit, default 1
	public Integer lbn_id; //bank rekening vendor
	public String mh_rek_no; //norek vendor
	public String mh_rek_nama; //atas nama rekening vendor

	public Date mh_tgl_input; //tgl input hadiah
	public Date mh_tgl_aksep; //tgl akseptasi hadiah oleh purchasing
	public Date mh_tgl_paid; //tgl paid oleh finance
	public Date mh_tgl_kirim_vendor; //tgl barang dikirim oleh vendor
	public Date mh_tgl_terima_ajs; //tgl barang diterima ajs (bila mh_flag_kirim = 1)
	public Date mh_tgl_kirim_ajs; //tgl barang dikirim oleh ajs 
	public Date mh_tgl_terima_nsbh; //tgl barang diterima nasabah
	public Date mh_tgl_deadline_bayar; //tgl deadline pembayaran

	public Integer create_id; //user yg create record, key ke LST_USER
	public Date create_dt; //tgl create record (sama dgn tgl input)
	
	//MST_HADIAH_HIST
	public String keterangan; //keterangan history
	
	//LST_HADIAH
	public String lhc_nama; //nama kategori hadiah
	public String lh_nama; //nama unit hadiah
    public Double lh_harga; //harga default
    public Integer lh_hari; //service level pengiriman dalam brp hari?
	
	//kolom2 tambahan
	public String policy_no; //no polis
	public String beg_date; //begdate polis
	public String end_date; //enddate polis
	public String pemegang; //nama pemegang
	public String tgl_aksep; //tgl aksep uw
	public String supplier_name; //nama vendor
	public String bank_name; //nama bank vendor
	public String pesan; //pesan saat simpan data saja
	public String lspd_position;
	public String username;
	
	//tambahan untuk program_hadiah
	public Date mh_tgl_kirim_uw;
	public Date mh_tgl_kirim_purchasing;
	public Date mh_tgl_kirim_distribution;
	public Date mh_tgl_kirim_ga;
	public Integer program_hadiah;
	public Date mh_tgl_order_purchasing;
	public Date mh_tgl_barang_ready;
	public String mh_sn;
	public Date mh_tgl_kirim_hadiah;
	public String mh_tgl_kirim_hadiah_str;
	public String mh_alamat_kirim;
	public String mh_kota_kirim;
	public String mh_kodepos_kirim;
	public Date mh_tgl_kirim_package;
	public Integer flag_standard;
	public Integer flag_active;
	
	public Integer getFlag_active() {
		return flag_active;
	}
	public void setFlag_active(Integer flag_active) {
		this.flag_active = flag_active;
	}
	public Integer getFlag_standard() {
		return flag_standard;
	}
	public void setFlag_standard(Integer flag_standard) {
		this.flag_standard = flag_standard;
	}
	public Date getMh_tgl_kirim_package() {
		return mh_tgl_kirim_package;
	}
	public void setMh_tgl_kirim_package(Date mh_tgl_kirim_package) {
		this.mh_tgl_kirim_package = mh_tgl_kirim_package;
	}
	public String getMh_kota_kirim() {
		return mh_kota_kirim;
	}
	public void setMh_kota_kirim(String mh_kota_kirim) {
		this.mh_kota_kirim = mh_kota_kirim;
	}
	public String getMh_kodepos_kirim() {
		return mh_kodepos_kirim;
	}
	public void setMh_kodepos_kirim(String mh_kodepos_kirim) {
		this.mh_kodepos_kirim = mh_kodepos_kirim;
	}
	public String getMh_alamat_kirim() {
		return mh_alamat_kirim;
	}
	public void setMh_alamat_kirim(String mh_alamat_kirim) {
		this.mh_alamat_kirim = mh_alamat_kirim;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLspd_position() {
		return lspd_position;
	}
	public void setLspd_position(String lspd_position) {
		this.lspd_position = lspd_position;
	}
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getLh_nama() {
		return lh_nama;
	}
	public void setLh_nama(String lh_nama) {
		this.lh_nama = lh_nama;
	}
	public Double getLh_harga() {
		return lh_harga;
	}
	public void setLh_harga(Double lh_harga) {
		this.lh_harga = lh_harga;
	}
	public Integer getLh_hari() {
		return lh_hari;
	}
	public void setLh_hari(Integer lh_hari) {
		this.lh_hari = lh_hari;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Date getMh_tgl_terima_ajs() {
		return mh_tgl_terima_ajs;
	}
	public void setMh_tgl_terima_ajs(Date mh_tgl_terima_ajs) {
		this.mh_tgl_terima_ajs = mh_tgl_terima_ajs;
	}
	public Integer getMh_flag_kirim() {
		return mh_flag_kirim;
	}
	public void setMh_flag_kirim(Integer mh_flag_kirim) {
		this.mh_flag_kirim = mh_flag_kirim;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getMh_no() {
		return mh_no;
	}
	public void setMh_no(Integer mh_no) {
		this.mh_no = mh_no;
	}
	public Integer getLhc_id() {
		return lhc_id;
	}
	public void setLhc_id(Integer lhc_id) {
		this.lhc_id = lhc_id;
	}
	public Integer getLh_id() {
		return lh_id;
	}
	public void setLh_id(Integer lh_id) {
		this.lh_id = lh_id;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public String getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}
	public Double getMh_budget() {
		return mh_budget;
	}
	public void setMh_budget(Double mh_budget) {
		this.mh_budget = mh_budget;
	}
	public Double getMh_harga() {
		return mh_harga;
	}
	public void setMh_harga(Double mh_harga) {
		this.mh_harga = mh_harga;
	}
	public String getMh_alamat() {
		return mh_alamat;
	}
	public void setMh_alamat(String mh_alamat) {
		this.mh_alamat = mh_alamat;
	}
	public String getMh_kota() {
		return mh_kota;
	}
	public void setMh_kota(String mh_kota) {
		this.mh_kota = mh_kota;
	}
	public String getMh_kodepos() {
		return mh_kodepos;
	}
	public void setMh_kodepos(String mh_kodepos) {
		this.mh_kodepos = mh_kodepos;
	}
	public String getMh_telepon() {
		return mh_telepon;
	}
	public void setMh_telepon(String mh_telepon) {
		this.mh_telepon = mh_telepon;
	}
	public String getMh_keterangan() {
		return mh_keterangan;
	}
	public void setMh_keterangan(String mh_keterangan) {
		this.mh_keterangan = mh_keterangan;
	}
	public Integer getMh_quantity() {
		return mh_quantity;
	}
	public void setMh_quantity(Integer mh_quantity) {
		this.mh_quantity = mh_quantity;
	}
	public Integer getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(Integer lbn_id) {
		this.lbn_id = lbn_id;
	}
	public String getMh_rek_no() {
		return mh_rek_no;
	}
	public void setMh_rek_no(String mh_rek_no) {
		this.mh_rek_no = mh_rek_no;
	}
	public String getMh_rek_nama() {
		return mh_rek_nama;
	}
	public void setMh_rek_nama(String mh_rek_nama) {
		this.mh_rek_nama = mh_rek_nama;
	}
	public Date getMh_tgl_input() {
		return mh_tgl_input;
	}
	public void setMh_tgl_input(Date mh_tgl_input) {
		this.mh_tgl_input = mh_tgl_input;
	}
	public Date getMh_tgl_aksep() {
		return mh_tgl_aksep;
	}
	public void setMh_tgl_aksep(Date mh_tgl_aksep) {
		this.mh_tgl_aksep = mh_tgl_aksep;
	}
	public Date getMh_tgl_paid() {
		return mh_tgl_paid;
	}
	public void setMh_tgl_paid(Date mh_tgl_paid) {
		this.mh_tgl_paid = mh_tgl_paid;
	}
	public Date getMh_tgl_kirim_vendor() {
		return mh_tgl_kirim_vendor;
	}
	public void setMh_tgl_kirim_vendor(Date mh_tgl_kirim_vendor) {
		this.mh_tgl_kirim_vendor = mh_tgl_kirim_vendor;
	}
	public Date getMh_tgl_kirim_ajs() {
		return mh_tgl_kirim_ajs;
	}
	public void setMh_tgl_kirim_ajs(Date mh_tgl_kirim_ajs) {
		this.mh_tgl_kirim_ajs = mh_tgl_kirim_ajs;
	}
	public Date getMh_tgl_terima_nsbh() {
		return mh_tgl_terima_nsbh;
	}
	public void setMh_tgl_terima_nsbh(Date mh_tgl_terima_nsbh) {
		this.mh_tgl_terima_nsbh = mh_tgl_terima_nsbh;
	}
	public Integer getCreate_id() {
		return create_id;
	}
	public void setCreate_id(Integer create_id) {
		this.create_id = create_id;
	}
	public Date getCreate_dt() {
		return create_dt;
	}
	public void setCreate_dt(Date create_dt) {
		this.create_dt = create_dt;
	}
	public String getPolicy_no() {
		return policy_no;
	}
	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}
	public String getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(String beg_date) {
		this.beg_date = beg_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getTgl_aksep() {
		return tgl_aksep;
	}
	public void setTgl_aksep(String tgl_aksep) {
		this.tgl_aksep = tgl_aksep;
	}
	public String getPemegang() {
		return pemegang;
	}
	public void setPemegang(String pemegang) {
		this.pemegang = pemegang;
	}
    public String getLhc_nama() {
		return lhc_nama;
	}
	public void setLhc_nama(String lhc_nama) {
		this.lhc_nama = lhc_nama;
	}
	public Date getMh_tgl_deadline_bayar() {
		return mh_tgl_deadline_bayar;
	}
	public void setMh_tgl_deadline_bayar(Date mh_tgl_deadline_bayar) {
		this.mh_tgl_deadline_bayar = mh_tgl_deadline_bayar;
	}
	
	public Date getMh_tgl_kirim_uw() {
		return mh_tgl_kirim_uw;
	}
	public void setMh_tgl_kirim_uw(Date mh_tgl_kirim_uw) {
		this.mh_tgl_kirim_uw = mh_tgl_kirim_uw;
	}
	public Date getMh_tgl_kirim_purchasing() {
		return mh_tgl_kirim_purchasing;
	}
	public void setMh_tgl_kirim_purchasing(Date mh_tgl_kirim_purchasing) {
		this.mh_tgl_kirim_purchasing = mh_tgl_kirim_purchasing;
	}
	public Date getMh_tgl_kirim_distribution() {
		return mh_tgl_kirim_distribution;
	}
	public void setMh_tgl_kirim_distribution(Date mh_tgl_kirim_distribution) {
		this.mh_tgl_kirim_distribution = mh_tgl_kirim_distribution;
	}
	public Date getMh_tgl_kirim_ga() {
		return mh_tgl_kirim_ga;
	}
	public void setMh_tgl_kirim_ga(Date mh_tgl_kirim_ga) {
		this.mh_tgl_kirim_ga = mh_tgl_kirim_ga;
	}
	public Integer getProgram_hadiah() {
		return program_hadiah;
	}
	public void setProgram_hadiah(Integer program_hadiah) {
		this.program_hadiah = program_hadiah;
	}
	public Date getMh_tgl_order_purchasing() {
		return mh_tgl_order_purchasing;
	}
	public void setMh_tgl_order_purchasing(Date mh_tgl_order_purchasing) {
		this.mh_tgl_order_purchasing = mh_tgl_order_purchasing;
	}
	public Date getMh_tgl_barang_ready() {
		return mh_tgl_barang_ready;
	}
	public void setMh_tgl_barang_ready(Date mh_tgl_barang_ready) {
		this.mh_tgl_barang_ready = mh_tgl_barang_ready;
	}
	public String getMh_sn() {
		return mh_sn;
	}
	public void setMh_sn(String mh_sn) {
		this.mh_sn = mh_sn;
	}
	public Date getMh_tgl_kirim_hadiah() {
		return mh_tgl_kirim_hadiah;
	}
	public void setMh_tgl_kirim_hadiah(Date mh_tgl_kirim_hadiah) {
		this.mh_tgl_kirim_hadiah = mh_tgl_kirim_hadiah;
	}
	public String getMh_tgl_kirim_hadiah_str() {
		return mh_tgl_kirim_hadiah_str;
	}
	public void setMh_tgl_kirim_hadiah_str(String mh_tgl_kirim_hadiah_str) {
		this.mh_tgl_kirim_hadiah_str = mh_tgl_kirim_hadiah_str;
	}
}