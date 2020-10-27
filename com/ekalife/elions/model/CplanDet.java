package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CplanDet implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2120981781545177444L;
	
	private String no_id;		
	private Integer urut;	
	private String nama_peserta;
	private String sex;	
	private String tmp_lahir_peserta;;
	private Date bod_peserta;	
	private Integer umur;
	private String address1_peserta;	
	private String address2_peserta;	
	private Integer relasi;
	private String nama_ibu;	
	private String product_code;
	private Integer plan;
	private Date beg_aktif;
	private Date end_aktif;
	private Date beg_date;
	private Integer flag_aksep;	
	private Integer user_aksep;		
	private String notes;		
	private Integer flag_aktif;
	private Integer flag_claim;
	private Integer flag_pk;
	private Integer flag_batal;
	private Date tgl_batal;
	private Integer user_batal;	
	private String alasan_batal;	
	private Date tgl_aksep;
	private Integer premi;
	
	//=========================
	
	private Integer status;

	public String getNo_id() {
		return no_id;
	}

	public void setNo_id(String noId) {
		no_id = noId;
	}

	public Integer getUrut() {
		return urut;
	}

	public void setUrut(Integer urut) {
		this.urut = urut;
	}

	public String getNama_peserta() {
		return nama_peserta;
	}

	public void setNama_peserta(String namaPeserta) {
		nama_peserta = namaPeserta;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTmp_lahir_peserta() {
		return tmp_lahir_peserta;
	}

	public void setTmp_lahir_peserta(String tmpLahirPeserta) {
		tmp_lahir_peserta = tmpLahirPeserta;
	}

	public Date getBod_peserta() {
		return bod_peserta;
	}

	public void setBod_peserta(Date bodPeserta) {
		bod_peserta = bodPeserta;
	}

	public Integer getUmur() {
		return umur;
	}

	public void setUmur(Integer umur) {
		this.umur = umur;
	}

	public String getAddress1_peserta() {
		return address1_peserta;
	}

	public void setAddress1_peserta(String address1Peserta) {
		address1_peserta = address1Peserta;
	}

	public String getAddress2_peserta() {
		return address2_peserta;
	}

	public void setAddress2_peserta(String address2Peserta) {
		address2_peserta = address2Peserta;
	}

	public Integer getRelasi() {
		return relasi;
	}

	public void setRelasi(Integer relasi) {
		this.relasi = relasi;
	}

	public String getNama_ibu() {
		return nama_ibu;
	}

	public void setNama_ibu(String namaIbu) {
		nama_ibu = namaIbu;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String productCode) {
		product_code = productCode;
	}

	public Integer getPlan() {
		return plan;
	}

	public void setPlan(Integer plan) {
		this.plan = plan;
	}

	public Date getBeg_aktif() {
		return beg_aktif;
	}

	public void setBeg_aktif(Date begAktif) {
		beg_aktif = begAktif;
	}

	public Date getEnd_aktif() {
		return end_aktif;
	}

	public void setEnd_aktif(Date endAktif) {
		end_aktif = endAktif;
	}

	public Date getBeg_date() {
		return beg_date;
	}

	public void setBeg_date(Date begDate) {
		beg_date = begDate;
	}

	public Integer getFlag_aksep() {
		return flag_aksep;
	}

	public void setFlag_aksep(Integer flagAksep) {
		flag_aksep = flagAksep;
	}

	public Integer getUser_aksep() {
		return user_aksep;
	}

	public void setUser_aksep(Integer userAksep) {
		user_aksep = userAksep;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getFlag_aktif() {
		return flag_aktif;
	}

	public void setFlag_aktif(Integer flagAktif) {
		flag_aktif = flagAktif;
	}

	public Integer getFlag_claim() {
		return flag_claim;
	}

	public void setFlag_claim(Integer flagClaim) {
		flag_claim = flagClaim;
	}

	public Integer getFlag_pk() {
		return flag_pk;
	}

	public void setFlag_pk(Integer flagPk) {
		flag_pk = flagPk;
	}

	public Integer getFlag_batal() {
		return flag_batal;
	}

	public void setFlag_batal(Integer flagBatal) {
		flag_batal = flagBatal;
	}

	public Date getTgl_batal() {
		return tgl_batal;
	}

	public void setTgl_batal(Date tglBatal) {
		tgl_batal = tglBatal;
	}

	public Integer getUser_batal() {
		return user_batal;
	}

	public void setUser_batal(Integer userBatal) {
		user_batal = userBatal;
	}

	public String getAlasan_batal() {
		return alasan_batal;
	}

	public void setAlasan_batal(String alasanBatal) {
		alasan_batal = alasanBatal;
	}

	public Date getTgl_aksep() {
		return tgl_aksep;
	}

	public void setTgl_aksep(Date tglAksep) {
		tgl_aksep = tglAksep;
	}

	public Integer getPremi() {
		return premi;
	}

	public void setPremi(Integer premi) {
		this.premi = premi;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}	
	
	
	

}
