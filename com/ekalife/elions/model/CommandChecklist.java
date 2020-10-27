package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

/**
 * Command Class untuk Input Checklist
 * 
 * @author Yusuf
 * @since Sep 25, 2008 (9:00:07 AM)
 */
public class CommandChecklist implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2501132050967059924L;
	public int lspd_id;
	public List<Checklist> listChecklist;
	public String reg_spaj;
	public boolean editable;
	public Integer lssa_id;
	public int centang;
	List lsError;
	public String lca_id;
	public Integer cekflag;
	public Integer flagbancass;

	public Integer getCekflag() {
		return cekflag;
	}
	public void setCekflag(Integer cekflag) {
		this.cekflag = cekflag;
	}
	public List getLsError() {
		return lsError;
	}
	public void setLsError(List lsError) {
		this.lsError = lsError;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public int getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(int lspd_id) {
		this.lspd_id = lspd_id;
	}
	public List<Checklist> getListChecklist() {
		return listChecklist;
	}
	public void setListChecklist(List<Checklist> listChecklist) {
		this.listChecklist = listChecklist;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public int getCentang() {
		return centang;
	}
	public void setCentang(int centang) {
		this.centang = centang;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public Integer getFlagbancass() {
		return flagbancass;
	}
	public void setFlagbancass(Integer flagbancass) {
		this.flagbancass = flagbancass;
	}
}
