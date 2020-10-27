package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.List;

import com.ekalife.elions.model.cross_selling.Fat;
import com.ekalife.elions.model.cross_selling.FatHistory;
import com.ekalife.elions.model.cross_selling.FatLetter;

/**
 * Model Class untuk inputan FAT
 * 
 * @author Yusuf
 * @since Apr 27, 2011 (6:50:06 PM)
 *
 */
public class CommandFat implements Serializable{

	private static final long serialVersionUID = 6004434710214814828L;

	public String begdate;
	public String enddate;
	public String nama;
	public String pesan;
	public Integer posisi;
	public List<DropDown> listPosisi;
	public List<FatHistory> listFatHistory;
	
	//Tambahan Antasari
	public String ket_term;

	//model data
	public Fat fat;
	public FatLetter fatLetter;
	public List<Fat> listFat;
	
	public List<FatHistory> getListFatHistory() {
		return listFatHistory;
	}
	public void setListFatHistory(List<FatHistory> listFatHistory) {
		this.listFatHistory = listFatHistory;
	}
	public List<DropDown> getListPosisi() {
		return listPosisi;
	}
	public void setListPosisi(List<DropDown> listPosisi) {
		this.listPosisi = listPosisi;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public String getBegdate() {
		return begdate;
	}
	public void setBegdate(String begdate) {
		this.begdate = begdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Fat getFat() {
		return fat;
	}
	public void setFat(Fat fat) {
		this.fat = fat;
	}
	public FatLetter getFatLetter() {
		return fatLetter;
	}
	public void setFatLetter(FatLetter fatLetter) {
		this.fatLetter = fatLetter;
	}
	public List<Fat> getListFat() {
		return listFat;
	}
	public void setListFat(List<Fat> listFat) {
		this.listFat = listFat;
	}

	public String getKet_term() {
		return ket_term;
	}
	public void setKet_term(String ket_term) {
		this.ket_term = ket_term;
	}
}