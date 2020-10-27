package com.ekalife.elions.model;

import java.io.Serializable;

public class DetilTopUp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id_tunggal;
	private Integer id_berkala;
	private Double premi_tunggal;
	private Double premi_berkala;
	private Integer pil_tunggal;
	private Integer pil_berkala;
	private Integer redFlag_topup_berkala;
	
	public Integer getPil_berkala() {
		return pil_berkala;
	}
	public void setPil_berkala(Integer pil_berkala) {
		this.pil_berkala = pil_berkala;
	}
	public Integer getPil_tunggal() {
		return pil_tunggal;
	}
	public void setPil_tunggal(Integer pil_tunggal) {
		this.pil_tunggal = pil_tunggal;
	}
	public Integer getId_berkala() {
		return id_berkala;
	}
	public void setId_berkala(Integer id_berkala) {
		this.id_berkala = id_berkala;
	}
	public Integer getId_tunggal() {
		return id_tunggal;
	}
	public void setId_tunggal(Integer id_tunggal) {
		this.id_tunggal = id_tunggal;
	}
	public Double getPremi_berkala() {
		return premi_berkala;
	}
	public void setPremi_berkala(Double premi_berkala) {
		this.premi_berkala = premi_berkala;
	}
	public Double getPremi_tunggal() {
		return premi_tunggal;
	}
	public void setPremi_tunggal(Double premi_tunggal) {
		this.premi_tunggal = premi_tunggal;
	}
	public Integer getRedFlag_topup_berkala() {
		return redFlag_topup_berkala;
	}
	public void setRedFlag_topup_berkala(Integer redFlag_topup_berkala) {
		this.redFlag_topup_berkala = redFlag_topup_berkala;
	}
	
}
