package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Upload implements Serializable {

	/**
	 * Author Ferry Harlim
	 * Created :Jan 9, 2007  
	 */
	private static final long serialVersionUID = 1L;
	
	private MultipartFile file1;
	private MultipartFile file2;
	private MultipartFile file3;
	private MultipartFile file4;
	private MultipartFile file5;
	private MultipartFile file6;
	private MultipartFile file7;
	private MultipartFile file8;
	private MultipartFile file9;
	private MultipartFile file10;
	
	private List<MultipartFile> daftarFile;	
	
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
	public List<MultipartFile> getDaftarFile() {
		return daftarFile;
	}
	public void setDaftarFile(List<MultipartFile> daftarFile) {
		this.daftarFile = daftarFile;
	}
	public MultipartFile getFile10() {
		return file10;
	}
	public void setFile10(MultipartFile file10) {
		this.file10 = file10;
	}
	public MultipartFile getFile4() {
		return file4;
	}
	public void setFile4(MultipartFile file4) {
		this.file4 = file4;
	}
	public MultipartFile getFile5() {
		return file5;
	}
	public void setFile5(MultipartFile file5) {
		this.file5 = file5;
	}
	public MultipartFile getFile6() {
		return file6;
	}
	public void setFile6(MultipartFile file6) {
		this.file6 = file6;
	}
	public MultipartFile getFile7() {
		return file7;
	}
	public void setFile7(MultipartFile file7) {
		this.file7 = file7;
	}
	public MultipartFile getFile8() {
		return file8;
	}
	public void setFile8(MultipartFile file8) {
		this.file8 = file8;
	}
	public MultipartFile getFile9() {
		return file9;
	}
	public void setFile9(MultipartFile file9) {
		this.file9 = file9;
	}
			
}