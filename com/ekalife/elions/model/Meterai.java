package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object (DTO) untuk meterai, tersimpan di sebuah tabel master-detail
 * yaitu MST_STAMP dan detailnya di MST_STAMP_HIST
 * 
 * @author Yusuf Sutarko
 * @since Feb 15, 2007 (11:39:20 AM)
 */
public class Meterai implements Serializable {

	//Detail untuk MST_STAMP
	private String mstm_kode;                      
	private String mstm_kode_dirjen;               
	private String mstm_bulan;                     
	private Double mstm_saldo_awal;               
	private Double mstm_saldo_akhir;              
	private Date mstm_create_dt;                   
	private Integer mstm_lus_id;

	//Detail untuk MST_STAMP_HIST
	private String msth_ref_no;                    
	private Integer msth_jenis;                    
	private Double msth_jumlah;                   
	private String msth_desc;                      
	private Date msth_tgl_bayar;      	

	public String getMsth_desc() {
		return msth_desc;
	}
	public void setMsth_desc(String msth_desc) {
		this.msth_desc = msth_desc;
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
	public Date getMsth_tgl_bayar() {
		return msth_tgl_bayar;
	}
	public void setMsth_tgl_bayar(Date msth_tgl_bayar) {
		this.msth_tgl_bayar = msth_tgl_bayar;
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

}