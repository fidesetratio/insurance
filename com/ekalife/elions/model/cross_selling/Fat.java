package com.ekalife.elions.model.cross_selling;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Class untuk LST_FAT (database HRD)
 * 
 * @author Yusuf
 * @since Apr 27, 2011 (5:53:30 PM)
 *
 */
public class Fat implements Serializable{

	private static final long serialVersionUID = -6763293506652281670L;

	//additional fields
	public boolean checked;
	
	//tambahan yusuf untuk table fields
	public Integer lus_id;
	public Date create_date;
	public Integer posisi;
	
	//Tambahan Antasari
	public String ket_term;

	//table fields
	public String fatid;
	public String kd_agent;
	public String nama;
	public String tpl;
	public Date tgl_lahir;
	public Integer gender;
	public String notelp;
	public String nohp;
	public String position;
	public Date act_date;
	public Date end_date;
	public Double honor;
	public Double target;
	public Double targetnoa;
	public String resign_why;
	public Integer is_active;
	public String alamat;
	public Integer getno;
	public Date tgl_update;
	public String nik_input;
	public String no_identitas;
	public String norek;
	public String ket_jbt;
	public String lcb_no;
	public String cabang;
	
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Integer getPosisi() {
		return posisi;
	}
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public String getFatid() {
		return fatid;
	}
	public void setFatid(String fatid) {
		this.fatid = fatid;
	}
	public String getKd_agent() {
		return kd_agent;
	}
	public void setKd_agent(String kd_agent) {
		this.kd_agent = kd_agent;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getTpl() {
		return tpl;
	}
	public void setTpl(String tpl) {
		this.tpl = tpl;
	}
	public Date getTgl_lahir() {
		return tgl_lahir;
	}
	public void setTgl_lahir(Date tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getNotelp() {
		return notelp;
	}
	public void setNotelp(String notelp) {
		this.notelp = notelp;
	}
	public String getNohp() {
		return nohp;
	}
	public void setNohp(String nohp) {
		this.nohp = nohp;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Date getAct_date() {
		return act_date;
	}
	public void setAct_date(Date act_date) {
		this.act_date = act_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Double getHonor() {
		return honor;
	}
	public void setHonor(Double honor) {
		this.honor = honor;
	}
	public Double getTarget() {
		return target;
	}
	public void setTarget(Double target) {
		this.target = target;
	}
	public Double getTargetnoa() {
		return targetnoa;
	}
	public void setTargetnoa(Double targetnoa) {
		this.targetnoa = targetnoa;
	}
	public String getResign_why() {
		return resign_why;
	}
	public void setResign_why(String resign_why) {
		this.resign_why = resign_why;
	}
	public Integer getIs_active() {
		return is_active;
	}
	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public Integer getGetno() {
		return getno;
	}
	public void setGetno(Integer getno) {
		this.getno = getno;
	}
	public Date getTgl_update() {
		return tgl_update;
	}
	public void setTgl_update(Date tgl_update) {
		this.tgl_update = tgl_update;
	}
	public String getNik_input() {
		return nik_input;
	}
	public void setNik_input(String nik_input) {
		this.nik_input = nik_input;
	}
	public String getNo_identitas() {
		return no_identitas;
	}
	public void setNo_identitas(String no_identitas) {
		this.no_identitas = no_identitas;
	}
	public String getNorek() {
		return norek;
	}
	public void setNorek(String norek) {
		this.norek = norek;
	}
	public String getKet_jbt() {
		return ket_jbt;
	}
	public void setKet_jbt(String ket_jbt) {
		this.ket_jbt = ket_jbt;
	}
	public String getLcb_no() {
		return lcb_no;
	}
	public void setLcb_no(String lcb_no) {
		this.lcb_no = lcb_no;
	}
	
	
	public String getKet_term() {
		return ket_term;
	}
	public void setKet_term(String ket_term) {
		this.ket_term = ket_term;
	}
}