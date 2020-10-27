package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain object untuk table EKA.PRW_SELLER
 * 
 * @author Canpri Setiawan
 * @since 10 Jun 2012
 *
 */

public class PrwSeller implements Serializable{

	private static final long serialVersionUID = 4298431921888579941L;
	
		private String id_seller;
		private Integer id;
		private String nama;
		private String alamat; 
		private String kota;
		private String kd_pos;
		private String area_tlp;
		private String no_telp;
		private String area_tlp_kantor;
		private String tlp_kantor;
		private String no_hp;
		private String tp_lahir;
		private Date tgl_lahir;
		private String no_id;
		private Date tgl_aktif;
		private String saldo_point; 
		private String email;
		private Date tgl_input;
		private String password; 
		private String no_polis;
		private String id_rm_sale;
		private Integer kode_program;
		
		public String getId_seller() {
			return id_seller;
		}
		public void setId_seller(String id_seller) {
			this.id_seller = id_seller;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getNama() {
			return nama;
		}
		public void setNama(String nama) {
			this.nama = nama;
		}
		public String getAlamat() {
			return alamat;
		}
		public void setAlamat(String alamat) {
			this.alamat = alamat;
		}
		public String getKota() {
			return kota;
		}
		public void setKota(String kota) {
			this.kota = kota;
		}
		public String getKd_pos() {
			return kd_pos;
		}
		public void setKd_pos(String kd_pos) {
			this.kd_pos = kd_pos;
		}
		public String getArea_tlp() {
			return area_tlp;
		}
		public void setArea_tlp(String area_tlp) {
			this.area_tlp = area_tlp;
		}
		public String getNo_telp() {
			return no_telp;
		}
		public void setNo_telp(String no_telp) {
			this.no_telp = no_telp;
		}
		public String getArea_tlp_kantor() {
			return area_tlp_kantor;
		}
		public void setArea_tlp_kantor(String area_tlp_kantor) {
			this.area_tlp_kantor = area_tlp_kantor;
		}
		public String getTlp_kantor() {
			return tlp_kantor;
		}
		public void setTlp_kantor(String tlp_kantor) {
			this.tlp_kantor = tlp_kantor;
		}
		public String getNo_hp() {
			return no_hp;
		}
		public void setNo_hp(String no_hp) {
			this.no_hp = no_hp;
		}
		public String getTp_lahir() {
			return tp_lahir;
		}
		public void setTp_lahir(String tp_lahir) {
			this.tp_lahir = tp_lahir;
		}
		public Date getTgl_lahir() {
			return tgl_lahir;
		}
		public void setTgl_lahir(Date tgl_lahir) {
			this.tgl_lahir = tgl_lahir;
		}
		public String getNo_id() {
			return no_id;
		}
		public void setNo_id(String no_id) {
			this.no_id = no_id;
		}
		public Date getTgl_aktif() {
			return tgl_aktif;
		}
		public void setTgl_aktif(Date tgl_aktif) {
			this.tgl_aktif = tgl_aktif;
		}
		public String getSaldo_point() {
			return saldo_point;
		}
		public void setSaldo_point(String saldo_point) {
			this.saldo_point = saldo_point;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public Date getTgl_input() {
			return tgl_input;
		}
		public void setTgl_input(Date tgl_input) {
			this.tgl_input = tgl_input;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getNo_polis() {
			return no_polis;
		}
		public void setNo_polis(String no_polis) {
			this.no_polis = no_polis;
		}
		public String getId_rm_sale() {
			return id_rm_sale;
		}
		public void setId_rm_sale(String id_rm_sale) {
			this.id_rm_sale = id_rm_sale;
		}
		public Integer getKode_program() {
			return kode_program;
		}
		public void setKode_program(Integer kode_program) {
			this.kode_program = kode_program;
		}
}
