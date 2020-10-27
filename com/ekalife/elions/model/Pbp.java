package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * MST_PBP
 * save bayar link
 * 
 * @author Yusuf
 * @since Oct 10, 2008 (6:23:04 PM)
 */
public class Pbp implements Serializable {

	public String reg_spaj;
	public String spaj_bayar;
	public Date tgl_jttempo;
	public Integer flag_cair;
	public Integer premi_ke;
	public Integer flag_transfer;
	public Powersave powerSave;
	
	public Integer getFlag_transfer() {
		return flag_transfer;
	}
	public void setFlag_transfer(Integer flag_transfer) {
		this.flag_transfer = flag_transfer;
	}
	public Powersave getPowerSave() {
		return powerSave;
	}
	public void setPowerSave(Powersave powerSave) {
		this.powerSave = powerSave;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getSpaj_bayar() {
		return spaj_bayar;
	}
	public void setSpaj_bayar(String spaj_bayar) {
		this.spaj_bayar = spaj_bayar;
	}
	public Date getTgl_jttempo() {
		return tgl_jttempo;
	}
	public void setTgl_jttempo(Date tgl_jttempo) {
		this.tgl_jttempo = tgl_jttempo;
	}
	public Integer getFlag_cair() {
		return flag_cair;
	}
	public void setFlag_cair(Integer flag_cair) {
		this.flag_cair = flag_cair;
	}
	public Integer getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}

}