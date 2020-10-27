package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Inputan Followup premi lanjutan jatuh tempo (mst_followup)
 * 
 * @author Yusuf
 * @since Aug 19, 2011 (10:49:35 AM)
 *
 */
public class Followup implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//elions.bas.selectFollowupBilling
	public String msbi_beg_date;
	public String msbi_end_date;
	public String msbi_due_date;
	public String mspo_policy_no_format;
	public String reg_spaj;
	public Integer msbi_tahun_ke;
	public Integer msbi_premi_ke;
	
	//elions.bas.selectDetailPolisFollowupBilling
	public String pemegang;
	public String alamat1;
	public String alamat2;
	public String telp1;
	public String telp2;
	public String no_hp;
	public String no_hp2;
	public String tgl_jt;
	public String kurs;
	public Double premi;
	public String tertanggung;
	public String produk;
	public String beg_date;
	public String end_date;
	public String pay_mode;
	public String vacc;
	public Integer pay_period;
	public Double tsi;
	public String lssp_status;
	
	//elions.bas.selectDetailPaymentFollowupBilling
	public Integer tahun_ke;
	public Integer premi_ke;
	public String tgl_rk;
	public String tgl_proses;
	public Double paid;
	public String status_paid;
	public Integer jml_fu;
	
	//MST_FOLLOWUP
	public Integer msfu_urut;
	public Integer lsfu_id;
	public String lsfu_desc;
	public String lus_id;
	public String user_name;
	public String msfu_ket;
	public Integer msfu_status; //0 = Outstanding (Sudah Followup), 1 = Closed, 2 = All, 3 = Belum Followup
	public String status;
	public String create_date;
	public String msfu_attachment;
	
	public String ket;
	
	public Date reminder_date;
	
	//tambahan
	public String email;
	public String jenkel;
	public String title;
	public String title_tertanggung;
	public Integer no;
	public Double topup;
	public String lca_id;
	public String lwk_id;
	public String lsrg_id;
	public String lca_nama;
	public String lwk_nama;
	public String lsrg_nama;

	public Followup() {}
	
	public Followup(String reg_spaj, String polis, Integer msbi_tahun_ke, Integer msbi_premi_ke, 
			Integer lsfu_id, String lus_id, String msfu_ket, Integer msfu_status) {
		this.reg_spaj = reg_spaj;
		this.mspo_policy_no_format = polis;
		this.msbi_tahun_ke = msbi_tahun_ke;
		this.msbi_premi_ke = msbi_premi_ke;
		this.lsfu_id = lsfu_id;
		this.lus_id = lus_id;
		this.msfu_ket = msfu_ket;
		this.msfu_status = msfu_status;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public String getMsbi_end_date() {
		return msbi_end_date;
	}

	public void setMsbi_end_date(String msbi_end_date) {
		this.msbi_end_date = msbi_end_date;
	}

	public String getMsfu_attachment() {
		return msfu_attachment;
	}

	public void setMsfu_attachment(String msfu_attachment) {
		this.msfu_attachment = msfu_attachment;
	}

	public Integer getJml_fu() {
		return jml_fu;
	}

	public void setJml_fu(Integer jml_fu) {
		this.jml_fu = jml_fu;
	}

	public String getStatus_paid() {
		return status_paid;
	}

	public void setStatus_paid(String status_paid) {
		this.status_paid = status_paid;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLsfu_desc() {
		return lsfu_desc;
	}
	public void setLsfu_desc(String lsfu_desc) {
		this.lsfu_desc = lsfu_desc;
	}
	public Integer getMsfu_urut() {
		return msfu_urut;
	}
	public void setMsfu_urut(Integer msfu_urut) {
		this.msfu_urut = msfu_urut;
	}
	public Integer getLsfu_id() {
		return lsfu_id;
	}
	public void setLsfu_id(Integer lsfu_id) {
		this.lsfu_id = lsfu_id;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Integer getMsfu_status() {
		return msfu_status;
	}
	public void setMsfu_status(Integer msfu_status) {
		this.msfu_status = msfu_status;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getMsfu_ket() {
		return msfu_ket;
	}
	public void setMsfu_ket(String msfu_ket) {
		this.msfu_ket = msfu_ket;
	}
	public Integer getTahun_ke() {
		return tahun_ke;
	}
	public void setTahun_ke(Integer tahun_ke) {
		this.tahun_ke = tahun_ke;
	}
	public Integer getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(Integer premi_ke) {
		this.premi_ke = premi_ke;
	}
	public String getTgl_rk() {
		return tgl_rk;
	}
	public void setTgl_rk(String tgl_rk) {
		this.tgl_rk = tgl_rk;
	}
	public String getTgl_proses() {
		return tgl_proses;
	}
	public void setTgl_proses(String tgl_proses) {
		this.tgl_proses = tgl_proses;
	}
	public Double getPaid() {
		return paid;
	}
	public void setPaid(Double paid) {
		this.paid = paid;
	}
	public String getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(String tertanggung) {
		this.tertanggung = tertanggung;
	}
	public String getProduk() {
		return produk;
	}
	public void setProduk(String produk) {
		this.produk = produk;
	}
	public String getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(String beg_date) {
		this.beg_date = beg_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getPay_mode() {
		return pay_mode;
	}
	public void setPay_mode(String pay_mode) {
		this.pay_mode = pay_mode;
	}
	public String getVacc() {
		return vacc;
	}
	public void setVacc(String vacc) {
		this.vacc = vacc;
	}
	public Integer getPay_period() {
		return pay_period;
	}
	public void setPay_period(Integer pay_period) {
		this.pay_period = pay_period;
	}
	public Double getTsi() {
		return tsi;
	}
	public void setTsi(Double tsi) {
		this.tsi = tsi;
	}
	public String getTelp1() {
		return telp1;
	}
	public void setTelp1(String telp1) {
		this.telp1 = telp1;
	}
	public String getTelp2() {
		return telp2;
	}
	public void setTelp2(String telp2) {
		this.telp2 = telp2;
	}
	public String getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}
	public String getNo_hp2() {
		return no_hp2;
	}
	public void setNo_hp2(String no_hp2) {
		this.no_hp2 = no_hp2;
	}
	public String getPemegang() {
		return pemegang;
	}
	public void setPemegang(String pemegang) {
		this.pemegang = pemegang;
	}
	public String getAlamat1() {
		return alamat1;
	}
	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}
	public String getAlamat2() {
		return alamat2;
	}
	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}
	public String getTgl_jt() {
		return tgl_jt;
	}
	public void setTgl_jt(String tgl_jt) {
		this.tgl_jt = tgl_jt;
	}
	public String getKurs() {
		return kurs;
	}
	public void setKurs(String kurs) {
		this.kurs = kurs;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public String getMsbi_beg_date() {
		return msbi_beg_date;
	}
	public void setMsbi_beg_date(String msbi_beg_date) {
		this.msbi_beg_date = msbi_beg_date;
	}
	public String getMsbi_due_date() {
		return msbi_due_date;
	}
	public void setMsbi_due_date(String msbi_due_date) {
		this.msbi_due_date = msbi_due_date;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Date getReminder_date() {
		return reminder_date;
	}
	public void setReminder_date(Date reminder_date) {
		this.reminder_date = reminder_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJenkel() {
		return jenkel;
	}
	public void setJenkel(String jenkel) {
		this.jenkel = jenkel;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle_tertanggung() {
		return title_tertanggung;
	}
	public void setTitle_tertanggung(String title_tertanggung) {
		this.title_tertanggung = title_tertanggung;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Double getTopup() {
		return topup;
	}
	public void setTopup(Double topup) {
		this.topup = topup;
	}
	public String getLca_id() {
		return lca_id;
	}

	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}

	public String getLca_nama() {
		return lca_nama;
	}

	public void setLca_nama(String lca_nama) {
		this.lca_nama = lca_nama;
	}
	public String getLwk_id() {
		return lwk_id;
	}

	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}

	public String getLsrg_id() {
		return lsrg_id;
	}

	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}

	public String getLwk_nama() {
		return lwk_nama;
	}

	public void setLwk_nama(String lwk_nama) {
		this.lwk_nama = lwk_nama;
	}

	public String getLsrg_nama() {
		return lsrg_nama;
	}

	public void setLsrg_nama(String lsrg_nama) {
		this.lsrg_nama = lsrg_nama;
	}
	
	public String getLssp_status() {
		return lssp_status;
	}
	public void setLssp_status(String lssp_status) {
		this.lssp_status = lssp_status;
	}
}