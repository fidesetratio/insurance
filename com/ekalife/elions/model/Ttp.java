/**
 * @author  : Ferry Harlim
 * @created : Apr 3, 2007 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Ttp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String spaj;
	private Integer flagKomisi;
	private String lcaId;
	private	String msagId;
	private String info;
	private Integer proses;
	private Integer fax;
	private Date tgl;
	private String posisi;
	private boolean simpan;
	private Integer lsle_id;
	private String msag_tabungan;
	private String lev_comm;
	private Integer flagWC;
	
	public Integer getFlagWC() {
		return flagWC;
	}
	public void setFlagWC(Integer flagWC) {
		this.flagWC = flagWC;
	}
	public String getLev_comm() {
		return lev_comm;
	}
	public void setLev_comm(String lev_comm) {
		this.lev_comm = lev_comm;
	}
	public String getMsag_tabungan() {
		return msag_tabungan;
	}
	public void setMsag_tabungan(String msag_tabungan) {
		this.msag_tabungan = msag_tabungan;
	}
	public Integer getLsle_id() {
		return lsle_id;
	}
	public void setLsle_id(Integer lsle_id) {
		this.lsle_id = lsle_id;
	}
	public boolean isSimpan() {
		return simpan;
	}
	public void setSimpan(boolean simpan) {
		this.simpan = simpan;
	}
	public Integer getFax() {
		return fax;
	}
	public void setFax(Integer fax) {
		this.fax = fax;
	}
	public Integer getFlagKomisi() {
		return flagKomisi;
	}
	public void setFlagKomisi(Integer flagKomisi) {
		this.flagKomisi = flagKomisi;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getLcaId() {
		return lcaId;
	}
	public void setLcaId(String lcaId) {
		this.lcaId = lcaId;
	}
	public String getMsagId() {
		return msagId;
	}
	public void setMsagId(String msagId) {
		this.msagId = msagId;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	public Integer getProses() {
		return proses;
	}
	public void setProses(Integer proses) {
		this.proses = proses;
	}
	public String getSpaj() {
		return spaj;
	}
	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}
	public String getPosisi() {
		return posisi;
	}
	public void setPosisi(String posisi) {
		this.posisi = posisi;
	}

	
}
