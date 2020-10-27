package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.ekalife.utils.Common;

public class User implements Serializable, HttpSessionBindingListener {

	private static final long serialVersionUID = 6956047510784865739L;

	private Integer lsb_id;
	private String ip;
	private String lus_id;
	private String lde_id;
	private String name;
	private String pass;
	private String dept;
	private String division;
	private String lus_active;
	private String lca_id;
	private Date loginTime;
	private ArrayList<String> aksesCabang;
	private String cabang;
	private String ldi_id;
	private String email;
	private String lus_full_name;
	private int screenWidth;
	private int screenHeight;
	private Integer lus_admin;
	private String cab_bank;
	private Integer valid_bank_1;
	private Integer valid_bank_2;
	private Integer online;	
	private Date lus_change;
	//JENIS BANK
	//0,1 = BII
	//2 = BANK SINARMAS
	//3 = SIMAS SEKURITAS
	private Integer jn_bank;
	private Integer jumlahSpaj;
	
	private String msag_id_ao;
	private Integer flag_approve;
	
	private Integer flag_akm;
	private Integer lus_bas;
	private String mall_nama_pp;
	private Date mall_tgl_lhr_pp;
	private String mall_kd_area_telp;
	private String mall_telp;
	private String mall_hp;
	private String mall_email;
	private String mall_msag_id;
	private String mall_lcb_no;
	private String mall_lrb_id;
	private String mall_nama_penutup;
	private Datausulan data_usulan;
	private Tamu tamu;
	private String mall_mspo_plan_provider;
	private String lus_nik;
	private Integer flag_user_test;
	private String lus_msag_id;
	private Integer jenis_hardcoded_user; // jenis pada eka.lst_user_hardcoded_referential
	
	//tambahan untuk userAdministration
	private String level;
	private String submitMode;
	private String lcb_no;
	private String lcb_no_pil;
	private String nama_cabang;
	private ArrayList<String> daftarUser;
	private String cabangList;
	private Integer jn_pil;
	private Integer jn_user;
	private Integer jn_cab;
	private Integer jn_spv;
	private Integer button;
	private String lus_login_name;
	private String lus_email;
	private String spv1_name;
	private String spv2_name;
	private ArrayList<String> detailUser;
	private ArrayList<String> daftarSpv1;
	private ArrayList<String> daftarSpv2;
	private Integer stat_Act;
	private String stat_sukses;
	private String lus_id_copy;
	private String kode;
	private String lus_password;
	
	
	
	
	private String tempJenisPemegangPolis;
	
	public String getTempJenisPemegangPolis() {
		return tempJenisPemegangPolis;
	}
	public void setTempJenisPemegangPolis(String tempJenisPemegangPolis) {
		this.tempJenisPemegangPolis = tempJenisPemegangPolis;
	}
	public Integer getFlag_user_test() {
		return flag_user_test;
	}
	public void setFlag_user_test(Integer flag_user_test) {
		this.flag_user_test = flag_user_test;
	}
	public String getMall_mspo_plan_provider() {
		return mall_mspo_plan_provider;
	}
	public void setMall_mspo_plan_provider(String mallMspoPlanProvider) {
		mall_mspo_plan_provider = mallMspoPlanProvider;
	}
	public Datausulan getData_usulan() {
		return data_usulan;
	}
	public void setData_usulan(Datausulan dataUsulan) {
		data_usulan = dataUsulan;
	}
	public Tamu getTamu() {
		return tamu;
	}
	public void setTamu(Tamu tamu) {
		this.tamu = tamu;
	}
	public String getMall_nama_penutup() {
		return mall_nama_penutup;
	}
	public void setMall_nama_penutup(String mallNamaPenutup) {
		mall_nama_penutup = mallNamaPenutup;
	}
	
	public String getMall_nama_pp() {
		return mall_nama_pp;
	}
	public void setMall_nama_pp(String mallNamaPp) {
		mall_nama_pp = mallNamaPp;
	}
	public Date getMall_tgl_lhr_pp() {
		return mall_tgl_lhr_pp;
	}
	public void setMall_tgl_lhr_pp(Date mallTglLhrPp) {
		mall_tgl_lhr_pp = mallTglLhrPp;
	}
	public String getMall_kd_area_telp() {
		return mall_kd_area_telp;
	}
	public void setMall_kd_area_telp(String mallKdAreaTelp) {
		mall_kd_area_telp = mallKdAreaTelp;
	}
	public String getMall_telp() {
		return mall_telp;
	}
	public void setMall_telp(String mallTelp) {
		mall_telp = mallTelp;
	}
	public String getMall_hp() {
		return mall_hp;
	}
	public void setMall_hp(String mallHp) {
		mall_hp = mallHp;
	}
	public String getMall_email() {
		return mall_email;
	}
	public void setMall_email(String mallEmail) {
		mall_email = mallEmail;
	}
	public String getMall_msag_id() {
		return mall_msag_id;
	}
	public void setMall_msag_id(String mallMsagId) {
		mall_msag_id = mallMsagId;
	}
	public String getMall_lcb_no() {
		return mall_lcb_no;
	}
	public void setMall_lcb_no(String mallLcbNo) {
		mall_lcb_no = mallLcbNo;
	}
	public Integer getLus_bas() {
		return lus_bas;
	}
	public void setLus_bas(Integer lusBas) {
		lus_bas = lusBas;
	}
	public Integer getFlag_akm() {
		return flag_akm;
	}
	public void setFlag_akm(Integer flagAkm) {
		flag_akm = flagAkm;
	}
	private boolean wideScreen;
	
	public boolean isWideScreen() {
		return wideScreen;
	}
	public void setWideScreen(boolean wideScreen) {
		this.wideScreen = wideScreen;
	}
	public Integer getLsb_id() {
		return lsb_id;
	}
	public void setLsb_id(Integer lsb_id) {
		this.lsb_id = lsb_id;
	}
	public Integer getJn_bank() {
		return jn_bank;
	}
	public void setJn_bank(Integer jn_bank) {
		this.jn_bank = jn_bank;
	}
	public Integer getValid_bank_1() {
		return valid_bank_1;
	}
	public void setValid_bank_1(Integer valid_bank_1) {
		this.valid_bank_1 = valid_bank_1;
	}
	public Integer getValid_bank_2() {
		return valid_bank_2;
	}
	public void setValid_bank_2(Integer valid_bank_2) {
		this.valid_bank_2 = valid_bank_2;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getCab_bank() {
		return cab_bank;
	}
	public void setCab_bank(String cab_bank) {
		this.cab_bank = cab_bank;
	}
	public Integer getLus_admin() {return lus_admin;}
	public void setLus_admin(Integer lus_admin) {this.lus_admin = lus_admin;}
	public int getScreenHeight() {return screenHeight;}
	public void setScreenHeight(int screenHeight) {this.screenHeight = screenHeight;}
	public int getScreenWidth() {return screenWidth;}
	public void setScreenWidth(int screenWidth) {this.screenWidth = screenWidth;}
	public String getCabang() {return cabang;}
	public void setCabang(String cabang) {this.cabang = cabang;}
	public String getLdi_id() {return ldi_id;}
	public void setLdi_id(String ldi_id) {this.ldi_id = ldi_id;}
	public ArrayList<String> getAksesCabang() {return aksesCabang;}
	public void setAksesCabang(List aksesCabang) {this.aksesCabang = Common.serializableList(aksesCabang);}
	public String getLde_id() {return lde_id;}
	public void setLde_id(String lde_id) {this.lde_id = lde_id;}
	public Date getLoginTime() {return loginTime;}
	public void setLoginTime(Date loginTime) {this.loginTime = loginTime;}
	public String getLus_id() {return lus_id;}
	public void setLus_id(String lus_id) {this.lus_id = lus_id;}
	public String getLus_active() {return lus_active;}
	public void setLus_active(String lus_active) {this.lus_active = lus_active;}
	public String getDept() {return dept;}
	public void setDept(String dept) {this.dept = dept;}
	public String getDivision() {return division;}
	public void setDivision(String division) {this.division = division;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getPass() {return pass;}
	public void setPass(String pass) {this.pass = pass;}
	public String getIp() {return ip;}
	public void setIp(String ip) {this.ip = ip;}
	public String getLca_id() {return lca_id;}
	public void setLca_id(String lca_id) {this.lca_id = lca_id;}
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	public String getLus_full_name() {return lus_full_name;}
	public void setLus_full_name(String lus_full_name) {this.lus_full_name = lus_full_name;}

	/** FUNGSI2 TAMBAHAN **/
	public void valueBound(HttpSessionBindingEvent e) {
		HttpSession session = e.getSession();
		ServletContext context = session.getServletContext();
		Map users = (HashMap) context.getAttribute("users");
		if(users==null) users = new HashMap();
		
		if(!users.containsKey(lus_id)) {
			Map<String, Object> tmp = new HashMap<String, Object>();
			tmp.put("user", this);
			tmp.put("session", session);
			users.put(lus_id, tmp);
			context.setAttribute("users", users);
		}
	}
	
	public void valueUnbound(HttpSessionBindingEvent e) {
		HttpSession session = e.getSession();
		ServletContext context = session.getServletContext();
		Map users = (HashMap) context.getAttribute("users");
		
		if(users!=null)
			if(users.containsKey(lus_id))
				users.remove(lus_id); 
		context.setAttribute("users", users);
	}
	
	public int getComboBoxSize() { //ukuran combo box tergantung resolusi layar
		if(screenHeight<=600) return 25;
		else if(screenHeight<=768) return 39;
		else return 64;
	}
	public Integer getOnline() {
		return online;
	}
	public void setOnline(Integer online) {
		this.online = online;
	}
	public Integer getJumlahSpaj() {
		return jumlahSpaj;
	}
	public void setJumlahSpaj(Integer jumlahSpaj) {
		this.jumlahSpaj = jumlahSpaj;
	}
	public String getMsag_id_ao() {
		return msag_id_ao;
	}
	public void setMsag_id_ao(String msag_id_ao) {
		this.msag_id_ao = msag_id_ao;
	}
	public Integer getFlag_approve() {
		return flag_approve;
	}
	public void setFlag_approve(Integer flag_approve) {
		this.flag_approve = flag_approve;
	}
	public String getMall_lrb_id() {
		return mall_lrb_id;
	}
	public void setMall_lrb_id(String mallLrbId) {
		mall_lrb_id = mallLrbId;
	}
	public String getLus_nik() {
		return lus_nik;
	}
	public void setLus_nik(String lus_nik) {
		this.lus_nik = lus_nik;
	}
	
	public String getLus_msag_id() {
		return lus_msag_id;
	}
	public void setLus_msag_id(String lus_msag_id) {
		this.lus_msag_id = lus_msag_id;
	}
	public Integer getJenis_hardcoded_user() {
		return jenis_hardcoded_user;
	}
	public void setJenis_hardcoded_user(Integer jenis_hardcoded_user) {
		this.jenis_hardcoded_user = jenis_hardcoded_user;
	}
	public Date getLus_change() {
		return lus_change;
	}
	public void setLus_change(Date lus_change) {
		this.lus_change = lus_change;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSubmitMode() {
		return submitMode;
	}
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}
	public String getLcb_no() {
		return lcb_no;
	}
	public void setLcb_no(String lcb_no) {
		this.lcb_no = lcb_no;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public ArrayList<String> getDaftarUser() {
		return daftarUser;
	}
	public void setDaftarUser(List daftarUser) {
		this.daftarUser = Common.serializableList(daftarUser);
	}
	public String getCabangList() {
		return cabangList;
	}
	public void setCabangList(String cabangList) {
		this.cabangList = cabangList;
	}
	public Integer getJn_pil() {
		return jn_pil;
	}
	public void setJn_pil(Integer jn_pil) {
		this.jn_pil = jn_pil;
	}
	public Integer getButton() {
		return button;
	}
	public void setButton(Integer button) {
		this.button = button;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public String getLus_email() {
		return lus_email;
	}
	public void setLus_email(String lus_email) {
		this.lus_email = lus_email;
	}
	public String getSpv1_name() {
		return spv1_name;
	}
	public void setSpv1_name(String spv1_name) {
		this.spv1_name = spv1_name;
	}
	public String getSpv2_name() {
		return spv2_name;
	}
	public void setSpv2_name(String spv2_name) {
		this.spv2_name = spv2_name;
	}
	public ArrayList<String> getDetailUser() {
		return detailUser;
	}
	public void setDetailUser(List detailUser) {
		this.detailUser = Common.serializableList(detailUser);
	}
	public Integer getJn_user() {
		return jn_user;
	}
	public void setJn_user(Integer jn_user) {
		this.jn_user = jn_user;
	}
	public ArrayList<String> getDaftarSpv1() {
		return daftarSpv1;
	}
	public void setDaftarSpv1(List daftarSpv1) {
		this.daftarSpv1 = Common.serializableList(daftarSpv1);
	}
	public Integer getJn_cab() {
		return jn_cab;
	}
	public void setJn_cab(Integer jn_cab) {
		this.jn_cab = jn_cab;
	}
	public ArrayList<String> getDaftarSpv2() {
		return daftarSpv2;
	}
	public void setDaftarSpv2(List daftarSpv2) {
		this.daftarSpv2 = Common.serializableList(daftarSpv2);
	}
	public Integer getJn_spv() {
		return jn_spv;
	}
	public void setJn_spv(Integer jn_spv) {
		this.jn_spv = jn_spv;
	}
	public Integer getStat_Act() {
		return stat_Act;
	}
	public void setStat_Act(Integer stat_Act) {
		this.stat_Act = stat_Act;
	}
	public String getStat_sukses() {
		return stat_sukses;
	}
	public void setStat_sukses(String stat_sukses) {
		this.stat_sukses = stat_sukses;
	}
	public String getLcb_no_pil() {
		return lcb_no_pil;
	}
	public void setLcb_no_pil(String lcb_no_pil) {
		this.lcb_no_pil = lcb_no_pil;
	}
	public String getLus_id_copy() {
		return lus_id_copy;
	}
	public void setLus_id_copy(String lus_id_copy) {
		this.lus_id_copy = lus_id_copy;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getLus_password() {
		return lus_password;
	}
	public void setLus_password(String lus_password) {
		this.lus_password = lus_password;
	}

	
	
		
}