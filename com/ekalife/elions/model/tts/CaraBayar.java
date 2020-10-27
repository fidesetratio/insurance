package com.ekalife.elions.model.tts;

import java.io.Serializable;
import java.util.Date;

public class CaraBayar implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer no_urut;
	private String mst_no;
	private Date tgl_jth_tempo;
	private String s_tgl_jth_tempo;
	private String mst_no_rekening;
	private String mst_nama_bank;
	private Integer lsjb_id;
	private String lsjb_type;
	private Integer size;
	public Integer getLsjb_id() {
		return lsjb_id;
	}
	public void setLsjb_id(Integer lsjb_id) {
		this.lsjb_id = lsjb_id;
	}
	public Date getTgl_jth_tempo() {
		return tgl_jth_tempo;
	}
	public void setTgl_jth_tempo(Date tgl_jth_tempo) {
		this.tgl_jth_tempo= tgl_jth_tempo;
	}
	public String getMst_nama_bank() {
		return mst_nama_bank;
	}
	public void setMst_nama_bank(String mst_nama_bank) {
		this.mst_nama_bank = mst_nama_bank;
	}
	public String getMst_no() {
		return mst_no;
	}
	public void setMst_no(String mst_no) {
		this.mst_no = mst_no;
	}
	public String getMst_no_rekening() {
		return mst_no_rekening;
	}
	public void setMst_no_rekening(String mst_no_rekening) {
		this.mst_no_rekening = mst_no_rekening;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getLsjb_type() {
		return lsjb_type;
	}
	public void setLsjb_type(String lsjb_type) {
		this.lsjb_type = lsjb_type;
	}
	public String getS_tgl_jth_tempo() {
		return s_tgl_jth_tempo;
	}
	public void setS_tgl_jth_tempo(String s_tgl_jth_tempo) {
		this.s_tgl_jth_tempo = s_tgl_jth_tempo;
	}
	
}
