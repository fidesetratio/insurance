/**
 * 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yusuf
 *
 */
public class SpajBill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9156214741461545249L;

	private Double jlh_insentif;
	
	private Integer mcr_flag;
	private Double ldec_premi;
	private Double ldec_kurs;
	private Double ldec_komisi;
	private Double ldec_tax;
	private Date ldt_prod;
	private String no_spaj;
	private String lku_id;
	private int tahun_ke;
	private int premi_ke;
	private double jumlah;
	private int ib_edit;
	private int user_id;
	private String no_pre;
	private int pilih;
	private double rst_bunga;
	private boolean deposit;
	private int flag_topup;
	private Date tgl;
	private Double refund;
	private String no_account;
	private Double mkt_allocation;
	
	private Date tgl_refund;
	private Integer jn_bank;
	
	public Date getTgl_refund() {
		return tgl_refund;
	}

	public void setTgl_refund(Date tgl_refund) {
		this.tgl_refund = tgl_refund;
	}

	public Integer getJn_bank() {
		return jn_bank;
	}

	public void setJn_bank(Integer jn_bank) {
		this.jn_bank = jn_bank;
	}

	public SpajBill() {
		mkt_allocation = new Double(0);
	}
	
	public Double getMkt_allocation() {
		return mkt_allocation;
	}
	public void setMkt_allocation(Double mkt_allocation) {
		this.mkt_allocation = mkt_allocation;
	}
	public String getNo_account() {
		return no_account;
	}
	public void setNo_account(String no_account) {
		this.no_account = no_account;
	}
	public Double getRefund() {
		return refund;
	}
	public void setRefund(Double refund) {
		this.refund = refund;
	}
	public Double getLdec_komisi() {
		return ldec_komisi;
	}
	public void setLdec_komisi(Double ldec_komisi) {
		this.ldec_komisi = ldec_komisi;
	}
	public Double getLdec_kurs() {
		return ldec_kurs;
	}
	public void setLdec_kurs(Double ldec_kurs) {
		this.ldec_kurs = ldec_kurs;
	}
	public Double getLdec_premi() {
		return ldec_premi;
	}
	public void setLdec_premi(Double ldec_premi) {
		this.ldec_premi = ldec_premi;
	}
	public Double getLdec_tax() {
		return ldec_tax;
	}
	public void setLdec_tax(Double ldec_tax) {
		this.ldec_tax = ldec_tax;
	}
	public Date getLdt_prod() {
		return ldt_prod;
	}
	public void setLdt_prod(Date ldt_prod) {
		this.ldt_prod = ldt_prod;
	}
	public boolean isDeposit() {
		return deposit;
	}
	public void setDeposit(boolean deposit) {
		this.deposit = deposit;
	}
	public int getFlag_topup() {
		return flag_topup;
	}
	public void setFlag_topup(int flag_topup) {
		this.flag_topup = flag_topup;
	}
	public int getIb_edit() {
		return ib_edit;
	}
	public void setIb_edit(int ib_edit) {
		this.ib_edit = ib_edit;
	}
	public double getJumlah() {
		return jumlah;
	}
	public void setJumlah(double jumlah) {
		this.jumlah = jumlah;
	}
	public String getNo_pre() {
		return no_pre;
	}
	public void setNo_pre(String no_pre) {
		this.no_pre = no_pre;
	}
	public String getNo_spaj() {
		return no_spaj;
	}
	public void setNo_spaj(String no_spaj) {
		this.no_spaj = no_spaj;
	}
	public int getPilih() {
		return pilih;
	}
	public void setPilih(int pilih) {
		this.pilih = pilih;
	}
	public int getPremi_ke() {
		return premi_ke;
	}
	public void setPremi_ke(int premi_ke) {
		this.premi_ke = premi_ke;
	}
	public double getRst_bunga() {
		return rst_bunga;
	}
	public void setRst_bunga(double rst_bunga) {
		this.rst_bunga = rst_bunga;
	}
	public int getTahun_ke() {
		return tahun_ke;
	}
	public void setTahun_ke(int tahun_ke) {
		this.tahun_ke = tahun_ke;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getMcr_flag() {
		return mcr_flag;
	}
	public void setMcr_flag(Integer mcr_flag) {
		this.mcr_flag = mcr_flag;
	}

	public Double getJlh_insentif() {
		return jlh_insentif;
	}

	public void setJlh_insentif(Double jlh_insentif) {
		this.jlh_insentif = jlh_insentif;
	}
	
}
