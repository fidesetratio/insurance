/**
 * @author  : Ferry Harlim
 * @created : Jul 3, 2007 
 */
package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class TabelMedis implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date mstm_tgl_periode;
	private Integer usia_awal;
	private Integer usia_akhir;
	private Integer sar_awal;
	private Integer sar_akhir;
	private String lku_id;
	private String jns_prefix;
	private Integer lsmc_id;
	private String lsmc_name;
	private Integer jns_medis;
	public Integer getJns_medis() {
		return jns_medis;
	}
	public void setJns_medis(Integer jns_medis) {
		this.jns_medis = jns_medis;
	}
	public String getJns_prefix() {
		return jns_prefix;
	}
	public void setJns_prefix(String jns_prefix) {
		this.jns_prefix = jns_prefix;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Integer getLsmc_id() {
		return lsmc_id;
	}
	public void setLsmc_id(Integer lsmc_id) {
		this.lsmc_id = lsmc_id;
	}
	public String getLsmc_name() {
		return lsmc_name;
	}
	public void setLsmc_name(String lsmc_name) {
		this.lsmc_name = lsmc_name;
	}
	public Date getMstm_tgl_periode() {
		return mstm_tgl_periode;
	}
	public void setMstm_tgl_periode(Date mstm_tgl_periode) {
		this.mstm_tgl_periode = mstm_tgl_periode;
	}
	public Integer getSar_akhir() {
		return sar_akhir;
	}
	public void setSar_akhir(Integer sar_akhir) {
		this.sar_akhir = sar_akhir;
	}
	public Integer getSar_awal() {
		return sar_awal;
	}
	public void setSar_awal(Integer sar_awal) {
		this.sar_awal = sar_awal;
	}
	public Integer getUsia_akhir() {
		return usia_akhir;
	}
	public void setUsia_akhir(Integer usia_akhir) {
		this.usia_akhir = usia_akhir;
	}
	public Integer getUsia_awal() {
		return usia_awal;
	}
	public void setUsia_awal(Integer usia_awal) {
		this.usia_awal = usia_awal;
	}
	
}
