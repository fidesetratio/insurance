package com.ekalife.elions.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Kebutuhan implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mns_kd_nasabah;                 
	private Integer mkb_no;                        
	private Integer ljk_id;                        
	private Integer mkb_rank;                      
	private String mkb_alasan;                     
	private BigDecimal mkb_jml_butuh;                 
	private BigDecimal mkb_pdpt_butuh;                
	private Integer mkb_wkt_butuh_th;              
	private Integer mkb_wkt_butuh_bl;              
	private BigDecimal mkb_jml_dpoleh;                
	private BigDecimal mkb_pdpt_dpoleh;               
	private Integer mkb_wkt_dpoleh_th;             
	private Integer mkb_wkt_dpoleh_bl;             
	private Integer mkb_penting;
	private String ljk_ket;
	private List<Kebutuhan> listKebutuhan;
	
	public List<Kebutuhan> getListKebutuhan() {
		return listKebutuhan;
	}
	public void setListKebutuhan(List<Kebutuhan> listKebutuhan) {
		this.listKebutuhan = listKebutuhan;
	}
	public Integer getLjk_id() {
		return ljk_id;
	}
	public void setLjk_id(Integer ljk_id) {
		this.ljk_id = ljk_id;
	}
	public String getMkb_alasan() {
		return mkb_alasan;
	}
	public void setMkb_alasan(String mkb_alasan) {
		this.mkb_alasan = mkb_alasan;
	}
	public BigDecimal getMkb_jml_butuh() {
		return mkb_jml_butuh;
	}
	public void setMkb_jml_butuh(BigDecimal mkb_jml_butuh) {
		this.mkb_jml_butuh = mkb_jml_butuh;
	}
	public BigDecimal getMkb_jml_dpoleh() {
		return mkb_jml_dpoleh;
	}
	public void setMkb_jml_dpoleh(BigDecimal mkb_jml_dpoleh) {
		this.mkb_jml_dpoleh = mkb_jml_dpoleh;
	}
	public Integer getMkb_no() {
		return mkb_no;
	}
	public void setMkb_no(Integer mkb_no) {
		this.mkb_no = mkb_no;
	}
	public BigDecimal getMkb_pdpt_butuh() {
		return mkb_pdpt_butuh;
	}
	public void setMkb_pdpt_butuh(BigDecimal mkb_pdpt_butuh) {
		this.mkb_pdpt_butuh = mkb_pdpt_butuh;
	}
	public BigDecimal getMkb_pdpt_dpoleh() {
		return mkb_pdpt_dpoleh;
	}
	public void setMkb_pdpt_dpoleh(BigDecimal mkb_pdpt_dpoleh) {
		this.mkb_pdpt_dpoleh = mkb_pdpt_dpoleh;
	}
	public Integer getMkb_penting() {
		return mkb_penting;
	}
	public void setMkb_penting(Integer mkb_penting) {
		this.mkb_penting = mkb_penting;
	}
	public Integer getMkb_rank() {
		return mkb_rank;
	}
	public void setMkb_rank(Integer mkb_rank) {
		this.mkb_rank = mkb_rank;
	}
	public Integer getMkb_wkt_butuh_bl() {
		return mkb_wkt_butuh_bl;
	}
	public void setMkb_wkt_butuh_bl(Integer mkb_wkt_butuh_bl) {
		this.mkb_wkt_butuh_bl = mkb_wkt_butuh_bl;
	}
	public Integer getMkb_wkt_butuh_th() {
		return mkb_wkt_butuh_th;
	}
	public void setMkb_wkt_butuh_th(Integer mkb_wkt_butuh_th) {
		this.mkb_wkt_butuh_th = mkb_wkt_butuh_th;
	}
	public Integer getMkb_wkt_dpoleh_bl() {
		return mkb_wkt_dpoleh_bl;
	}
	public void setMkb_wkt_dpoleh_bl(Integer mkb_wkt_dpoleh_bl) {
		this.mkb_wkt_dpoleh_bl = mkb_wkt_dpoleh_bl;
	}
	public Integer getMkb_wkt_dpoleh_th() {
		return mkb_wkt_dpoleh_th;
	}
	public void setMkb_wkt_dpoleh_th(Integer mkb_wkt_dpoleh_th) {
		this.mkb_wkt_dpoleh_th = mkb_wkt_dpoleh_th;
	}
	public String getMns_kd_nasabah() {
		return mns_kd_nasabah;
	}
	public void setMns_kd_nasabah(String mns_kd_nasabah) {
		this.mns_kd_nasabah = mns_kd_nasabah;
	}
	public String getLjk_ket() {
		return ljk_ket;
	}
	public void setLjk_ket(String ljk_ket) {
		this.ljk_ket = ljk_ket;
	}
	

}