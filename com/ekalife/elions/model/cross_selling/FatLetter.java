package com.ekalife.elions.model.cross_selling;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Class untuk LST_FAT_LETTER (database HRD)
 * 
 * @author Yusuf
 * @since Apr 27, 2011 (5:53:30 PM)
 *
 */
public class FatLetter implements Serializable{

	private static final long serialVersionUID = -6763293506652281670L;

	public String fatid;
	public String generate_surat;
	public Integer no_surat_generate;
	public Date date_create;
	public String nik_create;
	public Integer freq_view;
	public String list_date;
	public String list_nik;
	public String bulan;
	public String tahun;
	
	public String getFatid() {
		return fatid;
	}
	public void setFatid(String fatid) {
		this.fatid = fatid;
	}
	public String getGenerate_surat() {
		return generate_surat;
	}
	public void setGenerate_surat(String generate_surat) {
		this.generate_surat = generate_surat;
	}
	public Integer getNo_surat_generate() {
		return no_surat_generate;
	}
	public void setNo_surat_generate(Integer no_surat_generate) {
		this.no_surat_generate = no_surat_generate;
	}
	public Date getDate_create() {
		return date_create;
	}
	public void setDate_create(Date date_create) {
		this.date_create = date_create;
	}
	public String getNik_create() {
		return nik_create;
	}
	public void setNik_create(String nik_create) {
		this.nik_create = nik_create;
	}
	public Integer getFreq_view() {
		return freq_view;
	}
	public void setFreq_view(Integer freq_view) {
		this.freq_view = freq_view;
	}
	public String getList_date() {
		return list_date;
	}
	public void setList_date(String list_date) {
		this.list_date = list_date;
	}
	public String getList_nik() {
		return list_nik;
	}
	public void setList_nik(String list_nik) {
		this.list_nik = list_nik;
	}
	public String getBulan() {
		return bulan;
	}
	public void setBulan(String bulan) {
		this.bulan = bulan;
	}
	public String getTahun() {
		return tahun;
	}
	public void setTahun(String tahun) {
		this.tahun = tahun;
	}
	
}