package com.ekalife.elions.vo;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Bfa;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Kesehatan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class BacSpajParamVO {
	PdfContentByte over; 
	Pemegang pemegang; 
	Tertanggung tertanggung; 
	Kesehatan medical;
	Integer x;
	Integer y;
	Integer f;
	Datausulan datausulan;
	Powersave data_pwr;
	Rekening_client data_rek;
	Agen dataagen;
	InvestasiUtama inv;
	PdfReader reader;
	String dir ;
	String file ;
	PdfStamper stamp ;
	BaseFont bf;
	String nm_product;
	String noBlanko;
	

	
	public String getNoBlanko() {
		return noBlanko;
	}
	public void setNoBlanko(String noBlanko) {
		this.noBlanko = noBlanko;
	}
	/**
	 * Dian natalia
	 * param yang digunakan u/ download spaj online
	 * @return
	 */
	public String getNm_product() {
		return nm_product;
	}
	public void setNm_product(String nm_product) {
		this.nm_product = nm_product;
	}
	
	public BaseFont getBf() {
		return bf;
	}
	public void setBf(BaseFont bf) {
		this.bf = bf;
	}
	public PdfStamper getStamp() {
		return stamp;
	}
	public void setStamp(PdfStamper stamp) {
		this.stamp = stamp;
	}
	public InvestasiUtama getInv() {
		return inv;
	}
	public void setInv(InvestasiUtama inv) {
		this.inv = inv;
	}

	public Powersave getData_pwr() {
		return data_pwr;
	}
	public void setData_pwr(Powersave data_pwr) {
		this.data_pwr = data_pwr;
	}
	public Rekening_client getData_rek() {
		return data_rek;
	}
	public void setData_rek(Rekening_client data_rek) {
		this.data_rek = data_rek;
	}
	public Agen getDataagen() {
		return dataagen;
	}
	public void setDataagen(Agen dataagen) {
		this.dataagen = dataagen;
	}
	public Datausulan getDatausulan() {
		return datausulan;
	}
	public void setDatausulan(Datausulan datausulan) {
		this.datausulan = datausulan;
	}
	public Integer getF() {
		return f;
	}
	public void setF(Integer f) {
		this.f = f;
	}
	public PdfContentByte getOver() {
		return over;
	}
	public void setOver(PdfContentByte over) {
		this.over = over;
	}
	public Pemegang getPemegang() {
		return pemegang;
	}
	public void setPemegang(Pemegang pemegang) {
		this.pemegang = pemegang;
	}
	public Tertanggung getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(Tertanggung tertanggung) {
		this.tertanggung = tertanggung;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public PdfReader getReader() {
		return reader;
	}
	public void setReader(PdfReader reader) {
		this.reader = reader;
	}
	public Kesehatan getMedical() {
		return medical;
	}
	public void setMedical(Kesehatan medical) {
		this.medical = medical;
	}
	
	
}
