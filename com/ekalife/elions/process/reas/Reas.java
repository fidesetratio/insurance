package com.ekalife.elions.process.reas;

import java.io.Serializable;

/**
 * Class penampung data untuk proses reas yang baru
 * 
 * @author Yusuf
 * @since May 15, 2008 (6:58:46 PM)
 */
public class Reas implements Serializable {

	public String mcl_id;
	public String reg_spaj;
	public String id_simultan;
	public Integer mssm_number;
	
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getId_simultan() {
		return id_simultan;
	}
	public void setId_simultan(String id_simultan) {
		this.id_simultan = id_simultan;
	}
	public Integer getMssm_number() {
		return mssm_number;
	}
	public void setMssm_number(Integer mssm_number) {
		this.mssm_number = mssm_number;
	}
	
}
