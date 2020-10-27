package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MstInboxHist implements Serializable {
	
	/**Fadly Mathendra
	 * 
	 */
	private static final long serialVersionUID = -2032926849885830566L;
	private String mi_id; 
	private Integer lspd_before; 
	private Integer lspd_after; 
	private Integer pending_before; 
	private Integer pending_after; 
	private String mi_desc;
	private Integer create_id;
	private Date create_date;
	private Integer lock_id ;
	private Integer flag_kategori ;
	private Integer flag_validasi ;
	
	public MstInboxHist( String mi_id, Integer lspd_before, Integer lspd_after, Integer pending_before, Integer pending_after, 
			 String mi_desc, Integer create_id, Date create_date, Integer lock_id, Integer flag_kategori, Integer flag_validasi ){
		this.mi_id = mi_id;
		this.lspd_before = lspd_before;
		this.lspd_after = lspd_after;
		this.pending_before = pending_before;
		this.pending_after = pending_after;
		this.mi_desc = mi_desc;
		this.create_id = create_id;
		this.create_date = create_date;
		this.lock_id = lock_id;
		this.flag_kategori = flag_kategori;
		this.setFlag_validasi(flag_validasi);
	}
	
	public String getMi_id() {
		return mi_id;
	}
	public void setMi_id(String mi_id) {
		this.mi_id = mi_id;
	}
	
	public Integer getLspd_before() {
		return lspd_before;
	}

	public void setLspd_before(Integer lspd_before) {
		this.lspd_before = lspd_before;
	}

	public Integer getLspd_after() {
		return lspd_after;
	}

	public void setLspd_after(Integer lspd_after) {
		this.lspd_after = lspd_after;
	}

	public Integer getPending_before() {
		return pending_before;
	}
	public void setPending_before(Integer pending_before) {
		this.pending_before = pending_before;
	}
	public Integer getPending_after() {
		return pending_after;
	}
	public void setPending_after(Integer pending_after) {
		this.pending_after = pending_after;
	}
	public String getMi_desc() {
		return mi_desc;
	}
	public void setMi_desc(String mi_desc) {
		this.mi_desc = mi_desc;
	}
	public Integer getCreate_id() {
		return create_id;
	}
	public void setCreate_id(Integer create_id) {
		this.create_id = create_id;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Integer getLock_id() {
		return lock_id;
	}

	public void setLock_id(Integer lock_id) {
		this.lock_id = lock_id;
	}

	public Integer getFlag_kategori() {
		return flag_kategori;
	}

	public void setFlag_kategori(Integer flag_kategori) {
		this.flag_kategori = flag_kategori;
	}

	public Integer getFlag_validasi() {
		return flag_validasi;
	}

	public void setFlag_validasi(Integer flag_validasi) {
		this.flag_validasi = flag_validasi;
	}
	
}
