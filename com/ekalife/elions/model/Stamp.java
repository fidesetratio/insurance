package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Stamp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mstm_kode;                      
	private String mstm_kode_dirjen;               
	private String mstm_bulan;                     
	private Double mstm_saldo_awal;               
	private Double mstm_saldo_akhir;              
	private Date mstm_create_dt;                   
	private Integer mstm_lus_id;   
	private String msth_ref_no;                    
	private Integer msth_jenis;                    
	private Double msth_jumlah;                   
	private String tanggal1;
	private String tanggal2;
	private String tgl1;
	private String tgl2;
	private String user_id;
	private Double saldo;
	private String kode;
	private String msth_desc; 
	private String status;
	private String tgl_sementara;
	private Date msth_tgl_bayar;
	private String keterangan;
	
	//Untuk Biaya Penggantian Materai
	private String msf_no; 
	private String msf_lcb_no; 
	private String msf_cabang; 
	private String msf_pic; 
	private String msf_norek; 
	private Double msf_nominal; 
	private Integer msf_posisi;
	private String msf_date;
	private Integer msf_flag; 
	private Integer msf_lusid; 
	private String reg_spaj; 
	private Date msf_date_validasi;  
	private Date msf_date_send;  
	private Date msf_date_send2;  
	private Date msf_date_acc;  
	private Date msf_date_dec;  
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMsf_no() {
		return msf_no;
	}
	public void setMsf_no(String msf_no) {
		this.msf_no = msf_no;
	}
	public String getMsf_lcb_no() {
		return msf_lcb_no;
	}
	public void setMsf_lcb_no(String msf_lcb_no) {
		this.msf_lcb_no = msf_lcb_no;
	}
	public String getMsf_cabang() {
		return msf_cabang;
	}
	public void setMsf_cabang(String msf_cabang) {
		this.msf_cabang = msf_cabang;
	}
	public String getMsf_pic() {
		return msf_pic;
	}
	public void setMsf_pic(String msf_pic) {
		this.msf_pic = msf_pic;
	}
	public String getMsf_norek() {
		return msf_norek;
	}
	public void setMsf_norek(String msf_norek) {
		this.msf_norek = msf_norek;
	}
	public Double getMsf_nominal() {
		return msf_nominal;
	}
	public void setMsf_nominal(Double msf_nominal) {
		this.msf_nominal = msf_nominal;
	}
	public Integer getMsf_posisi() {
		return msf_posisi;
	}
	public void setMsf_posisi(Integer msf_posisi) {
		this.msf_posisi = msf_posisi;
	}

	public String getMsf_date() {
		return msf_date;
	}
	public void setMsf_date(String msf_date) {
		this.msf_date = msf_date;
	}
	public Integer getMsf_flag() {
		return msf_flag;
	}
	public void setMsf_flag(Integer msf_flag) {
		this.msf_flag = msf_flag;
	}
	public Integer getMsf_lusid() {
		return msf_lusid;
	}
	public void setMsf_lusid(Integer msf_lusid) {
		this.msf_lusid = msf_lusid;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Date getMsth_tgl_bayar() {
		return msth_tgl_bayar;
	}
	public void setMsth_tgl_bayar(Date msth_tgl_bayar) {
		this.msth_tgl_bayar = msth_tgl_bayar;
	}
	public String getTgl_sementara() {
		return tgl_sementara;
	}
	public void setTgl_sementara(String tgl_sementara) {
		this.tgl_sementara = tgl_sementara;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsth_desc() {
		return msth_desc;
	}
	public void setMsth_desc(String msth_desc) {
		this.msth_desc = msth_desc;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public Integer getMsth_jenis() {
		return msth_jenis;
	}
	public void setMsth_jenis(Integer msth_jenis) {
		this.msth_jenis = msth_jenis;
	}
	public Double getMsth_jumlah() {
		return msth_jumlah;
	}
	public void setMsth_jumlah(Double msth_jumlah) {
		this.msth_jumlah = msth_jumlah;
	}
	public String getMsth_ref_no() {
		return msth_ref_no;
	}
	public void setMsth_ref_no(String msth_ref_no) {
		this.msth_ref_no = msth_ref_no;
	}
	public String getMstm_bulan() {
		return mstm_bulan;
	}
	public void setMstm_bulan(String mstm_bulan) {
		this.mstm_bulan = mstm_bulan;
	}
	public Date getMstm_create_dt() {
		return mstm_create_dt;
	}
	public void setMstm_create_dt(Date mstm_create_dt) {
		this.mstm_create_dt = mstm_create_dt;
	}
	public String getMstm_kode() {
		return mstm_kode;
	}
	public void setMstm_kode(String mstm_kode) {
		this.mstm_kode = mstm_kode;
	}
	public String getMstm_kode_dirjen() {
		return mstm_kode_dirjen;
	}
	public void setMstm_kode_dirjen(String mstm_kode_dirjen) {
		this.mstm_kode_dirjen = mstm_kode_dirjen;
	}
	public Integer getMstm_lus_id() {
		return mstm_lus_id;
	}
	public void setMstm_lus_id(Integer mstm_lus_id) {
		this.mstm_lus_id = mstm_lus_id;
	}
	public Double getMstm_saldo_akhir() {
		return mstm_saldo_akhir;
	}
	public void setMstm_saldo_akhir(Double mstm_saldo_akhir) {
		this.mstm_saldo_akhir = mstm_saldo_akhir;
	}
	public Double getMstm_saldo_awal() {
		return mstm_saldo_awal;
	}
	public void setMstm_saldo_awal(Double mstm_saldo_awal) {
		this.mstm_saldo_awal = mstm_saldo_awal;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public String getTanggal1() {
		return tanggal1;
	}
	public void setTanggal1(String tanggal1) {
		this.tanggal1 = tanggal1;
	}
	public String getTanggal2() {
		return tanggal2;
	}
	public void setTanggal2(String tanggal2) {
		this.tanggal2 = tanggal2;
	}
	public String getTgl1() {
		return tgl1;
	}
	public void setTgl1(String tgl1) {
		this.tgl1 = tgl1;
	}
	public String getTgl2() {
		return tgl2;
	}
	public void setTgl2(String tgl2) {
		this.tgl2 = tgl2;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getMsf_date_validasi() {
		return msf_date_validasi;
	}
	public void setMsf_date_validasi(Date msf_date_validasi) {
		this.msf_date_validasi = msf_date_validasi;
	}
	public Date getMsf_date_send() {
		return msf_date_send;
	}
	public void setMsf_date_send(Date msf_date_send) {
		this.msf_date_send = msf_date_send;
	}
	public Date getMsf_date_send2() {
		return msf_date_send2;
	}
	public void setMsf_date_send2(Date msf_date_send2) {
		this.msf_date_send2 = msf_date_send2;
	}
	public Date getMsf_date_acc() {
		return msf_date_acc;
	}
	public void setMsf_date_acc(Date msf_date_acc) {
		this.msf_date_acc = msf_date_acc;
	}
	public Date getMsf_date_dec() {
		return msf_date_dec;
	}
	public void setMsf_date_dec(Date msf_date_dec) {
		this.msf_date_dec = msf_date_dec;
	}

	
}
