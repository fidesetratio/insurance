package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class CmdSpajExpired implements Serializable {

	private static final long serialVersionUID = 297007044580546629L;

	private String spaj;
	
	private List<String> errorMessages;
	
	private MultipartFile file1;
	private MultipartFile file2;
	private MultipartFile file3;

	private String reg_spaj;
	private String mcl_first;
	private String proses_terakhir;
	private String no_rek;
	private String atas_nama;
	private String nama_bank;
	private String kota;
	private Integer jumlah;

	private String form;

	private String emailto;

	private String emailcc;

	private String emailsubject;

	private String emailmessage;

	private List<UploadSpaj> daftarSpaj;

	private List<DropDown> daftarStatus;
	
	public List<DropDown> getDaftarStatus() {
		return daftarStatus;
	}

	public void setDaftarStatus(List<DropDown> daftarStatus) {
		this.daftarStatus = daftarStatus;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public List<UploadSpaj> getDaftarSpaj() {
		return daftarSpaj;
	}

	public void setDaftarSpaj(List<UploadSpaj> daftarSpaj) {
		this.daftarSpaj = daftarSpaj;
	}

	public String getSpaj() {
		return spaj;
	}

	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	
	public String getProses_terakhir() {
		return proses_terakhir;
	}
	public void setProses_terakhir(String proses_terakhir) {
		this.proses_terakhir = proses_terakhir;
	}
	
	public String getNo_rek() {
		return no_rek;
	}
	public void setNo_rek(String no_rek) {
		this.no_rek = no_rek;
	}
	
	public String getAtas_nama() {
		return atas_nama;
	}
	public void setAtas_nama(String atas_nama) {
		this.atas_nama = atas_nama;
	}	
	
	public String getNama_bank() {
		return nama_bank;
	}
	public void setNama_bank(String nama_bank) {
		this.nama_bank = nama_bank;
	}	
	
	public String getKota() {
		return kota;
	}
	public void setKota(String kota) {
		this.kota = kota;
	}		

	public Integer getJumlah(){
		return jumlah;
	}
	public void setJumlah(Integer jumlah){
		this.jumlah = jumlah;
	}
	
	public MultipartFile getFile1() {
		return file1;
	}

	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}

	public MultipartFile getFile2() {
		return file2;
	}

	public void setFile2(MultipartFile file2) {
		this.file2 = file2;
	}

	public MultipartFile getFile3() {
		return file3;
	}

	public void setFile3(MultipartFile file3) {
		this.file3 = file3;
	}

	public String getEmailcc() {
		return emailcc;
	}

	public void setEmailcc(String emailcc) {
		this.emailcc = emailcc;
	}

	public String getEmailmessage() {
		return emailmessage;
	}

	public void setEmailmessage(String emailmessage) {
		this.emailmessage = emailmessage;
	}

	public String getEmailsubject() {
		return emailsubject;
	}

	public void setEmailsubject(String emailsubject) {
		this.emailsubject = emailsubject;
	}

	public String getEmailto() {
		return emailto;
	}

	public void setEmailto(String emailto) {
		this.emailto = emailto;
	}

}
