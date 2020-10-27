package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ekalife.utils.Common;

//model untuk eka.lst_addr_region
public class AddressRegion implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer lar_id;
	private String lar_nama;
	private String lar_alamat;
	private String lar_admin;
	private String lar_telpon;
	private String lar_lca_id;
	private String lar_email;
	private String lar_no;
	private String lar_head;
	private Integer lar_kode_bmi;
	private Integer flag_aktif;
	private String lar_email_cc;
	private Integer lska_id;
	private String lar_email_tambahan;
	private String lar_email_tambahan_cc;           
	private String submitMode;
	private String index;
	private String kota;
	private String cabang;
	private ArrayList<Map> daftarKantor;
	private Integer lus_id;
	private String keterangan;
	private String lar_speedy;
	private Integer akses;
	
	//tambahan kolom
	private String lar_luas;
	private String lar_fax;
	private String lar_status_gedung;
	private Date lar_beg_date_sewa;
	private Date lar_end_date_sewa;
	private Date lar_beg_date_domisili;
	private Date lar_end_date_domisili;
	private Date lar_update_date;
	private MultipartFile file1;
	private MultipartFile file2;
	private MultipartFile file3;
	private MultipartFile file4;
	private ArrayList<Map> ls_attachment;
	private String last_update; 
	private ArrayList<Map> history;
	private Date lar_end_date_reklame;
	private Date lar_non_active_date;
	
	public Integer getLar_id() {
		return lar_id;
	}
	public void setLar_id(Integer lar_id) {
		this.lar_id = lar_id;
	}
	public String getLar_nama() {
		return lar_nama;
	}
	public void setLar_nama(String lar_nama) {
		this.lar_nama = lar_nama;
	}
	public String getLar_alamat() {
		return lar_alamat;
	}
	public void setLar_alamat(String lar_alamat) {
		this.lar_alamat = lar_alamat;
	}
	public String getLar_admin() {
		return lar_admin;
	}
	public void setLar_admin(String lar_admin) {
		this.lar_admin = lar_admin;
	}
	public String getLar_telpon() {
		return lar_telpon;
	}
	public void setLar_telpon(String lar_telpon) {
		this.lar_telpon = lar_telpon;
	}
	public String getLar_lca_id() {
		return lar_lca_id;
	}
	public void setLar_lca_id(String lar_lca_id) {
		this.lar_lca_id = lar_lca_id;
	}
	public String getLar_email() {
		return lar_email;
	}
	public void setLar_email(String lar_email) {
		this.lar_email = lar_email;
	}
	public String getLar_no() {
		return lar_no;
	}
	public void setLar_no(String lar_no) {
		this.lar_no = lar_no;
	}
	public String getLar_head() {
		return lar_head;
	}
	public void setLar_head(String lar_head) {
		this.lar_head = lar_head;
	}
	public Integer getLar_kode_bmi() {
		return lar_kode_bmi;
	}
	public void setLar_kode_bmi(Integer lar_kode_bmi) {
		this.lar_kode_bmi = lar_kode_bmi;
	}
	public Integer getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(Integer flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public String getLar_email_cc() {
		return lar_email_cc;
	}
	public void setLar_email_cc(String lar_email_cc) {
		this.lar_email_cc = lar_email_cc;
	}
	public Integer getLska_id() {
		return lska_id;
	}
	public void setLska_id(Integer lska_id) {
		this.lska_id = lska_id;
	}
	public String getLar_email_tambahan() {
		return lar_email_tambahan;
	}
	public void setLar_email_tambahan(String lar_email_tambahan) {
		this.lar_email_tambahan = lar_email_tambahan;
	}
	public String getLar_email_tambahan_cc() {
		return lar_email_tambahan_cc;
	}
	public void setLar_email_tambahan_cc(String lar_email_tambahan_cc) {
		this.lar_email_tambahan_cc = lar_email_tambahan_cc;
	}
	public String getKota() {
		return kota;
	}
	public void setKota(String kota) {
		this.kota = kota;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getSubmitMode() {
		return submitMode;
	}
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public ArrayList<Map> getDaftarKantor() {
		return daftarKantor;
	}
	public void setDaftarKantor(List<Map> daftarKantor) {
		this.daftarKantor = Common.serializableList(daftarKantor);
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getLar_speedy() {
		return lar_speedy;
	}
	public void setLar_speedy(String lar_speedy) {
		this.lar_speedy = lar_speedy;
	}
	
	public String getLar_luas() {
		return lar_luas;
	}
	public void setLar_luas(String lar_luas) {
		this.lar_luas = lar_luas;
	}
	public String getLar_fax() {
		return lar_fax;
	}
	public void setLar_fax(String lar_fax) {
		this.lar_fax = lar_fax;
	}
	public String getLar_status_gedung() {
		return lar_status_gedung;
	}
	public void setLar_status_gedung(String lar_status_gedung) {
		this.lar_status_gedung = lar_status_gedung;
	}
	public Date getLar_beg_date_sewa() {
		return lar_beg_date_sewa;
	}
	public void setLar_beg_date_sewa(Date lar_beg_date_sewa) {
		this.lar_beg_date_sewa = lar_beg_date_sewa;
	}
	public Date getLar_end_date_sewa() {
		return lar_end_date_sewa;
	}
	public void setLar_end_date_sewa(Date lar_end_date_sewa) {
		this.lar_end_date_sewa = lar_end_date_sewa;
	}
	public Date getLar_beg_date_domisili() {
		return lar_beg_date_domisili;
	}
	public void setLar_beg_date_domisili(Date lar_beg_date_domisili) {
		this.lar_beg_date_domisili = lar_beg_date_domisili;
	}
	public Date getLar_end_date_domisili() {
		return lar_end_date_domisili;
	}
	public void setLar_end_date_domisili(Date lar_end_date_domisili) {
		this.lar_end_date_domisili = lar_end_date_domisili;
	}
	public Date getLar_update_date() {
		return lar_update_date;
	}
	public void setLar_update_date(Date lar_update_date) {
		this.lar_update_date = lar_update_date;
	}
	public MultipartFile getFile1() {
		return file1;
	}
	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}
	public MultipartFile getFile2() {
		return file2;
	}
	public void setFile2(MultipartFile file2) {
		this.file2 = file2;
	}
	public MultipartFile getFile3() {
		return file3;
	}
	public void setFile3(MultipartFile file3) {
		this.file3 = file3;
	}
	public MultipartFile getFile4() {
		return file4;
	}
	public void setFile4(MultipartFile file4) {
		this.file4 = file4;
	}
	public ArrayList<Map> getLs_attachment() {
		return ls_attachment;
	}
	public void setLs_attachment(List<Map> ls_attachment) {
		this.ls_attachment = Common.serializableList(ls_attachment);
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	public ArrayList<Map> getHistory() {
		return history;
	}
	public void setHistory(List<Map> history) {
		this.history = Common.serializableList(history);
	}
	public Integer getAkses() {
		return akses;
	}
	public void setAkses(Integer akses) {
		this.akses = akses;
	}
	public Date getLar_end_date_reklame() {
		return lar_end_date_reklame;
	}
	public void setLar_end_date_reklame(Date lar_end_date_reklame) {
		this.lar_end_date_reklame = lar_end_date_reklame;
	}
	public Date getLar_non_active_date() {
		return lar_non_active_date;
	}
	public void setLar_non_active_date(Date lar_non_active_date) {
		this.lar_non_active_date = lar_non_active_date;
	}
}
