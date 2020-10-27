/**
 * @author  : Ferry Harlim
 * @created : Sep 24, 2007 3:46:38 PM
 */
package com.ekalife.elions.model.kyc;

import java.io.Serializable;
import java.util.List;

public class CommandKyc implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List lsKycNewBus;
	private List lsHrc;
	private String tglAwal;
	private String tglAkhir;
	private Integer proses;
	private Integer sub;
	private Integer size;
	private List lsKycTopUp;
	private List lsKycPencairan;
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public List getLsHrc() {
		return lsHrc;
	}
	public void setLsHrc(List lsHrc) {
		this.lsHrc = lsHrc;
	}
	public Integer getProses() {
		return proses;
	}
	public void setProses(Integer proses) {
		this.proses = proses;
	}
	public List getLsKycNewBus() {
		return lsKycNewBus;
	}
	public void setLsKycNewBus(List lsKycNewBus) {
		this.lsKycNewBus = lsKycNewBus;
	}
	public String getTglAkhir() {
		return tglAkhir;
	}
	public void setTglAkhir(String tglAkhir) {
		this.tglAkhir = tglAkhir;
	}
	public String getTglAwal() {
		return tglAwal;
	}
	public void setTglAwal(String tglAwal) {
		this.tglAwal = tglAwal;
	}
	public Integer getSub() {
		return sub;
	}
	public void setSub(Integer sub) {
		this.sub = sub;
	}
	public List getLsKycTopUp() {
		return lsKycTopUp;
	}
	public void setLsKycTopUp(List lsKycTopUp) {
		this.lsKycTopUp = lsKycTopUp;
	}
	public List getLsKycPencairan() {
		return lsKycPencairan;
	}
	public void setLsKycPencairan(List lsKycPencairan) {
		this.lsKycPencairan = lsKycPencairan;
	}
}
