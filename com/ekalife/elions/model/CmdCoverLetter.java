package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.ekalife.utils.Common;

public class CmdCoverLetter implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String pesan;
	private String lus_id;
	private String jenis_user;
	private Integer jenis_report;
	private Integer branch;
	private Integer status_polis;
	private Date tglcetak_awal;
	private Date tglcetak_akhir;
	private Date tglproses_awal;
	private Date tglproses_akhir;
	private CoverLetter coverletter;
	private ArrayList<CoverLetter> datapolis;
	
	public String[] input_resi;
	public Integer[] cek_polis;
	public Integer[] cek_simascard;
	public Integer[] cek_admedika;
	
	private String spajproses;
	private Integer jalur_dist;
	
	
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public String getJenis_user() {
		return jenis_user;
	}
	public void setJenis_user(String jenis_user) {
		this.jenis_user = jenis_user;
	}
	public Integer getJenis_report() {
		return jenis_report;
	}
	public void setJenis_report(Integer jenis_report) {
		this.jenis_report = jenis_report;
	}
	public Integer getBranch() {
		return branch;
	}
	public void setBranch(Integer branch) {
		this.branch = branch;
	}
	public Integer getStatus_polis() {
		return status_polis;
	}
	public void setStatus_polis(Integer status_polis) {
		this.status_polis = status_polis;
	}
	public Date getTglcetak_awal() {
		return tglcetak_awal;
	}
	public void setTglcetak_awal(Date tglcetak_awal) {
		this.tglcetak_awal = tglcetak_awal;
	}
	public Date getTglcetak_akhir() {
		return tglcetak_akhir;
	}
	public void setTglcetak_akhir(Date tglcetak_akhir) {
		this.tglcetak_akhir = tglcetak_akhir;
	}
	public Date getTglproses_awal() {
		return tglproses_awal;
	}
	public void setTglproses_awal(Date tglproses_awal) {
		this.tglproses_awal = tglproses_awal;
	}
	public Date getTglproses_akhir() {
		return tglproses_akhir;
	}
	public void setTglproses_akhir(Date tglproses_akhir) {
		this.tglproses_akhir = tglproses_akhir;
	}
	public CoverLetter getCoverletter() {
		return coverletter;
	}
	public void setCoverletter(CoverLetter coverletter) {
		this.coverletter = coverletter;
	}
	public ArrayList<CoverLetter> getDatapolis() {
		return datapolis;
	}
	public void setDatapolis(ArrayList<CoverLetter> datapolis) {
		this.datapolis = Common.serializableList(datapolis);
	}
	public Integer[] getCek_polis() {
		return cek_polis;
	}
	public void setCek_polis(Integer[] cek_polis) {
		this.cek_polis = cek_polis;
	}
	public Integer[] getCek_simascard() {
		return cek_simascard;
	}
	public void setCek_simascard(Integer[] cek_simascard) {
		this.cek_simascard = cek_simascard;
	}
	public Integer[] getCek_admedika() {
		return cek_admedika;
	}
	public void setCek_admedika(Integer[] cek_admedika) {
		this.cek_admedika = cek_admedika;
	}
	public String[] getInput_resi() {
		return input_resi;
	}
	public void setInput_resi(String[] input_resi) {
		this.input_resi = input_resi;
	}
	public String getSpajproses() {
		return spajproses;
	}
	public void setSpajproses(String spajproses) {
		this.spajproses = spajproses;
	}
	public Integer getJalur_dist() {
		return jalur_dist;
	}
	public void setJalur_dist(Integer jalur_dist) {
		this.jalur_dist = jalur_dist;
	}
}
