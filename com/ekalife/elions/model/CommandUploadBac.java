package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class CommandUploadBac implements Serializable {

	private static final long serialVersionUID = 297007044580546629L;

	private String spaj;
	
	private List<String> errorMessages;
	
	private MultipartFile file1;
	private MultipartFile file2;
	private MultipartFile file3;

	private String mode;

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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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
