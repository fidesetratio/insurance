package com.ekalife.elions.model.btpp;

import java.io.Serializable;
import java.util.Date;

public class Btpp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String kd_cabang;
	private String nama;
	private String nama_cabang;
	private String lst_kd_cab;
	private String mst_no;
	private Date mst_tgl_input;
	private String lst_nm_cab;
	private String lst_nm_admin;
	private String mst_nm_pemegang;
	private Integer flag_print;
	private Integer lus_id;
	private Date mst_tglsetor;
	private String mst_no_telp;
	private String mst_ket;
	private String mst_no_btpp;
	private Double premi;
	private Double extra_premi;
	private Double biaya_polis;
	private Double up;
	private Double tot_byr;
	private String kd_agen;
	private String nm_penutup;
	private Integer Mst_flag_batal;
	private String flag;
	private String mcl_id;
	private String mcl_first;
	private String msag_id;
	private String tglsetor;
	private Date prde_byr_awal;
	private Date prde_byr_akhr;
	private String mst_no_reff_btl;
	private String reg_spaj;
	private String  sprde_byr_awal;
	private String sprde_byr_akhr;
	private String s_tgl_rk;
	
public String getS_tgl_rk() {
		return s_tgl_rk;
	}
	public void setS_tgl_rk(String s_tgl_rk) {
		this.s_tgl_rk = s_tgl_rk;
	}
	public String getSprde_byr_akhr() {
		return sprde_byr_akhr;
	}
	public void setSprde_byr_akhr(String sprde_byr_akhr) {
		this.sprde_byr_akhr = sprde_byr_akhr;
	}
	public String getSprde_byr_awal() {
		return sprde_byr_awal;
	}
	public void setSprde_byr_awal(String sprde_byr_awal) {
		this.sprde_byr_awal = sprde_byr_awal;
	}
public String getTglsetor() {
		return tglsetor;
	}
	public void setTglsetor(String tglsetor) {
		this.tglsetor = tglsetor;
	}
	public Date getMst_tglsetor() {
		return mst_tglsetor;
	}
public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
public String getMcl_first() {
		return mcl_first;
	}
	public void setMcl_first(String mcl_first) {
		this.mcl_first = mcl_first;
	}
public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
public Integer getMst_flag_batal() {
		return Mst_flag_batal;
	}
	public void setMst_flag_batal(Integer mst_flag_batal) {
		Mst_flag_batal = mst_flag_batal;
	}
	//	public Integer getKd_agen() {
//		return kd_agen;
//	}
//	public void setKd_agen(Integer kd_agen) {
//		this.kd_agen = kd_agen;
//	}
//	public String getNm_penutup() {
//		return nm_penutup;
//	}
//	public void setNm_penutup(String nm_penutup) {
//		this.nm_penutup = nm_penutup;
//	}
//	public String getTot_byr() {
//		return tot_byr;
//	}
//	public void setTot_byr(String tot_byr) {
//		this.tot_byr = tot_byr;
//	}
	
	public Integer getFlag_print() {
		return flag_print;
	}
	public void setFlag_print(Integer flag_print) {
		this.flag_print = flag_print;
	}
	public String getLst_kd_cab() {
		return lst_kd_cab;
	}
	public void setLst_kd_cab(String lst_kd_cab) {
		this.lst_kd_cab = lst_kd_cab;
	}
	public String getLst_nm_admin() {
		return lst_nm_admin;
	}
	public void setLst_nm_admin(String lst_nm_admin) {
		this.lst_nm_admin = lst_nm_admin;
	}
	public String getLst_nm_cab() {
		return lst_nm_cab;
	}
	public void setLst_nm_cab(String lst_nm_cab) {
		this.lst_nm_cab = lst_nm_cab;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getMst_ket() {
		return mst_ket;
	}
	public void setMst_ket(String mst_ket) {
		this.mst_ket = mst_ket;
	}
	public String getMst_nm_pemegang() {
		return mst_nm_pemegang;
	}
	public void setMst_nm_pemegang(String mst_nm_pemegang) {
		this.mst_nm_pemegang = mst_nm_pemegang;
	}
	public String getMst_no() {
		return mst_no;
	}
	public void setMst_no(String mst_no) {
		this.mst_no = mst_no;
	}
	public Date getMst_tgl_input() {
		return mst_tgl_input;
	}
	public void setMst_tgl_input(Date mst_tgl_input) {
		this.mst_tgl_input = mst_tgl_input;
	}

	public Double getBiaya_polis() {
		return biaya_polis;
	}
	public void setBiaya_polis(Double biaya_polis) {
		this.biaya_polis = biaya_polis;
	}
	public Double getExtra_premi() {
		return extra_premi;
	}
	public void setExtra_premi(Double extra_premi) {
		this.extra_premi = extra_premi;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public Double getUp() {
		return up;
	}
	public void setUp(Double up) {
		this.up = up;
	}
	public String getMst_no_telp() {
		return mst_no_telp;
	}
	public void setMst_no_telp(String mst_no_telp) {
		this.mst_no_telp = mst_no_telp;
	}
	public String getMst_no_btpp() {
		return mst_no_btpp;
	}
	public void setMst_no_btpp(String mst_no_btpp) {
		this.mst_no_btpp = mst_no_btpp;
	}
	public String getKd_cabang() {
		return kd_cabang;
	}
	public void setKd_cabang(String kd_cabang) {
		this.kd_cabang = kd_cabang;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}

	public String getKd_agen() {
		return kd_agen;
	}
	public void setKd_agen(String kd_agen) {
		this.kd_agen = kd_agen;
	}
	public String getNm_penutup() {
		return nm_penutup;
	}
	public void setNm_penutup(String nm_penutup) {
		this.nm_penutup = nm_penutup;
	}
	public Double getTot_byr() {
		return tot_byr;
	}
	public void setTot_byr(Double tot_byr) {
		this.tot_byr = tot_byr;
	}
	public void setMst_tglsetor(Date mst_tglsetor) {
		this.mst_tglsetor = mst_tglsetor;
	}

	public String getMst_no_reff_btl() {
		return mst_no_reff_btl;
	}
	public void setMst_no_reff_btl(String mst_no_reff_btl) {
		this.mst_no_reff_btl = mst_no_reff_btl;
	}
	public Date getPrde_byr_awal() {
		return prde_byr_awal;
	}
	public void setPrde_byr_awal(Date prde_byr_awal) {
		this.prde_byr_awal = prde_byr_awal;
	}
	public Date getPrde_byr_akhr() {
		return prde_byr_akhr;
	}
	public void setPrde_byr_akhr(Date prde_byr_akhr) {
		this.prde_byr_akhr = prde_byr_akhr;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}


}
