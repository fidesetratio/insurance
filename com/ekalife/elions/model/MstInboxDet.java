package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MstInboxDet implements Serializable {
	
	/**Fadly Mathendra
	 * 
	 */
	private static final long serialVersionUID = -2032926849885830566L;
	private String mi_id; 
	private Integer mid_id; 
	private String mid_desc; 
	private String lku_id; 
	private Integer mid_kurs; 
	private Integer mid_nominal; 
	private Integer lsbp_id; 
	private String mrc_cabang; 
	private String mrc_atas_nama; 
	private String mrc_no_ac; 
	private Integer mid_status; 
	private Integer mid_status_acc; 
	private String mid_ket; 
	private String mrc_kota; 
	private String no_blanko; 
	private String ab_type; 
	private Date ab_tgl_guna; 
	private Integer ab_qty;
	
		
	public MstInboxDet(String mi_id, Integer mid_id,String mid_desc,String lku_id, 
				Integer mid_kurs, Integer mid_nominal,Integer lsbp_id,String mrc_cabang,
				String mrc_atas_nama,String mrc_no_ac,Integer mid_status,Integer mid_status_acc,
				String mid_ket,String mrc_kota,String no_blanko,Integer ab_qty,
				String ab_type,Date ab_tgl_guna){
		
		this.mi_id = mi_id;
		this.mid_id = mid_id;
		this.mid_desc = mid_desc;
		this.lku_id = lku_id;
		this.mid_kurs = mid_kurs;
		this.mid_nominal = mid_nominal;
		this.lsbp_id = lsbp_id;
		this.mrc_cabang = mrc_cabang;
		this.mrc_atas_nama = mrc_atas_nama;
		this.mrc_no_ac = mrc_no_ac;
		this.mid_status = mid_status;
		this.mid_status_acc = mid_status_acc;
		this.mid_ket = mid_ket;
		this.mrc_kota = mrc_kota;
		this.no_blanko = no_blanko;
		this.setAb_qty(ab_qty);
		this.ab_type = ab_type;
		this.ab_tgl_guna = ab_tgl_guna;
	}
	
	public String getMi_id() {
		return mi_id;
	}
	public void setMi_id(String mi_id) {
		this.mi_id = mi_id;
	}
	public Integer getMid_id() {
		return mid_id;
	}
	public void setMid_id(Integer mid_id) {
		this.mid_id = mid_id;
	}
	public String getMid_desc() {
		return mid_desc;
	}
	public void setMid_desc(String mid_desc) {
		this.mid_desc = mid_desc;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getMid_kurs() {
		return mid_kurs;
	}
	public void setMid_kurs(Integer mid_kurs) {
		this.mid_kurs = mid_kurs;
	}
	public Integer getMid_nominal() {
		return mid_nominal;
	}
	public void setMid_nominal(Integer mid_nominal) {
		this.mid_nominal = mid_nominal;
	}
	public Integer getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}
	public String getMrc_cabang() {
		return mrc_cabang;
	}
	public void setMrc_cabang(String mrc_cabang) {
		this.mrc_cabang = mrc_cabang;
	}
	public String getMrc_atas_nama() {
		return mrc_atas_nama;
	}
	public void setMrc_atas_nama(String mrc_atas_nama) {
		this.mrc_atas_nama = mrc_atas_nama;
	}
	public String getMrc_no_ac() {
		return mrc_no_ac;
	}
	public void setMrc_no_ac(String mrc_no_ac) {
		this.mrc_no_ac = mrc_no_ac;
	}
	public Integer getMid_status() {
		return mid_status;
	}
	public void setMid_status(Integer mid_status) {
		this.mid_status = mid_status;
	}
	public Integer getMid_status_acc() {
		return mid_status_acc;
	}
	public void setMid_status_acc(Integer mid_status_acc) {
		this.mid_status_acc = mid_status_acc;
	}
	public String getMid_ket() {
		return mid_ket;
	}
	public void setMid_ket(String mid_ket) {
		this.mid_ket = mid_ket;
	}
	public String getMrc_kota() {
		return mrc_kota;
	}
	public void setMrc_kota(String mrc_kota) {
		this.mrc_kota = mrc_kota;
	}
	public String getNo_blanko() {
		return no_blanko;
	}
	public void setNo_blanko(String no_blanko) {
		this.no_blanko = no_blanko;
	}
	public String getAb_type() {
		return ab_type;
	}
	public void setAb_type(String ab_type) {
		this.ab_type = ab_type;
	}
	public Date getAb_tgl_guna() {
		return ab_tgl_guna;
	}
	public void setAb_tgl_guna(Date ab_tgl_guna) {
		this.ab_tgl_guna = ab_tgl_guna;
	}

	public Integer getAb_qty() {
		return ab_qty;
	}

	public void setAb_qty(Integer ab_qty) {
		this.ab_qty = ab_qty;
	}
	
	
	

	
	
}
