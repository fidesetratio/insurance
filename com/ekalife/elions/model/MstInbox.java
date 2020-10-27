package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class MstInbox implements Serializable {
	
	/**Fadly Mathendra
	 * 
	 */
	private static final long serialVersionUID = -2032926849885830566L;
	private String mi_id; 
	private Integer ljj_id; 
	private Integer lspd_id; 
	private Integer lspd_id_pending; 
	private Integer lspd_id_from; 
	private Integer user_app1; 
	private Integer user_app2; 
	private String reg_spaj; 
	private String no_reff; 
	private String mi_desc; 
	private Integer mi_pos; 
	private Date tgl_berkas_masuk; 
	private Date tgl_berkas_lengkap; 
	private Integer trans_id; 
	private Date trans_date; 
	private Integer create_id; 
	private Date create_date;
	private Integer app_3;
	private Integer user_app_3;
	private Integer app_4;
	private Integer user_app_4;
	private Integer lstb_id ;
	private Date tgl_jt_tempo;
	private Date tgl_konfirmasi;
	private Date tgl_admin_terima;
	private Integer flag_priority;
	private Integer batal;
	private Date batal_date;
	private Integer batal_id;
	private Integer flag_hardcopy;
	private Integer flag_validasi;
	private Integer lock_id;
	private Integer flag_cabang;
	
	
	public Integer getBatal() {
		return batal;
	}

	public void setBatal(Integer batal) {
		this.batal = batal;
	}

	public Date getBatal_date() {
		return batal_date;
	}

	public void setBatal_date(Date batal_date) {
		this.batal_date = batal_date;
	}

	public Integer getBatal_id() {
		return batal_id;
	}

	public void setBatal_id(Integer batal_id) {
		this.batal_id = batal_id;
	}

	public Date getTgl_jt_tempo() {
		return tgl_jt_tempo;
	}

	public void setTgl_jt_tempo(Date tgl_jt_tempo) {
		this.tgl_jt_tempo = tgl_jt_tempo;
	}

	public Date getTgl_konfirmasi() {
		return tgl_konfirmasi;
	}

	public void setTgl_konfirmasi(Date tgl_konfirmasi) {
		this.tgl_konfirmasi = tgl_konfirmasi;
	}

	public Date getTgl_admin_terima() {
		return tgl_admin_terima;
	}

	public void setTgl_admin_terima(Date tgl_admin_terima) {
		this.tgl_admin_terima = tgl_admin_terima;
	}

	public Integer getFlag_priority() {
		return flag_priority;
	}

	public void setFlag_priority(Integer flag_priority) {
		this.flag_priority = flag_priority;
	}

	public Integer getApp_3() {
		return app_3;
	}

	public void setApp_3(Integer app_3) {
		this.app_3 = app_3;
	}

	public Integer getUser_app_3() {
		return user_app_3;
	}

	public void setUser_app_3(Integer user_app_3) {
		this.user_app_3 = user_app_3;
	}
	
	public Integer getApp_4() {
		return app_4;
	}

	public void setApp_4(Integer app_4) {
		this.app_4 = app_4;
	}

	public Integer getUser_app_4() {
		return user_app_4;
	}

	public void setUser_app_4(Integer user_app_4) {
		this.user_app_4 = user_app_4;
	}

	public MstInbox(String mi_id, Integer ljj_id, Integer lspd_id, Integer lspd_id_pending, Integer lspd_id_from,
    		Integer user_app1, Integer user_app2, String reg_spaj, String no_reff
    		, String mi_desc, Integer mi_pos, Date tgl_berkas_masuk, Date tgl_berkas_lengkap, 
    		Integer trans_id, Date trans_date, Integer create_id, Date create_date, Integer app_3, Integer user_app_3, 
    		Integer lstb_id, Date tgl_jt_tempo, Date tgl_konfirmasi, Date tgl_admin_terima, Integer flag_priority,
    		Integer batal, Date batal_date, Integer batal_id, Integer flag_hardcopy, Integer flag_validasi, Integer flag_cabang){
		this.mi_id = mi_id;
		this.ljj_id = ljj_id;
		this.lspd_id = lspd_id;
		this.lspd_id_pending = lspd_id_pending;
		this.lspd_id_from = lspd_id_from;
		this.app_3 = app_3;
		this.user_app1 = user_app1;
		this.user_app2 = user_app2;
		this.user_app_3 = user_app_3;
		this.reg_spaj = reg_spaj;
		this.no_reff = no_reff;
		this.mi_desc = mi_desc;
		this.mi_pos = mi_pos;
		this.tgl_berkas_masuk = tgl_berkas_masuk;
		this.tgl_berkas_lengkap = tgl_berkas_lengkap;
		this.trans_id = trans_id;
		this.trans_date = trans_date;
		this.create_id = create_id;
		this.create_date = create_date;
		this.lstb_id = lstb_id;
		this.tgl_jt_tempo = tgl_jt_tempo;
		this.tgl_konfirmasi = tgl_konfirmasi;
		this.tgl_admin_terima = tgl_admin_terima;
		this.flag_priority = flag_priority;
		this.batal = batal;
		this.batal_date = batal_date;
		this.batal_id = batal_id;
		this.flag_hardcopy = flag_hardcopy;
		this.flag_validasi = flag_validasi;
		this.flag_cabang = flag_cabang;
	}
	
	public MstInbox(String mi_id, Integer lock_id){
		this.mi_id = mi_id;
		this.lock_id = lock_id;		
	}
	
	public Integer getLspd_id_from() {
		return lspd_id_from;
	}

	public void setLspd_id_from(Integer lspd_id_from) {
		this.lspd_id_from = lspd_id_from;
	}

	public String getMi_id() {
		return mi_id;
	}
	public void setMi_id(String mi_id) {
		this.mi_id = mi_id;
	}
	public Integer getLjj_id() {
		return ljj_id;
	}
	public void setLjj_id(Integer ljj_id) {
		this.ljj_id = ljj_id;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLspd_id_pending() {
		return lspd_id_pending;
	}
	public void setLspd_id_pending(Integer lspd_id_pending) {
		this.lspd_id_pending = lspd_id_pending;
	}
	public Integer getUser_app1() {
		return user_app1;
	}
	public void setUser_app1(Integer user_app1) {
		this.user_app1 = user_app1;
	}
	public Integer getUser_app2() {
		return user_app2;
	}
	public void setUser_app2(Integer user_app2) {
		this.user_app2 = user_app2;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getNo_reff() {
		return no_reff;
	}
	public void setNo_reff(String no_reff) {
		this.no_reff = no_reff;
	}
	public String getMi_desc() {
		return mi_desc;
	}
	public void setMi_desc(String mi_desc) {
		this.mi_desc = mi_desc;
	}
	public Integer getMi_pos() {
		return mi_pos;
	}
	public void setMi_pos(Integer mi_pos) {
		this.mi_pos = mi_pos;
	}
	public Date getTgl_berkas_masuk() {
		return tgl_berkas_masuk;
	}
	public void setTgl_berkas_masuk(Date tgl_berkas_masuk) {
		this.tgl_berkas_masuk = tgl_berkas_masuk;
	}
	public Date getTgl_berkas_lengkap() {
		return tgl_berkas_lengkap;
	}
	public void setTgl_berkas_lengkap(Date tgl_berkas_lengkap) {
		this.tgl_berkas_lengkap = tgl_berkas_lengkap;
	}
	public Integer getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(Integer trans_id) {
		this.trans_id = trans_id;
	}
	public Date getTrans_date() {
		return trans_date;
	}
	public void setTrans_date(Date trans_date) {
		this.trans_date = trans_date;
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

	public Integer getLstb_id() {
		return lstb_id;
	}

	public void setLstb_id(Integer lstb_id) {
		this.lstb_id = lstb_id;
	}

	public Integer getFlag_hardcopy() {
		return flag_hardcopy;
	}

	public void setFlag_hardcopy(Integer flag_hardcopy) {
		this.flag_hardcopy = flag_hardcopy;
	}

	public Integer getFlag_validasi() {
		return flag_validasi;
	}

	public void setFlag_validasi(Integer flag_validasi) {
		this.flag_validasi = flag_validasi;
	}

	public Integer getLock_id() {
		return lock_id;
	}

	public void setLock_id(Integer lock_id) {
		this.lock_id = lock_id;
	}

	public Integer getFlag_cabang() {
		return flag_cabang;
	}

	public void setFlag_cabang(Integer flag_cabang) {
		this.flag_cabang = flag_cabang;
	}	
	
	
}
