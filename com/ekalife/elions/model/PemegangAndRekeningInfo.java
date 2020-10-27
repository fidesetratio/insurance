package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Class buat input topup
 * 
 * @author Yusuf
 * @since Jan 19, 2009 (8:31:12 AM)
 */
public class PemegangAndRekeningInfo implements Serializable {

	// w_entry_trans_stable_link > d_entry_trans_stable_link

	/**
	 * 
	 */
	private static final long serialVersionUID = 3271916411123706534L;
	public String mrc_nama;
	public String mcl_first;
	public String notes;
	public Integer mrc_kuasa;
	public Date tgl;
	
	
	public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
	public Integer getMrc_kuasa() {
		return mrc_kuasa;
	}
	public void setMrc_kuasa(Integer mrc_kuasa) {
		this.mrc_kuasa = mrc_kuasa;
	}
	public String getMrc_nama() {
		return mrc_nama;
	}
	public void setMrc_nama(String mrc_nama) {
		this.mrc_nama = mrc_nama;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	
	
	
}