package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class CommandUploadUw implements Serializable {
	private static final long serialVersionUID = 297007044580546629L;
	
	private List<String> errorMessages;
	
	private MultipartFile file1;
	private List<Icd> daftarStatus;
	
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	public MultipartFile getFile1() {
		return file1;
	}
	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}
	public List<Icd> getDaftarStatus() {
		return daftarStatus;
	}
	public void setDaftarStatus(List<Icd> daftarStatus) {
		this.daftarStatus = daftarStatus;
	}

}
