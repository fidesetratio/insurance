package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain object untuk table EKA.PRW_REDEEM
 * 
 * @author Canpri Setiawan
 * @since 26 Sep 2012
 *
 */

public class Redeem implements Serializable{

	private static final long serialVersionUID = 4298431921888579941L;
	
		private String id_seller;
		private String kd_ref;
		private String nama_ref;
		private Long poin_ref;
		private String id_item;
		private String hadiah;
		private Integer reg_hadiah;
		private String alamat;
		private String kd_pos;
		private Date create_date;
		private String lus_id;
		private Long poin_hadiah;
		private Integer flag_approve;
		
		public String getId_seller() {
			return id_seller;
		}
		public void setId_seller(String id_seller) {
			this.id_seller = id_seller;
		}
		public String getKd_ref() {
			return kd_ref;
		}
		public void setKd_ref(String kd_ref) {
			this.kd_ref = kd_ref;
		}
		public Integer getFlag_approve() {
			return flag_approve;
		}
		public void setFlag_approve(Integer flag_approve) {
			this.flag_approve = flag_approve;
		}
		public Long getPoin_hadiah() {
			return poin_hadiah;
		}
		public void setPoin_hadiah(Long poin_hadiah) {
			this.poin_hadiah = poin_hadiah;
		}
		public String getNama_ref() {
			return nama_ref;
		}
		public void setNama_ref(String nama_ref) {
			this.nama_ref = nama_ref;
		}
		public String getId_item() {
			return id_item;
		}
		public void setId_item(String id_item) {
			this.id_item = id_item;
		}
		public Integer getReg_hadiah() {
			return reg_hadiah;
		}
		public void setReg_hadiah(Integer reg_hadiah) {
			this.reg_hadiah = reg_hadiah;
		}
		public String getAlamat() {
			return alamat;
		}
		public void setAlamat(String alamat) {
			this.alamat = alamat;
		}
		public String getKd_pos() {
			return kd_pos;
		}
		public void setKd_pos(String kd_pos) {
			this.kd_pos = kd_pos;
		}
		public Date getCreate_date() {
			return create_date;
		}
		public void setCreate_date(Date create_date) {
			this.create_date = create_date;
		}
		public String getLus_id() {
			return lus_id;
		}
		public void setLus_id(String lus_id) {
			this.lus_id = lus_id;
		}
		public String getHadiah() {
			return hadiah;
		}
		public void setHadiah(String hadiah) {
			this.hadiah = hadiah;
		}
		public Long getPoin_ref() {
			return poin_ref;
		}
		public void setPoin_ref(Long poin_ref) {
			this.poin_ref = poin_ref;
		}
}
