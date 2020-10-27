package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class Promo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String spaj_utama;
	private Integer row_num;
	private String spaj_free;
	private Integer process_type;
	private Integer program_type;
	private String tgl_transfer_uw_print;
	private Integer posisi_spaj_utama_id;
	private String posisi_spaj_utama;
	private Integer posisi_spaj_free_id;
	private String posisi_spaj_free;
	private BigDecimal ape_manual;
	private BigDecimal ape_system;
	private BigDecimal nilai_ape;
	private String lcb_no;
	private String nama_cabang;
	private String wil_no;
	private String nama_kanwil;
	private String status;
	private String kurs;
	private Integer check;
	
	public String getSpaj_utama() {
		return spaj_utama;
	}
	public void setSpaj_utama(String spaj_utama) {
		this.spaj_utama = spaj_utama;
	}
	public Integer getRow_num() {
		return row_num;
	}
	public void setRow_num(Integer row_num) {
		this.row_num = row_num;
	}
	public String getSpaj_free() {
		return spaj_free;
	}
	public void setSpaj_free(String spaj_free) {
		this.spaj_free = spaj_free;
	}
	public Integer getProcess_type() {
		return process_type;
	}
	public void setProcess_type(Integer process_type) {
		this.process_type = process_type;
	}
	public Integer getProgram_type() {
		return program_type;
	}
	public void setProgram_type(Integer program_type) {
		this.program_type = program_type;
	}
	public String getTgl_transfer_uw_print() {
		return tgl_transfer_uw_print;
	}
	public void setTgl_transfer_uw_print(String tgl_transfer_uw_print) {
		this.tgl_transfer_uw_print = tgl_transfer_uw_print;
	}
	public Integer getPosisi_spaj_utama_id() {
		return posisi_spaj_utama_id;
	}
	public void setPosisi_spaj_utama_id(Integer posisi_spaj_utama_id) {
		this.posisi_spaj_utama_id = posisi_spaj_utama_id;
	}
	public String getPosisi_spaj_utama() {
		return posisi_spaj_utama;
	}
	public void setPosisi_spaj_utama(String posisi_spaj_utama) {
		this.posisi_spaj_utama = posisi_spaj_utama;
	}
	public Integer getPosisi_spaj_free_id() {
		return posisi_spaj_free_id;
	}
	public void setPosisi_spaj_free_id(Integer posisi_spaj_free_id) {
		this.posisi_spaj_free_id = posisi_spaj_free_id;
	}
	public String getPosisi_spaj_free() {
		return posisi_spaj_free;
	}
	public void setPosisi_spaj_free(String posisi_spaj_free) {
		this.posisi_spaj_free = posisi_spaj_free;
	}
	public BigDecimal getApe_manual() {
		return ape_manual;
	}
	public void setApe_manual(BigDecimal ape_manual) {
		this.ape_manual = ape_manual;
	}
	public BigDecimal getApe_system() {
		return ape_system;
	}
	public void setApe_system(BigDecimal ape_system) {
		this.ape_system = ape_system;
	}
	public BigDecimal getNilai_ape() {
		return nilai_ape;
	}
	public void setNilai_ape(BigDecimal nilai_ape) {
		this.nilai_ape = nilai_ape;
	}
	public String getLcb_no() {
		return lcb_no;
	}
	public void setLcb_no(String lcb_no) {
		this.lcb_no = lcb_no;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public String getWil_no() {
		return wil_no;
	}
	public void setWil_no(String wil_no) {
		this.wil_no = wil_no;
	}
	public String getNama_kanwil() {
		return nama_kanwil;
	}
	public void setNama_kanwil(String nama_kanwil) {
		this.nama_kanwil = nama_kanwil;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getKurs() {
		return kurs;
	}
	public void setKurs(String kurs) {
		this.kurs = kurs;
	}
	public Integer getCheck() {
		return check;
	}
	public void setCheck(Integer check) {
		this.check = check;
	}
	
	
}
