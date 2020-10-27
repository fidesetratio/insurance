package com.ekalife.elions.model.tts;

import java.io.Serializable;
import java.util.Date;

public class Tts implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String lst_kd_cab;
	private String lca_nama;
	private String mst_no;
	private String mst_noFormated;
	private String mst_ket;
	private Date mst_tgl_input;
	private Date mst_tgl_rk;
	private String lus_full_name;
	private Integer mst_flag_batal;
	private String mst_nm_pemegang;
	private Integer flag_print;
	private Integer lus_id;
	private String mst_no_reff_btl;
	private String mst_no_reff_new;
	private Date mst_tgl_setor;
	private String mst_no_telp;
	private Integer size;
	public Integer getFlag_print() {
		return flag_print;
	}
	public void setFlag_print(Integer flag_print) {
		this.flag_print = flag_print;
	}
	public String getLst_kd_cab() {
		return lst_kd_cab;
	}
	public void setLst_kd_cab(String lst_kd_cab) {
		this.lst_kd_cab = lst_kd_cab;
	}
	public String getLus_full_name() {
		return lus_full_name;
	}
	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}
	public Integer getMst_flag_batal() {
		return mst_flag_batal;
	}
	public void setMst_flag_batal(Integer mst_flag_batal) {
		this.mst_flag_batal = mst_flag_batal;
	}
	public String getMst_ket() {
		return mst_ket;
	}
	public void setMst_ket(String mst_ket) {
		this.mst_ket = mst_ket;
	}
	public String getMst_nm_pemegang() {
		return mst_nm_pemegang;
	}
	public void setMst_nm_pemegang(String mst_nm_pemegang) {
		this.mst_nm_pemegang = mst_nm_pemegang;
	}
	public String getMst_no() {
		return mst_no;
	}
	public void setMst_no(String mst_no) {
		this.mst_no = mst_no;
	}
	public Date getMst_tgl_input() {
		return mst_tgl_input;
	}
	public void setMst_tgl_input(Date mst_tgl_input) {
		this.mst_tgl_input = mst_tgl_input;
	}
	public Date getMst_tgl_rk() {
		return mst_tgl_rk;
	}
	public void setMst_tgl_rk(Date mst_tgl_rk) {
		this.mst_tgl_rk = mst_tgl_rk;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getMst_noFormated() {
		return mst_noFormated;
	}
	public void setMst_noFormated(String mst_noFormated) {
		this.mst_noFormated = mst_noFormated;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Date getMst_tgl_setor() {
		return mst_tgl_setor;
	}
	public void setMst_tgl_setor(Date mst_tgl_setor) {
		this.mst_tgl_setor = mst_tgl_setor;
	}
	public String getMst_no_reff_btl() {
		return mst_no_reff_btl;
	}
	public void setMst_no_reff_btl(String mst_no_reff_btl) {
		this.mst_no_reff_btl = mst_no_reff_btl;
	}
	public String getMst_no_reff_new() {
		return mst_no_reff_new;
	}
	public void setMst_no_reff_new(String mst_no_reff_new) {
		this.mst_no_reff_new = mst_no_reff_new;
	}
	public String getLca_nama() {
		return lca_nama;
	}
	public void setLca_nama(String lca_nama) {
		this.lca_nama = lca_nama;
	}
	public String getMst_no_telp() {
		return mst_no_telp;
	}
	public void setMst_no_telp(String mst_no_telp) {
		this.mst_no_telp = mst_no_telp;
	}
	
}
