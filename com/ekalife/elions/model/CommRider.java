package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * mst_comm_rider
 * 
 * @author Yusuf
 * @since Apr 28, 2008 (5:41:41 PM)
 */
public class CommRider implements Serializable {
	private String reg_spaj;
	private Integer msbi_tahun_ke;
	private Integer msbi_premi_ke;
	private Integer msdb_det_ke;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Integer level_id;
	private Integer flag_reward;
	private String msag_id;
	private Double komisi;
	private Double pajak;
	private Date tgl_komisi;
	private Integer flag_bayar;
	private Date tgl_bayar;
	private Integer nilai_kurs;
	private Date tgl_proses;
	private String nama;
	private String no_account;
	private Integer lbn_id;
	
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Integer getMsdb_det_ke() {
		return msdb_det_ke;
	}
	public void setMsdb_det_ke(Integer msdb_det_ke) {
		this.msdb_det_ke = msdb_det_ke;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}
	public Integer getFlag_reward() {
		return flag_reward;
	}
	public void setFlag_reward(Integer flag_reward) {
		this.flag_reward = flag_reward;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public Double getKomisi() {
		return komisi;
	}
	public void setKomisi(Double komisi) {
		this.komisi = komisi;
	}
	public Double getPajak() {
		return pajak;
	}
	public void setPajak(Double pajak) {
		this.pajak = pajak;
	}
	public Date getTgl_komisi() {
		return tgl_komisi;
	}
	public void setTgl_komisi(Date tgl_komisi) {
		this.tgl_komisi = tgl_komisi;
	}
	public Integer getFlag_bayar() {
		return flag_bayar;
	}
	public void setFlag_bayar(Integer flag_bayar) {
		this.flag_bayar = flag_bayar;
	}
	public Date getTgl_bayar() {
		return tgl_bayar;
	}
	public void setTgl_bayar(Date tgl_bayar) {
		this.tgl_bayar = tgl_bayar;
	}
	public Integer getNilai_kurs() {
		return nilai_kurs;
	}
	public void setNilai_kurs(Integer nilai_kurs) {
		this.nilai_kurs = nilai_kurs;
	}
	public Date getTgl_proses() {
		return tgl_proses;
	}
	public void setTgl_proses(Date tgl_proses) {
		this.tgl_proses = tgl_proses;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNo_account() {
		return no_account;
	}
	public void setNo_account(String no_account) {
		this.no_account = no_account;
	}
	public Integer getLbn_id() {
		return lbn_id;
	}
	public void setLbn_id(Integer lbn_id) {
		this.lbn_id = lbn_id;
	}
	
}
