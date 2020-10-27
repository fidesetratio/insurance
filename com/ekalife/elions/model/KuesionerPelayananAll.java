package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KuesionerPelayananAll implements Serializable{
	
	/**
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Oct 11, 2012 3:00:59 PM
	 * @Description :
	 * 
	 */
	private static final long serialVersionUID = -3701093403820165016L;
	private Double sangat_baik;
	private Double baik;
	private Double kurang_baik;
	private Double buruk;
	private Double buruk_sekali;
	
	public Double getSangat_baik() {
		return sangat_baik;
	}
	public void setSangat_baik(Double sangatBaik) {
		sangat_baik = sangatBaik;
	}
	public Double getBaik() {
		return baik;
	}
	public void setBaik(Double baik) {
		this.baik = baik;
	}
	public Double getKurang_baik() {
		return kurang_baik;
	}
	public void setKurang_baik(Double kurangBaik) {
		kurang_baik = kurangBaik;
	}
	public Double getBuruk() {
		return buruk;
	}
	public void setBuruk(Double buruk) {
		this.buruk = buruk;
	}
	public Double getBuruk_sekali() {
		return buruk_sekali;
	}
	public void setBuruk_sekali(Double burukSekali) {
		buruk_sekali = burukSekali;
	}
	
	
}
