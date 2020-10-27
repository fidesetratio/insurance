package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Company_ws implements Serializable {

	private static final long serialVersionUID = -4501271539111643080L;
	public String nama_perusahaan;
	public String mcl_id;
	public Date tgl_invoice;
	public BigDecimal jumlah_invoice;
	public String telp_kantor;
	public String lus_admin_ws;
	public String nama_lengkap;
	public String nama_admin;
	public String email;
	
	public String tgl_invoice_indo;
	public String jumlah_invoice_conv;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTgl_invoice_indo() {
		return tgl_invoice_indo;
	}
	public void setTgl_invoice_indo(String tglInvoiceIndo) {
		tgl_invoice_indo = tglInvoiceIndo;
	}
	public String getJumlah_invoice_conv() {
		return jumlah_invoice_conv;
	}
	public void setJumlah_invoice_conv(String jumlahInvoiceConv) {
		jumlah_invoice_conv = jumlahInvoiceConv;
	}
	public String getNama_perusahaan() {
		return nama_perusahaan;
	}
	public void setNama_perusahaan(String namaPerusahaan) {
		nama_perusahaan = namaPerusahaan;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mclId) {
		mcl_id = mclId;
	}
	public Date getTgl_invoice() {
		return tgl_invoice;
	}
	public void setTgl_invoice(Date tglInvoice) {
		tgl_invoice = tglInvoice;
	}
	public BigDecimal getJumlah_invoice() {
		return jumlah_invoice;
	}
	public void setJumlah_invoice(BigDecimal jumlahInvoice) {
		jumlah_invoice = jumlahInvoice;
	}
	public String getTelp_kantor() {
		return telp_kantor;
	}
	public void setTelp_kantor(String telpKantor) {
		telp_kantor = telpKantor;
	}
	public String getLus_admin_ws() {
		return lus_admin_ws;
	}
	public void setLus_admin_ws(String lusAdminWs) {
		lus_admin_ws = lusAdminWs;
	}
	public String getNama_lengkap() {
		return nama_lengkap;
	}
	public void setNama_lengkap(String namaLengkap) {
		nama_lengkap = namaLengkap;
	}
	public String getNama_admin() {
		return nama_admin;
	}
	public void setNama_admin(String namaAdmin) {
		nama_admin = namaAdmin;
	}
	
	
}