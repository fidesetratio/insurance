package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//untuk table mst_kartu_nama
public class KartuNama implements Serializable {

	private static final long serialVersionUID = 6956047510784865739L;
	
	private String mkn_id;
	private String msag_id;
	private Integer mkn_type;
	private Date mkn_create_date;
	private String mkn_lus_id;
	private String mkn_document;
	private Integer mkn_approve;
	private Integer mkn_position;
	private String position_desc;
	private List<KartuNama> daftarKartuNama;
	private List<KartuNama> daftarNoKartuNama;
	private String jenis;
	private String submitMode;
	private List<KartuNama> uploadKartuNama;
	private String approve;
	private String position;
	private String mcl_first;
	private String user_input;
	private String keterangan;
	private Integer mode;
	private Integer total_permintaan;
	private List<KartuNama> historyKartuNama;
	private String telp;
	
	public String getTelp() {
		return telp;
	}
	public void setTelp(String telp) {
		this.telp = telp;
	}
	public List<KartuNama> getHistoryKartuNama() {
		return historyKartuNama;
	}
	public void setHistoryKartuNama(List<KartuNama> historyKartuNama) {
		this.historyKartuNama = historyKartuNama;
	}
	public Integer getTotal_permintaan() {
		return total_permintaan;
	}
	public void setTotal_permintaan(Integer total_permintaan) {
		this.total_permintaan = total_permintaan;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getUser_input() {
		return user_input;
	}
	public void setUser_input(String user_input) {
		this.user_input = user_input;
	}
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public List<KartuNama> getUploadKartuNama() {
		return uploadKartuNama;
	}
	public void setUploadKartuNama(List<KartuNama> uploadKartuNama) {
		this.uploadKartuNama = uploadKartuNama;
	}
	public String getSubmitMode() {
		return submitMode;
	}
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}
	public String getJenis() {
		return jenis;
	}
	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	public List<KartuNama> getDaftarNoKartuNama() {
		return daftarNoKartuNama;
	}
	public void setDaftarNoKartuNama(List<KartuNama> daftarNoKartuNama) {
		this.daftarNoKartuNama = daftarNoKartuNama;
	}
	public String getMkn_id() {
		return mkn_id;
	}
	public void setMkn_id(String mkn_id) {
		this.mkn_id = mkn_id;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public Integer getMkn_type() {
		return mkn_type;
	}
	public void setMkn_type(Integer mkn_type) {
		this.mkn_type = mkn_type;
	}
	public Date getMkn_create_date() {
		return mkn_create_date;
	}
	public void setMkn_create_date(Date mkn_create_date) {
		this.mkn_create_date = mkn_create_date;
	}
	public String getMkn_lus_id() {
		return mkn_lus_id;
	}
	public void setMkn_lus_id(String mkn_lus_id) {
		this.mkn_lus_id = mkn_lus_id;
	}
	public String getMkn_document() {
		return mkn_document;
	}
	public void setMkn_document(String mkn_document) {
		this.mkn_document = mkn_document;
	}
	public Integer getMkn_approve() {
		return mkn_approve;
	}
	public void setMkn_approve(Integer mkn_approve) {
		this.mkn_approve = mkn_approve;
	}
	public Integer getMkn_position() {
		return mkn_position;
	}
	public void setMkn_position(Integer mkn_position) {
		this.mkn_position = mkn_position;
	}
	public String getPosition_desc() {
		return position_desc;
	}
	public void setPosition_desc(String position_desc) {
		this.position_desc = position_desc;
	}
	public List<KartuNama> getDaftarKartuNama() {
		return daftarKartuNama;
	}
	public void setDaftarKartuNama(List<KartuNama> daftarKartuNama) {
		this.daftarKartuNama = daftarKartuNama;
	}
}